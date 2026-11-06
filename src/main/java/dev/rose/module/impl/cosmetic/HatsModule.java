package dev.rose.module.impl.cosmetic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.Theme;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.Geometry;
import dev.rose.render.world.life.Motion;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ColorsSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import org.joml.Vector3f;


public final class HatsModule extends CosmeticModule {
   private static final float TOP = -0.5F;
   private final ChoiceSetting<HatsModule.Hat> hat = this.add(new ChoiceSetting<>("hat", HatsModule.Hat.CHINA));
   private final NumberSetting size = this.add(new NumberSetting("size", 100.0, 50.0, 200.0, 5.0, "rose.unit.percent"));
   private final NumberSetting height = this.add(new NumberSetting("height", 0.0, -30.0, 60.0, 1.0, ""));
   private final NumberSetting tilt = this.add(new NumberSetting("tilt", 0.0, -35.0, 35.0, 1.0, "rose.unit.degrees"));
   private final NumberSetting spin = this.add(new NumberSetting("spin", 40.0, -360.0, 360.0, 5.0, "rose.unit.degrees_per_second"))
      .visibleWhen(() -> this.hat.get() == HatsModule.Hat.CHINA || this.hat.get() == HatsModule.Hat.HALO || this.hat.get() == HatsModule.Hat.CROWN);
   private final NumberSetting radius;
   private final NumberSetting depth;
   private final BoolSetting rim;
   private final BoolSetting lines;
   private final BoolSetting doubleRing;
   private final BoolSetting pulse;
   private final NumberSetting glow;

