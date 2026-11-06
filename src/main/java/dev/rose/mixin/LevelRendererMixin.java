package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import dev.rose.compat.Compat;
import dev.rose.module.Modules;
import dev.rose.module.impl.visual.SelfViewModule;
import dev.rose.module.impl.world.ChunkFadeModule;
import dev.rose.module.impl.world.SkyModule;
import dev.rose.render.world.WorldProjection;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
   @Inject(method = "renderLevel", at = @At("HEAD"))
   private void rose$rememberProjection(
      GraphicsResourceAllocator allocator,
      DeltaTracker deltaTracker,
      boolean renderOutline,
      Camera camera,
      Matrix4f view,
      Matrix4f projection,
      Matrix4f cullProjection,
      GpuBufferSlice fog,
      Vector4f fogColor,
      boolean renderSky,
      CallbackInfo ci
   ) {
      WorldProjection.set(projection, view);
   }

   @ModifyExpressionValue(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;isDetached()Z"))
   private boolean rose$renderSelf(boolean detached) {
      SelfViewModule self = Modules.selfView();
      return detached || self != null && self.showsSelf();
   }

   @ModifyExpressionValue(
      method = "compileSections",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0)
   )
   private Object rose$fadeDuration(Object seconds) {
      ChunkFadeModule fade = Modules.chunkFade();
      return fade != null && fade.active() && !Compat.shaderPackActive() ? fade.totalSeconds() : seconds;
   }

   @ModifyExpressionValue(method = "prepareChunkRenders", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$RenderSection;getVisibility(J)F"))
   private float rose$fadeStyle(float visibility) {
      ChunkFadeModule fade = Modules.chunkFade();
      return fade != null && fade.active() && !Compat.shaderPackActive() ? fade.encode(visibility) : visibility;
   }

   @Inject(method = "addCloudsPass", at = @At("HEAD"), cancellable = true)
   private void rose$clouds(CallbackInfo ci) {
      SkyModule sky = Modules.sky();
      if (sky != null && sky.hideClouds()) {
         ci.cancel();
      }
   }

   @ModifyArg(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/LevelRenderer;addCloudsPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/CloudStatus;Lnet/minecraft/world/phys/Vec3;JFIF)V"
      ),
      index = 5
   )
   private int rose$cloudColor(int color) {
      SkyModule sky = Modules.sky();
      return sky != null && !Compat.shaderPackActive() ? sky.cloudColor(color) : color;
   }
}
