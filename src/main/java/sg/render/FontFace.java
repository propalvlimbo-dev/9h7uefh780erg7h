package sg.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Generated;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import settings.render.ClientResourceLoader;
import settings.render.ClientResourceIds;
import settings.render.ReallyWorldRankGlyphs;
import sg.ec.MinecraftAccess;

public final class FontFace implements MinecraftAccess {
   private final @com.google.gson.annotations.SerializedName("сЯ")  String fontName;
   @SerializedName("В")
   private final AbstractTexture texture;
   @SerializedName("В")
   private final FontMetadata.AtlasData atlas;
   @SerializedName("В")
   private final FontMetadata.Metrics metrics;
   private final @com.google.gson.annotations.SerializedName("必")  Map<Integer, FontGlyph> glyphsByCodepoint;
   private final @com.google.gson.annotations.SerializedName("щ")  Map<Integer, Map<Integer, Float>> kerningByLeftCodepoint;
   private final boolean useAtlasMetrics;
   private static final float ЯтЦ = 255.0F;
   private static final float Ятч = 255.0F;
   private static final float ЯтЭ = 255.0F;
   private static final float MIN_GLYPH_Y_INITIALIZER = Float.MAX_VALUE;
   private static final float MAX_GLYPH_Y_INITIALIZER = -Float.MAX_VALUE;

   private FontFace(
      String fontName,
      AbstractTexture texture,
      FontMetadata.AtlasData atlas,
      FontMetadata.Metrics metrics,
      Map<Integer, FontGlyph> glyphsByCodepoint,
      Map<Integer, Map<Integer, Float>> kerningByLeftCodepoint,
      boolean useAtlasMetrics
   ) {
      this.fontName = fontName;
      this.texture = texture;
      this.atlas = atlas;
      this.metrics = metrics;
      this.glyphsByCodepoint = glyphsByCodepoint;
      this.kerningByLeftCodepoint = kerningByLeftCodepoint;
      this.useAtlasMetrics = useAtlasMetrics;
   }

   @Generated
   public AbstractTexture getTexture() {
      return this.texture;
   }

   private FontGlyph textGlyph(int codepoint) {
      FontGlyph glyph = this.glyphsByCodepoint.get(codepoint);
      return glyph == null && this.useAtlasMetrics ? this.glyphsByCodepoint.get(63) : glyph;
   }

   @Generated
   public String getFontName() {
      return this.fontName;
   }

   public void appendTextQuads(VertexConsumer vertexConsumer, String text, float fontSize, float extraGlyphAdvance, float letterSpacing, float x, float baselineY, int color) {
      int previousCodepoint = -1;

      for (int characterIndex = 0; characterIndex < text.length(); characterIndex++) {
         char character = text.charAt(characterIndex);
         String rankGlyphText = ReallyWorldRankGlyphs.method__(character);
         if (rankGlyphText != null) {
            float alpha = (color >> 24 & 0xFF) / ЯтЦ;

            for (int rankCharacterIndex = 0; rankCharacterIndex < rankGlyphText.length(); rankCharacterIndex++) {
               char rankCharacter = rankGlyphText.charAt(rankCharacterIndex);
               FontGlyph rankGlyph = this.glyphsByCodepoint.get(Integer.valueOf(rankCharacter));
               if (rankGlyph != null) {
                  Map<Integer, Float> rankKerning = this.kerningByLeftCodepoint.get(previousCodepoint);
                  if (rankKerning != null) {
                     x += rankKerning.getOrDefault(Integer.valueOf(rankCharacter), 0.0F) * fontSize;
                  }

                  int rankColor = ReallyWorldRankGlyphs.必(character, rankCharacterIndex, rankGlyphText.length(), alpha, color);
                  x += rankGlyph.appendQuad(vertexConsumer, fontSize, x, baselineY, rankColor) + extraGlyphAdvance + letterSpacing;
                  previousCodepoint = rankCharacter;
               }
            }
         } else {
            FontGlyph glyph = this.textGlyph(character);
            if (glyph != null) {
               Map<Integer, Float> kerning = this.kerningByLeftCodepoint.get(previousCodepoint);
               if (kerning != null) {
                  x += kerning.getOrDefault(Integer.valueOf(character), 0.0F) * fontSize;
               }

               x += glyph.appendQuad(vertexConsumer, fontSize, x, baselineY, color) + extraGlyphAdvance + letterSpacing;
               previousCodepoint = character;
            }
         }
      }
   }

