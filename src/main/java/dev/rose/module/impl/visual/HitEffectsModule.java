package dev.rose.module.impl.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.module.Category;
import dev.rose.module.impl.cosmetic.CosmeticModule;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.world.CosmeticContext;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.EffectTextures;
import dev.rose.render.world.Geometry;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


public final class HitEffectsModule extends CosmeticModule {
   private static final int STAR = 0;
   private static final int HEART = 1;
   private static final int BUBBLE = 2;
   private static final int SHARD = 3;
   private static final int DOT = 4;
   private static final int SPARK = 5;
   private static final int RING = 6;
   private static final int LIMIT = 700;
   private final ChoiceSetting<HitEffectsModule.Style> style = this.add(new ChoiceSetting<>("style", HitEffectsModule.Style.STARS));
   private final NumberSetting amount = this.add(new NumberSetting("amount", 14.0, 4.0, 60.0, 1.0, ""));
   private final NumberSetting size = this.add(new NumberSetting("size", 100.0, 40.0, 250.0, 5.0, "rose.unit.percent"));
   private final NumberSetting speed = this.add(new NumberSetting("speed", 100.0, 30.0, 250.0, 5.0, "rose.unit.percent"));
   private final NumberSetting lifetime = this.add(new NumberSetting("lifetime", 1.1, 0.4, 3.0, 0.1, "rose.unit.seconds"));
   private final NumberSetting gravity = this.add(new NumberSetting("gravity", 100.0, 0.0, 250.0, 5.0, "rose.unit.percent"));
   private final NumberSetting glow = this.add(new NumberSetting("glow", 70.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
   private final BoolSetting critBoost = this.add(new BoolSetting("crit_boost", true));
   private final ActionSetting preview = this.add(new ActionSetting("preview", 57660, this::previewInFront));
   private final ChoiceSetting<HitEffectsModule.Flash> flash;
   private final NumberSetting flashDuration;
   private final NumberSetting flashStrength;
   private final ChoiceSetting<HitEffectsModule.Trigger> trigger;
   private final BoolSetting onlyPlayers;
   private final List<HitEffectsModule.Particle> particles;
   private final List<HitEffectsModule.Slash> slashes;
   private final List<HitEffectsModule.Glint> glints;
   private static final float WIND = 3.2F;
   private final Random random;
   private final Vector3f wind;
   private int hitId;
   private int critId;
   private long hitAt;
   private long critAt;

   public HitEffectsModule() {
      super("hit_effects", Category.VISUALS);
      this.section("flash", 57441);
      this.flash = this.add(new ChoiceSetting<>("flash", HitEffectsModule.Flash.GLOW));
      this.flashDuration = this.add(new NumberSetting("flash_duration", 0.4, 0.15, 0.6, 0.05, "rose.unit.seconds"));
      this.flashStrength = this.add(new NumberSetting("flash_strength", 80.0, 10.0, 100.0, 5.0, "rose.unit.percent"));
      this.section("rules", 57688);
      this.trigger = this.add(new ChoiceSetting<>("trigger", HitEffectsModule.Trigger.CONFIRMED));
      this.onlyPlayers = this.add(new BoolSetting("only_players", false));
      this.particles = new ArrayList<>();
      this.slashes = new ArrayList<>();
      this.glints = new ArrayList<>();
      this.random = new Random();
      this.wind = new Vector3f();
      this.hitId = -1;
      this.critId = -1;
      this.flashDuration.visibleWhen(() -> this.flash.get() != HitEffectsModule.Flash.NONE);
      this.flashStrength.visibleWhen(() -> this.flash.get() != HitEffectsModule.Flash.NONE);
      this.gravity.visibleWhen(() -> this.style.get() != HitEffectsModule.Style.SLASHES && this.style.get() != HitEffectsModule.Style.RINGS);
   }

   @Override
   public int icon() {
      return 57780;
   }

   @Override
   protected boolean alive() {
      return true;
   }

   @Override
   protected void onDisable() {
      this.particles.clear();
      this.slashes.clear();
      this.glints.clear();
   }

   private boolean counts(Entity target) {
      Minecraft mc = Minecraft.getInstance();
      if (!this.isEnabled() || target == null || mc.player == null || target == mc.player) {
         return false;
      } else {
         return target.isInvisibleTo(mc.player) ? false : !this.onlyPlayers.isOn() || target instanceof Player;
      }
   }

   public void onConfirmedHit(Entity target) {
      if (this.trigger.get() == HitEffectsModule.Trigger.CONFIRMED && this.counts(target)) {
         long now = System.currentTimeMillis();
         boolean crit = this.critId == target.getId() && now - this.critAt < 250L;
         this.hitId = target.getId();
         this.hitAt = now;
         this.critId = -1;
         this.burst(target, crit);
      }
   }

   public void onCrit(Entity target) {
      if (target != null) {
         long now = System.currentTimeMillis();
         if (this.trigger.get() == HitEffectsModule.Trigger.CONFIRMED && this.hitId == target.getId() && now - this.hitAt < 250L && this.counts(target)) {
            this.hitId = -1;
            if (this.critBoost.isOn()) {
               this.critExtra(target);
            }
         } else {
            this.critId = target.getId();
            this.critAt = now;
         }
      }
   }

   public void onClick(Entity target) {
      if (this.trigger.get() == HitEffectsModule.Trigger.CLICK && this.counts(target)) {
         this.burst(target, false);
      }
   }

   private void burst(Entity target, boolean crit) {
      AABB box = target.getBoundingBox();
      this.spawn(box, crit && this.critBoost.isOn());
      if (this.flash.get() != HitEffectsModule.Flash.NONE) {
         HitEffectsModule.Glint glint = new HitEffectsModule.Glint();
         glint.entity = target;
         glint.crit = crit;
         this.glints.add(glint);
         if (this.glints.size() > 8) {
            this.glints.removeFirst();
         }
      }
   }

   public void previewInFront() {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null) {
         Vec3 look = mc.player.getLookAngle();
         Vec3 feet = mc.player.position().add(look.x * 3.2, 0.0, look.z * 3.2);
         this.preview(feet, false);
      }
   }

   public void preview(Vec3 feet, boolean crit) {
      AABB box = new AABB(
         feet.x - 0.3, feet.y, feet.z - 0.3, feet.x + 0.3, feet.y + 1.8, feet.z + 0.3
      );
      this.spawn(box, crit);
      if (this.flash.get() != HitEffectsModule.Flash.NONE) {
         HitEffectsModule.Glint glint = new HitEffectsModule.Glint();
         glint.box = box;
         glint.crit = crit;
         this.glints.add(glint);
      }
   }

   private void spawn(AABB box, boolean crit) {
      Minecraft mc = Minecraft.getInstance();
      Vec3 camera = mc.gameRenderer.getMainCamera().position();
      Vec3 center = box.getCenter();
      Vec3 toCamera = camera.subtract(center);
      double distance = toCamera.length();
      toCamera = distance < 1.0E-4 ? new Vec3(0.0, 0.0, 1.0) : toCamera.scale(1.0 / distance);
      double half = Math.max(box.getXsize(), box.getZsize()) * 0.5;
      Vec3 origin = center.add(toCamera.x * half, box.getYsize() * 0.12, toCamera.z * half);
      float scale = this.size.floatValue() / 100.0F;
      float pace = this.speed.floatValue() / 100.0F * (crit ? 1.25F : 1.0F);
      float fall = this.gravity.floatValue() / 100.0F;
      float life = this.lifetime.floatValue();
      int count = Math.round(this.amount.intValue() * (crit ? 1.6F : 1.0F));
      switch ((HitEffectsModule.Style)this.style.get()) {
         case STARS:
            for (int i = 0; i < count; i++) {
               HitEffectsModule.Particle p = this.shoot(0, origin, toCamera, 2.0F + this.random.nextFloat() * 2.6F, pace, 0.7F);
               p.size = (0.07F + 0.09F * this.random.nextFloat() * this.random.nextFloat() + (i == 0 ? 0.06F : 0.0F)) * scale;
               p.spin = (this.random.nextBoolean() ? 1 : -1) * (1.5F + this.random.nextFloat() * 4.0F);
               p.drag = 2.4F;
               p.gravity = 5.0F * fall;
               p.life = life * (0.7F + 0.45F * this.random.nextFloat());
            }

            this.dust(origin, toCamera, count / 2 + 3, pace, scale, life * 0.6F, fall);
            break;
         case HEARTS:
            for (int i = 0; i < Math.max(3, Math.round(count * 0.6F)); i++) {
               HitEffectsModule.Particle p = this.shoot(1, origin, toCamera, 0.5F + this.random.nextFloat() * 1.1F, pace, 0.4F);
               p.vy = p.vy + (0.7F + this.random.nextFloat() * 0.9F) * pace;
               p.size = (0.085F + 0.08F * this.random.nextFloat()) * scale;
               p.drag = 1.6F;
               p.gravity = -0.5F * fall;
               p.life = life * (0.85F + 0.4F * this.random.nextFloat());
            }

            this.dust(origin, toCamera, count / 3 + 2, pace * 0.6F, scale, life * 0.5F, fall * 0.2F);
            break;
         case SPARKS:
            for (int i = 0; i < Math.round(count * 1.7F); i++) {
               HitEffectsModule.Particle p = this.shoot(5, origin, toCamera, 3.5F + this.random.nextFloat() * 6.0F, pace, 0.55F);
               p.size = (0.011F + 0.012F * this.random.nextFloat()) * scale;
               p.drag = 3.0F;
               p.gravity = 9.5F * fall;
               p.life = life * (0.3F + 0.35F * this.random.nextFloat());
            }

            this.ring(origin, 0.55F * scale, 0.28F, 0.1F);
            break;
         case SLASHES:
            int marks = Math.clamp(2 + count / 12, 2, 5);
            Vector3f right = new Vector3f(mc.gameRenderer.getMainCamera().leftVector()).negate();
            Vector3f up = new Vector3f(mc.gameRenderer.getMainCamera().upVector());
            float base = this.random.nextFloat() * (float) (Math.PI * 2);
            float reach = (float)Math.max(0.45, Math.min(box.getYsize(), 2.2) * 0.42);
            float middle = reach * 2.1F;
            float gap = reach * 0.3F * Math.max(0.7F, scale);
            float dirX = right.x * Mth.cos(base) + up.x * Mth.sin(base);
            float dirY = right.y * Mth.cos(base) + up.y * Mth.sin(base);
            float dirZ = right.z * Mth.cos(base) + up.z * Mth.sin(base);
            double centerX = origin.x + toCamera.x * 0.12 - dirX * middle;
            double centerY = center.y + box.getYsize() * 0.08 - dirY * middle;
            double centerZ = origin.z + toCamera.z * 0.12 - dirZ * middle;

            for (int i = 0; i < marks; i++) {
               HitEffectsModule.Slash slash = new HitEffectsModule.Slash();
               slash.x = centerX;
               slash.y = centerY;
               slash.z = centerZ;
               slash.right = right;
               slash.up = up;
               slash.radius = middle + (i - (marks - 1) / 2.0F) * gap;
               slash.sweep = 2.5F * reach / slash.radius * (0.8F + 0.35F * this.random.nextFloat()) * (crit ? 1.2F : 1.0F);
               slash.start = base - slash.sweep / 2.0F + (this.random.nextFloat() - 0.5F) * 0.14F;
               slash.width = (0.045F + 0.025F * this.random.nextFloat()) * scale * (i == marks / 2 ? 1.25F : 1.0F);
               slash.delay = i * 0.035F;
               slash.tone = (float)i / marks;
               this.slashes.add(slash);
            }

            while (this.slashes.size() > 24) {
               this.slashes.removeFirst();
            }

            for (int i = 0; i < count; i++) {
               HitEffectsModule.Particle p = this.shoot(5, origin, toCamera, 2.5F + this.random.nextFloat() * 4.0F, pace, 0.3F);
               p.size = (0.01F + 0.01F * this.random.nextFloat()) * scale;
               p.drag = 3.4F;
               p.gravity = 7.0F * fall;
               p.life = 0.25F + 0.3F * this.random.nextFloat();
            }
            break;
         case RINGS:
            this.ring(origin, 0.9F * scale, 0.5F, 0.0F);
            this.ring(origin, 0.6F * scale, 0.42F, 0.5F);
            this.dust(origin, toCamera, count, pace * 1.4F, scale, life * 0.6F, 0.0F);
            break;
         case BUBBLES:
            for (int i = 0; i < Math.max(4, Math.round(count * 0.8F)); i++) {
               HitEffectsModule.Particle p = this.shoot(2, origin, toCamera, 0.4F + this.random.nextFloat() * 1.2F, pace, 0.3F);
               p.vy = p.vy + (0.3F + this.random.nextFloat() * 0.6F) * pace;
               p.size = (0.05F + 0.1F * this.random.nextFloat() * this.random.nextFloat() + 0.02F) * scale;
               p.drag = 1.3F;
               p.gravity = -0.7F * fall;
               p.life = life * (0.7F + 0.6F * this.random.nextFloat());
            }
            break;
         case CRYSTALS:
            for (int i = 0; i < count; i++) {
               HitEffectsModule.Particle p = this.shoot(3, origin, toCamera, 2.2F + this.random.nextFloat() * 3.2F, pace, 0.75F);
               p.size = (0.05F + 0.07F * this.random.nextFloat()) * scale;
               p.spin = (this.random.nextBoolean() ? 1 : -1) * (3.0F + this.random.nextFloat() * 7.0F);
               p.drag = 1.7F;
               p.gravity = 7.5F * fall;
               p.life = life * (0.6F + 0.4F * this.random.nextFloat());
            }

            this.dust(origin, toCamera, count / 2, pace, scale, life * 0.5F, fall);
      }

      if (crit) {
         this.ring(origin, 1.15F * scale, 0.38F, 0.2F);
         this.dust(origin, toCamera, 10, pace * 1.5F, scale * 1.2F, life * 0.5F, fall * 0.5F);
      }

      while (this.particles.size() > 700) {
         this.particles.removeFirst();
      }
   }

   private void critExtra(Entity target) {
      Minecraft mc = Minecraft.getInstance();
      AABB box = target.getBoundingBox();
      Vec3 center = box.getCenter();
      Vec3 toCamera = mc.gameRenderer.getMainCamera().position().subtract(center).normalize();
      double half = Math.max(box.getXsize(), box.getZsize()) * 0.5;
      Vec3 origin = center.add(toCamera.x * half, box.getYsize() * 0.12, toCamera.z * half);
      float scale = this.size.floatValue() / 100.0F;
      float pace = this.speed.floatValue() / 100.0F;
      this.ring(origin, 1.15F * scale, 0.38F, 0.2F);
      this.dust(origin, toCamera, 12, pace * 1.5F, scale * 1.2F, this.lifetime.floatValue() * 0.5F, this.gravity.floatValue() / 100.0F * 0.5F);

      for (HitEffectsModule.Glint glint : this.glints) {
         if (glint.entity == target) {
            glint.crit = true;
         }
      }
   }

   private HitEffectsModule.Particle shoot(int kind, Vec3 origin, Vec3 toCamera, float velocity, float pace, float upward) {
      HitEffectsModule.Particle p = new HitEffectsModule.Particle();
      p.kind = kind;

      float dx;
      float dy;
      float dz;
      float length;
      do {
         dx = this.random.nextFloat() * 2.0F - 1.0F;
         dy = this.random.nextFloat() * 2.0F - 1.0F;
         dz = this.random.nextFloat() * 2.0F - 1.0F;
         length = dx * dx + dy * dy + dz * dz;
      } while (length > 1.0F || length < 0.01F);

      dx += (float)toCamera.x * 0.55F;
      dy += upward;
      dz += (float)toCamera.z * 0.55F;
      length = Mth.sqrt(dx * dx + dy * dy + dz * dz);
      float v = velocity * pace / Math.max(0.001F, length);
      p.vx = dx * v;
      p.vy = dy * v;
      p.vz = dz * v;
      p.x = origin.x + dx * 0.05;
      p.y = origin.y + dy * 0.05;
      p.z = origin.z + dz * 0.05;
      p.angle = this.random.nextFloat() * (float) (Math.PI * 2);
      p.tone = this.random.nextFloat();
      p.phase = this.random.nextFloat() * (float) (Math.PI * 2);
      p.life = 1.0F;
      this.particles.add(p);
      return p;
   }

   private void dust(Vec3 origin, Vec3 toCamera, int count, float pace, float scale, float life, float fall) {
      for (int i = 0; i < count; i++) {
         HitEffectsModule.Particle p = this.shoot(4, origin, toCamera, 1.2F + this.random.nextFloat() * 3.4F, pace, 0.45F);
         p.size = (0.018F + 0.03F * this.random.nextFloat()) * scale;
         p.drag = 2.8F;
         p.gravity = 3.2F * fall;
         p.life = Math.max(0.2F, life * (0.6F + 0.6F * this.random.nextFloat()));
      }
   }

   private void ring(Vec3 origin, float radius, float life, float tone) {
      HitEffectsModule.Particle p = new HitEffectsModule.Particle();
      p.kind = 6;
      p.x = origin.x;
      p.y = origin.y;
      p.z = origin.z;
      p.size = radius;
      p.life = life;
      p.tone = tone;
      this.particles.add(p);
   }

   public void render(CosmeticContext ctx) {
      float dt = ctx.delta();
      this.advance(dt, ctx.time());
      if (!this.particles.isEmpty() || !this.slashes.isEmpty() || !this.glints.isEmpty()) {
         float time = ctx.time();
         float shine = this.glow.floatValue() / 100.0F;
         PoseStack.Pose pose = ctx.pose().last();
         this.shapes(ctx, pose, 0, EffectTextures.star(), time, false, shine);
         this.shapes(ctx, pose, 1, EffectTextures.heart(), time, false, shine);
         this.shapes(ctx, pose, 2, EffectTextures.bubble(), time, false, shine);
         this.shapes(ctx, pose, 3, EffectTextures.shard(), time, false, shine);
         if (shine > 0.01F) {
            this.shapes(ctx, pose, 0, EffectTextures.star(), time, true, shine);
            this.shapes(ctx, pose, 1, EffectTextures.heart(), time, true, shine);
            this.shapes(ctx, pose, 2, EffectTextures.bubble(), time, true, shine);
            this.shapes(ctx, pose, 3, EffectTextures.shard(), time, true, shine);
         }

         this.rings(ctx, pose, time);
         this.dots(ctx, pose, time, shine);
         this.beams(ctx, pose, time);
         ctx.buffers().endBatch();
      }
   }

   private void advance(float dt, float time) {
      if (!(dt <= 0.0F)) {
         for (int i = this.particles.size() - 1; i >= 0; i--) {
            HitEffectsModule.Particle p = this.particles.get(i);
            p.age += dt;
            if (p.age >= p.life) {
               this.particles.remove(i);
            } else {
               float keep = (float)Math.exp(-p.drag * dt);
               this.flow(p.x, p.y, p.z, time, this.wind);
               p.vx = p.vx * keep + this.wind.x * 3.2F * dt;
               p.vy = p.vy * keep - p.gravity * dt + this.wind.y * 3.2F * dt;
               p.vz = p.vz * keep + this.wind.z * 3.2F * dt;
               p.x = p.x + p.vx * dt;
               p.y = p.y + p.vy * dt;
               p.z = p.z + p.vz * dt;
               p.angle = p.angle + p.spin * dt * (0.35F + 0.65F * keep);
            }
         }

         for (int i = this.slashes.size() - 1; i >= 0; i--) {
            HitEffectsModule.Slash slash = this.slashes.get(i);
            slash.age += dt;
            if (slash.age > slash.delay + 0.46F) {
               this.slashes.remove(i);
            }
         }

         float duration = this.flashDuration.floatValue();

         for (int i = this.glints.size() - 1; i >= 0; i--) {
            HitEffectsModule.Glint glint = this.glints.get(i);
            glint.age += dt;
            if (glint.age > duration || glint.entity != null && glint.entity.isRemoved()) {
               this.glints.remove(i);
            }
         }
      }
   }

   private static float appear(float age) {
      float t = Math.clamp(age / 0.16F, 0.0F, 1.0F) - 1.0F;
      return 1.0F + 2.2F * t * t * t + 1.2F * t * t;
   }

   private static float fade(HitEffectsModule.Particle p) {
      float t = p.age / p.life;
      float out = t < 0.6F ? 1.0F : 1.0F - (t - 0.6F) / 0.4F;
      return Math.min(1.0F, p.age / 0.05F) * out * out * (3.0F - 2.0F * out);
   }

   private int white(float strength) {
      return ColorUtil.withAlpha(16777215, Math.clamp(Math.round(255.0F * strength * this.opacity()), 0, 255));
   }

   private void shapes(CosmeticContext ctx, PoseStack.Pose pose, int kind, Identifier texture, float time, boolean glowing, float shine) {
      VertexConsumer consumer = null;

      for (HitEffectsModule.Particle p : this.particles) {
         if (p.kind == kind) {
            if (consumer == null) {
               consumer = ctx.buffers().getBuffer(glowing ? RoseRenderTypes.glowSprite(texture) : RoseRenderTypes.softSprite(texture));
            }

            Vector3f at = ctx.local(p.x, p.y, p.z);
            float visible = fade(p) * CosmeticContext.nearFade(at, 0.25F, 0.7F);
            if (!(visible <= 0.01F)) {
               float scale = p.size * appear(p.age);
               float angle = p.angle;
               float wide = 1.0F;
               float tall = 1.0F;
               switch (kind) {
                  case 0:
                     scale *= 1.0F + 0.12F * Mth.sin(p.age * 11.0F + p.phase);
                     break;
                  case 1:
                     angle = Mth.sin(p.age * 2.6F + p.phase) * 0.3F;
                     float sway = Mth.sin(p.age * 3.1F + p.phase) * 0.07F * Math.min(1.0F, p.age * 2.0F);
                     at.add(new Vector3f(ctx.right()).mul(sway));
                     scale *= 1.0F + 0.08F * Mth.sin(p.age * 7.0F + p.phase);
                     break;
                  case 2:
                     angle = 0.0F;
                     float t = p.age / p.life;
                     if (t > 0.88F) {
                        scale *= 1.0F + (t - 0.88F) / 0.12F * 0.55F;
                     }

                     float wobble = Mth.sin(p.age * 5.0F + p.phase) * 0.06F;
                     wide = 1.0F + wobble;
                     tall = 1.0F - wobble;
                     at.add(new Vector3f(ctx.right()).mul(Mth.sin(p.age * 2.2F + p.phase) * 0.05F));
                     break;
                  case 3:
                     wide = 0.85F;
                     tall = 1.3F;
               }

               if (glowing) {
                  Geometry.sprite(
                     consumer,
                     pose,
                     at,
                     ctx.right(),
                     ctx.up(),
                     scale * wide * 1.45F,
                     scale * tall * 1.45F,
                     angle,
                     this.color(p.tone, time, visible * 0.55F * shine)
                  );
               } else {
                  int body = ColorUtil.lerp(this.rgb(p.tone, time), -1, kind == 2 ? 0.55F : 0.32F);
                  Geometry.sprite(
                     consumer,
                     pose,
                     at,
                     ctx.right(),
                     ctx.up(),
                     scale * wide,
                     scale * tall,
                     angle,
                     ColorUtil.withAlpha(body, Math.clamp(Math.round(255.0F * visible * this.opacity()), 0, 255))
                  );
               }
            }
         }
      }
   }

   private void rings(CosmeticContext ctx, PoseStack.Pose pose, float time) {
      VertexConsumer consumer = null;

      for (HitEffectsModule.Particle p : this.particles) {
         if (p.kind == 6) {
            if (consumer == null) {
               consumer = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.ring()));
            }

            Vector3f at = ctx.local(p.x, p.y, p.z);
            float t = p.age / p.life;
            float eased = 1.0F - (1.0F - t) * (1.0F - t) * (1.0F - t);
            float radius = p.size * (0.15F + 0.85F * eased) / 0.8F;
            float visible = (1.0F - t) * (1.0F - t) * CosmeticContext.nearFade(at, 0.3F, 0.8F);
            Geometry.sprite(consumer, pose, at, ctx.right(), ctx.up(), radius, radius, 0.0F, this.color(p.tone, time, visible));
         }
      }
   }

   private void dots(CosmeticContext ctx, PoseStack.Pose pose, float time, float shine) {
      VertexConsumer consumer = ctx.buffers().getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));

      for (HitEffectsModule.Particle p : this.particles) {
         Vector3f at = ctx.local(p.x, p.y, p.z);
         float visible = fade(p) * CosmeticContext.nearFade(at, 0.25F, 0.7F);
         if (!(visible <= 0.01F)) {
            switch (p.kind) {
               case 0:
               case 3: {
                  float twinkle = Math.max(0.0F, Mth.sin(p.age * 9.0F + p.phase));
                  Geometry.dotSprite(
                     consumer, pose, at, ctx.right(), ctx.up(), p.size * (0.5F + 0.5F * twinkle), this.white(visible * 0.5F * shine * (0.4F + 0.6F * twinkle))
                  );
               }
               case 1:
               case 2:
               default:
                  break;
               case 4: {
                  float twinkle = 0.65F + 0.35F * Mth.sin(p.age * 18.0F + p.phase);
                  Geometry.dotSprite(consumer, pose, at, ctx.right(), ctx.up(), p.size * 1.6F, this.color(p.tone, time, visible * twinkle));
                  Geometry.dotSprite(consumer, pose, at, ctx.right(), ctx.up(), p.size * 0.6F, this.white(visible * twinkle));
                  break;
               }
               case 5:
                  Geometry.dotSprite(consumer, pose, at, ctx.right(), ctx.up(), p.size * 3.2F, this.color(p.tone, time, visible * 0.32F * shine));
            }
         }
      }

      if (this.flash.get() == HitEffectsModule.Flash.GLOW) {
         float duration = this.flashDuration.floatValue();
         float power = this.flashStrength.floatValue() / 100.0F;

         for (HitEffectsModule.Glint glint : this.glints) {
            AABB box = this.box(glint, ctx.partialTick());
            float t = Math.clamp(glint.age / duration, 0.0F, 1.0F);
            float visible = (float)Math.pow(1.0F - t, 1.4) * power;
            Vector3f center = ctx.local(box.getCenter());
            float reach = (float)Math.max(box.getYsize(), box.getXsize()) * (0.62F + 0.35F * t) * (glint.crit ? 1.2F : 1.0F);
            Geometry.dotSprite(consumer, pose, center, ctx.right(), ctx.up(), reach * 1.3F, this.color(0.15F, time, visible * 0.85F));
            Geometry.dotSprite(consumer, pose, center, ctx.right(), ctx.up(), reach * 0.6F, this.white(visible * 0.55F));
         }
      }
   }

   private void beams(CosmeticContext ctx, PoseStack.Pose pose, float time) {
      boolean frames = this.flash.get() == HitEffectsModule.Flash.BOX
         || this.flash.get() == HitEffectsModule.Flash.OUTLINE
         || this.flash.get() == HitEffectsModule.Flash.CORNERS;
      boolean sparks = false;

      for (HitEffectsModule.Particle p : this.particles) {
         if (p.kind == 5) {
            sparks = true;
            break;
         }
      }

      if (sparks || !this.slashes.isEmpty() || frames && !this.glints.isEmpty()) {
         VertexConsumer consumer = ctx.buffers().getBuffer(RoseRenderTypes.GLOW);
         Vector3f view = new Vector3f();

         for (HitEffectsModule.Particle p : this.particles) {
            if (p.kind == 5) {
               Vector3f head = ctx.local(p.x, p.y, p.z);
               float visible = fade(p) * CosmeticContext.nearFade(head, 0.25F, 0.7F);
               if (!(visible <= 0.01F)) {
                  Vector3f tail = new Vector3f(head).sub(p.vx * 0.05F, p.vy * 0.05F, p.vz * 0.05F);
                  int c = this.color(p.tone, time, visible);
                  Geometry.beam(consumer, pose, view, head, tail, p.size * 1.8F, p.size * 0.3F, c, c & 16777215);
                  Geometry.beam(consumer, pose, view, head, tail, p.size * 0.7F, p.size * 0.1F, this.white(visible), 16777215);
               }
            }
         }

         for (HitEffectsModule.Slash slash : this.slashes) {
            this.slash(ctx, consumer, pose, view, slash, time);
         }

         if (frames) {
            for (HitEffectsModule.Glint glint : this.glints) {
               this.frame(ctx, consumer, pose, view, glint, time);
            }
         }
      }
   }

   private void slash(CosmeticContext ctx, VertexConsumer consumer, PoseStack.Pose pose, Vector3f view, HitEffectsModule.Slash slash, float time) {
      float local = slash.age - slash.delay;
      if (!(local <= 0.0F)) {
         float drawn = Math.clamp(local / 0.1F, 0.0F, 1.0F);
         drawn = 1.0F - (1.0F - drawn) * (1.0F - drawn);
         float visible = 1.0F - Math.clamp((local - 0.1F) / 0.34F, 0.0F, 1.0F);
         visible *= visible;
         Vector3f center = ctx.local(slash.x, slash.y, slash.z);
         if (!(CosmeticContext.nearFade(center, 0.2F, 0.5F) <= 0.0F)) {
            int count = 20;
            List<Vector3f> points = new ArrayList<>(count);
            float[] halo = new float[count];
            float[] core = new float[count];
            float[] hot = new float[count];
            int[] haloColors = new int[count];
            int[] coreColors = new int[count];
            int[] hotColors = new int[count];

            for (int i = 0; i < count; i++) {
               float q = i / (count - 1.0F);
               float along = drawn * q;
               float angle = slash.start + slash.sweep * along;
               float cos = Mth.cos(angle) * slash.radius;
               float sin = Mth.sin(angle) * slash.radius;
               points.add(
                  new Vector3f(center)
                     .add(slash.right.x * cos + slash.up.x * sin, slash.right.y * cos + slash.up.y * sin, slash.right.z * cos + slash.up.z * sin)
               );
               float taper = (float)Math.pow(Math.max(0.0F, Mth.sin((float) Math.PI * along)), 0.75);
               if (drawn < 1.0F) {
                  taper *= Math.min(1.0F, (1.0F - q) * 5.0F + 0.2F);
               }

               float width = slash.width * taper * (0.6F + 0.4F * visible);
               halo[i] = width * 2.6F;
               core[i] = width;
               hot[i] = width * 0.24F;
               haloColors[i] = this.color(slash.tone + along * 0.25F, time, visible * 0.28F);
               coreColors[i] = this.color(slash.tone + along * 0.25F, time, visible * 0.95F);
               hotColors[i] = this.white(visible * 0.85F);
            }

            Geometry.ribbon(consumer, pose, view, points, halo, haloColors);
            Geometry.ribbon(consumer, pose, view, points, core, coreColors);
            Geometry.ribbon(consumer, pose, view, points, hot, hotColors);
         }
      }
   }

   private AABB box(HitEffectsModule.Glint glint, float partialTick) {
      if (glint.entity == null) {
         return glint.box;
      }

      Entity entity = glint.entity;
      Vec3 position = entity.getPosition(partialTick);
      double half = entity.getBbWidth() / 2.0;
      return new AABB(
         position.x - half,
         position.y,
         position.z - half,
         position.x + half,
         position.y + entity.getBbHeight(),
         position.z + half
      );
   }

   private void frame(CosmeticContext ctx, VertexConsumer consumer, PoseStack.Pose pose, Vector3f view, HitEffectsModule.Glint glint, float time) {
      float duration = this.flashDuration.floatValue();
      float power = this.flashStrength.floatValue() / 100.0F;
      float t = Math.clamp(glint.age / duration, 0.0F, 1.0F);
      float visible = (float)Math.pow(1.0F - t, 1.4) * power;
      if (!(visible <= 0.01F)) {
         float settle = (1.0F - t) * (1.0F - t) * (1.0F - t);
         double grow = 0.05 + 0.22 * settle * (glint.crit ? 1.5 : 1.0);
         AABB box = this.box(glint, ctx.partialTick()).inflate(grow, grow * 0.5, grow);
         Vector3f low = ctx.local(box.minX, box.minY, box.minZ);
         Vector3f high = ctx.local(box.maxX, box.maxY, box.maxZ);
         if (!(CosmeticContext.nearFade(ctx.local(box.getCenter()), 0.4F, 0.9F) <= 0.0F)) {
            Vector3f[] corners = new Vector3f[8];

            for (int i = 0; i < 8; i++) {
               corners[i] = new Vector3f((i & 1) == 0 ? low.x : high.x, (i & 2) == 0 ? low.y : high.y, (i & 4) == 0 ? low.z : high.z);
            }

            float thickness = 0.026F + 0.02F * power;
            int edge = this.color(0.1F, time, visible);
            int soft = this.color(0.6F, time, visible * 0.9F);
            HitEffectsModule.Flash kind = this.flash.get();
            if (kind == HitEffectsModule.Flash.BOX) {
               int bottom = this.color(0.1F, time, visible * 0.34F);
               int top = this.color(0.6F, time, visible * 0.08F);
               int[][] sides = new int[][]{{0, 1, 3, 2}, {4, 5, 7, 6}, {0, 4, 6, 2}, {1, 5, 7, 3}};

               for (int[] side : sides) {
                  Geometry.glowQuad(consumer, pose, corners[side[0]], corners[side[1]], corners[side[2]], corners[side[3]], bottom, bottom, top, top);
               }

               Geometry.glowQuad(consumer, pose, corners[2], corners[3], corners[7], corners[6], top, top, top, top);
            }

            int[][] edges = new int[][]{{0, 1}, {1, 5}, {5, 4}, {4, 0}, {2, 3}, {3, 7}, {7, 6}, {6, 2}, {0, 2}, {1, 3}, {5, 7}, {4, 6}};
            if (kind == HitEffectsModule.Flash.CORNERS) {
               float part = 0.24F;
               int faint = ColorUtil.withAlpha(soft, ColorUtil.alpha(soft) / 3);

               for (int[] pair : edges) {
                  Vector3f a = corners[pair[0]];
                  Vector3f b = corners[pair[1]];
                  Geometry.beam(consumer, pose, view, a, new Vector3f(a).lerp(b, part), thickness * 1.3F, thickness * 0.6F, edge, faint);
                  Geometry.beam(consumer, pose, view, b, new Vector3f(b).lerp(a, part), thickness * 1.3F, thickness * 0.6F, edge, faint);
               }
            } else {
               for (int[] pair : edges) {
                  boolean vertical = pair[0] + 2 == pair[1];
                  boolean lower = (pair[0] & 2) == 0;
                  Geometry.beam(
                     consumer,
                     pose,
                     view,
                     corners[pair[0]],
                     corners[pair[1]],
                     thickness,
                     thickness,
                     !vertical && !lower ? soft : edge,
                     !vertical && lower ? edge : soft
                  );
               }
            }
         }
      }
   }

   public boolean active() {
      return !this.particles.isEmpty() || !this.slashes.isEmpty() || !this.glints.isEmpty();
   }

   public enum Flash {
      NONE,
      GLOW,
      BOX,
      OUTLINE,
      CORNERS;
   }

   private static final class Glint {
      Entity entity;
      AABB box;
      float age;
      boolean crit;
   }

   private static final class Particle {
      int kind;
      double x;
      double y;
      double z;
      float vx;
      float vy;
      float vz;
      float age;
      float life;
      float size;
      float angle;
      float spin;
      float tone;
      float drag;
      float gravity;
      float phase;
   }

   private static final class Slash {
      double x;
      double y;
      double z;
      Vector3f right;
      Vector3f up;
      float radius;
      float start;
      float sweep;
      float width;
      float age;
      float delay;
      float tone;
   }

   public enum Style {
      STARS,
      HEARTS,
      SPARKS,
      SLASHES,
      RINGS,
      BUBBLES,
      CRYSTALS;
   }

   public enum Trigger {
      CONFIRMED,
      CLICK;
   }
}
