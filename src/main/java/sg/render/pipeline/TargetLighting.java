package sg.render.pipeline;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.BlendFactor;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.Optional;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;

public final class TargetLighting implements MinecraftAccess {
   private static final int А9 = 4;
   private static final int Ао = 4448;
   private static final RenderPipeline surfaceLightingPipeline = RenderPipelineBuilder.builder()
      .withLocation(TargetLighting.PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(TargetLighting.VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(TargetLighting.FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withBlend(new BlendFunction(BlendFactor.ONE, BlendFactor.ONE, BlendFactor.ZERO, BlendFactor.ONE))
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(TargetLighting.LIGHTING_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(TargetLighting.DEPTH_SAMPLER_LAYOUT_NAME)
      .withSampler(TargetLighting.SCENE_SAMPLER_LAYOUT_NAME)
      .build();
   private static final Vector3f[] lightViewPositions = new Vector3f[4];
   private static final Vector3f[] lightColors = new Vector3f[4];
   private static final float[] accumulatedLightWeights = new float[4];
   private static final float[] lightIntensities = new float[4];
   private static final float[] lightRanges = new float[4];
   private static final Matrix4f frameViewMatrix = new Matrix4f();
   private static final Matrix4f inverseProjectionMatrix = new Matrix4f();
   private static final Matrix4f inverseViewMatrix = new Matrix4f();
   private static final OcclusionVoxelGrid occlusionGrid = new OcclusionVoxelGrid();
   private static final Vector3f transformedLightPosition = new Vector3f();
   private static final Vector4f projectedCorner = new Vector4f();
   private static GpuTexture sceneDepthTexture;
   private static GpuTextureView sceneDepthView;
   private static GpuTexture sceneColorTexture;
   private static GpuTextureView sceneColorView;
   private static boolean sceneSnapshotReady;
   private static boolean collectingLights;
   private static int lightCount;
   private static final String DEPTH_TEXTURE_LABEL = "rose-target-lighting-depth";
   private static final String COLOR_TEXTURE_LABEL = "rose-target-lighting-color";
   private static final float MIN_LIGHT_SIZE = 0.02F;
   private static final float ALPHA_CHANNEL_SCALE = 255.0F;
   private static final float MIN_LIGHT_ALPHA = 0.025F;
   private static final float MAX_LIGHT_VIEW_DISTANCE_SQUARED = 6400.0F;
   private static final float MAX_LIGHT_WEIGHT_SIZE = 1.2F;
   private static final float RED_CHANNEL_SCALE = 255.0F;
   private static final float GREEN_CHANNEL_SCALE = 255.0F;
   private static final float BLUE_CHANNEL_SCALE = 255.0F;
   private static final float BASE_LIGHT_RANGE = 1.6F;
   private static final float LIGHT_RANGE_SIZE_FACTOR = 1.6F;
   private static final float MIN_LIGHT_RANGE = 1.8F;
   private static final float MAX_LIGHT_RANGE = 3.8F;
   private static final float LIGHT_MERGE_DISTANCE_SQUARED = 0.64F;
   private static final float MIN_PROJECTED_CORNER_W = 0.01F;
   private static final float NDC_X_SCALE = 0.5F;
   private static final float NDC_X_BIAS = 0.5F;
   private static final float NDC_Y_SCALE = 0.5F;
   private static final float NDC_Y_BIAS = 0.5F;
   private static final float SURFACE_LIGHT_GAIN = 4.5F;
   private static final String LIGHTING_UNIFORM_BINDING = "TargetLighting";
   private static final String DEPTH_SAMPLER_BINDING = "DepthSampler";
   private static final String SCENE_SAMPLER_BINDING = "SceneSampler";
   private static final String RENDER_PASS_LABEL = "Target surface lighting";
   private static final String PIPELINE_LOCATION = "rose/target_surface_light";
   private static final String VERTEX_SHADER = "hands_fullscreen";
   private static final String FRAGMENT_SHADER = "target_surface_light";
   private static final String LIGHTING_UNIFORM_LAYOUT_NAME = "TargetLighting";
   private static final String DEPTH_SAMPLER_LAYOUT_NAME = "DepthSampler";
   private static final String SCENE_SAMPLER_LAYOUT_NAME = "SceneSampler";

   public static void resetFrame() {
      sceneSnapshotReady = false;
      collectingLights = false;
      lightCount = 0;
   }

   public static void closeResources() {
      resetFrame();
      if (sceneDepthView != null && !sceneDepthView.isClosed()) {
         sceneDepthView.close();
      }

      if (sceneDepthTexture != null && !sceneDepthTexture.isClosed()) {
         sceneDepthTexture.close();
      }

      if (sceneColorView != null && !sceneColorView.isClosed()) {
         sceneColorView.close();
      }

      if (sceneColorTexture != null && !sceneColorTexture.isClosed()) {
         sceneColorTexture.close();
      }

      sceneDepthView = null;
      sceneDepthTexture = null;
      sceneColorView = null;
      sceneColorTexture = null;
      occlusionGrid.clear();
   }

   public static void beginLightCollection(boolean enabled, Matrix4f viewMatrix) {
      collectingLights = enabled && sceneSnapshotReady;
      lightCount = 0;
      frameViewMatrix.set(viewMatrix);
   }

   public static void addLight(float x, float y, float z, float size, int argbColor) {
      addLight(frameViewMatrix, x, y, z, size, argbColor);
   }

   public static void renderSurfaceLighting(Matrix4f projectionMatrix) {
      collectingLights = false;
      if (sceneSnapshotReady && lightCount != 0 && MINECRAFT_CLIENT.level != null && projectionMatrix != null) {
         sceneSnapshotReady = false;
         RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (mainTarget != null
            && mainTarget.getColorTextureView() != null
            && sceneDepthTexture != null
            && !sceneDepthTexture.isClosed()
            && sceneDepthView != null
            && !sceneDepthView.isClosed()
            && sceneColorTexture != null
            && !sceneColorTexture.isClosed()
            && sceneColorView != null
            && !sceneColorView.isClosed()
            && sceneDepthTexture.getWidth(0) == mainTarget.width
            && sceneDepthTexture.getHeight(0) == mainTarget.height) {
            inverseProjectionMatrix.set(projectionMatrix).invert();
            inverseViewMatrix.set(frameViewMatrix).invert();
            if (inverseProjectionMatrix.isFinite() && inverseViewMatrix.isFinite()) {
               float minScreenU = 1.0F;
               float minScreenV = 1.0F;
               float maxScreenU = 0.0F;
               float maxScreenV = 0.0F;

               for (int lightIndex = 0; lightIndex < lightCount; lightIndex++) {
                  Vector3f lightViewPosition = lightViewPositions[lightIndex];
                  float lightRange = lightRanges[lightIndex];
                  if (!(lightViewPosition.z - lightRange >= 0.0F)) {
                     for (int cornerIndex = 0; cornerIndex < 8; cornerIndex++) {
                        projectedCorner.set(
                              lightViewPosition.x + ((cornerIndex & 1) == 0 ? -lightRange : lightRange),
                              lightViewPosition.y + ((cornerIndex & 2) == 0 ? -lightRange : lightRange),
                              lightViewPosition.z + ((cornerIndex & 4) == 0 ? -lightRange : lightRange),
                              1.0F
                           )
                           .mul(projectionMatrix);
                        if (projectedCorner.w <= MIN_PROJECTED_CORNER_W) {
                           minScreenV = 0.0F;
                           minScreenU = 0.0F;
                           maxScreenV = 1.0F;
                           maxScreenU = 1.0F;
                           break;
                        }

                        float screenU = projectedCorner.x / projectedCorner.w * NDC_X_SCALE + NDC_X_BIAS;
                        float screenV = projectedCorner.y / projectedCorner.w * NDC_Y_SCALE + NDC_Y_BIAS;
                        minScreenU = Math.min(minScreenU, screenU);
                        minScreenV = Math.min(minScreenV, screenV);
                        maxScreenU = Math.max(maxScreenU, screenU);
                        maxScreenV = Math.max(maxScreenV, screenV);
                     }
                  }
               }

               int scissorMinX = Math.max(0, (int)Math.floor(minScreenU * mainTarget.width) - 2);
               int scissorMinY = Math.max(0, (int)Math.floor(minScreenV * mainTarget.height) - 2);
               int scissorMaxX = Math.min(mainTarget.width, (int)Math.ceil(maxScreenU * mainTarget.width) + 2);
               int scissorMaxY = Math.min(mainTarget.height, (int)Math.ceil(maxScreenV * mainTarget.height) + 2);
               if (scissorMaxX > scissorMinX && scissorMaxY > scissorMinY) {
                  Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
                  occlusionGrid.update(inverseViewMatrix, lightViewPositions, lightRanges, lightCount, cameraPosition);

                  try (MemoryStack stack = MemoryStack.stackPush()) {
                     Std140Builder uniformData = Std140Builder.onStack(stack, 4448)
                        .putMat4f(projectionMatrix)
                        .putMat4f(inverseProjectionMatrix)
                        .putVec4(1.0F / mainTarget.width, 1.0F / mainTarget.height, lightCount, SURFACE_LIGHT_GAIN);

                     for (int positionIndex = 0; positionIndex < 4; positionIndex++) {
                        Vector3f lightPosition = lightViewPositions[positionIndex];
                        uniformData.putVec4(lightPosition.x, lightPosition.y, lightPosition.z, lightRanges[positionIndex]);
                     }

                     for (int colorIndex = 0; colorIndex < 4; colorIndex++) {
                        Vector3f lightColor = lightColors[colorIndex];
                        uniformData.putVec4(lightColor.x, lightColor.y, lightColor.z, lightIntensities[colorIndex]);
                     }

                     uniformData.putMat4f(inverseViewMatrix).putVec4(occlusionGrid.getCameraRelativeOriginX(cameraPosition), occlusionGrid.getCameraRelativeOriginY(cameraPosition), occlusionGrid.getCameraRelativeOriginZ(cameraPosition), 2.0F);
                     int[] occluderWords = occlusionGrid.getOccluderWords();

                     for (int wordIndex = 0; wordIndex < occluderWords.length; wordIndex += 4) {
                        uniformData.putIVec4(occluderWords[wordIndex], occluderWords[wordIndex + 1], occluderWords[wordIndex + 2], occluderWords[wordIndex + 3]);
                     }

                     GpuBufferSlice uniformBuffer = TransientGpuUpload.uploadUniform(uniformData.get());

                     try (RenderPass renderPass = RenderSystem.getDevice()
                           .createCommandEncoder()
                           .createRenderPass(() -> RENDER_PASS_LABEL, mainTarget.getColorTextureView(), Optional.empty())) {
                        renderPass.setPipeline(surfaceLightingPipeline);
                        renderPass.setUniform(LIGHTING_UNIFORM_BINDING, uniformBuffer);
                        renderPass.bindTexture(DEPTH_SAMPLER_BINDING, sceneDepthView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                        renderPass.bindTexture(SCENE_SAMPLER_BINDING, sceneColorView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                        renderPass.enableScissor(scissorMinX, scissorMinY, scissorMaxX - scissorMinX, scissorMaxY - scissorMinY);
                        renderPass.draw(3, 1, 0, 0);
                     }
                  }
               }
            }
         }
      }
   }

   private TargetLighting() {
   }

   static {
      for (int lightIndex = 0; lightIndex < 4; lightIndex++) {
         lightViewPositions[lightIndex] = new Vector3f();
         lightColors[lightIndex] = new Vector3f();
      }
   }

   public static void addLight(Matrix4f poseMatrix, float x, float y, float z, float size, int argbColor) {
      if (collectingLights && !(size <= MIN_LIGHT_SIZE)) {
         float alpha = (argbColor >>> 24) / ALPHA_CHANNEL_SCALE;
         if (!(alpha <= MIN_LIGHT_ALPHA)) {
            poseMatrix.transformPosition(x, y, z, transformedLightPosition);
            if (transformedLightPosition.isFinite() && !(transformedLightPosition.lengthSquared() > MAX_LIGHT_VIEW_DISTANCE_SQUARED)) {
               float incomingWeight = Math.min(size, MAX_LIGHT_WEIGHT_SIZE) * alpha;
               int nearestLightIndex = -1;
               float nearestDistanceSquared = Float.POSITIVE_INFINITY;

               for (int lightIndex = 0; lightIndex < lightCount; lightIndex++) {
                  float distanceSquared = lightViewPositions[lightIndex].distanceSquared(transformedLightPosition);
                  if (distanceSquared < nearestDistanceSquared) {
                     nearestLightIndex = lightIndex;
                     nearestDistanceSquared = distanceSquared;
                  }
               }

               float red = (argbColor >> 16 & 0xFF) / RED_CHANNEL_SCALE;
               float green = (argbColor >> 8 & 0xFF) / GREEN_CHANNEL_SCALE;
               float blue = (argbColor & 0xFF) / BLUE_CHANNEL_SCALE;
               float range = Math.clamp(BASE_LIGHT_RANGE + size * LIGHT_RANGE_SIZE_FACTOR, MIN_LIGHT_RANGE, MAX_LIGHT_RANGE);
               if (nearestLightIndex >= 0 && (!(nearestDistanceSquared > LIGHT_MERGE_DISTANCE_SQUARED) || lightCount >= 4)) {
                  float combinedWeight = accumulatedLightWeights[nearestLightIndex] + incomingWeight;
                  float incomingWeightFraction = incomingWeight / combinedWeight;
                  lightViewPositions[nearestLightIndex].lerp(transformedLightPosition, incomingWeightFraction);
                  Vector3f existingColor = lightColors[nearestLightIndex];
                  existingColor.set(existingColor.x + (red - existingColor.x) * incomingWeightFraction, existingColor.y + (green - existingColor.y) * incomingWeightFraction, existingColor.z + (blue - existingColor.z) * incomingWeightFraction);
                  accumulatedLightWeights[nearestLightIndex] = combinedWeight;
                  lightIntensities[nearestLightIndex] = Math.max(lightIntensities[nearestLightIndex], incomingWeight);
                  lightRanges[nearestLightIndex] = Math.max(lightRanges[nearestLightIndex], range);
               } else {
                  int newLightIndex = lightCount++;
                  lightViewPositions[newLightIndex].set(transformedLightPosition);
                  lightColors[newLightIndex].set(red, green, blue);
                  accumulatedLightWeights[newLightIndex] = lightIntensities[newLightIndex] = incomingWeight;
                  lightRanges[newLightIndex] = range;
               }
            }
         }
      }
   }

   public static void captureSceneTextures() {
      if (!sceneSnapshotReady && MINECRAFT_CLIENT.level != null) {
         RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         if (mainTarget != null) {
            GpuTexture sourceDepthTexture = mainTarget.getDepthTexture();
            GpuTexture sourceColorTexture = mainTarget.getColorTexture();
            if (sourceDepthTexture != null && !sourceDepthTexture.isClosed() && sourceColorTexture != null && !sourceColorTexture.isClosed()) {
               int width = sourceDepthTexture.getWidth(0);
               int height = sourceDepthTexture.getHeight(0);
               if (width > 0 && height > 0) {
                  if (sceneDepthTexture == null
                     || sceneDepthTexture.isClosed()
                     || sceneDepthView == null
                     || sceneDepthView.isClosed()
                     || sceneDepthTexture.getWidth(0) != width
                     || sceneDepthTexture.getHeight(0) != height
                     || sceneDepthTexture.getFormat() != sourceDepthTexture.getFormat()
                     || sceneColorTexture == null
                     || sceneColorTexture.isClosed()
                     || sceneColorView == null
                     || sceneColorView.isClosed()
                     || sceneColorTexture.getFormat() != sourceColorTexture.getFormat()) {
                     closeResources();
                     sceneDepthTexture = RenderSystem.getDevice().createTexture(DEPTH_TEXTURE_LABEL, 5, sourceDepthTexture.getFormat(), width, height, 1, 1);
                     sceneDepthView = RenderSystem.getDevice().createTextureView(sceneDepthTexture);
                     sceneColorTexture = RenderSystem.getDevice().createTexture(COLOR_TEXTURE_LABEL, 5, sourceColorTexture.getFormat(), width, height, 1, 1);
                     sceneColorView = RenderSystem.getDevice().createTextureView(sceneColorTexture);
                  }

                  CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
                  encoder.copyTextureToTexture(sourceDepthTexture, sceneDepthTexture, 0, 0, 0, 0, 0, width, height);
                  encoder.copyTextureToTexture(sourceColorTexture, sceneColorTexture, 0, 0, 0, 0, 0, width, height);
                  sceneSnapshotReady = true;
               }
            }
         }
      }
   }
}
