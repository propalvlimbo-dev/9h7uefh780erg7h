package sg.animation;

import net.minecraft.util.Mth;

public class FloatAnimation {
   private @com.google.gson.annotations.SerializedName("тн")  float currentValue;
   private @com.google.gson.annotations.SerializedName("тЦ")  float targetValue;
   private @com.google.gson.annotations.SerializedName("тч")  float startValue;
   private @com.google.gson.annotations.SerializedName("тЭ")  float speed;
   private @com.google.gson.annotations.SerializedName("Н")  EasingFunction easing;
   private @com.google.gson.annotations.SerializedName("ХК")  long startTimeNanos;
   private @com.google.gson.annotations.SerializedName("Я_")  double durationSeconds;
   private @com.google.gson.annotations.SerializedName("ех")  boolean animating;
   private static final double RETARGET_DURATION_FACTOR = 2.0;
   private static final double NANOS_PER_SECOND = 1.0E9;

   public boolean isFinished() {
      return this.getProgress() >= 1.0;
   }

   public FloatAnimation(float initialValue, float speed) {
      this(initialValue, speed, Easings.LINEAR);
   }

   public void setImmediately(float value) {
      this.currentValue = value;
      this.targetValue = value;
      this.startValue = value;
      this.animating = false;
   }

   public boolean isRunning() {
      return !this.isFinished();
   }

   public void updateTarget(float target) {
      if (this.targetValue != target || !this.animating) {
         this.targetValue = target;
         this.startValue = this.currentValue;
         this.startTimeNanos = System.nanoTime();
         this.durationSeconds = 1.0 / this.speed * RETARGET_DURATION_FACTOR;
         this.animating = true;
      }

      if (this.isFinished()) {
         this.currentValue = this.targetValue;
         this.animating = false;
      } else {
         double progress = this.getProgress();
         float easedProgress = (float)this.easing.apply(progress);
         this.currentValue = Mth.lerp(easedProgress, this.startValue, this.targetValue);
      }
   }

   public float getTarget() {
      return this.targetValue;
   }

   public void setEasing(EasingFunction easing) {
      this.easing = easing != null ? easing : Easings.LINEAR;
   }

   public void setSpeed(float speed) {
      this.speed = speed;
      this.durationSeconds = 1.0 / speed;
   }

   private double getProgress() {
      if (!this.animating) {
         return 1.0;
      }

      long nowNanos = System.nanoTime();
      double elapsedSeconds = (nowNanos - this.startTimeNanos) / NANOS_PER_SECOND;
      return Mth.clamp(elapsedSeconds / this.durationSeconds, 0.0, 1.0);
   }

   public FloatAnimation(float initialValue, float speed, EasingFunction easing) {
      this.currentValue = initialValue;
      this.targetValue = initialValue;
      this.startValue = initialValue;
      this.speed = speed;
      this.easing = easing != null ? easing : Easings.LINEAR;
      this.startTimeNanos = 0L;
      this.durationSeconds = 0.0;
      this.animating = false;
   }

   public float getValue() {
      return this.currentValue;
   }
}
