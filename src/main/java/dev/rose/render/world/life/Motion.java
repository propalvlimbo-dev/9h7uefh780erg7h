package dev.rose.render.world.life;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;


public final class Motion {
   private static final double TELEPORT = 64.0;
   private static Vec3 position = Vec3.ZERO;
   private static Vec3 velocity = Vec3.ZERO;
   private static Vec3 acceleration = Vec3.ZERO;
   private static float speed;
   private static float horizontal;
   private static float fall;
   private static float yawRate;
   private static float pitchRate;
   private static float lastYaw;
   private static float lastPitch;
   private static float airborne;
   private static float sinceLanding;
   private static boolean ready;

   private Motion() {
   }

   public static void update(LocalPlayer player, float dt) {
      if (player == null) {
         ready = false;
      } else {
         Vec3 now = player.position();
         if (ready && !(dt <= 0.0F) && !(now.distanceToSqr(position) > 64.0)) {
            Vec3 previous = velocity;
            velocity = smooth(velocity, now.subtract(position).scale(1.0F / dt), dt, 0.08F);
            acceleration = smooth(acceleration, velocity.subtract(previous).scale(1.0F / dt), dt, 0.12F);
            position = now;
            speed = (float)velocity.length();
            horizontal = (float)Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
            fall = (float)(-velocity.y);
            float yaw = player.getYRot();
            float pitch = player.getXRot();
            yawRate = smooth(yawRate, Mth.degreesDifference(lastYaw, yaw) / dt, dt, 0.06F);
            pitchRate = smooth(pitchRate, (pitch - lastPitch) / dt, dt, 0.06F);
            lastYaw = yaw;
            lastPitch = pitch;
            if (player.onGround()) {
               airborne = 0.0F;
               sinceLanding += dt;
            } else {
               airborne += dt;
               sinceLanding = 0.0F;
            }
         } else {
            restart(player, now);
         }
      }
   }

   private static void restart(LocalPlayer player, Vec3 now) {
      position = now;
      velocity = acceleration = Vec3.ZERO;
      pitchRate = 0.0F;
      yawRate = 0.0F;
      fall = 0.0F;
      horizontal = 0.0F;
      speed = 0.0F;
      lastYaw = player.getYRot();
      lastPitch = player.getXRot();
      sinceLanding = 0.0F;
      airborne = 0.0F;
      ready = true;
   }

   public static Vec3 velocity() {
      return velocity;
   }

   public static Vec3 acceleration() {
      return acceleration;
   }

   public static float speed() {
      return speed;
   }

   public static float horizontalSpeed() {
      return horizontal;
   }

   public static float fallSpeed() {
      return fall;
   }

   public static float yawRate() {
      return yawRate;
   }

   public static float pitchRate() {
      return pitchRate;
   }

   public static float airborne() {
      return airborne;
   }

   public static float sinceLanding() {
      return sinceLanding;
   }

   public static float intensity() {
      return Math.clamp(speed / 12.0F, 0.0F, 1.0F);
   }

   private static Vec3 smooth(Vec3 current, Vec3 target, float dt, float tau) {
      double a = rate(dt, tau);
      return current.add(target.subtract(current).scale(a));
   }

   private static float smooth(float current, float target, float dt, float tau) {
      return current + (target - current) * rate(dt, tau);
   }

   private static float rate(float dt, float tau) {
      return 1.0F - (float)Math.exp(-dt / tau);
   }
}
