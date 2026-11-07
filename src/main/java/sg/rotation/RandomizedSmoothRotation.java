package sg.rotation;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.util.input.MouseSensitivityMath;
import sg.client.RoseClient;
import sg.rotation.RotationStrategy;
import sg.rotation.SpacedRandomFloat;
import sg.modules.impl.init.TickTimer;

public class RandomizedSmoothRotation extends RotationStrategy {
   @SerializedName("В")
   private final SpacedRandomFloat spacedRandomValue;
   private final @com.google.gson.annotations.SerializedName("е")  TickTimer tickTimer;
   private @com.google.gson.annotations.SerializedName("ъ")  Vec3 targetOffset;
   private @com.google.gson.annotations.SerializedName("Хю")  float speedFactor;
   private @com.google.gson.annotations.SerializedName("ХЩ")  float smoothedYawStep;
   private @com.google.gson.annotations.SerializedName("Ху")  float smoothedPitchStep;
   private boolean ВЫ;
   private int ХЯ;
   private float Х2;
   private static final float RANDOM_VALUE_MAX = 15.0F;
   private static final double OFFSET_X_CENTER = 0.5;
   private static final float OFFSET_X_WIDTH_FACTOR = 0.6F;
   private static final double OFFSET_Y_CENTER = 0.3;
   private static final float OFFSET_Y_HEIGHT_FACTOR = 0.5F;
   private static final double OFFSET_Z_CENTER = 0.5;
   private static final float OFFSET_Z_WIDTH_FACTOR = 0.6F;
   private static final double OFFSET_LERP_BASE = 0.12;
   private static final double OFFSET_LERP_RANGE = 0.08;
   private static final float INITIAL_JITTER_BASE = 0.003F;
   private static final float INITIAL_JITTER_RANGE = 0.008F;
   private static final double INITIAL_YAW_JITTER_CENTER = 0.5;
   private static final double INITIAL_PITCH_JITTER_CENTER = 0.5;
   private static final double INITIAL_PITCH_JITTER_FACTOR = 0.6F;
   private static final float BASE_SPEED_MIN = 80.0F;
   private static final float BASE_SPEED_RANGE = 20.0F;
   private static final float YAW_DISTANCE_SPEED_FACTOR = 0.2F;
   private static final double YAW_DISTANCE_NORMALIZER = 20.0;
   private static final float PITCH_BASE_SPEED_FACTOR = 0.6F;
   private static final float PITCH_DISTANCE_SPEED_FACTOR = 0.2F;
   private static final double PITCH_DISTANCE_NORMALIZER = 20.0;
   private static final double EASING_DISTANCE_NORMALIZER = 15.0;
   private static final float EASING_CUBIC_COEFFICIENT = 3.0F;
   private static final float EASING_GAIN = 1.2F;
   private static final float EASING_MAX = 0.98F;
   private static final float SPEED_FACTOR_RETENTION = 0.5F;
   private static final float SPEED_FACTOR_INCREMENT = 0.5F;
   private static final float MIN_SPEED_FACTOR = 0.4F;
   private static final float MAX_SPEED_FACTOR = 0.9F;
   private static final float YAW_NOISE_BASE = 0.985F;
   private static final float YAW_NOISE_RANGE = 0.01F;
   private static final float PITCH_NOISE_BASE = 0.98F;
   private static final float PITCH_NOISE_RANGE = 0.015F;
   private static final float YAW_STEP_BASE_FACTOR = 0.8F;
   private static final float YAW_STEP_SPEED_FACTOR = 0.15F;
   private static final float PITCH_STEP_BASE_FACTOR = 0.75F;
   private static final float PITCH_STEP_SPEED_FACTOR = 0.15F;
   private static final float YAW_SMOOTH_OLD_WEIGHT = 0.3F;
   private static final float YAW_SMOOTH_NEW_WEIGHT = 0.7F;
   private static final float PITCH_SMOOTH_OLD_WEIGHT = 0.3F;
   private static final float PITCH_SMOOTH_NEW_WEIGHT = 0.7F;
   private static final float YAW_OVERSHOOT_FACTOR = 1.02F;
   private static final float PITCH_OVERSHOOT_FACTOR = 1.02F;
   private static final float FINAL_JITTER_BASE = 0.002F;
   private static final float FINAL_JITTER_RANGE = 0.005F;
   private static final double FINAL_YAW_JITTER_CENTER = 0.5;
   private static final double FINAL_PITCH_JITTER_CENTER = 0.5;
   private static final double FINAL_PITCH_JITTER_FACTOR = 0.8F;
   private static final double RARE_JITTER_PROBABILITY = 0.001;
   private static final double RARE_YAW_JITTER_CENTER = 0.5;
   private static final double RARE_YAW_JITTER_RANGE = 0.1;
   private static final double RARE_PITCH_JITTER_CENTER = 0.5;
   private static final double RARE_PITCH_JITTER_RANGE = 0.08;
   private static final float MIN_PITCH = -90.0F;
   private static final float MAX_PITCH = 90.0F;

