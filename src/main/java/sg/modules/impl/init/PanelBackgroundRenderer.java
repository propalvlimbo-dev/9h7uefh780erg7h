package sg.modules.impl.init;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.joml.Matrix4f;
import org.joml.Vector4i;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.impl.render.ClickGui;
import sg.modules.impl.render.Hud;

public final class PanelBackgroundRenderer {
   private static final List<PanelBackgroundRenderer.PanelGeometry> К = new ArrayList<>();
   private static final float Я弟С = 8.0F;
   private static final float Я弟з = 8.0F;
   private static final float Я弟ц = 0.5F;
   private static final float Я弟я = 18.0F;
   private static final float Ябс = 22.0F;
   private static final double ЯбВ = 255.0;
   private static final float ЯбЯ = 8.0F;
   private static final float ЯбХ = 8.0F;
   private static final float Ябе = 0.5F;
   private static final float ЯбА = 14.0F;
   private static final float Яб4 = 1.6F;
   private static final float Ябп = 22.0F;
   private static final double ЯбД = 255.0;
   private static final float ЯбЪ = 8.0F;
   private static final float Ябт = 8.0F;
   private static final float Яба = 16.0F;
   private static final float ЯбЮ = 1.2F;
   private static final float ЯбЧ = 0.55F;
   private static final float ЯбЛ = 22.0F;
   private static final double Яб诶 = 255.0;
   private static final float Яб1 = 8.0F;
   private static final float ЯбЬ = 8.0F;
   private static final float Яб衣 = 8.0F;
   private static final float Ябш = 8.0F;
   private static final float ЯбИ = 4.0F;
   private static final float Ябь = 4.0F;
   private static final float Ябж = 0.01F;
   private static final float Яб弟 = 255.0F;
   private static final float Ябб = 165.0F;
   private static final float ЯбЫ = 130.0F;
   private static final String Ябl = "This is a utility class and cannot be instantiated";

   public static void ЕЩ() {
      К.clear();
   }

   public static void method_4(float var0, float var1, float var2, float var3, float var4) {
      if (!(var2 <= 0.0F) && !(var3 <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F)) {
         ClickGui var5 = RoseClient.getInstance().getServices().getModules().getClickGui();
         if (var5 != null && var5.иб()) {
            К.add(new PanelBackgroundRenderer.PanelGeometry(var0, var1, var2, var3, var4, new Matrix4f(UiDrawing.getTransform()), UiDrawing.getGlobalAlpha()));
         }
      }
   }

   public static void Ен() {
      if (!К.isEmpty()) {
         Matrix4f var0 = new Matrix4f(UiDrawing.getTransform());
         float var1 = UiDrawing.getGlobalAlpha();

         try {
            if (!К.isEmpty()) {
               for (PanelBackgroundRenderer.PanelGeometry var3 : К) {
                  UiDrawing.setTransform(var3.matrix);
                  UiDrawing.setGlobalAlpha(var3.alpha);
                  о(var3.x, var3.y, var3.w, var3.h, var3.round);
               }

               К.clear();
            }
         } finally {
            UiDrawing.setTransform(var0);
            UiDrawing.setGlobalAlpha(var1);
         }
      }
   }

   public static int method_5(int var0) {
      return ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
   }

   public static void о(float var0, float var1, float var2, float var3, float var4) {
      try {
         ClickGui var5 = RoseClient.getInstance().getServices().getModules().getClickGui();
         if (var5 == null) {
            return;
         }

         var5.Э(var0, var1, var2, var3, var4);
      } catch (Throwable var6) {
      }
   }

