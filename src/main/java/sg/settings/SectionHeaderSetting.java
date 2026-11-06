package sg.settings;

import java.util.function.BooleanSupplier;

public class SectionHeaderSetting extends ValueSetting<String> {
   public SectionHeaderSetting(String name) {
      super(name, "");
   }

   public SectionHeaderSetting(String name, BooleanSupplier visibilityCondition) {
      this(name);
      this.visibleWhen(visibilityCondition);
   }

   public SectionHeaderSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }
}
