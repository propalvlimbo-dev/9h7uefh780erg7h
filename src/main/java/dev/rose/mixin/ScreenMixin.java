package dev.rose.mixin;

import dev.rose.gui.screen.PauseDesign;
import dev.rose.gui.screen.ServerListDesign;
import dev.rose.gui.screen.StatusDesign;
import dev.rose.render.MenuBackground;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Screen.class)
public abstract class ScreenMixin {
   @Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true)
   private void rose$customBackground(GuiGraphics g, float partialTick, CallbackInfo ci) {
      Screen screen = (Screen)(Object)this;
      if (MenuBackground.everywhere() && MenuBackground.draw(g, screen.width, screen.height, 1.0F)) {
         ci.cancel();
      }
   }

   @Inject(method = "renderBackground", at = @At("TAIL"))
   private void rose$afterBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      Screen screen = (Screen)(Object)this;
      if (screen instanceof PauseScreen pause) {
         PauseDesign.renderHeader(g, pause);
      } else if (screen instanceof JoinMultiplayerScreen join && ServerListDesign.active()) {
         ServerListDesign.title(g, join);
      } else {
         StatusDesign.render(g, screen);
      }
   }
}
