package sg.ec;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sg.render.WeaponSkinVariant;
import sg.render.ClientCosmeticSelections;

public final class WeaponSkinStacks {
   public static ItemStack applySelectedSkin(ItemStack stack) {
      if (stack == null || stack.isEmpty()) {
         return stack;
      }

      if (!ClientCosmeticSelections.areWeaponSkinsEnabled()) {
         return stack;
      }

      WeaponSkinVariant skin;
      if (stack.is(Items.MACE)) {
         skin = ClientCosmeticSelections.getSelectedMaceSkin();
      } else {
         if (!stack.is(ItemTags.SWORDS)) {
            return stack;
         }

         skin = ClientCosmeticSelections.getSelectedSwordSkin();
      }

      ItemStack displayStack = stack.copy();
      displayStack.set(DataComponents.ITEM_MODEL, skin.getItemModelId());
      return displayStack;
   }

   private WeaponSkinStacks() {
   }
}
