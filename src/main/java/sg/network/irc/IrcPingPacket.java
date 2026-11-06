package sg.network.irc;

import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcPingPacket extends IrcPacket {
   @Override
   public void З() {
      new IrcPingHandler().х(this);
   }

   public IrcPingPacket() {
      super(new IrcPacketMetadata("ping", IrcPacketDirection.BOTH));
   }
}
