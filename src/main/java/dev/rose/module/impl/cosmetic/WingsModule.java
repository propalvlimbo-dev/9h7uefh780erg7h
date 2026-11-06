package dev.rose.module.impl.cosmetic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.Theme;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.Geometry;
import dev.rose.render.world.life.Motion;
import dev.rose.render.world.life.Noise;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


public final class WingsModule extends CosmeticModule {
   private final ChoiceSetting<WingsModule.Style> style = this.add(new ChoiceSetting<>("style", WingsModule.Style.ANGEL));
   private final NumberSetting size = this.add(new NumberSetting("size", 100.0, 50.0, 200.0, 5.0, "rose.unit.percent"));
   private final NumberSetting flapSpeed;
   private final NumberSetting flapStrength;
   private final NumberSetting spread;
   private final BoolSetting foldSneak;
   private final BoolSetting airBoost;
   private final NumberSetting offsetY;
   private final NumberSetting offsetZ;
   private final NumberSetting glow;
   private float flapPhase;
   private float lastTime;
   private float airTime;
   private float airBlend;
   private float foldBlend;
   private float liftBlend;
   private float speedBlend;
   private float swayBlend;
   private static final int MEMBRANE_STEPS = 40;
   private static final boolean DEBUG_MEMBRANE = Boolean.getBoolean("rose.wings.debug");
   private static final float ARM_END = 0.28F;
   private static final float FOREARM_END = 0.62F;
   private static final float BLEND = 0.09F;

