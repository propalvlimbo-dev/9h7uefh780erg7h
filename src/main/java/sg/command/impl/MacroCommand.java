package sg.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.ec.KeyNameRegistry;
import sg.events.EventListener;
import sg.events.impl.ChatOpenedEvent;
import sg.client.RoseClient;
import sg.network.ClientChat;
import sg.command.ClientCommand;
import sg.command.KeyMacro;
import sg.command.argument.ListedStringArgumentType;

public class MacroCommand extends ClientCommand {
   private final @com.google.gson.annotations.SerializedName("В")  ListedStringArgumentType availableKeysArgument = new ListedStringArgumentType();
   private final @com.google.gson.annotations.SerializedName("Я")  ListedStringArgumentType macroKeysArgument = new ListedStringArgumentType();
   private final @com.google.gson.annotations.SerializedName("сВ")  EventListener<ChatOpenedEvent> chatOpenedListener = this::refreshKeySuggestions;
   private static final String COMMAND_NAME = "macro";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_KEY_ARGUMENT_NAME = "key";
   private static final String ADD_COMMAND_ARGUMENT_NAME = "command";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_KEY_ARGUMENT_NAME = "key";
   private static final String CLEAR_LITERAL = "clear";
   private static final String MACROS_CLEARED_MESSAGE = "Очистил список макросов.";
   private static final String REMOVE_KEY_ARGUMENT_KEY = "key";
   private static final String ADD_KEY_ARGUMENT_KEY = "key";
   private static final String ADD_COMMAND_ARGUMENT_KEY = "command";

   public MacroCommand() {
      super(COMMAND_NAME);
   }

   public void refreshKeySuggestions(ChatOpenedEvent event) {
      this.availableKeysArgument.setList(KeyNameRegistry.сЦ.keySet().stream().toList());
      this.macroKeysArgument.setList(RoseClient.getInstance().getServices().getCommandShortcuts().А());
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(this.literal(ADD_LITERAL).then(this.argument(ADD_KEY_ARGUMENT_NAME, this.availableKeysArgument).then(this.argument(ADD_COMMAND_ARGUMENT_NAME, StringArgumentType.greedyString()).executes(commandContext -> {
         String keyName = commandContext.getArgument(ADD_KEY_ARGUMENT_KEY, String.class);
         String commandText = commandContext.getArgument(ADD_COMMAND_ARGUMENT_KEY, String.class);
         int keyCode = KeyNameRegistry.сЦ.get(keyName);
         RoseClient.getInstance().getServices().getCommandShortcuts().add(new KeyMacro(commandText, keyCode));
         ClientChat.З("Добавил в список макросов новый макро с клавишей: " + keyName + " и командой: " + commandText);
         return 1;
      })))).then(this.literal(REMOVE_LITERAL).then(this.argument(REMOVE_KEY_ARGUMENT_NAME, this.macroKeysArgument).executes(commandContext -> {
         String keyName = commandContext.getArgument(REMOVE_KEY_ARGUMENT_KEY, String.class);
         int keyCode = KeyNameRegistry.сЦ.get(keyName);
         RoseClient.getInstance().getServices().getCommandShortcuts().removeIf(macro -> macro.О() == keyCode);
         ClientChat.З("Удалил из списка макросов макро с клавишей: " + keyName);
         return 1;
      }))).then(this.literal(CLEAR_LITERAL).executes(commandContext -> {
         RoseClient.getInstance().getServices().getCommandShortcuts().clear();
         ClientChat.З(MACROS_CLEARED_MESSAGE);
         return 1;
      }));
   }
}
