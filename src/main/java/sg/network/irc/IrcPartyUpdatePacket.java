package sg.network.irc;

import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcPartyUpdatePacket extends IrcPacket {
   @Override
   public void З() {
      new IrcPartyUpdateHandler().х(this);
   }

   public IrcPartyUpdatePacket() {
      super(new IrcPacketMetadata("party_update", IrcPacketDirection.CLIENT));
   }
}