   @Override
   public Vector2f getTargetRotation() {
      LivingEntity auraTarget = null;
      float offsetX = (float)(Math.random() - OFFSET_X_CENTER) * MINECRAFT_CLIENT.player.getBbWidth() * OFFSET_X_WIDTH_FACTOR;
      float offsetY = (float)(Math.random() - OFFSET_Y_CENTER) * MINECRAFT_CLIENT.player.getBbHeight() * OFFSET_Y_HEIGHT_FACTOR;
      float offsetZ = (float)(Math.random() - OFFSET_Z_CENTER) * MINECRAFT_CLIENT.player.getBbWidth() * OFFSET_Z_WIDTH_FACTOR;
      Vec3 randomOffset = new Vec3(offsetX, offsetY, offsetZ);
      this.targetOffset = this.targetOffset.lerp(randomOffset, OFFSET_LERP_BASE + Math.random() * OFFSET_LERP_RANGE);
      Vec3 eyePosition = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 aimPosition = this.targetPosition.add(this.targetOffset);
      Vec3 targetDirection = aimPosition.subtract(eyePosition).normalize();
      float targetYaw = (float)Math.toDegrees(Math.atan2(-targetDirection.x, targetDirection.z));
      float targetPitch = (float)Math.toDegrees(Math.asin(targetDirection.y));
      return new Vector2f(targetYaw, -targetPitch);
   }

   @Override
   public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
      Minecraft minecraft = Minecraft.getInstance();
      float targetYaw = targetRotation.x;
      float targetPitch = targetRotation.y;
      float initialJitterMagnitude = INITIAL_JITTER_BASE + (float)Math.random() * INITIAL_JITTER_RANGE;
      targetYaw += (float)((Math.random() - INITIAL_YAW_JITTER_CENTER) * initialJitterMagnitude);
      targetPitch += (float)((Math.random() - INITIAL_PITCH_JITTER_CENTER) * initialJitterMagnitude * INITIAL_PITCH_JITTER_FACTOR);
      float currentYaw = currentRotation.x;
      float currentPitch = currentRotation.y;
      double yawDelta = Mth.wrapDegrees(targetYaw - currentYaw);
      double pitchDelta = targetPitch - currentPitch;
      double angularDistance = Math.sqrt(yawDelta * yawDelta + pitchDelta * pitchDelta);
      if (angularDistance == 0.0) {
         return new Vector2f(currentRotation.x, currentRotation.y);
      }

