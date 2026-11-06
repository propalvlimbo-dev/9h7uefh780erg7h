package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rose.gui.screen.ServerListDesign;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(JoinMultiplayerScreen.class)
public abstract class JoinMultiplayerScreenMixin {
   @ModifyArg(
      method = "init",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/multiplayer/ServerSelectionList;<init>(Lnet/minecraft/client/gui/screens/multiplayer/JoinMultiplayerScreen;Lnet/minecraft/client/Minecraft;IIII)V"),
      index = 5
   )
   private int rose$rowHeight(int height) {
      return ServerListDesign.active() ? 42 : height;
   }

   @WrapOperation(
      method = "init",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;addTitleHeader(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/Font;)V")
   )
   private void rose$title(HeaderAndFooterLayout layout, Component title, Font font, Operation<Void> original) {
      original.call(new Object[]{layout, ServerListDesign.active() ? CommonComponents.EMPTY : title, font});
   }
}
