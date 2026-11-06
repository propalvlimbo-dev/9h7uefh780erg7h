package sg.mixin;

import sg.mixinbridge.RenderSuppressionQueries;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelWeatherSuppressionMixin {
   @Inject(method = "tickWeatherEffects", at = @At("HEAD"), cancellable = true)
   private void rose$weatherParticles(CallbackInfo var1) {
      if (RenderSuppressionQueries.дЕ()) {
         var1.cancel();
      }
   }
}
