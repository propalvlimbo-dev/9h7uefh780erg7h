package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;

public class InputEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("с6")  int inputCode;
   private final @com.google.gson.annotations.SerializedName("с_")  int action;

   @Generated
   public int getAction() {
      return this.action;
   }

   @Generated
   public InputEvent(int inputCode, int action) {
      this.inputCode = inputCode;
      this.action = action;
   }

   @Generated
   public int getInputCode() {
      return this.inputCode;
   }
}
