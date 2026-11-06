package sg.ec;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import sg.mixin.ItemCooldownInstanceAccessor;
import sg.mixin.ItemCooldownsAccessor;

public final class ItemCooldownSeconds implements MinecraftAccess {
   private static final float TICKS_PER_SECOND = 20.0F;

   private ItemCooldownSeconds() {
   }

   public static float getRemainingSeconds(Item item, float partialTick) {
      return item == null ? 0.0F : getRemainingSeconds(new ItemStack(item), partialTick);
   }

   public static float getRemainingSeconds(ItemStack stack, float partialTick) {
      return MINECRAFT_CLIENT.player == null ? 0.0F : getRemainingSeconds(MINECRAFT_CLIENT.player.getCooldowns(), stack, partialTick);
   }

   public static float getRemainingSeconds(ItemCooldowns cooldowns, ItemStack stack, float partialTick) {
      if (cooldowns != null && stack != null && !stack.isEmpty()) {
         ItemCooldownsAccessor cooldownAccess = (ItemCooldownsAccessor)cooldowns;
         Identifier cooldownGroup = cooldownAccess.invokeGetCooldownGroup(stack);
         Object cooldownEntry = cooldownAccess.getCooldownEntries().get(cooldownGroup);
         if (cooldownEntry == null) {
            return 0.0F;
         }

         int endTick = ((ItemCooldownInstanceAccessor)cooldownEntry).getEndTick();
         float remainingTicks = endTick - (cooldownAccess.getCooldownTick() + partialTick);
         return Math.max(remainingTicks / TICKS_PER_SECOND, 0.0F);
      } else {
         return 0.0F;
      }
   }
}
