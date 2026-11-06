package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rose.gui.screen.GlassWidgets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractButton.class)
public abstract class AbstractButtonMixin {
   @WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/AbstractButton;renderContents(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
   private void rose$glassButton(AbstractButton button, GuiGraphics g, int mouseX, int mouseY, float partialTick, Operation<Void> original) {
      GlassWidgets.ButtonResult result = GlassWidgets.renderButton(button, g);
      if (result != GlassWidgets.ButtonResult.DONE) {
         if (result == GlassWidgets.ButtonResult.VANILLA) {
            original.call(new Object[]{button, g, mouseX, mouseY, partialTick});
         } else {
            GlassWidgets.skipBackground = true;

            try {
               original.call(new Object[]{button, g, mouseX, mouseY, partialTick});
            } finally {
               GlassWidgets.skipBackground = false;
            }
         }
      }
   }

   @Inject(method = "renderDefaultSprite", at = @At("HEAD"), cancellable = true)
   private void rose$noBackground(GuiGraphics g, CallbackInfo ci) {
      if (GlassWidgets.skipBackground) {
         ci.cancel();
      }
   }
}
