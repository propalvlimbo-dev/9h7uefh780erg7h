package sg.render.pipeline;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;
import sg.ec.EncryptedTextureRegistry;

public class TexturePass implements MinecraftAccess, UiRenderPassSupport {
   private final @com.google.gson.annotations.SerializedName("сх")  RenderPipeline pipeline;
   private final @com.google.gson.annotations.SerializedName("п")  CommandEncoder encoder;
   private static final String PIPELINE_LOCATION = "rose/texture";
   private static final String VERTEX_SHADER = "texture";
   private static final String FRAGMENT_SHADER = "texture";
   private static final String FORMS_UNIFORM_LAYOUT_NAME = "Forms";
   private static final String COLORS_UNIFORM_LAYOUT_NAME = "Colors";
   private static final String SCISSORS_UNIFORM_LAYOUT_NAME = "Scissors";
   private static final String TEXTURE_SAMPLER_LAYOUT_NAME = "Sampler0";
   private static final String TEXTURE_SAMPLER_BINDING = "Sampler0";
   private static final float CORNER_SMOOTHNESS = 1.5F;
   private static final String FORMS_UNIFORM_BINDING = "Forms";
   private static final String COLORS_UNIFORM_BINDING = "Colors";
   private static final String SCISSORS_UNIFORM_BINDING = "Scissors";
   private static final float NDC_LEFT = -1.0F;
   private static final String PASS_LABEL = "texturePass";

   public void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, Identifier textureId, FilterMode filterMode) {
      if (!this.isOutsideClip(x, y, width, height) && !this.isFullyTransparent(colors)) {
         SharedGeometryBuffer geometry = SharedGeometryBuffer.getInstance();
         BufferBuilder builder = geometry.begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX);
         this.appendTexturedQuad(builder, x, y, width, height, uvRect);
         AbstractTexture texture = EncryptedTextureRegistry.Х(textureId);
         RenderPass renderPass = this.encoder.createRenderPass(() -> PASS_LABEL, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
         RenderSystem.bindDefaultUniforms(renderPass);
         renderPass.bindTexture(TEXTURE_SAMPLER_BINDING, texture.getTextureView(), filterMode != null ? RenderSystem.getSamplerCache().getClampToEdge(filterMode) : texture.getSampler());

         try (MemoryStack stack = MemoryStack.stackPush()) {
            float smoothness = CORNER_SMOOTHNESS;
            if (cornerRadii.x == 0.0F && cornerRadii.y == 0.0F && cornerRadii.z == 0.0F && cornerRadii.w == 0.0F) {
               smoothness = 0.0F;
            }

            ByteBuffer formsData = Std140Builder.onStack(stack, 32).putVec4(cornerRadii).putVec2(height, width).putFloat(smoothness).get();
            GpuBufferSlice clipUniform = this.uploadClipUniform(new Vector4f(x, y, width, height), stack);
            GpuBufferSlice formsUniform = TransientGpuUpload.uploadUniform(formsData);
            GpuBufferSlice colorsUniform = this.uploadCornerColors(stack, colors);
            renderPass.setUniform(FORMS_UNIFORM_BINDING, formsUniform);
            renderPass.setUniform(COLORS_UNIFORM_BINDING, colorsUniform);
            renderPass.setUniform(SCISSORS_UNIFORM_BINDING, clipUniform);
         }

         this.drawAndClose(builder, renderPass, this.pipeline);
      }
   }

   public void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, Identifier textureId) {
      this.drawTexture(x, y, width, height, cornerRadii, uvRect, colors, textureId, null);
   }

   public TexturePass() {
      this.pipeline = RenderPipelineBuilder.builder()
         .withLocation(PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX, PrimitiveTopology.QUADS)
         .withUniform(FORMS_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(COLORS_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(SCISSORS_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withSampler(TEXTURE_SAMPLER_LAYOUT_NAME)
         .build();
      this.encoder = RenderSystem.getDevice().createCommandEncoder();
   }

   public void appendTexturedQuad(BufferBuilder builder, float x, float y, float width, float height, Vector4f uvRect) {
      float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * width;
      float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * height;
      float ndcLeft = NDC_LEFT + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
      float ndcBottom = 1.0F - y * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
      builder.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom, 0.0F).setUv(uvRect.x, uvRect.y + uvRect.w);
      builder.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom, 0.0F).setUv(uvRect.x + uvRect.z, uvRect.y + uvRect.w);
      builder.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F).setUv(uvRect.x + uvRect.z, uvRect.y);
      builder.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom + ndcHeight, 0.0F).setUv(uvRect.x, uvRect.y);
   }

   public void precompilePipeline(ShaderSource shaderSource) {
      RenderSystem.getDevice().precompilePipeline(this.pipeline, shaderSource);
   }
}
