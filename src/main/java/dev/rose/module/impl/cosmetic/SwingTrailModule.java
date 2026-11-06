package dev.rose.module.impl.cosmetic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.Theme;
import dev.rose.render.world.Geometry;
import dev.rose.render.world.ItemProbe;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import dev.rose.util.Weapons;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;


public final class SwingTrailModule extends CosmeticModule {
   private static final int STRIDE = 8;
   private static final int CAPACITY = 256;
   private static final float SLOW = 5.0F;
   private static final float FAST = 11.0F;
   private static final PoseStack.Pose IDENTITY = new PoseStack().last();
   private final NumberSetting length = this.add(new NumberSetting("length", 0.2, 0.06, 0.6, 0.02, "rose.unit.seconds"));
   private final NumberSetting width = this.add(new NumberSetting("width", 50.0, 20.0, 100.0, 5.0, "rose.unit.percent"));
   private final NumberSetting edge = this.add(new NumberSetting("edge", 100.0, 0.0, 200.0, 5.0, "rose.unit.percent"));
   private final BoolSetting onlyWeapons = this.add(new BoolSetting("only_weapons", true));
   private final SwingTrailModule.Trail mainTrail = new SwingTrailModule.Trail();
   private final SwingTrailModule.Trail offTrail = new SwingTrailModule.Trail();
   private final Vector3f base = new Vector3f();
   private final Vector3f tip = new Vector3f();
   private final Vector3f guard = new Vector3f();
   private final Vector3f[] rails = new Vector3f[]{new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f()};
   private final Vector3f[] previousRails = new Vector3f[]{new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f()};
   private final int[] colors = new int[5];
   private final int[] previousColors = new int[5];

   public SwingTrailModule() {
      super("swing_trail");
   }

   @Override
   public int icon() {
      return 58035;
   }

   @Override
   protected void onDisable() {
      this.mainTrail.clear();
      this.offTrail.clear();
   }

   private boolean accepts(ItemStack stack) {
      return stack.isEmpty() ? false : !this.onlyWeapons.isOn() || Weapons.is(stack);
   }

   public void afterItem(ItemProbe itemPose, LivingEntity entity, ItemStack stack, ItemDisplayContext context, boolean leftHand, MultiBufferSource buffers) {
      if (this.isEnabled() && context.firstPerson() && entity instanceof LocalPlayer player) {
         boolean mainHand = leftHand == (player.getMainArm() == HumanoidArm.LEFT);
         SwingTrailModule.Trail trail = mainHand ? this.mainTrail : this.offTrail;
         float now = Theme.time();
         float life = this.length.floatValue();
         boolean swinging = player.swinging
            && player.swingingArm == (mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND)
            && this.accepts(stack)
            && itemPose.found();
         if (swinging) {
            Matrix4f matrix = itemPose.pose();
            float from = 0.44F - 0.6F * this.width.floatValue() / 100.0F;
            matrix.transformPosition(this.base.set(from, from, 0.0F));
            matrix.transformPosition(this.tip.set(0.44F, 0.44F, 0.0F));
            matrix.transformPosition(this.guard.set(-0.16F, -0.16F, 0.0F));
            boolean fresh = player.attackAnim < trail.progress;
            trail.progress = player.attackAnim;
            trail.push(this.base, this.tip, now, this.guard.distance(this.tip), fresh);
         } else {
            trail.progress = 0.0F;
         }

         trail.expire(now, life);
         if (trail.count >= 2) {
            this.draw(trail, buffers, now, life);
         }
      }
   }

   private void draw(SwingTrailModule.Trail trail, MultiBufferSource buffers, float now, float life) {
      VertexConsumer glow = buffers.getBuffer(RoseRenderTypes.GLOW);
      float[] data = trail.data;
      float bright = this.edge.floatValue() / 100.0F;
      boolean started = false;

      for (int i = 0; i < trail.count - 1; i++) {
         int p0 = trail.at(Math.max(0, i - 1));
         int p1 = trail.at(i);
         int p2 = trail.at(i + 1);
         int p3 = trail.at(Math.min(trail.count - 1, i + 2));
         if (!(data[p2 + 7] < 0.0F) && (!(data[p1 + 7] <= 0.0F) || !(data[p2 + 7] <= 0.0F))) {
            if (data[p1 + 7] < 0.0F) {
               p0 = p1;
            }

            if (data[p3 + 7] < 0.0F) {
               p3 = p2;
            }

            float energyFrom = Math.max(0.0F, data[p1 + 7]);
            float energyTo = data[p2 + 7];
            float span = Vector3f.distance(data[p1 + 3], data[p1 + 4], data[p1 + 5], data[p2 + 3], data[p2 + 4], data[p2 + 5]);
            int steps = Mth.clamp((int)Math.ceil(span / 0.018F), 1, 12);

            for (int step = started ? 1 : 0; step <= steps; step++) {
               float t = (float)step / steps;
               spline(data, p0, p1, p2, p3, 0, t, this.base);
               spline(data, p0, p1, p2, p3, 3, t, this.tip);
               float age = Mth.clamp((now - Mth.lerp(t, data[p1 + 6], data[p2 + 6])) / life, 0.0F, 1.0F);
               this.section(age, Mth.lerp(t, energyFrom, energyTo), bright, now);
               if (started) {
                  for (int rail = 0; rail < 4; rail++) {
                     Geometry.glowQuad(
                        glow,
                        IDENTITY,
                        this.previousRails[rail],
                        this.previousRails[rail + 1],
                        this.rails[rail + 1],
                        this.rails[rail],
                        this.previousColors[rail],
                        this.previousColors[rail + 1],
                        this.colors[rail + 1],
                        this.colors[rail]
                     );
                  }
               }

               for (int rail = 0; rail < 5; rail++) {
                  this.previousRails[rail].set(this.rails[rail]);
                  this.previousColors[rail] = this.colors[rail];
               }

               started = true;
            }
         } else {
            started = false;
         }
      }
   }

