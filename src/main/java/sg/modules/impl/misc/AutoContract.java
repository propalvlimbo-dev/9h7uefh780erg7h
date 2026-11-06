package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket.Handler;
import net.minecraft.util.StringUtil;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.command.impl.AutoContractCommand;
import sg.network.ClientChat;
import sg.util.time.Stopwatch;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.misc.AutoJoiner;
import sg.settings.FloatSetting;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;

public final class AutoContract extends ClientModule {
   private final FloatSetting Яы;
   private final KeybindSetting э;
   @SerializedName("5")
   private final Stopwatch field_5;
   private boolean ХГ;
   private boolean Хл;
   private boolean Хэ;
   private UUID а;
   private final EventListener<PacketReceiveEvent> ЯН;
   private final EventListener<PlayerTickEvent> Ян;
   private final EventListener<InputEvent> ЯЦ;
   private static final String Яой = "AutoContract";
   private static final String ЯоЖ = "Автоматически ищет цель, на указанном вами грифе";
   private static final String Яом = "Гриф";
   private static final float ЯоК = 96.0F;
   private static final String ЯоI = "Клавиша отключения";
   private static final String ЯоТ = "Вы находитесь в PVP-режиме!";
   private static final String ЯоО = "Укажите цель! .autocontract <name> | .act <name>";
   private static final String ЯоФ = "Ваша цель";
   private static final String ЯоЙ = "Найден нужный игрок";
   private static final String Яок = "Ваша цель";
   private static final String Яог = "Не спамь!";
   private static final String Яо0 = "Не получилось";
   private static final String Яох = "К сожалению сервер переполнен";
   private static final String ЯоУ = "Подождите 20 секунд!";
   private static final String ЯоЗ = "большой поток игроков";
   private static final long ЯоЕ = 600L;
   private static final String ЯоП = "contract get";

   @Override
   public void onEnable() {
      this.ХГ = false;
      this.Хл = false;
      this.field_5.ВД();
      if (AutoContractCommand.targetNickname != null) {
         this.Э8();
      }

      super.onEnable();
   }

   private void Э8() {
      AutoJoiner var1 = RoseClient.getInstance().getServices().getModules().getAutoJoiner();
      var1.ы(this.Яы.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.ХГ = false;
      this.Хл = false;
      this.field_5.ВД();
      super.onDisable();
   }

   private void Н(InputEvent var1) {
      if (this.э.getValue() == var1.getInputCode() && var1.getAction() == 1) {
         this.toggle();
      }
   }

   private void л(ClientboundBossEventPacket var1) {
      var1.dispatch(new Handler() {
         private static final String Врм = "pvp";
         private static final String ВрК = "у вас осталось";

         @Override
         public void add(UUID var1, Component var2, float var3, BossBarColor var4, BossBarOverlay var5, boolean var6, boolean var7, boolean var8) {
            String var9 = StringUtil.stripColor(var2.getString()).toLowerCase(Locale.ROOT);
            if (var9.contains(Врм) || var9.contains(ВрК)) {
               AutoContract.this.Хэ = true;
               AutoContract.this.а = var1;
            }
         }

         @Override
         public void remove(UUID var1) {
            if (AutoContract.this.а != null && AutoContract.this.а.equals(var1)) {
               AutoContract.this.Хэ = false;
               AutoContract.this.а = null;
            }
         }
      });
   }

   private void с(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundBossEventPacket var2) {
         this.л(var2);
      }

      if (this.Е4()) {
         ClientChat.З(ЯоТ);
         this.toggle();
      } else if (AutoContractCommand.targetNickname == null) {
         ClientChat.З(ЯоО);
         this.toggle();
      } else {
         if (var1.getPacket() instanceof ClientboundLoginPacket) {
            this.Хл = true;
         }

         if (var1.getPacket() instanceof ClientboundSystemChatPacket var5) {
            String var7 = StringUtil.stripColor(var5.content().getString());
            if (var7 == null) {
               var7 = var5.content().getString();
            }

            String var4 = AutoContractCommand.targetNickname;
            if (var7.contains(ЯоФ) && var7.contains(var4)) {
               ClientChat.З(ЯоЙ);
               this.toggle();
            } else if (!var7.contains(Яок) && !var7.contains(Яог) && !var7.contains(Яо0)) {
               if (var7.contains(Яох) || var7.contains(ЯоУ) || var7.contains(ЯоЗ)) {
                  this.ХГ = true;
                  this.field_5.ВД();
               }
            } else {
               this.Хл = false;
               this.ХГ = true;
               this.field_5.ВД();
            }
         }
      }
   }

   private void вЙ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getConnection() != null) {
         if (this.ХГ && this.field_5.method_0(ЯоЕ)) {
            this.Э8();
            this.ХГ = false;
         }

         boolean var2 = true;
         String var3 = AutoContractCommand.targetNickname;
         if (var3 != null) {
            for (PlayerInfo var5 : MINECRAFT_CLIENT.getConnection().getOnlinePlayers()) {
               if (var5.getProfile().name().equals(var3)) {
                  var2 = false;
                  break;
               }
            }
         }

         if (this.Хл && !var2) {
            MINECRAFT_CLIENT.player.connection.sendCommand(ЯоП);
            this.Хл = false;
         }
      }
   }

   private boolean Е4() {
      return this.Хэ;
   }

   public AutoContract() {
      super(Яой, ЯоЖ, ModuleCategory.MISC);
      this.Яы = new FloatSetting(Яом, 1.0F, 1.0F, ЯоК).withStep(1.0F);
      this.э = new KeybindSetting(ЯоI, -1);
      this.field_5 = new Stopwatch();
      this.ЯН = this::с;
      this.Ян = this::вЙ;
      this.ЯЦ = this::Н;
      this.addSettings(this.Яы, this.э);
   }
}
