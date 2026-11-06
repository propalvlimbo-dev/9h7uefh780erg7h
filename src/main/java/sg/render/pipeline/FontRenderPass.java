package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
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
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;
import sg.ec.FontRenderer;
import sg.util.time.ColorMath;

public class FontRenderPass implements UiRenderPassSupport, MinecraftAccess {
   private final @com.google.gson.annotations.SerializedName("сI")  RenderPipeline fontPipeline;
   private final @com.google.gson.annotations.SerializedName("сТ")  RenderPipeline loadingLogoPipeline;
   @SerializedName("4")
   private final CommandEncoder encoder;
   private static final String FONT_PIPELINE_LOCATION = "rose/font";
   private static final String FONT_VERTEX_SHADER = "font";
   private static final String FONT_FRAGMENT_SHADER = "font";
   private static final String FONT_PIPELINE_PARAMS_UNIFORM = "Params";
   private static final String FONT_PIPELINE_SCISSORS_UNIFORM = "Scissors";
   private static final String FONT_PIPELINE_SAMPLER = "Sampler0";
   private static final String LOGO_PIPELINE_LOCATION = "rose/loading_logo";
   private static final String LOGO_VERTEX_SHADER = "loading_logo";
   private static final String LOGO_FRAGMENT_SHADER = "loading_logo";
   private static final String LOGO_PIPELINE_PARAMS_UNIFORM = "LogoParams";
   private static final String LOGO_PIPELINE_SAMPLER = "Sampler0";
   private static final String LOGO_DRAW_SAMPLER = "Sampler0";
   private static final String LOGO_DRAW_PARAMS_UNIFORM = "LogoParams";
   private static final float DEFAULT_TEXT_SMOOTHNESS = 0.5F;
   private static final float DEFAULT_COMPONENT_SMOOTHNESS = 0.5F;
   private static final float COMPONENT_PRECHECK_HEIGHT_FACTOR = 1.25F;
   private static final float COMPONENT_POSTCHECK_HEIGHT_FACTOR = 1.25F;
   private static final float COMPONENT_CLIP_HEIGHT_FACTOR = 1.25F;
   private static final String COMPONENT_DRAW_SAMPLER = "Sampler0";
   private static final String COMPONENT_DRAW_PARAMS_UNIFORM = "Params";
   private static final String COMPONENT_DRAW_SCISSORS_UNIFORM = "Scissors";
   private static final int RGB_MASK = 16777215;
   private static final float SHINY_TEXT_CHECK_HEIGHT_FACTOR = 1.25F;
   private static final float SHINY_TEXT_SMOOTHNESS = 0.5F;
   private static final float SHINY_TEXT_CLIP_WIDTH_PADDING_FACTOR = 0.15F;
   private static final float SHINY_TEXT_CLIP_HEIGHT_FACTOR = 1.25F;
   private static final float SHINY_TEXT_START_NDC_X_OFFSET = -1.0F;
   private static final float SHINY_TEXT_END_NDC_X_OFFSET = -1.0F;
   private static final float SHINE_DIRECTION_X = 0.8660254F;
   private static final float SHINE_DIRECTION_Y = -0.5F;
   private static final String SHINY_TEXT_DRAW_SAMPLER = "Sampler0";
   private static final String SHINY_TEXT_DRAW_PARAMS_UNIFORM = "Params";
   private static final String SHINY_TEXT_DRAW_SCISSORS_UNIFORM = "Scissors";
   private static final String SHINY_TEXT_DRAW_SHINE_UNIFORM = "Shine";
   private static final float TEXT_CHECK_HEIGHT_FACTOR = 1.25F;
   private static final float TEXT_THICKNESS_SPACING_FACTOR = 0.5F;
   private static final String TEXT_DRAW_SAMPLER = "Sampler0";
   private static final float TEXT_CLIP_WIDTH_PADDING_FACTOR = 0.15F;
   private static final float TEXT_CLIP_HEIGHT_FACTOR = 1.25F;
   private static final String TEXT_DRAW_PARAMS_UNIFORM = "Params";
   private static final String TEXT_DRAW_SCISSORS_UNIFORM = "Scissors";
   private static final float CLIP_NDC_X_OFFSET = -1.0F;
   private static final String TEXT_PASS_LABEL = "fontPass";
   private static final String SHINY_TEXT_PASS_LABEL = "shinyText";
   private static final String COMPONENT_PASS_LABEL = "font_text_pass";
   private static final String LOGO_PASS_LABEL = "loading_logo_pass";

