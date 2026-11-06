package sg.command.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.modules.impl.misc.AutoWarden;
import sg.client.RoseClient;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public final class AutoWardenCommand extends ClientCommand {
   private static final String COMMAND_NAME = "warden";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_ANARCHY_ARGUMENT_NAME = "анархия";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_ANARCHY_ARGUMENT_NAME = "анархия";
   private static final String LIST_LITERAL = "list";
   private static final String CLEAR_LITERAL = "clear";
   private static final String ANARCHY_LIMIT_MESSAGE = "Можно добавить максимум 10 анархий.";
   private static final String EMPTY_ANARCHY_LIST_MESSAGE = "Список анархий пуст.";
   private static final String ANARCHIES_CLEARED_MESSAGE = "Список анархий очищен.";
   private static final String REMOVE_ANARCHY_ARGUMENT_KEY = "анархия";
   private static final String ADD_ANARCHY_ARGUMENT_KEY = "анархия";

   private AutoWarden getAutoWarden() {
      return RoseClient.getInstance().getServices().getModules().getAutoWarden();
   }

   private int clearAnarchies() {
      this.getAutoWarden().т().clear();
      this.saveAutoWardenConfig();
      ClientChat.З(ANARCHIES_CLEARED_MESSAGE);
      return 1;
   }

   private int removeAnarchy(int anarchyNumber) {
      if (!this.getAutoWarden().т().remove(Integer.valueOf(anarchyNumber))) {
         ClientChat.З("Анархия " + anarchyNumber + " не найдена.");
         return 1;
      } else {
         this.saveAutoWardenConfig();
         ClientChat.З("Анархия " + anarchyNumber + " удалена.");
         return 1;
      }
   }

   private int listAnarchies() {
      List anarchyNumbers = this.getAutoWarden().т();
      if (anarchyNumbers.isEmpty()) {
         ClientChat.З(EMPTY_ANARCHY_LIST_MESSAGE);
         return 1;
      } else {
         ClientChat.З("Анархии (" + anarchyNumbers.size() + "): " + anarchyNumbers);
         return 1;
      }
   }

   public AutoWardenCommand() {
      super(COMMAND_NAME);
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(this.literal(ADD_LITERAL).then(this.argument(ADD_ANARCHY_ARGUMENT_NAME, IntegerArgumentType.integer(1, 999)).executes(commandContext -> this.addAnarchy(IntegerArgumentType.getInteger(commandContext, ADD_ANARCHY_ARGUMENT_KEY)))))
         .then(this.literal(REMOVE_LITERAL).then(this.argument(REMOVE_ANARCHY_ARGUMENT_NAME, IntegerArgumentType.integer(1, 999)).executes(commandContext -> this.removeAnarchy(IntegerArgumentType.getInteger(commandContext, REMOVE_ANARCHY_ARGUMENT_KEY)))))
         .then(this.literal(LIST_LITERAL).executes(commandContext -> this.listAnarchies()))
         .then(this.literal(CLEAR_LITERAL).executes(commandContext -> this.clearAnarchies()));
   }

   private int addAnarchy(int anarchyNumber) {
      List anarchyNumbers = this.getAutoWarden().т();
      if (anarchyNumbers.contains(anarchyNumber)) {
         ClientChat.З("Анархия " + anarchyNumber + " уже в списке.");
         return 1;
      } else if (anarchyNumbers.size() >= 10) {
         ClientChat.З(ANARCHY_LIMIT_MESSAGE);
         return 1;
      } else {
         anarchyNumbers.add(anarchyNumber);
         this.saveAutoWardenConfig();
         ClientChat.З("Анархия " + anarchyNumber + " добавлена.");
         return 1;
      }
   }

   private void saveAutoWardenConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
   }
}
