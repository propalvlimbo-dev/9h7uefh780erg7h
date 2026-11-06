package sg.network.irc.packet;

import lombok.Generated;

public enum IrcPacketDirection {
   CLIENT("cli"),
   SERVER("srv"),
   BOTH("both");

   private final String сТ;

   @Generated
   public String МН() {
      return this.сТ;
   }

   @Generated
   IrcPacketDirection(String var3) {
      this.сТ = var3;
   }
}
