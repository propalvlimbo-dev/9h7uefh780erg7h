package sg.render;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import net.minecraft.world.phys.Vec3;
import sg.ec.WingAnimationMath;

public final class AvianWingAnimator implements WingAnimator {
   private final @com.google.gson.annotations.SerializedName("Х")  AvianWingAnimator.WingPose spreadPose = new AvianWingAnimator.SpreadPose();
   private @com.google.gson.annotations.SerializedName("е")  AvianWingAnimator.WingPose activePose = new AvianWingAnimator.FoldedIdlePose();
   private @com.google.gson.annotations.SerializedName("Дъ")  float previousPhase;
   private @com.google.gson.annotations.SerializedName("ДМ")  float phase;

   private float getInterpolatedPhase(float partialTick) {
      return WingAnimationMath.lerpClamped(this.previousPhase, this.phase, partialTick);
   }

   @Override
   public void setFoldedIdle() {
      this.transitionToPose(new AvianWingAnimator.FoldedIdlePose(), 18);
   }

   public Vec3 getSecondaryRotationDegrees(int segmentIndex, float partialTick) {
      return this.activePose.getSecondaryRotationDegrees(segmentIndex, partialTick);
   }

   private void advancePhase(float phaseStep) {
      this.phase += phaseStep;
   }

   private void setPose(AvianWingAnimator.WingPose pose) {
      this.activePose.onReplaced();
      this.activePose = pose;
   }

   @Override
   public void tick() {
      this.previousPhase = this.phase;
      this.advancePhase(this.activePose.tickAndGetPhaseStep());
   }

   float getSegmentInfluence(int segmentIndex) {
      return Math.min(Math.abs(segmentIndex - 1), 2) / 2.0F;
   }

   @Override
   public void setFlapping() {
      this.transitionToPose(new AvianWingAnimator.FlappingPose(), 20);
   }

   @Override
   public void setExtendedFlapping() {
      this.transitionToPose(new AvianWingAnimator.ExtendedFlappingPose(), 2);
   }

   private void transitionToPose(AvianWingAnimator.WingPose targetPose, int durationTicks) {
      this.setPose(new AvianWingAnimator.TransitionPose(this.activePose, targetPose, durationTicks));
   }

   public Vec3 getPrimaryRotationDegrees(int segmentIndex, float partialTick) {
      return this.activePose.getPrimaryRotationDegrees(segmentIndex, partialTick);
   }

   @Override
   public void setFalling() {
      this.transitionToPose(new AvianWingAnimator.FallingPose(), 8);
   }

   @Override
   public void setGliding() {
      this.transitionToPose(new AvianWingAnimator.GlidingPose(), 60);
   }

   private final class GlidingPose implements AvianWingAnimator.WingPose {
      private final @com.google.gson.annotations.SerializedName("В")  SimplexNoise noise;
      private @com.google.gson.annotations.SerializedName("Хй")  int ageTicks;
      private static final long NOISE_SEED = 1234L;
      private static final double Y_ROTATION_AMPLITUDE_DEGREES = 5.0;
      private static final double Y_ROTATION_OFFSET_DEGREES = 14.0;
      private static final double NOISE_TIME_SCALE = 0.17;
      private static final double NOISE_SEGMENT_SCALE = 0.13;
      private static final double SECONDARY_X_NOISE_AMPLITUDE_DEGREES = 1.25;
      private static final float PHASE_STEP_RADIANS = 0.045F;

      @Override
      public Vec3 getPrimaryRotationDegrees(int segmentIndex, float partialTick) {
         float segmentInfluence = AvianWingAnimator.this.getSegmentInfluence(segmentIndex);
         float phase = AvianWingAnimator.this.getInterpolatedPhase(partialTick);
         double yOffsetDegrees = (Math.sin(phase) * Y_ROTATION_AMPLITUDE_DEGREES - Y_ROTATION_OFFSET_DEGREES) * segmentInfluence;
         return AvianWingAnimator.this.spreadPose.getPrimaryRotationDegrees(segmentIndex, partialTick).add(0.0, yOffsetDegrees, 0.0);
      }

      @Override
      public float tickAndGetPhaseStep() {
         this.ageTicks++;
         return PHASE_STEP_RADIANS;
      }

