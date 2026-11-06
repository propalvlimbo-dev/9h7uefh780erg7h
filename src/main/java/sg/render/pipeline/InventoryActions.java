package sg.render.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.state.BlockState;
import sg.ec.MinecraftAccess;
import sg.ec.PacketEventBypass;
import sg.modules.impl.init.TickTimer;

public final class InventoryActions implements MinecraftAccess {
   private static final List<InventoryActions.SwapSnapshot> Д = new ArrayList<>();
   private static int э = -1;
   private static final String сlБ = "драконид";
   private static final String сlС = "This is a utility class and cannot be instantiated";

   public static void Б(int var0) {
      э = var0;
   }

   public static int method_5() {
      return MINECRAFT_CLIENT.player.getInventory().selected + 1 > 8 ? MINECRAFT_CLIENT.player.getInventory().selected - 1 : MINECRAFT_CLIENT.player.getInventory().selected + 1;
   }

   @Generated
   private InventoryActions() {
      throw new UnsupportedOperationException(сlС);
   }

   public static boolean б(int var0) {
      return var0 >= 36 && var0 <= 44;
   }

   public static int ч(int var0) {
      return var0 >= 36 ? var0 - 36 : var0;
   }

   public static int ш(Item var0) {
      int var1 = -1;
      if (MINECRAFT_CLIENT.player != null) {
         for (int var2 = 0; var2 < MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().size(); var2++) {
            if (MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var2).getItem().equals(var0)) {
               var1 = var2;
               break;
            }
         }
      }

      if (var1 != -1 && var1 < 9) {
         var1 += 36;
      }

      return var1;
   }

   public static void method_7(int var0, int var1) {
      if (MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.gameMode != null
         && MINECRAFT_CLIENT.player.isAlive()
         && MINECRAFT_CLIENT.player.containerMenu == MINECRAFT_CLIENT.player.inventoryMenu
         && MINECRAFT_CLIENT.player.inventoryMenu.getCarried().isEmpty()
         && (var0 == 40 || var0 >= 0 && var0 <= 8)
         && var1 >= 0
         && var1 < MINECRAFT_CLIENT.player.inventoryMenu.slots.size()) {
         MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var1, var0, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         int var2 = var0 == 40 ? 45 : var0 + 36;
         Д.removeIf(var0x -> var0x.Г().hasElapsed(60));
         Д.add(
            new InventoryActions.SwapSnapshot(
               var1, var2, MINECRAFT_CLIENT.player.inventoryMenu.getSlot(var1).getItem().copy(), MINECRAFT_CLIENT.player.inventoryMenu.getSlot(var2).getItem().copy()
            )
         );
      }
   }

   public static int б(Holder<MobEffect> var0) {
      int var1 = -1;
      if (MINECRAFT_CLIENT.player != null) {
         for (int var2 = 0; var2 < MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().size(); var2++) {
            if (MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var2).has(DataComponents.POTION_CONTENTS)) {
               PotionContents var3 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var2).get(DataComponents.POTION_CONTENTS);

               for (MobEffectInstance var5 : var3.getAllEffects()) {
                  if (var5.getEffect().equals(var0)) {
                     var1 = var2;
                     break;
                  }
               }
            }
         }
      }

      if (var1 != -1 && var1 < 9) {
         var1 += 36;
      }

      return var1;
   }

   public static void 衣(int var0) {
      if (var0 != э) {
         if (var0 >= 0 && var0 <= 9) {
            э = var0;
            MINECRAFT_CLIENT.player.connection.send(new ServerboundSetCarriedItemPacket(э));
         }
      }
   }

   public static int б(Item var0, boolean var1) {
      int var2 = -1;
      if (MINECRAFT_CLIENT.player != null) {
         for (int var3 = 0; var3 < MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().size(); var3++) {
            if (MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var3).getItem().equals(var0)
               && (!MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var3).getHoverName().getString().toLowerCase(Locale.ROOT).contains(сlБ) || !var1)) {
               var2 = var3;
               break;
            }
         }
      }

      if (var2 != -1 && var2 < 9) {
         var2 += 36;
      }

      return var2;
   }

   public static void е(int var0, int var1) {
      method_7(var0, var1);
      PacketEventBypass.А(new ServerboundContainerClosePacket(0));
   }

   public static int б(BlockState var0) {
      int var1 = -1;
      ItemStack var2 = null;

      for (int var3 = 0; var3 < 9; var3++) {
         ItemStack var4 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var3);
         if ((var2 == null || var2.getDestroySpeed(var0) < var4.getDestroySpeed(var0)) && var4.getDestroySpeed(var0) > 1.0F) {
            var2 = var4;
            var1 = var3;
         }
      }

      return var1;
   }

   public static void 西Х() {
      PacketEventBypass.А(new ServerboundPlayerActionPacket(Action.SWAP_ITEM_WITH_OFFHAND, new BlockPos(0, 0, 0), Direction.DOWN));
   }

   @Generated
   public static List<InventoryActions.SwapSnapshot> ш_2571477d() {
      return Д;
   }

   public static class SwapSnapshot {
      private final int Дс;
      private final int ДВ;
      private final ItemStack ш;
      private final ItemStack И;
      private final TickTimer Д = new TickTimer();

      @Generated
      public SwapSnapshot(int var1, int var2, ItemStack var3, ItemStack var4) {
         this.Дс = var1;
         this.ДВ = var2;
         this.ш = var3;
         this.И = var4;
      }

      @Generated
      public ItemStack method_5() {
         return this.И;
      }

      @Generated
      public TickTimer Г() {
         return this.Д;
      }

      @Generated
      public int Тю() {
         return this.ДВ;
      }

      @Generated
      public int ТН() {
         return this.Дс;
      }

      @Generated
      public ItemStack Х() {
         return this.ш;
      }
   }
}
