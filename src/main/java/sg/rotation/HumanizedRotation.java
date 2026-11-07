package sg.rotation;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.client.RoseClient;
import sg.rotation.RotationStrategy;

public final class HumanizedRotation extends RotationStrategy {
   private static final int пг = Integer.MIN_VALUE;
   private static final long ХЦ = 350000000L;
   private static final float ДI = 0.002F;
   private static final float ДТ = 2.25F;
   private static final float ДО = 1.65F;
   private static final float ДФ = 3.35F;
   private static final float ДЙ = 2.45F;
   private static final float Дк = 0.48F;
   private static final float Дг = 0.34F;
   private final @com.google.gson.annotations.SerializedName("т")  Random random = new Random();
   private @com.google.gson.annotations.SerializedName("п0")  int targetEntityId;
   private @com.google.gson.annotations.SerializedName("пх")  int targetTicks;
   private @com.google.gson.annotations.SerializedName("пУ")  int nextAimPointTick;
   private @com.google.gson.annotations.SerializedName("Хч")  long lastUpdateNs;
   private @com.google.gson.annotations.SerializedName("пЗ")  int reactionDelayTicks;
   private @com.google.gson.annotations.SerializedName("е4")  boolean targetJustChanged;
   private @com.google.gson.annotations.SerializedName("еп")  boolean useRandomAimPoint;
   private @com.google.gson.annotations.SerializedName("ЯМ")  double normalizedAimX;
   private @com.google.gson.annotations.SerializedName("ЯН")  double normalizedAimY;
   private @com.google.gson.annotations.SerializedName("Ян")  double normalizedAimZ;
   private @com.google.gson.annotations.SerializedName("еД")  boolean overshootActive;
   private @com.google.gson.annotations.SerializedName("еЪ")  boolean overshootYawDominant;
   private @com.google.gson.annotations.SerializedName("пЕ")  int overshootElapsedTicks;
   private @com.google.gson.annotations.SerializedName("пП")  int overshootDurationTicks;
   private @com.google.gson.annotations.SerializedName("п西")  int settlingTicks;
   private @com.google.gson.annotations.SerializedName("Д0")  float overshootYaw;
   private @com.google.gson.annotations.SerializedName("Дх")  float overshootPitch;
   private @com.google.gson.annotations.SerializedName("ДУ")  float overshootDirection;
   private @com.google.gson.annotations.SerializedName("ет")  boolean lockedToStationaryTarget;
   private @com.google.gson.annotations.SerializedName("ДЗ")  float lockedYaw;
   private @com.google.gson.annotations.SerializedName("ДЕ")  float lockedPitch;
   private @com.google.gson.annotations.SerializedName("ДП")  float yawSweepOffset;
   private @com.google.gson.annotations.SerializedName("Д西")  float yawSweepStart;
   private @com.google.gson.annotations.SerializedName("Дд")  float yawSweepEnd;
   private @com.google.gson.annotations.SerializedName("пд")  int yawSweepTick;
   private @com.google.gson.annotations.SerializedName("пБ")  int yawSweepDuration;
   private @com.google.gson.annotations.SerializedName("еа")  boolean yawSweepActive;
   @SerializedName("В")
   private HumanizedRotation.EasingCurve yawSweepCurve;
   private @com.google.gson.annotations.SerializedName("пС")  int jitterRemainingTicks;
   private @com.google.gson.annotations.SerializedName("пз")  int nextJitterTick;
   private @com.google.gson.annotations.SerializedName("ДБ")  float jitterYaw;
   private @com.google.gson.annotations.SerializedName("ДС")  float jitterPitch;
   private @com.google.gson.annotations.SerializedName("Дз")  float targetJitterYaw;
   private @com.google.gson.annotations.SerializedName("Дц")  float targetJitterPitch;
   private @com.google.gson.annotations.SerializedName("пц")  int pitchDelayTicks;
   private @com.google.gson.annotations.SerializedName("пя")  int nextSpeedVariationTick;
   private @com.google.gson.annotations.SerializedName("Дя")  float speedVariation;
   private @com.google.gson.annotations.SerializedName("Ъс")  float smoothedYawStep;
   private @com.google.gson.annotations.SerializedName("ЪВ")  float smoothedPitchStep;
   private @com.google.gson.annotations.SerializedName("еЮ")  boolean motionSegmentActive;
   private @com.google.gson.annotations.SerializedName("Я")  HumanizedRotation.EasingCurve motionCurve;
   private @com.google.gson.annotations.SerializedName("ЪЯ")  float segmentStartYaw;
   private @com.google.gson.annotations.SerializedName("ЪХ")  float segmentStartPitch;
   private @com.google.gson.annotations.SerializedName("Ъе")  float segmentTargetYaw;
   private @com.google.gson.annotations.SerializedName("ЪА")  float segmentTargetPitch;
   private @com.google.gson.annotations.SerializedName("Ъ4")  float segmentDistance;
   private @com.google.gson.annotations.SerializedName("Ъп")  float segmentProgress;
   private @com.google.gson.annotations.SerializedName("ЪД")  float bezierControlX1;
   private @com.google.gson.annotations.SerializedName("ЪЪ")  float bezierControlY1;
   private @com.google.gson.annotations.SerializedName("Ът")  float bezierControlX2;
   private @com.google.gson.annotations.SerializedName("Ъа")  float bezierControlY2;
   private @com.google.gson.annotations.SerializedName("ЪЮ")  float curveYawBend;
   private @com.google.gson.annotations.SerializedName("ЪЧ")  float curvePitchBend;
   private @com.google.gson.annotations.SerializedName("еЧ")  boolean lateDeceleration;
   private @com.google.gson.annotations.SerializedName("еЛ")  boolean lateJitterScheduled;
   private static final int INITIAL_TARGET_ID = Integer.MIN_VALUE;
   private static final double INITIAL_AIM_X = 0.5;
   private static final double INITIAL_AIM_Y = 0.5;
   private static final double INITIAL_AIM_Z = 0.5;
   private static final int RESET_TARGET_ID = Integer.MIN_VALUE;
   private static final long TARGET_HISTORY_GAP_NS = 350000000L;
   private static final double STATIONARY_TARGET_VELOCITY_SQUARED = 0.002F;
   private static final double CURVE_BEND_PI = Math.PI;
   private static final float FULL_SPEED_ROTATION_DISTANCE = 90.0F;
   private static final float STATIONARY_ROTATION_GAIN = 0.54F;
   private static final float SETTLING_NEAR_ROTATION_GAIN = 0.54F;
   private static final float NEAR_ROTATION_GAIN = 0.43F;
   private static final float FAR_ROTATION_GAIN = 0.78F;
   private static final float MIN_YAW_STEP_LIMIT = 13.0F;
   private static final float MAX_YAW_STEP_LIMIT = 43.0F;
   private static final float MIN_PITCH_STEP_LIMIT = 8.0F;
   private static final float MAX_PITCH_STEP_LIMIT = 24.0F;
   private static final float SETTLING_YAW_STEP_LIMIT = 15.5F;
   private static final float SETTLING_PITCH_STEP_LIMIT = 9.5F;
   private static final float STATIONARY_YAW_STEP_LIMIT = 2.4F;
   private static final float STATIONARY_PITCH_STEP_LIMIT = 1.6F;
   private static final float DELAYED_PITCH_STEP_SCALE = 0.24F;
   private static final float STATIONARY_STEP_BLEND = 0.72F;
   private static final float NEAR_STEP_BLEND = 0.64F;
   private static final float FAR_STEP_BLEND = 0.82F;
   private static final float MIN_SETTLING_STEP_BLEND = 0.8F;
   private static final float SETTLING_YAW_MARGIN = 0.42F;
   private static final float STATIONARY_YAW_MARGIN = 0.035F;
   private static final float NORMAL_YAW_MARGIN = 0.1F;
   private static final float SETTLING_PITCH_MARGIN = 0.24F;
   private static final float STATIONARY_PITCH_MARGIN = 0.025F;
   private static final float NORMAL_PITCH_MARGIN = 0.07F;
   private static final float MIN_PITCH = -90.0F;
   private static final float MAX_PITCH = 90.0F;
   private static final float INITIAL_SWEEP_NEGATIVE_YAW = -15.0F;
   private static final float INITIAL_SWEEP_POSITIVE_YAW = 15.0F;
   private static final float NEGATIVE_SWEEP_ENDPOINT = -15.0F;
   private static final float POSITIVE_SWEEP_ENDPOINT = 15.0F;
   private static final float ALTERNATE_NEGATIVE_SWEEP_ENDPOINT = -15.0F;
   private static final float SWEEP_DISTANCE_SCALE = 1.5F;
   private static final float NEXT_POSITIVE_SWEEP_ENDPOINT = 15.0F;
   private static final float NEXT_NEGATIVE_SWEEP_ENDPOINT = -15.0F;
   private static final double SINE_EASING_PI = Math.PI;
   private static final double SINE_EASING_HALF_FACTOR = 0.5;
   private static final float QUADRATIC_EASING_MIDPOINT = 0.5F;
   private static final float QUADRATIC_EASING_MIRROR_SCALE = -2.0F;
   private static final double QUADRATIC_EASING_POWER = 2.0;
   private static final float QUADRATIC_EASING_END_FACTOR = 0.5F;
   private static final float QUARTIC_EASING_MIDPOINT = 0.5F;
   private static final float QUARTIC_EASING_START_SCALE = 8.0F;
   private static final float QUARTIC_EASING_MIRROR_SCALE = -2.0F;
   private static final double QUARTIC_EASING_POWER = 4.0;
   private static final float QUARTIC_EASING_END_FACTOR = 0.5F;
   private static final float BEZIER_FIRST_BASIS_SCALE = 3.0F;
   private static final float BEZIER_SECOND_BASIS_SCALE = 3.0F;
   private static final float RANDOM_AIM_POINT_CHANCE = 0.2F;
   private static final double MIN_RANDOM_AIM_X = 0.16;
   private static final double MAX_RANDOM_AIM_X = 0.84;
   private static final double MIN_RANDOM_AIM_Y = 0.2;
   private static final double MAX_RANDOM_AIM_Y = 0.82;
   private static final double MIN_RANDOM_AIM_Z = 0.16;
   private static final double MAX_RANDOM_AIM_Z = 0.84;
   private static final float NEAR_AIM_POINT_SELECTION_CHANCE = 0.78F;
   private static final float MIN_OVERSHOOT_ROTATION_DISTANCE = 4.0F;
   private static final float LARGE_OVERSHOOT_ANGLE = 45.0F;
   private static final float LARGE_ANGLE_OVERSHOOT_CHANCE = 0.78F;
   private static final float MEDIUM_OVERSHOOT_ANGLE = 12.0F;
   private static final float MEDIUM_ANGLE_OVERSHOOT_CHANCE = 0.62F;
   private static final float SMALL_ANGLE_OVERSHOOT_CHANCE = 0.44F;
   private static final float REPEATED_TARGET_OVERSHOOT_CHANCE_SCALE = 0.55F;
   private static final float MIN_YAW_OVERSHOOT_FRACTION = 0.035F;
   private static final float MAX_YAW_OVERSHOOT_FRACTION = 0.075F;
   private static final float MIN_YAW_OVERSHOOT = 0.42F;
   private static final float MAX_YAW_OVERSHOOT = 3.8F;
   private static final float MIN_PITCH_OVERSHOOT_FRACTION = 0.025F;
   private static final float MAX_PITCH_OVERSHOOT_FRACTION = 0.06F;
   private static final float MIN_PITCH_OVERSHOOT = 0.16F;
   private static final float MAX_PITCH_OVERSHOOT = 1.25F;
   private static final float OVERSHOOT_DURATION_ANGLE_SCALE = 28.0F;
   private static final float PITCH_DELAY_CHANCE = 0.36F;
   private static final float STATIONARY_LOCK_YAW_ACQUIRE_DISTANCE = 2.25F;
   private static final float STATIONARY_LOCK_PITCH_ACQUIRE_DISTANCE = 1.65F;
   private static final float STATIONARY_LOCK_YAW_STEP_SCALE = 0.25F;
   private static final float STATIONARY_LOCK_PITCH_STEP_SCALE = 0.25F;
   private static final float STATIONARY_LOCK_YAW_RELEASE_DISTANCE = 3.35F;
   private static final float STATIONARY_LOCK_PITCH_RELEASE_DISTANCE = 2.45F;
   private static final float STATIONARY_LOCK_YAW_DEAD_ZONE = 2.25F;
   private static final float STATIONARY_LOCK_YAW_OFFSET = 0.48F;
   private static final float STATIONARY_LOCK_YAW_TRACKING_GAIN = 0.62F;
   private static final float STATIONARY_LOCK_PITCH_DEAD_ZONE = 1.65F;
   private static final float STATIONARY_LOCK_PITCH_OFFSET = 0.34F;
   private static final float STATIONARY_LOCK_PITCH_TRACKING_GAIN = 0.62F;
   private static final float STATIONARY_LOCK_MIN_PITCH = -90.0F;
   private static final float STATIONARY_LOCK_MAX_PITCH = 90.0F;
   private static final float MIN_STATIONARY_YAW_JITTER = 0.08F;
   private static final float MAX_STATIONARY_YAW_JITTER = 0.28F;
   private static final float MIN_NORMAL_YAW_JITTER = 0.12F;
   private static final float MAX_NORMAL_YAW_JITTER = 0.42F;
   private static final float MIN_STATIONARY_PITCH_JITTER = 0.05F;
   private static final float MAX_STATIONARY_PITCH_JITTER = 0.18F;
   private static final float MIN_NORMAL_PITCH_JITTER = 0.07F;
   private static final float MAX_NORMAL_PITCH_JITTER = 0.26F;
   private static final float ACTIVE_YAW_JITTER_BLEND = 0.58F;
   private static final float ACTIVE_PITCH_JITTER_BLEND = 0.54F;
   private static final float INACTIVE_YAW_JITTER_DECAY = 0.46F;
   private static final float INACTIVE_PITCH_JITTER_DECAY = 0.42F;
   private static final float MIN_SPEED_VARIATION = 0.92F;
   private static final float MAX_SPEED_VARIATION = 1.08F;
   private static final float SEGMENT_COMPLETION_THRESHOLD = 0.985F;
   private static final float COMPLETED_SEGMENT_REMAINING_DISTANCE = 2.5F;
   private static final float SEGMENT_TARGET_SHIFT_THRESHOLD = 7.0F;
   private static final float MIN_NEW_SEGMENT_DISTANCE = 0.65F;
   private static final float MIN_CURVE_BEND_FRACTION = 0.008F;
   private static final float MAX_CURVE_BEND_FRACTION = 0.02F;
   private static final float MIN_CURVE_BEND = 0.1F;
   private static final float MAX_CURVE_BEND = 1.45F;
   private static final float MIN_PITCH_BEND_RATIO = 0.72F;
   private static final float MAX_PITCH_BEND_RATIO = 0.96F;
   private static final float PRIMARY_BEZIER_CHOICE_THRESHOLD = 0.34F;
   private static final float PRIMARY_BEZIER_X1_MIN = 0.12F;
   private static final float PRIMARY_BEZIER_X1_MAX = 0.24F;
   private static final float PRIMARY_BEZIER_Y1_MIN = 0.16F;
   private static final float PRIMARY_BEZIER_Y1_MAX = 0.3F;
   private static final float PRIMARY_BEZIER_X2_MIN = 0.48F;
   private static final float PRIMARY_BEZIER_X2_MAX = 0.66F;
   private static final float PRIMARY_BEZIER_Y2_MIN = 0.82F;
   private static final float PRIMARY_BEZIER_Y2_MAX = 0.94F;
   private static final float SECONDARY_BEZIER_CHOICE_THRESHOLD = 0.68F;
   private static final float SECONDARY_BEZIER_X1_MIN = 0.25F;
   private static final float SECONDARY_BEZIER_X1_MAX = 0.38F;
   private static final float SECONDARY_BEZIER_Y1_MIN = 0.02F;
   private static final float SECONDARY_BEZIER_Y1_MAX = 0.11F;
   private static final float SECONDARY_BEZIER_X2_MIN = 0.62F;
   private static final float SECONDARY_BEZIER_X2_MAX = 0.8F;
   private static final float SECONDARY_BEZIER_Y2_MIN = 0.86F;
   private static final float SECONDARY_BEZIER_Y2_MAX = 0.97F;
   private static final float TERTIARY_BEZIER_X1_MIN = 0.17F;
   private static final float TERTIARY_BEZIER_X1_MAX = 0.31F;
   private static final float TERTIARY_BEZIER_Y1_MIN = 0.07F;
   private static final float TERTIARY_BEZIER_Y1_MAX = 0.2F;
   private static final float TERTIARY_BEZIER_X2_MIN = 0.55F;
   private static final float TERTIARY_BEZIER_X2_MAX = 0.75F;
   private static final float TERTIARY_BEZIER_Y2_MIN = 0.84F;
   private static final float TERTIARY_BEZIER_Y2_MAX = 0.96F;
   private static final float LATE_DECELERATION_CHANCE = 0.22F;
   private static final float LATE_BEZIER_MIN_CONTROL_GAP = 0.16F;
   private static final float LATE_BEZIER_X2_MIN = 0.68F;
   private static final float LATE_BEZIER_X2_MAX = 0.84F;
   private static final float LATE_BEZIER_Y2_MIN = 0.972F;
   private static final float LATE_BEZIER_Y2_MAX = 0.995F;
   private static final float MIN_SEGMENT_DISTANCE = 1.0E-4F;
   private static final float LATE_JITTER_PROGRESS_THRESHOLD = 0.86F;
   private static final float BEZIER_DERIVATIVE_EPSILON = 1.0E-4F;
   private static final float LATE_DECELERATION_PROGRESS_THRESHOLD = 0.78F;
   private static final float MIN_LATE_CURVE_SPEED_SCALE = 0.56F;
   private static final float MIN_CURVE_SPEED_SCALE = 0.68F;
   private static final float MAX_CURVE_SPEED_SCALE = 1.28F;
   private static final double SINE_DERIVATIVE_AMPLITUDE = Math.PI / 2;
   private static final double SINE_DERIVATIVE_PI = Math.PI;
   private static final float QUADRATIC_DERIVATIVE_MIDPOINT = 0.5F;
   private static final float QUADRATIC_DERIVATIVE_START_SCALE = 4.0F;
   private static final float QUADRATIC_DERIVATIVE_END_SCALE = 4.0F;
   private static final float QUARTIC_DERIVATIVE_MIDPOINT = 0.5F;
   private static final float QUARTIC_DERIVATIVE_START_SCALE = 32.0F;
   private static final float QUARTIC_DERIVATIVE_END_SCALE = 32.0F;
   private static final float BEZIER_SOLVER_ERROR_EPSILON = 1.0E-4F;
   private static final float BEZIER_SOLVER_DERIVATIVE_EPSILON = 1.0E-4F;
   private static final float UNIT_BEZIER_FIRST_BASIS_SCALE = 3.0F;
   private static final float UNIT_BEZIER_SECOND_BASIS_SCALE = 3.0F;
   private static final float BEZIER_DERIVATIVE_FIRST_SCALE = 3.0F;
   private static final float BEZIER_DERIVATIVE_MIDDLE_SCALE = 6.0F;
   private static final float BEZIER_DERIVATIVE_LAST_SCALE = 3.0F;
   private static final float DEFAULT_BEZIER_X1 = 0.25F;
   private static final float DEFAULT_BEZIER_Y1 = 0.1F;
   private static final float DEFAULT_BEZIER_X2 = 0.7F;
   private static final float DEFAULT_BEZIER_Y2 = 0.9F;
   private static final float STEP_ZERO_EPSILON = 1.0E-4F;
   private static final float REMAINING_ANGLE_EPSILON = 1.0E-4F;
   private static final double AIM_X_INSET_FRACTION = 0.12;
   private static final double MAX_AIM_X_INSET = 0.07;
   private static final double AIM_Y_INSET_FRACTION = 0.09;
   private static final double MAX_AIM_Y_INSET = 0.11;
   private static final double AIM_Z_INSET_FRACTION = 0.12;
   private static final double MAX_AIM_Z_INSET = 0.07;
   private static final float SIGN_RANDOMIZATION_THRESHOLD = 0.05F;
   private static final float RANDOM_NEGATIVE_SIGN = -1.0F;

