package dev.rose.mixin;

import dev.rose.gui.screen.InventoryDesign;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {
   @ModifyArg(
      method = "renderLabels",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)V"),
      index = 4
   )
   private int rose$labelColor(int color) {
      return InventoryDesign.handles((AbstractContainerScreen<?>)(Object)this) ? -1644564 : color;
   }
}
