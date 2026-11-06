package sg.render.pipeline;

import com.google.common.base.Suppliers;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.ec.FontRenderer;
import sg.render.RoseBatchedItemAtlas;
import sg.render.avatar.AsyncTextureAtlas;
import sg.util.time.ColorMath;

public class UiBatchRenderer implements MinecraftAccess, UiRenderPassSupport {
   @SerializedName("ВЮ")
   private final Map<FontRenderer, Integer> fontSlots = new HashMap<>();
   @SerializedName("Я")
   private final VertexFormat vertexFormat;
   private final @com.google.gson.annotations.SerializedName("ВЛ")  RenderPipeline translucentPipeline;
   private final @com.google.gson.annotations.SerializedName("В诶")  RenderPipeline additivePipeline;
   private @com.google.gson.annotations.SerializedName("В1")  RenderPipeline activePipeline;
   private final @com.google.gson.annotations.SerializedName("В")  AsyncTextureAtlas textureAtlas;
   @SerializedName("Я")
   private final RoseBatchedItemAtlas itemAtlas;
   @SerializedName("4")
   private final Supplier<TextureTarget> sceneCopyTarget;
   private final @com.google.gson.annotations.SerializedName("Д")  RenderTarget mainTarget;
   @SerializedName("ВЮ")
   private final List<GpuTextureView> textureViews;
   private final @com.google.gson.annotations.SerializedName("ВЧ")  List<FilterMode> textureFilters;
   private @com.google.gson.annotations.SerializedName("ЪO")  int batchDepth;
   private @com.google.gson.annotations.SerializedName("Ау")  boolean usesLiveBlur;
   private @com.google.gson.annotations.SerializedName("А2")  boolean usesCachedBlur;
   private @com.google.gson.annotations.SerializedName("Аф")  boolean sceneCopyValid;
   private @com.google.gson.annotations.SerializedName("Ю")  GpuTextureView fallbackTexture;
   @SerializedName("Я")
   private final SharedGeometryBuffer geometryStorage;
   @SerializedName("Я")
   private BufferBuilder vertexBuilder;
   private static final String lВ = "Position";
   private static final String lЯ = "Color";
   private static final String lХ = "UV0";
   private static final String lе = "UV1";
   private static final String lА = "UV2";
   private static final String l4 = "rose/batch";
   private static final String lп = "rose/batch_lightning";
   private static final String lД = "rose";
   private static final String lЪ = "batching";
   private static final int INITIAL_GEOMETRY_CAPACITY = 2097152;
   private static final String lа = "batch";
   private static final String lЮ = "batch";
   private static final String lЧ = "Sampler0";
   private static final String lЛ = "Sampler1";
   private static final String l诶 = "Sampler2";
   private static final String l1 = "Sampler3";
   private static final String lЬ = "Sampler4";
   private static final String l衣 = "Sampler5";
   private static final String lш = "Sampler6";
   private static final String lИ = "Sampler10";
   private static final String lь = "Sampler11";
   private static final String lж = "Sampler12";
   private static final String l弟 = "Sampler13";
   private static final float lб = -1.0F;
   private static final float lЫ = 255.0F;
   private static final float ll = 255.0F;
   private static final float l8 = 255.0F;
   private static final float l5 = 255.0F;
   private static final float lв = 32767.0F;
   private static final float lъ = 32767.0F;
   private static final float lМ = -1.0F;
   private static final float lН = 255.0F;
   private static final float lн = 255.0F;
   private static final float lЦ = 255.0F;
   private static final float lч = 255.0F;
   private static final float lЭ = 32767.0F;
   private static final float l必 = 32767.0F;
   private static final float lщ = 0.5F;
   private static final float lГ = 0.5F;
   private static final float lл = -1.0F;
   private static final float lэ = 255.0F;
   private static final float lю = 32767.0F;
   private static final float lЩ = 32767.0F;
   private static final float lу = 30.0F;
   private static final float l2 = -1.0F;
   private static final float lф = 255.0F;
   private static final float lи = 32767.0F;
   private static final float l9 = 32767.0F;
   private static final float lо = 4.0F;
   private static final float lO = 20.0F;
   private static final float l7 = 4.0F;
   private static final float lР = 4.0F;
   private static final float l3 = 4.0F;
   private static final float lр = 4.0F;
   private static final String l6 = "Sampler10";
   private static final String l_ = "Sampler11";
   private static final String lШ = "Sampler12";
   private static final String lы = "Sampler13";
   private static final int lй = 16777215;
   private static final String lЖ = "batchPass";
   private static final String lм = "temp";

