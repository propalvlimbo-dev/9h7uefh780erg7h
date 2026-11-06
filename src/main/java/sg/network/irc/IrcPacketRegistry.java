package sg.network.irc;

import java.util.ArrayList;
import sg.network.irc.packet.IrcPacket;

public class IrcPacketRegistry extends ArrayList<IrcPacket> {
   public IrcPacketRegistry() {
      this.add(new IrcAuthPacket(""));
      this.add(new IrcPingPacket());
      this.add(new IrcMessagePacket(""));
      this.add(new IrcEncryptionPacket());
      this.add(new IrcDebugMessagePacket());
      this.add(new IrcPartyUpdatePacket());
      this.add(new IrcClientUpdatePacket());
   }
}
