package sg.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "einstein.subtle_effects.client.renderer.entity.EinsteinSolarSystemLayer", remap = false)
public abstract class EinsteinSolarSystemLayerDevelopmentMixin {
   @ModifyExpressionValue(
      method = "shouldRender",
      at = @At(value = "INVOKE", target = "Leinstein/subtle_effects/platform/services/IPlatformHelper;isDevelopmentEnvironment()Z"),
      require = 0,
      remap = false
   )
   private static boolean rose$disableDevelopmentHeads(boolean var0) {
      return false;
   }
}
