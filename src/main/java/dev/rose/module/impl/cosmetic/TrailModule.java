package dev.rose.module.impl.cosmetic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.world.CosmeticContext;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.Geometry;
import dev.rose.render.world.life.Motion;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


public final class TrailModule extends CosmeticModule {
   private static final double DRIFT = 0.42;
   private static final double RISE = 0.12;
   private static final float RAMP = 0.5F;
   private static final double THROW = 0.2;
   private static final double COAST = 0.25;
   private static final double SPARK_WIND = 4.0;
   private final ChoiceSetting<TrailModule.Style> style = this.add(new ChoiceSetting<>("style", TrailModule.Style.RIBBON));
   private final NumberSetting length;
   private final NumberSetting width;
   private final NumberSetting height;
   private final NumberSetting spacing;
   private final BoolSetting taper;
   private final BoolSetting onlyMoving;
   private final NumberSetting glow;
   private final Deque<TrailModule.Point> points;
   private final List<TrailModule.Spark> sparks;
   private final Random random;
   private final Vector3f wind;

   public TrailModule() {
      super("trail");
      this.section("shape", 58660);
      this.length = this.add(new NumberSetting("length", 1.2, 0.2, 6.0, 0.1, "rose.unit.seconds"));
      this.width = this.add(new NumberSetting("width", 100.0, 10.0, 300.0, 5.0, "rose.unit.percent"));
      this.height = this.add(new NumberSetting("height", 45.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.spacing = this.add(new NumberSetting("spacing", 25.0, 5.0, 100.0, 5.0, "")).visibleWhen(() -> this.style.get() == TrailModule.Style.DOTS);
      this.taper = this.add(new BoolSetting("taper", true));
      this.onlyMoving = this.add(new BoolSetting("only_moving", true));
      this.glow = this.add(new NumberSetting("glow", 60.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.points = new ArrayDeque<>();
      this.sparks = new ArrayList<>();
      this.random = new Random();
      this.wind = new Vector3f();
   }

   @Override
   public int icon() {
      return 58251;
   }

   @Override
   protected boolean alive() {
      return true;
   }

   @Override
   protected void onDisable() {
      this.points.clear();
      this.sparks.clear();
   }

   public void render(CosmeticContext ctx) {
      float time = ctx.time();
      Vec3 now = ctx.player().getPosition(ctx.partialTick()).add(0.0, 1.8 * this.height.floatValue() / 100.0, 0.0);
      this.record(now, time);
      float lifetime = this.length.floatValue();

      while (!this.points.isEmpty() && time - this.points.peekFirst().time > lifetime) {
         this.points.removeFirst();
      }

      this.drift(time);
      if (this.style.get() == TrailModule.Style.SPARKS) {
         this.sparks(ctx, now, time);
      } else if (this.points.size() >= 2) {
         List<Vec3> path = new ArrayList<>();
         List<Float> ages = new ArrayList<>();
         this.smooth(path, ages, time, lifetime, now);
         if (path.size() >= 2) {
            PoseStack.Pose pose = ctx.pose().last();
            float w = 0.2F * this.width.floatValue() / 100.0F;
            float strength = 0.35F + 0.65F * this.glow.floatValue() / 100.0F;
            switch ((TrailModule.Style)this.style.get()) {
               case RIBBON:
                  this.ribbon(ctx, pose, path, ages, w, 0.0F, time, strength, true);
                  break;
               case LINE:
                  this.ribbon(ctx, pose, path, ages, 0.035F * this.width.floatValue() / 100.0F, 0.0F, time, strength, true);
                  break;
               case DOTS:
                  this.dots(ctx, pose, path, ages, w, time, strength);
                  break;
               case DOUBLE:
                  this.ribbon(ctx, pose, path, ages, w * 0.45F, 0.28F, time, strength, true);
                  this.ribbon(ctx, pose, path, ages, w * 0.45F, -0.28F, time, strength, false);
                  break;
               case HELIX:
                  this.helix(ctx, pose, path, ages, w * 0.35F, time, strength);
            }

            ctx.buffers().endBatch();
         }
      }
   }

   private void record(Vec3 position, float time) {
      TrailModule.Point last = this.points.peekLast();
      if (last != null && last.position.distanceToSqr(position) > 64.0) {
         this.points.clear();
      }

      boolean moved = last == null || last.position.distanceToSqr(position) > 9.0E-4;
      if (moved || !this.onlyMoving.isOn() && time - last.time > 0.05F) {
         this.points.addLast(new TrailModule.Point(position, time, Motion.velocity().scale(0.2 * this.life())));
      }
   }

   private void drift(float time) {
      float strength = this.life();
      if (!(strength <= 0.0F) && !this.points.isEmpty()) {
         double amount = 0.42 * strength;
         double rise = 0.12 * strength;

         for (TrailModule.Point point : this.points) {
            float age = time - point.time;
            float ramp = Math.clamp(age / 0.5F, 0.0F, 1.0F);
            this.flow(point.origin.x, point.origin.y, point.origin.z, time, this.wind);
            double coast = 0.25 * (1.0 - Math.exp(-age / 0.25));
            point.position = point.origin
               .add(
                  this.wind.x * amount * ramp + point.throwX * coast,
                  this.wind.y * amount * ramp + point.throwY * coast + rise * ramp,
                  this.wind.z * amount * ramp + point.throwZ * coast
               );
         }
      }
   }

   private void smooth(List<Vec3> path, List<Float> ages, float time, float lifetime, Vec3 head) {
      List<TrailModule.Point> list = new ArrayList<>(this.points);
      list.add(new TrailModule.Point(head, time, Vec3.ZERO));

      for (int i = 0; i < list.size() - 1; i++) {
         TrailModule.Point p0 = list.get(Math.max(0, i - 1));
         TrailModule.Point p1 = list.get(i);
         TrailModule.Point p2 = list.get(i + 1);
         TrailModule.Point p3 = list.get(Math.min(list.size() - 1, i + 2));
         int steps = 3;

         for (int s = 0; s < steps; s++) {
            float t = (float)s / steps;
            path.add(catmull(p0.position, p1.position, p2.position, p3.position, t));
            float age = Mth.lerp(t, p1.time, p2.time);
            ages.add(Math.clamp(1.0F - (time - age) / lifetime, 0.0F, 1.0F));
         }
      }

      path.add(head);
      ages.add(1.0F);
   }

   private static Vec3 catmull(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3, float t) {
      double t2 = t * t;
      double t3 = t2 * t;
      return new Vec3(
         0.5
            * (
               2.0 * p1.x
                  + (-p0.x + p2.x) * t
                  + (2.0 * p0.x - 5.0 * p1.x + 4.0 * p2.x - p3.x) * t2
                  + (-p0.x + 3.0 * p1.x - 3.0 * p2.x + p3.x) * t3
            ),
         0.5
            * (
               2.0 * p1.y
                  + (-p0.y + p2.y) * t
                  + (2.0 * p0.y - 5.0 * p1.y + 4.0 * p2.y - p3.y) * t2
                  + (-p0.y + 3.0 * p1.y - 3.0 * p2.y + p3.y) * t3
            ),
         0.5
            * (
               2.0 * p1.z
                  + (-p0.z + p2.z) * t
                  + (2.0 * p0.z - 5.0 * p1.z + 4.0 * p2.z - p3.z) * t2
                  + (-p0.z + 3.0 * p1.z - 3.0 * p2.z + p3.z) * t3
            )
      );
   }

   private float alphaAt(Vector3f local, float age) {
      return (float)Math.pow(age, 1.3) * CosmeticContext.nearFade(local, 0.7F, 1.4F);
   }

   private float pulse(float time, float age) {
      float strength = this.life();
      return strength <= 0.0F ? 1.0F : 1.0F + 0.22F * strength * Mth.sin(age * 9.0F - time * 3.4F);
   }

   private void ribbon(
      CosmeticContext ctx, PoseStack.Pose pose, List<Vec3> path, List<Float> ages, float w, float sideOffset, float time, float strength, boolean halo
   ) {
      VertexConsumer consumer = ctx.buffers().getBuffer(RoseRenderTypes.GLOW);
      Vector3f zero = new Vector3f();
      float haloWidth = 2.4F + 1.2F * this.life() * Motion.intensity();

      for (int pass = 0; pass < (halo && this.glow.floatValue() > 0.0F ? 2 : 1); pass++) {
         boolean glowPass = halo && this.glow.floatValue() > 0.0F && pass == 0;
         float passWidth = glowPass ? w * haloWidth : w;
         float passAlpha = glowPass ? 0.3F * this.glow.floatValue() / 100.0F : 1.0F;
         Vector3f previous = null;
         float previousWidth = 0.0F;
         int previousColor = 0;

         for (int i = 0; i < path.size(); i++) {
            Vec3 point = path.get(i);
            if (sideOffset != 0.0F) {
               point = point.add(sideways(path, i).scale(sideOffset));
            }

            Vector3f local = ctx.local(point);
            float age = ages.get(i);
            float width = passWidth * (this.taper.isOn() ? 0.15F + 0.85F * age : 1.0F) * this.pulse(time, age);
            int color = this.color(1.0F - age, time, this.alphaAt(local, age) * strength * passAlpha);
            if (previous != null && (ColorUtil.alpha(color) > 1 || ColorUtil.alpha(previousColor) > 1)) {
               Geometry.beam(consumer, pose, zero, previous, local, previousWidth, width, previousColor, color);
            }

            previous = local;
            previousWidth = width;
            previousColor = color;
         }
      }
   }

   private static Vec3 sideways(List<Vec3> path, int i) {
      Vec3 a = path.get(Math.max(0, i - 1));
      Vec3 b = path.get(Math.min(path.size() - 1, i + 1));
      Vec3 direction = new Vec3(b.x - a.x, 0.0, b.z - a.z);
      if (direction.lengthSqr() < 1.0E-8) {
         return new Vec3(1.0, 0.0, 0.0);
      }

      direction = direction.normalize();
      return new Vec3(-direction.z, 0.0, direction.x);
   }

   private void helix(CosmeticContext ctx, PoseStack.Pose pose, List<Vec3> path, List<Float> ages, float w, float time, float strength) {
      VertexConsumer consumer = ctx.buffers().getBuffer(RoseRenderTypes.GLOW);
      Vector3f zero = new Vector3f();

      for (int strand = 0; strand < 2; strand++) {
         Vector3f previous = null;
         float previousWidth = 0.0F;
         int previousColor = 0;
         double distance = 0.0;

         for (int i = 0; i < path.size(); i++) {
            if (i > 0) {
               distance += path.get(i).distanceTo(path.get(i - 1));
            }

            Vec3 side = sideways(path, i);
            double phase = distance * 7.0 + time * 3.0F + strand * Math.PI;
            float age = ages.get(i);
            float radius = 0.24F * this.width.floatValue() / 100.0F * this.pulse(time, age);
            Vec3 offset = side.scale(Math.cos(phase) * radius).add(0.0, Math.sin(phase) * radius, 0.0);
            Vector3f local = ctx.local(path.get(i).add(offset));
            float width = w * (this.taper.isOn() ? 0.2F + 0.8F * age : 1.0F);
            int color = this.color(1.0F - age + strand * 0.3F, time, this.alphaAt(local, age) * strength);
            if (previous != null) {
               Geometry.beam(consumer, pose, zero, previous, local, previousWidth, width, previousColor, color);
            }

            previous = local;
            previousWidth = width;
            previousColor = color;
         }
      }
   }

   private void dots(CosmeticContext ctx, PoseStack.Pose pose, List<Vec3> path, List<Float> ages, float w, float time, float strength) {
      VertexConsumer consumer = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
      float step = this.spacing.floatValue() / 100.0F;
      double carried = 0.0;

      for (int i = path.size() - 1; i > 0; i--) {
         Vec3 a = path.get(i);
         Vec3 b = path.get(i - 1);
         double segment = a.distanceTo(b);

         double at;
         for (at = step - carried; at <= segment; at += step) {
            double t = at / segment;
            Vec3 point = a.lerp(b, t);
            float age = Mth.lerp((float)t, ages.get(i), ages.get(i - 1));
            Vector3f local = ctx.local(point);
            float size = w * 0.9F * (this.taper.isOn() ? 0.3F + 0.7F * age : 1.0F) * this.pulse(time, age);
            Geometry.dotSprite(consumer, pose, local, ctx.right(), ctx.up(), size, this.color(1.0F - age, time, this.alphaAt(local, age) * strength));
         }

         carried = segment - (at - step);
      }
   }

   private void sparks(CosmeticContext ctx, Vec3 head, float time) {
      float dt = ctx.delta();
      TrailModule.Point last = this.points.peekLast();
      boolean moving = last != null && time - last.time < 0.1F;
      if (!this.onlyMoving.isOn() || moving) {
         int spawn = this.random.nextFloat() < dt * 40.0F ? 2 : 0;

         for (int i = 0; i < spawn && this.sparks.size() < 300; i++) {
            TrailModule.Spark spark = new TrailModule.Spark();
            spark.position = head.add(
               (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3, (this.random.nextDouble() - 0.5) * 0.3
            );
            spark.velocity = new Vec3((this.random.nextDouble() - 0.5) * 0.4, 0.1 + this.random.nextDouble() * 0.3, (this.random.nextDouble() - 0.5) * 0.4);
            spark.life = 1.0F;
            spark.hue = this.random.nextFloat();
            this.sparks.add(spark);
         }
      }

      if (!this.sparks.isEmpty()) {
         VertexConsumer consumer = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
         PoseStack.Pose pose = ctx.pose().last();
         float strength = 0.35F + 0.65F * this.glow.floatValue() / 100.0F;
         float w = 0.12F * this.width.floatValue() / 100.0F;
         Iterator<TrailModule.Spark> iterator = this.sparks.iterator();

         while (iterator.hasNext()) {
            TrailModule.Spark spark = iterator.next();
            spark.life = spark.life - dt / Math.max(0.2F, this.length.floatValue());
            if (spark.life <= 0.0F) {
               iterator.remove();
            } else {
               this.flow(spark.position.x, spark.position.y, spark.position.z, time, this.wind);
               spark.velocity = spark.velocity
                  .scale(Math.pow(0.35, dt))
                  .add(this.wind.x * 4.0 * dt, this.wind.y * 4.0 * dt - 0.35 * dt, this.wind.z * 4.0 * dt);
               spark.position = spark.position.add(spark.velocity.scale(dt));
               Vector3f local = ctx.local(spark.position);
               float size = w * (0.4F + 0.6F * spark.life) * this.breath(time, spark.hue * 17.0F, 0.3F);
               Geometry.dotSprite(
                  consumer,
                  pose,
                  local,
                  ctx.right(),
                  ctx.up(),
                  size,
                  this.color(spark.hue, time, spark.life * CosmeticContext.nearFade(local, 0.7F, 1.4F) * strength)
               );
            }
         }

         ctx.buffers().endBatch();
      }
   }

   private static final class Point {
      final Vec3 origin;
      Vec3 position;
      final double throwX;
      final double throwY;
      final double throwZ;
      final float time;

      Point(Vec3 origin, float time, Vec3 thrown) {
         this.origin = origin;
         this.position = origin;
         this.time = time;
         this.throwX = thrown.x;
         this.throwY = thrown.y;
         this.throwZ = thrown.z;
      }
   }

   private static final class Spark {
      Vec3 position;
      Vec3 velocity;
      float life;
      float hue;
   }

   public enum Style {
      RIBBON,
      LINE,
      DOTS,
      DOUBLE,
      HELIX,
      SPARKS;
   }
}
