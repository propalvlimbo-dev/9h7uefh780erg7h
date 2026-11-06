package dev.rose.module.impl.hud;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.NumberSetting;


public final class TitlesModule extends Module {
   private final NumberSetting pop = this.add(new NumberSetting("pop", 25.0, 0.0, 60.0, 5.0, "rose.unit.percent"));

   public TitlesModule() {
      super("titles", Category.HUD, false);
   }

   @Override
   public int icon() {
      return 57752;
   }

   public float scale(float time, int fadeIn, int stay, int fadeOut) {
      float extra = this.pop.floatValue() / 100.0F;
      float shown = fadeIn + stay + fadeOut - time;
      if (fadeIn > 0 && shown < fadeIn) {
         float t = Math.clamp(shown / fadeIn, 0.0F, 1.0F);
         float eased = 1.0F - (float)Math.pow(1.0F - t, 3.0);
         return 1.0F + extra * (1.0F - eased);
      } else if (fadeOut > 0 && time < fadeOut) {
         float t = Math.clamp(time / fadeOut, 0.0F, 1.0F);
         return 1.0F - extra * 0.35F * (1.0F - t);
      } else {
         return 1.0F;
      }
   }
}
