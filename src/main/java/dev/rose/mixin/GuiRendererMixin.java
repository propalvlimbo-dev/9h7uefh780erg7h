package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.gpu.ItemAlpha;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiItemRenderState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin {
   @Shadow
   @Final
   private List<?> meshesToDraw;

   @Inject(method = "recordMesh", at = @At("HEAD"))
   private void rose$rememberMarkers(BufferBuilder builder, RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRectangle scissor, CallbackInfo ci) {
      GuiDraw.onRecordMesh(pipeline, this.meshesToDraw.size());
   }

   @WrapOperation(
      method = "draw",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/render/GuiRenderer;executeDrawRange(Ljava/util/function/Supplier;Lcom/mojang/blaze3d/pipeline/RenderTarget;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lcom/mojang/blaze3d/buffers/GpuBuffer;Lcom/mojang/blaze3d/vertex/VertexFormat$IndexType;II)V"
      )
   )
   private void rose$drawAroundMarkers(
      GuiRenderer self,
      Supplier<String> label,
      RenderTarget target,
      GpuBufferSlice fog,
      GpuBufferSlice transforms,
      GpuBuffer indices,
      VertexFormat.IndexType indexType,
      int from,
      int to,
      Operation<Void> original
   ) {
      if (!GuiDraw.hasRuns()) {
         original.call(new Object[]{self, label, target, fog, transforms, indices, indexType, from, to});
      } else {
         int start = from;

         for (int i = from; i < to; i++) {
            List<GuiDraw.Element> run = GuiDraw.runAt(i);
            if (run != null) {
               if (start < i) {
                  original.call(new Object[]{self, label, target, fog, transforms, indices, indexType, start, i});
               }

               GuiDraw.execute(run, target);
               start = i + 1;
            }
         }

         if (start < to) {
            original.call(new Object[]{self, label, target, fog, transforms, indices, indexType, start, to});
         }
      }
   }

   @ModifyArg(
      method = "submitBlitFromItemAtlas",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/render/state/BlitRenderState;<init>(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/gui/render/TextureSetup;Lorg/joml/Matrix3x2f;IIIIFFFFILnet/minecraft/client/gui/navigation/ScreenRectangle;Lnet/minecraft/client/gui/navigation/ScreenRectangle;)V"
      ),
      index = 11
   )
   private int rose$itemAlpha(int color, @Local(argsOnly = true) GuiItemRenderState state) {
      float alpha = ((ItemAlpha.Holder)(Object)state).rose$alpha();
      return alpha >= 0.999F ? color : ItemAlpha.blitColor(alpha);
   }

   @Inject(method = "render", at = @At("TAIL"))
   private void rose$frameDrawn(GpuBufferSlice fog, CallbackInfo ci) {
      GuiDraw.onGuiRendered();
   }
}
