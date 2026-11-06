package sg.command.impl;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sg.modules.impl.render.FakePlayer;
import sg.client.RoseClient;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public final class FakePlayerCommand extends ClientCommand {
   private static final FakePlayerCommand.TokenArgument tokenArgument = new FakePlayerCommand.TokenArgument();
   private static final Map<String, EquipmentSlot> equipmentSlots = Map.of(
      FakePlayerCommand.SLOT_NAME_HEAD,
      EquipmentSlot.HEAD,
      FakePlayerCommand.SLOT_NAME_CHEST,
      EquipmentSlot.CHEST,
      FakePlayerCommand.SLOT_NAME_LEGS,
      EquipmentSlot.LEGS,
      FakePlayerCommand.SLOT_NAME_FEET,
      EquipmentSlot.FEET,
      FakePlayerCommand.SLOT_NAME_MAINHAND,
      EquipmentSlot.MAINHAND,
      FakePlayerCommand.SLOT_NAME_OFFHAND,
      EquipmentSlot.OFFHAND
   );
   private static final String COMMAND_NAME = "fakeplayer";
   private static final String COMMAND_ALIAS_FAKE = "fake";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_NAME_ARGUMENT_NAME = "name";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_NAME_ARGUMENT_NAME = "name";
   private static final String EQUIP_LITERAL = "equip";
   private static final String EQUIP_NAME_ARGUMENT_NAME = "name";
   private static final String EQUIP_SLOT_ARGUMENT_NAME = "slot";
   private static final String EQUIP_ITEM_ARGUMENT_NAME = "item";
   private static final String EQUIP_COUNT_ARGUMENT_NAME = "count";
   private static final String GIVE_LITERAL = "give";
   private static final String GIVE_NAME_ARGUMENT_NAME = "name";
   private static final String GIVE_ITEM_ARGUMENT_NAME = "item";
   private static final String GIVE_COUNT_ARGUMENT_NAME = "count";
   private static final String COPY_LITERAL = "copy";
   private static final String COPY_NAME_ARGUMENT_NAME = "name";
   private static final String LIST_LITERAL = "list";
   private static final String CLEAR_LITERAL = "clear";
   private static final String ADD_FAKE_PLAYER_FAILED_MESSAGE = "Не удалось добавить FakePlayer: имя занято или мир не загружен.";
   private static final String GIVE_ITEM_FAILED_MESSAGE = "Не удалось выдать предмет: игрок не найден или инвентарь заполнен.";
   private static final String EMPTY_FAKE_PLAYER_LIST_MESSAGE = "Список FakePlayer пуст.";
   private static final String PLAYER_NAME_SEPARATOR = ", ";
   private static final String PARSE_ITEM_NAMESPACE_SEPARATOR = ":";
   private static final String SUGGEST_ITEM_NAMESPACE_SEPARATOR = ":";
   private static final String DEFAULT_NAMESPACE = "minecraft";
   private static final String COPY_PLAYER_NAME_KEY = "name";
   private static final String GIVE_COUNT_PLAYER_NAME_KEY = "name";
   private static final String GIVE_COUNT_ITEM_KEY = "item";
   private static final String GIVE_COUNT_KEY = "count";
   private static final String GIVE_PLAYER_NAME_KEY = "name";
   private static final String GIVE_ITEM_KEY = "item";
   private static final String EQUIP_COUNT_PLAYER_NAME_KEY = "name";
   private static final String EQUIP_COUNT_SLOT_KEY = "slot";
   private static final String EQUIP_COUNT_ITEM_KEY = "item";
   private static final String EQUIP_COUNT_KEY = "count";
   private static final String EQUIP_PLAYER_NAME_KEY = "name";
   private static final String EQUIP_SLOT_KEY = "slot";
   private static final String EQUIP_ITEM_KEY = "item";
   private static final String REMOVE_PLAYER_NAME_KEY = "name";
   private static final String ADD_PLAYER_NAME_KEY = "name";
   private static final String SLOT_NAME_HEAD = "head";
   private static final String SLOT_NAME_CHEST = "chest";
   private static final String SLOT_NAME_LEGS = "legs";
   private static final String SLOT_NAME_FEET = "feet";
   private static final String SLOT_NAME_MAINHAND = "mainhand";
   private static final String SLOT_NAME_OFFHAND = "offhand";

   private int clearFakePlayers() {
      int removedCount = this.getFakePlayer().МЕ();
      ClientChat.З("Удалено FakePlayer: " + removedCount);
      return 1;
   }

   public FakePlayerCommand() {
      super(COMMAND_NAME, COMMAND_ALIAS_FAKE);
   }

   private int copyEquipmentToPlayer(String playerName) {
      if (!this.getFakePlayer().С(playerName)) {
         ClientChat.З("FakePlayer не найден: " + playerName);
         return 1;
      } else {
         ClientChat.З("Экипировка и инвентарь скопированы на " + playerName);
         return 1;
      }
   }

   private int listFakePlayers() {
      List playerNames = this.getFakePlayer().С();
      if (playerNames.isEmpty()) {
         ClientChat.З(EMPTY_FAKE_PLAYER_LIST_MESSAGE);
         return 1;
      } else {
         ClientChat.З("FakePlayer: " + String.join(PLAYER_NAME_SEPARATOR, playerNames));
         return 1;
      }
   }

   private int giveItem(String playerName, String itemName, int count) {
      ItemStack stack = this.createItemStack(itemName, count);
      if (stack == null) {
         return 1;
      } else if (!this.getFakePlayer().х(playerName, stack)) {
         ClientChat.З(GIVE_ITEM_FAILED_MESSAGE);
         return 1;
      } else {
         ClientChat.З("Выдано " + playerName + ": " + BuiltInRegistries.ITEM.getKey(stack.getItem()) + " x" + stack.getCount());
         return 1;
      }
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      ((LiteralArgumentBuilder)commandBuilder.executes(commandContext -> this.listFakePlayers()))
         .then(
            this.literal(ADD_LITERAL)
               .executes(commandContext -> this.addFakePlayer((String)null))
               .then(this.argument(ADD_NAME_ARGUMENT_NAME, StringArgumentType.word()).executes(commandContext -> this.addFakePlayer(StringArgumentType.getString(commandContext, ADD_PLAYER_NAME_KEY))))
         )
         .then(
            this.literal(REMOVE_LITERAL)
               .then(
                  this.argument(REMOVE_NAME_ARGUMENT_NAME, StringArgumentType.word())
                     .suggests((commandContext, suggestionsBuilder) -> this.suggestFakePlayerNames(suggestionsBuilder))
                     .executes(commandContext -> this.removeFakePlayer(StringArgumentType.getString(commandContext, REMOVE_PLAYER_NAME_KEY)))
               )
         )
         .then(
            this.literal(EQUIP_LITERAL)
               .then(
                  this.argument(EQUIP_NAME_ARGUMENT_NAME, StringArgumentType.word())
                     .suggests((commandContext, suggestionsBuilder) -> this.suggestFakePlayerNames(suggestionsBuilder))
                     .then(
                        this.argument(EQUIP_SLOT_ARGUMENT_NAME, StringArgumentType.word())
                           .suggests((commandContext, suggestionsBuilder) -> this.suggestEquipmentSlots(suggestionsBuilder))
                           .then(
                              this.argument(EQUIP_ITEM_ARGUMENT_NAME, tokenArgument)
                                 .suggests((commandContext, suggestionsBuilder) -> this.suggestItems(suggestionsBuilder))
                                 .executes(
                                    commandContext -> this.equipItem(
                                       StringArgumentType.getString(commandContext, EQUIP_PLAYER_NAME_KEY),
                                       StringArgumentType.getString(commandContext, EQUIP_SLOT_KEY),
                                       commandContext.getArgument(EQUIP_ITEM_KEY, String.class),
                                       1
                                    )
                                 )
                                 .then(
                                    this.argument(EQUIP_COUNT_ARGUMENT_NAME, IntegerArgumentType.integer(1, 99))
                                       .executes(
                                          commandContext -> this.equipItem(
                                             StringArgumentType.getString(commandContext, EQUIP_COUNT_PLAYER_NAME_KEY),
                                             StringArgumentType.getString(commandContext, EQUIP_COUNT_SLOT_KEY),
                                             commandContext.getArgument(EQUIP_COUNT_ITEM_KEY, String.class),
                                             IntegerArgumentType.getInteger(commandContext, EQUIP_COUNT_KEY)
                                          )
                                       )
                                 )
                           )
                     )
               )
         )
         .then(
            this.literal(GIVE_LITERAL)
               .then(
                  this.argument(GIVE_NAME_ARGUMENT_NAME, StringArgumentType.word())
                     .suggests((commandContext, suggestionsBuilder) -> this.suggestFakePlayerNames(suggestionsBuilder))
                     .then(
                        this.argument(GIVE_ITEM_ARGUMENT_NAME, tokenArgument)
                           .suggests((commandContext, suggestionsBuilder) -> this.suggestItems(suggestionsBuilder))
                           .executes(commandContext -> this.giveItem(StringArgumentType.getString(commandContext, GIVE_PLAYER_NAME_KEY), commandContext.getArgument(GIVE_ITEM_KEY, String.class), 1))
                           .then(
                              this.argument(GIVE_COUNT_ARGUMENT_NAME, IntegerArgumentType.integer(1, 99))
                                 .executes(
                                    commandContext -> this.giveItem(
                                       StringArgumentType.getString(commandContext, GIVE_COUNT_PLAYER_NAME_KEY),
                                       commandContext.getArgument(GIVE_COUNT_ITEM_KEY, String.class),
                                       IntegerArgumentType.getInteger(commandContext, GIVE_COUNT_KEY)
                                    )
                                 )
                           )
                     )
               )
         )
         .then(
            this.literal(COPY_LITERAL)
               .then(
                  this.argument(COPY_NAME_ARGUMENT_NAME, StringArgumentType.word())
                     .suggests((commandContext, suggestionsBuilder) -> this.suggestFakePlayerNames(suggestionsBuilder))
                     .executes(commandContext -> this.copyEquipmentToPlayer(StringArgumentType.getString(commandContext, COPY_PLAYER_NAME_KEY)))
               )
         )
         .then(this.literal(LIST_LITERAL).executes(commandContext -> this.listFakePlayers()))
         .then(this.literal(CLEAR_LITERAL).executes(commandContext -> this.clearFakePlayers()));
   }

   private CompletableFuture<Suggestions> suggestFakePlayerNames(SuggestionsBuilder suggestionsBuilder) {
      String prefix = suggestionsBuilder.getRemaining().toLowerCase(Locale.ROOT);
      this.getFakePlayer()
         .С()
         .stream()
         .sorted(String.CASE_INSENSITIVE_ORDER)
         .filter(suggestionValue -> suggestionValue.toLowerCase(Locale.ROOT).startsWith(prefix))
         .forEach(suggestionsBuilder::suggest);
      return suggestionsBuilder.buildFuture();
   }

   private ItemStack createItemStack(String itemName, int count) {
      Identifier itemId = this.parseItemId(itemName);
      if (itemId != null && BuiltInRegistries.ITEM.containsKey(itemId)) {
         Item item = BuiltInRegistries.ITEM.getValue(itemId);
         ItemStack stack = new ItemStack(item);
         stack.setCount(Math.clamp(count, 1, stack.getMaxStackSize()));
         return stack;
      } else {
         ClientChat.З("Предмет не найден: " + itemName);
         return null;
      }
   }

   private FakePlayer getFakePlayer() {
      return RoseClient.getInstance().getServices().getModules().getFakePlayer();
   }

   private Identifier parseItemId(String itemName) {
      if (itemName != null && !itemName.isBlank()) {
         String normalizedItemName = itemName.trim().toLowerCase(Locale.ROOT);
         if (!normalizedItemName.contains(PARSE_ITEM_NAMESPACE_SEPARATOR)) {
            normalizedItemName = "minecraft:" + normalizedItemName;
         }

         return Identifier.tryParse(normalizedItemName);
      } else {
         return null;
      }
   }

   private CompletableFuture<Suggestions> suggestEquipmentSlots(SuggestionsBuilder suggestionsBuilder) {
      String prefix = suggestionsBuilder.getRemaining().toLowerCase(Locale.ROOT);
      equipmentSlots.keySet().stream().sorted().filter(suggestionValue -> suggestionValue.startsWith(prefix)).forEach(suggestionsBuilder::suggest);
      return suggestionsBuilder.buildFuture();
   }

   private CompletableFuture<Suggestions> suggestItems(SuggestionsBuilder suggestionsBuilder) {
      String prefix = suggestionsBuilder.getRemaining().toLowerCase(Locale.ROOT);
      boolean hasNamespace = prefix.contains(SUGGEST_ITEM_NAMESPACE_SEPARATOR);
      BuiltInRegistries.ITEM.keySet().stream().sorted(Comparator.comparing(Identifier::toString)).forEach(identifier -> {
         String identifierText = identifier.toString();
         if (identifierText.startsWith(prefix)) {
            suggestionsBuilder.suggest(identifierText);
         }

         if (!hasNamespace && identifier.getNamespace().equals(DEFAULT_NAMESPACE) && identifier.getPath().startsWith(prefix)) {
            suggestionsBuilder.suggest(identifier.getPath());
         }
      });
      return suggestionsBuilder.buildFuture();
   }

   private int equipItem(String playerName, String slotName, String itemName, int count) {
      EquipmentSlot slot = equipmentSlots.get(slotName.toLowerCase(Locale.ROOT));
      if (slot == null) {
         ClientChat.З("Неизвестный слот: " + slotName);
         return 1;
      } else {
         ItemStack stack = this.createItemStack(itemName, count);
         if (stack == null) {
            return 1;
         } else if (!this.getFakePlayer().х(playerName, slot, stack)) {
            ClientChat.З("FakePlayer не найден: " + playerName);
            return 1;
         } else {
            ClientChat.З("FakePlayer " + playerName + ": " + slotName + " = " + BuiltInRegistries.ITEM.getKey(stack.getItem()) + " x" + stack.getCount());
            return 1;
         }
      }
   }

   private int removeFakePlayer(String playerName) {
      if (!this.getFakePlayer().е(playerName)) {
         ClientChat.З("FakePlayer не найден: " + playerName);
         return 1;
      } else {
         ClientChat.З("Удалён FakePlayer: " + playerName);
         return 1;
      }
   }

   private int addFakePlayer(String playerName) {
      RemotePlayer fakePlayer = this.getFakePlayer().х(playerName);
      if (fakePlayer == null) {
         ClientChat.З(ADD_FAKE_PLAYER_FAILED_MESSAGE);
         return 1;
      } else {
         ClientChat.З("Добавлен FakePlayer: " + fakePlayer.getGameProfile().name());
         return 1;
      }
   }

   private static final class TokenArgument implements ArgumentType<String> {
      private static final SimpleCommandExceptionType EXPECTED_ITEM_ERROR = new SimpleCommandExceptionType(Component.literal(FakePlayerCommand.TokenArgument.EXPECTED_ITEM_MESSAGE));
      private static final String EXPECTED_ITEM_MESSAGE = "Ожидался предмет";

      public String parse(StringReader reader) throws CommandSyntaxException {
         int startCursor = reader.getCursor();

         while (reader.canRead() && !Character.isWhitespace(reader.peek())) {
            reader.skip();
         }

         if (reader.getCursor() == startCursor) {
            throw EXPECTED_ITEM_ERROR.createWithContext(reader);
         } else {
            return reader.getString().substring(startCursor, reader.getCursor());
         }
      }
   }
}
