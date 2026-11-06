package sg.render.pipeline;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.mixin.RenderTypeAccessor;
import sg.mixin.RenderPipelinesAccessor;
import sg.modules.impl.render.Chams;
import sg.render.WorldProjection;

public final class PrismaticChams {
   private static final int UNIFORM_BUFFER_BYTES = 112;
   private static final long START_TIME_NANOS = System.nanoTime();
   private static final Identifier SHADER_ID = ClientResourceIds.getCoreId(PrismaticChams.SHADER_PATH);
   private static final RenderPipeline THROUGH_WALLS_PIPELINE = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Identifier.fromNamespaceAndPath(PrismaticChams.VISIBLE_PIPELINE_NAMESPACE, PrismaticChams.VISIBLE_PIPELINE_PATH))
         .withVertexShader(SHADER_ID)
         .withFragmentShader(SHADER_ID)
         .withSampler(PrismaticChams.VISIBLE_PIPELINE_SAMPLER)
         .withUniform(PrismaticChams.VISIBLE_PIPELINE_UNIFORM, UniformType.UNIFORM_BUFFER)
         .withVertexFormat(DefaultVertexFormat.ENTITY, PrimitiveTopology.QUADS)
         .withCull(false)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withColorWrite(true, true)
         .withDepthWrite(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .build()
   );
   private static final RenderPipeline SCENE_DEPTH_PIPELINE = RenderPipelinesAccessor.register(
      RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Identifier.fromNamespaceAndPath(PrismaticChams.DEPTH_PIPELINE_NAMESPACE, PrismaticChams.DEPTH_PIPELINE_PATH))
         .withVertexShader(SHADER_ID)
         .withFragmentShader(SHADER_ID)
         .withSampler(PrismaticChams.DEPTH_PIPELINE_SAMPLER)
         .withUniform(PrismaticChams.DEPTH_PIPELINE_UNIFORM, UniformType.UNIFORM_BUFFER)
         .withVertexFormat(DefaultVertexFormat.ENTITY, PrimitiveTopology.QUADS)
         .withCull(false)
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withColorWrite(true, true)
         .withDepthWrite(true)
         .withBlend(BlendFunction.TRANSLUCENT)
         .build()
   );
   private static final RenderType RENDER_TYPE = RenderTypeAccessor.create(
      PrismaticChams.RENDER_TYPE_NAME, RenderSetup.builder(THROUGH_WALLS_PIPELINE).useLightmap().useOverlay().sortOnUpload().createRenderSetup()
   );
   private static final int SCENE_TEXTURE_USAGE = 5;
   private static final List<PrismaticChams.ModelSubmission> pendingModels = new ArrayList<>();
   private static GpuTexture sceneTexture;
   private static GpuTextureView sceneTextureView;
   private static GpuFormat sceneFormat;
   private static int sceneWidth;
   private static int sceneHeight;
   private static boolean sceneCaptured;
   private static final float MIN_OPACITY = 0.001F;
   private static final float MIN_ANIMATION_AMOUNT = 0.001F;
   private static final float HYBRID_PASS_THRESHOLD = 1.5F;
   private static final float DEPTH_PASS_THRESHOLD = 0.5F;
   private static final String DRAW_UNIFORM_NAME = "PrismaticChams";
   private static final String SCENE_SAMPLER_NAME = "Sampler0";
   private static final String OVERLAY_UNIFORM_NAME = "PrismaticChams";
   private static final String SCENE_TEXTURE_LABEL = "rose-prismatic-chams-screen";
   private static final float NANOS_PER_SECOND = 1.0E9F;
   private static final float MIN_CLIP_W = 1.0E-5F;
   private static final float NEGATIVE_MIN_CLIP_W = -1.0E-5F;
   private static final float POSITIVE_MIN_CLIP_W = 1.0E-5F;
   private static final float NDC_X_SCALE = 0.5F;
   private static final float NDC_X_OFFSET = 0.5F;
   private static final float NDC_Y_SCALE = 0.5F;
   private static final float NDC_Y_OFFSET = 0.5F;
   private static final int MISSING_ENTITY_ID = Integer.MIN_VALUE;
   private static final float SEED_X_FACTOR = 17.0F;
   private static final float SEED_Z_FACTOR = 31.0F;
   private static final int SEED_MASK = 65535;
   private static final float SEED_NORMALIZER = 65535.0F;
   private static final String RENDER_PASS_LABEL = "prismaticChams";
   private static final String SHADER_PATH = "prismatic_chams";
   private static final String VISIBLE_PIPELINE_NAMESPACE = "rose";
   private static final String VISIBLE_PIPELINE_PATH = "pipeline/prismatic_chams_visible";
   private static final String VISIBLE_PIPELINE_SAMPLER = "Sampler0";
   private static final String VISIBLE_PIPELINE_UNIFORM = "PrismaticChams";
   private static final String DEPTH_PIPELINE_NAMESPACE = "rose";
   private static final String DEPTH_PIPELINE_PATH = "pipeline/prismatic_chams_depth";
   private static final String DEPTH_PIPELINE_SAMPLER = "Sampler0";
   private static final String DEPTH_PIPELINE_UNIFORM = "PrismaticChams";
   private static final String RENDER_TYPE_NAME = "rose/prismatic_chams_visible";

   public static void beginFrame() {
      sceneCaptured = false;
      pendingModels.clear();
   }

   public static void initializeRenderType() {
      RENDER_TYPE.format();
   }

   private static void drawModel(Model model, LivingEntityRenderState renderState, Pose pose, int packedLight, int packedOverlay, int color, float depthPass, float animationAmount) {
      Chams chams = Chams.getActiveInstance();
      if (chams != null && model != null && renderState != null && pose != null) {
         model.setupAnim(renderState);
         PoseStack modelViewPoseStack = new PoseStack();
         modelViewPoseStack.mulPose(createModelViewMatrix(pose));
         BufferBuilder vertices = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.ENTITY);
         model.renderToBuffer(modelViewPoseStack, vertices, packedLight, packedOverlay, ARGB.opaque(color));
         MeshData mesh = vertices.build();
         if (mesh != null) {
            try {
               DrawState drawState = mesh.drawState();
               int indexCount = drawState.indexCount();
               if (indexCount <= 0) {
                  return;
               }

               AutoStorageIndexBuffer sequentialIndices = RenderSystem.getSequentialBuffer(drawState.primitiveTopology());
               GpuBuffer indexBuffer = sequentialIndices.getBuffer(indexCount);
               IndexType indexType = sequentialIndices.type();
               GpuBufferSlice vertexBuffer = TransientGpuUpload.uploadVertices(mesh.vertexBuffer(), drawState.format().getVertexSize());
               RenderTarget renderTarget = Minecraft.getInstance().gameRenderer.mainRenderTarget();
               if (renderTarget == null || renderTarget.getColorTextureView() == null) {
                  return;
               }

               if (sceneCaptured && sceneTextureView != null && !sceneTextureView.isClosed()) {
                  boolean hybridPass = depthPass > HYBRID_PASS_THRESHOLD;
                  GpuBufferSlice primaryUniform = createUniformBuffer(chams, renderState, pose, hybridPass ? 1.0F : depthPass, animationAmount);
                  GpuBufferSlice overlayUniform = hybridPass ? createUniformBuffer(chams, renderState, pose, 0.0F, animationAmount) : null;
                  CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
                  RenderPipeline primaryPipeline = depthPass > DEPTH_PASS_THRESHOLD ? SCENE_DEPTH_PIPELINE : THROUGH_WALLS_PIPELINE;

                  try (RenderPass renderPass = encoder.createRenderPass(
                        () -> RENDER_PASS_LABEL, renderTarget.getColorTextureView(), Optional.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty()
                     )) {
                     RenderSystem.bindDefaultUniforms(renderPass);
                     renderPass.setPipeline(primaryPipeline);
                     renderPass.setUniform(DRAW_UNIFORM_NAME, primaryUniform);
                     renderPass.bindTexture(SCENE_SAMPLER_NAME, sceneTextureView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                     renderPass.setVertexBuffer(0, vertexBuffer);
                     renderPass.setIndexBuffer(indexBuffer, indexType);
                     renderPass.drawIndexed(indexCount, 1, 0, 0, 0);
                     if (hybridPass) {
                        renderPass.setPipeline(THROUGH_WALLS_PIPELINE);
                        renderPass.setUniform(OVERLAY_UNIFORM_NAME, overlayUniform);
                        renderPass.drawIndexed(indexCount, 1, 0, 0, 0);
                     }

                     return;
                  }
               }
            } finally {
               mesh.close();
            }
         }
      }
   }

   private static Vec3 getCameraPosition() {
      Minecraft minecraft = Minecraft.getInstance();
      return minecraft != null && minecraft.gameRenderer != null && minecraft.gameRenderer.mainCamera() != null ? minecraft.gameRenderer.mainCamera().position() : Vec3.ZERO;
   }

   private static void ensureSceneTexture(GpuTexture sourceTexture, int width, int height) {
      GpuFormat format = sourceTexture.getFormat();
      if (sceneTexture == null || sceneTextureView == null || sceneTexture.isClosed() || sceneTextureView.isClosed() || sceneWidth != width || sceneHeight != height || sceneFormat != format) {
         closeSceneTexture();
         sceneTexture = RenderSystem.getDevice().createTexture(SCENE_TEXTURE_LABEL, 5, format, width, height, 1, 1);
         sceneTextureView = RenderSystem.getDevice().createTextureView(sceneTexture);
         sceneFormat = format;
         sceneWidth = width;
         sceneHeight = height;
      }
   }

   private static Matrix4f createModelViewMatrix(Pose pose) {
      Matrix4f modelViewMatrix = new Matrix4f();
      Matrix4f viewMatrix = WorldProjection.getViewMatrix();
      if (viewMatrix != null) {
         modelViewMatrix.set(viewMatrix);
      }

      return modelViewMatrix.mul(pose.pose());
   }

   public static void submitModel(Model model, LivingEntityRenderState renderState, Pose pose, int packedLight, int packedOverlay, int color, float depthPass) {
      if (model != null && renderState != null && pose != null) {
         pendingModels.add(new PrismaticChams.ModelSubmission(model, renderState, pose.copy(), packedLight, packedOverlay, color, depthPass));
      }
   }

   private PrismaticChams() {
   }

   private static void closeSceneTexture() {
      GpuTextureView oldView = sceneTextureView;
      GpuTexture oldTexture = sceneTexture;
      sceneTextureView = null;
      sceneTexture = null;
      sceneFormat = null;
      sceneWidth = 0;
      sceneHeight = 0;
      if (oldView != null && !oldView.isClosed()) {
         oldView.close();
      }

      if (oldTexture != null && !oldTexture.isClosed()) {
         oldTexture.close();
      }
   }

   public static void captureSceneIfNeeded() {
      if (!pendingModels.isEmpty() && !sceneCaptured) {
         captureScene();
      }
   }

   private static Vector4f projectEntityScreenPosition(Pose pose, Vector4f resolutionInfo) {
      Matrix4f projectionMatrix = WorldProjection.getProjectionMatrix();
      if (projectionMatrix == null) {
         projectionMatrix = new Matrix4f();
      }

      Vector4f clipPosition = new Vector4f(0.0F, 1.0F, 0.0F, 1.0F);
      createModelViewMatrix(pose).transform(clipPosition);
      projectionMatrix.transform(clipPosition);
      float safeClipW = Math.abs(clipPosition.w) < MIN_CLIP_W ? (clipPosition.w < 0.0F ? NEGATIVE_MIN_CLIP_W : POSITIVE_MIN_CLIP_W) : clipPosition.w;
      float screenX = (clipPosition.x / safeClipW * NDC_X_SCALE + NDC_X_OFFSET) * resolutionInfo.x;
      float screenY = (clipPosition.y / safeClipW * NDC_Y_SCALE + NDC_Y_OFFSET) * resolutionInfo.y;
      return new Vector4f(screenX, screenY, 0.0F, 0.0F);
   }

   public static RenderType getRenderType() {
      return RENDER_TYPE;
   }

   public static void renderPendingModels() {
      if (!pendingModels.isEmpty()) {
         try {
            Chams chams = Chams.getActiveInstance();
            if (chams == null || chams.getOpacity() <= MIN_OPACITY) {
               return;
            }

            captureScene();
            if (!sceneCaptured || sceneTextureView == null || sceneTextureView.isClosed()) {
               return;
            }

            for (PrismaticChams.ModelSubmission submission : pendingModels) {
               float animationAmount = chams.getAnimationAmount(submission.state);
               if (!(animationAmount <= MIN_ANIMATION_AMOUNT)) {
                  drawModel(submission.model, submission.state, submission.entry, submission.light, submission.overlay, submission.color, submission.depthPass, animationAmount);
               }
            }
         } finally {
            pendingModels.clear();
         }
      }
   }

   private static Vector4f getResolutionInfo() {
      int width = sceneCaptured && sceneWidth > 0 ? sceneWidth : 0;
      int height = sceneCaptured && sceneHeight > 0 ? sceneHeight : 0;
      if (width <= 0 || height <= 0) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft != null && minecraft.getWindow() != null) {
            width = minecraft.getWindow().getWidth();
            height = minecraft.getWindow().getHeight();
         }
      }

      width = Math.max(1, width);
      height = Math.max(1, height);
      return new Vector4f(width, height, 1.0F / width, 1.0F / height);
   }

   private static void captureScene() {
      if (!sceneCaptured) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft != null) {
            RenderTarget renderTarget = minecraft.gameRenderer.mainRenderTarget();
            if (renderTarget != null) {
               GpuTexture sourceTexture = renderTarget.getColorTexture();
               if (sourceTexture != null && !sourceTexture.isClosed()) {
                  int width = Math.max(1, sourceTexture.getWidth(0));
                  int height = Math.max(1, sourceTexture.getHeight(0));
                  ensureSceneTexture(sourceTexture, width, height);
                  if (sceneTexture != null && sceneTextureView != null && !sceneTexture.isClosed() && !sceneTextureView.isClosed()) {
                     RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(sourceTexture, sceneTexture, 0, 0, 0, 0, 0, width, height);
                     sceneWidth = width;
                     sceneHeight = height;
                     sceneCaptured = true;
                  }
               }
            }
         }
      }
   }

   private static float getShaderSeed(LivingEntityRenderState renderState) {
      int entityId = Chams.getEntityId(renderState);
      int seed = entityId == MISSING_ENTITY_ID ? Float.floatToIntBits((float)renderState.x * SEED_X_FACTOR + (float)renderState.z * SEED_Z_FACTOR) : entityId;
      seed ^= seed << 13;
      seed ^= seed >>> 17;
      seed ^= seed << 5;
      return (seed & SEED_MASK) / SEED_NORMALIZER;
   }

   private static GpuBufferSlice createUniformBuffer(Chams chams, LivingEntityRenderState renderState, Pose pose, float depthPass, float animationAmount) {
      float[] accentTop = chams.getUpperAccentColor();
      float[] accentBottom = chams.getLowerAccentColor();
      Vec3 cameraPosition = getCameraPosition();
      float elapsedSeconds = (float)(System.nanoTime() - START_TIME_NANOS) / NANOS_PER_SECOND;
      float shaderSeed = getShaderSeed(renderState);
      float depthMode = chams.isSceneDepthMode() ? 0.0F : (chams.isHybridDepthMode() ? 1.0F : 2.0F);
      Vector4f resolutionInfo = getResolutionInfo();
      Vector4f entityScreenPosition = projectEntityScreenPosition(pose, resolutionInfo);

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 112)
            .putVec4(accentTop[0], accentTop[1], accentTop[2], accentTop[3])
            .putVec4(accentBottom[0], accentBottom[1], accentBottom[2], accentBottom[3])
            .putVec4((float)cameraPosition.x, (float)cameraPosition.y, (float)cameraPosition.z, elapsedSeconds)
            .putVec4(chams.getIntensity(), chams.getOpacity(), chams.getRefraction(), chams.getShaderMode())
            .putVec4(animationAmount, depthPass, shaderSeed, depthMode)
            .putVec4(resolutionInfo.x, resolutionInfo.y, resolutionInfo.z, resolutionInfo.w)
            .putVec4(entityScreenPosition.x, entityScreenPosition.y, entityScreenPosition.z, entityScreenPosition.w)
            .get();
         return TransientGpuUpload.uploadUniform(uniformData);
      }
   }

   private record ModelSubmission(Model model, LivingEntityRenderState state, Pose entry, int light, int overlay, int color, float depthPass) {
   }
}
