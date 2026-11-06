package settings.render;

public final class ClipDepth {
   private static final float сЧп = 0.001F;
   private static final float сЧД = 0.5F;

   private ClipDepth() {
   }

   public static float м(float var0, float var1, float var2, boolean var3) {
      float var4 = var2 / Math.max(var0, сЧп) - var1;
      return Math.clamp(var3 ? var4 : (var4 + 1.0F) * сЧД, 0.0F, 1.0F);
   }
}
