package sg.command;

import com.google.gson.annotations.SerializedName;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.function.Consumer;
import lombok.Generated;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.command.impl.WaypointCommand;
import sg.command.impl.MacroCommand;
import sg.command.impl.HeadSwapCommand;
import sg.command.impl.UnhookCommand;
import sg.command.impl.HorizontalClipCommand;
import sg.command.impl.BindCommand;
import sg.modules.impl.init.WaypointManager;
import sg.command.impl.FriendCommand;
import sg.client.RoseClient;
import sg.command.impl.AuctionSellCommand;
import sg.command.impl.TeleportCommand;
import sg.command.impl.VerticalClipCommand;
import sg.command.impl.AutoContractCommand;
import sg.command.impl.AutoWardenCommand;
import sg.command.impl.BlockEspCommand;
import sg.command.impl.ChestStealerCommand;
import sg.command.impl.ConfigCommand;
import sg.command.impl.CustomNameCommand;
import sg.command.impl.FakePlayerCommand;
import sg.command.impl.GpsCommand;
import sg.command.impl.HelpCommand;
import sg.command.impl.IrcCommand;
import sg.command.impl.KillMessageCommand;
import sg.command.impl.PartyCommand;
import sg.command.impl.ReconnectCommand;
import sg.command.impl.TargetCommand;
import sg.command.impl.TotemPopMessageCommand;
import sg.network.ClientChat;