   private void section(float age, float energy, float bright, float now) {
      float fade = (float)Math.pow(1.0F - age, 1.5) * energy;
      float eased = age * age * (3.0F - 2.0F * age);
      float inner = Mth.lerp(eased, 0.0F, 0.9F);
      float rim = Mth.lerp(0.68F, inner, 1.0F);
      float beyond = 1.0F + 0.08F * (1.0F - eased);
      this.along(inner, this.rails[0]);
      this.along(rim, this.rails[1]);
      this.along(Mth.lerp(0.8F, rim, 1.0F), this.rails[2]);
      this.along(1.0F, this.rails[3]);
      this.along(beyond, this.rails[4]);
      int edgeColor = this.color(age * 0.7F + 0.15F, now, Math.min(1.0F, fade * bright));
      this.colors[0] = this.color(age * 0.7F, now, 0.0F);
      this.colors[1] = this.color(age * 0.7F, now, fade * 0.09F);
      this.colors[2] = this.color(age * 0.7F + 0.1F, now, Math.min(1.0F, fade * bright * 0.6F));
      this.colors[3] = ColorUtil.lerp(edgeColor, ColorUtil.withAlpha(-1, edgeColor >>> 24), 0.65F * (1.0F - age));
      this.colors[4] = edgeColor & 16777215;
   }

   private void along(float position, Vector3f out) {
      out.set(this.base).lerp(this.tip, position);
   }

   private static void spline(float[] data, int p0, int p1, int p2, int p3, int offset, float t, Vector3f out) {
      float t2 = t * t;
      float t3 = t2 * t;
      float w0 = -0.5F * t3 + t2 - 0.5F * t;
      float w1 = 1.5F * t3 - 2.5F * t2 + 1.0F;
      float w2 = -1.5F * t3 + 2.0F * t2 + 0.5F * t;
      float w3 = 0.5F * t3 - 0.5F * t2;
      out.set(
         w0 * data[p0 + offset] + w1 * data[p1 + offset] + w2 * data[p2 + offset] + w3 * data[p3 + offset],
         w0 * data[p0 + offset + 1] + w1 * data[p1 + offset + 1] + w2 * data[p2 + offset + 1] + w3 * data[p3 + offset + 1],
         w0 * data[p0 + offset + 2] + w1 * data[p1 + offset + 2] + w2 * data[p2 + offset + 2] + w3 * data[p3 + offset + 2]
      );
   }

   private static final class Trail {
      final float[] data = new float[2048];
      int start;
      int count;
      float progress;

      int at(int index) {
         return (this.start + index) % 256 * 8;
      }

      void clear() {
         this.start = this.count = 0;
      }

      void expire(float now, float life) {
         while (this.count > 0 && now - this.data[this.at(0) + 6] > life) {
            this.start = (this.start + 1) % 256;
            this.count--;
         }
      }

      void push(Vector3f base, Vector3f tip, float time, float bladeLength, boolean fresh) {
         float energy = -1.0F;
         if (this.count > 0 && !fresh) {
            int last = this.at(this.count - 1);
            float elapsed = time - this.data[last + 6];
            if (elapsed <= 0.0F) {
               return;
            }

            float speed = Vector3f.distance(tip.x, tip.y, tip.z, this.data[last + 3], this.data[last + 4], this.data[last + 5])
               / Math.max(bladeLength, 1.0E-4F)
               / elapsed;
            energy = Mth.clamp((speed - 5.0F) / 6.0F, 0.0F, 1.0F);
         }

         if (this.count == 256) {
            this.start = (this.start + 1) % 256;
            this.count--;
         }

         int slot = this.at(this.count++);
         this.data[slot] = base.x;
         this.data[slot + 1] = base.y;
         this.data[slot + 2] = base.z;
         this.data[slot + 3] = tip.x;
         this.data[slot + 4] = tip.y;
         this.data[slot + 5] = tip.z;
         this.data[slot + 6] = time;
         this.data[slot + 7] = energy;
      }
   }
}
