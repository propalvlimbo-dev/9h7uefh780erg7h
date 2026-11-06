package sg.gui;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import settings.render.ClientLocalization;
import sg.ec.MinecraftAccess;
import sg.ec.ClientFonts;
import sg.modules.impl.misc.PlayerEmotes;
import sg.render.WorldProjection;
import sg.render.PlayerEmoteController;
import sg.render.EmoteDefinition;
import sg.render.pipeline.PotionWheel;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public final class EmoteWheelScreen extends Screen implements MinecraftAccess {
   private static final int ВЬ = 8;
   private static final float ВЙ = (float) (Math.PI * 2);
   @SerializedName("В")
   private final PlayerEmotes В_e0ed8fbb;
   private final int В衣;
   @SerializedName("В")
   private final PotionWheel В_f1d0877d = new PotionWheel();
   private final float[] Х = new float[8];
   private float Вк;
   private float Вг;
   private float В0;
   private float Вх;
   private float ВУ;
   private float ВЗ;
   private float ВЕ;
   private float ВП = 1.0F;
   private int Вш = -1;
   private String сА = "";
   private int ВИ = -1;
   private int Вь;
   private boolean сж;
   private boolean с弟;
   private long сш = System.nanoTime();
   private static final String Лш = "Эмоции";
   private static final float ЛИ = 720.0F;
   private static final float Ль = 0.85F;
   private static final float Лж = 1.55F;
   private static final float Л弟 = 0.5F;
   private static final float Лб = 0.5F;
   private static final float ЛЫ = 174.0F;
   private static final float Лl = 0.008F;
   private static final float Л8 = 255.0F;
   private static final double Л5 = 60.0;
   private static final float Лв = 0.78F;
   private static final float Лъ = 0.22F;
   private static final float ЛМ = 72.0F;
   private static final float ЛН = (float) (Math.PI / 4);
   private static final float Лн = 0.16F;
   private static final float ЛЦ = 7.0F;
   private static final float Лч = 0.002F;
   private static final float ЛЭ = 16.0F;
   private static final float Л必 = 0.7F;
   private static final float Лщ = 0.3F;
   private static final float ЛГ = 6.0F;
   private static final float Лл = (float) (-Math.PI / 2);
   private static final float Лэ = 0.34F;
   private static final float Лю = 0.8F;
   private static final float ЛЩ = 6.5F;
   private static final float Лу = 2.2F;
   private static final float Л2 = 6.5F;
   private static final float Лф = 2.2F;
   private static final float Ли = 0.5F;
   private static final float Л9 = 0.5F;
   private static final float Ло = 4.0F;
   private static final float ЛO = 31.0F;
   private static final float Л7 = 5.0F;
   private static final float ЛР = 0.48F;
   private static final float Л3 = 0.52F;
   private static final float Лр = 0.5F;
   private static final float Л6 = 0.5F;
   private static final float Л_ = 6.0F;
   private static final float ЛШ = 0.25F;
   private static final long Лы = 5000L;
   private static final float Лй = 5000.0F;
   private static final float ЛЖ = (float) (Math.PI * 2);
   private static final float Лм = 0.7F;
   private static final float ЛК = 0.55F;
   private static final float ЛI = 26.0F;
   private static final double ЛТ = 20.0;
   private static final float ЛО = 46.0F;
   private static final float ЛФ = 0.82F;
   private static final float ЛЙ = 0.18F;
   private static final float Лк = 14.0F;
   private static final double Лг = 200.0;
   private static final double Л0 = 150.0;
   private static final float Лх = 12.0F;
   private static final float ЛУ = 14.0F;
   private static final float ЛЗ = 39.0F;
   private static final float ЛЕ = 0.75F;
   private static final float ЛП = 0.25F;
   private static final float Л西 = 0.5F;
   private static final float Лд = 0.62F;
   private static final float ЛБ = 8.0F;
   private static final float ЛС = 11.0F;
   private static final String Лз = "Эмоции";
   private static final float Лц = 0.5F;
   private static final float Ля = 8.0F;
   private static final float 诶с = 8.0F;
   private static final float 诶В = 0.5F;
   private static final float 诶Я = 25.0F;
   private static final float 诶Х = 0.02F;
   private static final float 诶е = 10.0F;
   private static final float 诶А = 28.0F;
   private static final float 诶4 = 24.0F;
   private static final float 诶п = 0.5F;
   private static final float 诶Д = 14.0F;
   private static final float 诶Ъ = 12.0F;
   private static final float 诶т = 0.5F;
   private static final double 诶а = 230.0;
   private static final float 诶Ю = 0.5F;
   private static final double 诶Ч = 100.0;
   private static final float 诶Л = 0.5F;
   private static final float 诶诶 = 0.5F;
   private static final float 诶1 = 0.5F;
   private static final float 诶Ь = 24.0F;
   private static final float 诶衣 = 18.0F;
   private static final float 诶ш = (float) (Math.PI / 2);
   private static final float 诶И = (float) (Math.PI * 2);
   private static final float 诶ь = (float) (Math.PI * 2);
   private static final float 诶ж = 8.0F;
   private static final float 诶弟 = 12.0F;
   private static final float 诶б = 1.0E9F;
   private static final float 诶Ы = 0.05F;
   private static final float 诶l = 12.0F;
   private static final float 诶8 = 8.5F;
   private static final float 诶5 = 0.008F;
   private static final float 诶в = 255.0F;
   private static final float 诶ъ = 3.0F;
   private static final float 诶М = 2.70158F;
   private static final float 诶Н = 1.70158F;

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      float var5 = this.minecraft.getWindow().getScreenWidth();
      float var6 = this.minecraft.getWindow().getScreenHeight();
      this.Вх = Math.clamp(var6 / ЛИ, Ль, Лж);
      this.Вк = Math.round(var5 * Л弟);
      this.Вг = Math.round(var6 * Лб);
      this.В0 = ЛЫ * this.Вх;
      Vector2f var7 = this.clampMouse(WorldProjection.transformScreenPoint(var2, var3));
      this.updateAnimation();
      if (this.сж && this.ВУ < Лl) {
         boolean var9 = this.с弟;
         this.minecraft.gui.setScreen(null);
         if (var9) {
            this.activateSelected();
         }
      } else {
         if (!this.сж) {
            this.ВИ = this.selectedAt(var7.x, var7.y);
         }

         float var8 = this.smooth(this.ВУ);
         UiDrawing.setGlobalAlpha(Л8 * var8);
         this.renderWheel(var1, var8);
         UiDrawing.resetGlobalAlpha();
      }
   }

   private void warpCursor(float var1, float var2) {
      double var3 = var1 * this.minecraft.getWindow().getScreenWidth() / Math.max(1.0, this.minecraft.getWindow().getWidth());
      double var5 = var2 * this.minecraft.getWindow().getScreenHeight() / Math.max(1.0, this.minecraft.getWindow().getHeight());
      GLFW.glfwSetCursorPos(this.minecraft.getWindow().handle(), var3, var5);
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (var1.button() == 0 && this.ВИ >= 0) {
         this.finish(true);
         return true;
      } else if (var1.button() == 1) {
         PlayerEmoteController.必ш();
         this.finish(false);
         return true;
      } else {
         return true;
      }
   }

   @Override
   protected void extractPanorama(GuiGraphicsExtractor var1, float var2) {
   }

   @Override
   protected void extractMenuBackground(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
   }

   @Override
   public void onClose() {
      this.finish(false);
   }

   private float smooth(float var1) {
      float var2 = Math.clamp(var1, 0.0F, 1.0F);
      return var2 * var2 * (诶ъ - 2.0F * var2);
   }

   public boolean renderClosing(GuiGraphicsExtractor var1) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      this.updateAnimation();
      if (this.ВУ < 诶5) {
         if (this.с弟) {
            this.activateSelected();
         }

         return true;
      } else {
         float var2 = this.smooth(this.ВУ);
         UiDrawing.setGlobalAlpha(诶в * var2);
         this.renderWheel(var1, var2);
         UiDrawing.resetGlobalAlpha();
         return false;
      }
   }

   private Vector2f clampMouse(Vector2f var1) {
      float var2 = var1.x - this.Вк;
      float var3 = var1.y - this.Вг;
      float var4 = (float)Math.sqrt(var2 * var2 + var3 * var3);
      float var5 = this.В0 + 诶弟 * this.Вх;
      if (!(var4 <= var5) && var4 != 0.0F) {
         float var6 = this.Вк + var2 / var4 * var5;
         float var7 = this.Вг + var3 / var4 * var5;
         this.warpCursor(var6, var7);
         return new Vector2f(var6, var7);
      } else {
         return var1;
      }
   }

   @Override
   public boolean keyReleased(KeyEvent var1) {
      if (var1.key() == this.В衣) {
         this.finish(true);
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   private static int pageCount() {
      return (EmoteDefinition.ALL.size() + 8 - 1) / 8;
   }

   public void finish(boolean var1) {
      if (!this.сж) {
         this.сж = true;
         this.с弟 = var1;
         this.В_e0ed8fbb.р(this);
         this.minecraft.gui.setScreen(null);
      }
   }

   private void updateAnimation() {
      long var1 = System.nanoTime();
      this.ВЗ = Math.clamp((float)(var1 - this.сш) / 诶б, 0.0F, 诶Ы);
      this.сш = var1;
      float var3 = this.сж ? 0.0F : 1.0F;
      float var4 = this.сж ? 诶l : 诶8;
      this.ВУ = this.ВУ + (var3 - this.ВУ) * (1.0F - (float)Math.exp(-this.ВЗ * var4));
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.сж && var7 != 0.0) {
         this.Вь = Math.floorMod(this.Вь + (var7 < 0.0 ? 1 : -1), pageCount());
         this.ВИ = -1;
         this.Вш = -1;
         this.ВЕ = 0.0F;

         for (int var9 = 0; var9 < this.Х.length; var9++) {
            this.Х[var9] = 0.0F;
         }
      }

      return true;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (var1.key() == 256) {
         this.finish(false);
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   private void activateSelected() {
      int var1 = this.Вь * 8 + this.ВИ;
      if (this.ВИ >= 0 && var1 >= 0 && var1 < EmoteDefinition.ALL.size()) {
         PlayerEmoteController.衣(EmoteDefinition.ALL.get(var1));
      }
   }

   private int selectedAt(float var1, float var2) {
      float var3 = var1 - this.Вк;
      float var4 = var2 - this.Вг;
      float var5 = (float)Math.sqrt(var3 * var3 + var4 * var4);
      if (!(var5 < 诶Ь * this.Вх) && !(var5 > this.В0 + 诶衣 * this.Вх)) {
         float var6 = (float)Math.atan2(var4, var3) + 诶ш;
         if (var6 < 0.0F) {
            var6 += 诶И;
         }

         int var7 = Math.min(7, (int)(var6 / 诶ь * 诶ж));
         return this.Вь * 8 + var7 < EmoteDefinition.ALL.size() ? var7 : -1;
      } else {
         return -1;
      }
   }

   private int animatedColor(int var1, int var2, int var3, int var4, float var5) {
      return ColorMath.packArgb(var1, var2, var3, var4 * Math.clamp(var5, 0.0F, 1.0F));
   }

   private void renderWheel(GuiGraphicsExtractor var1, float var2) {
      float var3 = this.minecraft.getWindow().getScreenWidth();
      float var4 = this.minecraft.getWindow().getScreenHeight();
      int var5 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int var6 = this.Вь * 8;
      int var7 = Math.min(8, EmoteDefinition.ALL.size() - var6);
      UiDrawing.drawRoundedRect(0.0F, 0.0F, var3, var4, 0.0F, ColorMath.packArgb(6, 9, 16, Л5));
      float var8 = this.outBack(var2);
      float var9 = this.В0 * (Лв + Лъ * var8);
      float var10 = ЛМ * this.Вх;
      float var11 = ЛН;
      this.В_f1d0877d.чр();

      for (int var12 = 0; var12 < 8; var12++) {
         float var13 = Лн * var12 / ЛЦ;
         float var14 = Math.clamp((this.ВУ - var13) / (1.0F - var13), 0.0F, 1.0F);
         float var15 = this.smooth(var14);
         if (!(var15 < Лч)) {
            float var16 = this.outBack(var14);
            float var17 = var12 == this.ВИ ? 1.0F : 0.0F;
            this.Х[var12] = this.Х[var12] + (var17 - this.Х[var12]) * (1.0F - (float)Math.exp(-this.ВЗ * ЛЭ));
            float var18 = this.Х[var12];
            float var19 = Л必 + Лщ * var16;
            float var20 = var10 * var19;
            float var21 = var9 * var19 + var18 * ЛГ * this.Вх;
            float var22 = Лл + var12 * var11 - (1.0F - var15) * Лэ;
            boolean var23 = var12 < var7;
            int var24 = ColorMath.lerpArgb(
               var23 ? this.animatedColor(222, 229, 242, 230, var15) : this.animatedColor(150, 158, 174, 150, var15),
               this.animatedColor(248, 251, 255, 245, var15),
               var18
            );
            int var25 = ColorMath.lerpArgb(
               var23 ? this.animatedColor(7, 11, 19, 150, var15) : this.animatedColor(7, 11, 19, 72, var15), ColorMath.withAlpha(var5, Лю * var15), var18
            );
            this.В_f1d0877d.method_0(this.Вк, this.Вг, var20, var21, var22, var11, ЛЩ * this.Вх, Лу * this.Вх, var24);
            this.В_f1d0877d.l(this.Вк, this.Вг, var20, var21, var22, var11, Л2 * this.Вх, Лф * this.Вх, var25);
            if (var23) {
               EmoteDefinition var26 = EmoteDefinition.ALL.get(var6 + var12);
               float var27 = var22 + var11 * Ли;
               float var28 = (var20 + var21) * Л9 + var18 * Ло * this.Вх;
               float var29 = (ЛO + var18 * Л7) * this.Вх * (ЛР + Л3 * var16);
               float var30 = this.Вк + (float)Math.cos(var27) * var28 - var29 * Лр;
               float var31 = this.Вг + (float)Math.sin(var27) * var28 - var29 * Л6;
               UiDrawing.drawTexture(var30, var31, var29, var29, Л_ * this.Вх, var18 > ЛШ ? var5 : -1, var26.icon());
            }
         }
      }

      float var32 = (float)(System.currentTimeMillis() % Лы) / Лй * ЛЖ;
      this.В_f1d0877d.l(this.Вк, this.Вг, var10 * Лм, var9, var32, ЛК, 0.0F, ЛI * this.Вх, ColorMath.packArgb(255, 255, 255, ЛТ));
      float var33 = ЛО * this.Вх * (ЛФ + ЛЙ * var8);
      UiDrawing.drawBlur(this.Вк - var33, this.Вг - var33, var33 * 2.0F, var33 * 2.0F, Лк, var33, ColorMath.packArgb(222, 229, 242, Лг));
      UiDrawing.drawRoundedRect(this.Вк - var33, this.Вг - var33, var33 * 2.0F, var33 * 2.0F, var33, ColorMath.packArgb(7, 11, 19, Л0));
      if (this.ВИ != this.Вш) {
         this.Вш = this.ВИ;
         if (this.ВИ >= 0) {
            this.ВП = 0.0F;
         }
      }

      this.ВП = this.ВП + (1.0F - this.ВП) * (1.0F - (float)Math.exp(-this.ВЗ * Лх));
      EmoteDefinition var34 = this.ВИ >= 0 && this.ВИ < var7 ? EmoteDefinition.ALL.get(var6 + this.ВИ) : null;
      if (var34 != null) {
         this.сА = var34.title();
      }

      float var35 = var34 == null ? 0.0F : 1.0F;
      this.ВЕ = this.ВЕ + (var35 - this.ВЕ) * (1.0F - (float)Math.exp(-this.ВЗ * ЛУ));
      if (var34 != null) {
         float var36 = this.smooth(this.ВП);
         float var39 = ЛЗ * this.Вх * (ЛЕ + ЛП * this.outBack(var36));
         UiDrawing.drawTexture(this.Вк - var39 * Л西, this.Вг - var39 * Лд, var39, var39, ЛБ * this.Вх, var5, var34.icon());
      } else {
         int var37 = Math.max(10, Math.round(ЛС * this.Вх));
         String var40 = ClientLocalization.translate(Лз);
         float var42 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var40, var37);
         UiDrawing.drawText(ClientFonts.getMediumFont(), var40, this.Вк - var42 * Лц, this.Вг - Ля * this.Вх, var37, ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT));
      }

      String var38 = this.Вь + 1 + " / " + pageCount();
      int var41 = Math.max(8, Math.round(诶с * this.Вх));
      float var43 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var38, var41);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var38, this.Вк - var43 * 诶В, this.Вг + 诶Я * this.Вх, var41, ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT));
      if (this.ВЕ > 诶Х && !this.сА.isEmpty()) {
         int var44 = Math.max(9, Math.round(诶е * this.Вх));
         float var45 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), ClientLocalization.translate(this.сА), var44);
         float var46 = var45 + 诶А * this.Вх;
         float var47 = 诶4 * this.Вх;
         float var48 = this.Вк - var46 * 诶п;
         float var49 = this.Вг + this.В0 + 诶Д * this.Вх;
         float var50 = UiDrawing.getGlobalAlpha();
         UiDrawing.setGlobalAlpha(var50 * this.ВЕ);
         UiDrawing.drawBlur(var48, var49, var46, var47, 诶Ъ, var47 * 诶т, ColorMath.packArgb(222, 229, 242, 诶а));
         UiDrawing.drawRoundedRect(var48, var49, var46, var47, var47 * 诶Ю, ColorMath.packArgb(7, 11, 19, 诶Ч));
         UiDrawing.drawText(
            ClientFonts.getMediumFont(),
            ClientLocalization.translate(this.сА),
            this.Вк - var45 * 诶Л,
            var49 + var47 * 诶诶 - var44 * 诶1 - 1.0F,
            var44,
            ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT)
         );
         UiDrawing.setGlobalAlpha(var50);
      }
   }

   public EmoteWheelScreen(PlayerEmotes var1, int var2) {
      super(Component.literal(ClientLocalization.translate(Лш)));
      this.В_e0ed8fbb = var1;
      this.В衣 = var2;
   }

   private float outBack(float var1) {
      float var2 = Math.clamp(var1, 0.0F, 1.0F) - 1.0F;
      return 1.0F + 诶М * var2 * var2 * var2 + 诶Н * var2 * var2;
   }
}
