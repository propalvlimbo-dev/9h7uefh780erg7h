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
import java.util.Arrays;
import java.util.Optional;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.theme.ThemeColorSlot;

public final class ThemeWave implements MinecraftAccess {
   public static final ThemeWave INSTANCE = new ThemeWave();
   private static final int PALETTE_SLOT_COUNT = ThemeColorSlot.values().length;
   private static final int field_4_ = 192;
   private static final int UNIFORM_DATA_BYTES = (2 + PALETTE_SLOT_COUNT * 2 + 192) * 16;
   private static final float пю = 1.72F;
   private final @com.google.gson.annotations.SerializedName("сО")  RenderPipeline pipeline;
   private @com.google.gson.annotations.SerializedName("щ")  TextureTarget currentSceneCopyTarget;
   private @com.google.gson.annotations.SerializedName("Г")  TextureTarget baseSceneCopyTarget;
   private @com.google.gson.annotations.SerializedName("ш")  int[] oldPaletteArgb;
   private @com.google.gson.annotations.SerializedName("И")  int[] newPaletteArgb;
   private @com.google.gson.annotations.SerializedName("пЩ")  float originXNormalized;
   private @com.google.gson.annotations.SerializedName("пу")  float originYNormalized;
   private @com.google.gson.annotations.SerializedName("ЯБ")  long transitionStartedAtNanos;
   private @com.google.gson.annotations.SerializedName("ХН")  boolean baseSnapshotReady;
   private @com.google.gson.annotations.SerializedName("Хн")  boolean collectingUiRects;
   @SerializedName("4ы")
   private int uiRectSuppressionDepth;
   private final @com.google.gson.annotations.SerializedName("Ы")  float[] uiRectBounds;
   @SerializedName("4й")
   private int uiRectCount;
   private static final String PIPELINE_LOCATION = "rose/theme_wave";
   private static final String VERTEX_SHADER = "theme_wave";
   private static final String FRAGMENT_SHADER = "theme_wave";
   private static final String THEME_WAVE_UNIFORM_LAYOUT_NAME = "ThemeWave";
   private static final String CURRENT_SCENE_SAMPLER_LAYOUT_NAME = "Sampler0";
   private static final String BASE_SCENE_SAMPLER_LAYOUT_NAME = "BaseSampler";
   private static final float NANOS_PER_SECOND = 1.0E9F;
   private static final float TRANSITION_DURATION_SECONDS = 1.72F;
   private static final float ЩЬ = 0.095F;
   private static final String CURRENT_SCENE_SAMPLER_BINDING = "Sampler0";
   private static final String BASE_SCENE_SAMPLER_BINDING = "BaseSampler";
   private static final String THEME_WAVE_UNIFORM_BINDING = "ThemeWave";
   private static final String CURRENT_SCENE_COPY_TARGET_LABEL = "theme-wave-copy";
   private static final String BASE_SCENE_COPY_TARGET_LABEL = "theme-wave-background";
   private static final float RED_CHANNEL_SCALE = 255.0F;
   private static final float GREEN_CHANNEL_SCALE = 255.0F;
   private static final float BLUE_CHANNEL_SCALE = 255.0F;
   private static final float ALPHA_CHANNEL_SCALE = 255.0F;
   private static final String PASS_LABEL = "theme wave";
   private static final String UNIFORM_BUFFER_LABEL = "theme wave uniforms";

