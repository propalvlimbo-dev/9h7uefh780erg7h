package sg.modules.impl.misc;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.InventoryActionQueue;

public class AntiServerSwap extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("ХМ")  EventListener<PacketReceiveEvent> packetReceiveListener = this::ч;
   private final @com.google.gson.annotations.SerializedName("ХН")  EventListener<PlayerTickEvent> playerTickListener = this::П5;
   private static final String MODULE_NAME = "AntiServerSwap";
   private static final String MODULE_DESCRIPTION = "Эксперементальная функция для предотврашения визуальных несоответсвий между сервером и клиентом";

   public AntiServerSwap() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
   }

   public void П5(PlayerTickEvent var1) {
      InventoryActionQueue.Т_b1d77d5c().removeIf(var0 -> var0.method__().hasElapsed(20));
   }

   @Override
   public void onEnable() {
      InventoryActionQueue.Т_b1d77d5c().clear();
   }

   public void ч(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundContainerSetSlotPacket var2) {
         if (var2.getContainerId() != 0) {
            return;
         }

         boolean var20 = false;

         for (InventoryActionQueue.SlotSwap var10 : InventoryActionQueue.Т_b1d77d5c()) {
            if (var10.ж() == var2.getSlot() || var10.ю() == var2.getSlot()) {
               var20 = true;
               break;
            }
         }

         var1.setCancelled(var20);
      } else if (var1.getPacket() instanceof ClientboundContainerSetContentPacket var3) {
         ClientboundContainerSetContentPacket var10000 = var3;
         int var31;

         try {
            var31 = var10000.containerId();
         } catch (Throwable var19) {
            throw new MatchException(var19.toString(), var19);
         }

         int var23 = var31;
         if (true) {
            int var4 = var23;
            var10000 = var3;

            int var33;
            try {
               var33 = var10000.stateId();
            } catch (Throwable var18) {
               throw new MatchException(var18.toString(), var18);
            }

            var23 = var33;
            if (true) {
               int var5 = var23;
               var10000 = var3;

               List<ItemStack> var35;
               try {
                  var35 = var10000.items();
               } catch (Throwable var17) {
                  throw new MatchException(var17.toString(), var17);
               }

               List<ItemStack> var25 = var35;
               List<ItemStack> var6 = var25;
               var10000 = var3;

               try {
                  var10000.carriedItem();
               } catch (Throwable var16) {
                  throw new MatchException(var16.toString(), var16);
               }

               if (var4 != 0) {
                  return;
               }

               boolean var22 = false;
               ArrayList<Integer> var26 = new ArrayList<>();

               for (InventoryActionQueue.SlotSwap var11 : InventoryActionQueue.Т_b1d77d5c()) {
                  for (int var12 = 0; var12 < var6.size(); var12++) {
                     if (var12 == var11.ю() || var12 == var11.ж()) {
                        var11.method__().reset();
                        var22 = true;
                        var26.add(var12);
                     }
                  }
               }

               for (int var28 = 0; var28 < var6.size(); var28++) {
                  ItemStack var29 = (ItemStack)var6.get(var28);
                  ItemStack var30 = MINECRAFT_CLIENT.player.inventoryMenu.getSlot(var28).getItem();
                  if (ItemStack.isSameItemSameComponents(var29, var30) && var29.getCount() != var30.getCount()) {
                     var30.setCount(var29.getCount());
                  }

                  if (!var26.contains(var28)) {
                     MINECRAFT_CLIENT.player.inventoryMenu.setItem(var28, var5, (ItemStack)var6.get(var28));
                  } else {
                     boolean var13 = false;

                     for (int var15 : var26) {
                        if (!((ItemStack)var6.get(var15)).getItem().equals(Items.AIR)) {
                           var13 = false;
                           break;
                        }

                        var13 = true;
                     }

                     if (var13 && !((ItemStack)var6.get(var28)).getItem().equals(Items.PLAYER_HEAD)) {
                        MINECRAFT_CLIENT.player.inventoryMenu.setItem(var28, var5, (ItemStack)var6.get(var28));
                     }
                  }
               }

               var1.setCancelled(var22);
            }
         }
      }
   }
}
