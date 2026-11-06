package sg.modules.impl.init;

import java.util.Optional;
import java.util.function.DoubleFunction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class ProjectileInterceptSolver {
   public static final int Т = 64;
   public static final double ъ = 0.99F;
   public static final double М = 0.6F;
   public static final double Н = 0.05;
   private static final double Въб = 0.05;
   private static final double ВъЫ = 0.3;
   private static final double Въl = 0.5;
   private static final double Въ8 = 0.72;
   private static final double Въ5 = 0.28;
   private static final double Във = 0.025;
   private static final double Въъ = 0.25;
   private static final double ВъМ = 64.0;
   private static final double ВъН = 0.5;
   private static final double Вън = 0.5;
   private static final double ВъЦ = 0.01;
   private static final double Въч = 0.25;
   private static final double ВъЭ = 0.6F;
   private static final double Въ必 = 0.99F;
   private static final double Въщ = 1.0E-10;
   private static final double ВъГ = 0.6F;
   private static final double Въл = 0.99F;
   private static final double Въэ = 0.05;
   private static final double Въю = 64.0;
   private static final double ВъЩ = 1.0E-9;
   private static final double Въу = 1.0E-5;
   private static final double Въ2 = 1.0E-5;
   private static final double Въф = 0.6F;
   private static final double Въи = 0.99F;
   private static final double Въ9 = -0.05;
   private static final double Въо = 0.5;
   private static final double ВъO = 0.5;

   private Vec3 Ю_2f96ac28(Vec3 var1, Vec3 var2, double var3, ProjectileInterceptSolver.Environment var5) {
      ProjectileInterceptSolver.FlightFactors var6 = Ю(var3, var5.method_3(var1) ? ВъЭ : Въ必);
      Vec3 var7 = var2.subtract(var1).add(0.0, var6.drop(), 0.0).scale(1.0 / var6.distance());

      for (int var8 = 0; var8 < 4; var8++) {
         ProjectileInterceptSolver.FlightFactors var9 = this.Ю_6f031b66(var1, var7, var3, var5);
         Vec3 var10 = var2.subtract(var1).add(0.0, var9.drop(), 0.0).scale(1.0 / var9.distance());
         if (var10.distanceToSqr(var7) < Въщ) {
            return var10;
         }

         var7 = var10;
      }

      return var7;
   }

   private static boolean Ш(Vec3 var0) {
      return Double.isFinite(var0.lengthSqr());
   }

   private ProjectileInterceptSolver.FlightFactors Ю_6f031b66(Vec3 var1, Vec3 var2, double var3, ProjectileInterceptSolver.Environment var5) {
      Vec3 var6 = var1;
      double var7 = 0.0;
      double var9 = 0.0;
      double var11 = 1.0;
      double var13 = 0.0;

      for (int var15 = 0; var15 < Math.ceil(var3); var15++) {
         double var16 = Math.min(1.0, var3 - var15);
         double var18 = var5.method_3(var6) ? ВъГ : Въл;
         var7 += var11 * var16;
         var9 += var13 * var16;
         var6 = new Vec3(var1.x + var2.x * var7, var1.y + var2.y * var7 - var9, var1.z + var2.z * var7);
         var11 *= var18;
         var13 = var13 * var18 + Въэ;
      }

      return new ProjectileInterceptSolver.FlightFactors(var7, var9);
   }

   private static Vec3 Ю(AABB var0, double var1) {
      return new Vec3((var0.minX + var0.maxX) * Въо, var0.minY + var0.getYsize() * var1, (var0.minZ + var0.maxZ) * ВъO);
   }

   public boolean Ю(Vec3 var1, Vec3 var2, DoubleFunction<AABB> var3, ProjectileInterceptSolver.Environment var4, double var5) {
      Vec3 var7 = var1;

      for (int var8 = 0; var8 < Math.min(Въю, Math.ceil(var5)); var8++) {
         Vec3 var9 = var7.add(var2);
         AABB var10 = (AABB)var3.apply(var8);
         AABB var11 = (AABB)var3.apply(var8 + 1);
         Vec3 var12 = var11.getCenter().subtract(var10.getCenter());
         Vec3 var13 = var9.subtract(var12);
         Optional var14 = var10.clip(var7, var13);
         if (var10.contains(var7)) {
            return true;
         }

         if (var14.isPresent()) {
            double var15 = var7.distanceTo(var13);
            double var17 = var15 < ВъЩ ? 0.0 : var7.distanceTo((Vec3)var14.get()) / var15;
            Vec3 var19 = var7.add(var2.scale(var17));
            return var4.O(var7, var19) + Въу >= var7.distanceTo(var19);
         }

         if (var4.O(var7, var9) < var7.distanceTo(var9) - Въ2) {
            return false;
         }

         var2 = var2.scale(var4.method_3(var7) ? Въф : Въи).add(0.0, Въ9, 0.0);
         var7 = var9;
      }

      return false;
   }

   public ProjectileInterceptSolver.Solution Ю(Vec3 var1, Vec3 var2, double var3, DoubleFunction<AABB> var5, ProjectileInterceptSolver.Environment var6) {
      if (Double.isFinite(var3) && !(var3 < ВъЫ) && Ш(var1) && Ш(var2)) {
         for (double var10 : new double[]{Въl, Въ8, Въ5}) {
            double var12 = Във;
            double var14 = this.Ю(var1, var2, var3, var5, var6, var12, var10);

            for (double var16 = Въъ; var16 <= ВъМ; var16 += Въч) {
               double var18 = this.Ю(var1, var2, var3, var5, var6, var16, var10);
               if (Double.isFinite(var14) && Double.isFinite(var18) && (var14 <= 0.0 && var18 >= 0.0 || var14 >= 0.0 && var18 <= 0.0)) {
                  double var20 = var12;
                  double var22 = var16;
                  boolean var24 = var14 >= 0.0;

                  for (int var25 = 0; var25 < 22; var25++) {
                     double var26 = (var20 + var22) * ВъН;
                     double var28 = this.Ю(var1, var2, var3, var5, var6, var26, var10);
                     if (var28 >= 0.0 == var24) {
                        var20 = var26;
                     } else {
                        var22 = var26;
                     }
                  }

                  double var33 = (var20 + var22) * Вън;
                  Vec3 var27 = Ю((AABB)var5.apply(var33), var10);
                  Vec3 var34 = this.Ю_2f96ac28(var1, var27, var33, var6).subtract(var2);
                  if (Math.abs(var34.length() - var3) < ВъЦ) {
                     Vec3 var29 = var34.normalize();
                     float var30 = (float)Math.toDegrees(Math.atan2(-var29.x, var29.z));
                     float var31 = (float)(-Math.toDegrees(Math.atan2(var29.y, var29.horizontalDistance())));
                     ProjectileInterceptSolver.Solution var32 = new ProjectileInterceptSolver.Solution(var30, var31, var27, var33);
                     if (this.Ю(var1, var32.direction().scale(var3).add(var2), var5, var6, var33 + 1.0)) {
                        return var32;
                     }
                  }
               }

               var12 = var16;
               var14 = var18;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static ProjectileInterceptSolver.FlightFactors Ю(double var0, double var2) {
      int var4 = (int)Math.floor(var0);
      double var5 = var0 - var4;
      double var7 = Math.pow(var2, var4);
      double var9 = (1.0 - var7) / (1.0 - var2);
      return new ProjectileInterceptSolver.FlightFactors(var9 + var5 * var7, Въб / (1.0 - var2) * (var4 - var9 + var5 * (1.0 - var7)));
   }

   private double Ю(Vec3 var1, Vec3 var2, double var3, DoubleFunction<AABB> var5, ProjectileInterceptSolver.Environment var6, double var7, double var9) {
      return this.Ю_2f96ac28(var1, Ю((AABB)var5.apply(var7), var9), var7, var6).subtract(var2).length() - var3;
   }

   public record FlightFactors(double distance, double drop) {
   }

   public interface Environment {
      double O(Vec3 var1, Vec3 var2);

      boolean method_3(Vec3 var1);
   }

   public record Solution(float yaw, float pitch, Vec3 targetPosition, double flightTicks) {
      public Vec3 direction() {
         return Vec3.directionFromRotation(this.pitch, this.yaw);
      }
   }
}
