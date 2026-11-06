package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class ChatMouseButtonEvent extends CancellableEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("а_")  float mouseX;
   private final @com.google.gson.annotations.SerializedName("аШ")  float mouseY;
   private final @com.google.gson.annotations.SerializedName("Ъф")  int button;
   private final @com.google.gson.annotations.SerializedName("АМ")  boolean doubleClick;

   @Generated
   public float getMouseX() {
      return this.mouseX;
   }

   @Generated
   public float getMouseY() {
      return this.mouseY;
   }

   @Generated
   public int getButton() {
      return this.button;
   }

   @Generated
   public ChatMouseButtonEvent(float mouseX, float mouseY, int button, boolean doubleClick) {
      this.mouseX = mouseX;
      this.mouseY = mouseY;
      this.button = button;
      this.doubleClick = doubleClick;
   }

   @Generated
   public boolean isDoubleClick() {
      return this.doubleClick;
   }
}
