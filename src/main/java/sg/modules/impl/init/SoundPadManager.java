package sg.modules.impl.init;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import settings.render.VoicechatAudioPlayback;
import sg.handler.VoicechatPushToTalk;
import sg.network.ClientChat;

public final class SoundPadManager {
   private static final Object Ю = SoundPadManager.class;
   private static final long ХТ = 250L;
   private final Map<String, Integer> сд = new LinkedHashMap<>();
   private Runnable т = () -> {};
   private boolean еП = true;
   private float т必;
   private long ХО;
   private long ХФ;
   private static final float ЯМф = 0.8F;
   private static final float ЯМи = 0.1F;
   private static final float ЯМ9 = 1.5F;
   private static final float ЯМо = 0.1F;
   private static final float ЯМO = 1.5F;
   private static final String ЯМ7 = "SoundPad: пользовательских .wav файлов нет";
   private static final String ЯМР = "explorer.exe";
   private static final long ЯМ3 = 250L;
   private static final String ЯМр = "Simple Voice Chat не найден";
   private static final long ЯМ6 = 1125899906842597L;
   private static final long ЯМ_ = 31L;
   private static final long ЯМШ = 31L;
   private static final long ЯМы = 31L;
   private static final String ЯМй = ".wav";

   private long method_7() {
      File[] var1 = SoundPadLibrary.А.listFiles((var0, var1x) -> var1x.toLowerCase().endsWith(ЯМй));
      if (var1 == null) {
         return 0L;
      }

      Arrays.sort(var1, (var0, var1x) -> var0.getName().compareToIgnoreCase(var1x.getName()));
      long var2 = ЯМ6;

      for (File var7 : var1) {
         var2 = ЯМ_ * var2 + var7.getName().toLowerCase().hashCode();
         var2 = ЯМШ * var2 + var7.length();
         var2 = ЯМы * var2 + var7.lastModified();
      }

      return var2;
   }

   public synchronized void б(Runnable var1) {
      this.т = var1 != null ? var1 : () -> {};
   }

   public synchronized void н(Map<String, Integer> var1, boolean var2, float var3) {
      this.сд.clear();
      if (var1 != null) {
         this.сд.putAll(var1);
      }

      this.еП = var2;
      this.т必 = Math.clamp(var3, ЯМи, ЯМ9);
   }

   public synchronized boolean Йы() {
      long var1 = this.method_7();
      if (var1 == this.ХФ) {
         return false;
      }

      SoundPadLibrary.л(false);
      this.ХФ = var1;
      return true;
   }

   public synchronized void б(String var1, int var2) {
      if (var1 != null && !var1.isBlank()) {
         if (var2 >= 0) {
            this.сд.replaceAll((var2x, var3) -> !var2x.equals(var1) && var3 == var2 ? -1 : var3);
         }

         this.сд.put(var1, var2);
         this.т.run();
      }
   }

   public synchronized List<SoundPadLibrary.Clip> method_1Ф() {
      return SoundPadLibrary.Л西();
   }

   public synchronized void method_1ю(boolean var1) {
      this.еП = var1;
      this.т.run();
   }

   public synchronized void method_12(float var1) {
      this.т必 = Math.clamp(var1, ЯМо, ЯМO);
      this.т.run();
   }

   public boolean м(int var1) {
      String var2 = null;
      synchronized (this) {
         for (SoundPadLibrary.Clip var5 : SoundPadLibrary.Л西()) {
            if (this.сд.getOrDefault(var5.slot(), -1) == var1) {
               var2 = var5.slot();
               break;
            }
         }
      }

      if (var2 == null) {
         return false;
      }

      this.method_1у(var2);
      return true;
   }

   public SoundPadManager() {
      this.т必 = ЯМф;
      SoundPadLibrary.нл();
      SoundPadLibrary.л(false);
      this.ХФ = this.method_7();
   }

   public synchronized void method_12(boolean var1) {
      SoundPadLibrary.LoadReport var2 = SoundPadLibrary.л(false);
      this.ХФ = this.method_7();
      if (var1) {
         int var3 = SoundPadLibrary.Л西().size();
         if (var3 == 0) {
            ClientChat.З(ЯМ7);
            ClientChat.З(SoundPadLibrary.А.getAbsolutePath());
         } else {
            ClientChat.З("SoundPad: загружено звуков — " + var3);
         }

         for (String var5 : var2.з) {
            ClientChat.З("SoundPad ошибка: " + var5);
         }
      }
   }

   public void уП() {
      VoicechatAudioPlayback.method_1(Ю);
   }

   public void method_1у(String var1) {
      long var2 = System.currentTimeMillis();
      synchronized (this) {
         if (var2 - this.ХО < ЯМ3) {
            return;
         }

         this.ХО = var2;
      }

      if (!VoicechatPushToTalk.Р7()) {
         ClientChat.З(ЯМр);
      } else {
         short[] var4 = SoundPadLibrary.л(var1);
         if (var4 == null) {
            this.method_12(false);
            var4 = SoundPadLibrary.л(var1);
         }

         if (var4 == null) {
            ClientChat.З("SoundPad: файл не найден — " + var1 + ".wav");
         } else {
            boolean var5;
            float var6;
            synchronized (this) {
               var5 = this.еП;
               var6 = this.т必;
            }

            VoicechatAudioPlayback.ь(var4, var5, var6, Ю);
         }
      }
   }

   public void уР() {
      try {
         SoundPadLibrary.нл();
         new ProcessBuilder(ЯМР, SoundPadLibrary.А.getAbsolutePath()).start();
      } catch (Exception var3) {
         String var2 = var3.getMessage() != null ? var3.getMessage() : var3.getClass().getSimpleName();
         ClientChat.З("Не удалось открыть папку SoundPad: " + var2);
         ClientChat.З(SoundPadLibrary.А.getAbsolutePath());
      }
   }

   public synchronized Map<String, Integer> Х() {
      return new LinkedHashMap<>(this.сд);
   }

   public synchronized int method_6(String var1) {
      return this.сд.getOrDefault(var1, -1);
   }

   public synchronized float б4() {
      return this.т必;
   }

   public synchronized boolean ЙЫ() {
      return this.еП;
   }
}
