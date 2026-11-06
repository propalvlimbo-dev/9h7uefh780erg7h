package sg.ec;

import lombok.Generated;

public final class UnhookState {
   private static boolean со = false;
   private static final String сЛ1 = "This is a utility class and cannot be instantiated";

   @Generated
   public static void ч(boolean var0) {
      со = var0;
   }

   @Generated
   private UnhookState() {
      throw new UnsupportedOperationException(сЛ1);
   }

   @Generated
   public static boolean ые() {
      return со;
   }
}
