package sg.network.irc.crypto;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class IrcEncryptionDecoder extends MessageToMessageDecoder<ByteBuf> {
   private final SecretKey Я;

   public IrcEncryptionDecoder(SecretKey var1) {
      this.Я = var1;
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) {
      var1.fireExceptionCaught(var2);
      var1.close();
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      if (var2.readableBytes() < 28) {
         throw new CorruptedFrameException("Truncated encrypted IRC frame");
      }

      byte[] var4 = new byte[12];
      var2.readBytes(var4);
      byte[] var5 = new byte[var2.readableBytes()];
      var2.readBytes(var5);
      Cipher var6 = Cipher.getInstance("AES/GCM/NoPadding");
      var6.init(2, this.Я, new GCMParameterSpec(128, var4));
      var3.add(Unpooled.wrappedBuffer(var6.doFinal(var5)));
   }
}
