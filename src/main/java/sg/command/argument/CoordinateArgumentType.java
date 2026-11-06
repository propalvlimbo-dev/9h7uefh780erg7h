package sg.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import sg.ec.MinecraftAccess;

public class CoordinateArgumentType implements MinecraftAccess, ArgumentType<Integer> {
   private final @com.google.gson.annotations.SerializedName("В")  CoordinateArgumentType.Axis axis;

   @Override
   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder suggestionsBuilder) {
      if (this.axis == CoordinateArgumentType.Axis.X) {
         suggestionsBuilder.suggest((int)Math.round(MINECRAFT_CLIENT.player.getX()));
      } else if (this.axis == CoordinateArgumentType.Axis.Y) {
         suggestionsBuilder.suggest((int)Math.round(MINECRAFT_CLIENT.player.getY()));
      } else {
         suggestionsBuilder.suggest((int)Math.round(MINECRAFT_CLIENT.player.getZ()));
      }

      return suggestionsBuilder.buildFuture();
   }

   public CoordinateArgumentType(CoordinateArgumentType.Axis axis) {
      this.axis = axis;
   }

   public Integer parse(StringReader reader) throws CommandSyntaxException {
      return reader.readInt();
   }

   public static CoordinateArgumentType coordinate(CoordinateArgumentType.Axis axis) {
      return new CoordinateArgumentType(axis);
   }

   public enum Axis {
      X,
      Y,
      Z;
   }
}
