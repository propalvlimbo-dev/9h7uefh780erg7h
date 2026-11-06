package sg.settings;

import com.google.gson.annotations.SerializedName;
import java.util.function.BooleanSupplier;
import lombok.Generated;

public abstract class ValueSetting<T> {
   private final @com.google.gson.annotations.SerializedName("сI")  String name;
   private @com.google.gson.annotations.SerializedName("А")  T value;
   @SerializedName("4")
   private final T defaultValue;
   private @com.google.gson.annotations.SerializedName("Х")  BooleanSupplier visibilityCondition = () -> true;

   @Generated
   public void setValue(T value) {
      this.value = (T)value;
   }

   @Generated
   public void setVisibilityCondition(BooleanSupplier visibilityCondition) {
      this.visibilityCondition = visibilityCondition;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   public ValueSetting(String name, T defaultValue) {
      this.name = name;
      this.value = (T)defaultValue;
      this.defaultValue = (T)defaultValue;
   }

   @Generated
   public T getDefaultValue() {
      return this.defaultValue;
   }

   @Generated
   public BooleanSupplier getVisibilityCondition() {
      return this.visibilityCondition;
   }

   @Generated
   public T getValue() {
      return this.value;
   }

   public abstract ValueSetting<T> visibleWhen(BooleanSupplier visibilityCondition);
}
