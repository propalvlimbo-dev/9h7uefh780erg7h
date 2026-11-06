package sg.command.impl;

import com.google.gson.annotations.SerializedName;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.File;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ClickEvent.RunCommand;
import net.minecraft.network.chat.HoverEvent.ShowText;
import net.minecraft.util.Util;
import sg.command.argument.ListedStringArgumentType;
import sg.events.EventListener;
import sg.events.impl.ChatOpenedEvent;
import sg.command.argument.ModuleNameArgument;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.settings.ValueSetting;
import sg.settings.MultiChoiceSetting;
import sg.config.ModuleConfig;
import sg.settings.SingleChoiceSetting;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public class ConfigCommand extends ClientCommand {
   @SerializedName("Х")
   private final ListedStringArgumentType configNamesArgument = new ListedStringArgumentType();
   private final @com.google.gson.annotations.SerializedName("с8")  EventListener<ChatOpenedEvent> chatOpenedListener = this::refreshConfigSuggestions;
   private static final String COMMAND_NAME = "config";
   private static final String COMMAND_ALIAS_CFG = "cfg";
   private static final String SAVEALL_LITERAL = "saveall";
   private static final String LOADALL_LITERAL = "loadall";
   private static final String SAVE_LITERAL = "save";
   private static final String SAVE_NAME_ARGUMENT_NAME = "name";
   private static final String LOAD_LITERAL = "load";
   private static final String LOAD_NAME_ARGUMENT_NAME = "name";
   private static final String LOAD_MODULE_ARGUMENT_NAME = "module";
   private static final String DIR_LITERAL = "dir";
   private static final String LIST_LITERAL = "list";
   private static final String REMOVE_LITERAL = "remove";
   private static final String REMOVE_CONFIG_ARGUMENT_NAME = "config";
   private static final String RESET_LITERAL = "reset";
   private static final String CONFIG_FILE_SYSTEM_DRIVE_ENV = "SystemDrive";
   private static final String LOAD_CONFIG_ACTION_LABEL = "[+]";
   private static final String LOAD_ACTION_SEPARATOR = " ";
   private static final String REMOVE_CONFIG_ACTION_LABEL = "[x]";
   private static final String REMOVE_ACTION_SEPARATOR = " ";
   private static final String REMOVE_CONFIG_ARGUMENT_KEY = "config";
   private static final String CONFIG_DELETE_FAILED_MESSAGE = "Не удалось удалить конфиг!";
   private static final String CONFIG_FILE_NOT_FOUND_MESSAGE = "Данного файла не существует!";
   private static final String NO_CONFIGS_MESSAGE = "У вас отсутствуют конфиги!";
   private static final String CONFIG_DIRECTORY_SYSTEM_DRIVE_TEST_ENV = "SystemDrive";
   private static final String CONFIG_DIRECTORY_SYSTEM_DRIVE_ENV = "SystemDrive";
   private static final String CUSTOM_CONFIG_DIRECTORY_PATH = "/Dimas/rose/configs/custom/";
   private static final String CONFIG_DIRECTORY_OPENED_MESSAGE = "Успешно открыл папку с конфигами.";
   private static final String LOAD_MODULE_ARGUMENT_KEY = "module";
   private static final String MODULE_LOAD_CONFIG_NAME_KEY = "name";
   private static final String MODULE_CONFIG_DIRECTORY = "rose/configs/custom";
   private static final String MODULE_NOT_FOUND_MESSAGE = "Модуль не найден!";
   private static final String LOAD_CONFIG_NAME_KEY = "name";
   private static final String LOAD_CONFIG_DIRECTORY = "rose/configs/custom";
   private static final String SAVE_NAME_ARGUMENT_KEY = "name";
   private static final String SAVE_CONFIG_DIRECTORY = "rose/configs/custom";
   private static final String ALL_SETTINGS_LOADED_MESSAGE = "Успешно загрузил все настройки.";
   private static final String ALL_SETTINGS_SAVED_MESSAGE = "Успешно сохранил все настройки.";

   private static File getConfigFile(String configName) {
      return new File(sg.config.ClientDirectories.forDevelopment(System.getenv(CONFIG_FILE_SYSTEM_DRIVE_ENV)) + "\\Dimas\\", "rose/configs/custom/" + configName + ".conf");
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(this.literal(SAVEALL_LITERAL).executes(commandContext -> {
         RoseClient.getInstance().getServices().getConfigManager().saveAll();
         ClientChat.З(ALL_SETTINGS_SAVED_MESSAGE);
         return 1;
      })).then(this.literal(LOADALL_LITERAL).executes(commandContext -> {
         RoseClient.getInstance().getServices().getConfigManager().loadAll();
         ClientChat.З(ALL_SETTINGS_LOADED_MESSAGE);
         return 1;
      })).then(this.literal(SAVE_LITERAL).then(this.argument(SAVE_NAME_ARGUMENT_NAME, StringArgumentType.word()).executes(commandContext -> {
         String configName = commandContext.getArgument(SAVE_NAME_ARGUMENT_KEY, String.class);
         new ModuleConfig(SAVE_CONFIG_DIRECTORY, configName).save();
         ClientChat.З("Успешно сохранил конфиг с именем: " + configName);
         return 1;
      }))).then(this.literal(LOAD_LITERAL).then(this.argument(LOAD_NAME_ARGUMENT_NAME, this.configNamesArgument).executes(commandContext -> {
         String configName = commandContext.getArgument(LOAD_CONFIG_NAME_KEY, String.class);
         new ModuleConfig(LOAD_CONFIG_DIRECTORY, configName).load();
         ClientChat.З("Успешно загрузил конфиг с именем: " + configName);
         return 1;
      }).then(this.argument(LOAD_MODULE_ARGUMENT_NAME, ModuleNameArgument.modules()).executes(commandContext -> {
         String moduleName = commandContext.getArgument(LOAD_MODULE_ARGUMENT_KEY, String.class);
         String configName = commandContext.getArgument(MODULE_LOAD_CONFIG_NAME_KEY, String.class);
         ClientModule selectedModule = null;

         for (ClientModule candidateModule : RoseClient.getInstance().getServices().getModules()) {
            if (candidateModule.getName().equalsIgnoreCase(moduleName)) {
               selectedModule = candidateModule;
               break;
            }
         }

         if (selectedModule != null) {
            new ModuleConfig(MODULE_CONFIG_DIRECTORY, configName).loadModuleConfig(selectedModule);
            ClientChat.З("Модуль " + moduleName + " из конфига " + configName + " успешно загружен!");
         } else {
            ClientChat.З(MODULE_NOT_FOUND_MESSAGE);
         }

         return 1;
      })))).then(this.literal(DIR_LITERAL).executes(commandContext -> {
         String baseDirectory = sg.config.ClientDirectories.forDevelopment(System.getenv(CONFIG_DIRECTORY_SYSTEM_DRIVE_TEST_ENV) == null ? Minecraft.getInstance().gameDirectory.toString() : System.getenv(CONFIG_DIRECTORY_SYSTEM_DRIVE_ENV));
         File configDirectory = new File(baseDirectory, CUSTOM_CONFIG_DIRECTORY_PATH);
         Util.getPlatform().openPath(configDirectory.toPath());
         ClientChat.З(CONFIG_DIRECTORY_OPENED_MESSAGE);
         return 1;
      })).then(this.literal(LIST_LITERAL).executes(commandContext -> {
         List<String> configNames = RoseClient.getInstance().getServices().getConfigManager().listCustomConfigNames();
         this.configNamesArgument.setList(configNames);
         if (configNames.isEmpty()) {
            ClientChat.З(NO_CONFIGS_MESSAGE);
            return 1;
         }

         String commandPrefix = RoseClient.getInstance().getServices().getCommands().getCommandPrefix().toString();

         for (String configName : configNames) {
            ClientChat.З(createConfigEntry(commandPrefix, configName));
         }

         return 1;
      })).then(this.literal(REMOVE_LITERAL).then(this.argument(REMOVE_CONFIG_ARGUMENT_NAME, this.configNamesArgument).executes(commandContext -> {
         String configName = commandContext.getArgument(REMOVE_CONFIG_ARGUMENT_KEY, String.class);
         File configFile = getConfigFile(configName);
         if (configFile.exists()) {
            if (configFile.delete()) {
               this.configNamesArgument.setList(RoseClient.getInstance().getServices().getConfigManager().listCustomConfigNames());
               ClientChat.З("Вы успешно удалили конфиг " + configName + "!");
            } else {
               ClientChat.З(CONFIG_DELETE_FAILED_MESSAGE);
            }
         } else {
            ClientChat.З(CONFIG_FILE_NOT_FOUND_MESSAGE);
         }

         return 1;
      }))).then(this.literal(RESET_LITERAL).executes(commandContext -> {
         for (ClientModule module : RoseClient.getInstance().getServices().getModules()) {
            for (ValueSetting setting : module.getSettings()) {
               if (setting instanceof SingleChoiceSetting singleChoiceSetting) {
                  singleChoiceSetting.resetToDefault();
               } else if (setting instanceof MultiChoiceSetting multiChoiceSetting) {
                  multiChoiceSetting.resetToDefaults();
               } else {
                  resetSetting(setting);
                  if (setting instanceof BooleanSetting booleanSetting) {
                     booleanSetting.getKeybind().setKeyCode(-1);
                  }
               }
            }

            if (module.isEnabled()) {
               module.toggle();
            }

            module.getKeybind().setKeyCode(-1);
         }

         return 1;
      }));
   }

   private static Component createConfigAction(String label, int color, String command, String hoverText) {
      return Component.literal(label)
         .setStyle(Style.EMPTY.withColor(color).withBold(true).withClickEvent(new RunCommand(command)).withHoverEvent(new ShowText(Component.literal(hoverText))));
   }

   public ConfigCommand() {
      super(COMMAND_NAME, COMMAND_ALIAS_CFG);
   }

   private static <T> void resetSetting(ValueSetting<T> setting) {
      setting.setValue(setting.getDefaultValue());
   }

   private static Component createConfigEntry(String commandPrefix, String configName) {
      return Component.empty()
         .append(createConfigAction(LOAD_CONFIG_ACTION_LABEL, ColorMath.packRgb(22, 251, 27), commandPrefix + "cfg load " + configName, "Загрузить " + configName))
         .append(Component.literal(LOAD_ACTION_SEPARATOR))
         .append(createConfigAction(REMOVE_CONFIG_ACTION_LABEL, ColorMath.packRgb(251, 22, 22), commandPrefix + "cfg remove " + configName, "Удалить " + configName))
         .append(Component.literal(REMOVE_ACTION_SEPARATOR))
         .append(Component.literal(configName));
   }

   public void refreshConfigSuggestions(ChatOpenedEvent event) {
      this.configNamesArgument.setList(RoseClient.getInstance().getServices().getConfigManager().listCustomConfigNames());
   }
}
