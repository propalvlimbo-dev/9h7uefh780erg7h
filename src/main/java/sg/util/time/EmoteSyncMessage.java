package sg.util.time;

import com.google.gson.Gson;
import java.util.UUID;

public record EmoteSyncMessage(
   int version,
   UUID session,
   long sequence,
   UUID id,
   UUID player,
   String name,
   String member,
   String server,
   String dimension,
   String party,
   UUID partySession,
   EmoteSyncMessage.Scope scope,
   String emote,
   String variant,
   EmoteSyncMessage.State state,
   long elapsedMillis,
   long stopAgeMillis,
   long sentAtMillis
) {
   private static final Gson GSON = new Gson();

   public boolean valid() {
      return this.version == 1
         && this.session != null
         && this.sequence > 0L
         && this.id != null
         && this.player != null
         && text(this.name, 16)
         && this.name.matches("[A-Za-z0-9_]{1,16}")
         && this.scope != null
         && safe(this.member, 128)
         && (this.scope != EmoteSyncMessage.Scope.PARTY || text(this.member, 128))
         && safe(this.server, 255)
         && text(this.dimension, 128)
         && this.party != null
         && this.party.length() <= 128
         && (this.party.isEmpty() ? this.partySession == null : text(this.party, 128) && this.partySession != null)
         && (this.scope != EmoteSyncMessage.Scope.PARTY || !this.party.isEmpty())
         && text(this.emote, 64)
         && text(this.variant, 64)
         && this.state != null
         && this.elapsedMillis >= 0L
         && this.elapsedMillis <= 86400000L
         && this.stopAgeMillis >= 0L
         && this.stopAgeMillis <= 10000L
         && (this.state != EmoteSyncMessage.State.PLAY || this.stopAgeMillis == 0L)
         && this.sentAtMillis > 0L;
   }

   public long transitMillis(long var1) {
      long var3 = var1 - this.sentAtMillis;
      return var3 >= 0L && var3 <= 1000L ? var3 : 0L;
   }

   public boolean visibleTo(boolean var1, boolean var2) {
      return var2 || this.scope == EmoteSyncMessage.Scope.ALL && !var1;
   }

   private static boolean safe(String var0, int var1) {
      return var0 != null && var0.length() <= var1 && var0.codePoints().noneMatch(var0x -> Character.isISOControl(var0x) || var0x == 167);
   }

   private static boolean text(String var0, int var1) {
      return var0 != null && !var0.isBlank() && var0.length() <= var1 && var0.codePoints().noneMatch(var0x -> Character.isISOControl(var0x) || var0x == 167);
   }

   public String encode() {
      return GSON.toJson(this);
   }

   public static EmoteSyncMessage decode(String var0) {
      if (var0 != null && var0.length() <= 4096) {
         try {
            EmoteSyncMessage var1 = GSON.fromJson(var0, EmoteSyncMessage.class);
            return var1 != null && var1.valid() ? var1 : null;
         } catch (RuntimeException var2) {
            return null;
         }
      } else {
         return null;
      }
   }

   public enum Scope {
      ALL,
      PARTY;
   }

   public enum State {
      PLAY,
      STOP,
      CLEAR;
   }
}
