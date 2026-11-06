package sg.network.irc;

import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcPartyDisbandPacket extends IrcPacket {
   @Override
   public void З() {
   }

   public IrcPartyDisbandPacket() {
      super(new IrcPacketMetadata("party_disband", IrcPacketDirection.SERVER));
   }
}
