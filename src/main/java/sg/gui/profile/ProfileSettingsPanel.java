package sg.gui.profile;

import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientLocalization;
import sg.ec.MinecraftAccess;
import sg.ec.ClientTextureLookup;
import sg.ec.ClientFonts;
import sg.config.ConfigurationManager;
import sg.modules.impl.init.ClickGuiPreferences;
import sg.client.RoseClient;
import sg.gui.click.ClickGuiScreen;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ColorMath;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.util.time.ThemeEditor;

public final class ProfileSettingsPanel implements MinecraftAccess {
   private static final float пф = 242.0F;
   private static final float пи = 288.0F;
   private static final float п9 = 12.0F;
   private static final float по = 225.0F;
   private static final float пO = 47.0F;
   private static final float п7 = 8.0F;
   private static final String ВФ = "\ue120";
   private static final String ВЙ = "\ue125";
   private static final String Вк = "\ue126";
   private static final String Вг = "\ue127";
   private static final String В0 = "\ue128";
   private static final String Вх = "\ue129";
   private final ClickGuiScreen е;
   private final FloatAnimation сп;
   private final FloatAnimation сД;
   private boolean Хч;
   private boolean ХЭ;
   private float пР;
   private boolean Х必;
   private static final float сЯ_ = 7.5F;
   private static final float сЯШ = 10.0F;
   private static final float сЯы = 254.0F;
   private static final float сЯй = 242.0F;
   private static final float сЯЖ = 12.0F;
   private static final float сЯм = 8.0F;
   private static final float сЯК = 288.0F;
   private static final float сЯI = 14.0F;
   private static final float сЯТ = 10.0F;
   private static final float сЯО = 10.0F;
   private static final float сЯФ = 53.0F;
   private static final float сЯЙ = 18.0F;
   private static final float сЯк = 4.0F;
   private static final float сЯг = 6.0F;
   private static final float сЯ0 = 32.0F;
   private static final float сЯх = 0.01F;
   private static final float сЯУ = 70.0F;
   private static final float сЯЗ = 40.0F;
   private static final float сЯЕ = 42.0F;
   private static final float сЯП = 45.0F;
   private static final float сЯ西 = 66.0F;
   private static final float сЯд = 44.0F;
   private static final float сЯБ = 8.0F;
   private static final float сЯС = 12.0F;
   private static final float сЯз = 66.0F;
   private static final float сЯц = 44.0F;
   private static final float сЯя = 25.0F;
   private static final float сХс = 10.0F;
   private static final String сХВ = "\ue127";
   private static final float сХЯ = 15.0F;
   private static final float сХХ = 16.0F;
   private static final float сХе = 12.0F;
   private static final float сХА = 10.0F;
   private static final float сХ4 = 53.0F;
   private static final float сХп = 18.0F;
   private static final double сХД = 45.0;
   private static final float сХЪ = 0.001F;
   private static final float сХт = 12.0F;
   private static final float сХа = 24.0F;
   private static final float сХЮ = 12.0F;
   private static final float сХЧ = 48.0F;
   private static final float сХЛ = 242.0F;
   private static final float сХ诶 = 288.0F;
   private static final float сХ1 = 14.0F;
   private static final float сХЬ = 14.0F;
   private static final float сХ衣 = 32.0F;
   private static final float сХш = 154.0F;
   private static final float сХИ = 56.0F;
   private static final float сХь = 14.0F;
   private static final float сХж = 13.0F;
   private static final float сХ弟 = 154.0F;
   private static final float сХб = 56.0F;
   private static final float сХЫ = 32.0F;
   private static final float сХl = 10.0F;
   private static final String сХ8 = "9";
   private static final float сХ5 = 242.0F;
   private static final float сХв = 25.0F;
   private static final float сХъ = 16.0F;
   private static final float сХМ = 10.0F;
   private static final String сХН = "\ue128";
   private static final String сХн = "UID";
   private static final float сХЦ = 60.0F;
   private static final String сХч = "—";
   private static final float сХЭ = 61.0F;
   private static final String сХ必 = "\ue129";
   private static final String сХщ = "Подписка";
   private static final String сХГ = "Subscription";
   private static final float сХл = 80.0F;
   private static final String сХэ = "Нет данных";
   private static final String сХю = "NowUser";
   private static final String сХЩ = "до ";
   private static final String сХу = "until ";
   private static final float сХ2 = 81.0F;
   private static final float сХф = 100.0F;
   private static final String сХи = "\ue120";
   private static final String сХ9 = "Язык";
   private static final String сХо = "Language";
   private static final float сХO = 110.0F;
   private static final String сХ7 = "English";
   private static final String сХР = "Русский";
   private static final float сХ3 = 111.0F;
   private static final String сХр = "\ue125";
   private static final String сХ6 = "Масштаб GUI";
   private static final String сХ_ = "GUI scale";
   private static final float сХШ = 142.0F;
   private static final float сХы = 100.0F;
   private static final float сХй = 143.0F;
   private static final float сХЖ = 204.0F;
   private static final float сХм = 0.7F;
   private static final float сХК = 0.5F;
   private static final float сХI = 19.0F;
   private static final float сХТ = 172.0F;
   private static final float сХО = 3.0F;
   private static final float сХФ = 1.5F;
   private static final float сХЙ = 19.0F;
   private static final float сХк = 172.0F;
   private static final float сХг = 3.0F;
   private static final float сХ0 = 3.0F;
   private static final float сХх = 1.5F;
   private static final float сХУ = 15.0F;
   private static final float сХЗ = 169.5F;
   private static final float сХЕ = 8.0F;
   private static final float сХП = 8.0F;
   private static final float сХ西 = 4.0F;
   private static final float сХд = 190.0F;
   private static final String сХБ = "\ue126";
   private static final String сХС = "Вид GUI";
   private static final String сХз = "GUI layout";
   private static final float сХц = 202.0F;
   private static final float сХя = 15.0F;
   private static final float сес = 55.0F;
   private static final float сеВ = 225.0F;
   private static final double сеЯ = 47.0;
   private static final double сеХ = 32.0;
   private static final float сее = 47.0F;
   private static final float сеА = 32.0F;
   private static final float се4 = 7.0F;
   private static final float сеп = 0.85F;
   private static final float сеД = 0.45F;
   private static final float сеЪ = 0.18F;
   private static final float сет = 47.0F;
   private static final float сеа = 32.0F;
   private static final float сеЮ = 7.0F;
   private static final float сеЧ = 0.5F;
   private static final float сеЛ = 15.0F;
   private static final float се诶 = 7.0F;
   private static final float се1 = 17.0F;
   private static final float сеЬ = 242.0F;
   private static final float се衣 = 0.5F;
   private static final float сеш = 268.0F;
   private static final float сеИ = 10.0F;
   private static final float сеь = 14.0F;
   private static final float сеж = 142.0F;
   private static final double се弟 = 103.0;
   private static final double себ = 32.0;
   private static final float сеЫ = 103.0F;
   private static final float сеl = 32.0F;
   private static final float се8 = 7.0F;
   private static final float се5 = 0.55F;
   private static final float сев = 0.18F;
   private static final float сеъ = 103.0F;
   private static final float сеМ = 32.0F;
   private static final float сеН = 7.0F;
   private static final float сен = 0.5F;
   private static final String сеЦ = "Русский";
   private static final String сеч = "English";
   private static final float сеЭ = 12.0F;
   private static final float се必 = 9.0F;
   private static final float сещ = 12.0F;
   private static final double сеГ = 103.0;
   private static final double сел = 32.0;
   private static final double сеэ = 242.0;
   private static final double сею = 288.0;
   private static final float сеЩ = 242.0F;
   private static final float сеу = 34.0F;
   private static final float се2 = 6.0F;
   private static final double сеф = 28.0;
   private static final double сеи = 28.0;
   private static final float се9 = 110.0F;
   private static final float сео = 12.0F;
   private static final float сеO = 164.0F;
   private static final double се7 = 218.0;
   private static final double сеР = 20.0;
   private static final float се3 = 15.0F;
   private static final float сер = 55.0F;
   private static final float се6 = 225.0F;
   private static final double се_ = 47.0;
   private static final double сеШ = 32.0;
   private static final float сеы = 0.7F;
   private static final float сей = 19.0F;
   private static final float сеЖ = 204.0F;
   private static final float сем = 0.5F;
   private static final float сеК = 0.7F;
   private static final float сеI = 1.2F;
   private static final float сеТ = 9.0F;
   private static final float сеО = 4.0F;
   private static final double сеФ = 224.0;
   private static final double сеЙ = 26.0;
   private static final float сек = 9.0F;
   private static final float сег = 4.0F;
   private static final double се0 = 224.0;
   private static final double сех = 26.0;
   private static final float сеУ = 9.0F;
   private static final float сеЗ = 4.0F;
   private static final float сеЕ = 224.0F;
   private static final float сеП = 26.0F;
   private static final float се西 = 6.0F;
   private static final float сед = 0.45F;
   private static final float сеБ = 16.0F;
   private static final float сеС = 12.0F;
   private static final float сез = 36.0F;
   private static final float сец = 11.0F;
   private static final float сея = 242.0F;
   private static final float сАс = 34.0F;
   private static final float сАВ = 11.0F;
   private static final String сАЯ = ">";
   private static final float сАХ = 242.0F;
   private static final float сАе = 25.0F;
   private static final float сАА = 9.0F;
   private static final float сА4 = 242.0F;
   private static final float сАп = 17.0F;
   private static final float сАД = 10.0F;
   private static final float сАЪ = 16.0F;
   private static final float сАт = 210.0F;
   private static final float сАа = 18.0F;
   private static final float сАЮ = 22.0F;
   private static final float сАЧ = 12.0F;
   private static final float сАЛ = 12.0F;
   private static final float сА诶 = 12.0F;
   private static final String сА1 = "Пользователь";
   private static final String сАЬ = "User";
   private static final float сА衣 = 9.0F;
   private static final String сАш = "W";
   private static final float сАИ = 0.25F;
   private static final float сАь = 0.22F;
   private static final float сАж = 0.5F;
   private static final float сА弟 = 9.0F;
   private static final float сАб = 0.125F;
   private static final float сАЫ = 9.0F;
   private static final float сАl = 0.625F;
   private static final float сА8 = 0.125F;
   private static final float сА5 = 0.125F;
   private static final float сАв = 0.125F;
   private static final float сАъ = 0.5F;
   private static final float сАМ = 0.5F;

