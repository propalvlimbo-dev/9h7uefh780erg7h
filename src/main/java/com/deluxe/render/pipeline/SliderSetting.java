package com.deluxe.render.pipeline;

import com.deluxe.util.time.Setting;

public class SliderSetting extends Setting<SliderSetting> {
   private final sg.settings.FloatSetting backing;

   public double doubleValue() {
      return this.getValue();
   }

   public float getStep() {
      return this.backing.getStep();
   }

   public float getNormalizedValue() {
      float minimum = this.getMinimum();
      float maximum = this.getMaximum();
      return maximum <= minimum ? 0.0F : (this.getValue() - minimum) / (maximum - minimum);
   }

   public void setNormalizedValue(float normalizedValue) {
      float clampedProgress = Math.max(0.0F, Math.min(1.0F, normalizedValue));
      this.setValue(this.getMinimum() + (this.getMaximum() - this.getMinimum()) * clampedProgress);
   }

   private int getDecimalPlaces() {
      float step = this.getStep();
      return step > 0.0F && step < 1.0F ? Math.max(1, (int)Math.ceil(-Math.log10(step))) : 0;
   }

   public int intValue() {
      return Math.round(this.getValue());
   }

   public String getSuffix() {
      return "";
   }

   public float getMinimum() {
      return this.backing.getMinimum();
   }

   public float getMaximum() {
      return this.backing.getMaximum();
   }

   public SliderSetting(sg.settings.FloatSetting backing) {
      super(backing.getName(), "");
      this.backing = backing;
      this.setVisibility(() -> backing.getVisibilityCondition().getAsBoolean());
   }

   public boolean isTextEntryEnabled() {
      return false;
   }

   public float floatValue() {
      return this.getValue();
   }

   public String getFormattedValue() {
      int decimalPlaces = this.getDecimalPlaces();
      return decimalPlaces <= 0 ? Integer.toString(Math.round(this.getValue())) : String.format("%." + decimalPlaces + "f", this.getValue());
   }

   public SliderSetting setValue(float value) {
      float clampedValue = Math.max(this.getMinimum(), Math.min(this.getMaximum(), value));
      if (clampedValue != this.getValue()) {
         this.backing.setClampedValue(clampedValue);
         this.notifyChanged();
      }

      return this;
   }

   public float getValue() {
      return this.backing.getValue();
   }
}
