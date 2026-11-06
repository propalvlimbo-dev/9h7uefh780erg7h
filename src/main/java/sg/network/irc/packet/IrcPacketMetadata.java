package sg.network.irc.packet;

import lombok.Generated;

public class IrcPacketMetadata {
   private final String В西;
   private final IrcPacketDirection В;
   private String Вд = "";

   @Generated
   public void и(String var1) {
      this.Вд = var1;
   }

   @Override
   public boolean equals(Object var1) {
      return !(var1 instanceof IrcPacketMetadata var2) ? super.equals(var1) : this.В西.equals(var2.ъЩ()) && this.В.equals(var2.Э());
   }

   @Generated
   public String ъЩ() {
      return this.В西;
   }

   @Generated
   public IrcPacketDirection Э() {
      return this.В;
   }

   @Generated
   public IrcPacketMetadata(String var1, IrcPacketDirection var2, String var3) {
      this.В西 = var1;
      this.В = var2;
      this.Вд = var3;
   }

   @Generated
   public IrcPacketMetadata(String var1, IrcPacketDirection var2) {
      this.В西 = var1;
      this.В = var2;
   }

   @Generated
   public String ъ2() {
      return this.Вд;
   }
}