   public static void м(float var0, float var1, float var2, float var3, float var4, boolean var5) {
      int var6 = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_BACKGROUND);
      if (var5) {
         UiBatchDrawing.drawRoundedRect(var0, var1, var2, var3, var4, var6);
         method_5(var0, var1, var2, var3, var4);
      } else {
         UiDrawing.drawRoundedRect(var0, var1, var2, var3, var4, var6);
         о(var0, var1, var2, var3, var4);
      }
   }

   public static boolean цр() {
      try {
         Hud var0 = RoseClient.getInstance().getServices().getModules().getHud();
         return var0 != null && var0.getGlassSetting().getValue();
      } catch (Throwable var1) {
         return false;
      }
   }

   public static void method_1(float var0, float var1, float var2, float var3, float var4) {
      if (!(var2 < ЯбЪ) && !(var3 < Ябт)) {
         UiDrawing.drawShadow(var0, var1, var2, var3, Math.max(Яба, var4 * ЯбЮ), ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW), ЯбЧ));
         UiDrawing.drawBlur(var0, var1, var2, var3, ЯбЛ, var4, ColorMath.packArgb(255, 255, 255, Яб诶));
         UiDrawing.drawRoundedRect(var0, var1, var2, var3, var4, м(ThemeColorSlot.BACKGROUND));
         UiDrawing.drawOutline(var0, var1, var2, var3, 1.0F, var4, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      }
   }

   public static Vector4i м(ThemeColorSlot var0) {
      return м(var0, 185, 165);
   }

   @Generated
   private PanelBackgroundRenderer() {
      throw new UnsupportedOperationException(Ябl);
   }

   public static void Д(float var0, float var1, float var2, float var3, float var4) {
      if (!(var2 < Яб1) && !(var3 < ЯбЬ)) {
         UiDrawing.drawRoundedRect(var0, var1, var2, var3, var4, м(ThemeColorSlot.BACKGROUND, 160, 135));
         UiDrawing.drawOutline(var0, var1, var2, var3, 1.0F, var4, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      }
   }

   public static void э(float var0, float var1, float var2, float var3, float var4) {
      if (!(var2 < ЯбЯ) && !(var3 < ЯбХ)) {
         int var5 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SHADOW), Ябе);
         UiDrawing.drawShadow(var0, var1, var2, var3, Math.max(ЯбА, var4 * Яб4), var5);
         UiDrawing.drawBlur(var0, var1, var2, var3, Ябп, var4, ColorMath.packArgb(255, 255, 255, ЯбД));
         UiDrawing.drawRoundedRect(var0, var1, var2, var3, var4, м(ThemeColorSlot.WINDOW_BACKGROUND));
         о(var0, var1, var2, var3, var4);
      }
   }

   public static void ц(float var0, float var1, float var2, float var3, float var4, float var5) {
      if (!(var2 < ЯбИ) && !(var3 < Ябь) && !(var5 <= Ябж)) {
         float var6 = UiDrawing.getGlobalAlpha() / Яб弟 * Math.clamp(var5, 0.0F, 1.0F);
         int var7 = ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED);
         int var8 = ColorMath.getRed(var7);
         int var9 = ColorMath.getGreen(var7);
         int var10 = ColorMath.getBlue(var7);
         int var11 = ColorMath.packArgb(Math.min(255, var8 + 14), Math.min(255, var9 + 14), Math.min(255, var10 + 14), (int)(Ябб * var6));
         int var12 = ColorMath.packArgb(var8, var9, var10, (int)(ЯбЫ * var6));
         UiDrawing.drawRoundedRect(var0, var1, var2, var3, var4, new Vector4i(var11, var11, var12, var12));
      }
   }

   public static void к(float var0, float var1, float var2, float var3, float var4) {
      if (!(var2 < Яб衣) && !(var3 < Ябш)) {
         if (цр()) {
            UiDrawing.drawRoundedRect(var0, var1, var2, var3, var4, м(ThemeColorSlot.WINDOW_BACKGROUND));
            UiDrawing.drawOutline(var0, var1, var2, var3, 1.0F, var4, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
         } else {
            UiDrawing.drawRoundedRect(var0, var1, var2, var3, var4, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_BACKGROUND));
         }
      }
   }

   private static Vector4i м(ThemeColorSlot var0, int var1, int var2) {
      int var3 = ThemeEditor.getColorArgb(var0);
      int var4 = ColorMath.getRed(var3);
      int var5 = ColorMath.getGreen(var3);
      int var6 = ColorMath.getBlue(var3);
      int var7 = ColorMath.packArgb(Math.min(255, var4 + 12), Math.min(255, var5 + 12), Math.min(255, var6 + 12), var1);
      int var8 = ColorMath.packArgb(var4, var5, var6, var2);
      return new Vector4i(var7, var7, var8, var8);
   }

   private static void method_5(float var0, float var1, float var2, float var3, float var4) {
      method_4(var0, var1, var2, var3, var4);
   }

   public static void в(float var0, float var1, float var2, float var3, float var4) {
      if (!(var2 < Я弟С) && !(var3 < Я弟з)) {
         int var5 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SHADOW), Я弟ц);
         UiBatchDrawing.drawShadow(var0, var1, var2, var3, Я弟я, var5);
         UiBatchDrawing.drawBlur(var0, var1, var2, var3, Ябс, var4, ColorMath.packArgb(255, 255, 255, ЯбВ));
         UiBatchDrawing.drawRoundedRect(var0, var1, var2, var3, var4, м(ThemeColorSlot.WINDOW_BACKGROUND));
         method_5(var0, var1, var2, var3, var4);
      }
   }

   private record PanelGeometry(float x, float y, float w, float h, float round, Matrix4f matrix, float alpha) {
   }
}
