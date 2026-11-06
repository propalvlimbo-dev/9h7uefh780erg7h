package sg.events.impl;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import sg.events.ClientEvent;

public final class UiAnimationEvents {
   @Generated
   private UiAnimationEvents() {
   }

   public static final class HudDisplacementQuery implements ClientEvent {
      private @com.google.gson.annotations.SerializedName("ВР")  boolean displacementEnabled;

      @Generated
      public void setDisplacementEnabled(boolean displacementEnabled) {
         this.displacementEnabled = displacementEnabled;
      }

      @Generated
      public boolean isDisplacementEnabled() {
         return this.displacementEnabled;
      }
   }

   public static final class ScoreboardPlacementQuery implements ClientEvent {
      private @com.google.gson.annotations.SerializedName("ХМ")  boolean customPlacementEnabled;

      @Generated
      public boolean isCustomPlacementEnabled() {
         return this.customPlacementEnabled;
      }

      @Generated
      public void setCustomPlacementEnabled(boolean customPlacementEnabled) {
         this.customPlacementEnabled = customPlacementEnabled;
      }
   }

   public static final class PerspectiveAnimationEvent implements ClientEvent {
      private final @com.google.gson.annotations.SerializedName("ВХ")  float progress;
      private @com.google.gson.annotations.SerializedName("х")  boolean animationEnabled;
      private @com.google.gson.annotations.SerializedName("сВ")  long durationNanos;
      private @com.google.gson.annotations.SerializedName("Ве")  float easedProgress;

      @Generated
      public void setDurationNanos(long durationNanos) {
         this.durationNanos = durationNanos;
      }

      @Generated
      public boolean isAnimationEnabled() {
         return this.animationEnabled;
      }

      @Generated
      public long getDurationNanos() {
         return this.durationNanos;
      }

      @Generated
      public void setEasedProgress(float easedProgress) {
         this.easedProgress = easedProgress;
      }

      @Generated
      public float getEasedProgress() {
         return this.easedProgress;
      }

      @Generated
      public void setAnimationEnabled(boolean animationEnabled) {
         this.animationEnabled = animationEnabled;
      }

      @Generated
      public float getProgress() {
         return this.progress;
      }

      @Generated
      public PerspectiveAnimationEvent(float progress) {
         this.progress = progress;
      }
   }

   public static final class PlayerListAnimationEvent implements ClientEvent {
      @SerializedName("2")
      private final boolean requestedVisible;
      private @com.google.gson.annotations.SerializedName("ф")  boolean animationEnabled;
      private @com.google.gson.annotations.SerializedName("сэ")  float scale = 1.0F;

      @Generated
      public boolean isAnimationEnabled() {
         return this.animationEnabled;
      }

      @Generated
      public boolean isRequestedVisible() {
         return this.requestedVisible;
      }

      @Generated
      public void setAnimationEnabled(boolean animationEnabled) {
         this.animationEnabled = animationEnabled;
      }

      @Generated
      public float getScale() {
         return this.scale;
      }

      @Generated
      public void setScale(float scale) {
         this.scale = scale;
      }

      @Generated
      public PlayerListAnimationEvent(boolean requestedVisible) {
         this.requestedVisible = requestedVisible;
      }
   }

   public static final class InventoryAnimationEvent implements ClientEvent {
      private final @com.google.gson.annotations.SerializedName("В")  UiAnimationEvents.InventoryAnimationEvent.Phase phase;
      private @com.google.gson.annotations.SerializedName("си")  boolean animationEnabled;
      private @com.google.gson.annotations.SerializedName("Я弟")  float scale = 1.0F;

      @Generated
      public float getScale() {
         return this.scale;
      }

      @Generated
      public UiAnimationEvents.InventoryAnimationEvent.Phase getPhase() {
         return this.phase;
      }

      @Generated
      public InventoryAnimationEvent(UiAnimationEvents.InventoryAnimationEvent.Phase phase) {
         this.phase = phase;
      }

      @Generated
      public void setAnimationEnabled(boolean animationEnabled) {
         this.animationEnabled = animationEnabled;
      }

      @Generated
      public void setScale(float scale) {
         this.scale = scale;
      }

      @Generated
      public boolean isAnimationEnabled() {
         return this.animationEnabled;
      }

      public enum Phase {
         START,
         UPDATE;
      }
   }
}
