package sg.ec;

import java.util.UUID;
import lombok.Generated;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;

public final class PlayerLatency implements MinecraftAccess {
   private static final String UTILITY_CONSTRUCTOR_MESSAGE = "This is a utility class and cannot be instantiated";

   public static int getLatencyMs(UUID playerId) {
      ClientPacketListener connection = MINECRAFT_CLIENT.getConnection();
      if (connection == null) {
         return -1;
      }

      PlayerInfo playerInfo = connection.getPlayerInfo(playerId);
      return playerInfo == null ? -1 : playerInfo.getLatency();
   }

   @Generated
   private PlayerLatency() {
      throw new UnsupportedOperationException(UTILITY_CONSTRUCTOR_MESSAGE);
   }
}
