package sg.command.impl;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.ec.KeyDisplayNames;
import sg.network.ClientChat;
import sg.command.ClientCommand;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleRegistry;

public class BindCommand extends ClientCommand {
   private static final String COMMAND_NAME = "bind";
   private static final String COMMAND_ALIAS_B = "b";
   private static final String ADD_LITERAL = "add";
   private static final String ADD_MODULE_ARGUMENT_NAME = "module";
   private static final String ADD_KEY_ARGUMENT_NAME = "key";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_TARGET_ARGUMENT_NAME = "target";
   private static final String LIST_LITERAL = "list";
   private static final String CLEAR_LITERAL = "clear";
   private static final String CLICK_GUI_BIND_PROTECTED_MESSAGE = "Нельзя удалить бинд модуля ClickGui.";
   private static final String MODULE_NAME_SEPARATOR = ", ";
   private static final String NO_BINDS_MESSAGE = "У вас отсутствуют установленные бинды.";
   private static final String BIND_LIST_HEADER = "Список биндов:";
   private static final String NO_REMOVABLE_BINDS_MESSAGE = "Нет биндов для удаления. Бинд ClickGui сохранён.";
   private static final String MODULE_NAME_QUOTE = "\"";
   private static final String MODULE_NAME_SPACE = " ";
   private static final String MODULE_NAME_UNDERSCORE = "_";
   private static final String MODULE_NAME_HYPHEN = "-";
   private static final String KEY_NAME_QUOTE = "\"";
   private static final String KEY_NAME_SPACE = " ";
   private static final String KEY_NAME_UNDERSCORE = "_";
   private static final String KEY_NAME_HYPHEN = "-";
   private static final String NORMALIZED_KEY_SEPARATOR = "_";
   private static final String KEY_NAME_CTRL = "CTRL";
   private static final String KEY_NAME_CONTROL = "CONTROL";
   private static final String KEY_NAME_RCTRL = "RCTRL";
   private static final String KEY_NAME_RIGHTCTRL = "RIGHTCTRL";
   private static final String KEY_NAME_RIGHT_CTRL = "RIGHT_CTRL";
   private static final String KEY_NAME_SHIFT = "SHIFT";
   private static final String KEY_NAME_LSHIFT = "LSHIFT";
   private static final String KEY_NAME_RSHIFT = "RSHIFT";
   private static final String KEY_NAME_RIGHTSHIFT = "RIGHTSHIFT";
   private static final String KEY_NAME_ALT = "ALT";
   private static final String KEY_NAME_LALT = "LALT";
   private static final String KEY_NAME_RALT = "RALT";
   private static final String KEY_NAME_RIGHTALT = "RIGHTALT";
   private static final String KEY_NAME_WIN = "WIN";
   private static final String KEY_NAME_WINDOWS = "WINDOWS";
   private static final String KEY_NAME_SUPER = "SUPER";
   private static final String KEY_NAME_ESC = "ESC";
   private static final String KEY_NAME_DEL = "DEL";
   private static final String KEY_NAME_INS = "INS";
   private static final String KEY_NAME_RETURN = "RETURN";
   private static final String KEY_NAME_PGUP = "PGUP";
   private static final String KEY_NAME_PGDOWN = "PGDOWN";
   private static final String KEY_NAME_PGDN = "PGDN";
   private static final String KEY_NAME_CAPS = "CAPS";
   private static final String KEY_NAME_NUMLOCK = "NUMLOCK";
   private static final String KEY_NAME_SCROLLLOCK = "SCROLLLOCK";
   private static final String KEY_NAME_PRINTSCREEN = "PRINTSCREEN";
   private static final String KEY_NAME_PRTSC = "PRTSC";
   private static final String KEY_NAME_ARROW_UP = "ARROW_UP";
   private static final String KEY_NAME_ARROW_DOWN = "ARROW_DOWN";
   private static final String KEY_NAME_ARROW_LEFT = "ARROW_LEFT";
   private static final String KEY_NAME_ARROW_RIGHT = "ARROW_RIGHT";
   private static final String KEY_NAME_LEFT_CONTROL = "LEFT_CONTROL";
   private static final String KEY_NAME_RIGHT_CONTROL = "RIGHT_CONTROL";
   private static final String KEY_NAME_LEFT_SHIFT = "LEFT_SHIFT";
   private static final String KEY_NAME_RIGHT_SHIFT = "RIGHT_SHIFT";
   private static final String KEY_NAME_LEFT_ALT = "LEFT_ALT";
   private static final String KEY_NAME_RIGHT_ALT = "RIGHT_ALT";
   private static final String KEY_NAME_LEFT_SUPER = "LEFT_SUPER";
   private static final String KEY_NAME_ESCAPE = "ESCAPE";
   private static final String KEY_NAME_DELETE = "DELETE";
   private static final String KEY_NAME_INSERT = "INSERT";
   private static final String KEY_NAME_ENTER = "ENTER";
   private static final String KEY_NAME_PAGE_UP = "PAGE_UP";
   private static final String KEY_NAME_PAGE_DOWN = "PAGE_DOWN";
   private static final String KEY_NAME_CAPS_LOCK = "CAPS_LOCK";
   private static final String KEY_NAME_NUM_LOCK = "NUM_LOCK";
   private static final String KEY_NAME_SCROLL_LOCK = "SCROLL_LOCK";
   private static final String KEY_NAME_PRINT_SCREEN = "PRINT_SCREEN";
   private static final String KEY_NAME_UP = "UP";
   private static final String KEY_NAME_DOWN = "DOWN";
   private static final String KEY_NAME_LEFT = "LEFT";
   private static final String KEY_NAME_RIGHT = "RIGHT";
   private static final String KEY_NAME_NONE = "NONE";
   private static final String KEY_FIELD_NAME_PREFIX = "GLFW_KEY_";
   private static final String KEY_FIELD_NAME_PREFIX_LENGTH_TEXT = "GLFW_KEY_";
   private static final String KEY_SUGGESTION_FIELD_PREFIX = "GLFW_KEY_";
   private static final String KEY_SUGGESTION_PREFIX_LENGTH_TEXT = "GLFW_KEY_";
   private static final String SUGGESTION_QUOTING_SPACE = " ";
   private static final String SUGGESTION_NORMALIZATION_QUOTE = "\"";
   private static final String SUGGESTION_NORMALIZATION_SPACE = " ";
   private static final String SUGGESTION_NORMALIZATION_UNDERSCORE = "_";
   private static final String SUGGESTION_NORMALIZATION_HYPHEN = "-";
   private static final String CLICK_GUI_MODULE_NAME = "ClickGui";
   private static final String REMOVE_TARGET_ARGUMENT_KEY = "target";
   private static final String ADD_MODULE_ARGUMENT_KEY = "module";
   private static final String ADD_KEY_ARGUMENT_KEY = "key";

