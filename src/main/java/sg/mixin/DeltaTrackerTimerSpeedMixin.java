package sg.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.DeltaTracker.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.ec.ClientTimerSpeed;

@Mixin(Timer.class)
public class DeltaTrackerTimerSpeedMixin {
   @ModifyExpressionValue(method = "advanceGameTime(J)I", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/floats/FloatUnaryOperator;apply(F)F"))
   private float tickCounter(float var1) {
      return var1 / ClientTimerSpeed.дю();
   }
}
