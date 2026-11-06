package sg.gui.autobuy;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import settings.render.ClientLocalization;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.impl.autobuy.FunTime;
import sg.gui.click.ClickGuiScreen;
import sg.gui.ClickGuiPanelHost;

public final class AutoBuyPanel {
   private static final float Хм = 74.0F;
   private static final float ХК = 8.0F;
   private static final float ХI = 11.0F;
   private static final float ХТ = 48.0F;
   private static final float ХО = 42.0F;
   private static final float ХФ = 6.0F;
   private static final float ХЙ = 54.0F;
   private static final float Хк = 7.0F;
   private static final float Хг = 92.0F;
   private static final float Х0 = 25.0F;
   private static AutoBuyPanel Я_ec501b46;
   private static boolean Въ;
   private static String с7;
   private static String сР;
   private static float Хх;
   private static float ХУ;
   private static float ХЗ;
   private static float ХЕ;
   @SerializedName("Я")
   private final ClickGuiPanelHost Я_a763ee07;
   @SerializedName("г")
   private final FloatAnimation г_04d91340;
   private final Map<FunTime.PurchaseEntry, FloatAnimation> Ф;
   private final Map<FunTime.PurchaseEntry, FloatAnimation> Й;
   @SerializedName("к")
   private final Map<FunTime.PurchaseEntry, FloatAnimation> к_ea411899;
   @SerializedName("г")
   private final Map<FunTime.PurchaseEntry, FloatAnimation> г_ea411899;
   @SerializedName("к")
   private final List<FunTime.PurchaseEntry> к_2571477d;
   private long сБ;
   private float ХП;
   private float Х西;
   private float Хд;
   private float ХБ;
   private FunTime.PurchaseEntry В;
   @SerializedName("Я")
   private FunTime.PurchaseEntry Я_47df95dc;
   private String с3;
   private static final float оп = 8.0F;
   private static final float оД = 20.0F;
   private static final float оЪ = 8.0F;
   private static final float от = 270.0F;
   private static final float оа = 602.0F;
   private static final float оЮ = 8.0F;
   private static final float оЧ = 24.0F;
   private static final float оЛ = 6.0F;
   private static final float о诶 = 48.0F;
   private static final float о1 = 10.0F;
   private static final float оЬ = 74.0F;
   private static final float о衣 = 8.0F;
   private static final float ош = 48.0F;
   private static final float оИ = 48.0F;
   private static final float оь = 10.0F;
   private static final float ож = 92.0F;
   private static final float о弟 = 12.0F;
   private static final float об = 10.0F;
   private static final long оЫ = 150L;
   private static final String оl = "Каталог";
   private static final String о8 = "Все доступные предметы";
   private static final String о5 = "К покупке · ";
   private static final String ов = "Выбранные предметы";
   private static final String оъ = "Ожидает парсер · ПКМ удалить";
   private static final String оМ = " · ПКМ удалить";
   private static final String оН = "Уже добавлен";
   private static final String он = "ЛКМ — добавить";
   private static final float оЦ = 10.0F;
   private static final float оч = 0.015F;
   private static final float оЭ = 0.18F;
   private static final float о必 = 0.18F;
   private static final float ощ = 255.0F;
   private static final float оГ = 16.0F;
   private static final float ол = 0.38F;
   private static final float оэ = 20.0F;
   private static final float ою = 11.0F;
   private static final float оЩ = 0.22F;
   private static final float оу = 11.0F;
   private static final float о2 = 0.18F;
   private static final float оф = 0.3F;
   private static final float ои = 13.0F;
   private static final float о9 = 11.0F;
   private static final float оо = 18.0F;
   private static final float оO = 7.0F;
   private static final float о7 = 0.25F;
   private static final float оР = 13.0F;
   private static final float о3 = 11.0F;
   private static final float ор = 18.0F;
   private static final float о6 = 0.86F;
   private static final float о_ = 22.0F;
   private static final float оШ = 9.0F;
   private static final float оы = 0.94F;
   private static final float ой = 22.0F;
   private static final float оЖ = 27.0F;
   private static final float ом = 0.72F;
   private static final float оК = 22.0F;
   private static final float оI = 12.0F;
   private static final float оТ = 0.5F;
   private static final float оО = 12.0F;
   private static final float оФ = 14.0F;
   private static final float оЙ = 0.88F;
   private static final float ок = 12.0F;
   private static final float ог = 40.0F;
   private static final float о0 = 24.0F;
   private static final float ох = 0.12F;
   private static final double оУ = 92.0;
   private static final double оЗ = 25.0;
   private static final float оЕ = 92.0F;
   private static final float оП = 25.0F;
   private static final float о西 = 7.0F;
   private static final float од = 0.52F;
   private static final float оБ = 0.12F;
   private static final String оС = "AutoBuy: ВКЛ";
   private static final String оз = "AutoBuy: ВЫКЛ";
   private static final float оц = 46.0F;
   private static final float оя = 8.0F;
   private static final float Oс = 0.5F;
   private static final float OВ = 0.94F;
   private static final float OЯ = 42.0F;
   private static final float OХ = 6.0F;
   private static final float Oе = 0.5F;
   private static final float OА = 5.0F;
   private static final float O4 = 10.0F;
   private static final float Oп = 48.0F;
   private static final float OД = 48.0F;
   private static final float OЪ = 42.0F;
   private static final double Oт = 42.0;
   private static final double Oа = 42.0;
   private static final float OЮ = 42.0F;
   private static final float OЧ = 42.0F;
   private static final float OЛ = 14.0F;
   private static final float O诶 = 11.0F;
   private static final float O1 = 0.45F;
   private static final float OЬ = 0.32F;
   private static final float O衣 = 0.42F;
   private static final float Oш = 0.42F;
   private static final float OИ = 0.3F;
   private static final float Oь = 0.18F;
   private static final float Oж = 0.06F;
   private static final float O弟 = 1.2F;
   private static final float Oб = 0.8F;
   private static final float OЫ = 0.01F;
   private static final float Ol = 42.0F;
   private static final float O8 = 42.0F;
   private static final float O5 = 8.0F;
   private static final float Oв = 0.26F;
   private static final float Oъ = 42.0F;
   private static final float OМ = 42.0F;
   private static final float OН = 8.0F;
   private static final float Oн = 25.0F;
   private static final float OЦ = 2.5F;
   private static final float Oч = 1.5F;
   private static final float OЭ = 42.0F;
   private static final float O必 = 0.5F;
   private static final float Oщ = 42.0F;
   private static final float OГ = 0.5F;
   private static final float Oл = 0.72F;
   private static final float Oэ = 0.28F;
   private static final double Oю = 260.0;
   private static final float OЩ = 42.0F;
   private static final float Oу = 9.0F;
   private static final float O2 = 4.0F;
   private static final float Oф = 5.0F;
   private static final float Oи = 5.0F;
   private static final float O9 = 2.5F;
   private static final float Oо = 5.0F;
   private static final float OO = 10.0F;
   private static final float O7 = 20.0F;
   private static final float OР = 5.0F;
   private static final float O3 = 10.0F;
   private static final float Oр = 61.0F;
   private static final float O6 = 54.0F;
   private static final float O_ = 0.01F;
   private static final double OШ = 54.0;
   private static final float Oы = 54.0F;
   private static final float Oй = 14.0F;
   private static final float OЖ = 18.0F;
   private static final float Oм = 1.5F;
   private static final float OК = 8.0F;
   private static final float OI = 0.78F;
   private static final float OТ = 0.34F;
   private static final float OО = 0.46F;
   private static final float OФ = 0.46F;
   private static final float OЙ = 0.31F;
   private static final float Oк = 0.17F;
   private static final float Oг = 0.01F;
   private static final float O0 = 0.01F;
   private static final float Oх = 54.0F;
   private static final float OУ = 10.0F;
   private static final float OЗ = 0.16F;
   private static final float OЕ = 0.2F;
   private static final float OП = 54.0F;
   private static final float O西 = 9.0F;
   private static final float Oд = 32.0F;
   private static final float OБ = 10.0F;
   private static final float OС = 54.0F;
   private static final float Oз = 0.5F;
   private static final float Oц = 120.0F;
   private static final float Oя = 52.0F;
   private static final float field_7с = 10.0F;
   private static final float field_7В = 0.94F;
   private static final float field_7Я = 52.0F;
   private static final float field_7Х = 27.0F;
   private static final float field_7е = 84.0F;
   private static final float field_7А = 20.0F;
   private static final float field_74 = 5.0F;
   private static final float field_7п = 0.62F;
   private static final float field_7Д = 0.62F;
   private static final long field_7Ъ = 900L;
   private static final long field_7т = 450L;
   private static final String field_7а = "|";
   private static final String field_7Ю = "Цена: ";
   private static final float field_7Ч = 7.0F;
   private static final float field_7Л = 5.0F;
   private static final float field_7诶 = 0.96F;
   private static final String field_71 = "Ожидает парсер";
   private static final float field_7Ь = 52.0F;
   private static final float field_7衣 = 31.0F;
   private static final String field_7ш = "×";
   private static final float field_7И = 20.0F;
   private static final float field_7ь = 18.0F;
   private static final float field_7ж = 0.55F;
   private static final float field_7弟 = 0.45F;
   private static final float field_7б = 5.0F;
   private static final float field_7Ы = 0.5F;
   private static final float field_7l = 0.5F;
   private static final float field_78 = 20.0F;
   private static final float field_75 = 40.0F;
   private static final float field_7в = 40.0F;
   private static final float field_7ъ = 40.0F;
   private static final float field_7М = 12.0F;
   private static final float field_7Н = 0.12F;
   private static final float field_7н = 0.06F;
   private static final String field_7Ц = "+";
   private static final float field_7ч = 31.0F;
   private static final float field_7Э = 0.86F;
   private static final String field_7必 = "Список покупок пуст";
   private static final float field_7щ = 12.0F;
   private static final float field_7Г = 0.9F;
   private static final String field_7л = "Нажми на предмет в каталоге";
   private static final float field_7э = 30.0F;
   private static final float field_7ю = 0.68F;
   private static final float field_7Щ = 0.66F;
   private static final float field_7у = 0.34F;
   private static final double field_72 = 420.0;
   private static final float field_7ф = 28.0F;
   private static final float field_7и = 0.48F;
   private static final float field_79 = 48.0F;
   private static final float field_7о = 6.0F;
   private static final float field_7O = 61.0F;
   private static final float field_77 = 7.0F;
   private static final float field_7Р = 16.0F;
   private static final float field_73 = 0.5F;
   private static final float field_7р = 10.0F;
   private static final float field_76 = 10.0F;
   private static final float field_7_ = 24.0F;
   private static final float field_7Ш = 42.0F;
   private static final float field_7ы = 14.0F;
   private static final float field_7й = 14.0F;
   private static final float field_7Ж = 10.0F;
   private static final float field_7м = 255.0F;
   private static final float field_7К = 0.32F;
   private static final float field_7I = 22.0F;
   private static final float field_7Т = 8.0F;
   private static final float field_7О = 0.2F;
   private static final float field_7Ф = 0.45F;
   private static final float field_7Й = 0.55F;
   private static final float field_7к = 0.45F;
   private static final float field_7г = 255.0F;
   private static final float field_70 = 8.0F;
   private static final float field_7х = 0.86F;
   private static final float field_7У = 12.0F;
   private static final float field_7З = 8.0F;
   private static final float field_7Е = 255.0F;
   private static final float field_7П = 0.94F;
   private static final float field_7西 = 12.0F;
   private static final float field_7д = 24.0F;
   private static final float field_7Б = 255.0F;
   private static final float field_7С = 0.76F;
   private static final double field_7з = 92.0;
   private static final double field_7ц = 25.0;
   private static final float field_7я = 42.0F;
   private static final float Рс = 6.0F;
   private static final float РВ = 0.5F;
   private static final float РЯ = 48.0F;
   private static final float РХ = 48.0F;
   private static final double Ре = 42.0;
   private static final double РА = 42.0;
   private static final float Р4 = 42.0F;
   private static final float Рп = 42.0F;
   private static final float РД = 10.0F;
   private static final float РЪ = 20.0F;
   private static final float Рт = 61.0F;
   private static final double Ра = 54.0;
   private static final float РЮ = 54.0F;
   private static final long РЧ = 1000000000L;
   private static final float РЛ = 42.0F;
   private static final float Р诶 = 42.0F;
   private static final float Р1 = 0.5F;
   private static final float РЬ = 10.0F;

