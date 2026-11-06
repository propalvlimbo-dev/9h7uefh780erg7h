package sg.player.manager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class VoicechatPlayerDirectory {
   private static Method Ъ;
   private static Method т;
   private static Method а;
   private static Method Ю;
   private static boolean ей;
   private static final String ВкO = "de.maxhenkel.voicechat.voice.client.ClientManager";
   private static final String Вк7 = "de.maxhenkel.voicechat.voice.common.PlayerState";
   private static final String ВкР = "getPlayerStateManager";
   private static final String Вк3 = "getPlayerStates";
   private static final String Вкр = "getUuid";
   private static final String Вк6 = "getName";

   public static List<VoicechatPlayerDirectory.PlayerEntry> хч() {
      if (!чш()) {
         return List.of();
      }

      try {
         Object var0 = Ъ.invoke(null);
         if (var0 == null) {
            return List.of();
         }

         if (т.invoke(var0, false) instanceof Collection var2) {
            ArrayList var3 = new ArrayList(var2.size());

            for (Object var5 : var2) {
               UUID var6 = (UUID)а.invoke(var5);
               String var7 = (String)Ю.invoke(var5);
               if (var6 != null && var7 != null && !var7.isBlank()) {
                  var3.add(new VoicechatPlayerDirectory.PlayerEntry(var6, var7));
               }
            }

            return var3;
         } else {
            return List.of();
         }
      } catch (ReflectiveOperationException | LinkageError var8) {
         return List.of();
      }
   }

   private VoicechatPlayerDirectory() {
   }

   private static boolean чш() {
      if (ей) {
         return Ъ != null;
      }

      ей = true;

      try {
         Class var0 = Class.forName(ВкO);
         Class var1 = Class.forName(Вк7);
         Ъ = var0.getMethod(ВкР);
         т = Ъ.getReturnType().getMethod(Вк3, boolean.class);
         а = var1.getMethod(Вкр);
         Ю = var1.getMethod(Вк6);
         return true;
      } catch (ReflectiveOperationException | LinkageError var2) {
         Ъ = null;
         return false;
      }
   }

   public record PlayerEntry(UUID uuid, String name) {
   }
}
