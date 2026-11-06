package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.rose.module.Modules;
import dev.rose.module.impl.visual.HandModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(LivingEntity.class)
public abstract class SwingDurationMixin {
   @ModifyReturnValue(method = "getCurrentSwingDuration", at = @At("RETURN"))
   private int rose$swingDuration(int original) {
      if ((Object)this != Minecraft.getInstance().player) {
         return original;
      }

      HandModule module = Modules.hand();
      return module == null ? original : module.swingDuration(original);
   }
}
