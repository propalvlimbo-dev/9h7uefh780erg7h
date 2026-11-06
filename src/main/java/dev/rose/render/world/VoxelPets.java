package dev.rose.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.rose.util.ColorUtil;
import net.minecraft.util.Mth;
import org.joml.Vector3f;


public final class VoxelPets {
   private static final int DARK = -15330278;
   private static final int WHITE = -1;
   private final VertexConsumer consumer;
   private final PoseStack pose;
   private final int light;
   private final float alpha;

   private VoxelPets(VertexConsumer consumer, PoseStack pose, int light, float alpha) {
      this.consumer = consumer;
      this.pose = pose;
      this.light = light;
      this.alpha = alpha;
   }

   public static void draw(
      VoxelPets.Kind kind, VertexConsumer consumer, PoseStack pose, int light, float time, float speed, float joy, int base, int second, float alpha
   ) {
      VoxelPets pet = new VoxelPets(consumer, pose, light, alpha);
      pose.pushPose();
      pose.scale(0.05F, 0.05F, 0.05F);
      if (joy > 0.01F) {
         pose.translate(0.0F, Math.abs(Mth.sin(joy * (float) Math.PI * 2.0F)) * 4.0F * joy, 0.0F);
         pose.mulPose(Axis.YP.rotation(joy * joy * (float) (Math.PI * 2)));
      }

      switch (kind) {
         case KING:
            pet.king(time, speed, base, second);
            break;
         case CAT:
            pet.cat(time, speed, base);
            break;
         case DRAGON:
            pet.dragon(time, speed, base, second);
            break;
         case GHOST:
            pet.ghost(time, base);
            break;
         case BEE:
            pet.bee(time, base);
            break;
         case SLIME:
            pet.slime(time, base);
      }

      pose.popPose();
   }

   private static int shade(int color, float amount) {
      return amount >= 0.0F ? ColorUtil.lerp(color, -1, amount) : ColorUtil.lerp(color, -16777216, -amount);
   }

   private void box(float x0, float y0, float z0, float x1, float y1, float z1, int color) {
      this.box(x0, y0, z0, x1, y1, z1, color, 1.0F);
   }

   private void box(float x0, float y0, float z0, float x1, float y1, float z1, int color, float opacity) {
      int c = ColorUtil.withAlpha(color, Math.clamp(Math.round(ColorUtil.alpha(color) * opacity * this.alpha), 0, 255));
      if (ColorUtil.alpha(c) >= 3) {
         PoseStack.Pose last = this.pose.last();
         Vector3f a = new Vector3f(x0, y0, z0);
         Vector3f b = new Vector3f(x1, y0, z0);
         Vector3f c2 = new Vector3f(x1, y0, z1);
         Vector3f d = new Vector3f(x0, y0, z1);
         Vector3f e = new Vector3f(x0, y1, z0);
         Vector3f f = new Vector3f(x1, y1, z0);
         Vector3f g = new Vector3f(x1, y1, z1);
         Vector3f h = new Vector3f(x0, y1, z1);
         Geometry.quad(this.consumer, last, e, h, g, f, c, this.light);
         Geometry.quad(this.consumer, last, a, b, c2, d, c, this.light);
         Geometry.quad(this.consumer, last, b, f, g, c2, c, this.light);
         Geometry.quad(this.consumer, last, a, d, h, e, c, this.light);
         Geometry.quad(this.consumer, last, d, c2, g, h, c, this.light);
         Geometry.quad(this.consumer, last, a, e, f, b, c, this.light);
      }
   }

   private void pair(float x0, float y0, float z0, float x1, float y1, float z1, int color) {
      this.box(x0, y0, z0, x1, y1, z1, color);
      this.box(-x1, y0, z0, -x0, y1, z1, color);
   }

   private void eyes(float time, float x0, float y0, float x1, float y1, float z, int color, boolean glint) {
      float cycle = time % 3.7F;
      boolean blink = cycle > 3.55F;
      float middle = (y0 + y1) / 2.0F;
      if (blink) {
         this.pair(x0, middle - 0.2F, z, x1, middle + 0.2F, z + 0.35F, -15330278);
      } else {
         this.pair(x0, y0, z, x1, y1, z + 0.35F, color);
         if (glint) {
            this.pair(x0 + 0.15F, y1 - 0.75F, z + 0.35F, x0 + 0.7F, y1 - 0.2F, z + 0.45F, -1);
         }
      }
   }

