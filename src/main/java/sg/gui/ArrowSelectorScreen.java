package sg.gui;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import sg.modules.impl.init.ArrowSwapScreen;
import sg.modules.impl.combat.ArrowSwap;

public final class ArrowSelectorScreen extends InventoryScreen {
   @SerializedName("Я")
   private final ArrowSwap Я_2dd7c9af;
   private final int Хд;
   @SerializedName("Я")
   private final ArrowSwapScreen Я_a217576d;
   private final int ХБ;

   public ArrowSelectorScreen(ArrowSwap var1, int var2, ArrowSwapScreen var3, int var4) {
      super(Minecraft.getInstance().player);
      this.Я_2dd7c9af = var1;
      this.Хд = var2;
      this.Я_a217576d = var3;
      this.ХБ = var4;
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.Я_a217576d);
   }

   @Override
   public boolean keyReleased(KeyEvent var1) {
      if (var1.key() == this.ХБ) {
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
         && ArrowSwap.Ф(var1.getItem())) {
         this.Я_2dd7c9af.Ш(this.Хд, var1.getItem());
         this.minecraft.gui.setScreen(this.Я_a217576d);
      }
   }
}
