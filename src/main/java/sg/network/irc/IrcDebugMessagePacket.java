package sg.network.irc;

import sg.modules.impl.init.CallbackIrcPacket;
import sg.network.irc.packet.IrcPacketDirection;
import sg.network.irc.packet.IrcPacketMetadata;

public class IrcDebugMessagePacket extends CallbackIrcPacket implements IrcResponseCallbacks {
   public IrcDebugMessagePacket() {
      super(new IrcPacketMetadata("debug_message", IrcPacketDirection.CLIENT));
   }

   @Override
   public void З() {
      new IrcDebugMessageHandler().х(this);
   }
}
