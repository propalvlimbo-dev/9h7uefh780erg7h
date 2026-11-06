package dev.rose.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.TridentItem;


public final class Weapons {
   private Weapons() {
   }

   public static boolean is(ItemStack stack) {
      return stack.has(DataComponents.TOOL)
         || stack.has(DataComponents.WEAPON)
         || stack.getItem() instanceof TridentItem
         || stack.getItem() instanceof MaceItem;
   }
}
