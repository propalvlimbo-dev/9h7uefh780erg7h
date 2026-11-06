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
import sg.util.time.ColorMath;

public class RectangleRenderPass implements UiRenderPassSupport, MinecraftAccess {
   private final @com.google.gson.annotations.SerializedName("сЭ")  RenderPipeline pipeline;
   private final @com.google.gson.annotations.SerializedName("е")  CommandEncoder encoder;
   private static final String PIPELINE_LOCATION = "rose/rect";
   private static final String VERTEX_SHADER = "rect";
   private static final String FRAGMENT_SHADER = "rect";
   private static final String RECTANGLE_UNIFORM_LAYOUT_NAME = "AllInOne";
   private static final float CORNER_SMOOTHNESS = 1.5F;
   private static final float FIRST_COLOR_GLOBAL_ALPHA_SCALE = 255.0F;
   private static final float SECOND_COLOR_GLOBAL_ALPHA_SCALE = 255.0F;
   private static final float THIRD_COLOR_GLOBAL_ALPHA_SCALE = 255.0F;
   private static final float FOURTH_COLOR_GLOBAL_ALPHA_SCALE = 255.0F;
   private static final String RECTANGLE_UNIFORM_BINDING = "AllInOne";
   private static final String PASS_LABEL = "rectPass";

   public void precompilePipeline(ShaderSource shaderSource) {
      RenderSystem.getDevice().precompilePipeline(this.pipeline, shaderSource);
   }

   public void drawRoundedRect(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors) {
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

            Vector4f firstColor = ColorMath.toRgbaVector(colors.x);
            Vector4f secondColor = ColorMath.toRgbaVector(colors.y);
            Vector4f thirdColor = ColorMath.toRgbaVector(colors.z);
            Vector4f fourthColor = ColorMath.toRgbaVector(colors.w);
            firstColor.w = firstColor.w * (UiDrawing.getGlobalAlpha() / FIRST_COLOR_GLOBAL_ALPHA_SCALE);
            secondColor.w = secondColor.w * (UiDrawing.getGlobalAlpha() / SECOND_COLOR_GLOBAL_ALPHA_SCALE);
            thirdColor.w = thirdColor.w * (UiDrawing.getGlobalAlpha() / THIRD_COLOR_GLOBAL_ALPHA_SCALE);
            fourthColor.w = fourthColor.w * (UiDrawing.getGlobalAlpha() / FOURTH_COLOR_GLOBAL_ALPHA_SCALE);
            ByteBuffer rectangleData = Std140Builder.onStack(stack, 128)
               .putVec4(cornerRadii)
               .putVec2(height, width)
               .putFloat(smoothness)
               .putVec4(firstColor)
               .putVec4(secondColor)
               .putVec4(thirdColor)
               .putVec4(fourthColor)
               .putVec4(this.getNormalizedClipRect(new Vector4f(x, y, width, height)))
               .putFloat(UiDrawing.isScissorEnabled() ? 1.0F : 0.0F)
               .get();
            GpuBufferSlice rectangleUniform = TransientGpuUpload.uploadUniform(rectangleData);
            renderPass.setUniform(RECTANGLE_UNIFORM_BINDING, rectangleUniform);
         }

         this.drawAndClose(builder, renderPass, this.pipeline);
      }
   }

   public RectangleRenderPass() {
      this.pipeline = RenderPipelineBuilder.builder()
         .withLocation(PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION, PrimitiveTopology.QUADS)
         .withUniform(RECTANGLE_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT)
         .build();
      this.encoder = RenderSystem.getDevice().createCommandEncoder();
   }
}
