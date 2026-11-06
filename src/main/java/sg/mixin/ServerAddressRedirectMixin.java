package sg.mixin;

import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import settings.render.ServerAddressRedirects;

@Mixin(ServerAddress.class)
public class ServerAddressRedirectMixin {
   @ModifyVariable(method = "parseString", at = @At("HEAD"), argsOnly = true)
   private static String rose$redirectAddress(String var0) {
      return ServerAddressRedirects.method_5(var0);
   }
}
