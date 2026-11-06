package dev.rose.module.impl.visual;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;


public final class HandModule extends Module {
   private final HandModule.Hand main;
   private final BoolSetting mirror;
   private final HandModule.Hand off;
   private final ChoiceSetting<HandModule.Swing> swing;
   private final NumberSetting swingSpeed;
   private final NumberSetting swingStrength;
   private final NumberSetting spin;
   private final BoolSetting noEquip;
   private final BoolSetting lowShield;
   private final ChoiceSetting<HandModule.Preset> preset;
   private final float[] swingProgress = new float[2];

   public HandModule() {
      super("hand", Category.VISUALS, false);
      this.section("presets", 58665);
      this.preset = this.add(new ChoiceSetting<HandModule.Preset>("preset", HandModule.Preset.NORMAL) {
         @Override
         public JsonElement toJson() {
            return JsonNull.INSTANCE;
         }

         @Override
         public void fromJson(JsonElement json) {
         }

         @Override
         public boolean isDefault() {
            return true;
         }
      });
      this.section("main_hand", 57815);
      this.main = new HandModule.Hand("main", () -> true);
      this.section("off_hand", 57830);
      this.mirror = this.add(new BoolSetting("mirror", true));
      this.off = new HandModule.Hand("off", () -> !this.mirror.isOn());
      this.section("swing", 58035);
      this.swing = this.add(new ChoiceSetting<>("swing_style", HandModule.Swing.SMOOTH));
      this.swingSpeed = this.add(new NumberSetting("swing_speed", 100.0, 25.0, 300.0, 5.0, "rose.unit.percent"));
      this.swingStrength = this.add(new NumberSetting("swing_strength", 100.0, 0.0, 200.0, 5.0, "rose.unit.percent"))
         .visibleWhen(() -> this.swing.get().ordinal() >= HandModule.Swing.SLIDE.ordinal());
      this.section("extra", 58386);
      this.spin = this.add(new NumberSetting("spin", 0.0, -360.0, 360.0, 10.0, "rose.unit.degrees_per_second"));
      this.noEquip = this.add(new BoolSetting("no_equip", false));
      this.lowShield = this.add(new BoolSetting("low_shield", true));
      this.preset.onChange(this::applyPreset);
   }

   @Override
   public int icon() {
      return 57830;
   }

   private void applyPreset() {
      switch ((HandModule.Preset)this.preset.get()) {
         case NORMAL:
            this.main.set(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 100.0F);
            break;
         case LOWERED:
            this.main.set(0.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.0F, 90.0F);
            break;
         case FAR:
            this.main.set(12.0F, 4.0F, -110.0F, 0.0F, 0.0F, 0.0F, 100.0F);
            break;
         case SIDEWAYS:
            this.main.set(6.0F, 0.0F, -20.0F, 0.0F, 90.0F, 0.0F, 100.0F);
            break;
         case TILTED:
            this.main.set(0.0F, 6.0F, -10.0F, -20.0F, -15.0F, 25.0F, 95.0F);
            break;
         case AURA:
            this.main.set(-28.0F, -14.0F, -10.0F, 0.0F, 0.0F, 0.0F, 140.0F);
            break;
         case SMALL:
            this.main.set(10.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 55.0F);
            break;
         case HUGE:
            this.main.set(-10.0F, -8.0F, -20.0F, 0.0F, 0.0F, 0.0F, 170.0F);
      }

      this.mirror.set(true);
   }

   private HandModule.Pose pose(boolean mainHand) {
      return !mainHand && !this.mirror.isOn() ? this.off.pose() : this.main.pose();
   }

   private static boolean isMainArm(HumanoidArm arm) {
      Minecraft mc = Minecraft.getInstance();
      return mc.player == null || mc.player.getMainArm() == arm;
   }

