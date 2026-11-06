package sg.render.pipeline;

import com.google.common.base.Suppliers;
import com.google.gson.annotations.SerializedName;
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
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;

public class UiBlurPass implements MinecraftAccess, UiRenderPassSupport {
   private final @com.google.gson.annotations.SerializedName("сд")  RenderPipeline pipeline;
   @SerializedName("А")
   private final Supplier<TextureTarget> sceneCopyTargetSupplier;
   private final @com.google.gson.annotations.SerializedName("п")  RenderTarget mainTarget;
   private final @com.google.gson.annotations.SerializedName("Д")  CommandEncoder encoder;
   private static final String PIPELINE_LOCATION = "rose/blur";
   private static final String VERTEX_SHADER = "blur";
   private static final String FRAGMENT_SHADER = "blur";
   private static final String FORMS_UNIFORM_LAYOUT_NAME = "Forms";
   private static final String COLORS_UNIFORM_LAYOUT_NAME = "Colors";
   private static final String SCISSORS_UNIFORM_LAYOUT_NAME = "Scissors";
   private static final String SCENE_SAMPLER_LAYOUT_NAME = "Sampler0";
   private static final String SCENE_SAMPLER_BINDING = "Sampler0";
   private static final float CORNER_SMOOTHNESS = 1.5F;
   private static final String FORMS_UNIFORM_BINDING = "Forms";
   private static final String COLORS_UNIFORM_BINDING = "Colors";
   private static final String SCISSORS_UNIFORM_BINDING = "Scissors";
   private static final String PASS_LABEL = "blurPass";
   private static final String SCENE_COPY_TARGET_LABEL = "temp";

   public UiBlurPass() {
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
         .withSampler(SCENE_SAMPLER_LAYOUT_NAME)
         .build();
      this.sceneCopyTargetSupplier = Suppliers.memoize(() -> new TextureTarget(SCENE_COPY_TARGET_LABEL, 1920, 1024, false, GpuFormat.RGBA8_UNORM));
      this.mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
      this.encoder = RenderSystem.getDevice().createCommandEncoder();
   }

   public void drawBlur(float x, float y, float width, float height, float blurRadius, Vector4f cornerRadii, Vector4i colors) {
      if (!this.isOutsideClip(x, y, width, height) && !this.isFullyTransparent(colors)) {
         TextureTarget sceneCopyTarget = this.sceneCopyTargetSupplier.get();
         if (sceneCopyTarget.width != this.mainTarget.width || sceneCopyTarget.height != this.mainTarget.height) {
            sceneCopyTarget.resize(this.mainTarget.width, this.mainTarget.height);
         }

         this.encoder.copyTextureToTexture(this.mainTarget.getColorTexture(), sceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, this.mainTarget.width, this.mainTarget.height);
         SharedGeometryBuffer geometry = SharedGeometryBuffer.getInstance();
         BufferBuilder builder = geometry.begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
         this.appendQuad(builder, x, y, width, height);
         RenderPass renderPass = this.encoder.createRenderPass(() -> PASS_LABEL, this.mainTarget.getColorTextureView(), Optional.empty());
         renderPass.bindTexture(SCENE_SAMPLER_BINDING, sceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         RenderSystem.bindDefaultUniforms(renderPass);

         try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer formsData = Std140Builder.onStack(stack, 32).putVec4(cornerRadii).putVec2(height, width).putFloat(CORNER_SMOOTHNESS).putFloat(blurRadius).get();
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
