package dev.rose.module.impl.cosmetic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.world.CosmeticContext;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.Geometry;
import dev.rose.render.world.life.Follow;
import dev.rose.render.world.life.Motion;
import dev.rose.render.world.life.Noise;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


public final class AuraModule extends CosmeticModule {
   private final ChoiceSetting<AuraModule.Style> style = this.add(new ChoiceSetting<>("style", AuraModule.Style.RING));
   private final NumberSetting radius;
   private final NumberSetting height;
   private final NumberSetting count;
   private final NumberSetting particle;
   private final NumberSetting speed;
   private final BoolSetting reverse;
   private final NumberSetting glow;
   private final BoolSetting firstPerson;
   private final List<AuraModule.Spark> sparks;
   private final Random random;
   private float sparkBudget;
   private final Follow anchor;

   public AuraModule() {
      super("aura");
      this.section("shape", 58660);
      this.radius = this.add(new NumberSetting("radius", 100.0, 30.0, 300.0, 5.0, "rose.unit.percent"));
      this.height = this.add(new NumberSetting("height", 100.0, 20.0, 300.0, 5.0, "rose.unit.percent"))
         .visibleWhen(() -> this.style.get() != AuraModule.Style.RING);
      this.count = this.add(new NumberSetting("count", 8.0, 1.0, 32.0, 1.0, ""))
         .visibleWhen(() -> this.style.get() != AuraModule.Style.RING && this.style.get() != AuraModule.Style.WAVES);
      this.particle = this.add(new NumberSetting("particle", 100.0, 20.0, 300.0, 5.0, "rose.unit.percent"))
         .visibleWhen(() -> this.style.get() != AuraModule.Style.RING && this.style.get() != AuraModule.Style.WAVES);
      this.section("motion", 57776);
      this.speed = this.add(new NumberSetting("speed", 100.0, 0.0, 300.0, 5.0, "rose.unit.percent"));
      this.reverse = this.add(new BoolSetting("reverse", false));
      this.glow = this.add(new NumberSetting("glow", 70.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.firstPerson = this.add(new BoolSetting("first_person", true));
      this.sparks = new ArrayList<>();
      this.random = new Random();
      this.anchor = new Follow(0.34, 0.6);
   }

   @Override
   public int icon() {
      return 58343;
   }

   @Override
   protected boolean alive() {
      return true;
   }

   @Override
   protected void onDisable() {
      this.sparks.clear();
      this.anchor.reset();
   }

   public void render(CosmeticContext ctx) {
      if (!ctx.firstPerson() || this.firstPerson.isOn()) {
         Vec3 feetWorld = ctx.player().getPosition(ctx.partialTick());
         feetWorld = this.lagged(ctx, feetWorld);
         Vector3f feet = ctx.local(feetWorld);
         float r = 0.75F * this.radius.floatValue() / 100.0F * (1.0F + 0.12F * this.life() * Motion.intensity());
         float h = 1.9F * this.height.floatValue() / 100.0F;
         float time = ctx.time();
         float direction = this.reverse.isOn() ? -1.0F : 1.0F;
         float motion = this.speed.floatValue() / 100.0F;
         float strength = 0.3F + 0.7F * this.glow.floatValue() / 100.0F;
         PoseStack pose = ctx.pose();
         pose.pushPose();
         pose.translate(feet.x, feet.y, feet.z);
         PoseStack.Pose last = pose.last();
         switch ((AuraModule.Style)this.style.get()) {
            case RING:
               this.ring(ctx, last, feet, r, time * motion * direction, time, strength);
               break;
            case WAVES:
               this.waves(ctx, last, feet, r, h, time * motion, time, strength);
               break;
            case ORBIT:
               this.orbit(ctx, last, feet, r, h, time * motion * direction, time, strength);
               break;
            case HELIX:
               this.helix(ctx, last, feet, r, h, time * motion * direction, time, strength);
               break;
            case SPARKS:
               this.sparks(ctx, last, feet, r, h, motion, time, strength);
               break;
            case PILLARS:
               this.pillars(ctx, last, feet, r, h, time * motion * direction, time, strength);
               break;
            case SURGE:
               this.surge(ctx, last, feet, r, h, time * motion * direction, time, strength);
               break;
            case FLAME:
               this.flame(ctx, last, feet, r, h, time * motion, time, strength);
         }

         pose.popPose();
         ctx.buffers().endBatch();
      }
   }

   private float size() {
      return this.particle.floatValue() / 100.0F;
   }

   private Vec3 lagged(CosmeticContext ctx, Vec3 feet) {
      if (this.life() <= 0.0F) {
         this.anchor.snap(feet.x, feet.y, feet.z);
         return feet;
      }

      this.anchor.step(ctx.delta(), feet.x, feet.y, feet.z);
      Vec3 offset = this.anchor.get().subtract(feet);
      double limit = 0.4 * this.life();
      if (offset.lengthSqr() > limit * limit) {
         offset = offset.normalize().scale(limit);
      }

      return feet.add(offset.x, offset.y * 0.5, offset.z);
   }

   private static float fade(Vector3f feet, Vector3f point) {
      return CosmeticContext.nearFade(new Vector3f(feet).add(point), 0.45F, 1.0F);
   }

   private void ring(CosmeticContext ctx, PoseStack.Pose last, Vector3f feet, float r, float spin, float time, float strength) {
      VertexConsumer sprites = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.ring()));
      float breath = (1.0F + 0.05F * Mth.sin(time * 2.2F)) * this.breath(time, 1.0F, 0.06F);
      Geometry.flatSprite(sprites, last, 0.0F, 0.03F, 0.0F, r * breath / 0.8F, spin * 0.8F, this.color(0.0F, time, 0.85F * strength));
      Geometry.flatSprite(sprites, last, 0.0F, 0.032F, 0.0F, r * 0.7F / 0.8F, -spin * 1.2F, this.color(0.5F, time, 0.45F * strength));
      VertexConsumer dots = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
      Geometry.flatSprite(dots, last, 0.0F, 0.028F, 0.0F, r * 1.15F, 0.0F, this.color(0.25F, time, 0.22F * strength));
      VertexConsumer glowBuffer = ctx.buffers().getBuffer(RoseRenderTypes.GLOW);
      int dashes = 28;

      for (int i = 0; i < dashes; i++) {
         float t = (float)i / dashes;
         double a0 = t * Math.PI * 2.0 + spin * 1.6F;
         double a1 = a0 + (Math.PI * 2) / dashes * 0.55;
         float inner = r * 1.08F * breath;
         float outer = r * 1.16F * breath;
         float c0 = (float)Math.cos(a0);
         float s0 = (float)Math.sin(a0);
         float c1 = (float)Math.cos(a1);
         float s1 = (float)Math.sin(a1);
         int c = this.color(t, time, 0.9F * strength);
         Geometry.glowQuad(
            glowBuffer,
            last,
            new Vector3f(c0 * inner, 0.035F, s0 * inner),
            new Vector3f(c0 * outer, 0.035F, s0 * outer),
            new Vector3f(c1 * outer, 0.035F, s1 * outer),
            new Vector3f(c1 * inner, 0.035F, s1 * inner),
            c & 16777215,
            c,
            c,
            c & 16777215
         );
      }
   }

