package dev.rose.render.world;

import com.mojang.blaze3d.platform.NativeImage;
import dev.rose.render.gpu.RoseTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;


public final class EffectTextures {
   private static final int SIZE = 96;
   private static Identifier star;
   private static Identifier heart;
   private static Identifier bubble;
   private static Identifier shard;

   private EffectTextures() {
   }

   public static Identifier star() {
      if (star == null) {
         star = register("fx_star", draw((x, y) -> starDistance(x, y, 0.62F, 0.5F) - 0.09F, 0.16F));
      }

      return star;
   }

   public static Identifier heart() {
      if (heart == null) {
         heart = register("fx_heart", draw((x, y) -> heartDistance(x * 0.72F, y * 0.72F + 0.53F) / 0.72F, 0.14F));
      }

      return heart;
   }

   public static Identifier shard() {
      if (shard == null) {
         shard = register("fx_shard", draw((x, y) -> (Math.abs(x) / 0.34F + Math.abs(y) / 0.8F - 1.0F) * 0.3F, 0.12F));
      }

      return shard;
   }

   public static Identifier bubble() {
      if (bubble == null) {
         NativeImage image = new NativeImage(96, 96, false);

         for (int py = 0; py < 96; py++) {
            for (int px = 0; px < 96; px++) {
               float x = (px + 0.5F) / 96.0F * 2.0F - 1.0F;
               float y = 1.0F - (py + 0.5F) / 96.0F * 2.0F;
               float d = (float)Math.sqrt(x * x + y * y);
               float rim = (float)Math.exp(-Math.pow((d - 0.78F) / 0.045F, 2.0));
               float film = d < 0.78F ? 0.1F + 0.16F * (float)Math.pow(d / 0.78F, 3.0) : 0.0F;
               float hx = x + 0.32F;
               float hy = y - 0.36F;
               float highlight = 0.9F * (float)Math.exp(-(hx * hx * 30.0F + hy * hy * 55.0F));
               float halo = 0.18F * (float)Math.exp(-Math.pow((d - 0.78F) / 0.16F, 2.0));
               float alpha = Math.clamp(rim + film + highlight + halo, 0.0F, 1.0F);
               image.setPixelABGR(px, py, Math.round(alpha * 255.0F) << 24 | 16777215);
            }
         }

         bubble = register("fx_bubble", image);
      }

      return bubble;
   }

   private static NativeImage draw(EffectTextures.Shape shape, float glow) {
      NativeImage image = new NativeImage(96, 96, false);
      float pixel = 0.020833334F;

      for (int py = 0; py < 96; py++) {
         for (int px = 0; px < 96; px++) {
            float x = (px + 0.5F) / 96.0F * 2.0F - 1.0F;
            float y = 1.0F - (py + 0.5F) / 96.0F * 2.0F;
            float d = shape.distance(x, y);
            float body = Math.clamp(0.5F - d / (pixel * 1.5F), 0.0F, 1.0F);
            float halo = d > 0.0F ? 0.42F * (float)Math.exp(-Math.pow(d / glow, 2.0) * 2.2F) : 0.0F;
            float alpha = Math.clamp(Math.max(body, halo), 0.0F, 1.0F);
            float depth = Math.clamp(-d / 0.35F, 0.0F, 1.0F);
            int value = Math.round(255.0F * (body > 0.5F ? 0.84F + 0.16F * depth : 1.0F));
            image.setPixelABGR(px, py, Math.round(alpha * 255.0F) << 24 | value << 16 | value << 8 | value);
         }
      }

      return image;
   }

   private static float starDistance(float x, float y, float r, float inner) {
      float k1x = 0.809017F;
      float k1y = -0.58778524F;
      x = Math.abs(x);
      float dot1 = Math.max(0.809017F * x + -0.58778524F * y, 0.0F) * 2.0F;
      x -= dot1 * 0.809017F;
      y -= dot1 * -0.58778524F;
      float dot2 = Math.max(-0.809017F * x + -0.58778524F * y, 0.0F) * 2.0F;
      x -= dot2 * -0.809017F;
      y -= dot2 * -0.58778524F;
      x = Math.abs(x);
      y -= r;
      float bax = inner * 0.58778524F;
      float bay = inner * 0.809017F - 1.0F;
      float h = Math.clamp((x * bax + y * bay) / (bax * bax + bay * bay), 0.0F, r);
      float dx = x - bax * h;
      float dy = y - bay * h;
      return (float)Math.sqrt(dx * dx + dy * dy) * Math.signum(y * bax - x * bay);
   }

   private static float heartDistance(float x, float y) {
      x = Math.abs(x);
      if (y + x > 1.0F) {
         float dx = x - 0.25F;
         float dy = y - 0.75F;
         return (float)Math.sqrt(dx * dx + dy * dy) - 0.35355338F;
      } else {
         float ax = x;
         float ay = y - 1.0F;
         float m = 0.5F * Math.max(x + y, 0.0F);
         float bx = x - m;
         float by = y - m;
         return (float)Math.sqrt(Math.min(ax * ax + ay * ay, bx * bx + by * by)) * Math.signum(x - y);
      }
   }

   private static Identifier register(String name, NativeImage image) {
      Identifier id = Identifier.fromNamespaceAndPath("rose", "dynamic/" + name);
      Minecraft.getInstance().getTextureManager().register(id, new RoseTexture(name, image));
      return id;
   }

   private interface Shape {
      float distance(float var1, float var2);
   }
}