   private void б(float var1, float var2, String var3, String var4, float var5) {
      if (WorldProjection.containsPoint(var1, var2, this.бР() + сек, var5 - сег, се0, сех)) {
         UiDrawing.drawRoundedRect(this.бР() + сеУ, var5 - сеЗ, сеЕ, сеП, се西, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED), сед));
      }

      this.б(var3, var4, var5);
   }

   private void Э(float var1, float var2) {
      ClickGuiPreferences.Layout var3 = ClickGuiPreferences.衣();

      for (ClickGuiPreferences.Layout var7 : ClickGuiPreferences.Layout.values()) {
         float var8 = this.бР() + сХя + var7.ordinal() * сес;
         float var9 = this.б1() + сеВ;
         boolean var10 = var7 == ClickGuiPreferences.衣();
         boolean var11 = WorldProjection.containsPoint(var1, var2, var8, var9, сеЯ, сеХ);
         if (var11) {
            var3 = var7;
         }

         UiDrawing.drawRoundedRect(var8, var9, сее, сеА, се4, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED), var10 ? сеп : (var11 ? сеД : сеЪ)));
         UiDrawing.drawOutline(var8, var9, сет, сеа, 1.0F, сеЮ, var10 ? ColorMath.withAlpha(бь(), сеЧ) : ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
         ь(var7.method_60(), var8 + сеЛ, var9 + се诶, се1, var10 ? бь() : (var11 ? би() : б5()));
      }

      String var12 = var3.method_67();
      б(var12, this.бР() + (сеЬ - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var12, 10)) * се衣, this.б1() + сеш, сеИ, б5());
   }

   private static Object б() {
      return null;
   }

   public void П(float var1, float var2) {
      float var3 = this.бр();
      if (!(var3 < сХЪ)) {
         float var4 = UiDrawing.getGlobalAlpha();
         UiDrawing.pushScissor(this.е.getGuiX() - this.б诶() - сХт, this.е.getGuiY() - сХа, this.б诶() + сХЮ, this.е.getGuiHeight() + сХЧ);

         try {
            UiDrawing.setGlobalAlpha(var4 * var3);
            this.Л(var1, var2);
         } finally {
            UiDrawing.setGlobalAlpha(var4);
            UiDrawing.popScissor();
         }
      }
   }

   private static String б(Object var0) {
      return "NowUser";
   }

   public ProfileSettingsPanel(ClickGuiScreen var1) {
      this.сп = new FloatAnimation(0.0F, сЯ_, Easings.CUBIC_OUT);
      this.сД = new FloatAnimation(0.0F, сЯШ, Easings.CUBIC_OUT);
      this.е = var1;
   }

   public float б诶() {
      return сЯы * this.бр();
   }

   private void Хц() {
      ConfigurationManager var1 = RoseClient.getInstance().getServices().getConfigManager();
      String var2 = var1.getLastSavedModuleConfigName();
      var1.getModuleConfig().save();
      var1.setLastSavedModuleConfigName(var2);
   }

   private float б1() {
      return this.е.getGuiY() + this.е.getGuiHeight() - сЯК - сЯI + сЯТ * (1.0F - this.бр());
   }

   public void Йч(float var1) {
      this.ЙГ(var1);
   }

   private static int бь() {
      return ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
   }

   public boolean Т(int var1) {
      if (!this.Хч) {
         return false;
      }

      if (var1 == 256) {
         if (this.Х必) {
            this.Х必 = false;
         } else {
            this.Х必();
         }
      }

      return true;
   }

   private float бШ() {
      return this.б1() + сеж;
   }

   public boolean Ф(float var1, float var2) {
      return WorldProjection.containsPoint(var1, var2, this.е.getGuiX() + сХА, this.е.getGuiY() + this.е.getGuiHeight() - сХ4, this.е.getSidebarWidth() - сХп, сХД);
   }

   private void б(String var1, String var2, float var3) {
      ь(var1, this.бР() + сеБ, var3 + 1.0F, сеС, б5());
      б(var2, this.бР() + сез, var3 + 1.0F, сец, би());
   }

   public boolean хП() {
      return this.Хч;
   }

   public boolean method_8(float var1, float var2, int var3) {
      if (!this.Хч) {
         return false;
      }

      if (this.Х必) {
         for (int var4 = 0; var4 < 2; var4++) {
            if (WorldProjection.containsPoint(var1, var2, this.ч(var4), this.бШ(), сеГ, сел)) {
               if (var3 == 0) {
                  ClickGuiPreferences.Н(var4 == 1);
                  this.Х必 = false;
                  this.Хц();
               }

               return true;
            }
         }
      }

      if (!WorldProjection.containsPoint(var1, var2, this.бР(), this.б1(), сеэ, сею)) {
         this.Х必();
         return true;
      }

      if (var3 != 0) {
         return true;
      }

      if (WorldProjection.containsPoint(var1, var2, this.бР() + сеЩ - сеу, this.б1() + се2, сеф, сеи)) {
         this.Х必();
      } else if (this.ь(var1, var2, се9)) {
         this.Х必 = !this.Х必;
      } else if (!this.Х必 && WorldProjection.containsPoint(var1, var2, this.бР() + сео, this.б1() + сеO, се7, сеР)) {
         this.ХЭ = true;
         this.ЙГ(var1);
      } else {
         for (ClickGuiPreferences.Layout var7 : ClickGuiPreferences.Layout.values()) {
            float var8 = this.бР() + се3 + var7.ordinal() * сер;
            if (WorldProjection.containsPoint(var1, var2, var8, this.б1() + се6, се_, сеШ)) {
               this.е.setLayout(var7);
               this.Хц();
               return true;
            }
         }
      }

      return true;
   }

   public void ХУ() {
      this.сп.updateTarget(this.Хч ? 1.0F : 0.0F);
   }

   private static String ь(Object var0) {
      return "NowUser";
   }

   private static int би() {
      return ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT);
   }

   private void ЙГ(float var1) {
      if (this.ХЭ) {
         this.пР = Math.clamp(сеы + (var1 - this.бР() - сей) / сеЖ * сем, сеК, сеI);
      }
   }

   public void Х西() {
      if (this.ХЭ) {
         ClickGuiPreferences.о(this.пР);
         this.ХЭ = false;
         this.Хц();
      }
   }

   private boolean ь(float var1, float var2, float var3) {
      return WorldProjection.containsPoint(var1, var2, this.бР() + сеТ, this.б1() + var3 - сеО, сеФ, сеЙ);
   }

   private static void б(String var0, float var1, float var2, float var3, int var4) {
      UiDrawing.drawText(ClientFonts.getMediumFont(), var0, var1, var2, Math.round(var3), var4);
   }

   public void Хл() {
      this.Х必();
      this.сп.setImmediately(0.0F);
   }

   private void Ж(float var1, float var2) {
      for (int var3 = 0; var3 < 2; var3++) {
         float var4 = this.ч(var3);
         float var5 = this.бШ();
         boolean var6 = var3 == (ClickGuiPreferences.й_() ? 1 : 0);
         boolean var7 = WorldProjection.containsPoint(var1, var2, var4, var5, се弟, себ);
         UiDrawing.drawRoundedRect(var4, var5, сеЫ, сеl, се8, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED), var6 ? се5 : сев));
         UiDrawing.drawOutline(var4, var5, сеъ, сеМ, 1.0F, сеН, var6 ? ColorMath.withAlpha(бь(), сен) : ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
         б(var3 == 0 ? сеЦ : сеч, var4 + сеЭ, var5 + се必, сещ, var6 ? бь() : (var7 ? би() : б5()));
      }
   }

   private static int б5() {
      return ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT);
   }

   public void Т(float var1, float var2) {
      float var3 = this.е.getGuiX() + сЯО;
      float var4 = this.е.getGuiY() + this.е.getGuiHeight() - сЯФ;
      float var5 = this.е.getSidebarWidth() - сЯЙ;
      this.сД.updateTarget(!this.Ф(var1, var2) && !this.Хч ? 0.0F : 1.0F);
      l(var3 + сЯк, var4 + сЯг, сЯ0);
      float var6 = this.е.getSidebarLabelAlpha();
      if (var6 > сЯх && var5 > сЯУ) {
         float var7 = UiDrawing.getGlobalAlpha();
         UiDrawing.pushScissor(var3 + сЯЗ, var4, Math.max(0.0F, var5 - сЯЕ), сЯП);
         UiDrawing.setGlobalAlpha(var7 * var6);
         Object var8 = б();
         б(ь(б(var8), var5 - сЯ西, 12), var3 + сЯд, var4 + сЯБ, сЯС, би());
         б(ь(ь(var8), var5 - сЯз, 10), var3 + сЯц, var4 + сЯя, сХс, б5());
         ь(сХВ, var3 + var5 - сХЯ, var4 + сХХ, сХе, ColorMath.lerpArgb(б5(), бь(), this.сД.getValue()));
         UiDrawing.setGlobalAlpha(var7);
         UiDrawing.popScissor();
      }
   }

   private void Йе(float var1) {
      UiDrawing.drawRoundedRect(this.бР() + сАЪ, var1, сАт, 1.0F, 0.0F, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
   }

   private float ч(int var1) {
      return this.бР() + сеь + var1 * 111;
   }

   private float бр() {
      return Math.clamp(this.сп.getValue(), 0.0F, 1.0F);
   }

   public void Х必() {
      this.Х西();
      this.Хч = false;
      this.Х必 = false;
   }

   private void Л(float var1, float var2) {
      float var3 = this.бР();
      float var4 = this.б1();
      衣(var3, var4, сХЛ, сХ诶);
      Object var5 = б();
      l(var3 + сХ1, var4 + сХЬ, сХ衣);
      б(ь(б(var5), сХш, 13), var3 + сХИ, var4 + сХь, сХж, би());
      б(ь(ь(var5), сХ弟, 10), var3 + сХб, var4 + сХЫ, сХl, бь());
      ь(сХ8, var3 + сХ5 - сХв, var4 + сХъ, сХМ, б5());
      this.б(сХН, сХн, var4 + сХЦ);
      this.б(var5 == null ? сХч : String.valueOf(-1), var4 + сХЭ, б5());
      this.б(сХ必, ClientLocalization.selectLocalizedText(сХщ, сХГ), var4 + сХл);
      String var10000;
      if (var5 != null && null != null) {
         var10000 = ClientLocalization.selectLocalizedText(сХЩ, сХу);
         Object var10001 = null;
         var10000 = var10000 + "NowUser";
      } else {
         var10000 = ClientLocalization.selectLocalizedText(сХэ, сХю);
      }

      String var6 = var10000;
      this.б(var6, var4 + сХ2, б5());
      this.Йе(var4 + сХф);
      this.б(var1, var2, сХи, ClientLocalization.selectLocalizedText(сХ9, сХо), var4 + сХO);
      this.ь(ClickGuiPreferences.й_() ? сХ7 : сХР, var4 + сХ3);
      if (this.Х必) {
         this.Ж(var1, var2);
      } else {
         this.б(сХр, ClientLocalization.selectLocalizedText(сХ6, сХ_), var4 + сХШ);
         float var7 = this.ХЭ ? this.пР : ClickGuiPreferences.х8();
         this.б(Math.round(var7 * сХы) + "%", var4 + сХй, би());
         float var8 = сХЖ;
         float var9 = (var7 - сХм) / сХК * var8;
         UiDrawing.drawRoundedRect(var3 + сХI, var4 + сХТ, var8, сХО, сХФ, ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_STATE));
         UiDrawing.drawRoundedRect(var3 + сХЙ, var4 + сХк, Math.max(сХг, var9), сХ0, сХх, бь());
         UiDrawing.drawRoundedRect(var3 + сХУ + var9, var4 + сХЗ, сХЕ, сХП, сХ西, би());
      }

      this.Йе(var4 + сХд);
      this.б(сХБ, ClientLocalization.selectLocalizedText(сХС, сХз), var4 + сХц);
      this.Э(var1, var2);
   }

   private static String ь(String var0, float var1, int var2) {
      if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var0, var2) <= var1) {
         return var0;
      }

      while (!var0.isEmpty() && UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var0 + "…", var2) > var1) {
         var0 = var0.substring(0, var0.offsetByCodePoints(var0.length(), -1));
      }

      return var0 + "…";
   }

   private static void 衣(float var0, float var1, float var2, float var3) {
      UiDrawing.drawShadow(var0, var1, var2, var3, сАа, ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW));
      if (ClickGuiPreferences.х弟() > 0) {
         UiDrawing.drawBlur(var0, var1, var2, var3, сАЮ, сАЧ, -1);
      }

      UiDrawing.drawRoundedRect(var0, var1, var2, var3, сАЛ, ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND));
      UiDrawing.drawOutline(var0, var1, var2, var3, 1.0F, сА诶, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
   }

   private static void l(float var0, float var1, float var2) {
      Identifier var3 = MINECRAFT_CLIENT.player == null ? null : ClientTextureLookup.Н_c635a640(MINECRAFT_CLIENT.player);
      if (var3 == null) {
         UiDrawing.drawRoundedRect(var0, var1, var2, var2, сА衣, ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED));
         ь(сАш, var0 + var2 * сАИ, var1 + var2 * сАь, var2 * сАж, б5());
      } else {
         UiDrawing.drawTexture(var0, var1, var2, var2, new Vector4f(сА弟), new Vector4f(сАб), new Vector4i(-1), var3, FilterMode.NEAREST);
         UiDrawing.drawTexture(var0, var1, var2, var2, new Vector4f(сАЫ), new Vector4f(сАl, сА8, сА5, сАв), new Vector4i(-1), var3, FilterMode.NEAREST);
      }
   }

   private float бР() {
      return this.е.getGuiX() - сЯй - сЯЖ + сЯм * (1.0F - this.бр());
   }

   private void ь(String var1, float var2) {
      б(var1, this.бР() + сея - сАс - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, 11), var2, сАВ, б5());
      ь(сАЯ, this.бР() + сАХ - сАе, var2 + 2.0F, сАА, б5());
   }

   public void Хг() {
      if (this.Хч) {
         this.Х必();
      } else {
         this.Хч = true;
         this.Х必 = false;
      }
   }

   private void б(String var1, float var2, int var3) {
      б(var1, this.бР() + сА4 - сАп - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, 10), var2, сАД, var3);
   }

   private static void ь(String var0, float var1, float var2, float var3, int var4) {
      int var5 = Math.round(var3);
      float var6 = ClientFonts.getIconsFont().getFontFace().getGlyphWidth(var0.codePointAt(0), var5);
      UiDrawing.drawText(
         ClientFonts.getIconsFont(), var0, var1 + (var3 - var6) * сАъ, UiDrawing.alignTextCenterY(ClientFonts.getIconsFont(), var0, var2 + var3 * сАМ, var5), var5, var4
      );
   }
}
