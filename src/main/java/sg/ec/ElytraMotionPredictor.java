package sg.ec;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import sg.mixin.LivingEntityGravityInvoker;
import sg.mixinbridge.ResolvedEntityPoseAccess;

public final class ElytraMotionPredictor {
   private static final int е = 8;
   private static final double Я = 1.0E-4;
   private static final double сшш = 1.0E-4;
   private static final double сшИ = 8.0;
   private static final float сшь = (float) (Math.PI / 180.0);
   private static final double сшж = -1.0;
   private static final double сш弟 = 0.75;
   private static final double сшб = 0.01;
   private static final double сшЫ = -0.1;
   private static final double сшl = 0.04;
   private static final double сш8 = 3.2;
   private static final double сш5 = 0.1;
   private static final double сшв = 0.1;
   private static final double сшъ = 0.99F;
   private static final double сшМ = 0.98F;
   private static final double сшН = 0.99F;

   public static Vec3 method_9(LivingEntity var0, float var1) {
      if (var0 != null && Float.isFinite(var1) && var1 != 0.0F) {
         ResolvedEntityPoseAccess var2 = (ResolvedEntityPoseAccess)var0;
         float var3 = var2.getResolvedPitch();
         Vec3 var4 = var0.calculateViewVector(var3, var2.getResolvedYaw());
         double var5 = ((LivingEntityGravityInvoker)var0).rose$getEffectiveGravity();
         Vec3 var7 = method_9(var0.getDeltaMovement(), var4, var3, var5, var0.hasEffect(MobEffects.SLOW_FALLING));
         double var8 = var7.lengthSqr();
         if (Double.isFinite(var8) && !(var8 < сшш)) {
            double var10 = Math.sqrt(var8);
            float var12 = (float)(var10 / сшИ);
            return var7.scale(1.0 / var10).scale(var1 * var12);
         } else {
            return Vec3.ZERO;
         }
      } else {
         return Vec3.ZERO;
      }
   }

   private ElytraMotionPredictor() {
   }

   private static Vec3 method_9(Vec3 var0, Vec3 var1, float var2, double var3, boolean var5) {
      float var6 = var2 * сшь;
      double var7 = Math.sqrt(var1.x * var1.x + var1.z * var1.z);
      boolean var9 = var7 > 0.0;
      double var10 = var9 ? var1.x / var7 : 0.0;
      double var12 = var9 ? var1.z / var7 : 0.0;
      double var14 = Mth.square(Math.cos(var6));
      double var16 = сшж + var14 * сш弟;
      double var18 = var3 * var16;
      double var20 = Math.min(var3, сшб) * var16;
      double var22 = сшЫ * var14;
      boolean var24 = var6 < 0.0F && var9;
      double var25 = -Mth.sin(var6) * сшl;
      double var27 = var0.x;
      double var29 = var0.y;
      double var31 = var0.z;
      double var33 = 0.0;
      double var35 = 0.0;
      double var37 = 0.0;

      for (int var39 = 0; var39 < 8; var39++) {
         double var40 = Math.sqrt(var27 * var27 + var31 * var31);
         var29 += var5 && var29 <= 0.0 ? var20 : var18;
         if (var9 && var29 < 0.0) {
            double var42 = var29 * var22;
            var27 += var10 * var42;
            var29 += var42;
            var31 += var12 * var42;
         }

         if (var24) {
            double var45 = var40 * var25;
            var27 -= var10 * var45;
            var29 += var45 * сш8;
            var31 -= var12 * var45;
         }

         if (var9) {
            var27 += (var10 * var40 - var27) * сш5;
            var31 += (var12 * var40 - var31) * сшв;
         }

         var27 *= сшъ;
         var29 *= сшМ;
         var31 *= сшН;
         var33 += var27;
         var35 += var29;
         var37 += var31;
      }

      return new Vec3(var33, var35, var37);
   }
}
