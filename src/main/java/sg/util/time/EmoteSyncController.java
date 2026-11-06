package sg.util.time;

import com.google.gson.annotations.SerializedName;
import io.netty.channel.Channel;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.entity.player.Player;
import sg.network.irc.IrcCustomPackets;
import sg.handler.PartyClient;
import sg.network.ClientChat;
import sg.render.PlayerEmoteController;
import sg.render.EmoteDefinition;
import sg.network.irc.RoseIrc;

public final class EmoteSyncController {
   private static final EmoteSyncController В_3214c619 = new EmoteSyncController();
   private static final Map<String, EmoteDefinition> ц = EmoteDefinition.ALL.stream().collect(Collectors.toUnmodifiableMap(EmoteDefinition::id, var0 -> (EmoteDefinition)var0));
   @SerializedName("В")
   private final EmoteSyncReplayCache В_8e53a6f4 = new EmoteSyncReplayCache();
   @SerializedName("4")
   private final UUID field_4_55adc84b = UUID.randomUUID();
   @SerializedName("е")
   private BooleanSupplier е_1dd325e2 = () -> false;
   private BooleanSupplier А = () -> false;
   @SerializedName("4")
   private BooleanSupplier field_4_1dd325e2 = () -> false;
   private boolean Вз;
   private boolean Вц;
   @SerializedName("е")
   private Channel е_04e5b147;
   @SerializedName("В")
   private EmoteSyncController.SyncContext В_24d74df8;
   @SerializedName("В")
   private EmoteSyncMessage В_80ab678b;
   private long В8;
   private long В5;
   private long Вв;
   private static final long ВЪо = 1000000000L;
   private static final String ВЪO = ":25565";
   private static final long ВЪ7 = 1000000000L;
   private static final String ВЪР = "Эмоция видна только вам: нет подтверждённой IRC-пати.";
   private static final String ВЪ3 = "Эмоция видна только вам: IRC не подключён или ещё проходит авторизацию.";
   private static final long ВЪр = 1000000L;
   private static final long ВЪ6 = 1000000L;
   private static final String ВЪ_ = "encrypt";
   private static final String ВЪШ = "decrypt";
   private static final long ВЪы = 3500L;
   private static final String ВЪй = "Не удалось отправить эмоцию в IRC; синхронизация временно недоступна.";

   public boolean жэ() {
      return this.е_1dd325e2.getAsBoolean();
   }

   public void lТ() {
      Minecraft var1 = Minecraft.getInstance();
      RoseIrc var2 = RoseIrc.З_3e6e5516();
      Channel var3 = var2 == null ? null : var2.З_d285c1ed().ч_04e5b147();
      if (var3 != null && !var3.isActive()) {
         var3 = null;
      }

      if (this.е_04e5b147 != var3) {
         this.l6();
         this.е_04e5b147 = var3;
         if (var3 != null) {
            this.б(var2, var3, var1);
         }
      }

      if (var3 != null && System.nanoTime() - this.Вв >= ВЪо) {
         this.Вв = System.nanoTime();
         this.б(var2, var3, var1);
      }

      EmoteSyncController.SyncContext var4 = this.Ы(var1, var2);
      if (!Objects.equals(this.В_24d74df8, var4)) {
         this.l6();
         this.В_24d74df8 = var4;
      }

      if (!this.жэ()) {
         this.l6();
      } else if (this.В_80ab678b != null && this.В_80ab678b.scope() != this.б()) {
         this.lы();
      }
   }

   private void Ы(Channel var1, EmoteSyncMessage var2) {
      var1.writeAndFlush(new EmoteSyncPayloadHandler(var2.encode(), var2.scope(), null)).addListener(var2x -> Minecraft.getInstance().execute(() -> {
         if (this.е_04e5b147 == var1) {
            if (var2x.isSuccess()) {
               this.Вц = false;
            } else {
               this.Б(ВЪй);
            }
         }
      }));
   }

