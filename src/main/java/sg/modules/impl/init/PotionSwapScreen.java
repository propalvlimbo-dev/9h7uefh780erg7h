package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import settings.render.ClientLocalization;
import sg.ec.MinecraftAccess;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.PotionWheel;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ColorMath;
import sg.modules.impl.misc.PotionSwap;

public final class PotionSwapScreen extends Screen implements MinecraftAccess {
   private static final float Ъя = (float) (Math.PI * 2);
   @SerializedName("Я")
   private final PotionSwap Я_b0ce6890;
   private final int ДГ;
   @SerializedName("Я")
   private final PotionWheel Я_f1d0877d = new PotionWheel();
   private final float[] щ = new float[8];
   private float тс;
   private float тВ;
   private float тЯ;
   private float тХ;
   private float те;
   private float тА;
   private float т4;
   private int Дл = -1;
   private int Дэ = -1;
   private boolean е_;
   private boolean еШ;
   private boolean еы;
   private float тп;
   private long Хр = System.nanoTime();
   private static final String сцА = "PotionSwap";
   private static final float сц4 = 720.0F;
   private static final float сцп = 0.85F;
   private static final float сцД = 1.65F;
   private static final float сцЪ = 0.5F;
   private static final float сцт = 0.5F;
   private static final float сца = 116.0F;
   private static final float сцЮ = 0.008F;
   private static final float сцЧ = 255.0F;
   private static final float сцЛ = 0.76F;
   private static final float сц诶 = 0.24F;
   private static final float сц1 = 76.0F;
   private static final float сцЬ = (float) (Math.PI * 2);
   private static final float сц衣 = 0.16F;
   private static final float сцш = 0.002F;
   private static final float сцИ = 15.0F;
   private static final float сць = 0.7F;
   private static final float сцж = 0.3F;
   private static final float сц弟 = 6.0F;
   private static final float сцб = (float) (-Math.PI / 2);
   private static final float сцЫ = 0.34F;
   private static final float сцl = 0.5F;
   private static final float сц8 = 0.5F;
   private static final float сц5 = 1.22F;
   private static final float сцв = 0.48F;
   private static final float сцъ = 0.52F;
   private static final float сцМ = 0.27F;
   private static final String сцН = "+";
   private static final float сцн = 8.0F;
   private static final float сцЦ = 16.0F;
   private static final float сцч = 8.0F;
   private static final float сцЭ = 8.0F;
   private static final float сц必 = 0.78F;
   private static final float сцщ = 0.22F;
   private static final float сцГ = 8.0F;
   private static final float сцл = 0.5F;
   private static final double сцэ = 4.2E8;
   private static final float сцю = 0.035F;
   private static final float сцЩ = 1.9F;
   private static final float сцу = 8.0F;
   private static final float сц2 = 13.0F;
   private static final float сцф = 8.0F;
   private static final String сци = "нет в инвентаре";
   private static final String сц9 = "отпусти для броска";
   private static final float сцо = 26.0F;
   private static final float сцO = 8.0F;
   private static final String сц7 = "СКМ — добавить   •   ЛКМ — удалить   •   ПКМ — выбрать";
   private static final float сцР = 25.0F;
   private static final float сц3 = 9.0F;
   private static final float сцр = 22.0F;
   private static final float сц6 = 13.0F;
   private static final double сц_ = 220.0;
   private static final float сцШ = 13.0F;
   private static final double сцы = 202.0;
   private static final float сцй = 13.0F;
   private static final double сцЖ = 34.0;
   private static final String сцм = "Выбери зелье";
   private static final float сцК = 12.0F;
   private static final float сцI = 10.0F;
   private static final float сцТ = 10.0F;
   private static final double сцО = 245.0;
   private static final String сцФ = "В инвентаре нет метательных зелий";
   private static final float сцЙ = 0.5F;
   private static final float сцк = 43.0F;
   private static final float сцг = 8.0F;
   private static final double сц0 = 230.0;
   private static final float сцх = 8.0F;
   private static final double сцУ = 235.0;
   private static final float сцЗ = 1.32F;
   private static final float сцЕ = 1.14F;
   private static final float сцП = 0.5F;
   private static final float сц西 = 8.0F;
   private static final float сцд = 0.5F;
   private static final float сцБ = 8.0F;
   private static final float сцС = 0.008F;
   private static final float сцз = 255.0F;
   private static final float сцц = 35.0F;
   private static final float сця = 65.0F;
   private static final float сяс = (float) (Math.PI / 2);
   private static final float сяВ = (float) (Math.PI * 2);
   private static final float сяЯ = (float) (Math.PI * 2);
   private static final float сяХ = 7.0F;
   private static final float сяе = 11.0F;
   private static final float сяА = 1.0E9F;
   private static final float ся4 = 0.05F;
   private static final float сяп = 12.0F;
   private static final float сяД = 8.5F;
   private static final float сяЪ = 3.0F;
   private static final float сят = 2.70158F;
   private static final float сяа = 1.70158F;
   private static final float сяЮ = 34.0F;
   private static final float сяЧ = 5.0F;
   private static final float сяЛ = 20.0F;
   private static final float ся诶 = 37.0F;
   private static final float ся1 = 10.0F;
   private static final float сяЬ = 18.0F;
   private static final float ся衣 = 10.0F;
   private static final float сяш = 18.0F;
   private static final float сяИ = 0.5F;
   private static final float сяь = 0.5F;

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      float var5 = this.minecraft.getWindow().getScreenWidth();
      float var6 = this.minecraft.getWindow().getScreenHeight();
      this.те = Math.clamp(var6 / сц4, сцп, сцД);
      this.тВ = var5 * сцЪ;
      this.тЯ = var6 * сцт;
      this.тХ = сца * this.те;
      if (!this.еШ) {
         Vector2f var7 = this.clampMouse(WorldProjection.transformScreenPoint(var2, var3));
         this.тА = var7.x;
         this.т4 = var7.y;
      }

