package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.rose.module.Modules;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(ClientLevel.ClientLevelData.class)
public abstract class ClientLevelDataMixin {
   @ModifyReturnValue(method = "getDayTime", at = @At("RETURN"))
   private long rose$dayTime(long time) {
      return Modules.timeWeather() != null ? Modules.timeWeather().dayTime(time) : time;
   }
}
