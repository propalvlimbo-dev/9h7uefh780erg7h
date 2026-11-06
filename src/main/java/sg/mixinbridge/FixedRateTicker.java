package sg.mixinbridge;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;

public class FixedRateTicker {
   private long еИ;
   private int те;
   private final boolean АП;
   private int тА = 0;
   private long еь = 0L;
   private static final long суС = 1000000000L;

   public void I(int var1, TickCallback... var2) {
      if (this.тА != var1) {
         this.еь = суС / var1;
         this.тА = var1;
      }

      long var3 = Util.getNanos();
      long var5 = var3 - this.еИ;
      this.те = this.те + (int)(var5 / this.еь);
      this.еИ = this.еИ + this.те * this.еь;

      for (this.те = Math.min(this.те, this.АП ? Math.min(this.тА, Minecraft.getInstance().getFps()) : this.тА); this.те > 0; this.те--) {
         for (TickCallback var10 : var2) {
            var10.Би();
         }
      }
   }

   public FixedRateTicker(boolean var1) {
      this.еИ = Util.getNanos();
      this.АП = var1;
      this.те = 0;
   }
}