   public HumanizedRotation(Vec3 targetPosition) {
      super(targetPosition);
      this.targetEntityId = INITIAL_TARGET_ID;
      this.normalizedAimX = INITIAL_AIM_X;
      this.normalizedAimY = INITIAL_AIM_Y;
      this.normalizedAimZ = INITIAL_AIM_Z;
      this.yawSweepCurve = HumanizedRotation.EasingCurve.CUBIC_BEZIER;
      this.speedVariation = 1.0F;
      this.motionCurve = HumanizedRotation.EasingCurve.CUBIC_BEZIER;
   }

   private Vector2f rotationToPoint(Vec3 point) {
      Vec3 eyeToPoint = point.subtract(MINECRAFT_CLIENT.player.getEyePosition());
      double horizontalDistance = Math.hypot(eyeToPoint.x, eyeToPoint.z);
      return new Vector2f((float)Math.toDegrees(Math.atan2(-eyeToPoint.x, eyeToPoint.z)), (float)(-Math.toDegrees(Math.atan2(eyeToPoint.y, horizontalDistance))));
   }

   private void randomizeBezierControls() {
      float curveChoice = this.random.nextFloat();
      if (curveChoice < PRIMARY_BEZIER_CHOICE_THRESHOLD) {
         this.bezierControlX1 = this.randomFloat(PRIMARY_BEZIER_X1_MIN, PRIMARY_BEZIER_X1_MAX);
         this.bezierControlY1 = this.randomFloat(PRIMARY_BEZIER_Y1_MIN, PRIMARY_BEZIER_Y1_MAX);
         this.bezierControlX2 = this.randomFloat(PRIMARY_BEZIER_X2_MIN, PRIMARY_BEZIER_X2_MAX);
         this.bezierControlY2 = this.randomFloat(PRIMARY_BEZIER_Y2_MIN, PRIMARY_BEZIER_Y2_MAX);
      } else if (curveChoice < SECONDARY_BEZIER_CHOICE_THRESHOLD) {
         this.bezierControlX1 = this.randomFloat(SECONDARY_BEZIER_X1_MIN, SECONDARY_BEZIER_X1_MAX);
         this.bezierControlY1 = this.randomFloat(SECONDARY_BEZIER_Y1_MIN, SECONDARY_BEZIER_Y1_MAX);
         this.bezierControlX2 = this.randomFloat(SECONDARY_BEZIER_X2_MIN, SECONDARY_BEZIER_X2_MAX);
         this.bezierControlY2 = this.randomFloat(SECONDARY_BEZIER_Y2_MIN, SECONDARY_BEZIER_Y2_MAX);
      } else {
         this.bezierControlX1 = this.randomFloat(TERTIARY_BEZIER_X1_MIN, TERTIARY_BEZIER_X1_MAX);
         this.bezierControlY1 = this.randomFloat(TERTIARY_BEZIER_Y1_MIN, TERTIARY_BEZIER_Y1_MAX);
         this.bezierControlX2 = this.randomFloat(TERTIARY_BEZIER_X2_MIN, TERTIARY_BEZIER_X2_MAX);
         this.bezierControlY2 = this.randomFloat(TERTIARY_BEZIER_Y2_MIN, TERTIARY_BEZIER_Y2_MAX);
      }

      this.lateDeceleration = this.random.nextFloat() < LATE_DECELERATION_CHANCE;
      this.lateJitterScheduled = false;
      if (this.lateDeceleration) {
         this.bezierControlX2 = this.randomFloat(Math.max(this.bezierControlX1 + LATE_BEZIER_MIN_CONTROL_GAP, LATE_BEZIER_X2_MIN), LATE_BEZIER_X2_MAX);
         this.bezierControlY2 = this.randomFloat(LATE_BEZIER_Y2_MIN, LATE_BEZIER_Y2_MAX);
      }
   }