      @Override
      public Vec3 getSecondaryRotationDegrees(int segmentIndex, float partialTick) {
         double noiseDegrees = this.noise.getValue((this.ageTicks + partialTick) * NOISE_TIME_SCALE, segmentIndex * NOISE_SEGMENT_SCALE) * SECONDARY_X_NOISE_AMPLITUDE_DEGREES;
         return AvianWingAnimator.this.spreadPose.getSecondaryRotationDegrees(segmentIndex, partialTick).add(noiseDegrees, 0.0, 0.0);
      }

      private GlidingPose() {
         this.noise = new SimplexNoise(RandomSource.create(NOISE_SEED));
      }
   }

   private interface WingPose {
      default void onReplaced() {
      }

      float tickAndGetPhaseStep();

      Vec3 getSecondaryRotationDegrees(int segmentIndex, float partialTick);

      Vec3 getPrimaryRotationDegrees(int segmentIndex, float partialTick);
   }

   private static final class SpreadPose implements AvianWingAnimator.WingPose {
      private final @com.google.gson.annotations.SerializedName("В")  AvianWingAnimator.SegmentTransforms primaryRotations;
      private final @com.google.gson.annotations.SerializedName("Я")  AvianWingAnimator.SegmentTransforms secondaryRotations;
      private static final double PRIMARY_0_Y_DEGREES = -23.5;
      private static final double PRIMARY_0_Z_DEGREES = -16.0;
      private static final double PRIMARY_1_Y_DEGREES = 13.0;
      private static final double PRIMARY_1_Z_DEGREES = 29.0;
      private static final double PRIMARY_2_Y_DEGREES = 12.0;
      private static final double PRIMARY_2_Z_DEGREES = -28.0;
      private static final double PRIMARY_3_Y_DEGREES = 4.0;
      private static final double PRIMARY_3_Z_DEGREES = 18.3;
      private static final double SECONDARY_0_Z_DEGREES = 23.48;

      @Override
      public float tickAndGetPhaseStep() {
         return 0.0F;
      }

      @Override
      public Vec3 getPrimaryRotationDegrees(int segmentIndex, float partialTick) {
         return this.primaryRotations.getRotationDegrees(segmentIndex);
      }

      @Override
      public Vec3 getSecondaryRotationDegrees(int segmentIndex, float partialTick) {
         return this.secondaryRotations.getRotationDegrees(segmentIndex);
      }

      private SpreadPose() {
         this.primaryRotations = AvianWingAnimator.SegmentTransforms.builder().putRotationDegrees(0, 0.0, PRIMARY_0_Y_DEGREES, PRIMARY_0_Z_DEGREES).putRotationDegrees(1, 0.0, PRIMARY_1_Y_DEGREES, PRIMARY_1_Z_DEGREES).putRotationDegrees(2, 0.0, PRIMARY_2_Y_DEGREES, PRIMARY_2_Z_DEGREES).putRotationDegrees(3, 0.0, PRIMARY_3_Y_DEGREES, PRIMARY_3_Z_DEGREES).build();
         this.secondaryRotations = AvianWingAnimator.SegmentTransforms.builder().putRotationDegrees(0, 0.0, 0.0, SECONDARY_0_Z_DEGREES).build();
      }
   }

   private final class TransitionPose implements AvianWingAnimator.WingPose {
      private final @com.google.gson.annotations.SerializedName("В")  AvianWingAnimator.WingPose sourcePose;
      private final @com.google.gson.annotations.SerializedName("Я")  AvianWingAnimator.WingPose targetPose;
      private final @com.google.gson.annotations.SerializedName("еф")  int durationTicks;
      private @com.google.gson.annotations.SerializedName("еи")  int previousAgeTicks;
      private @com.google.gson.annotations.SerializedName("е9")  int ageTicks;
      private @com.google.gson.annotations.SerializedName("Я衣")  boolean activateTargetOnCompletion = true;

      private TransitionPose(AvianWingAnimator.WingPose sourcePose, AvianWingAnimator.WingPose targetPose, int durationTicks) {
         this.sourcePose = sourcePose;
         this.targetPose = targetPose;
         this.durationTicks = durationTicks;
      }

      private Vec3 interpolateRotationDegrees(int segmentIndex, float partialTick, boolean primary) {
         Vec3 sourceRotation = primary ? this.sourcePose.getPrimaryRotationDegrees(segmentIndex, partialTick) : this.sourcePose.getSecondaryRotationDegrees(segmentIndex, partialTick);
         Vec3 targetRotation = primary ? this.targetPose.getPrimaryRotationDegrees(segmentIndex, partialTick) : this.targetPose.getSecondaryRotationDegrees(segmentIndex, partialTick);
         float blend = WingAnimationMath.easeInOutSine(WingAnimationMath.lerpClamped(this.previousAgeTicks, this.ageTicks, partialTick) / this.durationTicks);
         return new Vec3(WingAnimationMath.lerpDegrees(sourceRotation.x, targetRotation.x, blend), WingAnimationMath.lerpDegrees(sourceRotation.y, targetRotation.y, blend), WingAnimationMath.lerpDegrees(sourceRotation.z, targetRotation.z, blend));
      }

