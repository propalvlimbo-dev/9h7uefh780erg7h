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
   private static final double X_ROTATION_AMPLITUDE_DEGREES = 4.5;
   private static final double X_ROTATION_FREQUENCY = 0.53;
   private static final double Z_ROTATION_AMPLITUDE_DEGREES = 3.5;
   private static final double Z_ROTATION_FREQUENCY = 0.37;
   private static final double Z_ROTATION_OFFSET_DEGREES = 4.0;

   public Vec3 getRotationDegrees(float partialTick) {
      double interpolatedPhase = WingAnimationMath.lerpClamped(this.previousPhase, this.phase, partialTick);
      double breathingPhase = interpolatedPhase * (X_ROTATION_FREQUENCY / (double)1.0F);
      return new Vec3(
         Math.sin(breathingPhase) * X_ROTATION_AMPLITUDE_DEGREES,
         Math.sin(interpolatedPhase) * Y_ROTATION_AMPLITUDE_DEGREES - Y_ROTATION_OFFSET_DEGREES,
         Math.cos(interpolatedPhase * Z_ROTATION_FREQUENCY) * Z_ROTATION_AMPLITUDE_DEGREES + Z_ROTATION_OFFSET_DEGREES
      );
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
