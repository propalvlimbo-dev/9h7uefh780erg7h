package sg.handler;

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PartyTrace {
   private static final Logger е = LoggerFactory.getLogger(PartyTrace.Вф3);
   private static final ConcurrentHashMap<String, Long> Я = new ConcurrentHashMap<>();
   private static final boolean Я必 = Boolean.getBoolean(PartyTrace.Вфр);
   private static final long Вф9 = 1000000L;
   private static final long Вфо = 2000L;
   private static final String ВфO = "[PartyTrace] {} {}";
   private static final String Вф7 = "null";
   private static final String ВфР = "no-world";
   private static final String Вф3 = "Rose/Party";
   private static final String Вфр = "rose.partyTrace";

   public static boolean Р(String var0, long var1) {
      if (!Я必) {
         return false;
      }

      long var3 = System.nanoTime();
      Long var5 = Я.get(var0);
      if (var5 != null && var3 - var5 < var1 * Вф9) {
         return false;
      }

      if (Я.size() >= 512) {
         Я.clear();
      }

      Я.put(var0, var3);
      return true;
   }

   public static boolean IИ() {
      return Я必;
   }

   public static String Р(PartyPosition var0) {
      return var0 == null
         ? ВфР
         : "server=" + var0.server() + " dimension=" + var0.dimension() + " player=" + var0.name() + " xyz=" + var0.x() + "," + var0.y() + "," + var0.z();
   }

   private PartyTrace() {
   }

   public static void Ы(String var0, String var1) {
      if (Я必) {
         е.info(ВфO, var0, var1 == null ? "" : var1);
      }
   }

   public static boolean Р(String var0, PartyPresenceMessage var1) {
      return var1 != null && Р(var0 + ":" + var1.member(), Вфо);
   }

   public static String Р(PartyPresenceMessage var0) {
      return var0 == null
         ? Вф7
         : "party=" + var0.party() + " member=" + var0.member() + " session=" + var0.session() + " sequence=" + var0.sequence() + " kind=" + var0.kind();
   }
}
