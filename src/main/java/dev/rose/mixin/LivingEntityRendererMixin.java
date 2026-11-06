package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.rose.module.Modules;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
   @ModifyReturnValue(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At("RETURN"))
   private boolean rose$showOwnName(boolean show, LivingEntity entity, double distance) {
      if (!show && Modules.nametags() != null && Modules.nametags().showSelf()) {
         Minecraft mc = Minecraft.getInstance();
         return entity == mc.player
            && entity == mc.getCameraEntity()
            && !mc.options.getCameraType().isFirstPerson()
            && Minecraft.renderNames()
            && !entity.isInvisible();
      } else {
         return show;
      }
   }
}
