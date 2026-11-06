package sg.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import sg.client.RoseClient;
import sg.modules.impl.misc.Optimization;

@Mixin(LevelExtractor.class)
public class LevelExtractorEntityVisibilityMixin {
   @ModifyArg(
      method = "extractVisibleEntities",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setViewScale(D)V"),
      index = 0,
      require = 0
   )
   private double scaleEntityDistance(double var1) {
      return Optimization.мш(var1);
   }

   @Redirect(
      method = "extractVisibleEntities(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/DeltaTracker;Lnet/minecraft/client/renderer/state/level/LevelRenderState;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;isDetached()Z", ordinal = 0),
      require = 1
   )
   private boolean playerrneder(Camera var1) {
      return var1.isDetached() || RoseClient.getInstance() != null && RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled();
   }
}
