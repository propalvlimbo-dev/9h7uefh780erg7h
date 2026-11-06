package sg.mixin;

import io.netty.channel.Channel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.client.RoseClient;

@Mixin(targets = "net.minecraft.network.Connection$1")
public class ConnectionChannelInitializerMixin {
   @Inject(
      method = "initChannel",
      at = @At(
         value = "INVOKE",
         target = "Lio/netty/channel/ChannelConfig;setOption(Lio/netty/channel/ChannelOption;Ljava/lang/Object;)Z",
         shift = Shift.AFTER
      )
   )
   private void initChannel(Channel var1, CallbackInfo var2) {
      RoseClient.getInstance().getServices().getProxyManager().Х(var1.pipeline());
   }
}
