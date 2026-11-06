package dev.rose.anim;

public final class Animation {
   private double from;
   private double to;
   private long startNanos;
   private long durationNanos;
   private Easing easing = Easing.LINEAR;

   public Animation(double initial) {
      this.from = this.to = initial;
   }

   public Animation animate(double target, long durationMs, Easing easing) {
      if (target == this.to) {
         return this;
      }

      this.from = this.get();
      this.to = target;
      this.startNanos = System.nanoTime();
      this.durationNanos = durationMs * 1000000L;
      this.easing = easing;
      return this;
   }

   public void set(double value) {
      this.from = this.to = value;
      this.durationNanos = 0L;
   }

   public double get() {
      if (this.durationNanos <= 0L) {
         return this.to;
      }

      double t = (double)(System.nanoTime() - this.startNanos) / this.durationNanos;
      return t >= 1.0 ? this.to : this.from + (this.to - this.from) * this.easing.apply(t);
   }

   public float getFloat() {
      return (float)this.get();
   }

   public double target() {
      return this.to;
   }

   public boolean isDone() {
      return this.durationNanos <= 0L || System.nanoTime() - this.startNanos >= this.durationNanos;
   }
}