   @Override
   public GpuBufferSlice uploadClipUniform(Vector4f requestedClip, MemoryStack stack) {
      Vector4f globalClip = UiDrawing.getScissor();
      float requestedLeft = requestedClip.x;
      float requestedTop = requestedClip.y;
      float requestedRight = requestedClip.x + requestedClip.z;
      float requestedBottom = requestedClip.y + requestedClip.w;
      float globalLeft = globalClip.x;
      float globalTop = globalClip.y;
      float globalRight = globalClip.x + globalClip.z;
      float globalBottom = globalClip.y + globalClip.w;
      float intersectionLeft = Math.max(requestedLeft, globalLeft);
      float intersectionTop = Math.max(requestedTop, globalTop);
      float intersectionRight = Math.min(requestedRight, globalRight);
      float intersectionBottom = Math.min(requestedBottom, globalBottom);
      float clipLeft = intersectionLeft;
      float clipTop = intersectionTop;
      float clipWidth = intersectionRight - intersectionLeft;
      float clipHeight = intersectionBottom - intersectionTop;
      float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * clipWidth;
      float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * clipHeight;
      float ndcLeft = CLIP_NDC_X_OFFSET + clipLeft * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
      float ndcBottom = 1.0F - clipTop * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
      Vector3f transformedBottomLeft = UiDrawing.getTransform().transformPosition(ndcLeft, ndcBottom, 0.0F, new Vector3f());
      Vector3f transformedTopRight = UiDrawing.getTransform().transformPosition(ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F, new Vector3f());
      Vector4f transformedClipBounds = new Vector4f(transformedBottomLeft.x, transformedBottomLeft.y, transformedTopRight.x, transformedTopRight.y);
      ByteBuffer uniformData = Std140Builder.onStack(stack, 32)
         .putVec4(new Vector4f(transformedClipBounds))
         .putVec2(new Vector2f(MINECRAFT_CLIENT.getWindow().getScreenWidth(), MINECRAFT_CLIENT.getWindow().getScreenHeight()))
         .putFloat(UiDrawing.isScissorEnabled() ? 1.0F : 0.0F)
         .get();
      return TransientGpuUpload.uploadUniform(uniformData);
   }