      @Override
      public float tickAndGetPhaseStep() {
         this.previousAgeTicks = this.ageTicks;
         float sourcePhaseStep = this.sourcePose.tickAndGetPhaseStep();
         float targetPhaseStep = this.targetPose.tickAndGetPhaseStep();
         float phaseStep = WingAnimationMath.lerpClamped(sourcePhaseStep, targetPhaseStep, (float)this.ageTicks / this.durationTicks);
         if (this.ageTicks < this.durationTicks) {
            this.ageTicks++;
         } else if (this.activateTargetOnCompletion) {
            AvianWingAnimator.this.setPose(this.targetPose);
         }

         return phaseStep;
      }

      @Override
      public Vec3 getPrimaryRotationDegrees(int segmentIndex, float partialTick) {
         return this.interpolateRotationDegrees(segmentIndex, partialTick, true);
      }

      @Override
      public void onReplaced() {
         this.activateTargetOnCompletion = false;
      }

      @Override
      public Vec3 getSecondaryRotationDegrees(int segmentIndex, float partialTick) {
         return this.interpolateRotationDegrees(segmentIndex, partialTick, false);
      }
   }

   private final class FlappingPose implements AvianWingAnimator.WingPose {
      private @com.google.gson.annotations.SerializedName("Ъз")  int ageTicks;
      private static final float SEGMENT_PHASE_OFFSET_RADIANS = 1.2F;
      private static final double X_PHASE_OFFSET_RADIANS = (float) (Math.PI / 2);
      private static final double X_SINE_DIVISOR = 2.0;
      private static final double X_ROTATION_AMPLITUDE_DEGREES = 16.0;
      private static final double X_ROTATION_OFFSET_DEGREES = 8.0;
      private static final double Y_ROTATION_AMPLITUDE_DEGREES = 26.0;
      private static final double Y_ROTATION_OFFSET_DEGREES = 12.0;
      private static final float Y_MODULATION_PHASE_OFFSET_RADIANS = (float) Math.PI;
      private static final double Y_MODULATION_DIVISOR = 2.0;
      private static final float INITIAL_PHASE_STEP_RADIANS = 0.375F;
      private static final float SETTLED_PHASE_STEP_RADIANS = 0.225F;
      private static final float PHASE_SETTLE_TICKS = 100.0F;

      @Override
      public Vec3 getSecondaryRotationDegrees(int segmentIndex, float partialTick) {
         return AvianWingAnimator.this.spreadPose.getSecondaryRotationDegrees(segmentIndex, partialTick);
      }

      @Override
      public Vec3 getPrimaryRotationDegrees(int segmentIndex, float partialTick) {
         float segmentInfluence = AvianWingAnimator.this.getSegmentInfluence(segmentIndex);
         float phase = AvianWingAnimator.this.getInterpolatedPhase(partialTick);
         float segmentPhase = phase - segmentInfluence * SEGMENT_PHASE_OFFSET_RADIANS;
         double xOffsetDegrees = (Math.sin(segmentPhase + X_PHASE_OFFSET_RADIANS) - 1.0) / X_SINE_DIVISOR * X_ROTATION_AMPLITUDE_DEGREES + X_ROTATION_OFFSET_DEGREES;
         double yOffsetDegrees = (Math.sin(segmentPhase) * Y_ROTATION_AMPLITUDE_DEGREES + Y_ROTATION_OFFSET_DEGREES) * (1.0 - segmentInfluence * (Math.min(Math.sin(segmentPhase + Y_MODULATION_PHASE_OFFSET_RADIANS), 0.0) / Y_MODULATION_DIVISOR + 1.0) * Math.sin(phase));
         return AvianWingAnimator.this.spreadPose.getPrimaryRotationDegrees(segmentIndex, partialTick).add(xOffsetDegrees, yOffsetDegrees, 0.0);
      }

