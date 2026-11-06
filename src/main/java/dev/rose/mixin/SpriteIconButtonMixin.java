package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.rose.gui.screen.GlassWidgets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(SpriteIconButton.class)
public abstract class SpriteIconButtonMixin {
   @WrapWithCondition(
      method = "renderSprite",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIF)V"
      )
   )
   private boolean rose$icon(GuiGraphics g, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, float alpha) {
      return !GlassWidgets.replaceSprite((AbstractButton)(Object)this, g, sprite, x, y, width, height);
   }
}
