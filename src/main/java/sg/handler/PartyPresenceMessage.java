package sg.handler;

import com.google.gson.Gson;
import java.util.UUID;

public record PartyPresenceMessage(
   int version,
   String party,
   String member,
   UUID session,
   long sequence,
   PartyPresenceMessage.Kind kind,
   PartyPosition position,
   Marker marker,
   PartyCosmeticsPayload cosmetics
) {
   private static final Gson GSON = new Gson();

   public static PartyPresenceMessage decode(String var0) {
      if (var0 != null && var0.length() <= 4096) {
         try {
            PartyPresenceMessage var1 = GSON.fromJson(var0, PartyPresenceMessage.class);
            if (var1 != null && var1.valid()) {
               return var1;
            }

            if (PartyTrace.Р("decode-invalid", 5000L)) {
               PartyTrace.Ы("DECODE_DROP", "reason=invalid-fields " + (var1 == null ? "presence=null" : "version=" + var1.version() + " " + PartyTrace.Р(var1)));
            }

            return null;
         } catch (RuntimeException var2) {
            if (PartyTrace.Р("decode-json", 5000L)) {
               PartyTrace.Ы(
                  "DECODE_DROP",
                  "reason="
                     + var2.getClass().getSimpleName()
                     + " payloadLength="
                     + var0.length()
                     + " startsWithJsonObject="
                     + var0.stripLeading().startsWith("{")
               );
            }

            return null;
         }
      } else {
         if (PartyTrace.Р("decode-size", 5000L)) {
            PartyTrace.Ы("DECODE_DROP", "reason=null-or-oversized-payload");
         }

         return null;
      }
   }

   public PartyPresenceMessage(int var1, String var2, String var3, UUID var4, long var5, PartyPresenceMessage.Kind var7, PartyPosition var8) {
      this(var1, var2, var3, var4, var5, var7, var8, null, null);
   }

   public String encode() {
      return GSON.toJson(this);
   }

   boolean valid() {
      return this.version == 2
         && this.party != null
         && !this.party.isBlank()
         && this.party.length() <= 128
         && this.member != null
         && !this.member.isBlank()
         && this.member.length() <= 128
         && this.session != null
         && this.sequence > 0L
         && this.kind != null
         && (this.position == null || this.position.valid() && this.member.equalsIgnoreCase(this.position.member()))
         && (this.cosmetics == null || this.kind != PartyPresenceMessage.Kind.LEAVE && this.cosmetics.valid())
         && (
            this.marker == null
               || this.kind != PartyPresenceMessage.Kind.LEAVE
                  && this.marker.valid()
                  && this.position != null
                  && this.position.active()
                  && this.marker.server().equals(this.position.server())
                  && this.marker.dimension().equals(this.position.dimension())
         );
   }

   public PartyPresenceMessage(int var1, String var2, String var3, UUID var4, long var5, PartyPresenceMessage.Kind var7, PartyPosition var8, Marker var9) {
      this(var1, var2, var3, var4, var5, var7, var8, var9, null);
   }

   public enum Kind {
      HELLO,
      UPDATE,
      LEAVE;
   }
}
