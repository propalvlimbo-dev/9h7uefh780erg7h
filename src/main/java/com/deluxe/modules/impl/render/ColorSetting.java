package com.deluxe.modules.impl.render;

import com.deluxe.util.time.Setting;
import java.awt.Color;

public final class ColorSetting extends Setting<ColorSetting> {
   private final sg.settings.ColorSetting backing;

   private float[] toHsb() {
      Color color = new Color(this.getValue(), true);
      return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
   }

   public int getValue() {
      return this.backing.getValue();
   }

   public int getOpaqueRgb() {
      return (-16777216) | this.getValue() & (16777215);
   }

   public float getSaturation() {
      return this.toHsb()[1];
   }

   public ColorSetting setHsb(float hue, float saturation, float brightness) {
      int alpha = this.getAlpha();
      int rgb = Color.HSBtoRGB(hue, saturation, brightness) & (16777215);
      return this.setArgb(alpha << 24 | rgb);
   }

   public float getBrightness() {
      return this.toHsb()[2];
   }

   public ColorSetting(String name, String description, Color color) {
      super(name, description);
      this.backing = null;
   }

   public ColorSetting(sg.settings.ColorSetting backing) {
      super(backing.getName(), "");
      this.backing = backing;
      this.setVisibility(() -> backing.getVisibilityCondition().getAsBoolean());
   }

   public float getHue() {
      return this.toHsb()[0];
   }

   public ColorSetting setArgb(int argb) {
      if (argb != this.getValue() && this.backing != null) {
         this.backing.setValue(argb);
         this.notifyChanged();
      }

      return this;
   }

   public int getAlpha() {
      return this.getValue() >>> 24 & 0xFF;
   }
}
