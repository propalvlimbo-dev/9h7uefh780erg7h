package sg.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.state.GameRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.modules.impl.misc.Optimization;

@Mixin(GameRenderState.class)
public abstract class GameRenderStateTransparencyMixin {
   @ModifyReturnValue(method = "useShaderTransparency", at = @At("RETURN"), require = 0)
   private boolean forceFastTransparency(boolean var1) {
      return Optimization.г(var1);
   }
}
