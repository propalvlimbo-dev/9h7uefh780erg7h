package sg.command.impl;

import com.google.gson.annotations.SerializedName;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.handler.PartyClient;
import sg.client.RoseClient;
import sg.command.ClientCommand;
import sg.network.ClientChat;
import sg.network.IrcRoles;

public class IrcCommand extends ClientCommand {
   private static final long field_8 = 2000000000L;
   @SerializedName("5")
   private long lastMessageNanoTime;
   private static final String COMMAND_NAME = "irc";
   private static final String STRING_ARGUMENT_NAME = "string";
   private static final String STRING_ARGUMENT_KEY = "string";
   private static final String IRC_DISABLED_MESSAGE = "Модуль IRC выключен.";
   private static final long MESSAGE_COOLDOWN_NANOS = 2000000000L;
   private static final long MILLISECOND_CEILING_OFFSET = 999999L;
   private static final long NANOS_PER_MILLISECOND = 1000000L;

   public IrcCommand() {
      super(COMMAND_NAME);
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(this.argument(STRING_ARGUMENT_NAME, StringArgumentType.greedyString()).executes(commandContext -> {
         String message = commandContext.getArgument(STRING_ARGUMENT_KEY, String.class);
         if (!RoseClient.getInstance().getServices().getModules().getIrcModule().isEnabled()) {
            ClientChat.З(IRC_DISABLED_MESSAGE);
            return 0;
         }

         long nowNanoTime = System.nanoTime();
         if (!IrcRoles.рП() && this.lastMessageNanoTime != 0L) {
            long remainingCooldownNanos = MESSAGE_COOLDOWN_NANOS - (nowNanoTime - this.lastMessageNanoTime);
            if (remainingCooldownNanos > 0L) {
               long remainingCooldownMillis = (remainingCooldownNanos + MILLISECOND_CEILING_OFFSET) / NANOS_PER_MILLISECOND;
               ClientChat.З("Подождите " + remainingCooldownMillis + " мс. перед следующим сообщением в IRC.");
               return 0;
            }
         }

         if (!PartyClient.Х_4d6328fc().У(message)) {
            return 0;
         }

         if (!IrcRoles.рП()) {
            this.lastMessageNanoTime = nowNanoTime;
         }

         return 1;
      }));
   }
}
