package sg.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.misc.HpHelper;
import sg.client.RoseClient;
import sg.modules.impl.render.Ambience;

@Mixin(SkyRenderer.class)
public abstract class SkyRendererCustomizationMixin {
   @ModifyExpressionValue(
      method = {"renderSkyDisc", "renderDarkDisc", "renderSun", "renderMoon", "renderStars", "renderSunriseAndSunset", "renderEndSky", "renderEndFlash"},
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/SkyRenderer;renderTarget:Lcom/mojang/blaze3d/pipeline/RenderTarget;")
   )
   private RenderTarget rose$currentSkyTarget(RenderTarget var1) {
      return Minecraft.getInstance().gameRenderer.mainRenderTarget();
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void roseSkyOverrides(ClientLevel var1, float var2, Camera var3, SkyRenderState var4, CallbackInfo var5) {
      if (HpHelper.method_2西()) {
         int var6 = HpHelper.х_a83dd0cc();
         var4.skyColor = var6;
         var4.sunriseAndSunsetColor = var6;
         var4.starBrightness = 0.0F;
         var4.shouldRenderDarkDisc = false;
      } else {
         if (shouldHideVanillaStars()) {
            var4.starBrightness = 0.0F;
         }
      }
   }

   private static boolean shouldHideVanillaStars() {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         Ambience var0 = RoseClient.getInstance().getServices().getModules().getAmbience();
         return var0 != null && var0.shouldHideVanillaSky();
      } else {
         return false;
      }
   }
}
