package sg.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.client.RoseClient;
import sg.command.ClientCommand;

public final class UnhookCommand extends ClientCommand {
   private static final String COMMAND_NAME = "unhook";

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.executes(commandContext -> {
         RoseClient.getInstance().getServices().getUnhookFiles().ам();
         return 1;
      });
   }

   public UnhookCommand() {
      super(COMMAND_NAME);
   }
}
