package dev.rose.module.impl.visual;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ColorSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;


public final class NametagsModule extends Module {
   private final NumberSetting scale = this.add(new NumberSetting("scale", 100.0, 50.0, 150.0, 5.0, "rose.unit.percent"));
   private final ColorSetting background = this.add(new ColorSetting("background", 1427116052, true));
   private final BoolSetting shadow = this.add(new BoolSetting("shadow", true));
   private final BoolSetting self = this.add(new BoolSetting("self", true));

   public NametagsModule() {
      super("nametags", Category.VISUALS, false);
   }

   @Override
   public int icon() {
      return 57727;
   }

   public float scale() {
      return this.isEnabled() ? this.scale.floatValue() / 100.0F : 1.0F;
   }

   public int background(int vanilla) {
      return this.isEnabled() ? this.background.color() : vanilla;
   }

   public boolean shadow(boolean vanilla) {
      return this.isEnabled() ? this.shadow.isOn() : vanilla;
   }

   public boolean showSelf() {
      return this.isEnabled() && this.self.isOn();
   }

   static int opaque(int color) {
      return ColorUtil.withAlpha(color, 255);
   }
}
