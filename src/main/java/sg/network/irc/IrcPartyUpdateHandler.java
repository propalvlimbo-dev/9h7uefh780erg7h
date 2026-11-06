package sg.network.irc;

import sg.handler.IrcMemberName;
import sg.network.irc.RoseIrc;

public class IrcPartyUpdateHandler extends IrcPacketHandler<IrcPartyUpdatePacket> {
   public void х(IrcPartyUpdatePacket var1) {
      String[] var2 = var1.ъ_91e8a6f5().ъ2().split(":");
      RoseIrc.З_3e6e5516().З_677a0540().М_8cef9e59().clear();

      for (String var6 : var2) {
         RoseIrc.З_3e6e5516().З_677a0540().М_8cef9e59().add(new IrcMemberName(var6));
      }
   }
}
