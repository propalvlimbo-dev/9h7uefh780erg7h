package dev.rose.mixin;

import dev.rose.gui.screen.StatusDesign;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin({GenericMessageScreen.class, LevelLoadingScreen.class})
public abstract class OverriddenBackgroundMixin {
   @Inject(method = "renderBackground", at = @At("TAIL"))
   private void rose$card(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      StatusDesign.render(g, (Screen)(Object)this);
   }
}