   public WingsModule() {
      super("wings");
      this.section("motion", 57776);
      this.flapSpeed = this.add(new NumberSetting("flap_speed", 100.0, 0.0, 300.0, 5.0, "rose.unit.percent"));
      this.flapStrength = this.add(new NumberSetting("flap_strength", 100.0, 0.0, 200.0, 5.0, "rose.unit.percent"));
      this.spread = this.add(new NumberSetting("spread", 65.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.foldSneak = this.add(new BoolSetting("fold_sneak", true));
      this.airBoost = this.add(new BoolSetting("air_boost", true));
      this.section("placement", 57633);
      this.offsetY = this.add(new NumberSetting("offset_y", 0.0, -20.0, 30.0, 1.0, ""));
      this.offsetZ = this.add(new NumberSetting("offset_z", 0.0, -10.0, 30.0, 1.0, ""));
      this.glow = this.add(new NumberSetting("glow", 45.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.lastTime = -1.0F;
   }

   @Override
   public int icon() {
      return 57776;
   }

   @Override
   protected boolean alive() {
      return true;
   }

   private static float approach(float value, float target, float rate, float dt) {
      return value + (target - value) * (1.0F - (float)Math.exp(-rate * dt));
   }

   public void render(PoseStack pose, MultiBufferSource buffers, int light, AbstractClientPlayer player, float age) {
      float time = Theme.time();
      float dt = this.lastTime < 0.0F ? 0.0F : Math.clamp(time - this.lastTime, 0.0F, 0.1F);
      this.lastTime = time;
      float scale = this.size.floatValue() / 100.0F;
      boolean offGround = !player.onGround() && !player.isPassenger() && !player.isInWater() && !player.isVisuallySwimming();
      this.airTime = offGround ? this.airTime + dt : 0.0F;
      boolean airborne = this.airTime > 0.1F;
      this.airBlend = approach(this.airBlend, this.airBoost.isOn() && airborne ? 1.0F : 0.0F, airborne ? 5.0F : 7.0F, dt);
      this.foldBlend = approach(this.foldBlend, this.foldSneak.isOn() && player.isCrouching() && !airborne ? 1.0F : 0.0F, 10.0F, dt);
      Vec3 motion = player.getDeltaMovement();
      this.liftBlend = approach(this.liftBlend, Math.clamp((float)(-motion.y) * 1.6F, -0.2F, 1.0F) * (offGround ? 1 : 0), 6.0F, dt);
      this.speedBlend = approach(
         this.speedBlend,
         Math.clamp((float)Math.sqrt(motion.x * motion.x + motion.z * motion.z) * 3.2F, 0.0F, 1.0F),
         6.0F,
         dt
      );
      this.swayBlend = approach(this.swayBlend, Math.clamp(Motion.yawRate() / 150.0F, -1.0F, 1.0F), 8.0F, dt);
      float speed = this.flapSpeed.floatValue()
         / 100.0F
         * Mth.lerp(this.airBlend, 1.0F, 1.9F)
         * (this.style.get() == WingsModule.Style.BUTTERFLY ? 2.6F : 1.0F);
      float joints = this.flapStrength.floatValue()
         / 100.0F
         * Mth.lerp(this.foldBlend, 1.0F, 0.25F)
         * Mth.lerp(this.airBlend, 0.7F, 1.0F);
      float amplitude = joints * Mth.lerp(this.airBlend, 1.0F, 1.2F);
      this.flapPhase = (this.flapPhase + dt * (float) (Math.PI * 2) * 0.55F * speed) % 402.12387F;
      float phase = this.flapPhase;
      float flap = Mth.sin(phase) * amplitude;
      float rest = Mth.lerp(this.foldBlend, Mth.lerp(this.spread.floatValue() / 100.0F, 78.0F, 16.0F), 82.0F)
         - 14.0F * this.liftBlend * (1.0F - this.foldBlend)
         + this.wobble(time, 4.0F, 3.5F);
      float open = this.style.get() == WingsModule.Style.BUTTERFLY ? rest - 30.0F * flap : rest - (flap > 0.0F ? 20 : 11) * flap;
      float sway = this.swayBlend * 18.0F * this.life();
      float tiltBack = 14.0F + 6.0F * flap + 12.0F * this.speedBlend * (1.0F - this.foldBlend) + Math.abs(sway) * 0.5F;
      float membrane = membraneLayer(player);

      for (int side = -1; side <= 1; side += 2) {
         WingsModule.Frame frame = new WingsModule.Frame(side, scale, open + side * sway, tiltBack);
         frame.phase = phase;
         frame.flap = flap;
         frame.membrane = membrane;
         frame.shoulder = Mth.lerp(this.foldBlend, 34.0F, 8.0F)
            + 16.0F * flap
            + 14.0F * this.liftBlend
            + 9.0F * this.airBlend * (1.0F - this.foldBlend);
         frame.elbow = Mth.lerp(this.foldBlend, -18.0F, -52.0F) + 18.0F * Mth.sin(phase - 0.7F) * joints + 6.0F * this.liftBlend;
         frame.wrist = Mth.lerp(this.foldBlend, -12.0F, -40.0F) + 22.0F * Mth.sin(phase - 1.4F) * joints + 5.0F * this.liftBlend;
         switch ((WingsModule.Style)this.style.get()) {
            case ANGEL:
               this.angel(pose, buffers, light, frame, time, false);
               break;
            case DEMON:
               this.demon(pose, buffers, light, frame, time);
               break;
            case BUTTERFLY:
               this.butterfly(pose, buffers, light, frame, time);
               break;
            case CRYSTAL:
               this.crystal(pose, buffers, light, frame, time);
               break;
            case ENERGY:
               this.angel(pose, buffers, light, frame, time, true);
         }
      }
   }

   private float glowStrength() {
      return this.glow.floatValue() / 100.0F;
   }

   private WingsModule.Point2 boneAt(WingsModule.Frame frame, float t) {
      WingsModule.Point2 root = new WingsModule.Point2(0.0F, 0.0F);
      WingsModule.Point2 elbow = frame.elbowPoint();
      WingsModule.Point2 wrist = frame.wristPoint();
      if (t < 0.28F) {
         return root.lerp(elbow, t / 0.28F);
      } else {
         return t < 0.62F ? elbow.lerp(wrist, (t - 0.28F) / 0.34F) : wrist.lerp(frame.tipPoint(), (t - 0.62F) / 0.38F);
      }
   }

   private WingsModule.Point2 edgeAt(WingsModule.Frame frame, float t) {
      float hand = frame.shoulder + frame.elbow + frame.wrist;
      float fan = 1.0F + 0.22F * frame.flap;
      float armK = t / 0.28F;
      float armAngle = -100.0F + 6.0F * armK;
      float armLength = 0.26F + 0.08F * armK;
      float foreK = (t - 0.28F) / 0.34F;
      float foreAngle = -80.0F + 22.0F * foreK - 6.0F * frame.flap;
      float foreLength = 0.38F + 0.08F * foreK;
      float handK = Math.max(0.0F, (t - 0.62F) / 0.38F);
      float handAngle = hand - (6.0F + (1.0F - handK) * 52.0F) * fan;
      float handLength = 0.36F + 0.34F * (float)Math.pow(handK, 0.7);
      float toFore = ramp(t, 0.19F, 0.37F);
      float toHand = ramp(t, 0.53F, 0.71000004F);
      float angle = Mth.lerp(toHand, Mth.lerp(toFore, armAngle, foreAngle), handAngle);
      float length = Mth.lerp(toHand, Mth.lerp(toFore, armLength, foreLength), handLength);
      return this.boneAt(frame, t).plus(length * 0.93F, angle);
   }

   private static float ramp(float x, float a, float b) {
      float k = Math.clamp((x - a) / (b - a), 0.0F, 1.0F);
      return k * k * (3.0F - 2.0F * k);
   }

   private void underWing(VertexConsumer consumer, PoseStack.Pose pose, WingsModule.Frame frame, float time, int light, float layer) {
      for (int i = 0; i < 40; i++) {
         float t0 = i / 40.0F;
         float t1 = (i + 1) / 40.0F;
         WingsModule.Point2 bone0 = this.boneAt(frame, t0);
         WingsModule.Point2 bone1 = this.boneAt(frame, t1);
         Vector3f inner0 = frame.at(bone0, layer);
         Vector3f inner1 = frame.at(bone1, layer);
         Vector3f outer1 = frame.at(bone1.lerp(this.edgeAt(frame, t1), cover(t1)), layer);
         Vector3f outer0 = frame.at(bone0.lerp(this.edgeAt(frame, t0), cover(t0)), layer);
         Geometry.quad(consumer, pose, inner0, inner1, outer1, outer0, this.underShade(t0, time), light);
      }
   }

   private static float cover(float t) {
      return Math.min(1.0F, t / 0.15F) * Math.min(1.0F, (1.0F - t) / 0.13F);
   }

   private static float membraneLayer(AbstractClientPlayer player) {
      return behind(player) ? -1.0F : 5.0F;
   }

   private static boolean behind(AbstractClientPlayer player) {
      Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
      if (camera == null) {
         return true;
      }

      float yaw = player.yBodyRot * (float) (Math.PI / 180.0);
      double backX = Mth.sin(yaw);
      double backZ = -Mth.cos(yaw);
      Vec3 toCamera = camera.position().subtract(player.getX(), player.getY(), player.getZ());
      return toCamera.x * backX + toCamera.z * backZ > 0.0;
   }

   private int underShade(float along, float time) {
      if (DEBUG_MEMBRANE) {
         return -57280;
      }

      float dark = 0.54F - 0.3F * along;
      int base = ColorUtil.lerp(this.rgb(along * 0.85F, time), -14670802, dark);
      return ColorUtil.withAlpha(base, Math.clamp(Math.round(248.0F * this.opacity()), 0, 255));
   }

   private List<WingsModule.Feather> feathers(WingsModule.Frame frame) {
      List<WingsModule.Feather> list = new ArrayList<>();
      WingsModule.Point2 root = new WingsModule.Point2(0.0F, 0.0F);
      WingsModule.Point2 elbow = frame.elbowPoint();
      WingsModule.Point2 wrist = frame.wristPoint();
      WingsModule.Point2 tip = frame.tipPoint();
      float hand = frame.shoulder + frame.elbow + frame.wrist;
      float fan = 1.0F + 0.22F * frame.flap;

      for (int i = 0; i < 16; i++) {
         float t = i / 15.0F;
         WingsModule.Point2 base = wrist.lerp(tip, t * 0.95F);
         float angle = hand - (6.0F + (1.0F - t) * 52.0F) * fan;
         list.add(new WingsModule.Feather(base, angle, 0.36F + 0.34F * (float)Math.pow(t, 0.7), 0.1F + 0.05F * t, 0.0F, 0.62F + 0.38F * t));
      }

      for (int i = 0; i < 14; i++) {
         float t = i / 13.0F;
         WingsModule.Point2 base = elbow.lerp(wrist, t);
         float angle = -80.0F + 22.0F * t - 6.0F * frame.flap;
         list.add(new WingsModule.Feather(base, angle, 0.38F + 0.08F * t, 0.12F, 1.0F, 0.3F + 0.32F * t));
      }

      for (int i = 0; i < 7; i++) {
         float t = i / 6.0F;
         WingsModule.Point2 base = root.lerp(elbow, 0.25F + 0.75F * t);
         list.add(new WingsModule.Feather(base, -100.0F + 6.0F * t, 0.26F + 0.08F * t, 0.11F, 2.0F, 0.1F + 0.2F * t));
      }

      for (int i = 0; i < 18; i++) {
         float t = i / 17.0F;
         WingsModule.Point2 base = root.lerp(wrist, 0.08F + 0.92F * t);
         base = new WingsModule.Point2(base.u, base.v - 0.015F);
         list.add(new WingsModule.Feather(base, -72.0F + 12.0F * t, 0.17F + 0.04F * t, 0.095F, 3.0F, 0.05F + 0.6F * t));
      }

      for (int i = 0; i < 9; i++) {
         float t = i / 8.0F;
         WingsModule.Point2 base = root.lerp(elbow, t * 0.9F);
         list.add(new WingsModule.Feather(new WingsModule.Point2(base.u, base.v + 0.02F), -58.0F + 10.0F * t, 0.11F, 0.085F, 4.0F, t * 0.1F));
      }

      return list;
   }

   private void angel(PoseStack pose, MultiBufferSource buffers, int light, WingsModule.Frame frame, float time, boolean energy) {
      PoseStack.Pose last = pose.last();
      List<WingsModule.Feather> list = this.feathers(frame);
      float strength = this.glowStrength();
      if (!energy) {
         this.underWing(buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.white())), last, frame, time, light, frame.membrane);
         VertexConsumer consumer = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.feather()));

         for (WingsModule.Feather feather : list) {
            float flutter = Mth.sin(time * 5.0F + feather.along * 17.0F + frame.side) * 2.5F
               + Noise.value(feather.along * 6.1F, time * 1.7F, frame.side * 3.3F) * 6.0F * this.life();
            WingsModule.Point2 mid = feather.base.plus(feather.length * 0.5F, feather.angle + flutter);
            WingsModule.Point2 end = mid.plus(feather.length * 0.5F, feather.angle + flutter - 7.0F);
            float perp = (feather.angle + 90.0F) * (float) (Math.PI / 180.0);
            float halfWidth = feather.width * 0.5F;
            float du = Mth.cos(perp) * halfWidth;
            float dv = Mth.sin(perp) * halfWidth;
            int baseColor = this.shade(feather, time, 0.93F);
            int tipColor = ColorUtil.withAlpha(
               ColorUtil.lerp(this.rgb(feather.along, time), -1, feather.layer == 0.0F ? 0.08F : 0.2F), ColorUtil.alpha(baseColor)
            );
            Vector3f b0 = frame.at(new WingsModule.Point2(feather.base.u - du * 0.7F, feather.base.v - dv * 0.7F), feather.layer);
            Vector3f b1 = frame.at(new WingsModule.Point2(feather.base.u + du * 0.7F, feather.base.v + dv * 0.7F), feather.layer);
            Vector3f m0 = frame.at(new WingsModule.Point2(mid.u - du, mid.v - dv), feather.layer);
            Vector3f m1 = frame.at(new WingsModule.Point2(mid.u + du, mid.v + dv), feather.layer);
            Vector3f e0 = frame.at(new WingsModule.Point2(end.u - du * 0.9F, end.v - dv * 0.9F), feather.layer);
            Vector3f e1 = frame.at(new WingsModule.Point2(end.u + du * 0.9F, end.v + dv * 0.9F), feather.layer);
            int midColor = ColorUtil.lerp(baseColor, tipColor, 0.5F);
            Geometry.quadUv(consumer, last, b0, b1, m1, m0, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.5F, 0.0F, 0.5F, baseColor, baseColor, midColor, midColor, light);
            Geometry.quadUv(consumer, last, m0, m1, e1, e0, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F, 1.0F, 0.0F, 1.0F, midColor, midColor, tipColor, tipColor, light);
         }
      }

      if (energy || !(strength <= 0.01F)) {
         VertexConsumer lightBuffer = buffers.getBuffer(RoseRenderTypes.GLOW);

         for (WingsModule.Feather feather : list) {
            if (energy || feather.layer == 0.0F) {
               WingsModule.Point2 end = feather.base.plus(feather.length, feather.angle);
               float flow = 0.55F + 0.45F * Mth.sin(time * 6.0F - feather.along * 10.0F);
               float width = (energy ? 0.022F : 0.012F) * (feather.layer == 0.0F ? 1.0F : 0.8F);
               int start = this.color(feather.along, time, (energy ? 0.25F : 0.0F) * (0.5F + strength));
               int finish = this.color(feather.along + 0.1F, time, (energy ? 0.95F : 0.8F * strength) * flow);
               WingsModule.Point2 from = energy ? feather.base : feather.base.plus(feather.length * 0.55F, feather.angle);
               this.crossBeam(lightBuffer, last, frame, from, end, feather.layer, width, start, finish);
            }
         }
      }
   }

