package sg.modules.impl.init;

import lombok.Generated;
import sg.render.pipeline.PotionWheel;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public final class ItemWheelRenderer {
   private static final double Хты = 255.0;
   private static final float Хтй = 0.74F;
   private static final float ХтЖ = 6.5F;
   private static final float Хтм = 1.35F;
   private static final float ХтК = 1.05F;
   private static final float ХтI = 0.75F;
   private static final float ХтТ = 0.2F;
   private static final float ХтО = 14.0F;
   private static final float ХтФ = 185.0F;
   private static final float ХтЙ = 1.05F;
   private static final String Хтк = "This is a utility class and cannot be instantiated";

   @Generated
   private ItemWheelRenderer() {
      throw new UnsupportedOperationException(Хтк);
   }

   private static int К(int var0, int var1, int var2, int var3, float var4) {
      return ColorMath.packArgb(var0, var1, var2, var3 * Math.clamp(var4, 0.0F, 1.0F));
   }

   public static void а(float var0, float var1) {
      UiDrawing.drawRoundedRect(0.0F, 0.0F, var0, var1, 0.0F, К(ThemeColorSlot.WINDOW_BACKGROUND, 1.0F, 38));
   }

   private static int ж(ThemeColorSlot var0, float var1) {
      int var2 = ThemeEditor.getColorArgb(var0);
      return ColorMath.packArgb(
         ColorMath.getRed(var2), ColorMath.getGreen(var2), ColorMath.getBlue(var2), ColorMath.getAlpha(var2) * Math.clamp(var1, 0.0F, 1.0F)
      );
   }

   public static void К(
      PotionWheel var0, float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, float var8, float var9, float var10
   ) {
      int var11 = ColorMath.packArgb(232, 116, 134, Хты);
      int var12 = ColorMath.lerpArgb(var7 ? К(220, 227, 239, 185, var8) : К(225, 216, 220, 188, var8), К(244, 220, 225, 210, var8), var9);
      int var13 = ColorMath.lerpArgb(var7 ? ж(ThemeColorSlot.OUTLINE, var8) : К(199, 119, 133, 52, var8), ColorMath.withAlpha(var11, Хтй * var8), var9);
      int var14 = ColorMath.lerpArgb(var7 ? ж(ThemeColorSlot.WINDOW_BACKGROUND, var8) : К(18, 14, 18, 178, var8), К(126, 48, 64, 174, var8), var9);
      float var15 = ХтЖ * var10;
      float var16 = Хтм * var10;
      float var17 = ХтК * var10;
      var0.method_0(var1, var2, var3, var4, var5, var6, var15, var16, var12);
      var0.l(var1, var2, Math.max(0.0F, var3 - var17), var4 + var17, var5, var6, var15 - ХтI * var10, var16 + ХтТ * var10, var13);
      var0.l(var1, var2, var3, var4, var5, var6, var15, var16, var14);
   }

   public static void Ф(float var0, float var1, float var2, float var3, float var4) {
      UiDrawing.drawBlur(var0 - var2, var1 - var2, var2 * 2.0F, var2 * 2.0F, ХтО, var2, ColorMath.packArgb(220, 227, 239, ХтФ * var4));
      UiDrawing.drawRoundedRect(var0 - var2, var1 - var2, var2 * 2.0F, var2 * 2.0F, var2, ж(ThemeColorSlot.WINDOW_BACKGROUND, var4));
      UiDrawing.drawOutline(var0 - var2, var1 - var2, var2 * 2.0F, var2 * 2.0F, ХтЙ * var3, var2, ж(ThemeColorSlot.OUTLINE, var4));
   }

   private static int К(ThemeColorSlot var0, float var1, int var2) {
      int var3 = ThemeEditor.getColorArgb(var0);
      return ColorMath.packArgb(ColorMath.getRed(var3), ColorMath.getGreen(var3), ColorMath.getBlue(var3), var2 * Math.clamp(var1, 0.0F, 1.0F));
   }
}
