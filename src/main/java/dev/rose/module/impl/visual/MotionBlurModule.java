package dev.rose.module.impl.visual;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.NumberSetting;
import net.minecraft.client.Minecraft;


public final class MotionBlurModule extends Module {
   private final NumberSetting strength = this.add(new NumberSetting("strength", 45.0, 5.0, 85.0, 5.0, "rose.unit.percent"));

   public MotionBlurModule() {
      super("motion_blur", Category.VISUALS, false);
   }

   @Override
   public int icon() {
      return 57776;
   }

   public float strength() {
      float fps = Math.max(10, Minecraft.getInstance().getFps());
      return (float)Math.min(0.95, Math.pow(this.strength.floatValue() / 100.0F, 60.0 / fps));
   }
}
