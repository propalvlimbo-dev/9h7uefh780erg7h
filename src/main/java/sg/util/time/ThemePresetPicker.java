package sg.util.time;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Vector4i;
import settings.render.ClientLocalization;
import sg.ec.FontRenderer;
import sg.ec.ClientFonts;
import sg.theme.ColorTheme;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.theme.ThemePresetCatalog;
import sg.animation.Easings;
import sg.animation.FloatAnimation;

public class ThemePresetPicker {
   private static final float са = 420.0F;
   private static final float сЮ = 360.0F;
   private static final float сЧ = 122.0F;
   private static final float сЛ = 56.0F;
   private static final float с诶 = 10.0F;
   private static final float с1 = 10.0F;
   private static final int 必 = 3;
   private final ThemeEditor Я;
   @SerializedName("1")
   private final FloatAnimation field_1;
   @SerializedName("4")
   private final Map<String, FloatAnimation> field_4;
   private float сЬ;
   private static final float ВЫе = 10.0F;
   private static final float ВЫА = 210.0F;
   private static final float ВЫ4 = 180.0F;
   private static final float ВЫп = 15.0F;
   private static final float ВЫД = 20.0F;
   private static final double ВЫЪ = 255.0;
   private static final float ВЫт = 420.0F;
   private static final float ВЫа = 360.0F;
   private static final float ВЫЮ = 12.0F;
   private static final float ВЫЧ = 420.0F;
   private static final float ВЫЛ = 360.0F;
   private static final float ВЫ诶 = 12.0F;
   private static final String ВЫ1 = "Готовые темы";
   private static final float ВЫЬ = 14.0F;
   private static final float ВЫ衣 = 14.0F;
   private static final double ВЫш = 255.0;
   private static final String ВЫИ = "Выбери стиль и примени";
   private static final float ВЫь = 14.0F;
   private static final float ВЫж = 34.0F;
   private static final float ВЫ弟 = 420.0F;
   private static final float ВЫб = 34.0F;
   private static final float ВЫЫ = 10.0F;
   private static final float ВЫl = 24.0F;
   private static final float ВЫ8 = 10.0F;
   private static final String ВЫ5 = "s";
   private static final float ВЫв = 12.0F;
   private static final float ВЫъ = 10.0F;
   private static final float ВЫМ = 24.0F;
   private static final float ВЫН = 24.0F;
   private static final float ВЫн = 8.0F;
   private static final String ВЫЦ = "s";
   private static final float ВЫч = 14.0F;
   private static final float ВЫЭ = 58.0F;
   private static final float ВЫ必 = 288.0F;
   private static final float ВЫщ = 396.0F;
   private static final float ВЫГ = 12.0F;
   private static final float ВЫл = 20.0F;
   private static final float ВЫэ = 132.0F;
   private static final float ВЫю = 66.0F;
   private static final double ВЫЩ = 122.0;
   private static final double ВЫу = 56.0;
   private static final float ВЫ2 = 10.0F;
   private static final float ВЫф = 16.0F;
   private static final float ВЫи = 122.0F;
   private static final float ВЫ9 = 56.0F;
   private static final float ВЫо = 10.0F;
   private static final float ВЫO = 0.01F;
   private static final float ВЫ7 = 0.08F;
   private static final float ВЫР = 0.65F;
   private static final float ВЫ3 = 0.25F;
   private static final float ВЫр = 122.0F;
   private static final float ВЫ6 = 56.0F;
   private static final float ВЫ_ = 10.0F;
   private static final float ВЫШ = 10.0F;
   private static final float ВЫы = 34.0F;
   private static final double ВЫй = 255.0;
   private static final float ВЫЖ = 66.0F;
   private static final float ВЫм = 10.0F;
   private static final float ВЫК = 10.0F;
   private static final float ВЫI = 36.0F;
   private static final float ВЫТ = 16.0F;
   private static final float ВЫО = 4.0F;
   private static final float ВЫФ = 0.22F;
   private static final float ВЫЙ = 0.66F;
   private static final float ВЫк = 0.44F;
   private static final float ВЫг = 16.0F;
   private static final float ВЫ0 = 16.0F;
   private static final float ВЫх = 4.0F;
   private static final float ВЫУ = 12.0F;
   private static final float ВЫЗ = 6.0F;
   private static final float ВЫЕ = 16.0F;
   private static final float ВЫП = 16.0F;
   private static final float ВЫ西 = 4.0F;
   private static final float ВЫд = 20.0F;
   private static final float ВЫБ = 66.0F;
   private static final float ВЫС = 12.0F;
   private static final float ВЫз = 12.0F;
   private static final float ВЫц = 210.0F;
   private static final float ВЫя = 180.0F;
   private static final float Вlс = 420.0F;
   private static final float ВlВ = 34.0F;
   private static final float ВlЯ = 10.0F;
   private static final double ВlХ = 24.0;
   private static final double Вlе = 24.0;
   private static final float ВlА = 14.0F;
   private static final float Вl4 = 58.0F;
   private static final float Вlп = 288.0F;
   private static final double ВlД = 396.0;
   private static final double ВlЪ = 420.0;
   private static final double Вlт = 360.0;
   private static final float Вlа = 20.0F;
   private static final float ВlЮ = 66.0F;
   private static final float ВlЧ = 12.0F;
   private static final float ВlЛ = 20.0F;
   private static final float Вl诶 = 132.0F;
   private static final float Вl1 = 66.0F;
   private static final double ВlЬ = 122.0;
   private static final double Вl衣 = 56.0;
   private static final float Вlш = 210.0F;
   private static final float ВlИ = 180.0F;
   private static final double Вlь = 420.0;
   private static final double Вlж = 360.0;
   private static final float Вl弟 = 288.0F;
   private static final float Вlб = 42.0F;
   private static final float ВlЫ = 14.0F;

