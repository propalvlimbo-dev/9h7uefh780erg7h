package sg.mixin;

import sg.mixinbridge.RenderSuppressionQueries;

import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.client.RoseClient;
import sg.modules.impl.render.Rain;
import sg.events.impl.RenderSuppressionQuery;
import sg.modules.impl.render.Ambience;

@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectRendererSuppressionMixin {
   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void cancelVanillaPrecipitation(CallbackInfo var1) {
      if (shouldSuppressVanillaWeather()) {
         var1.cancel();
      }
   }

   private static boolean shouldSuppressVanillaWeather() {
      if (RoseClient.getInstance() == null) {
         return false;
      }

      Ambience var0 = RoseClient.getInstance().getServices().getModules().getAmbience();
      return Rain.Пз() || RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.RAIN) || var0 != null && var0.isWeatherOverrideEnabled();
   }
}
