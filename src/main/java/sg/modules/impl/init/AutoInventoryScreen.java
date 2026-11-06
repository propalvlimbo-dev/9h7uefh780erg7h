package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import sg.ec.FontRenderer;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.modules.impl.misc.AutoInventory;

public final class AutoInventoryScreen extends Screen {
   private static final float В必 = 425.0F;
   private static final float Вщ = 800.0F;
   private static final float ВГ = 504.0F;
   private static final float Вл = 1.882353F;
   private static final float Вэ = 267.75F;
   private static final float Вю = 20.0F;
   private static final float ВЩ = 4.0F;
   private static final int В诶 = 10;
   @SerializedName("В")
   private final AutoInventory В_6b64e6fe;
   private final FloatAnimation Ц;
   private final List<AutoInventoryScreen.EntryHitRegion> Э;
   @SerializedName("В")
   private AutoInventory.CollectionEntry В_071dcf5e;
   @SerializedName("Я")
   private AutoInventory.CollectionEntry Я_071dcf5e;
   private float Ву;
   private float В2;
   private float Вф;
   private float Ви;
   private float В9;
   private float Во;
   private float ВO;
   private float В7;
   private float ВР;
   @SerializedName("В")
   private AutoInventoryScreen.Bounds В_4a3ff18b;
   @SerializedName("Я")
   private AutoInventoryScreen.Bounds Я_4a3ff18b;
   private AutoInventoryScreen.Bounds Х;
   private static final String я8 = "Collector";
   private static final float я5 = 7.0F;
   private static final float яв = 255.0F;
   private static final float яъ = 800.0F;
   private static final float яМ = 0.5F;
   private static final float яН = 504.0F;
   private static final float ян = 0.5F;
   private static final float яЦ = 425.0F;
   private static final float яч = 0.5F;
   private static final float яЭ = 267.75F;
   private static final float я必 = 0.5F;
   private static final float ящ = 1.882353F;
   private static final float яГ = 0.85F;
   private static final float ял = 0.15F;
   private static final float яэ = 14.0F;
   private static final float яю = 133.875F;
   private static final float яЩ = 0.22F;
   private static final float яу = 800.0F;
   private static final float я2 = 504.0F;
   private static final float яф = 20.0F;
   private static final float яи = 425.0F;
   private static final float я9 = 267.75F;
   private static final float яо = 425.0F;
   private static final float яO = 267.75F;
   private static final float я7 = 8.0F;
   private static final String яР = "S";
   private static final float я3 = 8.5F;
   private static final String яр = "F";
   private static final float я6 = 8.5F;
   private static final float я_ = 36.0F;
   private static final float яШ = 425.0F;
   private static final float яы = 0.5F;
   private static final String яй = "P";
   private static final float яЖ = 10.0F;
   private static final float ям = 14.0F;
   private static final float яК = 27.0F;
   private static final float яI = 4.0F;
   private static final float яТ = 0.75F;
   private static final float яО = 8.0F;
   private static final double яФ = 25.0;
   private static final String яЙ = "rose client";
   private static final float як = 35.0F;
   private static final float яг = 3.5F;
   private static final float я0 = 8.75F;
   private static final float ях = 12.0F;
   private static final float яУ = 0.5F;
   private static final float яЗ = 3.0F;
   private static final float яЕ = 16.0F;
   private static final float яП = 6.0F;
   private static final float я西 = 0.5F;
   private static final float яд = 425.0F;
   private static final float яБ = 20.0F;
   private static final String яС = "collector section";
   private static final float яз = 16.0F;
   private static final float яц = 6.75F;
   private static final float яя = 4.5F;
   private static final float ссс = 6.75F;
   private static final float ссВ = 32.0F;
   private static final float ссЯ = 223.75F;
   private static final float ссХ = 8.0F;
   private static final double ссе = 10.0;
   private static final float ссА = 24.0F;
   private static final float сс4 = 4.0F;
   private static final float ссп = 212.5F;
   private static final float ссД = 425.0F;
   private static final float ссЪ = 24.0F;
   private static final float сст = 24.0F;
   private static final float сса = 20.0F;
   private static final double ссЮ = 20.0;
   private static final double ссЧ = 20.0;
   private static final float ссЛ = 0.024F;
   private static final float сс诶 = 0.012F;
   private static final float сс1 = 20.0F;
   private static final float ссЬ = 20.0F;
   private static final float сс衣 = 4.0F;
   private static final float ссш = 2.5F;
   private static final float ссИ = 2.5F;
   private static final float ссь = 15.0F;
   private static final float ссж = 15.0F;
   private static final float сс弟 = 20.0F;
   private static final float ссб = 6.5F;
   private static final float ссЫ = 20.0F;
   private static final float ссl = 8.0F;
   private static final float сс8 = 6.5F;
   private static final float сс5 = 0.35F;
   private static final float ссв = 20.0F;
   private static final float ссъ = 20.0F;
   private static final float ссМ = 260.0F;
   private static final float ссН = 42.0F;
   private static final float ссн = 8.0F;
   private static final float ссЦ = 12.0F;
   private static final float ссч = 12.0F;
   private static final float ссЭ = 129.0F;
   private static final float сс必 = 38.0F;
   private static final float ссщ = 6.0F;
   private static final float ссГ = 20.0F;
   private static final float ссл = 7.5F;
   private static final float ссэ = 20.0F;
   private static final float ссю = 5.5F;
   private static final float ссЩ = 7.5F;
   private static final float ссу = 4.25F;
   private static final float сс2 = 4.25F;
   private static final float ссф = 14.0F;
   private static final float сси = 8.5F;
   private static final float сс9 = 5.0F;
   private static final float ссо = 16.0F;
   private static final float ссO = 10.0F;
   private static final float сс7 = 16.0F;
   private static final float ссР = 5.0F;
   private static final float сс3 = 16.0F;
   private static final float сср = 10.0F;
   private static final String сс6 = "−";
   private static final float сс_ = 4.0F;
   private static final float ссШ = 5.5F;
   private static final float ссы = 11.0F;
   private static final float ссй = 16.0F;
   private static final float ссЖ = 6.75F;
   private static final float ссм = 0.5F;
   private static final float ссК = 3.4F;
   private static final float ссI = 6.75F;
   private static final String ссТ = "+";
   private static final float ссО = 26.0F;
   private static final float ссФ = 7.5F;
   private static final float ссЙ = 11.0F;
   private static final float сск = 0.18F;
   private static final float ссг = 14.0F;
   private static final float сс0 = 8.5F;
   private static final float ссх = 3.25F;
   private static final float ссУ = 14.0F;
   private static final float ссЗ = 8.5F;
   private static final float ссЕ = 0.3F;
   private static final float ссП = 3.25F;
   private static final float сс西 = 0.22F;
   private static final float ссд = 1.5F;
   private static final float ссБ = 5.5F;
   private static final float ссС = 1.5F;
   private static final float ссз = 5.5F;
   private static final float ссц = 5.5F;
   private static final float сся = 1.75F;
   private static final float сВс = 8.0F;
   private static final float сВВ = 32.0F;
   private static final double сВЯ = 236.0;
   private static final double сВХ = 223.75;
   private static final float сВе = 15.0F;
   private static final float сВА = 212.5F;