      float baseSpeed = BASE_SPEED_MIN + (float)Math.random() * BASE_SPEED_RANGE;
      float yawSpeedLimit = baseSpeed * (1.0F + YAW_DISTANCE_SPEED_FACTOR * (float)Math.min(angularDistance / YAW_DISTANCE_NORMALIZER, 1.0));
      float pitchSpeedLimit = baseSpeed * PITCH_BASE_SPEED_FACTOR * (1.0F + PITCH_DISTANCE_SPEED_FACTOR * (float)Math.min(angularDistance / PITCH_DISTANCE_NORMALIZER, 1.0));
      double yawShare = Math.abs(yawDelta / angularDistance);
      double pitchShare = Math.abs(pitchDelta / angularDistance);
      double maxYawStep = yawSpeedLimit * yawShare;
      double maxPitchStep = pitchSpeedLimit * pitchShare;
      float yawStep = (float)Math.clamp(yawDelta, -maxYawStep, maxYawStep);
      float pitchStep = (float)Math.clamp(pitchDelta, -maxPitchStep, maxPitchStep);
      float distanceFraction = (float)Math.min(angularDistance / EASING_DISTANCE_NORMALIZER, 1.0);
      float easedFraction = distanceFraction * distanceFraction * (EASING_CUBIC_COEFFICIENT - 2.0F * distanceFraction);
      easedFraction = Math.min(easedFraction * EASING_GAIN, EASING_MAX);
      yawStep *= easedFraction;
      pitchStep *= easedFraction;
      this.speedFactor = this.speedFactor * SPEED_FACTOR_RETENTION + SPEED_FACTOR_INCREMENT;
      this.speedFactor = Math.clamp(this.speedFactor, MIN_SPEED_FACTOR, MAX_SPEED_FACTOR);
      float yawNoiseScale = YAW_NOISE_BASE + (float)Math.random() * YAW_NOISE_RANGE;
      float pitchNoiseScale = PITCH_NOISE_BASE + (float)Math.random() * PITCH_NOISE_RANGE;
      yawStep *= yawNoiseScale * (YAW_STEP_BASE_FACTOR + this.speedFactor * YAW_STEP_SPEED_FACTOR);
      pitchStep *= pitchNoiseScale * (PITCH_STEP_BASE_FACTOR + this.speedFactor * PITCH_STEP_SPEED_FACTOR);
      this.smoothedYawStep = this.smoothedYawStep * YAW_SMOOTH_OLD_WEIGHT + yawStep * YAW_SMOOTH_NEW_WEIGHT;
      this.smoothedPitchStep = this.smoothedPitchStep * PITCH_SMOOTH_OLD_WEIGHT + pitchStep * PITCH_SMOOTH_NEW_WEIGHT;
      float yawOvershootLimit = Math.abs((float)yawDelta) * YAW_OVERSHOOT_FACTOR;
      float pitchOvershootLimit = Math.abs((float)pitchDelta) * PITCH_OVERSHOOT_FACTOR;
      this.smoothedYawStep = Math.clamp(this.smoothedYawStep, -yawOvershootLimit, yawOvershootLimit);
      this.smoothedPitchStep = Math.clamp(this.smoothedPitchStep, -pitchOvershootLimit, pitchOvershootLimit);
      targetYaw = currentYaw + this.smoothedYawStep;
      targetPitch = currentPitch + this.smoothedPitchStep;
      float finalJitterMagnitude = FINAL_JITTER_BASE + (float)Math.random() * FINAL_JITTER_RANGE;
      targetYaw += (float)((Math.random() - FINAL_YAW_JITTER_CENTER) * finalJitterMagnitude);
      targetPitch += (float)((Math.random() - FINAL_PITCH_JITTER_CENTER) * finalJitterMagnitude * FINAL_PITCH_JITTER_FACTOR);
      if (Math.random() < RARE_JITTER_PROBABILITY) {
         targetYaw += (float)((Math.random() - RARE_YAW_JITTER_CENTER) * RARE_YAW_JITTER_RANGE);
         targetPitch += (float)((Math.random() - RARE_PITCH_JITTER_CENTER) * RARE_PITCH_JITTER_RANGE);
      }

      targetPitch = Math.max(MIN_PITCH, Math.min(MAX_PITCH, targetPitch));
      return new Vector2f(MouseSensitivityMath.snapRotationDelta(targetYaw), MouseSensitivityMath.snapRotationDelta(targetPitch));
   }

   public RandomizedSmoothRotation(Vec3 targetPosition) {
      super(targetPosition);
      this.spacedRandomValue = new SpacedRandomFloat(1.0F, RANDOM_VALUE_MAX, 1.0F);
      this.tickTimer = new TickTimer();
      this.targetOffset = Vec3.ZERO;
      this.speedFactor = 0.0F;
      this.smoothedYawStep = 0.0F;
      this.smoothedPitchStep = 0.0F;
      this.ВЫ = false;
      this.ХЯ = 0;
      this.Х2 = 0.0F;
   }

   @Override
   public void refreshRandomState() {
      this.spacedRandomValue.chooseNext();
   }

   @Override
   public boolean usesVanillaCameraRotation() {
      return false;
   }
}
