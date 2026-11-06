package sg.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.UnhookState;
import sg.render.avatar.MainMenuLoadingRenderer;

@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayCustomizationMixin {
   @Unique
   private static final int BACKGROUND_WIDTH = 1672;
   @Unique
   private static final int BACKGROUND_HEIGHT = 941;
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private ReloadInstance reload;
   @Shadow
   @Final
   private boolean fadeIn;
   @Shadow
   private float currentProgress;
   @Shadow
   private long fadeOutStart;
   @Shadow
   private long fadeInStart;
   @Unique
   private long rose$openedAt;
   private static final long сl9 = -1L;
   private static final long сlо = -1L;
   private static final long сlO = -1L;
   private static final long сl7 = -1L;
   private static final float сlР = 1000.0F;
   private static final float сl3 = -1.0F;
   private static final long сlр = -1L;
   private static final float сl6 = 500.0F;
   private static final float сl_ = -1.0F;
   private static final int сlШ = -16447991;
   private static final float сlы = 1.7768332F;
   private static final float сlй = 1672.0F;
   private static final float сlЖ = 941.0F;
   private static final float сlм = 1672.0F;
   private static final float сlК = 941.0F;
   private static final float сlI = 1.035F;
   private static final float сlТ = 0.007F;
   private static final double сlО = 2600.0;
   private static final float сlФ = 1672.0F;
   private static final float сlЙ = 941.0F;
   private static final float сlк = 0.5F;
   private static final float сlг = 0.12F;
   private static final double сl0 = 5200.0;
   private static final float сlх = 0.5F;
   private static final float сlУ = 0.1F;
   private static final double сlЗ = 6100.0;
   private static final int сlЕ = -1811741432;
   private static final float сlП = 46.0F;
   private static final float сl西 = 3.0F;
   private static final int сlд = 16777215;

   public LoadingOverlayCustomizationMixin() {
      this.rose$openedAt = сl9;
   }

   @Inject(method = "registerTextures", at = @At("TAIL"))
   private static void rose$initializeLoadingScreen(TextureManager var0, CallbackInfo var1) {
      MainMenuLoadingRenderer.х(var0);
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
   private void rose$renderLoadingScreen(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (!UnhookState.ые()) {
         int var6 = var1.guiWidth();
         int var7 = var1.guiHeight();
         long var8 = Util.getMillis();
         if (this.rose$openedAt == сlо) {
            this.rose$openedAt = var8;
         }

         if (this.fadeIn && this.fadeInStart == сlO) {
            this.fadeInStart = var8;
         }

         float var10 = this.fadeOutStart > сl7 ? (float)(var8 - this.fadeOutStart) / сlР : сl3;
         float var11 = this.fadeInStart > сlр ? (float)(var8 - this.fadeInStart) / сl6 : сl_;
         float var12;
         if (var10 >= 1.0F) {
            this.rose$renderUnderlying(var1, 0, 0, var4);
            var12 = 1.0F - rose$smooth(Mth.clamp(var10 - 1.0F, 0.0F, 1.0F));
         } else if (this.fadeIn) {
            if (this.minecraft.gui.screen() != null && var11 < 1.0F) {
               this.minecraft.gui.screen().extractRenderStateWithTooltipAndSubtitles(var1, var2, var3, var4);
            } else {
               this.minecraft.gui.hud.extractDeferredSubtitles();
            }

            var12 = rose$smooth(Mth.clamp(var11, 0.0F, 1.0F));
         } else {
            var1.fill(0, 0, var6, var7, сlШ);
            var12 = 1.0F;
         }

         var1.nextStratum();
         rose$drawBackground(var1, var6, var7, var8, var12);
         this.currentProgress = Mth.clamp(this.reload.getActualProgress(), 0.0F, 1.0F);
         MainMenuLoadingRenderer.х(this.rose$openedAt, var8, var6, var7, var12, this.currentProgress);
         if (var10 >= 2.0F) {
            this.minecraft.gui.setOverlay(null);
         }

         var5.cancel();
      }
   }

   @Unique
   private void rose$renderUnderlying(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.minecraft.gui.screen() != null) {
         this.minecraft.gui.screen().extractRenderStateWithTooltipAndSubtitles(var1, var2, var3, var4);
      } else {
         this.minecraft.gui.hud.extractDeferredSubtitles();
      }
   }

   @Unique
   private static void rose$drawBackground(GuiGraphicsExtractor var0, int var1, int var2, long var3, float var5) {
      float var6 = (float)var1 / Math.max(var2, 1);
      float var7 = сlы;
      float var8 = сlй;
      float var9 = сlЖ;
      if (var6 > var7) {
         var9 = сlм / var6;
      } else {
         var8 = сlК * var6;
      }

      float var10 = сlI + сlТ * (float)Math.sin(var3 / сlО);
      var8 /= var10;
      var9 /= var10;
      float var11 = сlФ - var8;
      float var12 = сlЙ - var9;
      float var13 = var11 * (сlк + сlг * (float)Math.sin(var3 / сl0));
      float var14 = var12 * (сlх + сlУ * (float)Math.cos(var3 / сlЗ));
      var0.blit(
         RenderPipelines.GUI_TEXTURED,
         MainMenuLoadingRenderer.field_2,
         0,
         0,
         var13,
         var14,
         var1,
         var2,
         Math.max(1, Math.round(var8)),
         Math.max(1, Math.round(var9)),
         1672,
         941,
         rose$opacity(-1, var5)
      );
      var0.fill(0, 0, var1, var2, rose$opacity(сlЕ, var5));
      int var15 = Math.round(сlП * var5);
      int var16 = Math.max(8, Math.min(28, Math.min(var1, var2) / 24));

      for (int var17 = 0; var17 < var16; var17++) {
         float var18 = 1.0F - (float)var17 / var16;
         int var19 = rose$argb(Math.round(var15 * var18 * var18 / var16), 0, 0, 0);
         var0.fill(var17, var17, var1 - var17, var17 + 1, var19);
         var0.fill(var17, var2 - var17 - 1, var1 - var17, var2 - var17, var19);
         var0.fill(var17, var17, var17 + 1, var2 - var17, var19);
         var0.fill(var1 - var17 - 1, var17, var1 - var17, var2 - var17, var19);
      }
   }

   @Unique
   private static float rose$smooth(float var0) {
      float var1 = Mth.clamp(var0, 0.0F, 1.0F);
      return var1 * var1 * (сl西 - 2.0F * var1);
   }

   @Unique
   private static int rose$opacity(int var0, float var1) {
      int var2 = Math.round((var0 >>> 24 & 0xFF) * Mth.clamp(var1, 0.0F, 1.0F));
      return var0 & сlд | var2 << 24;
   }

   @Unique
   private static int rose$argb(int var0, int var1, int var2, int var3) {
      return Mth.clamp(var0, 0, 255) << 24 | Mth.clamp(var1, 0, 255) << 16 | Mth.clamp(var2, 0, 255) << 8 | Mth.clamp(var3, 0, 255);
   }
}
