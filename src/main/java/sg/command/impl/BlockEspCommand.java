package sg.command.impl;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
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
import java.util.stream.Stream;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import sg.client.RoseClient;
import sg.modules.impl.init.BlockEspTargets;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public final class BlockEspCommand extends ClientCommand {
   private static final BlockEspCommand.TokenArgument tokenArgument = new BlockEspCommand.TokenArgument();
   private static final Map<String, Integer> namedColors = Map.ofEntries(
      Map.entry(BlockEspCommand.COLOR_NAME_WHITE, BlockEspCommand.COLOR_RGB_WHITE),
      Map.entry(BlockEspCommand.COLOR_NAME_RED, BlockEspCommand.COLOR_RGB_RED),
      Map.entry(BlockEspCommand.COLOR_NAME_BLUE, 255),
      Map.entry(BlockEspCommand.COLOR_NAME_GREEN, BlockEspCommand.COLOR_RGB_GREEN),
      Map.entry(BlockEspCommand.COLOR_NAME_YELLOW, BlockEspCommand.COLOR_RGB_YELLOW),
      Map.entry(BlockEspCommand.COLOR_NAME_CYAN, BlockEspCommand.COLOR_RGB_CYAN),
      Map.entry(BlockEspCommand.COLOR_NAME_MAGENTA, BlockEspCommand.COLOR_RGB_MAGENTA),
      Map.entry(BlockEspCommand.COLOR_NAME_BLACK, 0),
      Map.entry(BlockEspCommand.COLOR_NAME_GRAY, BlockEspCommand.COLOR_RGB_GRAY),
      Map.entry(BlockEspCommand.COLOR_NAME_ORANGE, BlockEspCommand.COLOR_RGB_ORANGE),
      Map.entry(BlockEspCommand.COLOR_NAME_PURPLE, BlockEspCommand.COLOR_RGB_PURPLE),
      Map.entry(BlockEspCommand.COLOR_NAME_PINK, BlockEspCommand.COLOR_RGB_PINK),
      Map.entry(BlockEspCommand.COLOR_NAME_WHITE_RU, BlockEspCommand.COLOR_RGB_WHITE_RU),
      Map.entry(BlockEspCommand.COLOR_NAME_RED_RU, BlockEspCommand.COLOR_RGB_RED_RU),
      Map.entry(BlockEspCommand.COLOR_NAME_BLUE_RU, 255),
      Map.entry(BlockEspCommand.COLOR_NAME_GREEN_RU_YO, BlockEspCommand.COLOR_RGB_GREEN_RU_YO),
      Map.entry(BlockEspCommand.COLOR_NAME_GREEN_RU, BlockEspCommand.COLOR_RGB_GREEN_RU),
      Map.entry(BlockEspCommand.COLOR_NAME_YELLOW_RU_YO, BlockEspCommand.COLOR_RGB_YELLOW_RU_YO),
      Map.entry(BlockEspCommand.COLOR_NAME_YELLOW_RU, BlockEspCommand.COLOR_RGB_YELLOW_RU),
      Map.entry(BlockEspCommand.COLOR_NAME_CYAN_RU, BlockEspCommand.COLOR_RGB_CYAN_RU),
      Map.entry(BlockEspCommand.COLOR_NAME_PURPLE_RU, BlockEspCommand.COLOR_RGB_PURPLE_RU),
      Map.entry(BlockEspCommand.COLOR_NAME_PINK_RU, BlockEspCommand.COLOR_RGB_PINK_RU),
      Map.entry(BlockEspCommand.COLOR_NAME_BLACK_RU_YO, 0),
      Map.entry(BlockEspCommand.COLOR_NAME_BLACK_RU, 0),
      Map.entry(BlockEspCommand.COLOR_NAME_GRAY_RU, BlockEspCommand.COLOR_RGB_GRAY_RU)
   );
   private static final List<String> shulkerBoxIds = List.of(
      BlockEspCommand.SHULKER_BOX_ID,
      BlockEspCommand.WHITE_SHULKER_BOX_ID,
      BlockEspCommand.ORANGE_SHULKER_BOX_ID,
      BlockEspCommand.MAGENTA_SHULKER_BOX_ID,
      BlockEspCommand.LIGHT_BLUE_SHULKER_BOX_ID,
      BlockEspCommand.YELLOW_SHULKER_BOX_ID,
      BlockEspCommand.LIME_SHULKER_BOX_ID,
      BlockEspCommand.PINK_SHULKER_BOX_ID,
      BlockEspCommand.GRAY_SHULKER_BOX_ID,
      BlockEspCommand.LIGHT_GRAY_SHULKER_BOX_ID,
      BlockEspCommand.CYAN_SHULKER_BOX_ID,
      BlockEspCommand.PURPLE_SHULKER_BOX_ID,
      BlockEspCommand.BLUE_SHULKER_BOX_ID,
      BlockEspCommand.BROWN_SHULKER_BOX_ID,
      BlockEspCommand.GREEN_SHULKER_BOX_ID,
      BlockEspCommand.RED_SHULKER_BOX_ID,
      BlockEspCommand.BLACK_SHULKER_BOX_ID
   );
   private static final String COMMAND_NAME = "blockesp";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_SHULKERS_LITERAL = "shulkers";
   private static final String SHULKERS_COLOR_ARGUMENT_NAME = "color";
   private static final String ADD_TARGET_ARGUMENT_NAME = "target";
   private static final String ADD_COLOR_ARGUMENT_NAME = "color";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_TARGET_ARGUMENT_NAME = "target";
   private static final String LIST_LITERAL = "list";
   private static final String CLEAR_LITERAL = "clear";
   private static final String EMPTY_TARGET_LIST_MESSAGE = "Список BlockESP пуст.";
   private static final String TARGET_LIST_HEADER = "Список BlockESP:";
   private static final String ADD_NAMESPACE_SEPARATOR = ":";
   private static final String REMOVE_NAMESPACE_SEPARATOR = ":";
   private static final String DEFAULT_NAMESPACE = "minecraft";
   private static final String WHITE_HEX_PREFIX = "#ffffff";
   private static final String WHITE_HEX_SUGGESTION = "#FFFFFF";
   private static final String HASH_COLOR_PREFIX = "#";
   private static final String HEX_COLOR_PREFIX = "0x";
   private static final String HEX_COLOR_PATTERN = "[0-9a-f]{6}|[0-9a-f]{8}";
   private static final int PARSE_RGB_MASK = 16777215;
   private static final String RGB_HEX_FORMAT = "#%06X";
   private static final int FORMAT_RGB_MASK = 16777215;
   private static final String REMOVE_TARGET_ARGUMENT_KEY = "target";
   private static final String ADD_TARGET_ARGUMENT_KEY = "target";
   private static final String ADD_COLOR_ARGUMENT_KEY = "color";
   private static final String SHULKERS_COLOR_ARGUMENT_KEY = "color";
   private static final String COLOR_NAME_WHITE = "white";
   private static final int COLOR_RGB_WHITE = 16777215;
   private static final String COLOR_NAME_RED = "red";
   private static final int COLOR_RGB_RED = 16711680;
   private static final String COLOR_NAME_BLUE = "blue";
   private static final String COLOR_NAME_GREEN = "green";
   private static final int COLOR_RGB_GREEN = 65280;
   private static final String COLOR_NAME_YELLOW = "yellow";
   private static final int COLOR_RGB_YELLOW = 16776960;
   private static final String COLOR_NAME_CYAN = "cyan";
   private static final int COLOR_RGB_CYAN = 65535;
   private static final String COLOR_NAME_MAGENTA = "magenta";
   private static final int COLOR_RGB_MAGENTA = 16711935;
   private static final String COLOR_NAME_BLACK = "black";
   private static final String COLOR_NAME_GRAY = "gray";
   private static final int COLOR_RGB_GRAY = 8421504;
   private static final String COLOR_NAME_ORANGE = "orange";
   private static final int COLOR_RGB_ORANGE = 16744448;
   private static final String COLOR_NAME_PURPLE = "purple";
   private static final int COLOR_RGB_PURPLE = 8388863;
   private static final String COLOR_NAME_PINK = "pink";
   private static final int COLOR_RGB_PINK = 16738740;
   private static final String COLOR_NAME_WHITE_RU = "белый";
   private static final int COLOR_RGB_WHITE_RU = 16777215;
   private static final String COLOR_NAME_RED_RU = "красный";
   private static final int COLOR_RGB_RED_RU = 16711680;
   private static final String COLOR_NAME_BLUE_RU = "синий";
   private static final String COLOR_NAME_GREEN_RU_YO = "зелёный";
   private static final int COLOR_RGB_GREEN_RU_YO = 65280;
   private static final String COLOR_NAME_GREEN_RU = "зеленый";
   private static final int COLOR_RGB_GREEN_RU = 65280;
   private static final String COLOR_NAME_YELLOW_RU_YO = "жёлтый";
   private static final int COLOR_RGB_YELLOW_RU_YO = 16776960;
   private static final String COLOR_NAME_YELLOW_RU = "желтый";
   private static final int COLOR_RGB_YELLOW_RU = 16776960;
   private static final String COLOR_NAME_CYAN_RU = "голубой";
   private static final int COLOR_RGB_CYAN_RU = 65535;
   private static final String COLOR_NAME_PURPLE_RU = "фиолетовый";
   private static final int COLOR_RGB_PURPLE_RU = 8388863;
   private static final String COLOR_NAME_PINK_RU = "розовый";
   private static final int COLOR_RGB_PINK_RU = 16738740;
   private static final String COLOR_NAME_BLACK_RU_YO = "чёрный";
   private static final String COLOR_NAME_BLACK_RU = "черный";
   private static final String COLOR_NAME_GRAY_RU = "серый";
   private static final int COLOR_RGB_GRAY_RU = 8421504;
   private static final String SHULKER_BOX_ID = "minecraft:shulker_box";
   private static final String WHITE_SHULKER_BOX_ID = "minecraft:white_shulker_box";
   private static final String ORANGE_SHULKER_BOX_ID = "minecraft:orange_shulker_box";
   private static final String MAGENTA_SHULKER_BOX_ID = "minecraft:magenta_shulker_box";
   private static final String LIGHT_BLUE_SHULKER_BOX_ID = "minecraft:light_blue_shulker_box";
   private static final String YELLOW_SHULKER_BOX_ID = "minecraft:yellow_shulker_box";
   private static final String LIME_SHULKER_BOX_ID = "minecraft:lime_shulker_box";
   private static final String PINK_SHULKER_BOX_ID = "minecraft:pink_shulker_box";
   private static final String GRAY_SHULKER_BOX_ID = "minecraft:gray_shulker_box";
   private static final String LIGHT_GRAY_SHULKER_BOX_ID = "minecraft:light_gray_shulker_box";
   private static final String CYAN_SHULKER_BOX_ID = "minecraft:cyan_shulker_box";
   private static final String PURPLE_SHULKER_BOX_ID = "minecraft:purple_shulker_box";
   private static final String BLUE_SHULKER_BOX_ID = "minecraft:blue_shulker_box";
   private static final String BROWN_SHULKER_BOX_ID = "minecraft:brown_shulker_box";
   private static final String GREEN_SHULKER_BOX_ID = "minecraft:green_shulker_box";
   private static final String RED_SHULKER_BOX_ID = "minecraft:red_shulker_box";
   private static final String BLACK_SHULKER_BOX_ID = "minecraft:black_shulker_box";

   private Integer parseColor(String colorText) {
      if (colorText != null && !colorText.isBlank()) {
         String normalizedColor = colorText.trim().toLowerCase(Locale.ROOT);
         Integer namedColor = namedColors.get(normalizedColor);
         if (namedColor != null) {
            return namedColor;
         }

         if (normalizedColor.startsWith(HASH_COLOR_PREFIX)) {
            normalizedColor = normalizedColor.substring(1);
         }

         if (normalizedColor.startsWith(HEX_COLOR_PREFIX)) {
            normalizedColor = normalizedColor.substring(2);
         }

         if (!normalizedColor.matches(HEX_COLOR_PATTERN)) {
            return null;
         }

         try {
            long parsedColor = Long.parseLong(normalizedColor, 16);
            return (int)parsedColor & PARSE_RGB_MASK;
         } catch (NumberFormatException parseError) {
            return null;
         }
      } else {
         return null;
      }
   }

   private void suggestIdentifier(SuggestionsBuilder suggestionsBuilder, String prefix, boolean hasNamespace, Identifier identifier) {
      String identifierText = identifier.toString();
      if (identifierText.startsWith(prefix)) {
         suggestionsBuilder.suggest(identifierText);
      }

      if (!hasNamespace && identifier.getNamespace().equals(DEFAULT_NAMESPACE)) {
         String identifierPath = identifier.getPath();
         if (identifierPath.startsWith(prefix)) {
            suggestionsBuilder.suggest(identifierPath);
         }
      }
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(
            this.literal(ADD_LITERAL)
               .then(this.literal(ADD_SHULKERS_LITERAL).then(this.argument(SHULKERS_COLOR_ARGUMENT_NAME, tokenArgument).suggests((commandContext, suggestionsBuilder) -> this.suggestColors(suggestionsBuilder)).executes(commandContext -> this.addAllShulkers(commandContext.getArgument(SHULKERS_COLOR_ARGUMENT_KEY, String.class)))))
               .then(
                  this.argument(ADD_TARGET_ARGUMENT_NAME, tokenArgument)
                     .suggests((commandContext, suggestionsBuilder) -> this.suggestTargets(suggestionsBuilder))
                     .then(
                        this.argument(ADD_COLOR_ARGUMENT_NAME, tokenArgument)
                           .suggests((commandContext, suggestionsBuilder) -> this.suggestColors(suggestionsBuilder))
                           .executes(commandContext -> this.addTarget(commandContext.getArgument(ADD_TARGET_ARGUMENT_KEY, String.class), commandContext.getArgument(ADD_COLOR_ARGUMENT_KEY, String.class)))
                     )
               )
         )
         .then(this.literal(REMOVE_LITERAL).then(this.argument(REMOVE_TARGET_ARGUMENT_NAME, tokenArgument).suggests((commandContext, suggestionsBuilder) -> this.suggestSelectedTargets(suggestionsBuilder)).executes(commandContext -> this.removeTarget(commandContext.getArgument(REMOVE_TARGET_ARGUMENT_KEY, String.class)))))
         .then(this.literal(LIST_LITERAL).executes(commandContext -> this.listTargets()))
         .then(this.literal(CLEAR_LITERAL).executes(commandContext -> this.clearTargets()));
   }

   private CompletableFuture<Suggestions> suggestTargets(SuggestionsBuilder suggestionsBuilder) {
      String prefix = suggestionsBuilder.getRemaining().toLowerCase(Locale.ROOT);
      boolean hasNamespace = prefix.contains(ADD_NAMESPACE_SEPARATOR);
      Stream.concat(BuiltInRegistries.BLOCK.keySet().stream(), BuiltInRegistries.ENTITY_TYPE.keySet().stream())
         .distinct()
         .sorted(Comparator.comparing(Identifier::toString))
         .forEach(identifier -> this.suggestIdentifier(suggestionsBuilder, prefix, hasNamespace, identifier));
      return suggestionsBuilder.buildFuture();
   }

   private String formatColor(int rgb) {
      return String.format(RGB_HEX_FORMAT, rgb & FORMAT_RGB_MASK);
   }

   private BlockEspTargets getTargets() {
      return RoseClient.getInstance().getServices().getBlockEspTargets();
   }

   private void saveTargetsConfig() {
      RoseClient.getInstance().getServices().getConfigManager().getBlockEspConfig().save();
   }

   private int clearTargets() {
      int removedCount = this.getTargets().РД();
      this.saveTargetsConfig();
      ClientChat.З("Список BlockESP очищен. Удалено: " + removedCount);
      return 1;
   }

   private int removeTarget(String targetName) {
      String normalizedTarget = BlockEspTargets.То_388008bb(targetName);
      if (normalizedTarget != null && this.getTargets().То_bbeebd87(normalizedTarget)) {
         this.saveTargetsConfig();
         ClientChat.З("BlockESP: удалён " + normalizedTarget);
         return 1;
      } else {
         ClientChat.З("Запись BlockESP не найдена: " + targetName);
         return 1;
      }
   }

   private int addTarget(String targetName, String colorText) {
      String normalizedTarget = BlockEspTargets.То_388008bb(targetName);
      Identifier targetId = normalizedTarget == null ? null : Identifier.tryParse(normalizedTarget);
      if (targetId != null && (BuiltInRegistries.BLOCK.containsKey(targetId) || BuiltInRegistries.ENTITY_TYPE.containsKey(targetId))) {
         Integer color = this.parseColor(colorText);
         if (color == null) {
            ClientChat.З("Неизвестный цвет: " + colorText + ". Используй название или #RRGGBB.");
            return 1;
         } else {
            this.getTargets().O(normalizedTarget, color);
            this.saveTargetsConfig();
            ClientChat.З("BlockESP: добавлен " + normalizedTarget + " с цветом " + this.formatColor(color));
            return 1;
         }
      } else {
         ClientChat.З("Блок или сущность " + targetName + " не найдены.");
         return 1;
      }
   }

   private CompletableFuture<Suggestions> suggestColors(SuggestionsBuilder suggestionsBuilder) {
      String prefix = suggestionsBuilder.getRemaining().toLowerCase(Locale.ROOT);
      namedColors.keySet().stream().sorted().filter(colorName -> colorName.startsWith(prefix)).forEach(suggestionsBuilder::suggest);
      if (prefix.isEmpty() || WHITE_HEX_PREFIX.startsWith(prefix)) {
         suggestionsBuilder.suggest(WHITE_HEX_SUGGESTION);
      }

      return suggestionsBuilder.buildFuture();
   }

   private int listTargets() {
      List<BlockEspTargets.Target> targets = this.getTargets().Т2().stream().sorted(Comparator.comparing(BlockEspTargets.Target::target)).toList();
      if (targets.isEmpty()) {
         ClientChat.З(EMPTY_TARGET_LIST_MESSAGE);
         return 1;
      }

      ClientChat.З(TARGET_LIST_HEADER);

      for (BlockEspTargets.Target target : targets) {
         ClientChat.З(target.target() + " — " + this.formatColor(target.color()));
      }

      ClientChat.З("Всего: " + targets.size());
      return 1;
   }

   private CompletableFuture<Suggestions> suggestSelectedTargets(SuggestionsBuilder suggestionsBuilder) {
      String prefix = suggestionsBuilder.getRemaining().toLowerCase(Locale.ROOT);
      boolean hasNamespace = prefix.contains(REMOVE_NAMESPACE_SEPARATOR);
      this.getTargets()
         .Т2()
         .stream()
         .map(BlockEspTargets.Target::target)
         .map(Identifier::tryParse)
         .filter(identifier -> identifier != null)
         .sorted(Comparator.comparing(Identifier::toString))
         .forEach(identifier -> this.suggestIdentifier(suggestionsBuilder, prefix, hasNamespace, identifier));
      return suggestionsBuilder.buildFuture();
   }

   public BlockEspCommand() {
      super(COMMAND_NAME);
   }

   private int addAllShulkers(String colorText) {
      Integer color = this.parseColor(colorText);
      if (color == null) {
         ClientChat.З("Неизвестный цвет: " + colorText + ". Используй название или #RRGGBB.");
         return 1;
      }

      for (String shulkerId : shulkerBoxIds) {
         this.getTargets().O(shulkerId, color);
      }

      this.saveTargetsConfig();
      ClientChat.З("BlockESP: добавлены все шалкеры с цветом " + this.formatColor(color));
      return 1;
   }

   private static final class TokenArgument implements ArgumentType<String> {
      private static final SimpleCommandExceptionType EXPECTED_TOKEN_ERROR = new SimpleCommandExceptionType(Component.literal(BlockEspCommand.TokenArgument.EXPECTED_TOKEN_MESSAGE));
      private static final String EXPECTED_TOKEN_MESSAGE = "Ожидалось значение";

      public String parse(StringReader reader) throws CommandSyntaxException {
         int startCursor = reader.getCursor();

         while (reader.canRead() && !Character.isWhitespace(reader.peek())) {
            reader.skip();
         }

         if (reader.getCursor() == startCursor) {
            throw EXPECTED_TOKEN_ERROR.createWithContext(reader);
         } else {
            return reader.getString().substring(startCursor, reader.getCursor());
         }
      }
   }
}
