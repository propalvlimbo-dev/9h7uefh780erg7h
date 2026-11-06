package sg.ec;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class ArmorItems {
   private static final String UTILITY_CONSTRUCTOR_MESSAGE = "This is a utility class and cannot be instantiated";

   @Generated
   private ArmorItems() {
      throw new UnsupportedOperationException(UTILITY_CONSTRUCTOR_MESSAGE);
   }

   public static List<ItemStack> getArmorItems(LivingEntity entity) {
      ArrayList armorItems = new ArrayList();
      armorItems.add(entity.getItemBySlot(EquipmentSlot.HEAD));
      armorItems.add(entity.getItemBySlot(EquipmentSlot.CHEST));
      armorItems.add(entity.getItemBySlot(EquipmentSlot.LEGS));
      armorItems.add(entity.getItemBySlot(EquipmentSlot.FEET));
      return armorItems;
   }
}
