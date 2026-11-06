package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;

public class CameraRotationEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("еч")  float yaw;
   private @com.google.gson.annotations.SerializedName("еЭ")  float pitch;

   @Generated
   public float getYaw() {
      return this.yaw;
   }

   @Generated
   public CameraRotationEvent(float yaw, float pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
   }

   @Generated
   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   @Generated
   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   @Generated
   public float getPitch() {
      return this.pitch;
   }
}
