package sg.gui.third;

/**
 * Exponential smoothing animation and easing helpers, ported 1:1 from catlavanskid.
 */
public final class ThirdGuiAnim {
   public float value;
   public float target;
   public float speed;

   private static final float SETTLE_EPSILON = 0.0006F;
   private static final float BACK_OVERSHOOT = 1.70158F;
   private static final float BACK_TOTAL = BACK_OVERSHOOT + 1.0F;
   private static final float HALF_PROGRESS = 0.5F;
   private static final double DOUBLE_PROGRESS = 2.0;

   public ThirdGuiAnim(float start, float speed) {
      this.value = start;
      this.target = start;
      this.speed = speed;
   }

   public ThirdGuiAnim set(float newTarget) {
      this.target = newTarget;
      return this;
   }

   public void snap(float newValue) {
      this.value = newValue;
      this.target = newValue;
   }

   public float update(float deltaSeconds) {
      this.value += (this.target - this.value) * (1.0F - (float)Math.exp(-this.speed * deltaSeconds));
      if (Math.abs(this.target - this.value) < SETTLE_EPSILON) {
         this.value = this.target;
      }

      return this.value;
   }

   public boolean settled() {
      return this.value == this.target;
   }

   public static float clamp01(float value) {
      return value < 0.0F ? 0.0F : (value > 1.0F ? 1.0F : value);
   }

   public static float lerp(float from, float to, float progress) {
      return from + (to - from) * progress;
   }

   public static float outCubic(float progress) {
      float clamped = clamp01(progress);
      float inverted = 1.0F - clamped;
      return 1.0F - inverted * inverted * inverted;
   }

   public static float outBack(float progress) {
      float clamped = clamp01(progress);
      float shifted = clamped - 1.0F;
      return 1.0F + BACK_TOTAL * shifted * shifted * shifted + BACK_OVERSHOOT * shifted * shifted;
   }

   public static float inOutQuad(float progress) {
      float clamped = clamp01(progress);
      return clamped < HALF_PROGRESS
         ? (float)DOUBLE_PROGRESS * clamped * clamped
         : 1.0F - (float)Math.pow(-DOUBLE_PROGRESS * clamped + DOUBLE_PROGRESS, 2.0) / (float)HALF_PROGRESS;
   }
}