   public void beginBatch(FontRenderer... fonts) {
      if (this.batchDepth <= 0) {
         this.batchDepth = 1;
         this.sceneCopyValid = false;
         this.fontSlots.clear();
         this.itemAtlas._ш();
         int nextFontSlot = 0;

         for (FontRenderer font : fonts) {
            this.fontSlots.put(font, nextFontSlot++);
         }
      } else {
         this.batchDepth++;
         boolean addsNewFont = false;

         for (FontRenderer requestedFont : fonts) {
            if (!this.fontSlots.containsKey(requestedFont)) {
               addsNewFont = true;
               break;
            }
         }

         if (addsNewFont && (!this.textureViews.isEmpty() || this.fallbackTexture != null)) {
            this.flush(this.getBatchFonts());
         }

         for (FontRenderer newFont : fonts) {
            this.fontSlots.putIfAbsent(newFont, this.fontSlots.size());
         }
      }
   }

   public void endBatch(FontRenderer... fonts) {
      if (this.batchDepth > 0) {
         this.batchDepth--;
      }

      if (this.batchDepth <= 0) {
         this.flush(this.getBatchFonts());
         this.fontSlots.clear();
      }
   }

   private FontRenderer[] getBatchFonts() {
      return this.fontSlots.entrySet().stream().sorted(Entry.comparingByValue()).map(Entry::getKey).toArray(FontRenderer[]::new);
   }