   public void appendBatchText(VertexConsumer vertexConsumer, String text, float fontSize, float distanceRange, int fontSlot, float letterSpacing, float x, float baselineY, int color) {
      int previousCodepoint = -1;

      for (int characterIndex = 0; characterIndex < text.length(); characterIndex++) {
         char character = text.charAt(characterIndex);
         String rankGlyphText = ReallyWorldRankGlyphs.method__(character);
         if (rankGlyphText != null) {
            float alpha = (color >> 24 & 0xFF) / Ятч;

            for (int rankCharacterIndex = 0; rankCharacterIndex < rankGlyphText.length(); rankCharacterIndex++) {
               char rankCharacter = rankGlyphText.charAt(rankCharacterIndex);
               FontGlyph rankGlyph = this.glyphsByCodepoint.get(Integer.valueOf(rankCharacter));
               if (rankGlyph != null) {
                  Map<Integer, Float> rankKerning = this.kerningByLeftCodepoint.get(previousCodepoint);
                  if (rankKerning != null) {
                     x += rankKerning.getOrDefault(Integer.valueOf(rankCharacter), 0.0F) * fontSize;
                  }

                  int rankColor = ReallyWorldRankGlyphs.必(character, rankCharacterIndex, rankGlyphText.length(), alpha, color);
                  x += rankGlyph.appendBatchQuad(vertexConsumer, fontSize, distanceRange, fontSlot, x, baselineY, rankColor) + letterSpacing;
                  previousCodepoint = rankCharacter;
               }
            }
         } else {
            FontGlyph glyph = this.textGlyph(character);
            if (glyph != null) {
               Map<Integer, Float> kerning = this.kerningByLeftCodepoint.get(previousCodepoint);
               if (kerning != null) {
                  x += kerning.getOrDefault(Integer.valueOf(character), 0.0F) * fontSize;
               }

               x += glyph.appendBatchQuad(vertexConsumer, fontSize, distanceRange, fontSlot, x, baselineY, color) + letterSpacing;
               previousCodepoint = character;
            }
         }
      }
   }

   @Generated
   public FontMetadata.AtlasData getAtlas() {
      return this.atlas;
   }

   public float getGlyphWidth(int unicode, float fontSize) {
      FontGlyph glyph = this.glyphsByCodepoint.get(unicode);
      return glyph == null ? 0.0F : glyph.getScaledWidth(fontSize);
   }

   @Generated
   public FontMetadata.Metrics getMetrics() {
      return this.metrics;
   }

   public Vector4f getGlyphUvBounds(int unicode) {
      FontGlyph glyph = this.glyphsByCodepoint.get(unicode);
      return glyph == null ? null : glyph.getUvBounds();
   }

   public float measureTextWidth(String text, float fontSize, float letterSpacing, float advanceScale) {
      int previousCodepoint = -1;
      float width = 0.0F;

      for (int characterIndex = 0; characterIndex < text.length(); characterIndex++) {
         char character = text.charAt(characterIndex);
         if (character == 167 && characterIndex + 1 < text.length()) {
            characterIndex++;
         } else {
            String rankGlyphText = ReallyWorldRankGlyphs.method__(character);
            if (rankGlyphText != null) {
               for (int rankCharacterIndex = 0; rankCharacterIndex < rankGlyphText.length(); rankCharacterIndex++) {
                  char rankCharacter = rankGlyphText.charAt(rankCharacterIndex);
                  FontGlyph rankGlyph = this.glyphsByCodepoint.get(Integer.valueOf(rankCharacter));
                  if (rankGlyph != null) {
                     Map<Integer, Float> rankKerning = this.kerningByLeftCodepoint.get(previousCodepoint);
                     if (rankKerning != null) {
                        width += rankKerning.getOrDefault(Integer.valueOf(rankCharacter), 0.0F) * fontSize;
                     }

                     width += rankGlyph.getScaledAdvance(fontSize) * advanceScale + letterSpacing;
                     previousCodepoint = rankCharacter;
                  }
               }
            } else {
               FontGlyph glyph = this.textGlyph(character);
               if (glyph != null) {
                  Map<Integer, Float> kerning = this.kerningByLeftCodepoint.get(previousCodepoint);
                  if (kerning != null) {
                     width += kerning.getOrDefault(Integer.valueOf(character), 0.0F) * fontSize;
                  }

                  width += glyph.getScaledAdvance(fontSize) * advanceScale + letterSpacing;
                  previousCodepoint = character;
               }
            }
         }
      }

      return width;
   }

