package sg.network.irc;

import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcClientUpdatePacket extends IrcPacket {
   @Override
   public void З() {
      new IrcClientUpdateHandler().х(this);
   }

   public IrcClientUpdatePacket() {
      super(new IrcPacketMetadata("client_update", IrcPacketDirection.CLIENT));
   }
}
