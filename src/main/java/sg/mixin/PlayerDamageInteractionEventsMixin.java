package sg.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.modules.impl.misc.AutoBrewerBuilder;
import sg.client.RoseClient;
import sg.events.impl.LivingEntityDamageEvent;

@Mixin(Player.class)
public abstract class PlayerDamageInteractionEventsMixin {
   @Inject(method = "animateHurt", at = @At("HEAD"))
   private void onAnimateDamage(float var1, CallbackInfo var2) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getEventBus() != null) {
         RoseClient.getInstance().getEventBus().post(new LivingEntityDamageEvent((LivingEntity)(Object)this, var1));
      }
   }

   @Inject(method = "isSecondaryUseActive", at = @At("HEAD"), cancellable = true)
   private void autoBrewerInteractionOverride(CallbackInfoReturnable<Boolean> var1) {
      if (AutoBrewerBuilder.с4) {
         var1.setReturnValue(false);
      } else {
         if (AutoBrewerBuilder.сп) {
            var1.setReturnValue(true);
         }
      }
   }
}
