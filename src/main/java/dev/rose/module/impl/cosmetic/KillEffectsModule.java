package dev.rose.module.impl.cosmetic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.Theme;
import dev.rose.render.world.CosmeticContext;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.Geometry;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


public final class KillEffectsModule extends CosmeticModule {
   private final ChoiceSetting<KillEffectsModule.Style> style = this.add(new ChoiceSetting<>("style", KillEffectsModule.Style.SOULS));
   private final NumberSetting size = this.add(new NumberSetting("size", 100.0, 50.0, 250.0, 5.0, "rose.unit.percent"));
   private final NumberSetting duration = this.add(new NumberSetting("duration", 1.8, 0.4, 4.0, 0.1, "rose.unit.seconds"));
   private final BoolSetting onlyPlayers = this.add(new BoolSetting("only_players", false));
   private final List<KillEffectsModule.Effect> effects = new ArrayList<>();

   public KillEffectsModule() {
      super("kill_effects");
   }

   @Override
   public int icon() {
      return 57889;
   }

   @Override
   protected void onDisable() {
      this.effects.clear();
   }

   public void onKill(Entity entity, Vec3 position) {
      if (this.isEnabled() && (!this.onlyPlayers.isOn() || entity instanceof Player)) {
         this.spawn(position, entity.getBbHeight());
      }
   }

   public void preview(Vec3 position) {
      this.spawn(position, 1.8F);
   }

   private void spawn(Vec3 position, float height) {
      this.effects.add(new KillEffectsModule.Effect(position, height, Theme.time(), new Random().nextLong()));
      if (this.effects.size() > 12) {
         this.effects.removeFirst();
      }
   }

   public void render(CosmeticContext ctx) {
      float now = ctx.time();
      float life = this.duration.floatValue();
      this.effects.removeIf(effectx -> now - effectx.start() > life);
      if (!this.effects.isEmpty()) {
         KillEffectsModule.Style shown = this.style.get();
         float scale = this.size.floatValue() / 100.0F;
         PoseStack.Pose pose = ctx.pose().last();
         if (shown == KillEffectsModule.Style.CONFETTI) {
            VertexConsumer paper = ctx.buffers().getBuffer(RoseRenderTypes.softSprite(CosmeticTextures.white()));

            for (KillEffectsModule.Effect effect : this.effects) {
               this.confetti(ctx, paper, pose, ctx.local(effect.position()), effect, progress(effect, now, life), scale, now);
            }
         }

         if (shown == KillEffectsModule.Style.LIGHTNING || shown == KillEffectsModule.Style.SHOCKWAVE) {
            VertexConsumer rings = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.ring()));

            for (KillEffectsModule.Effect effect : this.effects) {
               this.groundRings(rings, pose, ctx.local(effect.position()), progress(effect, now, life), scale, now, shown);
            }
         }