   public float getTextCenterOffsetY(String text, float fontSize) {
      float baselineOffset = this.metrics.getBaselineOffset() * fontSize;
      float minGlyphY = MIN_GLYPH_Y_INITIALIZER;
      float maxGlyphY = MAX_GLYPH_Y_INITIALIZER;

      for (int characterIndex = 0; characterIndex < text.length(); characterIndex++) {
         FontGlyph glyph = this.textGlyph(text.charAt(characterIndex));
         if (glyph != null && !(glyph.getHeight() <= 0.0F)) {
            float glyphTop = baselineOffset - glyph.getTopBearing() * fontSize;
            minGlyphY = Math.min(minGlyphY, glyphTop);
            maxGlyphY = Math.max(maxGlyphY, glyphTop + glyph.getHeight() * fontSize);
         }
      }

      return minGlyphY > maxGlyphY ? baselineOffset : (minGlyphY + maxGlyphY) / 2.0F;
   }

   public boolean usesAtlasMetrics() {
      return this.useAtlasMetrics;
   }

   /** Full padded quad bounds: imported atlases can extend left/above the em box. */
   public Vector4f getTextQuadBounds(String text, float x, float y, float fontSize) {
      float baseline = y + this.metrics.getBaselineOffset() * fontSize;
      float pen = x;
      float left = Float.MAX_VALUE;
      float top = Float.MAX_VALUE;
      float right = -Float.MAX_VALUE;
      float bottom = -Float.MAX_VALUE;
      int previous = -1;
      for (int index = 0; index < text.length(); index++) {
         int codepoint = text.charAt(index);
         FontGlyph glyph = this.textGlyph(codepoint);
         if (glyph == null) {
            continue;
         }
         Map<Integer, Float> kerning = this.kerningByLeftCodepoint.get(previous);
         if (kerning != null) {
            pen += kerning.getOrDefault(codepoint, 0.0F) * fontSize;
         }
         if (glyph.getHeight() > 0.0F) {
            float glyphX = pen + glyph.getLeftBearing() * fontSize;
            float glyphY = baseline - glyph.getTopBearing() * fontSize;
            left = Math.min(left, glyphX);
            top = Math.min(top, glyphY);
            right = Math.max(right, glyphX + glyph.getScaledWidth(fontSize));
            bottom = Math.max(bottom, glyphY + glyph.getHeight() * fontSize);
         }
         pen += glyph.getScaledAdvance(fontSize);
         previous = codepoint;
      }
      return left > right ? new Vector4f(x, y, 0.0F, fontSize)
         : new Vector4f(left, top, right - left, bottom - top);
   }

   public void appendFadedBatchText(VertexConsumer vertexConsumer, String text, float fontSize, float distanceRange, int fontSlot, float letterSpacing, float x, float baselineY, int color, float fadeStart, float fadeEnd) {
      int previousCodepoint = -1;

      for (int characterIndex = 0; characterIndex < text.length(); characterIndex++) {
         char character = text.charAt(characterIndex);
         String rankGlyphText = ReallyWorldRankGlyphs.method__(character);
         if (rankGlyphText != null) {
            float alpha = (color >> 24 & 0xFF) / ЯтЭ;

            for (int rankCharacterIndex = 0; rankCharacterIndex < rankGlyphText.length(); rankCharacterIndex++) {
               char rankCharacter = rankGlyphText.charAt(rankCharacterIndex);
               FontGlyph rankGlyph = this.glyphsByCodepoint.get(Integer.valueOf(rankCharacter));
               if (rankGlyph != null) {
                  Map<Integer, Float> rankKerning = this.kerningByLeftCodepoint.get(previousCodepoint);
                  if (rankKerning != null) {
                     x += rankKerning.getOrDefault(Integer.valueOf(rankCharacter), 0.0F) * fontSize;
                  }

                  int rankColor = ReallyWorldRankGlyphs.必(character, rankCharacterIndex, rankGlyphText.length(), alpha, color);
                  x += rankGlyph.appendFadedBatchQuad(vertexConsumer, fontSize, distanceRange, fontSlot, x, baselineY, rankColor, fadeStart, fadeEnd) + letterSpacing;
                  previousCodepoint = rankCharacter;
               }
            }
         } else {
            FontGlyph glyph = this.textGlyph(character);
            if (glyph != null) {
               Map<Integer, Float> kerning = this.kerningByLeftCodepoint.get(previousCodepoint);
               if (kerning != null) {
                  x += kerning.getOrDefault(Integer.valueOf(character), 0.0F) * fontSize;
               }

               x += glyph.appendFadedBatchQuad(vertexConsumer, fontSize, distanceRange, fontSlot, x, baselineY, color, fadeStart, fadeEnd) + letterSpacing;
               previousCodepoint = character;
            }
         }
      }
   }

