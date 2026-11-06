package sg.render.pipeline;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
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
import java.nio.ByteBuffer;
import java.util.Optional;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;

public final class EntityEspOuterGlowRenderer implements AutoCloseable, MinecraftAccess {
   private static final int сР = 32;
   private static final int с3 = 80;
   private static final RenderPipeline maskSeedPipeline = RenderPipelineBuilder.builder()
      .withLocation(EntityEspOuterGlowRenderer.MASK_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspOuterGlowRenderer.MASK_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspOuterGlowRenderer.MASK_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withSampler(EntityEspOuterGlowRenderer.MASK_SAMPLER_LAYOUT_NAME)
      .build();
   private static final RenderPipeline blurPipeline = RenderPipelineBuilder.builder()
      .withLocation(EntityEspOuterGlowRenderer.BLUR_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspOuterGlowRenderer.BLUR_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspOuterGlowRenderer.BLUR_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspOuterGlowRenderer.BLUR_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspOuterGlowRenderer.BLUR_SAMPLER_LAYOUT_NAME)
      .build();
   private static final RenderPipeline glowCompositePipeline = RenderPipelineBuilder.builder()
      .withLocation(EntityEspOuterGlowRenderer.COMPOSITE_PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(EntityEspOuterGlowRenderer.COMPOSITE_VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(EntityEspOuterGlowRenderer.COMPOSITE_FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(BlendFunction.LIGHTNING)
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(EntityEspOuterGlowRenderer.COMPOSITE_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(EntityEspOuterGlowRenderer.COMPOSITE_BLOOM_SAMPLER_LAYOUT_NAME)
      .withSampler(EntityEspOuterGlowRenderer.COMPOSITE_MASK_SAMPLER_LAYOUT_NAME)
      .build();
   private static TextureTarget blurPingTarget;
   private static TextureTarget blurPongTarget;
   private static GpuBuffer horizontalBlurUniformBuffer;
   private static GpuBuffer verticalBlurUniformBuffer;
   private static GpuBuffer glowUniformBuffer;
   private static final float MAX_BLUR_RADIUS = 12.0F;
   private static final String MASK_INPUT_SAMPLER_BINDING = "InSampler";
   private static final String COMPOSITE_BLOOM_SAMPLER_BINDING = "BloomSampler";
   private static final String COMPOSITE_MASK_SAMPLER_BINDING = "MaskSampler";
   private static final String COMPOSITE_UNIFORM_BINDING = "EspOuterGlow";
   private static final String BLUR_INPUT_SAMPLER_BINDING = "InSampler";
   private static final String BLUR_UNIFORM_BINDING = "EspBlur";
   private static final String HORIZONTAL_BLUR_UNIFORM_BUFFER_LABEL = "entityEspOtherBlurHorizontalUniforms";
   private static final String VERTICAL_BLUR_UNIFORM_BUFFER_LABEL = "entityEspOtherBlurVerticalUniforms";
   private static final String COMPOSITE_UNIFORM_BUFFER_LABEL = "entityEspOtherGlowUniforms";
   private static final float SECONDARY_GLOW_RED_SCALE = 0.5F;
   private static final float SECONDARY_GLOW_GREEN_SCALE = 0.5F;
   private static final float SECONDARY_GLOW_BLUE_SCALE = 0.5F;
   private static final String BLUR_PING_TARGET_LABEL = "entity-esp-other-blur-horizontal";
   private static final String BLUR_PONG_TARGET_LABEL = "entity-esp-other-blur-vertical";
   private static final float RED_BYTE_SCALE = 255.0F;
   private static final float GREEN_BYTE_SCALE = 255.0F;
   private static final float BLUE_BYTE_SCALE = 255.0F;
   private static final float ALPHA_BYTE_SCALE = 255.0F;
   private static final String BLUR_PASS_LABEL = "entityEspOtherBlur";
   private static final String COMPOSITE_PASS_LABEL = "entityEspOtherGlow";
   private static final String MASK_PASS_LABEL = "entityEspOtherMask";
   private static final String MASK_PIPELINE_LOCATION = "rose/entity_esp_other_mask";
   private static final String MASK_VERTEX_SHADER = "hands_fullscreen";
   private static final String MASK_FRAGMENT_SHADER = "entity_esp_mask";
   private static final String MASK_SAMPLER_LAYOUT_NAME = "InSampler";
   private static final String BLUR_PIPELINE_LOCATION = "rose/entity_esp_other_blur";
   private static final String BLUR_VERTEX_SHADER = "hands_fullscreen";
   private static final String BLUR_FRAGMENT_SHADER = "entity_esp_gaussian_blur";
   private static final String BLUR_UNIFORM_LAYOUT_NAME = "EspBlur";
   private static final String BLUR_SAMPLER_LAYOUT_NAME = "InSampler";
   private static final String COMPOSITE_PIPELINE_LOCATION = "rose/entity_esp_other_glow";
   private static final String COMPOSITE_VERTEX_SHADER = "hands_fullscreen";
   private static final String COMPOSITE_FRAGMENT_SHADER = "entity_esp_outer_glow";
   private static final String COMPOSITE_UNIFORM_LAYOUT_NAME = "EspOuterGlow";
   private static final String COMPOSITE_BLOOM_SAMPLER_LAYOUT_NAME = "BloomSampler";
   private static final String COMPOSITE_MASK_SAMPLER_LAYOUT_NAME = "MaskSampler";

   private static void updateGlowUniforms(CommandEncoder encoder, RenderTarget sourceMaskTarget, int glowColorArgb, float glowStrength, float saturation, boolean useMaskColor, float innerGlowStrength, int outlineColorArgb, float outlineWidthPixels) {
      glowUniformBuffer = ensureUniformBuffer(glowUniformBuffer, 80, COMPOSITE_UNIFORM_BUFFER_LABEL);
      float glowRed = getRedNormalized(glowColorArgb);
      float glowGreen = getGreenNormalized(glowColorArgb);
      float glowBlue = getBlueNormalized(glowColorArgb);

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 80)
            .putVec4(glowStrength, useMaskColor ? 1.0F : 0.0F, saturation, innerGlowStrength)
            .putVec4(glowRed, glowGreen, glowBlue, 1.0F)
            .putVec4(glowRed * SECONDARY_GLOW_RED_SCALE, glowGreen * SECONDARY_GLOW_GREEN_SCALE, glowBlue * SECONDARY_GLOW_BLUE_SCALE, outlineWidthPixels)
            .putVec4(getRedNormalized(outlineColorArgb), getGreenNormalized(outlineColorArgb), getBlueNormalized(outlineColorArgb), getAlphaNormalized(outlineColorArgb))
            .putVec4(sourceMaskTarget.width, sourceMaskTarget.height, outlineWidthPixels, 0.0F)
            .get();
         encoder.writeToBuffer(glowUniformBuffer.slice(), uniformData);
      }
   }

   private static GpuBuffer ensureUniformBuffer(GpuBuffer buffer, int byteSize, String label) {
      return buffer != null && !buffer.isClosed() ? buffer : RenderSystem.getDevice().createBuffer(() -> label, 136, byteSize);
   }

   private static float getBlueNormalized(int colorArgb) {
      return (colorArgb & 0xFF) / BLUE_BYTE_SCALE;
   }

   private static boolean hasColorTextureView(RenderTarget target) {
      return target != null && target.getColorTextureView() != null;
   }

   private static void ensureBlurTargets(int width, int height) {
      if (blurPingTarget == null || blurPongTarget == null) {
         blurPingTarget = new TextureTarget(BLUR_PING_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
         blurPongTarget = new TextureTarget(BLUR_PONG_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (blurPingTarget.width != width || blurPingTarget.height != height) {
         blurPingTarget.resize(width, height);
         blurPongTarget.resize(width, height);
      }
   }

   public static void compositeOuterGlow(RenderTarget sourceMaskTarget, RenderTarget outputTarget, int glowColorArgb, float blurRadius, float glowStrength, float saturation, boolean useMaskColor, float innerGlowStrength, int outlineColorArgb, float outlineWidthPixels) {
      if (hasColorTextureView(sourceMaskTarget) && hasColorTextureView(outputTarget)) {
         int blurWidth = Math.max(1, sourceMaskTarget.width / 2);
         int blurHeight = Math.max(1, sourceMaskTarget.height / 2);
         float clampedBlurRadius = Math.clamp(blurRadius, 1.0F, MAX_BLUR_RADIUS);
         ensureBlurTargets(blurWidth, blurHeight);
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
         updateBlurUniforms(encoder, blurWidth, blurHeight, clampedBlurRadius);
         updateGlowUniforms(encoder, sourceMaskTarget, glowColorArgb, glowStrength, saturation, useMaskColor, innerGlowStrength, outlineColorArgb, outlineWidthPixels);

         try (RenderPass maskPass = encoder.createRenderPass(() -> MASK_PASS_LABEL, blurPingTarget.getColorTextureView(), Optional.of(new Vector4f(0.0F)))) {
            maskPass.setPipeline(maskSeedPipeline);
            maskPass.bindTexture(MASK_INPUT_SAMPLER_BINDING, sourceMaskTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            maskPass.draw(3, 1, 0, 0);
         }

         drawBlurPass(encoder, blurPingTarget, blurPongTarget, horizontalBlurUniformBuffer);
         drawBlurPass(encoder, blurPongTarget, blurPingTarget, verticalBlurUniformBuffer);

         try (RenderPass compositePass = encoder.createRenderPass(() -> COMPOSITE_PASS_LABEL, outputTarget.getColorTextureView(), Optional.empty())) {
            compositePass.setPipeline(glowCompositePipeline);
            compositePass.bindTexture(COMPOSITE_BLOOM_SAMPLER_BINDING, blurPingTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            compositePass.bindTexture(COMPOSITE_MASK_SAMPLER_BINDING, sourceMaskTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            compositePass.setUniform(COMPOSITE_UNIFORM_BINDING, glowUniformBuffer);
            compositePass.draw(3, 1, 0, 0);
         }
      }
   }

   private static void drawBlurPass(CommandEncoder encoder, RenderTarget sourceTarget, RenderTarget destinationTarget, GpuBuffer blurUniformBuffer) {
      try (RenderPass renderPass = encoder.createRenderPass(() -> BLUR_PASS_LABEL, destinationTarget.getColorTextureView(), Optional.of(new Vector4f(0.0F)))) {
         renderPass.setPipeline(blurPipeline);
         renderPass.bindTexture(BLUR_INPUT_SAMPLER_BINDING, sourceTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         renderPass.setUniform(BLUR_UNIFORM_BINDING, blurUniformBuffer);
         renderPass.draw(3, 1, 0, 0);
      }
   }

   private static float getGreenNormalized(int colorArgb) {
      return (colorArgb >>> 8 & 0xFF) / GREEN_BYTE_SCALE;
   }

   private static float getAlphaNormalized(int colorArgb) {
      return (colorArgb >>> 24 & 0xFF) / ALPHA_BYTE_SCALE;
   }

   @Override
   public void close() {
      if (blurPingTarget != null) {
         blurPingTarget.destroyBuffers();
         blurPingTarget = null;
      }

      if (blurPongTarget != null) {
         blurPongTarget.destroyBuffers();
         blurPongTarget = null;
      }

      if (horizontalBlurUniformBuffer != null) {
         horizontalBlurUniformBuffer.close();
         horizontalBlurUniformBuffer = null;
      }

      if (verticalBlurUniformBuffer != null) {
         verticalBlurUniformBuffer.close();
         verticalBlurUniformBuffer = null;
      }

      if (glowUniformBuffer != null) {
         glowUniformBuffer.close();
         glowUniformBuffer = null;
      }
   }

   public static void compositeOuterGlow(RenderTarget sourceMaskTarget, RenderTarget outputTarget, int glowColorArgb, float blurRadius, float glowStrength, float saturation) {
      compositeOuterGlow(sourceMaskTarget, outputTarget, glowColorArgb, blurRadius, glowStrength, saturation, false, 0.0F, 0, 0.0F);
   }

   private static void updateBlurUniforms(CommandEncoder encoder, int width, int height, float blurRadius) {
      horizontalBlurUniformBuffer = ensureUniformBuffer(horizontalBlurUniformBuffer, 32, HORIZONTAL_BLUR_UNIFORM_BUFFER_LABEL);
      verticalBlurUniformBuffer = ensureUniformBuffer(verticalBlurUniformBuffer, 32, VERTICAL_BLUR_UNIFORM_BUFFER_LABEL);

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer horizontalUniformData = Std140Builder.onStack(stack, 32).putVec2(1.0F / width, 1.0F / height).putVec2(1.0F, 0.0F).putFloat(blurRadius).align(16).get();
         encoder.writeToBuffer(horizontalBlurUniformBuffer.slice(), horizontalUniformData);
         ByteBuffer verticalUniformData = Std140Builder.onStack(stack, 32).putVec2(1.0F / width, 1.0F / height).putVec2(0.0F, 1.0F).putFloat(blurRadius).align(16).get();
         encoder.writeToBuffer(verticalBlurUniformBuffer.slice(), verticalUniformData);
      }
   }

   private static float getRedNormalized(int colorArgb) {
      return (colorArgb >>> 16 & 0xFF) / RED_BYTE_SCALE;
   }
}
