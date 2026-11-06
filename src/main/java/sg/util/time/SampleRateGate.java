package sg.util.time;

public final class SampleRateGate {
   private static final long Я1 = 1000000000L;
   private long ЯЬ;
   private int АI;
   private int АТ;
   private boolean ЯЙ = true;
   private static final long Я必l = 1000000000L;
   private static final double Я必8 = 1.0E9;

   public int еЙ() {
      return this.АТ;
   }

   public boolean я(long var1, int var3) {
      if (this.ЯЬ == 0L) {
         this.ЯЬ = var1;
      }

      this.АI++;
      long var4 = var1 - this.ЯЬ;
      if (var4 >= Я必l) {
         this.АТ = (int)Math.round(this.АI * Я必8 / var4);
         this.ЯЙ = this.АТ >= var3;
         this.ЯЬ = var1;
         this.АI = 0;
      }

      return this.ЯЙ;
   }

   public void Кр() {
      this.ЯЬ = 0L;
      this.АI = this.АТ = 0;
      this.ЯЙ = true;
   }
}