   private void king(float time, float speed, int base, int second) {
      float breathe = 1.0F + 0.04F * Mth.sin(time * 3.6F);
      this.pose.pushPose();
      this.pose.scale(1.0F / Mth.sqrt(breathe), breathe, 1.0F / Mth.sqrt(breathe));
      int light = shade(base, 0.45F);
      int darker = shade(base, -0.22F);
      this.box(-4.0F, 0.0F, -3.5F, 4.0F, 8.0F, 3.5F, base);
      this.box(-2.6F, 1.0F, 3.5F, 2.6F, 4.8F, 3.85F, light);
      this.pair(2.3F, 2.7F, 3.5F, 3.5F, 3.7F, 3.8F, shade(base, 0.25F));
      this.eyes(time, 1.1F, 5.0F, 2.6F, 7.0F, 3.5F, -15330278, true);
      this.box(-0.8F, 3.5F, 3.85F, 0.8F, 4.0F, 4.05F, -15330278);

      for (int side = -1; side <= 1; side += 2) {
         this.pose.pushPose();
         this.pose.translate(side * 4, 5.2F, 0.0F);
         this.pose.mulPose(Axis.ZP.rotation(side * (0.5F + 0.35F * Mth.sin(time * 3.2F + side))));
         this.box(side > 0 ? 0.0F : -1.7F, -2.8F, -1.0F, side > 0 ? 1.7F : 0.0F, 0.2F, 1.0F, darker);
         this.pose.popPose();
         this.pose.pushPose();
         this.pose.translate(side * 1.9F, 0.0F, 0.2F);
         this.pose.mulPose(Axis.XP.rotation(Mth.sin(time * (3.0F + 5.0F * speed) + side * 1.5F) * (0.25F + 0.5F * speed)));
         this.box(-1.1F, -1.5F, -1.3F, 1.1F, 0.0F, 1.5F, darker);
         this.pose.popPose();
      }

      int gold = -14275;
      int goldDark = shade(gold, -0.2F);
      this.pose.pushPose();
      this.pose.translate(0.0F, 8.0F, 0.0F);
      this.pose.mulPose(Axis.ZP.rotation(Mth.sin(time * 1.9F) * 0.07F));
      this.pose.mulPose(Axis.XP.rotation(Mth.sin(time * 1.3F + 1.0F) * 0.04F));
      this.box(-3.6F, 0.0F, -3.1F, 3.6F, 1.2F, 3.1F, gold);

      for (int z = -1; z <= 1; z += 2) {
         float z0 = z > 0 ? 2.1F : -3.1F;
         float z1 = z > 0 ? 3.1F : -2.1F;
         this.pair(2.4F, 1.2F, z0, 3.6F, 3.0F, z1, gold);
         this.box(-0.6F, 1.2F, z0, 0.6F, 3.8F, z1, gold);
      }

      this.pair(2.6F, 1.2F, -0.6F, 3.6F, 2.6F, 0.6F, goldDark);
      this.box(-0.55F, 0.25F, 3.1F, 0.55F, 0.95F, 3.4F, ColorUtil.lerp(second, -49830, 0.5F));
      this.pose.popPose();
      this.pose.popPose();
   }

   private void cat(float time, float speed, int base) {
      int white = shade(base, 0.8F);
      int darker = shade(base, -0.25F);
      int pink = -25928;
      this.box(-2.5F, 2.5F, -5.0F, 2.5F, 7.0F, 3.0F, base);
      this.box(-1.8F, 2.4F, 2.2F, 1.8F, 5.4F, 3.2F, white);

      for (int i = 0; i < 4; i++) {
         float x = i % 2 == 0 ? -1.5F : 1.5F;
         float z = i < 2 ? 1.8F : -3.6F;
         this.pose.pushPose();
         this.pose.translate(x, 2.6F, z);
         this.pose.mulPose(Axis.XP.rotation(Mth.sin(time * (2.4F + 6.0F * speed) + i * 1.57F) * (0.3F + 0.45F * speed)));
         this.box(-0.75F, -2.6F, -0.8F, 0.75F, 0.0F, 0.8F, base);
         this.box(-0.75F, -2.6F, -0.8F, 0.75F, -1.9F, 0.85F, white);
         this.pose.popPose();
      }

      this.pose.pushPose();
      this.pose.translate(0.0F, 7.0F, 3.2F);
      this.pose.mulPose(Axis.ZP.rotation(Mth.sin(time * 0.9F) * 0.12F));
      this.pose.mulPose(Axis.XP.rotation(Mth.sin(time * 1.3F) * 0.06F));
      this.box(-3.0F, -1.6F, -0.8F, 3.0F, 3.4F, 4.2F, base);
      this.pair(1.2F, 3.4F, 0.6F, 3.0F, 5.4F, 2.1F, base);
      this.pair(1.6F, 3.4F, 2.1F, 2.6F, 4.7F, 2.3F, pink);
      this.box(-1.4F, -0.9F, 4.2F, 1.4F, 0.7F, 5.0F, white);
      this.box(-0.4F, 0.3F, 5.0F, 0.4F, 0.8F, 5.2F, pink);
      this.eyes(time, 1.1F, 0.9F, 2.3F, 2.3F, 4.2F, -8585334, false);
      if (time % 3.7F <= 3.55F) {
         this.pair(1.55F, 0.9F, 4.55F, 1.85F, 2.3F, 4.62F, -15330278);
      }

      this.pair(1.4F, 0.1F, 4.6F, 3.8F, 0.25F, 4.7F, white);
      this.pose.popPose();
      this.pose.pushPose();
      this.pose.translate(0.0F, 6.2F, -5.0F);
      this.pose.mulPose(Axis.XP.rotation(-0.9F));

      for (int i = 0; i < 5; i++) {
         this.pose.mulPose(Axis.YP.rotation(Mth.sin(time * 2.6F - i * 0.7F) * 0.32F));
         this.pose.mulPose(Axis.XP.rotation(0.16F));
         this.box(-0.7F, -0.7F, -2.2F, 0.7F, 0.7F, 0.0F, i == 4 ? white : (i % 2 == 0 ? base : darker));
         this.pose.translate(0.0F, 0.0F, -2.2F);
      }

      this.pose.popPose();
   }

