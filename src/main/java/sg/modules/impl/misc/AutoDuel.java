package sg.modules.impl.misc;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;
import sg.settings.SingleChoiceSetting;

public final class AutoDuel extends ClientModule {
   private static final Pattern Л = Pattern.compile(AutoDuel.Рл);
   private final SingleChoiceSetting р;
   private final KeybindSetting ч;
   private final List<String> сп;
   private final Stopwatch Ч;
   private int еЪ;
   private final EventListener<PlayerTickEvent> ВЬ;
   private final EventListener<PacketReceiveEvent> В衣;
   private final EventListener<InputEvent> Вш;
   private static final String Р衣 = "AutoDuel";
   private static final String Рш = "Автоматически отправляет вызов на дуэль игрокам";
   private static final String РИ = "Режим";
   private static final String Рь = "Ball";
   private static final String Рж = "Shield";
   private static final String Р弟 = "Spikes";
   private static final String Рб = "Netherite";
   private static final String РЫ = "CheatParadise";
   private static final String Рl = "Bow";
   private static final String Р8 = "Classic";
   private static final String Р5 = "Totems";
   private static final String Рв = "NoDebuff";
   private static final String Ръ = "Клавиша отключения";
   private static final long РМ = 800L;
   private static final long РН = 1000L;
   private static final String Рн = "Выбор набора (1/1)";
   private static final long РЦ = 150L;
   private static final String Рч = "Настройка поединка";
   private static final long РЭ = 150L;
   private static final String Р必 = "начало";
   private static final String Рщ = "через";
   private static final String РГ = "секунд!";
   private static final String Рл = "^\\w{3,16}$";

   private void р(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundSystemChatPacket var2) {
         String var4 = var2.content().getString().toLowerCase(Locale.ROOT);
         if (var4.contains(Р必) && var4.contains(Рщ) && var4.contains(РГ) || var4.isEmpty()) {
            this.toggle();
         }
      }
   }

   private void вб(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getConnection() != null && MINECRAFT_CLIENT.gameMode != null) {
         List var2 = MINECRAFT_CLIENT.getConnection()
            .getOnlinePlayers()
            .stream()
            .map(PlayerInfo::getProfile)
            .map(GameProfile::name)
            .filter(var0 -> Л.matcher(var0).matches())
            .collect(Collectors.toList());
         if (this.Ч.method_0(РМ * var2.size())) {
            this.сп.clear();
            this.еЪ = 0;
            this.Ч.ВД();
         }

         if (!var2.isEmpty() && this.Ч.method_0(РН)) {
            if (this.еЪ >= var2.size()) {
               this.еЪ = 0;
            }

            String var3 = (String)var2.get(this.еЪ);
            String var4 = MINECRAFT_CLIENT.player.getGameProfile().name();
            if (!this.сп.contains(var3) && !var3.equals(var4)) {
               MINECRAFT_CLIENT.getConnection().sendCommand("duel " + var3);
               this.сп.add(var3);
            }

            this.еЪ++;
            this.Ч.ВД();
         }

         if (MINECRAFT_CLIENT.player.containerMenu instanceof ChestMenu var6) {
            if (MINECRAFT_CLIENT.gui.screen() != null) {
               String var8 = MINECRAFT_CLIENT.gui.screen().getTitle().getString();
               if (var8.contains(Рн)) {
                  if (!this.Ч.method_0(РЦ)) {
                     return;
                  }

                  AutoDuel.DuelKit var5 = AutoDuel.DuelKit.valueOf(this.р.getSelectedOption().getName());
                  MINECRAFT_CLIENT.gameMode.handleContainerInput(var6.containerId, var5.ordinal(), 0, ContainerInput.QUICK_MOVE, MINECRAFT_CLIENT.player);
                  this.Ч.ВД();
               } else if (var8.contains(Рч)) {
                  if (!this.Ч.method_0(РЭ)) {
                     return;
                  }

                  MINECRAFT_CLIENT.gameMode.handleContainerInput(var6.containerId, 0, 0, ContainerInput.QUICK_MOVE, MINECRAFT_CLIENT.player);
                  this.Ч.ВД();
               }
            }
         }
      }
   }

   public AutoDuel() {
      super(Р衣, Рш, ModuleCategory.MISC);
      this.р = new SingleChoiceSetting(
         РИ,
         new ChoiceOption(Рь),
         new ChoiceOption(Рж),
         new ChoiceOption(Р弟),
         new ChoiceOption(Рб),
         new ChoiceOption(РЫ),
         new ChoiceOption(Рl),
         new ChoiceOption(Р8),
         new ChoiceOption(Р5),
         new ChoiceOption(Рв)
      );
      this.ч = new KeybindSetting(Ръ, -1);
      this.сп = new ArrayList<>();
      this.Ч = new Stopwatch();
      this.ВЬ = this::вб;
      this.В衣 = this::р;
      this.Вш = this::Б;
      this.addSettings(this.р, this.ч);
   }

   @Override
   public void onEnable() {
      this.сп.clear();
      this.еЪ = 0;
      this.Ч.ВД();
      super.onEnable();
   }

   @Override
   public void onDisable() {
      this.сп.clear();
      this.еЪ = 0;
      this.Ч.ВД();
      super.onDisable();
   }

   private void Б(InputEvent var1) {
      if (this.ч.getValue() == var1.getInputCode() && var1.getAction() == 1) {
         this.toggle();
      }
   }

   public enum DuelKit {
      Shield,
      Spikes,
      Bow,
      Totems,
      NoDebuff,
      Ball,
      Classic,
      CheatParadise,
      Netherite;
   }
}
