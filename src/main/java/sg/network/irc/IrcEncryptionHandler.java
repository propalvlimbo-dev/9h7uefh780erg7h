package sg.network.irc;

import javax.crypto.spec.SecretKeySpec;
import sg.network.irc.crypto.IrcEncryptionEncoder;
import sg.network.irc.crypto.IrcEncryptionDecoder;
import sg.network.irc.RoseIrc;

public class IrcEncryptionHandler extends IrcPacketHandler<IrcEncryptionPacket> {
   public void х(IrcEncryptionPacket var1) {
      String var2 = var1.Ч();
      byte[] var3 = var1.ъ_1be31765();
      SecretKeySpec var4 = new SecretKeySpec(var3, var2);
      RoseIrc.З_3e6e5516().З_d285c1ed().ч_b50448e0().addBefore("inbound", "decrypt", new IrcEncryptionDecoder(var4));
      RoseIrc.З_3e6e5516().З_d285c1ed().ч_b50448e0().addBefore("outbound", "encrypt", new IrcEncryptionEncoder(var4));
      System.out.println("Encryption enabled!");
   }
}
