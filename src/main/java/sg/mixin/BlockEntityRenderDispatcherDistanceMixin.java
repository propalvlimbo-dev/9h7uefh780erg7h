package sg.mixin;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.modules.impl.misc.Optimization;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherDistanceMixin {
   @Shadow
   private Vec3 cameraPos;

   @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true, require = 0)
   private <E extends BlockEntity, S extends BlockEntityRenderState> void cullDistantBlockEntities(
      E var1, float var2, CrumblingOverlay var3, boolean var4, CallbackInfoReturnable<S> var5
   ) {
      if (this.cameraPos != null) {
         double var6 = var1.getBlockPos().distToCenterSqr(this.cameraPos);
         if (!Optimization.ш(var6)) {
            var5.setReturnValue(null);
         }
      }
   }
}
