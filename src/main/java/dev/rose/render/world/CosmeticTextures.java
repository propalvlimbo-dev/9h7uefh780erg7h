package dev.rose.render.world;

import com.mojang.blaze3d.platform.NativeImage;
import dev.rose.RoseClient;
import dev.rose.render.Theme;
import dev.rose.render.gpu.RoseTexture;
import dev.rose.util.ColorUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;


public final class CosmeticTextures {
   private static Identifier white;
   private static Identifier feather;
   private static Identifier dot;
   private static Identifier ring;
   private static Identifier membrane;
   private static Identifier butterfly;
   private static Identifier petal;
   private static Identifier leaf;
   private static Identifier noise;
   private static Identifier gradientCape;
   private static int gradientColors;
   private static final Map<Path, Identifier> capes = new HashMap<>();
   private static final Map<Path, Long> capeTimes = new HashMap<>();

   private CosmeticTextures() {
   }

   public static Identifier white() {
      if (white == null) {
         NativeImage image = new NativeImage(1, 1, false);
         image.setPixelABGR(0, 0, -1);
         white = register("white", image);
      }

      return white;
   }

   public static Identifier dot() {
      if (dot == null) {
         int size = 64;
         NativeImage image = new NativeImage(size, size, false);

         for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
               float dx = (x + 0.5F) / size * 2.0F - 1.0F;
               float dy = (y + 0.5F) / size * 2.0F - 1.0F;
               float distance = Math.min(1.0F, (float)Math.sqrt(dx * dx + dy * dy));
               float alpha = (float)Math.pow(1.0F - distance, 2.2);
               image.setPixelABGR(x, y, Math.round(alpha * 255.0F) << 24 | 16777215);
            }
         }

