package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.setting.BoolSetting;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public final class ClockModule extends InfoElement {
   private final BoolSetting seconds = this.add(new BoolSetting("seconds", false));
   private final BoolSetting twelveHours = this.add(new BoolSetting("twelve_hours", false));

   public ClockModule() {
      super("clock", Anchor.BOTTOM_RIGHT, -6.0F, -112.0F, false);
   }

   @Override
   public int icon() {
      return 57479;
   }

   @Override
   protected int valueIcon() {
      return 57479;
   }

   @Override
   protected String value(HudContext hud) {
      String pattern = (this.twelveHours.isOn() ? "h:mm" : "HH:mm") + (this.seconds.isOn() ? ":ss" : "") + (this.twelveHours.isOn() ? " a" : "");
      return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));
   }
}
