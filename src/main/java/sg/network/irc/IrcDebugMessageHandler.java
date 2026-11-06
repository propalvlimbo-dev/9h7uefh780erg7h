package sg.network.irc;

import sg.network.irc.RoseIrc;

public class IrcDebugMessageHandler extends IrcPacketHandler<IrcDebugMessagePacket> {
   public void х(IrcDebugMessagePacket var1) {
      String[] var2 = var1.ъ_91e8a6f5().ъ2().split(":");
      var1.н(var2[0], RoseIrc.З_3e6e5516().З_677a0540().М_5774a79f().д().getOrDefault(var2[1], var2[1]));
   }
}
