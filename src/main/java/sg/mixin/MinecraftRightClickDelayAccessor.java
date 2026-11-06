package sg.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftRightClickDelayAccessor {
   @Accessor("rightClickDelay")
   void setItemUseCooldown(int var1);
}
