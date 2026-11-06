package sg.network.irc;

import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcEncryptionPacket extends IrcPacket {
   public IrcEncryptionPacket() {
      super(new IrcPacketMetadata("encryption", IrcPacketDirection.CLIENT, ""));
   }

   @Override
   public void З() {
      new IrcEncryptionHandler().х(this);
   }
}
