package sg.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.client.RoseClient;
import sg.modules.impl.misc.KillMessage;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public class TotemPopMessageCommand extends ClientCommand {
   private static final String COMMAND_NAME = "pt";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_TEXT_ARGUMENT_NAME = "text";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_INDEX_ARGUMENT_NAME = "index";
   private static final String LIST_LITERAL = "list";
   private static final String CLEAR_LITERAL = "clear";
   private static final String MESSAGE_REJECTED_MESSAGE = "Сообщение уже есть или пустое.";
   private static final String EMPTY_TOTEM_MESSAGE_LIST_MESSAGE = "Список pt-сообщений пуст. Добавь: .pt add <текст>";
   private static final String TOTEM_MESSAGE_LIST_HEADER = "Pt-сообщения (ник + текст):";
   private static final String LIST_NEWLINE = "\n";
   private static final String LIST_NUMBER_SEPARATOR = ". ";
   private static final String REMOVE_INDEX_ARGUMENT_KEY = "index";
   private static final String ADD_TEXT_ARGUMENT_KEY = "text";

   private int listTotemMessages() {
      KillMessage killMessage = this.getKillMessage();
      if (killMessage.к().isEmpty()) {
         ClientChat.З(EMPTY_TOTEM_MESSAGE_LIST_MESSAGE);
         return 1;
      }

      StringBuilder messageList = new StringBuilder(TOTEM_MESSAGE_LIST_HEADER);
      int messageNumber = 1;

      for (String message : killMessage.к()) {
         messageList.append(LIST_NEWLINE).append(messageNumber++).append(LIST_NUMBER_SEPARATOR).append(message);
      }

      ClientChat.З(messageList.toString());
      return 1;
   }

   private int removeTotemMessage(int messageNumber) {
      KillMessage killMessage = this.getKillMessage();
      if (!killMessage.Ы(messageNumber - 1)) {
         ClientChat.З("Сообщение #" + messageNumber + " не найдено. Всего: " + killMessage.к().size());
         return 1;
      } else {
         this.saveMessagesConfig();
         ClientChat.З("Удалил pt-сообщение #" + messageNumber);
         return 1;
      }
   }

   private KillMessage getKillMessage() {
      return RoseClient.getInstance().getServices().getModules().getKillMessage();
   }

   public TotemPopMessageCommand() {
      super(COMMAND_NAME);
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      ((LiteralArgumentBuilder)commandBuilder.executes(commandContext -> this.listTotemMessages()))
         .then(this.literal(ADD_LITERAL).then(this.argument(ADD_TEXT_ARGUMENT_NAME, StringArgumentType.greedyString()).executes(commandContext -> this.addTotemMessage(StringArgumentType.getString(commandContext, ADD_TEXT_ARGUMENT_KEY)))))
         .then(this.literal(REMOVE_LITERAL).then(this.argument(REMOVE_INDEX_ARGUMENT_NAME, IntegerArgumentType.integer(1)).executes(commandContext -> this.removeTotemMessage(IntegerArgumentType.getInteger(commandContext, REMOVE_INDEX_ARGUMENT_KEY)))))
         .then(this.literal(LIST_LITERAL).executes(commandContext -> this.listTotemMessages()))
         .then(this.literal(CLEAR_LITERAL).executes(commandContext -> this.clearTotemMessages()));
   }

   private int addTotemMessage(String message) {
      KillMessage killMessage = this.getKillMessage();
      if (!killMessage.н(message)) {
         ClientChat.З(MESSAGE_REJECTED_MESSAGE);
         return 1;
      } else {
         this.saveMessagesConfig();
         ClientChat.З("Добавил pt-сообщение: " + message.trim() + " (ник противника подставится сам)");
         return 1;
      }
   }

   private void saveMessagesConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
   }

   private int clearTotemMessages() {
      int removedCount = this.getKillMessage().к().size();
      this.getKillMessage().й6();
      this.saveMessagesConfig();
      ClientChat.З("Очистил pt-сообщения. Удалено: " + removedCount);
      return 1;
   }
}
