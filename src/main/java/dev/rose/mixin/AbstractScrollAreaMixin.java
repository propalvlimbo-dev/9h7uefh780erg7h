package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.rose.gui.screen.ServerListDesign;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(AbstractScrollArea.class)
public abstract class AbstractScrollAreaMixin {
   @WrapWithCondition(
      method = "renderScrollbar",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
   )
   private boolean rose$scrollbar(GuiGraphics g, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
      return !((Object)this instanceof AbstractSelectionList<?> list && ServerListDesign.styled(list) && ServerListDesign.scrollbar(g, sprite, x, y, width, height));
   }
}
