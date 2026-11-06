package dev.rose.mixin;

import dev.rose.gui.screen.GlassWidgets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractSliderButton.class)
public abstract class AbstractSliderButtonMixin {
   @Shadow
   protected double value;
   @Shadow
   private boolean canChangeValue;

   @Inject(method = "renderWidget", at = @At("HEAD"), cancellable = true)
   private void rose$glassSlider(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      if (GlassWidgets.renderSlider((AbstractSliderButton)(Object)this, g, this.value, this.canChangeValue)) {
         ci.cancel();
      }
   }
}
