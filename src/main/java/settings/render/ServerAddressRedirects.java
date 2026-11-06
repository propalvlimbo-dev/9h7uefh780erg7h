package settings.render;

import java.util.Locale;
import java.util.Set;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public final class ServerAddressRedirects {
   private static final String ВМ = "redirect.bravohvh.fun";
   private static final Set<String> Н = Set.of(ServerAddressRedirects.сЙУ, ServerAddressRedirects.сЙЗ, ServerAddressRedirects.сЙЕ);
   private static final String сЙк = "redirect.bravohvh.fun";
   private static final String сЙг = "redirect.bravohvh.fun";
   private static final String сЙ0 = ".";
   private static final String сЙх = "[";
   private static final String сЙУ = "mc.svinworld.space";
   private static final String сЙЗ = "mc.svinworld.fun";
   private static final String сЙЕ = "46.174.48.116";

   private static String ю(String var0) {
      if (var0 == null) {
         return "";
      }

      String var1 = var0.trim().toLowerCase(Locale.ROOT);
      return var1.endsWith(сЙ0) ? var1.substring(0, var1.length() - 1) : var1;
   }

   private ServerAddressRedirects() {
   }

   public static String method_5(String var0) {
      if (var0 != null && !var0.isBlank()) {
         ServerAddressRedirects.HostPort var1 = К(var0.trim());
         if (!Н.contains(var1.host())) {
            return var0;
         } else {
            return var1.port() != null ? "redirect.bravohvh.fun:" + var1.port() : сЙк;
         }
      } else {
         return var0;
      }
   }

   private static ServerAddressRedirects.HostPort К(String var0) {
      String var1 = var0;
      String var2 = null;
      if (var0.startsWith(сЙх)) {
         int var3 = var0.indexOf(93);
         if (var3 != -1) {
            var1 = var0.substring(1, var3);
            if (var3 + 1 < var0.length() && var0.charAt(var3 + 1) == ':') {
               var2 = var0.substring(var3 + 2);
            }
         }
      } else {
         int var4 = var0.lastIndexOf(58);
         if (var4 != -1 && var0.indexOf(58) == var4) {
            var1 = var0.substring(0, var4);
            var2 = var0.substring(var4 + 1);
         }
      }

      if (var2 != null && var2.isBlank()) {
         var2 = null;
      }

      return new ServerAddressRedirects.HostPort(ю(var1), var2);
   }

   public static ServerAddress К(ServerAddress var0) {
      if (var0 == null) {
         return null;
      } else if (!Н.contains(ю(var0.getHost()))) {
         return var0;
      } else {
         return var0.getPort() == 25565 ? ServerAddress.parseString(сЙг) : ServerAddress.parseString("redirect.bravohvh.fun:" + var0.getPort());
      }
   }

   private record HostPort(String host, String port) {
   }
}
