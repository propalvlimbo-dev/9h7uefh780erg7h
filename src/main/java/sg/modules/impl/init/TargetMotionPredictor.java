package sg.modules.impl.init;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class TargetMotionPredictor {
   public static final double сУ = 3.0;
   private int Хю;
   private TargetMotionPredictor.Observation В;
   private TargetMotionPredictor.Observation Я;
   private static final int ЯЛе = Integer.MIN_VALUE;
   private static final double ЯЛА = 256.0;
   private static final double ЯЛ4 = 6.0;
   private static final double ЯЛп = 1.0E-10;
   private static final double ЯЛД = 6.0;
   private static final double ЯЛЪ = 1.0E-8;
   private static final double ЯЛт = 6.0;
   private static final double ЯЛа = 0.3;
   private static final double ЯЛЮ = 1.0E-8;
   private static final double ЯЛЧ = 1.0E-8;
   private static final double ЯЛЛ = 8.0;
   private static final double ЯЛ诶 = 0.5;
   private static final double ЯЛ1 = 2.0;
   private static final double ЯЛЬ = 3.0;
   private static final int ЯЛ衣 = Integer.MIN_VALUE;

   public void method_1ж() {
      this.Хю = ЯЛ衣;
      this.В = this.Я = null;
   }

   private EntityMotionTracker.Sample Ь() {
      int var1 = this.В.velocity().lengthSqr() < ЯЛЧ ? 0 : Math.clamp(this.В.tick() - this.В.positionTick(), 0, 2);
      return new EntityMotionTracker.Sample(this.В.position(), this.В.velocity(), this.В.acceleration(), 0.0F, 0.0F, var1);
   }

   public EntityMotionTracker.Sample O(int var1, int var2, Vec3 var3, Vec3 var4) {
      if (this.В == null || var1 != this.Хю || var2 < this.В.tick() || var2 - this.В.tick() > 10 || var3.distanceToSqr(this.В.position()) > ЯЛА) {
         this.method_1ж();
         this.Хю = var1;
         this.В = new TargetMotionPredictor.Observation(var2, var2, var3, O(var4, ЯЛ4), Vec3.ZERO);
         return this.Ь();
      }

      if (var2 == this.В.tick() && var3.distanceToSqr(this.В.position()) < ЯЛп) {
         return this.Ь();
      }

      if (var2 > this.В.tick()) {
         this.Я = this.В;
      }

      TargetMotionPredictor.Observation var5 = this.Я;
      if (var5 == null) {
         this.В = new TargetMotionPredictor.Observation(var2, var2, var3, O(var4, ЯЛД), Vec3.ZERO);
         return this.Ь();
      }

      Vec3 var6 = var3.subtract(var5.position());
      int var7 = Math.max(1, var2 - var5.positionTick());
      Vec3 var9 = Vec3.ZERO;
      int var10 = var5.positionTick();
      Vec3 var8;
      if (var6.lengthSqr() > ЯЛЪ) {
         var8 = O(var6.scale(1.0 / var7), ЯЛт);
         double var11 = var8.x * var5.velocity().x + var8.z * var5.velocity().z;
         if (var11 > 0.0) {
            Vec3 var13 = var8.subtract(var5.velocity()).scale(1.0 / var7);
            if (var13.x * var8.x + var13.z * var8.z >= 0.0) {
               var9 = O(var13, ЯЛа);
            }
         }

         var10 = var2;
      } else {
         var8 = var7 >= 3 ? Vec3.ZERO : var5.velocity();
         if (var8.lengthSqr() < ЯЛЮ) {
            var10 = var2;
         }
      }

      this.В = new TargetMotionPredictor.Observation(var2, var10, var3, var8, var9);
      return this.Ь();
   }

   public static AABB O(AABB var0, Vec3 var1, Vec3 var2) {
      return var0.move(var2.subtract(var1));
   }

   public static TargetMotionPredictor.PredictionWindow O(
      AABB var0,
      EntityMotionTracker.Sample var1,
      EntityMotionSimulator.MotionProfile var2,
      double var3,
      boolean var5,
      EntityMotionSimulator.CollisionResolver var6,
      Predicate<AABB> var7
   ) {
      double var8 = Math.clamp(var3, 0.0, ЯЛЛ);
      double var10 = var8 + var1.age() + ЯЛ诶 + (var5 ? 1 : 0);
      double var12 = Math.max(1.0, Math.ceil(var8)) + 1.0 + (var5 ? 1 : 0);
      int var14 = (int)Math.ceil(var10 + ЯЛ1 * var12 + ЯЛЬ + 1.0);
      List var15 = EntityMotionSimulator.К(var0, var1, var2, var14, var6, var7);
      return new TargetMotionPredictor.PredictionWindow(var15, var10, var12);
   }

   public TargetMotionPredictor() {
      this.Хю = ЯЛе;
   }

   private static Vec3 O(Vec3 var0, double var1) {
      double var3 = var0.length();
      if (!Double.isFinite(var3)) {
         return Vec3.ZERO;
      } else {
         return var3 > var1 ? var0.scale(var1 / var3) : var0;
      }
   }

   private record Observation(int tick, int positionTick, Vec3 position, Vec3 velocity, Vec3 acceleration) {
   }

   public record PredictionWindow(List<AABB> boxes, double leadTicks, double supportTicks) {
      public double readyAt(int var1) {
         return this.leadTicks + Math.max(0, var1 - 1) * this.supportTicks;
      }
   }
}
