package sg.handler;

import com.google.gson.annotations.SerializedName;
import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import sg.network.irc.IrcPartyInboundHandler;
import sg.network.irc.IrcCustomPackets;
import sg.network.irc.IrcPartyJoinPacket;
import sg.network.irc.IrcMessagePacket;
import sg.network.irc.IrcPartyLeavePacket;
import sg.network.irc.IrcPartyCreatePacket;
import sg.network.irc.IrcPartyDisbandPacket;
import sg.client.RoseClient;
import sg.network.irc.packet.IrcPacket;
import sg.modules.impl.misc.Party;
import sg.network.ClientChat;
import sg.network.IrcRoles;
import sg.render.ClientCosmeticSelections;
import sg.network.irc.RoseIrc;

public final class PartyClient {
   private static final PartyClient В_4d6328fc = new PartyClient();
   @SerializedName("В")
   private final PartyOperationState В_d2e0b95d = new PartyOperationState();
   @SerializedName("В")
   private final ArrayBlockingQueue<PartyClient.InboundPacket> В_880b51bd = new ArrayBlockingQueue<>(512);
   private final Map<UUID, LivingEntity> Г = new HashMap<>();
   private Channel Х;
   @SerializedName("В")
   private PartySession В_33b0eb23;
   private boolean сЬ;
   private boolean с衣;
   @SerializedName("В")
   private PartyClient.QueuedOperation В_15c46ef4;
   private static final long Ягя = 999999L;
   private static final long Я0с = 1000000L;
   private static final String Я0В = "Дождитесь ответа на предыдущую операцию с группой.";
   private static final String Я0Я = "Название и пароль группы: до 128 символов, латиница, цифры, _, -, + и точка.";
   private static final String Я0Х = "Сначала покиньте текущую группу.";
   private static final String Я0е = "IRC ещё не инициализирован.";
   private static final String Я0А = "Ожидаю готовности IRC; запрос группы поставлен в очередь.";
   private static final String Я04 = "IRC недоступен. Операция с группой не отправлена.";
   private static final String Я0п = "Операция с группой уже выполняется или содержит неверные данные.";
   private static final String Я0Д = "IRC недоступен или ещё проходит авторизацию. Сообщение не отправлено.";
   private static final String Я0Ъ = "Вы не состоите в подтверждённой группе.";
   private static final String Я0т = "Дождитесь завершения операции с группой.";
   private static final String Я0а = "Сообщение должно содержать от 1 до 512 символов без управляющих кодов и §.";
   private static final String Я0Ю = "IRC недоступен или ещё проходит авторизацию. Сообщение не отправлено.";
   private static final String Я0Ч = "Сообщение должно содержать от 1 до 512 символов без управляющих кодов и §.";
   private static final String Я0Л = "[\\p{Cntrl}]";
   private static final String Я0诶 = "Соединение потеряно до ответа на операцию с группой. Результат неизвестен.";
   private static final String Я01 = ":25565";
   private static final long Я0Ь = 30000000000L;
   private static final String Я0衣 = "IRC не подтвердил операцию с группой. Результат неизвестен; соединение будет обновлено.";
   private static final long Я0ш = 30000000000L;
   private static final String Я0И = "Не удалось дождаться IRC. Запрос группы отменён.";
   private static final String Я0ь = "tracking-tick";
   private static final long Я0ж = 5000L;
   private static final String Я0弟 = "TICK_STATUS";
   private static final String Я0б = "null";
   private static final String Я0Ы = "none";
   private static final String Я0l = "null";
   private static final String Я08 = "rose-party";
   private static final String Я05 = "rose-party";
   private static final String Я0в = "inbound";
   private static final String Я0ъ = "rose-party";
   private static final String Я0М = ":";
   private static final String Я0Н = "party.disbanded";
   private static final String Я0н = "Не удалось отправить сообщение в IRC.";
   private static final String Я0Ц = "Не удалось отправить сообщение в группу.";
   private static final String Я0ч = "Не удалось отправить операцию с группой. Соединение будет восстановлено.";

   private void ХЭ() {
      RoseClient var1 = RoseClient.getInstance();
      if (var1 != null && var1.getServices() != null && var1.getServices().getModules() != null) {
         Party var2 = var1.getServices().getModules().getParty();
         if (var2 != null && !var2.isEnabled()) {
            var2.enable();
         }
      }
   }

