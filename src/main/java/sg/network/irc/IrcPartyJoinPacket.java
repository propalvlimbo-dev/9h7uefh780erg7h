package sg.network.irc;

import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcPartyJoinPacket extends IrcPacket {
   @Override
   public void З() {
   }

   public IrcPartyJoinPacket(String var1) {
      super(new IrcPacketMetadata("party_join", IrcPacketDirection.SERVER, var1));
   }
}
