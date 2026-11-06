package sg.ec;

import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;

public class MissingArmorTotemTrigger extends TotemTrigger {
   private static final String ЯЖв = "Броня";

   @Override
   public boolean ац() {
      if (!this.В.Ы().isSelected(ЯЖв)) {
         return false;
      }

      for (ItemStack var2 : ArmorItems.getArmorItems(MINECRAFT_CLIENT.player)) {
         if (var2.getItem() instanceof AirItem) {
            return true;
         }
      }

      return false;
   }
}