   public void ХВ() {
      if (this.В_33b0eb23 != null) {
         this.В_33b0eb23.Ж(System.nanoTime());
      }
   }

   public LivingEntity Х_95bedd6e(Marker var1) {
      LivingEntity var2 = this.Г.get(var1.entityUuid());
      return var2 != null && !var2.isRemoved() && var2.isAlive() ? var2 : null;
   }

   public boolean Х(String var1, String var2, UUID var3, UUID var4) {
      return var1 != null && !var1.isEmpty() && var1.equalsIgnoreCase(this.ьо()) && this.В_33b0eb23 != null && this.В_33b0eb23.З(var2, var3, var4);
   }

   public List<String> O() {
      return this.ьо().isEmpty() ? List.of() : this.В_33b0eb23.Ы_2571477d(System.nanoTime());
   }

   private static boolean Ж(String var0) {
      ClientChat.З(var0);
      return false;
   }

   public void ХП() {
      if (!this.сЬ) {
         this.сЬ = true;
         this.ХН();
         ClientTickEvents.END_CLIENT_TICK.register(this::Щ);
      }
   }

   private boolean Х(PartyOperationState.Action var1, String var2, String var3) {
      this.ХН();
      if (this.ХЩ()) {
         return Ж(Я0В);
      }

      boolean var4 = var1 == PartyOperationState.Action.JOIN || var1 == PartyOperationState.Action.CREATE;
      if (!var4 || PartyOperationState.Щ(var2) && PartyOperationState.Я(var3)) {
         if (var4 && this.ХХ()) {
            return Ж(Я0Х);
         }

         if (!this.Ху()) {
            RoseIrc var5 = RoseIrc.З_3e6e5516();
            if (var5 == null) {
               return Ж(Я0е);
            }

            if (!var4) {
               this.В_d2e0b95d.жь();
            }

            this.В_15c46ef4 = new PartyClient.QueuedOperation(var1, var2, var3, System.nanoTime());
            var5.жв();
            ClientChat.З(Я0А);
            return true;
         } else {
            return this.Х(var1, var2, var3, false);
         }
      } else {
         return Ж(Я0Я);
      }
   }

   public String ь_() {
      RoseIrc var1 = RoseIrc.З_3e6e5516();
      return "channel="
         + (this.Х == null ? Я0б : this.Х.id())
         + " ready="
         + (var1 != null && var1.Щ6())
         + " bridgeReady="
         + this.с衣
         + " pending="
         + (this.В_d2e0b95d.О_278bf792() == null ? Я0Ы : this.В_d2e0b95d.О_278bf792().action())
         + " session="
         + (this.В_33b0eb23 == null ? Я0l : this.В_33b0eb23.М2());
   }

   public boolean Х(PartyPosition var1) {
      return this.В_33b0eb23 != null && this.В_33b0eb23.Ы(var1);
   }

   public boolean У(String var1) {
      this.ХН();
      if (!this.Ху()) {
         return Ж(Я0Ю);
      } else if (var1 != null && !var1.isBlank() && var1.length() <= 512 && !var1.codePoints().anyMatch(var0 -> Character.isISOControl(var0) || var0 == 167)) {
         this.Х.writeAndFlush(new IrcMessagePacket(IrcRoles.ЦЖ(var1))).addListener(var0 -> {
            if (!var0.isSuccess()) {
               ClientChat.З(Я0н);
            }
         });
         return true;
      } else {
         return Ж(Я0Ч);
      }
   }

   public UUID Х_55adc84b() {
      return this.Х != null && this.Х.isActive() && this.В_33b0eb23 != null ? this.В_33b0eb23.Ы() : null;
   }

   public boolean Й(String var1) {
      this.ХН();
      if (!this.Ху()) {
         return Ж(Я0Д);
      }

      if (!this.ХХ()) {
         return Ж(Я0Ъ);
      }

      if (this.ХЩ()) {
         return Ж(Я0т);
      }

      PartySession var2 = this.В_33b0eb23;
      PartyChatMessage var3 = var2.З(var1 == null ? "" : var1.strip());
      if (var3 == null) {
         return Ж(Я0а);
      }

      Channel var4 = this.Х;
      var4.writeAndFlush(new IrcPartyInboundHandler.PartyChatPacket(var3.encode(), null)).addListener(var3x -> Minecraft.getInstance().execute(() -> {
         if (!var3x.isSuccess()) {
            ClientChat.З(Я0Ц);
         } else if (this.В_33b0eb23 == var2 && var2.З(var3)) {
            ClientChat.З(IrcRoles.Х_f022d65c(var3));
         }
      }));
      return true;
   }

