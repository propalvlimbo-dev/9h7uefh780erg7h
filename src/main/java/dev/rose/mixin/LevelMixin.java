package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.rose.module.Modules;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(Level.class)
public abstract class LevelMixin {
   @ModifyReturnValue(method = "getRainLevel", at = @At("RETURN"))
   private float rose$rain(float rain) {
      return (Object)this instanceof ClientLevel && Modules.timeWeather() != null ? Modules.timeWeather().rainLevel(rain) : rain;
   }

   @ModifyReturnValue(method = "getThunderLevel", at = @At("RETURN"))
   private float rose$thunder(float thunder) {
      return (Object)this instanceof ClientLevel && Modules.timeWeather() != null ? Modules.timeWeather().thunderLevel(thunder) : thunder;
   }
}