      this.updateAnimation();
      if (this.еШ && this.тс < сцЮ) {
         int var10 = this.Дл;
         boolean var8 = this.еы;
         this.minecraft.gui.setScreen(null);
         if (var8) {
            this.Я_b0ce6890.西з(var10);
         }
      } else {
         this.Дл = this.еШ ? this.Дл : (this.е_ ? this.Дэ : this.selectedAt(this.тА, this.т4));
         float var9 = this.smooth(this.тс);
         UiDrawing.setGlobalAlpha(сцЧ * var9);
         this.renderWheel(var1, var9);
         if (this.е_) {
            this.renderPicker(var1);
         }

         UiDrawing.resetGlobalAlpha();
      }
   }

   private void warpCursor(float var1, float var2) {
      double var3 = var1 * this.minecraft.getWindow().getScreenWidth() / Math.max(1.0, this.minecraft.getWindow().getWidth());
      double var5 = var2 * this.minecraft.getWindow().getScreenHeight() / Math.max(1.0, this.minecraft.getWindow().getHeight());
      GLFW.glfwSetCursorPos(this.minecraft.getWindow().handle(), var3, var5);
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (var1.button() == this.ДГ) {
         this.finish(true);
         return true;
      } else {
         return super.mouseReleased(var1);
      }
   }

   private int selectedAt(float var1, float var2) {
      float var3 = var1 - this.тВ;
      float var4 = var2 - this.тЯ;
      float var5 = (float)Math.sqrt(var3 * var3 + var4 * var4);
      if (!(var5 < сцц * this.те) && !(var5 > this.тХ + сця * this.те)) {
         float var6 = (float)Math.atan2(var4, var3) + сяс;
         if (var6 < 0.0F) {
            var6 += сяВ;
         }

         return Math.min(this.Я_b0ce6890.西Т().size() - 1, (int)(var6 / сяЯ * this.Я_b0ce6890.西Т().size()));
      } else {
         return -1;
      }
   }

   private float outBack(float var1) {
      float var2 = this.clamp01(var1) - 1.0F;
      return 1.0F + сят * var2 * var2 * var2 + сяа * var2 * var2;
   }

   private int animatedColor(int var1, int var2, int var3, int var4, float var5) {
      return ColorMath.packArgb(var1, var2, var3, var4 * this.clamp01(var5));
   }

   public void finish(boolean var1) {
      if (!this.еШ) {
         this.еШ = true;
         this.еы = var1 && !this.е_;
         this.Я_b0ce6890.м(this);
         this.minecraft.gui.setScreen(null);
      }
   }

   private ItemStack centerStack() {
      if (this.Дл >= 0 && this.Дл < this.Я_b0ce6890.西Т().size()) {
         ItemStack var1 = this.Я_b0ce6890.西Т().get(this.Дл);
         if (!var1.isEmpty()) {
            return var1;
         }
      }

      for (ItemStack var2 : this.Я_b0ce6890.西Т()) {
         if (!var2.isEmpty()) {
            return var2;
         }
      }

      return ItemStack.EMPTY;
   }

   @Override
   protected void extractMenuBackground(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
   }

   private void renderWheel(GuiGraphicsExtractor var1, float var2) {
      float var3 = this.outBack(var2);
      float var4 = this.тХ * (сцЛ + сц诶 * var3);
      float var5 = сц1 * this.те;
      ItemWheelRenderer.а(this.minecraft.getWindow().getScreenWidth(), this.minecraft.getWindow().getScreenHeight());
      this.Я_f1d0877d.чр();
      int var6 = this.Я_b0ce6890.西Т().size();
      float var7 = сцЬ / var6;

      for (int var8 = 0; var8 < var6; var8++) {
         float var9 = var6 == 1 ? 0.0F : сц衣 * var8 / (var6 - 1.0F);
         float var10 = this.clamp01((this.тс - var9) / (1.0F - var9));
         float var11 = this.smooth(var10);
         if (!(var11 < сцш)) {
            float var12 = this.outBack(var10);
            float var13 = var8 == this.Дл ? 1.0F : 0.0F;
            this.щ[var8] = this.щ[var8] + (var13 - this.щ[var8]) * (1.0F - (float)Math.exp(-this.тп * сцИ));
            float var14 = this.щ[var8];
            float var15 = сць + сцж * var12;
            float var16 = var5 * var15;
            float var17 = var4 * var15 + var14 * сц弟 * this.те;
            float var18 = сцб + var8 * var7 - (1.0F - var11) * сцЫ;
            ItemStack var19 = this.Я_b0ce6890.西Т().get(var8);
            boolean var20 = this.Я_b0ce6890.щ(var19);
            ItemWheelRenderer.К(this.Я_f1d0877d, this.тВ, this.тЯ, var16, var17, var18, var7, var20, var11, var14, this.те);
            float var21 = var18 + var7 * сцl;
            float var22 = (var16 + var17) * сц8;
            float var23 = this.те * сц5 * (сцв + сцъ * var12) * (1.0F + var14 * сцМ);
            float var24 = this.тВ + (float)Math.cos(var21) * var22;
            float var25 = this.тЯ + (float)Math.sin(var21) * var22;
            if (var19.isEmpty()) {
               this.centered(сцН, var24, var25 - сцн * this.те, Math.max(12, Math.round(сцЦ * this.те)), this.animatedColor(106, 132, 154, 220, var11));
            } else {
               UiDrawing.drawItem(var1, var19, var24 - сцч * var23, var25 - сцЭ * var23, var23, false);
            }
         }
      }

      float var26 = сц必 + сцщ * var3;
      float var27 = (var5 - сцГ * this.те) * 2.0F * var26;
      ItemWheelRenderer.Ф(this.тВ, this.тЯ, var27 * сцл, this.те, var2);
      ItemStack var28 = this.centerStack();
      if (!var28.isEmpty()) {
         float var29 = 1.0F + (float)Math.sin(System.nanoTime() / сцэ) * сцю;
         float var30 = this.те * сцЩ * var26 * var29;
         UiDrawing.drawItem(var1, var28, this.тВ - сцу * var30, this.тЯ - сц2 * this.те - сцф * var30, var30, false);
      }

      this.centered(
         this.Дл >= 0 && !this.Я_b0ce6890.щ(this.Я_b0ce6890.西Т().get(this.Дл)) ? ClientLocalization.translate(сци) : ClientLocalization.translate(сц9),
         this.тВ,
         this.тЯ + сцо * this.те,
         Math.max(7, Math.round(сцO * this.те)),
         this.animatedColor(104, 135, 158, 220, var2)
      );
      this.centered(
         ClientLocalization.translate(сц7), this.тВ, this.тЯ + var4 + сцР * this.те, Math.max(8, Math.round(сц3 * this.те)), this.animatedColor(142, 169, 190, 220, var2)
      );
   }

   private float smooth(float var1) {
      float var2 = this.clamp01(var1);
      return var2 * var2 * (сяЪ - 2.0F * var2);
   }

   private float clamp01(float var1) {
      return Math.clamp(var1, 0.0F, 1.0F);
   }

   @Override
   protected void extractPanorama(GuiGraphicsExtractor var1, float var2) {
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   public PotionSwapScreen(PotionSwap var1, int var2) {
      super(Component.literal(сцА));
      this.Я_b0ce6890 = var1;
      this.ДГ = var2;
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

   @Override
   public void onClose() {
      this.finish(false);
   }

   private PotionSwapScreen.PickerLayout pickerLayout(int var1) {
      byte var2 = 5;
      int var3 = Math.max(1, (var1 + var2 - 1) / var2);
      float var4 = сяЮ * this.те;
      float var5 = сяЧ * this.те;
      float var6 = var2 * var4 + (var2 - 1) * var5 + сяЛ * this.те;
      float var7 = ся诶 * this.те + var3 * var4 + Math.max(0, var3 - 1) * var5 + ся1 * this.те;
      float var8 = this.тВ + this.тХ + сяЬ * this.те;
      if (var8 + var6 > this.minecraft.getWindow().getScreenWidth() - ся衣 * this.те) {
         var8 = this.тВ - this.тХ - сяш * this.те - var6;
      }

      return new PotionSwapScreen.PickerLayout(var8, this.тЯ - var7 * сяИ, var6, var7, var4, var5, var2);
   }

   private void updateAnimation() {
      long var1 = System.nanoTime();
      float var3 = Math.clamp((float)(var1 - this.Хр) / сяА, 0.0F, ся4);
      this.Хр = var1;
      this.тп = var3;
      float var4 = this.еШ ? 0.0F : 1.0F;
      float var5 = 1.0F - (float)Math.exp(-var3 * (this.еШ ? сяп : сяД));
      this.тс = this.тс + (var4 - this.тс) * var5;
   }

   private void centered(String var1, float var2, float var3, int var4, int var5) {
      UiDrawing.drawText(ClientFonts.getMediumFont(), var1, var2 - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, var4) * сяь, var3, var4, var5);
   }

   private Vector2f clampMouse(Vector2f var1) {
      if (!this.е_) {
         float var8 = var1.x - this.тВ;
         float var9 = var1.y - this.тЯ;
         float var10 = (float)Math.sqrt(var8 * var8 + var9 * var9);
         float var11 = this.тХ + сяе * this.те;
         if (!(var10 <= var11) && var10 != 0.0F) {
            float var6 = this.тВ + var8 / var10 * var11;
            float var7 = this.тЯ + var9 / var10 * var11;
            this.warpCursor(var6, var7);
            return new Vector2f(var6, var7);
         } else {
            return var1;
         }
      } else {
         PotionSwapScreen.PickerLayout var2 = this.pickerLayout(Math.min(15, this.Я_b0ce6890.西Ч().size()));
         float var3 = сяХ * this.те;
         float var4 = Math.clamp(var1.x, Math.min(this.тВ - this.тХ - var3, var2.x - var3), Math.max(this.тВ + this.тХ + var3, var2.x + var2.width + var3));
         float var5 = Math.clamp(var1.y, Math.min(this.тЯ - this.тХ - var3, var2.y - var3), Math.max(this.тЯ + this.тХ + var3, var2.y + var2.height + var3));
         if (var4 != var1.x || var5 != var1.y) {
            this.warpCursor(var4, var5);
         }

         return new Vector2f(var4, var5);
      }
   }

   private void renderPicker(GuiGraphicsExtractor var1) {
      List var2 = this.Я_b0ce6890.西Ч();
      int var3 = Math.min(15, var2.size());
      PotionSwapScreen.PickerLayout var4 = this.pickerLayout(var3);
      UiDrawing.drawBlur(var4.x, var4.y, var4.width, var4.height, сцр, сц6 * this.те, ColorMath.packArgb(255, 255, 255, сц_));
      UiDrawing.drawRoundedRect(var4.x, var4.y, var4.width, var4.height, сцШ * this.те, ColorMath.packArgb(4, 7, 12, сцы));
      UiDrawing.drawOutline(var4.x, var4.y, var4.width, var4.height, this.те, сцй * this.те, ColorMath.packArgb(116, 207, 248, сцЖ));
      UiDrawing.drawText(
         ClientFonts.getMediumFont(),
         ClientLocalization.translate(сцм),
         var4.x + сцК * this.те,
         var4.y + сцI * this.те,
         Math.max(9, Math.round(сцТ * this.те)),
         ColorMath.packArgb(226, 244, 255, сцО)
      );
      if (var2.isEmpty()) {
         this.centered(
            ClientLocalization.translate(сцФ),
            var4.x + var4.width * сцЙ,
            var4.y + сцк * this.те,
            Math.max(7, Math.round(сцг * this.те)),
            ColorMath.packArgb(113, 137, 157, сц0)
         );
      } else {
         for (int var5 = 0; var5 < var3; var5++) {
            float var6 = var4.cellX(var5);
            float var7 = var4.cellY(var5);
            boolean var8 = WorldProjection.containsPoint(this.тА, this.т4, var6, var7, var4.cell, var4.cell);
            UiDrawing.drawRoundedRect(
               var6, var7, var4.cell, var4.cell, сцх * this.те, var8 ? ColorMath.packArgb(18, 55, 76, сцУ) : ColorMath.packArgb(16, 23, 34, 0.0)
            );
            float var9 = this.те * (var8 ? сцЗ : сцЕ);
            UiDrawing.drawItem(var1, (ItemStack)var2.get(var5), var6 + var4.cell * сцП - сц西 * var9, var7 + var4.cell * сцд - сцБ * var9, var9, false);
         }
      }
   }

   @Override
   public boolean keyReleased(KeyEvent var1) {
      if (var1.key() == this.ДГ) {
         this.finish(true);
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      Vector2f var3 = WorldProjection.guiToScreen(var1.x(), var1.y());
      if (var1.button() == 2) {
         this.Дл = this.Я_b0ce6890.мИ();
         return true;
      }

      if (var1.button() == 1 && this.Дл >= 0) {
         this.Дэ = this.Дл;
         this.е_ = true;
         return true;
      }

      if (var1.button() == 0 && this.е_) {
         List var4 = this.Я_b0ce6890.西Ч();
         int var5 = Math.min(15, var4.size());
         int var6 = this.pickerLayout(var5).at(var3.x, var3.y, var5);
         if (var6 >= 0) {
            this.Я_b0ce6890.К(this.Дэ, (ItemStack)var4.get(var6));
            this.е_ = false;
            this.Дл = this.Дэ;
         }

         return true;
      } else if (var1.button() == 0 && this.Дл >= 0) {
         this.Я_b0ce6890.西O(this.Дл);
         this.Дл = -1;
         return true;
      } else {
         return true;
      }
   }

   public boolean renderClosing(GuiGraphicsExtractor var1) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      this.updateAnimation();
      if (this.тс < сцС) {
         if (this.еы) {
            this.Я_b0ce6890.西з(this.Дл);
         }

         return true;
      } else {
         float var2 = this.smooth(this.тс);
         UiDrawing.setGlobalAlpha(сцз * var2);
         this.renderWheel(var1, var2);
         UiDrawing.resetGlobalAlpha();
         return false;
      }
   }

   private record PickerLayout(float x, float y, float width, float height, float cell, float gap, int columns) {
      private float cellX(int var1) {
         return this.x + 10.0F * (this.cell / 34.0F) + var1 % this.columns * (this.cell + this.gap);
      }

      private int at(float var1, float var2, int var3) {
         for (int var4 = 0; var4 < var3; var4++) {
            if (WorldProjection.containsPoint(var1, var2, this.cellX(var4), this.cellY(var4), this.cell, this.cell)) {
               return var4;
            }
         }

         return -1;
      }

      private float cellY(int var1) {
         return this.y + 37.0F * (this.cell / 34.0F) + var1 / this.columns * (this.cell + this.gap);
      }
   }
}
