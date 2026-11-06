package sg.rotation;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.rotation.RotationStrategy;
import sg.util.input.MouseSensitivityMath;

public class DirectAimRotation extends RotationStrategy {
   private static final double field_9У = 999.0;
   private static final double field_9З = 999.0;
   private static final double MAX_YAW_DELTA = 999.0;
   private static final double MIN_YAW_DELTA = -999.0;
   private static final double MAX_PITCH_DELTA = 999.0;
   private static final double MIN_PITCH_DELTA = -999.0;
   private static final float MIN_PITCH = -90.0F;
   private static final float MAX_PITCH = 90.0F;

   @Override
   public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
      float yaw = targetRotation.x;
      float pitch = targetRotation.y;
      float currentYaw = currentRotation.x;
      float currentPitch = currentRotation.y;
      double yawDelta = Mth.wrapDegrees(yaw - currentYaw);
      double pitchDelta = pitch - currentPitch;
      double var11 = field_9У;
      double var13 = field_9З;
      float yawStep = (float)Math.max(Math.min(yawDelta, MAX_YAW_DELTA), MIN_YAW_DELTA);
      float pitchStep = (float)Math.max(Math.min(pitchDelta, MAX_PITCH_DELTA), MIN_PITCH_DELTA);
      yaw = currentYaw + yawStep;
      pitch = currentPitch + pitchStep;
      Vector2f clampedRotation = new Vector2f(yaw, pitch);
      yaw = clampedRotation.x;
      pitch = Math.max(MIN_PITCH, Math.min(MAX_PITCH, clampedRotation.y));
      return new Vector2f(MouseSensitivityMath.quantizeRotationDelta(yaw), MouseSensitivityMath.quantizeRotationDelta(pitch));
   }

   public DirectAimRotation(Vec3 targetPosition) {
      super(targetPosition, RotationStrategy.Speed.FAST);
   }
}
