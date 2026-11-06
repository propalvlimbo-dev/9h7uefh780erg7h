package sg.events.impl;

import lombok.Generated;
import net.minecraft.world.entity.Entity;
import sg.events.ClientEvent;

public class EntityRemovalEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("Д")  Entity entity;

   @Generated
   public void setEntity(Entity entity) {
      this.entity = entity;
   }

   @Generated
   public EntityRemovalEvent(Entity entity) {
      this.entity = entity;
   }

   @Generated
   public Entity getEntity() {
      return this.entity;
   }
}
