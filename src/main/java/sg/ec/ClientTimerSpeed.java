package sg.ec;

import lombok.Generated;

public final class ClientTimerSpeed {
   private static volatile float ЪI = 1.0F;
   private static volatile float ЪТ = 1.0F;
   private static volatile long Х2;
   private static final long ХЫ_ = 1000000L;
   private static final String ХЫШ = "This is a utility class and cannot be instantiated";

   public static void тч() {
      Х2 = 0L;
      ЪТ = 1.0F;
   }

   public static void Оэ(float var0) {
      ЪI = var0;
   }

   @Generated
   private ClientTimerSpeed() {
      throw new UnsupportedOperationException(ХЫШ);
   }

   public static void М(float var0, long var1) {
      ЪТ = var0;
      Х2 = System.nanoTime() + var1 * ХЫ_;
   }

   public static float дю() {
      long var0 = Х2;
      if (var0 != 0L) {
         if (System.nanoTime() < var0) {
            return ЪТ;
         }

         Х2 = 0L;
         ЪТ = 1.0F;
      }

      return ЪI;
   }
}
