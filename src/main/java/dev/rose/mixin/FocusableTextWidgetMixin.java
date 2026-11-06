package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.rose.gui.screen.GlassWidgets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.FocusableTextWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(FocusableTextWidget.class)
public abstract class FocusableTextWidgetMixin {
   @WrapWithCondition(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
   private boolean rose$glassPanel(GuiGraphics g, int x0, int y0, int x1, int y1, int color) {
      return !GlassWidgets.renderTextPanel((AbstractWidget)(Object)this, g, x0 - 1, y0, x1 - x0 + 1, y1 - y0);
   }

   @WrapWithCondition(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderOutline(IIIII)V"))
   private boolean rose$outline(GuiGraphics g, int x, int y, int width, int height, int color) {
      return !GlassWidgets.active();
   }
}