   private FloatAnimation Ю(String var1) {
      return this.field_4.computeIfAbsent(var1, var0 -> new FloatAnimation(0.0F, ВlЫ, Easings.CUBIC_OUT));
   }

   public void Ю(GuiGraphicsExtractor var1, float var2, float var3, float var4) {
      float var5 = this.Я.getGuiScreen().getGuiX();
      float var6 = this.Я.getGuiScreen().getGuiY();
      float var7 = var5 + this.Я.getGuiScreen().getGuiWidth() / 2.0F - ВЫА;
      float var8 = var6 + this.Я.getGuiScreen().getGuiHeight() / 2.0F - ВЫ4;
      UiDrawing.drawBlur(
         this.Я.getGuiScreen().getGuiX(),
         this.Я.getGuiScreen().getGuiY(),
         this.Я.getGuiScreen().getGuiWidth(),
         this.Я.getGuiScreen().getGuiHeight(),
         ВЫп,
         ВЫД,
         ColorMath.packArgb(255, 255, 255, ВЫЪ)
      );
      UiDrawing.drawRoundedRect(var7, var8, ВЫт, ВЫа, ВЫЮ, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND), 1.0F));
      UiDrawing.drawOutline(var7, var8, ВЫЧ, ВЫЛ, 1.0F, ВЫ诶, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(ВЫ1), var7 + ВЫЬ, var8 + ВЫ衣, 16, ColorMath.packArgb(255, 255, 255, ВЫш));
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(ВЫИ), var7 + ВЫь, var8 + ВЫж, 12, ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT));
      float var9 = var7 + ВЫ弟 - ВЫб;
      float var10 = var8 + ВЫЫ;
      FontRenderer var11 = ClientFonts.getIconsFont();
      float var12 = var9 + (ВЫl - var11.getFontFace().getGlyphWidth(115, ВЫ8)) / 2.0F;
      float var13 = UiDrawing.alignTextCenterY(var11, ВЫ5, var10 + ВЫв, ВЫъ);
      UiDrawing.drawOutline(var9, var10, ВЫМ, ВЫН, 1.0F, ВЫн, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawText(var11, ВЫЦ, var12, var13, 10, ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT));
      float var14 = var7 + ВЫч;
      float var15 = var8 + ВЫЭ;
      float var16 = ВЫ必;
      float var17 = this.Т();
      float var18 = Math.max(0.0F, var17 - var16);
      this.сЬ = Math.clamp(this.сЬ, -var18, 0.0F);
      this.field_1.updateTarget(this.сЬ);
      float var19 = this.field_1.getValue();
      UiDrawing.setScissor(var14 - 2.0F, var15, ВЫщ, var16);
      float var20 = var19;
      var20 = this.Ю_f67ab10a(ThemePresetCatalog.ThemeMode.SOLID, var14, var15 + var20, var2, var3);
      var20 = this.Ю_f67ab10a(ThemePresetCatalog.ThemeMode.DUAL, var14, var20 + ВЫГ, var2, var3);
      UiDrawing.disableScissor();
   }

   public void л() {
      this.сЬ = 0.0F;
      this.field_1.setImmediately(0.0F);

      for (FloatAnimation var2 : this.field_4.values()) {
         var2.setImmediately(0.0F);
      }
   }

   private void Ю(ThemePresetCatalog.ThemePreset var1, float var2, float var3) {
      float var4 = var2 + ВЫм;
      float var5 = var3 + ВЫК;
      if (var1.isRainbow()) {
         UiDrawing.drawRoundedRect(
            var4,
            var5,
            ВЫI,
            ВЫТ,
            ВЫО,
            new Vector4i(ThemePresetCatalog.getRainbowColor(0.0F, 255), ThemePresetCatalog.getRainbowColor(ВЫФ, 255), ThemePresetCatalog.getRainbowColor(ВЫЙ, 255), ThemePresetCatalog.getRainbowColor(ВЫк, 255))
         );
      } else {
         UiDrawing.drawRoundedRect(var4, var5, ВЫг, ВЫ0, ВЫх, var1.getPrimaryColor());
         if (var1.getMode() == ThemePresetCatalog.ThemeMode.DUAL) {
            UiDrawing.drawRoundedRect(var4 + ВЫУ, var5 + ВЫЗ, ВЫЕ, ВЫП, ВЫ西, var1.getSecondaryColor());
         }
      }
   }

   private boolean Ю_bbeebd87(ThemePresetCatalog.ThemeMode var1, float var2, float var3, float var4, float var5) {
      List<ThemePresetCatalog.ThemePreset> var6 = ThemePresetCatalog.getPresetsByMode(var1);
      float var7 = var3 + ВlЛ;
      int var8 = 0;

      for (ThemePresetCatalog.ThemePreset var10 : var6) {
         int var11 = var8 % 3;
         int var12 = var8 / 3;
         float var13 = var2 + var11 * Вl诶;
         float var14 = var7 + var12 * Вl1;
         if (WorldProjection.containsPoint(var4, var5, var13, var14, ВlЬ, Вl衣)) {
            this.Я.applyPresetWithWave(var10, var4, var5);
            this.Я.setPresetPickerOpen(false);
            return true;
         }

         var8++;
      }

      return false;
   }

   private String к() {
      ColorTheme var1 = this.Я.getThemeRegistry().getActiveTheme();
      return var1 == null ? "" : var1.getName();
   }

   public ThemePresetPicker(ThemeEditor var1) {
      this.field_1 = new FloatAnimation(0.0F, ВЫе);
      this.field_4 = new HashMap<>();
      this.Я = var1;
   }

   public boolean Ю(float var1, float var2, int var3) {
      float var4 = this.Я.getGuiScreen().getGuiX();
      float var5 = this.Я.getGuiScreen().getGuiY();
      float var6 = var4 + this.Я.getGuiScreen().getGuiWidth() / 2.0F - ВЫц;
      float var7 = var5 + this.Я.getGuiScreen().getGuiHeight() / 2.0F - ВЫя;
      if (WorldProjection.containsPoint(var1, var2, var6 + Вlс - ВlВ, var7 + ВlЯ, ВlХ, Вlе)) {
         this.Я.setPresetPickerOpen(false);
         return true;
      }

      float var8 = var6 + ВlА;
      float var9 = var7 + Вl4;
      float var10 = Вlп;
      float var11 = this.field_1.getValue();
      if (!WorldProjection.containsPoint(var1, var2, var8 - 2.0F, var9, ВlД, var10)) {
         if (!WorldProjection.containsPoint(var1, var2, var6, var7, ВlЪ, Вlт)) {
            this.Я.setPresetPickerOpen(false);
         }

         return true;
      } else {
         float var12 = var9 + var11;
         if (this.Ю_bbeebd87(ThemePresetCatalog.ThemeMode.SOLID, var8, var12, var1, var2)) {
            return true;
         }

         List var13 = ThemePresetCatalog.getPresetsByMode(ThemePresetCatalog.ThemeMode.SOLID);
         int var14 = (var13.size() + 3 - 1) / 3;
         var12 += Вlа + var14 * ВlЮ + ВlЧ;
         this.Ю_bbeebd87(ThemePresetCatalog.ThemeMode.DUAL, var8, var12, var1, var2);
         return true;
      }
   }

   private float Т() {
      float var1 = 0.0F;

      for (ThemePresetCatalog.ThemeMode var5 : ThemePresetCatalog.ThemeMode.values()) {
         List var6 = ThemePresetCatalog.getPresetsByMode(var5);
         int var7 = (var6.size() + 3 - 1) / 3;
         var1 += ВЫд + var7 * ВЫБ + ВЫС;
      }

      return Math.max(0.0F, var1 - ВЫз);
   }

   public boolean Ю(float var1, float var2, double var3) {
      float var5 = this.Я.getGuiScreen().getGuiX();
      float var6 = this.Я.getGuiScreen().getGuiY();
      float var7 = var5 + this.Я.getGuiScreen().getGuiWidth() / 2.0F - Вlш;
      float var8 = var6 + this.Я.getGuiScreen().getGuiHeight() / 2.0F - ВlИ;
      if (!WorldProjection.containsPoint(var1, var2, var7, var8, Вlь, Вlж)) {
         return true;
      } else {
         float var9 = Вl弟;
         float var10 = Math.max(0.0F, this.Т() - var9);
         if (var10 <= 0.0F) {
            this.сЬ = 0.0F;
            return true;
         } else {
            this.сЬ = this.сЬ + (float)var3 * Вlб;
            this.сЬ = Math.clamp(this.сЬ, -var10, 0.0F);
            return true;
         }
      }
   }

   private float Ю_f67ab10a(ThemePresetCatalog.ThemeMode var1, float var2, float var3, float var4, float var5) {
      List<ThemePresetCatalog.ThemePreset> var6 = ThemePresetCatalog.getPresetsByMode(var1);
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(var1.getDisplayName()), var2, var3, 13, ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT));
      float var7 = var3 + ВЫл;
      int var8 = 0;
      String var9 = this.к();

      for (ThemePresetCatalog.ThemePreset var11 : var6) {
         int var12 = var8 % 3;
         int var13 = var8 / 3;
         float var14 = var2 + var12 * ВЫэ;
         float var15 = var7 + var13 * ВЫю;
         boolean var16 = WorldProjection.containsPoint(var4, var5, var14, var15, ВЫЩ, ВЫу);
         boolean var17 = var9.equalsIgnoreCase(var11.getName());
         FloatAnimation var18 = this.Ю(var11.getName());
         var18.updateTarget(var16 ? 1.0F : 0.0F);
         float var19 = var18.getValue();
         int var20 = (int)(ВЫ2 + ВЫф * var19 + (var17 ? 10 : 0));
         UiDrawing.drawRoundedRect(var14, var15, ВЫи, ВЫ9, ВЫо, ColorMath.packArgb(255, 255, 255, var20));
         if (var17 || var19 > ВЫO) {
            int var21 = var11.isRainbow() ? ThemePresetCatalog.getRainbowColor(ВЫ7, 255) : var11.getPrimaryColor();
            float var22 = var17 ? ВЫР : ВЫ3 * var19;
            UiDrawing.drawOutline(var14, var15, ВЫр, ВЫ6, 1.0F, ВЫ_, ColorMath.withAlpha(var21, var22));
         }

         this.Ю(var11, var14, var15);
         UiDrawing.drawText(
            ClientFonts.getMediumFont(),
            ClientLocalization.translate(var11.getName()),
            var14 + ВЫШ,
            var15 + ВЫы,
            11,
            !var16 && !var17 ? ThemeEditor.getColorArgb(ThemeColorSlot.TEXT) : ColorMath.packArgb(255, 255, 255, ВЫй)
         );
         var8++;
      }

      int var23 = (var6.size() + 3 - 1) / 3;
      return var7 + var23 * ВЫЖ;
   }
}