   public float getCapHeightCenterOffsetY(float fontSize) {
      return this.metrics.getBaselineOffset() * fontSize - this.getCapHeight(fontSize) / 2.0F;
   }

   public float measureTextWidth(String text, float fontSize, float letterSpacing) {
      return this.measureTextWidth(text, fontSize, letterSpacing, 1.0F);
   }

   public float getCapHeight(float fontSize) {
      if (this.useAtlasMetrics && this.metrics.getCapHeight() != null) {
         return this.metrics.getCapHeight() * fontSize;
      }
      FontGlyph capitalH = this.glyphsByCodepoint.get(72);
      return (capitalH != null ? capitalH.getTopBearing() : this.metrics.getAscender()) * fontSize;
   }

   public static FontFace.Builder builder() {
      return new FontFace.Builder();
   }

   public static class Builder {
      private @com.google.gson.annotations.SerializedName("сД")  String fontName;
      private @com.google.gson.annotations.SerializedName("诶")  Identifier metadataResourceId;
      @SerializedName("1")
      private Identifier textureResourceId;
      private static final String DEFAULT_FONT_NAME = "?";
      private static final String MEDIUM_FONT_NAME = "medium";
      private static final float MEDIUM_KE_KERNING_ADJUSTMENT = -0.085F;
      private boolean useAtlasMetrics;

      public FontFace.Builder useAtlasMetrics(boolean useAtlasMetrics) {
         this.useAtlasMetrics = useAtlasMetrics;
         return this;
      }

      public FontFace.Builder metadataResource(String resourceName) {
         this.metadataResourceId = ClientResourceIds.getFontId(resourceName + ".json");
         return this;
      }

      public FontFace build() {
         FontMetadata metadata = ClientResourceLoader.readJson(this.metadataResourceId, FontMetadata.class);
         AbstractTexture texture = MinecraftAccess.MINECRAFT_CLIENT.getTextureManager().getTexture(this.textureResourceId);
         if (metadata == null) {
            throw new RuntimeException(
               "Failed to read font data file: " + this.metadataResourceId.toString() + "; Are you sure this is json file? Try to check the correctness of its syntax."
            );
         }

         float atlasWidth = metadata.getAtlas().getWidth();
         float atlasHeight = metadata.getAtlas().getHeight();
         Map<Integer, FontGlyph> glyphsByCodepoint = metadata.getGlyphs().stream().collect(Collectors.toMap(FontMetadata.GlyphData::getUnicode, glyphData -> new FontGlyph(glyphData, atlasWidth, atlasHeight, this.useAtlasMetrics)));
         HashMap<Integer, Map<Integer, Float>> kerningByLeftCodepoint = new HashMap<>();
         metadata.getKernings().forEach(kerningPair -> {
            Map<Integer, Float> rightCodepointAdvances = kerningByLeftCodepoint.computeIfAbsent(kerningPair.getLeftChar(), var0x -> new HashMap<>());
            rightCodepointAdvances.put(kerningPair.getRightChar(), kerningPair.getAdvance());
         });
         if (MEDIUM_FONT_NAME.equals(this.fontName)) {
            kerningByLeftCodepoint.computeIfAbsent(75, var0 -> new HashMap<>()).put(101, MEDIUM_KE_KERNING_ADJUSTMENT);
         }

         return new FontFace(this.fontName, texture, metadata.getAtlas(), metadata.getMetrics(), glyphsByCodepoint, kerningByLeftCodepoint, this.useAtlasMetrics);
      }

      public FontFace.Builder textureResource(String resourceName) {
         this.textureResourceId = ClientResourceIds.getFontId(resourceName + ".png");
         return this;
      }

      public FontFace.Builder fontName(String fontName) {
         this.fontName = fontName;
         return this;
      }

      private Builder() {
         this.fontName = DEFAULT_FONT_NAME;
      }
   }
}
