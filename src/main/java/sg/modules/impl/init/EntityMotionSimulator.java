package sg.modules.impl.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class EntityMotionSimulator {
   private static final double ЯПо = 0.03;
   private static final double ЯПO = 3.0;
   private static final double ЯП7 = 3.0;
   private static final float ЯПР = -90.0F;
   private static final float ЯП3 = 90.0F;
   private static final double ЯПр = 0.01;
   private static final double ЯП6 = 0.16;
   private static final double ЯП_ = 2.0;
   private static final double ЯПШ = 2.0;
   private static final double ЯПы = -0.08;
   private static final double ЯПй = 0.05;
   private static final double ЯПЖ = 0.2;
   private static final double ЯПм = 2.0;
   private static final double ЯПК = 0.03;
   private static final double ЯПI = -0.15;
   private static final double ЯПТ = 0.15;
   private static final double ЯПО = -0.15;
   private static final double ЯПФ = -0.15;
   private static final double ЯПЙ = 0.15;
   private static final double ЯПк = 0.25;
   private static final double ЯПг = 0.05;
   private static final double ЯП0 = 0.25;
   private static final double ЯПх = 1.0E-7;
   private static final double ЯПУ = 1.0E-7;
   private static final double ЯПЗ = 1.0E-7;
   private static final double ЯПЕ = 1.0E-7;
   private static final double ЯПП = 1.0E-7;
   private static final double ЯП西 = 2.0;
   private static final double ЯПд = -1.0;
   private static final double ЯПБ = 0.75;
   private static final double ЯПС = 1.0E-8;
   private static final double ЯПз = 0.1;
   private static final double ЯПц = 1.0E-8;
   private static final double ЯПя = 0.04;
   private static final double Я西с = 3.2;
   private static final double Я西В = 1.0E-8;
   private static final double Я西Я = 0.1;
   private static final double Я西Х = 0.1;
   private static final double Я西е = 0.99F;
   private static final double Я西А = 0.98F;
   private static final double Я西4 = 0.99F;
   private static final double Я西п = 0.5;
   private static final double Я西Д = 0.85;
   private static final double Я西Ъ = 1.0E-7;
   private static final double Я西т = 1.0E-7;

   public static Vec3 К(Vec3 var0, float var1, float var2, double var3) {
      Vec3 var5 = Vec3.directionFromRotation(var2, var1);
      double var6 = var5.horizontalDistance();
      double var8 = var0.horizontalDistance();
      double var10 = Math.toRadians(var2);
      double var12 = Math.pow(Math.cos(var10), ЯП西);
      Vec3 var14 = var0.add(0.0, var3 * (ЯПд + var12 * ЯПБ), 0.0);
      if (var14.y < 0.0 && var6 > ЯПС) {
         double var15 = -var14.y * ЯПз * var12;
         var14 = var14.add(var5.x * var15 / var6, var15, var5.z * var15 / var6);
      }

      if (var2 < 0.0F && var6 > ЯПц) {
         double var17 = var8 * -Math.sin(var10) * ЯПя;
         var14 = var14.add(-var5.x * var17 / var6, var17 * Я西с, -var5.z * var17 / var6);
      }

      if (var6 > Я西В) {
         var14 = var14.add((var5.x / var6 * var8 - var14.x) * Я西Я, 0.0, (var5.z / var6 * var8 - var14.z) * Я西Х);
      }

      return var14.multiply(Я西е, Я西А, Я西4);
   }

   private static boolean К(Vec3 var0, Vec3 var1) {
      return Math.abs(var0.x - var1.x) > Я西Ъ || Math.abs(var0.z - var1.z) > Я西т;
   }

   public static AABB К(List<AABB> var0, double var1) {
      double var3 = Mth.clamp(var1, 0.0, var0.size() - 1);
      int var5 = (int)var3;
      AABB var6 = (AABB)var0.get(var5);
      if (var5 == var0.size() - 1) {
         return var6;
      }

      Vec3 var7 = ((AABB)var0.get(var5 + 1)).getCenter().subtract(var6.getCenter()).scale(var3 - var5);
      return var6.move(var7);
   }

   public static Vec3 К(Vec3 var0, float var1, float var2) {
      return var0.scale(Я西п).add(Vec3.directionFromRotation(var2, var1).scale(Я西Д));
   }

   public static List<AABB> К(
      AABB var0,
      EntityMotionTracker.Sample var1,
      EntityMotionSimulator.MotionProfile var2,
      int var3,
      EntityMotionSimulator.CollisionResolver var4,
      Predicate<AABB> var5
   ) {
      ArrayList var6 = new ArrayList();
      AABB var7 = var0;
      Vec3 var8 = var1.velocity();
      boolean var9 = var2.grounded() && var8.y <= ЯПо;
      boolean var10 = var2.gliding();
      var6.add(var7);

      for (int var11 = 1; var11 <= Math.clamp(var3, 0, 80); var11++) {
         double var12 = ЯПO * (1.0 - Math.exp(-var11 / ЯП7));
         float var14 = var2.yaw() + (float)(var1.yawRate() * var12);
         float var15 = Mth.clamp(var2.pitch() + (float)(var1.pitchRate() * var12), ЯПР, ЯП3);
         double var16 = var2.slowFalling() && var8.y <= 0.0 ? Math.min(ЯПр, var2.gravity()) : var2.gravity();
         Vec3 var18;
         if (var10 && !var9 && !var2.fluid()) {
            if (var2.boosted() && var11 <= 8) {
               var8 = К(var8, var14, var15);
            }

            var18 = К(var8, var14, var15, var16);
            if (!var2.boosted()) {
               Vec3 var31 = К(var1.velocity().subtract(var1.acceleration()), var2.yaw(), var2.pitch(), var16);
               Vec3 var20 = var1.velocity().subtract(var31);
               if (var20.lengthSqr() < ЯП6) {
                  var18 = var18.add(var20.scale(Math.exp(-var11 / ЯП_)));
               }
            }
         } else if (!var2.flying() && !var2.fluid()) {
            double var19 = var9 ? ЯПы : (var8.y - var16) * var2.verticalDrag();
            if (var2.levitation() >= 0) {
               var19 = (var8.y + (ЯПй * (var2.levitation() + 1) - var8.y) * ЯПЖ) * var2.verticalDrag();
            }

            double var21 = Math.exp(-var11 / ЯПм);
            double var23 = var9
               ? var8.x + var1.acceleration().x * var21
               : var8.x * var2.horizontalDrag() + (var1.acceleration().x + var1.velocity().x * (1.0 - var2.horizontalDrag())) * var21;
            double var25 = var9
               ? var8.z + var1.acceleration().z * var21
               : var8.z * var2.horizontalDrag() + (var1.acceleration().z + var1.velocity().z * (1.0 - var2.horizontalDrag())) * var21;
            if (var9) {
               double var27 = Math.hypot(var23, var25);
               double var29 = var1.velocity().horizontalDistance() + ЯПК;
               if (var27 > var29) {
                  var23 *= var29 / var27;
                  var25 *= var29 / var27;
               }
            }

            var18 = new Vec3(var23, var19, var25);
            if (var2.climbing()) {
               var18 = new Vec3(Mth.clamp(var23, ЯПI, ЯПТ), Math.max(var19, ЯПО), Mth.clamp(var25, ЯПФ, ЯПЙ));
            }
         } else {
            var18 = var8.add(var1.acceleration().scale(Math.exp(-var11 / ЯПШ)));
         }

         if (var5.test(var7)) {
            var18 = var18.multiply(ЯПк, ЯПг, ЯП0);
         }

         Vec3 var32 = var4.и(var7, var18);
         if (var9 && var2.stepHeight() > 0.0 && К(var18, var32)) {
            Vec3 var33 = var4.и(var7, new Vec3(0.0, var2.stepHeight(), 0.0));
            Vec3 var34 = var4.и(var7.move(var33), new Vec3(var18.x, 0.0, var18.z));
            Vec3 var22 = var4.и(var7.move(var33).move(var34), new Vec3(0.0, var18.y - var33.y, 0.0));
            Vec3 var35 = var33.add(var34).add(var22);
            if (var35.horizontalDistanceSqr() > var32.horizontalDistanceSqr() + ЯПх) {
               var32 = var35;
            }
         }

         var9 = var18.y < 0.0 && var32.y > var18.y + ЯПУ;
         if (var9) {
            var10 = false;
         }

         var7 = var7.move(var32);
         var6.add(var7);
         var8 = new Vec3(
            Math.abs(var32.x - var18.x) > ЯПЗ ? 0.0 : var18.x,
            Math.abs(var32.y - var18.y) > ЯПЕ ? 0.0 : var18.y,
            Math.abs(var32.z - var18.z) > ЯПП ? 0.0 : var18.z
         );
      }

      return var6;
   }

   private EntityMotionSimulator() {
   }

   public record MotionProfile(
      boolean grounded,
      boolean gliding,
      boolean flying,
      boolean fluid,
      boolean climbing,
      boolean slowFalling,
      int levitation,
      boolean boosted,
      double gravity,
      double horizontalDrag,
      double verticalDrag,
      double stepHeight,
      float yaw,
      float pitch
   ) {
   }

   @FunctionalInterface
   public interface CollisionResolver {
      Vec3 и(AABB var1, Vec3 var2);
   }
}