public class CommandRegistry extends ArrayList<ClientCommand> {
   @SerializedName("В")
   private final CommandDispatcher<ClientSuggestionProvider> dispatcher = new CommandDispatcher<>();
   private final @com.google.gson.annotations.SerializedName("Ю")  Consumer<Throwable> errorHandler = Throwable::printStackTrace;
   @SerializedName("В")
   private Character commandPrefix = '.';
   private static final String HELP_DESCRIPTION = "Показывает список всех команд и их описания";
   private static final String HELP_DEFAULT_HELP = "Показать список команд клиента";
   private static final String сРТ = "Выделяет регион вокруг участника IRC-пати по синхронизированным координатам";
   private static final String сРО = "<nickname> <radius>";
   private static final String сРФ = "Minecraft-ник участника пати и радиус выделения по всем осям";
   private static final String CONFIG_DESCRIPTION = "Сохраняет и загружает конфигурации клиента";
   private static final String CONFIG_SAVEALL_SYNTAX = "saveall";
   private static final String CONFIG_SAVEALL_HELP = "Сохранить все текущие настройки";
   private static final String CONFIG_LOADALL_SYNTAX = "loadall";
   private static final String CONFIG_LOADALL_HELP = "Загрузить все основные настройки";
   private static final String CONFIG_SAVE_NAME_SYNTAX = "save <name>";
   private static final String CONFIG_SAVE_NAME_HELP = "Сохранить пользовательский конфиг";
   private static final String CONFIG_LOAD_NAME_SYNTAX = "load <name>";
   private static final String CONFIG_LOAD_NAME_HELP = "Загрузить пользовательский конфиг";
   private static final String CONFIG_LOAD_NAME_MODULE_SYNTAX = "load <name> <module>";
   private static final String CONFIG_LOAD_NAME_MODULE_HELP = "Загрузить настройки одного модуля из конфига";
   private static final String CONFIG_REMOVE_NAME_SYNTAX = "remove <name>";
   private static final String CONFIG_REMOVE_NAME_HELP = "Удалить пользовательский конфиг";
   private static final String CONFIG_LIST_SYNTAX = "list";
   private static final String CONFIG_LIST_HELP = "Показать сохранённые конфиги";
   private static final String CONFIG_DIR_SYNTAX = "dir";
   private static final String CONFIG_DIR_HELP = "Открыть папку конфигов";
   private static final String CONFIG_RESET_SYNTAX = "reset";
   private static final String CONFIG_RESET_HELP = "Сбросить настройки модулей к значениям по умолчанию";
   private static final String CUSTOM_NAME_DESCRIPTION = "Задаёт отображаемый ник для NameProtect";
   private static final String CUSTOM_NAME_DEFAULT_HELP = "Показать текущий отображаемый ник";
   private static final String CUSTOM_NAME_NAME_SYNTAX = "<ник>";
   private static final String CUSTOM_NAME_NAME_HELP = "Установить отображаемый ник; поддерживаются русский и английский";
   private static final String CUSTOM_NAME_OFF_SYNTAX = "off";
   private static final String CUSTOM_NAME_OFF_HELP = "Сбросить кастомный ник, не выключая NameProtect";
   private static final String FRIEND_DESCRIPTION = "Управляет списком друзей";
   private static final String FRIEND_ADD_NICK_FAKE_NAME_SYNTAX = "add <nick> [fakeName]";
   private static final String FRIEND_ADD_NICK_FAKE_NAME_HELP = "Добавить друга; fakeName задаёт отображаемый ник";
   private static final String FRIEND_REMOVE_NICK_SYNTAX = "remove <nick>";
   private static final String FRIEND_REMOVE_NICK_HELP = "Удалить друга";
   private static final String FRIEND_LIST_SYNTAX = "list";
   private static final String FRIEND_LIST_HELP = "Показать друзей и их отображаемые ники";
   private static final String FRIEND_CLEAR_SYNTAX = "clear";
   private static final String FRIEND_CLEAR_HELP = "Очистить список друзей";
   private static final String TARGET_DESCRIPTION = "Фиксирует Aura на одном игроке и красит его неймтег";
   private static final String TARGET_DEFAULT_HELP = "Показать текущую цель";
   private static final String TARGET_NAME_SYNTAX = "<ник>";
   private static final String TARGET_NAME_HELP = "Зафиксировать Aura только на этом игроке";
   private static final String TARGET_OFF_SYNTAX = "off";
   private static final String TARGET_OFF_HELP = "Снять фиксацию цели";
   private static final String MACRO_DESCRIPTION = "Создаёт и удаляет макросы на клавиши";
   private static final String MACRO_ADD_KEY_COMMAND_SYNTAX = "add <key> <command...>";
   private static final String MACRO_ADD_KEY_COMMAND_HELP = "Назначить выполнение команды на клавишу";
   private static final String MACRO_REMOVE_KEY_SYNTAX = "remove <key>";
   private static final String MACRO_REMOVE_KEY_HELP = "Удалить макрос с клавиши";
   private static final String MACRO_CLEAR_SYNTAX = "clear";
   private static final String MACRO_CLEAR_HELP = "Удалить все макросы";
   private static final String VERTICAL_CLIP_DESCRIPTION = "Перемещает игрока по вертикали";
   private static final String VERTICAL_CLIP_UP_SYNTAX = "up";
   private static final String VERTICAL_CLIP_UP_HELP = "Переместиться вверх";
   private static final String VERTICAL_CLIP_DOWN_SYNTAX = "down";
   private static final String VERTICAL_CLIP_DOWN_HELP = "Переместиться вниз";
   private static final String VERTICAL_CLIP_BLOCKS_SYNTAX = "<blocks>";
   private static final String VERTICAL_CLIP_BLOCKS_HELP = "Переместиться на указанное число блоков";
   private static final String TELEPORT_DESCRIPTION = "Перемещает игрока к указанным координатам";
   private static final String TELEPORT_NICK_SYNTAX = "<nick>";
   private static final String TELEPORT_NICK_HELP = "Телепортироваться к загруженному игроку";
   private static final String TELEPORT_X_Y_Z_SYNTAX = "<x> <y> <z>";
   private static final String TELEPORT_X_Y_Z_HELP = "Телепортироваться по координатам";
   private static final String TELEPORT_RW_NICK_SYNTAX = "rw <nick>";
   private static final String TELEPORT_RW_NICK_HELP = "Телепортировать ноги в ближайший к игроку блок";
   private static final String TELEPORT_RW_X_Z_SYNTAX = "rw <x> <z>";
   private static final String TELEPORT_RW_X_Z_HELP = "Запустить пошаговый маршрут TP RW";
   private static final String TELEPORT_STOP_SYNTAX = "stop";
   private static final String TELEPORT_STOP_HELP = "Остановить активный маршрут TP RW";
   private static final String HORIZONTAL_CLIP_DESCRIPTION = "Перемещает игрока по горизонтали";
   private static final String HORIZONTAL_CLIP_DISTANCE_SYNTAX = "<distance>";
   private static final String HORIZONTAL_CLIP_DISTANCE_HELP = "Переместиться вперёд на расстояние от 1 до 10000 блоков";
   private static final String WAYPOINT_DESCRIPTION = "Управляет сохранёнными точками мира";
   private static final String WAYPOINT_DEFAULT_HELP = "Показать сохранённые точки";
   private static final String WAYPOINT_ADD_NAME_SYNTAX = "add <name>";
   private static final String WAYPOINT_ADD_NAME_HELP = "Сохранить текущую позицию";
   private static final String WAYPOINT_ADD_NAME_X_Y_Z_SYNTAX = "add <name> <x> <y> <z>";
   private static final String WAYPOINT_ADD_NAME_X_Y_Z_HELP = "Сохранить точку с указанными координатами";
   private static final String WAYPOINT_REMOVE_NAME_SYNTAX = "remove <name>";
   private static final String WAYPOINT_REMOVE_NAME_HELP = "Удалить точку";
   private static final String WAYPOINT_DELETE_NAME_SYNTAX = "delete <name>";
   private static final String WAYPOINT_DELETE_NAME_HELP = "Удалить точку";
   private static final String WAYPOINT_LIST_SYNTAX = "list";
   private static final String WAYPOINT_LIST_HELP = "Показать сохранённые точки";
   private static final String WAYPOINT_CLEAR_SYNTAX = "clear";
   private static final String WAYPOINT_CLEAR_HELP = "Удалить все точки";
   private static final String WAYPOINT_CLEARALL_SYNTAX = "clearall";
   private static final String WAYPOINT_CLEARALL_HELP = "Удалить все точки";
   private static final String GPS_DESCRIPTION = "Устанавливает или отключает GPS-указатель";
   private static final String GPS_X_Z_SYNTAX = "<x> <z>";
   private static final String GPS_X_Z_HELP = "Установить GPS на координаты";
   private static final String GPS_INFO_SYNTAX = "info";
   private static final String GPS_INFO_HELP = "Показать текущую цель GPS";
   private static final String GPS_OFF_SYNTAX = "off";
   private static final String GPS_OFF_HELP = "Отключить GPS";
   private static final String BIND_DESCRIPTION = "Устанавливает бинды функций на кнопки";
   private static final String BIND_ADD_MODULE_KEY_SYNTAX = "add <module> <key>";
   private static final String BIND_ADD_MODULE_KEY_HELP = "Назначить модулю клавишу";
   private static final String BIND_REMOVE_MODULE_KEY_SYNTAX = "remove <module|key>";
   private static final String BIND_REMOVE_MODULE_KEY_HELP = "Удалить бинд по модулю или клавише";
   private static final String BIND_LIST_SYNTAX = "list";
   private static final String BIND_LIST_HELP = "Показать активные бинды";
   private static final String BIND_CLEAR_SYNTAX = "clear";
   private static final String BIND_CLEAR_HELP = "Очистить бинды; бинд ClickGui сохранится";
   private static final String UNHOOK_DESCRIPTION = "Временно отключает клиент до ввода кода восстановления в чат";
   private static final String UNHOOK_DEFAULT_HELP = "Отключить модули, команды и бинды и получить код восстановления";
   private static final String RECONNECT_DESCRIPTION = "Перезаходит на текущий гриф через Auto Joiner";
   private static final String RECONNECT_DEFAULT_HELP = "Начать перезаход на текущий гриф или анархию";
   private static final String RECONNECT_GRIEF_SYNTAX = "<grief>";
   private static final String RECONNECT_GRIEF_HELP = "Перезайти на указанный гриф от 1 до 96";
   private static final String AUTO_CONTRACT_DESCRIPTION = "Задаёт цель для модуля AutoContract";
   private static final String AUTO_CONTRACT_DEFAULT_HELP = "Показать текущую цель";
   private static final String AUTO_CONTRACT_NICKNAME_SYNTAX = "<nickname>";
   private static final String AUTO_CONTRACT_NICKNAME_HELP = "Установить ник цели";
   private static final String AUCTION_SELL_DESCRIPTION = "Ищет предмет в руке на аукционе или выставляет его по цене рынка";
   private static final String AUCTION_SELL_DEFAULT_HELP = "Найти предмет из основной руки на аукционе";
   private static final String AUCTION_SELL_SELL_SYNTAX = "sell";
   private static final String AUCTION_SELL_SELL_HELP = "Выставить донатный предмет по рассчитанной цене";
   private static final String AUCTION_SELL_SELL_PERCENT_SYNTAX = "sell <процент>";
   private static final String AUCTION_SELL_SELL_PERCENT_HELP = "Выставить предмет со скидкой от рассчитанной цены";
   private static final String AUTO_WARDEN_DESCRIPTION = "Управляет списком анархий для Auto Warden";
   private static final String AUTO_WARDEN_ADD_ANARCHY_SYNTAX = "add <анархия>";
   private static final String AUTO_WARDEN_ADD_ANARCHY_HELP = "Добавить анархию в список фарма";
   private static final String AUTO_WARDEN_REMOVE_ANARCHY_SYNTAX = "remove <анархия>";
   private static final String AUTO_WARDEN_REMOVE_ANARCHY_HELP = "Удалить анархию из списка";
   private static final String AUTO_WARDEN_LIST_SYNTAX = "list";
   private static final String AUTO_WARDEN_LIST_HELP = "Показать список анархий";
   private static final String AUTO_WARDEN_CLEAR_SYNTAX = "clear";
   private static final String AUTO_WARDEN_CLEAR_HELP = "Очистить список анархий";
   private static final String CHEST_STEALER_DESCRIPTION = "Настраивает список предметов ChestStealer";
   private static final String CHEST_STEALER_DEFAULT_HELP = "Показать выбранные предметы";
   private static final String CHEST_STEALER_ADD_ITEM_SYNTAX = "add <item>";
   private static final String CHEST_STEALER_ADD_ITEM_HELP = "Добавить предмет для лутания";
   private static final String CHEST_STEALER_REMOVE_ITEM_SYNTAX = "remove <item>";
   private static final String CHEST_STEALER_REMOVE_ITEM_HELP = "Удалить предмет из списка";
   private static final String CHEST_STEALER_LIST_SYNTAX = "list";
   private static final String CHEST_STEALER_LIST_HELP = "Показать выбранные предметы";
   private static final String CHEST_STEALER_CLEAR_SYNTAX = "clear";
   private static final String CHEST_STEALER_CLEAR_HELP = "Очистить список предметов";
   private static final String HEAD_SWAP_DESCRIPTION = "Настраивает предмет для HeadSwap";
   private static final String HEAD_SWAP_DEFAULT_HELP = "Показать текущий предмет";
   private static final String HEAD_SWAP_ADD_SYNTAX = "add";
   private static final String HEAD_SWAP_ADD_HELP = "Добавить предмет из руки";
   private static final String HEAD_SWAP_REMOVE_SYNTAX = "remove";
   private static final String HEAD_SWAP_REMOVE_HELP = "Удалить текущий предмет";
   private static final String HEAD_SWAP_CLEAR_SYNTAX = "clear";
   private static final String HEAD_SWAP_CLEAR_HELP = "Удалить текущий предмет";
   private static final String HEAD_SWAP_LIST_SYNTAX = "list";
   private static final String HEAD_SWAP_LIST_HELP = "Показать текущий предмет";
   private static final String BLOCK_ESP_DESCRIPTION = "Настраивает подсветку блоков и сущностей";
   private static final String BLOCK_ESP_ADD_BLOCK_ENTITY_COLOR_SYNTAX = "add <block|entity> <color>";
   private static final String BLOCK_ESP_ADD_BLOCK_ENTITY_COLOR_HELP = "Добавить подсветку; цвет — имя или #RRGGBB";
   private static final String BLOCK_ESP_ADD_SHULKERS_COLOR_SYNTAX = "add shulkers <color>";
   private static final String BLOCK_ESP_ADD_SHULKERS_COLOR_HELP = "Добавить подсветку всех шалкеров";
   private static final String BLOCK_ESP_REMOVE_BLOCK_ENTITY_SYNTAX = "remove <block|entity>";
   private static final String BLOCK_ESP_REMOVE_BLOCK_ENTITY_HELP = "Удалить блок или сущность из подсветки";
   private static final String BLOCK_ESP_LIST_SYNTAX = "list";
   private static final String BLOCK_ESP_LIST_HELP = "Показать настройки BlockESP";
   private static final String BLOCK_ESP_CLEAR_SYNTAX = "clear";
   private static final String BLOCK_ESP_CLEAR_HELP = "Очистить настройки BlockESP";
   private static final String FAKE_PLAYER_DESCRIPTION = "Управляет фейковыми игроками и их экипировкой";
   private static final String FAKE_PLAYER_DEFAULT_HELP = "Показать фейковых игроков";
   private static final String FAKE_PLAYER_ADD_NAME_SYNTAX = "add [name]";
   private static final String FAKE_PLAYER_ADD_NAME_HELP = "Создать фейкового игрока";
   private static final String FAKE_PLAYER_REMOVE_NAME_SYNTAX = "remove <name>";
   private static final String FAKE_PLAYER_REMOVE_NAME_HELP = "Удалить фейкового игрока";
   private static final String FAKE_PLAYER_EQUIP_NAME_SLOT_ITEM_COUNT_SYNTAX = "equip <name> <slot> <item> [count]";
   private static final String FAKE_PLAYER_EQUIP_NAME_SLOT_ITEM_COUNT_HELP = "Надеть предмет в выбранный слот";
   private static final String FAKE_PLAYER_GIVE_NAME_ITEM_COUNT_SYNTAX = "give <name> <item> [count]";
   private static final String FAKE_PLAYER_GIVE_NAME_ITEM_COUNT_HELP = "Добавить предмет в инвентарь";
   private static final String FAKE_PLAYER_COPY_NAME_SYNTAX = "copy <name>";
   private static final String FAKE_PLAYER_COPY_NAME_HELP = "Скопировать экипировку на игрока";
   private static final String FAKE_PLAYER_LIST_SYNTAX = "list";
   private static final String FAKE_PLAYER_LIST_HELP = "Показать фейковых игроков";
   private static final String FAKE_PLAYER_CLEAR_SYNTAX = "clear";
   private static final String FAKE_PLAYER_CLEAR_HELP = "Удалить всех фейковых игроков";
   private static final String KILL_MESSAGE_DESCRIPTION = "Добавляет сообщения после убийства для KillMessage";
   private static final String KILL_MESSAGE_DEFAULT_HELP = "Показать kill-сообщения";
   private static final String KILL_MESSAGE_ADD_TEXT_SYNTAX = "add <text...>";
   private static final String KILL_MESSAGE_ADD_TEXT_HELP = "Добавить сообщение после убийства";
   private static final String KILL_MESSAGE_REMOVE_INDEX_SYNTAX = "remove <index>";
   private static final String KILL_MESSAGE_REMOVE_INDEX_HELP = "Удалить сообщение по номеру";
   private static final String KILL_MESSAGE_LIST_SYNTAX = "list";
   private static final String KILL_MESSAGE_LIST_HELP = "Показать kill-сообщения";
   private static final String KILL_MESSAGE_CLEAR_SYNTAX = "clear";
   private static final String KILL_MESSAGE_CLEAR_HELP = "Очистить kill-сообщения";
   private static final String TOTEM_POP_MESSAGE_DESCRIPTION = "Добавляет сообщения после сноса тотема для KillMessage";
   private static final String TOTEM_POP_MESSAGE_DEFAULT_HELP = "Показать сообщения после сноса тотема";
   private static final String TOTEM_POP_MESSAGE_ADD_TEXT_SYNTAX = "add <text...>";
   private static final String TOTEM_POP_MESSAGE_ADD_TEXT_HELP = "Добавить сообщение после сноса тотема";
   private static final String TOTEM_POP_MESSAGE_REMOVE_INDEX_SYNTAX = "remove <index>";
   private static final String TOTEM_POP_MESSAGE_REMOVE_INDEX_HELP = "Удалить сообщение по номеру";
   private static final String TOTEM_POP_MESSAGE_LIST_SYNTAX = "list";
   private static final String TOTEM_POP_MESSAGE_LIST_HELP = "Показать pt-сообщения";
   private static final String TOTEM_POP_MESSAGE_CLEAR_SYNTAX = "clear";
   private static final String TOTEM_POP_MESSAGE_CLEAR_HELP = "Очистить pt-сообщения";
   private static final String IRC_DESCRIPTION = "Отправляет сообщения в общий IRC-чат клиента";
   private static final String IRC_MESSAGE_SYNTAX = "<сообщение...>";
   private static final String IRC_MESSAGE_HELP = "Отправить сообщение; модуль IRC должен быть включён";
   private static final String PARTY_DESCRIPTION = "Управляет IRC-пати и отправляет сообщения её участникам";
   private static final String PARTY_CREATE_NAME_PASSWORD_SYNTAX = "create <name> [password]";
   private static final String PARTY_CREATE_NAME_PASSWORD_HELP = "Создать пати с необязательным паролем";
   private static final String PARTY_JOIN_NAME_PASSWORD_SYNTAX = "join <name> [password]";
   private static final String PARTY_JOIN_NAME_PASSWORD_HELP = "Войти в пати; указать пароль, если он установлен";
   private static final String PARTY_LEAVE_SYNTAX = "leave";
   private static final String PARTY_LEAVE_HELP = "Выйти из пати";
   private static final String PARTY_DISBAND_SYNTAX = "disband";
   private static final String PARTY_DISBAND_HELP = "Распустить свою пати";
   private static final String PARTY_MESSAGE_SYNTAX = "<сообщение...>";
   private static final String PARTY_MESSAGE_HELP = "Отправить сообщение участникам пати";
   private static final String PARTY_TRACE_SYNTAX = "trace";
   private static final String PARTY_TRACE_HELP = "Показать состояние подключения и синхронизации пати";
   private static final String COMMAND_WHITESPACE_PATTERN = "\\s+";
   private static final String UNKNOWN_COMMAND_MESSAGE = "Команда не найдена";

