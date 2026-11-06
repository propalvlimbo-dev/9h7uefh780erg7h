package sg.hud;

import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.ec.ClientTextureLookup;
import sg.events.EventListener;
import sg.ec.FontRenderer;
import sg.ec.ClientFonts;
import sg.render.FontFace;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.util.time.ClientMetrics;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.events.impl.HudRenderEvent;

public class WaterMark extends HudElement implements InteractiveHudElement {
   private static final long Хк = 190000000L;
   private final FloatAnimation сы;
   private final List<WaterMark.AnimatedDigit> сС;
   private String Яй;
   private int ДТ;
   private final EventListener<HudRenderEvent> Хщ;
   private static final String ЯРр = "WaterMark";
   private static final float ЯР6 = 41.0F;
   private static final float ЯР_ = 44.0F;
   private static final float ЯРШ = 120.0F;
   private static final float ЯРы = 35.0F;
   private static final float ЯРй = 12.0F;
   private static final String ЯРЖ = "Логотип";
   private static final float ЯРм = 130.0F;
   private static final String ЯРК = "P";
   private static final float ЯРI = 26.0F;
   private static final String ЯРТ = "U";
   private static final float ЯРО = 13.0F;
   private static final String ЯРФ = "|";
   private static final float ЯРЙ = 13.0F;
   private static final float ЯРк = 13.0F;
   private static final float ЯРг = 13.0F;
   private static final float ЯР0 = 34.0F;
   private static final float ЯРх = 34.0F;
   private static final float ЯРУ = 12.0F;
   private static final String ЯРЗ = "P";
   private static final float ЯРЕ = 10.0F;
   private static final float ЯРП = -0.1F;
   private static final float ЯР西 = 10.0F;
   private static final String ЯРд = "P";
   private static final float ЯРБ = 26.0F;
   private static final float ЯРС = 17.0F;
   private static final float ЯРз = 3.0F;
   private static final float ЯРц = 6.0F;
   private static final float ЯРя = 6.0F;
   private static final float Я3с = 2.5F;
   private static final float Я3В = 14.0F;
   private static final float Я3Я = 17.0F;
   private static final float Я3Х = 11.0F;
   private static final float Я3е = 20.0F;
   private static final float Я3А = 20.0F;
   private static final float Я34 = 9.0F;
   private static final float Я3п = 0.125F;
   private static final float Я3Д = 28.0F;
   private static final float Я3Ъ = 9.0F;
   private static final float Я3т = 13.0F;
   private static final float Я3а = 36.0F;
   private static final float Я3Ю = 17.0F;
   private static final float Я3Ч = 3.0F;
   private static final float Я3Л = 6.0F;
   private static final float Я3诶 = 6.0F;
   private static final float Я31 = 2.5F;
   private static final float Я3Ь = 14.0F;
   private static final String Я3衣 = "U";
   private static final float Я3ш = 9.0F;
   private static final String Я3И = "U";
   private static final float Я3ь = 13.0F;
   private static final float Я3ж = 8.0F;
   private static final float Я3弟 = 16.0F;
   private static final String Я3б = "U";
   private static final float Я3Ы = 13.0F;
   private static final float Я3l = 17.0F;
   private static final float Я38 = 3.0F;
   private static final float Я35 = 6.0F;
   private static final float Я3в = 6.0F;
   private static final float Я3ъ = 2.5F;
   private static final float Я3М = 14.0F;
   private static final String Я3Н = "|";
   private static final float Я3н = 9.0F;
   private static final String Я3Ц = "|";
   private static final float Я3ч = 13.0F;
   private static final float Я3Э = 8.0F;
   private static final float Я3必 = 9.0F;
   private static final String Я3щ = " ms";
   private static final float Я3Г = 13.0F;
   private static final String Я3л = "|";
   private static final float Я3э = 13.0F;
   private static final float Я3ю = 8.0F;
   private static final float Я3Щ = 9.0F;
   private static final float Я3у = 9.0F;
   private static final float Я32 = 13.0F;
   private static final double Я3ф = 3.0;
   private static final float Я3и = 6.0F;
   private static final double Я39 = Math.PI;
   private static final float Я3о = 0.28F;
   private static final float Я3O = 6.0F;
   private static final float Я37 = 6.0F;
   private static final float Я3Р = 13.0F;
   private static final String Я33 = " fps";
   private static final float Я3р = 13.0F;

