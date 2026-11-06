package sg.animation;

import lombok.Generated;

public final class Easings {
   public static final double BACK_OVERSHOOT = 1.70158;
   public static final double BACK_IN_OUT_OVERSHOOT = 2.5949095;
   public static final double BACK_PLUS_ONE = 2.70158;
   public static final double ELASTIC_ANGULAR_FACTOR = Math.PI * 2.0 / 3.0;
   public static final double ELASTIC_IN_OUT_ANGULAR_FACTOR = Math.PI * 4.0 / 9.0;
   public static final EasingFunction LINEAR = progress -> progress;
   public static final EasingFunction QUAD_IN = powerIn(2);
   public static final EasingFunction QUAD_OUT = powerOut(2);
   public static final EasingFunction QUAD_IN_OUT = powerInOut(Easings.QUAD_IN_OUT_EXPONENT);
   public static final EasingFunction CUBIC_IN = powerIn(3);
   public static final EasingFunction CUBIC_OUT = powerOut(3);
   public static final EasingFunction CUBIC_IN_OUT = powerInOut(Easings.CUBIC_IN_OUT_EXPONENT);
   public static final EasingFunction QUART_IN = powerIn(4);
   public static final EasingFunction QUART_OUT = powerOut(4);
   public static final EasingFunction QUART_IN_OUT = powerInOut(Easings.QUART_IN_OUT_EXPONENT);
   public static final EasingFunction QUINT_IN = powerIn(5);
   public static final EasingFunction QUINT_OUT = powerOut(5);
   public static final EasingFunction QUINT_IN_OUT = powerInOut(Easings.QUINT_IN_OUT_EXPONENT);
   public static final EasingFunction SINE_IN = progress -> 1.0 - Math.cos(progress * Easings.SINE_IN_PI_FACTOR / Easings.SINE_IN_ANGLE_DIVISOR);
   public static final EasingFunction SINE_OUT = progress -> Math.sin(progress * Easings.SINE_OUT_PI_FACTOR / Easings.SINE_OUT_ANGLE_DIVISOR);
   public static final EasingFunction SINE_IN_OUT = progress -> -(Math.cos(Easings.SINE_IN_OUT_PI_FACTOR * progress) - 1.0) / Easings.SINE_IN_OUT_DIVISOR;
   public static final EasingFunction CIRC_IN = progress -> 1.0 - Math.sqrt(1.0 - Math.pow(progress, Easings.CIRC_IN_QUADRATIC_EXPONENT));
   public static final EasingFunction CIRC_OUT = progress -> Math.sqrt(1.0 - Math.pow(progress - 1.0, Easings.CIRC_OUT_QUADRATIC_EXPONENT));
   public static final EasingFunction CIRC_IN_OUT = progress -> progress < Easings.CIRC_IN_OUT_SPLIT_THRESHOLD
      ? (1.0 - Math.sqrt(1.0 - Math.pow(Easings.CIRC_IN_OUT_FIRST_HALF_PROGRESS_COEFFICIENT * progress, Easings.CIRC_IN_OUT_FIRST_HALF_QUADRATIC_EXPONENT))) / Easings.CIRC_IN_OUT_FIRST_HALF_DIVISOR
      : (Math.sqrt(1.0 - Math.pow(Easings.CIRC_IN_OUT_SECOND_HALF_PROGRESS_COEFFICIENT * progress + Easings.CIRC_IN_OUT_SECOND_HALF_PROGRESS_OFFSET, Easings.CIRC_IN_OUT_SECOND_HALF_QUADRATIC_EXPONENT)) + 1.0) / Easings.CIRC_IN_OUT_SECOND_HALF_DIVISOR;
   public static final EasingFunction ELASTIC_IN = progress -> progress != 0.0 && progress != 1.0
      ? Math.pow(Easings.ELASTIC_IN_POWER_BASE, Easings.ELASTIC_IN_EXPONENT_COEFFICIENT * progress - Easings.ELASTIC_IN_EXPONENT_OFFSET)
         * Math.sin((progress * Easings.ELASTIC_IN_PHASE_COEFFICIENT - Easings.ELASTIC_IN_PHASE_OFFSET) * Easings.ELASTIC_IN_ANGULAR_FACTOR)
      : progress;
   public static final EasingFunction ELASTIC_OUT = progress -> progress != 0.0 && progress != 1.0
      ? Math.pow(Easings.ELASTIC_OUT_POWER_BASE, Easings.ELASTIC_OUT_EXPONENT_COEFFICIENT * progress)
            * Math.sin((progress * Easings.ELASTIC_OUT_PHASE_COEFFICIENT - Easings.ELASTIC_OUT_PHASE_OFFSET) * Easings.ELASTIC_OUT_ANGULAR_FACTOR)
         + 1.0
      : progress;
   public static final EasingFunction ELASTIC_IN_OUT = progress -> {
      if (progress != 0.0 && progress != 1.0) {
         return progress < Easings.ELASTIC_IN_OUT_SPLIT_THRESHOLD
            ? -(
                  Math.pow(Easings.ELASTIC_IN_OUT_FIRST_HALF_POWER_BASE, Easings.ELASTIC_IN_OUT_FIRST_HALF_EXPONENT_COEFFICIENT * progress - Easings.ELASTIC_IN_OUT_FIRST_HALF_EXPONENT_OFFSET)
                     * Math.sin((Easings.ELASTIC_IN_OUT_FIRST_HALF_PHASE_COEFFICIENT * progress - Easings.ELASTIC_IN_OUT_FIRST_HALF_PHASE_OFFSET) * Easings.ELASTIC_IN_OUT_FIRST_HALF_ANGULAR_FACTOR)
               )
               / Easings.ELASTIC_IN_OUT_FIRST_HALF_DIVISOR
            : Math.pow(Easings.ELASTIC_IN_OUT_SECOND_HALF_POWER_BASE, Easings.ELASTIC_IN_OUT_SECOND_HALF_EXPONENT_COEFFICIENT * progress + Easings.ELASTIC_IN_OUT_SECOND_HALF_EXPONENT_OFFSET)
                  * Math.sin((Easings.ELASTIC_IN_OUT_SECOND_HALF_PHASE_COEFFICIENT * progress - Easings.ELASTIC_IN_OUT_SECOND_HALF_PHASE_OFFSET) * Easings.ELASTIC_IN_OUT_SECOND_HALF_ANGULAR_FACTOR)
                  / Easings.ELASTIC_IN_OUT_SECOND_HALF_DIVISOR
               + 1.0;
      } else {
         return progress;
      }
   };
   public static final EasingFunction EXPO_IN = progress -> progress != 0.0
      ? Math.pow(Easings.EXPO_IN_POWER_BASE, Easings.EXPO_IN_EXPONENT_COEFFICIENT * progress - Easings.EXPO_IN_EXPONENT_OFFSET)
      : progress;
   public static final EasingFunction EXPO_OUT = progress -> progress != 1.0 ? 1.0 - Math.pow(Easings.EXPO_OUT_POWER_BASE, Easings.EXPO_OUT_EXPONENT_COEFFICIENT * progress) : progress;
   public static final EasingFunction EXPO_IN_OUT = progress -> {
      if (progress != 0.0 && progress != 1.0) {
         return progress < Easings.EXPO_IN_OUT_SPLIT_THRESHOLD
            ? Math.pow(Easings.EXPO_IN_OUT_FIRST_HALF_POWER_BASE, Easings.EXPO_IN_OUT_FIRST_HALF_EXPONENT_COEFFICIENT * progress - Easings.EXPO_IN_OUT_FIRST_HALF_EXPONENT_OFFSET) / Easings.EXPO_IN_OUT_FIRST_HALF_DIVISOR
            : (Easings.EXPO_IN_OUT_SECOND_HALF_OUTPUT_OFFSET - Math.pow(Easings.EXPO_IN_OUT_SECOND_HALF_POWER_BASE, Easings.EXPO_IN_OUT_SECOND_HALF_EXPONENT_COEFFICIENT * progress + Easings.EXPO_IN_OUT_SECOND_HALF_EXPONENT_OFFSET)) / Easings.EXPO_IN_OUT_SECOND_HALF_DIVISOR;
      } else {
         return progress;
      }
   };
   public static final EasingFunction BACK_IN = progress -> Easings.BACK_IN_CUBIC_COEFFICIENT * Math.pow(progress, Easings.BACK_IN_CUBIC_EXPONENT)
      - Easings.BACK_IN_QUADRATIC_COEFFICIENT * Math.pow(progress, Easings.BACK_IN_QUADRATIC_EXPONENT);
   public static final EasingFunction BACK_OUT = progress -> 1.0
      + Easings.BACK_OUT_CUBIC_COEFFICIENT * Math.pow(progress - 1.0, Easings.BACK_OUT_CUBIC_EXPONENT)
      + Easings.BACK_OUT_QUADRATIC_COEFFICIENT * Math.pow(progress - 1.0, Easings.BACK_OUT_QUADRATIC_EXPONENT);
   public static final EasingFunction BACK_IN_OUT = progress -> progress < Easings.BACK_IN_OUT_SPLIT_THRESHOLD
      ? Math.pow(Easings.BACK_IN_OUT_FIRST_HALF_PROGRESS_COEFFICIENT * progress, Easings.BACK_IN_OUT_FIRST_HALF_QUADRATIC_EXPONENT) * (Easings.BACK_IN_OUT_FIRST_HALF_LINEAR_COEFFICIENT * progress - Easings.BACK_IN_OUT_FIRST_HALF_OVERSHOOT) / Easings.BACK_IN_OUT_FIRST_HALF_DIVISOR
      : (
            Math.pow(Easings.BACK_IN_OUT_SECOND_HALF_QUADRATIC_PROGRESS_COEFFICIENT * progress - Easings.BACK_IN_OUT_SECOND_HALF_QUADRATIC_PROGRESS_OFFSET, Easings.BACK_IN_OUT_SECOND_HALF_QUADRATIC_EXPONENT)
                  * (Easings.BACK_IN_OUT_SECOND_HALF_LINEAR_COEFFICIENT * (progress * Easings.BACK_IN_OUT_SECOND_HALF_LINEAR_PROGRESS_COEFFICIENT - Easings.BACK_IN_OUT_SECOND_HALF_LINEAR_PROGRESS_OFFSET) + Easings.BACK_IN_OUT_SECOND_HALF_OVERSHOOT)
               + Easings.BACK_IN_OUT_SECOND_HALF_OUTPUT_OFFSET
         )
         / Easings.BACK_IN_OUT_SECOND_HALF_DIVISOR;
   public static final EasingFunction BOUNCE_OUT = progress -> {
      double bounceCoefficient = Easings.BOUNCE_OUT_PARABOLA_COEFFICIENT;
      double bounceIntervalDivisor = Easings.BOUNCE_OUT_INTERVAL_DIVISOR;
      if (progress < 1.0 / bounceIntervalDivisor) {
         return bounceCoefficient * Math.pow(progress, Easings.BOUNCE_OUT_FIRST_PARABOLA_EXPONENT);
      } else if (progress < Easings.BOUNCE_OUT_SECOND_INTERVAL_NUMERATOR / bounceIntervalDivisor) {
         return bounceCoefficient * Math.pow(progress - Easings.BOUNCE_OUT_SECOND_PARABOLA_OFFSET_NUMERATOR / bounceIntervalDivisor, Easings.BOUNCE_OUT_SECOND_PARABOLA_EXPONENT) + Easings.BOUNCE_OUT_SECOND_PARABOLA_HEIGHT;
      } else {
         return progress < Easings.BOUNCE_OUT_THIRD_INTERVAL_NUMERATOR / bounceIntervalDivisor
            ? bounceCoefficient * Math.pow(progress - Easings.BOUNCE_OUT_THIRD_PARABOLA_OFFSET_NUMERATOR / bounceIntervalDivisor, Easings.BOUNCE_OUT_THIRD_PARABOLA_EXPONENT) + Easings.BOUNCE_OUT_THIRD_PARABOLA_HEIGHT
            : bounceCoefficient * Math.pow(progress - Easings.BOUNCE_OUT_FOURTH_PARABOLA_OFFSET_NUMERATOR / bounceIntervalDivisor, Easings.BOUNCE_OUT_FOURTH_PARABOLA_EXPONENT) + Easings.BOUNCE_OUT_FOURTH_PARABOLA_HEIGHT;
      }
   };
   public static final EasingFunction BOUNCE_IN = progress -> 1.0 - BOUNCE_OUT.apply(1.0 - progress);
   public static final EasingFunction BOUNCE_IN_OUT = progress -> progress < Easings.BOUNCE_IN_OUT_SPLIT_THRESHOLD
      ? (1.0 - BOUNCE_OUT.apply(1.0 - Easings.BOUNCE_IN_OUT_FIRST_HALF_PROGRESS_COEFFICIENT * progress)) / Easings.BOUNCE_IN_OUT_FIRST_HALF_DIVISOR
      : (1.0 + BOUNCE_OUT.apply(Easings.BOUNCE_IN_OUT_SECOND_HALF_PROGRESS_COEFFICIENT * progress - 1.0)) / Easings.BOUNCE_IN_OUT_SECOND_HALF_DIVISOR;
   private static final String INSTANTIATION_ERROR_MESSAGE = "This is a utility class and cannot be instantiated";
   private static final double POWER_IN_OUT_SPLIT_THRESHOLD = 0.5;
   private static final double POWER_IN_OUT_FIRST_HALF_AMPLITUDE_BASE = 2.0;
   private static final double POWER_IN_OUT_SECOND_HALF_PROGRESS_COEFFICIENT = -2.0;
   private static final double POWER_IN_OUT_SECOND_HALF_PROGRESS_OFFSET = 2.0;
   private static final double POWER_IN_OUT_SECOND_HALF_DIVISOR = 2.0;
   private static final double BOUNCE_IN_OUT_SPLIT_THRESHOLD = 0.5;
   private static final double BOUNCE_IN_OUT_FIRST_HALF_PROGRESS_COEFFICIENT = 2.0;
   private static final double BOUNCE_IN_OUT_FIRST_HALF_DIVISOR = 2.0;
   private static final double BOUNCE_IN_OUT_SECOND_HALF_PROGRESS_COEFFICIENT = 2.0;
   private static final double BOUNCE_IN_OUT_SECOND_HALF_DIVISOR = 2.0;
   private static final double BOUNCE_OUT_PARABOLA_COEFFICIENT = 7.5625;
   private static final double BOUNCE_OUT_INTERVAL_DIVISOR = 2.75;
   private static final double BOUNCE_OUT_FIRST_PARABOLA_EXPONENT = 2.0;
   private static final double BOUNCE_OUT_SECOND_INTERVAL_NUMERATOR = 2.0;
   private static final double BOUNCE_OUT_SECOND_PARABOLA_OFFSET_NUMERATOR = 1.5;
   private static final double BOUNCE_OUT_SECOND_PARABOLA_EXPONENT = 2.0;
   private static final double BOUNCE_OUT_SECOND_PARABOLA_HEIGHT = 0.75;
   private static final double BOUNCE_OUT_THIRD_INTERVAL_NUMERATOR = 2.5;
   private static final double BOUNCE_OUT_THIRD_PARABOLA_OFFSET_NUMERATOR = 2.25;
   private static final double BOUNCE_OUT_THIRD_PARABOLA_EXPONENT = 2.0;
   private static final double BOUNCE_OUT_THIRD_PARABOLA_HEIGHT = 0.9375;
   private static final double BOUNCE_OUT_FOURTH_PARABOLA_OFFSET_NUMERATOR = 2.625;
   private static final double BOUNCE_OUT_FOURTH_PARABOLA_EXPONENT = 2.0;
   private static final double BOUNCE_OUT_FOURTH_PARABOLA_HEIGHT = 0.984375;
   private static final double BACK_IN_OUT_SPLIT_THRESHOLD = 0.5;
   private static final double BACK_IN_OUT_FIRST_HALF_PROGRESS_COEFFICIENT = 2.0;
   private static final double BACK_IN_OUT_FIRST_HALF_QUADRATIC_EXPONENT = 2.0;
   private static final double BACK_IN_OUT_FIRST_HALF_LINEAR_COEFFICIENT = 7.189819;
   private static final double BACK_IN_OUT_FIRST_HALF_OVERSHOOT = 2.5949095;
   private static final double BACK_IN_OUT_FIRST_HALF_DIVISOR = 2.0;
   private static final double BACK_IN_OUT_SECOND_HALF_QUADRATIC_PROGRESS_COEFFICIENT = 2.0;
   private static final double BACK_IN_OUT_SECOND_HALF_QUADRATIC_PROGRESS_OFFSET = 2.0;
   private static final double BACK_IN_OUT_SECOND_HALF_QUADRATIC_EXPONENT = 2.0;
   private static final double BACK_IN_OUT_SECOND_HALF_LINEAR_COEFFICIENT = 3.5949095;
   private static final double BACK_IN_OUT_SECOND_HALF_LINEAR_PROGRESS_COEFFICIENT = 2.0;
   private static final double BACK_IN_OUT_SECOND_HALF_LINEAR_PROGRESS_OFFSET = 2.0;
   private static final double BACK_IN_OUT_SECOND_HALF_OVERSHOOT = 2.5949095;
   private static final double BACK_IN_OUT_SECOND_HALF_OUTPUT_OFFSET = 2.0;
   private static final double BACK_IN_OUT_SECOND_HALF_DIVISOR = 2.0;
   private static final double BACK_OUT_CUBIC_COEFFICIENT = 2.70158;
   private static final double BACK_OUT_CUBIC_EXPONENT = 3.0;
   private static final double BACK_OUT_QUADRATIC_COEFFICIENT = 1.70158;
   private static final double BACK_OUT_QUADRATIC_EXPONENT = 2.0;
   private static final double BACK_IN_CUBIC_COEFFICIENT = 2.70158;
   private static final double BACK_IN_CUBIC_EXPONENT = 3.0;
   private static final double BACK_IN_QUADRATIC_COEFFICIENT = 1.70158;
   private static final double BACK_IN_QUADRATIC_EXPONENT = 2.0;
   private static final double EXPO_IN_OUT_SPLIT_THRESHOLD = 0.5;
   private static final double EXPO_IN_OUT_FIRST_HALF_POWER_BASE = 2.0;
   private static final double EXPO_IN_OUT_FIRST_HALF_EXPONENT_COEFFICIENT = 20.0;
   private static final double EXPO_IN_OUT_FIRST_HALF_EXPONENT_OFFSET = 10.0;
   private static final double EXPO_IN_OUT_FIRST_HALF_DIVISOR = 2.0;
   private static final double EXPO_IN_OUT_SECOND_HALF_OUTPUT_OFFSET = 2.0;
   private static final double EXPO_IN_OUT_SECOND_HALF_POWER_BASE = 2.0;
   private static final double EXPO_IN_OUT_SECOND_HALF_EXPONENT_COEFFICIENT = -20.0;
   private static final double EXPO_IN_OUT_SECOND_HALF_EXPONENT_OFFSET = 10.0;
   private static final double EXPO_IN_OUT_SECOND_HALF_DIVISOR = 2.0;
   private static final double EXPO_OUT_POWER_BASE = 2.0;
   private static final double EXPO_OUT_EXPONENT_COEFFICIENT = -10.0;
   private static final double EXPO_IN_POWER_BASE = 2.0;
   private static final double EXPO_IN_EXPONENT_COEFFICIENT = 10.0;
   private static final double EXPO_IN_EXPONENT_OFFSET = 10.0;
   private static final double ELASTIC_IN_OUT_SPLIT_THRESHOLD = 0.5;
   private static final double ELASTIC_IN_OUT_FIRST_HALF_POWER_BASE = 2.0;
   private static final double ELASTIC_IN_OUT_FIRST_HALF_EXPONENT_COEFFICIENT = 20.0;
   private static final double ELASTIC_IN_OUT_FIRST_HALF_EXPONENT_OFFSET = 10.0;
   private static final double ELASTIC_IN_OUT_FIRST_HALF_PHASE_COEFFICIENT = 20.0;
   private static final double ELASTIC_IN_OUT_FIRST_HALF_PHASE_OFFSET = 11.125;
   private static final double ELASTIC_IN_OUT_FIRST_HALF_ANGULAR_FACTOR = Math.PI * 4.0 / 9.0;
   private static final double ELASTIC_IN_OUT_FIRST_HALF_DIVISOR = 2.0;
   private static final double ELASTIC_IN_OUT_SECOND_HALF_POWER_BASE = 2.0;
   private static final double ELASTIC_IN_OUT_SECOND_HALF_EXPONENT_COEFFICIENT = -20.0;
   private static final double ELASTIC_IN_OUT_SECOND_HALF_EXPONENT_OFFSET = 10.0;
   private static final double ELASTIC_IN_OUT_SECOND_HALF_PHASE_COEFFICIENT = 20.0;
   private static final double ELASTIC_IN_OUT_SECOND_HALF_PHASE_OFFSET = 11.125;
   private static final double ELASTIC_IN_OUT_SECOND_HALF_ANGULAR_FACTOR = Math.PI * 4.0 / 9.0;
   private static final double ELASTIC_IN_OUT_SECOND_HALF_DIVISOR = 2.0;
   private static final double ELASTIC_OUT_POWER_BASE = 2.0;
   private static final double ELASTIC_OUT_EXPONENT_COEFFICIENT = -10.0;
   private static final double ELASTIC_OUT_PHASE_COEFFICIENT = 10.0;
   private static final double ELASTIC_OUT_PHASE_OFFSET = 0.75;
   private static final double ELASTIC_OUT_ANGULAR_FACTOR = Math.PI * 2.0 / 3.0;
   private static final double ELASTIC_IN_POWER_BASE = -2.0;
   private static final double ELASTIC_IN_EXPONENT_COEFFICIENT = 10.0;
   private static final double ELASTIC_IN_EXPONENT_OFFSET = 10.0;
   private static final double ELASTIC_IN_PHASE_COEFFICIENT = 10.0;
   private static final double ELASTIC_IN_PHASE_OFFSET = 10.75;
   private static final double ELASTIC_IN_ANGULAR_FACTOR = Math.PI * 2.0 / 3.0;
   private static final double CIRC_IN_OUT_SPLIT_THRESHOLD = 0.5;
   private static final double CIRC_IN_OUT_FIRST_HALF_PROGRESS_COEFFICIENT = 2.0;
   private static final double CIRC_IN_OUT_FIRST_HALF_QUADRATIC_EXPONENT = 2.0;
   private static final double CIRC_IN_OUT_FIRST_HALF_DIVISOR = 2.0;
   private static final double CIRC_IN_OUT_SECOND_HALF_PROGRESS_COEFFICIENT = -2.0;
   private static final double CIRC_IN_OUT_SECOND_HALF_PROGRESS_OFFSET = 2.0;
   private static final double CIRC_IN_OUT_SECOND_HALF_QUADRATIC_EXPONENT = 2.0;
   private static final double CIRC_IN_OUT_SECOND_HALF_DIVISOR = 2.0;
   private static final double CIRC_OUT_QUADRATIC_EXPONENT = 2.0;
   private static final double CIRC_IN_QUADRATIC_EXPONENT = 2.0;
   private static final double SINE_IN_OUT_PI_FACTOR = Math.PI;
   private static final double SINE_IN_OUT_DIVISOR = 2.0;
   private static final double SINE_OUT_PI_FACTOR = Math.PI;
   private static final double SINE_OUT_ANGLE_DIVISOR = 2.0;
   private static final double SINE_IN_PI_FACTOR = Math.PI;
   private static final double SINE_IN_ANGLE_DIVISOR = 2.0;
   private static final double QUAD_IN_OUT_EXPONENT = 2.0;
   private static final double CUBIC_IN_OUT_EXPONENT = 3.0;
   private static final double QUART_IN_OUT_EXPONENT = 4.0;
   private static final double QUINT_IN_OUT_EXPONENT = 5.0;

   public static EasingFunction powerOut(double exponent) {
      return progress -> 1.0 - Math.pow(1.0 - progress, exponent);
   }

   public static EasingFunction powerIn(int exponent) {
      return powerIn((double)exponent);
   }

   public static EasingFunction powerIn(double exponent) {
      return progress -> Math.pow(progress, exponent);
   }

   @Generated
   private Easings() {
      throw new UnsupportedOperationException(INSTANTIATION_ERROR_MESSAGE);
   }

   public static EasingFunction powerInOut(double exponent) {
      return progress -> progress < POWER_IN_OUT_SPLIT_THRESHOLD ? Math.pow(POWER_IN_OUT_FIRST_HALF_AMPLITUDE_BASE, exponent - 1.0) * Math.pow(progress, exponent) : 1.0 - Math.pow(POWER_IN_OUT_SECOND_HALF_PROGRESS_COEFFICIENT * progress + POWER_IN_OUT_SECOND_HALF_PROGRESS_OFFSET, exponent) / POWER_IN_OUT_SECOND_HALF_DIVISOR;
   }

   public static EasingFunction powerOut(int exponent) {
      return powerOut((double)exponent);
   }
}
