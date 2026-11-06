package dev.rose.anim;

public final class Spring {
   private static final double STEP = 0.004166666666666667;
   private static final double MAX_FRAME = 0.1;
   private double value;
   private double velocity;
   private double target;
   private double stiffness;
   private double damping;
   private double accumulator;
   private long lastNanos = System.nanoTime();

   public Spring(double initial, double response, double dampingFraction) {
      this.value = this.target = initial;
      this.configure(response, dampingFraction);
   }

   public Spring configure(double response, double dampingFraction) {
      double omega = (Math.PI * 2) / Math.max(response, 0.01);
      this.stiffness = omega * omega;
      this.damping = 2.0 * dampingFraction * omega;
      return this;
   }

   public Spring setTarget(double target) {
      this.update();
      this.target = target;
      return this;
   }

   public void snap(double value) {
      this.update();
      this.value = this.target = value;
      this.velocity = 0.0;
   }

   public void push(double speed) {
      this.update();
      this.velocity += speed;
   }

   public double get() {
      this.update();
      return this.value;
   }

   public float getFloat() {
      return (float)this.get();
   }

   public double velocity() {
      this.update();
      return this.velocity;
   }

   public double target() {
      return this.target;
   }

   private void update() {
      long now = System.nanoTime();
      this.accumulator = this.accumulator + Math.min((now - this.lastNanos) / 1.0E9, 0.1);

      for (this.lastNanos = now; this.accumulator >= 0.004166666666666667; this.accumulator -= 0.004166666666666667) {
         double force = -this.stiffness * (this.value - this.target) - this.damping * this.velocity;
         this.velocity += force * 0.004166666666666667;
         this.value = this.value + this.velocity * 0.004166666666666667;
      }
   }
}
