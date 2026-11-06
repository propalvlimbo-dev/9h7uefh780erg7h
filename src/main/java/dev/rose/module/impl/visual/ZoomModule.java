package dev.rose.module.impl.visual;

import com.mojang.blaze3d.platform.InputConstants;
import dev.rose.anim.Spring;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.KeySetting;
import dev.rose.setting.NumberSetting;
import net.minecraft.client.Minecraft;


public final class ZoomModule extends Module {
   private final KeySetting hold = this.add(new KeySetting("hold", 67));
   private final NumberSetting power = this.add(new NumberSetting("power", 4.0, 1.5, 12.0, 0.5, "rose.unit.times"));
   private final BoolSetting wheel = this.add(new BoolSetting("wheel", true));
   private final BoolSetting smooth = this.add(new BoolSetting("smooth", true));
   private final BoolSetting slowMouse = this.add(new BoolSetting("slow_mouse", true));
   private final Spring amount = new Spring(0.0, 0.26, 0.92);
   private float wheelPower;
   private boolean held;

   public ZoomModule() {
      super("zoom", Category.VISUALS, true);
   }

   @Override
   public int icon() {
      return 57782;
   }

   @Override
   public boolean hasKeybind() {
      return false;
   }

   private boolean keyDown() {
      Minecraft mc = Minecraft.getInstance();
      int key = this.hold.get();
      return this.isEnabled() && key != -1 && mc.screen == null && mc.player != null ? InputConstants.isKeyDown(mc.getWindow(), key) : false;
   }

   public float factor() {
      boolean down = this.keyDown();
      if (down && !this.held) {
         this.wheelPower = this.power.floatValue();
      }

      this.held = down;
      float target = down ? 1.0F : 0.0F;
      float t = this.smooth.isOn() ? Math.clamp(this.amount.setTarget(target).getFloat(), 0.0F, 1.0F) : target;
      if (!this.smooth.isOn()) {
         this.amount.snap(target);
      }

      return t <= 0.001F ? 1.0F : (float)Math.pow(Math.max(1.5F, this.wheelPower), t);
   }

   public boolean scroll(double amountScrolled) {
      if (this.wheel.isOn() && this.held && amountScrolled != 0.0) {
         this.wheelPower = Math.clamp(this.wheelPower * (amountScrolled > 0.0 ? 1.18F : 0.84745765F), 1.5F, 24.0F);
         return true;
      } else {
         return false;
      }
   }

   public double sensitivity(double vanilla) {
      if (!this.slowMouse.isOn()) {
         return vanilla;
      }

      float factor = !this.held && !(this.amount.getFloat() > 0.01F) ? 1.0F : this.factorNow();
      if (factor <= 1.001F) {
         return vanilla;
      }

      double base = (vanilla * 0.6 + 0.2) / Math.cbrt(factor);
      return (base - 0.2) / 0.6;
   }

   private float factorNow() {
      float t = Math.clamp(this.amount.getFloat(), 0.0F, 1.0F);
      return t <= 0.001F ? 1.0F : (float)Math.pow(Math.max(1.5F, this.wheelPower), t);
   }
}
