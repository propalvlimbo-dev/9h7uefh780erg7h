package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;

public class MouseDeltaEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("ш")  double deltaX;
   private final @com.google.gson.annotations.SerializedName("И")  double deltaY;

   @Generated
   public double getDeltaX() {
      return this.deltaX;
   }

   @Generated
   public double getDeltaY() {
      return this.deltaY;
   }

   @Generated
   public MouseDeltaEvent(double deltaX, double deltaY) {
      this.deltaX = deltaX;
      this.deltaY = deltaY;
   }
}
