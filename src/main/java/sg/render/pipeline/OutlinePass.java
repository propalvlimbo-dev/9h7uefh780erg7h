package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
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
import org.joml.Vector4i;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;

public class OutlinePass implements UiRenderPassSupport, MinecraftAccess {
   @SerializedName("3")
   private final RenderPipeline pipeline;
   private final @com.google.gson.annotations.SerializedName("Х")  CommandEncoder encoder;
   private static final String PIPELINE_LOCATION = "rose/outline";
   private static final String VERTEX_SHADER = "outline";
   private static final String FRAGMENT_SHADER = "outline";
   private static final String FORMS_UNIFORM_LAYOUT_NAME = "Forms";
   private static final String COLORS_UNIFORM_LAYOUT_NAME = "Colors";
   private static final String SCISSORS_UNIFORM_LAYOUT_NAME = "Scissors";
   private static final float CORNER_SMOOTHNESS = 1.5F;
   private static final float THICKNESS_INSET = 1.5F;
   private static final String FORMS_UNIFORM_BINDING = "Forms";
   private static final String COLORS_UNIFORM_BINDING = "Colors";
   private static final String SCISSORS_UNIFORM_BINDING = "Scissors";
   private static final String PASS_LABEL = "outlinePass";

   public OutlinePass() {
      this.pipeline = RenderPipelineBuilder.builder()
         .withLocation(PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(FORMS_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(COLORS_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withUniform(SCISSORS_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT)
         .build();
      this.encoder = RenderSystem.getDevice().createCommandEncoder();
   }

   public void drawOutline(float x, float y, float width, float height, float thickness, Vector4f cornerRadii, Vector4i colors) {
      if (!this.isOutsideClip(x, y, width, height) && !this.isFullyTransparent(colors)) {
         SharedGeometryBuffer geometry = SharedGeometryBuffer.getInstance();
         BufferBuilder builder = geometry.begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
         this.appendQuad(builder, x, y, width, height);
         RenderPass renderPass = this.encoder.createRenderPass(() -> PASS_LABEL, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
         RenderSystem.bindDefaultUniforms(renderPass);

         try (MemoryStack stack = MemoryStack.stackPush()) {
            float smoothness = CORNER_SMOOTHNESS;
            if (cornerRadii.x == 0.0F && cornerRadii.y == 0.0F && cornerRadii.z == 0.0F && cornerRadii.w == 0.0F) {
               smoothness = 0.0F;
            }

            ByteBuffer formsData = Std140Builder.onStack(stack, 32).putVec4(cornerRadii).putVec2(height, width).putFloat(smoothness).putFloat(thickness - THICKNESS_INSET).get();
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
}
