package sg.modules.impl.init;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class WindChargeInterceptSolver {
   public static final double с3 = 1.5;
   public static final int ХД = 40;
   private static final double ВОм = 40.0;
   private static final double ВОК = 0.5;
   private static final double ВОI = 0.72;
   private static final double ВОТ = 0.28;
   private static final double ВОО = 1.0E-8;
   private static final double ВОФ = 2.25;
   private static final double ВОЙ = 2.0;
   private static final double ВОк = 1.0E-6;
   private static final double ВОг = 1.0E-7;
   private static final double ВО0 = 1.0E-7;
   private static final double ВОх = 1.5;
   private static final double ВОУ = 1.0E-5;
   private static final double ВОЗ = 1.5;
   private static final double ВОЕ = 1.0E-10;
   private static final double ВОП = 1.0E-10;
   private static final double ВО西 = 4.0;
   private static final double ВОд = -0.5;
   private static final double ВОБ = 1.0E-12;
   private static final double ВОС = 2.0;
   private static final double ВОз = 1.0E-8;
   private static final double ВОц = 1.0E-9;
   private static final double ВОя = 1.0E-6;
   private static final double ВФс = 1.0E-6;
   private static final double ВФВ = 0.5;
   private static final double ВФЯ = 0.5;

   private boolean М(Vec3 var1, Vec3 var2, List<AABB> var3, double var4, double var6, WindChargeInterceptSolver.CollisionProbe var8) {
      double var9 = 0.0;

      while (var9 < var6) {
         double var11 = Math.min(var6, Math.floor(var4 + var9 + ВОз) + 1.0 - var4);
         if (var11 <= var9) {
            return false;
         }

         Vec3 var13 = var1.add(var2.scale(var9));
         Vec3 var14 = var1.add(var2.scale(var11));
         AABB var15 = EntityMotionSimulator.К(var3, var4 + var9);
         AABB var16 = EntityMotionSimulator.К(var3, var4 + var11);
         Vec3 var17 = var14.subtract(var16.getCenter().subtract(var15.getCenter()));
         Optional var18 = var15.clip(var13, var17);
         if (var15.contains(var13)) {
            return true;
         }

         if (var18.isPresent()) {
            double var19 = var13.distanceTo(var17);
            double var21 = var19 < ВОц ? 0.0 : var13.distanceTo((Vec3)var18.get()) / var19;
            Vec3 var23 = var13.lerp(var14, var21);
            return var8.п(var13, var23) + ВОя >= var13.distanceTo(var23);
         }

         if (var8.п(var13, var14) + ВФс < var13.distanceTo(var14)) {
            return false;
         }

         var9 = var11;
      }

      return false;
   }

   private static double[] М(double var0, double var2, double var4) {
      if (Math.abs(var0) < ВОЕ) {
         return Math.abs(var2) < ВОП ? new double[0] : new double[]{-var4 / var2};
      }

      double var6 = var2 * var2 - ВО西 * var0 * var4;
      if (var6 < 0.0) {
         return new double[0];
      }

      double var8 = ВОд * (var2 + Math.copySign(Math.sqrt(var6), var2));
      return Math.abs(var8) < ВОБ ? new double[]{-var2 / (ВОС * var0)} : new double[]{var8 / var0, var4 / var8};
   }

   private static Vec3 Ъ(AABB var0, double var1) {
      return new Vec3((var0.minX + var0.maxX) * ВФВ, var0.minY + var0.getYsize() * var1, (var0.minZ + var0.maxZ) * ВФЯ);
   }

   public WindChargeInterceptSolver.Solution М(Vec3 var1, Vec3 var2, List<AABB> var3, double var4, WindChargeInterceptSolver.CollisionProbe var6) {
      if (Double.isFinite(var1.lengthSqr()) && Double.isFinite(var2.lengthSqr()) && Double.isFinite(var4) && !(var4 < 0.0) && var3.size() >= 2) {
         double var7 = Math.min(ВОм, var3.size() - 1 - var4);
         WindChargeInterceptSolver.Solution var9 = null;

         for (double var13 : new double[]{ВОК, ВОI, ВОТ}) {
            double var15 = 0.0;

            while (var15 < var7) {
               double var17 = Math.min(var7, Math.floor(var4 + var15 + ВОО) + 1.0 - var4);
               if (var17 <= var15) {
                  break;
               }

               Vec3 var19 = Ъ(EntityMotionSimulator.К(var3, var4 + var15), var13);
               Vec3 var20 = Ъ(EntityMotionSimulator.К(var3, var4 + var17), var13).subtract(var19).scale(1.0 / (var17 - var15));
               Vec3 var21 = var19.subtract(var20.scale(var15)).subtract(var1);
               Vec3 var22 = var20.subtract(var2);
               double var23 = var22.lengthSqr() - ВОФ;
               double var25 = ВОЙ * var21.dot(var22);
               double var27 = var21.lengthSqr();

               for (double var32 : М(var23, var25, var27)) {
                  if (Double.isFinite(var32)
                     && !(var32 <= ВОк)
                     && !(var32 < var15 - ВОг)
                     && !(var32 > var17 + ВО0)
                     && (var9 == null || !(var32 >= var9.flightTicks()))) {
                     Vec3 var34 = Ъ(EntityMotionSimulator.К(var3, var4 + var32), var13);
                     Vec3 var35 = var34.subtract(var1).scale(1.0 / var32).subtract(var2);
                     if (!(Math.abs(var35.length() - ВОх) > ВОУ)) {
                        float var36 = (float)Math.toDegrees(Math.atan2(-var35.x, var35.z));
                        float var37 = (float)(-Math.toDegrees(Math.atan2(var35.y, var35.horizontalDistance())));
                        WindChargeInterceptSolver.Solution var38 = new WindChargeInterceptSolver.Solution(var36, var37, var32);
                        if (this.М(var1, var38.direction().scale(ВОЗ).add(var2), var3, var4, var32, var6)) {
                           var9 = var38;
                        }
                     }
                  }
               }

               var15 = var17;
            }

            if (var9 != null) {
               return var9;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @FunctionalInterface
   public interface CollisionProbe {
      double п(Vec3 var1, Vec3 var2);
   }

   public record Solution(float yaw, float pitch, double flightTicks) {
      public Vec3 direction() {
         return Vec3.directionFromRotation(this.pitch, this.yaw).normalize();
      }
   }
}
