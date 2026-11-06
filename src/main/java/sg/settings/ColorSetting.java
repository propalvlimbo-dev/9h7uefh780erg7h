package sg.settings;

import com.google.gson.annotations.SerializedName;
import java.util.function.BooleanSupplier;
import lombok.Generated;

public class ColorSetting extends ValueSetting<Integer> {
   @SerializedName("4д")
   private final int defaultColor;
   private final @com.google.gson.annotations.SerializedName("ХЩ")  boolean alphaEnabled;

   @Generated
   public boolean isAlphaEnabled() {
      return this.alphaEnabled;
   }

   public ColorSetting(String name, boolean alphaEnabled, int defaultColor) {
      super(name, defaultColor);
      this.defaultColor = defaultColor;
      this.alphaEnabled = alphaEnabled;
   }

   public ColorSetting(String name, int defaultColor) {
      this(name, true, defaultColor);
   }

   public ColorSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }

   @Generated
   public int getDefaultColor() {
      return this.defaultColor;
   }

   public float getAlphaChannel() {
      return this.getValue() >>> 24 & 0xFF;
   }
}
