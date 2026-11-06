package sg.render.pipeline;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.gui.pip.GuiEntityRenderState;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.settings.EntityEspStyleSettings;

public final class PreviewFade {
   private static final Map<EntityRenderState, PreviewFade.PreviewState> pendingPreviewStates = new IdentityHashMap<>();
   private static final RenderPipeline fadePipeline = RenderPipelineBuilder.builder()
      .withLocation(PreviewFade.PIPELINE_LOCATION)
      .withVertexShader(ClientResourceIds.getCoreId(PreviewFade.VERTEX_SHADER))
      .withFragmentShader(ClientResourceIds.getCoreId(PreviewFade.FRAGMENT_SHADER))
      .withDepthTest(CompareOp.ALWAYS_PASS)
      .withDepthWrite(false)
      .withCull(false)
      .withoutBlend()
      .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
      .withUniform(PreviewFade.FADE_UNIFORM_LAYOUT_NAME, UniformType.UNIFORM_BUFFER)
      .withSampler(PreviewFade.INPUT_SAMPLER_LAYOUT_NAME)
      .build();
   private static TextureTarget fadeOutputTarget;
   private static final float NO_PREVIEW_ALPHA = -1.0F;
   private static final float MIN_PREVIEW_SCALE = 0.001F;
   private static final float PREVIEW_CENTER_X_FACTOR = 0.5F;
   private static final float PREVIEW_CENTER_Y_FACTOR = 0.5F;
   private static final float MIN_PREVIEW_MODEL_HEIGHT = 0.001F;
   private static final String INPUT_SAMPLER_BINDING = "InSampler";
   private static final String FADE_UNIFORM_BINDING = "PreviewFade";
   private static final String FADE_TARGET_LABEL = "entity-esp1-gui-preview-fade";
   private static final String FADE_PASS_LABEL = "entityEsp1GuiPreviewFade";
   private static final String FADE_UNIFORM_BUFFER_LABEL = "entityEsp1GuiPreviewFadeUniforms";
   private static final String PIPELINE_LOCATION = "rose/entity_esp1_gui_preview_fade";
   private static final String VERTEX_SHADER = "hands_fullscreen";
   private static final String FRAGMENT_SHADER = "gui_preview_fade";
   private static final String FADE_UNIFORM_LAYOUT_NAME = "PreviewFade";
   private static final String INPUT_SAMPLER_LAYOUT_NAME = "InSampler";

   public static void registerPreview(EntityRenderState renderState, float alpha) {
      pendingPreviewStates.put(renderState, new PreviewFade.PreviewState(Math.clamp(alpha, 0.0F, 1.0F), null, null));
   }

   public static void registerPreview(EntityRenderState renderState, float alpha, EntityEspSurface renderer, EntityEspStyleSettings settings) {
      pendingPreviewStates.put(renderState, new PreviewFade.PreviewState(Math.clamp(alpha, 0.0F, 1.0F), renderer, settings));
   }

   private PreviewFade() {
   }

   public static GpuTextureView renderRegisteredPreview(GuiEntityRenderState guiState, GpuTexture texture, GpuTextureView inputView, GpuTextureView depthView, Matrix4f projectionMatrix, int guiScale) {
      PreviewFade.PreviewState previewState = pendingPreviewStates.remove(guiState.renderState());
      if (previewState != null && texture != null && inputView != null) {
         int width = texture.getWidth(0);
         int height = texture.getHeight(0);
         if (width > 0 && height > 0) {
            GpuTextureView outputView = inputView;
            if (previewState.renderer() != null && previewState.settings() != null) {
               float modelScale = Math.max(MIN_PREVIEW_SCALE, guiState.scale() * guiScale);
               Matrix4f modelViewMatrix = new Matrix4f(RenderSystem.getModelViewStack())
                  .translate(width * PREVIEW_CENTER_X_FACTOR, height * PREVIEW_CENTER_Y_FACTOR, 0.0F)
                  .scale(modelScale, modelScale, -modelScale)
                  .translate(guiState.translation())
                  .rotate(guiState.rotation());
               Matrix4f inverseModelViewProjection = new Matrix4f(projectionMatrix).mul(modelViewMatrix).invert();
               GpuTextureView renderedView = previewState.renderer().renderPreview(outputView, width, height, previewState.settings(), depthView, inverseModelViewProjection, Math.max(MIN_PREVIEW_MODEL_HEIGHT, guiState.renderState().boundingBoxHeight));
               if (renderedView != null) {
                  outputView = renderedView;
               }
            }

            return outputView;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private static void ensureFadeOutputTarget(int width, int height) {
      if (fadeOutputTarget == null) {
         fadeOutputTarget = new TextureTarget(FADE_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
      } else if (fadeOutputTarget.width != width || fadeOutputTarget.height != height) {
         fadeOutputTarget.resize(width, height);
      }
   }

   public static float getPreviewAlpha(EntityRenderState renderState) {
      PreviewFade.PreviewState previewState = pendingPreviewStates.get(renderState);
      return previewState == null ? NO_PREVIEW_ALPHA : previewState.alpha();
   }

   public static void discardPreview(EntityRenderState renderState) {
      pendingPreviewStates.remove(renderState);
   }

   private static GpuTextureView drawFadePass(GpuTextureView inputView, int width, int height, float alpha) {
      ensureFadeOutputTarget(width, height);
      GpuBuffer uniformBuffer = null;

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 16).putVec4(alpha, 0.0F, 0.0F, 0.0F).get();
         uniformBuffer = RenderSystem.getDevice().createBuffer(() -> FADE_UNIFORM_BUFFER_LABEL, 128, uniformData);
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

         try (RenderPass renderPass = encoder.createRenderPass(() -> FADE_PASS_LABEL, fadeOutputTarget.getColorTextureView(), Optional.of(new Vector4f(0.0F)))) {
            renderPass.setPipeline(fadePipeline);
            renderPass.bindTexture(INPUT_SAMPLER_BINDING, inputView, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.setUniform(FADE_UNIFORM_BINDING, uniformBuffer);
            renderPass.draw(3, 1, 0, 0);
         }

         return fadeOutputTarget.getColorTextureView();
      } finally {
         if (uniformBuffer != null) {
            uniformBuffer.close();
         }
      }
   }

   public static GpuTextureView fadeTexture(GpuTexture texture, GpuTextureView inputView, float alpha) {
      if (texture != null && inputView != null) {
         int width = texture.getWidth(0);
         int height = texture.getHeight(0);
         return width > 0 && height > 0 ? drawFadePass(inputView, width, height, alpha) : null;
      } else {
         return null;
      }
   }

   private record PreviewState(float alpha, EntityEspSurface renderer, EntityEspStyleSettings settings) {
   }
}
