package sg.network;

import java.util.Locale;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.FontDescription.Resource;
import net.minecraft.resources.Identifier;
import sg.handler.PartyChatMessage;

public final class IrcRoles {
   private static final String ЯУ = "ꔂ";
   private static final String ЯЗ = "ꔃ";
   private static final String ЯЕ = "ꔆ";
   private static final String ЯП = "ꔇ";
   private static final Set<String> и = Set.of(IrcRoles.Я弟х, IrcRoles.Я弟У, IrcRoles.Я弟З, IrcRoles.Я弟Е);
   private static final FontDescription В_1d9fdaac = new Resource(Identifier.fromNamespaceAndPath(IrcRoles.Я弟П, IrcRoles.Я弟西));
   private static final Style В_87195578 = Style.EMPTY.withFont(В_1d9fdaac).withColor(IrcRoles.Я弟д).withBold(false);
   private static final Style Я = Style.EMPTY.withColor(IrcRoles.Я弟Б).withBold(false);
   private static final String Я弟Н = "ADMIN";
   private static final String Я弟н = "BETA";
   private static final String Я弟Ц = "YOUTUBE";
   private static final String Я弟ч = "ADMIN";
   private static final String Я弟Э = "BETA";
   private static final String Я弟必 = "MEDIA";
   private static final String Я弟щ = "USER";
   private static final String Я弟Г = "DEFAULT";
   private static final String Я弟л = "ADMIN";
   private static final String Я弟э = "BETA";
   private static final String Я弟ю = "YOUTUBE";
   private static final String Я弟Щ = "ꔂ";
   private static final String Я弟у = "ꔃ";
   private static final String Я弟2 = "ꔆ";
   private static final String Я弟ф = "ꔇ";
   private static final String Я弟и = "ꔂ";
   private static final String Я弟9 = "ꔃ";
   private static final String Я弟о = "ꔆ";
   private static final String Я弟O = "ꔇ";
   private static final String Я弟7 = " ";
   private static final String Я弟Р = "ꔂ";
   private static final String Я弟3 = "ꔂ";
   private static final String Я弟р = "ꔃ";
   private static final String Я弟6 = "ꔃ";
   private static final String Я弟_ = "ꔆ";
   private static final String Я弟Ш = "ꔆ";
   private static final String Я弟ы = "ꔇ";
   private static final String Я弟й = "ꔇ";
   private static final String Я弟Ж = "DEFAULT";
   private static final String Я弟м = "ADMIN";
   private static final String Я弟К = "ADMINISTRATOR";
   private static final String Я弟I = "BETA";
   private static final String Я弟Т = "YOUTUBE";
   private static final String Я弟О = "MEDIA";
   private static final String Я弟Ф = "YOUTUBER";
   private static final String Я弟Й = "ADMIN";
   private static final String Я弟к = "BETA";
   private static final String Я弟г = "YOUTUBE";
   private static final String Я弟0 = "DEFAULT";
   private static final String Я弟х = "ADMIN";
   private static final String Я弟У = "BETA";
   private static final String Я弟З = "YOUTUBE";
   private static final String Я弟Е = "DEFAULT";
   private static final String Я弟П = "rose";
   private static final String Я弟西 = "roles";
   private static final int Я弟д = 16777215;
   private static final int Я弟Б = 13158600;

   private IrcRoles() {
   }

   public static String Яп() {
      return "NowUser";
   }

   private static Component Я(String var0, String var1) {
      MutableComponent var2 = Component.empty();
      if (!var0.isEmpty()) {
         var2.append(Component.literal(var0).setStyle(В_87195578));
         var2.append(Я弟7);
      }

      return var2.append(Component.literal(var1).setStyle(Я));
   }

   private static String ЦЕ(String var0) {
      if (var0.startsWith(Я弟Р)) {
         return Я弟3;
      } else if (var0.startsWith(Я弟р)) {
         return Я弟6;
      } else if (var0.startsWith(Я弟_)) {
         return Я弟Ш;
      } else {
         return var0.startsWith(Я弟ы) ? Я弟й : "";
      }
   }

   public static boolean Ц__bbeebd87(String var0) {
      return var0 == null || и.contains(var0.trim().toUpperCase(Locale.ROOT));
   }

   public static Component Х_f022d65c(PartyChatMessage var0) {
      return Я(Цш(var0.role()), "<" + var0.member() + "> " + var0.text());
   }

   public static boolean рП() {
      return false;
   }

   public static String Цш(String var0) {
      if (var0 != null && !var0.isBlank()) {
         return switch (Ц__388008bb(var0)) {
            case Я弟л -> Я弟Щ;
            case Я弟э -> Я弟у;
            case Я弟ю -> Я弟2;
            default -> Я弟ф;
         };
      } else {
         return "";
      }
   }

   public static String Х_388008bb(PartyChatMessage var0) {
      String var1 = Цш(var0.role());
      return (var1.isEmpty() ? "" : var1 + " ") + "<" + var0.member() + "> " + var0.text();
   }

   public static Component method_4(String var0, String var1) {
      String var2 = var1 == null ? "" : var1;
      String var3 = ЦЕ(var2);
      if (!var3.isEmpty()) {
         var2 = var2.substring(var3.length()).stripLeading();
      }

      return Я(var3, var0 + " -> " + var2);
   }

   public static String ЦЖ(String var0) {
      String var1 = var0 == null ? "" : var0;
      return !var1.startsWith(Я弟и) && !var1.startsWith(Я弟9) && !var1.startsWith(Я弟о) && !var1.startsWith(Я弟O) ? Цш(Яп()) + " " + var1 : var1;
   }

   public static String к(String var0, String var1) {
      String var2 = var1 == null ? "" : var1;
      String var3 = ЦЕ(var2);
      if (!var3.isEmpty()) {
         var2 = var2.substring(var3.length()).stripLeading();
      }

      return (var3.isEmpty() ? "" : var3 + " ") + var0 + " -> " + var2;
   }

   public static String Ц7(String var0) {
      return switch (Ц__388008bb(var0)) {
         case Я弟Н -> Я弟ч;
         case Я弟н -> Я弟Э;
         case Я弟Ц -> Я弟必;
         default -> Я弟щ;
      };
   }

   private static String Ц__388008bb(String var0) {
      String var1 = var0 == null ? Я弟Ж : var0.trim().toUpperCase(Locale.ROOT);

      return switch (var1) {
         case Я弟м, Я弟К -> Я弟Й;
         case Я弟I -> Я弟к;
         case Я弟Т, Я弟О, Я弟Ф -> Я弟г;
         default -> Я弟0;
      };
   }
}
