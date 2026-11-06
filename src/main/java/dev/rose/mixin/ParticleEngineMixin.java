package dev.rose.mixin;

import dev.rose.module.Modules;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {
   @Inject(method = "add", at = @At("HEAD"), cancellable = true)
   private void rose$density(Particle particle, CallbackInfo ci) {
      if (Modules.particles() != null && Modules.particles().skip()) {
         ci.cancel();
      }
   }
}
