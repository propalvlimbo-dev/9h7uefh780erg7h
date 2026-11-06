package sg.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.annotation.RequiresMods;
import sg.render.pipeline.RoseChunkOffsets;

@RequiresMods("sodium")
@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer", remap = false)
public class SodiumDefaultChunkRendererAnimationMixin implements RoseChunkOffsets.ChunkAnimationLifecycle {
   @Unique
   private RoseChunkOffsets rose$chunkAnimation;

   @Inject(method = "render", at = @At("HEAD"))
   private void rose$prepareChunkOffsets(CallbackInfo var1) {
      if (this.rose$chunkAnimation == null) {
         this.rose$chunkAnimation = new RoseChunkOffsets();
      }

      this.rose$chunkAnimation.ц衣();
   }

   @WrapOperation(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/lists/ChunkRenderList;getRegion()Lnet/caffeinemc/mods/sodium/client/render/chunk/region/RenderRegion;",
         ordinal = 0
      )
   )
   @Coerce
   private Object rose$prepareRegionOffsets(@Coerce Object var1, Operation<Object> var2) {
      Object var3 = var2.call(var1);
      this.rose$chunkAnimation.Б((RoseChunkOffsets.RegionAccess)var3);
      return var3;
   }

   @WrapOperation(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/caffeinemc/mods/sodium/client/gpu/device/context/DrawContext;updateData(Lnet/caffeinemc/mods/sodium/client/render/chunk/region/RenderRegion;Lnet/caffeinemc/mods/sodium/client/render/viewport/CameraTransform;)V"
      )
   )
   private void rose$bindChunkOffsets(@Coerce Object var1, @Coerce Object var2, @Coerce Object var3, Operation<Void> var4, @Local RenderPass var5) {
      var5.setUniform(RoseChunkOffsets.Л诶(), this.rose$chunkAnimation.Б((RoseChunkOffsets.RegionAccess)var2));
      var4.call(var1, var2, var3);
   }

   @Inject(method = "rotate", at = @At("HEAD"))
   private void rose$nextAnimationFrame(CallbackInfo var1) {
      if (this.rose$chunkAnimation != null) {
         this.rose$chunkAnimation.цз();
      }
   }

   @Override
   public void rose$closeChunkAnimation() {
      if (this.rose$chunkAnimation != null) {
         this.rose$chunkAnimation.close();
         this.rose$chunkAnimation = null;
      }
   }
}
