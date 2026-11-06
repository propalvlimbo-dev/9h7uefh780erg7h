package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Optional;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.util.time.ColorMath;

public final class NightixAurora implements AutoCloseable {
   private final @com.google.gson.annotations.SerializedName("ВЧ")  RenderPipeline pipeline;
   private @com.google.gson.annotations.SerializedName("о")  GpuBuffer skyUniformBuffer;
   private @com.google.gson.annotations.SerializedName("O")  GpuBuffer viewUniformBuffer;
   private final @com.google.gson.annotations.SerializedName("Р")  Matrix4f inverseProjectionMatrix;
   @SerializedName("3")
   private final Matrix4f inverseViewMatrix;
   private static final String PIPELINE_LOCATION = "rose/nightix_aurora";
   private static final String VERTEX_SHADER = "nightix_aurora";
   private static final String FRAGMENT_SHADER = "nightix_aurora";
   private static final String PIPELINE_SKY_UNIFORM = "ShaderSkyData";
   private static final String PIPELINE_VIEW_UNIFORM = "AuroraView";
   private static final long SKY_UNIFORM_BYTES = 96L;
   private static final long VIEW_UNIFORM_BYTES = 128L;
   private static final float VANILLA_SKY_OPACITY_FACTOR = 0.78F;
   private static final float MILLIS_PER_SECOND = 1000.0F;
   private static final String DRAW_SKY_UNIFORM = "ShaderSkyData";
   private static final String DRAW_VIEW_UNIFORM = "AuroraView";
   private static final String RENDER_PASS_LABEL = "Nightix Aurora";
   private static final String VIEW_BUFFER_LABEL = "Aurora camera";
   private static final String SKY_BUFFER_LABEL = "Aurora settings";

   public NightixAurora() {
      this.pipeline = RenderPipelineBuilder.builder()
         .withLocation(PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withUniform(PIPELINE_SKY_UNIFORM, UniformType.UNIFORM_BUFFER)
         .withUniform(PIPELINE_VIEW_UNIFORM, UniformType.UNIFORM_BUFFER)
         .build();
      this.inverseProjectionMatrix = new Matrix4f();
      this.inverseViewMatrix = new Matrix4f();
   }

   public void renderAurora(
      RenderTarget renderTarget, Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, long animationStartMillis, float strength, float speed, float scale, float starAmount, int primaryArgb, int secondaryArgb, boolean blendVanillaSky
   ) {
      if (this.skyUniformBuffer == null) {
         this.skyUniformBuffer = RenderSystem.getDevice().createBuffer(() -> SKY_BUFFER_LABEL, 136, SKY_UNIFORM_BYTES);
         this.viewUniformBuffer = RenderSystem.getDevice().createBuffer(() -> VIEW_BUFFER_LABEL, 136, VIEW_UNIFORM_BYTES);
      }

      Vector4f primaryColor = ColorMath.toRgbaVector(primaryArgb);
      Vector4f secondaryColor = ColorMath.toRgbaVector(secondaryArgb);
      float opacityFactor = blendVanillaSky ? VANILLA_SKY_OPACITY_FACTOR : 1.0F;
      CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

      try (MemoryStack stack = MemoryStack.stackPush()) {
         encoder.writeToBuffer(
            this.skyUniformBuffer.slice(),
            Std140Builder.onStack(stack, 96)
               .putVec4(renderTarget.width, renderTarget.height, (float)(System.currentTimeMillis() - animationStartMillis) / MILLIS_PER_SECOND, opacityFactor)
               .putVec4(primaryColor.x, primaryColor.y, primaryColor.z, primaryColor.w * opacityFactor)
               .putVec4(secondaryColor.x, secondaryColor.y, secondaryColor.z, secondaryColor.w * opacityFactor)
               .putVec4(1.0F, 1.0F, 1.0F, opacityFactor)
               .putVec4(0.0F, speed, scale, strength)
               .putVec4(starAmount, blendVanillaSky ? 1.0F : 0.0F, 0.0F, 0.0F)
               .get()
         );
         encoder.writeToBuffer(
            this.viewUniformBuffer.slice(), Std140Builder.onStack(stack, 128).putMat4f(this.inverseProjectionMatrix.set(sourceProjectionMatrix).invert()).putMat4f(this.inverseViewMatrix.set(sourceViewMatrix).invert()).get()
         );
      }

      try (RenderPass renderPass = encoder.createRenderPass(() -> RENDER_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty())) {
         renderPass.setPipeline(this.pipeline);
         renderPass.setUniform(DRAW_SKY_UNIFORM, this.skyUniformBuffer);
         renderPass.setUniform(DRAW_VIEW_UNIFORM, this.viewUniformBuffer);
         renderPass.draw(3, 1, 0, 0);
      }
   }

   @Override
   public void close() {
      if (this.skyUniformBuffer != null) {
         this.skyUniformBuffer.close();
      }

      if (this.viewUniformBuffer != null) {
         this.viewUniformBuffer.close();
      }

      this.skyUniformBuffer = this.viewUniformBuffer = null;
   }
}
