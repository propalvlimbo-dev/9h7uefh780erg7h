package sg.modules.impl.init;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record BoundItemIndicator(ItemStack stack, int key, Identifier icon) {
   public BoundItemIndicator(Item var1, int var2) {
      this(var1.getDefaultInstance(), var2, null);
   }

   public BoundItemIndicator(ItemStack var1, int var2) {
      this(var1, var2, null);
   }

   public BoundItemIndicator(Item var1, int var2, Identifier var3) {
      this(var1.getDefaultInstance(), var2, var3);
   }

   public Item item() {
      return this.stack.getItem();
   }

   public boolean hasCustomIcon() {
      return this.icon != null;
   }
}