   private void suggestModuleName(SuggestionsBuilder suggestionsBuilder, String normalizedPrefix, String candidateName, boolean appendSpace) {
      if (this.normalizeSuggestionText(candidateName).startsWith(normalizedPrefix)) {
         String suggestedName = candidateName.contains(SUGGESTION_QUOTING_SPACE) ? "\"" + candidateName + "\"" : candidateName;
         if (appendSpace) {
            suggestedName = suggestedName + " ";
         }

         suggestionsBuilder.suggest(suggestedName);
      }
   }

   private CompletableFuture<Suggestions> suggestModules(SuggestionsBuilder suggestionsBuilder) {
      String normalizedPrefix = this.normalizeSuggestionText(suggestionsBuilder.getRemaining());

      for (ClientModule module : this.getModules()) {
         this.suggestModuleName(suggestionsBuilder, normalizedPrefix, module.getName(), true);
         String className = module.getClass().getSimpleName();
         if (!className.equalsIgnoreCase(module.getName())) {
            this.suggestModuleName(suggestionsBuilder, normalizedPrefix, className, true);
         }
      }

      return suggestionsBuilder.buildFuture();
   }

   private int removeBind(String moduleOrKey) {
      ClientModule module = this.findModule(moduleOrKey);
      if (module != null) {
         if (this.isClickGuiModule(module)) {
            ClientChat.З(CLICK_GUI_BIND_PROTECTED_MESSAGE);
            return 1;
         } else if (module.getKeybind().getKeyCode() == -1) {
            ClientChat.З("У модуля " + module.getName() + " отсутствует бинд.");
            return 1;
         } else {
            String previousKeyName = this.getKeyName(module.getKeybind().getKeyCode());
            module.getKeybind().setKeyCode(-1);
            this.saveBinds();
            ClientChat.З("Удалён бинд: " + module.getName() + " — " + previousKeyName);
            return 1;
         }
      } else {
         int keyCode = this.parseKeyCode(moduleOrKey);
         if (keyCode == -1) {
            ClientChat.З("Модуль или клавиша " + moduleOrKey + " не найдены.");
            return 1;
         }

         ArrayList removedModuleNames = new ArrayList();
         boolean protectedBindFound = false;

         for (ClientModule boundModule : this.getModules()) {
            if (boundModule.getKeybind().getKeyCode() == keyCode) {
               if (this.isClickGuiModule(boundModule)) {
                  protectedBindFound = true;
               } else {
                  boundModule.getKeybind().setKeyCode(-1);
                  removedModuleNames.add(boundModule.getName());
               }
            }
         }

         if (removedModuleNames.isEmpty()) {
            if (protectedBindFound) {
               ClientChat.З("Нельзя удалить бинд ClickGui с клавиши " + this.getKeyName(keyCode) + ".");
            } else {
               ClientChat.З("На клавишу " + this.getKeyName(keyCode) + " ничего не назначено.");
            }

            return 1;
         } else {
            this.saveBinds();
            ClientChat.З("С клавиши " + this.getKeyName(keyCode) + " удалены бинды: " + String.join(MODULE_NAME_SEPARATOR, removedModuleNames));
            return 1;
         }
      }
   }

