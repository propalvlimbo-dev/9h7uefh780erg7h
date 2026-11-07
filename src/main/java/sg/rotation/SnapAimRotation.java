package sg.rotation;

import com.google.gson.annotations.SerializedName;
import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.client.RoseClient;
import sg.rotation.RotationStrategy;
import sg.util.input.MouseSensitivityMath;

public final class SnapAimRotation extends RotationStrategy {
   private static final int UNUSED_LEGACY_ENTITY_ID_SENTINEL = Integer.MIN_VALUE;
   private static final int UNUSED_LEGACY_COUNT_THREE = 3;
   private static final float UNUSED_LEGACY_NEGATIVE_YAW_OFFSET = -25.0F;
   private static final float UNUSED_LEGACY_POSITIVE_YAW_OFFSET = 25.0F;
   private @com.google.gson.annotations.SerializedName("Вн")  boolean returnRequested;
   @SerializedName("Х")
   private final Random random = new Random();
   @SerializedName("В")
   private SnapAimRotation.SnapState snapState = SnapAimRotation.SnapState.IDLE;
   private @com.google.gson.annotations.SerializedName("ХИ")  int targetEntityId;
   private @com.google.gson.annotations.SerializedName("Хь")  int snapPhase;
   private @com.google.gson.annotations.SerializedName("еВ")  float yawOffset;
   private @com.google.gson.annotations.SerializedName("еЯ")  float yawOffsetStart;
   private @com.google.gson.annotations.SerializedName("еХ")  float yawOffsetTarget;
   private @com.google.gson.annotations.SerializedName("Хж")  int yawOscillationTicks;
   private @com.google.gson.annotations.SerializedName("Х弟")  int yawOscillationDurationTicks;
   private @com.google.gson.annotations.SerializedName("ВЦ")  boolean yawOscillationInitialized;
   private @com.google.gson.annotations.SerializedName("Вч")  boolean raycastTransitionHandled;
   @SerializedName("В")
   private SnapAimRotation.EasingCurve yawOscillationEasing;
   private @com.google.gson.annotations.SerializedName("ВЭ")  boolean snapMotionInitialized;
   private @com.google.gson.annotations.SerializedName("Хб")  int snapProgressTicks;
   private @com.google.gson.annotations.SerializedName("ХЫ")  int snapDurationTicks;
   @SerializedName("4")
   private Vector2f snapStartRotation;
   private @com.google.gson.annotations.SerializedName("п")  Vector2f smoothedTargetRotation;
   private @com.google.gson.annotations.SerializedName("ее")  float yawOvershoot;
   private @com.google.gson.annotations.SerializedName("еА")  float pitchOvershoot;
   private @com.google.gson.annotations.SerializedName("е4")  float snapPhaseSplit;
   private @com.google.gson.annotations.SerializedName("еп")  float snapYawDriftAmplitude;
   private @com.google.gson.annotations.SerializedName("еД")  float snapPitchDriftAmplitude;
   private @com.google.gson.annotations.SerializedName("еЪ")  float snapYawOscillationAmplitude;
   private @com.google.gson.annotations.SerializedName("ет")  float snapPitchOscillationAmplitude;
   private @com.google.gson.annotations.SerializedName("еа")  float snapOscillationPhase;
   private @com.google.gson.annotations.SerializedName("еЮ")  float snapOscillationFrequency;
   private @com.google.gson.annotations.SerializedName("еЧ")  float pitchEasingOscillationAmplitude;
   private @com.google.gson.annotations.SerializedName("еЛ")  float snapBezierControl1;
   private @com.google.gson.annotations.SerializedName("е诶")  float snapBezierControl2;
   private @com.google.gson.annotations.SerializedName("Я")  SnapAimRotation.EasingCurve snapEasing;
   private @com.google.gson.annotations.SerializedName("В必")  boolean returnMotionInitialized;
   private @com.google.gson.annotations.SerializedName("Хl")  int returnProgressTicks;
   private @com.google.gson.annotations.SerializedName("Х8")  int returnDurationTicks;
   private @com.google.gson.annotations.SerializedName("е1")  float returnBezierControl1;
   private @com.google.gson.annotations.SerializedName("еЬ")  float returnBezierControl2;
   private @com.google.gson.annotations.SerializedName("Д")  Vector2f returnStartRotation;
   @SerializedName("Х")
   private SnapAimRotation.EasingCurve returnEasing;
   private @com.google.gson.annotations.SerializedName("Ъ")  Vector2f lastRotation;
   private @com.google.gson.annotations.SerializedName("т")  Vector2f heldRotation;
   private @com.google.gson.annotations.SerializedName("е衣")  float heldYawOffset;
   private static final int RESET_TARGET_ENTITY_ID = Integer.MIN_VALUE;
   private static final float RESET_SNAP_PHASE_SPLIT = 0.68F;
   private static final float DIRECT_RETURN_PROBABILITY_CUTOFF = 0.38F;
   private static final int CLEAR_TARGET_ENTITY_ID = Integer.MIN_VALUE;
   private static final float MIN_HELD_PITCH = -90.0F;
   private static final float MAX_HELD_PITCH = 90.0F;
   private static final float CAMERA_ALIGNMENT_YAW_TOLERANCE = 1.5F;
   private static final float CAMERA_ALIGNMENT_PITCH_TOLERANCE = 1.5F;
   private static final float MIN_OVERSHOOT_PITCH = -90.0F;
   private static final float MAX_OVERSHOOT_PITCH = 90.0F;
   private static final double PITCH_EASING_SINE_PI = Math.PI;
   private static final double SNAP_ENVELOPE_SINE_PI = Math.PI;
   private static final float MIN_SNAPPED_PITCH = -90.0F;
   private static final float MAX_SNAPPED_PITCH = 90.0F;
   private static final float YAW_OFFSET_EPSILON = 0.001F;
   private static final double MIN_RAYCAST_DISTANCE = 3.0;
   private static final double RAYCAST_DISTANCE_MARGIN = 1.5;
   private static final double RAYCAST_BOX_INFLATION = 0.001;
   private static final float REVERSE_YAW_OVERSHOOT_CHANCE = 0.22F;
   private static final float REVERSE_PITCH_OVERSHOOT_CHANCE = 0.28F;
   private static final float MIN_YAW_OVERSHOOT = 0.35F;
   private static final float MAX_YAW_OVERSHOOT = 5.0F;
   private static final float YAW_OVERSHOOT_DELTA_FACTOR = 0.12F;
   private static final float MIN_PITCH_OVERSHOOT = 0.2F;
   private static final float MAX_PITCH_OVERSHOOT = 3.5F;
   private static final float PITCH_OVERSHOOT_BASE = 0.8F;
   private static final float PITCH_OVERSHOOT_DELTA_FACTOR = 0.15F;
   private static final float MIN_SNAP_PHASE_SPLIT = 0.58F;
   private static final float MAX_SNAP_PHASE_SPLIT = 0.78F;
   private static final float MIN_SNAP_YAW_DRIFT = 0.25F;
   private static final float MAX_SNAP_YAW_DRIFT = 3.2F;
   private static final float SNAP_YAW_DRIFT_BASE = 0.8F;
   private static final float SNAP_YAW_DRIFT_DELTA_FACTOR = 0.08F;
   private static final float NEGATIVE_PITCH_DRIFT_SIGN = -1.0F;
   private static final float MIN_SNAP_PITCH_DRIFT = 0.15F;
   private static final float MAX_SNAP_PITCH_DRIFT = 1.8F;
   private static final float SNAP_PITCH_DRIFT_BASE = 0.5F;
   private static final float SNAP_PITCH_DRIFT_DELTA_FACTOR = 0.08F;
   private static final float MIN_SNAP_YAW_OSCILLATION = 0.08F;
   private static final float MAX_SNAP_YAW_OSCILLATION = 0.42F;
   private static final float MIN_YAW_OSCILLATION_SIGN_SAMPLE = -1.0F;
   private static final float MIN_SNAP_PITCH_OSCILLATION = 0.05F;
   private static final float MAX_SNAP_PITCH_OSCILLATION = 0.28F;
   private static final float MIN_PITCH_OSCILLATION_SIGN_SAMPLE = -1.0F;
   private static final float MAX_OSCILLATION_PHASE = (float) (Math.PI * 2);
   private static final float MIN_OSCILLATION_FREQUENCY = 5.5F;
   private static final float MAX_OSCILLATION_FREQUENCY = 9.5F;
   private static final float MIN_PITCH_EASING_OSCILLATION = -0.055F;
   private static final float MAX_PITCH_EASING_OSCILLATION = 0.095F;
   private static final float MIN_SNAP_BEZIER_CONTROL1 = 0.02F;
   private static final float MAX_SNAP_BEZIER_CONTROL1 = 0.22F;
   private static final float MIN_SNAP_BEZIER_CONTROL2 = 0.72F;
   private static final float MAX_SNAP_BEZIER_CONTROL2 = 0.98F;
   private static final float MIN_TARGET_SMOOTHING_FACTOR = 0.68F;
   private static final float MIN_RETURN_BEZIER_CONTROL1 = 0.02F;
   private static final float MAX_RETURN_BEZIER_CONTROL1 = 0.14F;
   private static final float MIN_RETURN_BEZIER_CONTROL2 = 0.82F;
   private static final float MAX_RETURN_BEZIER_CONTROL2 = 0.97F;
   private static final float INITIAL_NEGATIVE_YAW_OFFSET = -25.0F;
   private static final float INITIAL_POSITIVE_YAW_OFFSET = 25.0F;
   private static final float NEGATIVE_YAW_OFFSET_COMPARISON = -25.0F;
   private static final float INITIAL_OPPOSITE_POSITIVE_OFFSET = 25.0F;
   private static final float INITIAL_OPPOSITE_NEGATIVE_OFFSET = -25.0F;
   private static final float YAW_OFFSET_DISTANCE_SCALE = 1.5F;
   private static final float NEXT_POSITIVE_YAW_OFFSET = 25.0F;
   private static final float NEXT_NEGATIVE_YAW_OFFSET = -25.0F;
   private static final double SINE_EASING_PI = Math.PI;
   private static final double SINE_EASING_SCALE = 0.5;
   private static final float QUADRATIC_EASING_MIDPOINT = 0.5F;
   private static final float QUADRATIC_EASING_SHIFT = -2.0F;
   private static final double QUADRATIC_EASING_POWER = 2.0;
   private static final float QUADRATIC_EASING_SCALE = 0.5F;
   private static final float QUARTIC_EASING_MIDPOINT = 0.5F;
   private static final float QUARTIC_EASING_FIRST_HALF_SCALE = 8.0F;
   private static final float QUARTIC_EASING_SHIFT = -2.0F;
   private static final double QUARTIC_EASING_POWER = 4.0;
   private static final float QUARTIC_EASING_SECOND_HALF_SCALE = 0.5F;
   private static final float BEZIER_FIRST_CONTROL_COEFFICIENT = 3.0F;
   private static final float BEZIER_SECOND_CONTROL_COEFFICIENT = 3.0F;
   private static final float MIN_TARGET_PITCH = -90.0F;
   private static final float MAX_TARGET_PITCH = 90.0F;
   private static final float DIRECTION_SIGN_EPSILON = 0.1F;
   private static final float NEGATIVE_DIRECTION_SIGN = -1.0F;
   private static final float DEFAULT_SNAP_PHASE_SPLIT = 0.68F;
   private static final float YAW_DEGREES_TO_RADIANS = (float) (Math.PI / 180.0);
   private static final float PITCH_DEGREES_TO_RADIANS = (float) (Math.PI / 180.0);

