package sg.modules.impl.init;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;
import sg.network.ClientChat;

public final class SoundPadLibrary {
   public static final File А = new File(sg.config.ClientDirectories.forDevelopment(System.getenv(SoundPadLibrary.ЯЫ3)) + "\\Dimas\\", SoundPadLibrary.ЯЫр);
   public static final String Яв = "kidai_shar";
   public static final String Яъ = "totem";
   public static final String ЯМ = "slabost";
   public static final String ЯН = "pokupai";
   private static final int Да = 48000;
   private static final int ДЮ = 16;
   private static final Map<String, String> с0 = Map.of(
      SoundPadLibrary.ЯЫ6, SoundPadLibrary.ЯЫ_, SoundPadLibrary.ЯЫШ, SoundPadLibrary.ЯЫы, SoundPadLibrary.ЯЫй, SoundPadLibrary.ЯЫЖ, SoundPadLibrary.ЯЫм, SoundPadLibrary.ЯЫК
   );
   private static final String Ян = "Кастомные звуки SoundPad\n===============================\n\nФормат: WAV (PCM), лучше моно 48kHz 16-bit.\n\nЛюбой .wav в этой папке = новый звук в SoundPad.\nНазвание звука = имя файла без расширения.\n  hello.wav  ->  звук «hello»\n\nВстроенные (если файла нет — берутся из клиента):\n  kidai_shar.wav  ->  «кидай шар отпущу, чмошник ебанный»\n  totem.wav       ->  «Тотем ебливому пидорасу»\n  slabost.wav     ->  «признай свою слабость пидорас»\n  pokupai.wav     ->  «Покупай Димасик клиент сын шлюхи»\n\nПосле добавления файлов вернись во вкладку SoundPad.\nСписок обновится автоматически; также можно нажать «Обновить».\n";
   private static final Map<String, SoundPadLibrary.Clip> сх = new LinkedHashMap<>();
   private static final String ЯЫГ = "README.txt";
   private static final String ЯЫл = "Кастомные звуки SoundPad\n===============================\n\nФормат: WAV (PCM), лучше моно 48kHz 16-bit.\n\nЛюбой .wav в этой папке = новый звук в SoundPad.\nНазвание звука = имя файла без расширения.\n  hello.wav  ->  звук «hello»\n\nВстроенные (если файла нет — берутся из клиента):\n  kidai_shar.wav  ->  «кидай шар отпущу, чмошник ебанный»\n  totem.wav       ->  «Тотем ебливому пидорасу»\n  slabost.wav     ->  «признай свою слабость пидорас»\n  pokupai.wav     ->  «Покупай Димасик клиент сын шлюхи»\n\nПосле добавления файлов вернись во вкладку SoundPad.\nСписок обновится автоматически; также можно нажать «Обновить».\n";
   private static final String ЯЫэ = "README.txt";
   private static final String ЯЫю = "wav";
   private static final int ЯЫЩ = 544501094;
   private static final int ЯЫу = 65535;
   private static final int ЯЫ2 = 65535;
   private static final int ЯЫф = 65535;
   private static final int ЯЫи = 1635017060;
   private static final float ЯЫ9 = 48000.0F;
   private static final float ЯЫо = 48000.0F;
   private static final float ЯЫO = 0.5F;
   private static final String ЯЫ7 = "Звуки VoiceChat: файлов нет. Кинь .wav в папку и нажми ещё раз.";
   private static final String ЯЫР = ", ";
   private static final String ЯЫ3 = "SystemDrive";
   private static final String ЯЫр = "rose/voicechat";
   private static final String ЯЫ6 = "kidai_shar";
   private static final String ЯЫ_ = "кидай шар отпущу, чмошник ебанный";
   private static final String ЯЫШ = "totem";
   private static final String ЯЫы = "Тотем ебливому пидорасу";
   private static final String ЯЫй = "slabost";
   private static final String ЯЫЖ = "признай свою слабость пидорас";
   private static final String ЯЫм = "pokupai";
   private static final String ЯЫК = "Покупай Димасик клиент сын шлюхи";

   private SoundPadLibrary() {
   }

   public static synchronized List<SoundPadLibrary.Clip> Лл() {
      return Collections.unmodifiableList(new ArrayList<>(сх.values()));
   }

   public static synchronized List<SoundPadLibrary.Clip> Лш() {
      return сх.values().stream().filter(var0 -> с0.containsKey(var0.slot())).toList();
   }

   public static synchronized short[] л(String var0) {
      SoundPadLibrary.Clip var1 = сх.get(var0.toLowerCase(Locale.ROOT));
      return var1 != null ? var1.pcm() : null;
   }

