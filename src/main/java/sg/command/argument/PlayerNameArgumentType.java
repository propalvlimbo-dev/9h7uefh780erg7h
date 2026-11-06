package sg.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;
import settings.render.PlayerNamePatterns;
import sg.ec.MinecraftAccess;

public class PlayerNameArgumentType implements MinecraftAccess, ArgumentType<String> {
   private final @com.google.gson.annotations.SerializedName("В")  PlayerNameArgumentType.PlayerScope playerScope;

   public String parse(StringReader reader) throws CommandSyntaxException {
      return reader.readUnquotedString();
   }

   public static PlayerNameArgumentType players(PlayerNameArgumentType.PlayerScope playerScope) {
      return new PlayerNameArgumentType(playerScope);
   }

   public PlayerNameArgumentType(PlayerNameArgumentType.PlayerScope playerScope) {
      this.playerScope = playerScope;
   }

   @Override
   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder suggestionsBuilder) {
      StringReader remainingReader = new StringReader(suggestionsBuilder.getRemaining());
      if (this.playerScope == PlayerNameArgumentType.PlayerScope.ALL) {
         for (PlayerInfo listedPlayerInfo : MINECRAFT_CLIENT.player.connection.getListedOnlinePlayers()) {
            if (PlayerNamePatterns.必().matcher(listedPlayerInfo.getProfile().name()).matches()
               && listedPlayerInfo.getProfile().name().toLowerCase().startsWith(remainingReader.getString().toLowerCase())) {
               suggestionsBuilder.suggest(listedPlayerInfo.getProfile().name());
            }
         }
      } else {
         for (Player nearbyPlayer : MINECRAFT_CLIENT.level.players()) {
            if (nearbyPlayer.getGameProfile().name().toLowerCase().startsWith(remainingReader.getString().toLowerCase())) {
               suggestionsBuilder.suggest(nearbyPlayer.getGameProfile().name());
            }
         }
      }

      return suggestionsBuilder.buildFuture();
   }

   public enum PlayerScope {
      ALL,
      NEARBY;
   }
}
