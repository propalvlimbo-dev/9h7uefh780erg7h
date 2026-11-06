package sg.rotation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import lombok.Generated;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import sg.ec.MinecraftAccess;

public final class AimGeometryMath {
   private static final double YAW_RADIANS_TO_DEGREES = 180.0 / Math.PI;
   private static final double YAW_OFFSET_DEGREES = 90.0;
   private static final double PITCH_RADIANS_TO_DEGREES = 180.0 / Math.PI;
   private static final double TICKS_PER_SECOND = 20.0;
   private static final double RANDOM_GAUSSIAN_SCALE = 0.02F;
   private static final double RANDOM_EXPONENT_RANGE = 0.7;
   private static final double RANDOM_WEIGHT_RANGE = 0.8;
   private static final double RANDOM_WEIGHT_OFFSET = 0.1;
   private static final double RANDOM_LOG_SCALE = 3.0;
   private static final double RANDOM_LOG_WEIGHT = 0.5;
   private static final double RANDOM_BASE_WEIGHT = 0.5;
   private static final String UTILITY_CLASS_MESSAGE = "This is a utility class and cannot be instantiated";

   public static double getFrameTimeSeconds() {
      return MinecraftAccess.MINECRAFT_CLIENT.getFps() > 0 ? 1.0 / MinecraftAccess.MINECRAFT_CLIENT.getFps() : 1.0;
   }

   public static float lerpByFrameTime(float var0, float var1, float var2) {
      return (float)(var0 + (var1 - var0) * Mth.clamp(getFrameTimeSeconds() * var2, 0.0, 1.0));
   }

   @Generated
   private AimGeometryMath() {
      throw new UnsupportedOperationException(UTILITY_CLASS_MESSAGE);
   }

   public static float randomizedInterpolation(float var0, float var1) {
      SecureRandom var2 = new SecureRandom();
      double var3 = var2.nextDouble();
      double var5 = var2.nextDouble();
      double var7 = var2.nextGaussian() * RANDOM_GAUSSIAN_SCALE;
      double var9 = Math.pow(var3, 1.0 + var2.nextDouble() * RANDOM_EXPONENT_RANGE);
      double var11 = (var5 * RANDOM_WEIGHT_RANGE + RANDOM_WEIGHT_OFFSET) * (Math.log1p(var3 * RANDOM_LOG_SCALE) * RANDOM_LOG_WEIGHT + RANDOM_BASE_WEIGHT);
      return (float)(var0 + (var1 - var0) * var9 * var11 + var7);
   }

   public static Vec3 lerpVector(Vec3 var0, Vec3 var1, float var2) {
      return new Vec3(Mth.lerp(var2, var0.x, var1.x), Mth.lerp(var2, var0.y, var1.y), Mth.lerp(var2, var0.z, var1.z));
   }

   public static Vec2 lookAnglesToPosition(Vec3 var0) {
      return lookAnglesToCoordinates(var0.x, var0.y, var0.z);
   }

   public static Vec3 lerpVectorComponents(Vec3 var0, Vec3 var1, float var2) {
      return new Vec3(
         blendByFrameTime((float)var0.x(), (float)var1.x(), var2), blendByFrameTime((float)var0.y(), (float)var1.y(), var2), blendByFrameTime((float)var0.z(), (float)var1.z(), var2)
      );
   }

   public static double getEntityMovementPerSecond(Entity var0) {
      double var1 = var0.getX() - var0.xo;
      double var3 = var0.getY() - var0.yo;
      double var5 = var0.getZ() - var0.zo;
      double var7 = Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
      return TICKS_PER_SECOND * var7;
   }

   public static Vec3 clampPointToBox(Vec3 var0, AABB var1) {
      return new Vec3(Mth.clamp(var0.x(), var1.minX, var1.maxX), Mth.clamp(var0.y(), var1.minY, var1.maxY), Mth.clamp(var0.z(), var1.minZ, var1.maxZ));
   }

   public static double roundToStep(double var0, double var2) {
      double var4 = Math.round(var0 / var2) * var2;
      BigDecimal var6 = new BigDecimal(var4);
      var6 = var6.setScale(2, RoundingMode.HALF_UP);
      return var6.doubleValue();
   }

   public static float lerpFloat(float var0, float var1, float var2) {
      return Mth.lerp(var2, var0, var1);
   }

   public static Vec2 lookAnglesToCoordinates(double var0, double var2, double var4) {
      double var6 = var0 - MinecraftAccess.MINECRAFT_CLIENT.player.getX();
      double var8 = var2 - MinecraftAccess.MINECRAFT_CLIENT.player.getEyeY();
      double var10 = var4 - MinecraftAccess.MINECRAFT_CLIENT.player.getZ();
      double var12 = Mth.sqrt((float)(var6 * var6 + var10 * var10));
      float var14 = (float)(Mth.atan2(var10, var6) * YAW_RADIANS_TO_DEGREES - YAW_OFFSET_DEGREES);
      float var15 = (float)(-Mth.atan2(var8, var12) * PITCH_RADIANS_TO_DEGREES);
      return new Vec2(var14, var15);
   }

   public static float blendByFrameTime(float var0, float var1, float var2) {
      return (1.0F - Mth.clamp((float)(getFrameTimeSeconds() * var2), 0.0F, 1.0F)) * var0 + Mth.clamp((float)(getFrameTimeSeconds() * var2), 0.0F, 1.0F) * var1;
   }
}
