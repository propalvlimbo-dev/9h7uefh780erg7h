package sg.rotation;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.ec.MinecraftAccess;
import sg.modules.impl.movement.Speed;

public class RotationStrategy implements MinecraftAccess {
   private @com.google.gson.annotations.SerializedName("В")  RotationStrategy.Speed speed = RotationStrategy.Speed.SLOW;
   private @com.google.gson.annotations.SerializedName("ЯВ")  boolean usesVanillaCameraRotation = false;
   private @com.google.gson.annotations.SerializedName("ЯЯ")  boolean captureCameraRotationOnActivate = false;
   protected @com.google.gson.annotations.SerializedName("ЯХ")  boolean returningToCamera = false;
   @SerializedName("2")
   protected Vec3 targetPosition;

   @Generated
   public boolean isReturningToCamera() {
      return this.returningToCamera;
   }

   public Vector2f getTargetRotation() {
      Vec3 eyePosition = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 targetDirection = this.targetPosition.subtract(eyePosition).normalize();
      float targetYaw = (float)Math.toDegrees(Math.atan2(-targetDirection.x, targetDirection.z));
      float targetPitchDegrees = (float)Math.toDegrees(Math.asin(targetDirection.y));
      return new Vector2f(targetYaw, -targetPitchDegrees);
   }

   @Generated
   public void setReturningToCamera(boolean returningToCamera) {
      this.returningToCamera = returningToCamera;
   }

   @Generated
   public Vec3 getTargetPosition() {
      return this.targetPosition;
   }

   public void setTargetPosition(Vec3 targetPosition) {
      this.targetPosition = targetPosition;
      this.returningToCamera = false;
   }

   public void refreshRandomState() {
   }

   @Generated
   public RotationStrategy.Speed getSpeed() {
      return this.speed;
   }

   public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
      return new Vector2f();
   }

   @Generated
   public boolean shouldCaptureCameraRotationOnActivate() {
      return this.captureCameraRotationOnActivate;
   }

   public RotationStrategy(Vec3 targetPosition) {
      this.targetPosition = targetPosition;
   }

   @Generated
   public void setSpeed(RotationStrategy.Speed speed) {
      this.speed = speed;
   }

   public void resetState() {
   }

   @Generated
   public void setCaptureCameraRotationOnActivate(boolean capture) {
      this.captureCameraRotationOnActivate = capture;
   }

   public Vector2f я(Vector2f var1, Vector2f var2) {
      return new Vector2f();
   }

   @Generated
   public boolean usesVanillaCameraRotation() {
      return this.usesVanillaCameraRotation;
   }

   public RotationStrategy(Vec3 targetPosition, RotationStrategy.Speed speed) {
      this.targetPosition = targetPosition;
      this.speed = speed;
   }

   @Generated
   public void setUsesVanillaCameraRotation(boolean enabled) {
      this.usesVanillaCameraRotation = enabled;
   }

   public enum Speed {
      SLOW,
      MEDIUM,
      FAST;
   }
}
