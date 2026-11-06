package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import dev.rose.gui.screen.TitleDesign;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
   @Shadow
   @Nullable
   private SplashRenderer splash;
   @Shadow
   private boolean fading;
   @Shadow
   private long fadeInStart;

   protected TitleScreenMixin(Component title) {
      super(title);
   }

   @Inject(method = "render", at = @At("HEAD"))
   private void rose$resetFade(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      TitleDesign.setFade(0.0F);
   }

   @Inject(
      method = "render",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;renderPanorama(Lnet/minecraft/client/gui/GuiGraphics;F)V", shift = Shift.AFTER)
   )
   private void rose$backdrop(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      TitleDesign.renderBackdrop(g, this.width, this.height);
   }

   @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LogoRenderer;renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IF)V"))
   private boolean rose$logo(LogoRenderer logo, GuiGraphics g, int screenWidth, float alpha) {
      TitleDesign.setFade(alpha);
      return !TitleDesign.laidOut();
   }

   @WrapWithCondition(
      method = "render",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/SplashRenderer;render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/client/gui/Font;F)V")
   )
   private boolean rose$splash(SplashRenderer renderer, GuiGraphics g, int screenWidth, Font font, float alpha) {
      return !TitleDesign.laidOut();
   }

   @WrapWithCondition(
      method = "render",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V")
   )
   private boolean rose$version(GuiGraphics g, Font font, String text, int x, int y, int color) {
      return !TitleDesign.laidOut();
   }

   @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/realmsclient/gui/screens/RealmsNotificationsScreen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
   private void rose$realmsIcons(RealmsNotificationsScreen screen, GuiGraphics g, int mouseX, int mouseY, float partialTick, Operation<Void> original) {
      if (!TitleDesign.laidOut()) {
         original.call(new Object[]{screen, g, mouseX, mouseY, partialTick});
      } else {
         g.pose().pushMatrix();
         g.pose().translate(TitleDesign.realmsDx(), TitleDesign.realmsDy());
         original.call(new Object[]{screen, g, mouseX - TitleDesign.realmsDx(), mouseY - TitleDesign.realmsDy(), partialTick});
         g.pose().popMatrix();
      }
   }

   @Inject(method = "render", at = @At("TAIL"))
   private void rose$foreground(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
      TitleDesign.renderForeground(
         g,
         this.width,
         this.height,
         mouseX,
         mouseY,
         this.splash == null ? null : ((SplashRendererAccessor)this.splash).rose$text().getString()
      );
   }

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void rose$click(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
      if (event.button() == 0 && TitleDesign.mouseClicked(event.x(), event.y())) {
         cir.setReturnValue(true);
      }
   }
}