   private String normalizeModuleName(String name) {
      return name.trim().replace(MODULE_NAME_QUOTE, "").replace(MODULE_NAME_SPACE, "").replace(MODULE_NAME_UNDERSCORE, "").replace(MODULE_NAME_HYPHEN, "").toLowerCase(Locale.ROOT);
   }

   private String normalizeSuggestionText(String text) {
      return text.trim().toLowerCase(Locale.ROOT).replace(SUGGESTION_NORMALIZATION_QUOTE, "").replace(SUGGESTION_NORMALIZATION_SPACE, "").replace(SUGGESTION_NORMALIZATION_UNDERSCORE, "").replace(SUGGESTION_NORMALIZATION_HYPHEN, "");
   }

   private int clearBinds() {
      int removedCount = 0;

      for (ClientModule module : this.getModules()) {
         if (!this.isClickGuiModule(module) && module.getKeybind().getKeyCode() != -1) {
            module.getKeybind().setKeyCode(-1);
            removedCount++;
         }
      }

      if (removedCount == 0) {
         ClientChat.З(NO_REMOVABLE_BINDS_MESSAGE);
         return 1;
      } else {
         this.saveBinds();
         ClientChat.З("Удалены все бинды, кроме ClickGui. Количество: " + removedCount);
         return 1;
      }
   }

   private boolean isClickGuiModule(ClientModule module) {
      return module == this.getModules().getClickGui() || module.getName().equalsIgnoreCase(CLICK_GUI_MODULE_NAME);
   }

