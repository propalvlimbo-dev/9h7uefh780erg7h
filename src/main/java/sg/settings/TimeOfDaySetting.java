package sg.settings;

import java.util.List;
import java.util.function.BooleanSupplier;

public class TimeOfDaySetting extends SingleChoiceSetting {
   private static final String WORLD_TIME_OPTION = "Обычное";
   private static final String MORNING_OPTION = "Утро";
   private static final String DAY_OPTION = "День";
   private static final String EVENING_OPTION = "Вечер";
   private static final String SUNSET_OPTION = "Закат";
   private static final String NIGHT_OPTION = "Ночь";
   private static final long MORNING_TICKS = 1000L;
   private static final long DAY_TICKS = 6000L;
   private static final long EVENING_TICKS = 10000L;
   private static final long SUNSET_TICKS = 12000L;
   private static final long NIGHT_TICKS = 21000L;
   private static final long DAY_CYCLE_TICKS = 24000L;
   private static final float DAY_CYCLE_TICKS_FLOAT = 24000.0F;
   private static final String WORLD_TIME_DISPLAY_OPTION = "Обычное";
   private static final String MORNING_DISPLAY_OPTION = "Утро";
   private static final String AUTOMATIC_DISPLAY_LABEL = "Авто";
   private static final String DAWN_DISPLAY_LABEL = "Рассвет";
   private static final float INITIAL_NEAREST_DISTANCE = Float.MAX_VALUE;
   private static final String NEAREST_MORNING_OPTION = "Утро";
   private static final String NEAREST_DAY_OPTION = "День";
   private static final String NEAREST_EVENING_OPTION = "Вечер";
   private static final String NEAREST_SUNSET_OPTION = "Закат";
   private static final String NEAREST_NIGHT_OPTION = "Ночь";
   private static final long NEAREST_MORNING_TICKS = 1000L;
   private static final long NEAREST_DAY_TICKS = 6000L;
   private static final long NEAREST_EVENING_TICKS = 10000L;
   private static final long NEAREST_SUNSET_TICKS = 12000L;
   private static final long NEAREST_NIGHT_TICKS = 21000L;
   private static final long UNSUPPORTED_OPTION_TICKS = -1L;
   private static final float NEAREST_DAY_CYCLE_TICKS_FLOAT = 24000.0F;

   public boolean isWorldTimeSelected() {
      return this.isSelected(WORLD_TIME_OPTION);
   }

   public void cycleSelection(int selectionOffset) {
      List options = this.getValue();
      if (!options.isEmpty()) {
         int selectedIndex = Math.max(0, options.indexOf(this.getSelectedOption()));
         this.setSelectedOption((ChoiceOption)options.get(Math.floorMod(selectedIndex + selectionOffset, options.size())));
      }
   }

   public long resolveTimeOfDayTicks(long worldTime) {
      return switch (this.getSelectedOption().getName()) {
         case MORNING_OPTION -> MORNING_TICKS;
         case DAY_OPTION -> DAY_TICKS;
         case EVENING_OPTION -> EVENING_TICKS;
         case SUNSET_OPTION -> SUNSET_TICKS;
         case NIGHT_OPTION -> NIGHT_TICKS;
         default -> Math.floorMod(worldTime, DAY_CYCLE_TICKS);
      };
   }

   public TimeOfDaySetting(String name, ChoiceOption... options) {
      super(name, options);
   }

   public TimeOfDaySetting visibleWhen(BooleanSupplier visibilityCondition) {
      this.setVisibilityCondition(visibilityCondition);
      return this;
   }

   public String getDisplayLabel() {
      return switch (this.getSelectedOption().getName()) {
         case WORLD_TIME_DISPLAY_OPTION -> AUTOMATIC_DISPLAY_LABEL;
         case MORNING_DISPLAY_OPTION -> DAWN_DISPLAY_LABEL;
         default -> this.getSelectedOption().getName();
      };
   }

   public float getTimeOfDayFraction(long worldTime) {
      return (float)this.resolveTimeOfDayTicks(worldTime) / DAY_CYCLE_TICKS_FLOAT;
   }

   public void selectNearestTimeOfDay(float timeOfDayFraction) {
      float wrappedFraction = timeOfDayFraction - (float)Math.floor(timeOfDayFraction);
      ChoiceOption nearestOption = null;
      float nearestDistance = INITIAL_NEAREST_DISTANCE;

      for (ChoiceOption option : this.getValue()) {
         long optionTicks = switch (option.getName()) {
            case NEAREST_MORNING_OPTION -> NEAREST_MORNING_TICKS;
            case NEAREST_DAY_OPTION -> NEAREST_DAY_TICKS;
            case NEAREST_EVENING_OPTION -> NEAREST_EVENING_TICKS;
            case NEAREST_SUNSET_OPTION -> NEAREST_SUNSET_TICKS;
            case NEAREST_NIGHT_OPTION -> NEAREST_NIGHT_TICKS;
            default -> UNSUPPORTED_OPTION_TICKS;
         };
         if (optionTicks >= 0L) {
            float optionFraction = (float)optionTicks / NEAREST_DAY_CYCLE_TICKS_FLOAT;
            float circularDistance = Math.abs(optionFraction - wrappedFraction);
            circularDistance = Math.min(circularDistance, 1.0F - circularDistance);
            if (circularDistance < nearestDistance) {
               nearestDistance = circularDistance;
               nearestOption = option;
            }
         }
      }

      if (nearestOption != null) {
         this.setSelectedOption(nearestOption);
      }
   }
}
