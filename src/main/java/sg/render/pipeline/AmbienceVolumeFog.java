package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;

public final class AmbienceVolumeFog implements AutoCloseable {
   private static final int field_4Ч = 176;
   private final @com.google.gson.annotations.SerializedName("сЖ")  RenderPipeline compositePipeline;
   private final @com.google.gson.annotations.SerializedName("см")  RenderPipeline densityPipeline;
   private final @com.google.gson.annotations.SerializedName("l")  Matrix4f inverseProjectionMatrix;
   @SerializedName("8")
   private final Matrix4f inverseViewMatrix;
   private @com.google.gson.annotations.SerializedName("л")  GpuBuffer uniformBuffer;
   private @com.google.gson.annotations.SerializedName("必")  TextureTarget densityTarget;
   private static final String COMPOSITE_PIPELINE_LOCATION = "rose/ambience_volume_fog";
   private static final String COMPOSITE_VERTEX_SHADER = "hands_fullscreen";
   private static final String COMPOSITE_FRAGMENT_SHADER = "ambience_volume_fog";
   private static final String COMPOSITE_PIPELINE_DEPTH_SAMPLER = "DepthSampler";
   private static final String COMPOSITE_PIPELINE_DENSITY_SAMPLER = "DensitySampler";
   private static final String COMPOSITE_PIPELINE_UNIFORM = "VolumeFog";
   private static final String DENSITY_PIPELINE_LOCATION = "rose/ambience_fog_density";
   private static final String DENSITY_VERTEX_SHADER = "hands_fullscreen";
   private static final String DENSITY_FRAGMENT_SHADER = "ambience_volume_fog";
   private static final String DENSITY_PASS_DEFINE = "FOG_DENSITY_PASS";
   private static final String DENSITY_PIPELINE_DEPTH_SAMPLER = "DepthSampler";
   private static final String DENSITY_PIPELINE_UNIFORM = "VolumeFog";
   private static final float PROJECTION_DETERMINANT_EPSILON = 1.0E-8F;
   private static final float VIEW_DETERMINANT_EPSILON = 1.0E-8F;
   private static final long UNIFORM_BYTES = 176L;
   private static final String DENSITY_TARGET_LABEL = "Ambience fog density";
   private static final double CAMERA_X_WRAP_PERIOD = 8192.0;
   private static final double CAMERA_Y_WRAP_PERIOD = 8192.0;
   private static final double CAMERA_Z_WRAP_PERIOD = 8192.0;
   private static final float RED_BYTE_DIVISOR = 255.0F;
   private static final float GREEN_BYTE_DIVISOR = 255.0F;
   private static final float BLUE_BYTE_DIVISOR = 255.0F;
   private static final float OPTICAL_DEPTH_FACTOR = 3.0F;
   private static final String DENSITY_DRAW_UNIFORM = "VolumeFog";
   private static final String DENSITY_DRAW_DEPTH_SAMPLER = "DepthSampler";
   private static final String COMPOSITE_DRAW_UNIFORM = "VolumeFog";
   private static final String COMPOSITE_DRAW_DEPTH_SAMPLER = "DepthSampler";
   private static final String COMPOSITE_DRAW_DENSITY_SAMPLER = "DensitySampler";
   private static final String COMPOSITE_PASS_LABEL = "Ambience volumetric fog";
   private static final String DENSITY_PASS_LABEL = "Ambience fog density";
   private static final String UNIFORM_BUFFER_LABEL = "Ambience volume fog uniforms";

