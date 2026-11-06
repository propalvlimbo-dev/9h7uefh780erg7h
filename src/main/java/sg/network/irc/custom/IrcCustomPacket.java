package sg.network.irc.custom;

import lombok.Generated;

public abstract class IrcCustomPacket {
   private final IrcPacket В;

   public abstract void Жу();

   public IrcCustomPacket(IrcPacket var1) {
      this.В = new IrcPacket("custom_" + var1.method_1В(), var1.э(), var1.method_1п());
   }

   @Generated
   public IrcPacket method_0() {
      return this.В;
   }
}
