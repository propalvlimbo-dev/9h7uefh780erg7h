package sg.network.irc;

import sg.modules.impl.init.CallbackIrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcMessagePacket extends CallbackIrcPacket implements IrcResponseCallbacks {
   @Override
   public void З() {
      new IrcMessageHandler().х(this);
   }

   public IrcMessagePacket(String var1) {
      super(new IrcPacketMetadata("message", IrcPacketDirection.BOTH, var1));
   }
}
