package sg.settings;

import java.util.function.BooleanSupplier;

public class KeybindSetting extends ValueSetting<Integer> {
   public KeybindSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }

   public KeybindSetting(String name, int keyCode) {
      super(name, keyCode);
   }

   public KeybindSetting(String name, BooleanSupplier visibilityCondition) {
      super(name, -1);
      this.visibleWhen(visibilityCondition);
   }

   public KeybindSetting(String name, int keyCode, BooleanSupplier visibilityCondition) {
      super(name, keyCode);
      this.visibleWhen(visibilityCondition);
   }

   public KeybindSetting(String name) {
      super(name, -1);
   }
}
