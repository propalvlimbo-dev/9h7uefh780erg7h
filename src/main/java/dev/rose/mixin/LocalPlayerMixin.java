package dev.rose.mixin;

import dev.rose.module.Modules;
import dev.rose.module.impl.ItemLocksModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
   @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
   private void rose$lockedDrop(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
      ItemLocksModule locks = Modules.itemLocks();
      LocalPlayer player = (LocalPlayer)(Object)this;
      if (locks != null && locks.isLocked(player.getInventory().getSelectedSlot())) {
         locks.notifyBlocked();
         cir.setReturnValue(false);
      }
   }
}
