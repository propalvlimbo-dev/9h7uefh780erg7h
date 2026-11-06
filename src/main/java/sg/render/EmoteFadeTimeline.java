package sg.render;

public final class EmoteFadeTimeline {
   public long ы;
   public long й;
   private static final long ЯТи = 1000000L;
   private static final long ЯТ9 = 1000000L;
   private static final long ЯТо = -10000000L;
   private static final long ЯТO = 10000000L;
   private static final long ЯТ7 = 1000000L;
   private static final long ЯТР = 180000000L;
   private static final float ЯТ3 = 1.0E9F;
   private static final float ЯТр = 0.18F;
   private static final float ЯТ6 = 1.8E8F;
   private static final float ЯТ_ = 3.0F;
   private static final float ЯТШ = 3.0F;

   EmoteFadeTimeline(long var1, long var3) {
      this.ы = var1 - var3 * ЯТи;
   }

   float 必(long var1) {
      float var3 = (float)(var1 - this.ы) / ЯТ3;
      float var4 = Math.clamp(var3 / ЯТр, 0.0F, 1.0F);
      float var5 = this.й == 0L ? 1.0F : 1.0F - Math.clamp((float)(var1 - this.й) / ЯТ6, 0.0F, 1.0F);
      return var4 * var4 * (ЯТ_ - 2.0F * var4) * (var5 * var5 * (ЯТШ - 2.0F * var5));
   }

   void з(long var1) {
      if (this.й == 0L) {
         this.й = var1;
      }
   }

   void д(long var1, long var3, boolean var5, long var6) {
      long var8 = var1 - var3 * ЯТ9;
      this.ы = this.ы + Math.clamp(var8 - this.ы, ЯТо, ЯТO);
      if (var5) {
         long var10 = var1 - var6 * ЯТ7;
         this.й = this.й == 0L ? var10 : Math.min(this.й, var10);
      }
   }

   boolean I(long var1) {
      return this.й != 0L && var1 - this.й >= ЯТР;
   }
}
