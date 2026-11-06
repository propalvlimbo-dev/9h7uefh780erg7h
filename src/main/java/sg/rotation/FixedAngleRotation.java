package sg.rotation;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.util.input.MouseSensitivityMath;
import sg.rotation.RotationStrategy;

public class FixedAngleRotation extends RotationStrategy {
   private final @com.google.gson.annotations.SerializedName("Х")  Vector2f targetAngles = new Vector2f();
   private static final float П诶 = -90.0F;
   private static final float П1 = 90.0F;
   private static final float ПЬ = 75.0F;
   private static final float П衣 = 45.0F;
   private static final float Пш = -90.0F;
   private static final float ПИ = 90.0F;

   @Override
   public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
      float yawDelta = Mth.wrapDegrees(targetRotation.x - currentRotation.x);
      float pitchDelta = targetRotation.y - currentRotation.y;
      float maxYawStep = ПЬ;
      float maxPitchStep = П衣;
      float nextYaw = currentRotation.x + Mth.clamp(yawDelta, -maxYawStep, maxYawStep);
      float nextPitch = currentRotation.y + Mth.clamp(pitchDelta, -maxPitchStep, maxPitchStep);
      nextPitch = Mth.clamp(nextPitch, Пш, ПИ);
      return new Vector2f(MouseSensitivityMath.snapRotationDelta(nextYaw), MouseSensitivityMath.snapRotationDelta(nextPitch));
   }

   @Override
   public Vector2f getTargetRotation() {
      return new Vector2f(this.targetAngles);
   }

   public FixedAngleRotation() {
      super(Vec3.ZERO);
   }

   public void setTargetAngles(float yaw, float pitch) {
      this.targetAngles.set(Mth.wrapDegrees(yaw), Mth.clamp(pitch, П诶, П1));
      this.returningToCamera = false;
   }
}