      @Override
      public float tickAndGetPhaseStep() {
         float phaseStep = WingAnimationMath.lerpClamped(INITIAL_PHASE_STEP_RADIANS, SETTLED_PHASE_STEP_RADIANS, this.ageTicks / PHASE_SETTLE_TICKS);
         if (this.ageTicks < 100) {
            this.ageTicks++;
         }

         return phaseStep;
      }
   }

   private final class ExtendedFlappingPose implements AvianWingAnimator.WingPose {
      private static final float SEGMENT_PHASE_OFFSET_RADIANS = 1.2F;
      private static final double X_PHASE_OFFSET_RADIANS = (float) (Math.PI / 2);
      private static final double X_SINE_DIVISOR = 2.0;
      private static final double X_ROTATION_AMPLITUDE_DEGREES = 20.0;
      private static final double X_ROTATION_OFFSET_DEGREES = 50.0;
      private static final double Y_ROTATION_AMPLITUDE_DEGREES = 20.0;
      private static final double Y_ROTATION_OFFSET_DEGREES = 14.0;
      private static final float Y_MODULATION_PHASE_OFFSET_RADIANS = (float) Math.PI;
      private static final double Y_MODULATION_DIVISOR = 2.0;
      private static final double Z_ROTATION_OFFSET_DEGREES = 4.0;
      private static final float PHASE_STEP_RADIANS = 0.67F;

      @Override
      public Vec3 getPrimaryRotationDegrees(int segmentIndex, float partialTick) {
         float segmentInfluence = AvianWingAnimator.this.getSegmentInfluence(segmentIndex + 1);
         float phase = AvianWingAnimator.this.getInterpolatedPhase(partialTick);
         float segmentPhase = phase - segmentInfluence * SEGMENT_PHASE_OFFSET_RADIANS;
         double xOffsetDegrees = (Math.sin(segmentPhase + X_PHASE_OFFSET_RADIANS) - 1.0) / X_SINE_DIVISOR * X_ROTATION_AMPLITUDE_DEGREES + (1.0 - segmentInfluence) * X_ROTATION_OFFSET_DEGREES;
         double yOffsetDegrees = (Math.sin(segmentPhase) * Y_ROTATION_AMPLITUDE_DEGREES + (1.0 - segmentInfluence) * Y_ROTATION_OFFSET_DEGREES) * (1.0 - segmentInfluence * (Math.min(Math.sin(segmentPhase + Y_MODULATION_PHASE_OFFSET_RADIANS), 0.0) / Y_MODULATION_DIVISOR + 1.0) * Math.sin(phase));
         return AvianWingAnimator.this.spreadPose.getPrimaryRotationDegrees(segmentIndex, partialTick).add(xOffsetDegrees, yOffsetDegrees, Z_ROTATION_OFFSET_DEGREES);
      }

      @Override
      public Vec3 getSecondaryRotationDegrees(int segmentIndex, float partialTick) {
         return AvianWingAnimator.this.spreadPose.getSecondaryRotationDegrees(segmentIndex, partialTick);
      }

      @Override
      public float tickAndGetPhaseStep() {
         return PHASE_STEP_RADIANS;
      }
   }

   private final class FoldedIdlePose implements AvianWingAnimator.WingPose {
      private final @com.google.gson.annotations.SerializedName("Х")  AvianWingAnimator.SegmentTransforms primaryRotations;
      @SerializedName("е")
      private final AvianWingAnimator.SegmentTransforms secondaryRotations;
      private static final double PRIMARY_0_X_DEGREES = 40.0;
      private static final double PRIMARY_0_Y_DEGREES = -60.0;
      private static final double PRIMARY_0_Z_DEGREES = -50.0;
      private static final double PRIMARY_1_X_DEGREES = 72.0;
      private static final double PRIMARY_1_Y_DEGREES = 10.0;
      private static final double PRIMARY_1_Z_DEGREES = 100.0;
      private static final double PRIMARY_2_Y_DEGREES = -10.0;
      private static final double PRIMARY_2_Z_DEGREES = -120.0;
      private static final double PRIMARY_3_X_DEGREES = 10.0;
      private static final double PRIMARY_3_Z_DEGREES = 100.0;
      private static final double SECONDARY_0_X_DEGREES = 10.0;
      private static final double SECONDARY_0_Y_DEGREES = 20.0;
      private static final double SECONDARY_0_Z_DEGREES = 23.48;
      private static final double SECONDARY_1_Y_DEGREES = 20.0;
      private static final double SECONDARY_1_Z_DEGREES = -70.0;
      private static final double SECONDARY_2_Y_DEGREES = 10.0;
      private static final double SECONDARY_2_Z_DEGREES = 40.0;
      private static final double SECONDARY_3_X_DEGREES = -20.0;
      private static final double SECONDARY_3_Z_DEGREES = 20.0;
      private static final double PRIMARY_SEGMENT_PHASE_OFFSET_RADIANS = 0.6;
      private static final double PRIMARY_X_SWAY_AMPLITUDE_DEGREES = 5.0;
      private static final double PRIMARY_Y_SWAY_AMPLITUDE_DEGREES = 16.0;
      private static final double SECONDARY_SEGMENT_PHASE_OFFSET_RADIANS = 0.6;
      private static final double SECONDARY_X_SWAY_AMPLITUDE_DEGREES = 4.0;
      private static final double SECONDARY_Y_SWAY_AMPLITUDE_DEGREES = 11.0;
      private static final float PHASE_STEP_RADIANS = 0.06F;

