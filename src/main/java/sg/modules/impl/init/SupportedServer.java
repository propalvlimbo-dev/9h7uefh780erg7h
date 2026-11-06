package sg.modules.impl.init;

import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public enum SupportedServer {
   FUNTIME("FunTime", "FT", "play.funtime.su", List.of("play.funtime.su", "mc.funtime.su", "funtime.su")),
   HOLY_WORLD("HolyWorld", "HW", "mc.holyworld.ru", List.of("mc.holyworld.ru", "play.holyworld.ru", "holyworld.ru")),
   REALLY_WORLD("ReallyWorld", "RW", "mc.reallyworld.ru", List.of("mc.reallyworld.ru", "play.reallyworld.ru", "reallyworld.ru"));

   private final String ВЗ;
   private final String ВЕ;
   private final String ВП;
   private final List<String> сю;

   SupportedServer(String var3, String var4, String var5, List<String> var6) {
      this.ВЗ = var3;
      this.ВЕ = var4;
      this.ВП = var5;
      this.сю = var6;
   }

   public String 诶衣() {
      return this.ВЗ;
   }

   public String 诶2() {
      return this.ВЕ;
   }

   public String 诶I() {
      return this.ВП;
   }

   public boolean у(String var1) {
      String var2 = method_1(var1);
      if (var2.isEmpty()) {
         return false;
      }

      for (String var4 : this.сю) {
         if (var2.equals(var4)) {
            return true;
         }
      }

      return false;
   }

   public boolean Х(String var1) {
      String var2 = method_1(var1);
      if (var2.isEmpty()) {
         return false;
      }

      String var3 = this.сю.get(this.сю.size() - 1);
      return var2.equals(var3) || var2.endsWith("." + var3);
   }

   public static SupportedServer method_8(String var0) {
      for (SupportedServer var4 : values()) {
         if (var4.у(var0)) {
            return var4;
         }
      }

      return null;
   }

   public static SupportedServer method_8() {
      Minecraft var0 = Minecraft.getInstance();
      ServerData var1 = var0.getCurrentServer();
      if (var1 == null && var0.getConnection() != null) {
         var1 = var0.getConnection().getServerData();
      }

      if (var1 == null) {
         return null;
      }

      for (SupportedServer var5 : values()) {
         if (var5.Х(var1.ip)) {
            return var5;
         }
      }

      return null;
   }

   private static String method_1(String var0) {
      if (var0 != null && !var0.isBlank()) {
         String var1 = var0.trim().toLowerCase(Locale.ROOT);
         if (var1.startsWith("[")) {
            int var2 = var1.indexOf(93);
            if (var2 != -1) {
               var1 = var1.substring(1, var2);
            }
         } else {
            int var3 = var1.lastIndexOf(58);
            if (var3 != -1 && var1.indexOf(58) == var3) {
               var1 = var1.substring(0, var3);
            }
         }

         if (var1.endsWith(".")) {
            var1 = var1.substring(0, var1.length() - 1);
         }

         return var1;
      } else {
         return "";
      }
   }
}
