package sg.mixin;

import sg.mixinbridge.RenderSuppressionQueries;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.modules.impl.render.KillEffects;
import sg.events.impl.RenderSuppressionQuery;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerFovMixin {
   @ModifyReturnValue(method = "getFieldOfViewModifier", at = @At("RETURN"))
   private float staticFov(float var1) {
      float var2 = RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.DYNAMIC_FOV) ? 1.0F : var1;
      return var2 * KillEffects.getFovScale();
   }
}