   public HatsModule() {
      super("hats");
      this.section("shape", 58660);
      this.radius = this.add(new NumberSetting("radius", 100.0, 50.0, 170.0, 5.0, "rose.unit.percent"))
         .visibleWhen(() -> this.hat.get() == HatsModule.Hat.CHINA || this.hat.get() == HatsModule.Hat.HALO);
      this.depth = this.add(new NumberSetting("depth", 100.0, 20.0, 220.0, 5.0, "rose.unit.percent"))
         .visibleWhen(() -> this.hat.get() == HatsModule.Hat.CHINA);
      this.rim = this.add(new BoolSetting("rim", true)).visibleWhen(() -> this.hat.get() == HatsModule.Hat.CHINA || this.hat.get() == HatsModule.Hat.TOP_HAT);
      this.lines = this.add(new BoolSetting("lines", false)).visibleWhen(() -> this.hat.get() == HatsModule.Hat.CHINA);
      this.doubleRing = this.add(new BoolSetting("double_ring", false)).visibleWhen(() -> this.hat.get() == HatsModule.Hat.HALO);
      this.pulse = this.add(new BoolSetting("pulse", true)).visibleWhen(() -> this.hat.get() == HatsModule.Hat.HALO);
      this.glow = this.add(new NumberSetting("glow", 70.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
   }

   @Override
   public int icon() {
      return 57814;
   }

   @Override
   protected boolean alive() {
      return true;
   }

   public void render(PoseStack pose, MultiBufferSource buffers, int light, float age) {
      float time = Theme.time();
      float scale = this.size.floatValue() / 100.0F;
      pose.pushPose();
      pose.translate(0.0F, -0.5F - this.height.floatValue() / 100.0F, 0.0F);
      if (this.tilt.floatValue() != 0.0F) {
         pose.mulPose(Axis.XP.rotationDegrees(-this.tilt.floatValue()));
      }

      this.lean(pose, time);
      pose.scale(scale, scale, scale);
      switch ((HatsModule.Hat)this.hat.get()) {
         case CHINA:
            this.china(pose, buffers, time);
            break;
         case HALO:
            this.halo(pose, buffers, time);
            break;
         case CROWN:
            this.crown(pose, buffers, light, time);
            break;
         case HORNS:
            this.horns(pose, buffers, light, time);
            break;
         case EARS:
            this.ears(pose, buffers, light, time);
            break;
         case TOP_HAT:
            this.topHat(pose, buffers, light, time);
      }

      pose.popPose();
   }

   private void lean(PoseStack pose, float time) {
      float strength = this.life();
      if (!(strength <= 0.0F)) {
         float roll = Math.clamp(Motion.yawRate() / 220.0F, -1.0F, 1.0F) * 10.0F * strength + this.wobble(time, 5.0F, 1.6F);
         float nod = Math.clamp(Motion.pitchRate() / 220.0F, -1.0F, 1.0F) * 7.0F * strength + this.wobble(time, 6.0F, 1.2F);
         if (roll != 0.0F) {
            pose.mulPose(Axis.ZP.rotationDegrees(roll));
         }

         if (nod != 0.0F) {
            pose.mulPose(Axis.XP.rotationDegrees(nod));
         }
      }
   }

   private float glowStrength() {
      return this.glow.floatValue() / 100.0F;
   }

   private void china(PoseStack pose, MultiBufferSource buffers, float time) {
      float r = 0.62F * this.radius.floatValue() / 100.0F;
      float h = 0.24F * this.depth.floatValue() / 100.0F;
      float baseY = -0.035F;
      float apexY = baseY - h;
      float phase = time * this.spin.floatValue() / 360.0F;
      int segments = 72;
      PoseStack.Pose last = pose.last();
      VertexConsumer solid = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.white()));
      Vector3f apex = new Vector3f(0.0F, apexY, 0.0F);
      int apexColor = ColorUtil.withAlpha(ColorUtil.lerp(this.rgb(phase, time), -1, 0.35F), Math.round(235.0F * this.opacity()));

      for (int i = 0; i < segments; i++) {
         float t0 = (float)i / segments;
         float t1 = (float)(i + 1) / segments;
         double a0 = t0 * Math.PI * 2.0;
         double a1 = t1 * Math.PI * 2.0;
         Vector3f p0 = new Vector3f((float)Math.cos(a0) * r, baseY, (float)Math.sin(a0) * r);
         Vector3f p1 = new Vector3f((float)Math.cos(a1) * r, baseY, (float)Math.sin(a1) * r);
         int c0 = this.color(t0 + phase, time, 0.72F);
         int c1 = this.color(t1 + phase, time, 0.72F);
         Geometry.quadUv(solid, last, p0, p1, apex, apex, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, c0, c1, apexColor, apexColor, 15728880);
      }

      float strength = this.glowStrength();
      if (!(strength <= 0.01F) && (this.rim.isOn() || this.lines.isOn())) {
         VertexConsumer light = buffers.getBuffer(RoseRenderTypes.GLOW);
         if (this.rim.isOn()) {
            for (int i = 0; i < segments; i++) {
               float t0 = (float)i / segments;
               float t1 = (float)(i + 1) / segments;
               double a0 = t0 * Math.PI * 2.0;
               double a1 = t1 * Math.PI * 2.0;
               float c0 = (float)Math.cos(a0);
               float s0 = (float)Math.sin(a0);
               float c1 = (float)Math.cos(a1);
               float s1 = (float)Math.sin(a1);
               int bright0 = ColorUtil.lerp(this.color(t0 + phase, time, strength), -1, 0.25F);
               int bright1 = this.color(t1 + phase, time, strength);
               int clear0 = bright0 & 16777215;
               int clear1 = bright1 & 16777215;
               float inner = r - 0.035F;
               float outer = r + 0.02F;
               Geometry.glowQuad(
                  light,
                  last,
                  new Vector3f(c0 * inner, baseY - 0.002F, s0 * inner),
                  new Vector3f(c0 * r, baseY - 0.002F, s0 * r),
                  new Vector3f(c1 * r, baseY - 0.002F, s1 * r),
                  new Vector3f(c1 * inner, baseY - 0.002F, s1 * inner),
                  clear0,
                  bright0,
                  bright1,
                  clear1
               );
               Geometry.glowQuad(
                  light,
                  last,
                  new Vector3f(c0 * r, baseY - 0.002F, s0 * r),
                  new Vector3f(c0 * outer, baseY - 0.002F, s0 * outer),
                  new Vector3f(c1 * outer, baseY - 0.002F, s1 * outer),
                  new Vector3f(c1 * r, baseY - 0.002F, s1 * r),
                  bright0,
                  clear0,
                  clear1,
                  bright1
               );
            }
         }

         if (this.lines.isOn()) {
            int spokes = 12;

            for (int i = 0; i < spokes; i++) {
               float t = (float)i / spokes;
               double angle = (t + phase) * Math.PI * 2.0;
               Vector3f end = new Vector3f((float)Math.cos(angle) * r, baseY - 0.003F, (float)Math.sin(angle) * r);
               Vector3f across = new Vector3f((float)(-Math.sin(angle)), 0.0F, (float)Math.cos(angle)).mul(0.012F);
               int c = this.color(t + phase, time, strength * 0.8F);
               Geometry.glowQuad(
                  light,
                  last,
                  new Vector3f(apex).sub(across),
                  new Vector3f(apex).add(across),
                  new Vector3f(end).add(across),
                  new Vector3f(end).sub(across),
                  c & 16777215,
                  c & 16777215,
                  c,
                  c
               );
            }
         }
      }
   }

