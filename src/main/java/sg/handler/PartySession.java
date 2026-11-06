package sg.handler;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Consumer;
import sg.network.IrcRoles;

public final class PartySession {
   private static final long сИ = 200000000L;
   private static final long сь = 1000000000L;
   public static final long сж = 30000000000L;
   private static final long с弟 = 3000000000L;
   private static final long сб = 300000000L;
   public static final int Вж = 32;
   private final String с4;
   private final Consumer<PartyPresenceMessage> Д;
   private final Map<String, PartySession.PeerSession> л = new HashMap<>();
   private final Map<UUID, Long> э = new HashMap<>();
   private final Map<UUID, PartySession.SmoothedPosition> ю = new HashMap<>();
   private final Map<UUID, PartySession.SmoothedPosition> Щ = Collections.unmodifiableMap(this.ю);
   private final Map<String, PartySession.TrackedMarker> у = new HashMap<>();
   @SerializedName("2")
   private final Map<String, PartySession.TrackedMarker> field_2 = Collections.unmodifiableMap(this.у);
   @SerializedName("В")
   private Marker В_1160dcfe;
   private boolean сН;
   @SerializedName("В")
   private final LinkedHashSet<UUID> В_4f458157 = new LinkedHashSet<>();
   @SerializedName("Я")
   private final LinkedHashMap<UUID, PartySession.ReceivedChat> Я_d5b8fa95 = new LinkedHashMap<>();
   private long сЫ;
   private long сl;
   private Set<String> 诶 = Set.of();
   private String сп = "";
   @SerializedName("е")
   private final Queue<String> е_d8882623 = new ArrayDeque<>();
   @SerializedName("е")
   private UUID е_55adc84b;
   private long с8;
   private long с5;
   @SerializedName("В")
   private PartyPosition В_c65a3f47;
   @SerializedName("Я")
   private PartyPosition Я_c65a3f47;
   @SerializedName("В")
   private PartyCosmeticsPayload В_3b05c6e2;
   @SerializedName("Я")
   private PartyCosmeticsPayload Я_3b05c6e2;
   private boolean сн;
   private long св;
   private long съ;
   private static final String ВуЪ = "JOIN_REQUEST";
   private static final String Вут = "party.";
   private static final String Вуа = "NOTICE_APPLY";
   private static final String ВуЮ = "party.created";
   private static final String ВуЧ = "party.joined";
   private static final String ВуЛ = "party.leaved";
   private static final String Ву诶 = "party.disband";
   private static final String Ву1 = "party.disbanded";
   private static final String ВуЬ = "party.deleted";
   private static final String Ву衣 = "party.null";
   private static final String Вуш = "party.kicked";
   private static final String ВуИ = "party.exist";
   private static final String Вуь = "party.not.found";
   private static final String Вуж = "party.password.incorrect";
   private static final String Ву弟 = "party.already";
   private static final String Вуб = "JOIN_IGNORED";
   private static final String ВуЫ = "PARTY_CONFIRMED";
   private static final String Вуl = ":";
   private static final String Ву8 = "DIRECTORY_APPLY";
   private static final String Ву5 = "PARTY_REMOVED";
   private static final String Вув = "MEMBERS_CLEAR";
   private static final long Вуъ = 3000000000L;
   private static final long ВуМ = 300000000L;
   private static final long ВуН = 1000000L;
   private static final String Вун = "MARKER_PLACE";
   private static final long ВуЦ = 30000000000L;
   private static final long Вуч = 30000000000L;
   private static final String ВуЭ = "LOCAL_CONTEXT";
   private static final long Ву必 = 200000000L;
   private static final long Вущ = 1000000000L;
   private static final String ВуГ = "no-confirmed-local-party";
   private static final String Вул = "null-presence";
   private static final String Вуэ = "invalid-presence";
   private static final String Вую = "party-mismatch";
   private static final String ВуЩ = "self-irc-name";
   private static final String Вуу = "retired-peer-session";
   private static final long Ву2 = 2000L;
   private static final String Вуф = "PRESENCE_DROP";
   private static final String Вуи = "null";
   private static final String Ву9 = "session-receive";
   private static final String Вуо = "PRESENCE_APPLY";
   private static final String ВуO = "PRESENCE_DROP";
   private static final String Ву7 = "MEMBER_REMOVE";
   private static final String ВуР = "MEMBER_INSERT";
   private static final String Ву3 = "COORDINATE_DROP";
   private static final String Вур = "COORDINATE_DROP";
   private static final String Ву6 = "COORDINATE_INSERT";
   private static final String Ву_ = "COORDINATE_UPDATE";
   private static final long ВуШ = 1000000L;
   private static final String Вуы = "MARKER_RECEIVE";
   private static final long Вуй = 180000L;
   private static final long ВуЖ = 1000000L;
   private static final String Вум = "session-send";
   private static final String ВуК = "PRESENCE_SEND";
   private static final long ВуI = 30000000000L;
   private static final String ВуТ = "MEMBER_REMOVE";
   private static final long ВуО = 1000000L;
   private static final long ВуФ = 30000000000L;