   public void drawLoadingLogo(FontRenderer fontRenderer, int glyphCode, float x, float y, float fontSize, int primaryArgb, int secondaryArgb, float traceProgress, float fillProgress, float sweepProgress) {
      Vector4f atlasUvBounds = fontRenderer.getFontFace().getGlyphUvBounds(glyphCode);
      if (atlasUvBounds != null && ColorMath.toRgbaVector(primaryArgb).w != 0.0F) {
         BufferBuilder vertexBuilder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         fontRenderer.getFontFace().appendTextQuads(vertexBuilder, Character.toString(glyphCode), fontSize, 0.0F, 0.0F, x, y + fontRenderer.getFontFace().getMetrics().getBaselineOffset() * fontSize, primaryArgb);
         GpuBufferSlice logoUniforms = null;
         RenderPass renderPass = null;

         try {
            try (MemoryStack stack = MemoryStack.stackPush()) {
               Vector4f secondaryColor = ColorMath.toRgbaVector(secondaryArgb);
               ByteBuffer uniformData = Std140Builder.onStack(stack, 48)
                  .putVec4(atlasUvBounds)
                  .putVec4(
                     new Vector4f(fontRenderer.getFontFace().getAtlas().getRange(), Mth.clamp(traceProgress, 0.0F, 1.0F), Mth.clamp(fillProgress, 0.0F, 1.0F), Mth.clamp(sweepProgress, 0.0F, 1.0F))
                  )
                  .putVec4(secondaryColor)
                  .get();
               logoUniforms = TransientGpuUpload.uploadUniform(uniformData);
            }

            renderPass = this.encoder.createRenderPass(() -> LOGO_PASS_LABEL, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.bindTexture(LOGO_DRAW_SAMPLER, fontRenderer.getFontFace().getTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.setUniform(LOGO_DRAW_PARAMS_UNIFORM, logoUniforms);
            this.drawAndClose(vertexBuilder, renderPass, this.loadingLogoPipeline);
         } finally {
            if (renderPass != null) {
               renderPass.close();
            }
         }
      }
   }

   private void discardBuilderMesh(BufferBuilder vertexBuilder) {
      MeshData mesh = vertexBuilder.build();
      if (mesh != null) {
         mesh.close();
      }
   }

   public void drawShinyText(FontRenderer fontRenderer, String text, float x, float y, float fontSize, int baseArgb, int shineArgb, float shineProgress) {
      if (!text.isEmpty() && !(fontSize <= 0.0F) && ColorMath.getAlpha(baseArgb) != 0 && !(UiDrawing.getGlobalAlpha() <= 0.0F)) {
         float textWidth = fontRenderer.getFontFace().measureTextWidth(text, fontSize, 0.0F, 1.0F);
         if (!(textWidth <= 0.0F) && !this.isOutsideClip(x, y, textWidth, fontSize * SHINY_TEXT_CHECK_HEIGHT_FACTOR)) {
            RenderPipeline pipeline = FontRenderPass.ShinyTextPipeline.PIPELINE;

            GpuBufferSlice fontUniforms;
            GpuBufferSlice clipUniforms;
            GpuBufferSlice shineUniforms;
            try (MemoryStack stack = MemoryStack.stackPush()) {
               fontUniforms = TransientGpuUpload.uploadUniform(Std140Builder.onStack(stack, 16).putFloat(fontRenderer.getFontFace().getAtlas().getRange()).putFloat(0.0F).putFloat(SHINY_TEXT_SMOOTHNESS).get());
               clipUniforms = this.uploadClipUniform(new Vector4f(x, y, textWidth + fontSize * SHINY_TEXT_CLIP_WIDTH_PADDING_FACTOR, fontSize * SHINY_TEXT_CLIP_HEIGHT_FACTOR), stack);
               float screenWidth = MINECRAFT_CLIENT.getWindow().getScreenWidth();
               float screenHeight = MINECRAFT_CLIENT.getWindow().getScreenHeight();
               Vector3f transformedTextStart = UiDrawing.getTransform().transformPosition(SHINY_TEXT_START_NDC_X_OFFSET + x * 2.0F / screenWidth, 1.0F - y * 2.0F / screenHeight, 0.0F, new Vector3f());
               Vector3f transformedTextEnd = UiDrawing.getTransform()
                  .transformPosition(SHINY_TEXT_END_NDC_X_OFFSET + (x + textWidth) * 2.0F / screenWidth, 1.0F - (y + fontSize) * 2.0F / screenHeight, 0.0F, new Vector3f());
               shineUniforms = TransientGpuUpload.uploadUniform(
                  Std140Builder.onStack(stack, 48)
                     .putVec4(new Vector4f(transformedTextStart.x, transformedTextStart.y, transformedTextEnd.x - transformedTextStart.x, transformedTextEnd.y - transformedTextStart.y))
                     .putVec4(new Vector4f(Mth.clamp(shineProgress, 0.0F, 1.0F), fontSize / textWidth, SHINE_DIRECTION_X, SHINE_DIRECTION_Y))
                     .putVec4(ColorMath.toRgbaVector(shineArgb))
                     .get()
               );
            }

            BufferBuilder vertexBuilder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            fontRenderer.getFontFace().appendTextQuads(vertexBuilder, text, fontSize, 0.0F, 0.0F, x, y + fontRenderer.getFontFace().getMetrics().getBaselineOffset() * fontSize, baseArgb);

            try (RenderPass renderPass = this.encoder.createRenderPass(() -> SHINY_TEXT_PASS_LABEL, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty())) {
               RenderSystem.bindDefaultUniforms(renderPass);
               renderPass.bindTexture(SHINY_TEXT_DRAW_SAMPLER, fontRenderer.getFontFace().getTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               renderPass.setUniform(SHINY_TEXT_DRAW_PARAMS_UNIFORM, fontUniforms);
               renderPass.setUniform(SHINY_TEXT_DRAW_SCISSORS_UNIFORM, clipUniforms);
               renderPass.setUniform(SHINY_TEXT_DRAW_SHINE_UNIFORM, shineUniforms);
               this.drawAndClose(vertexBuilder, renderPass, pipeline);
            }
         }
      }
   }

   public void drawComponent(FontRenderer fontRenderer, @NotNull Component component, float x, float y, float fontSize, int argb, float thickness, float smoothness, float widthAdvanceScale) {
      if (!this.isOutsideClip(x, y, fontRenderer.getFontFace().measureTextWidth(component.getString(), fontSize, 0.0F), fontSize * COMPONENT_PRECHECK_HEIGHT_FACTOR) || ColorMath.toRgbaVector(argb).w != 0.0F) {
         BufferBuilder vertexBuilder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         float baselineY = y + fontRenderer.getFontFace().getMetrics().getBaselineOffset() * fontSize;
         float[] cursorX = new float[]{x};
         int baseAlpha = argb >>> 24;
         component.visit((style, segmentText) -> {
            if (!segmentText.isEmpty()) {
               int segmentArgb = style.getColor() == null ? argb : this.withAlpha(style.getColor().getValue(), baseAlpha);
               cursorX[0] = this.appendLegacyFormattedText(fontRenderer, vertexBuilder, segmentText, cursorX[0], baselineY, fontSize, thickness, widthAdvanceScale, segmentArgb, baseAlpha);
            }

            return Optional.empty();
         }, Style.EMPTY);
         float drawnWidth = cursorX[0] - x;
         if (!(drawnWidth <= 0.0F) && !this.isOutsideClip(x, y, drawnWidth, fontSize * COMPONENT_POSTCHECK_HEIGHT_FACTOR)) {
            GpuBufferSlice fontUniforms = null;
            GpuBufferSlice clipUniforms = null;
            RenderPass renderPass = null;

            try {
               try (MemoryStack stack = MemoryStack.stackPush()) {
                  ByteBuffer uniformData = Std140Builder.onStack(stack, 16).putFloat(fontRenderer.getFontFace().getAtlas().getRange()).putFloat(thickness).putFloat(smoothness).get();
                  fontUniforms = TransientGpuUpload.uploadUniform(uniformData);
                  clipUniforms = this.uploadClipUniform(new Vector4f(x, y, drawnWidth, fontSize * COMPONENT_CLIP_HEIGHT_FACTOR), stack);
               }

               renderPass = this.encoder.createRenderPass(() -> COMPONENT_PASS_LABEL, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
               RenderSystem.bindDefaultUniforms(renderPass);
               renderPass.bindTexture(COMPONENT_DRAW_SAMPLER, fontRenderer.getFontFace().getTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               renderPass.setUniform(COMPONENT_DRAW_PARAMS_UNIFORM, fontUniforms);
               renderPass.setUniform(COMPONENT_DRAW_SCISSORS_UNIFORM, clipUniforms);
               this.drawAndClose(vertexBuilder, renderPass, this.fontPipeline);
            } finally {
               if (renderPass != null) {
                  renderPass.close();
               }
            }
         } else {
            this.discardBuilderMesh(vertexBuilder);
         }
      }
   }

   public void precompilePipelines(ShaderSource shaderSource) {
      RenderSystem.getDevice().precompilePipeline(this.fontPipeline, shaderSource);
      RenderSystem.getDevice().precompilePipeline(this.loadingLogoPipeline, shaderSource);
   }

   public void drawText(FontRenderer fontRenderer, @NotNull String text, float x, float y, float fontSize, int argb, float thickness, float smoothness, float widthAdvanceScale) {
      Vector4f atlasBounds = fontRenderer.getFontFace().usesAtlasMetrics()
         ? fontRenderer.getFontFace().getTextQuadBounds(text, x, y, fontSize) : null;
      boolean outsideClip = atlasBounds == null
         ? this.isOutsideClip(x, y, fontRenderer.getFontFace().measureTextWidth(text, fontSize, 0.0F, widthAdvanceScale), fontSize * TEXT_CHECK_HEIGHT_FACTOR)
         : this.isOutsideClip(atlasBounds.x, atlasBounds.y, atlasBounds.z, atlasBounds.w);
      if (!text.isEmpty() && !outsideClip && ColorMath.toRgbaVector(argb).w != 0.0F) {
         BufferBuilder vertexBuilder = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         fontRenderer.getFontFace().appendTextQuads(vertexBuilder, text, fontSize, thickness * TEXT_THICKNESS_SPACING_FACTOR * fontSize, 0.0F, x, y + fontRenderer.getFontFace().getMetrics().getBaselineOffset() * fontSize, argb);
         RenderPass renderPass = this.encoder.createRenderPass(() -> TEXT_PASS_LABEL, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
         RenderSystem.bindDefaultUniforms(renderPass);
         renderPass.bindTexture(TEXT_DRAW_SAMPLER, fontRenderer.getFontFace().getTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));

         try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer uniformData = Std140Builder.onStack(stack, 16).putFloat(fontRenderer.getFontFace().getAtlas().getRange()).putFloat(thickness).putFloat(smoothness).get();
            GpuBufferSlice fontUniforms = TransientGpuUpload.uploadUniform(uniformData);
            float textWidth = fontRenderer.getFontFace().measureTextWidth(text, fontSize, 0.0F, widthAdvanceScale);
            float clipWidthPadding = fontSize * TEXT_CLIP_WIDTH_PADDING_FACTOR;
            GpuBufferSlice clipUniforms = this.uploadClipUniform(atlasBounds == null
               ? new Vector4f(x, y, textWidth + clipWidthPadding, fontSize * TEXT_CLIP_HEIGHT_FACTOR) : atlasBounds, stack);
            renderPass.setUniform(TEXT_DRAW_PARAMS_UNIFORM, fontUniforms);
            renderPass.setUniform(TEXT_DRAW_SCISSORS_UNIFORM, clipUniforms);
         }

         this.drawAndClose(vertexBuilder, renderPass, this.fontPipeline);
         renderPass.close();
      }
   }

   private float appendTextSegment(FontRenderer fontRenderer, BufferBuilder vertexBuilder, String segmentText, float cursorX, float baselineY, float fontSize, float extraSpacing, float widthAdvanceScale, int argb) {
      if (segmentText.isEmpty()) {
         return cursorX;
      }

      fontRenderer.getFontFace().appendTextQuads(vertexBuilder, segmentText, fontSize, extraSpacing, 0.0F, cursorX, baselineY, argb);
      return cursorX + fontRenderer.getFontFace().measureTextWidth(segmentText, fontSize, 0.0F, widthAdvanceScale);
   }

   public FontRenderPass() {
      this.fontPipeline = RenderPipelineBuilder.builder()
         .withLocation(FONT_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(FONT_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FONT_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withUniform(FONT_PIPELINE_PARAMS_UNIFORM, UniformType.UNIFORM_BUFFER)
         .withUniform(FONT_PIPELINE_SCISSORS_UNIFORM, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withSampler(FONT_PIPELINE_SAMPLER)
         .build();
      this.loadingLogoPipeline = RenderPipelineBuilder.builder()
         .withLocation(LOGO_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(LOGO_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(LOGO_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withUniform(LOGO_PIPELINE_PARAMS_UNIFORM, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withSampler(LOGO_PIPELINE_SAMPLER)
         .build();
      this.encoder = RenderSystem.getDevice().createCommandEncoder();
   }

   private float appendLegacyFormattedText(FontRenderer fontRenderer, BufferBuilder vertexBuilder, String text, float cursorX, float baselineY, float fontSize, float extraSpacing, float widthAdvanceScale, int baseArgb, int baseAlpha) {
      int segmentArgb = baseArgb;
      int segmentStart = 0;

      for (int characterIndex = 0; characterIndex < text.length() - 1; characterIndex++) {
         if (text.charAt(characterIndex) == 167) {
            if (segmentStart < characterIndex) {
               cursorX = this.appendTextSegment(fontRenderer, vertexBuilder, text.substring(segmentStart, characterIndex), cursorX, baselineY, fontSize, extraSpacing, widthAdvanceScale, segmentArgb);
            }

            ChatFormatting formatting = ChatFormatting.getByCode(Character.toLowerCase(text.charAt(characterIndex + 1)));
            if (formatting != null) {
               if (TextColor.fromLegacyFormat(formatting) != null) {
                  Integer legacyRgb = TextColor.fromLegacyFormat(formatting).getValue();
                  if (legacyRgb != null) {
                     segmentArgb = this.withAlpha(legacyRgb, baseAlpha);
                  }
               } else if (formatting == ChatFormatting.RESET) {
                  segmentArgb = baseArgb;
               }
            }

            segmentStart = ++characterIndex + 1;
         }
      }

      if (segmentStart < text.length()) {
         cursorX = this.appendTextSegment(fontRenderer, vertexBuilder, segmentStart == 0 ? text : text.substring(segmentStart), cursorX, baselineY, fontSize, extraSpacing, widthAdvanceScale, segmentArgb);
      }

      return cursorX;
   }

   public void drawText(FontRenderer fontRenderer, @NotNull String text, float x, float y, int fontSize, int argb) {
      this.drawText(fontRenderer, text, x, y, fontSize, argb, 0.0F, DEFAULT_TEXT_SMOOTHNESS, 1.0F);
   }

   public void drawComponent(FontRenderer fontRenderer, @NotNull Component component, float x, float y, int fontSize, int argb) {
      this.drawComponent(fontRenderer, component, x, y, fontSize, argb, 0.0F, DEFAULT_COMPONENT_SMOOTHNESS, 1.0F);
   }

   private int withAlpha(int rgb, int alpha) {
      return alpha << 24 | rgb & RGB_MASK;
   }

   private static final class ShinyTextPipeline {
      private static final RenderPipeline PIPELINE = RenderPipelineBuilder.builder()
         .withLocation(FontRenderPass.ShinyTextPipeline.PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(FontRenderPass.ShinyTextPipeline.VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FontRenderPass.ShinyTextPipeline.FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withUniform(FontRenderPass.ShinyTextPipeline.PARAMS_UNIFORM, UniformType.UNIFORM_BUFFER)
         .withUniform(FontRenderPass.ShinyTextPipeline.SCISSORS_UNIFORM, UniformType.UNIFORM_BUFFER)
         .withUniform(FontRenderPass.ShinyTextPipeline.SHINE_UNIFORM, UniformType.UNIFORM_BUFFER)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withSampler(FontRenderPass.ShinyTextPipeline.SAMPLER)
         .build();
      private static final String PIPELINE_LOCATION = "rose/shiny_text";
      private static final String VERTEX_SHADER = "font";
      private static final String FRAGMENT_SHADER = "shiny_text";
      private static final String PARAMS_UNIFORM = "Params";
      private static final String SCISSORS_UNIFORM = "Scissors";
      private static final String SHINE_UNIFORM = "Shine";
      private static final String SAMPLER = "Sampler0";
   }
}
