package sg.render.pipeline;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class TrailPathHistory {
   public static final double сI = 0.8;
   private static final double сТ = 0.03;
   private static final double сО = 0.05;
   private final List<TrailPathHistory.TimedPoint> О = new ArrayList<>();
   private TrailPathHistory.TimedPoint В;
   private double сФ;
   private static final double Вг8 = 0.8;
   private static final double Вг5 = 256.0;
   private static final double Вгв = 0.03;
   private static final double Вгъ = 0.05;
   private static final double ВгМ = 0.8;
   private static final double ВгН = 0.8;
   private static final double Вгн = 0.8;

   public void method_1О() {
      this.О.clear();
      this.В = null;
      this.сФ = 0.0;
   }

   public void 西(Vec3 var1, double var2, int var4) {
      if (this.В != null && (var2 < this.В.time() || var2 - this.В.time() >= Вг8 || var1.distanceToSqr(this.В.position()) > Вг5)) {
         this.method_1О();
      }

      TrailPathHistory.TimedPoint var5 = new TrailPathHistory.TimedPoint(var1, var2);
      if (this.В == null) {
         this.В = var5;
         this.О.add(var5);
      } else {
         double var6 = Math.max(var1.distanceTo(this.В.position()) / Вгв, (var2 - this.В.time()) / Вгъ);
         if (var6 > 0.0) {
            double var8 = this.сФ + var6;
            int var10 = (int)Math.floor(var8);

            for (int var11 = 1; var11 <= var10; var11++) {
               this.О.add(this.В.lerp(var5, (var11 - this.сФ) / var6));
            }

            this.сФ = var8 - var10;
         }

         this.В = var5;
         int var12 = 0;

         while (var12 + 1 < this.О.size() && this.О.get(var12 + 1).time() <= var2 - ВгМ) {
            var12++;
         }

         var12 = Math.max(var12, this.О.size() - var4 - 1);
         if (var12 > 0) {
            this.О.subList(0, var12).clear();
         }
      }
   }

   public TrailPathHistory.TrailSnapshot 西(int var1) {
      if (this.В == null) {
         return new TrailPathHistory.TrailSnapshot(List.of(), 0.0);
      }

      double var2 = 0.0;
      if (this.О.size() > 1 && this.О.getFirst().time() < this.В.time() - ВгН) {
         TrailPathHistory.TimedPoint var4 = this.О.getFirst();
         TrailPathHistory.TimedPoint var5 = this.О.get(1);
         var2 = Math.clamp((this.В.time() - Вгн - var4.time()) / (var5.time() - var4.time()), 0.0, 1.0);
      }

      double var10 = Math.min(var1 - 1, this.О.size() - 1 + this.сФ - var2);
      int var6 = Math.min(this.О.size(), (int)var10 + 1);
      ArrayList var7 = new ArrayList(var6);
      var7.add(this.В);

      for (int var8 = 1; var8 < var6; var8++) {
         int var9 = this.О.size() - 1 - var8;
         var7.add(this.О.get(var9).lerp(this.О.get(var9 + 1), this.сФ));
      }

      return new TrailPathHistory.TrailSnapshot(var7, var10 + 1.0);
   }

   public record TrailSnapshot(List<TrailPathHistory.TimedPoint> points, double length) {
   }

   public record TimedPoint(Vec3 position, double time) {
      private TrailPathHistory.TimedPoint lerp(TrailPathHistory.TimedPoint var1, double var2) {
         return new TrailPathHistory.TimedPoint(this.position.lerp(var1.position(), var2), this.time + (var1.time - this.time) * var2);
      }
   }
}
