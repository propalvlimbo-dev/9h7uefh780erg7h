package sg.network.irc.custom;

import lombok.Generated;

public class IrcPacket {
   private final String ЯВ;
   private final IrcAudience В;
   private String ЯЯ;

   @Generated
   public void О(String var1) {
      this.ЯЯ = var1;
   }

   @Generated
   public String method_1п() {
      return this.ЯЯ;
   }

   @Generated
   public IrcPacket(String var1, IrcAudience var2, String var3) {
      this.ЯВ = var1;
      this.В = var2;
      this.ЯЯ = var3;
   }

   @Generated
   public IrcAudience э() {
      return this.В;
   }

   @Generated
   public String method_1В() {
      return this.ЯВ;
   }
}
