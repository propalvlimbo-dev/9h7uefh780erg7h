package dev.rose.mixin;

import dev.rose.module.Modules;
import dev.rose.module.impl.visual.GlassTooltipsModule;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TooltipRenderUtil.class)
public abstract class TooltipRenderUtilMixin {
   @Inject(method = "renderTooltipBackground", at = @At("HEAD"), cancellable = true)
   private static void rose$glassTooltip(GuiGraphics g, int x, int y, int width, int height, @Nullable Identifier style, CallbackInfo ci) {
      GlassTooltipsModule module = Modules.glassTooltips();
      if (module != null && module.isEnabled() && style == null) {
         module.renderBackground(g, x, y, width, height, 0);
         ci.cancel();
      }
   }
}
