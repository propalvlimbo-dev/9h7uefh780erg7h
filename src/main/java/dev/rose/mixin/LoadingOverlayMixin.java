package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.rose.gui.screen.RoseSplash;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin {
   @Shadow
   private float currentProgress;

   @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/function/IntSupplier;getAsInt()I"))
   private int rose$background(int original) {
      return RoseSplash.active() ? -15987696 : original;
   }

   @WrapOperation(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIIIII)V"
      )
   )
   private void rose$logo(
      GuiGraphics g,
      RenderPipeline pipeline,
      Identifier texture,
      int x,
      int y,
      float u,
      float v,
      int width,
      int height,
      int uWidth,
      int vHeight,
      int textureWidth,
      int textureHeight,
      int color,
      Operation<Void> original
   ) {
      if (!RoseSplash.active()) {
         original.call(new Object[]{g, pipeline, texture, x, y, u, v, width, height, uWidth, vHeight, textureWidth, textureHeight, color});
      } else if (v == 0.0F) {
         RoseSplash.draw(g, ARGB.alpha(color) / 255.0F, this.currentProgress);
      }
   }

   @Inject(method = "drawProgressBar", at = @At("HEAD"), cancellable = true)
   private void rose$progressBar(GuiGraphics g, int x0, int y0, int x1, int y1, float alpha, CallbackInfo ci) {
      if (RoseSplash.active()) {
         ci.cancel();
      }
   }
}
