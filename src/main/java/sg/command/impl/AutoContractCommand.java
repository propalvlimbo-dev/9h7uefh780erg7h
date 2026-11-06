package sg.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public final class AutoContractCommand extends ClientCommand {
   public static String targetNickname;
   private static final String COMMAND_NAME = "autocontract";
   private static final String COMMAND_ALIAS_ACT = "act";
   private static final String NICKNAME_ARGUMENT_NAME = "nickname";
   private static final String NICKNAME_ARGUMENT_KEY = "nickname";
   private static final String TARGET_SET_MESSAGE_PREFIX = "Цель установлена: ";
   private static final String CURRENT_TARGET_MESSAGE_PREFIX = "Текущая цель: ";
   private static final String TARGET_NOT_SET_MESSAGE = "не задано";

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.executes(
         commandContext -> {
            String currentTargetNickname = targetNickname;
            ClientChat.З(
               Component.literal(CURRENT_TARGET_MESSAGE_PREFIX)
                  .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
                  .append(Component.literal(currentTargetNickname == null ? TARGET_NOT_SET_MESSAGE : currentTargetNickname).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
            );
            return 1;
         }
      );
      commandBuilder.then(
         this.argument(NICKNAME_ARGUMENT_NAME, StringArgumentType.greedyString())
            .executes(
               commandContext -> {
                  String requestedTargetNickname = StringArgumentType.getString(commandContext, NICKNAME_ARGUMENT_KEY);
                  targetNickname = requestedTargetNickname;
                  ClientChat.З(
                     Component.literal(TARGET_SET_MESSAGE_PREFIX)
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
                        .append(Component.literal(requestedTargetNickname).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                  );
                  return 1;
               }
            )
      );
   }

   public AutoContractCommand() {
      super(COMMAND_NAME, COMMAND_ALIAS_ACT);
   }
}
