package sg.render;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public final class FontMetadata {
   private FontMetadata.AtlasData atlas;
   private FontMetadata.Metrics metrics;
   private List<FontMetadata.GlyphData> glyphs;
   @SerializedName("kerning")
   private List<FontMetadata.KerningPair> kernings;

   public List<FontMetadata.GlyphData> getGlyphs() {
      return this.glyphs;
   }

   public List<FontMetadata.KerningPair> getKernings() {
      return this.kernings;
   }

   public FontMetadata.Metrics getMetrics() {
      return this.metrics;
   }

   public FontMetadata.AtlasData getAtlas() {
      return this.atlas;
   }

   public static final class GlyphBounds {
      private float left;
      private float top;
      private float right;
      private float bottom;

      public float getBottom() {
         return this.bottom;
      }

      public float getTop() {
         return this.top;
      }

      public float getRight() {
         return this.right;
      }

      public float getLeft() {
         return this.left;
      }
   }

   public static final class KerningPair {
      @SerializedName("unicode1")
      private int leftChar;
      @SerializedName("unicode2")
      private int rightChar;
      private float advance;

      public int getRightChar() {
         return this.rightChar;
      }

      public float getAdvance() {
         return this.advance;
      }

      public int getLeftChar() {
         return this.leftChar;
      }
   }

   public static final class GlyphData {
      private int unicode;
      private float advance;
      private FontMetadata.GlyphBounds planeBounds;
      private FontMetadata.GlyphBounds atlasBounds;

      public int getUnicode() {
         return this.unicode;
      }

      public float getAdvance() {
         return this.advance;
      }

      public FontMetadata.GlyphBounds getPlaneBounds() {
         return this.planeBounds;
      }

      public FontMetadata.GlyphBounds getAtlasBounds() {
         return this.atlasBounds;
      }
   }

   public static final class AtlasData {
      @SerializedName("distanceRange")
      private float range;
      private float width;
      private float height;

      public float getHeight() {
         return this.height;
      }

      public float getRange() {
         return this.range;
      }

      public float getWidth() {
         return this.width;
      }
   }

   public static final class Metrics {
      private float lineHeight;
      private float ascender;
      private float descender;
      private Float capHeight;

      public Float getCapHeight() {
         return this.capHeight;
      }

      public float getLineHeight() {
         return this.lineHeight;
      }

      public float getDescender() {
         return this.descender;
      }

      public float getAscender() {
         return this.ascender;
      }

      public float getBaselineOffset() {
         return this.lineHeight + this.descender;
      }
   }
}
