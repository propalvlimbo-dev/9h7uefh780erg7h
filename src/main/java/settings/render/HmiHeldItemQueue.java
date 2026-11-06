package settings.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;

public class HmiHeldItemQueue {
   private static final List<ItemStack> сВ = new ArrayList<>();

   public static void н3() {
      сВ.clear();
   }

   public static ItemStack н() {
      if (!сВ.isEmpty()) {
         ItemStack var0 = сВ.getFirst();
         сВ.remove(var0);
         return var0;
      } else {
         return Items.AIR.getDefaultInstance();
      }
   }

   public static void н(ItemStack var0) {
      if (!var0.isEmpty() && var0.getUseAnimation() != ItemUseAnimation.BLOCK && var0.getUseAnimation() != ItemUseAnimation.TRIDENT) {
         сВ.add(var0);
      }
   }
}