   public boolean р(String var1) {
      return !this.ьо().isEmpty() && this.В_33b0eb23.Ч(var1);
   }

   public static PartyClient Х_4d6328fc() {
      return В_4d6328fc;
   }

   public Map<String, PartySession.TrackedMarker> Ж() {
      return this.В_33b0eb23 == null ? Map.of() : this.В_33b0eb23.method_5();
   }

   public boolean Х_bbeebd87(Marker var1) {
      return this.В_33b0eb23 != null && this.В_33b0eb23.Ы(var1);
   }

   public boolean Х(String var1, String var2, boolean var3) {
      return this.Х(var3 ? PartyOperationState.Action.CREATE : PartyOperationState.Action.JOIN, var1, var2);
   }

   public boolean Х(String var1, LivingEntity var2, int var3) {
      if (this.Ху() && !this.ХЩ() && this.В_33b0eb23 != null && var2 != null && var2.isAlive()) {
         String var4 = var2.getName().getString().replaceAll(Я0Л, "").strip();
         if (var4.isEmpty()) {
            var4 = var2.getType().getDescription().getString();
         }

         if (var4.length() > 64) {
            var4 = var4.substring(0, 64);
         }

         boolean var5 = this.В_33b0eb23
            .З(
               var1,
               var2.getX(),
               var2.getY(),
               var2.getZ(),
               var3,
               var2.getUUID(),
               BuiltInRegistries.ENTITY_TYPE.getKey(var2.getType()).toString(),
               var4,
               System.nanoTime()
            );
         if (var5) {
            this.Г.put(var2.getUUID(), var2);
         }

         return var5;
      } else {
         return false;
      }
   }

   public boolean Х_bbeebd87(UUID var1, String var2) {
      return this.В_33b0eb23 != null && this.В_33b0eb23.Ы_bbeebd87(var1, var2);
   }

   private boolean Ху() {
      RoseIrc var1 = RoseIrc.З_3e6e5516();
      return this.с衣 && var1 != null && var1.Щ6() && var1.Й() == this.Х && this.Х != null && this.Х.isActive() && this.Х.isWritable();
   }

   public boolean ХХ() {
      return !this.ьо().isEmpty();
   }

   public boolean Х6() {
      return this.Х(PartyOperationState.Action.LEAVE, "", "");
   }

   public String Х_388008bb(UUID var1, String var2) {
      return this.В_33b0eb23 == null ? null : this.В_33b0eb23.Ы_388008bb(var1, var2);
   }

   public boolean Хл() {
      return this.Х(PartyOperationState.Action.DISBAND, "", "");
   }

   private boolean Х(PartyOperationState.Action var1, String var2, String var3, boolean var4) {
      if (this.Ху() && this.В_33b0eb23 != null) {
         PartySession var5 = this.В_33b0eb23;
         Channel var6 = this.Х;
         PartyOperationState.PendingOperation var7 = this.В_d2e0b95d.О(var5, var1, var2, var3, var4, System.nanoTime());
         if (var7 == null) {
            return Ж(Я0п);
         }

         String var8 = var3.isEmpty() ? var2 : var2 + ":" + var3;

         IrcPacket var9 = switch (var1) {
            case JOIN -> new IrcPartyJoinPacket(var8);
            case CREATE -> new IrcPartyCreatePacket(var8);
            case LEAVE -> new IrcPartyLeavePacket();
            case DISBAND -> new IrcPartyDisbandPacket();
         };
         var6.writeAndFlush(var9).addListener(var4x -> {
            if (!var4x.isSuccess()) {
               Minecraft.getInstance().execute(() -> {
                  if (this.Х == var6 && this.В_d2e0b95d.О(var5, var7)) {
                     ClientChat.З(Я0ч);
                     var6.close();
                  }
               });
            }
         });
         return true;
      } else {
         return Ж(Я04);
      }
   }

   public long ф() {
      return this.В_33b0eb23 == null ? 0L : (this.В_33b0eb23.Ы_6da43b94(System.nanoTime()) + Ягя) / Я0с;
   }

   public PartyCosmeticsPayload Х_3b05c6e2(UUID var1, String var2) {
      return this.В_33b0eb23 == null ? null : this.В_33b0eb23.Ы_3b05c6e2(var1, var2);
   }

