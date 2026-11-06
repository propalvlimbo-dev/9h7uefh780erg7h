package sg.ec;

import net.minecraft.util.Mth;

public final class WingAnimationMath {
   private static final float RADIANS_PER_DEGREE = (float) (Math.PI / 180.0);
   private static final double FULL_ROTATION_DEGREES = 360.0;
   private static final double HALF_PERIOD_SHIFT_DIVISOR = 2.0;
   private static final double HALF_PERIOD_CENTER_DIVISOR = 2.0;
   private static final float PI_RADIANS = (float) Math.PI;
   private static final float DEGREES_PER_RADIAN = (float) (180.0 / Math.PI);

   public static float easeInOutSine(float progress) {
      return -(Mth.cos(PI_RADIANS * progress) - 1.0F) / 2.0F;
   }

   public static double wrapModulo(double value, double modulus) {
      return (value % modulus + modulus) % modulus;
   }

   private static double wrappedDelta(double start, double end, double period) {
      return wrapModulo(end - start + period / HALF_PERIOD_SHIFT_DIVISOR, period) - period / HALF_PERIOD_CENTER_DIVISOR;
   }

   public static float toRadians(float degrees) {
      return degrees * RADIANS_PER_DEGREE;
   }

   private WingAnimationMath() {
   }

   public static float lerpClamped(float start, float end, float fraction) {
      return fraction <= 0.0F ? start : (fraction >= 1.0F ? end : start + (end - start) * fraction);
   }

   public static double lerpDegrees(double startDegrees, double endDegrees, double fraction) {
      return startDegrees + fraction * wrappedDelta(startDegrees, endDegrees, FULL_ROTATION_DEGREES);
   }

   public static float toDegrees(float radians) {
      return radians * DEGREES_PER_RADIAN;
   }
}
