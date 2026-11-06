package sg.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.modules.impl.misc.AutoJoiner;
import sg.client.RoseClient;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public class ReconnectCommand extends ClientCommand {
   private static final String COMMAND_NAME = "rct";
   private static final String GRIEF_ARGUMENT_NAME = "grief";
   private static final String GRIEF_ARGUMENT_KEY = "grief";
   private static final String GRIEF_NOT_FOUND_MESSAGE = "Гриф не найден!";

   public ReconnectCommand() {
      super(COMMAND_NAME);
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.executes(commandContext -> {
         AutoJoiner autoJoiner = RoseClient.getInstance().getServices().getModules().getAutoJoiner();
         AutoJoiner.HolyWorldSelection currentAnarchy = AutoJoiner.method_9();
         if (currentAnarchy != null) {
            if (!autoJoiner.method_9(currentAnarchy)) {
               ClientChat.З("Не удалось начать перезаход на анархию #" + currentAnarchy.number());
               return 1;
            } else {
               ClientChat.З("Перезаход на " + currentAnarchy.category().name() + " анархию #" + currentAnarchy.number());
               return 1;
            }
         } else {
            int currentGriefNumber = AutoJoiner.method_9С();
            if (currentGriefNumber == -1) {
               ClientChat.З(GRIEF_NOT_FOUND_MESSAGE);
               return 1;
            } else if (!autoJoiner.ы(currentGriefNumber)) {
               ClientChat.З("Не удалось начать перезаход на гриф #" + currentGriefNumber);
               return 1;
            } else {
               ClientChat.З("Перезаход на гриф #" + currentGriefNumber);
               return 1;
            }
         }
      });
      commandBuilder.then(this.argument(GRIEF_ARGUMENT_NAME, IntegerArgumentType.integer(1, 96)).executes(commandContext -> {
         int requestedGriefNumber = IntegerArgumentType.getInteger(commandContext, GRIEF_ARGUMENT_KEY);
         AutoJoiner autoJoiner = RoseClient.getInstance().getServices().getModules().getAutoJoiner();
         if (!autoJoiner.ы(requestedGriefNumber)) {
            ClientChat.З("Не удалось начать перезаход на гриф #" + requestedGriefNumber);
            return 1;
         } else {
            ClientChat.З("Перезаход на гриф #" + requestedGriefNumber);
            return 1;
         }
      }));
   }
}
