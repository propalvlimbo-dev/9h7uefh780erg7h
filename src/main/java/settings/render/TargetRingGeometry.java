package settings.render;

public final class TargetRingGeometry {
   private static final float сжЫ = 1.65F;
   private static final float сжl = 0.15F;
   private static final float сж8 = 0.5F;
   private static final float сж5 = 0.7F;
   private static final float сжв = 0.2F;
   private static final float сжъ = 1.7F;
   private static final double сжМ = 1.5;
   private static final float сжН = 255.0F;
   private static final float сжн = 0.08F;
   private static final float сжЦ = 255.0F;
   private static final float сжч = 0.22F;
   private static final float сжЭ = 1.75F;
   private static final double сж必 = 1.5;
   private static final double сжщ = 30.0;
   private static final float сжГ = 1.08F;

   public static void Ж(float var0, float var1, double var2, float var4, float var5, int var6, TargetRingGeometry.ParticleConsumer var7) {
      float var8 = var0 * сжЫ;
      float var9 = var1 - сжl;
      float var10 = Math.max(сж8, сж5 - сжв * var4);

      for (int var11 = 0; var11 < 360; var11 += 2) {
         double var12 = Math.toRadians(var11 + var2);
         float var14 = (float)(Math.cos(var12) * var8 * var10);
         float var15 = (float)(Math.sin(var12) * var8 * var10);

         for (int var16 = 0; var16 < var6; var16++) {
            float var17 = var9 / сжъ + var9 / 2.0F * (float)Math.cos(Math.toRadians(var2 / сжМ + var16 * 2));
            int var18 = Math.clamp((int)(сжН * var4 * ((float)var16 / var6) * сжн), 0, 255);
            var7.щ(var14, var17, var15, var5, var18);
         }
      }

      int var19 = Math.clamp((int)(сжЦ * var4 * сжч), 0, 255);

      for (int var20 = 0; var20 < 360; var20 += 2) {
         double var13 = Math.toRadians(var20 + var2);
         float var21 = (float)(Math.cos(var13) * var8 * var10);
         float var22 = (float)(Math.sin(var13) * var8 * var10);
         float var23 = var9 / сжЭ + var9 / 2.0F * (float)Math.cos(Math.toRadians(var2 / сж必 + сжщ));
         var7.щ(var21, var23, var22, var5 * сжГ, var19);
      }
   }

   private TargetRingGeometry() {
   }

   @FunctionalInterface
   public interface ParticleConsumer {
      void щ(float var1, float var2, float var3, float var4, int var5);
   }
}
