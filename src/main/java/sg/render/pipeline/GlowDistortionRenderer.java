package sg.render.pipeline;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
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
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.nio.ByteBuffer;
import java.util.Optional;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;

public final class GlowDistortionRenderer implements MinecraftAccess {
   public static final GlowDistortionRenderer INSTANCE = new GlowDistortionRenderer();
   private static final RenderPipeline QUAD_MASK_PIPELINE = buildMaskPipeline(GlowDistortionRenderer.QUAD_MASK_PIPELINE_LOCATION, PrimitiveTopology.QUADS);
   private static final RenderPipeline TRIANGLE_MASK_PIPELINE = buildMaskPipeline(GlowDistortionRenderer.TRIANGLE_MASK_PIPELINE_LOCATION, PrimitiveTopology.TRIANGLES);
   private static final RenderPipeline COMPOSITE_PIPELINE = RenderPipelineBuilder.builder()
      .withLocation(GlowDistortionRenderer.COMPOSITE_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(GlowDistortionRenderer.COMPOSITE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(GlowDistortionRenderer.COMPOSITE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(GlowDistortionRenderer.DISTORTION_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(GlowDistortionRenderer.SCENE_SAMPLER_LAYOUT_NAME)
      .withSampler(GlowDistortionRenderer.GLOW_SAMPLER_LAYOUT_NAME)
      .build();
   private @com.google.gson.annotations.SerializedName("Ц")  TextureTarget maskTarget;
   private @com.google.gson.annotations.SerializedName("ч")  TextureTarget sceneCopyTarget;
   private static final String MASK_VERTEX_SHADER = "dead_chams";
   private static final String MASK_FRAGMENT_SHADER = "target_esp_rhomb_distortion_mask";
   private static final String SCENE_SAMPLER_BINDING = "SceneSampler";
   private static final String GLOW_SAMPLER_BINDING = "GlowSampler";
   private static final String DISTORTION_UNIFORM_BINDING = "GlowDistortion";
   private static final String SCENE_TARGET_LABEL = "glow-distortion-scene";
   private static final String MASK_TARGET_LABEL = "glow-distortion-mask";
   private static final String COMPOSITE_PASS_LABEL = "glowDistortionComposite";
   private static final String MASK_PASS_LABEL = "glowDistortionMask";
   private static final String QUAD_MASK_PIPELINE_LOCATION = "rose/glow_distortion_mask";
   private static final String TRIANGLE_MASK_PIPELINE_LOCATION = "rose/glow_distortion_mask_triangles";
   private static final String COMPOSITE_PIPELINE_LOCATION = "rose/glow_distortion";
   private static final String COMPOSITE_VERTEX_SHADER = "hands_fullscreen";
   private static final String COMPOSITE_FRAGMENT_SHADER = "target_esp_rhomb_distortion";
   private static final String DISTORTION_UNIFORM_LAYOUT_NAME = "GlowDistortion";
   private static final String SCENE_SAMPLER_LAYOUT_NAME = "SceneSampler";
   private static final String GLOW_SAMPLER_LAYOUT_NAME = "GlowSampler";

   private static RenderPipeline buildMaskPipeline(String location, PrimitiveTopology topology) {
      return RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(location)
         .withVertexShader(ClientResourceIds.getCoreId(MASK_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(MASK_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, topology)
         .build();
   }

   public void renderMaskAndDistort(BufferBuilder builder, float strength) {
      MeshData mesh = builder.build();
      if (mesh != null) {
         try {
            if (!(strength <= 0.0F)) {
               DrawState drawState = mesh.drawState();
               int indexCount = drawState.indexCount();
               AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
               GpuBuffer indexBuffer = sequentialIndices.getBuffer(indexCount);
               IndexType indexType = sequentialIndices.type();
               GpuBufferSlice vertexSlice = TransientGpuUpload.uploadVertices(mesh.vertexBuffer(), drawState.format().getVertexSize());
               this.renderMaskAndDistort(vertexSlice, indexBuffer, indexType, indexCount, strength, drawState.primitiveTopology());
               return;
            }
         } finally {
            mesh.close();
         }
      }
   }

   public void distortMaskTarget(RenderTarget mask, float strength) {
      if (!(strength <= 0.0F)) {
         RenderTarget outputTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (outputTarget != null && outputTarget.getColorTextureView() != null && mask != null && mask.getColorTextureView() != null) {
            this.ensureTargets(outputTarget.width, outputTarget.height, false);
            this.compositeDistortion(mask, outputTarget, strength, RenderSystem.getDevice().createCommandEncoder());
         }
      }
   }

   private GlowDistortionRenderer() {
   }

   private void renderMaskAndDistort(GpuBufferSlice vertices, GpuBuffer indices, IndexType indexType, int indexCount, float strength, PrimitiveTopology topology) {
      RenderTarget outputTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
      if (outputTarget != null && outputTarget.getColorTextureView() != null && indexCount > 0 && !(strength <= 0.0F)) {
         this.ensureTargets(outputTarget.width, outputTarget.height, true);
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

         try (RenderPass maskPass = encoder.createRenderPass(() -> MASK_PASS_LABEL, this.maskTarget.getColorTextureView(), Optional.of(new Vector4f(0.0F)))) {
            RenderSystem.bindDefaultUniforms(maskPass);
            maskPass.setPipeline(topology == PrimitiveTopology.TRIANGLES ? TRIANGLE_MASK_PIPELINE : QUAD_MASK_PIPELINE);
            maskPass.setVertexBuffer(0, vertices);
            maskPass.setIndexBuffer(indices, indexType);
            maskPass.drawIndexed(indexCount, 1, 0, 0, 0);
         }

         this.compositeDistortion(this.maskTarget, outputTarget, strength, encoder);
      }
   }

   public void renderMaskAndDistort(GpuBuffer vertices, GpuBuffer indices, IndexType indexType, int indexCount, float strength, PrimitiveTopology topology) {
      this.renderMaskAndDistort(vertices.slice(), indices, indexType, indexCount, strength, topology);
   }

   private void compositeDistortion(RenderTarget mask, RenderTarget outputTarget, float strength, CommandEncoder encoder) {
      encoder.copyTextureToTexture(outputTarget.getColorTexture(), this.sceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, outputTarget.width, outputTarget.height);

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 16).putFloat(strength).align(16).get();
         GpuBufferSlice uniformSlice = TransientGpuUpload.uploadUniform(uniformData);

         try (RenderPass compositePass = encoder.createRenderPass(() -> COMPOSITE_PASS_LABEL, outputTarget.getColorTextureView(), Optional.empty())) {
            compositePass.setPipeline(COMPOSITE_PIPELINE);
            compositePass.bindTexture(SCENE_SAMPLER_BINDING, this.sceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            compositePass.bindTexture(GLOW_SAMPLER_BINDING, mask.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            compositePass.setUniform(DISTORTION_UNIFORM_BINDING, uniformSlice);
            compositePass.draw(3, 1, 0, 0);
         }
      }
   }

   public void renderMaskAndDistort(GpuBuffer vertices, GpuBuffer indices, IndexType indexType, int indexCount, float strength) {
      this.renderMaskAndDistort(vertices, indices, indexType, indexCount, strength, PrimitiveTopology.QUADS);
   }

   private void ensureTargets(int width, int height, boolean needsMask) {
      if (this.sceneCopyTarget == null) {
         this.sceneCopyTarget = new TextureTarget(SCENE_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.sceneCopyTarget.width != width || this.sceneCopyTarget.height != height) {
         this.sceneCopyTarget.resize(width, height);
      }

      if (needsMask) {
         if (this.maskTarget == null) {
            this.maskTarget = new TextureTarget(MASK_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
         } else if (this.maskTarget.width != width || this.maskTarget.height != height) {
            this.maskTarget.resize(width, height);
         }
      }
   }
}
