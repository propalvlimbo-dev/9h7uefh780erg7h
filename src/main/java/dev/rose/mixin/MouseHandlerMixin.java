package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.rose.hud.GameStats;
import dev.rose.module.Modules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
   @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
   private void rose$zoomWheel(long window, double horizontal, double vertical, CallbackInfo ci) {
      Minecraft mc = Minecraft.getInstance();
      if (window == mc.getWindow().handle() && mc.screen == null && Modules.zoom() != null && Modules.zoom().scroll(vertical)) {
         ci.cancel();
      }
   }

   @ModifyExpressionValue(
      method = "turnPlayer",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0)
   )
   private Object rose$zoomSensitivity(Object sensitivity) {
      return sensitivity instanceof Double value && Modules.zoom() != null ? Modules.zoom().sensitivity(value) : sensitivity;
   }

   @Inject(method = "onButton", at = @At("HEAD"))
   private void rose$countClick(long window, MouseButtonInfo info, int action, CallbackInfo ci) {
      Minecraft mc = Minecraft.getInstance();
      if (action == 1 && window == mc.getWindow().handle() && mc.screen == null) {
         GameStats.mousePressed(info.button());
      }
   }
}
