package settings.render;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import sg.ec.MinecraftAccess;
import sg.ec.Aim60Schema;

public final class AimFeatureSampler implements MinecraftAccess {
   private static final double Ях = 0.008;
   private static final double ЯУ = 0.03334;
   private static final double ЯЗ = 2.0;
   private static final double ЯЕ = 2.0;
   private static final float аз = 90.0F;
   private static final float ац = 60.0F;
   private static final Vec3 ц = new Vec3(0.0, 1.0, 0.0);
   private double ЯП;
   private UUID Ь;
   @SerializedName("1")
   private Object field_1;
   private AimFeatureSampler.Observation В;
   private static final double field_9е = 0.82F;
   private static final String field_9А = "heightRatio must be within 0..1";
   private static final float field_94 = -90.0F;
   private static final float field_9п = 90.0F;
   private static final String field_9Д = "Invalid observed look or partial tick";
   private static final long field_9Ъ = 8000000L;
   private static final double field_9т = 1.0E9;
   private static final double field_9а = 0.008;
   private static final double field_9Ю = 0.03334;
   private static final double field_9Ч = 2.0;
   private static final double field_9Л = 2.0;
   private static final float field_9诶 = 90.0F;
   private static final float field_91 = 60.0F;
   private static final String field_9Ь = "sample_dt_ms";
   private static final double field_9衣 = 1000.0;
   private static final String field_9ш = "world_tick_changed";
   private static final String field_9И = "world_tick_phase";
   private static final String field_9ь = "rel_pos";
   private static final String field_9ж = "distance";
   private static final String field_9弟 = "rel_vel";
   private static final String field_9б = "aim_error_yaw";
   private static final String field_9Ы = "aim_error_pitch";
   private static final String field_9l = "yaw_velocity";
   private static final String field_98 = "pitch_velocity";
   private static final String field_95 = "target_relative_yaw_sin";
   private static final String field_9в = "target_relative_yaw_cos";
   private static final String field_9ъ = "target_on_ground";
   private static final String field_9М = "target_sprinting";
   private static final String field_9Н = "target_sneaking";
   private static final String field_9н = "target_hurt_time";
   private static final String field_9Ц = "player_on_ground";
   private static final String field_9ч = "player_sprinting";
   private static final String field_9Э = "player_sneaking";
   private static final String field_9必 = "input_forward";
   private static final String field_9щ = "input_back";
   private static final String field_9Г = "input_left";
   private static final String field_9л = "input_right";
   private static final String field_9э = "input_jump";
   private static final String field_9ю = "input_attack";
   private static final String field_9Щ = "exact_crosshair";

   public AimFeatureSampler() {
      this.ЯП = field_9е;
   }