   private void registerCommand(ClientCommand command) {
      this.registerLiteral(command, command.getName());

      for (String alias : command.getAliases()) {
         this.registerLiteral(command, alias);
      }

      RoseClient.getInstance().getEventBus().register(command);
   }

   @Generated
   public Consumer<Throwable> getErrorHandler() {
      return this.errorHandler;
   }

   public CommandRegistry() {
      WaypointManager.method__().ру();
      this.addDocumentedCommand(new HelpCommand(), HELP_DESCRIPTION, usageExample("", HELP_DEFAULT_HELP));
      this.addDocumentedCommand(
         new ConfigCommand(),
         CONFIG_DESCRIPTION,
         usageExample(CONFIG_SAVEALL_SYNTAX, CONFIG_SAVEALL_HELP),
         usageExample(CONFIG_LOADALL_SYNTAX, CONFIG_LOADALL_HELP),
         usageExample(CONFIG_SAVE_NAME_SYNTAX, CONFIG_SAVE_NAME_HELP),
         usageExample(CONFIG_LOAD_NAME_SYNTAX, CONFIG_LOAD_NAME_HELP),
         usageExample(CONFIG_LOAD_NAME_MODULE_SYNTAX, CONFIG_LOAD_NAME_MODULE_HELP),
         usageExample(CONFIG_REMOVE_NAME_SYNTAX, CONFIG_REMOVE_NAME_HELP),
         usageExample(CONFIG_LIST_SYNTAX, CONFIG_LIST_HELP),
         usageExample(CONFIG_DIR_SYNTAX, CONFIG_DIR_HELP),
         usageExample(CONFIG_RESET_SYNTAX, CONFIG_RESET_HELP)
      );
      this.addDocumentedCommand(new CustomNameCommand(), CUSTOM_NAME_DESCRIPTION, usageExample("", CUSTOM_NAME_DEFAULT_HELP), usageExample(CUSTOM_NAME_NAME_SYNTAX, CUSTOM_NAME_NAME_HELP), usageExample(CUSTOM_NAME_OFF_SYNTAX, CUSTOM_NAME_OFF_HELP));
      this.addDocumentedCommand(new FriendCommand(), FRIEND_DESCRIPTION, usageExample(FRIEND_ADD_NICK_FAKE_NAME_SYNTAX, FRIEND_ADD_NICK_FAKE_NAME_HELP), usageExample(FRIEND_REMOVE_NICK_SYNTAX, FRIEND_REMOVE_NICK_HELP), usageExample(FRIEND_LIST_SYNTAX, FRIEND_LIST_HELP), usageExample(FRIEND_CLEAR_SYNTAX, FRIEND_CLEAR_HELP));
      this.addDocumentedCommand(new TargetCommand(), TARGET_DESCRIPTION, usageExample("", TARGET_DEFAULT_HELP), usageExample(TARGET_NAME_SYNTAX, TARGET_NAME_HELP), usageExample(TARGET_OFF_SYNTAX, TARGET_OFF_HELP));
      this.addDocumentedCommand(new MacroCommand(), MACRO_DESCRIPTION, usageExample(MACRO_ADD_KEY_COMMAND_SYNTAX, MACRO_ADD_KEY_COMMAND_HELP), usageExample(MACRO_REMOVE_KEY_SYNTAX, MACRO_REMOVE_KEY_HELP), usageExample(MACRO_CLEAR_SYNTAX, MACRO_CLEAR_HELP));
      this.addDocumentedCommand(new VerticalClipCommand(), VERTICAL_CLIP_DESCRIPTION, usageExample(VERTICAL_CLIP_UP_SYNTAX, VERTICAL_CLIP_UP_HELP), usageExample(VERTICAL_CLIP_DOWN_SYNTAX, VERTICAL_CLIP_DOWN_HELP), usageExample(VERTICAL_CLIP_BLOCKS_SYNTAX, VERTICAL_CLIP_BLOCKS_HELP));
      this.addDocumentedCommand(new TeleportCommand(), TELEPORT_DESCRIPTION, usageExample(TELEPORT_NICK_SYNTAX, TELEPORT_NICK_HELP), usageExample(TELEPORT_X_Y_Z_SYNTAX, TELEPORT_X_Y_Z_HELP), usageExample(TELEPORT_RW_NICK_SYNTAX, TELEPORT_RW_NICK_HELP), usageExample(TELEPORT_RW_X_Z_SYNTAX, TELEPORT_RW_X_Z_HELP), usageExample(TELEPORT_STOP_SYNTAX, TELEPORT_STOP_HELP));
      this.addDocumentedCommand(new HorizontalClipCommand(), HORIZONTAL_CLIP_DESCRIPTION, usageExample(HORIZONTAL_CLIP_DISTANCE_SYNTAX, HORIZONTAL_CLIP_DISTANCE_HELP));
      this.addDocumentedCommand(
         new WaypointCommand(),
         WAYPOINT_DESCRIPTION,
         usageExample("", WAYPOINT_DEFAULT_HELP),
         usageExample(WAYPOINT_ADD_NAME_SYNTAX, WAYPOINT_ADD_NAME_HELP),
         usageExample(WAYPOINT_ADD_NAME_X_Y_Z_SYNTAX, WAYPOINT_ADD_NAME_X_Y_Z_HELP),
         usageExample(WAYPOINT_REMOVE_NAME_SYNTAX, WAYPOINT_REMOVE_NAME_HELP),
         usageExample(WAYPOINT_DELETE_NAME_SYNTAX, WAYPOINT_DELETE_NAME_HELP),
         usageExample(WAYPOINT_LIST_SYNTAX, WAYPOINT_LIST_HELP),
         usageExample(WAYPOINT_CLEAR_SYNTAX, WAYPOINT_CLEAR_HELP),
         usageExample(WAYPOINT_CLEARALL_SYNTAX, WAYPOINT_CLEARALL_HELP)
      );
      this.addDocumentedCommand(new GpsCommand(), GPS_DESCRIPTION, usageExample(GPS_X_Z_SYNTAX, GPS_X_Z_HELP), usageExample(GPS_INFO_SYNTAX, GPS_INFO_HELP), usageExample(GPS_OFF_SYNTAX, GPS_OFF_HELP));
      this.addDocumentedCommand(new BindCommand(), BIND_DESCRIPTION, usageExample(BIND_ADD_MODULE_KEY_SYNTAX, BIND_ADD_MODULE_KEY_HELP), usageExample(BIND_REMOVE_MODULE_KEY_SYNTAX, BIND_REMOVE_MODULE_KEY_HELP), usageExample(BIND_LIST_SYNTAX, BIND_LIST_HELP), usageExample(BIND_CLEAR_SYNTAX, BIND_CLEAR_HELP));
      this.addDocumentedCommand(new UnhookCommand(), UNHOOK_DESCRIPTION, usageExample("", UNHOOK_DEFAULT_HELP));
      this.addDocumentedCommand(new ReconnectCommand(), RECONNECT_DESCRIPTION, usageExample("", RECONNECT_DEFAULT_HELP), usageExample(RECONNECT_GRIEF_SYNTAX, RECONNECT_GRIEF_HELP));
      this.addDocumentedCommand(new AutoContractCommand(), AUTO_CONTRACT_DESCRIPTION, usageExample("", AUTO_CONTRACT_DEFAULT_HELP), usageExample(AUTO_CONTRACT_NICKNAME_SYNTAX, AUTO_CONTRACT_NICKNAME_HELP));
      this.addDocumentedCommand(new AuctionSellCommand(), AUCTION_SELL_DESCRIPTION, usageExample("", AUCTION_SELL_DEFAULT_HELP), usageExample(AUCTION_SELL_SELL_SYNTAX, AUCTION_SELL_SELL_HELP), usageExample(AUCTION_SELL_SELL_PERCENT_SYNTAX, AUCTION_SELL_SELL_PERCENT_HELP));
      this.addDocumentedCommand(new AutoWardenCommand(), AUTO_WARDEN_DESCRIPTION, usageExample(AUTO_WARDEN_ADD_ANARCHY_SYNTAX, AUTO_WARDEN_ADD_ANARCHY_HELP), usageExample(AUTO_WARDEN_REMOVE_ANARCHY_SYNTAX, AUTO_WARDEN_REMOVE_ANARCHY_HELP), usageExample(AUTO_WARDEN_LIST_SYNTAX, AUTO_WARDEN_LIST_HELP), usageExample(AUTO_WARDEN_CLEAR_SYNTAX, AUTO_WARDEN_CLEAR_HELP));
      this.addDocumentedCommand(new ChestStealerCommand(), CHEST_STEALER_DESCRIPTION, usageExample("", CHEST_STEALER_DEFAULT_HELP), usageExample(CHEST_STEALER_ADD_ITEM_SYNTAX, CHEST_STEALER_ADD_ITEM_HELP), usageExample(CHEST_STEALER_REMOVE_ITEM_SYNTAX, CHEST_STEALER_REMOVE_ITEM_HELP), usageExample(CHEST_STEALER_LIST_SYNTAX, CHEST_STEALER_LIST_HELP), usageExample(CHEST_STEALER_CLEAR_SYNTAX, CHEST_STEALER_CLEAR_HELP));
      this.addDocumentedCommand(new HeadSwapCommand(), HEAD_SWAP_DESCRIPTION, usageExample("", HEAD_SWAP_DEFAULT_HELP), usageExample(HEAD_SWAP_ADD_SYNTAX, HEAD_SWAP_ADD_HELP), usageExample(HEAD_SWAP_REMOVE_SYNTAX, HEAD_SWAP_REMOVE_HELP), usageExample(HEAD_SWAP_CLEAR_SYNTAX, HEAD_SWAP_CLEAR_HELP), usageExample(HEAD_SWAP_LIST_SYNTAX, HEAD_SWAP_LIST_HELP));
      this.addDocumentedCommand(new BlockEspCommand(), BLOCK_ESP_DESCRIPTION, usageExample(BLOCK_ESP_ADD_BLOCK_ENTITY_COLOR_SYNTAX, BLOCK_ESP_ADD_BLOCK_ENTITY_COLOR_HELP), usageExample(BLOCK_ESP_ADD_SHULKERS_COLOR_SYNTAX, BLOCK_ESP_ADD_SHULKERS_COLOR_HELP), usageExample(BLOCK_ESP_REMOVE_BLOCK_ENTITY_SYNTAX, BLOCK_ESP_REMOVE_BLOCK_ENTITY_HELP), usageExample(BLOCK_ESP_LIST_SYNTAX, BLOCK_ESP_LIST_HELP), usageExample(BLOCK_ESP_CLEAR_SYNTAX, BLOCK_ESP_CLEAR_HELP));
      this.addDocumentedCommand(
         new FakePlayerCommand(),
         FAKE_PLAYER_DESCRIPTION,
         usageExample("", FAKE_PLAYER_DEFAULT_HELP),
         usageExample(FAKE_PLAYER_ADD_NAME_SYNTAX, FAKE_PLAYER_ADD_NAME_HELP),
         usageExample(FAKE_PLAYER_REMOVE_NAME_SYNTAX, FAKE_PLAYER_REMOVE_NAME_HELP),
         usageExample(FAKE_PLAYER_EQUIP_NAME_SLOT_ITEM_COUNT_SYNTAX, FAKE_PLAYER_EQUIP_NAME_SLOT_ITEM_COUNT_HELP),
         usageExample(FAKE_PLAYER_GIVE_NAME_ITEM_COUNT_SYNTAX, FAKE_PLAYER_GIVE_NAME_ITEM_COUNT_HELP),
         usageExample(FAKE_PLAYER_COPY_NAME_SYNTAX, FAKE_PLAYER_COPY_NAME_HELP),
         usageExample(FAKE_PLAYER_LIST_SYNTAX, FAKE_PLAYER_LIST_HELP),
         usageExample(FAKE_PLAYER_CLEAR_SYNTAX, FAKE_PLAYER_CLEAR_HELP)
      );
      this.addDocumentedCommand(new KillMessageCommand(), KILL_MESSAGE_DESCRIPTION, usageExample("", KILL_MESSAGE_DEFAULT_HELP), usageExample(KILL_MESSAGE_ADD_TEXT_SYNTAX, KILL_MESSAGE_ADD_TEXT_HELP), usageExample(KILL_MESSAGE_REMOVE_INDEX_SYNTAX, KILL_MESSAGE_REMOVE_INDEX_HELP), usageExample(KILL_MESSAGE_LIST_SYNTAX, KILL_MESSAGE_LIST_HELP), usageExample(KILL_MESSAGE_CLEAR_SYNTAX, KILL_MESSAGE_CLEAR_HELP));
      this.addDocumentedCommand(new TotemPopMessageCommand(), TOTEM_POP_MESSAGE_DESCRIPTION, usageExample("", TOTEM_POP_MESSAGE_DEFAULT_HELP), usageExample(TOTEM_POP_MESSAGE_ADD_TEXT_SYNTAX, TOTEM_POP_MESSAGE_ADD_TEXT_HELP), usageExample(TOTEM_POP_MESSAGE_REMOVE_INDEX_SYNTAX, TOTEM_POP_MESSAGE_REMOVE_INDEX_HELP), usageExample(TOTEM_POP_MESSAGE_LIST_SYNTAX, TOTEM_POP_MESSAGE_LIST_HELP), usageExample(TOTEM_POP_MESSAGE_CLEAR_SYNTAX, TOTEM_POP_MESSAGE_CLEAR_HELP));
      this.addDocumentedCommand(new IrcCommand(), IRC_DESCRIPTION, usageExample(IRC_MESSAGE_SYNTAX, IRC_MESSAGE_HELP));
      this.addDocumentedCommand(new PartyCommand(), PARTY_DESCRIPTION, usageExample(PARTY_CREATE_NAME_PASSWORD_SYNTAX, PARTY_CREATE_NAME_PASSWORD_HELP), usageExample(PARTY_JOIN_NAME_PASSWORD_SYNTAX, PARTY_JOIN_NAME_PASSWORD_HELP), usageExample(PARTY_LEAVE_SYNTAX, PARTY_LEAVE_HELP), usageExample(PARTY_DISBAND_SYNTAX, PARTY_DISBAND_HELP), usageExample(PARTY_MESSAGE_SYNTAX, PARTY_MESSAGE_HELP), usageExample(PARTY_TRACE_SYNTAX, PARTY_TRACE_HELP));
      this.forEach(this::registerCommand);
   }

