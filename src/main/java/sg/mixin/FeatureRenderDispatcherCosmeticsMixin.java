package sg.mixin;

import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher.PreparedFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settings.render.HmiHeldItemQueue;
import sg.ec.HmiHandSwitchQueue;
import sg.render.WingOutlineQueue;

@Mixin(FeatureRenderDispatcher.class)
public class FeatureRenderDispatcherCosmeticsMixin {
   @Inject(method = "prepareFrame", at = @At("HEAD"))
   private void rose$submitWingOutlines(SubmitNodeStorage var1, CallbackInfoReturnable<PreparedFrame> var2) {
      WingOutlineQueue.flush(var1);
   }

   @Inject(method = "renderAllFeatures", at = @At("TAIL"))
   private void rose$clearRenderStorage(SubmitNodeStorage var1, CallbackInfo var2) {
      HmiHeldItemQueue.н3();
      HmiHandSwitchQueue.фс();
   }
}
