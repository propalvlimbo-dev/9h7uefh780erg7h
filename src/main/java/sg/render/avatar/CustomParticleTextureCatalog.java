package sg.render.avatar;

import com.mojang.blaze3d.platform.NativeImage;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import sg.ec.MinecraftAccess;
import sg.network.ClientChat;

public final class CustomParticleTextureCatalog implements MinecraftAccess {
   public static final File В_f0e5dd92 = new File(sg.config.ClientDirectories.forDevelopment(System.getenv(CustomParticleTextureCatalog.ВНс)) + "\\Dimas\\", CustomParticleTextureCatalog.ВНВ);
   private static final String сС = "Кастомные формы частиц Rose\n==============================\n\nФормат файла: PNG или JPG/JPEG\n(можно даже JPG, переименованный в .png — тоже подхватит)\n\nИмя в меню: имя файла без расширения\n  abemish.png  ->  \"abemish\"\n  weslo.jpg    ->  \"weslo\"\n\nРазмер картинки:\n  - лучше квадрат (256x256, 512x512 или 1000x1000)\n  - можно меньше/больше — размер партикла задаётся слайдером в модуле\n  - слишком маленькие (<64) будут мыльными, слишком большие (>2048) зря жрут память\n\nКак рисовать:\n  - прозрачный фон (для PNG)\n  - сама форма — белая или светлая (цвет партикла красится сверху)\n  - чёрный/тёмный почти не видно (аддитивный бленд)\n\nПосле добавления файлов нажми «Загрузить свои» ещё раз, чтобы обновить список.\n";
   private static final Set<String> ж = Set.of(
      CustomParticleTextureCatalog.ВНЯ, CustomParticleTextureCatalog.ВНХ, CustomParticleTextureCatalog.ВНе, CustomParticleTextureCatalog.ВНА, CustomParticleTextureCatalog.ВН4, CustomParticleTextureCatalog.ВНп, CustomParticleTextureCatalog.ВНД, CustomParticleTextureCatalog.ВНЪ, CustomParticleTextureCatalog.ВНт, CustomParticleTextureCatalog.ВНа, CustomParticleTextureCatalog.ВНЮ, CustomParticleTextureCatalog.ВНЧ, CustomParticleTextureCatalog.ВНЛ
   );
   private static final Map<String, Identifier> я = new LinkedHashMap<>();
   private static final List<Identifier> сЯ = new ArrayList<>();
   private static final AtomicInteger В_18558580 = new AtomicInteger();
   private static final String ВМг = "README.txt";
   private static final String ВМ0 = "Кастомные формы частиц Rose\n==============================\n\nФормат файла: PNG или JPG/JPEG\n(можно даже JPG, переименованный в .png — тоже подхватит)\n\nИмя в меню: имя файла без расширения\n  abemish.png  ->  \"abemish\"\n  weslo.jpg    ->  \"weslo\"\n\nРазмер картинки:\n  - лучше квадрат (256x256, 512x512 или 1000x1000)\n  - можно меньше/больше — размер партикла задаётся слайдером в модуле\n  - слишком маленькие (<64) будут мыльными, слишком большие (>2048) зря жрут память\n\nКак рисовать:\n  - прозрачный фон (для PNG)\n  - сама форма — белая или светлая (цвет партикла красится сверху)\n  - чёрный/тёмный почти не видно (аддитивный бленд)\n\nПосле добавления файлов нажми «Загрузить свои» ещё раз, чтобы обновить список.\n";
   private static final String ВМх = "TextureManager ещё не готов";
   private static final String ВМУ = "README.txt";
   private static final String ВМЗ = "rose";
   private static final String ВМЕ = ", ";
   private static final String ВМП = "png";
   private static final String ВМ西 = "jpg";
   private static final String ВМд = "jpeg";
   private static final String ВМБ = "пустой файл";
   private static final String ВМС = "не удалось прочитать изображение";
   private static final String ВМз = "png";
   private static final String ВМц = "не удалось конвертировать в PNG";
   private static final String ВМя = "rose-custom-particle";
   private static final String ВНс = "SystemDrive";
   private static final String ВНВ = "rose/particles";
   private static final String ВНЯ = "корона";
   private static final String ВНХ = "взрыв куба";
   private static final String ВНе = "доллар";
   private static final String ВНА = "сердце";
   private static final String ВН4 = "молния";
   private static final String ВНп = "линия";
   private static final String ВНД = "ромб";
   private static final String ВНЪ = "снежинка";
   private static final String ВНт = "звезда";
   private static final String ВНа = "звезда 2";
   private static final String ВНЮ = "треугольник";
   private static final String ВНЧ = "свечение";
   private static final String ВНЛ = "рандом";

