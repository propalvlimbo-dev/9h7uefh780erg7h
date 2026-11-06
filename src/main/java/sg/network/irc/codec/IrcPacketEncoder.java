package sg.network.irc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import sg.network.irc.codec.IrcPacketCodec;

public class IrcPacketEncoder extends ChannelOutboundHandlerAdapter {
   @Override
   public void flush(ChannelHandlerContext var1) throws Exception {
      super.flush(var1);
   }

   @Override
   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      ByteBuf var4 = IrcPacketCodec.Ж(var2, var1.alloc());
      if (var4 == null) {
         System.out.println("Cannot write null data!");
      } else {
         var1.write(var4, var3);
      }
   }
}
