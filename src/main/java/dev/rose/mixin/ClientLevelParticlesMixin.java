package dev.rose.mixin;

import dev.rose.module.Modules;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientLevel.class)
public abstract class ClientLevelParticlesMixin {
   @Inject(method = "addDestroyBlockEffect", at = @At("HEAD"), cancellable = true)
   private void rose$blockBreak(BlockPos pos, BlockState state, CallbackInfo ci) {
      if (Modules.particles() != null && Modules.particles().hideBlockBreak()) {
         ci.cancel();
      }
   }

   @Inject(method = "addBreakingBlockEffect", at = @At("HEAD"), cancellable = true)
   private void rose$blockCrack(BlockPos pos, Direction side, CallbackInfo ci) {
      if (Modules.particles() != null && Modules.particles().hideBlockCrack()) {
         ci.cancel();
      }
   }
}
