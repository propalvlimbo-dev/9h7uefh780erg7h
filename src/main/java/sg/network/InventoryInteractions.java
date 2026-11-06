package sg.network;

import lombok.Generated;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.state.BlockState;
import sg.ec.MinecraftAccess;
import sg.ec.TranslationKeyProvider;
import sg.mixin.MultiPlayerGameModeAccessor;
import sg.client.RoseClient;

public final class InventoryInteractions implements MinecraftAccess {
   private static final String ВМк = "This is a utility class and cannot be instantiated";

   public static void Ъ_de5a6f78(Item var0) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
         int var1 = О(var0);
         if (var1 != -1) {
            int var2 = MINECRAFT_CLIENT.player.getInventory().selected;
            boolean var3 = var1 < 9;
            if (var3 && var1 == var2) {
               if (MINECRAFT_CLIENT.player.isUsingItem() && !MINECRAFT_CLIENT.player.isBlocking()) {
                  MINECRAFT_CLIENT.getConnection()
                     .send(
                        new ServerboundUseItemPacket(
                           InteractionHand.OFF_HAND,
                           MINECRAFT_CLIENT.level.getBlockStatePredictionHandler().startPredicting().currentSequence(),
                           MINECRAFT_CLIENT.player.getYRot(),
                           MINECRAFT_CLIENT.player.getXRot()
                        )
                     );
               } else {
                  MINECRAFT_CLIENT.getConnection()
                     .send(
                        new ServerboundUseItemPacket(
                           InteractionHand.MAIN_HAND,
                           MINECRAFT_CLIENT.level.getBlockStatePredictionHandler().startPredicting().currentSequence(),
                           MINECRAFT_CLIENT.player.getYRot(),
                           MINECRAFT_CLIENT.player.getXRot()
                        )
                     );
               }
            } else if (var3) {
               О(var1, var2);
            } else {
               int var4 = var2 % 8 + 1;
               MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var1, var4, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
               О(var4, var2);
               MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var1, var4, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
            }
         }
      }
   }

   public static void щ(int var0, int var1) {
      if (var0 != var1) {
         var0 = var0 < 9 ? var0 + 36 : var0;
         MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var0, 0, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var1, 0, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var0, 0, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
      }
   }

   public static void Ъ(int var0, int var1) {
      if (var0 != -1 && var1 != -1) {
         MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, var0, var1, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         MINECRAFT_CLIENT.getConnection().send(new ServerboundContainerClosePacket(0));
      }
   }

   public static boolean Ъ_bbeebd87(Item var0) {
      return MINECRAFT_CLIENT.player == null ? false : MINECRAFT_CLIENT.player.getMainHandItem().is(var0) || MINECRAFT_CLIENT.player.getOffhandItem().is(var0);
   }

   public static int Ъ_a83dd0cc(Item var0) {
      return Ъ(var0, 0, 35);
   }

   @Generated
   private InventoryInteractions() {
      throw new UnsupportedOperationException(ВМк);
   }

   public static int Ъ_a83dd0cc(int var0) {
      return var0 >= 0 && var0 <= 8 ? 36 + var0 : var0;
   }

   public static int Ц_a83dd0cc() {
      int var0 = -1;
      int var1 = -1;

      for (int var2 = 0; var2 < MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().size(); var2++) {
         ItemStack var3 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var2);
         if (!var3.isEmpty() && !var3.is(Items.ELYTRA)) {
            Equippable var4 = var3.get(DataComponents.EQUIPPABLE);
            if (var4 != null && var4.slot() == EquipmentSlot.CHEST) {
               int var5 = О(var3);
               if (var5 > var1) {
                  var1 = var5;
                  var0 = var2;
               }
            }
         }
      }

      return var0 >= 0 && var0 < 9 ? var0 + 36 : var0;
   }

   public static void О(InventoryInteractions.SlotSwitchMode var0, int var1, int var2) {
      if (var1 != -1 && var2 != -1 && var1 != RoseClient.getInstance().getControllers().getServerPlayers().ПO()) {
         switch (var0) {
            case Normal:
               MINECRAFT_CLIENT.player.getInventory().selected = var1;
               ((MultiPlayerGameModeAccessor)MINECRAFT_CLIENT.gameMode).syncSelectedSlot$drug();
               break;
            case Silent:
               MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(var1));
               break;
            case Alternative:
               Ъ(InventoryInteractions.SwapMode.Swap, var1, var2);
         }
      }
   }

   public static void Ъ(InventoryInteractions.SwingMode var0) {
      switch (var0) {
         case MainHand:
            MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
            break;
         case OffHand:
            MINECRAFT_CLIENT.player.swing(InteractionHand.OFF_HAND);
            break;
         case Packet:
            MINECRAFT_CLIENT.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
      }
   }

   private static void О(int var0, int var1) {
      if (MINECRAFT_CLIENT.player != null) {
         if (MINECRAFT_CLIENT.player.isUsingItem() && !MINECRAFT_CLIENT.player.isBlocking()) {
            щ(var0, 45);
            MINECRAFT_CLIENT.getConnection()
               .send(
                  new ServerboundUseItemPacket(
                     InteractionHand.OFF_HAND,
                     MINECRAFT_CLIENT.level.getBlockStatePredictionHandler().startPredicting().currentSequence(),
                     MINECRAFT_CLIENT.player.getYRot(),
                     MINECRAFT_CLIENT.player.getXRot()
                  )
               );
            щ(var0, 45);
         } else {
            MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(var0));
            MINECRAFT_CLIENT.getConnection()
               .send(
                  new ServerboundUseItemPacket(
                     InteractionHand.MAIN_HAND,
                     MINECRAFT_CLIENT.level.getBlockStatePredictionHandler().startPredicting().currentSequence(),
                     MINECRAFT_CLIENT.player.getYRot(),
                     MINECRAFT_CLIENT.player.getXRot()
                  )
               );
            MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(var1));
         }
      }
   }

   public static int О(Item var0) {
      if (MINECRAFT_CLIENT != null && MINECRAFT_CLIENT.player != null) {
         for (int var1 = 0; var1 < 36; var1++) {
            ItemStack var2 = MINECRAFT_CLIENT.player.getInventory().getItem(var1);
            if (!var2.isEmpty() && var2.getItem() == var0) {
               return var1;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public static void Ъ(InventoryInteractions.SwapMode var0, int var1, int var2) {
      if (var1 != -1 && var2 != -1) {
         switch (var0) {
            case Pickup:
               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.containerMenu.containerId, Ъ_a83dd0cc(var1), 0, ContainerInput.PICKUP, MINECRAFT_CLIENT.player);
               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.containerMenu.containerId, Ъ_a83dd0cc(var2), 0, ContainerInput.PICKUP, MINECRAFT_CLIENT.player);
               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.containerMenu.containerId, Ъ_a83dd0cc(var1), 0, ContainerInput.PICKUP, MINECRAFT_CLIENT.player);
               break;
            case Swap:
               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.containerMenu.containerId, var1, var2, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         }
      }
   }

   private static int О(ItemStack var0) {
      if (var0.is(Items.NETHERITE_CHESTPLATE)) {
         return 5;
      } else if (var0.is(Items.DIAMOND_CHESTPLATE)) {
         return 4;
      } else if (var0.is(Items.IRON_CHESTPLATE)) {
         return 3;
      } else if (var0.is(Items.CHAINMAIL_CHESTPLATE)) {
         return 2;
      } else if (var0.is(Items.GOLDEN_CHESTPLATE)) {
         return 1;
      } else {
         return var0.is(Items.LEATHER_CHESTPLATE) ? 0 : -1;
      }
   }

   public static int Ъ(Item var0, int var1, int var2) {
      for (int var3 = var2; var3 >= var1; var3--) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var3).getItem() == var0) {
            return var3;
         }
      }

      return -1;
   }

   public static int щ(Item var0) {
      if (MINECRAFT_CLIENT.player == null) {
         return 0;
      }

      int var1 = 0;

      for (ItemStack var3 : MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems()) {
         if (!var3.isEmpty() && var3.getItem().equals(var0)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static int Ч() {
      for (int var0 = 0; var0 < 45; var0++) {
         ItemStack var1 = MINECRAFT_CLIENT.player.getInventory().getItem(var0);
         if (!var1.isEmpty() && var1.getItem() instanceof AxeItem) {
            return var0;
         }
      }

      return -1;
   }

   public static void Ъ(InventoryInteractions.SlotSwitchMode var0, int var1, int var2) {
      if (var1 != -1 && var2 != -1 && var1 != RoseClient.getInstance().getControllers().getServerPlayers().ПO()) {
         switch (var0) {
            case Normal:
               MINECRAFT_CLIENT.player.getInventory().selected = var2;
               ((MultiPlayerGameModeAccessor)MINECRAFT_CLIENT.gameMode).syncSelectedSlot$drug();
               break;
            case Silent:
               MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(var2));
               break;
            case Alternative:
               Ъ(InventoryInteractions.SwapMode.Swap, var1, var2);
         }
      }
   }

   public static int Ъ(BlockState var0, boolean var1) {
      if (MINECRAFT_CLIENT.player == null) {
         return -1;
      }

      int var2 = -1;
      float var3 = 1.0F;
      int var4 = var1 ? 9 : 36;

      for (int var5 = 0; var5 < var4; var5++) {
         ItemStack var6 = MINECRAFT_CLIENT.player.getInventory().getItem(var5);
         if (!var6.isEmpty()) {
            float var7 = var6.getDestroySpeed(var0);
            if (var7 > var3) {
               var3 = var7;
               var2 = var5;
            }
         }
      }

      return var2;
   }

   public static void Ъ_de5a6f78(int var0) {
      if (var0 != -1 && var0 != RoseClient.getInstance().getControllers().getServerPlayers().ПO()) {
         MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(var0));
      }
   }

   public enum SwapMode {
      Pickup,
      Swap;
   }

   public enum SlotSwitchMode implements TranslationKeyProvider {
      Normal("settings.normal"),
      Silent("settings.switch.silent"),
      Alternative("settings.switch.alternative"),
      None("settings.none");

      private final String ХХ;

      @Override
      public String у9() {
         return this.ХХ;
      }

      @Generated
      SlotSwitchMode(String var3) {
         this.ХХ = var3;
      }
   }

   public enum SwingMode implements TranslationKeyProvider {
      MainHand("settings.swing.mainhand"),
      OffHand("settings.swing.offhand"),
      Packet("settings.packet"),
      None("settings.none");

      private final String Е;

      @Override
      public String у9() {
         return this.Е;
      }

      @Generated
      SwingMode(String var3) {
         this.Е = var3;
      }
   }
}
