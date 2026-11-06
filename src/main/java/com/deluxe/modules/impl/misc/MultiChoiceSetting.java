package com.deluxe.modules.impl.misc;

import com.deluxe.util.time.Setting;
import java.util.ArrayList;
import java.util.List;

public class MultiChoiceSetting extends Setting<MultiChoiceSetting> {
   private final sg.settings.MultiChoiceSetting backing;
   public boolean expanded;

   public boolean isSelected(String option) {
      return this.backing.isSelected(option);
   }

   public MultiChoiceSetting(sg.settings.MultiChoiceSetting backing) {
      super(backing.getName(), "");
      this.backing = backing;
      this.setVisibility(() -> backing.getVisibilityCondition().getAsBoolean());
   }

   public int getSelectedCount() {
      int selectedCount = 0;

      for (sg.settings.ChoiceOption option : this.backing.getValue()) {
         if (option.isSelected()) {
            selectedCount++;
         }
      }

      return selectedCount;
   }

   public List<String> getSelectedOptions() {
      ArrayList<String> selectedOptions = new ArrayList();

      for (sg.settings.ChoiceOption option : this.backing.getValue()) {
         if (option.isSelected()) {
            selectedOptions.add(option.getName());
         }
      }

      return selectedOptions;
   }

   public void toggleOption(String option) {
      for (sg.settings.ChoiceOption choiceOption : this.backing.getValue()) {
         if (choiceOption.getName().equalsIgnoreCase(option)) {
            choiceOption.setSelected(!choiceOption.isSelected());
            this.notifyChanged();
            return;
         }
      }
   }

   public void toggleOptionAt(int index) {
      List<sg.settings.ChoiceOption> options = this.backing.getValue();
      if (index >= 0 && index < options.size()) {
         this.toggleOption(options.get(index).getName());
      }
   }

   public boolean isSelectedAt(int index) {
      List<sg.settings.ChoiceOption> options = this.backing.getValue();
      return index >= 0 && index < options.size() && options.get(index).isSelected();
   }

   public List<String> getOptions() {
      ArrayList<String> optionNames = new ArrayList();

      for (sg.settings.ChoiceOption option : this.backing.getValue()) {
         optionNames.add(option.getName());
      }

      return optionNames;
   }

   public String getSelectionSummary() {
      return this.getSelectedCount() + "/" + this.getOptions().size();
   }

   public boolean matchesOption(String option) {
      return this.isSelected(option);
   }
}
