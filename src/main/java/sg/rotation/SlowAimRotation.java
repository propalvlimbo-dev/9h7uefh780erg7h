package sg.rotation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.rotation.RotationStrategy;
import sg.util.input.MouseSensitivityMath;

public class SlowAimRotation extends RotationStrategy {
   private static final double MAX_YAW_STEP = 360.0;
   private static final double MAX_PITCH_STEP = 360.0;
   private static final float YAW_JITTER_RANGE = 5.0F;
   private static final float PITCH_JITTER_RANGE = 4.0F;
   private static final float FPS_JITTER_DIVISOR = 20.0F;
   private static final double EXTRA_JITTER_ITERATIONS = 20.0;
   private static final double JITTER_CENTER = 0.5;
   private static final double YAW_JITTER_DIVISOR = 900.0;
   private static final double PITCH_JITTER_DIVISOR = 100.0;
   private static final float MIN_PITCH = -90.0F;
   private static final float MAX_PITCH = 90.0F;

   public SlowAimRotation(Vec3 targetPosition) {
      super(targetPosition, RotationStrategy.Speed.SLOW);
   }

   @Override
   public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
      float yaw = targetRotation.x;
      float pitch = targetRotation.y;
      float currentYaw = currentRotation.x;
      float currentPitch = currentRotation.y;
      double yawDelta = Mth.wrapDegrees(yaw - currentYaw);
      double pitchDelta = pitch - currentPitch;
      double rotationDistance = Math.sqrt(yawDelta * yawDelta + pitchDelta * pitchDelta);
      if (rotationDistance == 0.0) {
         return new Vector2f(currentRotation.x, currentRotation.y);
      }

      double yawShare = Math.abs(yawDelta / rotationDistance);
      double pitchShare = Math.abs(pitchDelta / rotationDistance);
      double maxYawStep = MAX_YAW_STEP * yawShare;
      double maxPitchStep = MAX_PITCH_STEP * pitchShare;
      float yawStep = (float)Math.max(Math.min(yawDelta, maxYawStep), -maxYawStep);
      float pitchStep = (float)Math.max(Math.min(pitchDelta, maxPitchStep), -maxPitchStep);
      float yawJitter = !this.returningToCamera ? this.randomOffset(YAW_JITTER_RANGE) : 0.0F;
      float pitchJitter = !this.returningToCamera ? this.randomOffset(PITCH_JITTER_RANGE) : 0.0F;
      yaw = currentYaw + yawStep + yawJitter;
      pitch = currentPitch + pitchStep + pitchJitter;

      for (int var25 = 1; var25 <= (int)(Minecraft.getInstance().getFps() / FPS_JITTER_DIVISOR + Math.random() * EXTRA_JITTER_ITERATIONS); var25++) {
         if (Math.abs(yawStep) + Math.abs(pitchStep) > 1.0F) {
            yaw += (float)((Math.random() - JITTER_CENTER) / YAW_JITTER_DIVISOR);
            pitch -= (float)(Math.random() / PITCH_JITTER_DIVISOR);
         }

         Vector2f candidateRotation = new Vector2f(yaw, pitch);
         yaw = candidateRotation.x;
         pitch = Math.max(MIN_PITCH, Math.min(MAX_PITCH, candidateRotation.y));
      }

      return new Vector2f(MouseSensitivityMath.quantizeRotationDelta(yaw), MouseSensitivityMath.quantizeRotationDelta(pitch));
   }
}
