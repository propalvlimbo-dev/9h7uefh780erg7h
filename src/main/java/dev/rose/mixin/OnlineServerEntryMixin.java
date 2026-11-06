package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.rose.gui.screen.ServerListDesign;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(targets = "net/minecraft/client/gui/screens/multiplayer/ServerSelectionList$OnlineServerEntry")
public abstract class OnlineServerEntryMixin {
   @Shadow
   @Final
   private JoinMultiplayerScreen screen;
   @Shadow
   @Final
   private ServerData serverData;
   @Unique
   private int rose$left;
   @Unique
   private int rose$top;
   @Unique
   private int rose$mouseX;
   @Unique
   private int rose$mouseY;
   @Unique
   private boolean rose$hovered;

   @Inject(method = "renderContent", at = @At("HEAD"))
   private void rose$remember(GuiGraphics g, int mouseX, int mouseY, boolean hovered, float partialTick, CallbackInfo ci) {
      LayoutElement self = (LayoutElement)(Object)this;
      this.rose$left = self.getX() + 2;
      this.rose$top = self.getY() + 2;
      this.rose$mouseX = mouseX;
      this.rose$mouseY = mouseY;
      this.rose$hovered = hovered;
   }

   @WrapWithCondition(
      method = "renderContent",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
   )
   private boolean rose$status(GuiGraphics g, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
      return width == 10 && height == 8 && ServerListDesign.active()
         ? !ServerListDesign.status(
            this, this.screen, this.serverData, g, x, y, this.rose$left, this.rose$top, this.rose$hovered, this.rose$mouseX, this.rose$mouseY
         )
         : true;
   }

   @WrapOperation(method = "renderContent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I"))
   private int rose$statusSpace(Font font, FormattedText text, Operation<Integer> original) {
      int width = (Integer)original.call(new Object[]{font, text});
      return ServerListDesign.active() ? width + ServerListDesign.extraStatusWidth(this.serverData) : width;
   }

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void rose$favorite(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
      if (event.button() == 0 && ServerListDesign.click(this, this.screen, this.serverData, event.x(), event.y())) {
         cir.setReturnValue(true);
      }
   }
}
