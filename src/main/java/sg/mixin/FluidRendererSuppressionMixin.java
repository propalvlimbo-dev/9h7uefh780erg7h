package sg.mixin;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.client.renderer.block.FluidRenderer.Output;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.misc.Optimization;

@Mixin(FluidRenderer.class)
public class FluidRendererSuppressionMixin {
   @Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
   private void render(BlockAndTintGetter var1, BlockPos var2, Output var3, BlockState var4, FluidState var5, CallbackInfo var6) {
      boolean var7 = Optimization.method_7ж() && var5.is(FluidTags.WATER);
      boolean var8 = Optimization.method_7ю() && var5.is(FluidTags.LAVA);
      if (var7 || var8) {
         var6.cancel();
      }
   }
}
