package sg.gui;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import sg.modules.impl.init.AutoSwapScreen;
import sg.modules.impl.combat.AutoSwap;

public final class SwapItemSelectorScreen extends InventoryScreen {
   @SerializedName("Я")
   private final AutoSwap Я_eef6a369;
   private final int е4;
   @SerializedName("Я")
   private final AutoSwapScreen Я_e281f645;
   private final int еп;

   @Override
   public boolean keyReleased(KeyEvent var1) {
      if (var1.key() == this.еп) {
         this.minecraft.gui.setScreen(null);
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   @Override
   protected void slotClicked(Slot var1, int var2, int var3, ContainerInput var4) {
      if (var1 != null
         && var1.container == this.minecraft.player.getInventory()
         && var1.getContainerSlot() >= 0
         && var1.getContainerSlot() < 36
         && var1.hasItem()
         && this.Я_eef6a369.й(this.е4, var1.getItem())) {
         this.minecraft.gui.setScreen(this.Я_e281f645);
      }
   }

   public SwapItemSelectorScreen(AutoSwap var1, int var2, AutoSwapScreen var3, int var4) {
      super(Minecraft.getInstance().player);
      this.Я_eef6a369 = var1;
      this.е4 = var2;
      this.Я_e281f645 = var3;
      this.еп = var4;
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.Я_e281f645);
   }
}
