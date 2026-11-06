package dev.rose.mixin;

import dev.rose.module.impl.world.ItemPhysicsModule;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(ItemEntityRenderState.class)
public abstract class ItemEntityRenderStateMixin implements ItemPhysicsModule.State {
   @Unique
   private boolean rose$onGround;
   @Unique
   private int rose$id;
   @Unique
   private boolean rose$block;

   @Override
   public void rose$physics(boolean onGround, int id, boolean block) {
      this.rose$onGround = onGround;
      this.rose$id = id;
      this.rose$block = block;
   }

   @Override
   public boolean rose$onGround() {
      return this.rose$onGround;
   }

   @Override
   public int rose$id() {
      return this.rose$id;
   }

   @Override
   public boolean rose$block() {
      return this.rose$block;
   }
}
