package sg.network.irc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.Generated;

public class IrcLanguageResource {
   private final String 必;

   @Generated
   public String аы() {
      return this.必;
   }

   @Generated
   public IrcLanguageResource(String var1) {
      this.必 = var1;
   }

   public String ар() {
      try (InputStream var1 = this.getClass().getResourceAsStream("/lang/" + this.必)) {
         return var1 == null ? null : new String(var1.readAllBytes(), StandardCharsets.UTF_8);
      } catch (Exception var6) {
         System.out.println(var6.getMessage());
         return null;
      }
   }
}
