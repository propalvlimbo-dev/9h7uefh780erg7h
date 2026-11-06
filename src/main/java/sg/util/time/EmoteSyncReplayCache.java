package sg.util.time;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class EmoteSyncReplayCache {
   static final long Вр = 3500000000L;
   private final Map<UUID, EmoteSyncReplayCache.ReceivedMessage> сИ = new HashMap<>();
   private final Map<UUID, Long> сь = new HashMap<>();
   private static final long В7х = 30000000000L;
   private static final long В7У = 3500000000L;
   private static final long В7З = 30000000000L;

   boolean Ц(UUID var1, EmoteSyncMessage var2, long var3) {
      if (var2.valid() && !this.сь.containsKey(var2.session())) {
         EmoteSyncReplayCache.ReceivedMessage var5 = this.сИ.get(var1);
         if (var5 != null) {
            if (var5.message.session().equals(var2.session())) {
               if (var2.sequence() <= var5.message.sequence()) {
                  return false;
               }
            } else {
               if (this.сь.size() >= 512) {
                  return false;
               }

               this.сь.put(var5.message.session(), var3 + В7х);
            }
         } else if (this.сИ.size() >= 256) {
            return false;
         }

         this.сИ.put(var1, new EmoteSyncReplayCache.ReceivedMessage(var2, var3));
         return true;
      } else {
         return false;
      }
   }

   EmoteSyncMessage Ц(UUID var1, long var2) {
      EmoteSyncReplayCache.ReceivedMessage var4 = this.сИ.get(var1);
      return var4 != null && var2 - var4.received < В7У && var4.message.state() != EmoteSyncMessage.State.CLEAR ? var4.message : null;
   }

   void ф9() {
      this.сИ.clear();
      this.сь.clear();
   }

   void Л(long var1) {
      this.сИ.values().removeIf(var2 -> var1 - var2.received >= В7З);
      this.сь.values().removeIf(var2 -> var1 >= var2);
   }

   private record ReceivedMessage(EmoteSyncMessage message, long received) {
   }
}
