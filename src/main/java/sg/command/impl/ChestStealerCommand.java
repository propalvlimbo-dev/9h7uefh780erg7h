package sg.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import sg.modules.impl.misc.ChestStealer;
import sg.client.RoseClient;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public final class ChestStealerCommand extends ClientCommand {
   private static final String COMMAND_NAME = "stealer";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_ITEM_ARGUMENT_NAME = "item";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_ITEM_ARGUMENT_NAME = "item";
   private static final String LIST_LITERAL = "list";
   private static final String CLEAR_LITERAL = "clear";
   private static final String EMPTY_ITEM_LIST_MESSAGE = "Кастомный список ChestStealer пуст.";
   private static final String ITEM_LIST_SEPARATOR = ", ";
   private static final String ADD_NAMESPACE_SEPARATOR = ":";
   private static final String REMOVE_NAMESPACE_SEPARATOR = ":";
   private static final String REMOVE_DEFAULT_NAMESPACE = "minecraft";
   private static final String ADD_DEFAULT_NAMESPACE = "minecraft";
   private static final String REMOVE_ITEM_ARGUMENT_KEY = "item";
   private static final String ADD_ITEM_ARGUMENT_KEY = "item";

   private ChestStealer getChestStealer() {
      return RoseClient.getInstance().getServices().getModules().getChestStealer();
   }

   private int addItem(String itemName) {
      Identifier itemId = this.parseItemId(itemName);
      if (itemId != null && BuiltInRegistries.ITEM.containsKey(itemId)) {
         ChestStealer chestStealer = this.getChestStealer();
         if (!chestStealer.method_1(itemId)) {
            ClientChat.З("Предмет " + itemId + " уже есть в списке ChestStealer.");
            return 1;
         } else {
            this.saveItemsConfig();
            ClientChat.З("Добавил в ChestStealer: " + itemId);
            return 1;
         }
      } else {
         ClientChat.З("Предмет " + itemName + " не найден.");
         return 1;
      }
   }

   private CompletableFuture<Suggestions> suggestSelectedItems(SuggestionsBuilder suggestionsBuilder) {
      String prefix = suggestionsBuilder.getRemaining().trim().toLowerCase(Locale.ROOT);
      boolean hasNamespace = prefix.contains(REMOVE_NAMESPACE_SEPARATOR);
      this.getChestStealer().П_2571477d().stream().sorted(Comparator.comparing(Identifier::toString)).forEach(identifier -> {
         String identifierText = identifier.toString();
         if (identifierText.startsWith(prefix)) {
            suggestionsBuilder.suggest(identifierText);
         }

         if (!hasNamespace && identifier.getNamespace().equals(REMOVE_DEFAULT_NAMESPACE)) {
            String identifierPath = identifier.getPath();
            if (identifierPath.startsWith(prefix)) {
               suggestionsBuilder.suggest(identifierPath);
            }
         }
      });
      return suggestionsBuilder.buildFuture();
   }

   private Identifier parseItemId(String itemName) {
      return itemName != null && !itemName.isBlank() ? Identifier.tryParse(itemName.trim().toLowerCase(Locale.ROOT)) : null;
   }

   private CompletableFuture<Suggestions> suggestItems(SuggestionsBuilder suggestionsBuilder) {
      String prefix = suggestionsBuilder.getRemaining().trim().toLowerCase(Locale.ROOT);
      boolean hasNamespace = prefix.contains(ADD_NAMESPACE_SEPARATOR);
      BuiltInRegistries.ITEM.keySet().stream().sorted(Comparator.comparing(Identifier::toString)).forEach(identifier -> {
         String identifierText = identifier.toString();
         if (identifierText.startsWith(prefix)) {
            suggestionsBuilder.suggest(identifierText);
         }

         if (!hasNamespace && identifier.getNamespace().equals(ADD_DEFAULT_NAMESPACE)) {
            String identifierPath = identifier.getPath();
            if (identifierPath.startsWith(prefix)) {
               suggestionsBuilder.suggest(identifierPath);
            }
         }
      });
      return suggestionsBuilder.buildFuture();
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      ((LiteralArgumentBuilder)commandBuilder.executes(commandContext -> this.listItems()))
         .then(
            this.literal(ADD_LITERAL)
               .then(
                  this.argument(ADD_ITEM_ARGUMENT_NAME, StringArgumentType.word())
                     .suggests((commandContext, suggestionsBuilder) -> this.suggestItems(suggestionsBuilder))
                     .executes(commandContext -> this.addItem(StringArgumentType.getString(commandContext, ADD_ITEM_ARGUMENT_KEY)))
               )
         )
         .then(
            this.literal(REMOVE_LITERAL)
               .then(
                  this.argument(REMOVE_ITEM_ARGUMENT_NAME, StringArgumentType.word())
                     .suggests((commandContext, suggestionsBuilder) -> this.suggestSelectedItems(suggestionsBuilder))
                     .executes(commandContext -> this.removeItem(StringArgumentType.getString(commandContext, REMOVE_ITEM_ARGUMENT_KEY)))
               )
         )
         .then(this.literal(LIST_LITERAL).executes(commandContext -> this.listItems()))
         .then(this.literal(CLEAR_LITERAL).executes(commandContext -> this.clearItems()));
   }

   private int removeItem(String itemName) {
      Identifier itemId = this.parseItemId(itemName);
      if (itemId != null && this.getChestStealer().Я(itemId)) {
         this.saveItemsConfig();
         ClientChat.З("Удалил из ChestStealer: " + itemId);
         return 1;
      } else {
         ClientChat.З("Предмет " + itemName + " отсутствует в списке ChestStealer.");
         return 1;
      }
   }

   private int listItems() {
      List<Identifier> itemIds = this.getChestStealer().П_2571477d().stream().sorted(Comparator.comparing(Identifier::toString)).toList();
      if (itemIds.isEmpty()) {
         ClientChat.З(EMPTY_ITEM_LIST_MESSAGE);
         return 1;
      } else {
         ClientChat.З("ChestStealer: " + String.join(ITEM_LIST_SEPARATOR, itemIds.stream().map(Identifier::toString).toList()));
         return 1;
      }
   }

   private int clearItems() {
      int removedCount = this.getChestStealer().П_2571477d().size();
      this.getChestStealer().method_4П();
      this.saveItemsConfig();
      ClientChat.З("Очищен список ChestStealer. Удалено: " + removedCount);
      return 1;
   }

   private void saveItemsConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getChestStealerConfig().save();
   }

   public ChestStealerCommand() {
      super(COMMAND_NAME);
   }
}
