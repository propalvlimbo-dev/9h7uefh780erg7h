package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.rose.module.Modules;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(LightTexture.class)
public abstract class LightTextureMixin {
   @ModifyExpressionValue(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
   private float rose$fullbrightGamma(float gamma) {
      return Modules.fullbright() != null ? Modules.fullbright().gamma(gamma) : gamma;
   }
}
