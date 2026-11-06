package sg.manager;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sg.annotation.LuaExport;

public class LuaItemRegistry {
   @LuaExport("get")
   public Item get(String itemId) {
      Identifier var2 = Identifier.parse(itemId);
      return BuiltInRegistries.ITEM.getValue(var2);
   }

   @LuaExport
   public String 必(ItemStack var1) {
      return var1.getCustomName().toString();
   }
}
