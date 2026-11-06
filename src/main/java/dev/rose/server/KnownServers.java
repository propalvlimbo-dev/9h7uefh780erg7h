package dev.rose.server;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.language.I18n;


public final class KnownServers {
   private static final List<KnownServers.Server> WHITELISTED = List.of(
      new KnownServers.Server("FunTime", List.of("funtime.su", "funtime.ru")),
      new KnownServers.Server("ReallyWorld", List.of("reallyworld.ru", "reallyworld.me")),
      new KnownServers.Server("HolyWorld", List.of("holyworld.me", "holyworld.ru", "holyworld.io"))
   );

   private KnownServers() {
   }

   public static String currentHost() {
      Minecraft mc = Minecraft.getInstance();
      ServerData data = mc.getCurrentServer();
      return !mc.hasSingleplayerServer() && data != null ? normalize(data.ip) : null;
   }

   public static Optional<KnownServers.Server> whitelisted(String host) {
      return host == null ? Optional.empty() : WHITELISTED.stream().filter(server -> server.matches(host)).findFirst();
   }

   public static String currentName() {
      String host = currentHost();
      return host == null ? I18n.get("rose.server.singleplayer", new Object[0]) : whitelisted(host).map(KnownServers.Server::name).orElse(host);
   }

   public static String normalizeAddress(String address) {
      return normalize(address);
   }

   static String normalize(String address) {
      String host = address.trim().toLowerCase(Locale.ROOT);
      int colon = host.lastIndexOf(58);
      if (colon > 0 && host.indexOf(58) == colon) {
         host = host.substring(0, colon);
      }

      while (host.endsWith(".")) {
         host = host.substring(0, host.length() - 1);
      }

      return host;
   }

   public record Server(String name, List<String> domains) {
      boolean matches(String host) {
         for (String domain : this.domains) {
            if (host.equals(domain) || host.endsWith("." + domain)) {
               return true;
            }
         }

         return false;
      }
   }
}
