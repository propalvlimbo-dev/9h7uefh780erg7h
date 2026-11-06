package sg.modules.impl.init;

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
import sg.gui.SwapItemSelectorScreen;
import sg.render.WorldProjection;
import sg.render.pipeline.PotionWheel;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.impl.combat.AutoSwap;

public final class AutoSwapScreen extends Screen implements MinecraftAccess {
   private static final int Ъй = 3;
   private static final float Юа = (float) (Math.PI * 2);
   private final AutoSwap Х;
   private final int ЪЖ;
   private final PotionWheel е = new PotionWheel();
   private final float[] Щ = new float[3];
   private float ЮЮ;
   private float ЮЧ;
   private float ЮЛ;
   private float Ю诶;
   private float Ю1;
   private float ЮЬ;
   private float Ю衣;
   private float Юш = 1.0F;
   private int Ъм = -1;
   private String ХЪ = "";
   private int ЪК = -1;
   private boolean АР;
   private boolean А3;
   private long еа = System.nanoTime();
   private static final String ха = "AutoSwap";
   private static final float хЮ = 720.0F;
   private static final float хЧ = 0.85F;
   private static final float хЛ = 1.55F;
   private static final float х诶 = 0.5F;
   private static final float х1 = 0.5F;
   private static final float хЬ = 132.0F;
   private static final float х衣 = 0.008F;
   private static final float хш = 255.0F;
   private static final float хИ = 0.78F;
   private static final float хь = 0.22F;
   private static final float хж = 70.0F;
   private static final float х弟 = (float) (Math.PI * 2.0 / 3.0);
   private static final float хб = 0.16F;
   private static final float хЫ = 0.002F;
   private static final float хl = 16.0F;
   private static final float х8 = 0.7F;
   private static final float х5 = 0.3F;
   private static final float хв = 6.0F;
   private static final float хъ = (float) (-Math.PI / 2);
   private static final float хМ = 0.34F;
   private static final float хН = 0.5F;
   private static final float хн = 0.5F;
   private static final float хЦ = 4.0F;
   private static final float хч = 1.38F;
   private static final float хЭ = 0.32F;
   private static final float х必 = 0.48F;
   private static final float хщ = 0.52F;
   private static final float хГ = 16.0F;
   private static final String хл = "+";
   private static final float хэ = 0.5F;
   private static final float хю = 8.0F;
   private static final float хЩ = 8.0F;
   private static final float ху = 8.0F;
   private static final float х2 = 46.0F;
   private static final float хф = 0.82F;
   private static final float хи = 0.18F;
   private static final float х9 = 12.0F;
   private static final float хо = 14.0F;
   private static final float хO = 1.55F;
   private static final float х7 = 0.75F;
   private static final float хР = 0.25F;
   private static final float х3 = 8.0F;
   private static final float хр = 8.0F;
   private static final float х6 = 0.02F;
   private static final float х_ = 10.0F;
   private static final float хШ = 28.0F;
   private static final float хы = 24.0F;
   private static final float хй = 0.5F;
   private static final float хЖ = 14.0F;
   private static final float хм = 12.0F;
   private static final float хК = 0.5F;
   private static final double хI = 230.0;
   private static final float хТ = 0.5F;
   private static final double хО = 100.0;
   private static final float хФ = 0.5F;
   private static final float хЙ = 0.5F;
   private static final float хк = 0.5F;
   private static final float хг = 24.0F;
   private static final float х0 = 18.0F;
   private static final float хх = (float) (Math.PI / 2);
   private static final float хУ = (float) (Math.PI * 2);
   private static final float хЗ = (float) (Math.PI * 2);
   private static final float хЕ = 3.0F;
   private static final float хП = 12.0F;
   private static final float х西 = 1.0E9F;
   private static final float хд = 0.05F;
   private static final float хБ = 12.0F;
   private static final float хС = 8.5F;
   private static final float хз = 0.008F;
   private static final float хц = 255.0F;
   private static final float хя = 3.0F;
   private static final float Ус = 2.70158F;
   private static final float УВ = 1.70158F;

   private Vector2f clampMouse(Vector2f var1) {
      float var2 = var1.x - this.ЮЮ;
      float var3 = var1.y - this.ЮЧ;
      float var4 = (float)Math.sqrt(var2 * var2 + var3 * var3);
      float var5 = this.ЮЛ + хП * this.Ю诶;
      if (!(var4 <= var5) && var4 != 0.0F) {
         float var6 = this.ЮЮ + var2 / var4 * var5;
         float var7 = this.ЮЧ + var3 / var4 * var5;
         this.warpCursor(var6, var7);
         return new Vector2f(var6, var7);
      } else {
         return var1;
      }
   }

   @Override
   public void onClose() {
      this.finish(false);
   }

   @Override
   protected void extractMenuBackground(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
   }

