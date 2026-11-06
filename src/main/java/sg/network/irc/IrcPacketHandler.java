package sg.network.irc;

import sg.network.irc.packet.IrcPacket;

public abstract class IrcPacketHandler<T extends IrcPacket> {
   public abstract void х(T var1);
}
