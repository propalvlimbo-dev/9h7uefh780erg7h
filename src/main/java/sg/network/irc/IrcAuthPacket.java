package sg.network.irc;

import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcAuthPacket extends IrcPacket {
   public IrcAuthPacket(String var1) {
      super(new IrcPacketMetadata("auth", IrcPacketDirection.BOTH, var1));
   }

   @Override
   public void З() {
   }
}
