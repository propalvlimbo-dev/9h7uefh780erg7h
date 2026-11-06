package sg.ec;

import net.minecraft.core.BlockPos;
import sg.network.ClientChat;
import sg.events.EventListener;
import sg.events.impl.PlayerTickEvent;

public class DeathCoordinatesNotifier extends ClientService {
   private boolean З;
   private final EventListener<PlayerTickEvent> ы = this::ю;

   private void ю(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null) {
         if (MINECRAFT_CLIENT.player.getHealth() <= 0.0F) {
            if (!this.З) {
               this.З = true;
               BlockPos var2 = MINECRAFT_CLIENT.player.blockPosition();
               ClientChat.З("Вы умерли на кординатах " + var2.getX() + " " + var2.getY() + " " + var2.getZ());
            }
         } else {
            this.З = false;
         }
      }
   }
}
