package sg.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface MultiPlayerGameModeAccessor {
   @Invoker("ensureHasSentCarriedItem")
   void syncSelectedSlot$drug();

   @Accessor("destroyProgress")
   float getCurrentBreakingProgress();

   @Accessor("destroyProgress")
   void setCurrentBreakingProgress(float var1);

   @Accessor("destroyDelay")
   int getBlockBreakingCooldown();

   @Accessor("destroyDelay")
   void setBlockBreakingCooldown(int var1);
}
