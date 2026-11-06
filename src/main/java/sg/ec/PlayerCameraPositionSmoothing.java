package sg.ec;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class PlayerCameraPositionSmoothing {
   private static final long Яб = 300000000L;
   private static LocalPlayer Х;
   private static Vec3 ы;
   private static Vec3 й;
   private static long ЯЫ;
   private static boolean ЯП;
   private static final double ВИ6 = 3.0E8;
   private static final double ВИ_ = 3.0;
   private static final double ВИШ = 2.0;

   public static void У(LocalPlayer var0) {
      if (var0 != null && Х == var0 && ы != null) {
         й = ы;
         ЯЫ = System.nanoTime();
         ЯП = true;
      } else {
         ЯП = false;
      }
   }

   public static Vec3 к(LocalPlayer var0, Vec3 var1) {
      if (Х != var0) {
         Ъ7();
         Х = var0;
      }

      if (ЯП && й != null) {
         double var2 = (System.nanoTime() - ЯЫ) / ВИ6;
         if (var2 >= 1.0) {
            ЯП = false;
            й = null;
            ы = var1;
            return var1;
         } else {
            var2 = Math.max(0.0, var2);
            double var4 = var2 * var2 * (ВИ_ - ВИШ * var2);
            ы = new Vec3(й.x + (var1.x - й.x) * var4, й.y + (var1.y - й.y) * var4, й.z + (var1.z - й.z) * var4);
            return ы;
         }
      } else {
         ы = var1;
         return var1;
      }
   }

   public static void Ъ7() {
      Х = null;
      ы = null;
      й = null;
      ЯЫ = 0L;
      ЯП = false;
   }

   private PlayerCameraPositionSmoothing() {
   }
}