   private String getKeyName(int keyCode) {
      if (keyCode == -1) {
         return KEY_NAME_NONE;
      }

      for (Field keyField : InputConstants.class.getFields()) {
         if (keyField.getName().startsWith(KEY_FIELD_NAME_PREFIX) && keyField.getType() == int.class && Modifier.isStatic(keyField.getModifiers())) {
            try {
               if (keyField.getInt(null) == keyCode) {
                  return keyField.getName().substring(KEY_FIELD_NAME_PREFIX_LENGTH_TEXT.length());
               }
            } catch (IllegalAccessException accessError) {
            }
         }
      }

      return String.valueOf(keyCode);
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(
         this.literal(ADD_LITERAL)
            .then(
               this.argument(ADD_MODULE_ARGUMENT_NAME, StringArgumentType.string())
                  .suggests((commandContext, suggestionsBuilder) -> this.suggestModules(suggestionsBuilder))
                  .then(this.argument(ADD_KEY_ARGUMENT_NAME, StringArgumentType.word()).suggests((commandContext, suggestionsBuilder) -> this.suggestKeys(suggestionsBuilder)).executes(commandContext -> {
                     String moduleName = StringArgumentType.getString(commandContext, ADD_MODULE_ARGUMENT_KEY);
                     String keyName = StringArgumentType.getString(commandContext, ADD_KEY_ARGUMENT_KEY);
                     return this.setBind(moduleName, keyName);
                  }))
            )
      );
      commandBuilder.then(this.literal(REMOVE_LITERAL).then(this.argument(REMOVE_TARGET_ARGUMENT_NAME, StringArgumentType.string()).suggests((commandContext, suggestionsBuilder) -> this.suggestBindTargets(suggestionsBuilder)).executes(commandContext -> {
         String moduleOrKey = StringArgumentType.getString(commandContext, REMOVE_TARGET_ARGUMENT_KEY);
         return this.removeBind(moduleOrKey);
      })));
      commandBuilder.then(this.literal(LIST_LITERAL).executes(commandContext -> this.listBinds()));
      commandBuilder.then(this.literal(CLEAR_LITERAL).executes(commandContext -> this.clearBinds()));
   }

   public BindCommand() {
      super(COMMAND_NAME, COMMAND_ALIAS_B);
   }

   private ModuleRegistry getModules() {
      return RoseClient.getInstance().getServices().getModules();
   }

   private int setBind(String moduleName, String keyName) {
      ClientModule module = this.findModule(moduleName);
      if (module == null) {
         ClientChat.З("Модуль " + moduleName + " не найден.");
         return 1;
      } else {
         int keyCode = this.parseKeyCode(keyName);
         if (keyCode == -1) {
            ClientChat.З("Клавиша " + keyName + " не найдена.");
            return 1;
         } else {
            module.getKeybind().setKeyCode(keyCode);
            this.saveBinds();
            ClientChat.З("Установлен бинд: " + module.getName() + " — " + this.getKeyName(keyCode));
            return 1;
         }
      }
   }

   private int listBinds() {
      List<ClientModule> boundModules = this.getModules()
         .stream()
         .filter(moduleEntry -> moduleEntry.getKeybind().getKeyCode() != -1)
         .sorted(Comparator.comparing(ClientModule::getName, String.CASE_INSENSITIVE_ORDER))
         .toList();
      if (boundModules.isEmpty()) {
         ClientChat.З(NO_BINDS_MESSAGE);
         return 1;
      }

      ClientChat.З(BIND_LIST_HEADER);

      for (ClientModule module : boundModules) {
         ClientChat.З(module.getName() + " -> " + KeyDisplayNames.О(module.getKeybind().getKeyCode()));
      }

      return 1;
   }

   private void saveBinds() {
      RoseClient.getInstance().getServices().getConfigManager().saveAll();
   }