         VertexConsumer dots = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));

         for (KillEffectsModule.Effect effect : this.effects) {
            Vector3f at = ctx.local(effect.position());
            float t = progress(effect, now, life);
            switch (shown) {
               case LIGHTNING:
                  this.impact(ctx, dots, pose, at, strike(t), scale, now);
                  break;
               case SOULS:
                  this.souls(ctx, dots, pose, at, effect, t, scale, now);
                  break;
               case SHOCKWAVE:
                  this.impact(ctx, dots, pose, at, (1.0F - t) * (1.0F - t) * 0.7F, scale, now);
                  break;
               case CONFETTI:
                  this.pop(ctx, dots, pose, at, effect, t, scale, now);
                  break;
               case FLASH:
                  this.flash(ctx, dots, pose, at, effect, t, scale, now);
            }
         }

         if (shown == KillEffectsModule.Style.LIGHTNING || shown == KillEffectsModule.Style.SHOCKWAVE) {
            VertexConsumer glow = ctx.buffers().getBuffer(RoseRenderTypes.GLOW);

            for (KillEffectsModule.Effect effect : this.effects) {
               Vector3f at = ctx.local(effect.position());
               float t = progress(effect, now, life);
               if (shown == KillEffectsModule.Style.LIGHTNING) {
                  this.lightning(glow, pose, at, effect, t, scale, now);
               } else {
                  this.pillar(glow, pose, at, t, scale, now);
               }
            }
         }

         ctx.buffers().endBatch();
      }
   }

   private static float progress(KillEffectsModule.Effect effect, float now, float life) {
      return Math.clamp((now - effect.start()) / life, 0.0F, 1.0F);
   }

   private void souls(CosmeticContext ctx, VertexConsumer dots, PoseStack.Pose pose, Vector3f at, KillEffectsModule.Effect effect, float t, float scale, float now) {
      Geometry.flatSprite(dots, pose, at.x, at.y + 0.02F, at.z, (0.7F + 0.5F * t) * scale, 0.0F, this.color(0.0F, now, (1.0F - t) * 0.5F));
      Random random = new Random(effect.seed());
      int count = 9;

      for (int i = 0; i < count; i++) {
         float delay = random.nextFloat() * 0.3F;
         float spin = (random.nextBoolean() ? 1 : -1) * (1.6F + random.nextFloat() * 1.4F);
         float startAngle = random.nextFloat() * (float) (Math.PI * 2);
         float spread = (0.2F + random.nextFloat() * 0.35F) * scale;
         float rise = (1.7F + random.nextFloat() * 1.5F) * scale;
         float headSize = (0.09F + random.nextFloat() * 0.05F) * scale;
         float position = (float)i / count;

         for (int k = 8; k >= 0; k--) {
            float local = (t - delay) / (1.0F - delay) - k * 0.02F;
            if (!(local <= 0.0F) && !(local >= 1.0F)) {
               float eased = 1.0F - (1.0F - local) * (1.0F - local);
               float angle = startAngle + spin * local * (float) Math.PI;
               float radius = spread * (0.5F + 0.9F * Mth.sin(local * (float) Math.PI));
               Vector3f point = new Vector3f(
                  at.x + Mth.cos(angle) * radius, at.y + effect.height() * 0.3F + eased * rise, at.z + Mth.sin(angle) * radius
               );
               float tail = 1.0F - k / 9.0F;
               float alpha = Math.min(1.0F, local * 7.0F) * (1.0F - local * local) * tail;
               float sizeNow = headSize * (0.3F + 0.7F * tail) * (1.15F - 0.45F * local);
               Geometry.dotSprite(dots, pose, point, ctx.right(), ctx.up(), sizeNow, this.color(position, now, alpha * (k == 0 ? 1.0F : 0.65F)));
               if (k == 0) {
                  Geometry.dotSprite(dots, pose, point, ctx.right(), ctx.up(), sizeNow * 0.38F, this.white(alpha * 0.9F));
               }
            }
         }
      }
   }

   private void confetti(CosmeticContext ctx, VertexConsumer paper, PoseStack.Pose pose, Vector3f at, KillEffectsModule.Effect effect, float t, float scale, float now) {
      Random random = new Random(effect.seed());
      float seconds = t * this.duration.floatValue();
      float drag = 4.0F;
      float terminal = 1.9F;
      float decay = (1.0F - (float)Math.exp(-drag * seconds)) / drag;
      float fade = t < 0.72F ? 1.0F : 1.0F - (t - 0.72F) / 0.28F;
      int count = 64;

      for (int i = 0; i < count; i++) {
         double angle = random.nextDouble() * Math.PI * 2.0;
         float out = (1.6F + random.nextFloat() * 3.4F) * scale;
         float up = (6.0F + random.nextFloat() * 5.5F) * scale;
         float phase = random.nextFloat() * (float) (Math.PI * 2);
         float spin = (random.nextFloat() - 0.5F) * 12.0F;
         float flipSpeed = 5.0F + random.nextFloat() * 9.0F;
         float tone = random.nextFloat();
         float light = random.nextFloat() * 0.5F;
         float halfW = (0.03F + random.nextFloat() * 0.018F) * scale;
         float halfH = halfW * (1.5F + random.nextFloat() * 0.9F);
         float x = (float)Math.cos(angle) * out * decay;
         float z = (float)Math.sin(angle) * out * decay;
         float y = effect.height() * 0.45F + (up + terminal) * decay - terminal * seconds;
         boolean landed = y <= 0.03F;
         float sway = 0.0F;
         if (landed) {
            y = 0.03F;
         } else {
            sway = Mth.sin(seconds * 4.5F + phase) * 0.1F * Math.min(1.0F, seconds * 1.5F);
         }

         float flip = landed ? 0.5F : Math.max(0.12F, Math.abs(Mth.cos(seconds * flipSpeed + phase)));
         int rgb = ColorUtil.lerp(this.rgb(tone, now), -1, light);
         int alpha = Math.clamp(Math.round(255.0F * fade * this.opacity()), 0, 255);
         Geometry.sprite(
            paper,
            pose,
            new Vector3f(at.x + x + sway, at.y + y, at.z + z - sway * 0.6F),
            ctx.right(),
            ctx.up(),
            halfW,
            halfH * flip,
            phase + (landed ? 0.0F : spin * seconds),
            ColorUtil.withAlpha(rgb, alpha)
         );
      }
   }

   private void pop(CosmeticContext ctx, VertexConsumer dots, PoseStack.Pose pose, Vector3f at, KillEffectsModule.Effect effect, float t, float scale, float now) {
      float burst = (float)Math.exp(-t * 16.0F);
      if (!(burst < 0.02F)) {
         Vector3f center = new Vector3f(at.x, at.y + effect.height() * 0.45F, at.z);
         Geometry.dotSprite(dots, pose, center, ctx.right(), ctx.up(), (0.5F + 0.9F * (1.0F - burst)) * scale, this.color(0.3F, now, burst * 0.8F));
         Geometry.dotSprite(dots, pose, center, ctx.right(), ctx.up(), 0.22F * scale, this.white(burst));
      }
   }

   private void flash(CosmeticContext ctx, VertexConsumer dots, PoseStack.Pose pose, Vector3f at, KillEffectsModule.Effect effect, float t, float scale, float now) {
      float burst = (float)Math.exp(-t * 6.0F);
      Vector3f center = new Vector3f(at.x, at.y + effect.height() * 0.55F, at.z);
      Geometry.dotSprite(dots, pose, center, ctx.right(), ctx.up(), (0.8F + 1.4F * (1.0F - burst)) * scale, this.color(0.0F, now, burst));
      Geometry.dotSprite(dots, pose, center, ctx.right(), ctx.up(), 0.35F * scale, this.white(burst));
      Geometry.flatSprite(dots, pose, at.x, at.y + 0.02F, at.z, (0.8F + 1.2F * (1.0F - burst)) * scale, 0.0F, this.color(0.5F, now, burst * 0.6F));
      Random random = new Random(effect.seed());

      for (int i = 0; i < 28; i++) {
         Vector3f direction = new Vector3f(random.nextFloat() * 2.0F - 1.0F, random.nextFloat() * 2.0F - 1.0F, random.nextFloat() * 2.0F - 1.0F).normalize();
         float distance = (0.3F + 1.7F * (1.0F - (float)Math.pow(1.0F - t, 2.0))) * scale * (0.6F + random.nextFloat() * 0.6F);
         Vector3f point = new Vector3f(center).add(direction.mul(distance));
         float sparkSize = (0.035F + random.nextFloat() * 0.035F) * scale * (1.2F - t);
         Geometry.dotSprite(dots, pose, point, ctx.right(), ctx.up(), sparkSize, this.color(i / 28.0F, now, 1.0F - t));
         Geometry.dotSprite(dots, pose, point, ctx.right(), ctx.up(), sparkSize * 0.4F, this.white((1.0F - t) * 0.8F));
      }
   }

   private static float strike(float t) {
      float base = t < 0.07F ? 1.0F : (t < 0.13F ? 0.2F : (t < 0.3F ? 1.0F : Math.max(0.0F, 1.0F - (t - 0.3F) / 0.4F)));
      return base * (0.85F + 0.15F * Mth.sin(t * 170.0F));
   }

   private void groundRings(VertexConsumer rings, PoseStack.Pose pose, Vector3f at, float t, float scale, float now, KillEffectsModule.Style shown) {
      float eased = 1.0F - (float)Math.pow(1.0F - t, 3.0);
      float alpha = (1.0F - t) * (1.0F - t);
      Geometry.flatSprite(rings, pose, at.x, at.y + 0.03F, at.z, (0.5F + 3.0F * eased) * scale, t * 2.0F, this.color(t, now, alpha));
      if (shown == KillEffectsModule.Style.SHOCKWAVE) {
         float second = Math.clamp((t - 0.12F) / 0.88F, 0.0F, 1.0F);
         Geometry.flatSprite(rings, pose, at.x, at.y + 0.04F, at.z, (0.4F + 2.0F * second) * scale, -t * 2.0F, this.color(0.5F, now, (1.0F - second) * 0.8F));
      }
   }

   private void impact(CosmeticContext ctx, VertexConsumer dots, PoseStack.Pose pose, Vector3f at, float strength, float scale, float now) {
      if (!(strength <= 0.01F)) {
         Geometry.flatSprite(dots, pose, at.x, at.y + 0.02F, at.z, 1.5F * scale, 0.0F, this.color(0.0F, now, strength * 0.55F));
         Geometry.dotSprite(
            dots, pose, new Vector3f(at.x, at.y + 0.4F * scale, at.z), ctx.right(), ctx.up(), 0.9F * scale, this.color(0.2F, now, strength * 0.45F)
         );
         Geometry.dotSprite(dots, pose, new Vector3f(at.x, at.y + 0.2F * scale, at.z), ctx.right(), ctx.up(), 0.3F * scale, this.white(strength * 0.7F));
      }
   }

   private void lightning(VertexConsumer glow, PoseStack.Pose pose, Vector3f at, KillEffectsModule.Effect effect, float t, float scale, float now) {
      float strength = strike(t);
      if (!(strength <= 0.01F)) {
         Random random = new Random(effect.seed());
         Vector3f sky = new Vector3f(at.x + (random.nextFloat() - 0.5F) * 4.0F * scale, at.y + 18.0F * scale, at.z + (random.nextFloat() - 0.5F) * 4.0F * scale);
         List<Vector3f> trunk = bolt(random, sky, new Vector3f(at), 5, 2.6F * scale);
         this.drawBolt(glow, pose, trunk, 1.0F, strength, scale, now);
         int branches = 2 + random.nextInt(2);

         for (int b = 0; b < branches; b++) {
            Vector3f start = trunk.get(4 + random.nextInt(trunk.size() / 2));
            float length = (2.5F + random.nextFloat() * 3.5F) * scale;
            double direction = random.nextDouble() * Math.PI * 2.0;
            Vector3f end = new Vector3f(
               start.x + (float)Math.cos(direction) * length * 0.6F,
               Math.max(at.y + 0.5F, start.y - length),
               start.z + (float)Math.sin(direction) * length * 0.6F
            );
            this.drawBolt(glow, pose, bolt(random, start, end, 3, 0.9F * scale), 0.45F, strength * 0.75F, scale, now);
         }
      }
   }

   private static List<Vector3f> bolt(Random random, Vector3f from, Vector3f to, int depth, float jitter) {
      List<Vector3f> points = new ArrayList<>(List.of(from, to));

      for (int level = 0; level < depth; level++) {
         List<Vector3f> next = new ArrayList<>(points.size() * 2);

         for (int i = 0; i < points.size() - 1; i++) {
            Vector3f a = points.get(i);
            Vector3f b = points.get(i + 1);
            next.add(a);
            next.add(
               new Vector3f(a)
                  .add(b)
                  .mul(0.5F)
                  .add((random.nextFloat() - 0.5F) * jitter, (random.nextFloat() - 0.5F) * jitter * 0.25F, (random.nextFloat() - 0.5F) * jitter)
            );
         }

         next.add(points.getLast());
         points = next;
         jitter *= 0.55F;
      }

      return points;
   }

   private void drawBolt(VertexConsumer glow, PoseStack.Pose pose, List<Vector3f> points, float thickness, float strength, float scale, float now) {
      Vector3f view = new Vector3f();
      int count = points.size();
      float[] halo = new float[count];
      float[] core = new float[count];
      float[] hot = new float[count];
      int[] haloColors = new int[count];
      int[] coreColors = new int[count];
      int[] hotColors = new int[count];

      for (int i = 0; i < count; i++) {
         float f = (float)i / (count - 1);
         halo[i] = 0.45F * scale * thickness;
         core[i] = 0.12F * scale * thickness;
         hot[i] = 0.035F * scale * thickness;
         haloColors[i] = this.color(f, now, strength * 0.3F);
         coreColors[i] = this.color(f, now, strength);
         hotColors[i] = this.white(strength);
      }

      Geometry.ribbon(glow, pose, view, points, halo, haloColors);
      Geometry.ribbon(glow, pose, view, points, core, coreColors);
      Geometry.ribbon(glow, pose, view, points, hot, hotColors);
   }

   private void pillar(VertexConsumer glow, PoseStack.Pose pose, Vector3f at, float t, float scale, float now) {
      float alpha = (1.0F - t) * (1.0F - t);
      Vector3f bottom = new Vector3f(at);
      Vector3f top = new Vector3f(at.x, at.y + 3.5F * scale * (0.4F + 0.6F * t), at.z);
      int c = this.color(0.2F, now, alpha);
      Geometry.beam(glow, pose, new Vector3f(), bottom, top, 0.35F * scale, 0.05F * scale, c, c & 16777215);
   }

   private int white(float strength) {
      return ColorUtil.withAlpha(16777215, Math.clamp(Math.round(255.0F * strength * this.opacity()), 0, 255));
   }

   private record Effect(Vec3 position, float height, float start, long seed) {
   }

   public enum Style {
      LIGHTNING,
      SOULS,
      SHOCKWAVE,
      CONFETTI,
      FLASH;
   }
}
