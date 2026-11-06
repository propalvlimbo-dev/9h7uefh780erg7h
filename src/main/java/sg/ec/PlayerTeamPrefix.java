package sg.ec;

import lombok.Generated;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;

public final class PlayerTeamPrefix {
   private static final String UTILITY_CONSTRUCTOR_MESSAGE = "This is a utility class and cannot be instantiated";

   public static Component getPrefix(Player player) {
      PlayerTeam team = player.getTeam();
      return team != null ? team.getPlayerPrefix() : Component.empty();
   }

   @Generated
   private PlayerTeamPrefix() {
      throw new UnsupportedOperationException(UTILITY_CONSTRUCTOR_MESSAGE);
   }
}