   private void В必() {
      if (this.Я_47df95dc != null) {
         try {
            long var1 = this.с3.isBlank() ? 1L : Long.parseLong(this.с3);
            this.Я_47df95dc.setting().setClampedValue((float)Math.clamp(var1, 1L, РЧ));
         } catch (NumberFormatException var3) {
         }

         this.Я_47df95dc = null;
         this.с3 = "";
      }
   }

   private float Л(Map<FunTime.PurchaseEntry, FloatAnimation> var1, FunTime.PurchaseEntry var2, boolean var3, float var4) {
      FloatAnimation var5 = var1.computeIfAbsent(var2, var2x -> new FloatAnimation(var3 ? 1.0F : 0.0F, var4, Easings.CUBIC_OUT));
      var5.updateTarget(var3 ? 1.0F : 0.0F);
      return Mth.clamp(var5.getValue(), 0.0F, 1.0F);
   }

   private void Л(String var1, float var2, float var3, int var4, int var5) {
      UiDrawing.drawText(ClientFonts.getMediumFont(), var1, var2 - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, var4) * field_73, var3, var4, var5);
   }

   private void 诶(List<FunTime.PurchaseEntry> var1) {
      for (FunTime.PurchaseEntry var3 : var1) {
         if (var3.enabled() && !this.к_2571477d.contains(var3)) {
            this.к_2571477d.add(var3);
            this.г_ea411899.put(var3, new FloatAnimation(0.0F, оЦ, Easings.CUBIC_OUT));
         }
      }

      Iterator var5 = this.к_2571477d.iterator();

      while (var5.hasNext()) {
         FunTime.PurchaseEntry var6 = (FunTime.PurchaseEntry)var5.next();
         FloatAnimation var4 = this.г_ea411899.computeIfAbsent(var6, var1x -> new FloatAnimation(var6.enabled() ? 1.0F : 0.0F, РЬ, Easings.CUBIC_OUT));
         var4.updateTarget(var6.enabled() ? 1.0F : 0.0F);
         if (!var6.enabled() && var4.getValue() < оч) {
            var5.remove();
            this.г_ea411899.remove(var6);
            this.к_ea411899.remove(var6);
         }
      }

      if (this.В != null && !this.В.enabled()) {
         this.В = null;
      }

      if (this.Я_47df95dc != null && !this.Я_47df95dc.enabled()) {
         this.В0();
      }
   }

   private float Лк() {
      return this.Лх() + this.ЛЧ() - ож - о弟;
   }

   public static void Вй() {
      Вй((Integer)null);
   }

   public static void Вй(Integer backgroundColor) {
      if (Въ && с7 != null) {
         float var0 = Math.max(UiDrawing.getTextWidth(ClientFonts.getMediumFont(), с7, 12), UiDrawing.getTextWidth(ClientFonts.getMediumFont(), сР, 10)) + field_7_;
         float var1 = field_7Ш;
         float var2 = Math.min(Хх + field_7ы, ХЗ - var0);
         float var3 = Math.min(ХУ + field_7й, ХЕ - var1);
         UiDrawing.drawShadow(
            var2, var3, var0, var1, field_7Ж, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW), ThemeEditor.getColorAlpha(ThemeColorSlot.SHADOW) / field_7м * field_7К)
         );
         UiDrawing.drawBlur(
            var2,
            var3,
            var0,
            var1,
            field_7I,
            field_7Т,
            ColorMath.packArgb(255, 255, 255, Math.min(60, (int)(ThemeEditor.getColorAlpha(ThemeColorSlot.BACKGROUND) * field_7О)))
         );
         int var4 = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND), ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_STATE), field_7Ф);
         float var5 = (ThemeEditor.getColorAlpha(ThemeColorSlot.BACKGROUND) * field_7Й + ThemeEditor.getColorAlpha(ThemeColorSlot.DEACTIVE_STATE) * field_7к) / field_7г;
         UiDrawing.drawRoundedRect(var2, var3, var0, var1, field_70, backgroundColor != null ? backgroundColor : ColorMath.withAlpha(var4, var5 * field_7х));
         UiDrawing.drawText(
            ClientFonts.getMediumFont(),
            с7,
            var2 + field_7У,
            var3 + field_7З,
            12,
            ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.TEXT) / field_7Е * field_7П)
         );
         UiDrawing.drawText(
            ClientFonts.getMediumFont(),
            сР,
            var2 + field_7西,
            var3 + field_7д,
            10,
            ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SUR_TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.SUR_TEXT) / field_7Б * field_7С)
         );
         Въ = false;
         Я_ec501b46 = null;
      }
   }

   private FunTime.PurchaseEntry Л_47df95dc(float var1, float var2) {
      if (!诶(var1, var2, this.Лх(), this.Лв(), this.ЛЧ(), this.Лэ())) {
         return null;
      }

      float var3 = this.Лх() + РД;
      float var4 = this.ЛЧ() - РЪ;
      float var5 = this.Лв() + this.Хд;
      float var6 = 0.0F;

      for (int var7 = 0; var7 < this.к_2571477d.size(); var7++) {
         FunTime.PurchaseEntry var8 = this.к_2571477d.get(var7);
         float var9 = this.г_ea411899.get(var8) == null ? 1.0F : this.г_ea411899.get(var8).getValue();
         float var10 = var5 + var6;
         var6 += Рт * var9;
         if (var8.enabled() && 诶(var1, var2, var3, var10, var4, Ра) && !this.诶(var3, var10, var4, РЮ)) {
            return var8;
         }
      }

      return null;
   }

   public boolean х(float var1, float var2, int var3) {
      FunTime var4 = this.诶();
      if (var4 == null) {
         return false;
      }

      if (var3 == 0 && 诶(var1, var2, this.Лк(), this.ЛО(), field_7з, field_7ц)) {
         if (var4.诶9()) {
            var4.method_9Р(false);
         } else {
            if (!var4.isEnabled()) {
               var4.enable();
            }

            var4.method_9Р(true);
         }

         return true;
      } else {
         List var5 = var4.Ж_2571477d();
         FunTime.PurchaseEntry var6 = this.诶(var5, var1, var2);
         if (var6 != null && var3 == 0) {
            this.В必();
            this.В = var6;
            if (!var6.enabled()) {
               var6.setting().setClampedValue(1.0F);
            }

            return true;
         } else {
            FunTime.PurchaseEntry var7 = this.Л_47df95dc(var1, var2);
            if (var7 != null) {
               if (var3 == 1) {
                  if (this.Я_47df95dc == var7) {
                     this.В0();
                  }

                  var7.setting().setClampedValue(0.0F);
                  if (this.В == var7) {
                     this.В = null;
                  }
               } else if (var3 == 0) {
                  this.В = var7;
                  this.Л(var7);
               }

               return true;
            } else {
               if (var3 == 0) {
                  this.В必();
               }

               return false;
            }
         }
      }
   }

   private void Л(float var1, float var2, String var3, String var4, String var5) {
      float var6 = this.ЛВ();
      UiDrawing.drawShadow(var1 + ои, var6 + о9, 2.0F, оо, оO, this.В(ThemeColorSlot.MAIN, о7));
      UiDrawing.drawRoundedRect(var1 + оР, var6 + о3, 2.0F, ор, 1.0F, this.В(ThemeColorSlot.MAIN, о6));
      UiDrawing.drawText(ClientFonts.getMediumFont(), var3, var1 + о_, var6 + оШ, 13, this.В(ThemeColorSlot.TEXT, оы));
      UiDrawing.drawText(ClientFonts.getMediumFont(), var5, var1 + ой, var6 + оЖ, 8, this.В(ThemeColorSlot.SUR_TEXT, ом));
      if (!var4.isEmpty()) {
         float var7 = Math.max(оК, UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var4, 9) + оI);
         this.Л(var4, var1 + var2 - var7 * оТ - оО, var6 + оФ, 9, this.В(ThemeColorSlot.MAIN, оЙ));
      }

      UiDrawing.drawRoundedRect(var1 + ок, var6 + ог, var2 - о0, 1.0F, 0.0F, this.В(ThemeColorSlot.LINE, ох));
   }

   private int Чг() {
      return Math.max(1, (int)((this.ЧЭ() - оЧ + оЛ) / о诶));
   }

   private static boolean 诶(double var0, double var2, double var4, double var6, double var8, double var10) {
      return var0 >= var4 && var0 <= var4 + var8 && var2 >= var6 && var2 <= var6 + var10;
   }

   private float Лы() {
      return field_7Щ + field_7у * (float)Math.sin(System.currentTimeMillis() / field_72);
   }

   private void Л(String var1, String var2, float var3, float var4) {
      Я_ec501b46 = this;
      Въ = true;
      с7 = var1;
      сР = var2;
      Хх = var3;
      ХУ = var4;
      ХЗ = this.Я_a763ee07.getGuiX() + this.Я_a763ee07.getGuiWidth() - field_7р;
      ХЕ = this.Я_a763ee07.getGuiY() + this.Я_a763ee07.getGuiHeight() - field_76;
   }

   private FunTime.PurchaseEntry 诶(List<FunTime.PurchaseEntry> var1, float var2, float var3) {
      if (!诶(var2, var3, this.ЛЫ(), this.Лв(), this.ЧЭ(), this.Лэ())) {
         return null;
      }

      float var4 = this.Чг() * field_7я + (this.Чг() - 1) * Рс;
      float var5 = this.ЛЫ() + (this.ЧЭ() - var4) * РВ;
      float var6 = this.Лв() + this.ХП;

      for (int var7 = 0; var7 < var1.size(); var7++) {
         float var8 = var5 + var7 % this.Чг() * РЯ;
         float var9 = var6 + var7 / this.Чг() * РХ;
         if (诶(var2, var3, var8, var9, Ре, РА) && !this.诶(var8, var9, Р4, Рп)) {
            return (FunTime.PurchaseEntry)var1.get(var7);
         }
      }

      return null;
   }

   private float Лв() {
      return this.ЛВ() + ош;
   }

   private void В(float var1, float var2, float var3, float var4) {
      UiDrawing.drawShadow(var1, var2, var3, var4, оГ, this.В(ThemeColorSlot.SHADOW, ол));
      UiDrawing.drawBlur(
         var1, var2, var3, var4, оэ, ою, ColorMath.packArgb(255, 255, 255, Math.min(64, (int)(ThemeEditor.getColorAlpha(ThemeColorSlot.BACKGROUND) * оЩ)))
      );
      UiDrawing.drawRoundedRect(var1, var2, var3, var4, оу, this.Л_a83dd0cc(о2, оф));
   }

   private FunTime 诶() {
      return RoseClient.getInstance().getServices().getModules().getFunTime();
   }

   private void ВЮ() {
      float var1 = this.Лх() + this.ЛЧ() * field_7Ы;
      float var2 = this.Лв() + this.Лэ() * field_7l;
      float var3 = this.Лы();
      UiDrawing.drawRoundedRect(var1 - field_78, var2 - field_75, field_7в, field_7ъ, field_7М, this.В(ThemeColorSlot.SELECTED, field_7Н + var3 * field_7н));
      this.Л(field_7Ц, var1, var2 - field_7ч, 18, this.В(ThemeColorSlot.MAIN, field_7Э));
      this.Л(ClientLocalization.translate(field_7必), var1, var2 + field_7щ, 12, this.В(ThemeColorSlot.TEXT, field_7Г));
      this.Л(ClientLocalization.translate(field_7л), var1, var2 + field_7э, 9, this.В(ThemeColorSlot.SUR_TEXT, field_7ю));
   }

   private boolean 诶(float var1, float var2, float var3, float var4) {
      return this.Я_a763ee07.getSearchOverlayAlpha() > Р1 && this.Я_a763ee07.isUnderSearch(var1, var2, var3, var4);
   }

    private float Л_f67ab10a(ThemeColorSlot var1) {
       if (this.Я_a763ee07.usesCustomBackground() && var1 == ThemeColorSlot.BACKGROUND)
          return (this.Я_a763ee07.getPanelBackgroundColor() >>> 24) / 255.0F;
       return Mth.clamp(ThemeEditor.getColorAlpha(var1) / ощ, 0.0F, 1.0F);
   }

   private float к_f67ab10a(int var1) {
      return Math.max(0.0F, var1 * field_7O - (var1 > 0 ? field_77 : 0.0F));
   }

   public static void Л(String var0, String var1, float var2, float var3, float var4, float var5) {
      Я_ec501b46 = null;
      Въ = true;
      с7 = var0;
      сР = var1;
      Хх = var2;
      ХУ = var3;
      ХЗ = var4;
      ХЕ = var5;
   }

   private void Л(FunTime.PurchaseEntry var1) {
      if (this.Я_47df95dc != null && this.Я_47df95dc != var1) {
         this.В必();
      }

      this.Я_47df95dc = var1;
      this.с3 = var1.price() <= 1L ? "" : Long.toString(var1.price());
   }

    private int Л_a83dd0cc(ThemeColorSlot var1) {
       if (var1 == ThemeColorSlot.BACKGROUND) return this.Я_a763ee07.getPanelBackgroundColor();
       return ThemeEditor.getColorArgb(var1);
   }

   private void Ы(float var1, float var2, float var3, float var4) {
      if (!(var3 <= var4)) {
         float var5 = Math.max(field_7ф, var4 * var4 / var3);
         float var6 = Mth.clamp(-var2 / (var3 - var4), 0.0F, 1.0F);
         UiDrawing.drawRoundedRect(var1, this.Лв() + (var4 - var5) * var6, 2.0F, var5, 1.0F, this.В(ThemeColorSlot.SCROLL, field_7и));
      }
   }

   private float ЛВ() {
      return this.Я_a763ee07.getGuiY() + оЬ;
   }

   private float ЛЫ() {
      return this.Л诶();
   }

   private float ЛО() {
      return this.ЛВ() + об;
   }

   private void Л(ItemStack var1, float var2, float var3, float var4) {
      UiDrawing.drawItem(var1, var2, var3, var4 / field_7Р, false);
   }

   public boolean Л(float var1, float var2, float var3) {
      if (诶(var1, var2, this.ЛЫ(), this.Лв(), this.ЧЭ(), this.Лэ())) {
         this.Х西 = this.Х西 + var3 * РЛ;
         return true;
      } else if (诶(var1, var2, this.Лх(), this.Лв(), this.ЛЧ(), this.Лэ())) {
         this.ХБ = this.ХБ + var3 * Р诶;
         return true;
      } else {
         return false;
      }
   }

   private void Л(FunTime var1, float var2, float var3) {
      float var4 = this.Лк();
      float var5 = this.ЛО();
      boolean var6 = 诶(var2, var3, var4, var5, оУ, оЗ);
      float var7 = var1.诶9() ? 1.0F : 0.0F;
      int var8 = ColorMath.lerpArgb(this.Л_a83dd0cc(ThemeColorSlot.DEACTIVE_STATE), this.Л_a83dd0cc(ThemeColorSlot.SELECTED), var7);
      UiDrawing.drawRoundedRect(var4, var5, оЕ, оП, о西, ColorMath.withAlpha(var8, од + (var6 ? оБ : 0.0F)));
      this.Л(var1.诶9() ? ClientLocalization.translate(оС) : ClientLocalization.translate(оз), var4 + оц, var5 + оя, 9, this.В(var7 > Oс ? ThemeColorSlot.TEXT : ThemeColorSlot.SUR_TEXT, OВ));
   }

   private void В0() {
      this.Я_47df95dc = null;
      this.с3 = "";
   }

   public void 诶(GuiGraphicsExtractor var1, float var2, float var3, float var4) {
      FunTime var5 = this.诶();
      if (var5 != null) {
         long var6 = System.currentTimeMillis();
         if (var6 - this.сБ > оЫ) {
            this.г_04d91340.setImmediately(0.0F);
         }

         this.сБ = var6;
         this.г_04d91340.updateTarget(1.0F);
         float var8 = UiDrawing.getGlobalAlpha();
         UiDrawing.setGlobalAlpha(var8 * this.г_04d91340.getValue() * this.Я_a763ee07.getLayoutContentAlpha(0));
         List var9 = var5.Ж_2571477d();
         this.诶(var9);
         this.O(var9.size(), this.к_2571477d.size());
         this.В(this.ЛЫ(), this.ЛВ(), this.ЧЭ(), this.ЧГ());
         this.В(this.Лх(), this.ЛВ(), this.ЛЧ(), this.ЧГ());
         this.Л(this.ЛЫ(), this.ЧЭ(), ClientLocalization.translate(оl), Integer.toString(var9.size()), ClientLocalization.translate(о8));
         this.Л(this.Лх(), this.ЛЧ(), ClientLocalization.translate(о5) + var5.method_9必(), "", ClientLocalization.translate(ов));
         this.Л(var5, var2, var3);
         FunTime.PurchaseEntry var10 = this.Л(var9, var2, var3);
         FunTime.PurchaseEntry var11 = this.Л(this.к_2571477d, var2, var3, var5);
         if (var11 != null) {
            String var12 = var11.price() <= 1L ? ClientLocalization.translate(оъ) : var5.Р(var11.price()) + ClientLocalization.translate(оМ);
            this.Л(ClientLocalization.translate(var11.name()), var12, var2, var3);
         } else if (var10 != null) {
            String var13 = var10.enabled() ? ClientLocalization.translate(оН) : ClientLocalization.translate(он);
            this.Л(ClientLocalization.translate(var10.name()), var13, var2, var3);
         } else if (Я_ec501b46 == this) {
            Я_ec501b46 = null;
            Въ = false;
         }

         UiDrawing.setGlobalAlpha(var8);
      }
   }

   private float Л(ThemeColorSlot var1, ThemeColorSlot var2, float var3) {
      float var4 = Mth.clamp(var3, 0.0F, 1.0F);
      return this.Л_f67ab10a(var1) + (this.Л_f67ab10a(var2) - this.Л_f67ab10a(var1)) * var4;
   }

   private float ЧЦ() {
      return this.Я_a763ee07.getGuiWidth() - this.Я_a763ee07.getSidebarWidth() - оД;
   }

   private String Л(String var1, float var2, int var3) {
      if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, var3) <= var2) {
         return var1;
      }

      String var4 = var1;

      while (var4.length() > 1 && UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var4 + "...", var3) > var2) {
         var4 = var4.substring(0, var4.length() - 1);
      }

      return var4 + "...";
   }

   private FunTime.PurchaseEntry Л(List<FunTime.PurchaseEntry> var1, float var2, float var3, FunTime var4) {
      float var5 = this.Лх() + OO;
      float var6 = this.ЛЧ() - O7;
      float var7 = this.Лв() + this.Хд;
      float var8 = this.Лв() + this.Лэ();
      FunTime.PurchaseEntry var9 = null;
      UiDrawing.pushScissor(this.Лх() + OР, this.Лв(), this.ЛЧ() - O3, this.Лэ());
      if (var1.isEmpty()) {
         this.ВЮ();
      }

      float var10 = 0.0F;

      for (int var11 = 0; var11 < var1.size(); var11++) {
         FunTime.PurchaseEntry var12 = (FunTime.PurchaseEntry)var1.get(var11);
         float var13 = this.г_ea411899.get(var12) == null ? 1.0F : this.г_ea411899.get(var12).getValue();
         float var14 = var7 + var10;
         var10 += Oр * var13;
         if (!(var14 + O6 < this.Лв()) && !(var14 > var8) && !(var13 <= O_)) {
            boolean var15 = var12.enabled();
            boolean var16 = var15
               && 诶(var2, var3, var5, var14, var6, OШ)
               && 诶(var2, var3, this.Лх(), this.Лв(), this.ЛЧ(), this.Лэ())
               && !this.诶(var5, var14, var6, Oы);
            float var17 = this.Л(this.к_ea411899, var12, var16, Oй);
            float var18 = var12 == this.В ? 1.0F : 0.0F;
            float var19 = var5 + (1.0F - var13) * OЖ - var17 * Oм;
            float var20 = var6 - (1.0F - var13) * OК;
            float var21 = var13;
            float var22 = Math.max(var18 * OI, var17 * OТ);
            int var23 = ColorMath.lerpArgb(
               ColorMath.lerpArgb(this.Л_a83dd0cc(ThemeColorSlot.BACKGROUND), this.Л_a83dd0cc(ThemeColorSlot.DEACTIVE_STATE), OО),
               this.Л_a83dd0cc(ThemeColorSlot.SELECTED),
               var22
            );
            float var24 = this.Л(ThemeColorSlot.BACKGROUND, ThemeColorSlot.DEACTIVE_STATE, OФ);
            var24 += (this.Л_f67ab10a(ThemeColorSlot.SELECTED) - var24) * var22;
            int var25 = ColorMath.withAlpha(var23, var24 * (OЙ + var22 * Oк) * var13);
            float var26 = UiDrawing.getGlobalAlpha();
            UiDrawing.setGlobalAlpha(var26 * var13);
            if (var17 > Oг || var18 > O0) {
               UiDrawing.drawShadow(
                  var19, var14, var20, Oх, OУ, var18 > var17 ? this.В(ThemeColorSlot.MAIN, var18 * OЗ) : this.В(ThemeColorSlot.SHADOW, var17 * OЕ)
               );
            }

            UiDrawing.drawRoundedRect(var19, var14, var20, OП, O西, var25);
            float var27 = Oд + var17 * 2.0F;
            this.Л(var12.stack(), var19 + OБ, var14 + (OС - var27) * Oз, var27);
            UiDrawing.drawText(
               ClientFonts.getMediumFont(),
               this.Л(ClientLocalization.translate(var12.name()), var20 - Oц, 11),
               var19 + Oя,
               var14 + field_7с,
               11,
               this.В(ThemeColorSlot.TEXT, var21 * field_7В)
            );
            if (var12 == this.Я_47df95dc) {
               float var28 = var19 + field_7Я;
               float var29 = var14 + field_7Х;
               float var30 = var20 - field_7е;
               UiDrawing.drawRoundedRect(var28, var29, var30, field_7А, field_74, this.Л_a83dd0cc(field_7п, field_7Д));
               String var31 = System.currentTimeMillis() % field_7Ъ < field_7т ? field_7а : "";
               UiDrawing.drawText(
                  ClientFonts.getMediumFont(), ClientLocalization.translate(field_7Ю) + this.с3 + var31, var28 + field_7Ч, var29 + field_7Л, 9, this.В(ThemeColorSlot.TEXT, field_7诶)
               );
            } else {
               boolean var33 = var12.price() <= 1L;
               String var34 = var33 ? ClientLocalization.translate(field_71) : var4.Р(var12.price());
               int var35 = var33 ? this.Л_a83dd0cc(ThemeColorSlot.SUR_TEXT) : this.Л_a83dd0cc(ThemeColorSlot.MAIN);
               UiDrawing.drawText(
                  ClientFonts.getMediumFont(),
                  var34,
                  var19 + field_7Ь,
                  var14 + field_7衣,
                  9,
                  ColorMath.withAlpha(var35, this.Л_f67ab10a(var33 ? ThemeColorSlot.SUR_TEXT : ThemeColorSlot.MAIN) * var21 * (var33 ? this.Лы() : 1.0F))
               );
            }

            UiDrawing.drawText(
               ClientFonts.getMediumFont(),
               field_7ш,
               var19 + var20 - field_7И,
               var14 + field_7ь,
               13,
               this.В(ThemeColorSlot.SUR_TEXT, var21 * (field_7ж + var17 * field_7弟))
            );
            UiDrawing.setGlobalAlpha(var26);
            if (var16) {
               var9 = var12;
            }
         }
      }

      UiDrawing.popScissor();
      this.Ы(this.Лх() + this.ЛЧ() - field_7б, this.Хд, this.к_f67ab10a(var1.size()), this.Лэ());
      return var9;
   }

   public boolean Н(String var1) {
      if (this.Я_47df95dc != null && var1 != null) {
         int var2 = 0;

         while (var2 < var1.length()) {
            int var3 = var1.codePointAt(var2);
            var2 += Character.charCount(var3);
            if (var3 >= 48 && var3 <= 57 && this.с3.length() < 10) {
               this.с3 = this.с3 + (char)var3;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public float ЛЛ() {
      return this.ЧГ();
   }

   private float ЛЧ() {
      return this.ЧЦ() - оЮ - this.ЧЭ();
   }

   private FunTime.PurchaseEntry Л(List<FunTime.PurchaseEntry> var1, float var2, float var3) {
      float var4 = this.Чг() * OЯ + (this.Чг() - 1) * OХ;
      float var5 = this.ЛЫ() + (this.ЧЭ() - var4) * Oе;
      float var6 = this.Лв() + this.ХП;
      float var7 = this.Лв() + this.Лэ();
      FunTime.PurchaseEntry var8 = null;
      UiDrawing.pushScissor(this.ЛЫ() + OА, this.Лв(), this.ЧЭ() - O4, this.Лэ());

      for (int var9 = 0; var9 < var1.size(); var9++) {
         FunTime.PurchaseEntry var10 = (FunTime.PurchaseEntry)var1.get(var9);
         float var11 = var5 + var9 % this.Чг() * Oп;
         float var12 = var6 + var9 / this.Чг() * OД;
         if (!(var12 + OЪ < this.Лв()) && !(var12 > var7)) {
            boolean var13 = 诶(var2, var3, var11, var12, Oт, Oа) && 诶(var2, var3, this.ЛЫ(), this.Лв(), this.ЧЭ(), this.Лэ()) && !this.诶(var11, var12, OЮ, OЧ);
            float var14 = this.Л(this.Ф, var10, var13, OЛ);
            float var15 = this.Л(this.Й, var10, var10 == this.В, O诶);
            float var16 = var10.enabled() ? 1.0F : 0.0F;
            float var17 = Math.max(var15, var16 * O1);
            float var18 = Math.max(var17, var14 * OЬ);
            int var19 = ColorMath.lerpArgb(
               ColorMath.lerpArgb(this.Л_a83dd0cc(ThemeColorSlot.BACKGROUND), this.Л_a83dd0cc(ThemeColorSlot.DEACTIVE_STATE), O衣),
               this.Л_a83dd0cc(ThemeColorSlot.SELECTED),
               var18
            );
            float var20 = this.Л(ThemeColorSlot.BACKGROUND, ThemeColorSlot.DEACTIVE_STATE, Oш);
            var20 += (this.Л_f67ab10a(ThemeColorSlot.SELECTED) - var20) * var18;
            int var21 = ColorMath.withAlpha(var19, var20 * (OИ + var18 * Oь + var14 * Oж));
            float var22 = var14 * O弟 + var15 * Oб;
            if (var14 > OЫ) {
               UiDrawing.drawShadow(var11, var12 - var22, Ol, O8, O5, this.В(ThemeColorSlot.SHADOW, var14 * Oв));
            }

            UiDrawing.drawRoundedRect(var11, var12 - var22, Oъ, OМ, OН, var21);
            float var23 = Oн + var14 * OЦ + var15 * Oч;
            this.Л(var10.stack(), var11 + (OЭ - var23) * O必, var12 - var22 + (Oщ - var23) * OГ, var23);
            if (var10.enabled()) {
               float var24 = Oл + Oэ * (float)Math.sin(System.currentTimeMillis() / Oю + var9);
               UiDrawing.drawRoundedRect(var11 + OЩ - Oу, var12 - var22 + O2, Oф, Oи, O9, this.В(ThemeColorSlot.MAIN, var24));
            }

            if (var13) {
               var8 = var10;
            }
         }
      }

      UiDrawing.popScissor();
      this.Ы(this.ЛЫ() + this.ЧЭ() - Oо, this.ХП, this.э(var1.size()), this.Лэ());
      return var8;
   }

   private float Л诶() {
      return this.Я_a763ee07.getGuiX() + this.Я_a763ee07.getSidebarWidth() + о1;
   }

   private float Лэ() {
      return this.ЧГ() - оИ - оь;
   }

   public AutoBuyPanel(ClickGuiScreen var1) {
      this(ClickGuiPanelHost.of(var1));
   }

   public AutoBuyPanel(ClickGuiPanelHost var1) {
      this.г_04d91340 = new FloatAnimation(0.0F, оп, Easings.CUBIC_OUT);
      this.Ф = new HashMap<>();
      this.Й = new HashMap<>();
      this.к_ea411899 = new HashMap<>();
      this.г_ea411899 = new HashMap<>();
      this.к_2571477d = new ArrayList<>();
      this.с3 = "";
      this.Я_a763ee07 = var1;
   }

   public boolean к_bbeebd87(int var1) {
      if (this.Я_47df95dc == null) {
         return false;
      }

      if (var1 == 257 || var1 == 335) {
         this.В必();
      } else if (var1 == 256) {
         this.В0();
      } else if (var1 == 259) {
         if (!this.с3.isEmpty()) {
            this.с3 = this.с3.substring(0, this.с3.length() - 1);
         }
      } else if (var1 == 261) {
         this.с3 = "";
      }

      return true;
   }

   private float Лх() {
      return this.ЛЫ() + this.ЧЭ() + о衣;
   }

   private int Л_a83dd0cc(float var1, float var2) {
      int var3 = ColorMath.lerpArgb(this.Л_a83dd0cc(ThemeColorSlot.BACKGROUND), this.Л_a83dd0cc(ThemeColorSlot.DEACTIVE_STATE), var1);
      return ColorMath.withAlpha(var3, this.Л(ThemeColorSlot.BACKGROUND, ThemeColorSlot.DEACTIVE_STATE, var1) * var2);
   }

   private float ЧЭ() {
      return (this.ЧЦ() - оЪ) * от / оа;
   }

   private int В(ThemeColorSlot var1, float var2) {
      return ColorMath.withAlpha(this.Л_a83dd0cc(var1), this.Л_f67ab10a(var1) * var2);
   }

   private float э(int var1) {
      int var2 = (var1 + this.Чг() - 1) / this.Чг();
      return Math.max(0.0F, var2 * field_79 - field_7о);
   }

   private void O(int var1, int var2) {
      float var3 = Math.max(0.0F, this.э(var1) - this.Лэ());
      this.Х西 = Math.clamp(this.Х西, -var3, 0.0F);
      this.ХП = Math.clamp(this.ХП + (this.Х西 - this.ХП) * оЭ, -var3, 0.0F);
      float var4 = Math.max(0.0F, this.к_f67ab10a(var2) - this.Лэ());
      this.ХБ = Math.clamp(this.ХБ, -var4, 0.0F);
      this.Хд = Math.clamp(this.Хд + (this.ХБ - this.Хд) * о必, -var4, 0.0F);
   }

   private float ЧГ() {
      return this.Я_a763ee07.getModuleViewportHeight();
   }
}