   private int shade(WingsModule.Feather feather, float time, float alpha) {
      int base = this.rgb(feather.along * 0.85F, time);
      float light = feather.layer >= 3.0F ? 0.28F : (feather.layer == 2.0F ? 0.18F : 0.1F);
      return ColorUtil.withAlpha(ColorUtil.lerp(base, -1, light), Math.clamp(Math.round(255.0F * alpha * this.opacity()), 0, 255));
   }

   private void crossBeam(
      VertexConsumer consumer,
      PoseStack.Pose pose,
      WingsModule.Frame frame,
      WingsModule.Point2 from,
      WingsModule.Point2 to,
      float layer,
      float width,
      int colorFrom,
      int colorTo
   ) {
      Vector3f a = frame.at(from, layer + 0.5F);
      Vector3f b = frame.at(to, layer + 0.5F);
      Vector3f along = new Vector3f(b).sub(a);
      if (!(along.lengthSquared() < 1.0E-8F)) {
         Vector3f inPlane = new Vector3f(along).cross(frame.normal).normalize(width * frame.scale);
         Vector3f outOfPlane = new Vector3f(frame.normal).mul(width * frame.scale);
         int clearFrom = colorFrom & 16777215;
         int clearTo = colorTo & 16777215;

         for (Vector3f offset : new Vector3f[]{inPlane, outOfPlane}) {
            Geometry.glowQuad(
               consumer,
               pose,
               new Vector3f(a).sub(offset),
               new Vector3f(a),
               new Vector3f(b),
               new Vector3f(b).sub(offset),
               clearFrom,
               colorFrom,
               colorTo,
               clearTo
            );
            Geometry.glowQuad(
               consumer,
               pose,
               new Vector3f(a),
               new Vector3f(a).add(offset),
               new Vector3f(b).add(offset),
               new Vector3f(b),
               colorFrom,
               clearFrom,
               clearTo,
               colorTo
            );
         }
      }
   }

