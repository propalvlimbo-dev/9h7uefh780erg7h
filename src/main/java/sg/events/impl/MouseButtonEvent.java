package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class MouseButtonEvent extends CancellableEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("Д")  float mouseX;
   private final @com.google.gson.annotations.SerializedName("Ъ")  float mouseY;
   private final @com.google.gson.annotations.SerializedName("А")  int button;

   @Generated
   public float getMouseX() {
      return this.mouseX;
   }

   @Generated
   public MouseButtonEvent(float mouseX, float mouseY, int button) {
      this.mouseX = mouseX;
      this.mouseY = mouseY;
      this.button = button;
   }

   @Generated
   public float getMouseY() {
      return this.mouseY;
   }

   @Generated
   public int getButton() {
      return this.button;
   }
}