   private void waves(CosmeticContext ctx, PoseStack.Pose last, Vector3f feet, float r, float h, float clock, float time, float strength) {
      VertexConsumer sprites = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.ring()));

      for (int k = 0; k < 3; k++) {
         float t = frac(clock * 0.5F + k / 3.0F);
         float alpha = (float)Math.pow(1.0F - t, 1.5);
         Geometry.flatSprite(sprites, last, 0.0F, 0.03F, 0.0F, r * (0.15F + 1.15F * t) / 0.8F, 0.0F, this.color(t, time, alpha * strength));
      }

      VertexConsumer glowBuffer = ctx.buffers().getBuffer(RoseRenderTypes.GLOW);

      for (int k = 0; k < 2; k++) {
         float t = frac(clock * 0.35F + k * 0.5F);
         float y = 0.05F + t * h;
         float alpha = (1.0F - t) * fade(feet, new Vector3f(0.0F, y, 0.0F));
         if (!(alpha <= 0.01F)) {
            int c = this.color(t, time, alpha * strength);
            Geometry.glowRing(glowBuffer, last, y, r * 0.78F, r * 0.86F, 48, c & 16777215, c);
            Geometry.glowRing(glowBuffer, last, y, r * 0.86F, r * 0.92F, 48, c, c & 16777215);
         }
      }
   }

   private void orbit(CosmeticContext ctx, PoseStack.Pose last, Vector3f feet, float r, float h, float clock, float time, float strength) {
      VertexConsumer dots = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
      int n = this.count.intValue();
      float size = 0.1F * this.size();

      for (int i = 0; i < n; i++) {
         float base = clock * 1.3F + i * (float) (Math.PI * 2) / n;
         float y = h * 0.45F + Mth.sin(time * 1.5F + i * 1.7F) * h * 0.14F;

         for (int j = 0; j < 9; j++) {
            float angle = base - j * 0.09F * Math.signum(clock == 0.0F ? 1.0F : clock);
            Vector3f point = new Vector3f(
               Mth.cos(angle) * r, y + Mth.sin(time * 1.5F + i * 1.7F - j * 0.05F) * 0.02F, Mth.sin(angle) * r
            );
            float tail = 1.0F - j / 9.0F;
            float alpha = tail * tail * fade(feet, point);
            if (!(alpha <= 0.01F)) {
               Geometry.dotSprite(
                  dots,
                  last,
                  point,
                  ctx.right(),
                  ctx.up(),
                  size * (0.45F + 0.55F * tail) * (j == 0 ? 1.3F : 1.0F),
                  this.color((float)i / n, time, alpha * strength)
               );
            }
         }
      }
   }

   private void helix(CosmeticContext ctx, PoseStack.Pose last, Vector3f feet, float r, float h, float clock, float time, float strength) {
      VertexConsumer dots = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
      int n = Math.max(6, this.count.intValue() * 3);
      float size = 0.07F * this.size();

      for (int strand = 0; strand < 2; strand++) {
         for (int j = 0; j < n; j++) {
            float f = (float)j / (n - 1);
            float angle = f * (float) Math.PI * 4.0F + clock * 1.6F + strand * (float) Math.PI;
            Vector3f point = new Vector3f(Mth.cos(angle) * r * 0.8F, 0.05F + f * h, Mth.sin(angle) * r * 0.8F);
            float alpha = (0.35F + 0.65F * Mth.sin(f * (float) Math.PI)) * fade(feet, point);
            if (!(alpha <= 0.01F)) {
               float twinkle = 0.7F + 0.3F * Mth.sin(time * 5.0F + j * 0.8F + strand);
               Geometry.dotSprite(dots, last, point, ctx.right(), ctx.up(), size * twinkle, this.color(f * 0.8F + strand * 0.2F, time, alpha * strength));
            }
         }
      }
   }

   private void sparks(CosmeticContext ctx, PoseStack.Pose last, Vector3f feet, float r, float h, float motion, float time, float strength) {
      float dt = ctx.delta();
      this.sparkBudget = this.sparkBudget + dt * this.count.intValue() * 4.0F * Math.max(0.2F, motion);

      while (this.sparkBudget >= 1.0F && this.sparks.size() < 400) {
         this.sparkBudget--;
         AuraModule.Spark spark = new AuraModule.Spark();
         spark.angle = this.random.nextFloat() * (float) (Math.PI * 2);
         spark.distance = r * (0.35F + 0.65F * this.random.nextFloat());
         spark.rise = (0.5F + this.random.nextFloat() * 0.7F) * Math.max(0.2F, motion) * h / 1.9F;
         spark.swirl = (this.random.nextFloat() - 0.5F) * 2.0F * (this.reverse.isOn() ? -1 : 1);
         spark.life = 1.0F;
         spark.size = 0.04F + this.random.nextFloat() * 0.05F;
         spark.hue = this.random.nextFloat();
         this.sparks.add(spark);
      }

      if (this.sparkBudget > 1.0F) {
         this.sparkBudget = 1.0F;
      }

      VertexConsumer dots = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));

      for (int i = this.sparks.size() - 1; i >= 0; i--) {
         AuraModule.Spark spark = this.sparks.get(i);
         spark.life -= dt / 1.4F;
         if (!(spark.life <= 0.0F) && !(spark.y > h)) {
            spark.y = spark.y + spark.rise * dt;
            spark.angle = spark.angle + spark.swirl * dt;
            Vector3f point = new Vector3f(Mth.cos(spark.angle) * spark.distance, spark.y, Mth.sin(spark.angle) * spark.distance);
            float alpha = Math.min(1.0F, spark.life * 2.5F) * Math.min(1.0F, (1.0F - spark.y / h) * 3.0F) * fade(feet, point);
            if (!(alpha <= 0.01F)) {
               Geometry.dotSprite(
                  dots,
                  last,
                  point,
                  ctx.right(),
                  ctx.up(),
                  spark.size * this.size() * (0.6F + 0.4F * spark.life),
                  this.color(spark.hue, time, alpha * strength)
               );
            }
         } else {
            this.sparks.remove(i);
         }
      }
   }

   private void pillars(CosmeticContext ctx, PoseStack.Pose last, Vector3f feet, float r, float h, float clock, float time, float strength) {
      int n = this.count.intValue();
      VertexConsumer glowBuffer = ctx.buffers().getBuffer(RoseRenderTypes.GLOW);

      for (int i = 0; i < n; i++) {
         float angle = clock * 0.35F + i * (float) (Math.PI * 2) / n;
         float top = h * (0.55F + 0.45F * Mth.sin(time * 1.8F + i * 1.3F));
         Vector3f bottom = new Vector3f(Mth.cos(angle) * r, 0.02F, Mth.sin(angle) * r);
         Vector3f upper = new Vector3f(bottom.x, top, bottom.z);
         float alpha = fade(feet, new Vector3f(bottom.x, Math.min(top, 1.2F), bottom.z));
         if (!(alpha <= 0.01F)) {
            int c = this.color((float)i / n, time, alpha * strength * 0.9F);
            Geometry.beam(glowBuffer, last, feet, bottom, upper, 0.05F * this.size(), 0.02F * this.size(), c, c & 16777215);
         }
      }

      VertexConsumer dots = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));

      for (int i = 0; i < n; i++) {
         float angle = clock * 0.35F + i * (float) (Math.PI * 2) / n;
         Geometry.flatSprite(
            dots,
            last,
            Mth.cos(angle) * r,
            0.03F,
            Mth.sin(angle) * r,
            0.16F * this.size(),
            0.0F,
            this.color((float)i / n, time, 0.7F * strength)
         );
      }
   }

   private void surge(CosmeticContext ctx, PoseStack.Pose last, Vector3f feet, float r, float h, float clock, float time, float strength) {
      VertexConsumer dots = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
      Geometry.flatSprite(dots, last, 0.0F, 0.03F, 0.0F, r * 1.5F, 0.0F, this.color(0.0F, time, 0.3F * strength));
      VertexConsumer glowBuffer = ctx.buffers().getBuffer(RoseRenderTypes.GLOW);
      int sides = 32;

      for (int k = 0; k < 2; k++) {
         float t = frac(Math.abs(clock) * 0.42F + k * 0.5F);
         float base = t * h * 0.9F;
         float tall = 0.55F * (1.0F - t * 0.4F);
         float wallRadius = r * (0.82F + 0.2F * t);
         float alpha = (float)Math.pow(1.0F - t, 1.3) * 0.75F * strength * fade(feet, new Vector3f(0.0F, base + tall * 0.5F, wallRadius));
         if (!(alpha <= 0.01F)) {
            int bright = this.color(t * 0.5F, time, alpha);
            int clear = bright & 16777215;

            for (int i = 0; i < sides; i++) {
               float a0 = i * (float) (Math.PI * 2) / sides;
               float a1 = (i + 1) * (float) (Math.PI * 2) / sides;
               float x0 = Mth.cos(a0) * wallRadius;
               float z0 = Mth.sin(a0) * wallRadius;
               float x1 = Mth.cos(a1) * wallRadius;
               float z1 = Mth.sin(a1) * wallRadius;
               float mid = base + tall * 0.35F;
               float top = base + tall;
               Geometry.glowQuad(
                  glowBuffer,
                  last,
                  new Vector3f(x0, base, z0),
                  new Vector3f(x1, base, z1),
                  new Vector3f(x1, mid, z1),
                  new Vector3f(x0, mid, z0),
                  clear,
                  clear,
                  bright,
                  bright
               );
               Geometry.glowQuad(
                  glowBuffer,
                  last,
                  new Vector3f(x0, mid, z0),
                  new Vector3f(x1, mid, z1),
                  new Vector3f(x1, top, z1),
                  new Vector3f(x0, top, z0),
                  bright,
                  bright,
                  clear,
                  clear
               );
            }
         }
      }

      int arcs = Math.clamp(this.count.intValue() / 2, 2, 8);
      int points = 22;
      float thickness = 0.085F * this.size();

      for (int k = 0; k < arcs; k++) {
         float tilt = (k % 2 == 0 ? 1 : -1) * (0.35F + 0.22F * k);
         float around = k * 2.4F;
         float centerY = h * (0.28F + 0.5F * frac(k * 0.37F + 0.2F));
         float arcRadius = r * (0.95F + 0.12F * (k % 3));
         float start = clock * (2.2F + 0.5F * k) * (k % 2 == 0 ? 1 : -1) + k * 1.9F;
         float sweep = 2.2F + 0.5F * Mth.sin(time * 0.9F + k);
         float cosT = Mth.cos(tilt);
         float sinT = Mth.sin(tilt);
         float cosA = Mth.cos(around);
         float sinA = Mth.sin(around);
         List<Vector3f> line = new ArrayList<>(points);
         float[] halo = new float[points];
         float[] core = new float[points];
         float[] hot = new float[points];
         int[] haloColors = new int[points];
         int[] coreColors = new int[points];
         int[] hotColors = new int[points];

         for (int i = 0; i < points; i++) {
            float q = i / (points - 1.0F);
            float angle = start + sweep * q;
            float x = Mth.cos(angle) * arcRadius;
            float z = Mth.sin(angle) * arcRadius;
            float y = z * sinT;
            z *= cosT;
            Vector3f point = new Vector3f(x * cosA - z * sinA, centerY + y, x * sinA + z * cosA);
            line.add(point);
            float taper = (float)Math.pow(Math.max(0.0F, Mth.sin((float) Math.PI * q)), 0.8);
            float near = fade(feet, point);
            halo[i] = thickness * 3.2F * taper;
            core[i] = thickness * taper;
            hot[i] = thickness * 0.3F * taper;
            float along = k * 0.31F + q * 0.35F;
            haloColors[i] = this.color(along, time, 0.26F * strength * near);
            coreColors[i] = this.color(along, time, 0.95F * strength * near * (0.35F + 0.65F * q));
            hotColors[i] = ColorUtil.withAlpha(-1, Math.round(255.0F * this.opacity() * strength * near * q * q));
         }

         Geometry.ribbon(glowBuffer, last, feet, line, halo, haloColors);
         Geometry.ribbon(glowBuffer, last, feet, line, core, coreColors);
         Geometry.ribbon(glowBuffer, last, feet, line, hot, hotColors);
      }
   }

   private void flame(CosmeticContext ctx, PoseStack.Pose last, Vector3f feet, float r, float h, float clock, float time, float strength) {
      VertexConsumer dots = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
      Geometry.flatSprite(dots, last, 0.0F, 0.03F, 0.0F, r * 1.35F, 0.0F, this.color(0.0F, time, 0.34F * strength));
      int tongues = Math.clamp(this.count.intValue() * 2, 6, 48);

      for (int i = 0; i < tongues; i += 3) {
         float t = frac(clock * 0.6F + i * 0.173F);
         float angle = i * (float) (Math.PI * 2) / tongues + t * 0.6F;
         Vector3f point = new Vector3f(Mth.cos(angle) * r * 0.8F, 0.2F + t * h * 0.8F, Mth.sin(angle) * r * 0.8F);
         Geometry.dotSprite(
            dots,
            last,
            point,
            ctx.right(),
            ctx.up(),
            0.035F * this.size() * (1.0F - t * 0.5F),
            this.color(0.3F + t * 0.5F, time, (1.0F - t) * strength * fade(feet, point))
         );
      }

      VertexConsumer glowBuffer = ctx.buffers().getBuffer(RoseRenderTypes.GLOW);

      for (int i = 0; i < tongues; i++) {
         float angle = i * (float) (Math.PI * 2) / tongues;
         float flicker = Math.clamp(
            0.55F
               + 0.45F * Mth.sin(clock * 5.1F + i * 1.7F) * Mth.sin(clock * 3.3F + i * 0.9F)
               + 0.4F * Noise.value(i * 2.3F, time * 3.1F, 0.0F) * this.life(),
            0.05F,
            1.4F
         );
         float tall = h * 0.5F * (0.45F + 0.55F * flicker) * (i % 2 == 0 ? 1.0F : 0.62F);
         float sway = 0.12F * Mth.sin(clock * 2.6F + i * 2.1F);
         float x = Mth.cos(angle) * r * 0.86F;
         float z = Mth.sin(angle) * r * 0.86F;
         Vector3f bottom = new Vector3f(x, 0.02F, z);
         Vector3f middle = new Vector3f(
            x * 0.96F - sway * 0.4F * Mth.sin(angle), tall * 0.5F, z * 0.96F + sway * 0.4F * Mth.cos(angle)
         );
         Vector3f top = new Vector3f(x * 0.9F - sway * Mth.sin(angle), tall, z * 0.9F + sway * Mth.cos(angle));
         float near = fade(feet, middle);
         float width = 0.085F * this.size();
         int low = this.color((float)i / tongues * 0.3F, time, 0.85F * strength * near);
         int mid = this.color(0.35F, time, 0.6F * strength * near);
         int tip = this.color(0.7F, time, 0.0F);
         Geometry.beam(glowBuffer, last, feet, bottom, middle, width, width * 0.7F, low, mid);
         Geometry.beam(glowBuffer, last, feet, middle, top, width * 0.7F, 0.004F, mid, tip);
      }
   }

   private static float frac(float value) {
      return value - (float)Math.floor(value);
   }

   private static final class Spark {
      float angle;
      float distance;
      float y;
      float rise;
      float swirl;
      float life;
      float size;
      float hue;
   }

   public enum Style {
      RING,
      WAVES,
      ORBIT,
      HELIX,
      SPARKS,
      PILLARS,
      SURGE,
      FLAME;
   }
}
