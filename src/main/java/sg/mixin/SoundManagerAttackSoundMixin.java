package sg.mixin;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.modules.impl.render.HitEffect;

@Mixin(SoundManager.class)
public class SoundManagerAttackSoundMixin {
   @Inject(
      method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;",
      at = @At("HEAD"),
      cancellable = true
   )
   private void hitEffectMuteVanillaAttack(SoundInstance var1, CallbackInfoReturnable<PlayResult> var2) {
      if (HitEffect.必(var1)) {
         var2.setReturnValue(PlayResult.NOT_STARTED);
      }
   }

   @Inject(method = "playDelayed(Lnet/minecraft/client/resources/sounds/SoundInstance;I)V", at = @At("HEAD"), cancellable = true)
   private void hitEffectMuteVanillaAttackDelayed(SoundInstance var1, int var2, CallbackInfo var3) {
      if (HitEffect.必(var1)) {
         var3.cancel();
      }
   }
}
