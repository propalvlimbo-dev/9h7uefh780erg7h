package dev.rose.mixin;

import dev.rose.gui.screen.GlassWidgets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LockIconButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LockIconButton.class)
public abstract class LockIconButtonMixin {
   @Shadow
   private boolean locked;

   @Inject(method = "renderContents", at = @At("HEAD"), cancellable = true)
   private void rose$glassLock(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      if (GlassWidgets.renderLock((LockIconButton)(Object)this, g, this.locked)) {
         ci.cancel();
      }
   }
}
