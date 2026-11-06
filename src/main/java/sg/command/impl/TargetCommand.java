package sg.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import sg.ec.MinecraftAccess;
import sg.ec.ForcedPlayerTarget;
import sg.modules.impl.combat.Aura;
import sg.client.RoseClient;
import sg.command.argument.PlayerNameArgumentType;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public final class TargetCommand extends ClientCommand implements MinecraftAccess {
   private static final String COMMAND_NAME = "target";
   private static final String OFF_LITERAL = "off";
   private static final String NAME_ARGUMENT_NAME = "name";
   private static final String TARGET_NOT_SET_MESSAGE = "Цель не задана. ";
   private static final String SET_TARGET_USAGE = ".target <ник>";
   private static final String SET_TARGET_USAGE_DESCRIPTION = " — зафиксировать, ";
   private static final String CLEAR_TARGET_USAGE = ".target off";
   private static final String CLEAR_TARGET_USAGE_DESCRIPTION = " — снять.";
   private static final String CURRENT_TARGET_MESSAGE_PREFIX = "Текущая цель: ";
   private static final String CLEAR_TARGET_KEYWORD = "off";
   private static final String TARGET_FIXED_MESSAGE_PREFIX = "Aura зафиксирована на ";
   private static final String TARGET_ALREADY_CLEARED_MESSAGE = "Цель уже снята.";
   private static final String TARGET_CLEARED_MESSAGE_PREFIX = "Цель снята: ";
   private static final String NAME_ARGUMENT_KEY = "name";

   private int setTarget(String playerName) {
      String resolvedName = this.resolvePlayerName(playerName.trim());
      if (!resolvedName.isEmpty() && !resolvedName.equalsIgnoreCase(CLEAR_TARGET_KEYWORD)) {
         ForcedPlayerTarget.ло_de5a6f78(resolvedName);
         this.clearMismatchedAuraTarget();
         ClientChat.З(
            Component.literal(TARGET_FIXED_MESSAGE_PREFIX)
               .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
               .append(Component.literal(resolvedName).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)))
         );
         return 1;
      } else {
         return this.clearTarget();
      }
   }

   private int showTarget() {
      if (!ForcedPlayerTarget.method_3и()) {
         ClientChat.З(
            Component.literal(TARGET_NOT_SET_MESSAGE)
               .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
               .append(Component.literal(SET_TARGET_USAGE).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
               .append(Component.literal(SET_TARGET_USAGE_DESCRIPTION).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)))
               .append(Component.literal(CLEAR_TARGET_USAGE).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
               .append(Component.literal(CLEAR_TARGET_USAGE_DESCRIPTION).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)))
         );
         return 1;
      } else {
         ClientChat.З(
            Component.literal(CURRENT_TARGET_MESSAGE_PREFIX)
               .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
               .append(Component.literal(ForcedPlayerTarget.ОМ()).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)))
         );
         return 1;
      }
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.executes(commandContext -> this.showTarget())
         .then(this.literal(OFF_LITERAL).executes(commandContext -> this.clearTarget()))
         .then(this.argument(NAME_ARGUMENT_NAME, PlayerNameArgumentType.players(PlayerNameArgumentType.PlayerScope.ALL)).executes(commandContext -> this.setTarget(commandContext.getArgument(NAME_ARGUMENT_KEY, String.class))));
   }

   private String resolvePlayerName(String playerName) {
      if (playerName.isEmpty()) {
         return playerName;
      }

      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.connection != null) {
         for (PlayerInfo onlinePlayerInfo : MINECRAFT_CLIENT.player.connection.getListedOnlinePlayers()) {
            String onlinePlayerName = onlinePlayerInfo.getProfile().name();
            if (onlinePlayerName.equalsIgnoreCase(playerName)) {
               return onlinePlayerName;
            }
         }
      }

      if (MINECRAFT_CLIENT.level != null) {
         for (Player nearbyPlayer : MINECRAFT_CLIENT.level.players()) {
            String nearbyPlayerName = nearbyPlayer.getGameProfile().name();
            if (nearbyPlayerName.equalsIgnoreCase(playerName)) {
               return nearbyPlayerName;
            }
         }
      }

      return playerName;
   }

   public TargetCommand() {
      super(COMMAND_NAME);
   }

   private void clearMismatchedAuraTarget() {
      Aura aura = RoseClient.getInstance().getServices().getModules().getAura();
      if (!(aura.Ж_95bedd6e() instanceof Player currentPlayerTarget && ForcedPlayerTarget.ю(currentPlayerTarget))) {
         aura.Т(null);
      }
   }

   private int clearTarget() {
      if (!ForcedPlayerTarget.method_3и()) {
         ClientChat.З(TARGET_ALREADY_CLEARED_MESSAGE);
         return 1;
      } else {
         String previousTargetName = ForcedPlayerTarget.ОМ();
         ForcedPlayerTarget.ЪФ();
         ClientChat.З(
            Component.literal(TARGET_CLEARED_MESSAGE_PREFIX)
               .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
               .append(Component.literal(previousTargetName).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
         );
         return 1;
      }
   }
}
