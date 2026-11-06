package sg.mixin;

import sg.mixinbridge.RenderSuppressionQueries;

import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.ParticlesRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.ec.HiddenBlockPositions;
import sg.modules.impl.misc.Optimization;
import sg.events.impl.RenderSuppressionQuery;

@Mixin(ParticleEngine.class)
public class ParticleEngineVisibilityMixin {
   private static final double Вlч = 0.5;
   private static final double ВlЭ = 0.5;
   private static final double Вl必 = 0.5;

   @Inject(method = "add(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true, require = 0)
   private void cullDistantParticle(Particle var1, CallbackInfo var2) {
      AABB var3 = var1.getBoundingBox();
      double var4 = (var3.minX + var3.maxX) * Вlч;
      double var6 = (var3.minY + var3.maxY) * ВlЭ;
      double var8 = (var3.minZ + var3.maxZ) * Вl必;
      if (!Optimization.method_7(var4, var6, var8)) {
         var2.cancel();
      }
   }

   @Inject(
      method = "createParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;",
      at = @At("HEAD"),
      cancellable = true
   )
   private void addParticle(
      ParticleOptions var1, double var2, double var4, double var6, double var8, double var10, double var12, CallbackInfoReturnable<Particle> var14
   ) {
      if (var1 instanceof BlockParticleOption) {
         if (HiddenBlockPositions.О(BlockPos.containing(var2, var4, var6))) {
            var14.setReturnValue(null);
            return;
         }

         if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.DESTROY_PARTICLES)) {
            var14.setReturnValue(null);
         }
      }
   }

   @Inject(method = "extract", at = @At("HEAD"), cancellable = true)
   private void addToBatch(ParticlesRenderState var1, Frustum var2, Camera var3, float var4, CallbackInfo var5) {
      if (Optimization.method_7В() || RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.SMOKE)) {
         var5.cancel();
      }
   }
}
