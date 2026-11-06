package sg.modules.impl.init;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.Generated;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import sg.ec.MinecraftAccess;
import sg.client.RoseClient;

public class InventoryActionCoordinator implements MinecraftAccess {
   private static Object а;
   private final TickTimer п = new TickTimer();
   private InventoryActionCoordinator.State В = InventoryActionCoordinator.State.ACTIVE;

   public InventoryActionCoordinator.Result л(Runnable var1, boolean var2) {
      return this.л(var1, RoseClient.getInstance().getServices().getModules().getGuiMove().И_989fed9a().getValue().intValue());
   }

   public InventoryActionCoordinator.Result л(Runnable var1, int var2) {
      if (Гм()) {
         return InventoryActionCoordinator.Result.WAIT;
      }

      KeyMapping[] var3 = new KeyMapping[]{MINECRAFT_CLIENT.options.keyUp, MINECRAFT_CLIENT.options.keyDown, MINECRAFT_CLIENT.options.keyLeft, MINECRAFT_CLIENT.options.keyRight, MINECRAFT_CLIENT.options.keyJump, MINECRAFT_CLIENT.options.keySprint};

      for (KeyMapping var7 : var3) {
         var7.setDown(false);
      }

      RoseClient.getInstance().getServices().getModules().getGuiMove().з(true);
      if (!MovementUtils.hasMovementInput()) {
         if (this.В == InventoryActionCoordinator.State.ACTIVE) {
            var1.run();
            this.п.reset();
            this.В = InventoryActionCoordinator.State.WAIT;
         }

         if (var2 <= 0 || this.п.hasElapsed(var2 - 1)) {
            RoseClient.getInstance().getServices().getModules().getGuiMove().з(false);
            if (MINECRAFT_CLIENT.gui.screen() == null) {
               MINECRAFT_CLIENT.player.connection.send(new ServerboundContainerClosePacket(0));

               for (KeyMapping var12 : var3) {
                  boolean var8 = InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), var12.key.getValue());
                  var12.setDown(var8);
               }
            }

            this.В = InventoryActionCoordinator.State.ACTIVE;
            return InventoryActionCoordinator.Result.SUCCESS;
         }
      } else {
         this.п.reset();
      }

      return InventoryActionCoordinator.Result.WAIT;
   }

   @Generated
   public InventoryActionCoordinator.State л() {
      return this.В;
   }

   public static synchronized boolean и(Object var0) {
      if (var0 == null) {
         return false;
      }

      if (а == null) {
         а = var0;
      }

      return а == var0;
   }

   public static synchronized void к(Object var0) {
      if (а == var0) {
         а = null;
      }
   }

   private static synchronized boolean Гм() {
      return а != null;
   }

   public void яш() {
      this.В = InventoryActionCoordinator.State.ACTIVE;
   }

   public enum State {
      WAIT,
      ACTIVE;
   }

   public enum Result {
      ERROR,
      SUCCESS,
      WAIT;
   }
}
