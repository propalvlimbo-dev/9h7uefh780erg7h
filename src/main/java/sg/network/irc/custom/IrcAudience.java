package sg.network.irc.custom;

import lombok.Generated;

public enum IrcAudience {
   ALL_USERS("all_users"),
   PARTY_ONLY("party_only"),
   USER_ONLY("user_only"),
   PARTY_USER_ONLY("party_user");

   private final String В1;

   public static IrcAudience М(String var0) {
      for (IrcAudience var4 : values()) {
         if (var4.М6().equals(var0)) {
            return var4;
         }
      }

      return null;
   }

   @Generated
   public String М6() {
      return this.В1;
   }

   @Generated
   IrcAudience(String var3) {
      this.В1 = var3;
   }
}
