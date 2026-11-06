package sg.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.client.RoseClient;
import sg.modules.impl.misc.NameProtect;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public final class CustomNameCommand extends ClientCommand {
   private static final String COMMAND_NAME = "customn";
   private static final String OFF_LITERAL = "off";
   private static final String NAME_ARGUMENT_NAME = "name";
   private static final String DEFAULT_PROTECTED_NAME = "Protected";
   private static final String CUSTOM_NAME_RESET_MESSAGE = "Кастомный ник сброшен. Состояние NameProtect не изменено.";
   private static final String NAME_REQUIRED_MESSAGE = "Укажите ник: .customn <ник>";
   private static final String NAME_ARGUMENT_KEY = "name";

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      ((LiteralArgumentBuilder)commandBuilder.executes(commandContext -> {
            ClientChat.З("Текущий отображаемый ник: " + this.getNameProtect().фи());
            return 1;
         }))
         .then(this.literal(OFF_LITERAL).executes(commandContext -> this.resetCustomName()))
         .then(this.argument(NAME_ARGUMENT_NAME, StringArgumentType.greedyString()).executes(commandContext -> this.setCustomName(StringArgumentType.getString(commandContext, NAME_ARGUMENT_KEY))));
   }

   private NameProtect getNameProtect() {
      return RoseClient.getInstance().getServices().getModules().getNameProtect();
   }

   private int setCustomName(String name) {
      String trimmedName = name.trim();
      if (trimmedName.isEmpty()) {
         ClientChat.З(NAME_REQUIRED_MESSAGE);
         return 1;
      }

      NameProtect nameProtect = this.getNameProtect();
      nameProtect.西(trimmedName);
      if (!nameProtect.isEnabled()) {
         nameProtect.enable();
      }

      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
      ClientChat.З("Отображаемый ник установлен: " + nameProtect.фи());
      return 1;
   }

   private int resetCustomName() {
      NameProtect nameProtect = this.getNameProtect();
      nameProtect.西(DEFAULT_PROTECTED_NAME);
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
      ClientChat.З(CUSTOM_NAME_RESET_MESSAGE);
      return 1;
   }

   public CustomNameCommand() {
      super(COMMAND_NAME);
   }
}
