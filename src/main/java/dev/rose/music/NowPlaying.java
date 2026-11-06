package dev.rose.music;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinUser.INPUT;
import com.sun.jna.ptr.IntByReference;
import dev.rose.RoseClient;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;


public final class NowPlaying {
   private static final int VK_MEDIA_NEXT_TRACK = 176;
   private static final int VK_MEDIA_PREV_TRACK = 177;
   private static final int VK_MEDIA_PLAY_PAUSE = 179;
   private static final int KEYEVENTF_KEYUP = 2;
   private static volatile NowPlaying.Track current = NowPlaying.Track.NONE;
   private static volatile NowPlaying.Source source = NowPlaying.Source.ANY;
   private static volatile long lastAsked;
   private static ScheduledExecutorService executor;
   private static volatile boolean failed;
   private static volatile NowPlaying.Track override;

   private NowPlaying() {
   }

   public static boolean supported() {
      return Platform.isWindows() && !failed;
   }

   public static void override(NowPlaying.Track track) {
      override = track;
   }

   public static NowPlaying.Track current(NowPlaying.Source wanted) {
      if (override != null) {
         return override;
      }

      source = wanted;
      lastAsked = System.currentTimeMillis();
      if (supported() && executor == null) {
         start();
      }

      return current;
   }

   private static synchronized void start() {
      if (executor == null) {
         executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "Rose Now Playing");
            thread.setDaemon(true);
            return thread;
         });
         executor.scheduleWithFixedDelay(NowPlaying::poll, 0L, 1L, TimeUnit.SECONDS);
      }
   }

   private static void poll() {
      if (System.currentTimeMillis() - lastAsked <= 5000L) {
         try {
            current = find(source);
         } catch (Throwable e) {
            failed = true;
            current = NowPlaying.Track.NONE;
            RoseClient.LOGGER.warn("[Rose] now playing is unavailable: {}", e.toString());
            executor.shutdown();
         }
      }
   }

   private static NowPlaying.Track find(NowPlaying.Source wanted) {
      NowPlaying.Track[] found = new NowPlaying.Track[]{NowPlaying.Track.NONE};
      User32.INSTANCE.EnumWindows((window, data) -> {
         if (!User32.INSTANCE.IsWindowVisible(window)) {
            return true;
         }

         int length = User32.INSTANCE.GetWindowTextLength(window);
         if (length > 0 && length <= 512) {
            char[] buffer = new char[length + 1];
            User32.INSTANCE.GetWindowText(window, buffer, buffer.length);
            String title = new String(buffer).replace(String.valueOf('\u0000'), "").trim();
            if (title.isEmpty()) {
               return true;
            } else {
               String app = processName(window);
               NowPlaying.Track track = parse(app, title, wanted);
               if (track != null) {
                  found[0] = track;
                  return !track.playing();
               } else {
                  return true;
               }
            }
         } else {
            return true;
         }
      }, null);
      return found[0];
   }

   private static String processName(HWND window) {
      IntByReference pid = new IntByReference();
      User32.INSTANCE.GetWindowThreadProcessId(window, pid);
      Optional<String> command = ProcessHandle.of(pid.getValue()).flatMap(handle -> handle.info().command());
      if (command.isEmpty()) {
         return "";
      }

      String path = command.get();
      int slash = Math.max(path.lastIndexOf(92), path.lastIndexOf(47));
      return path.substring(slash + 1).toLowerCase(Locale.ROOT);
   }

   static NowPlaying.Track parse(String app, String title, NowPlaying.Source wanted) {
      boolean spotify = app.equals("spotify.exe");
      if (spotify && wanted != NowPlaying.Source.YOUTUBE) {
         String lower = title.toLowerCase(Locale.ROOT);
         if (lower.startsWith("spotify")) {
            return new NowPlaying.Track("", "Spotify", false, "Spotify");
         }

         int dash = title.indexOf(" - ");
         return dash > 0
            ? new NowPlaying.Track(title.substring(0, dash).trim(), title.substring(dash + 3).trim(), true, "Spotify")
            : new NowPlaying.Track("", title, true, "Spotify");
      } else {
         Predicate<String> browser = name -> name.equals("chrome.exe")
            || name.equals("msedge.exe")
            || name.equals("firefox.exe")
            || name.equals("opera.exe")
            || name.equals("browser.exe")
            || name.equals("yandex.exe")
            || name.equals("brave.exe");
         if (browser.test(app) && wanted != NowPlaying.Source.SPOTIFY) {
            String clean = title.replaceAll(" [—-] (Google Chrome|Microsoft Edge|Mozilla Firefox|Opera|Яндекс Браузер|Brave)$", "");
            if (clean.endsWith(" - YouTube Music")) {
               String body = clean.substring(0, clean.length() - " - YouTube Music".length());
               int dot = body.lastIndexOf(" • ");
               return dot > 0
                  ? new NowPlaying.Track(body.substring(dot + 3), body.substring(0, dot), true, "YouTube Music")
                  : new NowPlaying.Track("", body, true, "YouTube Music");
            }

            if (clean.endsWith(" - YouTube")) {
               String body = clean.substring(0, clean.length() - " - YouTube".length()).replaceFirst("^\\(\\d+\\) ", "");
               int dash = body.indexOf(" - ");
               return dash > 0
                  ? new NowPlaying.Track(body.substring(0, dash), body.substring(dash + 3), true, "YouTube")
                  : new NowPlaying.Track("", body, true, "YouTube");
            }
         }

         return null;
      }
   }

   public static void playPause() {
      press(179);
   }

   public static void next() {
      press(176);
   }

   public static void previous() {
      press(177);
   }

   private static void send(int key, int flags) {
      INPUT input = new INPUT();
      input.type = new DWORD(1L);
      input.input.setType("ki");
      input.input.ki.wVk = new WORD(key);
      input.input.ki.wScan = new WORD(0L);
      input.input.ki.dwFlags = new DWORD(flags);
      input.input.ki.time = new DWORD(0L);
      input.input.ki.dwExtraInfo = new ULONG_PTR(0L);
      User32.INSTANCE.SendInput(new DWORD(1L), (INPUT[])input.toArray(1), input.size());
   }

   private static void press(int key) {
      if (supported()) {
         try {
            send(key, 0);
            send(key, 2);
            lastAsked = System.currentTimeMillis();
            if (executor != null) {
               executor.schedule(NowPlaying::poll, 350L, TimeUnit.MILLISECONDS);
            }
         } catch (Throwable e) {
            failed = true;
            RoseClient.LOGGER.warn("[Rose] media keys are unavailable: {}", e.toString());
         }
      }
   }

   public enum Source {
      SPOTIFY,
      YOUTUBE,
      ANY;
   }

   public record Track(String artist, String title, boolean playing, String app) {
      public static final NowPlaying.Track NONE = new NowPlaying.Track("", "", false, "");

      public boolean present() {
         return !this.title.isEmpty();
      }
   }
}
