package sg.command;

import com.google.gson.annotations.SerializedName;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import lombok.Generated;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import sg.network.ClientChat;

public abstract class ClientCommand {
   @SerializedName("_")
   private final String commandName;
   private final @com.google.gson.annotations.SerializedName("Х")  String[] aliases;
   private @com.google.gson.annotations.SerializedName("Ш")  String description;
   private @com.google.gson.annotations.SerializedName("ж")  List<ClientCommand.UsageExample> usageExamples = List.of();
   private static final String HELP_LITERAL = "help";
   private static final String ALIAS_SEPARATOR = ", ";
   private static final String MISSING_DESCRIPTION_MESSAGE = "Описание отсутствует";
   private static final String ARGUMENT_NOTATION_HELP = "<...> — обязательно, [...] — необязательно";

   public void attachHelpHandlers(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder, Supplier<String> prefixSupplier) {
      if (commandBuilder.getCommand() == null) {
         commandBuilder.executes(commandContext -> this.printHelp((String)prefixSupplier.get()));
      }

      commandBuilder.then(this.literal(HELP_LITERAL).executes(commandContext -> this.printHelp((String)prefixSupplier.get())));
   }

   public void setUsageExamples(ClientCommand.UsageExample... examples) {
      this.usageExamples = List.copyOf(Arrays.asList(examples));
   }

   protected <T> RequiredArgumentBuilder<ClientSuggestionProvider, T> argument(String argumentName, ArgumentType<T> argumentType) {
      return RequiredArgumentBuilder.argument(argumentName, argumentType);
   }

   @Generated
   public String[] getAliases() {
      return this.aliases;
   }

   public LiteralArgumentBuilder<ClientSuggestionProvider> literal(String literalName) {
      return LiteralArgumentBuilder.literal(literalName);
   }

   public abstract void build(LiteralArgumentBuilder<ClientSuggestionProvider> commandBuilder);

   @Generated
   public String getName() {
      return this.commandName;
   }

   @Generated
   public void setDescription(String description) {
      this.description = description;
   }

   @Generated
   public List<ClientCommand.UsageExample> getUsageExamples() {
      return this.usageExamples;
   }

   public ClientCommand(String name, String... aliases) {
      this.commandName = name;
      this.aliases = aliases;
   }

   public int printHelp(String commandPrefix) {
      String prefixedCommandName = commandPrefix + this.commandName;
      String aliasSummary = this.aliases.length == 0 ? "" : " | Алиасы: " + String.join(ALIAS_SEPARATOR, Arrays.stream(this.aliases).map(alias -> commandPrefix + alias).toList());
      ClientChat.З("Помощь по " + prefixedCommandName + aliasSummary);
      ClientChat.З(this.description != null && !this.description.isBlank() ? this.description : MISSING_DESCRIPTION_MESSAGE);
      ClientChat.З(ARGUMENT_NOTATION_HELP);
      if (this.usageExamples.isEmpty()) {
         ClientChat.З(prefixedCommandName);
         return 1;
      }

      for (ClientCommand.UsageExample example : this.usageExamples) {
         String exampleSyntax = example.syntax().isBlank() ? prefixedCommandName : prefixedCommandName + " " + example.syntax();
         ClientChat.З(exampleSyntax + " — " + example.description());
      }

      return 1;
   }

   @Generated
   public String getDescription() {
      return this.description;
   }

   public record UsageExample(String syntax, String description) {
   }
}
