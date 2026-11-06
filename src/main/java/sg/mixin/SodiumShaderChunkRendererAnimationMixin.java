package sg.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BindGroupLayout.Builder;
import com.mojang.blaze3d.shaders.UniformType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.annotation.RequiresMods;
import sg.render.pipeline.RoseChunkOffsets;

@RequiresMods("sodium")
@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.ShaderChunkRenderer", remap = false)
public class SodiumShaderChunkRendererAnimationMixin {
   @WrapOperation(
      method = "<clinit>",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/BindGroupLayout$Builder;build()Lcom/mojang/blaze3d/pipeline/BindGroupLayout;")
   )
   private static BindGroupLayout rose$chunkAnimationLayout(Builder var0, Operation<BindGroupLayout> var1) {
      var0.withUniform(RoseChunkOffsets.Л诶(), UniformType.UNIFORM_BUFFER);
      return (BindGroupLayout)var1.call(var0);
   }

   @Inject(method = "delete", at = @At("HEAD"))
   private void rose$releaseAnimationBuffers(CallbackInfo var1) {
      if (this instanceof RoseChunkOffsets.ChunkAnimationLifecycle var2) {
         var2.rose$closeChunkAnimation();
      }
   }
}
