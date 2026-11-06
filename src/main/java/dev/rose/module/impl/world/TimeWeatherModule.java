package dev.rose.module.impl.world;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;


public final class TimeWeatherModule extends Module {
   private final BoolSetting fixTime = this.add(new BoolSetting("fix_time", true));
   private final NumberSetting hour = this.add(new NumberSetting("hour", 12.0, 0.0, 23.5, 0.5, "rose.unit.hours"));
   private final ChoiceSetting<TimeWeatherModule.Weather> weather = this.add(new ChoiceSetting<>("weather", TimeWeatherModule.Weather.CLEAR));

   public TimeWeatherModule() {
      super("time_weather", Category.WORLD, false);
      this.hour.visibleWhen(this.fixTime::isOn);
   }

   @Override
   public int icon() {
      return 57630;
   }

   public long dayTime(long vanilla) {
      if (this.isEnabled() && this.fixTime.isOn()) {
         long ticks = Math.floorMod(Math.round((this.hour.get() - 6.0) * 1000.0), 24000);
         return Math.floorDiv(vanilla, 24000L) * 24000L + ticks;
      } else {
         return vanilla;
      }
   }

   public float rainLevel(float vanilla) {
      if (!this.isEnabled()) {
         return vanilla;
      }

      return switch ((TimeWeatherModule.Weather)this.weather.get()) {
         case SERVER -> vanilla;
         case CLEAR -> 0.0F;
         case RAIN, THUNDER -> 1.0F;
      };
   }

   public float thunderLevel(float vanilla) {
      if (!this.isEnabled()) {
         return vanilla;
      }

      return switch ((TimeWeatherModule.Weather)this.weather.get()) {
         case SERVER -> vanilla;
         case CLEAR, RAIN -> 0.0F;
         case THUNDER -> 1.0F;
      };
   }

   public enum Weather {
      SERVER,
      CLEAR,
      RAIN,
      THUNDER;
   }
}