         dot = register("dot", image);
      }

      return dot;
   }

   public static Identifier ring() {
      if (ring == null) {
         int size = 128;
         NativeImage image = new NativeImage(size, size, false);

         for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
               float dx = (x + 0.5F) / size * 2.0F - 1.0F;
               float dy = (y + 0.5F) / size * 2.0F - 1.0F;
               float d = (float)Math.sqrt(dx * dx + dy * dy);
               float band = (float)Math.exp(-Math.pow((d - 0.8) / 0.055, 2.0));
               float halo = 0.28F * (float)Math.exp(-Math.pow((d - 0.8) / 0.2, 2.0));
               float fill = d < 0.8F ? 0.05F * (d / 0.8F) : 0.0F;
               float alpha = Math.clamp(band + halo + fill, 0.0F, 1.0F) * (d > 0.99F ? 0 : 1);
               image.setPixelABGR(x, y, Math.round(alpha * 255.0F) << 24 | 16777215);
            }
         }

         ring = register("ring", image);
      }

      return ring;
   }

   public static Identifier feather() {
      if (feather == null) {
         int w = 48;
         int h = 128;
         NativeImage image = new NativeImage(w, h, false);

         for (int y = 0; y < h; y++) {
            float along = (float)y / (h - 1);
            float grow = Math.clamp((along - 0.06F) / 0.94F, 0.0F, 1.0F);
            float halfWidth = 0.47F * (float)Math.pow(Math.sin(Math.pow(grow, 0.75) * Math.PI), 0.55);

            for (int x = 0; x < w; x++) {
               float across = (float)x / (w - 1) - 0.5F;
               float distance = Math.abs(across);
               float vane = halfWidth <= 0.001F ? 0.0F : Math.clamp((halfWidth - distance) / 0.07F, 0.0F, 1.0F);
               float barbs = 0.8F + 0.2F * (float)Math.sin(along * 150.0F - distance * 55.0F);
               float value = 0.86F + 0.14F * (1.0F - distance / 0.5F);
               float alpha = vane * barbs;
               if (along < 0.22F) {
                  alpha *= 0.45F + 0.55F * (along / 0.22F);
               }

               float shaftWidth = 0.028F * (1.0F - along * 0.8F);
               if (distance < shaftWidth && along < 0.97F) {
                  alpha = Math.max(alpha, 0.95F);
                  value = 0.78F;
               }

               int a = Math.round(Math.clamp(alpha, 0.0F, 1.0F) * 255.0F);
               int v = Math.round(Math.clamp(value, 0.0F, 1.0F) * 255.0F);
               image.setPixelABGR(x, y, a << 24 | v << 16 | v << 8 | v);
            }
         }

         feather = register("feather", image);
      }

      return feather;
   }

   public static Identifier membrane() {
      if (membrane == null) {
         int size = 128;
         NativeImage image = new NativeImage(size, size, false);

         for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
               float u = (float)x / size;
               float v = (float)y / size;
               float angle = (float)Math.atan2(v, u);
               float radius = (float)Math.sqrt(u * u + v * v);
               float veins = Math.abs((float)Math.sin(angle * 14.0F + radius * 3.0F)) < 0.07F ? 0.55F : 1.0F;
               float texture = 0.82F + 0.08F * (float)Math.sin(x * 0.9F) * (float)Math.sin(y * 0.7F) + 0.06F * (float)Math.sin((x + y) * 0.21F);
               float value = Math.clamp(texture * veins, 0.0F, 1.0F);
               int c = Math.round(value * 255.0F);
               image.setPixelABGR(x, y, -436207616 | c << 16 | c << 8 | c);
            }
         }

         membrane = register("membrane", image);
      }

      return membrane;
   }

   public static Identifier butterfly() {
      if (butterfly == null) {
         int size = 128;
         NativeImage image = new NativeImage(size, size, false);

         for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
               float u = (x + 0.5F) / size;
               float v = (y + 0.5F) / size;
               float value = 1.0F - 0.18F * Math.max(0.0F, 0.25F - v) / 0.25F;
               if (v < 0.8F && Math.abs((float)Math.sin(u * Math.PI * 7.0)) < 0.07F + 0.05F * (1.0F - v)) {
                  value = 0.3F;
               }

               if (v >= 0.8F) {
                  value = 0.14F;
                  float spotU = (float)(Math.floor(u * 6.0F) + 0.5) / 6.0F;
                  float du = (u - spotU) * 6.0F;
                  float dv = (v - 0.89F) * 9.0F;
                  if (du * du + dv * dv < 0.12F) {
                     value = 1.0F;
                  }
               }

               float alpha = v > 0.97F ? Math.clamp((1.0F - v) / 0.03F, 0.0F, 1.0F) : 1.0F;
               int c = Math.round(value * 255.0F);
               int a = Math.round(alpha * 255.0F);
               image.setPixelABGR(x, y, a << 24 | c << 16 | c << 8 | c);
            }
         }

         butterfly = register("butterfly", image);
      }

      return butterfly;
   }

   public static Identifier petal() {
      if (petal == null) {
         int size = 64;
         NativeImage image = new NativeImage(size, size, false);

         for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
               float u = (x + 0.5F) / size * 2.0F - 1.0F;
               float v = (y + 0.5F) / size * 2.0F - 1.0F;
               float shape = u / 0.62F * (u / 0.62F) + (v + 0.08F) / 0.92F * ((v + 0.08F) / 0.92F);
               float edge = Math.clamp((1.0F - shape) / 0.12F, 0.0F, 1.0F);
               if (v < -0.62F && Math.abs(u) < 0.16F * (-0.62F - v) / 0.3F + 0.02F) {
                  edge = 0.0F;
               }

               float value = 0.82F + 0.18F * (1.0F - Math.min(1.0F, (float)Math.sqrt(u * u + (v - 0.5F) * (v - 0.5F))));
               int a = Math.round(edge * 255.0F);
               int c = Math.round(Math.clamp(value, 0.0F, 1.0F) * 255.0F);
               image.setPixelABGR(x, y, a << 24 | c << 16 | c << 8 | c);
            }
         }

         petal = register("petal", image);
      }

      return petal;
   }

   public static Identifier leaf() {
      if (leaf == null) {
         int size = 64;
         NativeImage image = new NativeImage(size, size, false);

         for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
               float u = (x + 0.5F) / size * 2.0F - 1.0F;
               float v = (y + 0.5F) / size * 2.0F - 1.0F;
               float halfWidth = 0.55F * (float)Math.sqrt(Math.max(0.0F, 1.0F - v * v)) * (1.0F - 0.15F * v);
               float edge = halfWidth <= 0.0F ? 0.0F : Math.clamp((halfWidth - Math.abs(u)) / 0.08F, 0.0F, 1.0F);
               float value = Math.abs(u) < 0.03F ? 0.62F : 0.85F + 0.15F * (float)Math.sin(v * 18.0F + Math.abs(u) * 10.0F) * 0.5F;
               int a = Math.round(edge * 255.0F);
               int c = Math.round(Math.clamp(value, 0.0F, 1.0F) * 255.0F);
               image.setPixelABGR(x, y, a << 24 | c << 16 | c << 8 | c);
            }
         }

         leaf = register("leaf", image);
      }

      return leaf;
   }

   public static Identifier noise() {
      if (noise == null) {
         int size = 256;
         NativeImage image = new NativeImage(size, size, false);
         Random random = new Random(7L);

         for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
               float value = random.nextFloat();
               int a = Math.round(Math.abs(value - 0.5F) * 2.0F * 255.0F * 0.6F);
               int c = value > 0.5F ? 255 : 0;
               image.setPixelABGR(x, y, a << 24 | c << 16 | c << 8 | c);
            }
         }

         noise = register("noise", image);
      }

      return noise;
   }

   public static Identifier gradientCape() {
      int accent = Theme.accent();
      int accent2 = Theme.accent2();
      int key = accent * 31 + accent2;
      if (gradientCape != null && key == gradientColors) {
         return gradientCape;
      }

      gradientColors = key;
      NativeImage image = new NativeImage(64, 32, false);

      for (int y = 0; y < 32; y++) {
         for (int x = 0; x < 64; x++) {
            float t = Math.clamp(y / 17.0F, 0.0F, 1.0F);
            int color = ColorUtil.lerp(accent, accent2, t);
            float shine = 0.12F * (float)Math.max(0.0, Math.sin((x + y) * 0.35));
            color = ColorUtil.lerp(color, -1, shine);
            image.setPixelABGR(x, y, abgr(color));
         }
      }

      if (gradientCape != null) {
         Minecraft.getInstance().getTextureManager().release(gradientCape);
      }

      gradientCape = register("cape_gradient_" + Integer.toHexString(key), image);
      return gradientCape;
   }

   public static Identifier cape(Path file) {
      try {
         long modified = Files.getLastModifiedTime(file).toMillis();
         Identifier known = capes.get(file);
         if (known != null && capeTimes.getOrDefault(file, 0L) == modified) {
            return known;
         }

         NativeImage image;
         try (InputStream in = Files.newInputStream(file)) {
            image = NativeImage.read(in);
         }

         if (known != null) {
            Minecraft.getInstance().getTextureManager().release(known);
         }

         Identifier id = register("cape_" + Integer.toHexString(file.toAbsolutePath().toString().hashCode()) + "_" + Long.toHexString(modified), image);
         capes.put(file, id);
         capeTimes.put(file, modified);
         return id;
      } catch (IOException | RuntimeException e) {
         RoseClient.LOGGER.warn("[Rose] cannot read cape {}", file, e);
         return null;
      }
   }

   private static Identifier register(String name, NativeImage image) {
      Identifier id = Identifier.fromNamespaceAndPath("rose", "dynamic/" + name);
      Minecraft.getInstance().getTextureManager().register(id, new RoseTexture(name, image));
      return id;
   }

   private static int abgr(int argb) {
      return ColorUtil.alpha(argb) << 24 | ColorUtil.blue(argb) << 16 | ColorUtil.green(argb) << 8 | ColorUtil.red(argb);
   }
}
