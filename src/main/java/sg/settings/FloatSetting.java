package sg.settings;

import com.google.gson.annotations.SerializedName;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import lombok.Generated;
import org.joml.Vector2f;
import sg.rotation.AimGeometryMath;

public class FloatSetting extends ValueSetting<Float> {
   private final @com.google.gson.annotations.SerializedName("а")  Vector2f bounds = new Vector2f();
   private @com.google.gson.annotations.SerializedName("еь")  float step = 0.0F;
   private @com.google.gson.annotations.SerializedName("В")  NumericDisplayFormat displayFormat;
   @SerializedName("Х")
   private Supplier<Float> minimumSupplier;
   private @com.google.gson.annotations.SerializedName("е")  Supplier<Float> maximumSupplier;

   public float getMaximum() {
      return this.maximumSupplier == null ? this.bounds.y : this.maximumSupplier.get();
   }

   public FloatSetting withMaximum(Supplier<Float> maximumSupplier) {
      this.maximumSupplier = maximumSupplier;
      this.setValue(this.getValue());
      return this;
   }

   public FloatSetting withDisplayFormat(NumericDisplayFormat displayFormat) {
      this.displayFormat = displayFormat;
      return this;
   }

   @Generated
   public Vector2f getBounds() {
      return this.bounds;
   }

   @Generated
   public float getStep() {
      return this.step;
   }

   public void setClampedValue(float value) {
      super.setValue(Math.clamp(value, this.getMinimum(), this.getMaximum()));
   }

   @Generated
   public NumericDisplayFormat getDisplayFormat() {
      return this.displayFormat;
   }

   public FloatSetting withMinimum(FloatSetting minimumSetting) {
      return this.withMinimum(minimumSetting::getValue);
   }

   public FloatSetting withMinimum(Supplier<Float> minimumSupplier) {
      this.minimumSupplier = minimumSupplier;
      this.setValue(this.getValue());
      return this;
   }

   public FloatSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }

   public float getMinimum() {
      return this.minimumSupplier == null ? this.bounds.x : this.minimumSupplier.get();
   }

   public FloatSetting(String name, float defaultValue, float minimum, float maximum) {
      super(name, defaultValue);
      this.bounds.set(minimum, maximum);
   }

   @Generated
   public Supplier<Float> getMinimumSupplier() {
      return this.minimumSupplier;
   }

   public FloatSetting withMaximum(FloatSetting maximumSetting) {
      this.maximumSupplier = maximumSetting::getValue;
      maximumSetting.withMinimum(this);
      this.setValue(this.getValue());
      return this;
   }

   public void setValue(Float value) {
      float minimum = this.getMinimum();
      float maximum = this.getMaximum();
      float snappedValue = this.step > 0.0F ? (float)AimGeometryMath.roundToStep(value.floatValue(), this.step) : value;
      super.setValue(Math.clamp(snappedValue, minimum, maximum));
   }

   @Generated
   public Supplier<Float> getMaximumSupplier() {
      return this.maximumSupplier;
   }

   public FloatSetting withStep(float step) {
      this.step = step;
      return this;
   }

   public void setRawValue(float value) {
      super.setValue(value);
   }
}
