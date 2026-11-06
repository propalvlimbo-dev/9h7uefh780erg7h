package sg.ec;

import lombok.Generated;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import sg.client.RoseClient;

public final class EntityHealth implements MinecraftAccess {
   private static final String UTILITY_CONSTRUCTOR_MESSAGE = "This is a utility class and cannot be instantiated";

   @Generated
   private EntityHealth() {
      throw new UnsupportedOperationException(UTILITY_CONSTRUCTOR_MESSAGE);
   }

   public static float getHealth(LivingEntity entity) {
      float health = entity.getHealth();
      ScoreHolder scoreHolder = ScoreHolder.forNameOnly(entity.getScoreboardName());
      if (MINECRAFT_CLIENT.level.getScoreboard().getDisplayObjective(DisplaySlot.BELOW_NAME) != null && entity instanceof Player player) {
         ScoreAccess score = MINECRAFT_CLIENT.level.getScoreboard().getOrCreatePlayerScore(scoreHolder, MINECRAFT_CLIENT.level.getScoreboard().getDisplayObjective(DisplaySlot.BELOW_NAME));
         if (MINECRAFT_CLIENT.getCurrentServer() != null && RoseClient.getInstance().getServices().getModules().getScoreboardHealth().isEnabled()) {
            health = score.get();
         }
      }

      return health;
   }
}
