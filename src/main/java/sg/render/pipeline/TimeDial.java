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

public class TimeDial implements MinecraftAccess, UiRenderPassSupport {
   private final @com.google.gson.annotations.SerializedName("ВЮ")  RenderPipeline pipeline;
   private final @com.google.gson.annotations.SerializedName("Ъ")  CommandEncoder encoder;
   private static final String PIPELINE_LOCATION = "rose/time_dial";
   private static final String VERTEX_SHADER = "time_dial";
   private static final String FRAGMENT_SHADER = "time_dial";
   private static final String TIME_DIAL_UNIFORM_LAYOUT_NAME = "TimeDial";
   private static final String SCISSORS_UNIFORM_LAYOUT_NAME = "Scissors";
   private static final float GLOBAL_ALPHA_SCALE = 255.0F;
   private static final String TIME_DIAL_UNIFORM_BINDING = "TimeDial";
   private static final String SCISSORS_UNIFORM_BINDING = "Scissors";
   private static final String PASS_LABEL = "timeDialPass";

   public void drawTimeDial(float x, float y, float width, float height, float phase, float hover) {
      if (!this.isOutsideClip(x, y, width, height)) {
         BufferBuilder builder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
         this.appendQuad(builder, x, y, width, height);
         RenderPass renderPass = this.encoder.createRenderPass(() -> PASS_LABEL, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
         RenderSystem.bindDefaultUniforms(renderPass);

         try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer timeDialData = Std140Builder.onStack(stack, 32)
               .putVec2(width, height)
               .putFloat(phase)
               .putFloat(hover)
               .putFloat(UiDrawing.getGlobalAlpha() / GLOBAL_ALPHA_SCALE)
               .align(16)
               .get();
            GpuBufferSlice timeDialUniform = TransientGpuUpload.uploadUniform(timeDialData);
            GpuBufferSlice clipUniform = this.uploadClipUniform(new Vector4f(x, y, width, height), stack);
            renderPass.setUniform(TIME_DIAL_UNIFORM_BINDING, timeDialUniform);
            renderPass.setUniform(SCISSORS_UNIFORM_BINDING, clipUniform);
         }

         this.drawAndClose(builder, renderPass, this.pipeline);
      }
   }

   public TimeDial() {
      this.pipeline = RenderPipelineBuilder.builder()
         .withLocation(PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(TIME_DIAL_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(SCISSORS_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .build();
      this.encoder = RenderSystem.getDevice().createCommandEncoder();
   }
}
