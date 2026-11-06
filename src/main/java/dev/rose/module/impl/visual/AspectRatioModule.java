package dev.rose.module.impl.visual;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;


public final class AspectRatioModule extends Module {
   private final ChoiceSetting<AspectRatioModule.Ratio> ratio = this.add(new ChoiceSetting<>("ratio", AspectRatioModule.Ratio.R4_3));
   private final NumberSetting custom = this.add(new NumberSetting("custom", 1.33, 0.5, 3.5, 0.01, ""));

   public AspectRatioModule() {
      super("aspect_ratio", Category.VISUALS, false);
      this.custom.visibleWhen(() -> this.ratio.get() == AspectRatioModule.Ratio.CUSTOM);
   }

   @Override
   public int icon() {
      return 58831;
   }

   public float aspect(float real) {
      if (!this.isEnabled()) {
         return real;
      } else {
         return this.ratio.get() == AspectRatioModule.Ratio.CUSTOM ? this.custom.floatValue() : this.ratio.get().value;
      }
   }

   public enum Ratio {
      R4_3(1.3333334F),
      R5_4(1.25F),
      R16_10(1.6F),
      R16_9(1.7777778F),
      R21_9(2.3333333F),
      CUSTOM(0.0F);

      private final float value;

      Ratio(float value) {
         this.value = value;
      }
   }
}
