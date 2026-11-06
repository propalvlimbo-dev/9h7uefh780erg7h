package sg.mixin;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.PacketEventBypass;
import sg.events.impl.PacketReceiveEvent;
import sg.events.impl.PacketSendEvent;
import sg.events.impl.PacketSentEvent;
import sg.client.RoseClient;

@Mixin(Connection.class)
public class ConnectionPacketEventsMixin {
   @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"), cancellable = true)
   private void onSend(Packet<?> var1, @Nullable ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      boolean var5 = PacketEventBypass.хI().remove(var1);
      Minecraft var6 = Minecraft.getInstance();
      if (var6.player != null && var6.getConnection() != null) {
         if (!var5) {
            PacketSendEvent var7 = new PacketSendEvent(var1);
            RoseClient.getInstance().getEventBus().post(var7);
            if (var7.isCancelled()) {
               var4.cancel();
               return;
            }
         }

         RoseClient.getInstance().getEventBus().post(new PacketSentEvent(var1));
      }
   }

   @Inject(
      method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
      at = @At(value = "FIELD", target = "Lnet/minecraft/network/Connection;packetListener:Lnet/minecraft/network/PacketListener;", opcode = 180),
      cancellable = true
   )
   private void onReceive(ChannelHandlerContext var1, Packet<?> var2, CallbackInfo var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.getConnection() != null) {
         PacketReceiveEvent var5 = new PacketReceiveEvent(var2);
         RoseClient.getInstance().getEventBus().post(var5);
         if (var5.isCancelled()) {
            var3.cancel();
         }
      }
   }
}