   private boolean contains(AutoInventoryScreen.Bounds var1, Vector2f var2) {
      return var1 != null && var1.contains(var2.x, var2.y);
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   private void icon(String var1, float var2, float var3, float var4, int var5) {
      UiBatchDrawing.drawText(ClientFonts.getIconsFont(), var1, var2, var3, (int)var4, var5);
   }

   private String fit(String var1, float var2, float var3) {
      if (this.width(ClientFonts.getRegularFont(), var1, var3) <= var2) {
         return var1;
      }

      String var4 = var1;

      while (var4.length() > 1 && this.width(ClientFonts.getRegularFont(), var4 + "…", var3) > var2) {
         var4 = var4.substring(0, var4.length() - 1);
      }

      return var4 + "…";
   }

   private boolean inside(double var1, double var3, double var5, double var7, double var9, double var11) {
      return var1 >= var5 && var1 <= var5 + var9 && var3 >= var7 && var3 <= var7 + var11;
   }

   public AutoInventoryScreen(AutoInventory var1) {
      super(Component.literal(я8));
      this.Ц = new FloatAnimation(0.0F, я5, Easings.CUBIC_OUT);
      this.Э = new ArrayList<>();
      this.В9 = 1.0F;
      this.В_6b64e6fe = var1;
   }

   @Override
   protected void extractBlurredBackground(GuiGraphicsExtractor var1) {
   }

   @Override
   public void clearFocus() {
   }

   private Vector2f logicalMouse(float var1, float var2) {
      float var3 = this.Ву + сВА;
      return new Vector2f(var3 + (var1 - var3) / this.В9, this.Во + (var2 - this.Во) / this.В9);
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      Vector2f var9 = WorldProjection.guiToScreen(var1, var3);
      Vector2f var10 = this.logicalMouse(var9.x, var9.y);
      if (this.inside(var10.x, var10.y, this.Ву + сВс, this.В2 + сВВ, сВЯ, сВХ)) {
         this.В7 = Math.clamp(this.В7 + (float)var7 * сВе, this.ВР, 0.0F);
      }

      return true;
   }

   private void drawHeader() {
      float var1 = this.В2 + я7;
      float var2 = this.iconWidth(яР, я3);
      float var3 = this.iconWidth(яр, я6);
      float var4 = var2 + var3 + я_;
      float var5 = this.Ву + (яШ - var4) * яы;
      this.icon(яй, this.Ву + яЖ, var1, ям, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
      UiBatchDrawing.drawRoundedRect(this.Ву + яК, var1 + яI, яТ, яО, 0.0F, ColorMath.packArgb(255, 255, 255, яФ));
      this.text(ClientFonts.getRegularFont(), яЙ, this.Ву + як, var1 + яг, я0, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
      float var6 = var5 + ях;
      float var7 = var6 + var2 * яУ;
      UiBatchDrawing.drawRoundedRect(var7 - яЗ, var1 + яЕ, яП, я西, 0.0F, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
      float var8 = this.Ву + яд - яБ;
      String var9 = яС;
      this.text(ClientFonts.getRegularFont(), var9, var8 + яз - this.width(ClientFonts.getRegularFont(), var9, яц), var1 + яя, ссс, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
   }

   private void toggle(float var1, float var2, boolean var3) {
      int var4 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT), сск);
      UiBatchDrawing.drawRoundedRect(var1, var2, ссг, сс0, ссх, var3 ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : var4);
      UiBatchDrawing.drawOutline(var1, var2, ссУ, ссЗ, ссЕ, ссП, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT), сс西));
      UiBatchDrawing.drawRoundedRect(
         var1 + ссд + (var3 ? ссБ : 0.0F),
         var2 + ссС,
         ссз,
         ссц,
         сся,
         var3 ? ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT) : ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
      );
   }