   private void demon(PoseStack pose, MultiBufferSource buffers, int light, WingsModule.Frame frame, float time) {
      PoseStack.Pose last = pose.last();
      WingsModule.Point2 root = new WingsModule.Point2(0.0F, 0.0F);
      WingsModule.Point2 elbow = frame.elbowPoint();
      WingsModule.Point2 wrist = frame.wristPoint();
      float hand = frame.shoulder + frame.elbow;
      float lag = Mth.sin(frame.phase - 1.1F) * 10.0F * Math.abs(frame.flap);
      float[] offsets = new float[]{8.0F, -24.0F, -56.0F, -92.0F};
      float[] lengths = new float[]{0.64F, 0.7F, 0.6F, 0.46F};
      WingsModule.Point2[] fingers = new WingsModule.Point2[4];

      for (int i = 0; i < 4; i++) {
         fingers[i] = wrist.plus(lengths[i], hand + offsets[i] - lag * (i / 3.0F));
      }

      WingsModule.Point2 lower = new WingsModule.Point2(0.03F, -0.52F);
      VertexConsumer skin = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.membrane()));
      float minV = -0.9F;
      float maxV = 0.9F;
      float maxU = 1.3F;

      for (int i = 0; i < 3; i++) {
         WingsModule.Point2 a = fingers[i];
         WingsModule.Point2 b = fingers[i + 1];
         WingsModule.Point2 middle = a.lerp(b, 0.5F).lerp(wrist, 0.28F);
         this.membrane(skin, last, frame, wrist, a, middle, i / 3.0F, time, light, minV, maxV, maxU);
         this.membrane(skin, last, frame, wrist, middle, b, (i + 0.5F) / 3.0F, time, light, minV, maxV, maxU);
      }

      WingsModule.Point2 lastMiddle = fingers[3].lerp(lower, 0.5F).lerp(elbow, 0.22F);
      this.membrane(skin, last, frame, wrist, fingers[3], lastMiddle, 0.9F, time, light, minV, maxV, maxU);
      this.membrane(skin, last, frame, wrist, lastMiddle, elbow, 0.8F, time, light, minV, maxV, maxU);
      this.membrane(skin, last, frame, elbow, lastMiddle, lower, 0.6F, time, light, minV, maxV, maxU);
      this.membrane(skin, last, frame, root, elbow, lower, 0.3F, time, light, minV, maxV, maxU);
      VertexConsumer bones = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.white()));
      int boneColor = ColorUtil.withAlpha(ColorUtil.lerp(this.rgb(0.0F, time), -15594482, 0.72F), Math.round(255.0F * this.opacity()));
      this.bone(bones, last, frame, root, elbow, 0.036F, boneColor, light);
      this.bone(bones, last, frame, elbow, wrist, 0.03F, boneColor, light);

      for (WingsModule.Point2 finger : fingers) {
         this.bone(bones, last, frame, wrist, finger, 0.018F, boneColor, light);
      }

      WingsModule.Point2 claw = wrist.plus(0.1F, hand + 38.0F);
      this.bone(bones, last, frame, wrist, claw, 0.02F, ColorUtil.lerp(boneColor, -1646378, 0.35F), light);
      float strength = this.glowStrength();
      if (!(strength <= 0.01F)) {
         VertexConsumer lightBuffer = buffers.getBuffer(RoseRenderTypes.GLOW);

         for (int i = 0; i < 4; i++) {
            int c = this.color(i / 3.0F, time, strength * (0.6F + 0.4F * Mth.sin(time * 3.0F + i)));
            this.crossBeam(lightBuffer, last, frame, fingers[i].lerp(wrist, 0.35F), fingers[i], 1.0F, 0.012F, c & 16777215, c);
         }
      }
   }

   private void membrane(
      VertexConsumer consumer,
      PoseStack.Pose pose,
      WingsModule.Frame frame,
      WingsModule.Point2 a,
      WingsModule.Point2 b,
      WingsModule.Point2 c,
      float along,
      float time,
      int light,
      float minV,
      float maxV,
      float maxU
   ) {
      int color = ColorUtil.withAlpha(ColorUtil.lerp(this.rgb(along, time), -15069676, 0.35F), Math.clamp(Math.round(235.0F * this.opacity()), 0, 255));
      Vector3f pa = frame.at(a, 0.0F);
      Vector3f pb = frame.at(b, 0.0F);
      Vector3f pc = frame.at(c, 0.0F);
      Geometry.quadUv(
         consumer,
         pose,
         pa,
         pb,
         pc,
         pc,
         a.u / maxU,
         (a.v - minV) / (maxV - minV),
         b.u / maxU,
         (b.v - minV) / (maxV - minV),
         c.u / maxU,
         (c.v - minV) / (maxV - minV),
         c.u / maxU,
         (c.v - minV) / (maxV - minV),
         color,
         color,
         color,
         color,
         light
      );
   }

   private void bone(
      VertexConsumer consumer, PoseStack.Pose pose, WingsModule.Frame frame, WingsModule.Point2 from, WingsModule.Point2 to, float width, int color, int light
   ) {
      Vector3f a = frame.at(from, 0.6F);
      Vector3f b = frame.at(to, 0.6F);
      Vector3f along = new Vector3f(b).sub(a);
      if (!(along.lengthSquared() < 1.0E-8F)) {
         Vector3f side = new Vector3f(along).cross(frame.normal).normalize(width * frame.scale * 0.5F);
         Vector3f depth = new Vector3f(frame.normal).mul(width * frame.scale * 0.5F);
         Vector3f tipSide = new Vector3f(side).mul(0.35F);
         Vector3f tipDepth = new Vector3f(depth).mul(0.35F);
         Geometry.quad(
            consumer, pose, new Vector3f(a).sub(side), new Vector3f(a).add(side), new Vector3f(b).add(tipSide), new Vector3f(b).sub(tipSide), color, light
         );
         Geometry.quad(
            consumer, pose, new Vector3f(a).sub(depth), new Vector3f(a).add(depth), new Vector3f(b).add(tipDepth), new Vector3f(b).sub(tipDepth), color, light
         );
      }
   }

   private void butterfly(PoseStack pose, MultiBufferSource buffers, int light, WingsModule.Frame frame, float time) {
      PoseStack.Pose last = pose.last();
      VertexConsumer consumer = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.butterfly()));
      this.lobe(consumer, last, frame, time, light, 4.0F, 108.0F, 0.66F, true);
      this.lobe(consumer, last, frame, time, light, -104.0F, -6.0F, 0.48F, false);
      float strength = this.glowStrength();
      if (!(strength <= 0.01F)) {
         VertexConsumer lightBuffer = buffers.getBuffer(RoseRenderTypes.GLOW);
         int segments = 16;

         for (int i = 0; i < segments; i++) {
            float t0 = (float)i / segments;
            float t1 = (float)(i + 1) / segments;
            WingsModule.Point2 a = lobePoint(4.0F + t0 * 104.0F, 0.66F, true);
            WingsModule.Point2 b = lobePoint(4.0F + t1 * 104.0F, 0.66F, true);
            this.crossBeam(lightBuffer, last, frame, a, b, 1.0F, 0.01F, this.color(t0, time, strength * 0.7F), this.color(t1, time, strength * 0.7F));
         }
      }
   }

   private static WingsModule.Point2 lobePoint(float degrees, float length, boolean upper) {
      float t = upper ? (degrees - 4.0F) / 104.0F : (degrees + 104.0F) / 98.0F;
      float shape = upper
         ? 0.42F + 0.58F * (float)Math.pow(Math.max(0.0F, Mth.sin(t * (float) Math.PI)), 0.75)
         : 0.38F + 0.62F * (float)Math.pow(Math.max(0.0F, Mth.sin(t * (float) Math.PI)), 0.9);
      float wobble = 1.0F + 0.08F * Mth.sin(t * (float) Math.PI * 3.0F);
      return new WingsModule.Point2(0.0F, 0.0F).plus(length * shape * wobble, degrees);
   }

   private void lobe(VertexConsumer consumer, PoseStack.Pose pose, WingsModule.Frame frame, float time, int light, float from, float to, float length, boolean upper) {
      int segments = 18;
      float[] rings = new float[]{0.0F, 0.45F, 0.8F, 1.0F};

      for (int i = 0; i < segments; i++) {
         float t0 = (float)i / segments;
         float t1 = (float)(i + 1) / segments;
         WingsModule.Point2 edge0 = lobePoint(from + (to - from) * t0, length, upper);
         WingsModule.Point2 edge1 = lobePoint(from + (to - from) * t1, length, upper);

         for (int r = 0; r < rings.length - 1; r++) {
            WingsModule.Point2 a = scaled(edge0, rings[r]);
            WingsModule.Point2 b = scaled(edge1, rings[r]);
            WingsModule.Point2 c = scaled(edge1, rings[r + 1]);
            WingsModule.Point2 d = scaled(edge0, rings[r + 1]);
            int c0 = this.color(t0 * 0.6F + (upper ? 0.0F : 0.4F), time, 0.96F);
            int c1 = this.color(t1 * 0.6F + (upper ? 0.0F : 0.4F), time, 0.96F);
            Geometry.quadUv(
               consumer,
               pose,
               frame.at(a, 0.0F),
               frame.at(b, 0.0F),
               frame.at(c, 0.0F),
               frame.at(d, 0.0F),
               t0,
               rings[r],
               t1,
               rings[r],
               t1,
               rings[r + 1],
               t0,
               rings[r + 1],
               c0,
               c1,
               c1,
               c0,
               light
            );
         }
      }
   }

   private static WingsModule.Point2 scaled(WingsModule.Point2 point, float factor) {
      return new WingsModule.Point2(point.u * factor, point.v * factor);
   }

   private void crystal(PoseStack pose, MultiBufferSource buffers, int light, WingsModule.Frame frame, float time) {
      PoseStack.Pose last = pose.last();
      WingsModule.Point2 root = new WingsModule.Point2(0.0F, 0.0F);
      WingsModule.Point2 tip = frame.tipPoint();
      WingsModule.Point2 wrist = frame.wristPoint();
      int count = 7;
      WingsModule.Point2[] bases = new WingsModule.Point2[count];
      WingsModule.Point2[] tips = new WingsModule.Point2[count];
      WingsModule.Point2[] lefts = new WingsModule.Point2[count];
      WingsModule.Point2[] rights = new WingsModule.Point2[count];
      float[] floats = new float[count];

      for (int i = 0; i < count; i++) {
         float t = (float)i / (count - 1);
         WingsModule.Point2 base = t < 0.6F ? root.lerp(wrist, t / 0.6F) : wrist.lerp(tip, (t - 0.6F) / 0.4F);
         float angle = Mth.lerp(t, frame.shoulder + 26.0F, -74.0F);
         float length = 0.36F + 0.34F * Mth.sin(t * (float) Math.PI * 0.9F + 0.3F);
         float bob = Mth.sin(time * 2.2F + i * 0.9F) * 0.015F;
         base = new WingsModule.Point2(base.u + bob + this.wobble(time, i, 0.012F), base.v + bob + this.wobble(time, i + 9, 0.012F));
         WingsModule.Point2 end = base.plus(length, angle);
         float perp = angle + 90.0F;
         bases[i] = base;
         tips[i] = end;
         lefts[i] = base.plus(length * 0.38F, angle).plus(0.07F, perp);
         rights[i] = base.plus(length * 0.38F, angle).plus(0.07F, perp + 180.0F);
         floats[i] = t;
      }

      VertexConsumer solid = buffers.getBuffer(RoseRenderTypes.lit(CosmeticTextures.white()));

      for (int i = 0; i < count; i++) {
         Vector3f b = frame.at(bases[i], 0.0F);
         Vector3f l = frame.at(lefts[i], 0.0F);
         Vector3f r = frame.at(rights[i], 0.0F);
         Vector3f e = frame.at(tips[i], 0.0F);
         Vector3f center = frame.at(bases[i].lerp(tips[i], 0.4F), 0.0F);
         Vector3f front = new Vector3f(center).add(new Vector3f(frame.normal).mul(0.035F * frame.scale));
         Vector3f back = new Vector3f(center).sub(new Vector3f(frame.normal).mul(0.035F * frame.scale));
         int bright = this.color(floats[i], time, 0.72F);
         int dim = ColorUtil.withAlpha(ColorUtil.lerp(this.rgb(floats[i], time), -16777216, 0.25F), ColorUtil.alpha(bright));
         int pale = ColorUtil.withAlpha(ColorUtil.lerp(this.rgb(floats[i], time), -1, 0.35F), ColorUtil.alpha(bright));
         Geometry.triangle(solid, last, b, l, front, bright, light);
         Geometry.triangle(solid, last, l, e, front, pale, light);
         Geometry.triangle(solid, last, e, r, front, bright, light);
         Geometry.triangle(solid, last, r, b, front, dim, light);
         Geometry.triangle(solid, last, l, b, back, dim, light);
         Geometry.triangle(solid, last, e, l, back, bright, light);
         Geometry.triangle(solid, last, r, e, back, dim, light);
         Geometry.triangle(solid, last, b, r, back, pale, light);
      }

      float strength = this.glowStrength();
      if (!(strength <= 0.01F)) {
         VertexConsumer lightBuffer = buffers.getBuffer(RoseRenderTypes.GLOW);

         for (int i = 0; i < count; i++) {
            float shimmer = Math.max(0.0F, Mth.sin(time * 2.6F - floats[i] * 5.0F));
            int c = this.color(floats[i], time, strength * (0.35F + 0.65F * shimmer));
            this.crossBeam(lightBuffer, last, frame, bases[i], lefts[i], 1.0F, 0.008F, c, c);
            this.crossBeam(lightBuffer, last, frame, lefts[i], tips[i], 1.0F, 0.008F, c, ColorUtil.withAlpha(c, Math.min(255, ColorUtil.alpha(c) + 60)));
            this.crossBeam(lightBuffer, last, frame, tips[i], rights[i], 1.0F, 0.008F, c, c);
            this.crossBeam(lightBuffer, last, frame, rights[i], bases[i], 1.0F, 0.008F, c, c);
         }
      }
   }

   private record Feather(WingsModule.Point2 base, float angle, float length, float width, float layer, float along) {
   }

   private final class Frame {
      final int side;
      final float scale;
      final Vector3f root;
      final Vector3f outward;
      final Vector3f up;
      final Vector3f normal;
      float shoulder;
      float elbow;
      float wrist;
      float flap;
      float phase;
      float membrane;

      Frame(int side, float scale, float open, float tiltBack) {
         this.side = side;
         this.scale = scale;
         this.root = new Vector3f(side * 0.07F, 0.13F + WingsModule.this.offsetY.floatValue() / 100.0F, 0.15F + WingsModule.this.offsetZ.floatValue() / 100.0F);
         float openRadians = open * (float) (Math.PI / 180.0);
         float tilt = tiltBack * (float) (Math.PI / 180.0);
         this.outward = new Vector3f(side * Mth.cos(openRadians), 0.0F, Mth.sin(openRadians));
         this.up = new Vector3f(0.0F, -Mth.cos(tilt), Mth.sin(tilt));
         Vector3f n = new Vector3f(this.outward).cross(this.up);
         if (n.z < 0.0F) {
            n.negate();
         }

         this.normal = n.normalize();
      }

      Vector3f at(WingsModule.Point2 point, float layer) {
         return new Vector3f(this.root)
            .add(new Vector3f(this.outward).mul(point.u * this.scale))
            .add(new Vector3f(this.up).mul(point.v * this.scale))
            .add(new Vector3f(this.normal).mul(layer * 0.009F));
      }

      WingsModule.Point2 elbowPoint() {
         return new WingsModule.Point2(0.0F, 0.0F).plus(0.26F, this.shoulder);
      }

      WingsModule.Point2 wristPoint() {
         return this.elbowPoint().plus(0.34F, this.shoulder + this.elbow);
      }

      WingsModule.Point2 tipPoint() {
         return this.wristPoint().plus(0.28F, this.shoulder + this.elbow + this.wrist);
      }
   }

   private record Point2(float u, float v) {
      WingsModule.Point2 plus(float length, float degrees) {
         float radians = degrees * (float) (Math.PI / 180.0);
         return new WingsModule.Point2(this.u + Mth.cos(radians) * length, this.v + Mth.sin(radians) * length);
      }

      WingsModule.Point2 lerp(WingsModule.Point2 other, float t) {
         return new WingsModule.Point2(this.u + (other.u - this.u) * t, this.v + (other.v - this.v) * t);
      }
   }

   public enum Style {
      ANGEL,
      DEMON,
      BUTTERFLY,
      CRYSTAL,
      ENERGY;
   }
}
