package dev.rose.anim;

import java.util.function.DoubleUnaryOperator;


public enum Easing {
   LINEAR(t -> t),
   SINE_IN_OUT(t -> -(Math.cos(Math.PI * t) - 1.0) / 2.0),
   QUAD_OUT(t -> 1.0 - (1.0 - t) * (1.0 - t)),
   CUBIC_OUT(t -> 1.0 - Math.pow(1.0 - t, 3.0)),
   CUBIC_IN_OUT(t -> t < 0.5 ? 4.0 * t * t * t : 1.0 - Math.pow(-2.0 * t + 2.0, 3.0) / 2.0),
   QUART_OUT(t -> 1.0 - Math.pow(1.0 - t, 4.0)),
   QUINT_OUT(t -> 1.0 - Math.pow(1.0 - t, 5.0)),
   EXPO_OUT(t -> t >= 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * t)),
   BACK_OUT(t -> {
      double c1 = 1.70158;
      double c3 = c1 + 1.0;
      return 1.0 + c3 * Math.pow(t - 1.0, 3.0) + c1 * Math.pow(t - 1.0, 2.0);
   }),
   ELASTIC_OUT(t -> !(t <= 0.0) && !(t >= 1.0) ? Math.pow(2.0, -10.0 * t) * Math.sin((t * 10.0 - 0.75) * (Math.PI * 2.0 / 3.0)) + 1.0 : t);

   private final DoubleUnaryOperator function;

   Easing(DoubleUnaryOperator function) {
      this.function = function;
   }

   public double apply(double t) {
      return this.function.applyAsDouble(Math.clamp(t, 0.0, 1.0));
   }
}