   private Vector2f selectAimPointRotation(Entity target, Vector2f defaultRotation) {
      return !this.useRandomAimPoint ? defaultRotation : this.rotationToPoint(this.pointInsideBox(target.getBoundingBox(), this.normalizedAimX, this.normalizedAimY, this.normalizedAimZ));
   }

   private void chooseRandomAimPoint(Entity target, Vector2f currentRotation) {
      this.nextAimPointTick = this.targetTicks + 12 + this.random.nextInt(15);
      this.useRandomAimPoint = this.random.nextFloat() < RANDOM_AIM_POINT_CHANCE;
      if (this.useRandomAimPoint) {
         AABB targetBox = target.getBoundingBox();
         ArrayList candidates = new ArrayList(9);

         for (int var5 = 0; var5 < 9; var5++) {
            double normalizedX = this.randomDouble(MIN_RANDOM_AIM_X, MAX_RANDOM_AIM_X);
            double normalizedY = this.randomDouble(MIN_RANDOM_AIM_Y, MAX_RANDOM_AIM_Y);
            double normalizedZ = this.randomDouble(MIN_RANDOM_AIM_Z, MAX_RANDOM_AIM_Z);
            Vector2f candidateRotation = this.rotationToPoint(this.pointInsideBox(targetBox, normalizedX, normalizedY, normalizedZ));
            float yawDistance = Mth.wrapDegrees(candidateRotation.x - currentRotation.x);
            float pitchDistance = candidateRotation.y - currentRotation.y;
            candidates.add(new HumanizedRotation.AimPoint(normalizedX, normalizedY, normalizedZ, (float)Math.hypot(yawDistance, pitchDistance)));
         }

         candidates.sort(Comparator.comparingDouble(HumanizedRotation.AimPoint::rotationDistance));
         int candidateIndex;
         if (this.random.nextFloat() < NEAR_AIM_POINT_SELECTION_CHANCE) {
            candidateIndex = 1 + this.random.nextInt(Math.min(3, candidates.size() - 1));
         } else {
            int var16 = Math.min(4, candidates.size() - 1);
            candidateIndex = var16 + this.random.nextInt(candidates.size() - var16);
         }

         HumanizedRotation.AimPoint selectedPoint = (HumanizedRotation.AimPoint)candidates.get(candidateIndex);
         this.normalizedAimX = selectedPoint.x();
         this.normalizedAimY = selectedPoint.y();
         this.normalizedAimZ = selectedPoint.z();
      }
   }

