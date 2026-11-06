package sg.network.irc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import sg.network.irc.custom.IrcCustomPacket;

public class IrcCustomPacketDispatcher extends SimpleChannelInboundHandler<IrcCustomPacket> {
   protected void channelRead0(ChannelHandlerContext var1, IrcCustomPacket var2) throws Exception {
      var2.Жу();
   }
}
