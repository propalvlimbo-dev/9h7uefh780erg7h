package sg.events.impl;

import lombok.Generated;
import net.minecraft.world.InteractionHand;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class SwingDurationEvent extends CancellableEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("е")  InteractionHand hand;
   private @com.google.gson.annotations.SerializedName("е诶")  int durationControlValue;

   @Generated
   public void setHand(InteractionHand hand) {
      this.hand = hand;
   }

   @Generated
   public SwingDurationEvent(InteractionHand hand, int durationControlValue) {
      this.hand = hand;
      this.durationControlValue = durationControlValue;
   }

   @Generated
   public void setDurationControlValue(int durationControlValue) {
      this.durationControlValue = durationControlValue;
   }

   @Generated
   public int getDurationControlValue() {
      return this.durationControlValue;
   }

   @Generated
   public InteractionHand getHand() {
      return this.hand;
   }
}