      @Override
      public Vec3 getSecondaryRotationDegrees(int segmentIndex, float partialTick) {
         float segmentInfluence = AvianWingAnimator.this.getSegmentInfluence(segmentIndex);
         float phase = AvianWingAnimator.this.getInterpolatedPhase(partialTick);
         double sway = Math.sin(phase - segmentInfluence * SECONDARY_SEGMENT_PHASE_OFFSET_RADIANS);
         return this.secondaryRotations.getRotationDegrees(segmentIndex).add(sway * SECONDARY_X_SWAY_AMPLITUDE_DEGREES * segmentInfluence, -sway * SECONDARY_Y_SWAY_AMPLITUDE_DEGREES * segmentInfluence, 0.0);
      }

      @Override
      public float tickAndGetPhaseStep() {
         return PHASE_STEP_RADIANS;
      }

      private FoldedIdlePose() {
         this.primaryRotations = AvianWingAnimator.SegmentTransforms.builder().putRotationDegrees(0, PRIMARY_0_X_DEGREES, PRIMARY_0_Y_DEGREES, PRIMARY_0_Z_DEGREES).putRotationDegrees(1, PRIMARY_1_X_DEGREES, PRIMARY_1_Y_DEGREES, PRIMARY_1_Z_DEGREES).putRotationDegrees(2, 0.0, PRIMARY_2_Y_DEGREES, PRIMARY_2_Z_DEGREES).putRotationDegrees(3, PRIMARY_3_X_DEGREES, 0.0, PRIMARY_3_Z_DEGREES).build();
         this.secondaryRotations = AvianWingAnimator.SegmentTransforms.builder().putRotationDegrees(0, SECONDARY_0_X_DEGREES, SECONDARY_0_Y_DEGREES, SECONDARY_0_Z_DEGREES).putRotationDegrees(1, 0.0, SECONDARY_1_Y_DEGREES, SECONDARY_1_Z_DEGREES).putRotationDegrees(2, 0.0, SECONDARY_2_Y_DEGREES, SECONDARY_2_Z_DEGREES).putRotationDegrees(3, SECONDARY_3_X_DEGREES, 0.0, SECONDARY_3_Z_DEGREES).build();
      }

      @Override
      public Vec3 getPrimaryRotationDegrees(int segmentIndex, float partialTick) {
         float segmentInfluence = AvianWingAnimator.this.getSegmentInfluence(segmentIndex);
         float phase = AvianWingAnimator.this.getInterpolatedPhase(partialTick);
         double sway = Math.sin(phase - segmentInfluence * PRIMARY_SEGMENT_PHASE_OFFSET_RADIANS);
         return this.primaryRotations.getRotationDegrees(segmentIndex).add(sway * PRIMARY_X_SWAY_AMPLITUDE_DEGREES * segmentInfluence, sway * PRIMARY_Y_SWAY_AMPLITUDE_DEGREES * segmentInfluence, 0.0);
      }
   }

   private static final class SegmentTransforms {
      private final @com.google.gson.annotations.SerializedName("В")  ImmutableMap<Integer, Vec3> rotationsBySegment;

      private SegmentTransforms(ImmutableMap<Integer, Vec3> rotationsBySegment) {
         this.rotationsBySegment = rotationsBySegment;
      }

      public Vec3 getRotationDegrees(int segmentIndex) {
         return this.rotationsBySegment.getOrDefault(segmentIndex, Vec3.ZERO);
      }

      private static AvianWingAnimator.SegmentTransforms.Builder builder() {
         return new AvianWingAnimator.SegmentTransforms.Builder();
      }