   private void dragon(float time, float speed, int base, int second) {
      int belly = shade(base, 0.5F);
      int darker = shade(base, -0.3F);
      int bone = -989486;
      int eye = ColorUtil.lerp(second, -15811, 0.6F);
      this.box(-2.0F, 3.0F, -4.0F, 2.0F, 7.0F, 3.0F, base);
      this.box(-1.5F, 2.5F, -3.4F, 1.5F, 3.3F, 2.6F, belly);

      for (int i = 0; i < 3; i++) {
         this.box(-0.4F, 7.0F, -3.0F + i * 2.2F, 0.4F, 8.1F, -2.0F + i * 2.2F, belly);
      }

      this.pair(0.9F, 1.0F, 1.2F, 2.0F, 3.0F, 2.4F, darker);
      this.pair(0.9F, 1.0F, -3.4F, 2.0F, 3.0F, -2.2F, darker);
      this.pose.pushPose();
      this.pose.translate(0.0F, 6.4F, 2.6F);
      this.pose.mulPose(Axis.XP.rotation(-0.35F + Mth.sin(time * 1.7F) * 0.08F));
      this.box(-1.5F, -0.6F, 0.0F, 1.5F, 2.0F, 2.6F, base);
      this.pose.translate(0.0F, 1.2F, 2.2F);
      this.pose.mulPose(Axis.XP.rotation(0.4F + Mth.sin(time * 1.7F + 1.0F) * 0.06F));
      this.box(-2.0F, -1.2F, 0.0F, 2.0F, 1.8F, 4.0F, base);
      this.box(-1.5F, -1.2F, 4.0F, 1.5F, 0.6F, 6.2F, base);
      this.box(-1.5F, -1.5F, 3.2F, 1.5F, -1.2F, 6.0F, belly);
      this.pair(0.7F, 1.8F, 0.2F, 1.5F, 3.9F, 1.0F, bone);
      this.pair(1.3F, 0.2F, 2.6F, 2.05F, 1.2F, 3.6F, eye);
      this.pair(0.5F, 0.3F, 6.2F, 0.9F, 0.6F, 6.3F, -15330278);
      this.pose.popPose();
      float flap = Mth.sin(time * (5.5F + 4.0F * speed)) * 0.7F;

      for (int side = -1; side <= 1; side += 2) {
         this.pose.pushPose();
         this.pose.translate(side * 1.8F, 6.8F, 0.4F);
         this.pose.mulPose(Axis.ZP.rotation(side * (0.25F + flap)));
         this.pose.mulPose(Axis.YP.rotation(side * -0.25F));
         float x0 = side > 0 ? 0.0F : -7.5F;
         float x1 = side > 0 ? 7.5F : 0.0F;
         this.box(x0, 0.1F, 1.4F, x1, 0.8F, 2.2F, darker);
         this.box(x0, 0.2F, -3.2F, x1, 0.55F, 1.4F, shade(second, -0.1F), 0.92F);
         this.box(side > 0 ? 5.0F : -7.5F, 0.2F, -4.4F, side > 0 ? 7.5F : -5.0F, 0.55F, -3.2F, shade(second, -0.1F), 0.92F);
         this.pose.popPose();
      }

      this.pose.pushPose();
      this.pose.translate(0.0F, 5.0F, -4.0F);

      for (int i = 0; i < 5; i++) {
         float thick = 1.5F - i * 0.22F;
         this.pose.mulPose(Axis.YP.rotation(Mth.sin(time * 2.2F - i * 0.8F) * 0.26F));
         this.pose.mulPose(Axis.XP.rotation(i == 0 ? 0.25F : -0.08F));
         this.box(-thick, -thick, -2.3F, thick, thick, 0.0F, i % 2 == 0 ? base : darker);
         this.pose.translate(0.0F, 0.0F, -2.3F);
      }

      this.box(-1.6F, -0.3F, -1.8F, 1.6F, 0.3F, 0.2F, belly);
      this.pose.popPose();
   }

