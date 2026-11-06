package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class EatBobbingEvent extends CancellableEvent implements ClientEvent {
   public @com.google.gson.annotations.SerializedName("ЯТ")  float bobbingScale;

   @Generated
   public EatBobbingEvent(float bobbingScale) {
      this.bobbingScale = bobbingScale;
   }

   @Generated
   public void setBobbingScale(float bobbingScale) {
      this.bobbingScale = bobbingScale;
   }

   @Generated
   public float getBobbingScale() {
      return this.bobbingScale;
   }
}
