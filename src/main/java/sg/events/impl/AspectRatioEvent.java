package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;

public class AspectRatioEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("ВЯ")  float aspectRatio;

   @Generated
   public float getAspectRatio() {
      return this.aspectRatio;
   }

   @Generated
   public void setAspectRatio(float aspectRatio) {
      this.aspectRatio = aspectRatio;
   }

   @Generated
   public AspectRatioEvent(float aspectRatio) {
      this.aspectRatio = aspectRatio;
   }
}
