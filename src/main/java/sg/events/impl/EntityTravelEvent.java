package sg.events.impl;

import lombok.Generated;
import net.minecraft.world.entity.LivingEntity;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class EntityTravelEvent extends CancellableEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("Л")  LivingEntity entity;

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof EntityTravelEvent;
   }

   @Generated
   @Override
   public int hashCode() {
      byte hashMultiplier = 59;
      byte initialHash = 1;
      LivingEntity entity = this.getEntity();
      return initialHash * 59 + (entity == null ? 43 : entity.hashCode());
   }

   @Generated
   public void setEntity(LivingEntity entity) {
      this.entity = entity;
   }

   @Generated
   public LivingEntity getEntity() {
      return this.entity;
   }

   @Generated
   public EntityTravelEvent(LivingEntity entity) {
      this.entity = entity;
   }

   @Generated
   @Override
   public boolean equals(Object other) {
      if (other == this) {
         return true;
      } else if (!(other instanceof EntityTravelEvent otherEvent)) {
         return false;
      } else {
         if (!otherEvent.canEqual(this)) {
            return false;
         }

         LivingEntity thisEntity = this.getEntity();
         LivingEntity otherEntity = otherEvent.getEntity();
         return thisEntity == null ? otherEntity == null : thisEntity.equals(otherEntity);
      }
   }

   @Generated
   @Override
   public String toString() {
      return "EventTraveling(entity=" + this.getEntity() + ")";
   }
}
