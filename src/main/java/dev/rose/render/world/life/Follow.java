package dev.rose.render.world.life;

import net.minecraft.world.phys.Vec3;


public final class Follow {
   private static final double STEP = 0.004166666666666667;
   private static final double MAX_FRAME = 0.1;
   private static final double TELEPORT = 64.0;
   private double x;
   private double y;
   private double z;
   private double vx;
   private double vy;
   private double vz;
   private double stiffness;
   private double damping;
   private double leftover;
   private boolean ready;

   public Follow(double response, double dampingFraction) {
      this.configure(response, dampingFraction);
   }

   public Follow configure(double response, double dampingFraction) {
      double omega = (Math.PI * 2) / Math.max(response, 0.01);
      this.stiffness = omega * omega;
      this.damping = 2.0 * dampingFraction * omega;
      return this;
   }

   public void step(float dt, double tx, double ty, double tz) {
      if (!this.ready) {
         this.snap(tx, ty, tz);
      } else {
         double dx = tx - this.x;
         double dy = ty - this.y;
         double dz = tz - this.z;
         if (dx * dx + dy * dy + dz * dz > 64.0) {
            this.snap(tx, ty, tz);
         } else {
            for (this.leftover = this.leftover + Math.min(dt, 0.1); this.leftover >= 0.004166666666666667; this.leftover -= 0.004166666666666667) {
               this.vx = this.vx + (-this.stiffness * (this.x - tx) - this.damping * this.vx) * 0.004166666666666667;
               this.vy = this.vy + (-this.stiffness * (this.y - ty) - this.damping * this.vy) * 0.004166666666666667;
               this.vz = this.vz + (-this.stiffness * (this.z - tz) - this.damping * this.vz) * 0.004166666666666667;
               this.x = this.x + this.vx * 0.004166666666666667;
               this.y = this.y + this.vy * 0.004166666666666667;
               this.z = this.z + this.vz * 0.004166666666666667;
            }
         }
      }
   }

   public void snap(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.vx = this.vy = this.vz = 0.0;
      this.leftover = 0.0;
      this.ready = true;
   }

   public void push(double dx, double dy, double dz) {
      this.vx += dx;
      this.vy += dy;
      this.vz += dz;
   }

   public boolean empty() {
      return !this.ready;
   }

   public void reset() {
      this.ready = false;
   }

   public double x() {
      return this.x;
   }

   public double y() {
      return this.y;
   }

   public double z() {
      return this.z;
   }

   public Vec3 get() {
      return new Vec3(this.x, this.y, this.z);
   }

   public Vec3 velocity() {
      return new Vec3(this.vx, this.vy, this.vz);
   }

   public double lag(double tx, double ty, double tz) {
      double dx = tx - this.x;
      double dy = ty - this.y;
      double dz = tz - this.z;
      return Math.sqrt(dx * dx + dy * dy + dz * dz);
   }
}
