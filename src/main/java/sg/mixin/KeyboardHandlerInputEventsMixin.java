package sg.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.misc.ClientSounds;
import sg.client.RoseClient;
import sg.events.impl.InputEvent;
import sg.network.ScoreboardValueEditor;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerInputEventsMixin {
   @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/FramerateLimitTracker;onInputReceived()V"), cancellable = true)
   private void onKey(long var1, int var3, KeyEvent var4, CallbackInfo var5) {
      InputEvent var6 = new InputEvent(var4.input(), var3);
       RoseClient.getInstance().getEventBus().post(var6);
       var clickGui = RoseClient.getInstance().getServices().getModules().getClickGui();
       if (!RoseClient.getInstance().getServices().getUnhookFiles().аА()
          && (var4.input() == 344 || var4.input() == clickGui.getKeybind().getKeyCode())) {
          var5.cancel();
       }
   }

   @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
   private void onChar(long var1, CharacterEvent var3, CallbackInfo var4) {
      if (ScoreboardValueEditor.с(var3.codepointAsString())) {
         var4.cancel();
      } else {
         Screen var5 = Minecraft.getInstance().gui.screen();
         if (var5 != null && var5.getFocused() instanceof EditBox) {
            ClientSounds.Чж();
         }
      }
   }
}