   public static synchronized CustomParticleTextureCatalog.LoadReport с() {
      О9();
      ОЗ();
      я.clear();
      CustomParticleTextureCatalog.LoadReport var0 = new CustomParticleTextureCatalog.LoadReport();
      File[] var1 = В_f0e5dd92.listFiles();
      if (var1 == null) {
         var0.l.add("Папка недоступна: " + В_f0e5dd92.getAbsolutePath());
         return var0;
      }

      if (MINECRAFT_CLIENT != null && MINECRAFT_CLIENT.getTextureManager() != null) {
         for (File var5 : var1) {
            if (var5.isFile()) {
               String var6 = var5.getName();
               if (!var6.equalsIgnoreCase(ВМУ)) {
                  int var7 = var6.lastIndexOf(46);
                  if (var7 > 0) {
                     String var8 = var6.substring(var7 + 1).toLowerCase(Locale.ROOT);
                     if (М(var8)) {
                        String var9 = var6.substring(0, var7).trim();
                        if (!var9.isEmpty()) {
                           if (ж.contains(var9.toLowerCase(Locale.ROOT))) {
                              var0.l.add(var6 + " — имя занято встроенной формой");
                           } else if (!я.containsKey(var9)) {
                              try {
                                 NativeImage var10 = с(var5);
                                 Identifier var13 = Identifier.fromNamespaceAndPath(ВМЗ, "custom_particle/" + В_18558580.incrementAndGet());
                                 MINECRAFT_CLIENT.getTextureManager().register(var13, new DynamicTexture(() -> ВМя, var10));
                                 сЯ.add(var13);
                                 я.put(var9, var13);
                                 var0.Ы.add(var9);
                              } catch (Exception var12) {
                                 String var11 = var12.getMessage() != null ? var12.getMessage() : var12.getClass().getSimpleName();
                                 var0.l.add(var6 + " — " + var11);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return var0;
      } else {
         var0.l.add(ВМх);
         return var0;
      }
   }

   private static void с(CustomParticleTextureCatalog.LoadReport var0) {
      if (MINECRAFT_CLIENT.player != null) {
         if (var0.Ы.isEmpty() && var0.l.isEmpty()) {
            ClientChat.З("В папке нет PNG/JPG. Путь: " + В_f0e5dd92.getAbsolutePath());
         } else {
            if (!var0.Ы.isEmpty()) {
               ClientChat.З("Загружено форм: " + String.join(ВМЕ, var0.Ы));
            }

            for (String var2 : var0.l) {
               ClientChat.З("Ошибка: " + var2);
            }
         }
      }
   }

   private static void ОЗ() {
      if (MINECRAFT_CLIENT != null && MINECRAFT_CLIENT.getTextureManager() != null) {
         for (Identifier var1 : сЯ) {
            MINECRAFT_CLIENT.getTextureManager().release(var1);
         }

         сЯ.clear();
      } else {
         сЯ.clear();
      }
   }

   private static boolean М(String var0) {
      return var0.equals(ВМП) || var0.equals(ВМ西) || var0.equals(ВМд);
   }

   public static synchronized List<Identifier> 诶_2571477d() {
      return Collections.unmodifiableList(new ArrayList<>(я.values()));
   }

   public static synchronized void с(boolean var0, Runnable var1) {
      О9();
      if (MINECRAFT_CLIENT == null) {
         if (var1 != null) {
            var1.run();
         }
      } else {
         MINECRAFT_CLIENT.execute(() -> {
            CustomParticleTextureCatalog.LoadReport var2 = с();
            if (var0) {
               с(var2);
            }

            if (var1 != null) {
               var1.run();
            }
         });
      }
   }

   public static synchronized List<String> ц() {
      return Collections.unmodifiableList(new ArrayList<>(я.keySet()));
   }

   private static boolean Ш(byte[] var0) {
      return var0.length >= 8 && (var0[0] & 255) == 137 && var0[1] == 80 && var0[2] == 78 && var0[3] == 71;
   }

   private static NativeImage с(File var0) throws Exception {
      byte[] var1 = Files.readAllBytes(var0.toPath());
      if (var1.length < 4) {
         throw new IllegalArgumentException(ВМБ);
      } else if (Ш(var1)) {
         return NativeImage.read(var1);
      } else {
         BufferedImage var2 = ImageIO.read(var0);
         if (var2 == null) {
            throw new IllegalArgumentException(ВМС);
         } else {
            BufferedImage var3 = new BufferedImage(var2.getWidth(), var2.getHeight(), 2);
            Graphics var4 = var3.getGraphics();
            var4.drawImage(var2, 0, 0, null);
            var4.dispose();
            ByteArrayOutputStream var5 = new ByteArrayOutputStream();
            if (!ImageIO.write(var3, ВМз, var5)) {
               throw new IllegalArgumentException(ВМц);
            } else {
               return NativeImage.read(var5.toByteArray());
            }
         }
      }
   }

   public static void О9() {
      if (!В_f0e5dd92.exists()) {
         В_f0e5dd92.mkdirs();
      }

      File var0 = new File(В_f0e5dd92, ВМг);

      try {
         Files.writeString(var0.toPath(), ВМ0, StandardCharsets.UTF_8);
      } catch (Exception var2) {
      }
   }

   private CustomParticleTextureCatalog() {
   }

   public static synchronized Identifier Ш(String var0) {
      return я.get(var0);
   }

   public static synchronized boolean ц(String var0) {
      return я.containsKey(var0);
   }

   public static final class LoadReport {
      private final List<String> Ы = new ArrayList<>();
      private final List<String> l = new ArrayList<>();
   }
}