   private void updateStationaryLock(Vector2f currentRotation, Vector2f centerRotation, boolean targetStationary) {
      float yawDistance = Mth.wrapDegrees(centerRotation.x - currentRotation.x);
      float pitchDistance = centerRotation.y - currentRotation.y;
      if (!this.lockedToStationaryTarget) {
         if (targetStationary && Math.abs(yawDistance) < STATIONARY_LOCK_YAW_ACQUIRE_DISTANCE && Math.abs(pitchDistance) < STATIONARY_LOCK_PITCH_ACQUIRE_DISTANCE) {
            this.lockedToStationaryTarget = true;
            this.lockedYaw = currentRotation.x;
            this.lockedPitch = currentRotation.y;
            this.smoothedYawStep = this.smoothedYawStep * STATIONARY_LOCK_YAW_STEP_SCALE;
            this.smoothedPitchStep = this.smoothedPitchStep * STATIONARY_LOCK_PITCH_STEP_SCALE;
         }
      } else if (targetStationary && !(Math.abs(yawDistance) > STATIONARY_LOCK_YAW_RELEASE_DISTANCE) && !(Math.abs(pitchDistance) > STATIONARY_LOCK_PITCH_RELEASE_DISTANCE)) {
         float lockedYawDistance = Mth.wrapDegrees(centerRotation.x - this.lockedYaw);
         float lockedPitchDistance = centerRotation.y - this.lockedPitch;
         this.lockedYaw = this.lockedYaw + this.removeDeadZone(lockedYawDistance, STATIONARY_LOCK_YAW_DEAD_ZONE, STATIONARY_LOCK_YAW_OFFSET) * STATIONARY_LOCK_YAW_TRACKING_GAIN;
         this.lockedPitch = Mth.clamp(this.lockedPitch + this.removeDeadZone(lockedPitchDistance, STATIONARY_LOCK_PITCH_DEAD_ZONE, STATIONARY_LOCK_PITCH_OFFSET) * STATIONARY_LOCK_PITCH_TRACKING_GAIN, STATIONARY_LOCK_MIN_PITCH, STATIONARY_LOCK_MAX_PITCH);
      } else {
         this.lockedToStationaryTarget = false;
      }
   }

   private float randomSignedUnit() {
      return this.random.nextFloat() * 2.0F - 1.0F;
   }

   private boolean startOvershoot(Vector2f currentRotation, Vector2f targetRotation, boolean freshTarget) {
      float yawDistance = Mth.wrapDegrees(targetRotation.x - currentRotation.x);
      float pitchDistance = targetRotation.y - currentRotation.y;
      float rotationDistance = (float)Math.hypot(yawDistance, pitchDistance);
      if (rotationDistance < MIN_OVERSHOOT_ROTATION_DISTANCE) {
         return false;
      }

      float overshootProbability = rotationDistance > LARGE_OVERSHOOT_ANGLE ? LARGE_ANGLE_OVERSHOOT_CHANCE : (rotationDistance > MEDIUM_OVERSHOOT_ANGLE ? MEDIUM_ANGLE_OVERSHOOT_CHANCE : SMALL_ANGLE_OVERSHOOT_CHANCE);
      if (!freshTarget) {
         overshootProbability *= REPEATED_TARGET_OVERSHOOT_CHANCE_SCALE;
      }

      if (this.random.nextFloat() >= overshootProbability) {
         return false;
      }

      float yawSign = this.signOrRandom(yawDistance);
      float pitchSign = this.signOrRandom(pitchDistance);
      this.overshootYaw = yawSign * Mth.clamp(Math.abs(yawDistance) * this.randomFloat(MIN_YAW_OVERSHOOT_FRACTION, MAX_YAW_OVERSHOOT_FRACTION), MIN_YAW_OVERSHOOT, MAX_YAW_OVERSHOOT);
      this.overshootPitch = pitchSign * Mth.clamp(Math.abs(pitchDistance) * this.randomFloat(MIN_PITCH_OVERSHOOT_FRACTION, MAX_PITCH_OVERSHOOT_FRACTION), MIN_PITCH_OVERSHOOT, MAX_PITCH_OVERSHOOT);
      this.overshootYawDominant = Math.abs(yawDistance) >= Math.abs(pitchDistance);
      this.overshootDirection = this.overshootYawDominant ? yawSign : pitchSign;
      this.overshootElapsedTicks = 0;
      this.overshootDurationTicks = Mth.clamp(4 + (int)Math.ceil(rotationDistance / OVERSHOOT_DURATION_ANGLE_SCALE), 4, 11);
      this.overshootActive = true;
      this.settlingTicks = 0;
      this.pitchDelayTicks = Math.abs(pitchDistance) > 2.0F && this.random.nextFloat() < PITCH_DELAY_CHANCE ? 1 : 0;
      return true;
   }

