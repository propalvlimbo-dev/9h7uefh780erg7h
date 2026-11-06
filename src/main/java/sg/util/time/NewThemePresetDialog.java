package sg.util.time;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import settings.render.ClientLocalization;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;

public class NewThemePresetDialog {
   private final ThemeEditor Х;
   private String сЕ = "";
   private final boolean В_ = true;
   private static final float ВФХ = 15.0F;
   private static final float ВФе = 20.0F;
   private static final double ВФА = 255.0;
   private static final float ВФ4 = 300.0F;
   private static final float ВФп = 100.0F;
   private static final float ВФД = 10.0F;
   private static final float ВФЪ = 300.0F;
   private static final float ВФт = 100.0F;
   private static final float ВФа = 10.0F;
   private static final String ВФЮ = "s";
   private static final float ВФЧ = 300.0F;
   private static final float ВФЛ = 30.0F;
   private static final float ВФ诶 = 10.0F;
   private static final float ВФ1 = 10.0F;
   private static final String ВФЬ = "Создайте свой пресет";
   private static final float ВФ衣 = 12.0F;
   private static final float ВФш = 12.0F;
   private static final double ВФИ = 255.0;
   private static final float ВФь = 10.0F;
   private static final float ВФж = 50.0F;
   private static final float ВФ弟 = 230.0F;
   private static final float ВФб = 40.0F;
   private static final float ВФЫ = 10.0F;
   private static final float ВФl = 10.0F;
   private static final float ВФ8 = 50.0F;
   private static final float ВФ5 = 230.0F;
   private static final float ВФв = 40.0F;
   private static final float ВФъ = 10.0F;
   private static final float ВФМ = 300.0F;
   private static final float ВФН = 50.0F;
   private static final float ВФн = 50.0F;
   private static final float ВФЦ = 40.0F;
   private static final float ВФч = 40.0F;
   private static final float ВФЭ = 10.0F;
   private static final float ВФ必 = 300.0F;
   private static final float ВФщ = 50.0F;
   private static final float ВФГ = 50.0F;
   private static final float ВФл = 40.0F;
   private static final float ВФэ = 40.0F;
   private static final float ВФю = 10.0F;
   private static final String ВФЩ = "{";
   private static final float ВФу = 22.0F;
   private static final float ВФ2 = 65.0F;
   private static final long ВФф = 500L;
   private static final long ВФи = 2L;
   private static final String ВФ9 = "|";
   private static final float ВФо = 40.0F;
   private static final float ВФO = 63.0F;
   private static final String ВФ7 = "r";
   private static final float ВФР = 300.0F;
   private static final float ВФ3 = 50.0F;
   private static final float ВФр = 13.0F;
   private static final float ВФ6 = 65.0F;
   private static final float ВФ_ = 400.0F;
   private static final float ВФШ = 150.0F;
   private static final float ВФы = 252.0F;
   private static final float ВФй = 75.0F;
   private static final float ВФЖ = 300.0F;
   private static final float ВФм = 30.0F;
   private static final double ВФК = 30.0;
   private static final double ВФI = 30.0;
   private static final float ВФТ = 300.0F;
   private static final float ВФО = 50.0F;
   private static final float ВФФ = 50.0F;
   private static final double ВФЙ = 40.0;
   private static final double ВФк = 40.0;

   public boolean г(float var1, float var2, int var3) {
      float var4 = this.Х.getGuiScreen().getGuiX();
      float var5 = this.Х.getGuiScreen().getGuiY();
      float var6 = var4 + ВФ_ - ВФШ;
      float var7 = var5 + ВФы - ВФй;
      if (WorldProjection.containsPoint(var1, var2, var6 + ВФЖ - ВФм, var7, ВФК, ВФI)) {
         this.Х.setNewPresetDialogOpen(false);
         this.сЕ = "";
         return true;
      }

      if (WorldProjection.containsPoint(var1, var2, var6 + ВФТ - ВФО, var7 + ВФФ, ВФЙ, ВФк)) {
         if (!this.сЕ.trim().isEmpty()) {
            this.Х.createUserTheme(this.сЕ.trim());
            this.сЕ = "";
            this.Х.setNewPresetDialogOpen(false);
         }

         return true;
      } else {
         return true;
      }
   }

   public NewThemePresetDialog(ThemeEditor var1) {
      this.Х = var1;
   }

   public void Ь(GuiGraphicsExtractor var1, float var2, float var3, float var4, float var5, float var6) {
      UiDrawing.drawBlur(
         this.Х.getGuiScreen().getGuiX(),
         this.Х.getGuiScreen().getGuiY(),
         this.Х.getGuiScreen().getGuiWidth(),
         this.Х.getGuiScreen().getGuiHeight(),
         ВФХ,
         ВФе,
         ColorMath.packArgb(255, 255, 255, ВФА)
      );
      UiDrawing.drawRoundedRect(var5, var6, ВФ4, ВФп, ВФД, ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND));
      UiDrawing.drawOutline(var5, var6, ВФЪ, ВФт, 1.0F, ВФа, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawText(ClientFonts.getIconsFont(), ВФЮ, var5 + ВФЧ - ВФЛ + ВФ诶, var6 + ВФ1, 10, ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT));
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(ВФЬ), var5 + ВФ衣, var6 + ВФш, 14, ColorMath.packArgb(255, 255, 255, ВФИ));
      UiDrawing.drawRoundedRect(var5 + ВФь, var6 + ВФж, ВФ弟, ВФб, ВФЫ, ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND));
      UiDrawing.drawOutline(var5 + ВФl, var6 + ВФ8, ВФ5, ВФв, 1.0F, ВФъ, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawRoundedRect(var5 + ВФМ - ВФН, var6 + ВФн, ВФЦ, ВФч, ВФЭ, ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND));
      UiDrawing.drawOutline(var5 + ВФ必 - ВФщ, var6 + ВФГ, ВФл, ВФэ, 1.0F, ВФю, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawText(ClientFonts.getIconsFont(), ВФЩ, var5 + ВФу, var6 + ВФ2, 10, ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT));
      String var7 = this.сЕ + (System.currentTimeMillis() / ВФф % ВФи == 0L ? ВФ9 : "");
      UiDrawing.drawText(
         ClientFonts.getMediumFont(),
         var7,
         var5 + ВФо,
         var6 + ВФO,
         12,
         this.сЕ.isEmpty() ? ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT) : ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT)
      );
      UiDrawing.drawText(ClientFonts.getIconsFont(), ВФ7, var5 + ВФР - ВФ3 + ВФр, var6 + ВФ6, 12, ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT));
   }

   public boolean т(String var1) {
      if (this.сЕ.codePointCount(0, this.сЕ.length()) >= 32) {
         return true;
      }

      char var2 = var1.charAt(0);
      if (Character.isISOControl(var2)) {
         return false;
      }

      this.сЕ = this.сЕ + var1;
      return true;
   }

   public boolean Ь(KeyEvent var1) {
      switch (var1.key()) {
         case 257:
         case 335:
            if (!this.сЕ.trim().isEmpty()) {
               this.Х.createUserTheme(this.сЕ.trim());
               this.сЕ = "";
               this.Х.setNewPresetDialogOpen(false);
            }

            return true;
         case 259:
            if (!this.сЕ.isEmpty()) {
               this.сЕ = this.сЕ.substring(0, this.сЕ.offsetByCodePoints(this.сЕ.length(), -1));
            }

            return true;
         default:
            return false;
      }
   }
}