   private static boolean З(PartyPosition var0, PartyPosition var1) {
      return var0 == var1
         || var0 != null && var1 != null && var0.uuid().equals(var1.uuid()) && var0.x() == var1.x() && var0.y() == var1.y() && var0.z() == var1.z();
   }

   public Marker З() {
      return this.В_1160dcfe;
   }

   public void Ж(long var1) {
      if (this.В_1160dcfe != null) {
         this.В_1160dcfe = null;
         this.сЫ = 0L;
         this.у.remove(й(this.с4));
         if (this.Зй()) {
            this.З(PartyPresenceMessage.Kind.UPDATE, var1);
         }
      }
   }

   public List<PartyChatMessage> З(long var1) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = this.Я_d5b8fa95.values().iterator();

      while (var4.hasNext()) {
         PartySession.ReceivedChat var5 = (PartySession.ReceivedChat)var4.next();
         if (var1 - var5.received >= Вуъ) {
            var4.remove();
         } else if (this.Зй() && this.Ы(var5.chat)) {
            var4.remove();
            if (this.Ц(var5.chat)) {
               var3.add(var5.chat);
            }
         }
      }

      return var3;
   }

   public void р(String var1) {
      HashSet var2 = new HashSet();
      if (var1 != null) {
         for (String var6 : var1.split(Вуl)) {
            if (!var6.isBlank()) {
               var2.add(й(var6));
            }
         }
      }

      this.诶 = var2;
      PartyTrace.Ы(Ву8, "entries=" + var2 + " " + this.М2());
      if (this.Зй()) {
         if (var2.contains(й(this.сп))) {
            this.сн = true;
         } else if (this.сн) {
            PartyTrace.Ы(Ву5, "reason=confirmed-party-missing-from-directory party=" + this.сп);
            this.ЗШ();
         }
      }
   }

   public Map<UUID, PartySession.SmoothedPosition> ю() {
      return this.Щ;
   }

   public boolean Ы(Marker var1) {
      return this.В_c65a3f47 != null && this.В_c65a3f47.active() && var1 != null && this.В_c65a3f47.dimension().equals(var1.dimension());
   }

   public void З(UUID var1, double var2, double var4, double var6) {
      if (this.В_1160dcfe != null && var1 != null && var1.equals(this.В_1160dcfe.entityUuid())) {
         if (this.В_1160dcfe.x() != var2 || this.В_1160dcfe.y() != var4 || this.В_1160dcfe.z() != var6) {
            Marker var8 = this.В_1160dcfe.position(var2, var4, var6);
            if (var8.valid()) {
               this.В_1160dcfe = var8;
               this.сН = true;
               PartySession.TrackedMarker var9 = this.у.get(й(this.с4));
               if (var9 != null && var9.Я.id().equals(var8.id())) {
                  var9.Я = var8;
               }
            }
         }
      }
   }

   private void З(PartyPresenceMessage.Kind var1, long var2) {
      this.сН = false;
      this.с5 = var2;
      this.Я_c65a3f47 = this.В_c65a3f47;
      this.Я_3b05c6e2 = this.В_3b05c6e2;
      Marker var4 = this.В_1160dcfe != null && var1 != PartyPresenceMessage.Kind.LEAVE && var2 < this.сЫ
         ? this.В_1160dcfe.remaining((int)Math.min(Вуй, Math.max(1L, (this.сЫ - var2) / ВуЖ)))
         : null;
      PartyPresenceMessage var5 = new PartyPresenceMessage(
         2, this.сп, this.с4, this.е_55adc84b, ++this.с8, var1, this.В_c65a3f47, var4, var1 == PartyPresenceMessage.Kind.LEAVE ? null : this.В_3b05c6e2
      );
      if (PartyTrace.Р(Вум, var5)) {
         PartyTrace.Ы(ВуК, PartyTrace.Р(var5));
      }

      this.Д.accept(var5);
   }

   public PartySession(String var1, Consumer<PartyPresenceMessage> var2) {
      this.с4 = var1;
      this.Д = var2;
   }

   public void ЗЕ() {
      this.е_d8882623.clear();
      this.诶 = Set.of();
      this.В_c65a3f47 = null;
      this.ЗШ();
   }

   public boolean З(String var1, UUID var2, UUID var3) {
      if (this.Зй() && var1 != null && var2 != null && var3 != null) {
         PartySession.PeerSession var4 = this.л.get(й(var1));
         return var4 != null && var3.equals(var4.Ъ) && var2.equals(var4.т);
      } else {
         return false;
      }
   }

   public PartyChatMessage З(String var1) {
      if (!this.Зй()) {
         return null;
      }

      PartyChatMessage var2 = new PartyChatMessage(UUID.randomUUID(), this.сп, this.с4, this.е_55adc84b, IrcRoles.Яп(), var1);
      return var2.valid() ? var2 : null;
   }

   public UUID Ы() {
      return this.е_55adc84b;
   }

   public boolean З(String var1, double var2, double var4, double var6, int var8, long var9) {
      return this.З(var1, var2, var4, var6, var8, null, null, null, var9);
   }

   public void ю(long var1) {
      if (this.Зй()) {
         this.З(PartyPresenceMessage.Kind.LEAVE, var1);
      }

      this.е_d8882623.clear();
      this.ЗШ();
   }

   public void З(PartyPosition var1, PartyCosmeticsPayload var2, long var3) {
      boolean var5 = this.В_c65a3f47 == null
         ? var1 != null
         : var1 == null
            || !this.В_c65a3f47.uuid().equals(var1.uuid())
            || !this.В_c65a3f47.server().equals(var1.server())
            || !this.В_c65a3f47.dimension().equals(var1.dimension());
      this.В_c65a3f47 = var1;
      this.В_3b05c6e2 = var2;
      if (var5) {
         PartyTrace.Ы(ВуЭ, PartyTrace.Р(this.В_c65a3f47));
      }

      if (this.Зй()) {
         this.э.values().removeIf(var2x -> var3 - var2x >= ВуФ);
         this.л.entrySet().removeIf(var3x -> {
            PartySession.PeerSession var4 = var3x.getValue();
            if (var3 - var4.Яч < ВуI) {
               return false;
            }

            PartyTrace.Ы(ВуТ, "reason=heartbeat-timeout irc=" + var3x.getKey() + " uuid=" + var4.т + " ageMs=" + (var3 - var4.Яч) / ВуО);
            if (var4.т != null) {
               this.ю.remove(var4.т);
            }

            return true;
         });
         this.у.values().removeIf(var2x -> var3 >= var2x.Х必);
         boolean var6 = this.В_1160dcfe != null && var3 >= this.сЫ;
         if (var6) {
            this.В_1160dcfe = null;
         }

         if (var5) {
            this.у.remove(й(this.с4));
            this.В_1160dcfe = null;
            this.сЫ = 0L;
            this.З(PartyPresenceMessage.Kind.HELLO, var3);
         } else if (var6
            || var3 - this.с5 >= Ву必
               && (this.сН || var3 - this.с5 >= Вущ || !З(this.В_c65a3f47, this.Я_c65a3f47) || !Objects.equals(this.В_3b05c6e2, this.Я_3b05c6e2))) {
            this.З(PartyPresenceMessage.Kind.UPDATE, var3);
         }
      }
   }

   public List<String> Ы_2571477d(long var1) {
      if (!this.Зй()) {
         return List.of();
      }

      TreeSet var3 = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

      for (PartySession.PeerSession var5 : this.л.values()) {
         if (var5.т != null && var5.В9 != null && !var5.В9.isBlank() && var1 >= var5.Яч && var1 - var5.Яч < ВуЦ) {
            var3.add(var5.В9);
         }
      }

      return List.copyOf(var3);
   }

   public String М2() {
      return "active="
         + this.Зй()
         + " party="
         + this.сп
         + " selfIrc="
         + this.с4
         + " pending="
         + this.е_d8882623
         + " members="
         + this.л.size()
         + " memberAccounts="
         + this.л.keySet()
         + " coordinates="
         + this.ю.size()
         + " received="
         + this.св
         + " accepted="
         + this.съ
         + " txSequence="
         + this.с8;
   }

   private boolean Ы(PartyChatMessage var1) {
      if (this.с4.equalsIgnoreCase(var1.member())) {
         return this.е_55adc84b.equals(var1.session());
      }

      PartySession.PeerSession var2 = this.л.get(й(var1.member()));
      return var2 != null && var2.Ъ.equals(var1.session());
   }

   public Map<String, PartySession.TrackedMarker> method_5() {
      return this.field_2;
   }

   private static String й(String var0) {
      return var0.trim().toLowerCase(Locale.ROOT);
   }

   public void З_de5a6f78(String var1, long var2) {
      if (var1.startsWith(Вут)) {
         PartyTrace.Ы(Вуа, "key=" + var1 + " " + this.М2());
      }

      switch (var1) {
         case ВуЮ:
         case ВуЧ:
            String var6 = this.е_d8882623.poll();
            if (var6 == null) {
               PartyTrace.Ы(Вуб, "reason=no-pending-request key=" + var1);
               return;
            }

            this.ЗШ();
            this.сп = var6;
            this.е_55adc84b = UUID.randomUUID();
            this.сн = this.诶.contains(й(this.сп));
            PartyTrace.Ы(ВуЫ, this.М2() + " awaitingPeerPresence=true");
            this.З(PartyPresenceMessage.Kind.HELLO, var2);
            break;
         case ВуЛ:
         case Ву诶:
         case Ву1:
         case ВуЬ:
         case Ву衣:
         case Вуш:
            this.ЗШ();
            break;
         case ВуИ:
         case Вуь:
         case Вуж:
         case Ву弟:
            this.е_d8882623.poll();
      }
   }

   public String Мж() {
      return this.сп;
   }

   public long Ы_6da43b94(long var1) {
      return this.сl == 0L ? 0L : Math.max(0L, ВуМ - (var1 - this.сl));
   }

   public boolean Ы(PartyPosition var1) {
      return this.В_c65a3f47 != null && this.В_c65a3f47.active() && var1 != null && var1.active() && this.В_c65a3f47.dimension().equals(var1.dimension());
   }

   public String Ы_388008bb(UUID var1, String var2) {
      if (!this.Зй()) {
         return null;
      }

      for (PartySession.PeerSession var4 : this.л.values()) {
         if (var1 != null && var1.equals(var4.т)) {
            return var4.Во;
         }
      }

      if (var2 != null) {
         for (PartySession.PeerSession var6 : this.л.values()) {
            if (var2.equalsIgnoreCase(var6.В9)) {
               return var6.Во;
            }
         }
      }

      return null;
   }

   private void ЗШ() {
      PartyTrace.Ы(Вув, this.М2());
      this.сп = "";
      this.е_55adc84b = null;
      this.с8 = 0L;
      this.с5 = 0L;
      this.Я_c65a3f47 = null;
      this.В_3b05c6e2 = null;
      this.Я_3b05c6e2 = null;
      this.сн = false;
      this.л.clear();
      this.э.clear();
      this.ю.clear();
      this.у.clear();
      this.В_4f458157.clear();
      this.Я_d5b8fa95.clear();
      this.В_1160dcfe = null;
      this.сЫ = 0L;
      this.сl = 0L;
   }

   public boolean З(PartyChatMessage var1, long var2) {
      if (!this.Зй() || var1 == null || !var1.valid() || !this.сп.equalsIgnoreCase(var1.party()) || this.В_4f458157.contains(var1.id())) {
         return false;
      }

      if (!this.Ы(var1)) {
         if (this.Я_d5b8fa95.size() < 128) {
            this.Я_d5b8fa95.putIfAbsent(var1.id(), new PartySession.ReceivedChat(var1, var2));
         }

         return false;
      } else {
         this.Я_d5b8fa95.remove(var1.id());
         return this.Ц(var1);
      }
   }

   public PartyPosition З_c65a3f47(String var1, long var2) {
      if (!this.Ч(var1)) {
         return null;
      }

      for (PartySession.SmoothedPosition var5 : this.ю.values()) {
         PartyPosition var6 = var5.Х;
         if (var1.equalsIgnoreCase(var6.name())
            && var6.active()
            && var6.valid()
            && var2 >= var5.Я衣
            && var2 - var5.Я衣 < Вуч
            && this.Ы(var6)
            && this.Ы_bbeebd87(var6.uuid(), var6.name())) {
            return var6;
         }
      }

      return null;
   }

   public PartyCosmeticsPayload Ы_3b05c6e2(UUID var1, String var2) {
      if (!this.Зй()) {
         return null;
      }

      for (PartySession.PeerSession var4 : this.л.values()) {
         if (var1 != null && var1.equals(var4.т) || var2 != null && var2.equalsIgnoreCase(var4.В9)) {
            return var4.Х;
         }
      }

      return null;
   }

   public boolean Зй() {
      return this.е_55adc84b != null;
   }

   public boolean method_6(String var1) {
      if (this.е_d8882623.isEmpty() && PartyOperationState.Щ(var1)) {
         this.е_d8882623.add(var1);
         PartyTrace.Ы(ВуЪ, "requestedParty=" + var1 + " " + this.М2());
         return true;
      } else {
         return false;
      }
   }

   public boolean З(String var1, double var2, double var4, double var6, int var8, UUID var9, String var10, String var11, long var12) {
      if (this.Зй() && this.В_c65a3f47 != null && this.В_c65a3f47.active() && this.Ы_6da43b94(var12) <= 0L) {
         Marker var14 = new Marker(UUID.randomUUID(), var1, this.В_c65a3f47.server(), this.В_c65a3f47.dimension(), var2, var4, var6, var8, var9, var10, var11);
         String var15 = й(this.с4);
         if (var14.valid() && (this.у.containsKey(var15) || this.у.size() < 32)) {
            this.В_1160dcfe = var14;
            this.сЫ = var12 + var8 * ВуН;
            this.сl = var12;
            this.у.put(var15, new PartySession.TrackedMarker(this.с4, this.В_c65a3f47.name(), var14, var12));
            PartyTrace.Ы(Вун, "irc=" + this.с4 + " id=" + var14.id() + " name=" + var14.name());
            this.З(PartyPresenceMessage.Kind.UPDATE, var12);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void З_() {
      this.е_d8882623.clear();
   }

   public boolean З(PartyChatMessage var1) {
      return this.З(var1, System.nanoTime());
   }

   public void З(PartyPresenceMessage var1, long var2) {
      this.св++;
      String var4 = !this.Зй()
         ? ВуГ
         : (
            var1 == null
               ? Вул
               : (
                  !var1.valid()
                     ? Вуэ
                     : (
                        !this.сп.equalsIgnoreCase(var1.party())
                           ? Вую
                           : (this.с4.equalsIgnoreCase(var1.member()) ? ВуЩ : (this.э.containsKey(var1.session()) ? Вуу : null))
                     )
               )
         );
      if (var4 != null) {
         if (PartyTrace.Р("session-drop:" + var4, Ву2)) {
            PartyTrace.Ы(Вуф, "reason=" + var4 + " " + this.М2() + " remote=" + (var1 == null ? Вуи : PartyTrace.Р(var1)));
         }
      } else {
         boolean var5 = PartyTrace.Р(Ву9, var1);
         if (var5) {
            PartyTrace.Ы(Вуо, PartyTrace.Р(var1));
         }

         String var6 = й(var1.member());
         PartySession.PeerSession var7 = this.л.get(var6);
         if (var7 != null && var7.Ъ.equals(var1.session()) && var1.sequence() <= var7.ЯЦ) {
            if (var5) {
               PartyTrace.Ы(ВуO, "reason=old-sequence last=" + var7.ЯЦ + " " + PartyTrace.Р(var1));
            }
         } else {
            this.съ++;
            if (var1.kind() == PartyPresenceMessage.Kind.LEAVE) {
               this.э.put(var1.session(), var2);
               if (var7 == null || var7.Ъ.equals(var1.session())) {
                  this.у.remove(var6);
               }

               if (var7 != null && var7.Ъ.equals(var1.session())) {
                  if (var7.т != null) {
                     this.ю.remove(var7.т);
                  }

                  this.л.remove(var6);
                  this.у.remove(var6);
                  PartyTrace.Ы(Ву7, "reason=leave irc=" + var6 + " uuid=" + var7.т);
               }
            } else {
               boolean var8 = var7 == null || !var7.Ъ.equals(var1.session());
               if (var8) {
                  if (var7 != null) {
                     this.э.put(var7.Ъ, var2);
                     if (var7.т != null) {
                        this.ю.remove(var7.т);
                     }

                     this.у.remove(var6);
                  }

                  var7 = new PartySession.PeerSession(var1.session());
                  this.л.put(var6, var7);
                  PartyTrace.Ы(ВуР, "irc=" + var6 + " session=" + var7.Ъ + " members=" + this.л.size());
               }

               var7.ЯЦ = var1.sequence();
               var7.Яч = var2;
               var7.Во = var1.member();
               var7.Х = var1.cosmetics();
               PartyPosition var9 = var1.position();
               if (var9 != null && var9.active()) {
                  if (var7.т != null && !var7.т.equals(var9.uuid())) {
                     this.ю.remove(var7.т);
                  }

                  var7.т = var9.uuid();
                  var7.В9 = var9.name();
                  if (this.В_c65a3f47 != null && this.В_c65a3f47.uuid().equals(var9.uuid())) {
                     this.ю.remove(var9.uuid());
                     if (var5) {
                        PartyTrace.Ы(Вур, "reason=self-minecraft-uuid irc=" + var6 + " local=" + PartyTrace.Р(this.В_c65a3f47) + " remote=" + PartyTrace.Р(var9));
                     }
                  } else {
                     PartySession.SmoothedPosition var10 = this.ю.get(var9.uuid());
                     if (var10 == null) {
                        this.ю.put(var9.uuid(), new PartySession.SmoothedPosition(var9, var2));
                     } else {
                        var10.O(var9, var2);
                     }

                     if (var10 == null || var5) {
                        PartyTrace.Ы(
                           var10 == null ? Ву6 : Ву_, "irc=" + var6 + " seq=" + var1.sequence() + " coordinates=" + this.ю.size() + " " + PartyTrace.Р(var9)
                        );
                     }
                  }
               } else {
                  if (var7.т != null) {
                     this.ю.remove(var7.т);
                  }

                  var7.т = null;
                  var7.В9 = null;
                  if (var5) {
                     PartyTrace.Ы(Ву3, "reason=missing-or-inactive-position irc=" + var6);
                  }
               }

               Marker var12 = var1.marker();
               if (var12 == null) {
                  this.у.remove(var6);
               } else {
                  PartySession.TrackedMarker var11 = this.у.get(var6);
                  if (var11 != null && var11.Я.id().equals(var12.id())) {
                     var11.Х必 = Math.min(var11.Х必, var2 + var12.remainingMillis() * ВуШ);
                     if (var11.Я.entityUuid() != null
                        && var11.Я.entityUuid().equals(var12.entityUuid())
                        && var11.Я.entityType().equals(var12.entityType())
                        && var11.Я.dimension().equals(var12.dimension())) {
                        var11.Я = var12;
                     }
                  } else if (var11 != null || this.у.size() < 32) {
                     this.у.put(var6, new PartySession.TrackedMarker(var1.member(), var9.name(), var12, var2));
                     PartyTrace.Ы(Вуы, "irc=" + var6 + " id=" + var12.id() + " name=" + var12.name());
                  }
               }

               if (var1.kind() == PartyPresenceMessage.Kind.HELLO || var8) {
                  this.З(PartyPresenceMessage.Kind.UPDATE, var2);
               }
            }
         }
      }
   }

   public boolean Ы_bbeebd87(UUID var1, String var2) {
      for (PartySession.PeerSession var4 : this.л.values()) {
         if (var1.equals(var4.т) || var2.equalsIgnoreCase(var4.В9)) {
            return true;
         }
      }

      return false;
   }

   public boolean Ч(String var1) {
      if (this.Зй() && var1 != null) {
         for (PartySession.PeerSession var3 : this.л.values()) {
            if (var1.equalsIgnoreCase(var3.В9)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void З(PartyPosition var1, long var2) {
      this.З(var1, null, var2);
   }

   private boolean Ц(PartyChatMessage var1) {
      if (!this.В_4f458157.add(var1.id())) {
         return false;
      }

      if (this.В_4f458157.size() > 128) {
         this.В_4f458157.removeFirst();
      }

      return true;
   }

   public static final class SmoothedPosition {
      private PartyPosition Х;
      private double Вф;
      private double Ви;
      private long Я衣;
      private static final double ЮТ = 4096.0;
      private static final double ЮО = 2.0E8;

      public PartyPosition Д() {
         return this.Х;
      }

      private SmoothedPosition(PartyPosition var1, long var2) {
         this.Х = var1;
         this.Вф = var1.x();
         this.Ви = var1.z();
         this.Я衣 = var2;
      }

      private double Ъ(long var1) {
         return Math.clamp((var1 - this.Я衣) / ЮО, 0.0, 1.0);
      }

      public double O(long var1) {
         return this.Вф + (this.Х.x() - this.Вф) * this.Ъ(var1);
      }

      public double method__(long var1) {
         return this.Ви + (this.Х.z() - this.Ви) * this.Ъ(var1);
      }

      private void O(PartyPosition var1, long var2) {
         this.Вф = this.O(var2);
         this.Ви = this.method__(var2);
         double var4 = var1.x() - this.Х.x();
         double var6 = var1.z() - this.Х.z();
         if (!this.Х.server().equals(var1.server()) || !this.Х.dimension().equals(var1.dimension()) || var4 * var4 + var6 * var6 > ЮТ) {
            this.Вф = var1.x();
            this.Ви = var1.z();
         }

         this.Х = var1;
         this.Я衣 = var2;
      }
   }

   private record ReceivedChat(PartyChatMessage chat, long received) {
   }

   private static final class PeerSession {
      private final UUID Ъ;
      private long ЯЦ;
      private long Яч;
      private UUID т;
      private String В9;
      private String Во;
      private PartyCosmeticsPayload Х;

      private PeerSession(UUID var1) {
         this.Ъ = var1;
      }
   }

   public static final class TrackedMarker {
      private final String Я8;
      private final String Я5;
      private Marker Я;
      private final long ХЭ;
      private long Х必;
      private static final long field_9з = 1000000L;

      public long И() {
         return this.ХЭ;
      }

      private TrackedMarker(String var1, String var2, Marker var3, long var4) {
         this.Я8 = var1;
         this.Я5 = var2;
         this.Я = var3;
         this.ХЭ = var4;
         this.Х必 = var4 + var3.remainingMillis() * field_9з;
      }

      public long Ы() {
         return this.Х必;
      }

      public String Ц弟() {
         return this.Я8;
      }

      public Marker Ц() {
         return this.Я;
      }

      public String Ц5() {
         return this.Я5;
      }
   }
}