   private float cubicBezierCoordinate(float progress, float firstControl, float secondControl) {
      float var4 = 1.0F - progress;
      return UNIT_BEZIER_FIRST_BASIS_SCALE * var4 * var4 * progress * firstControl + UNIT_BEZIER_SECOND_BASIS_SCALE * var4 * progress * progress * secondControl + progress * progress * progress;
   }

   private boolean updateOvershoot(Vector2f currentRotation, Vector2f targetRotation) {
      if (!this.overshootActive) {
         return false;
      }

      this.overshootElapsedTicks++;
      float yawDistance = Mth.wrapDegrees(targetRotation.x - currentRotation.x);
      float pitchDistance = targetRotation.y - currentRotation.y;
      float dominantAxisDistance = this.overshootYawDominant ? yawDistance : pitchDistance;
      boolean passedTarget = dominantAxisDistance * this.overshootDirection <= 0.0F;
      if (!passedTarget && this.overshootElapsedTicks < this.overshootDurationTicks) {
         return false;
      }

      this.overshootActive = false;
      this.settlingTicks = 2 + this.random.nextInt(3);
      return true;
   }

   @Override
   public void resetState() {
      this.targetEntityId = RESET_TARGET_ID;
      this.targetTicks = 0;
      this.nextAimPointTick = 0;
      this.lastUpdateNs = 0L;
      this.reactionDelayTicks = 0;
      this.targetJustChanged = false;
      this.useRandomAimPoint = false;
      this.resetOvershoot();
      this.settlingTicks = 0;
      this.lockedToStationaryTarget = false;
      this.resetYawSweep();
      this.jitterRemainingTicks = 0;
      this.nextJitterTick = 0;
      this.jitterYaw = 0.0F;
      this.jitterPitch = 0.0F;
      this.targetJitterYaw = 0.0F;
      this.targetJitterPitch = 0.0F;
      this.pitchDelayTicks = 0;
      this.nextSpeedVariationTick = 0;
      this.speedVariation = 1.0F;
      this.smoothedYawStep = 0.0F;
      this.smoothedPitchStep = 0.0F;
      this.resetMotionSegment();
   }

   private void updateSpeedVariation() {
      if (this.targetTicks >= this.nextSpeedVariationTick) {
         this.speedVariation = this.randomFloat(MIN_SPEED_VARIATION, MAX_SPEED_VARIATION);
         this.nextSpeedVariationTick = this.targetTicks + 4 + this.random.nextInt(5);
      }
   }

   private float updateSegmentProgress(Vector2f currentRotation, float targetYaw, float targetPitch) {
      if (this.motionSegmentActive && !(this.segmentDistance < MIN_SEGMENT_DISTANCE)) {
         float var4 = Mth.wrapDegrees(targetYaw - currentRotation.x);
         float var5 = targetPitch - currentRotation.y;
         float remainingDistance = (float)Math.hypot(var4, var5);
         float distanceProgress = 1.0F - Mth.clamp(remainingDistance / this.segmentDistance, 0.0F, 1.0F);
         float var8 = Mth.wrapDegrees(currentRotation.x - this.segmentStartYaw);
         float var9 = currentRotation.y - this.segmentStartPitch;
         float traveledDistance = (float)Math.hypot(var8, var9);
         float traveledProgress = Mth.clamp(traveledDistance / this.segmentDistance, 0.0F, 1.0F);
         this.segmentProgress = Math.max(this.segmentProgress, Math.max(distanceProgress, traveledProgress));
         if (this.lateDeceleration && !this.lateJitterScheduled && this.segmentProgress >= LATE_JITTER_PROGRESS_THRESHOLD) {
            this.nextJitterTick = this.targetTicks + Math.max(1, this.jitterRemainingTicks + 1);
            this.lateJitterScheduled = true;
         }

         return this.segmentProgress;
      } else {
         return 1.0F;
      }
   }

   private float evaluateEasing(HumanizedRotation.EasingCurve curve, float progress) {
      float var3 = Mth.clamp(progress, 0.0F, 1.0F);

      return switch (curve) {
         case CUBIC_BEZIER -> this.evaluateCubicBezier(0.0F, 0.0F, 1.0F, 1.0F, var3);
         case SINE_IN_OUT -> (float)(-(Math.cos(SINE_EASING_PI * var3) - 1.0) * SINE_EASING_HALF_FACTOR);
         case QUADRATIC_IN_OUT -> var3 < QUADRATIC_EASING_MIDPOINT ? 2.0F * var3 * var3 : 1.0F - (float)Math.pow(QUADRATIC_EASING_MIRROR_SCALE * var3 + 2.0F, QUADRATIC_EASING_POWER) * QUADRATIC_EASING_END_FACTOR;
         case QUARTIC_IN_OUT -> var3 < QUARTIC_EASING_MIDPOINT ? QUARTIC_EASING_START_SCALE * var3 * var3 * var3 * var3 : 1.0F - (float)Math.pow(QUARTIC_EASING_MIRROR_SCALE * var3 + 2.0F, QUARTIC_EASING_POWER) * QUARTIC_EASING_END_FACTOR;
      };
   }

   private boolean isAuraReadyToAttack(Entity target) {
      return false;
   }

   private double randomDouble(double minimum, double maximum) {
      return minimum + this.random.nextDouble() * (maximum - minimum);
   }

   private float removeDeadZone(float distance, float deadZone, float offset) {
      return Math.abs(distance) <= deadZone ? 0.0F : distance - Math.copySign(offset, distance);
   }

   private float limitStepByRemainingAngle(float step, float remainingAngle, float margin) {
      if (Math.abs(step) < STEP_ZERO_EPSILON) {
         return 0.0F;
      } else if (Math.abs(remainingAngle) < REMAINING_ANGLE_EPSILON) {
         return Math.copySign(Math.min(Math.abs(step), margin), step);
      } else {
         return Math.signum(step) != Math.signum(remainingAngle)
            ? Math.copySign(Math.min(Math.abs(step), margin), step)
            : Math.copySign(Math.min(Math.abs(step), Math.abs(remainingAngle) + margin), step);
      }
   }

