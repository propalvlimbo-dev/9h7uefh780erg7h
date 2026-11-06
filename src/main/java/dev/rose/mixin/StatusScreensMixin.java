package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.rose.gui.screen.StatusDesign;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin({ConnectScreen.class, LevelLoadingScreen.class, ProgressScreen.class})
public abstract class StatusScreensMixin {
   @WrapWithCondition(
      method = "render",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawCenteredString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V")
   )
   private boolean rose$text(GuiGraphics g, Font font, Component text, int x, int y, int color) {
      return !StatusDesign.styled((Screen)(Object)this);
   }
}