   public void drawTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, GpuTextureView texture) {
      this.appendTexture(x, y, width, height, cornerRadii, new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), colors, texture, false, FilterMode.LINEAR);
   }

   private void appendItemRegion(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, RoseBatchedItemAtlas.UvRegion region) {
      float screenWidth = Minecraft.getInstance().getWindow().getScreenWidth();
      float screenHeight = Minecraft.getInstance().getWindow().getScreenHeight();
      float ndcWidth = 2.0F * width / screenWidth;
      float ndcHeight = 2.0F * height / screenHeight;
      float ndcLeft = lл + x * 2.0F / screenWidth;
      float ndcBottom = 1.0F - y * 2.0F / screenHeight - ndcHeight;
      Vector4f bottomLeftColor = ColorMath.toRgbaVector(colors.x);
      Vector4f bottomRightColor = ColorMath.toRgbaVector(colors.y);
      Vector4f topRightColor = ColorMath.toRgbaVector(colors.z);
      Vector4f topLeftColor = ColorMath.toRgbaVector(colors.w);
      float alphaMultiplier = UiDrawing.getGlobalAlpha() / lэ;
      bottomLeftColor.w *= alphaMultiplier;
      bottomRightColor.w *= alphaMultiplier;
      topRightColor.w *= alphaMultiplier;
      topLeftColor.w *= alphaMultiplier;
      int packedWidth = Math.round(Math.clamp(width, 0.0F, lю));
      int packedHeight = Math.round(Math.clamp(height, 0.0F, lЩ));
      int packedRadiiXy = this.packRadiusPair(cornerRadii.x, cornerRadii.y);
      int packedRadiiZw = this.packRadiusPair(cornerRadii.z, cornerRadii.w);
      float itemUvOffset = lу;
      float u0 = region.u0();
      float u1 = region.u1();
      float vTop = region.vTop();
      float vBottom = region.vBottom();
      this.vertexBuilder
         .addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom, 0.0F)
         .setUv(itemUvOffset + u0, itemUvOffset + vBottom)
         .setColor(bottomLeftColor.x, bottomLeftColor.y, bottomLeftColor.z, bottomLeftColor.w)
         .setUv1(packedRadiiXy, packedRadiiZw)
         .setUv2(packedHeight, packedWidth);
      this.vertexBuilder
         .addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom, 0.0F)
         .setUv(itemUvOffset + u1, itemUvOffset + vBottom)
         .setColor(bottomRightColor.x, bottomRightColor.y, bottomRightColor.z, bottomRightColor.w)
         .setUv1(packedRadiiXy, packedRadiiZw)
         .setUv2(packedHeight, packedWidth);
      this.vertexBuilder
         .addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F)
         .setUv(itemUvOffset + u1, itemUvOffset + vTop)
         .setColor(topRightColor.x, topRightColor.y, topRightColor.z, topRightColor.w)
         .setUv1(packedRadiiXy, packedRadiiZw)
         .setUv2(packedHeight, packedWidth);
      this.vertexBuilder
         .addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom + ndcHeight, 0.0F)
         .setUv(itemUvOffset + u0, itemUvOffset + vTop)
         .setColor(topLeftColor.x, topLeftColor.y, topLeftColor.z, topLeftColor.w)
         .setUv1(packedRadiiXy, packedRadiiZw)
         .setUv2(packedHeight, packedWidth);
   }

   public void drawTextureFlipped(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, GpuTextureView texture) {
      this.appendTexture(x, y, width, height, cornerRadii, uvRect, colors, texture, true, FilterMode.LINEAR);
   }

   public void drawTextureNearestFlipped(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, GpuTextureView texture) {
      this.appendTexture(x, y, width, height, cornerRadii, uvRect, colors, texture, true, FilterMode.NEAREST);
   }

   public void setAdditiveBlend(boolean additive) {
      RenderPipeline pipeline = additive ? this.additivePipeline : this.translucentPipeline;
      if (this.activePipeline != pipeline) {
         if (this.vertexBuilder.vertices > 0) {
            this.flush(this.getBatchFonts());
         }

         this.activePipeline = pipeline;
      }
   }

   public void drawAtlasItem(
      float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, ItemStack item, RoseBatchedItemAtlas.ItemHandle handle
   ) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F) && (colors.x | colors.y | colors.z | colors.w) >>> 24 != 0) {
         RoseBatchedItemAtlas.UvRegion region = this.itemAtlas.Ъ(item, handle);
         if (region != null) {
            this.appendItemRegion(x, y, width, height, cornerRadii, colors, region);
         }
      }
   }

   public void drawItem(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, ItemStack item) {
      this.drawItem(x, y, width, height, cornerRadii, colors, item, null);
   }

   public void drawText(FontRenderer font, String text, float x, float y, int fontSize, int color) {
      Integer fontSlot = this.fontSlots.get(font);
      if (fontSlot != null) {
         font.getFontFace()
            .appendBatchText(this.vertexBuilder, text, fontSize, font.getFontFace().getAtlas().getRange(), fontSlot, 0.0F, x, y + font.getFontFace().getMetrics().getBaselineOffset() * fontSize, color);
      }
   }

   public void drawItem(
      float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, ItemStack item, RoseBatchedItemAtlas.ItemHandle handle
   ) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F) && (colors.x | colors.y | colors.z | colors.w) >>> 24 != 0) {
         RoseBatchedItemAtlas.UvRegion region = handle == null ? this.itemAtlas.Ъ(item) : this.itemAtlas.Ъ(item, handle);
         if (region != null) {
            GpuTextureView atlasTexture = this.itemAtlas.Ъ_dd184401();
            float halfTexelU = lщ / atlasTexture.getWidth(0);
            float halfTexelV = lГ / atlasTexture.getHeight(0);
            Vector4f uvRect = new Vector4f(
               region.u0() + halfTexelU, region.vTop() - halfTexelV, region.u1() - region.u0() - halfTexelU * 2.0F, region.vBottom() - region.vTop() + halfTexelV * 2.0F
            );
            this.appendTexture(x, y, width, height, cornerRadii, uvRect, colors, atlasTexture, true, FilterMode.LINEAR);
         }
      }
   }

   private BufferBuilder createVertexBuilder() {
      return this.geometryStorage.begin(PrimitiveTopology.QUADS, this.vertexFormat);
   }

   public void appendShape(float x, float y, float width, float height, float effectAmount, int instruction, Vector4f cornerRadii, Vector4i colors) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F) && (colors.x | colors.y | colors.z | colors.w) >>> 24 != 0) {
         this.usesLiveBlur |= instruction == 9;
         this.usesCachedBlur |= instruction == 11;
         float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * width;
         float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * height;
         float ndcLeft = lб + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
         float ndcBottom = 1.0F - y * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
         Vector4f bottomLeftColor = ColorMath.toRgbaVector(colors.x);
         Vector4f bottomRightColor = ColorMath.toRgbaVector(colors.y);
         Vector4f topRightColor = ColorMath.toRgbaVector(colors.z);
         Vector4f topLeftColor = ColorMath.toRgbaVector(colors.w);
         bottomLeftColor.w = bottomLeftColor.w * (UiDrawing.getGlobalAlpha() / lЫ);
         bottomRightColor.w = bottomRightColor.w * (UiDrawing.getGlobalAlpha() / ll);
         topRightColor.w = topRightColor.w * (UiDrawing.getGlobalAlpha() / l8);
         topLeftColor.w = topLeftColor.w * (UiDrawing.getGlobalAlpha() / l5);
         int packedWidth = Math.round(Math.clamp(width, 0.0F, lв));
         int packedHeight = Math.round(Math.clamp(height, 0.0F, lъ));
         int packedRadiiXy = this.packRadiusPair(cornerRadii.x, cornerRadii.y);
         int packedRadiiZw = this.packRadiusPair(cornerRadii.z, cornerRadii.w);
         this.vertexBuilder
            .addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom, 0.0F)
            .setUv(effectAmount, instruction)
            .setColor(bottomLeftColor.x, bottomLeftColor.y, bottomLeftColor.z, bottomLeftColor.w)
            .setUv1(packedRadiiXy, packedRadiiZw)
            .setUv2(packedHeight, packedWidth);
         this.vertexBuilder
            .addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom, 0.0F)
            .setUv(effectAmount, instruction)
            .setColor(bottomRightColor.x, bottomRightColor.y, bottomRightColor.z, bottomRightColor.w)
            .setUv1(packedRadiiXy, packedRadiiZw)
            .setUv2(packedHeight, packedWidth);
         this.vertexBuilder
            .addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F)
            .setUv(effectAmount, instruction)
            .setColor(topRightColor.x, topRightColor.y, topRightColor.z, topRightColor.w)
            .setUv1(packedRadiiXy, packedRadiiZw)
            .setUv2(packedHeight, packedWidth);
         this.vertexBuilder
            .addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom + ndcHeight, 0.0F)
            .setUv(effectAmount, instruction)
            .setColor(topLeftColor.x, topLeftColor.y, topLeftColor.z, topLeftColor.w)
            .setUv1(packedRadiiXy, packedRadiiZw)
            .setUv2(packedHeight, packedWidth);
      }
   }

   private float appendTextRun(
      FontRenderer font, BufferBuilder buffer, String text, float x, float baselineY, float fontSize, int fontSlot, int color, float fadeStart, float fadeEnd
   ) {
      if (text.isEmpty()) {
         return x;
      }

      if (Float.isFinite(fadeStart) && Float.isFinite(fadeEnd)) {
         font.getFontFace().appendFadedBatchText(buffer, text, fontSize, font.getFontFace().getAtlas().getRange(), fontSlot, 0.0F, x, baselineY, color, fadeStart, fadeEnd);
      } else {
         font.getFontFace().appendBatchText(buffer, text, fontSize, font.getFontFace().getAtlas().getRange(), fontSlot, 0.0F, x, baselineY, color);
      }

      return x + font.getFontFace().measureTextWidth(text, fontSize, 0.0F, 1.0F);
   }

   private int withAlpha(int rgb, int alpha) {
      return alpha << 24 | rgb & lй;
   }

   public void appendShape(float x, float y, float width, float height, float effectAmount, int instruction, Vector4f cornerRadii, int color) {
      this.appendShape(x, y, width, height, effectAmount, instruction, cornerRadii, new Vector4i(color));
   }

   private RenderPipeline createPipeline(String location, BlendFunction blendFunction) {
      return RenderPipelineBuilder.builder()
         .withLocation(location)
         .withVertexShader(ClientResourceIds.getCoreId(lа))
         .withFragmentShader(ClientResourceIds.getCoreId(lЮ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withVertexFormat(this.vertexFormat, PrimitiveTopology.QUADS)
         .withBlend(blendFunction)
         .withSampler(lЧ)
         .withSampler(lЛ)
         .withSampler(l诶)
         .withSampler(l1)
         .withSampler(lЬ)
         .withSampler(l衣)
         .withSampler(lш)
         .withSampler(lИ)
         .withSampler(lь)
         .withSampler(lж)
         .withSampler(l弟)
         .withCull(false)
         .build();
   }

   private float appendFormattedText(
      FontRenderer font, BufferBuilder buffer, String text, float x, float baselineY, float fontSize, int fontSlot, int baseColor, int alpha, float fadeStart, float fadeEnd
   ) {
      int currentColor = baseColor;
      int runStart = 0;

      for (int characterIndex = 0; characterIndex < text.length() - 1; characterIndex++) {
         if (text.charAt(characterIndex) == 167) {
            if (runStart < characterIndex) {
               x = this.appendTextRun(font, buffer, text.substring(runStart, characterIndex), x, baselineY, fontSize, fontSlot, currentColor, fadeStart, fadeEnd);
            }

            ChatFormatting formatting = ChatFormatting.getByCode(Character.toLowerCase(text.charAt(characterIndex + 1)));
            if (formatting != null) {
               if (TextColor.fromLegacyFormat(formatting) != null) {
                  Integer rgb = TextColor.fromLegacyFormat(formatting).getValue();
                  if (rgb != null) {
                     currentColor = this.withAlpha(rgb, alpha);
                  }
               } else if (formatting == ChatFormatting.RESET) {
                  currentColor = baseColor;
               }
            }

            runStart = ++characterIndex + 1;
         }
      }

      if (runStart < text.length()) {
         x = this.appendTextRun(font, buffer, runStart == 0 ? text : text.substring(runStart), x, baselineY, fontSize, fontSlot, currentColor, fadeStart, fadeEnd);
      }

      return x;
   }

   public void flushAndReset() {
      try {
         if (this.vertexBuilder.vertices > 0) {
            this.flush(this.getBatchFonts());
         }
      } finally {
         this.batchDepth = 0;
         this.usesLiveBlur = false;
         this.usesCachedBlur = false;
         this.sceneCopyValid = false;
         this.fontSlots.clear();
         this.fallbackTexture = null;
         this.textureViews.clear();
         this.textureFilters.clear();
         this.activePipeline = this.translucentPipeline;
         if (this.vertexBuilder.vertices > 0) {
            this.discardGeometry();
         }
      }
   }

   public void flushPendingGeometry() {
      this.flush(this.getBatchFonts());
   }

   private void discardGeometry() {
      MeshData mesh = this.vertexBuilder.build();
      if (mesh != null) {
         mesh.close();
      }

      this.vertexBuilder = this.createVertexBuilder();
   }

   public void drawAtlasTexture(float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, Identifier textureId) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F) && (colors.x | colors.y | colors.z | colors.w) >>> 24 != 0) {
         AsyncTextureAtlas.AtlasRegion region = this.textureAtlas.Ы(textureId);
         if (region != null) {
            float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * width;
            float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * height;
            float ndcLeft = lМ + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
            float ndcBottom = 1.0F - y * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
            Vector4f bottomLeftColor = ColorMath.toRgbaVector(colors.x);
            Vector4f bottomRightColor = ColorMath.toRgbaVector(colors.y);
            Vector4f topRightColor = ColorMath.toRgbaVector(colors.z);
            Vector4f topLeftColor = ColorMath.toRgbaVector(colors.w);
            bottomLeftColor.w = bottomLeftColor.w * (UiDrawing.getGlobalAlpha() / lН);
            bottomRightColor.w = bottomRightColor.w * (UiDrawing.getGlobalAlpha() / lн);
            topRightColor.w = topRightColor.w * (UiDrawing.getGlobalAlpha() / lЦ);
            topLeftColor.w = topLeftColor.w * (UiDrawing.getGlobalAlpha() / lч);
            int packedWidth = Math.round(Math.clamp(width, 0.0F, lЭ));
            int packedHeight = Math.round(Math.clamp(height, 0.0F, l必));
            int packedRadiiXy = this.packRadiusPair(cornerRadii.x, cornerRadii.y);
            int packedRadiiZw = this.packRadiusPair(cornerRadii.z, cornerRadii.w);
            float regionWidth = region.u1() - region.u0();
            float regionHeight = region.v1() - region.v0();
            float u0 = region.u0() + uvRect.x * regionWidth;
            float v0 = region.v0() + uvRect.y * regionHeight;
            float u1 = u0 + uvRect.z * regionWidth;
            float v1 = v0 + uvRect.w * regionHeight;
            this.vertexBuilder
               .addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom, 0.0F)
               .setUv(2.0F + u0, 2.0F + v1)
               .setColor(bottomLeftColor.x, bottomLeftColor.y, bottomLeftColor.z, bottomLeftColor.w)
               .setUv1(packedRadiiXy, packedRadiiZw)
               .setUv2(packedHeight, packedWidth);
            this.vertexBuilder
               .addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom, 0.0F)
               .setUv(2.0F + u1, 2.0F + v1)
               .setColor(bottomRightColor.x, bottomRightColor.y, bottomRightColor.z, bottomRightColor.w)
               .setUv1(packedRadiiXy, packedRadiiZw)
               .setUv2(packedHeight, packedWidth);
            this.vertexBuilder
               .addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F)
               .setUv(2.0F + u1, 2.0F + v0)
               .setColor(topRightColor.x, topRightColor.y, topRightColor.z, topRightColor.w)
               .setUv1(packedRadiiXy, packedRadiiZw)
               .setUv2(packedHeight, packedWidth);
            this.vertexBuilder
               .addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom + ndcHeight, 0.0F)
               .setUv(2.0F + u0, 2.0F + v0)
               .setColor(topLeftColor.x, topLeftColor.y, topLeftColor.z, topLeftColor.w)
               .setUv1(packedRadiiXy, packedRadiiZw)
               .setUv2(packedHeight, packedWidth);
         }
      }
   }

   public void reset() {
      this.batchDepth = 0;
      this.usesLiveBlur = false;
      this.usesCachedBlur = false;
      this.sceneCopyValid = false;
      this.fontSlots.clear();
      this.fallbackTexture = null;
      this.textureViews.clear();
      this.textureFilters.clear();
      this.activePipeline = this.translucentPipeline;
      if (this.vertexBuilder.vertices > 0) {
         this.discardGeometry();
      }
   }

   public void flush(FontRenderer... fonts) {
      if (this.vertexBuilder.vertices > 0) {
         this.itemAtlas._ю();
         this.textureAtlas.О(4);
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
         GpuTextureView sceneTexture = this.textureAtlas.Ы_be9bcd64().getTextureView();
         if (this.usesLiveBlur || this.usesCachedBlur) {
            TextureTarget sceneTarget = this.sceneCopyTarget.get();
            if (sceneTarget.width != this.mainTarget.width || sceneTarget.height != this.mainTarget.height) {
               sceneTarget.resize(this.mainTarget.width, this.mainTarget.height);
               this.sceneCopyValid = false;
            }

            if (this.usesLiveBlur || !this.sceneCopyValid) {
               encoder.copyTextureToTexture(this.mainTarget.getColorTexture(), sceneTarget.getColorTexture(), 0, 0, 0, 0, 0, this.mainTarget.width, this.mainTarget.height);
               this.sceneCopyValid = true;
            }

            sceneTexture = sceneTarget.getColorTextureView();
         }

         RenderPass renderPass = encoder.createRenderPass(() -> lЖ, MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty());
         RenderSystem.bindDefaultUniforms(renderPass);
         int samplerSlot = 0;

         for (FontRenderer font : fonts) {
            renderPass.bindTexture("Sampler" + samplerSlot, font.getFontFace().getTexture().getTextureView(), font.getFontFace().getTexture().getSampler());
            samplerSlot++;
         }

         for (int textureIndex = 0; textureIndex < this.textureViews.size(); textureIndex++) {
            renderPass.bindTexture("Sampler" + samplerSlot, this.textureViews.get(textureIndex), RenderSystem.getSamplerCache().getClampToEdge(this.textureFilters.get(textureIndex)));
            samplerSlot++;
         }

         while (samplerSlot < 7) {
            renderPass.bindTexture("Sampler" + samplerSlot, this.textureAtlas.Ы_be9bcd64().getTextureView(), this.textureAtlas.Ы_be9bcd64().getSampler());
            samplerSlot++;
         }

         renderPass.bindTexture(l6, sceneTexture, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         renderPass.bindTexture(l_, this.textureAtlas.Ы_be9bcd64().getTextureView(), this.textureAtlas.Ы_be9bcd64().getSampler());
         renderPass.bindTexture(lШ, this.fallbackTexture == null ? this.textureAtlas.Ы_be9bcd64().getTextureView() : this.fallbackTexture, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         renderPass.bindTexture(lы, this.itemAtlas.Ъ_dd184401(), RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST));

         try {
            this.drawAndClose(this.vertexBuilder, renderPass, this.activePipeline);
         } finally {
            this.vertexBuilder = this.createVertexBuilder();
            this.usesLiveBlur = false;
            this.usesCachedBlur = false;
            this.fallbackTexture = null;
            this.textureViews.clear();
            this.textureFilters.clear();
         }
      }
   }

   public void drawFadingText(FontRenderer font, Component text, float x, float y, int fontSize, int color, float width, float fadeWidth) {
      Integer fontSlot = this.fontSlots.get(font);
      if (fontSlot != null) {
         float baselineY = y + font.getFontFace().getMetrics().getBaselineOffset() * fontSize;
         float[] cursorX = new float[]{x};
         int alpha = color >>> 24;
         float fadeEnd = x + width;
         float fadeStart = Math.max(x, fadeEnd - fadeWidth);
         text.visit((style, textRun) -> {
            if (!textRun.isEmpty()) {
               int runColor = style.getColor() == null ? color : this.withAlpha(style.getColor().getValue(), alpha);
               cursorX[0] = this.appendFormattedText(font, this.vertexBuilder, textRun, cursorX[0], baselineY, fontSize, fontSlot.intValue(), runColor, alpha, fadeStart, fadeEnd);
            }

            return Optional.empty();
         }, Style.EMPTY);
      }
   }

   public void appendShape(float x, float y, float width, float height, float effectAmount, int instruction, float cornerRadius, int color) {
      this.appendShape(x, y, width, height, effectAmount, instruction, new Vector4f(cornerRadius), color);
   }

   private void appendTexture(
      float x, float y, float width, float height, Vector4f cornerRadii, Vector4f uvRect, Vector4i colors, GpuTextureView texture, boolean flipV, FilterMode filterMode
   ) {
      if (texture != null && !(width <= 0.0F) && !(height <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F) && colors.x >>> 24 != 0) {
         int samplerIndex = -1;
         int availableTextureSlots = Math.max(0, 7 - this.fontSlots.size());
         if (availableTextureSlots > 0) {
            for (int textureIndex = 0; textureIndex < this.textureViews.size(); textureIndex++) {
               if (this.textureViews.get(textureIndex) == texture) {
                  samplerIndex = this.fontSlots.size() + textureIndex;
                  break;
               }
            }

            if (samplerIndex < 0) {
               if (this.textureViews.size() >= availableTextureSlots) {
                  this.flush(this.getBatchFonts());
               }

               this.textureViews.add(texture);
               this.textureFilters.add(filterMode);
               samplerIndex = this.fontSlots.size() + this.textureViews.size() - 1;
            }
         } else {
            if (this.fallbackTexture != null && this.fallbackTexture != texture) {
               this.flush(this.getBatchFonts());
            }

            this.fallbackTexture = texture;
         }

         float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * width;
         float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * height;
         float ndcLeft = l2 + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
         float ndcBottom = 1.0F - y * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
         Vector4f color = ColorMath.toRgbaVector(colors.x);
         color.w = color.w * (UiDrawing.getGlobalAlpha() / lф);
         int packedWidth = Math.round(Math.clamp(width, 0.0F, lи));
         int packedHeight = Math.round(Math.clamp(height, 0.0F, l9));
         int packedRadiiXy = this.packRadiusPair(cornerRadii.x, cornerRadii.y);
         int packedRadiiZw = this.packRadiusPair(cornerRadii.z, cornerRadii.w);
         float textureUvOffset = samplerIndex < 0 ? lо : lO + samplerIndex * 2.0F;
         float u0 = uvRect.x;
         float v0 = uvRect.y;
         float u1 = u0 + uvRect.z;
         float v1 = v0 + uvRect.w;
         float bottomV = flipV ? v1 : v0;
         float topV = flipV ? v0 : v1;
         this.vertexBuilder
            .addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom, 0.0F)
            .setUv(textureUvOffset + u0, l7 + bottomV)
            .setColor(color.x, color.y, color.z, color.w)
            .setUv1(packedRadiiXy, packedRadiiZw)
            .setUv2(packedHeight, packedWidth);
         this.vertexBuilder
            .addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom, 0.0F)
            .setUv(textureUvOffset + u1, lР + bottomV)
            .setColor(color.x, color.y, color.z, color.w)
            .setUv1(packedRadiiXy, packedRadiiZw)
            .setUv2(packedHeight, packedWidth);
         this.vertexBuilder
            .addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F)
            .setUv(textureUvOffset + u1, l3 + topV)
            .setColor(color.x, color.y, color.z, color.w)
            .setUv1(packedRadiiXy, packedRadiiZw)
            .setUv2(packedHeight, packedWidth);
         this.vertexBuilder
            .addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom + ndcHeight, 0.0F)
            .setUv(textureUvOffset + u0, lр + topV)
            .setColor(color.x, color.y, color.z, color.w)
            .setUv1(packedRadiiXy, packedRadiiZw)
            .setUv2(packedHeight, packedWidth);
      }
   }

   private int packRadiusPair(float firstRadius, float secondRadius) {
      int firstByte = Math.clamp(Math.round(firstRadius), 0, 255);
      int secondByte = Math.clamp(Math.round(secondRadius), 0, 255);
      return firstByte & 0xFF | (secondByte & 0xFF) << 8;
   }

   public UiBatchRenderer() {
      this.vertexFormat = VertexFormat.builder(0)
         .addAttribute(lВ, GpuFormat.RGB32_FLOAT)
         .addAttribute(lЯ, GpuFormat.RGBA8_UNORM)
         .addAttribute(lХ, GpuFormat.RG32_FLOAT)
         .addAttribute(lе, GpuFormat.RG16_SINT)
         .addAttribute(lА, GpuFormat.RG16_SINT)
         .build();
      this.translucentPipeline = this.createPipeline(l4, BlendFunction.TRANSLUCENT);
      this.additivePipeline = this.createPipeline(lп, BlendFunction.LIGHTNING);
      this.activePipeline = this.translucentPipeline;
      this.textureAtlas = new AsyncTextureAtlas(Identifier.fromNamespaceAndPath(lД, lЪ), 4096);
      this.itemAtlas = new RoseBatchedItemAtlas();
      this.sceneCopyTarget = Suppliers.memoize(() -> new TextureTarget(lм, 1920, 1024, false, GpuFormat.RGBA8_UNORM));
      this.mainTarget = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
      this.textureViews = new ArrayList<>();
      this.textureFilters = new ArrayList<>();
      this.geometryStorage = new SharedGeometryBuffer(INITIAL_GEOMETRY_CAPACITY);
      this.vertexBuilder = this.geometryStorage.begin(PrimitiveTopology.QUADS, this.vertexFormat);
   }

   public void drawAtlasItem(float x, float y, float width, float height, Vector4f cornerRadii, Vector4i colors, ItemStack item) {
      if (!(width <= 0.0F) && !(height <= 0.0F) && !(UiDrawing.getGlobalAlpha() <= 0.0F) && (colors.x | colors.y | colors.z | colors.w) >>> 24 != 0) {
         RoseBatchedItemAtlas.UvRegion region = this.itemAtlas.Ъ(item);
         if (region != null) {
            this.appendItemRegion(x, y, width, height, cornerRadii, colors, region);
         }
      }
   }

   public void drawText(FontRenderer font, Component text, float x, float y, int fontSize, int color) {
      this.drawFadingText(font, text, x, y, fontSize, color, Float.NaN, Float.NaN);
   }
}
