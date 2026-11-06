package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;

public class FireworkBoostRotationEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("Я西")  float yaw;
   private @com.google.gson.annotations.SerializedName("Яд")  float pitch;

   @Generated
   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   @Generated
   public float getYaw() {
      return this.yaw;
   }

   @Generated
   public FireworkBoostRotationEvent(float yaw, float pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
   }

   @Generated
   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   @Generated
   public float getPitch() {
      return this.pitch;
   }
}
