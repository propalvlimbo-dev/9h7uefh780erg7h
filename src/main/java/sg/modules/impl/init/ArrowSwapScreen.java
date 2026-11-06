package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import sg.ec.MinecraftAccess;
import sg.ec.ClientFonts;
import sg.gui.ArrowSelectorScreen;
import sg.render.WorldProjection;
import sg.render.pipeline.PotionWheel;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.impl.combat.ArrowSwap;

public final class ArrowSwapScreen extends Screen implements MinecraftAccess {
   private static final float аы = (float) (Math.PI * 2);
   @SerializedName("Х")
   private final ArrowSwap Х_2dd7c9af;
   private final int Ъи;
   @SerializedName("Х")
   private final PotionWheel Х_f1d0877d = new PotionWheel();
   private final float[] ю = new float[6];
   private float ай;
   private float аЖ;
   private float ам;
   private float аК;
   private float аI;
   private float аТ;
   private float аО;
   private float аФ = 1.0F;
   private int Ъ9 = -1;
   private String ХЯ = "";
   private int Ъо = -1;
   private boolean Ан;
   private boolean АЦ;
   private long е4 = System.nanoTime();
   private static final String ЯЖщ = "ArrowSwap";
   private static final float ЯЖГ = 720.0F;
   private static final float ЯЖл = 0.85F;
   private static final float ЯЖэ = 1.55F;
   private static final float ЯЖю = 0.5F;
   private static final float ЯЖЩ = 0.5F;
   private static final float ЯЖу = 132.0F;
   private static final float ЯЖ2 = 0.008F;
   private static final float ЯЖф = 255.0F;
   private static final float ЯЖи = 0.78F;
   private static final float ЯЖ9 = 0.22F;
   private static final float ЯЖо = 70.0F;
   private static final float ЯЖO = (float) (Math.PI * 2);
   private static final float ЯЖ7 = 0.16F;
   private static final float ЯЖР = 0.002F;
   private static final float ЯЖ3 = 16.0F;
   private static final float ЯЖр = 0.7F;
   private static final float ЯЖ6 = 0.3F;
   private static final float ЯЖ_ = 6.0F;
   private static final float ЯЖШ = (float) (-Math.PI / 2);
   private static final float ЯЖы = 0.34F;
   private static final float ЯЖй = 0.5F;
   private static final float ЯЖЖ = 0.5F;
   private static final float ЯЖм = 4.0F;
   private static final float ЯЖК = 1.38F;
   private static final float ЯЖI = 0.32F;
   private static final float ЯЖТ = 0.48F;
   private static final float ЯЖО = 0.52F;
   private static final float ЯЖФ = 16.0F;
   private static final String ЯЖЙ = "+";
   private static final float ЯЖк = 0.5F;
   private static final float ЯЖг = 8.0F;
   private static final float ЯЖ0 = 8.0F;
   private static final float ЯЖх = 8.0F;
   private static final float ЯЖУ = 46.0F;
   private static final float ЯЖЗ = 0.82F;
   private static final float ЯЖЕ = 0.18F;
   private static final float ЯЖП = 12.0F;
   private static final float ЯЖ西 = 14.0F;
   private static final float ЯЖд = 1.55F;
   private static final float ЯЖБ = 0.75F;
   private static final float ЯЖС = 0.25F;
   private static final float ЯЖз = 8.0F;
   private static final float ЯЖц = 8.0F;
   private static final float ЯЖя = 0.02F;
   private static final float Ямс = 10.0F;
   private static final float ЯмВ = 28.0F;
   private static final float ЯмЯ = 24.0F;
   private static final float ЯмХ = 0.5F;
   private static final float Яме = 14.0F;
   private static final float ЯмА = 12.0F;
   private static final float Ям4 = 0.5F;
   private static final double Ямп = 230.0;
   private static final float ЯмД = 0.5F;
   private static final double ЯмЪ = 100.0;
   private static final float Ямт = 0.5F;
   private static final float Яма = 0.5F;
   private static final float ЯмЮ = 0.5F;
   private static final float ЯмЧ = 24.0F;
   private static final float ЯмЛ = 18.0F;
   private static final float Ям诶 = (float) (Math.PI / 2);
   private static final float Ям1 = (float) (Math.PI * 2);
   private static final float ЯмЬ = (float) (Math.PI * 2);
   private static final float Ям衣 = 12.0F;
   private static final float Ямш = 1.0E9F;
   private static final float ЯмИ = 0.05F;
   private static final float Ямь = 12.0F;
   private static final float Ямж = 8.5F;
   private static final float Ям弟 = 0.008F;
   private static final float Ямб = 255.0F;
   private static final float ЯмЫ = 3.0F;
   private static final float Ямl = 2.70158F;
   private static final float Ям8 = 1.70158F;