   public AimFeatureSampler.SampleResult З(LocalPlayer var1, LivingEntity var2, float var3, long var4, float var6, float var7, boolean var8) {
      if (Float.isFinite(var6) && Float.isFinite(var7) && !(var7 < field_94) && !(var7 > field_9п) && Float.isFinite(var3)) {
         ClientLevel var9 = MINECRAFT_CLIENT.level;
         AimFeatureSampler.Observation var10 = AimFeatureSampler.Observation.capture(var1, var2, var3, this.ЯП, var4, var6, var7);
         UUID var11 = var2.getUUID();
         if (var11.equals(this.Ь) && this.В != null && var9 == this.field_1) {
            long var12 = var10.nanoTime - this.В.nanoTime;
            if (var12 > 0L && var12 >= field_9Ъ) {
               double var14 = var12 / field_9т;
               Vec3 var16 = var10.targetPoint.subtract(this.В.targetPoint);
               Vec3 var17 = var10.playerEye.subtract(this.В.playerEye);
               float var18 = Mth.wrapDegrees(var10.playerYaw - this.В.playerYaw);
               float var19 = var10.playerPitch - this.В.playerPitch;
               if (Double.isFinite(var14)
                  && !(var14 < field_9а)
                  && !(var14 > field_9Ю)
                  && !(var16.length() > field_9Ч)
                  && !(var17.length() > field_9Л)
                  && !(Math.abs(var18) > field_9诶)
                  && !(Math.abs(var19) > field_91)) {
                  AimFeatureSampler.MotionSample var20 = AimFeatureSampler.MotionSample.of(var16, var17, var18, var19, var14);
                  AimFeatureSampler.CameraBasis var21 = AimFeatureSampler.CameraBasis.of(var10.playerYaw, var10.playerPitch);
                  Vec3 var22 = var10.targetPoint.subtract(var10.playerEye);
                  Vec3 var23 = var21.project(var22);
                  Vec3 var24 = var21.project(var20.targetVelocity.subtract(var20.playerVelocity));
                  double var25 = Math.toRadians(Mth.wrapDegrees(var2.getYRot(var3) - var10.playerYaw));
                  AimFeatureSampler.FeatureVectorBuilder var27 = new AimFeatureSampler.FeatureVectorBuilder();
                  var27.Д(field_9Ь, var14 * field_9衣);
                  var27.Л(field_9ш, var10.worldTime != this.В.worldTime);
                  var27.Д(field_9И, Mth.clamp(var3, 0.0F, 1.0F));
                  var27.Д(field_9ь, var23);
                  var27.Д(field_9ж, var22.length());
                  var27.Д(field_9弟, var24);
                  var27.Д(field_9б, Mth.wrapDegrees(var10.idealYaw - var10.playerYaw));
                  var27.Д(field_9Ы, var10.idealPitch - var10.playerPitch);
                  var27.Д(field_9l, var20.yawVelocity);
                  var27.Д(field_98, var20.pitchVelocity);
                  var27.Д(field_95, Math.sin(var25));
                  var27.Д(field_9в, Math.cos(var25));
                  var27.Л(field_9ъ, var2.onGround());
                  var27.Л(field_9М, var2.isSprinting());
                  var27.Л(field_9Н, var2.isShiftKeyDown());
                  var27.Д(field_9н, var2.hurtTime);
                  var27.Л(field_9Ц, var1.onGround());
                  var27.Л(field_9ч, var1.isSprinting());
                  var27.Л(field_9Э, var1.isShiftKeyDown());
                  var27.Л(field_9必, MINECRAFT_CLIENT.options.keyUp.isDown());
                  var27.Л(field_9щ, MINECRAFT_CLIENT.options.keyDown.isDown());
                  var27.Л(field_9Г, MINECRAFT_CLIENT.options.keyLeft.isDown());
                  var27.Л(field_9л, MINECRAFT_CLIENT.options.keyRight.isDown());
                  var27.Л(field_9э, MINECRAFT_CLIENT.options.keyJump.isDown());
                  var27.Л(field_9ю, MINECRAFT_CLIENT.options.keyAttack.isDown());
                  var27.Л(field_9Щ, var8);
                  this.В = var10;
                  return new AimFeatureSampler.SampleResult(var27.Х(), var10.playerYaw, var10.playerPitch, false);
               } else {
                  this.В = var10;
                  return AimFeatureSampler.SampleResult.reset();
               }
            } else {
               return AimFeatureSampler.SampleResult.skip();
            }
         } else {
            this.Ь = var11;
            this.field_1 = var9;
            this.В = var10;
            return AimFeatureSampler.SampleResult.reset();
         }
      } else {
         throw new IllegalArgumentException(field_9Д);
      }
   }

   public void К(double var1) {
      if (Double.isFinite(var1) && !(var1 < 0.0) && !(var1 > 1.0)) {
         if (Double.compare(var1, this.ЯП) != 0) {
            this.ЯП = var1;
            this.ЬЫ();
         }
      } else {
         throw new IllegalArgumentException(field_9А);
      }
   }

   public void ЬЫ() {
      this.Ь = null;
      this.field_1 = null;
      this.В = null;
   }

   public static Vec3 З(LivingEntity var0, float var1, double var2) {
      Vec3 var4 = var0.getPosition(var1);
      return new Vec3(var4.x, var4.y + var0.getBbHeight() * var2, var4.z);
   }

