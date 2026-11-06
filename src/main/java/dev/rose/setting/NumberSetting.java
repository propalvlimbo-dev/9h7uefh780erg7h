package dev.rose.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import net.minecraft.locale.Language;


public class NumberSetting extends Setting<Double> {
   private final double min;
   private final double max;
   private final double step;
   private final String unitKey;

   public NumberSetting(String id, double defaultValue, double min, double max, double step, String unitKey) {
      super(id, defaultValue);
      this.min = min;
      this.max = max;
      this.step = step;
      this.unitKey = unitKey;
   }

   public double min() {
      return this.min;
   }

   public double max() {
      return this.max;
   }

   public double step() {
      return this.step;
   }

   public float floatValue() {
      return this.get().floatValue();
   }

   public int intValue() {
      return (int)Math.round(this.get());
   }

   public double normalized() {
      return this.max > this.min ? (this.get() - this.min) / (this.max - this.min) : 0.0;
   }

   public void setNormalized(double t) {
      this.set(this.min + (this.max - this.min) * t);
   }

   public String format() {
      int decimals = this.decimals();
      String number = decimals == 0 ? Long.toString(Math.round(this.get())) : String.format(Locale.ROOT, "%." + decimals + "f", this.get());
      return this.unitKey.isEmpty() ? number : number + Language.getInstance().getOrDefault(this.unitKey);
   }

   protected Double sanitize(Double newValue) {
      if (newValue != null && !newValue.isNaN()) {
         double value = Math.clamp(newValue, this.min, this.max);
         if (this.step > 0.0) {
            value = this.min + Math.round((value - this.min) / this.step) * this.step;
            value = BigDecimal.valueOf(value).setScale(this.decimals(), RoundingMode.HALF_UP).doubleValue();
            value = Math.clamp(value, this.min, this.max);
         }

         return value;
      } else {
         return this.defaultValue();
      }
   }

   private int decimals() {
      return this.step <= 0.0 ? 2 : Math.max(0, BigDecimal.valueOf(this.step).stripTrailingZeros().scale());
   }

   @Override
   public JsonElement toJson() {
      return new JsonPrimitive(this.get());
   }

   @Override
   public void fromJson(JsonElement json) {
      if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
         this.set(json.getAsDouble());
      }
   }
}
