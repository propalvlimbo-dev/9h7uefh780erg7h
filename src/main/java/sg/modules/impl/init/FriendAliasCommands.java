package sg.modules.impl.init;

import java.util.Comparator;
import java.util.Locale;
import lombok.Generated;
import sg.client.RoseClient;

public final class FriendAliasCommands {
   private static final String сlч = "/";
   private static final String сlЭ = "tp";
   private static final String сl必 = "tpa";
   private static final String сlщ = "tpahere";
   private static final String сlГ = "This is a utility class and cannot be instantiated";

   @Generated
   private FriendAliasCommands() {
      throw new UnsupportedOperationException(сlГ);
   }

   public static String х(String var0) {
      if (var0 != null && var0.startsWith(сlч)) {
         int var1 = var0.indexOf(32);
         String var2 = (var1 < 0 ? var0.substring(1) : var0.substring(1, var1)).toLowerCase(Locale.ROOT);
         if (!var2.equals(сlЭ) && !var2.equals(сl必) && !var2.equals(сlщ)) {
            return var0;
         }

         int var3 = var1;

         while (var3 >= 0 && var3 < var0.length() && Character.isWhitespace(var0.charAt(var3))) {
            var3++;
         }

         if (var3 >= 0 && var3 < var0.length()) {
            String var4 = var0.substring(var3);
            FriendEntry var5 = RoseClient.getInstance()
               .getServices()
               .getFriends()
               .stream()
               .filter(FriendEntry::lк)
               .filter(var1x -> method__(var4, var1x.l3()))
               .max(Comparator.comparingInt(var0x -> var0x.l3().length()))
               .orElse(null);
            if (var5 == null) {
               return var0;
            }

            String var6 = var5.l3();
            return var0.substring(0, var3) + var5.lз() + var4.substring(var6.length());
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   private static boolean method__(String var0, String var1) {
      return !var0.regionMatches(true, 0, var1, 0, var1.length())
         ? false
         : var0.length() == var1.length() || Character.isWhitespace(var0.charAt(var1.length()));
   }
}
