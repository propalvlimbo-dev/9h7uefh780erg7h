package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class PlayerTurnEvent extends CancellableEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("сМ")  float yawDelta;
   private @com.google.gson.annotations.SerializedName("сН")  float pitchDelta;

   @Generated
   public PlayerTurnEvent(float yawDelta, float pitchDelta) {
      this.yawDelta = yawDelta;
      this.pitchDelta = pitchDelta;
   }

   @Generated
   public float getPitchDelta() {
      return this.pitchDelta;
   }

   @Generated
   public void setYawDelta(float yawDelta) {
      this.yawDelta = yawDelta;
   }

   @Generated
   public void setPitchDelta(float pitchDelta) {
      this.pitchDelta = pitchDelta;
   }

   @Generated
   public float getYawDelta() {
      return this.yawDelta;
   }
}