   public void renderVolumeFog(RenderTarget renderTarget, Matrix4f sourceProjectionMatrix, Matrix4f sourceViewMatrix, Vec3 cameraWorldPosition, float animationSeconds, int tintArgb, float fogStartDistance, float fogEndDistance) {
      if (renderTarget != null
         && renderTarget.getColorTextureView() != null
         && renderTarget.getDepthTextureView() != null
         && !renderTarget.getDepthTexture().isClosed()
         && sourceProjectionMatrix != null
         && sourceViewMatrix != null
         && !(Math.abs(sourceProjectionMatrix.determinant()) < PROJECTION_DETERMINANT_EPSILON)
         && !(Math.abs(sourceViewMatrix.determinant()) < VIEW_DETERMINANT_EPSILON)) {
         if (this.uniformBuffer == null || this.uniformBuffer.isClosed()) {
            this.uniformBuffer = RenderSystem.getDevice().createBuffer(() -> UNIFORM_BUFFER_LABEL, 136, UNIFORM_BYTES);
         }

         int densityWidth = Math.max(1, (renderTarget.width + 3) / 4);
         int densityHeight = Math.max(1, (renderTarget.height + 3) / 4);
         if (this.densityTarget == null) {
            this.densityTarget = new TextureTarget(DENSITY_TARGET_LABEL, densityWidth, densityHeight, false, GpuFormat.RGBA8_UNORM);
         } else if (this.densityTarget.width != densityWidth || this.densityTarget.height != densityHeight) {
            this.densityTarget.resize(densityWidth, densityHeight);
         }

         this.inverseProjectionMatrix.set(sourceProjectionMatrix).invert();
         this.inverseViewMatrix.set(sourceViewMatrix).invert();
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

         try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer uniformData = Std140Builder.onStack(stack, 176)
               .putMat4f(this.inverseProjectionMatrix)
               .putMat4f(this.inverseViewMatrix)
               .putVec4((float)(cameraWorldPosition.x % CAMERA_X_WRAP_PERIOD), (float)(cameraWorldPosition.y % CAMERA_Y_WRAP_PERIOD), (float)(cameraWorldPosition.z % CAMERA_Z_WRAP_PERIOD), animationSeconds)
               .putVec4((tintArgb >> 16 & 0xFF) / RED_BYTE_DIVISOR, (tintArgb >> 8 & 0xFF) / GREEN_BYTE_DIVISOR, (tintArgb & 0xFF) / BLUE_BYTE_DIVISOR, 1.0F)
               .putVec4(fogStartDistance, fogEndDistance * 2.0F, OPTICAL_DEPTH_FACTOR / Math.max(1.0F, fogEndDistance - fogStartDistance), 0.0F)
               .get();
            encoder.writeToBuffer(this.uniformBuffer.slice(), uniformData);
         }

         try (RenderPass densityPass = encoder.createRenderPass(() -> DENSITY_PASS_LABEL, this.densityTarget.getColorTextureView(), Optional.empty())) {
            densityPass.setPipeline(this.densityPipeline);
            densityPass.setUniform(DENSITY_DRAW_UNIFORM, this.uniformBuffer);
            densityPass.bindTexture(DENSITY_DRAW_DEPTH_SAMPLER, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            densityPass.draw(3, 1, 0, 0);
         }

         try (RenderPass compositePass = encoder.createRenderPass(() -> COMPOSITE_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty())) {
            compositePass.setPipeline(this.compositePipeline);
            compositePass.setUniform(COMPOSITE_DRAW_UNIFORM, this.uniformBuffer);
            compositePass.bindTexture(COMPOSITE_DRAW_DEPTH_SAMPLER, renderTarget.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            compositePass.bindTexture(COMPOSITE_DRAW_DENSITY_SAMPLER, this.densityTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            compositePass.draw(3, 1, 0, 0);
         }
      }
   }

   public AmbienceVolumeFog() {
      this.compositePipeline = RenderPipelineBuilder.builder()
         .withLocation(COMPOSITE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(COMPOSITE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(COMPOSITE_FRAGMENT_SHADER))
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(new BlendFunction(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA, BlendFactor.ZERO, BlendFactor.ONE))
         .withSampler(COMPOSITE_PIPELINE_DEPTH_SAMPLER)
         .withSampler(COMPOSITE_PIPELINE_DENSITY_SAMPLER)
         .withUniform(COMPOSITE_PIPELINE_UNIFORM, UniformType.UNIFORM_BUFFER)
         .build();
      this.densityPipeline = RenderPipelineBuilder.builder()
         .withLocation(DENSITY_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(DENSITY_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(DENSITY_FRAGMENT_SHADER))
         .withShaderDefine(DENSITY_PASS_DEFINE)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withSampler(DENSITY_PIPELINE_DEPTH_SAMPLER)
         .withUniform(DENSITY_PIPELINE_UNIFORM, UniformType.UNIFORM_BUFFER)
         .build();
      this.inverseProjectionMatrix = new Matrix4f();
      this.inverseViewMatrix = new Matrix4f();
   }

   @Override
   public void close() {
      if (this.densityTarget != null) {
         this.densityTarget.destroyBuffers();
         this.densityTarget = null;
      }

      if (this.uniformBuffer != null) {
         this.uniformBuffer.close();
         this.uniformBuffer = null;
      }
   }
}
