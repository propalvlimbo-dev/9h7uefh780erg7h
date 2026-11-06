package sg.modules.impl.init;

public final class FixedRateScheduler {
   private static final long Вл = 500000L;
   private static final long Вэ = 100000000L;
   private long Вю;
   private static final long вI = 100000000L;
   private static final long вТ = 16666666L;
   private static final long вО = 500000L;
   private static final long вФ = 16666666L;
   private static final long вЙ = 16666666L;
   private static final long вк = 16666666L;

   public boolean method_4(long var1) {
      if (this.Вю == 0L || var1 - this.Вю > вI) {
         this.Вю = var1 + вТ;
         return true;
      }

      if (var1 + вО < this.Вю) {
         return false;
      }

      this.Вю = this.Вю + вФ;
      if (this.Вю <= var1) {
         this.Вю = this.Вю + ((var1 - this.Вю) / вЙ + 1L) * вк;
      }

      return true;
   }

   public void ы5() {
      this.Вю = 0L;
   }
}
