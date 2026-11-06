package sg.ec;

import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

public final class ForcedPlayerTarget {
   public static final int Дш = 16733525;
   private static volatile String Я必;
   private static final int ХЯ弟 = 16733525;

   public static boolean ло_bbeebd87(String var0) {
      return method_3и() && var0 != null && var0.equalsIgnoreCase(Я必);
   }

   private ForcedPlayerTarget() {
   }

   public static boolean ю(Player var0) {
      return var0 != null && ло_bbeebd87(var0.getGameProfile().name());
   }

   public static String ОМ() {
      return Я必;
   }

   public static boolean method_3и() {
      return Я必 != null && !Я必.isBlank();
   }

   public static void ло_de5a6f78(String var0) {
      Я必 = var0 != null && !var0.isBlank() ? var0 : null;
   }

   public static Component о(Component var0) {
      if (var0 == null) {
         return null;
      }

      MutableComponent var1 = Component.empty();
      var0.visit((var1x, var2) -> {
         if (!var2.isEmpty()) {
            var1.append(Component.literal(var2).setStyle(var1x.withColor(ХЯ弟)));
         }

         return Optional.empty();
      }, Style.EMPTY);
      return var1;
   }

   public static void ЪФ() {
      Я必 = null;
   }
}