   private void ghost(float time, int base) {
      int body = shade(base, 0.72F);
      int hem = shade(base, 0.45F);
      float pulse = 0.78F + 0.14F * Mth.sin(time * 1.7F);
      this.pose.pushPose();
      this.pose.mulPose(Axis.ZP.rotation(Mth.sin(time * 1.5F) * 0.1F));
      this.eyes(time, 1.0F, 6.0F, 2.4F, 8.4F, 3.05F, -15330278, false);
      this.box(-0.9F, 3.8F, 3.05F, 0.9F, 5.1F, 3.3F, -15330278);
      this.box(-3.5F, 2.0F, -3.0F, 3.5F, 10.0F, 3.0F, body, pulse);
      this.box(-2.6F, 10.0F, -2.2F, 2.6F, 11.2F, 2.2F, body, pulse);

      for (int i = 0; i < 4; i++) {
         float x = -3.5F + i * 1.75F;
         float wave = Mth.sin(time * 3.2F + i * 1.4F);
         this.box(x, 0.2F + wave * 0.7F, -3.0F, x + 1.75F, 2.0F, 3.0F, i % 2 == 0 ? hem : body, pulse);
      }

      for (int side = -1; side <= 1; side += 2) {
         this.pose.pushPose();
         this.pose.translate(side * 3.5F, 6.5F, 0.5F);
         this.pose.mulPose(Axis.ZP.rotation(side * (0.7F + 0.3F * Mth.sin(time * 2.4F + side))));
         this.box(side > 0 ? 0.0F : -2.6F, -1.0F, -1.0F, side > 0 ? 2.6F : 0.0F, 1.0F, 1.0F, body, pulse);
         this.pose.popPose();
      }

      this.pose.popPose();
   }

   private void bee(float time, int base) {
      int stripe = ColorUtil.lerp(base, -14019, 0.86F);
      int dark = -14476268;
      this.pose.pushPose();
      this.pose.mulPose(Axis.XP.rotation(Mth.sin(time * 2.1F) * 0.12F));

      for (int i = 0; i < 4; i++) {
         this.box(-2.5F, 2.0F, -4 + i * 2, 2.5F, 7.0F, -2 + i * 2, i % 2 == 0 ? dark : stripe);
      }

      this.box(-2.2F, 2.5F, 4.0F, 2.2F, 6.5F, 6.4F, dark);
      this.pair(0.8F, 4.3F, 6.4F, 1.9F, 5.7F, 6.6F, -1);
      this.pair(1.1F, 4.6F, 6.6F, 1.6F, 5.2F, 6.68F, -15330278);
      this.pair(0.6F, 6.5F, 5.0F, 0.95F, 8.6F, 5.35F, dark);
      this.box(-0.4F, 3.9F, -5.3F, 0.4F, 4.7F, -4.0F, dark);

      for (int i = 0; i < 3; i++) {
         this.pair(1.2F, 0.9F, -2.2F + i * 2, 1.7F, 2.0F, -1.7F + i * 2, dark);
      }

      for (int side = -1; side <= 1; side += 2) {
         this.pose.pushPose();
         this.pose.translate(side * 1, 7.0F, 1.0F);
         this.pose.mulPose(Axis.ZP.rotation(side * (0.45F + 0.6F * Mth.sin(time * 42.0F))));
         this.box(side > 0 ? 0.0F : -5.5F, 0.0F, -1.6F, side > 0 ? 5.5F : 0.0F, 0.3F, 2.4F, -1378561, 0.55F);
         this.pose.popPose();
      }

      this.pose.popPose();
   }

   private void slime(float time, int base) {
      float stretch = 1.0F + 0.16F * Mth.sin(time * 4.2F);
      float wide = 1.0F / Mth.sqrt(stretch);
      this.pose.pushPose();
      this.pose.scale(wide, stretch, wide);
      this.box(-2.6F, 1.6F, -2.6F, 2.6F, 6.6F, 2.6F, shade(base, -0.15F));
      this.eyes(time, 1.2F, 5.0F, 2.8F, 7.0F, 4.5F, -15330278, true);
      this.box(-1.0F, 3.0F, 4.5F, 1.0F, 3.6F, 4.8F, -15330278);
      this.box(-4.5F, 0.0F, -4.5F, 4.5F, 9.0F, 4.5F, shade(base, 0.2F), 0.5F);
      this.pose.popPose();
   }

   public enum Kind {
      KING,
      CAT,
      DRAGON,
      GHOST,
      BEE,
      SLIME;
   }
}