   public void Х3() {
      this.Х6();
   }

   private void Щ(Minecraft var1) {
      this.ХН();
      long var2 = System.nanoTime();
      if (this.В_33b0eb23 != null) {
         PartyPosition var4 = null;
         ServerData var5 = var1.getConnection() == null ? null : var1.getConnection().getServerData();
         if (var1.level != null && var1.player != null && !var1.isLocalServer() && var5 != null) {
            String var6 = var5.ip.trim().toLowerCase(Locale.ROOT);
            if (var6.endsWith(Я01)) {
               var6 = var6.substring(0, var6.length() - 6);
            }

            var4 = new PartyPosition(
               1,
               RoseIrc.З_3e6e5516().З_d285c1ed().Юс(),
               var1.player.getUUID(),
               var1.player.getGameProfile().name(),
               var6,
               var1.level.dimension().identifier().toString(),
               var1.player.getX(),
               var1.player.getY(),
               var1.player.getZ(),
               true
            );
         }

         for (int var13 = 0; var13 < 128; var13++) {
            PartyClient.InboundPacket var7 = this.В_880b51bd.poll();
            if (var7 == null) {
               break;
            }

            if (var7.channel == this.Х && var2 - var7.received < Я0Ь) {
               if (var7.packet instanceof PartyPresenceMessage var8) {
                  this.В_33b0eb23.З(var8, var7.received);
               } else if (var7.packet instanceof PartyChatMessage var9 && this.В_33b0eb23.З(var9, var7.received)) {
                  ClientChat.З(IrcRoles.Х_f022d65c(var9));
               }
            }
         }

         this.В_33b0eb23.З(var4, ClientCosmeticSelections.createPartyCosmeticsPayload(), var2);
         this.Х(var1, var4);

         for (PartyChatMessage var15 : this.В_33b0eb23.З(var2)) {
            ClientChat.З(IrcRoles.Х_f022d65c(var15));
         }

         if (this.В_d2e0b95d.Ш(var2)) {
            this.В_d2e0b95d.О(this.В_33b0eb23, this.В_d2e0b95d.О_278bf792());
            ClientChat.З(Я0衣);
            this.Х.close();
            return;
         }
      }

      if (this.В_15c46ef4 != null) {
         PartyClient.QueuedOperation var11 = this.В_15c46ef4;
         if (var2 - var11.started >= Я0ш) {
            this.В_15c46ef4 = null;
            ClientChat.З(Я0И);
         } else if (this.Ху()) {
            this.В_15c46ef4 = null;
            this.Х(var11.action, var11.name, var11.password, false);
         }
      } else if (this.Ху() && this.В_d2e0b95d.О_278bf792() == null && this.В_d2e0b95d.О_eaf667c1() != null) {
         PartyOperationState.Credentials var12 = this.В_d2e0b95d.О_eaf667c1();
         this.Х(PartyOperationState.Action.JOIN, var12.name(), var12.password(), true);
      }

      if (PartyTrace.Р(Я0ь, Я0ж)) {
         PartyTrace.Ы(Я0弟, this.ь_());
      }
   }

   public boolean ХЩ() {
      return this.В_d2e0b95d.О_278bf792() != null || this.В_15c46ef4 != null;
   }

   public Map<UUID, PartySession.SmoothedPosition> К() {
      return this.В_33b0eb23 == null ? Map.of() : this.В_33b0eb23.ю();
   }

   private void Х(Minecraft var1, PartyPosition var2) {
      this.Г.clear();
      if (var2 != null && var1.level != null) {
         HashSet var3 = new HashSet();

         for (PartySession.TrackedMarker var5 : this.В_33b0eb23.method_5().values()) {
            Marker var6 = var5.Ц();
            if (var6.entityUuid() != null && this.В_33b0eb23.Ы(var6)) {
               var3.add(var6.entityUuid());
            }
         }

         if (!var3.isEmpty()) {
            for (Entity var9 : var1.level.entitiesForRendering()) {
               if (var9 instanceof LivingEntity var11 && var11.isAlive() && var3.contains(var9.getUUID())) {
                  this.Г.put(var9.getUUID(), var11);
               }
            }

            Marker var8 = this.В_33b0eb23.З();
            if (var8 != null && var8.entityUuid() != null) {
               LivingEntity var10 = this.Г.get(var8.entityUuid());
               if (var10 != null) {
                  this.В_33b0eb23.З(var10.getUUID(), var10.getX(), var10.getY(), var10.getZ());
               }
            }
         }
      }
   }

