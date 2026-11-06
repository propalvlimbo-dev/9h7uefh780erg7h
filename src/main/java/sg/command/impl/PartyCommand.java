package sg.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.handler.PartyTrace;
import sg.handler.PartyClient;
import sg.command.ClientCommand;
import sg.network.ClientChat;

public class PartyCommand extends ClientCommand {
   private static final String COMMAND_NAME = "party";
   private static final String TRACE_LITERAL = "trace";
   private static final String CREATE_LITERAL = "create";
   private static final String JOIN_LITERAL = "join";
   private static final String LEAVE_LITERAL = "leave";
   private static final String DISBAND_LITERAL = "disband";
   private static final String MESSAGE_ARGUMENT_NAME = "message";
   private static final String PARTY_NAME_ARGUMENT_NAME = "name";
   private static final String PARTY_PASSWORD_ARGUMENT_NAME = "password";
   private static final String PARTY_NAME_WITH_PASSWORD_KEY = "name";
   private static final String PARTY_PASSWORD_KEY = "password";
   private static final String PARTY_NAME_KEY = "name";
   private static final String MESSAGE_ARGUMENT_KEY = "message";
   private static final String MANUAL_STATUS_TRACE_LABEL = "MANUAL_STATUS";
   private static final String TRACE_ENABLED_MESSAGE = "включён. ";
   private static final String TRACE_DISABLED_MESSAGE = "выключен. ";

   public PartyCommand() {
      super(COMMAND_NAME);
   }

   private LiteralArgumentBuilder<ClientSuggestionProvider> buildPartyAccessBranch(String literalName, boolean createParty) {
      return this.literal(literalName)
         .then(
            this.argument(PARTY_NAME_ARGUMENT_NAME, StringArgumentType.word())
               .executes(commandContext -> PartyClient.Х_4d6328fc().Х(commandContext.getArgument(PARTY_NAME_KEY, String.class), "", createParty) ? 1 : 0)
               .then(
                  this.argument(PARTY_PASSWORD_ARGUMENT_NAME, StringArgumentType.word())
                     .executes(commandContext -> PartyClient.Х_4d6328fc().Х(commandContext.getArgument(PARTY_NAME_WITH_PASSWORD_KEY, String.class), commandContext.getArgument(PARTY_PASSWORD_KEY, String.class), createParty) ? 1 : 0)
               )
         );
   }

   @Override
   public void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder) {
      commandBuilder.then(this.literal(TRACE_LITERAL).executes(commandContext -> {
         PartyTrace.Ы(MANUAL_STATUS_TRACE_LABEL, PartyClient.Х_4d6328fc().ь_());
         ClientChat.З("Party trace " + (PartyTrace.IИ() ? TRACE_ENABLED_MESSAGE : TRACE_DISABLED_MESSAGE) + PartyClient.Х_4d6328fc().ь_());
         return 1;
      }));
      commandBuilder.then(this.buildPartyAccessBranch(CREATE_LITERAL, true));
      commandBuilder.then(this.buildPartyAccessBranch(JOIN_LITERAL, false));
      commandBuilder.then(this.literal(LEAVE_LITERAL).executes(commandContext -> PartyClient.Х_4d6328fc().Х6() ? 1 : 0));
      commandBuilder.then(this.literal(DISBAND_LITERAL).executes(commandContext -> PartyClient.Х_4d6328fc().Хл() ? 1 : 0));
      commandBuilder.then(this.argument(MESSAGE_ARGUMENT_NAME, StringArgumentType.greedyString()).executes(commandContext -> PartyClient.Х_4d6328fc().Й(commandContext.getArgument(MESSAGE_ARGUMENT_KEY, String.class)) ? 1 : 0));
   }
}