      private static final class Builder {
         private final @com.google.gson.annotations.SerializedName("В")  ImmutableMap.Builder<Integer, Vec3> rotations = ImmutableMap.builder();

         private AvianWingAnimator.SegmentTransforms build() {
            return new AvianWingAnimator.SegmentTransforms(this.rotations.build());
         }

         private AvianWingAnimator.SegmentTransforms.Builder putRotationDegrees(int segmentIndex, double xDegrees, double yDegrees, double zDegrees) {
            this.rotations.put(segmentIndex, new Vec3(xDegrees, yDegrees, zDegrees));
            return this;
         }
      }
   }

   private static final class FallingPose implements AvianWingAnimator.WingPose {
      private final @com.google.gson.annotations.SerializedName("Я")  SimplexNoise noise;
      private final @com.google.gson.annotations.SerializedName("А")  AvianWingAnimator.SegmentTransforms primaryRotations;
      private @com.google.gson.annotations.SerializedName("ЪЯ")  int ageTicks;
      private static final long NOISE_SEED = 1234L;
      private static final double PRIMARY_0_X_DEGREES = 30.0;
      private static final double PRIMARY_0_Y_DEGREES = -23.0;
      private static final double PRIMARY_0_Z_DEGREES = -50.0;
      private static final double PRIMARY_1_X_DEGREES = -10.0;
      private static final double PRIMARY_1_Y_DEGREES = 5.0;
      private static final double PRIMARY_1_Z_DEGREES = -10.0;
      private static final double PRIMARY_2_X_DEGREES = -30.0;
      private static final double PRIMARY_2_Y_DEGREES = -20.0;
      private static final double PRIMARY_2_Z_DEGREES = -20.0;
      private static final double PRIMARY_3_X_DEGREES = -20.0;
      private static final double PRIMARY_3_Z_DEGREES = 20.0;
      private static final double PRIMARY_NOISE_TIME_SCALE = 0.18;
      private static final double PRIMARY_NOISE_SEGMENT_SCALE = 0.13;
      private static final double PRIMARY_NOISE_AMPLITUDE_DEGREES = 0.92;
      private static final double SECONDARY_NOISE_TIME_SCALE = 0.2;
      private static final double SECONDARY_NOISE_SEGMENT_SCALE = 0.13;
      private static final double SECONDARY_NOISE_AMPLITUDE_DEGREES = 1.75;
      private static final double SECONDARY_Y_NOISE_MULTIPLIER = 4.0;

      @Override
      public Vec3 getSecondaryRotationDegrees(int segmentIndex, float partialTick) {
         double noiseDegrees = this.noise.getValue((this.ageTicks + partialTick) * SECONDARY_NOISE_TIME_SCALE, segmentIndex * SECONDARY_NOISE_SEGMENT_SCALE) * SECONDARY_NOISE_AMPLITUDE_DEGREES;
         return new Vec3(-noiseDegrees, noiseDegrees * SECONDARY_Y_NOISE_MULTIPLIER, 0.0);
      }

      @Override
      public Vec3 getPrimaryRotationDegrees(int segmentIndex, float partialTick) {
         double noiseDegrees = this.noise.getValue((this.ageTicks + partialTick) * PRIMARY_NOISE_TIME_SCALE, segmentIndex * PRIMARY_NOISE_SEGMENT_SCALE) * PRIMARY_NOISE_AMPLITUDE_DEGREES * (segmentIndex + 1);
         return this.primaryRotations.getRotationDegrees(segmentIndex).add(noiseDegrees, 0.0, noiseDegrees);
      }

      private FallingPose() {
         this.noise = new SimplexNoise(RandomSource.create(NOISE_SEED));
         this.primaryRotations = AvianWingAnimator.SegmentTransforms.builder().putRotationDegrees(0, PRIMARY_0_X_DEGREES, PRIMARY_0_Y_DEGREES, PRIMARY_0_Z_DEGREES).putRotationDegrees(1, PRIMARY_1_X_DEGREES, PRIMARY_1_Y_DEGREES, PRIMARY_1_Z_DEGREES).putRotationDegrees(2, PRIMARY_2_X_DEGREES, PRIMARY_2_Y_DEGREES, PRIMARY_2_Z_DEGREES).putRotationDegrees(3, PRIMARY_3_X_DEGREES, 0.0, PRIMARY_3_Z_DEGREES).build();
      }

      @Override
      public float tickAndGetPhaseStep() {
         this.ageTicks++;
         return 0.0F;
      }
   }
}