   private void drawSettings() {
      this.В_4a3ff18b = this.Я_4a3ff18b = this.Х = null;
      if (this.В_071dcf5e != null) {
         float var1 = this.Ву + ссМ;
         float var2 = this.В2 + ссН;
         UiBatchDrawing.drawItemWithOverlay(var1, var2 - ссн, ссЦ, ссч, this.В_071dcf5e.В(), false);
         float var3 = var1 + ссЭ;
         float var4 = var3 - сс必;
         String var5 = this.fit(this.В_071dcf5e.ЮЖ(), var4 - ссщ - (var1 + ссГ), ссл);
         this.text(
            ClientFonts.getRegularFont(),
            var5,
            var1 + ссэ,
            var2 - ссю,
            ссЩ,
            this.В_071dcf5e.иЬ() ? ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT) : ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
         );
         this.toggle(var3, var2 - ссу, this.В_071dcf5e.иЬ());
         this.В_4a3ff18b = new AutoInventoryScreen.Bounds(var3, var2 - сс2, ссф, сси);
         this.Я_4a3ff18b = new AutoInventoryScreen.Bounds(var4, var2 - сс9, ссо, ссO);
         this.Х = new AutoInventoryScreen.Bounds(var4 + сс7, var2 - ссР, сс3, сср);
         this.text(ClientFonts.getRegularFont(), сс6, var4 + сс_, var2 - ссШ, ссы, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
         String var6 = String.valueOf(this.В_071dcf5e.Л6());
         this.text(
            ClientFonts.getRegularFont(),
            var6,
            var4 + ссй - this.width(ClientFonts.getRegularFont(), var6, ссЖ) * ссм,
            var2 - ссК,
            ссI,
            this.В_071dcf5e.иЬ() ? ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT) : ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
         );
         this.text(ClientFonts.getRegularFont(), ссТ, var4 + ссО, var2 - ссФ, ссЙ, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
      }
   }

