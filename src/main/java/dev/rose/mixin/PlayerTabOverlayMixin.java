package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.rose.module.Modules;
import dev.rose.module.impl.hud.GlassTabModule;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {
   @Shadow
   @Nullable
   private Component header;
   @Shadow
   @Nullable
   private Component footer;
   @Unique
   private int rose$panel;

   @Inject(method = "render", at = @At("HEAD"))
   private void rose$startPanels(GuiGraphics guiGraphics, int width, Scoreboard scoreboard, @Nullable Objective objective, CallbackInfo ci) {
      this.rose$panel = 0;
   }

   @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
   private boolean rose$glassPanels(GuiGraphics guiGraphics, int x0, int y0, int x1, int y1, int color) {
      GlassTabModule tab = Modules.glassTab();
      if (tab != null && tab.isEnabled()) {
         if (color == Integer.MIN_VALUE) {
            boolean hasHeader = this.header != null;
            boolean hasFooter = this.footer != null;
            int part = (hasHeader ? 0 : 1) + this.rose$panel++;
            boolean top = part == 0 || part == 1 && !hasHeader;
            boolean bottom = part == 2 || part == 1 && !hasFooter;
            tab.drawPanel(guiGraphics, x0, y0, x1, y1, top, bottom);
         }

         return false;
      } else {
         return true;
      }
   }
}
