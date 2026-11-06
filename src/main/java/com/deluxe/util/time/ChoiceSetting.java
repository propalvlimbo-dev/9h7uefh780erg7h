package com.deluxe.util.time;

import java.util.ArrayList;
import java.util.List;

public class ChoiceSetting extends Setting<ChoiceSetting> {
   private final sg.settings.SingleChoiceSetting backing;
   public boolean expanded;

   public void selectNext() {
      List<String> options = this.getOptions();
      if (!options.isEmpty()) {
         this.setSelectedIndex((this.getSelectedIndex() + 1) % options.size());
      }
   }

   public ChoiceSetting withSelectedIndex(int selectedIndex) {
      this.setSelectedIndex(selectedIndex);
      return this;
   }

   public String getSelectedOption() {
      sg.settings.ChoiceOption selectedOption = this.backing.getSelectedOption();
      return selectedOption == null ? "" : selectedOption.getName();
   }

   public void setSelectedIndex(int selectedIndex) {
      List<String> options = this.getOptions();
      if (selectedIndex >= 0 && selectedIndex < options.size()) {
         this.selectOption(options.get(selectedIndex));
      }
   }

   public ChoiceSetting withSelectedOption(String selectedOption) {
      this.selectOption(selectedOption);
      return this;
   }

   public List<String> getOptions() {
      ArrayList<String> optionNames = new ArrayList();

      for (sg.settings.ChoiceOption option : this.backing.getValue()) {
         optionNames.add(option.getName());
      }

      return optionNames;
   }

   public ChoiceSetting(String name, String description) {
      super(name, description);
      this.backing = null;
   }

   public ChoiceSetting(sg.settings.SingleChoiceSetting backing) {
      super(backing.getName(), "");
      this.backing = backing;
      this.setVisibility(() -> backing.getVisibilityCondition().getAsBoolean());
   }

   public boolean isSelected(String option) {
      return this.getSelectedOption().equalsIgnoreCase(option);
   }

   public void selectOption(String option) {
      if (option == null) {
         return;
      }

      String previousSelection = this.getSelectedOption();
      this.backing.selectByName(option);
      if (!previousSelection.equals(this.getSelectedOption())) {
         this.notifyChanged();
      }
   }

   public int getSelectedIndex() {
      List<String> options = this.getOptions();
      String selectedOption = this.getSelectedOption();

      for (int optionIndex = 0; optionIndex < options.size(); optionIndex++) {
         if (options.get(optionIndex).equalsIgnoreCase(selectedOption)) {
            return optionIndex;
         }
      }

      return 0;
   }

   public boolean matchesOption(String option) {
      return this.isSelected(option);
   }

   public String getValue() {
      return this.getSelectedOption();
   }

   public ChoiceSetting withOptions(String... options) {
      return this;
   }
}
