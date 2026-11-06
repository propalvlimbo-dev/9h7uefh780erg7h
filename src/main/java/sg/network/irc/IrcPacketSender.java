package sg.network.irc;

import lombok.Generated;
import sg.network.irc.custom.IrcCustomPacket;
import sg.network.irc.packet.IrcPacket;
import sg.network.irc.RoseIrc;

public final class IrcPacketSender {
   @Generated
   private IrcPacketSender() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void Щ(Object var0) {
      if (!(var0 instanceof IrcPacket) && !(var0 instanceof IrcCustomPacket)) {
         System.out.println("Cannot send not packet!");
      } else {
         RoseIrc.З_3e6e5516().З_d285c1ed().ч_04e5b147().writeAndFlush(var0);
      }
   }
}
