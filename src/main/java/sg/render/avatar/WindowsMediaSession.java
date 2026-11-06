package sg.render.avatar;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.ec.MinecraftAccess;

public final class WindowsMediaSession implements MinecraftAccess {
   private static final Logger Д = LoggerFactory.getLogger(WindowsMediaSession.ХЬ4);
   private static final WindowsMediaSession В_1751811a = new WindowsMediaSession();
   private static final String Яэ = "/assets/rose/media/media_session.ps1";
   private static final long ХO = 400L;
   private static final int Дl = 3;
   @SerializedName("е")
   private final ExecutorService е_11dd5828 = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, ХЬА);
      var1.setDaemon(true);
      return var1;
   });
   private final Path Ъ;
   private final Path т;
   private final Path а;
   @SerializedName("е")
   private final AtomicInteger е_18558580;
   @SerializedName("В")
   private volatile Process В_f3f6c3a0;
   private volatile boolean е7;
   private volatile int Д8;
   private volatile int Д5;
   @SerializedName("В")
   private volatile WindowsMediaSession.MediaSnapshot В_a2e8f1f0;
   private volatile String Яю;
   private volatile String ЯЩ;
   private volatile Identifier Е;
   private volatile String Яу;
   private volatile int Дв;
   private volatile long Х7;
   private volatile long ХР;
   private static final String Х1В = "rose_media_session.ps1";
   private static final String Х1Я = "rose_media_art.png";
   private static final String Х1Х = "rose_media_cmd.txt";
   private static final long Х1е = -1L;
   private static final long Х1А = -1L;
   private static final String Х14 = "java.io.tmpdir";
   private static final String Х1п = ".";
   private static final String Х1Д = "os.name";
   private static final String Х1Ъ = "win";
   private static final String Х1т = "Доступно только на Windows";
   private static final long Х1а = -1L;
   private static final long Х1Ю = -1L;
   private static final String Х1Ч = "powershell.exe";
   private static final String Х1Л = "-NoProfile";
   private static final String Х1诶 = "-NonInteractive";
   private static final String Х11 = "-WindowStyle";
   private static final String Х1Ь = "Hidden";
   private static final String Х1衣 = "-ExecutionPolicy";
   private static final String Х1ш = "Bypass";
   private static final String Х1И = "-File";
   private static final String Х1ь = "-IntervalMs";
   private static final long Х1ж = 400L;
   private static final String Х1弟 = "-ArtPath";
   private static final String Х1б = "-CommandPath";
   private static final String Х1Ы = "Rose-Media";
   private static final String Х1l = "Rose-Media-Errors";
   private static final String Х18 = "Медиаплеер: мост не запустился";
   private static final String Х15 = "/assets/rose/media/media_session.ps1";
   private static final String Х1в = "скрипт моста отсутствует в ресурсах";
   private static final String Х1ъ = "Медиаплеер: чтение состояния прервано: {}";
   private static final String Х1М = "#<";
   private static final String Х1Н = "<Objs";
   private static final String Х1н = "Медиаплеер (мост): {}";
   private static final String Х1Ц = "Мост медиасеанса не отвечает";
   private static final String Х1ч = "Медиаплеер: мост завершился, перезапуск {}/{}";
   private static final long Х1Э = 3000L;
   private static final String Х1必 = "ok";
   private static final String Х1щ = "reason";
   private static final String Х1Г = "reason";
   private static final String Х1л = "volume";
   private static final long Х1э = -1L;
   private static final long Х1ю = -1L;
   private static final String Х1Щ = "no-session";
   private static final String Х1у = "thumbId";
   private static final String Х12 = "thumb";
   private static final String Х1ф = "Playing";
   private static final String Х1и = "status";
   private static final String Х19 = "position";
   private static final String Х1о = "age";
   private static final long Х1O = 2000L;
   private static final String Х17 = "title";
   private static final String Х1Р = "artist";
   private static final String Х13 = "duration";
   private static final String Х1р = "app";
   private static final String Х16 = "canSeek";
   private static final String Х1_ = "canNext";
   private static final String Х1Ш = "canPrev";
   private static final String Х1ы = "volume";
   private static final String Х1й = "Медиаплеер: не разобрал строку состояния: {}";
   private static final long Х1Ж = 80L;
   private static final long Х1м = -1L;
   private static final long Х1К = -1L;
   private static final long Х1I = 400L;
   private static final long Х1Т = -1L;
   private static final long Х1О = 350L;
   private static final long Х1Ф = -1L;
   private static final long Х1Й = 200L;
   private static final long Х1к = 2500L;
   private static final long Х1г = -1L;
   private static final long Х10 = 20000L;
   private static final long Х1х = 20000L;
   private static final long Х1У = 5000L;
   private static final String Х1З = "rose";
   private static final String Х1Е = "pause";
   private static final String Х1П = "play";
   private static final String Х1西 = "next";
   private static final String Х1д = "previous";
   private static final String Х1Б = "seek";
   private static final String Х1С = "volume";
   private static final String Х1з = ".exe";
   private static final long Х1ц = 1000000L;
   private static final long Х1я = 60000L;
   private static final long ХЬс = -1L;
   private static final long ХЬВ = -1L;
   private static final String ХЬЯ = "Медиаплеер: команда {} не отправлена: {}";
   private static final String ХЬХ = "rose-artwork";
   private static final String ХЬе = "Медиаплеер: обложка не прочитана: {}";
   private static final String ХЬА = "Rose-Media-Command";
   private static final String ХЬ4 = "Rose/Music";

   private static String У(String var0, String var1) {
      return (var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT)) + (var1 == null ? "" : var1.trim().toLowerCase(Locale.ROOT));
   }

   public boolean Цэ() {
      return this.В_a2e8f1f0.present() && this.В_a2e8f1f0.seekable() && this.В_a2e8f1f0.durationMs() > 0L;
   }

   public boolean Цв() {
      return this.В_a2e8f1f0.present() && !this.В_a2e8f1f0.playing();
   }

   private void Фы(String var1) {
      try {
         JsonObject var2 = JsonParser.parseString(var1).getAsJsonObject();
         if (!var2.get(Х1必).getAsBoolean()) {
            String var23 = var2.has(Х1щ) ? var2.get(Х1Г).getAsString() : "";
            this.В_a2e8f1f0 = new WindowsMediaSession.MediaSnapshot(false, "", "", "", false, 0L, 0L, false, false, false, П(var2, Х1л, -1), System.nanoTime());
            this.Яу = "";
            this.Дв = 0;
            this.Х7 = Х1э;
            this.ХР = Х1ю;
            this.Яю = Х1Щ.equals(var23) ? "" : var23;
            this.ЯЩ = "";
            this.Уд();
            return;
         }

         this.Яю = "";
         String var3 = С(var2, Х1у);
         String var4 = С(var2, Х12);
         boolean var5 = Х1ф.equalsIgnoreCase(С(var2, Х1и));
         long var6 = Math.max(0L, var2.get(Х19).getAsLong());
         long var8 = var5 ? Math.clamp(П(var2, Х1о, 0L), 0L, Х1O) : 0L;
         long var10 = var6 + var8;
         String var12 = С(var2, Х17);
         String var13 = С(var2, Х1Р);
         String var14 = У(var12, var13);
         long var15 = this.Е();
         boolean var17 = this.В_a2e8f1f0.present() && (var14.equals(this.Яу) || ъ(this.Яу, var14));
         long var18 = this.П(Math.max(0L, var2.get(Х13).getAsLong()), var15);
         long var20 = this.П(var5, var17, var10, var15);
         this.Яу = var14;
         this.ХР = var10;
         this.В_a2e8f1f0 = new WindowsMediaSession.MediaSnapshot(
            true,
            var12,
            var13,
            С(var2, Х1р),
            var5,
            var20,
            var18,
            var2.get(Х16).getAsBoolean(),
            var2.get(Х1_).getAsBoolean(),
            var2.get(Х1Ш).getAsBoolean(),
            П(var2, Х1ы, -1),
            System.nanoTime()
         );
         if (!var3.equals(this.ЯЩ)) {
            this.ЯЩ = var3;
            if (var4.isBlank()) {
               this.Уд();
            } else {
               this.Х(var4, var3);
            }
         }
      } catch (Exception var22) {
         Д.warn(Х1й, var22.getMessage());
      }
   }

   private long П(long var1, long var3) {
      if (var1 >= Х10) {
         return var1;
      }

      long var5 = this.В_a2e8f1f0.durationMs();
      return var5 >= Х1х && var3 < var5 + Х1У ? var5 : 0L;
   }

   private static String С(JsonObject var0, String var1) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsString() : "";
   }

   private static Path С(String var0) {
      return Path.of(System.getProperty(Х14, Х1п), var0);
   }

   public String Сю() {
      return this.Яю;
   }

   public synchronized void Уц() {
      if (this.Д8 > 0) {
         this.Д8--;
      }

      if (this.Д8 <= 0) {
         this.е7 = false;
         Process var1 = this.В_f3f6c3a0;
         this.В_f3f6c3a0 = null;
         if (var1 != null) {
            var1.destroy();
         }

         this.В_a2e8f1f0 = WindowsMediaSession.MediaSnapshot.EMPTY;
         this.Яу = "";
         this.Дв = 0;
         this.Х7 = Х1а;
         this.ХР = Х1Ю;
         this.Яю = "";
         this.Уд();
      }
   }

   private static boolean Ц西() {
      return System.getProperty(Х1Д, "").toLowerCase().contains(Х1Ъ);
   }

   public void Уг() {
      this.Ч(Х1д, 0L);
   }

   public boolean Цl() {
      return this.В_a2e8f1f0.present() && !this.В_a2e8f1f0.title().isBlank();
   }

   private void Уд() {
      Identifier var1 = this.Е;
      this.Е = null;
      if (var1 != null) {
         MINECRAFT_CLIENT.execute(() -> MINECRAFT_CLIENT.getTextureManager().release(var1));
      }
   }

   private void УЙ() {
      try {
         this.УТ();
         ProcessBuilder var1 = new ProcessBuilder(
            Х1Ч, Х1Л, Х1诶, Х11, Х1Ь, Х1衣, Х1ш, Х1И, this.Ъ.toString(), Х1ь, String.valueOf(Х1ж), Х1弟, this.т.toString(), Х1б, this.а.toString()
         );
         Files.deleteIfExists(this.а);
         Process var2 = var1.start();
         this.В_f3f6c3a0 = var2;
         this.ь(Х1Ы, () -> this.П(var2));
         this.ь(Х1l, () -> this.С(var2));
         this.Яю = "";
      } catch (Exception var3) {
         this.Яю = "Не удалось запустить мост: " + var3.getMessage();
         Д.error(Х18, var3);
      }
   }

   public synchronized void Ул() {
      if (!Ц西()) {
         this.Яю = Х1т;
      } else {
         this.Д8++;
         if (!this.е7) {
            this.е7 = true;
            this.Д5 = 0;
            this.УЙ();
         }
      }
   }

   public long Е() {
      WindowsMediaSession.MediaSnapshot var1 = this.В_a2e8f1f0;
      if (!var1.present()) {
         return 0L;
      }

      long var2 = var1.basePositionMs();
      if (var1.playing()) {
         var2 += Math.max(0L, (System.nanoTime() - var1.receivedAt()) / Х1ц);
      }

      long var4 = var1.durationMs();
      return var4 > 0L && var2 > var4 && var4 >= Х1я ? var4 : Math.max(0L, var2);
   }

   public void УЮ() {
      this.Ч(Х1西, 0L);
   }

   private void Х(String var1, String var2) {
      this.е_11dd5828.execute(() -> {
         try {
            byte[] var3 = Files.readAllBytes(Path.of(var1));
            NativeImage var4 = NativeImage.read(var3);
            MINECRAFT_CLIENT.execute(() -> this.П(var2, var4));
         } catch (Exception var5) {
            Д.warn(ХЬе, var5.getMessage());
         }
      });
   }

   private void УТ() throws IOException {
      try (InputStream var1 = WindowsMediaSession.class.getResourceAsStream(Х15)) {
         if (var1 == null) {
            throw new IOException(Х1в);
         }

         byte[] var2 = var1.readAllBytes();
         boolean var3 = var2.length >= 3 && (var2[0] & 255) == 239 && (var2[1] & 255) == 187 && (var2[2] & 255) == 191;

         try (OutputStream var4 = Files.newOutputStream(this.Ъ)) {
            if (!var3) {
               var4.write(new byte[]{-17, -69, -65});
            }

            var4.write(var2);
         }
      }
   }

   public String СЗ() {
      String var1 = this.В_a2e8f1f0.app();
      if (var1.isBlank()) {
         return "";
      }

      String var2 = var1.endsWith(Х1з) ? var1.substring(0, var1.length() - 4) : var1;
      int var3 = Math.max(var2.lastIndexOf(92), var2.lastIndexOf(33));
      if (var3 >= 0 && var3 + 1 < var2.length()) {
         var2 = var2.substring(var3 + 1);
      }

      return var2;
   }

   public Identifier Ь() {
      return this.Е;
   }

   public boolean ЦЩ() {
      return this.В_a2e8f1f0.volume() >= 0;
   }

   public static WindowsMediaSession П_1751811a() {
      return В_1751811a;
   }

   private void Ч(String var1, long var2) {
      if (this.В_f3f6c3a0 != null) {
         this.е_11dd5828.execute(() -> {
            try {
               Path var4 = Path.of(this.а + ".tmp");
               Files.writeString(var4, var1 + " " + var2 + System.lineSeparator(), StandardCharsets.UTF_8);
               Files.move(var4, this.а, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception var5) {
               Д.warn(ХЬЯ, var1, var5.getMessage());
            }
         });
      }
   }

   private static long П(JsonObject var0, String var1, long var2) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsLong() : var2;
   }

   public void УO() {
      synchronized (this) {
         this.Д8 = 0;
         this.е7 = false;
         Process var2 = this.В_f3f6c3a0;
         this.В_f3f6c3a0 = null;
         if (var2 != null) {
            var2.destroy();
         }

         this.В_a2e8f1f0 = WindowsMediaSession.MediaSnapshot.EMPTY;
         this.Яу = "";
         this.Дв = 0;
         this.Х7 = ХЬс;
         this.ХР = ХЬВ;
         this.Яю = "";
         this.Уд();
      }

      this.е_11dd5828.shutdownNow();
   }

   public void УЛ() {
      if (!this.Ц7()) {
         this.УН();
      }
   }

   private WindowsMediaSession() {
      this.Ъ = С(Х1В);
      this.т = С(Х1Я);
      this.а = С(Х1Х);
      this.е_18558580 = new AtomicInteger();
      this.В_a2e8f1f0 = WindowsMediaSession.MediaSnapshot.EMPTY;
      this.Яю = "";
      this.ЯЩ = "";
      this.Яу = "";
      this.Х7 = Х1е;
      this.ХР = Х1А;
   }

   public int ПТ() {
      return Math.max(0, this.В_a2e8f1f0.volume());
   }

   private void П(Process var1) {
      String var3;
      try (BufferedReader var2 = new BufferedReader(new InputStreamReader(var1.getInputStream(), StandardCharsets.UTF_8))) {
         while ((var3 = var2.readLine()) != null) {
            if (!this.е7) {
               return;
            }

            this.Фы(var3);
         }
      } catch (IOException var7) {
         if (this.е7) {
            Д.warn(Х1ъ, var7.getMessage());
         }
      }

      if (this.е7) {
         this.У诶();
      }
   }

   public void У0() {
      if (this.Ц7()) {
         this.УН();
      }
   }

   private long П(boolean var1, boolean var2, long var3, long var5) {
      boolean var7 = this.ХР < 0L || var3 > this.ХР + Х1Ж;
      if (!var2) {
         this.Дв = 0;
         this.Х7 = Х1м;
         return var3;
      }

      if (!var1) {
         this.Дв = 0;
         this.Х7 = Х1К;
         return var5;
      }

      if (var7 && var5 > var3 + Х1I) {
         this.Дв = 0;
         this.Х7 = Х1Т;
         return var3;
      }

      if (var3 >= var5 - Х1О) {
         this.Дв = 0;
         this.Х7 = Х1Ф;
         return Math.max(var5, var3);
      }

      boolean var8 = this.Х7 >= 0L && var3 > this.Х7 + Х1Й;
      int var9 = var3 < Х1к ? 3 : 2;
      if (var8) {
         this.Дв++;
         this.Х7 = var3;
         if (this.Дв >= var9) {
            this.Дв = 0;
            this.Х7 = Х1г;
            return var3;
         } else {
            return var5;
         }
      } else {
         this.Дв = 0;
         this.Х7 = var3;
         return var5;
      }
   }

   public String Сй() {
      return this.В_a2e8f1f0.title();
   }

   public String Сф() {
      return this.В_a2e8f1f0.artist();
   }

   private void ь(String var1, Runnable var2) {
      Thread var3 = new Thread(var2, var1);
      var3.setDaemon(true);
      var3.start();
   }

   private static boolean ъ(String var0, String var1) {
      if (var0 == null || var0.isEmpty() || var1 == null) {
         return false;
      }

      if (var0.equals(var1)) {
         return true;
      }

      int var2 = var0.indexOf(1);
      int var3 = var1.indexOf(1);
      String var4 = var2 < 0 ? var0 : var0.substring(0, var2);
      String var5 = var3 < 0 ? var1 : var1.substring(0, var3);
      return var4.length() >= 3 && var5.length() >= 3 ? var4.equals(var5) || var4.contains(var5) || var5.contains(var4) : false;
   }

   private void П(String var1, NativeImage var2) {
      if (!var1.equals(this.ЯЩ)) {
         var2.close();
      } else {
         Identifier var3 = this.Е;
         Identifier var4 = Identifier.fromNamespaceAndPath(Х1З, "music/artwork_" + this.е_18558580.incrementAndGet());
         MINECRAFT_CLIENT.getTextureManager().register(var4, new DynamicTexture(() -> ХЬХ, var2));
         this.Е = var4;
         if (var3 != null) {
            MINECRAFT_CLIENT.getTextureManager().release(var3);
         }
      }
   }

   public void O(long var1) {
      long var3 = this.о_6da43b94();
      long var5 = Math.max(0L, var3 > 0L ? Math.min(var1, var3) : var1);
      WindowsMediaSession.MediaSnapshot var7 = this.В_a2e8f1f0;
      this.В_a2e8f1f0 = new WindowsMediaSession.MediaSnapshot(
         var7.present(),
         var7.title(),
         var7.artist(),
         var7.app(),
         var7.playing(),
         var5,
         var7.durationMs(),
         var7.seekable(),
         var7.canNext(),
         var7.canPrevious(),
         var7.volume(),
         System.nanoTime()
      );
      this.Ч(Х1Б, var5);
   }

   public boolean Ц7() {
      return this.В_a2e8f1f0.playing();
   }

   private void С(Process var1) {
      String var3;
      try (BufferedReader var2 = new BufferedReader(new InputStreamReader(var1.getErrorStream(), StandardCharsets.UTF_8))) {
         while ((var3 = var2.readLine()) != null) {
            if (!var3.isBlank() && !var3.startsWith(Х1М) && !var3.startsWith(Х1Н)) {
               Д.warn(Х1н, var3);
            }
         }
      } catch (IOException var7) {
      }
   }

   public boolean Ца() {
      return this.В_a2e8f1f0.canNext() || this.В_a2e8f1f0.canPrevious();
   }

   private synchronized void У诶() {
      if (this.е7) {
         if (++this.Д5 > 3) {
            this.Яю = Х1Ц;
            this.е7 = false;
         } else {
            Д.warn(Х1ч, this.Д5, 3);

            try {
               Thread.sleep(Х1Э);
            } catch (InterruptedException var2) {
               Thread.currentThread().interrupt();
               return;
            }

            if (this.е7) {
               this.УЙ();
            }
         }
      }
   }

   public void Фи(int var1) {
      int var2 = Math.max(0, Math.min(100, var1));
      WindowsMediaSession.MediaSnapshot var3 = this.В_a2e8f1f0;
      this.В_a2e8f1f0 = new WindowsMediaSession.MediaSnapshot(
         var3.present(),
         var3.title(),
         var3.artist(),
         var3.app(),
         var3.playing(),
         var3.basePositionMs(),
         var3.durationMs(),
         var3.seekable(),
         var3.canNext(),
         var3.canPrevious(),
         var2,
         var3.receivedAt()
      );
      this.Ч(Х1С, var2);
   }

   private static int П(JsonObject var0, String var1, int var2) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsInt() : var2;
   }

   public void УН() {
      boolean var1 = this.Ц7();
      WindowsMediaSession.MediaSnapshot var2 = this.В_a2e8f1f0;
      this.В_a2e8f1f0 = new WindowsMediaSession.MediaSnapshot(
         var2.present(),
         var2.title(),
         var2.artist(),
         var2.app(),
         !var1,
         this.Е(),
         var2.durationMs(),
         var2.seekable(),
         var2.canNext(),
         var2.canPrevious(),
         var2.volume(),
         System.nanoTime()
      );
      this.Ч(var1 ? Х1Е : Х1П, 0L);
   }

   public long о_6da43b94() {
      return this.В_a2e8f1f0.durationMs();
   }

   private record MediaSnapshot(
      boolean present,
      String title,
      String artist,
      String app,
      boolean playing,
      long basePositionMs,
      long durationMs,
      boolean seekable,
      boolean canNext,
      boolean canPrevious,
      int volume,
      long receivedAt
   ) {
      private static final WindowsMediaSession.MediaSnapshot EMPTY = new WindowsMediaSession.MediaSnapshot(
         false, "", "", "", false, 0L, 0L, false, false, false, -1, 0L
      );
   }
}
