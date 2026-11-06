package sg.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import sg.client.RoseClient;
import sg.modules.ClientModule;

public class ModuleNameArgument implements ArgumentType<String> {
   private static final String QUOTE_CHARACTER = "\"";

   public String parse(StringReader reader) throws CommandSyntaxException {
      String requestedModuleName = reader.readString();
      return RoseClient.getInstance()
         .getServices()
         .getModules()
         .stream()
         .filter(moduleEntry -> moduleEntry.getName().equals(requestedModuleName.replace(QUOTE_CHARACTER, "")))
         .findFirst()
         .get()
         .getName();
   }

   public static ModuleNameArgument modules() {
      return new ModuleNameArgument();
   }

   @Override
   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder suggestionsBuilder) {
      for (ClientModule module : RoseClient.getInstance().getServices().getModules()) {
         if (module.getName().toLowerCase().startsWith(suggestionsBuilder.getRemaining().toLowerCase())) {
            suggestionsBuilder.suggest("\"" + module.getName() + "\"");
         }
      }

      return suggestionsBuilder.buildFuture();
   }
}
