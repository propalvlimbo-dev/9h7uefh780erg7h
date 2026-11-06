package com.deluxe.modules.impl.render;

public final class TextSetting extends com.deluxe.util.time.TextSetting {
   private final sg.settings.TextSetting backing;

   public TextSetting(sg.settings.TextSetting backing) {
      super(backing.getName(), "", backing.getValue(), 256);
      this.backing = backing;
      this.setVisibility(() -> backing.getVisibilityCondition().getAsBoolean());
   }

   @Override
   protected void notifyChanged() {
      if (this.backing != null) {
         this.backing.setValue(this.getValue());
      }

      super.notifyChanged();
   }
}
