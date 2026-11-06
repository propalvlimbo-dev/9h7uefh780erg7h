package sg.mixin;

import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import settings.render.ServerAddressRedirects;

@Mixin(ConnectScreen.class)
public class ConnectScreenRedirectMixin {
   @ModifyVariable(
      method = "startConnecting(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/multiplayer/resolver/ServerAddress;Lnet/minecraft/client/multiplayer/ServerData;ZLnet/minecraft/client/multiplayer/TransferState;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private static ServerAddress rose$redirectConnect(ServerAddress var0) {
      return ServerAddressRedirects.К(var0);
   }
}
