package dev.rose.mixin;

import dev.rose.module.Modules;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
   @Inject(method = "keyPress", at = @At("HEAD"))
   private void rose$onKeyPress(long window, int action, KeyEvent event, CallbackInfo ci) {
      int key = event.key();
      if (action == 1) {
         Minecraft mc = Minecraft.getInstance();
         if (window == mc.getWindow().handle()) {
            if (mc.screen instanceof TitleScreen && key == Modules.clickGui().key().get()) {
               Modules.clickGui().onKeyBind();
            } else if (mc.screen == null) {
               Modules.onKeyPressed(key);
            }
         }
      }
   }
}
