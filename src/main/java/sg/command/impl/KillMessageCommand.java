package sg.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.client.RoseClient;
import sg.modules.impl.misc.KillMessage;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public class KillMessageCommand extends ClientCommand {
   private static final String COMMAND_NAME = "kill";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_TEXT_ARGUMENT_NAME = "text";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_INDEX_ARGUMENT_NAME = "index";
   private static final String LIST_LITERAL = "list";
   private static final String CLEAR_LITERAL = "clear";
   private static final String MESSAGE_REJECTED_MESSAGE = "Сообщение уже есть или пустое.";
   private static final String EMPTY_KILL_MESSAGE_LIST_MESSAGE = "Список kill-сообщений пуст. Добавь: .kill add <текст>";
   private static final String KILL_MESSAGE_LIST_HEADER = "Kill-сообщения (ник + текст):";
   private static final String LIST_NEWLINE = "\n";
   private static final String LIST_NUMBER_SEPARATOR = ". ";
   private static final String REMOVE_INDEX_ARGUMENT_KEY = "index";
   private static final String ADD_TEXT_ARGUMENT_KEY = "text";

   private int addKillMessage(String message) {
      KillMessage killMessage = this.getKillMessage();
      if (!killMessage.method_5(message)) {
         ClientChat.З(MESSAGE_REJECTED_MESSAGE);
         return 1;
      } else {
         this.saveMessagesConfig();
         ClientChat.З("Добавил kill-сообщение: " + message.trim() + " (ник противника подставится сам)");
         return 1;
      }
   }

   private int removeKillMessage(int messageNumber) {
      KillMessage killMessage = this.getKillMessage();
      if (!killMessage.method_8(messageNumber - 1)) {
         ClientChat.З("Сообщение #" + messageNumber + " не найдено. Всего: " + killMessage.ч_2571477d().size());
         return 1;
      } else {
         this.saveMessagesConfig();
         ClientChat.З("Удалил kill-сообщение #" + messageNumber);
         return 1;
      }
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      ((LiteralArgumentBuilder)commandBuilder.executes(commandContext -> this.listKillMessages()))
         .then(this.literal(ADD_LITERAL).then(this.argument(ADD_TEXT_ARGUMENT_NAME, StringArgumentType.greedyString()).executes(commandContext -> this.addKillMessage(StringArgumentType.getString(commandContext, ADD_TEXT_ARGUMENT_KEY)))))
         .then(this.literal(REMOVE_LITERAL).then(this.argument(REMOVE_INDEX_ARGUMENT_NAME, IntegerArgumentType.integer(1)).executes(commandContext -> this.removeKillMessage(IntegerArgumentType.getInteger(commandContext, REMOVE_INDEX_ARGUMENT_KEY)))))
         .then(this.literal(LIST_LITERAL).executes(commandContext -> this.listKillMessages()))
         .then(this.literal(CLEAR_LITERAL).executes(commandContext -> this.clearKillMessages()));
   }

   private int listKillMessages() {
      KillMessage killMessage = this.getKillMessage();
      if (killMessage.ч_2571477d().isEmpty()) {
         ClientChat.З(EMPTY_KILL_MESSAGE_LIST_MESSAGE);
         return 1;
      }

      StringBuilder messageList = new StringBuilder(KILL_MESSAGE_LIST_HEADER);
      int messageNumber = 1;

      for (String message : killMessage.ч_2571477d()) {
         messageList.append(LIST_NEWLINE).append(messageNumber++).append(LIST_NUMBER_SEPARATOR).append(message);
      }

      ClientChat.З(messageList.toString());
      return 1;
   }

   private int clearKillMessages() {
      int removedCount = this.getKillMessage().ч_2571477d().size();
      this.getKillMessage().й4();
      this.saveMessagesConfig();
      ClientChat.З("Очистил kill-сообщения. Удалено: " + removedCount);
      return 1;
   }

   private void saveMessagesConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
   }

   private KillMessage getKillMessage() {
      return RoseClient.getInstance().getServices().getModules().getKillMessage();
   }

   public KillMessageCommand() {
      super(COMMAND_NAME);
   }
}