   public void lы() {
      EmoteSyncMessage var1 = this.В_80ab678b;
      this.В_80ab678b = null;
      this.В5 = 0L;
      if (var1 != null && this.Ы(this.е_04e5b147)) {
         if (var1.scope() != EmoteSyncMessage.Scope.PARTY
            || var1.party().equalsIgnoreCase(PartyClient.Х_4d6328fc().ьо()) && Objects.equals(var1.partySession(), PartyClient.Х_4d6328fc().Х_55adc84b())) {
            this.Ы(
               this.е_04e5b147,
               new EmoteSyncMessage(
                  1,
                  this.field_4_55adc84b,
                  ++this.В8,
                  var1.id(),
                  var1.player(),
                  var1.name(),
                  var1.member(),
                  var1.server(),
                  var1.dimension(),
                  var1.party(),
                  var1.partySession(),
                  var1.scope(),
                  var1.emote(),
                  var1.variant(),
                  EmoteSyncMessage.State.CLEAR,
                  var1.elapsedMillis(),
                  var1.stopAgeMillis(),
                  System.currentTimeMillis()
               )
            );
         }
      }
   }

   private boolean Ы(Channel var1) {
      return var1 != null && var1.isActive() && var1.isWritable() && var1.pipeline().get(ВЪ_) != null && var1.pipeline().get(ВЪШ) != null;
   }

   private void Б(String var1) {
      if (!this.Вц) {
         this.Вц = true;
         ClientChat.З(var1);
      }
   }

   public void lв() {
      this.lТ();
      this.必(true);
   }

   public boolean Ы(UUID var1, long var2) {
      EmoteSyncMessage var4 = this.В_8e53a6f4.Ц(var1, var2);
      return var4 != null && var1.equals(this.Ы(var4));
   }