   private void halo(PoseStack pose, MultiBufferSource buffers, float time) {
      float r = 0.25F * this.radius.floatValue() / 100.0F;
      float breath = this.pulse.isOn() ? 0.8F + 0.2F * Mth.sin(time * 2.6F) : 1.0F;
      float spinAngle = time * this.spin.floatValue() * (float) (Math.PI / 180.0);
      pose.pushPose();
      pose.translate(0.0F, -0.2F + Mth.sin(time * 1.7F) * 0.012F, 0.0F);
      pose.mulPose(Axis.XP.rotationDegrees(-10.0F));
      PoseStack.Pose last = pose.last();
      VertexConsumer ring = buffers.getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.ring()));
      float strength = 0.55F + 0.45F * this.glowStrength();
      Geometry.flatSprite(ring, last, 0.0F, 0.0F, 0.0F, r / 0.8F, spinAngle, this.color(0.0F, time, strength * breath));
      Geometry.flatSprite(ring, last, 0.0F, 0.0F, 0.0F, r / 0.8F * 1.02F, -spinAngle * 0.6F, this.color(0.5F, time, strength * 0.6F * breath));
      if (this.doubleRing.isOn()) {
         Geometry.flatSprite(ring, last, 0.0F, -0.06F, 0.0F, r * 0.72F / 0.8F, -spinAngle, this.color(0.3F, time, strength * breath));
      }

      VertexConsumer core = buffers.getBuffer(RoseRenderTypes.GLOW);
      int segments = 56;

      for (int i = 0; i < segments; i++) {
         float t0 = (float)i / segments;
         float t1 = (float)(i + 1) / segments;
         double a0 = t0 * Math.PI * 2.0 + spinAngle;
         double a1 = t1 * Math.PI * 2.0 + spinAngle;
         float c0 = (float)Math.cos(a0);
         float s0 = (float)Math.sin(a0);
         float c1 = (float)Math.cos(a1);
         float s1 = (float)Math.sin(a1);
         int bright0 = ColorUtil.lerp(this.color(t0, time, 0.9F * breath), -1, 0.45F);
         int bright1 = ColorUtil.lerp(this.color(t1, time, 0.9F * breath), -1, 0.45F);
         float inner = r - 0.008F;
         float outer = r + 0.008F;
         Geometry.glowQuad(
            core,
            last,
            new Vector3f(c0 * inner, 0.0F, s0 * inner),
            new Vector3f(c0 * outer, 0.0F, s0 * outer),
            new Vector3f(c1 * outer, 0.0F, s1 * outer),
            new Vector3f(c1 * inner, 0.0F, s1 * inner),
            bright0,
            bright0,
            bright1,
            bright1
         );
      }

      pose.popPose();
   }

   private int gold(float position, float time) {
      return this.colorMode() == ColorsSetting.Mode.THEME ? ColorUtil.lerp(-15043, this.rgb(position, time), 0.18F) : this.rgb(position, time);
   }

   private void crown(PoseStack pose, MultiBufferSource buffers, int light, float time) {
      float r = 0.215F;
      float bottom = 0.005F;
      float bandTop = -0.075F;
      int segments = 32;
      int spikes = 8;
      float spinAngle = time * this.spin.floatValue() * 0.25F * (float) (Math.PI / 180.0);
      pose.pushPose();
      pose.mulPose(Axis.YP.rotation(spinAngle));
      PoseStack.Pose last = pose.last();
      VertexConsumer solid = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.white()));
      int alpha = Math.round(255.0F * this.opacity());

      for (int i = 0; i < segments; i++) {
         float t0 = (float)i / segments;
         float t1 = (float)(i + 1) / segments;
         double a0 = t0 * Math.PI * 2.0;
         double a1 = t1 * Math.PI * 2.0;
         float x0 = (float)Math.cos(a0) * r;
         float z0 = (float)Math.sin(a0) * r;
         float x1 = (float)Math.cos(a1) * r;
         float z1 = (float)Math.sin(a1) * r;
         int base = ColorUtil.withAlpha(this.gold(t0, time), alpha);
         int shade = ColorUtil.withAlpha(ColorUtil.lerp(this.gold(t0, time), -16777216, 0.3F), alpha);
         Geometry.quad(
            solid, last, new Vector3f(x0, bottom, z0), new Vector3f(x1, bottom, z1), new Vector3f(x1, bandTop, z1), new Vector3f(x0, bandTop, z0), base, light
         );
         float bulge = 1.06F;
         Geometry.quad(
            solid,
            last,
            new Vector3f(x0 * bulge, -0.025F, z0 * bulge),
            new Vector3f(x1 * bulge, -0.025F, z1 * bulge),
            new Vector3f(x1 * bulge, -0.05F, z1 * bulge),
            new Vector3f(x0 * bulge, -0.05F, z0 * bulge),
            shade,
            light
         );
      }

      for (int i = 0; i < spikes; i++) {
         double center = (Math.PI * 2) * (i + 0.5) / spikes;
         double halfWidth = Math.PI / spikes * 0.9;
         float tall = i % 2 == 0 ? 0.2F : 0.14F;
         Vector3f left = new Vector3f((float)Math.cos(center - halfWidth) * r, bandTop, (float)Math.sin(center - halfWidth) * r);
         Vector3f right = new Vector3f((float)Math.cos(center + halfWidth) * r, bandTop, (float)Math.sin(center + halfWidth) * r);
         Vector3f tip = new Vector3f((float)Math.cos(center) * r * 0.98F, bandTop - tall, (float)Math.sin(center) * r * 0.98F);
         Vector3f front = new Vector3f((float)Math.cos(center) * (r + 0.02F), bandTop - tall * 0.35F, (float)Math.sin(center) * (r + 0.02F));
         int base = ColorUtil.withAlpha(this.gold((float)i / spikes, time), alpha);
         Geometry.triangle(solid, last, left, front, tip, base, light);
         Geometry.triangle(solid, last, front, right, tip, ColorUtil.lerp(base, -16777216, 0.18F), light);
         Geometry.triangle(solid, last, right, left, tip, ColorUtil.lerp(base, -16777216, 0.35F), light);
      }

      VertexConsumer gems = buffers.getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
      float strength = 0.35F + 0.65F * this.glowStrength();

      for (int i = 0; i < spikes; i++) {
         double center = (Math.PI * 2) * (i + 0.5) / spikes;
         float tall = i % 2 == 0 ? 0.2F : 0.14F;
         float twinkle = 0.65F + 0.35F * Mth.sin(time * 3.0F + i * 1.3F);
         Vector3f tip = new Vector3f((float)Math.cos(center) * r * 0.98F, bandTop - tall - 0.012F, (float)Math.sin(center) * r * 0.98F);
         Vector3f outward = new Vector3f((float)(-Math.sin(center)), 0.0F, (float)Math.cos(center));
         Geometry.dotSprite(gems, last, tip, outward, new Vector3f(0.0F, -1.0F, 0.0F), 0.045F, this.color((float)i / spikes, time, strength * twinkle));
         double gemAngle = (Math.PI * 2) * i / spikes;
         Vector3f gem = new Vector3f((float)Math.cos(gemAngle) * (r * 1.07F + 0.004F), -0.0375F, (float)Math.sin(gemAngle) * (r * 1.07F + 0.004F));
         Vector3f gemSide = new Vector3f((float)(-Math.sin(gemAngle)), 0.0F, (float)Math.cos(gemAngle));
         Geometry.dotSprite(gems, last, gem, gemSide, new Vector3f(0.0F, -1.0F, 0.0F), 0.034F, this.color(0.5F + (float)i / spikes, time, strength));
      }

      pose.popPose();
   }

   private void horns(PoseStack pose, MultiBufferSource buffers, int light, float time) {
      PoseStack.Pose last = pose.last();
      VertexConsumer solid = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.white()));
      int along = 12;
      int around = 10;
      int alpha = Math.round(255.0F * this.opacity());

      for (int side = -1; side <= 1; side += 2) {
         Vector3f p0 = new Vector3f(side * 0.15F, 0.01F, -0.06F);
         Vector3f p1 = new Vector3f(side * 0.3F, -0.1F, -0.02F);
         Vector3f p2 = new Vector3f(side * 0.27F, -0.3F, 0.1F);
         Vector3f[][] rings = new Vector3f[along + 1][];

         for (int i = 0; i <= along; i++) {
            float t = (float)i / along;
            Vector3f center = bezier(p0, p1, p2, t);
            Vector3f tangent = bezierTangent(p0, p1, p2, t).normalize();
            Vector3f normal = new Vector3f(tangent).cross(0.0F, 0.0F, 1.0F);
            if (normal.lengthSquared() < 1.0E-6F) {
               normal.set(1.0F, 0.0F, 0.0F);
            }

            normal.normalize();
            Vector3f binormal = new Vector3f(tangent).cross(normal).normalize();
            float radiusAt = 0.058F * (1.0F - t) + 0.004F;
            rings[i] = new Vector3f[around];

            for (int j = 0; j < around; j++) {
               double angle = (Math.PI * 2) * j / around;
               rings[i][j] = new Vector3f(center)
                  .add(new Vector3f(normal).mul((float)Math.cos(angle) * radiusAt))
                  .add(new Vector3f(binormal).mul((float)Math.sin(angle) * radiusAt));
            }
         }

         for (int i = 0; i < along; i++) {
            float t0 = (float)i / along;
            float t1 = (float)(i + 1) / along;
            int c0 = ColorUtil.withAlpha(ColorUtil.lerp(ColorUtil.lerp(this.rgb(0.0F, time), -15463412, 0.55F), this.rgb(t0, time), t0), alpha);
            int c1 = ColorUtil.withAlpha(ColorUtil.lerp(ColorUtil.lerp(this.rgb(0.0F, time), -15463412, 0.55F), this.rgb(t1, time), t1), alpha);

            for (int j = 0; j < around; j++) {
               int k = (j + 1) % around;
               int ridge0 = i % 2 == 0 ? c0 : ColorUtil.lerp(c0, -16777216, 0.12F);
               int ridge1 = i % 2 == 0 ? c1 : ColorUtil.lerp(c1, -16777216, 0.12F);
               Geometry.quadUv(
                  solid,
                  last,
                  rings[i][j],
                  rings[i][k],
                  rings[i + 1][k],
                  rings[i + 1][j],
                  0.0F,
                  0.0F,
                  1.0F,
                  0.0F,
                  1.0F,
                  1.0F,
                  0.0F,
                  1.0F,
                  ridge0,
                  ridge0,
                  ridge1,
                  ridge1,
                  light
               );
            }
         }
      }
   }

   private static Vector3f bezier(Vector3f a, Vector3f b, Vector3f c, float t) {
      float u = 1.0F - t;
      return new Vector3f(a).mul(u * u).add(new Vector3f(b).mul(2.0F * u * t)).add(new Vector3f(c).mul(t * t));
   }

   private static Vector3f bezierTangent(Vector3f a, Vector3f b, Vector3f c, float t) {
      return new Vector3f(b).sub(a).mul(2.0F * (1.0F - t)).add(new Vector3f(c).sub(b).mul(2.0F * t));
   }

   private void ears(PoseStack pose, MultiBufferSource buffers, int light, float time) {
      VertexConsumer solid = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.white()));
      int alpha = Math.round(255.0F * this.opacity());
      int outer = ColorUtil.withAlpha(ColorUtil.lerp(this.rgb(0.0F, time), -14803422, 0.3F), alpha);
      int inner = ColorUtil.withAlpha(ColorUtil.lerp(this.rgb(0.5F, time), -10524, 0.55F), alpha);

      for (int side = -1; side <= 1; side += 2) {
         float cycle = (time + (side > 0 ? 1.7F : 0.0F)) % 4.3F;
         float twitch = cycle < 0.22F ? Mth.sin(cycle / 0.22F * (float) Math.PI) : 0.0F;
         pose.pushPose();
         pose.translate(side * 0.16F, 0.0F, -0.02F);
         pose.mulPose(Axis.ZP.rotationDegrees(side * (8.0F + 16.0F * twitch)));
         PoseStack.Pose last = pose.last();
         float w = 0.085F;
         float d = 0.035F;
         float h = 0.2F;
         Vector3f frontLeft = new Vector3f(-w, 0.01F, -d);
         Vector3f frontRight = new Vector3f(w, 0.01F, -d);
         Vector3f backLeft = new Vector3f(-w, 0.01F, d);
         Vector3f backRight = new Vector3f(w, 0.01F, d);
         Vector3f tip = new Vector3f(side * 0.02F, -h, 0.0F);
         Geometry.triangle(solid, last, frontLeft, frontRight, tip, outer, light);
         Geometry.triangle(solid, last, backRight, backLeft, tip, outer, light);
         Geometry.triangle(solid, last, backLeft, frontLeft, tip, ColorUtil.lerp(outer, -16777216, 0.15F), light);
         Geometry.triangle(solid, last, frontRight, backRight, tip, ColorUtil.lerp(outer, -16777216, 0.15F), light);
         Vector3f innerLeft = new Vector3f(-w * 0.55F, -0.012F, -d - 0.002F);
         Vector3f innerRight = new Vector3f(w * 0.55F, -0.012F, -d - 0.002F);
         Vector3f innerTip = new Vector3f(side * 0.014F, -h * 0.72F, -d * 0.45F);
         Geometry.triangle(solid, last, innerLeft, innerRight, innerTip, inner, light);
         pose.popPose();
      }
   }

   private void topHat(PoseStack pose, MultiBufferSource buffers, int light, float time) {
      PoseStack.Pose last = pose.last();
      VertexConsumer solid = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.white()));
      int alpha = Math.round(255.0F * this.opacity());
      int felt = ColorUtil.withAlpha(-14869214, alpha);
      int feltTop = ColorUtil.withAlpha(-14277076, alpha);
      int segments = 32;
      float brim = 0.36F;
      float crown = 0.21F;
      float crownTop = -0.36F;
      float brimY = 0.0F;
      float brimThickness = 0.022F;

      for (int i = 0; i < segments; i++) {
         float t0 = (float)i / segments;
         float t1 = (float)(i + 1) / segments;
         double a0 = t0 * Math.PI * 2.0;
         double a1 = t1 * Math.PI * 2.0;
         float c0 = (float)Math.cos(a0);
         float s0 = (float)Math.sin(a0);
         float c1 = (float)Math.cos(a1);
         float s1 = (float)Math.sin(a1);
         Geometry.quad(
            solid,
            last,
            new Vector3f(c0 * crown, brimY - brimThickness, s0 * crown),
            new Vector3f(c1 * crown, brimY - brimThickness, s1 * crown),
            new Vector3f(c1 * brim, brimY - brimThickness, s1 * brim),
            new Vector3f(c0 * brim, brimY - brimThickness, s0 * brim),
            feltTop,
            light
         );
         Geometry.quad(
            solid,
            last,
            new Vector3f(c0 * brim, brimY, s0 * brim),
            new Vector3f(c1 * brim, brimY, s1 * brim),
            new Vector3f(c1 * crown, brimY, s1 * crown),
            new Vector3f(c0 * crown, brimY, s0 * crown),
            felt,
            light
         );
         Geometry.quad(
            solid,
            last,
            new Vector3f(c0 * brim, brimY, s0 * brim),
            new Vector3f(c1 * brim, brimY, s1 * brim),
            new Vector3f(c1 * brim, brimY - brimThickness, s1 * brim),
            new Vector3f(c0 * brim, brimY - brimThickness, s0 * brim),
            felt,
            light
         );
         Geometry.quad(
            solid,
            last,
            new Vector3f(c0 * crown, brimY - brimThickness, s0 * crown),
            new Vector3f(c1 * crown, brimY - brimThickness, s1 * crown),
            new Vector3f(c1 * crown, crownTop, s1 * crown),
            new Vector3f(c0 * crown, crownTop, s0 * crown),
            felt,
            light
         );
         float band = crown + 0.004F;
         int ribbon = ColorUtil.withAlpha(this.rgb(t0, time), alpha);
         Geometry.quad(
            solid,
            last,
            new Vector3f(c0 * band, brimY - brimThickness - 0.004F, s0 * band),
            new Vector3f(c1 * band, brimY - brimThickness - 0.004F, s1 * band),
            new Vector3f(c1 * band, brimY - 0.1F, s1 * band),
            new Vector3f(c0 * band, brimY - 0.1F, s0 * band),
            ribbon,
            light
         );
         Geometry.quad(
            solid,
            last,
            new Vector3f(0.0F, crownTop, 0.0F),
            new Vector3f(c1 * crown, crownTop, s1 * crown),
            new Vector3f(c0 * crown, crownTop, s0 * crown),
            new Vector3f(c0 * crown, crownTop, s0 * crown),
            feltTop,
            light
         );
      }

      if (this.rim.isOn() && !(this.glowStrength() <= 0.01F)) {
         VertexConsumer glowBuffer = buffers.getBuffer(RoseRenderTypes.GLOW);

         for (int i = 0; i < segments; i++) {
            float t0 = (float)i / segments;
            float t1 = (float)(i + 1) / segments;
            double a0 = t0 * Math.PI * 2.0;
            double a1 = t1 * Math.PI * 2.0;
            float c0 = (float)Math.cos(a0);
            float s0 = (float)Math.sin(a0);
            float c1 = (float)Math.cos(a1);
            float s1 = (float)Math.sin(a1);
            int bright0 = this.color(t0, time, this.glowStrength() * 0.8F);
            int bright1 = this.color(t1, time, this.glowStrength() * 0.8F);
            float band = crown + 0.008F;
            Geometry.glowQuad(
               glowBuffer,
               last,
               new Vector3f(c0 * band, brimY - 0.035F, s0 * band),
               new Vector3f(c1 * band, brimY - 0.035F, s1 * band),
               new Vector3f(c1 * band, brimY - 0.075F, s1 * band),
               new Vector3f(c0 * band, brimY - 0.075F, s0 * band),
               bright0,
               bright1,
               bright1,
               bright0
            );
         }
      }
   }

   public enum Hat {
      CHINA,
      HALO,
      CROWN,
      HORNS,
      EARS,
      TOP_HAT;
   }
}