   public void applyHandTransform(PoseStack pose, AbstractClientPlayer player, InteractionHand hand, ItemStack stack) {
      if (this.isEnabled()) {
         boolean mainHand = hand == InteractionHand.MAIN_HAND;
         HumanoidArm arm = mainHand ? player.getMainArm() : player.getMainArm().getOpposite();
         float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
         HandModule.Pose values = this.pose(mainHand);
         pose.translate(side * values.x() / 100.0F, values.y() / 100.0F, values.z() / 100.0F);
         if (this.lowShield.isOn()
            && stack.is(Items.SHIELD)
            && player.isUsingItem()
            && player.getUsedItemHand() == hand
            && stack.getUseAnimation() == ItemUseAnimation.BLOCK) {
            pose.translate(side * 0.05F, -0.22F, 0.1F);
            pose.mulPose(Axis.XP.rotationDegrees(12.0F));
         }
      }
   }

   public float swingProgress(InteractionHand hand, ItemStack stack, float progress) {
      if (!this.isEnabled()) {
         return progress;
      }

      this.swingProgress[hand.ordinal()] = progress;

      return switch ((HandModule.Swing)this.swing.get()) {
         case VANILLA, CLASSIC -> progress;
         case SMOOTH -> progress < 0.5F ? 2.0F * progress * progress : 1.0F - (float)Math.pow(-2.0F * progress + 2.0F, 2.0) / 2.0F;
         default -> stack.isEmpty() ? progress : 0.0F;
      };
   }

   public boolean skipAttackRotation() {
      return this.isEnabled() && this.swing.get() == HandModule.Swing.CLASSIC;
   }

   public void applyItemTransform(PoseStack pose, LivingEntity entity, boolean leftHand) {
      if (this.isEnabled()) {
         HumanoidArm arm = leftHand ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
         boolean mainHand = isMainArm(arm);
         float side = leftHand ? -1.0F : 1.0F;
         float progress = this.swingProgress[mainHand ? 0 : 1];
         if (progress > 0.0F && this.swing.get().ordinal() >= HandModule.Swing.SLIDE.ordinal()) {
            this.customSwing(pose, side, progress, this.swingStrength.floatValue() / 100.0F);
         }

         HandModule.Pose values = this.pose(mainHand);
         if (values.rotY() != 0.0F) {
            pose.mulPose(Axis.YP.rotationDegrees(side * values.rotY()));
         }

         if (values.rotX() != 0.0F) {
            pose.mulPose(Axis.XP.rotationDegrees(values.rotX()));
         }

         if (values.rotZ() != 0.0F) {
            pose.mulPose(Axis.ZP.rotationDegrees(side * values.rotZ()));
         }

         float speed = this.spin.floatValue();
         if (speed != 0.0F) {
            pose.mulPose(Axis.YP.rotationDegrees(side * ((float)System.nanoTime() / 1.0E9F * speed % 360.0F)));
         }

         float size = values.scale() / 100.0F;
         if (size != 1.0F) {
            pose.scale(size, size, size);
         }
      }
   }

   private void customSwing(PoseStack pose, float side, float p, float k) {
      float wave = Mth.sin(p * (float) Math.PI) * k;
      switch ((HandModule.Swing)this.swing.get()) {
         case SLIDE:
            pose.translate(side * 0.04F * wave, -0.18F * wave, 0.06F * wave);
            pose.mulPose(Axis.XP.rotationDegrees(-28.0F * wave));
            pose.mulPose(Axis.ZP.rotationDegrees(side * 10.0F * wave));
            break;
         case PUNCH: {
            float thrust = Mth.sin(Mth.sqrt(p) * (float) Math.PI) * k;
            pose.translate(side * -0.06F * thrust, 0.04F * thrust, -0.32F * thrust);
            pose.mulPose(Axis.XP.rotationDegrees(-14.0F * thrust));
            break;
         }
         case CHOP:
            pose.translate(0.0F, 0.06F * wave, -0.08F * wave);
            pose.mulPose(Axis.XP.rotationDegrees(-80.0F * wave));
            break;
         case STAB: {
            float thrust = Mth.sin(Mth.sqrt(p) * (float) Math.PI) * k;
            pose.translate(side * -0.03F * thrust, 0.02F * thrust, -0.48F * thrust);
            pose.mulPose(Axis.YP.rotationDegrees(side * -12.0F * thrust));
            break;
         }
         case SWIPE:
            pose.translate(side * -0.16F * wave, 0.02F * wave, -0.06F * wave);
            pose.mulPose(Axis.YP.rotationDegrees(side * -70.0F * wave));
            pose.mulPose(Axis.ZP.rotationDegrees(side * 22.0F * wave));
            break;
         case SPIN:
            float eased = 1.0F - (1.0F - p) * (1.0F - p);
            pose.mulPose(Axis.YP.rotationDegrees(side * 360.0F * eased));
            pose.translate(0.0F, 0.05F * wave, 0.0F);
            break;
         case WAVE:
            pose.translate(0.0F, 0.05F * wave, 0.0F);
            pose.mulPose(Axis.ZP.rotationDegrees(side * 32.0F * Mth.sin(p * (float) (Math.PI * 2)) * k));
      }
   }

