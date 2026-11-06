package sg.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.UnhookState;
import sg.client.RoseClient;

@Mixin(Screen.class)
public class ScreenClientCommandMixin {
   private static final String ВРЧ = "/";

   @Inject(method = "clickCommandAction", at = @At("HEAD"), cancellable = true)
   private static void rose$runClientCommand(LocalPlayer var0, String var1, @Nullable Screen var2, CallbackInfo var3) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null) {
         String var4 = var1.startsWith(ВРЧ) ? var1.substring(1) : var1;
         String var5 = RoseClient.getInstance().getServices().getCommands().getCommandPrefix().toString();
         if (var4.startsWith(var5)) {
            if (!UnhookState.ые()) {
               RoseClient.getInstance().getServices().getCommands().executeChatCommand(var4);
               var3.cancel();
            }
         }
      }
   }
}
