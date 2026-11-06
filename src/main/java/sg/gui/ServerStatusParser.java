package sg.gui;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import sg.ec.MinecraftAccess;
import sg.mixin.BossHealthOverlayAccessor;
import sg.mixin.PlayerTabOverlayAccessor;

public final class ServerStatusParser implements MinecraftAccess {
   private static final Pattern е = Pattern.compile(ServerStatusParser.Вуе);
   private static final Pattern А_at_786f4923 = Pattern.compile(ServerStatusParser.ВуА);
   private static final Pattern field_4 = Pattern.compile(ServerStatusParser.Ву4);
   private static final Pattern п = Pattern.compile(ServerStatusParser.Вуп);
   private static final String ВуВ = "pvp";
   private static final String ВуЯ = "пвп";
   private static final String ВуХ = "дуэль";
   private static final String Вуе = "Анархия-\\s*(\\d+)";
   private static final String ВуА = "(?:-|#)(\\d+)";
   private static final String Ву4 = "(\\d+):(\\d+)";
   private static final String Вуп = "(\\d+)";

   public static boolean З_bbeebd87() {
      return ц() >= 0;
   }

   public static int method_6_a83dd0cc() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gui.hud != null) {
         Component var0 = ((PlayerTabOverlayAccessor)MINECRAFT_CLIENT.gui.hud.getTabList()).getHeader();
         if (var0 != null) {
            Matcher var1 = е.matcher(var0.getString());
            if (var1.find()) {
               return Integer.parseInt(var1.group(1));
            }
         }

         if (MINECRAFT_CLIENT.level == null) {
            return -1;
         }

         Objective var3 = MINECRAFT_CLIENT.level.getScoreboard().getDisplayObjective(DisplaySlot.SIDEBAR);
         if (var3 == null) {
            return -1;
         }

         Matcher var2 = А_at_786f4923.matcher(var3.getDisplayName().getString());
         return var2.find() ? Integer.parseInt(var2.group(1)) : -1;
      } else {
         return -1;
      }
   }

   public static int ц() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gui.hud != null) {
         Map<?, LerpingBossEvent> var0 = ((BossHealthOverlayAccessor)MINECRAFT_CLIENT.gui.hud.getBossOverlay()).getBossBars();
         if (var0 != null && !var0.isEmpty()) {
            for (LerpingBossEvent var2 : var0.values()) {
               String var3 = var2.getName().getString().toLowerCase(Locale.ROOT);
               if (var3.contains(ВуВ) || var3.contains(ВуЯ) || var3.contains(ВуХ)) {
                  Matcher var4 = field_4.matcher(var3);
                  if (var4.find()) {
                     return Integer.parseInt(var4.group(1)) * 60 + Integer.parseInt(var4.group(2));
                  }

                  Matcher var5 = п.matcher(var3);
                  return var5.find() ? Integer.parseInt(var5.group(1)) : 0;
               }
            }

            return -1;
         } else {
            return -1;
         }
      } else {
         return -1;
      }
   }

   private ServerStatusParser() {
   }
}
