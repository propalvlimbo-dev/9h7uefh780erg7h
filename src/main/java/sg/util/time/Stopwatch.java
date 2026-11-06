package sg.util.time;

import lombok.Generated;

public class Stopwatch {
   private long Х6 = System.currentTimeMillis();
   private long Х_;

   public boolean Т(float var1) {
      return (float)System.currentTimeMillis() - var1 >= (float)this.Х_;
   }

   public boolean Т(double var1) {
      return System.currentTimeMillis() - this.Х6 >= var1;
   }

   public void ВД() {
      this.Х6 = System.currentTimeMillis();
   }

   public long method_9() {
      return System.currentTimeMillis() - this.Х6;
   }

   public boolean ф(long var1, boolean var3) {
      boolean var4 = this.Д() >= var1;
      if (var4 && var3) {
         this.ВД();
      }

      return var4;
   }

   public long Д() {
      return System.currentTimeMillis() - this.Х6;
   }

   public boolean method_0(long var1) {
      return this.Д() >= var1;
   }

   public boolean М(long var1) {
      return System.currentTimeMillis() - this.Х6 > var1;
   }

   @Generated
   public void я(long var1) {
      this.Х6 = var1;
   }

   public boolean эб() {
      return this.Х6 < System.currentTimeMillis();
   }
}
