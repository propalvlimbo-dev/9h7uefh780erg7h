package sg.modules.impl.init;

import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.entity.player.Player;
import sg.ec.MinecraftAccess;
import sg.client.RoseClient;

public final class HotbarSelectionSession implements MinecraftAccess {
   private int пф = -1;
   private int пи = -1;
   private int п9 = -1;
   private Player Ъ;

   private static void Ык(int var0) {
      MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(var0);
      MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
      if (RoseClient.getInstance().getControllers().getServerPlayers().ПO() != var0) {
         MINECRAFT_CLIENT.player.connection.send(new ServerboundSetCarriedItemPacket(var0));
      }
   }

   public void еЬ() {
      if (this.пф != -1 && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player == this.Ъ && MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.player.getInventory().getSelectedSlot() == this.пи) {
         Ык(this.пф);
      }

      this.пф = this.пи = this.п9 = -1;
      this.Ъ = null;
   }

   public boolean Я(int var1, boolean var2) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null && var1 >= 0 && var1 <= 8) {
         if (this.Ъ != MINECRAFT_CLIENT.player) {
            this.пф = this.пи = this.п9 = -1;
            this.Ъ = MINECRAFT_CLIENT.player;
         }

         int var3 = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
         boolean var4 = this.пи != var1 || var3 != var1 || RoseClient.getInstance().getControllers().getServerPlayers().ПO() != var1;
         if (this.пф == -1 || var3 != this.пи) {
            this.пф = var3;
         }

         this.пи = var1;
         if (var4) {
            this.п9 = MINECRAFT_CLIENT.player.tickCount;
         }

         Ык(var1);
         return !var2 || MINECRAFT_CLIENT.player.tickCount > this.п9;
      } else {
         return false;
      }
   }
}
