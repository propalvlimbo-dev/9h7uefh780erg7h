package sg.network.irc.packet;

import com.google.gson.annotations.SerializedName;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.CorruptedFrameException;
import java.nio.charset.StandardCharsets;

public abstract class IrcPacket {
   @SerializedName("В")
   protected final IrcPacketMetadata В_91e8a6f5;
   @SerializedName("В")
   private final ByteBuf В_3279c64b = Unpooled.buffer(0, 1048576);

   public void ъ(byte[] var1) {
      this.В_3279c64b.writeInt(var1.length);
      this.В_3279c64b.writeBytes(var1);
   }

   public String Ч() {
      return new String(this.ъ_1be31765(), StandardCharsets.UTF_8);
   }

   @Override
   public String toString() {
      return "Packet[name=" + this.В_91e8a6f5.ъЩ() + ", bound=" + this.В_91e8a6f5.Э() + "]";
   }

   public abstract void З();

   public IrcPacket(IrcPacketMetadata var1) {
      this.В_91e8a6f5 = var1;
   }

   public void е(String var1) {
      this.ъ(var1.getBytes(StandardCharsets.UTF_8));
   }

   public IrcPacketMetadata ъ_91e8a6f5() {
      return this.В_91e8a6f5;
   }

   public byte[] ъ_1be31765() {
      if (this.В_3279c64b.readableBytes() < 4) {
         throw new CorruptedFrameException("Missing IRC packet field");
      } else {
         int var1 = this.В_3279c64b.readInt();
         if (var1 >= 0 && var1 <= this.В_3279c64b.readableBytes()) {
            byte[] var2 = new byte[var1];
            this.В_3279c64b.readBytes(var2);
            return var2;
         } else {
            throw new CorruptedFrameException("Invalid IRC packet field length");
         }
      }
   }

   public ByteBuf ъ_3279c64b() {
      return this.В_3279c64b;
   }
}
