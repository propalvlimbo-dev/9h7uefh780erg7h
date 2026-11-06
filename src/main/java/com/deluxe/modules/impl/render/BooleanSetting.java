package com.deluxe.modules.impl.render;

import com.deluxe.util.time.Setting;

public final class BooleanSetting extends Setting<BooleanSetting> {
   private final sg.settings.BooleanSetting backing;

   public BooleanSetting setValue(boolean value) {
      if (value != this.getValue()) {
         this.backing.setValue(value);
         this.notifyChanged();
      }

      return this;
   }

   public boolean isEnabled() {
      return this.getValue();
   }

   public boolean getValue() {
      return this.backing.getValue();
   }

   public BooleanSetting toggle() {
      this.setValue(!this.getValue());
      return this;
   }

   public BooleanSetting(sg.settings.BooleanSetting backing) {
      super(backing.getName(), "");
      this.backing = backing;
      this.setVisibility(() -> backing.getVisibilityCondition().getAsBoolean());
   }
}
