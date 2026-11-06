package sg.mixin;

import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemCooldowns.class)
public interface ItemCooldownsAccessor {
   @Accessor("cooldowns")
   Map<Identifier, Object> getCooldownEntries();

   @Accessor("tickCount")
   int getCooldownTick();

   @Invoker("getCooldownGroup")
   Identifier invokeGetCooldownGroup(ItemStack var1);
}
