package sg.mixin;

import sg.mixinbridge.EntityRenderStateIdAccess;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public abstract class EntityRenderStateIdMixin implements EntityRenderStateIdAccess {
   @Unique
   private int rose$entityId;
   private static final int ВьЦ = Integer.MIN_VALUE;

   public EntityRenderStateIdMixin() {
      this.rose$entityId = ВьЦ;
   }

   @Override
   public int rose$getEntityId() {
      return this.rose$entityId;
   }

   @Override
   public void rose$setEntityId(int var1) {
      this.rose$entityId = var1;
   }
}
