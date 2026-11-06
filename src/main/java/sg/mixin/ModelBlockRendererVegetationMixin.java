package sg.mixin;

import sg.mixinbridge.RenderSuppressionQueries;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.misc.Optimization;
import sg.events.impl.RenderSuppressionQuery;

@Mixin(ModelBlockRenderer.class)
public abstract class ModelBlockRendererVegetationMixin {
   @Inject(method = "tesselateBlock", at = @At("HEAD"), cancellable = true)
   private void rose$cullVegetation(
      BlockQuadOutput var1,
      float var2,
      float var3,
      float var4,
      BlockAndTintGetter var5,
      BlockPos var6,
      BlockState var7,
      BlockStateModel var8,
      long var9,
      CallbackInfo var11
   ) {
      if (var7.getBlock() instanceof VegetationBlock && (Optimization.method_7М() || RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.GRASS))) {
         var11.cancel();
      }
   }
}