   @Override
   public void onClose() {
      if (this.В_6b64e6fe.method_3Д() > 0 && !this.В_6b64e6fe.УI()) {
         this.В_6b64e6fe.МЙ();
      }

      super.onClose();
   }

   private float iconWidth(String var1, float var2) {
      return ClientFonts.getIconsFont().getFontFace().measureTextWidth(var1, var2, 0.0F);
   }

   private float width(FontRenderer var1, String var2, float var3) {
      return var1.getFontFace().measureTextWidth(var2, var3, 0.0F);
   }

   private void drawGrid(List<AutoInventory.CollectionEntry> var1, float var2, float var3) {
      float var4 = this.В2 + ссВ;
      float var5 = ссЯ;
      float var6 = this.Ву + ссХ;
      float var7 = (float)Math.ceil(var1.size() / ссе);
      float var8 = Math.max(0.0F, var7 * ссА - сс4);
      this.ВР = Math.min(0.0F, var5 - var8);
      this.В7 = Math.clamp(this.В7, this.ВР, 0.0F);
      this.ВO = Math.clamp(this.ВO, this.ВР, 0.0F);
      this.Я_071dcf5e = null;
      this.Э.clear();
      float var9 = this.Ву + ссп;
      UiDrawing.pushScissor(var9 + (this.Ву - var9) * this.В9, this.Во + (var4 - this.Во) * this.В9, ссД * this.В9, var5 * this.В9);

      for (int var10 = 0; var10 < var1.size(); var10++) {
         AutoInventory.CollectionEntry var11 = (AutoInventory.CollectionEntry)var1.get(var10);
         float var12 = var6 + var10 % 10 * ссЪ;
         float var13 = var4 + var10 / 10 * сст + this.ВO;
         if (!(var13 + сса < var4) && !(var13 > var4 + var5)) {
            boolean var14 = this.inside(var2, var3, var12, var13, ссЮ, ссЧ);
            if (var14) {
               this.Я_071dcf5e = var11;
            }

            float var15 = var11 == this.В_071dcf5e ? ссЛ : (var14 ? сс诶 : 0.0F);
            if (var15 > 0.0F) {
               UiBatchDrawing.drawRoundedRect(var12, var13, сс1, ссЬ, сс衣, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT), var15));
            }

            UiBatchDrawing.drawItemWithOverlay(var12 + ссш, var13 + ссИ, ссь, ссж, var11.В(), false);
            if (var11.Л6() > 1) {
               String var16 = String.valueOf(var11.Л6());
               this.text(
                  ClientFonts.getRegularFont(),
                  var16,
                  var12 + сс弟 - 2.0F - this.width(ClientFonts.getRegularFont(), var16, ссб),
                  var13 + ссЫ - ссl,
                  сс8,
                  var11.иЬ() ? ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT) : ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT), сс5)
               );
            }

            this.Э.add(new AutoInventoryScreen.EntryHitRegion(new AutoInventoryScreen.Bounds(var12, var13, ссв, ссъ), var11));
         }
      }

      UiDrawing.popScissor();
   }

   private void text(FontRenderer var1, String var2, float var3, float var4, float var5, int var6) {
      UiBatchDrawing.drawText(var1, var2, var3, var4, (int)var5, var6);
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      this.Ц.updateTarget(1.0F);
      float var5 = Math.clamp(this.Ц.getValue(), 0.0F, 1.0F);
      UiDrawing.setGlobalAlpha(яв * var5);
      this.Вф = (this.minecraft.getWindow().getScreenWidth() - яъ) * яМ;
      this.Ви = (this.minecraft.getWindow().getScreenHeight() - яН) * ян;
      this.Ву = (this.minecraft.getWindow().getScreenWidth() - яЦ) * яч;
      this.В2 = (this.minecraft.getWindow().getScreenHeight() - яЭ) * я必;
      this.В9 = ящ * (яГ + var5 * ял);
      float var6 = this.В2 + (1.0F - var5) * яэ;
      this.Во = var6 + яю;
      Vector2f var7 = WorldProjection.transformScreenPoint(var2, var3);
      Vector2f var8 = this.logicalMouse(var7.x, var7.y);
      List var9 = this.В_6b64e6fe.р();
      if (this.В_071dcf5e == null && !var9.isEmpty()) {
         this.В_071dcf5e = (AutoInventory.CollectionEntry)var9.getFirst();
      }

      this.ВO = this.ВO + (this.В7 - this.ВO) * яЩ;
      UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getRegularFont(), ClientFonts.getIconsFont());
      UiBatchDrawing.drawHudWindowBackground(this.Вф, this.Ви, яу, я2, яф);
      WorldProjection.scaleAroundRect(this.Ву, var6, яи, я9, this.В9);
      this.drawHeader();
      this.drawGrid(var9, var8.x, var8.y);
      this.drawSettings();
      WorldProjection.unscaleAroundRect(this.Ву, var6, яо, яO, this.В9);
      UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getRegularFont(), ClientFonts.getIconsFont());
      UiDrawing.resetGlobalAlpha();
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      Vector2f var3 = WorldProjection.guiToScreen(var1.x(), var1.y());
      Vector2f var4 = this.logicalMouse(var3.x, var3.y);
      if (var1.button() == 0) {
         if (this.contains(this.В_4a3ff18b, var4)) {
            this.В_071dcf5e.Я_(!this.В_071dcf5e.иЬ());
            return true;
         }

         if (this.contains(this.Я_4a3ff18b, var4)) {
            this.В_071dcf5e.Яр(this.В_071dcf5e.Л6() - 1);
            return true;
         }

         if (this.contains(this.Х, var4)) {
            this.В_071dcf5e.Яр(this.В_071dcf5e.Л6() + 1);
            return true;
         }

         for (AutoInventoryScreen.EntryHitRegion var6 : this.Э) {
            if (var6.bounds.contains(var4.x, var4.y)) {
               this.В_071dcf5e = var6.entry;
               return true;
            }
         }
      }

      if (var1.button() == 1 && this.Я_071dcf5e != null) {
         this.Я_071dcf5e.Я_(!this.Я_071dcf5e.иЬ());
         return true;
      } else {
         return true;
      }
   }

   private record EntryHitRegion(AutoInventoryScreen.Bounds bounds, AutoInventory.CollectionEntry entry) {
   }

   private record Bounds(float x, float y, float width, float height) {
      private boolean contains(float var1, float var2) {
         return var1 >= this.x && var1 <= this.x + this.width && var2 >= this.y && var2 <= this.y + this.height;
      }
   }
}