   public void finish(boolean var1) {
      if (!this.Ан) {
         this.Ан = true;
         this.АЦ = var1;
         this.Х_2dd7c9af.Ш(this);
         this.minecraft.gui.setScreen(null);
      }
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      float var5 = this.minecraft.getWindow().getScreenWidth();
      float var6 = this.minecraft.getWindow().getScreenHeight();
      this.аК = Math.clamp(var6 / ЯЖГ, ЯЖл, ЯЖэ);
      this.ай = Math.round(var5 * ЯЖю);
      this.аЖ = Math.round(var6 * ЯЖЩ);
      this.ам = ЯЖу * this.аК;
      Vector2f var7 = this.clampMouse(WorldProjection.transformScreenPoint(var2, var3));
      this.updateAnimation();
      if (this.Ан && this.аI < ЯЖ2) {
         int var10 = this.Ъо;
         boolean var9 = this.АЦ;
         this.minecraft.gui.setScreen(null);
         if (var9 && var10 >= 0) {
            this.Х_2dd7c9af.С(var10);
         }
      } else {
         if (!this.Ан) {
            this.Ъо = this.selectedAt(var7.x, var7.y);
         }

         float var8 = this.smooth(this.аI);
         UiDrawing.setGlobalAlpha(ЯЖф * var8);
         this.renderWheel(var1, var8);
         UiDrawing.resetGlobalAlpha();
      }
   }

   @Override
   public void onClose() {
      this.finish(false);
   }

   private void warpCursor(float var1, float var2) {
      double var3 = var1 * this.minecraft.getWindow().getScreenWidth() / Math.max(1.0, this.minecraft.getWindow().getWidth());
      double var5 = var2 * this.minecraft.getWindow().getScreenHeight() / Math.max(1.0, this.minecraft.getWindow().getHeight());
      GLFW.glfwSetCursorPos(this.minecraft.getWindow().handle(), var3, var5);
   }

