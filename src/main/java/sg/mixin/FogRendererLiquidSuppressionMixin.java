package sg.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.modules.impl.render.Removals;

@Mixin(FogRenderer.class)
public class FogRendererLiquidSuppressionMixin {
   private static final float В2ц = 1000000.0F;
   private static final float В2я = 1000001.0F;
   private static final float Вфс = 1000000.0F;
   private static final float ВфВ = 1000001.0F;

   @Inject(method = "setupFog", at = @At("RETURN"))
   private void removeLiquidFog(Camera var1, int var2, DeltaTracker var3, float var4, ClientLevel var5, CallbackInfoReturnable<FogData> var6) {
      if (Removals.ыу()) {
         FogType var7 = var1.getFluidInCamera();
         if (var7 == FogType.WATER || var7 == FogType.LAVA) {
            FogData var8 = (FogData)var6.getReturnValue();
            var8.environmentalStart = В2ц;
            var8.environmentalEnd = В2я;
            var8.renderDistanceStart = Вфс;
            var8.renderDistanceEnd = ВфВ;
         }
      }
   }
}
