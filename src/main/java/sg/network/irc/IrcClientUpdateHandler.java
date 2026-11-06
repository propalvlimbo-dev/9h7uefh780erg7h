package sg.network.irc;

import sg.network.irc.IrcClientUpdateEntry;
import sg.network.irc.RoseIrc;

public class IrcClientUpdateHandler extends IrcPacketHandler<IrcClientUpdatePacket> {
   public void х(IrcClientUpdatePacket var1) {
      String[] var2 = var1.ъ_91e8a6f5().ъ2().split(":");
      RoseIrc.З_3e6e5516().З_677a0540().М_7a59fa43().clear();

      for (String var6 : var2) {
         RoseIrc.З_3e6e5516().З_677a0540().М_7a59fa43().add(new IrcClientUpdateEntry(var6));
      }
   }
}
