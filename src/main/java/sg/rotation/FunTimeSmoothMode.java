package sg.rotation;

import java.security.SecureRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.client.RoseClient;
import sg.modules.impl.combat.Aura;

/** Recoin FunTime smoothing adapted to the base's tick-driven rotation strategy. */
public final class FunTimeSmoothMode extends RotationStrategy {
   private final SecureRandom random = new SecureRandom();

   public FunTimeSmoothMode(Vec3 targetPosition) {
      super(targetPosition, Speed.SLOW);
      this.setCaptureCameraRotationOnActivate(true);
   }

   @Override public Vector2f calculateRotation(Vector2f current, Vector2f target) {
      if (MINECRAFT_CLIENT.player == null || target == null) return new Vector2f(current);
      float yawDelta = Mth.wrapDegrees(target.x - current.x);
      float pitchDelta = target.y - current.y;
      float distance = (float)Math.hypot(yawDelta, pitchDelta);
      if (distance < 1.0E-6F) return new Vector2f(current);
      Aura aura = RoseClient.getInstance().getServices().getModules().getAura();
      boolean canAttack = !this.returningToCamera && aura.Ж_95bedd6e() != null && aura.и(0) == 0;
      double phase = System.currentTimeMillis() / 50.0;
      float yawNoise = canAttack || this.returningToCamera ? 0.0F : (float)(this.randomLerp(14.0F, 18.0F) * Math.sin(phase));
      float pitchNoise = canAttack || this.returningToCamera ? 0.0F : (float)(this.randomLerp(6.0F, 8.0F) * Math.cos(phase));
      float speed = canAttack || this.returningToCamera ? 1.0F
         : MINECRAFT_CLIENT.player.tickCount % 2 == 0 ? (this.random.nextBoolean() ? 0.25F : 0.1F) : 0.0F;
      float yawLimit = Math.abs(yawDelta / distance) * 50.0F;
      float pitchLimit = Math.abs(pitchDelta / distance) * 50.0F;
      float yawMove = Mth.clamp(yawDelta, -yawLimit, yawLimit);
      float pitchMove = Mth.clamp(pitchDelta, -pitchLimit, pitchLimit);
      float yaw = current.x + this.randomLerp(speed, speed + 0.2F) * yawMove + yawNoise;
      float pitch = current.y + this.randomLerp(speed, speed + 0.2F) * pitchMove + pitchNoise;
      if (this.returningToCamera) {
         yaw = current.x + yawMove;
         pitch = current.y + pitchMove;
      }
      return new Vector2f(yaw, Mth.clamp(pitch, -90.0F, 90.0F));
   }

   private float randomLerp(float min, float max) { return Mth.lerp(this.random.nextFloat(), min, max); }
}