   private void updateYawSweep(Entity target) {
      if (!this.yawSweepActive) {
         this.yawSweepActive = true;
         this.yawSweepOffset = this.random.nextBoolean() ? INITIAL_SWEEP_NEGATIVE_YAW : INITIAL_SWEEP_POSITIVE_YAW;
         this.yawSweepStart = this.yawSweepOffset;
         this.yawSweepEnd = this.yawSweepOffset == NEGATIVE_SWEEP_ENDPOINT ? POSITIVE_SWEEP_ENDPOINT : ALTERNATE_NEGATIVE_SWEEP_ENDPOINT;
         this.yawSweepTick = 0;
         this.yawSweepDuration = this.randomSweepDuration();
         this.yawSweepCurve = this.randomEasingCurve();
      }

      this.yawSweepTick = Math.min(this.yawSweepTick + 1, this.yawSweepDuration);
      float sweepProgress = Mth.clamp((float)this.yawSweepTick / this.yawSweepDuration, 0.0F, 1.0F);
      float easedProgress = this.evaluateEasing(this.yawSweepCurve, sweepProgress);
      this.yawSweepOffset = Mth.lerp(easedProgress, this.yawSweepStart, this.yawSweepEnd) * Math.clamp(SWEEP_DISTANCE_SCALE / target.distanceTo(MINECRAFT_CLIENT.player), 0.0F, 1.0F);
      if (this.yawSweepTick >= this.yawSweepDuration) {
         this.yawSweepStart = this.yawSweepEnd;
         this.yawSweepEnd = this.yawSweepEnd <= 0.0F ? NEXT_POSITIVE_SWEEP_ENDPOINT : NEXT_NEGATIVE_SWEEP_ENDPOINT;
         this.yawSweepTick = 0;
         this.yawSweepDuration = this.randomSweepDuration();
         this.yawSweepCurve = this.randomEasingCurve();
      }
   }

   private void updateJitter(boolean stationaryLock) {
      if (this.jitterRemainingTicks == 0 && this.targetTicks >= this.nextJitterTick) {
         this.jitterRemainingTicks = 2 + this.random.nextInt(3);
         float yawJitterRange = stationaryLock ? this.randomFloat(MIN_STATIONARY_YAW_JITTER, MAX_STATIONARY_YAW_JITTER) : this.randomFloat(MIN_NORMAL_YAW_JITTER, MAX_NORMAL_YAW_JITTER);
         float pitchJitterRange = stationaryLock ? this.randomFloat(MIN_STATIONARY_PITCH_JITTER, MAX_STATIONARY_PITCH_JITTER) : this.randomFloat(MIN_NORMAL_PITCH_JITTER, MAX_NORMAL_PITCH_JITTER);
         this.targetJitterYaw = this.randomSignedUnit() * yawJitterRange;
         this.targetJitterPitch = this.randomSignedUnit() * pitchJitterRange;
         this.nextJitterTick = this.targetTicks + this.jitterRemainingTicks + 3 + this.random.nextInt(6);
      }

      if (this.jitterRemainingTicks > 0) {
         this.jitterYaw = Mth.lerp(ACTIVE_YAW_JITTER_BLEND, this.jitterYaw, this.targetJitterYaw);
         this.jitterPitch = Mth.lerp(ACTIVE_PITCH_JITTER_BLEND, this.jitterPitch, this.targetJitterPitch);
         this.jitterRemainingTicks--;
         if (this.jitterRemainingTicks == 0) {
            this.targetJitterYaw = 0.0F;
            this.targetJitterPitch = 0.0F;
         }
      } else {
         this.jitterYaw = Mth.lerp(INACTIVE_YAW_JITTER_DECAY, this.jitterYaw, 0.0F);
         this.jitterPitch = Mth.lerp(INACTIVE_PITCH_JITTER_DECAY, this.jitterPitch, 0.0F);
      }
   }

   private void beginTargetTracking(Entity target, Vector2f currentRotation) {
      this.targetEntityId = target.getId();
      this.targetTicks = 0;
      this.reactionDelayTicks = 1 + this.random.nextInt(2);
      this.targetJustChanged = true;
      this.lockedToStationaryTarget = false;
      this.settlingTicks = 0;
      this.resetOvershoot();
      this.jitterRemainingTicks = 0;
      this.nextJitterTick = 2 + this.random.nextInt(4);
      this.jitterYaw = 0.0F;
      this.jitterPitch = 0.0F;
      this.targetJitterYaw = 0.0F;
      this.targetJitterPitch = 0.0F;
      this.smoothedYawStep = 0.0F;
      this.smoothedPitchStep = 0.0F;
      this.nextSpeedVariationTick = 0;
      this.resetMotionSegment();
      this.resetYawSweep();
      this.chooseRandomAimPoint(target, currentRotation);
   }

   private float evaluateCubicBezier(float start, float firstControl, float secondControl, float end, float progress) {
      float var6 = 1.0F - progress;
      return var6 * var6 * var6 * start + BEZIER_FIRST_BASIS_SCALE * var6 * var6 * progress * firstControl + BEZIER_SECOND_BASIS_SCALE * var6 * progress * progress * secondControl + progress * progress * progress * end;
   }

   private Vec3 pointInsideBox(AABB box, double normalizedX, double normalizedY, double normalizedZ) {
      double insetX = Math.min(box.getXsize() * AIM_X_INSET_FRACTION, MAX_AIM_X_INSET);
      double insetY = Math.min(box.getYsize() * AIM_Y_INSET_FRACTION, MAX_AIM_Y_INSET);
      double insetZ = Math.min(box.getZsize() * AIM_Z_INSET_FRACTION, MAX_AIM_Z_INSET);
      return new Vec3(
         Mth.lerp(normalizedX, box.minX + insetX, box.maxX - insetX),
         Mth.lerp(normalizedY, box.minY + insetY, box.maxY - insetY),
         Mth.lerp(normalizedZ, box.minZ + insetZ, box.maxZ - insetZ)
      );
   }

   private HumanizedRotation.EasingCurve randomEasingCurve() {
      HumanizedRotation.EasingCurve[] curves = HumanizedRotation.EasingCurve.values();
      return curves[this.random.nextInt(curves.length)];
   }

   private float solveBezierParameter(float progress) {
      float parameter = progress;

      for (int var3 = 0; var3 < 5; var3++) {
         float error = this.cubicBezierCoordinate(parameter, this.bezierControlX1, this.bezierControlX2) - progress;
         float derivative = this.cubicBezierDerivative(parameter, this.bezierControlX1, this.bezierControlX2);
         if (Math.abs(error) < BEZIER_SOLVER_ERROR_EPSILON || Math.abs(derivative) < BEZIER_SOLVER_DERIVATIVE_EPSILON) {
            break;
         }

         parameter = Mth.clamp(parameter - error / derivative, 0.0F, 1.0F);
      }

      return parameter;
   }

   private float randomFloat(float minimum, float maximum) {
      return minimum + this.random.nextFloat() * (maximum - minimum);
   }

   private int randomSweepDuration() {
      return 5 + this.random.nextInt(6);
   }

   @Override
   public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
      LivingEntity target = null;
      if (currentRotation == null || targetRotation == null) {
         return currentRotation != null ? currentRotation : targetRotation;
      }

