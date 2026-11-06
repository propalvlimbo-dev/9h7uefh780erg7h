package sg.modules.impl.init;

import lombok.Generated;
import net.minecraft.util.Mth;
import sg.rotation.AimGeometryMath;
import sg.util.math.EasingFunctions;

public class TickAnimation {
   private @com.google.gson.annotations.SerializedName("сС")  float currentValue;
   private @com.google.gson.annotations.SerializedName("сз")  float previousValue;
   private @com.google.gson.annotations.SerializedName("сц")  float tickStep;
   private @com.google.gson.annotations.SerializedName("ся")  float minValue;
   private @com.google.gson.annotations.SerializedName("Вс")  float maxValue;
   private @com.google.gson.annotations.SerializedName("ВВ")  float interpolatedValue;
   private static final float VISIBILITY_THRESHOLD = 0.1F;

   public void tick(boolean increasing) {
      this.previousValue = this.currentValue;
      this.currentValue = Mth.clamp(this.currentValue + (increasing ? this.tickStep : -this.tickStep), this.minValue, this.maxValue);
   }

   @Generated
   public float getCurrentValue() {
      return this.currentValue;
   }

   @Generated
   public float getInterpolatedValue() {
      return this.interpolatedValue;
   }

   @Generated
   public float getPreviousValue() {
      return this.previousValue;
   }

   @Generated
   public void setInterpolatedValue(float value) {
      this.interpolatedValue = value;
   }

   public void configureAndInterpolate(float minimum, float maximum, float step, EasingFunctions.Easing easing, float partialTick) {
      this.tickStep = step;
      this.minValue = minimum;
      this.maxValue = maximum;
      this.interpolatedValue = easing.apply(AimGeometryMath.lerpFloat(this.previousValue, this.currentValue, partialTick));
   }

   public boolean isVisible() {
      return this.interpolatedValue > VISIBILITY_THRESHOLD;
   }

   @Generated
   public void setCurrentValue(float value) {
      this.currentValue = value;
   }

   @Generated
   public void setPreviousValue(float value) {
      this.previousValue = value;
   }
}
