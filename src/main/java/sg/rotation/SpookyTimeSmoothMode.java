package sg.rotation;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.client.RoseClient;
import sg.util.input.MouseSensitivityMath;

/** Recoin SpookyTime speeds, ray stop and three-tick post-attack pause. */
public final class SpookyTimeSmoothMode extends RotationStrategy {
   private float currentSpeedYaw = 24.0F;
   private float currentSpeedPitch = 6.0F;
   private int attackPauseTicks;
   private Vector2f attackPauseAngle;

   public SpookyTimeSmoothMode(Vec3 targetPosition) {
      super(targetPosition, Speed.SLOW);
      this.setCaptureCameraRotationOnActivate(true);
   }

   public void onAttack(Vector2f current) {
      if (MINECRAFT_CLIENT.player == null) return;
      this.attackPauseAngle = current == null
         ? new Vector2f(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot()) : new Vector2f(current);
      this.attackPauseTicks = 3;
   }

   @Override public Vector2f getTargetRotation() {
      LivingEntity entity = null;
      if (entity == null || MINECRAFT_CLIENT.player == null) return super.getTargetRotation();
      // Точка прицела берётся из текущего положения сущности.
      double yExpand = Mth.clamp(entity.getEyeY() - entity.getY() - 0.3, 0.0, entity.getBbHeight());
      Vec3 direction = entity.position().add(0.0, yExpand, 0.0).subtract(MINECRAFT_CLIENT.player.getEyePosition());
      return new Vector2f((float)Math.toDegrees(Math.atan2(-direction.x, direction.z)),
         Mth.clamp((float)-Math.toDegrees(Math.atan2(direction.y, Math.hypot(direction.x, direction.z))), -90.0F, 90.0F));
   }

   @Override public Vector2f calculateRotation(Vector2f current, Vector2f target) {
      if (MINECRAFT_CLIENT.player == null || target == null) return new Vector2f(current);
      if (!this.returningToCamera && this.attackPauseTicks > 0) {
         this.attackPauseTicks--;
         return new Vector2f(this.attackPauseAngle);
      }
      LivingEntity entity = null;
      float speedYaw = randomLerp(45.0F, 60.0F);
      float speedPitch = randomLerp(8.0F, 14.0F);
      if (entity != null && this.rayIntersects(current, entity)) {
         speedYaw = 0.0F;
         speedPitch = 0.0F;
      }
      this.currentSpeedYaw += (speedYaw - this.currentSpeedYaw) * 0.35F;
      this.currentSpeedPitch += (speedPitch - this.currentSpeedPitch) * 0.35F;
      float yawDelta = Mth.wrapDegrees(target.x - current.x);
      float pitchDelta = target.y - current.y;
      float yawMove = Mth.clamp(yawDelta, -this.currentSpeedYaw, this.currentSpeedYaw);
      float pitchMove = Mth.clamp(pitchDelta, -this.currentSpeedPitch, this.currentSpeedPitch);
      yawMove = MouseSensitivityMath.snapRotationDelta(yawMove);
      pitchMove = MouseSensitivityMath.snapRotationDelta(pitchMove);
      return new Vector2f(current.x + yawMove, Mth.clamp(current.y + pitchMove, -90.0F, 90.0F));
   }

   private boolean rayIntersects(Vector2f angle, LivingEntity entity) {
      Vec3 eyes = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 end = eyes.add(Vec3.directionFromRotation(angle.y, angle.x).scale(10.0));
      return entity.getBoundingBox().contains(eyes) || entity.getBoundingBox().clip(eyes, end).isPresent();
   }

   @Override public void resetState() {
      this.currentSpeedYaw = 24.0F;
      this.currentSpeedPitch = 6.0F;
      this.attackPauseTicks = 0;
      this.attackPauseAngle = null;
   }

   private static float randomLerp(float min, float max) { return min + ThreadLocalRandom.current().nextFloat() * (max - min); }
}
