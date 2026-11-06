package sg.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.LightCoordsUtil.BrightnessGetter;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.modules.impl.render.FullBright;
import sg.client.RoseClient;

@Mixin(LightCoordsUtil.class)
public class LightCoordsUtilFullBrightMixin {
   @Inject(
      method = "getLightCoords(Lnet/minecraft/util/LightCoordsUtil$BrightnessGetter;Lnet/minecraft/world/level/BlockAndLightGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I",
      at = @At("RETURN"),
      cancellable = true
   )
   private static void applyAdaptiveFullBright(
      BrightnessGetter var0, BlockAndLightGetter var1, BlockState var2, BlockPos var3, CallbackInfoReturnable<Integer> var4
   ) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         FullBright var5 = RoseClient.getInstance().getServices().getModules().getFullBright();
         if (var5 != null) {
            var4.setReturnValue(var5.е((Integer)var4.getReturnValue(), var3));
         }
      }
   }
}
