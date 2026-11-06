package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
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
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;

public final class WorldColorGradingPass implements MinecraftAccess {
   public static final WorldColorGradingPass INSTANCE = new WorldColorGradingPass();
   @SerializedName("В")
   private final RenderPipeline pipeline;
   @SerializedName("В")
   private TextureTarget sceneCopyTarget;
   private static final String PIPELINE_LOCATION = "rose/color_plus_grade";
   private static final String VERTEX_SHADER = "world_grade";
   private static final String FRAGMENT_SHADER = "world_grade";
   private static final String COLOR_GRADE_UNIFORM_LAYOUT_NAME = "ColorPlusGrade";
   private static final String SCENE_SAMPLER_LAYOUT_NAME = "Sampler0";
   private static final String SCENE_SAMPLER_BINDING = "Sampler0";
   private static final String COLOR_GRADE_UNIFORM_BINDING = "ColorPlusGrade";
   private static final String SCENE_COPY_TARGET_LABEL = "color-plus-scene";
   private static final String PASS_LABEL = "colorPlusGrade";
   private static final String UNIFORM_BUFFER_LABEL = "colorPlusGradeData";

   private void drawGradePass(
      CommandEncoder encoder,
      RenderTarget outputTarget,
      float strength,
      float exposure,
      float contrast,
      float saturation,
      float vibrance,
      float gamma,
      float temperature,
      float tint,
      float sharpness,
      float vignette
   ) {
      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 112)
            .putVec2(outputTarget.width, outputTarget.height)
            .putVec4(strength, exposure, contrast, saturation)
            .putVec4(vibrance, gamma, temperature, tint)
            .putVec4(0.0F, 0.0F, 0.0F, 0.0F)
            .putVec4(0.0F, 0.0F, 0.0F, 0.0F)
            .putVec4(0.0F, 0.0F, 0.0F, 0.0F)
            .putVec4(sharpness, vignette, 0.0F, 0.0F)
            .get();

         try (
            GpuBuffer uniformBuffer = RenderSystem.getDevice().createBuffer(() -> UNIFORM_BUFFER_LABEL, 128, uniformData);
            RenderPass renderPass = encoder.createRenderPass(() -> PASS_LABEL, outputTarget.getColorTextureView(), Optional.empty());
         ) {
            renderPass.setPipeline(this.pipeline);
            renderPass.bindTexture(SCENE_SAMPLER_BINDING, this.sceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.setUniform(COLOR_GRADE_UNIFORM_BINDING, uniformBuffer);
            renderPass.draw(3, 1, 0, 0);
         }
      }
   }

   public void applyColorGrade(float strength, float exposure, float contrast, float saturation, float vibrance, float gamma, float temperature, float tint, float sharpness, float vignette) {
      RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
      this.ensureSceneCopyTarget(mainTarget.width, mainTarget.height);
      CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
      encoder.copyTextureToTexture(mainTarget.getColorTexture(), this.sceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, mainTarget.width, mainTarget.height);
      this.drawGradePass(encoder, mainTarget, strength, exposure, contrast, saturation, vibrance, gamma, temperature, tint, sharpness, vignette);
   }

   private void ensureSceneCopyTarget(int width, int height) {
      if (this.sceneCopyTarget == null) {
         this.sceneCopyTarget = new TextureTarget(SCENE_COPY_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else {
         if (this.sceneCopyTarget.width != width || this.sceneCopyTarget.height != height) {
            this.sceneCopyTarget.resize(width, height);
         }
      }
   }

   private WorldColorGradingPass() {
      this.pipeline = RenderPipelineBuilder.builder()
         .withLocation(PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(COLOR_GRADE_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(SCENE_SAMPLER_LAYOUT_NAME)
         .build();
   }
}
