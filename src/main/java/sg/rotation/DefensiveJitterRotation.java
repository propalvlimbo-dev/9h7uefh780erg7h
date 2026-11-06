package sg.rotation;

import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Vector2f;
import sg.util.input.MouseSensitivityMath;
import sg.client.RoseClient;
import sg.modules.impl.combat.Aura;
import sg.rotation.RotationStrategy;

public class DefensiveJitterRotation extends RotationStrategy {
   private final @com.google.gson.annotations.SerializedName("е")  Random random = new Random();
   private @com.google.gson.annotations.SerializedName("ВЮ")  long nextPitchSwitchTime;
   private @com.google.gson.annotations.SerializedName("Ви")  boolean negativePitchPhase = true;
   private static final String DEFENSIVE_OPTION = "Дефенсив";
   private static final String JITTER_TARGET_OPTION = "Джиттер";
   private static final String JITTER_STEP_OPTION = "Джиттер";
   private static final float MIN_INPUT_PITCH = -90.0F;
   private static final float MAX_INPUT_PITCH = 90.0F;
   private static final float TRACKING_WAVE_TIME_DIVISOR = 2.1F;
   private static final double TRACKING_WAVE_NORMALIZER = 2.0;
   private static final double MIN_EYE_HEIGHT_FACTOR = 0.1;
   private static final float MAX_EYE_HEIGHT_FACTOR = 3.0F;
   private static final double TRACKING_Y_JITTER_CENTER = 0.5;
   private static final double TRACKING_Y_JITTER_SCALE = 0.05;
   private static final double OCCLUDED_OFFSET_MAX = 1.5;
   private static final double OCCLUDED_OFFSET_MIN = 0.5;
   private static final double OCCLUDED_DISTANCE_DIVISOR = 6.0;
   private static final float TRACKING_YAW_OFFSET = 90.0F;
   private static final float MIN_TRACKING_PITCH = -90.0F;
   private static final float MAX_TRACKING_PITCH = 90.0F;
   private static final double DEFENSIVE_YAW_OFFSET = 90.0;
   private static final double JITTER_CORNER_YAW_OFFSET = 90.0;
   private static final double JITTER_TARGET_YAW_OFFSET = 90.0;
   private static final float OCCLUDED_TARGET_PITCH = 89.0F;
   private static final float MAX_JITTER_YAW_STEP = 180.0F;
   private static final float VISIBLE_PITCH_STEP_OFFSET = 18.0F;
   private static final float VISIBLE_PITCH_STEP_BASE = 15.0F;
   private static final float MIN_VISIBLE_PITCH = -89.0F;
   private static final float MAX_VISIBLE_PITCH = 89.0F;
   private static final float MIN_HIDDEN_PITCH = -89.0F;
   private static final float MAX_HIDDEN_PITCH = 89.0F;
   private static final long INITIAL_PITCH_SWITCH_MILLIS = 80L;
   private static final long PITCH_SWITCH_INTERVAL_MILLIS = 80L;
   private static final float NEGATIVE_PHASE_PITCH = -90.0F;
   private static final float POSITIVE_PHASE_PITCH = 85.0F;
   private static final double TOP_SIGHT_SAMPLE_HEIGHT_FACTOR = 0.9;
   private static final double MIDDLE_SIGHT_SAMPLE_HEIGHT_FACTOR = 0.5;
   private static final double LOWER_SIGHT_SAMPLE_HEIGHT_FACTOR = 0.1;
   private static final double BOUND_CORNER_YAW_OFFSET = 90.0;
   private static final float MIN_ROTATION_STEP = 0.001F;
   private static final float FALLBACK_ROTATION_STEP = 0.001F;
   private static final float MIN_QUANTIZED_PITCH = -90.0F;
   private static final float MAX_QUANTIZED_PITCH = 90.0F;