   @Override
   public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
      LivingEntity var4 = null;
      if (currentRotation != null && targetRotation != null && var4 != null && MINECRAFT_CLIENT.player != null) {
         this.updateYawOscillation();

         return switch (this.snapState) {
            case IDLE -> this.followCameraRotation(currentRotation);
            case SNAP -> this.computeSnapRotation(currentRotation, targetRotation, var4);
            case RETURN_CAMERA -> this.computeReturnToCameraRotation(currentRotation);
            case HOLD_LAST -> this.holdLastRotation(currentRotation);
            case HOLD_CAMERA -> this.holdCameraRotation();
         };
      } else {
         return currentRotation != null ? currentRotation : targetRotation;
      }
   }

   private void initializeReturnMotion(Vector2f currentRotation) {
      this.returnMotionInitialized = true;
      this.returnStartRotation = this.copyRotation(currentRotation);
      this.returnProgressTicks = 0;
      this.returnDurationTicks = 6 + this.random.nextInt(5);
      this.returnBezierControl1 = this.randomBetween(MIN_RETURN_BEZIER_CONTROL1, MAX_RETURN_BEZIER_CONTROL1);
      this.returnBezierControl2 = this.randomBetween(MIN_RETURN_BEZIER_CONTROL2, MAX_RETURN_BEZIER_CONTROL2);
      this.returnEasing = this.randomEasingCurve();
   }

   private Vector2f computeSnapRotation(Vector2f currentRotation, Vector2f targetRotation, Entity targetEntity) {
      if (!this.snapMotionInitialized) {
         this.initializeSnapMotion(currentRotation, targetRotation);
      }

      if (this.snapPhase == 0) {
         this.snapProgressTicks = this.snapDurationTicks;
      } else {
         this.snapProgressTicks = Math.min(this.snapProgressTicks + 1, this.snapDurationTicks);
      }

      float var4 = Mth.clamp((float)this.snapProgressTicks / this.snapDurationTicks, 0.0F, 1.0F);
      Vector2f var5 = this.smoothTargetRotation(this.wrapAndClampTargetRotation(this.snapStartRotation, targetRotation), var4);
      this.adjustYawOscillationForHit(var5, targetEntity);
      Vector2f var6 = new Vector2f(var5.x + this.yawOffset, var5.y);
      Vector2f var7 = new Vector2f(var6.x + this.yawOvershoot, Mth.clamp(var6.y + this.pitchOvershoot, MIN_OVERSHOOT_PITCH, MAX_OVERSHOOT_PITCH));
      float var8 = this.cubicBezier(0.0F, this.snapBezierControl1, this.snapBezierControl2, 1.0F, var4);
      float var9 = Mth.clamp(var8 - this.pitchEasingOscillationAmplitude * (float)Math.sin(PITCH_EASING_SINE_PI * var8), 0.0F, 1.0F);
      Vector2f var10 = new Vector2f(this.interpolateSnapPhase(var8, this.snapStartRotation.x, var7.x, var6.x), this.interpolateSnapPhase(var9, this.snapStartRotation.y, var7.y, var6.y));
      float var11 = (float)Math.sin(SNAP_ENVELOPE_SINE_PI * var4);
      float var12 = (float)Math.sin(this.snapOscillationPhase + var4 * this.snapOscillationFrequency);
      var10.add(this.snapYawDriftAmplitude * var11 + this.snapYawOscillationAmplitude * var12 * var11, this.snapPitchDriftAmplitude * var11 + this.snapPitchOscillationAmplitude * var12 * var11);
      var10 = this.quantizeRotation(new Vector2f(var10.x, Mth.clamp(var10.y, MIN_SNAPPED_PITCH, MAX_SNAPPED_PITCH)));
      return this.rememberRotation(var10);
   }

   public boolean shouldContinueReturn() {
      if (!this.returnRequested) {
         return false;
      }

      if (this.snapState == SnapAimRotation.SnapState.HOLD_LAST) {
         this.snapState = SnapAimRotation.SnapState.RETURN_CAMERA;
         this.returnMotionInitialized = false;
      }

      return this.snapState == SnapAimRotation.SnapState.RETURN_CAMERA
         ? true
         : this.snapState == SnapAimRotation.SnapState.HOLD_CAMERA && !this.isCameraAlignedWithLastRotation();
   }

   public void updateTargetPhase(Entity targetEntity, int phase) {
      if (targetEntity == null) {
         this.resetState();
      } else {
         if (this.targetEntityId != targetEntity.getId()) {
            this.resetForTarget(targetEntity);
         }

         if (this.returnRequested) {
            if (phase < 0 || phase > 3) {
               return;
            }

            if (this.snapState == SnapAimRotation.SnapState.RETURN_CAMERA) {
               return;
            }

            this.returnRequested = false;
         }

         this.snapPhase = phase;
         if (this.snapState == SnapAimRotation.SnapState.SNAP) {
            if (phase < 0) {
               this.resetSnapToIdle();
            }
         } else {
            if (phase >= 0 && phase <= 3) {
               this.snapState = SnapAimRotation.SnapState.SNAP;
               this.snapMotionInitialized = false;
               this.snapProgressTicks = 0;
               this.snapDurationTicks = Math.max(2, phase + 1);
            }
         }
      }
   }

   private void resetSnapMotion() {
      this.snapMotionInitialized = false;
      this.snapProgressTicks = 0;
      this.snapDurationTicks = 1;
      this.snapStartRotation = null;
      this.smoothedTargetRotation = null;
      this.yawOvershoot = 0.0F;
      this.pitchOvershoot = 0.0F;
      this.snapPhaseSplit = DEFAULT_SNAP_PHASE_SPLIT;
      this.snapYawDriftAmplitude = 0.0F;
      this.snapPitchDriftAmplitude = 0.0F;
      this.snapYawOscillationAmplitude = 0.0F;
      this.snapPitchOscillationAmplitude = 0.0F;
      this.snapOscillationPhase = 0.0F;
      this.snapOscillationFrequency = 0.0F;
      this.pitchEasingOscillationAmplitude = 0.0F;
      this.snapBezierControl1 = 0.0F;
      this.snapBezierControl2 = 1.0F;
      this.snapEasing = SnapAimRotation.EasingCurve.CUBIC_BEZIER;
   }

   private boolean isCameraAlignedWithLastRotation() {
      if (this.lastRotation == null) {
         return true;
      }

      Vector2f var1 = this.getCameraRotationCopy();
      return Math.abs(Mth.wrapDegrees(this.lastRotation.x - var1.x)) <= CAMERA_ALIGNMENT_YAW_TOLERANCE && Math.abs(this.lastRotation.y - var1.y) <= CAMERA_ALIGNMENT_PITCH_TOLERANCE;
   }

   private Vector2f followCameraRotation(Vector2f currentRotation) {
      Vector2f var2 = this.getCameraRotationCopy();
      float var3 = this.unwrapTowardYaw(currentRotation.x, var2.x);
      Vector2f var4 = this.quantizeRotation(new Vector2f(var3 + this.yawOffset, Mth.clamp(var2.y, MIN_HELD_PITCH, MAX_HELD_PITCH)));
      return this.rememberRotation(var4);
   }

   private float chooseDirectionSign(float delta) {
      if (Math.abs(delta) > DIRECTION_SIGN_EPSILON) {
         return Math.signum(delta);
      } else {
         return this.random.nextBoolean() ? 1.0F : NEGATIVE_DIRECTION_SIGN;
      }
   }

   private float unwrapTowardYaw(float currentYaw, float targetYaw) {
      return currentYaw + Mth.wrapDegrees(targetYaw - currentYaw);
   }

   private float cubicBezier(float start, float control1, float control2, float end, float progress) {
      float var6 = 1.0F - progress;
      return var6 * var6 * var6 * start + BEZIER_FIRST_CONTROL_COEFFICIENT * var6 * var6 * progress * control1 + BEZIER_SECOND_CONTROL_COEFFICIENT * var6 * progress * progress * control2 + progress * progress * progress * end;
   }

   private void updateYawOscillation() {
      if (!this.yawOscillationInitialized) {
         this.yawOscillationInitialized = true;
         this.yawOffset = this.random.nextBoolean() ? INITIAL_NEGATIVE_YAW_OFFSET : INITIAL_POSITIVE_YAW_OFFSET;
         this.yawOffsetStart = this.yawOffset;
         this.yawOffsetTarget = this.yawOffset == NEGATIVE_YAW_OFFSET_COMPARISON ? INITIAL_OPPOSITE_POSITIVE_OFFSET : INITIAL_OPPOSITE_NEGATIVE_OFFSET;
         this.yawOscillationTicks = 0;
         this.yawOscillationDurationTicks = this.randomYawOscillationDuration();
         this.yawOscillationEasing = this.randomEasingCurve();
      }

      LivingEntity var1 = null;
      this.yawOscillationTicks = Math.min(this.yawOscillationTicks + 1, this.yawOscillationDurationTicks);
      float var2 = Mth.clamp((float)this.yawOscillationTicks / this.yawOscillationDurationTicks, 0.0F, 1.0F);
      float var3 = this.evaluateEasing(this.yawOscillationEasing, var2);
      this.yawOffset = var1 == null ? 0.0F
         : Mth.lerp(var3, this.yawOffsetStart, this.yawOffsetTarget)
            * Math.clamp(YAW_OFFSET_DISTANCE_SCALE / var1.distanceTo(MINECRAFT_CLIENT.player), 0.0F, 1.0F);
      if (this.yawOscillationTicks >= this.yawOscillationDurationTicks) {
         this.yawOffsetStart = this.yawOffsetTarget;
         this.yawOffsetTarget = this.yawOffsetTarget <= 0.0F ? NEXT_POSITIVE_YAW_OFFSET : NEXT_NEGATIVE_YAW_OFFSET;
         this.yawOscillationTicks = 0;
         this.yawOscillationDurationTicks = this.randomYawOscillationDuration();
         this.yawOscillationEasing = this.randomEasingCurve();
      }
   }

   private void adjustYawOscillationForHit(Vector2f targetRotation, Entity targetEntity) {
      Vector2f var3 = new Vector2f(targetRotation.x + this.yawOffset, targetRotation.y);
      boolean var4 = this.rayIntersectsEntity(targetRotation, targetEntity);
      boolean var5 = this.rayIntersectsEntity(var3, targetEntity);
      if (var5) {
         this.raycastTransitionHandled = false;
      } else if (var4 && !this.raycastTransitionHandled) {
         this.raycastTransitionHandled = true;
         if (!(Math.abs(this.yawOffset) < YAW_OFFSET_EPSILON)) {
            this.yawOffsetStart = this.yawOffset;
            this.yawOffsetTarget = -this.yawOffset;
            this.yawOscillationTicks = 0;
            this.yawOscillationDurationTicks = Math.max(2, Math.max(1, this.snapPhase) * 2);
            this.yawOscillationEasing = this.randomEasingCurve();
         }
      }
   }

   private Vec3 directionFromRotation(Vector2f rotation) {
      float var2 = rotation.x * YAW_DEGREES_TO_RADIANS;
      float var3 = rotation.y * PITCH_DEGREES_TO_RADIANS;
      float var4 = Mth.cos(var3);
      return new Vec3(-Mth.sin(var2) * var4, -Mth.sin(var3), Mth.cos(var2) * var4);
   }

   private int randomYawOscillationDuration() {
      return 5 + this.random.nextInt(6);
   }

   private void initializeSnapMotion(Vector2f currentRotation, Vector2f targetRotation) {
      this.snapMotionInitialized = true;
      this.snapEasing = this.randomEasingCurve();
      this.snapStartRotation = this.copyRotation(currentRotation);
      this.smoothedTargetRotation = this.wrapAndClampTargetRotation(this.snapStartRotation, targetRotation);
      float var3 = this.smoothedTargetRotation.x - this.snapStartRotation.x;
      float var4 = this.smoothedTargetRotation.y - this.snapStartRotation.y;
      float var5 = this.chooseDirectionSign(var3);
      float var6 = this.chooseDirectionSign(var4);
      float var7 = this.random.nextFloat() < REVERSE_YAW_OVERSHOOT_CHANCE ? -var5 : var5;
      float var8 = this.random.nextFloat() < REVERSE_PITCH_OVERSHOOT_CHANCE ? -var6 : var6;
      this.yawOvershoot = var7 * this.randomBetween(MIN_YAW_OVERSHOOT, Math.min(MAX_YAW_OVERSHOOT, 1.0F + Math.abs(var3) * YAW_OVERSHOOT_DELTA_FACTOR));
      this.pitchOvershoot = var8 * this.randomBetween(MIN_PITCH_OVERSHOOT, Math.min(MAX_PITCH_OVERSHOOT, PITCH_OVERSHOOT_BASE + Math.abs(var4) * PITCH_OVERSHOOT_DELTA_FACTOR));
      this.snapPhaseSplit = this.randomBetween(MIN_SNAP_PHASE_SPLIT, MAX_SNAP_PHASE_SPLIT);
      this.snapYawDriftAmplitude = -var5 * this.randomBetween(MIN_SNAP_YAW_DRIFT, Math.min(MAX_SNAP_YAW_DRIFT, SNAP_YAW_DRIFT_BASE + Math.abs(var3) * SNAP_YAW_DRIFT_DELTA_FACTOR));
      this.snapPitchDriftAmplitude = (this.random.nextBoolean() ? 1.0F : NEGATIVE_PITCH_DRIFT_SIGN) * this.randomBetween(MIN_SNAP_PITCH_DRIFT, Math.min(MAX_SNAP_PITCH_DRIFT, SNAP_PITCH_DRIFT_BASE + Math.abs(var4) * SNAP_PITCH_DRIFT_DELTA_FACTOR));
      this.snapYawOscillationAmplitude = this.randomBetween(MIN_SNAP_YAW_OSCILLATION, MAX_SNAP_YAW_OSCILLATION) * this.chooseDirectionSign(this.randomBetween(MIN_YAW_OSCILLATION_SIGN_SAMPLE, 1.0F));
      this.snapPitchOscillationAmplitude = this.randomBetween(MIN_SNAP_PITCH_OSCILLATION, MAX_SNAP_PITCH_OSCILLATION) * this.chooseDirectionSign(this.randomBetween(MIN_PITCH_OSCILLATION_SIGN_SAMPLE, 1.0F));
      this.snapOscillationPhase = this.randomBetween(0.0F, MAX_OSCILLATION_PHASE);
      this.snapOscillationFrequency = this.randomBetween(MIN_OSCILLATION_FREQUENCY, MAX_OSCILLATION_FREQUENCY);
      this.pitchEasingOscillationAmplitude = this.randomBetween(MIN_PITCH_EASING_OSCILLATION, MAX_PITCH_EASING_OSCILLATION);
      this.snapBezierControl1 = this.randomBetween(MIN_SNAP_BEZIER_CONTROL1, MAX_SNAP_BEZIER_CONTROL1);
      this.snapBezierControl2 = this.randomBetween(MIN_SNAP_BEZIER_CONTROL2, MAX_SNAP_BEZIER_CONTROL2);
      this.heldRotation = null;
   }

   private Vector2f wrapAndClampTargetRotation(Vector2f currentRotation, Vector2f targetRotation) {
      return new Vector2f(this.unwrapTowardYaw(currentRotation.x, targetRotation.x), Mth.clamp(targetRotation.y, MIN_TARGET_PITCH, MAX_TARGET_PITCH));
   }

   private void resetSnapToIdle() {
      this.snapState = SnapAimRotation.SnapState.IDLE;
      this.resetSnapMotion();
   }

   private Vector2f getCameraRotationCopy() {
      return new Vector2f(RoseClient.getInstance().getControllers().getRotations().getCameraRotation());
   }

   private boolean rayIntersectsEntity(Vector2f rotation, Entity entity) {
      Vec3 var3 = MINECRAFT_CLIENT.player.getEyePosition();
      double var4 = Math.max(MIN_RAYCAST_DISTANCE, var3.distanceTo(entity.getBoundingBox().getCenter()) + RAYCAST_DISTANCE_MARGIN);
      Vec3 var6 = var3.add(this.directionFromRotation(rotation).scale(var4));
      return entity.getBoundingBox().inflate(RAYCAST_BOX_INFLATION).clip(var3, var6).isPresent();
   }

   public void requestReturnToCamera() {
      Vector2f var1 = this.lastRotation != null ? this.lastRotation : this.smoothedTargetRotation;
      this.heldRotation = this.copyRotation(var1);
      this.heldYawOffset = this.yawOffset;
      this.snapMotionInitialized = false;
      this.snapPhase = -1;
      this.returnRequested = true;
      if (this.random.nextFloat(0.0F, 1.0F) > DIRECT_RETURN_PROBABILITY_CUTOFF) {
         this.snapState = SnapAimRotation.SnapState.RETURN_CAMERA;
         this.returnMotionInitialized = false;
      } else {
         this.snapState = SnapAimRotation.SnapState.HOLD_LAST;
      }
   }

   private Vector2f computeReturnToCameraRotation(Vector2f currentRotation) {
      if (!this.returnMotionInitialized) {
         this.initializeReturnMotion(currentRotation);
      }

      Vector2f var2 = this.getCameraRotationCopy();
      float var3 = this.unwrapTowardYaw(this.returnStartRotation.x, var2.x + this.yawOffset);
      this.returnProgressTicks = Math.min(this.returnProgressTicks + 1, this.returnDurationTicks);
      float var4 = Mth.clamp((float)this.returnProgressTicks / this.returnDurationTicks, 0.0F, 1.0F);
      float var5 = this.returnEasing == SnapAimRotation.EasingCurve.CUBIC_BEZIER
         ? this.cubicBezier(0.0F, this.returnBezierControl1, this.returnBezierControl2, 1.0F, var4)
         : this.evaluateEasing(this.returnEasing, var4);
      Vector2f var6 = this.quantizeRotation(new Vector2f(Mth.lerp(var5, this.returnStartRotation.x, var3), Mth.lerp(var5, this.returnStartRotation.y, var2.y)));
      this.rememberRotation(var6);
      if (this.returnProgressTicks >= this.returnDurationTicks) {
         this.snapState = SnapAimRotation.SnapState.HOLD_CAMERA;
         this.returnMotionInitialized = false;
      }

      return var6;
   }

   private float interpolateSnapPhase(float progress, float start, float middle, float end) {
      if (progress < this.snapPhaseSplit) {
         float var6 = Mth.clamp(progress / this.snapPhaseSplit, 0.0F, 1.0F);
         return Mth.lerp(this.evaluateEasing(this.snapEasing, var6), start, middle);
      } else {
         float var5 = Mth.clamp((progress - this.snapPhaseSplit) / (1.0F - this.snapPhaseSplit), 0.0F, 1.0F);
         return Mth.lerp(this.evaluateEasing(this.snapEasing, var5), middle, end);
      }
   }

   public SnapAimRotation(Vec3 targetPosition) {
      super(targetPosition);
      this.targetEntityId = RESET_TARGET_ENTITY_ID;
      this.snapPhase = -1;
      this.yawOscillationEasing = SnapAimRotation.EasingCurve.CUBIC_BEZIER;
      this.snapDurationTicks = 1;
      this.snapPhaseSplit = RESET_SNAP_PHASE_SPLIT;
      this.snapEasing = SnapAimRotation.EasingCurve.CUBIC_BEZIER;
      this.returnDurationTicks = 1;
      this.returnEasing = SnapAimRotation.EasingCurve.CUBIC_BEZIER;
   }

   private Vector2f rememberRotation(Vector2f rotation) {
      this.lastRotation = this.copyRotation(rotation);
      return rotation;
   }

   private void resetReturnMotion() {
      this.returnMotionInitialized = false;
      this.returnProgressTicks = 0;
      this.returnDurationTicks = 1;
      this.returnBezierControl1 = 0.0F;
      this.returnBezierControl2 = 1.0F;
      this.returnStartRotation = null;
      this.returnEasing = SnapAimRotation.EasingCurve.CUBIC_BEZIER;
   }

   @Override
   public void resetState() {
      this.snapState = SnapAimRotation.SnapState.IDLE;
      this.targetEntityId = CLEAR_TARGET_ENTITY_ID;
      this.snapPhase = -1;
      this.lastRotation = null;
      this.heldRotation = null;
      this.heldYawOffset = 0.0F;
      this.returnRequested = false;
      this.resetSnapMotion();
      this.resetReturnMotion();
      this.resetYawOscillation();
   }

   private SnapAimRotation.EasingCurve randomEasingCurve() {
      SnapAimRotation.EasingCurve[] var1 = SnapAimRotation.EasingCurve.values();
      return var1[this.random.nextInt(var1.length)];
   }

   public void clearHoldState() {
      this.lastRotation = null;
      this.heldRotation = null;
      this.heldYawOffset = this.yawOffset;
      this.snapMotionInitialized = false;
      this.returnMotionInitialized = false;
   }

   private float evaluateEasing(SnapAimRotation.EasingCurve curve, float progress) {
      float var3 = Mth.clamp(progress, 0.0F, 1.0F);

      return switch (curve) {
         case CUBIC_BEZIER -> this.cubicBezier(0.0F, 0.0F, 1.0F, 1.0F, var3);
         case SINE_IN_OUT -> (float)(-(Math.cos(SINE_EASING_PI * var3) - 1.0) * SINE_EASING_SCALE);
         case QUADRATIC_IN_OUT -> var3 < QUADRATIC_EASING_MIDPOINT ? 2.0F * var3 * var3 : 1.0F - (float)Math.pow(QUADRATIC_EASING_SHIFT * var3 + 2.0F, QUADRATIC_EASING_POWER) * QUADRATIC_EASING_SCALE;
         case QUARTIC_IN_OUT -> var3 < QUARTIC_EASING_MIDPOINT ? QUARTIC_EASING_FIRST_HALF_SCALE * var3 * var3 * var3 * var3 : 1.0F - (float)Math.pow(QUARTIC_EASING_SHIFT * var3 + 2.0F, QUARTIC_EASING_POWER) * QUARTIC_EASING_SECOND_HALF_SCALE;
      };
   }

   private void resetYawOscillation() {
      this.yawOscillationInitialized = false;
      this.raycastTransitionHandled = false;
      this.yawOffset = 0.0F;
      this.yawOffsetStart = 0.0F;
      this.yawOffsetTarget = 0.0F;
      this.yawOscillationTicks = 0;
      this.yawOscillationDurationTicks = 1;
      this.yawOscillationEasing = SnapAimRotation.EasingCurve.CUBIC_BEZIER;
   }

   private Vector2f quantizeRotation(Vector2f rotation) {
      return new Vector2f(MouseSensitivityMath.quantizeRotationDelta(rotation.x), MouseSensitivityMath.quantizeRotationDelta(rotation.y));
   }

   private Vector2f holdCameraRotation() {
      Vector2f var1 = this.getCameraRotationCopy();
      Vector2f var2 = this.quantizeRotation(new Vector2f(var1.x + this.yawOffset, var1.y));
      return this.rememberRotation(var2);
   }

   private Vector2f copyRotation(Vector2f rotation) {
      return rotation == null ? null : new Vector2f(rotation.x, rotation.y);
   }

   private void resetForTarget(Entity targetEntity) {
      this.targetEntityId = targetEntity.getId();
      this.snapState = SnapAimRotation.SnapState.IDLE;
      this.snapPhase = -1;
      this.lastRotation = null;
      this.heldRotation = null;
      this.heldYawOffset = 0.0F;
      this.returnRequested = false;
      this.resetSnapMotion();
      this.resetReturnMotion();
      this.resetYawOscillation();
   }

   private Vector2f holdLastRotation(Vector2f currentRotation) {
      if (this.heldRotation == null) {
         this.heldRotation = this.copyRotation(this.lastRotation != null ? this.lastRotation : currentRotation);
         this.heldYawOffset = this.yawOffset;
      }

      Vector2f var2 = this.quantizeRotation(new Vector2f(this.heldRotation.x + (this.yawOffset - this.heldYawOffset), this.heldRotation.y));
      return this.rememberRotation(var2);
   }

   private Vector2f smoothTargetRotation(Vector2f targetRotation, float progress) {
      if (this.smoothedTargetRotation != null && !(progress >= 1.0F)) {
         float var3 = Mth.lerp(this.evaluateEasing(this.snapEasing, progress), MIN_TARGET_SMOOTHING_FACTOR, 1.0F);
         this.smoothedTargetRotation = new Vector2f(Mth.lerp(var3, this.smoothedTargetRotation.x, targetRotation.x), Mth.lerp(var3, this.smoothedTargetRotation.y, targetRotation.y));
         return this.smoothedTargetRotation;
      } else {
         this.smoothedTargetRotation = targetRotation;
         return targetRotation;
      }
   }

   private float randomBetween(float min, float max) {
      return min + this.random.nextFloat() * (max - min);
   }

   private enum SnapState {
      IDLE,
      SNAP,
      RETURN_CAMERA,
      HOLD_LAST,
      HOLD_CAMERA;
   }

   private enum EasingCurve {
      CUBIC_BEZIER,
      SINE_IN_OUT,
      QUADRATIC_IN_OUT,
      QUARTIC_IN_OUT;
   }
}
