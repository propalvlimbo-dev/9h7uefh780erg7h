package sg.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.ClientClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.client.RoseClient;
import sg.modules.impl.render.Ambience;

@Mixin(ClientClockManager.class)
public abstract class ClientClockManagerTimeOverrideMixin {
   @ModifyReturnValue(method = "getTotalTicks", at = @At("RETURN"))
   private long rose$overrideWorldTime(long var1, Holder<WorldClock> var3) {
      if (!var3.is(WorldClocks.OVERWORLD)) {
         return var1;
      } else {
         RoseClient var4 = RoseClient.getInstance();
         if (var4 != null && var4.getServices() != null && var4.getServices().getModules() != null) {
            Ambience var5 = var4.getServices().getModules().getAmbience();
            return var5 == null ? var1 : var5.resolveWorldTime(var1);
         } else {
            return var1;
         }
      }
   }
}