   private boolean hasLineOfSightToTarget(LivingEntity target) {
      if (target != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         Vec3 eyePosition = MINECRAFT_CLIENT.player.getEyePosition(MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
         double targetX = target.getX();
         double targetY = target.getY();
         double targetZ = target.getZ();
         Vec3[] samplePoints = new Vec3[]{
            new Vec3(targetX, targetY + target.getBbHeight() * TOP_SIGHT_SAMPLE_HEIGHT_FACTOR, targetZ),
            new Vec3(targetX, targetY + target.getBbHeight() * MIDDLE_SIGHT_SAMPLE_HEIGHT_FACTOR, targetZ),
            new Vec3(targetX, targetY + target.getBbHeight() * LOWER_SIGHT_SAMPLE_HEIGHT_FACTOR, targetZ)
         };

         for (Vec3 samplePoint : samplePoints) {
            ClipContext clipContext = new ClipContext(eyePosition, samplePoint, Block.COLLIDER, Fluid.NONE, MINECRAFT_CLIENT.player);
            if (MINECRAFT_CLIENT.level.clip(clipContext).getType() == Type.MISS) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private Aura getAuraModuleOrNull() {
      return RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null
         ? RoseClient.getInstance().getServices().getModules().getAura()
         : null;
   }

   @Override
   public Vector2f getTargetRotation() {
      Aura aura = this.getAuraModuleOrNull();
      LivingEntity target = aura != null ? aura.Ж_95bedd6e() : null;
      if (aura != null && target != null && MINECRAFT_CLIENT.player != null) {
         if (aura.Г_f1816549().isSelected(DEFENSIVE_OPTION)) {
            return this.calculateDefensiveTargetRotation(target);
         } else {
            return aura.Г_f1816549().isSelected(JITTER_TARGET_OPTION) ? this.calculateJitterTargetRotation(target) : this.calculateTrackingRotation(aura, target);
         }
      } else {
         return super.getTargetRotation();
      }
   }

   private Vector2f calculateTrackingRotation(Aura aura, LivingEntity target) {
      Vec3 eyePosition = MINECRAFT_CLIENT.player.getEyePosition();
      float predictionTicks = aura.Ц_989fed9a().getValue();
      double predictedX = target.getX() + (target.getX() - target.xo) * predictionTicks;
      double predictedZ = target.getZ() + (target.getZ() - target.zo) * predictionTicks;
      float oscillationDivisor = TRACKING_WAVE_TIME_DIVISOR;
      double verticalWave = (Math.sin((double)System.currentTimeMillis() / oscillationDivisor) + 1.0) / TRACKING_WAVE_NORMALIZER;
      double minimumEyeOffset = target.getEyeHeight(target.getPose()) * MIN_EYE_HEIGHT_FACTOR;
      double maximumEyeOffset = target.getEyeHeight(target.getPose()) * MAX_EYE_HEIGHT_FACTOR;
      double targetY = target.getY() + minimumEyeOffset + (maximumEyeOffset - minimumEyeOffset) * verticalWave;
      targetY += (Math.random() - TRACKING_Y_JITTER_CENTER) * TRACKING_Y_JITTER_SCALE;
      if (!this.hasLineOfSightToTarget(target)) {
         double distance = eyePosition.distanceTo(target.position());
         double occludedOffset = Math.min(OCCLUDED_OFFSET_MAX, Math.max(OCCLUDED_OFFSET_MIN, distance / OCCLUDED_DISTANCE_DIVISOR));
         targetY = target.getY() + target.getBbHeight() + occludedOffset;
      }

      double deltaX = predictedX - eyePosition.x;
      double deltaY = targetY - eyePosition.y;
      double deltaZ = predictedZ - eyePosition.z;
      double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
      float yaw = MINECRAFT_CLIENT.player.getYRot() + Mth.wrapDegrees((float)Math.toDegrees(Math.atan2(deltaZ, deltaX)) - TRACKING_YAW_OFFSET - MINECRAFT_CLIENT.player.getYRot());
      float pitch = Mth.clamp((float)(-Math.toDegrees(Math.atan2(deltaY, horizontalDistance))), MIN_TRACKING_PITCH, MAX_TRACKING_PITCH);
      float[] clampedAngles = this.clampToTargetBounds(yaw, pitch, target);
      return new Vector2f(clampedAngles[0], clampedAngles[1]);
   }

   @Override
   public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
      Aura aura = this.getAuraModuleOrNull();
      if (aura != null && aura.Г_f1816549().isSelected(JITTER_STEP_OPTION)) {
         LivingEntity target = aura.Ж_95bedd6e();
         return this.calculateJitterStep(currentRotation, targetRotation, target != null && this.hasLineOfSightToTarget(target));
      } else {
         return this.quantizeRotation(currentRotation, this.unwrapYaw(currentRotation.x, targetRotation.x), Mth.clamp(targetRotation.y, MIN_INPUT_PITCH, MAX_INPUT_PITCH));
      }
   }

   private float nextDefensivePitch() {
      long now = System.currentTimeMillis();
      if (this.nextPitchSwitchTime == 0L) {
         this.negativePitchPhase = true;
         this.nextPitchSwitchTime = now + INITIAL_PITCH_SWITCH_MILLIS;
      } else if (now >= this.nextPitchSwitchTime) {
         this.negativePitchPhase = !this.negativePitchPhase;
         this.nextPitchSwitchTime = now + PITCH_SWITCH_INTERVAL_MILLIS;
      }

      return this.negativePitchPhase ? NEGATIVE_PHASE_PITCH : POSITIVE_PHASE_PITCH;
   }

   private float[] clampToTargetBounds(float yaw, float pitch, LivingEntity target) {
      Vec3 eyePosition = MINECRAFT_CLIENT.player.getEyePosition();
      AABB bounds = target.getBoundingBox();
      float minYaw = Float.POSITIVE_INFINITY;
      float maxYaw = Float.NEGATIVE_INFINITY;
      float minPitch = Float.POSITIVE_INFINITY;
      float maxPitch = Float.NEGATIVE_INFINITY;

      for (double cornerX : new double[]{bounds.minX, bounds.maxX}) {
         for (double cornerY : new double[]{bounds.minY, bounds.maxY}) {
            for (double cornerZ : new double[]{bounds.minZ, bounds.maxZ}) {
               Vec3 relativeCorner = new Vec3(cornerX, cornerY, cornerZ).subtract(eyePosition);
               float cornerYaw = (float)Mth.wrapDegrees(Math.toDegrees(Math.atan2(relativeCorner.z, relativeCorner.x)) - BOUND_CORNER_YAW_OFFSET);
               float cornerPitch = (float)(-Math.toDegrees(Math.atan2(relativeCorner.y, Math.sqrt(relativeCorner.x * relativeCorner.x + relativeCorner.z * relativeCorner.z))));
               minYaw = Math.min(minYaw, cornerYaw);
               maxYaw = Math.max(maxYaw, cornerYaw);
               minPitch = Math.min(minPitch, cornerPitch);
               maxPitch = Math.max(maxPitch, cornerPitch);
            }
         }
      }

      return new float[]{Mth.clamp(yaw, minYaw, maxYaw), Mth.clamp(pitch, minPitch, maxPitch)};
   }

   private Vector2f calculateJitterStep(Vector2f currentRotation, Vector2f targetRotation, boolean targetVisible) {
      int jitterAmount = this.random.nextInt(1) + 1;
      float yawStep = Math.min(Math.max(Math.abs(Mth.wrapDegrees(targetRotation.x - currentRotation.x)), 1.0F), MAX_JITTER_YAW_STEP);
      float nextPitch;
      if (targetVisible) {
         float visiblePitchStep = Math.max(Math.abs(Mth.wrapDegrees(targetRotation.y - currentRotation.y) - VISIBLE_PITCH_STEP_OFFSET), VISIBLE_PITCH_STEP_BASE + jitterAmount);
         nextPitch = Mth.clamp(currentRotation.y + (Mth.wrapDegrees(targetRotation.y - currentRotation.y) > 0.0F ? visiblePitchStep : -visiblePitchStep), MIN_VISIBLE_PITCH, MAX_VISIBLE_PITCH);
      } else {
         float hiddenPitchStep = Math.max(Math.abs(targetRotation.y - currentRotation.y), 1.0F);
         nextPitch = Mth.clamp(currentRotation.y + (targetRotation.y > currentRotation.y ? hiddenPitchStep : -hiddenPitchStep), MIN_HIDDEN_PITCH, MAX_HIDDEN_PITCH);
      }

      float nextYaw = currentRotation.x + (Mth.wrapDegrees(targetRotation.x - currentRotation.x) > 0.0F ? yawStep : -yawStep);
      return new Vector2f(nextYaw, nextPitch);
   }

   private float unwrapYaw(float currentYaw, float targetYaw) {
      return currentYaw + Mth.wrapDegrees(targetYaw - currentYaw);
   }

   private Vector2f calculateJitterTargetRotation(LivingEntity target) {
      float partialTick = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
      Vec3 eyePosition = MINECRAFT_CLIENT.player.getEyePosition(partialTick);
      Vec3 targetPosition = target.getPosition(partialTick);
      AABB bounds = target.getBoundingBox();
      float minYaw = Float.POSITIVE_INFINITY;
      float maxYaw = Float.NEGATIVE_INFINITY;
      float minPitch = Float.POSITIVE_INFINITY;
      float maxPitch = Float.NEGATIVE_INFINITY;

      for (double cornerX : new double[]{bounds.minX, bounds.maxX}) {
         for (double cornerY : new double[]{bounds.minY, bounds.maxY}) {
            for (double cornerZ : new double[]{bounds.minZ, bounds.maxZ}) {
               Vec3 relativeCorner = new Vec3(cornerX, cornerY, cornerZ).subtract(eyePosition);
               float cornerYaw = (float)Mth.wrapDegrees(Math.toDegrees(Math.atan2(relativeCorner.z, relativeCorner.x)) - JITTER_CORNER_YAW_OFFSET);
               float cornerPitch = (float)(-Math.toDegrees(Math.atan2(relativeCorner.y, Math.hypot(relativeCorner.x, relativeCorner.z))));
               minYaw = Math.min(minYaw, cornerYaw);
               maxYaw = Math.max(maxYaw, cornerYaw);
               minPitch = Math.min(minPitch, cornerPitch);
               maxPitch = Math.max(maxPitch, cornerPitch);
            }
         }
      }

      Vec3 relativeTarget = targetPosition.subtract(eyePosition);
      float targetYaw = (float)Mth.wrapDegrees(Math.toDegrees(Math.atan2(relativeTarget.z, relativeTarget.x)) - JITTER_TARGET_YAW_OFFSET);
      float targetPitch = (float)(-Math.toDegrees(Math.atan2(relativeTarget.y, Math.hypot(relativeTarget.x, relativeTarget.z))));
      targetYaw = Mth.clamp(targetYaw, minYaw, maxYaw);
      targetPitch = Mth.clamp(targetPitch, minPitch, maxPitch);
      if (!this.hasLineOfSightToTarget(target)) {
         targetPitch = OCCLUDED_TARGET_PITCH;
      }

      return new Vector2f(targetYaw, targetPitch);
   }

   public DefensiveJitterRotation(Vec3 targetPosition) {
      super(targetPosition);
   }

   private Vector2f calculateDefensiveTargetRotation(LivingEntity target) {
      Vec3 targetDirection = target.position().subtract(MINECRAFT_CLIENT.player.getEyePosition());
      float targetYaw = (float)Mth.wrapDegrees(Math.toDegrees(Math.atan2(targetDirection.z, targetDirection.x)) - DEFENSIVE_YAW_OFFSET);
      float[] clampedAngles = this.clampToTargetBounds(targetYaw, 0.0F, target);
      return new Vector2f(clampedAngles[0], this.nextDefensivePitch());
   }

   @Override
   public void resetState() {
      this.nextPitchSwitchTime = 0L;
      this.negativePitchPhase = true;
   }

   private Vector2f quantizeRotation(Vector2f currentRotation, float targetYaw, float targetPitch) {
      float rotationStep = MouseSensitivityMath.getRotationStep();
      if (rotationStep < MIN_ROTATION_STEP) {
         rotationStep = FALLBACK_ROTATION_STEP;
      }

      targetYaw -= (targetYaw - currentRotation.x) % rotationStep;
      targetPitch -= (targetPitch - currentRotation.y) % rotationStep;
      return new Vector2f(targetYaw, Mth.clamp(targetPitch, MIN_QUANTIZED_PITCH, MAX_QUANTIZED_PITCH));
   }
}
