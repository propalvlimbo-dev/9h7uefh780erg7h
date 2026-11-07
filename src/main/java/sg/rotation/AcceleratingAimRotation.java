package sg.rotation;

import java.util.Random;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.client.RoseClient;
import sg.rotation.RotationStrategy;
import sg.util.input.MouseSensitivityMath;

public class AcceleratingAimRotation extends RotationStrategy {
   private @com.google.gson.annotations.SerializedName("пж")  float accelerationProgress;
   private static final float ACCELERATION_BASE = 0.08F;
   private static final float ACCELERATION_RANDOM_RANGE = 0.02F;
   private static final float ACCELERATION_RANDOM_CENTER = 0.01F;
   private static final float ACCELERATION_SCALE = 0.01F;
   private static final double YAW_WOBBLE_TIME_FACTOR = 0.001;
   private static final double YAW_WOBBLE_AMPLITUDE = 0.5;
   private static final double PITCH_WOBBLE_TIME_FACTOR = 0.0015;
   private static final double PITCH_WOBBLE_AMPLITUDE = 0.3;
   private static final float YAW_RANDOM_CENTER = 0.5F;
   private static final float YAW_RANDOM_AMPLITUDE = 0.4F;
   private static final float PITCH_RANDOM_CENTER = 0.5F;
   private static final float PITCH_RANDOM_AMPLITUDE = 0.2F;
   private static final float BASE_YAW_SPEED = 90.0F;
   private static final float BASE_PITCH_SPEED = 90.0F;
   private static final float SPEED_BONUS_RANGE = 5.0F;
   private static final long YAW_WAVE_PERIOD_MILLIS = 10000L;
   private static final float YAW_WAVE_TIME_DIVISOR = 375.0F;
   private static final float YAW_WAVE_ANGULAR_FACTOR = 3.14F;
   private static final double YAW_WAVE_PI = Math.PI;
   private static final float YAW_WAVE_AMPLITUDE = 7.23F;
   private static final float YAW_WAVE_SCALE = 0.083333336F;
   private static final long PITCH_WAVE_PERIOD_MILLIS = 10000L;
   private static final float PITCH_WAVE_TIME_DIVISOR = 425.0F;
   private static final float PITCH_WAVE_ANGULAR_FACTOR = 3.14F;
   private static final double PITCH_WAVE_PI = Math.PI;
   private static final float PITCH_WAVE_AMPLITUDE = 15.2F;
   private static final float PITCH_WAVE_SCALE = 0.083333336F;

   @Override
   public Vector2f calculateRotation(Vector2f var1, Vector2f var2) {
      double var4 = Mth.wrapDegrees(var2.x - var1.x);
      double var6 = var2.y - var1.y;
      float var11 = (ACCELERATION_BASE + ((float)Math.random() * ACCELERATION_RANDOM_RANGE - ACCELERATION_RANDOM_CENTER)) * ACCELERATION_SCALE;
      this.accelerationProgress += var11;
      this.accelerationProgress = Math.clamp(this.accelerationProgress, 0.0F, 1.0F);
      float var10 = this.accelerationProgress;
      float var12 = 0.0F;
      float var13 = 0.0F;
      LivingEntity var14 = null;
      if (MINECRAFT_CLIENT.hitResult instanceof EntityHitResult var15 && var15.getEntity() == var14 && !this.returningToCamera) {
         this.accelerationProgress = 0.0F;
         var10 = 0.0F;
      } else {
         var12 = (float)(Math.sin(System.currentTimeMillis() * YAW_WOBBLE_TIME_FACTOR) * YAW_WOBBLE_AMPLITUDE);
         var13 = (float)(Math.cos(System.currentTimeMillis() * PITCH_WOBBLE_TIME_FACTOR) * PITCH_WOBBLE_AMPLITUDE);
         if (Math.abs(var4) > 1.0 || Math.abs(var6) > 1.0) {
            var12 += ((float)Math.random() - YAW_RANDOM_CENTER) * YAW_RANDOM_AMPLITUDE;
            var13 += ((float)Math.random() - PITCH_RANDOM_CENTER) * PITCH_RANDOM_AMPLITUDE;
         }
      }

      float var36 = BASE_YAW_SPEED;
      float var37 = BASE_PITCH_SPEED;
      float var17 = new Random().nextFloat(SPEED_BONUS_RANGE);
      double var18 = Math.sqrt(var4 * var4 + var6 * var6);
      if (var18 == 0.0) {
         return new Vector2f(var1.x, var1.y);
      }

      double var20 = Math.abs(var4 / var18);
      double var22 = Math.abs(var6 / var18);
      double var24 = (var36 + var17) * var20;
      double var26 = (var37 + var17) * var22;
      float var28 = (float)Math.max(Math.min(var4, var24), -var24) + var12;
      float var29 = (float)Math.max(Math.min(var6, var26), -var26) + var13;
      float var8 = var1.x + var28 * var10;
      float var9 = var1.y + var29 * var10;
      float var30 = (float)(System.currentTimeMillis() % YAW_WAVE_PERIOD_MILLIS) / YAW_WAVE_TIME_DIVISOR;
      float var31 = (float)Math.sin(var30 * YAW_WAVE_ANGULAR_FACTOR * YAW_WAVE_PI) * YAW_WAVE_AMPLITUDE * YAW_WAVE_SCALE;
      var8 -= !this.returningToCamera ? var31 : 0.0F;
      float var32 = (float)(System.currentTimeMillis() % PITCH_WAVE_PERIOD_MILLIS) / PITCH_WAVE_TIME_DIVISOR;
      float var33 = (float)Math.sin(var32 * PITCH_WAVE_ANGULAR_FACTOR * PITCH_WAVE_PI) * PITCH_WAVE_AMPLITUDE * PITCH_WAVE_SCALE;
      var9 += !this.returningToCamera ? var33 : 0.0F;
      return new Vector2f(MouseSensitivityMath.snapRotationDelta(var8), MouseSensitivityMath.snapRotationDelta(var9));
   }

   public AcceleratingAimRotation(Vec3 var1) {
      super(var1, RotationStrategy.Speed.FAST);
   }
}
