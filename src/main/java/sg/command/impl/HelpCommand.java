package sg.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Arrays;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.client.RoseClient;
import sg.command.ClientCommand;
import sg.command.CommandRegistry;
import sg.network.ClientChat;

public class HelpCommand extends ClientCommand {
   private static final String COMMAND_NAME = "help";
   private static final String COMMAND_LIST_HEADER = "Команды клиента:";
   private static final String ALIAS_SEPARATOR = ", ";
   private static final String MISSING_DESCRIPTION_MESSAGE = "Описание отсутствует";

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.executes(
         commandContext -> {
            CommandRegistry commands = RoseClient.getInstance().getServices().getCommands();
            String commandPrefix = commands.getCommandPrefix().toString();
            ClientChat.З(COMMAND_LIST_HEADER);
            ClientChat.З("Подробная помощь: " + commandPrefix + "<команда> help");

            for (ClientCommand command : commands) {
               String aliasSummary = command.getAliases().length == 0
                  ? ""
                  : " (" + String.join(ALIAS_SEPARATOR, Arrays.stream(command.getAliases()).map(alias -> commandPrefix + alias).toList()) + ")";
               String description = command.getDescription() != null && !command.getDescription().isBlank() ? command.getDescription() : MISSING_DESCRIPTION_MESSAGE;
               ClientChat.З(commandPrefix + command.getName() + aliasSummary + " — " + description);
            }

            return 1;
         }
      );
   }

   public HelpCommand() {
      super(COMMAND_NAME);
   }
}
