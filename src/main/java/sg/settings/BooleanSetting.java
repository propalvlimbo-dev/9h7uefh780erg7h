package sg.settings;

import com.google.gson.annotations.SerializedName;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import sg.settings.KeybindMode;
import sg.settings.Keybind;
import sg.settings.ValueSetting;

public class BooleanSetting extends ValueSetting<Boolean> {
   @SerializedName("Х")
   private final Keybind keybind = new Keybind(KeybindMode.TOGGLE, -1);

   @Generated
   public Keybind getKeybind() {
      return this.keybind;
   }

   public BooleanSetting(String name, boolean defaultValue) {
      super(name, defaultValue);
   }

   public BooleanSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }
}
