package sg.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import lombok.Generated;

public class SingleChoiceSetting extends ValueSetting<List<ChoiceOption>> {
   private @com.google.gson.annotations.SerializedName("е")  ChoiceOption selectedOption = null;
   private @com.google.gson.annotations.SerializedName("Я_")  String pendingSelectionName;
   private final @com.google.gson.annotations.SerializedName("ЯШ")  String defaultSelectionName;

   public void setOptions(List<ChoiceOption> options) {
      String selectionName = this.pendingSelectionName != null ? this.pendingSelectionName : (this.selectedOption != null ? this.selectedOption.getName() : null);
      this.setValue(new ArrayList<>(options));
      if (selectionName != null) {
         this.selectByName(selectionName);
      } else if (!this.getValue().isEmpty()) {
         this.selectedOption = this.getValue().getFirst();
      } else {
         this.selectedOption = null;
      }
   }

   @Generated
   public ChoiceOption getSelectedOption() {
      return this.selectedOption;
   }

   @Generated
   public void setPendingSelectionName(String optionName) {
      this.pendingSelectionName = optionName;
   }

   public void resetToDefault() {
      this.pendingSelectionName = null;
      this.selectedOption = null;

      for (ChoiceOption option : this.getValue()) {
         option.getKeybind().setKeyCode(-1);
         if (this.defaultSelectionName != null && option.getName().equals(this.defaultSelectionName)) {
            this.selectedOption = option;
         }
      }

      if (this.selectedOption == null && !this.getValue().isEmpty()) {
         this.selectedOption = this.getValue().getFirst();
      }
   }

   public SingleChoiceSetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }

   @Generated
   public void setSelectedOption(ChoiceOption option) {
      this.selectedOption = option;
   }

   public void selectByName(String optionName) {
      if (optionName != null && !optionName.isEmpty()) {
         for (ChoiceOption option : this.getValue()) {
            if (option.getName().equals(optionName)) {
               this.selectedOption = option;
               this.pendingSelectionName = null;
               return;
            }
         }

         this.pendingSelectionName = optionName;
      }
   }

   @Generated
   public String getPendingSelectionName() {
      return this.pendingSelectionName;
   }

   public SingleChoiceSetting(String name, int defaultIndex, ChoiceOption... options) {
      super(name, new ArrayList<>(List.of(options)));
      if (!this.getValue().isEmpty()) {
         this.selectedOption = this.getValue().get(Math.min(defaultIndex, this.getValue().size() - 1));
      }

      this.defaultSelectionName = this.selectedOption == null ? null : this.selectedOption.getName();
   }

   @Generated
   public String getDefaultSelectionName() {
      return this.defaultSelectionName;
   }

   public boolean isSelected(String optionName) {
      return this.selectedOption != null && this.selectedOption.getName().equals(optionName);
   }

   public SingleChoiceSetting(String name, ChoiceOption... options) {
      this(name, 0, options);
   }
}