   private UUID Ы(EmoteSyncMessage var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.жэ()
         && this.В_24d74df8 != null
         && this.В_24d74df8.world == var2.level
         && var2.player != null
         && !var1.player().equals(var2.player.getUUID())
         && this.В_24d74df8.server.equals(var1.server())
         && this.В_24d74df8.dimension.equals(var1.dimension())) {
         Player var3 = var2.level.getPlayerByUUID(var1.player());
         if (var3 == null || !var3.getGameProfile().name().equalsIgnoreCase(var1.name())) {
            var3 = var2.level.players().stream().filter(var1x -> var1x.getGameProfile().name().equalsIgnoreCase(var1.name())).findFirst().orElse(null);
         }

         if (var3 != null && var3 != var2.player && !var3.getUUID().equals(var2.player.getUUID())) {
            boolean var4 = PartyClient.Х_4d6328fc().Х(var1.party(), var1.member(), var1.player(), var1.partySession());
            return var1.visibleTo(this.А.getAsBoolean(), var4) ? var3.getUUID() : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private EmoteSyncController.SyncContext Ы(Minecraft var1, RoseIrc var2) {
      if (var1.player != null && var1.level != null) {
         ServerData var3 = var1.getConnection() == null ? null : var1.getConnection().getServerData();
         String var4 = var3 != null && !var1.isLocalServer() ? var3.ip.trim().toLowerCase(Locale.ROOT) : "";
         if (var4.endsWith(ВЪO)) {
            var4 = var4.substring(0, var4.length() - 6);
         }

         PartyClient var5 = PartyClient.Х_4d6328fc();
         String var6 = var5.ьо();
         return new EmoteSyncController.SyncContext(
            var1.level,
            var1.player.getUUID(),
            var1.player.getGameProfile().name(),
            var2 == null ? "" : var2.З_d285c1ed().Юс(),
            var4,
            var1.level.dimension().identifier().toString(),
            var6,
            var6.isEmpty() ? null : var5.Х_55adc84b()
         );
      } else {
         return null;
      }
   }

   private void б(RoseIrc var1, Channel var2, Minecraft var3) {
      var2.eventLoop().execute(() -> {
         if (var1.З_d285c1ed().ч_04e5b147() == var2 && var2.isActive()) {
            IrcCustomPackets var4 = var1.З_677a0540().М_4c863fd0();
            boolean var5 = var4.stream().anyMatch(var0 -> var0 instanceof EmoteSyncPayloadHandler var1xx && var1xx.З() == EmoteSyncMessage.Scope.ALL);
            boolean var6 = var4.stream().anyMatch(var0 -> var0 instanceof EmoteSyncPayloadHandler var1xx && var1xx.З() == EmoteSyncMessage.Scope.PARTY);
            if (!var5 || !var6) {
               var4.removeIf(var0 -> var0 instanceof EmoteSyncPayloadHandler);

               for (EmoteSyncMessage.Scope var10 : EmoteSyncMessage.Scope.values()) {
                  var4.add(new EmoteSyncPayloadHandler("", var10, var3xx -> {
                     long var4x = System.currentTimeMillis();
                     var3.execute(() -> {
                        if (this.е_04e5b147 == var2 && var2.isActive()) {
                           this.Ы(var3xx, var4x);
                        }
                     });
                  }));
               }
            }
         }
      });
   }

   private boolean Ы(EmoteSyncController.SyncContext var1) {
      PartyClient var2 = PartyClient.Х_4d6328fc();
      return !var1.party.isEmpty() && var1.party.equalsIgnoreCase(var2.ьо()) && Objects.equals(var1.partySession, var2.Х_55adc84b());
   }

   public void Ы(BooleanSupplier var1, BooleanSupplier var2, BooleanSupplier var3) {
      this.е_1dd325e2 = var1;
      this.А = var2;
      this.field_4_1dd325e2 = var3;
      if (!this.Вз) {
         this.Вз = true;
         ClientTickEvents.END_CLIENT_TICK.register(this::l);
      }
   }

   public void l6() {
      this.lы();
      PlayerEmoteController.必и();
      this.В_8e53a6f4.ф9();
      this.Вц = false;
   }

   public static EmoteSyncController Ы() {
      return В_3214c619;
   }

   private void Ы(EmoteSyncMessage var1, long var2) {
      UUID var4 = this.Ы(var1);
      if (var4 != null) {
         EmoteDefinition var5 = ц.get(var1.emote());
         if (var5 != null && var5.animations().contains(var1.variant())) {
            long var6 = Math.max(0L, System.currentTimeMillis() - var2);
            if (var6 < ВЪы) {
               long var8 = System.nanoTime();
               if (this.В_8e53a6f4.Ц(var4, var1, var8)) {
                  PlayerEmoteController.衣(var4, var1, var5, var8, var1.transitMillis(var2) + var6);
               }
            }
         }
      }
   }

   private void 必(boolean var1) {
      PlayerEmoteController.ActiveEmote var2 = PlayerEmoteController.衣();
      if (this.жэ() && var2 != null && this.В_24d74df8 != null) {
         long var3 = System.nanoTime();
         if (var1 || this.В_80ab678b == null || var3 - this.В5 >= ВЪ7) {
            if (this.б() == EmoteSyncMessage.Scope.PARTY && !this.Ы(this.В_24d74df8)) {
               if (var1) {
                  this.Б(ВЪР);
               }
            } else if (!this.Ы(this.е_04e5b147)) {
               if (var1) {
                  this.Б(ВЪ3);
               }
            } else {
               EmoteSyncMessage.Scope var5 = this.б();
               String var6 = var5 == EmoteSyncMessage.Scope.PARTY ? this.В_24d74df8.party : "";
               UUID var7 = var5 == EmoteSyncMessage.Scope.PARTY ? this.В_24d74df8.partySession : null;
               EmoteSyncMessage var8 = new EmoteSyncMessage(
                  1,
                  this.field_4_55adc84b,
                  ++this.В8,
                  var2.А,
                  this.В_24d74df8.player,
                  this.В_24d74df8.name,
                  this.В_24d74df8.member,
                  this.В_24d74df8.server,
                  this.В_24d74df8.dimension,
                  var6,
                  var7,
                  var5,
                  var2.В_df4768fc.id(),
                  var2.ск,
                  var2.В_1c6e94e4.й == 0L ? EmoteSyncMessage.State.PLAY : EmoteSyncMessage.State.STOP,
                  Math.max(0L, (var3 - var2.В_1c6e94e4.ы) / ВЪр),
                  var2.В_1c6e94e4.й == 0L ? 0L : Math.max(0L, (var3 - var2.В_1c6e94e4.й) / ВЪ6),
                  System.currentTimeMillis()
               );
               if (var8.valid()) {
                  this.В_80ab678b = var8;
                  this.В5 = var3;
                  this.Ы(this.е_04e5b147, var8);
               }
            }
         }
      }
   }

   private EmoteSyncMessage.Scope б() {
      return this.А.getAsBoolean() ? EmoteSyncMessage.Scope.PARTY : EmoteSyncMessage.Scope.ALL;
   }

   private void l(Minecraft var1) {
      this.lТ();
      if (this.жэ() && this.В_24d74df8 != null) {
         PlayerEmoteController.ш(this.field_4_1dd325e2.getAsBoolean());
         this.必(false);
         this.В_8e53a6f4.Л(System.nanoTime());
      }
   }

   private record SyncContext(Object world, UUID player, String name, String member, String server, String dimension, String party, UUID partySession) {
   }
}
