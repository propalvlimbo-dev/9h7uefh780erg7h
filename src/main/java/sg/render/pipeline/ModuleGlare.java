package sg.render.pipeline;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.nio.ByteBuffer;
import java.util.Optional;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;

public class ModuleGlare implements UiRenderPassSupport, MinecraftAccess {
   private final @com.google.gson.annotations.SerializedName("Х")  RenderPipeline pipeline;
   private final @com.google.gson.annotations.SerializedName("В")  CommandEncoder encoder;
   private static final String PIPELINE_LOCATION = "rose/module_glare";
   private static final String VERTEX_SHADER = "module_glare";
   private static final String FRAGMENT_SHADER = "module_glare";
   private static final String GLARE_UNIFORM_LAYOUT_NAME = "GlareData";
   private static final String SCISSORS_UNIFORM_LAYOUT_NAME = "Scissors";
   private static final String GLARE_UNIFORM_BINDING = "GlareData";
   private static final String SCISSORS_UNIFORM_BINDING = "Scissors";
   private static final String PASS_LABEL = "moduleGlarePass";

   public ModuleGlare() {
      this.pipeline = RenderPipelineBuilder.builder()
         .withLocation(PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(GLARE_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(SCISSORS_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT)
         .build();
      this.encoder = RenderSystem.getDevice().createCommandEncoder();
   }

   public void drawGlare(float x, float y, float width, float height, float progress, float opacity, float glareSize, float cornerRadius) {
      SharedGeometryBuffer geometry = SharedGeometryBuffer.getInstance();
      BufferBuilder builder = geometry.begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
      this.appendQuad(builder, x, y, width, height);
      RenderPass renderPass = this.encoder.createRenderPass(() -> PASS_LABEL, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
      RenderSystem.bindDefaultUniforms(renderPass);

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer glareData = Std140Builder.onStack(stack, 48)
            .putVec4(new Vector4f(cornerRadius))
            .putVec4(new Vector4f(height, width, progress, opacity))
            .putVec4(new Vector4f(glareSize, 0.0F, 0.0F, 0.0F))
            .get();
         GpuBufferSlice clipUniform = this.uploadClipUniform(new Vector4f(x, y, width, height), stack);
         GpuBufferSlice glareUniform = TransientGpuUpload.uploadUniform(glareData);
         renderPass.setUniform(GLARE_UNIFORM_BINDING, glareUniform);
         renderPass.setUniform(SCISSORS_UNIFORM_BINDING, clipUniform);
      }

      this.drawAndClose(builder, renderPass, this.pipeline);
   }
}