   private void renderWheel(GuiGraphicsExtractor var1, float var2) {
      float var3 = this.minecraft.getWindow().getScreenWidth();
      float var4 = this.minecraft.getWindow().getScreenHeight();
      int var5 = this.Х_2dd7c9af.诶г();
      ItemWheelRenderer.а(var3, var4);
      float var6 = this.outBack(var2);
      float var7 = this.ам * (ЯЖи + ЯЖ9 * var6);
      float var8 = ЯЖо * this.аК;
      float var9 = ЯЖO / var5;
      this.Х_f1d0877d.чр();

      for (int var10 = 0; var10 < var5; var10++) {
         float var11 = var5 <= 1 ? 0.0F : ЯЖ7 * var10 / (var5 - 1.0F);
         float var12 = Math.clamp((this.аI - var11) / (1.0F - var11), 0.0F, 1.0F);
         float var13 = this.smooth(var12);
         if (!(var13 < ЯЖР)) {
            float var14 = this.outBack(var12);
            float var15 = var10 == this.Ъо ? 1.0F : 0.0F;
            this.ю[var10] = this.ю[var10] + (var15 - this.ю[var10]) * (1.0F - (float)Math.exp(-this.аТ * ЯЖ3));
            float var16 = this.ю[var10];
            float var17 = ЯЖр + ЯЖ6 * var14;
            float var18 = var8 * var17;
            float var19 = var7 * var17 + var16 * ЯЖ_ * this.аК;
            float var20 = ЯЖШ + var10 * var9 - (1.0F - var13) * ЯЖы;
            boolean var21 = this.Х_2dd7c9af.с_bbeebd87(var10);
            float var22 = var20 + var9 * ЯЖй;
            ItemWheelRenderer.К(this.Х_f1d0877d, this.ай, this.аЖ, var18, var19, var20, var9, var21, var13, var16, this.аК);
            float var23 = (var18 + var19) * ЯЖЖ + var16 * ЯЖм * this.аК;
            float var24 = this.ай + (float)Math.cos(var22) * var23;
            float var25 = this.аЖ + (float)Math.sin(var22) * var23;
            ItemStack var26 = this.Х_2dd7c9af.ч(var10);
            float var27 = this.аК * (ЯЖК + var16 * ЯЖI) * (ЯЖТ + ЯЖО * var14);
            if (var26.isEmpty()) {
               int var28 = Math.max(12, Math.round(ЯЖФ * this.аК));
               String var29 = ЯЖЙ;
               UiDrawing.drawText(
                  ClientFonts.getMediumFont(),
                  var29,
                  var24 - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var29, var28) * ЯЖк,
                  var25 - ЯЖг * this.аК,
                  var28,
                  ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), var13)
               );
            } else {
               UiDrawing.drawItem(var1, var26, var24 - ЯЖ0 * var27, var25 - ЯЖх * var27, var27, false);
            }
         }
      }

      float var30 = ЯЖУ * this.аК * (ЯЖЗ + ЯЖЕ * var6);
      ItemWheelRenderer.Ф(this.ай, this.аЖ, var30, this.аК, var2);
      if (this.Ъо != this.Ъ9) {
         this.Ъ9 = this.Ъо;
         if (this.Ъо >= 0) {
            this.аФ = 0.0F;
         }
      }

      this.аФ = this.аФ + (1.0F - this.аФ) * (1.0F - (float)Math.exp(-this.аТ * ЯЖП));
      ItemStack var31 = this.Ъо >= 0 ? this.Х_2dd7c9af.ч(this.Ъо) : ItemStack.EMPTY;
      if (!var31.isEmpty()) {
         this.ХЯ = var31.getHoverName().getString();
      }

      float var32 = var31.isEmpty() ? 0.0F : 1.0F;
      this.аО = this.аО + (var32 - this.аО) * (1.0F - (float)Math.exp(-this.аТ * ЯЖ西));
      if (!var31.isEmpty()) {
         float var33 = this.smooth(this.аФ);
         float var35 = ЯЖд * this.аК * (ЯЖБ + ЯЖС * this.outBack(var33));
         UiDrawing.drawItem(var1, var31, this.ай - ЯЖз * var35, this.аЖ - ЯЖц * var35, var35, false);
      }

      if (this.аО > ЯЖя && !this.ХЯ.isEmpty()) {
         int var34 = Math.max(9, Math.round(Ямс * this.аК));
         float var36 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.ХЯ, var34);
         float var37 = var36 + ЯмВ * this.аК;
         float var38 = ЯмЯ * this.аК;
         float var39 = this.ай - var37 * ЯмХ;
         float var40 = this.аЖ + this.ам + Яме * this.аК;
         float var41 = UiDrawing.getGlobalAlpha();
         UiDrawing.setGlobalAlpha(var41 * this.аО);
         UiDrawing.drawBlur(var39, var40, var37, var38, ЯмА, var38 * Ям4, ColorMath.packArgb(222, 229, 242, Ямп));
         UiDrawing.drawRoundedRect(var39, var40, var37, var38, var38 * ЯмД, ColorMath.packArgb(7, 11, 19, ЯмЪ));
         UiDrawing.drawText(
            ClientFonts.getMediumFont(),
            this.ХЯ,
            this.ай - var36 * Ямт,
            var40 + var38 * Яма - var34 * ЯмЮ - 1.0F,
            var34,
            this.Ъо >= 0 && !this.Х_2dd7c9af.с_bbeebd87(this.Ъо) ? ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT) : ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT)
         );
         UiDrawing.setGlobalAlpha(var41);
      }
   }

   @Override
   public boolean keyReleased(KeyEvent var1) {
      if (var1.key() == this.Ъи) {
         this.finish(true);
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   @Override
   protected void extractMenuBackground(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (var1.button() == 0 && this.Ъо >= 0) {
         this.minecraft.gui.setScreen(new ArrowSelectorScreen(this.Х_2dd7c9af, this.Ъо, this, this.Ъи));
         return true;
      }

      if (var1.button() == 0) {
         this.Ъо = this.Х_2dd7c9af.诶Ф();
         return true;
      }

      if (var1.button() == 1) {
         this.Х_2dd7c9af.т(this.Ъо);
         if (this.Ъо >= this.Х_2dd7c9af.诶г()) {
            this.Ъо = this.Х_2dd7c9af.诶г() - 1;
         }

         return true;
      } else {
         return true;
      }
   }

   private float outBack(float var1) {
      float var2 = Math.clamp(var1, 0.0F, 1.0F) - 1.0F;
      return 1.0F + Ямl * var2 * var2 * var2 + Ям8 * var2 * var2;
   }

   private float smooth(float var1) {
      float var2 = Math.clamp(var1, 0.0F, 1.0F);
      return var2 * var2 * (ЯмЫ - 2.0F * var2);
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   public ArrowSwapScreen(ArrowSwap var1, int var2) {
      super(Component.literal(ЯЖщ));
      this.Х_2dd7c9af = var1;
      this.Ъи = var2;
   }

   public boolean renderClosing(GuiGraphicsExtractor var1) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      this.updateAnimation();
      if (this.аI < Ям弟) {
         if (this.АЦ && this.Ъо >= 0) {
            this.Х_2dd7c9af.С(this.Ъо);
         }

         return true;
      } else {
         float var2 = this.smooth(this.аI);
         UiDrawing.setGlobalAlpha(Ямб * var2);
         this.renderWheel(var1, var2);
         UiDrawing.resetGlobalAlpha();
         return false;
      }
   }

   private int selectedAt(float var1, float var2) {
      int var3 = this.Х_2dd7c9af.诶г();
      if (var3 <= 0) {
         return -1;
      }

      float var4 = var1 - this.ай;
      float var5 = var2 - this.аЖ;
      float var6 = (float)Math.sqrt(var4 * var4 + var5 * var5);
      if (!(var6 < ЯмЧ * this.аК) && !(var6 > this.ам + ЯмЛ * this.аК)) {
         float var7 = (float)Math.atan2(var5, var4) + Ям诶;
         if (var7 < 0.0F) {
            var7 += Ям1;
         }

         return Math.min(var3 - 1, (int)(var7 / ЯмЬ * var3));
      } else {
         return -1;
      }
   }

   private Vector2f clampMouse(Vector2f var1) {
      float var2 = var1.x - this.ай;
      float var3 = var1.y - this.аЖ;
      float var4 = (float)Math.sqrt(var2 * var2 + var3 * var3);
      float var5 = this.ам + Ям衣 * this.аК;
      if (!(var4 <= var5) && var4 != 0.0F) {
         float var6 = this.ай + var2 / var4 * var5;
         float var7 = this.аЖ + var3 / var4 * var5;
         this.warpCursor(var6, var7);
         return new Vector2f(var6, var7);
      } else {
         return var1;
      }
   }

   @Override
   protected void extractPanorama(GuiGraphicsExtractor var1, float var2) {
   }

   private int animatedColor(int var1, int var2, int var3, int var4, float var5) {
      return ColorMath.packArgb(var1, var2, var3, var4 * Math.clamp(var5, 0.0F, 1.0F));
   }

   private void updateAnimation() {
      long var1 = System.nanoTime();
      this.аТ = Math.clamp((float)(var1 - this.е4) / Ямш, 0.0F, ЯмИ);
      this.е4 = var1;
      float var3 = this.Ан ? 0.0F : 1.0F;
      float var4 = this.Ан ? Ямь : Ямж;
      this.аI = this.аI + (var3 - this.аI) * (1.0F - (float)Math.exp(-this.аТ * var4));
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
}
