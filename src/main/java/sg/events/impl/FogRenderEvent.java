package sg.events.impl;

import lombok.Generated;
import net.minecraft.client.Camera;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class FogRenderEvent extends CancellableEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("В")  Camera camera;
   private final @com.google.gson.annotations.SerializedName("ЪЛ")  float baseEndDistance;
   private @com.google.gson.annotations.SerializedName("Ъ诶")  float startDistance;
   private @com.google.gson.annotations.SerializedName("Ъ1")  float endDistance;
   private @com.google.gson.annotations.SerializedName("ДА")  int color;
   private @com.google.gson.annotations.SerializedName("еш")  boolean distanceOverride;
   private @com.google.gson.annotations.SerializedName("еИ")  boolean colorOverride;

   @Generated
   public boolean hasDistanceOverride() {
      return this.distanceOverride;
   }

   @Generated
   public float getEndDistance() {
      return this.endDistance;
   }

   @Generated
   public boolean hasColorOverride() {
      return this.colorOverride;
   }

   @Generated
   public float getBaseEndDistance() {
      return this.baseEndDistance;
   }

   @Generated
   public int getColor() {
      return this.color;
   }

   public void setDistanceOverride(float startDistance, float endDistance) {
      this.startDistance = startDistance;
      this.endDistance = endDistance;
      this.distanceOverride = true;
      this.setCancelled(true);
   }

   public FogRenderEvent(Camera camera, float endDistance) {
      this.camera = camera;
      this.baseEndDistance = endDistance;
      this.endDistance = endDistance;
   }

   public void setColorOverride(int color) {
      this.color = color;
      this.colorOverride = true;
      this.setCancelled(true);
   }

   @Generated
   public Camera getCamera() {
      return this.camera;
   }

   @Generated
   public float getStartDistance() {
      return this.startDistance;
   }
}
