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

public final class CustomPictureCatalog implements MinecraftAccess {
   public static final File field_4 = new File(sg.config.ClientDirectories.forDevelopment(System.getenv(CustomPictureCatalog.счМ)) + "\\Dimas\\", CustomPictureCatalog.счН);
   private static final String Я西 = "Пользовательские картинки для модуля Picture\n=============================================\n\nПоложите сюда PNG, JPG, JPEG или WebP.\nИмя файла без расширения появится в списке «Картинка».\n\nПосле добавления файлов нажмите «Загрузить свои» в настройках Picture ещё раз.\nЛучше использовать изображения не больше 4096x4096 пикселей.\n";
   private static final Set<String> field_9 = Set.of(CustomPictureCatalog.счн, CustomPictureCatalog.счЦ);
   private static final Map<String, CustomPictureCatalog.PictureTexture> ВД = new LinkedHashMap<>();
   private static final List<Identifier> Ва = new ArrayList<>();
   private static final AtomicInteger А_at_8a8e8ba9 = new AtomicInteger();
   private static final String сч1 = "README.txt";
   private static final String счЬ = "Пользовательские картинки для модуля Picture\n=============================================\n\nПоложите сюда PNG, JPG, JPEG или WebP.\nИмя файла без расширения появится в списке «Картинка».\n\nПосле добавления файлов нажмите «Загрузить свои» в настройках Picture ещё раз.\nЛучше использовать изображения не больше 4096x4096 пикселей.\n";
   private static final String сч衣 = "TextureManager ещё не готов";
   private static final String счш = "README.txt";
   private static final String счИ = "rose";
   private static final String счь = "png";
   private static final String счж = "jpg";
   private static final String сч弟 = "jpeg";
   private static final String счб = "webp";
   private static final String счЫ = "не удалось прочитать изображение";
   private static final String счl = "размер больше 4096x4096";
   private static final String сч8 = "png";
   private static final String сч5 = "не удалось конвертировать в PNG";
   private static final String счв = ", ";
   private static final String счъ = "rose-custom-picture";
   private static final String счМ = "SystemDrive";
   private static final String счН = "rose/pictures";
   private static final String счн = "сюся";
   private static final String счЦ = "могучий орёл";

   public static synchronized List<String> шl() {
      return Collections.unmodifiableList(new ArrayList<>(ВД.keySet()));
   }

   public static synchronized void ж(boolean var0, Runnable var1) {
      西у();
      if (MINECRAFT_CLIENT == null) {
         if (var1 != null) {
            var1.run();
         }
      } else {
         MINECRAFT_CLIENT.execute(() -> {
            CustomPictureCatalog.LoadReport var2 = method_4();
            if (var0) {
               method_4(var2);
            }

            if (var1 != null) {
               var1.run();
            }
         });
      }
   }

   private CustomPictureCatalog() {
   }

   private static void 西В() {
      if (MINECRAFT_CLIENT != null && MINECRAFT_CLIENT.getTextureManager() != null) {
         for (Identifier var1 : Ва) {
            MINECRAFT_CLIENT.getTextureManager().release(var1);
         }

         Ва.clear();
      } else {
         Ва.clear();
      }
   }

   private static boolean шЕ(String var0) {
      String var1 = var0.toLowerCase(Locale.ROOT);
      return var1.equals(счь) || var1.equals(счж) || var1.equals(сч弟) || var1.equals(счб);
   }

   private static synchronized CustomPictureCatalog.LoadReport method_4() {
      西у();
      西В();
      ВД.clear();
      CustomPictureCatalog.LoadReport var0 = new CustomPictureCatalog.LoadReport();
      File[] var1 = field_4.listFiles();
      if (var1 == null) {
         var0.с7.add("Папка недоступна: " + field_4.getAbsolutePath());
         return var0;
      }

      if (MINECRAFT_CLIENT != null && MINECRAFT_CLIENT.getTextureManager() != null) {
         for (File var5 : var1) {
            if (var5.isFile() && !var5.getName().equalsIgnoreCase(счш)) {
               String var6 = var5.getName();
               int var7 = var6.lastIndexOf(46);
               if (var7 > 0 && шЕ(var6.substring(var7 + 1))) {
                  String var8 = var6.substring(0, var7).trim();
                  if (!var8.isEmpty()) {
                     if (field_9.contains(var8.toLowerCase(Locale.ROOT))) {
                        var0.с7.add(var6 + " — имя занято встроенной картинкой");
                     } else if (!ВД.containsKey(var8)) {
                        try {
                           NativeImage var9 = И(var5);
                           float var13 = (float)var9.getWidth() / var9.getHeight();
                           Identifier var11 = Identifier.fromNamespaceAndPath(счИ, "custom_picture/" + А_at_8a8e8ba9.incrementAndGet());
                           MINECRAFT_CLIENT.getTextureManager().register(var11, new DynamicTexture(() -> счъ, var9));
                           Ва.add(var11);
                           ВД.put(var8, new CustomPictureCatalog.PictureTexture(var11, var13));
                           var0.сO.add(var8);
                        } catch (Exception var12) {
                           String var10 = var12.getMessage() != null ? var12.getMessage() : var12.getClass().getSimpleName();
                           var0.с7.add(var6 + " — " + var10);
                        }
                     }
                  }
               }
            }
         }

         return var0;
      } else {
         var0.с7.add(сч衣);
         return var0;
      }
   }

   private static void method_4(CustomPictureCatalog.LoadReport var0) {
      if (MINECRAFT_CLIENT.player != null) {
         if (var0.сO.isEmpty() && var0.с7.isEmpty()) {
            ClientChat.З("В папке нет PNG/JPG/WebP. Путь: " + field_4.getAbsolutePath());
         } else {
            if (!var0.сO.isEmpty()) {
               ClientChat.З("Загружено картинок: " + String.join(счв, var0.сO));
            }

            for (String var2 : var0.с7) {
               ClientChat.З("Ошибка: " + var2);
            }
         }
      }
   }

   public static void 西у() {
      if (!field_4.exists()) {
         field_4.mkdirs();
      }

      File var0 = new File(field_4, сч1);

      try {
         Files.writeString(var0.toPath(), счЬ, StandardCharsets.UTF_8);
      } catch (Exception var2) {
      }
   }

   public static synchronized CustomPictureCatalog.PictureTexture method_4(String var0) {
      return ВД.get(var0);
   }

   private static NativeImage И(File var0) throws Exception {
      BufferedImage var1 = ImageIO.read(var0);
      if (var1 == null) {
         throw new IllegalArgumentException(счЫ);
      }

      if (var1.getWidth() <= 4096 && var1.getHeight() <= 4096) {
         BufferedImage var2 = new BufferedImage(var1.getWidth(), var1.getHeight(), 2);
         Graphics var3 = var2.getGraphics();
         var3.drawImage(var1, 0, 0, null);
         var3.dispose();
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         if (!ImageIO.write(var2, сч8, var4)) {
            throw new IllegalArgumentException(сч5);
         } else {
            return NativeImage.read(var4.toByteArray());
         }
      } else {
         throw new IllegalArgumentException(счl);
      }
   }

   private static final class LoadReport {
      private final List<String> сO = new ArrayList<>();
      private final List<String> с7 = new ArrayList<>();
   }

   public record PictureTexture(Identifier texture, float aspectRatio) {
   }
}