   public static synchronized SoundPadLibrary.LoadReport л(boolean var0) {
      нл();
      сх.clear();
      SoundPadLibrary.LoadReport var1 = new SoundPadLibrary.LoadReport();
      File[] var2 = А.listFiles();
      if (var2 == null) {
         var1.з.add("Папка недоступна: " + А.getAbsolutePath());
      } else {
         Arrays.sort(var2, (var0x, var1x) -> var0x.getName().compareToIgnoreCase(var1x.getName()));

         for (File var6 : var2) {
            if (var6.isFile()) {
               String var7 = var6.getName();
               if (!var7.equalsIgnoreCase(ЯЫэ)) {
                  int var8 = var7.lastIndexOf(46);
                  if (var8 > 0) {
                     String var9 = var7.substring(var8 + 1).toLowerCase(Locale.ROOT);
                     if (var9.equals(ЯЫю)) {
                        String var10 = var7.substring(0, var8).trim();
                        if (!var10.isEmpty()) {
                           String var11 = var10.toLowerCase(Locale.ROOT);
                           if (!сх.containsKey(var11)) {
                              try {
                                 short[] var12 = л(var6);
                                 if (var12 != null && var12.length != 0) {
                                    String var21 = с0.getOrDefault(var11, var10);
                                    сх.put(var11, new SoundPadLibrary.Clip(var11, var21, var12));
                                    var1.Б.add(var21);
                                 } else {
                                    var1.з.add("Пустой файл: " + var7);
                                 }
                              } catch (Exception var15) {
                                 String var13 = var15.getMessage() != null ? var15.getMessage() : var15.getClass().getSimpleName();
                                 var1.з.add(var7 + ": " + var13);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      for (Entry var17 : с0.entrySet()) {
         String var18 = (String)var17.getKey();
         if (!сх.containsKey(var18)) {
            try {
               short[] var19 = ш(var18);
               if (var19 != null && var19.length > 0) {
                  сх.put(var18, new SoundPadLibrary.Clip(var18, (String)var17.getValue(), var19));
                  var1.Б.add((String)var17.getValue() + " (builtin)");
               }
            } catch (Exception var14) {
               String var20 = var14.getMessage() != null ? var14.getMessage() : var14.getClass().getSimpleName();
               var1.з.add(var18 + " builtin: " + var20);
            }
         }
      }

      if (var0) {
         л(var1);
      }

      return var1;
   }

   private static short[] л(AudioInputStream var0) throws IOException {
      AudioFormat var1 = var0.getFormat();
      if (var1.getEncoding() != Encoding.PCM_SIGNED || var1.getSampleSizeInBits() != 16 || var1.isBigEndian()) {
         AudioFormat var2 = new AudioFormat(
            Encoding.PCM_SIGNED, var1.getSampleRate(), 16, var1.getChannels(), var1.getChannels() * 16 / 8, var1.getSampleRate(), false
         );
         var0 = AudioSystem.getAudioInputStream(var2, var0);
         var1 = var0.getFormat();
      }

      short[] var4 = ш(var0);
      short[] var3 = л(var4, Math.max(1, var1.getChannels()));
      return л(var3, var1.getSampleRate(), ЯЫо);
   }

   private static short[] л(short[] var0, float var1, float var2) {
      if (var0.length != 0 && !(Math.abs(var1 - var2) < ЯЫO)) {
         int var3 = Math.max(1, Math.round(var0.length * (var2 / var1)));
         short[] var4 = new short[var3];
         double var5 = (double)(var0.length - 1) / Math.max(1, var3 - 1);

         for (int var7 = 0; var7 < var3; var7++) {
            double var8 = var7 * var5;
            int var10 = (int)var8;
            int var11 = Math.min(var10 + 1, var0.length - 1);
            double var12 = var8 - var10;
            var4[var7] = (short)Math.round(var0[var10] * (1.0 - var12) + var0[var11] * var12);
         }

         return var4;
      } else {
         return var0;
      }
   }

   private static short[] л(short[] var0, int var1) {
      if (var1 <= 1) {
         return var0;
      }

      int var2 = var0.length / var1;
      short[] var3 = new short[var2];

      for (int var4 = 0; var4 < var2; var4++) {
         int var5 = 0;
         int var6 = var4 * var1;

         for (int var7 = 0; var7 < var1; var7++) {
            var5 += var0[var6 + var7];
         }

         var3[var4] = (short)(var5 / var1);
      }

      return var3;
   }

   public static void нл() {
      if (!А.exists()) {
         А.mkdirs();
      }

      File var0 = new File(А, ЯЫГ);

      try {
         Files.writeString(var0.toPath(), ЯЫл, StandardCharsets.UTF_8);
      } catch (Exception var2) {
      }
   }

   public static synchronized boolean Лш(String var0) {
      return сх.containsKey(var0.toLowerCase(Locale.ROOT));
   }

   private static short[] ш(AudioInputStream var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      byte[] var2 = new byte[8192];

      int var3;
      while ((var3 = var0.read(var2)) >= 0) {
         if (var3 > 0) {
            var1.write(var2, 0, var3);
         }
      }

      byte[] var4 = var1.toByteArray();
      int var5 = var4.length / 2;
      short[] var6 = new short[var5];

      for (int var7 = 0; var7 < var5; var7++) {
         int var8 = var4[var7 * 2] & 255;
         int var9 = var4[var7 * 2 + 1] << 8;
         var6[var7] = (short)(var8 | var9);
      }

      return var6;
   }

   private static void л(SoundPadLibrary.LoadReport var0) {
      if (var0.Б.isEmpty() && var0.з.isEmpty()) {
         ClientChat.З(ЯЫ7);
         ClientChat.З(А.getAbsolutePath());
      } else {
         if (!var0.Б.isEmpty()) {
            ClientChat.З("Загружено звуков: " + var0.Б.size() + " (" + String.join(ЯЫР, var0.Б) + ")");
         }

         for (String var2 : var0.з) {
            ClientChat.З("Ошибка: " + var2);
         }
      }
   }

   public static synchronized List<SoundPadLibrary.Clip> Л西() {
      return сх.values().stream().filter(var0 -> !с0.containsKey(var0.slot())).toList();
   }

   private static short[] л(byte[] var0) throws UnsupportedAudioFileException, IOException {
      short[] var1 = ш(var0);
      if (var1 != null && var1.length > 0) {
         return var1;
      }

      try (AudioInputStream var2 = AudioSystem.getAudioInputStream(new ByteArrayInputStream(var0))) {
         return л(var2);
      }
   }

   private static short[] ш(byte[] var0) {
      if (var0.length < 44 || var0[0] != 82 || var0[1] != 73 || var0[2] != 70 || var0[3] != 70) {
         return null;
      }

      if (var0[8] == 87 && var0[9] == 65 && var0[10] == 86 && var0[11] == 69) {
         int var1 = 0;
         int var2 = 0;
         int var3 = 0;
         int var4 = 0;
         int var5 = -1;
         int var6 = 0;
         int var7 = 12;

         while (var7 + 8 <= var0.length) {
            int var8 = var0[var7] & 255 | (var0[var7 + 1] & 255) << 8 | (var0[var7 + 2] & 255) << 16 | (var0[var7 + 3] & 255) << 24;
            int var9 = ByteBuffer.wrap(var0, var7 + 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            int var10 = var7 + 8;
            if (var9 < 0 || var10 + var9 > var0.length) {
               return null;
            }

            if (var8 == ЯЫЩ) {
               if (var9 < 16) {
                  return null;
               }

               ByteBuffer var11 = ByteBuffer.wrap(var0, var10, var9).order(ByteOrder.LITTLE_ENDIAN);
               var4 = var11.getShort() & ЯЫу;
               var1 = var11.getShort() & ЯЫ2;
               var2 = var11.getInt();
               var11.getInt();
               var11.getShort();
               var3 = var11.getShort() & ЯЫф;
            } else if (var8 == ЯЫи) {
               var5 = var10;
               var6 = var9;
               break;
            }

            var7 = var10 + var9 + (var9 & 1);
         }

         if (var5 < 0 || var1 <= 0 || var2 <= 0) {
            return null;
         }

         if (var4 == 1 && var3 == 16) {
            int var15 = var1 * (var3 / 8);
            if (var15 <= 0) {
               return null;
            }

            var6 = Math.min(var6, var0.length - var5);
            var6 -= var6 % var15;
            int var16 = var6 / var15;
            if (var16 <= 0) {
               return null;
            }

            short[] var17 = new short[var16 * var1];
            ByteBuffer var18 = ByteBuffer.wrap(var0, var5, var6).order(ByteOrder.LITTLE_ENDIAN);

            for (int var12 = 0; var12 < var17.length; var12++) {
               var17[var12] = var18.getShort();
            }

            short[] var19 = л(var17, var1);
            return л(var19, var2, ЯЫ9);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private static short[] л(File var0) throws UnsupportedAudioFileException, IOException {
      return л(Files.readAllBytes(var0.toPath()));
   }

   private static short[] ш(String var0) throws IOException, UnsupportedAudioFileException {
      String var1 = "/assets/rose/sounds/voicechat/" + var0 + ".wav";

      try (InputStream var2 = SoundPadLibrary.class.getResourceAsStream(var1)) {
         return var2 == null ? null : л(var2.readAllBytes());
      }
   }

   public record Clip(String slot, String displayName, short[] pcm) {
   }

   public static final class LoadReport {
      public final List<String> Б = new ArrayList<>();
      public final List<String> С = new ArrayList<>();
      public final List<String> з = new ArrayList<>();
   }
}