   private ClientCommand findCommand(String commandInput) {
      String commandName = commandInput.split(COMMAND_WHITESPACE_PATTERN, 2)[0];

      for (ClientCommand command : this) {
         if (commandName.equals(command.getName())) {
            return command;
         }

         for (String alias : command.getAliases()) {
            if (commandName.equals(alias)) {
               return command;
            }
         }
      }

      return null;
   }

   public boolean isClientCommand(String input) {
      String commandInput = input.startsWith(this.commandPrefix.toString()) ? input.substring(1) : input;
      return this.findCommand(commandInput) != null;
   }

   private static ClientCommand.UsageExample usageExample(String syntax, String description) {
      return new ClientCommand.UsageExample(syntax, description);
   }

   @Generated
   public void setCommandPrefix(Character prefix) {
      this.commandPrefix = prefix;
   }

   @Generated
   public Character getCommandPrefix() {
      return this.commandPrefix;
   }

   private void addDocumentedCommand(ClientCommand command, String description, ClientCommand.UsageExample... examples) {
      command.setDescription(description);
      command.setUsageExamples(examples);
      this.add(command);
   }

   public void executeChatCommand(String input) {
      try {
         if (RoseClient.getInstance().getServices().getUnhookFiles().аА()) {
            return;
         }

         if (!input.startsWith(this.commandPrefix.toString())) {
            return;
         }

         input = input.substring(1);
         if (input.isEmpty()) {
            return;
         }

         try {
            this.dispatcher.execute(input, null);
         } catch (CommandSyntaxException syntaxError) {
            ClientCommand command = this.findCommand(input);
            if (command == null) {
               ClientChat.З(UNKNOWN_COMMAND_MESSAGE);
            } else {
               command.printHelp(this.commandPrefix.toString());
            }
         }
      } catch (Throwable failure) {
         this.errorHandler.accept(failure);
      }
   }

   @Generated
   public CommandDispatcher<ClientSuggestionProvider> getDispatcher() {
      return this.dispatcher;
   }

   private void registerLiteral(ClientCommand command, String literalName) {
      LiteralArgumentBuilder commandBuilder = command.literal(literalName);
      command.build(commandBuilder);
      command.attachHelpHandlers(commandBuilder, () -> this.commandPrefix.toString());
      this.dispatcher.register(commandBuilder);
   }
}