   public AimFeatureSampler.SampleResult З(LocalPlayer var1, LivingEntity var2, float var3, long var4) {
      return this.З(var1, var2, var3, var4, var1.getYRot(), var1.getXRot(), MINECRAFT_CLIENT.crosshairPickEntity == var2);
   }

   private static final class FeatureVectorBuilder {
      private final float[] б = new float[Aim60Schema.Хп];
      @SerializedName("4Х")
      private int field_4Х;

      float[] Х() {
         if (this.field_4Х != this.б.length) {
            throw new IllegalStateException("Feature count mismatch: " + this.field_4Х + "/" + this.б.length);
         } else {
            return this.б;
         }
      }

      void Л(String var1, boolean var2) {
         this.Д(var1, var2 ? 1.0 : 0.0);
      }

      void Д(String var1, Vec3 var2) {
         this.Д(var1 + "_right", var2.x);
         this.Д(var1 + "_up", var2.y);
         this.Д(var1 + "_forward", var2.z);
      }

      void Д(String var1, double var2) {
         if (this.field_4Х < this.б.length && Aim60Schema.Ъ[this.field_4Х].equals(var1)) {
            float var4 = (float)var2;
            if (Double.isFinite(var2) && Float.isFinite(var4)) {
               this.б[this.field_4Х++] = var4;
            } else {
               throw new IllegalArgumentException("Non-finite feature: " + var1);
            }
         } else {
            throw new IllegalStateException("Feature order mismatch at " + this.field_4Х + ": " + var1);
         }
      }
   }

   private record MotionSample(Vec3 targetVelocity, Vec3 playerVelocity, float yawVelocity, float pitchVelocity) {
      static AimFeatureSampler.MotionSample of(Vec3 var0, Vec3 var1, float var2, float var3, double var4) {
         double var6 = 1.0 / var4;
         return new AimFeatureSampler.MotionSample(var0.scale(var6), var1.scale(var6), (float)(var2 * var6), (float)(var3 * var6));
      }
   }

   public record SampleResult(float[] features, float playerYaw, float playerPitch, boolean resetHistory) {
      static AimFeatureSampler.SampleResult reset() {
         return new AimFeatureSampler.SampleResult(null, 0.0F, 0.0F, true);
      }

      static AimFeatureSampler.SampleResult skip() {
         return new AimFeatureSampler.SampleResult(null, 0.0F, 0.0F, false);
      }
   }

   private record CameraBasis(Vec3 right, Vec3 up, Vec3 forward) {
      static AimFeatureSampler.CameraBasis of(float var0, float var1) {
         double var2 = Math.toRadians(var0);
         double var4 = Math.toRadians(var1);
         Vec3 var6 = new Vec3(-Math.sin(var2), 0.0, Math.cos(var2)).normalize();
         Vec3 var7 = var6.cross(AimFeatureSampler.ц).normalize();
         Vec3 var8 = new Vec3(var6.x * Math.cos(var4), -Math.sin(var4), var6.z * Math.cos(var4)).normalize();
         return new AimFeatureSampler.CameraBasis(var7, var7.cross(var8).normalize(), var8);
      }

      Vec3 project(Vec3 var1) {
         return new Vec3(var1.dot(this.right), var1.dot(this.up), var1.dot(this.forward));
      }
   }

   private record Observation(
      long nanoTime, long worldTime, Vec3 playerEye, Vec3 targetPoint, float playerYaw, float playerPitch, float idealYaw, float idealPitch
   ) {
      static AimFeatureSampler.Observation capture(LocalPlayer var0, LivingEntity var1, float var2, double var3, long var5, float var7, float var8) {
         Vec3 var9 = var0.getEyePosition(var2);
         Vec3 var10 = AimFeatureSampler.З(var1, var2, var3);
         Vec3 var11 = var10.subtract(var9);
         float var12 = (float)Math.toDegrees(Math.atan2(var11.z, var11.x)) - 90.0F;
         float var13 = (float)(-Math.toDegrees(Math.atan2(var11.y, Math.hypot(var11.x, var11.z))));
         return new AimFeatureSampler.Observation(var5, MinecraftAccess.MINECRAFT_CLIENT.level.getGameTime(), var9, var10, var7, var8, var12, var13);
      }
   }
}
