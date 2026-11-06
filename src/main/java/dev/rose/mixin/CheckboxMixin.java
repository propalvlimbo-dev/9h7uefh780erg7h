package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.rose.gui.screen.GlassWidgets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(Checkbox.class)
public abstract class CheckboxMixin {
   @Shadow
   private boolean selected;

   @WrapWithCondition(
      method = "renderContents",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"
      )
   )
   private boolean rose$glassBox(GuiGraphics g, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height, int color) {
      return !GlassWidgets.renderCheckbox((Checkbox)(Object)this, g, this.selected, x, y, width);
   }
}
