package sg.network.irc;

import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcPartyCreatePacket extends IrcPacket {
   @Override
   public void З() {
   }

   public IrcPartyCreatePacket(String var1) {
      super(new IrcPacketMetadata("party_create", IrcPacketDirection.SERVER, var1));
   }
}
