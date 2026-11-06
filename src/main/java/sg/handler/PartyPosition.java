package sg.handler;

import com.google.gson.Gson;
import java.util.UUID;
import java.util.regex.Pattern;

public record PartyPosition(int version, String member, UUID uuid, String name, String server, String dimension, double x, double y, double z, boolean active) {
   private static final Gson GSON = new Gson();
   private static final Pattern PLAYER_NAME = Pattern.compile("[A-Za-z0-9_]{3,16}");

   boolean valid() {
      return this.version == 1
         && this.member != null
         && !this.member.isBlank()
         && this.member.length() <= 128
         && this.uuid != null
         && this.name != null
         && PLAYER_NAME.matcher(this.name).matches()
         && this.server != null
         && !this.server.isBlank()
         && this.server.length() <= 255
         && this.dimension != null
         && !this.dimension.isBlank()
         && this.dimension.length() <= 128
         && Double.isFinite(this.x)
         && Double.isFinite(this.y)
         && Double.isFinite(this.z)
         && Math.abs(this.x) <= 3.0E7
         && Math.abs(this.z) <= 3.0E7
         && Math.abs(this.y) <= 3.0E7;
   }

   public static PartyPosition decode(String var0) {
      if (var0 != null && var0.length() <= 2048) {
         try {
            PartyPosition var1 = GSON.fromJson(var0, PartyPosition.class);
            return var1 != null && var1.valid() ? var1 : null;
         } catch (RuntimeException var2) {
            return null;
         }
      } else {
         return null;
      }
   }

   public String encode() {
      return GSON.toJson(this);
   }
}
