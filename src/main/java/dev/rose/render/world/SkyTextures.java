package dev.rose.render.world;

import com.mojang.blaze3d.platform.NativeImage;
import dev.rose.render.gpu.RoseTexture;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;


public final class SkyTextures {
   private static Identifier aurora;

   private SkyTextures() {
   }

   public static Identifier aurora() {
      if (aurora == null) {
         int width = 512;
         int height = 128;
         NativeImage image = new NativeImage(width, height, false);
         Random random = new Random(11L);
         int[] fine = new int[]{5, 11, 19, 37, 59, 83, 127};
         int[] coarse = new int[]{2, 3, 7};
         float[] finePhase = new float[fine.length];
         float[] coarsePhase = new float[coarse.length];

         for (int i = 0; i < fine.length; i++) {
            finePhase[i] = random.nextFloat() * 6.2832F;
         }

         for (int i = 0; i < coarse.length; i++) {
            coarsePhase[i] = random.nextFloat() * 6.2832F;
         }

         for (int x = 0; x < width; x++) {
            float u = (float)x / width;
            float ray = 0.0F;
            float weight = 0.0F;

            for (int i = 0; i < fine.length; i++) {
               float amplitude = 1.0F / (1.0F + i * 0.45F);
               ray += amplitude * (0.5F + 0.5F * (float)Math.sin(6.2832F * fine[i] * u + finePhase[i]));
               weight += amplitude;
            }

            ray = (float)Math.pow(ray / weight, 2.3) * 2.6F;
            ray = Math.clamp(ray, 0.0F, 1.0F);
            float tall = 0.0F;

            for (int i = 0; i < coarse.length; i++) {
               tall += (0.5F + 0.5F * (float)Math.sin(6.2832F * coarse[i] * u + coarsePhase[i])) / coarse.length;
            }

            for (int y = 0; y < height; y++) {
               float s = 1.0F - (float)y / (height - 1);
               float edge = smooth(0.0F, 0.05F, s) * (1.0F - smooth(0.82F, 1.0F, s));
               float body = (float)Math.exp(-s * (1.9F + 3.2F * (1.0F - tall)));
               float rim = 0.34F * (float)Math.exp(-Math.pow((s - 0.07F) / 0.08F, 2.0));
               float streaks = (0.5F + 0.5F * ray) * (1.0F - s) + ray * s;
               float alpha = Math.clamp(edge * (body * streaks + rim * (0.5F + 0.5F * ray)), 0.0F, 1.0F);
               image.setPixelABGR(x, y, Math.round(alpha * 255.0F) << 24 | 16777215);
            }
         }

         aurora = register("sky_aurora", image);
      }

      return aurora;
   }

   public static Identifier smooth(Identifier texture) {
      return texture;
   }

   private static float smooth(float from, float to, float value) {
      float t = Math.clamp((value - from) / (to - from), 0.0F, 1.0F);
      return t * t * (3.0F - 2.0F * t);
   }

   private static Identifier register(String name, NativeImage image) {
      Identifier id = Identifier.fromNamespaceAndPath("rose", "dynamic/" + name);
      Minecraft.getInstance().getTextureManager().register(id, new RoseTexture(name, image));
      return id;
   }
}
