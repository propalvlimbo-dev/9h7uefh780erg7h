package sg.render;

import net.minecraft.world.phys.Vec3;
import sg.ec.WingAnimationMath;

public final class SimpleWingAnimator implements WingAnimator {
   private @com.google.gson.annotations.SerializedName("Я5")  float targetPhaseStep;
   private @com.google.gson.annotations.SerializedName("Яв")  float phaseStep;
   private @com.google.gson.annotations.SerializedName("Яъ")  float previousPhase;
   private @com.google.gson.annotations.SerializedName("ЯМ")  float phase;
   private static final float INITIAL_PHASE_STEP_RADIANS = 0.05F;
   private static final float IDLE_PHASE_STEP_RADIANS = 0.05F;
   private static final float FLAPPING_PHASE_STEP_RADIANS = 1.2F;
   private static final double Y_ROTATION_AMPLITUDE_DEGREES = 35.0;
   private static final double Y_ROTATION_OFFSET_DEGREES = 42.0;
   private static final float PHASE_STEP_SMOOTHING_FACTOR = 0.4F;

   public Vec3 getRotationDegrees(float partialTick) {
      return new Vec3(0.0, Math.sin(WingAnimationMath.lerpClamped(this.previousPhase, this.phase, partialTick)) * Y_ROTATION_AMPLITUDE_DEGREES - Y_ROTATION_OFFSET_DEGREES, 0.0);
   }

   @Override
   public void setFlapping() {
      this.targetPhaseStep = FLAPPING_PHASE_STEP_RADIANS;
   }

   public SimpleWingAnimator() {
      this.targetPhaseStep = INITIAL_PHASE_STEP_RADIANS;
   }

   @Override
   public void setFoldedIdle() {
      this.targetPhaseStep = IDLE_PHASE_STEP_RADIANS;
   }

   @Override
   public void setGliding() {
      this.setFlapping();
   }

   @Override
   public void setExtendedFlapping() {
      this.setFlapping();
   }

   @Override
   public void tick() {
      this.previousPhase = this.phase;
      this.phase = this.phase + this.phaseStep;
      this.phaseStep = this.phaseStep + (this.targetPhaseStep - this.phaseStep) * PHASE_STEP_SMOOTHING_FACTOR;
   }

   @Override
   public void setFalling() {
      this.setFoldedIdle();
   }
}
