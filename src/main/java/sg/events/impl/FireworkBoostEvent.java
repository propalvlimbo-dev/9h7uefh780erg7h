package sg.events.impl;

import lombok.Generated;
import net.minecraft.world.entity.LivingEntity;
import sg.events.ClientEvent;

public class FireworkBoostEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("И")  LivingEntity entity;
   private @com.google.gson.annotations.SerializedName("ЪО")  float xBoostMultiplier;
   private @com.google.gson.annotations.SerializedName("ЪФ")  float yBoostMultiplier;
   private @com.google.gson.annotations.SerializedName("ЪЙ")  float zBoostMultiplier;

   @Generated
   public float getZBoostMultiplier() {
      return this.zBoostMultiplier;
   }

   @Generated
   public LivingEntity getEntity() {
      return this.entity;
   }

   @Generated
   public void setXBoostMultiplier(float xBoostMultiplier) {
      this.xBoostMultiplier = xBoostMultiplier;
   }

   public FireworkBoostEvent(float xBoostMultiplier, float yBoostMultiplier, float zBoostMultiplier) {
      this(null, xBoostMultiplier, yBoostMultiplier, zBoostMultiplier);
   }

   public FireworkBoostEvent(LivingEntity entity, float xBoostMultiplier, float yBoostMultiplier, float zBoostMultiplier) {
      this.entity = entity;
      this.xBoostMultiplier = xBoostMultiplier;
      this.yBoostMultiplier = yBoostMultiplier;
      this.zBoostMultiplier = zBoostMultiplier;
   }

   @Generated
   public void setEntity(LivingEntity entity) {
      this.entity = entity;
   }

   @Generated
   public void setYBoostMultiplier(float yBoostMultiplier) {
      this.yBoostMultiplier = yBoostMultiplier;
   }

   @Generated
   public void setZBoostMultiplier(float zBoostMultiplier) {
      this.zBoostMultiplier = zBoostMultiplier;
   }

   @Generated
   public float getYBoostMultiplier() {
      return this.yBoostMultiplier;
   }

   @Generated
   public float getXBoostMultiplier() {
      return this.xBoostMultiplier;
   }
}