   public WaterMark() {
      super(ЯРр, ЯР6, ЯР_, ЯРШ, ЯРы);
      this.сы = new FloatAnimation(0.0F, ЯРй, Easings.CUBIC_OUT);
      this.сС = new ArrayList<>();
      this.Яй = "";
      this.ДТ = -1;
      this.Хщ = this::дд;
   }

   @Override
   public void Б5() {
   }

   private void дШ(float var1) {
      int var2 = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT);
      int var3 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      float var4 = this.getY() + Я3у;
      long var5 = System.nanoTime();

      for (int var7 = 0; var7 < this.сС.size(); var7++) {
         WaterMark.AnimatedDigit var8 = this.сС.get(var7);
         float var9 = var1 + ClientFonts.getMediumFont().getFontFace().measureTextWidth(this.Яй.substring(0, var7), Я32, 0.0F);
         float var10 = var8.method_3(var5);
         float var11 = 1.0F - (float)Math.pow(1.0F - var10, Я3ф);
         if (var10 < 1.0F) {
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var8.сс, var9, var4 - var11 * Я3и, 13, ColorMath.withAlpha(var2, 1.0F - var11));
            float var12 = (float)Math.sin(var10 * Я39);
            int var13 = ColorMath.blendArgb(var2, var3, var12 * Я3о);
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var8.сВ, var9, var4 + Я3O - var11 * Я37, 13, ColorMath.withAlpha(var13, var11));
         } else {
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var8.сВ, var9, var4, 13, var2);
         }
      }

      float var14 = var1 + ClientFonts.getMediumFont().getFontFace().measureTextWidth(this.Яй, Я3Р, 0.0F);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), Я33, var14, var4, 13, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
   }

   private boolean method_7Й() {
      long var1 = System.nanoTime();

      for (WaterMark.AnimatedDigit var4 : this.сС) {
         if (var4.method_3(var1) < 1.0F) {
            return true;
         }
      }

      return false;
   }

   private void дЗ(String var1) {
      this.сС.clear();

      for (int var2 = 0; var2 < var1.length(); var2++) {
         this.сС.add(new WaterMark.AnimatedDigit(var1.charAt(var2)));
      }
   }

   private float еЬ() {
      return ClientFonts.getMediumFont().getFontFace().measureTextWidth(this.Яй + " fps", Я3р, 0.0F);
   }

   private void да(String var1) {
      if (var1.length() != this.сС.size()) {
         this.Яй = var1;
         this.дЗ(var1);
      } else {
         long var2 = System.nanoTime();

         for (int var4 = 0; var4 < var1.length(); var4++) {
            this.сС.get(var4).я(var1.charAt(var4), var2);
         }

         this.Яй = var1;
      }
   }

   public void дд(HudRenderEvent var1) {
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(ЯРЖ)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         UiDrawing.setTransform(new Matrix4f());
         this.а6();
         float var10001 = ЯРм
             + sg.render.ClientLogo.width(ЯРI)
            + ClientFonts.getIconsFont().getFontFace().measureTextWidth(ЯРТ, ЯРО, 0.0F)
            + ClientFonts.getIconsFont().getFontFace().measureTextWidth(ЯРФ, ЯРЙ, 0.0F)
            + this.сы.getValue()
            + ClientFonts.getMediumFont().getFontFace().measureTextWidth(ClientMetrics.getPlayerLatencyMillis(MINECRAFT_CLIENT.player) + " ms", ЯРк, 0.0F);
         FontFace var10002 = ClientFonts.getMediumFont().getFontFace();
         Object var10003 = null;
         var10003 = null;
         this.setWidth(var10001 + var10002.measureTextWidth("NowUser", ЯРг, 0.0F));
         this.setHeight(ЯР0);
         float var2 = this.getX();
         float var3 = this.getY();
         this.pushGuiScaleTransform();
          UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont(), sg.render.ClientLogo.font());

         try {
            UiBatchDrawing.drawHudWindowBackground(var2, var3 - 1.0F, this.width, ЯРх, ЯРУ);
             sg.render.ClientLogo.drawCenteredBatched(var2 + ЯРЕ + sg.render.ClientLogo.width(26.0F) / 2.0F,
                 var3 + 16.0F, 24, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
             var2 += ЯР西 + sg.render.ClientLogo.width(ЯРБ);
            UiBatchDrawing.drawRoundedRect(var2, var3 + ЯРС - ЯРз, ЯРц, ЯРя, Я3с, ColorMath.packRgb(108, 108, 108));
            var2 += Я3В;
            GpuTextureView var4 = ClientTextureLookup.Н_dd184401(MINECRAFT_CLIENT.player);
            UiBatchDrawing.drawTextureNearestFlipped(var2, var3 + Я3Я - Я3Х, Я3е, Я3А, new Vector4f(Я34), new Vector4f(Я3п), new Vector4i(-1), var4);
            FontRenderer var10000 = ClientFonts.getMediumFont();
            UiBatchDrawing.drawText(var10000, "NowUser", var2 + Я3Д, var3 + Я3Ъ, 13, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
            FontFace var16 = ClientFonts.getMediumFont().getFontFace();
            Object var17 = null;
            var17 = null;
            var2 += var16.measureTextWidth("NowUser", Я3т, 0.0F) + Я3а;
            UiBatchDrawing.drawRoundedRect(var2, var3 + Я3Ю - Я3Ч, Я3Л, Я3诶, Я31, ColorMath.packRgb(108, 108, 108));
            var2 += Я3Ь;
            UiBatchDrawing.drawText(ClientFonts.getIconsFont(), Я3衣, var2, var3 + Я3ш, 13, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
            this.дШ(var2 + ClientFonts.getIconsFont().getFontFace().measureTextWidth(Я3И, Я3ь, 0.0F) + Я3ж);
            var2 += this.сы.getValue() + Я3弟 + ClientFonts.getIconsFont().getFontFace().measureTextWidth(Я3б, Я3Ы, 0.0F);
            UiBatchDrawing.drawRoundedRect(var2, var3 + Я3l - Я38, Я35, Я3в, Я3ъ, ColorMath.packRgb(108, 108, 108));
            var2 += Я3М;
            UiBatchDrawing.drawText(ClientFonts.getIconsFont(), Я3Н, var2, var3 + Я3н, 13, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
            UiBatchDrawing.drawText(
               ClientFonts.getMediumFont(),
               ClientMetrics.getPlayerLatencyMillis(MINECRAFT_CLIENT.player) + "",
               var2 + ClientFonts.getIconsFont().getFontFace().measureTextWidth(Я3Ц, Я3ч, 0.0F) + Я3Э,
               var3 + Я3必,
               13,
               ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT)
            );
            UiBatchDrawing.drawText(
               ClientFonts.getMediumFont(),
               Я3щ,
               var2
                  + ClientFonts.getMediumFont().getFontFace().measureTextWidth(ClientMetrics.getPlayerLatencyMillis(MINECRAFT_CLIENT.player) + "", Я3Г, 0.0F)
                  + ClientFonts.getIconsFont().getFontFace().measureTextWidth(Я3л, Я3э, 0.0F)
                  + Я3ю,
               var3 + Я3Щ,
               13,
               ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
            );
         } finally {
             UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont(), sg.render.ClientLogo.font());
            this.popGuiScaleTransform();
         }
      }
   }

   @Override
   public boolean ш(float var1, float var2, int var3) {
      return false;
   }

   private void а6() {
      int var1 = MINECRAFT_CLIENT.getFps();
      if (this.Яй.isEmpty()) {
         this.Яй = Integer.toString(var1);
         this.дЗ(this.Яй);
         this.сы.setImmediately(this.еЬ());
      } else {
         this.ДТ = !this.Яй.equals(Integer.toString(var1)) ? var1 : -1;
      }

      if (!this.method_7Й() && this.ДТ >= 0) {
         String var2 = Integer.toString(this.ДТ);
         this.ДТ = -1;
         this.да(var2);
      }

      this.сы.updateTarget(this.еЬ());
   }

   private static final class AnimatedDigit {
      private String сс;
      private String сВ;
      private long са;
      private static final float Я2Н = 1.9E8F;

      private AnimatedDigit(char var1) {
         this.сс = Character.toString(var1);
         this.сВ = this.сс;
         this.са = 0L;
      }

      private float method_3(long var1) {
         return this.са == 0L ? 1.0F : Math.clamp((float)(var1 - this.са) / Я2Н, 0.0F, 1.0F);
      }

      private void я(char var1, long var2) {
         String var4 = Character.toString(var1);
         if (!this.сВ.equals(var4)) {
            this.сс = this.сВ;
            this.сВ = var4;
            this.са = var2;
         }
      }
   }
}
