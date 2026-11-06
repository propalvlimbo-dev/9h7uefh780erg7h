package sg.events.impl;

import lombok.Generated;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class MovementInputEvent extends CancellableEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("ВА")  float forwardAmount;
   private @com.google.gson.annotations.SerializedName("В4")  float leftAmount;
   private @com.google.gson.annotations.SerializedName("Е")  boolean forwardPressed;
   private @com.google.gson.annotations.SerializedName("П")  boolean backwardPressed;
   private @com.google.gson.annotations.SerializedName("西")  boolean leftPressed;
   private @com.google.gson.annotations.SerializedName("д")  boolean rightPressed;
   private @com.google.gson.annotations.SerializedName("Б")  boolean jumpPressed;
   private @com.google.gson.annotations.SerializedName("С")  boolean sneakPressed;
   private @com.google.gson.annotations.SerializedName("з")  boolean sprintPressed;

   @Generated
   public MovementInputEvent(float forwardAmount, float leftAmount, boolean forwardPressed, boolean backwardPressed, boolean leftPressed, boolean rightPressed, boolean jumpPressed, boolean sneakPressed, boolean sprintPressed) {
      this.forwardAmount = forwardAmount;
      this.leftAmount = leftAmount;
      this.forwardPressed = forwardPressed;
      this.backwardPressed = backwardPressed;
      this.leftPressed = leftPressed;
      this.rightPressed = rightPressed;
      this.jumpPressed = jumpPressed;
      this.sneakPressed = sneakPressed;
      this.sprintPressed = sprintPressed;
   }

   @Generated
   public void setForwardPressed(boolean forwardPressed) {
      this.forwardPressed = forwardPressed;
   }

   @Generated
   public float getForwardAmount() {
      return this.forwardAmount;
   }

   @Generated
   public boolean isBackwardPressed() {
      return this.backwardPressed;
   }

   @Generated
   public void setForwardAmount(float forwardAmount) {
      this.forwardAmount = forwardAmount;
   }

   @Generated
   public boolean isSprintPressed() {
      return this.sprintPressed;
   }

   @Generated
   public void setSneakPressed(boolean sneakPressed) {
      this.sneakPressed = sneakPressed;
   }

   @Generated
   public void setSprintPressed(boolean sprintPressed) {
      this.sprintPressed = sprintPressed;
   }

   @Generated
   public void setRightPressed(boolean rightPressed) {
      this.rightPressed = rightPressed;
   }

   @Generated
   public boolean isForwardPressed() {
      return this.forwardPressed;
   }

   @Generated
   public void setLeftAmount(float leftAmount) {
      this.leftAmount = leftAmount;
   }

   @Generated
   public void setBackwardPressed(boolean backwardPressed) {
      this.backwardPressed = backwardPressed;
   }

   @Generated
   public boolean isJumpPressed() {
      return this.jumpPressed;
   }

   @Generated
   public void setLeftPressed(boolean leftPressed) {
      this.leftPressed = leftPressed;
   }

   @Generated
   public boolean isRightPressed() {
      return this.rightPressed;
   }

   @Generated
   public boolean isSneakPressed() {
      return this.sneakPressed;
   }

   @Generated
   public boolean isLeftPressed() {
      return this.leftPressed;
   }

   @Generated
   public float getLeftAmount() {
      return this.leftAmount;
   }

   @Generated
   public void setJumpPressed(boolean jumpPressed) {
      this.jumpPressed = jumpPressed;
   }
}
