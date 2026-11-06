package sg.settings;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class MultiChoiceSetting extends ValueSetting<List<ChoiceOption>> {
   private final @com.google.gson.annotations.SerializedName("н")  Map<String, Boolean> defaultSelections = new LinkedHashMap<>();
   private final @com.google.gson.annotations.SerializedName("Ц")  Map<String, ChoiceOption> optionsByName = new HashMap<>();
   private @com.google.gson.annotations.SerializedName("с诶")  boolean hasDuplicateNames;

   public MultiChoiceSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }

   public boolean isSelected(String optionName) {
      List options = this.getValue();
      if (options == this.getDefaultValue() && !this.hasDuplicateNames) {
         ChoiceOption cachedOption = this.optionsByName.get(optionName);
         return cachedOption != null && cachedOption.isSelected();
      }

      for (int optionIndex = 0; optionIndex < options.size(); optionIndex++) {
         ChoiceOption option = (ChoiceOption)options.get(optionIndex);
         if (option.getName().equals(optionName) && option.isSelected()) {
            return true;
         }
      }

      return false;
   }

   public MultiChoiceSetting(String name, ChoiceOption... options) {
      super(name, List.of(options));

      for (ChoiceOption option : options) {
         this.defaultSelections.put(option.getName(), option.isSelected());
         this.hasDuplicateNames = this.hasDuplicateNames | this.optionsByName.putIfAbsent(option.getName(), option) != null;
      }
   }

   public void resetToDefaults() {
      for (ChoiceOption option : this.getValue()) {
         option.setSelected(this.defaultSelections.getOrDefault(option.getName(), false));
         option.getKeybind().setKeyCode(-1);
      }
   }
}
