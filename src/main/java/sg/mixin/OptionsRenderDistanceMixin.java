package sg.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.modules.impl.misc.Optimization;

@Mixin(Options.class)
public abstract class OptionsRenderDistanceMixin {
   @ModifyReturnValue(method = "getEffectiveRenderDistance", at = @At("RETURN"))
   private int rose$capRenderDistance(int var1) {
      return Optimization.Ж(var1);
   }
}
