package sg.settings;

import java.util.function.BooleanSupplier;
import lombok.Generated;
import org.joml.Vector2f;

public class RangeSetting extends ValueSetting<Vector2f> {
   private final @com.google.gson.annotations.SerializedName("И")  Vector2f bounds = new Vector2f();
   private @com.google.gson.annotations.SerializedName("тд")  float step = 0.0F;
   private @com.google.gson.annotations.SerializedName("тБ")  float minimumGap = 0.0F;
   private @com.google.gson.annotations.SerializedName("Я")  NumericDisplayFormat displayFormat;

   @Generated
   public float getMinimumGap() {
      return this.minimumGap;
   }

   public RangeSetting withDisplayFormat(NumericDisplayFormat displayFormat) {
      this.displayFormat = displayFormat;
      return this;
   }

   public RangeSetting withStep(float step) {
      this.step = step;
      return this;
   }

   public RangeSetting(String name, Vector2f defaultRange, float minimum, float maximum) {
      super(name, defaultRange);
      this.bounds.set(minimum, maximum);
   }

   @Generated
   public Vector2f getBounds() {
      return this.bounds;
   }

   public RangeSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }

   @Generated
   public NumericDisplayFormat getDisplayFormat() {
      return this.displayFormat;
   }

   public RangeSetting withMinimumGap(float minimumGap) {
      this.minimumGap = minimumGap;
      return this;
   }

   @Generated
   public float getStep() {
      return this.step;
   }
}
