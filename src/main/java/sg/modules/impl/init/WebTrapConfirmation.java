package sg.modules.impl.init;

import java.util.UUID;

public final class WebTrapConfirmation {
   private static final int ЪФ = 8;
   private UUID 衣;
   private int ЪЙ = -1;
   private int Ък = -1;
   private int Ъг = -1;
   private boolean Ам;

   public void method_4ч() {
      this.衣 = null;
      this.ЪЙ = this.Ък = this.Ъг = -1;
      this.Ам = false;
   }

   public boolean I(UUID var1) {
      return !this.Ам && this.Ък >= 0 && this.衣 != null && this.衣.equals(var1);
   }

   public void Ш(UUID var1, int var2, boolean var3) {
      this.Ш(var1, var2, var3, var3);
   }

   public void Ш(UUID var1, int var2, boolean var3, boolean var4) {
      if (var1 == null || !var1.equals(this.衣) || var2 < this.Ъг) {
         this.method_4ч();
      }

      this.衣 = var1;
      this.Ъг = var2;
      if (var1 != null && var4) {
         if (this.Ък < 0) {
            this.Ък = var2;
         }

         if (!this.Ам) {
            if (var3) {
               if (this.ЪЙ < 0) {
                  this.ЪЙ = var2;
               }

               this.Ам = var2 > this.ЪЙ;
            } else {
               this.ЪЙ = -1;
            }

            this.Ам = this.Ам | var2 - this.Ък >= 8;
         }
      } else {
         this.ЪЙ = this.Ък = -1;
         this.Ам = false;
      }
   }

   public boolean э(UUID var1) {
      return this.Ам && this.衣 != null && this.衣.equals(var1);
   }
}