   private float smooth(float var1) {
      float var2 = Math.clamp(var1, 0.0F, 1.0F);
      return var2 * var2 * (хя - 2.0F * var2);
   }

   private int animatedColor(int var1, int var2, int var3, int var4, float var5) {
      return ColorMath.packArgb(var1, var2, var3, var4 * Math.clamp(var5, 0.0F, 1.0F));
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

   public void finish(boolean var1) {
      if (!this.АР) {
         this.АР = true;
         this.А3 = var1;
         this.Х.Э(this);
         this.minecraft.gui.setScreen(null);
      }
   }

   @Override
   protected void extractPanorama(GuiGraphicsExtractor var1, float var2) {
   }

   private void updateAnimation() {
      long var1 = System.nanoTime();
      this.ЮЬ = Math.clamp((float)(var1 - this.еа) / х西, 0.0F, хд);
      this.еа = var1;
      float var3 = this.АР ? 0.0F : 1.0F;
      float var4 = this.АР ? хБ : хС;
      this.Ю1 = this.Ю1 + (var3 - this.Ю1) * (1.0F - (float)Math.exp(-this.ЮЬ * var4));
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      float var5 = this.minecraft.getWindow().getScreenWidth();
      float var6 = this.minecraft.getWindow().getScreenHeight();
      this.Ю诶 = Math.clamp(var6 / хЮ, хЧ, хЛ);
      this.ЮЮ = Math.round(var5 * х诶);
      this.ЮЧ = Math.round(var6 * х1);
      this.ЮЛ = хЬ * this.Ю诶;
      Vector2f var7 = this.clampMouse(WorldProjection.transformScreenPoint(var2, var3));
      this.updateAnimation();
      if (this.АР && this.Ю1 < х衣) {
         int var10 = this.ЪК;
         boolean var9 = this.А3;
         this.minecraft.gui.setScreen(null);
         if (var9 && var10 >= 0) {
            this.Х.с_de5a6f78(var10);
         }
      } else {
         if (!this.АР) {
            this.ЪК = this.selectedAt(var7.x, var7.y);
         }

         float var8 = this.smooth(this.Ю1);
         UiDrawing.setGlobalAlpha(хш * var8);
         this.renderWheel(var1, var8);
         UiDrawing.resetGlobalAlpha();
      }
   }

   private void renderWheel(GuiGraphicsExtractor var1, float var2) {
      float var3 = this.minecraft.getWindow().getScreenWidth();
      float var4 = this.minecraft.getWindow().getScreenHeight();
      ItemWheelRenderer.а(var3, var4);
      float var5 = this.outBack(var2);
      float var6 = this.ЮЛ * (хИ + хь * var5);
      float var7 = хж * this.Ю诶;
      float var8 = х弟;
      this.е.чр();

      for (int var9 = 0; var9 < 3; var9++) {
         float var10 = хб * var9 / 2.0F;
         float var11 = Math.clamp((this.Ю1 - var10) / (1.0F - var10), 0.0F, 1.0F);
         float var12 = this.smooth(var11);
         if (!(var12 < хЫ)) {
            float var13 = this.outBack(var11);
            float var14 = var9 == this.ЪК ? 1.0F : 0.0F;
            this.Щ[var9] = this.Щ[var9] + (var14 - this.Щ[var9]) * (1.0F - (float)Math.exp(-this.ЮЬ * хl));
            float var15 = this.Щ[var9];
            float var16 = х8 + х5 * var13;
            float var17 = var7 * var16;
            float var18 = var6 * var16 + var15 * хв * this.Ю诶;
            float var19 = хъ + var9 * var8 - (1.0F - var12) * хМ;
            boolean var20 = this.Х.У(var9);
            float var21 = var19 + var8 * хН;
            ItemWheelRenderer.К(this.е, this.ЮЮ, this.ЮЧ, var17, var18, var19, var8, var20, var12, var15, this.Ю诶);
            float var22 = (var17 + var18) * хн + var15 * хЦ * this.Ю诶;
            float var23 = this.ЮЮ + (float)Math.cos(var21) * var22;
            float var24 = this.ЮЧ + (float)Math.sin(var21) * var22;
            ItemStack var25 = this.Х.ц(var9);
            float var26 = this.Ю诶 * (хч + var15 * хЭ) * (х必 + хщ * var13);
            if (var25.isEmpty()) {
               int var27 = Math.max(12, Math.round(хГ * this.Ю诶));
               String var28 = хл;
               UiDrawing.drawText(
                  ClientFonts.getMediumFont(),
                  var28,
                  var23 - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var28, var27) * хэ,
                  var24 - хю * this.Ю诶,
                  var27,
                  ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), var12)
               );
            } else {
               UiDrawing.drawItem(var1, var25, var23 - хЩ * var26, var24 - ху * var26, var26, false);
            }
         }
      }

      float var29 = х2 * this.Ю诶 * (хф + хи * var5);
      ItemWheelRenderer.Ф(this.ЮЮ, this.ЮЧ, var29, this.Ю诶, var2);
      if (this.ЪК != this.Ъм) {
         this.Ъм = this.ЪК;
         if (this.ЪК >= 0) {
            this.Юш = 0.0F;
         }
      }

      this.Юш = this.Юш + (1.0F - this.Юш) * (1.0F - (float)Math.exp(-this.ЮЬ * х9));
      ItemStack var30 = this.ЪК >= 0 ? this.Х.ц(this.ЪК) : ItemStack.EMPTY;
      if (!var30.isEmpty()) {
         this.ХЪ = var30.getHoverName().getString();
      }

      float var31 = var30.isEmpty() ? 0.0F : 1.0F;
      this.Ю衣 = this.Ю衣 + (var31 - this.Ю衣) * (1.0F - (float)Math.exp(-this.ЮЬ * хо));
      if (!var30.isEmpty()) {
         float var32 = this.smooth(this.Юш);
         float var34 = хO * this.Ю诶 * (х7 + хР * this.outBack(var32));
         UiDrawing.drawItem(var1, var30, this.ЮЮ - х3 * var34, this.ЮЧ - хр * var34, var34, false);
      }

      if (this.Ю衣 > х6 && !this.ХЪ.isEmpty()) {
         int var33 = Math.max(9, Math.round(х_ * this.Ю诶));
         float var35 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.ХЪ, var33);
         float var36 = var35 + хШ * this.Ю诶;
         float var37 = хы * this.Ю诶;
         float var38 = this.ЮЮ - var36 * хй;
         float var39 = this.ЮЧ + this.ЮЛ + хЖ * this.Ю诶;
         float var40 = UiDrawing.getGlobalAlpha();
         UiDrawing.setGlobalAlpha(var40 * this.Ю衣);
         UiDrawing.drawBlur(var38, var39, var36, var37, хм, var37 * хК, ColorMath.packArgb(222, 229, 242, хI));
         UiDrawing.drawRoundedRect(var38, var39, var36, var37, var37 * хТ, ColorMath.packArgb(7, 11, 19, хО));
         UiDrawing.drawText(
            ClientFonts.getMediumFont(),
            this.ХЪ,
            this.ЮЮ - var35 * хФ,
            var39 + var37 * хЙ - var33 * хк - 1.0F,
            var33,
            this.ЪК >= 0 && !this.Х.У(this.ЪК) ? ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT) : ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT)
         );
         UiDrawing.setGlobalAlpha(var40);
      }
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   private float outBack(float var1) {
      float var2 = Math.clamp(var1, 0.0F, 1.0F) - 1.0F;
      return 1.0F + Ус * var2 * var2 * var2 + УВ * var2 * var2;
   }

   public boolean renderClosing(GuiGraphicsExtractor var1) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      this.updateAnimation();
      if (this.Ю1 < хз) {
         if (this.А3 && this.ЪК >= 0) {
            this.Х.с_de5a6f78(this.ЪК);
         }

         return true;
      } else {
         float var2 = this.smooth(this.Ю1);
         UiDrawing.setGlobalAlpha(хц * var2);
         this.renderWheel(var1, var2);
         UiDrawing.resetGlobalAlpha();
         return false;
      }
   }

   public AutoSwapScreen(AutoSwap var1, int var2) {
      super(Component.literal(ха));
      this.Х = var1;
      this.ЪЖ = var2;
   }

   @Override
   public boolean keyReleased(KeyEvent var1) {
      if (var1.key() == this.ЪЖ) {
         this.finish(true);
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if ((var1.button() == 0 || var1.button() == 1) && this.ЪК >= 0) {
         this.minecraft.gui.setScreen(new SwapItemSelectorScreen(this.Х, this.ЪК, this, this.ЪЖ));
      }

      return true;
   }

   private int selectedAt(float var1, float var2) {
      float var3 = var1 - this.ЮЮ;
      float var4 = var2 - this.ЮЧ;
      float var5 = (float)Math.sqrt(var3 * var3 + var4 * var4);
      if (!(var5 < хг * this.Ю诶) && !(var5 > this.ЮЛ + х0 * this.Ю诶)) {
         float var6 = (float)Math.atan2(var4, var3) + хх;
         if (var6 < 0.0F) {
            var6 += хУ;
         }

         return Math.min(2, (int)(var6 / хЗ * хЕ));
      } else {
         return -1;
      }
   }

   private void warpCursor(float var1, float var2) {
      double var3 = var1 * this.minecraft.getWindow().getScreenWidth() / Math.max(1.0, this.minecraft.getWindow().getWidth());
      double var5 = var2 * this.minecraft.getWindow().getScreenHeight() / Math.max(1.0, this.minecraft.getWindow().getHeight());
      GLFW.glfwSetCursorPos(this.minecraft.getWindow().handle(), var3, var5);
   }
}