   private int parseKeyCode(String keyName) {
      if (keyName != null && !keyName.isBlank()) {
         String normalizedKeyName = keyName.trim().toUpperCase(Locale.ROOT).replace(KEY_NAME_QUOTE, "").replace(KEY_NAME_SPACE, KEY_NAME_UNDERSCORE).replace(KEY_NAME_HYPHEN, NORMALIZED_KEY_SEPARATOR);

         normalizedKeyName = switch (normalizedKeyName) {
            case KEY_NAME_CTRL, KEY_NAME_CONTROL -> KEY_NAME_LEFT_CONTROL;
            case KEY_NAME_RCTRL, KEY_NAME_RIGHTCTRL, KEY_NAME_RIGHT_CTRL -> KEY_NAME_RIGHT_CONTROL;
            case KEY_NAME_SHIFT, KEY_NAME_LSHIFT -> KEY_NAME_LEFT_SHIFT;
            case KEY_NAME_RSHIFT, KEY_NAME_RIGHTSHIFT -> KEY_NAME_RIGHT_SHIFT;
            case KEY_NAME_ALT, KEY_NAME_LALT -> KEY_NAME_LEFT_ALT;
            case KEY_NAME_RALT, KEY_NAME_RIGHTALT -> KEY_NAME_RIGHT_ALT;
            case KEY_NAME_WIN, KEY_NAME_WINDOWS, KEY_NAME_SUPER -> KEY_NAME_LEFT_SUPER;
            case KEY_NAME_ESC -> KEY_NAME_ESCAPE;
            case KEY_NAME_DEL -> KEY_NAME_DELETE;
            case KEY_NAME_INS -> KEY_NAME_INSERT;
            case KEY_NAME_RETURN -> KEY_NAME_ENTER;
            case KEY_NAME_PGUP -> KEY_NAME_PAGE_UP;
            case KEY_NAME_PGDOWN, KEY_NAME_PGDN -> KEY_NAME_PAGE_DOWN;
            case KEY_NAME_CAPS -> KEY_NAME_CAPS_LOCK;
            case KEY_NAME_NUMLOCK -> KEY_NAME_NUM_LOCK;
            case KEY_NAME_SCROLLLOCK -> KEY_NAME_SCROLL_LOCK;
            case KEY_NAME_PRINTSCREEN, KEY_NAME_PRTSC -> KEY_NAME_PRINT_SCREEN;
            case KEY_NAME_ARROW_UP -> KEY_NAME_UP;
            case KEY_NAME_ARROW_DOWN -> KEY_NAME_DOWN;
            case KEY_NAME_ARROW_LEFT -> KEY_NAME_LEFT;
            case KEY_NAME_ARROW_RIGHT -> KEY_NAME_RIGHT;
            default -> normalizedKeyName;
         };

         try {
            Field keyField = InputConstants.class.getField("GLFW_KEY_" + normalizedKeyName);
            return keyField.getType() == int.class && Modifier.isStatic(keyField.getModifiers()) ? keyField.getInt(null) : -1;
         } catch (ReflectiveOperationException reflectionError) {
            return -1;
         }
      } else {
         return -1;
      }
   }

   private CompletableFuture<Suggestions> suggestBindTargets(SuggestionsBuilder suggestionsBuilder) {
      String normalizedPrefix = this.normalizeSuggestionText(suggestionsBuilder.getRemaining());

      for (ClientModule module : this.getModules()) {
         if (module.getKeybind().getKeyCode() != -1) {
            this.suggestModuleName(suggestionsBuilder, normalizedPrefix, module.getName(), false);
            String className = module.getClass().getSimpleName();
            if (!className.equalsIgnoreCase(module.getName())) {
               this.suggestModuleName(suggestionsBuilder, normalizedPrefix, className, false);
            }

            String keyName = this.getKeyName(module.getKeybind().getKeyCode());
            if (this.normalizeSuggestionText(keyName).startsWith(normalizedPrefix)) {
               suggestionsBuilder.suggest(keyName);
            }
         }
      }

      return suggestionsBuilder.buildFuture();
   }

   private CompletableFuture<Suggestions> suggestKeys(SuggestionsBuilder suggestionsBuilder) {
      String keyPrefix = suggestionsBuilder.getRemaining().trim().toUpperCase(Locale.ROOT);

      for (Field keyField : InputConstants.class.getFields()) {
         if (keyField.getName().startsWith(KEY_SUGGESTION_FIELD_PREFIX) && keyField.getType() == int.class && Modifier.isStatic(keyField.getModifiers())) {
            String keyName = keyField.getName().substring(KEY_SUGGESTION_PREFIX_LENGTH_TEXT.length());
            if (keyName.startsWith(keyPrefix)) {
               suggestionsBuilder.suggest(keyName);
            }
         }
      }

      return suggestionsBuilder.buildFuture();
   }

   private ClientModule findModule(String name) {
      String normalizedName = this.normalizeModuleName(name);

      for (ClientModule module : this.getModules()) {
         String normalizedDisplayName = this.normalizeModuleName(module.getName());
         String normalizedClassName = this.normalizeModuleName(module.getClass().getSimpleName());
         if (normalizedDisplayName.equals(normalizedName) || normalizedClassName.equals(normalizedName)) {
            return module;
         }
      }

      return null;
   }
}
