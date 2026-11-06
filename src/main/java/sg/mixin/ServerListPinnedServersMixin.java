package sg.mixin;

import java.util.List;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settings.render.PinnedServers;

@Mixin(ServerList.class)
public class ServerListPinnedServersMixin {
   @Shadow
   @Final
   private List<ServerData> serverList;

   @Inject(method = "load", at = @At("RETURN"))
   private void rose$addPinnedServers(CallbackInfo var1) {
      if (PinnedServers.Д(this.serverList)) {
         ((ServerList)(Object)this).save();
      }
   }

   @Inject(method = "remove", at = @At("RETURN"))
   private void rose$keepPinnedServers(ServerData var1, CallbackInfo var2) {
      if (PinnedServers.Д_bbeebd87(var1)) {
         PinnedServers.Д(this.serverList);
      }
   }

   @Inject(method = "swap", at = @At("RETURN"))
   private void rose$keepPinnedOrder(int var1, int var2, CallbackInfo var3) {
      PinnedServers.Д(this.serverList);
   }
}
