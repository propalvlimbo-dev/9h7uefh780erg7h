package sg.ec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class CollisionMotionPredictor {
   private static final double ЯOМ = -0.08;
   private static final double ЯOН = 0.25;
   private static final double ЯOн = 0.05;
   private static final double ЯOЦ = 0.25;
   private static final double ЯOч = 1.0E-7;
   private static final double ЯOЭ = 1.0E-7;
   private static final double ЯO必 = 1.0E-7;
   private static final double ЯOщ = 1.0E-7;
   private static final double ЯOГ = 0.98;
   private static final double ЯOл = 0.98;
   private static final double ЯOэ = 0.98;

   private CollisionMotionPredictor() {
   }

   public static List<CollisionMotionPredictor.MotionSample> Ч(
      AABB var0, Vec3 var1, boolean var2, double var3, int var5, CollisionMotionPredictor.CollisionResolver var6, Predicate<AABB> var7
   ) {
      ArrayList var8 = new ArrayList();
      var8.add(new CollisionMotionPredictor.MotionSample(0, var0, var2));

      for (int var9 = 1; var9 <= Math.clamp(var5, 0, 12); var9++) {
         boolean var10 = var7.test(var0);
         Vec3 var11 = var1;
         if (var2 && var11.y <= 0.0) {
            var11 = new Vec3(var11.x, ЯOМ, var11.z);
         }

         if (var10) {
            var11 = var11.multiply(ЯOН, ЯOн, ЯOЦ);
         }

         Vec3 var12 = var6.ь(var0, var11);
         var2 = var11.y < 0.0 && Math.abs(var12.y - var11.y) > ЯOч;
         var0 = var0.move(var12);
         var8.add(new CollisionMotionPredictor.MotionSample(var9, var0, var2));
         double var13 = Math.abs(var12.x - var11.x) > ЯOЭ ? 0.0 : var11.x;
         double var15 = Math.abs(var12.z - var11.z) > ЯO必 ? 0.0 : var11.z;
         double var17 = Math.abs(var12.y - var11.y) > ЯOщ ? 0.0 : var11.y;
         var1 = new Vec3(var13 * (var2 ? 1.0 : ЯOГ), var2 ? 0.0 : (var17 - var3) * ЯOл, var15 * (var2 ? 1.0 : ЯOэ));
      }

      return var8;
   }

   public record MotionSample(int tick, AABB box, boolean grounded) {
   }

   @FunctionalInterface
   public interface CollisionResolver {
      Vec3 ь(AABB var1, Vec3 var2);
   }
}
