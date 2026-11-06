package sg.settings;

import java.util.function.BooleanSupplier;
import lombok.Generated;

public class TextSetting extends ValueSetting<String> {
   private @com.google.gson.annotations.SerializedName("В")  TextInputFormat inputFormat;

   @Generated
   public TextInputFormat getInputFormat() {
      return this.inputFormat;
   }

   public TextSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }

   public TextSetting(String name, String defaultValue) {
      super(name, defaultValue);
   }

   public TextSetting withInputFormat(TextInputFormat inputFormat) {
      this.inputFormat = inputFormat;
      return this;
   }
}
