package sg.network.irc.crypto;

import com.google.gson.annotations.SerializedName;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class IrcEncryptionEncoder extends MessageToByteEncoder<ByteBuf> {
   @SerializedName("В")
   private final SecretKey В_c0e9628c;
   @SerializedName("В")
   private final SecureRandom В_51159ae8 = new SecureRandom();

   public IrcEncryptionEncoder(SecretKey var1) {
      this.В_c0e9628c = var1;
   }

   protected void encode(ChannelHandlerContext var1, ByteBuf var2, ByteBuf var3) throws Exception {
      byte[] var4 = new byte[12];
      this.В_51159ae8.nextBytes(var4);
      byte[] var5 = new byte[var2.readableBytes()];
      var2.readBytes(var5);
      Cipher var6 = Cipher.getInstance("AES/GCM/NoPadding");
      var6.init(1, this.В_c0e9628c, new GCMParameterSpec(128, var4));
      var3.writeBytes(var4);
      var3.writeBytes(var6.doFinal(var5));
   }
}
