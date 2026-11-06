package settings.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import net.minecraft.resources.Identifier;

public final class ReallyWorldRankTextures {
   public static final Path В = Я();
   private static final String с1ъ = "assets/minecraft/font/default.json";
   private static final String с1М = "providers";
   private static final String с1Н = "bitmap";
   private static final String с1н = "type";
   private static final String с1Ц = "type";
   private static final String с1ч = "chars";
   private static final String с1Э = "file";
   private static final int с1必 = 65535;
   private static final String с1щ = "png";
   private static final int с1Г = 65535;
   private static final String с1л = "rose";
   private static final int с1э = 42240;
   private static final float с1ю = 4.0F;
   private static final float с1Щ = 3.9375F;
   private static final String с1у = "[^a-z0-9]+";
   private static final String с12 = "_";
   private static final String с1ф = "^_+|_+$";
   private static final String с1и = "src/client/resources/assets/rose";
   private static final String с19 = "textures/reallyworld/ranks";
   private static final String с1о = "src/client/resources/assets/rose/textures/reallyworld/ranks";
   private static final int с1O = 65535;
   private static final String с17 = "minecraft";

   private static Path Я() {
      Path var0 = Path.of("").toAbsolutePath().normalize();

      for (int var1 = 0; var1 < 4 && var0 != null; var0 = var0.getParent()) {
         Path var2 = var0.resolve(с1и);
         if (Files.isDirectory(var2)) {
            return var2.resolve(с19);
         }

         var1++;
      }

      return Path.of(с1о).toAbsolutePath().normalize();
   }

   private static String в(String var0) {
      int var1 = var0.indexOf(58);
      String var2 = var1 >= 0 ? var0.substring(0, var1) : с17;
      String var3 = var1 >= 0 ? var0.substring(var1 + 1) : var0;
      return "assets/" + var2 + "/textures/" + var3;
   }

   private static boolean Ж(JsonArray var0) {
      for (JsonElement var2 : var0) {
         for (int var6 : var2.getAsString().codePoints().toArray()) {
            if (var6 <= с1O && ReallyWorldRankGlyphs.щ((char)var6)) {
               return true;
            }
         }
      }

      return false;
   }

   public static float Я(int var0, float var1) {
      return var1 * ((char)var0 == с1э ? с1ю : с1Щ);
   }

   public static Identifier Ж(int var0) {
      return var0 <= с1Г && ReallyWorldRankGlyphs.щ((char)var0) ? Identifier.fromNamespaceAndPath(с1л, "textures/reallyworld/ranks/" + method_8(var0) + ".png") : null;
   }

   private static int Ж(BufferedImage var0, int var1, int var2, int var3, int var4) {
      for (int var5 = var3 - 1; var5 >= 0; var5--) {
         for (int var6 = 0; var6 < var4; var6++) {
            if (var0.getRGB(var1 + var5, var2 + var6) >>> 24 != 0) {
               return var5 + 1;
            }
         }
      }

      return 0;
   }

   public static void Я(Path var0) throws Exception {
      Files.createDirectories(В);
      HashMap var1 = new HashMap();

      try (ZipFile var2 = new ZipFile(var0.toFile())) {
         ZipEntry var3 = var2.getEntry(с1ъ);
         if (var3 == null) {
            return;
         }

         InputStreamReader var5 = new InputStreamReader(var2.getInputStream(var3), StandardCharsets.UTF_8);

         JsonObject var4;
         try {
            var4 = JsonParser.parseReader(var5).getAsJsonObject();
         } catch (Throwable var27) {
            try {
               var5.close();
            } catch (Throwable var26) {
               var27.addSuppressed(var26);
            }

            throw var27;
         }

         var5.close();
         JsonArray var30 = var4.getAsJsonArray(с1М);
         if (var30 == null) {
            return;
         }

         for (JsonElement var7 : var30) {
            if (var7.isJsonObject()) {
               JsonObject var8 = var7.getAsJsonObject();
               if (с1Н.equals(var8.has(с1н) ? var8.get(с1Ц).getAsString() : "")) {
                  JsonArray var9 = var8.getAsJsonArray(с1ч);
                  if (var9 != null && Ж(var9)) {
                     String var10 = var8.get(с1Э).getAsString();
                     BufferedImage var11 = (BufferedImage)var1.get(var10);
                     if (var11 == null) {
                        ZipEntry var12 = var2.getEntry(в(var10));
                        if (var12 == null) {
                           continue;
                        }

                        try (InputStream var13 = var2.getInputStream(var12)) {
                           var11 = ImageIO.read(var13);
                        }

                        if (var11 == null) {
                           continue;
                        }

                        var1.put(var10, var11);
                     }

                     int var31 = var9.size();
                     int var32 = 0;

                     for (JsonElement var15 : var9) {
                        String var16 = var15.getAsString();
                        var32 = Math.max(var32, var16.codePointCount(0, var16.length()));
                     }

                     if (var31 > 0 && var32 > 0) {
                        int var33 = var11.getWidth() / var32;
                        int var34 = var11.getHeight() / var31;

                        for (int var35 = 0; var35 < var31; var35++) {
                           int[] var17 = var9.get(var35).getAsString().codePoints().toArray();

                           for (int var18 = 0; var18 < var17.length; var18++) {
                              int var19 = var17[var18];
                              if (var19 <= с1必 && ReallyWorldRankGlyphs.щ((char)var19)) {
                                 int var20 = Ж(var11, var18 * var33, var35 * var34, var33, var34);
                                 if (var20 > 0) {
                                    int var21 = Я(var11, var18 * var33, var35 * var34, var20, var34);
                                    if (var21 > 0) {
                                       BufferedImage var22 = Ж(var11.getSubimage(var18 * var33, var35 * var34, var20, var21));
                                       String var23 = method_8(var19);
                                       ImageIO.write(var22, с1щ, В.resolve(var23 + ".png").toFile());
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private ReallyWorldRankTextures() {
   }

   private static int Я(BufferedImage var0, int var1, int var2, int var3, int var4) {
      for (int var5 = var4 - 1; var5 >= 0; var5--) {
         for (int var6 = 0; var6 < var3; var6++) {
            if (var0.getRGB(var1 + var6, var2 + var5) >>> 24 != 0) {
               return var5 + 1;
            }
         }
      }

      return 0;
   }

   private static String method_8(int var0) {
      return ReallyWorldRankGlyphs.Ч(var0).toLowerCase(Locale.ROOT).replaceAll(с1у, с12).replaceAll(с1ф, "");
   }

   private static BufferedImage Ж(BufferedImage var0) {
      BufferedImage var1 = new BufferedImage(var0.getWidth() * 4, var0.getHeight() * 4, 2);
      Graphics2D var2 = var1.createGraphics();
      var2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      var2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      var2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      var2.drawImage(var0, 0, 0, var1.getWidth(), var1.getHeight(), null);
      var2.dispose();
      return var1;
   }
}
