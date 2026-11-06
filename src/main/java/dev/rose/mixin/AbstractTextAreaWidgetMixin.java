package dev.rose.mixin;

import dev.rose.gui.screen.GlassWidgets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractTextAreaWidget;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractTextAreaWidget.class)
public abstract class AbstractTextAreaWidgetMixin {
   @Inject(method = "renderBorder", at = @At("HEAD"), cancellable = true)
   private void rose$glassPanel(GuiGraphics g, int x, int y, int width, int height, CallbackInfo ci) {
      if ((Object)this instanceof FittingMultiLineTextWidget widget && GlassWidgets.renderTextPanel(widget, g, x, y, width, height)) {
         ci.cancel();
      }
   }
}