      if (target != null && MINECRAFT_CLIENT.player != null) {
         long timestampNs = System.nanoTime();
         boolean historyExpired = this.lastUpdateNs != 0L && timestampNs - this.lastUpdateNs > TARGET_HISTORY_GAP_NS;
         boolean targetChanged = historyExpired || this.targetEntityId != target.getId();
         this.lastUpdateNs = timestampNs;
         if (targetChanged) {
            this.beginTargetTracking(target, currentRotation);
         }

         this.updateYawSweep(target);
         if (this.waitForReactionDelay(target)) {
            return currentRotation;
         }

         if (!targetChanged) {
            this.targetTicks++;
         }

         boolean freshTarget = this.targetJustChanged;
         this.targetJustChanged = false;
         boolean aimPointChanged = false;
         if (this.targetTicks >= this.nextAimPointTick) {
            this.chooseRandomAimPoint(target, currentRotation);
            aimPointChanged = true;
         }

         Vector2f aimPointRotation = this.applyYawSweep(this.selectAimPointRotation(target, targetRotation));
         Vector2f centerRotation = this.applyYawSweep(this.rotationToPoint(target.getBoundingBox().getCenter()));
         boolean targetStationary = target.getDeltaMovement().lengthSqr() < STATIONARY_TARGET_VELOCITY_SQUARED;
         boolean wasStationaryLocked = this.lockedToStationaryTarget;
         this.updateStationaryLock(currentRotation, centerRotation, targetStationary);
         this.updateJitter(this.lockedToStationaryTarget);
         boolean segmentChanged = freshTarget || aimPointChanged || wasStationaryLocked != this.lockedToStationaryTarget;
         if (this.lockedToStationaryTarget) {
            segmentChanged |= this.overshootActive;
            this.resetOvershoot();
         } else if (freshTarget || aimPointChanged) {
            segmentChanged |= this.startOvershoot(currentRotation, aimPointRotation, freshTarget);
         }

         float desiredYaw;
         float desiredPitch;
         if (this.lockedToStationaryTarget) {
            desiredYaw = this.lockedYaw + this.jitterYaw;
            desiredPitch = this.lockedPitch + this.jitterPitch;
         } else {
            segmentChanged |= this.updateOvershoot(currentRotation, aimPointRotation);
            desiredYaw = aimPointRotation.x + (this.overshootActive ? this.overshootYaw : 0.0F) + this.jitterYaw;
            desiredPitch = aimPointRotation.y + (this.overshootActive ? this.overshootPitch : 0.0F) + this.jitterPitch;
         }

         float curveSpeedScale = 1.0F;
         if (this.lockedToStationaryTarget) {
            this.resetMotionSegment();
         } else {
            this.ensureMotionSegment(currentRotation, desiredYaw, desiredPitch, segmentChanged);
            float segmentProgress = this.updateSegmentProgress(currentRotation, desiredYaw, desiredPitch);
            float easedProgress = this.evaluateMotionProgress(segmentProgress);
            float curveBend = (float)Math.sin(CURVE_BEND_PI * easedProgress);
            desiredYaw += this.curveYawBend * curveBend;
            desiredPitch += this.curvePitchBend * curveBend;
            curveSpeedScale = this.getCurveSpeedScale(segmentProgress);
         }

         float yawDistance = Mth.wrapDegrees(desiredYaw - currentRotation.x);
         float pitchDistance = desiredPitch - currentRotation.y;
         float rotationDistance = (float)Math.hypot(yawDistance, pitchDistance);
         this.updateSpeedVariation();
         float var22 = Mth.clamp(rotationDistance / FULL_SPEED_ROTATION_DISTANCE, 0.0F, 1.0F);
         float var23 = this.lockedToStationaryTarget ? STATIONARY_ROTATION_GAIN : Mth.lerp(var22, this.settlingTicks > 0 ? SETTLING_NEAR_ROTATION_GAIN : NEAR_ROTATION_GAIN, FAR_ROTATION_GAIN);
         var23 *= curveSpeedScale;
         float maxYawStep = Mth.lerp(var22, MIN_YAW_STEP_LIMIT, MAX_YAW_STEP_LIMIT) * this.speedVariation * curveSpeedScale;
         float maxPitchStep = Mth.lerp(var22, MIN_PITCH_STEP_LIMIT, MAX_PITCH_STEP_LIMIT) * this.speedVariation * curveSpeedScale;
         if (this.settlingTicks > 0 && !this.overshootActive) {
            maxYawStep = Math.min(maxYawStep, SETTLING_YAW_STEP_LIMIT);
            maxPitchStep = Math.min(maxPitchStep, SETTLING_PITCH_STEP_LIMIT);
         }

         if (this.lockedToStationaryTarget) {
            maxYawStep = Math.min(maxYawStep, STATIONARY_YAW_STEP_LIMIT);
            maxPitchStep = Math.min(maxPitchStep, STATIONARY_PITCH_STEP_LIMIT);
         }

         float desiredYawStep = Mth.clamp(yawDistance * var23, -maxYawStep, maxYawStep);
         float desiredPitchStep = Mth.clamp(pitchDistance * var23, -maxPitchStep, maxPitchStep);
         if (this.pitchDelayTicks > 0 && !this.lockedToStationaryTarget) {
            desiredPitchStep *= DELAYED_PITCH_STEP_SCALE;
            this.pitchDelayTicks--;
         }

         float var28 = this.lockedToStationaryTarget ? STATIONARY_STEP_BLEND : Mth.lerp(var22, NEAR_STEP_BLEND, FAR_STEP_BLEND);
         if (this.settlingTicks > 0 && !this.overshootActive) {
            var28 = Math.max(var28, MIN_SETTLING_STEP_BLEND);
         }

         float yawStep = Mth.lerp(var28, this.smoothedYawStep, desiredYawStep);
         float pitchStep = Mth.lerp(var28, this.smoothedPitchStep, desiredPitchStep);
         float var31 = this.settlingTicks > 0 ? SETTLING_YAW_MARGIN : (this.lockedToStationaryTarget ? STATIONARY_YAW_MARGIN : NORMAL_YAW_MARGIN);
         float var32 = this.settlingTicks > 0 ? SETTLING_PITCH_MARGIN : (this.lockedToStationaryTarget ? STATIONARY_PITCH_MARGIN : NORMAL_PITCH_MARGIN);
         yawStep = this.limitStepByRemainingAngle(yawStep, yawDistance, var31);
         pitchStep = this.limitStepByRemainingAngle(pitchStep, pitchDistance, var32);
         this.smoothedYawStep = yawStep;
         this.smoothedPitchStep = pitchStep;
         if (this.settlingTicks > 0 && !this.overshootActive) {
            this.settlingTicks--;
         }

         return new Vector2f(currentRotation.x + yawStep, Mth.clamp(currentRotation.y + pitchStep, MIN_PITCH, MAX_PITCH));
      } else {
         return currentRotation;
      }
   }

   private float signOrRandom(float value) {
      if (Math.abs(value) > SIGN_RANDOMIZATION_THRESHOLD) {
         return Math.signum(value);
      } else {
         return this.random.nextBoolean() ? 1.0F : RANDOM_NEGATIVE_SIGN;
      }
   }

   private void resetYawSweep() {
      this.yawSweepActive = false;
      this.yawSweepOffset = 0.0F;
      this.yawSweepStart = 0.0F;
      this.yawSweepEnd = 0.0F;
      this.yawSweepTick = 0;
      this.yawSweepDuration = 1;
      this.yawSweepCurve = HumanizedRotation.EasingCurve.CUBIC_BEZIER;
   }

   private void ensureMotionSegment(Vector2f currentRotation, float targetYaw, float targetPitch, boolean forceRestart) {
      float var5 = Mth.wrapDegrees(targetYaw - this.segmentTargetYaw);
      float var6 = targetPitch - this.segmentTargetPitch;
      float targetShiftDistance = (float)Math.hypot(var5, var6);
      float remainingDistance = (float)Math.hypot(Mth.wrapDegrees(targetYaw - currentRotation.x), targetPitch - currentRotation.y);
      boolean completedButMisaligned = this.motionSegmentActive && this.segmentProgress >= SEGMENT_COMPLETION_THRESHOLD && remainingDistance > COMPLETED_SEGMENT_REMAINING_DISTANCE;
      if (forceRestart || !this.motionSegmentActive || targetShiftDistance > SEGMENT_TARGET_SHIFT_THRESHOLD || completedButMisaligned) {
         this.startMotionSegment(currentRotation, targetYaw, targetPitch);
      }
   }

   private void startMotionSegment(Vector2f currentRotation, float targetYaw, float targetPitch) {
      float yawDistance = Mth.wrapDegrees(targetYaw - currentRotation.x);
      float pitchDistance = targetPitch - currentRotation.y;
      float rotationDistance = (float)Math.hypot(yawDistance, pitchDistance);
      if (rotationDistance < MIN_NEW_SEGMENT_DISTANCE) {
         this.resetMotionSegment();
      } else {
         this.motionSegmentActive = true;
         this.segmentStartYaw = currentRotation.x;
         this.segmentStartPitch = currentRotation.y;
         this.segmentTargetYaw = targetYaw;
         this.segmentTargetPitch = targetPitch;
         this.segmentDistance = rotationDistance;
         this.segmentProgress = 0.0F;
         this.motionCurve = this.randomEasingCurve();
         this.randomizeBezierControls();
         float bendMagnitude = Mth.clamp(rotationDistance * this.randomFloat(MIN_CURVE_BEND_FRACTION, MAX_CURVE_BEND_FRACTION), MIN_CURVE_BEND, MAX_CURVE_BEND);
         if (this.random.nextBoolean()) {
            bendMagnitude = -bendMagnitude;
         }

         this.curveYawBend = -pitchDistance / rotationDistance * bendMagnitude;
         this.curvePitchBend = yawDistance / rotationDistance * bendMagnitude * this.randomFloat(MIN_PITCH_BEND_RATIO, MAX_PITCH_BEND_RATIO);
      }
   }

   private Vector2f applyYawSweep(Vector2f rotation) {
      return new Vector2f(rotation.x + this.yawSweepOffset, rotation.y);
   }

   private float cubicBezierDerivative(float progress, float firstControl, float secondControl) {
      float var4 = 1.0F - progress;
      return BEZIER_DERIVATIVE_FIRST_SCALE * var4 * var4 * firstControl + BEZIER_DERIVATIVE_MIDDLE_SCALE * var4 * progress * (secondControl - firstControl) + BEZIER_DERIVATIVE_LAST_SCALE * progress * progress * (1.0F - secondControl);
   }

   private void resetMotionSegment() {
      this.motionSegmentActive = false;
      this.motionCurve = HumanizedRotation.EasingCurve.CUBIC_BEZIER;
      this.segmentStartYaw = 0.0F;
      this.segmentStartPitch = 0.0F;
      this.segmentTargetYaw = 0.0F;
      this.segmentTargetPitch = 0.0F;
      this.segmentDistance = 0.0F;
      this.segmentProgress = 0.0F;
      this.bezierControlX1 = DEFAULT_BEZIER_X1;
      this.bezierControlY1 = DEFAULT_BEZIER_Y1;
      this.bezierControlX2 = DEFAULT_BEZIER_X2;
      this.bezierControlY2 = DEFAULT_BEZIER_Y2;
      this.curveYawBend = 0.0F;
      this.curvePitchBend = 0.0F;
      this.lateDeceleration = false;
      this.lateJitterScheduled = false;
   }

   private float getCurveSpeedScale(float progress) {
      if (!this.motionSegmentActive) {
         return 1.0F;
      }

      float var2 = Mth.clamp(progress, 0.0F, 1.0F);
      float curveDerivative;
      if (this.motionCurve == HumanizedRotation.EasingCurve.CUBIC_BEZIER) {
         float bezierParameter = this.solveBezierParameter(var2);
         float xDerivative = this.cubicBezierDerivative(bezierParameter, this.bezierControlX1, this.bezierControlX2);
         float yDerivative = this.cubicBezierDerivative(bezierParameter, this.bezierControlY1, this.bezierControlY2);
         curveDerivative = yDerivative / Math.max(BEZIER_DERIVATIVE_EPSILON, xDerivative);
      } else {
         curveDerivative = this.evaluateEasingDerivative(this.motionCurve, var2);
      }

      float minimumSpeedScale = this.lateDeceleration && progress > LATE_DECELERATION_PROGRESS_THRESHOLD ? MIN_LATE_CURVE_SPEED_SCALE : MIN_CURVE_SPEED_SCALE;
      return Mth.clamp(curveDerivative, minimumSpeedScale, MAX_CURVE_SPEED_SCALE);
   }

   private void resetOvershoot() {
      this.overshootActive = false;
      this.overshootElapsedTicks = 0;
      this.overshootDurationTicks = 0;
      this.overshootYaw = 0.0F;
      this.overshootPitch = 0.0F;
      this.overshootDirection = 0.0F;
   }

   private float evaluateEasingDerivative(HumanizedRotation.EasingCurve curve, float progress) {
      float var3 = Mth.clamp(progress, 0.0F, 1.0F);

      return switch (curve) {
         case CUBIC_BEZIER -> 1.0F;
         case SINE_IN_OUT -> (float)(SINE_DERIVATIVE_AMPLITUDE * Math.sin(SINE_DERIVATIVE_PI * var3));
         case QUADRATIC_IN_OUT -> var3 < QUADRATIC_DERIVATIVE_MIDPOINT ? QUADRATIC_DERIVATIVE_START_SCALE * var3 : QUADRATIC_DERIVATIVE_END_SCALE * (1.0F - var3);
         case QUARTIC_IN_OUT -> var3 < QUARTIC_DERIVATIVE_MIDPOINT ? QUARTIC_DERIVATIVE_START_SCALE * var3 * var3 * var3 : QUARTIC_DERIVATIVE_END_SCALE * (1.0F - var3) * (1.0F - var3) * (1.0F - var3);
      };
   }

   private boolean waitForReactionDelay(Entity target) {
      if (this.reactionDelayTicks <= 0) {
         return false;
      } else if (this.isAuraReadyToAttack(target)) {
         this.reactionDelayTicks = 0;
         return false;
      } else {
         this.reactionDelayTicks--;
         this.smoothedYawStep = 0.0F;
         this.smoothedPitchStep = 0.0F;
         return true;
      }
   }

   private float evaluateMotionProgress(float progress) {
      if (!this.motionSegmentActive) {
         return progress;
      }

      if (this.motionCurve != HumanizedRotation.EasingCurve.CUBIC_BEZIER) {
         return this.evaluateEasing(this.motionCurve, progress);
      }

      float bezierParameter = this.solveBezierParameter(Mth.clamp(progress, 0.0F, 1.0F));
      return this.cubicBezierCoordinate(bezierParameter, this.bezierControlY1, this.bezierControlY2);
   }

   @Override
   public Vector2f getTargetRotation() {
      LivingEntity target = null;
      if (target != null && MINECRAFT_CLIENT.player != null) {
         AABB targetBox = target.getBoundingBox();
         Vec3 eyePosition = MINECRAFT_CLIENT.player.getEyePosition();
         return this.rotationToPoint(new Vec3(Mth.clamp(eyePosition.x, targetBox.minX, targetBox.maxX), Mth.clamp(eyePosition.y, targetBox.minY, targetBox.maxY), Mth.clamp(eyePosition.z, targetBox.minZ, targetBox.maxZ)));
      } else {
         return super.getTargetRotation();
      }
   }

   private record AimPoint(double x, double y, double z, float rotationDistance) {
   }

   private enum EasingCurve {
      CUBIC_BEZIER,
      SINE_IN_OUT,
      QUADRATIC_IN_OUT,
      QUARTIC_IN_OUT;
   }
}
