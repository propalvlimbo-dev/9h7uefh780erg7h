package sg.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.client.RoseClient;
import sg.events.impl.FogRenderEvent;
import sg.util.time.ColorMath;

@Mixin(FogRenderer.class)
public class FogRendererCustomizationMixin {
   private static final float Яод = 16.0F;
   private static final float ЯоБ = 1.0E7F;
   private static final float ЯоС = 1.0E7F;

   @Inject(method = "setupFog", at = @At("RETURN"))
   private void modifyFog(Camera var1, int var2, DeltaTracker var3, float var4, ClientLevel var5, CallbackInfoReturnable<FogData> var6) {
      if (RoseClient.getInstance() != null) {
         FogData var7 = (FogData)var6.getReturnValue();
         FogRenderEvent var8 = new FogRenderEvent(var1, Math.max(Яод, var7.renderDistanceEnd));
         RoseClient.getInstance().getEventBus().post(var8);
         if (var8.isCancelled()) {
            if (var8.hasColorOverride()) {
               var7.color.set(ColorMath.toRgbaVector(var8.getColor()));
               if (var7.color.w == 0.0F) {
                  var7.color.w = 1.0F;
               }
            }

            if (var8.hasDistanceOverride()) {
               var7.environmentalStart = ЯоБ;
               var7.environmentalEnd = ЯоС;
               var7.renderDistanceStart = var8.getStartDistance();
               var7.renderDistanceEnd = var8.getEndDistance();
               var7.skyEnd = var8.getEndDistance();
               var7.cloudEnd = var8.getEndDistance();
            }
         }
      }
   }
}
