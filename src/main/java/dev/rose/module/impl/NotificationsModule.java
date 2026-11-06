package dev.rose.module.impl;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;


public final class NotificationsModule extends Module {
   private final BoolSetting toggles = this.add(new BoolSetting("toggles", true));
   private final NumberSetting duration = this.add(new NumberSetting("duration", 2.2, 0.8, 8.0, 0.1, "rose.unit.seconds"));
   private final BoolSetting updates = this.add(new BoolSetting("updates", true));

   public NotificationsModule() {
      super("notifications", Category.INTERFACE, true);
   }

   @Override
   public int icon() {
      return 57433;
   }

   public boolean showToggles() {
      return this.toggles.isOn();
   }

   public boolean checkUpdates() {
      return this.updates.isOn();
   }

   public long durationMs() {
      return Math.round(this.duration.get() * 1000.0);
   }
}
