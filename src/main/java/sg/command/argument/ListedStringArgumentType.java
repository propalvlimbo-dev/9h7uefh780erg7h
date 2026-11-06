package sg.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Generated;

public class ListedStringArgumentType implements ArgumentType<String> {
   private @com.google.gson.annotations.SerializedName("сд")  List<String> suggestionValues;

   @Override
   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder suggestionsBuilder) {
      StringReader remainingReader = new StringReader(suggestionsBuilder.getRemaining());

      for (String listedValue : this.suggestionValues) {
         if (listedValue.toLowerCase().startsWith(remainingReader.getString().toLowerCase())) {
            suggestionsBuilder.suggest(listedValue);
         }
      }

      return suggestionsBuilder.buildFuture();
   }

   public String parse(StringReader reader) throws CommandSyntaxException {
      String parsedValue = reader.readUnquotedString();
      return this.suggestionValues.stream().filter(listedValue -> listedValue.equalsIgnoreCase(parsedValue)).findFirst().get();
   }

   @Generated
   public void setList(List<String> suggestionValues) {
      this.suggestionValues = suggestionValues;
   }
}
