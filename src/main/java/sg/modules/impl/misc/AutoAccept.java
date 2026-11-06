package sg.modules.impl.misc;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.MovementUtils;

public final class AutoAccept extends ClientModule {
   private static final Pattern а = Pattern.compile(AutoAccept.сЧв);
   private static final Pattern Ю = Pattern.compile(AutoAccept.сЧъ);
   private static final Pattern Ч = Pattern.compile(AutoAccept.сЧМ);
   private static final long В弟 = 250L;
   private final @com.google.gson.annotations.SerializedName("сж")  BooleanSetting friendsOnlySetting;
   private @com.google.gson.annotations.SerializedName("ХЙ")  int pendingAcceptDelayTicks;
   private @com.google.gson.annotations.SerializedName("ВЗ")  boolean acceptPending;
   private final @com.google.gson.annotations.SerializedName("сБ")  EventListener<PacketReceiveEvent> packetReceiveListener;
   private final @com.google.gson.annotations.SerializedName("сС")  EventListener<PlayerTickEvent> playerTickListener;
   private static final String сЧЬ = "AutoAccept";
   private static final String сЧ衣 = "Автоматически принимает запросы телепортации";
   private static final String сЧш = "Только друзья";
   private static final String сЧИ = "Рейджовый";
   private static final String сЧь = "tpaccept";
   private static final String сЧж = "tpaccept";
   private static final String сЧ弟 = "телепорт";
   private static final String сЧб = "teleport";
   private static final String сЧЫ = "просит";
   private static final String сЧl = "request";
   private static final String сЧ8 = "want";
   private static final String сЧ5 = "хочет";
   private static final String сЧв = "§.";
   private static final String сЧъ = "^([a-zA-Z0-9_]{3,16})\\b";
   private static final String сЧМ = "(?i)\\b(?:ник|nick)\\s*:?\\s*([a-zA-Z0-9_]{3,16})\\b";

   private void 必(PacketReceiveEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getConnection() != null) {
         if (var1.getPacket() instanceof ClientboundSystemChatPacket var2) {
            String var6 = var2.content().getString();
            String var4 = ы(var6);
            if (!К(var4)) {
               return;
            }

            Optional var5 = O(var4);
            if (this.friendsOnlySetting.getValue()) {
               if (var5.isEmpty()) {
                  return;
               }

               if (!RoseClient.getInstance().getServices().getFriends().О((String)var5.get())) {
                  return;
               }
            }

            if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(сЧИ)) {
               MINECRAFT_CLIENT.getConnection().sendCommand(сЧь);
            } else if (!this.acceptPending) {
               this.acceptPending = true;
               this.pendingAcceptDelayTicks = RoseClient.getInstance().getServices().getModules().getGuiMove().И_989fed9a().getValue().intValue();
            }
         }
      }
   }

   public AutoAccept() {
      super(сЧЬ, сЧ衣, ModuleCategory.MISC);
      this.friendsOnlySetting = new BooleanSetting(сЧш, false);
      this.pendingAcceptDelayTicks = 0;
      this.packetReceiveListener = this::必;
      this.playerTickListener = this::method_7;
      this.addSettings(this.friendsOnlySetting);
   }

   private void method_7(PlayerTickEvent var1) {
      if (this.acceptPending) {
         KeyMapping[] var2 = new KeyMapping[]{
            MINECRAFT_CLIENT.options.keyUp, MINECRAFT_CLIENT.options.keyDown, MINECRAFT_CLIENT.options.keyLeft, MINECRAFT_CLIENT.options.keyRight, MINECRAFT_CLIENT.options.keyJump, MINECRAFT_CLIENT.options.keySprint
         };

         for (KeyMapping var6 : var2) {
            var6.setDown(false);
         }

         RoseClient.getInstance().getServices().getModules().getGuiMove().з(true);
         if (!MovementUtils.hasMovementInput()) {
            if (this.pendingAcceptDelayTicks > 0) {
               this.pendingAcceptDelayTicks--;
               return;
            }

            MINECRAFT_CLIENT.getConnection().sendCommand(сЧж);
            this.acceptPending = false;
            RoseClient.getInstance().getServices().getModules().getGuiMove().з(false);
            if (MINECRAFT_CLIENT.gui.screen() == null) {
               for (KeyMapping var10 : var2) {
                  var10.setDown(InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), var10.key.getValue()));
               }
            }
         }
      }
   }

   private static String ы(String var0) {
      if (var0 == null) {
         return "";
      }

      String var1 = а.matcher(var0).replaceAll("");
      return var1.replace('\n', ' ').trim().toLowerCase(Locale.ROOT);
   }

   private static boolean К(String var0) {
      boolean var1 = var0.contains(сЧ弟) || var0.contains(сЧб);
      if (!var1) {
         return false;
      } else {
         return !var0.contains(сЧЫ) ? false : var0.contains(сЧl) || var0.contains(сЧ8) || var0.contains(сЧ5);
      }
   }

   private static Optional<String> O(String var0) {
      Matcher var1 = Ч.matcher(var0);
      if (var1.find()) {
         return Optional.ofNullable(var1.group(1));
      }

      Matcher var2 = Ю.matcher(var0);
      return var2.find() ? Optional.ofNullable(var2.group(1)) : Optional.empty();
   }
}
