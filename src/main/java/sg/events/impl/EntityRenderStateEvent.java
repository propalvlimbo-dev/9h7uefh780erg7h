package sg.events.impl;

import lombok.Generated;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import sg.events.ClientEvent;

public class EntityRenderStateEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("А")  Entity entity;
   private final @com.google.gson.annotations.SerializedName("В")  EntityRenderState renderState;

   @Generated
   public EntityRenderStateEvent(Entity entity, EntityRenderState renderState) {
      this.entity = entity;
      this.renderState = renderState;
   }

   @Generated
   public EntityRenderState getRenderState() {
      return this.renderState;
   }

   @Generated
   public Entity getEntity() {
      return this.entity;
   }
}
