package sg.render.pipeline;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Supplier;
import org.joml.Vector4i;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;

public final class PotionWheel implements MinecraftAccess, UiRenderPassSupport {
   private final RenderPipeline Вь;
   private final RenderPipeline Вж;
   private final CommandEncoder т;
   private final Supplier<TextureTarget> п;
   private static final String ЯЧГ = "rose/potion_wheel";
   private static final String ЯЧл = "potion_wheel";
   private static final String ЯЧэ = "potion_wheel";
   private static final String ЯЧю = "Wheel";
   private static final String ЯЧЩ = "Colors";
   private static final String ЯЧу = "rose/potion_wheel_blur";
   private static final String ЯЧ2 = "potion_wheel";
   private static final String ЯЧф = "potion_wheel_blur";
   private static final String ЯЧи = "Wheel";
   private static final String ЯЧ9 = "Colors";
   private static final String ЯЧо = "Sampler0";
   private static final String ЯЧO = "Sampler0";
   private static final String ЯЧ7 = "Wheel";
   private static final String ЯЧР = "Colors";
   private static final String ЯЧ3 = "Wheel";
   private static final String ЯЧр = "Colors";
   private static final String ЯЧ6 = "potionWheelPass";
   private static final String ЯЧ_ = "potionWheelBlurPass";
   private static final String ЯЧШ = "potion_wheel_blur";

   public void l(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      if (!(var4 <= var3) && !(var4 <= 0.0F)) {
         float var10 = var4 * 2.0F;
         BufferBuilder var11 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
         this.appendQuad(var11, var1 - var4, var2 - var4, var10, var10);
         RenderPass var12 = this.т.createRenderPass(() -> ЯЧ6, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
         RenderSystem.bindDefaultUniforms(var12);

         try (MemoryStack var15 = MemoryStack.stackPush()) {
            ByteBuffer var16 = Std140Builder.onStack(var15, 32)
               .putVec2(var10, var10)
               .putFloat(var3)
               .putFloat(var4)
               .putFloat(var5)
               .putFloat(var6)
               .putFloat(var7)
               .putFloat(var8)
               .get();
            GpuBufferSlice var13 = TransientGpuUpload.uploadUniform(var16);
            GpuBufferSlice var14 = this.uploadCornerColors(var15, new Vector4i(var9));
            var12.setUniform(ЯЧ3, var13);
            var12.setUniform(ЯЧр, var14);
         }

         this.drawAndClose(var11, var12, this.Вь);
      }
   }

   public void чр() {
      RenderTarget var1 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
      TextureTarget var2 = this.п.get();
      if (var2.width != var1.width || var2.height != var1.height) {
         var2.resize(var1.width, var1.height);
      }

      this.т.copyTextureToTexture(var1.getColorTexture(), var2.getColorTexture(), 0, 0, 0, 0, 0, var1.width, var1.height);
   }

   public PotionWheel() {
      this.Вь = RenderPipelineBuilder.builder()
         .withLocation(ЯЧГ)
         .withVertexShader(ClientResourceIds.getCoreId(ЯЧл))
         .withFragmentShader(ClientResourceIds.getCoreId(ЯЧэ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(ЯЧю, UniformType.UNIFORM_BUFFER)
         .withUniform(ЯЧЩ, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT)
         .build();
      this.Вж = RenderPipelineBuilder.builder()
         .withLocation(ЯЧу)
         .withVertexShader(ClientResourceIds.getCoreId(ЯЧ2))
         .withFragmentShader(ClientResourceIds.getCoreId(ЯЧф))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(ЯЧи, UniformType.UNIFORM_BUFFER)
         .withUniform(ЯЧ9, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withSampler(ЯЧо)
         .build();
      this.т = RenderSystem.getDevice().createCommandEncoder();
      this.п = Suppliers.memoize(() -> new TextureTarget(ЯЧШ, 1920, 1024, false, GpuFormat.RGBA8_UNORM));
   }

   public void method_0(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      if (!(var4 <= var3) && !(var4 <= 0.0F)) {
         float var10 = var4 * 2.0F;
         BufferBuilder var11 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
         this.appendQuad(var11, var1 - var4, var2 - var4, var10, var10);
         RenderPass var12 = this.т.createRenderPass(() -> ЯЧ_, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
         var12.bindTexture(ЯЧO, this.п.get().getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         RenderSystem.bindDefaultUniforms(var12);

         try (MemoryStack var15 = MemoryStack.stackPush()) {
            ByteBuffer var16 = Std140Builder.onStack(var15, 32)
               .putVec2(var10, var10)
               .putFloat(var3)
               .putFloat(var4)
               .putFloat(var5)
               .putFloat(var6)
               .putFloat(var7)
               .putFloat(var8)
               .get();
            GpuBufferSlice var13 = TransientGpuUpload.uploadUniform(var16);
            GpuBufferSlice var14 = this.uploadCornerColors(var15, new Vector4i(var9));
            var12.setUniform(ЯЧ7, var13);
            var12.setUniform(ЯЧР, var14);
         }

         this.drawAndClose(var11, var12, this.Вж);
      }
   }
}
