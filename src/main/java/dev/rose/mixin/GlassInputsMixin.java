package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.rose.gui.screen.GlassWidgets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(EditBox.class)
public abstract class GlassInputsMixin {
   @WrapWithCondition(
      method = "renderWidget",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
   )
   private boolean rose$glassField(GuiGraphics g, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
      return !GlassWidgets.renderEditBox((AbstractWidget)(Object)this, g, x, y, width, height);
   }
}