   private void ensureSnapshotTargets(int width, int height) {
      if (this.currentSceneCopyTarget == null) {
         this.currentSceneCopyTarget = new TextureTarget(CURRENT_SCENE_COPY_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.currentSceneCopyTarget.width != width || this.currentSceneCopyTarget.height != height) {
         this.currentSceneCopyTarget.resize(width, height);
      }

      if (this.baseSceneCopyTarget == null) {
         this.baseSceneCopyTarget = new TextureTarget(BASE_SCENE_COPY_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (this.baseSceneCopyTarget.width != width || this.baseSceneCopyTarget.height != height) {
         this.baseSceneCopyTarget.resize(width, height);
      }
   }

   public void beginFrame() {
      if (this.transitionStartedAtNanos != 0L) {
         this.uiRectCount = 0;
         this.collectingUiRects = true;
         RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         this.ensureSnapshotTargets(mainTarget.width, mainTarget.height);
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
         encoder.copyTextureToTexture(mainTarget.getColorTexture(), this.baseSceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, mainTarget.width, mainTarget.height);
         this.baseSnapshotReady = true;
         this.registerUiRect(0.0F, 0.0F, mainTarget.width, mainTarget.height);
      }
   }

   public void popUiRectSuppression() {
      if (this.uiRectSuppressionDepth > 0) {
         this.uiRectSuppressionDepth--;
      }
   }

   private static int[] copyPaletteSlots(int[] inputPalette) {
      int[] copiedPalette = new int[PALETTE_SLOT_COUNT];

      for (int slotIndex = 0; slotIndex < copiedPalette.length; slotIndex++) {
         copiedPalette[slotIndex] = slotIndex < inputPalette.length ? inputPalette[slotIndex] : -1;
      }

      return copiedPalette;
   }

   public void registerUiRect(float x, float y, float width, float height) {
      if (this.collectingUiRects && this.uiRectSuppressionDepth <= 0 && this.transitionStartedAtNanos != 0L && !(width <= 0.0F) && !(height <= 0.0F)) {
         float left = x;
         float top = y;
         float right = x + width;
         float bottom = y + height;
         if (UiDrawing.isScissorEnabled()) {
            left = Math.max(left, UiDrawing.getScissor().x);
            top = Math.max(top, UiDrawing.getScissor().y);
            right = Math.min(right, UiDrawing.getScissor().x + UiDrawing.getScissor().z);
            bottom = Math.min(bottom, UiDrawing.getScissor().y + UiDrawing.getScissor().w);
         }

         if (!(right <= left) && !(bottom <= top)) {
            for (int rectIndex = 0; rectIndex < this.uiRectCount; rectIndex++) {
               int rectOffset = rectIndex * 4;
               float storedLeft = this.uiRectBounds[rectOffset];
               float storedTop = this.uiRectBounds[rectOffset + 1];
               float storedRight = this.uiRectBounds[rectOffset + 2];
               float storedBottom = this.uiRectBounds[rectOffset + 3];
               if (right >= storedLeft && left <= storedRight && bottom >= storedTop && top <= storedBottom) {
                  this.uiRectBounds[rectOffset] = Math.min(left, storedLeft);
                  this.uiRectBounds[rectOffset + 1] = Math.min(top, storedTop);
                  this.uiRectBounds[rectOffset + 2] = Math.max(right, storedRight);
                  this.uiRectBounds[rectOffset + 3] = Math.max(bottom, storedBottom);
                  return;
               }
            }

            if (this.uiRectCount < 192) {
               int newRectOffset = this.uiRectCount++ * 4;
               this.uiRectBounds[newRectOffset] = left;
               this.uiRectBounds[newRectOffset + 1] = top;
               this.uiRectBounds[newRectOffset + 2] = right;
               this.uiRectBounds[newRectOffset + 3] = bottom;
            }
         }
      }
   }

   public void renderTransition() {
      this.collectingUiRects = false;
      if (this.transitionStartedAtNanos != 0L && this.oldPaletteArgb != null && this.newPaletteArgb != null && this.baseSnapshotReady) {
         float progress = (float)(System.nanoTime() - this.transitionStartedAtNanos) / NANOS_PER_SECOND / TRANSITION_DURATION_SECONDS;
         if (progress >= 1.0F) {
            this.transitionStartedAtNanos = 0L;
            this.oldPaletteArgb = null;
            this.newPaletteArgb = null;
            this.baseSnapshotReady = false;
         } else {
            RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
            this.ensureSnapshotTargets(mainTarget.width, mainTarget.height);
            CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
            encoder.copyTextureToTexture(mainTarget.getColorTexture(), this.currentSceneCopyTarget.getColorTexture(), 0, 0, 0, 0, 0, mainTarget.width, mainTarget.height);

            try (MemoryStack stack = MemoryStack.stackPush()) {
               Std140Builder uniformBuilder = Std140Builder.onStack(stack, UNIFORM_DATA_BYTES)
                  .putVec4(this.originXNormalized, this.originYNormalized, progress, 1.0F)
                  .putVec4(mainTarget.width, mainTarget.height, ЩЬ, this.uiRectCount);

               for (int oldColorArgb : this.oldPaletteArgb) {
                  appendNormalizedArgb(uniformBuilder, oldColorArgb);
               }

               for (int newColorArgb : this.newPaletteArgb) {
                  appendNormalizedArgb(uniformBuilder, newColorArgb);
               }

               for (int rectIndex = 0; rectIndex < 192; rectIndex++) {
                  if (rectIndex < this.uiRectCount) {
                     int rectOffset = rectIndex * 4;
                     uniformBuilder.putVec4(
                        this.uiRectBounds[rectOffset] / mainTarget.width,
                        1.0F - this.uiRectBounds[rectOffset + 3] / mainTarget.height,
                        this.uiRectBounds[rectOffset + 2] / mainTarget.width,
                        1.0F - this.uiRectBounds[rectOffset + 1] / mainTarget.height
                     );
                  } else {
                     uniformBuilder.putVec4(0.0F, 0.0F, 0.0F, 0.0F);
                  }
               }

               ByteBuffer uniformData = uniformBuilder.get();

               try (
                  GpuBuffer uniformBuffer = RenderSystem.getDevice().createBuffer(() -> UNIFORM_BUFFER_LABEL, 128, uniformData);
                  RenderPass renderPass = encoder.createRenderPass(() -> PASS_LABEL, mainTarget.getColorTextureView(), Optional.empty());
               ) {
                  RenderSystem.bindDefaultUniforms(renderPass);
                  renderPass.setPipeline(this.pipeline);
                  renderPass.bindTexture(CURRENT_SCENE_SAMPLER_BINDING, this.currentSceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                  renderPass.bindTexture(BASE_SCENE_SAMPLER_BINDING, this.baseSceneCopyTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                  renderPass.setUniform(THEME_WAVE_UNIFORM_BINDING, uniformBuffer);
                  renderPass.draw(3, 1, 0, 0);
               }
            }

            this.baseSnapshotReady = false;
         }
      }
   }

   public boolean isTransitionActive() {
      return this.transitionStartedAtNanos != 0L;
   }

   private static void appendNormalizedArgb(Std140Builder builder, int colorArgb) {
      builder.putVec4((colorArgb >> 16 & 0xFF) / RED_CHANNEL_SCALE, (colorArgb >> 8 & 0xFF) / GREEN_CHANNEL_SCALE, (colorArgb & 0xFF) / BLUE_CHANNEL_SCALE, (colorArgb >>> 24 & 0xFF) / ALPHA_CHANNEL_SCALE);
   }

   private ThemeWave() {
      this.pipeline = RenderPipelineBuilder.builder()
         .withLocation(PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(THEME_WAVE_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(CURRENT_SCENE_SAMPLER_LAYOUT_NAME)
         .withSampler(BASE_SCENE_SAMPLER_LAYOUT_NAME)
         .build();
      this.uiRectBounds = new float[768];
   }

   public void pushUiRectSuppression() {
      this.uiRectSuppressionDepth++;
   }

   public void startTransition(float originX, float originY, int[] oldColorsArgb, int[] newColorsArgb) {
      if (oldColorsArgb != null && newColorsArgb != null && !Arrays.equals(oldColorsArgb, newColorsArgb)) {
         RenderTarget mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         float safeWidth = Math.max(1.0F, mainTarget.width);
         float safeHeight = Math.max(1.0F, mainTarget.height);
         this.originXNormalized = Math.clamp(originX / safeWidth, 0.0F, 1.0F);
         this.originYNormalized = Math.clamp(1.0F - originY / safeHeight, 0.0F, 1.0F);
         this.oldPaletteArgb = copyPaletteSlots(oldColorsArgb);
         this.newPaletteArgb = copyPaletteSlots(newColorsArgb);
         this.transitionStartedAtNanos = System.nanoTime();
         this.baseSnapshotReady = false;
         this.uiRectCount = 0;
      }
   }
}
