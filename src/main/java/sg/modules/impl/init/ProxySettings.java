package sg.modules.impl.init;

import lombok.Generated;

public class ProxySettings {
   private final ProxySettings.Type В;
   private final String си;
   private final int ЯФ;
   private final String с9;
   private final String со;

   @Generated
   public ProxySettings.Type method_8() {
      return this.В;
   }

   @Generated
   public int 诶е() {
      return this.ЯФ;
   }

   @Generated
   public String method_8Х() {
      return this.со;
   }

   @Generated
   public String method_8ь() {
      return this.си;
   }

   @Generated
   public String method_8х() {
      return this.с9;
   }

   @Generated
   public ProxySettings(ProxySettings.Type var1, String var2, int var3, String var4, String var5) {
      this.В = var1;
      this.си = var2;
      this.ЯФ = var3;
      this.с9 = var4;
      this.со = var5;
   }

   public enum Type {
      HTTP,
      SOCKS4,
      SOCKS5;
   }
}
