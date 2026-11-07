package settings.render;

import java.util.List;
import java.util.Locale;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerData.Type;

public final class PinnedServers {
   private static final List<PinnedServers.ServerEntry> сЪ = List.of(
      new PinnedServers.ServerEntry(PinnedServers.МЕ, PinnedServers.МА, PinnedServers.МВ),
      new PinnedServers.ServerEntry(PinnedServers.М7, PinnedServers.МР, PinnedServers.М3)
   );
   private static final String МЕ = "Elytrix";
   private static final String МА = "mc.elytrix.pw";
   private static final int МВ = 1726228811;
   private static final String МO = ":25565";
   private static final String М7 = "БравоХвХ";
   private static final String МР = "mc.bravohvh.ru";
   private static final int М3 = 1726298421;

   private static boolean х(List<ServerData> var0) {
      if (var0.size() < сЪ.size()) {
         return false;
      }

      for (int var1 = 0; var1 < сЪ.size(); var1++) {
         PinnedServers.ServerEntry var2 = сЪ.get(var1);
         ServerData var3 = (ServerData)var0.get(var1);
         if (!Ю(var3.ip, var2.address()) || !var2.name().equals(var3.name)) {
            return false;
         }
      }

      return true;
   }

   public static boolean Д(List<ServerData> var0) {
      if (var0 != null && !х(var0)) {
         ServerData[] var1 = new ServerData[сЪ.size()];

         for (int var2 = 0; var2 < сЪ.size(); var2++) {
            PinnedServers.ServerEntry var3 = сЪ.get(var2);
            ServerData var4 = Д(var0, var3.address());
            if (var4 != null) {
               var4.name = var3.name();
               var1[var2] = var4;
            } else {
               var1[var2] = new ServerData(var3.name(), var3.address(), Type.OTHER);
            }
         }

         for (int var5 = var1.length - 1; var5 >= 0; var5--) {
            var0.add(0, var1[var5]);
         }

         return true;
      } else {
         return false;
      }
   }

   private static ServerData Д(List<ServerData> var0, String var1) {
      ServerData var2 = null;
      int var3 = 0;

      while (var3 < var0.size()) {
         ServerData var4 = (ServerData)var0.get(var3);
         if (!Ю(var4.ip, var1)) {
            var3++;
         } else {
            ServerData var5 = (ServerData)var0.remove(var3);
            if (var2 == null) {
               var2 = var5;
            }
         }
      }

      return var2;
   }

   public static PinnedServers.HighlightStyle Д_aee17b97(ServerData var0) {
      if (var0 == null) {
         return null;
      }

      for (PinnedServers.ServerEntry var2 : сЪ) {
         if (Ю(var0.ip, var2.address())) {
            return new PinnedServers.HighlightStyle(var2.fill());
         }
      }

      return null;
   }

   public static boolean Д_bbeebd87(ServerData var0) {
      return var0 != null && Э(var0.ip);
   }

   private PinnedServers() {
   }

   private static boolean Ю(String var0, String var1) {
      return 诶(var0).equals(诶(var1));
   }

   private static boolean Э(String var0) {
      for (PinnedServers.ServerEntry var2 : сЪ) {
         if (Ю(var0, var2.address())) {
            return true;
         }
      }

      return false;
   }

   private static String 诶(String var0) {
      if (var0 == null) {
         return "";
      }

      String var1 = var0.trim().toLowerCase(Locale.ROOT);
      return var1.endsWith(МO) ? var1.substring(0, var1.length() - 6) : var1;
   }

   public record HighlightStyle(int fill) {
      public int fill(boolean var1) {
         if (!var1) {
            return this.fill;
         }

         int var2 = Math.min(255, (this.fill >>> 24 & 0xFF) + 34);
         return var2 << 24 | this.fill & 16777215;
      }
   }

   private record ServerEntry(String name, String address, int fill) {
   }
}