   public float equipProgress(float vanilla) {
      return this.isEnabled() && this.noEquip.isOn() ? 0.0F : vanilla;
   }

   public int swingDuration(int vanilla) {
      return !this.isEnabled() ? vanilla : Math.max(1, Math.round(vanilla * 100.0F / this.swingSpeed.floatValue()));
   }

   private final class Hand {
      final NumberSetting x;
      final NumberSetting y;
      final NumberSetting z;
      final NumberSetting rotX;
      final NumberSetting rotY;
      final NumberSetting rotZ;
      final NumberSetting scale;

      Hand(String prefix, BooleanSupplier visible) {
         this.x = HandModule.this.add(new NumberSetting(prefix + "_x", 0.0, -150.0, 150.0, 1.0, "")).visibleWhen(visible);
         this.y = HandModule.this.add(new NumberSetting(prefix + "_y", 0.0, -150.0, 150.0, 1.0, "")).visibleWhen(visible);
         this.z = HandModule.this.add(new NumberSetting(prefix + "_z", 0.0, -250.0, 100.0, 1.0, "")).visibleWhen(visible);
         this.rotX = HandModule.this.add(new NumberSetting(prefix + "_rot_x", 0.0, -180.0, 180.0, 1.0, "rose.unit.degrees")).visibleWhen(visible);
         this.rotY = HandModule.this.add(new NumberSetting(prefix + "_rot_y", 0.0, -180.0, 180.0, 1.0, "rose.unit.degrees")).visibleWhen(visible);
         this.rotZ = HandModule.this.add(new NumberSetting(prefix + "_rot_z", 0.0, -180.0, 180.0, 1.0, "rose.unit.degrees")).visibleWhen(visible);
         this.scale = HandModule.this.add(new NumberSetting(prefix + "_scale", 100.0, 10.0, 250.0, 5.0, "rose.unit.percent")).visibleWhen(visible);
      }

      HandModule.Pose pose() {
         return new HandModule.Pose(
            this.x.floatValue(),
            this.y.floatValue(),
            this.z.floatValue(),
            this.rotX.floatValue(),
            this.rotY.floatValue(),
            this.rotZ.floatValue(),
            this.scale.floatValue()
         );
      }

      void set(float px, float py, float pz, float rx, float ry, float rz, float size) {
         this.x.set((double)px);
         this.y.set((double)py);
         this.z.set((double)pz);
         this.rotX.set((double)rx);
         this.rotY.set((double)ry);
         this.rotZ.set((double)rz);
         this.scale.set((double)size);
      }
   }

   private record Pose(float x, float y, float z, float rotX, float rotY, float rotZ, float scale) {
   }

   public enum Preset {
      NORMAL,
      LOWERED,
      FAR,
      SIDEWAYS,
      TILTED,
      AURA,
      SMALL,
      HUGE;
   }

   public enum Swing {
      VANILLA,
      SMOOTH,
      CLASSIC,
      SLIDE,
      PUNCH,
      CHOP,
      STAB,
      SWIPE,
      SPIN,
      WAVE;
   }
}