   public PartyPosition Х(String var1) {
      return this.ьо().isEmpty() ? null : this.В_33b0eb23.З_c65a3f47(var1, System.nanoTime());
   }

   public boolean Х(String var1, double var2, double var4, double var6, int var8) {
      return this.Ху() && !this.ХЩ() && this.В_33b0eb23 != null && this.В_33b0eb23.З(var1, var2, var4, var6, var8, System.nanoTime());
   }

   private void ХН() {
      RoseIrc var1 = RoseIrc.З_3e6e5516();
      Channel var2 = var1 == null ? null : var1.Й();
      if (var2 != null && !var2.isActive()) {
         var2 = null;
      }

      if (this.Х != var2) {
         if (this.В_33b0eb23 != null) {
            if (this.В_d2e0b95d.О_278bf792() != null) {
               ClientChat.З(Я0诶);
            }

            this.В_d2e0b95d.О(this.В_33b0eb23);
         }

         this.Х = var2;
         this.В_33b0eb23 = null;
         this.с衣 = false;
         this.Г.clear();
         this.В_880b51bd.clear();
         if (var2 != null) {
            Channel var3 = var2;
            Minecraft var4 = Minecraft.getInstance();
            PartySession var5 = new PartySession(var1.З_d285c1ed().Юс(), var2x -> {
               if (this.Х == var3 && this.Ху()) {
                  var3.writeAndFlush(new IrcPartyInboundHandler.PresencePacket(var2x.encode(), null)).addListener(var1xx -> {
                     if (!var1xx.isSuccess()) {
                        var3.close();
                     }
                  });
               }
            });
            this.В_33b0eb23 = var5;
            var3.eventLoop()
               .execute(
                  () -> {
                     if (var1.Й() == var3 && var3.isActive()) {
                        IrcCustomPackets var5x = var1.З_677a0540().М_4c863fd0();
                        var5x.removeIf(var0 -> var0 instanceof IrcPartyInboundHandler.PresencePacket || var0 instanceof IrcPartyInboundHandler.PartyChatPacket);
                        var5x.add(
                           new IrcPartyInboundHandler.PresencePacket(
                              "", var2xx -> this.В_880b51bd.offer(new PartyClient.InboundPacket(var3, var2xx, System.nanoTime()))
                           )
                        );
                        var5x.add(
                           new IrcPartyInboundHandler.PartyChatPacket(
                              "", var2xx -> this.В_880b51bd.offer(new PartyClient.InboundPacket(var3, var2xx, System.nanoTime()))
                           )
                        );
                        if (var3.pipeline().get(Я08) != null) {
                           var3.pipeline().remove(Я05);
                        }

                        var3.pipeline().addAfter(Я0в, Я0ъ, new IrcPartyInboundHandler(var4xx -> var4.execute(() -> {
                           if (this.Х == var3 && var3.isActive()) {
                              boolean var4xxx = var5.Зй();
                              this.В_d2e0b95d.О(var5, var4xx, System.nanoTime());
                              if (!var4xxx && var5.Зй()) {
                                 this.ХЭ();
                              }

                              if (!var5.Зй()) {
                                 this.Г.clear();
                              }
                           }
                        }), var4xx -> var4.execute(() -> {
                           if (this.Х == var3 && var3.isActive()) {
                              boolean var4xxx = var5.Зй();
                              var5.р(var4xx);
                              if (var4xxx && !var5.Зй()) {
                                 this.В_d2e0b95d.О(var5, Я0Н, System.nanoTime());
                                 this.Г.clear();
                              }
                           }
                        }), () -> var4.execute(this::ХН)));
                        String var6 = String.join(Я0М, var1.З_677a0540().М_8cef9e59().stream().map(var0 -> var0.Ад()).toList());
                        var4.execute(() -> {
                           if (this.Х == var3 && var3.isActive()) {
                              var5.р(var6);
                              this.с衣 = true;
                           }
                        });
                     }
                  }
               );
         }
      }
   }

   public String ьо() {
      return this.Х != null && this.Х.isActive() && this.В_33b0eb23 != null && this.В_33b0eb23.Зй() ? this.В_33b0eb23.Мж() : "";
   }

   private record QueuedOperation(PartyOperationState.Action action, String name, String password, long started) {
   }

   private record InboundPacket(Channel channel, Object packet, long received) {
   }
}
