package sg.network.irc;

import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcPartyLeavePacket extends IrcPacket {
   public IrcPartyLeavePacket() {
      super(new IrcPacketMetadata("party_leave", IrcPacketDirection.SERVER));
   }

   @Override
   public void З() {
   }
}
