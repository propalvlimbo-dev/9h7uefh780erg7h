package settings.render;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat.Encoding;
import sg.handler.VoicechatPushToTalk;

public final class VoicechatAudioPlayback {
   public static final int А1 = 960;
   private static final int АЬ = 48000;
   private static final int А衣 = 4096;
   private static final long ВО = 1500000000L;
   private static final Object Ъ = new Object();
   private static short[] В;
   private static short[] Я_55da0ae9;
   private static int Аш;
   private static boolean Я5;
   private static long ВФ;
   private static Object т;
   private static final AtomicInteger Я_18558580 = new AtomicInteger();
   private static final ExecutorService Я_11dd5828 = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, VoicechatAudioPlayback.ЯЦА);
      var1.setDaemon(true);
      var1.setPriority(5);
      return var1;
   });
   private static final long ЯЦЯ = 1500000000L;
   private static final float ЯЦХ = 48000.0F;
   private static final float ЯЦе = 48000.0F;
   private static final String ЯЦА = "rose-voicechat-local";

   private static void жч() {
      жо();
      Я_55da0ae9 = null;
      т = null;
      Я_18558580.incrementAndGet();
   }

   public static boolean жЭ() {
      synchronized (Ъ) {
         return Я5;
      }
   }

   public static void ь(short[] var0, boolean var1, float var2) {
      ь(var0, var1, var2, null);
   }

   public static void method_1(Object var0) {
      synchronized (Ъ) {
         if (Objects.equals(т, var0)) {
            жч();
         }
      }
   }

   public static short[] ь(int var0) {
      if (var0 <= 0) {
         var0 = 960;
      }

      synchronized (Ъ) {
         if (Я5 && В != null) {
            ВФ = System.nanoTime();
            if (Аш >= В.length) {
               жо();
               return null;
            }

            short[] var2 = new short[var0];
            int var3 = В.length - Аш;
            int var4 = Math.min(var0, var3);
            System.arraycopy(В, Аш, var2, 0, var4);
            Аш += var4;
            Я_55da0ae9 = var2;
            if (Аш >= В.length) {
               жо();
            }

            return var2;
         } else {
            return null;
         }
      }
   }

   public static void жК() {
      synchronized (Ъ) {
         жч();
      }
   }

   private static void жо() {
      Я5 = false;
      В = null;
      Аш = 0;
      ВФ = 0L;
      VoicechatPushToTalk.ре();
   }

   public static short[] ь() {
      synchronized (Ъ) {
         short[] var1 = Я_55da0ae9;
         Я_55da0ae9 = null;
         return var1;
      }
   }

   public static void ь(short[] var0, boolean var1, float var2, Object var3) {
      if (var0 != null && var0.length != 0) {
         synchronized (Ъ) {
            жч();
            В = Arrays.copyOf(var0, var0.length);
            Аш = 0;
            Я5 = true;
            ВФ = System.nanoTime();
            т = var3;
            VoicechatPushToTalk.р弟();
         }

         if (var1 && var2 > 0.0F) {
            int var7 = Я_18558580.incrementAndGet();
            short[] var5 = Arrays.copyOf(var0, var0.length);
            Я_11dd5828.execute(() -> ь(var5, var2, var7));
         }
      }
   }

   private static void ь(short[] var0, float var1, int var2) {
      AudioFormat var3 = new AudioFormat(Encoding.PCM_SIGNED, ЯЦХ, 16, 1, 2, ЯЦе, false);

      try (SourceDataLine var4 = AudioSystem.getSourceDataLine(var3)) {
         var4.open(var3);
         var4.start();
         byte[] var5 = new byte[4096];
         int var6 = 0;

         while (var6 < var0.length && Я_18558580.get() == var2) {
            int var7 = Math.min(var5.length / 2, var0.length - var6);

            for (int var8 = 0; var8 < var7; var8++) {
               int var9 = Math.clamp(Math.round(var0[var6 + var8] * var1), -32768, 32767);
               var5[var8 * 2] = (byte)var9;
               var5[var8 * 2 + 1] = (byte)(var9 >> 8);
            }

            var4.write(var5, 0, var7 * 2);
            var6 += var7;
         }

         if (Я_18558580.get() == var2) {
            var4.drain();
         }

         var4.stop();
      } catch (LineUnavailableException | IllegalArgumentException var12) {
      }
   }

   private VoicechatAudioPlayback() {
   }

   public static boolean жь() {
      synchronized (Ъ) {
         if (!Я5) {
            return false;
         }

         VoicechatPushToTalk.рл();
         if (System.nanoTime() - ВФ <= ЯЦЯ) {
            return false;
         }

         жо();
         return true;
      }
   }
}
