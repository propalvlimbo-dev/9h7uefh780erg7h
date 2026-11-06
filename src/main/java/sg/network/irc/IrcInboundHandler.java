package sg.network.irc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import sg.network.irc.packet.IrcPacket;
import sg.modules.impl.init.CallbackIrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.RoseIrc;

public class IrcInboundHandler extends SimpleChannelInboundHandler<IrcPacket> {
   protected void channelRead0(ChannelHandlerContext var1, IrcPacket var2) throws Exception {
      if (var2.ъ_91e8a6f5().Э().equals(IrcPacketDirection.BOTH) || var2.ъ_91e8a6f5().Э().equals(IrcPacketDirection.CLIENT)) {
         var2.З();
         if (var2 instanceof CallbackIrcPacket var3) {
            for (CallbackIrcPacket var5 : RoseIrc.З_3e6e5516().З_677a0540().М_6aee10a2()) {
               if (var3.ъ_91e8a6f5().equals(var5.ъ_91e8a6f5())) {
                  var5.П().Б(var3.ъ_91e8a6f5().ъ2());
                  RoseIrc.З_3e6e5516().З_677a0540().М_6aee10a2().remove(var5);
               }
            }
         }
      }
   }
}
