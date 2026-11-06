package sg.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import org.joml.Vector4f;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ColorMath;

public final class FontGlyph {
   private final @com.google.gson.annotations.SerializedName("Д西")  int unicode;
   private final @com.google.gson.annotations.SerializedName("тТ")  float uLeft;
   private final @com.google.gson.annotations.SerializedName("тО")  float uRight;
   private final @com.google.gson.annotations.SerializedName("тФ")  float vTop;
   private final @com.google.gson.annotations.SerializedName("тЙ")  float vBottom;
   private final @com.google.gson.annotations.SerializedName("тк")  float advance;
   private final @com.google.gson.annotations.SerializedName("тг")  float topBearing;
   private final @com.google.gson.annotations.SerializedName("т0")  float width;
   private final @com.google.gson.annotations.SerializedName("тх")  float height;
   private final float leftBearing;
   private static final float Яб西 = -1.0F;
   private static final float Ябд = 255.0F;
   private static final float ЯбБ = 255.0F;
   private static final float ЯбС = 255.0F;
   private static final float Ябз = 255.0F;
   private static final float Ябц = -1.0F;
   private static final float Ябя = 255.0F;
   private static final float ЯЫс = 255.0F;
   private static final float ЯЫВ = 255.0F;
   private static final float ЯЫЯ = 255.0F;
   private static final float ЯЫХ = -1.0F;
   private static final float ЯЫе = 255.0F;
   private static final float ЯЫА = 3.0F;

   public FontGlyph(FontMetadata.GlyphData glyphData, float atlasWidthPixels, float atlasHeightPixels) {
      this(glyphData, atlasWidthPixels, atlasHeightPixels, false);
   }

   public FontGlyph(FontMetadata.GlyphData glyphData, float atlasWidthPixels, float atlasHeightPixels, boolean useAtlasMetrics) {
      this.unicode = glyphData.getUnicode();
      this.advance = glyphData.getAdvance();
      FontMetadata.GlyphBounds atlasBounds = glyphData.getAtlasBounds();
      if (atlasBounds != null) {
         this.uLeft = atlasBounds.getLeft() / atlasWidthPixels;
         this.uRight = atlasBounds.getRight() / atlasWidthPixels;
         this.vTop = 1.0F - atlasBounds.getTop() / atlasHeightPixels;
         this.vBottom = 1.0F - atlasBounds.getBottom() / atlasHeightPixels;
      } else {
         this.uLeft = this.uRight = this.vTop = this.vBottom = 0.0F;
      }

      FontMetadata.GlyphBounds planeBounds = glyphData.getPlaneBounds();
      this.leftBearing = useAtlasMetrics && planeBounds != null ? planeBounds.getLeft() : 0.0F;
      if (planeBounds != null) {
         this.width = planeBounds.getRight() - planeBounds.getLeft();
         this.height = planeBounds.getTop() - planeBounds.getBottom();
         this.topBearing = planeBounds.getTop();
      } else {
         this.width = this.height = this.topBearing = 0.0F;
      }
   }

   public float getHeight() {
      return this.height;
   }

   public float getLeftBearing() {
      return this.leftBearing;
   }

   public float appendQuad(VertexConsumer vertexConsumer, float fontSize, float x, float baselineY, int color) {
      x += this.leftBearing * fontSize;
      baselineY -= this.topBearing * fontSize;
      float widthPixels = this.width * fontSize;
      float heightPixels = this.height * fontSize;
      float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * widthPixels;
      float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * heightPixels;
      float ndcLeft = Яб西 + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
      float ndcBottom = 1.0F - baselineY * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
      Vector4f rgba = ColorMath.toRgbaVector(color);
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom, 0.0F)
         .setUv(this.uLeft, this.vBottom)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * (UiDrawing.getGlobalAlpha() / Ябд));
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom, 0.0F)
         .setUv(this.uRight, this.vBottom)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * (UiDrawing.getGlobalAlpha() / ЯбБ));
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F)
         .setUv(this.uRight, this.vTop)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * (UiDrawing.getGlobalAlpha() / ЯбС));
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom + ndcHeight, 0.0F)
         .setUv(this.uLeft, this.vTop)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * (UiDrawing.getGlobalAlpha() / Ябз));
      return this.advance * fontSize;
   }

   public float getScaledWidth(float fontSize) {
      return this.width * fontSize;
   }

   public int getUnicode() {
      return this.unicode;
   }

   public float appendFadedBatchQuad(VertexConsumer vertexConsumer, float fontSize, float distanceRange, int fontSlot, float x, float baselineY, int color, float fadeStart, float fadeEnd) {
      x += this.leftBearing * fontSize;
      baselineY -= this.topBearing * fontSize;
      float widthPixels = this.width * fontSize;
      float heightPixels = this.height * fontSize;
      float advancePixels = this.advance * fontSize;
      if (x >= fadeEnd) {
         return advancePixels;
      }

      float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * widthPixels;
      float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * heightPixels;
      float ndcLeft = ЯЫХ + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
      float ndcBottom = 1.0F - baselineY * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
      Vector4f rgba = ColorMath.toRgbaVector(color);
      float globalAlpha = UiDrawing.getGlobalAlpha() / ЯЫе;
      float leftFade = this.getHorizontalFade(x, fadeStart, fadeEnd);
      float rightFade = this.getHorizontalFade(x + widthPixels, fadeStart, fadeEnd);
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom, 0.0F)
         .setUv(this.uLeft, this.vBottom)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * globalAlpha * leftFade)
         .setUv1(Math.round(distanceRange), fontSlot)
         .setUv2(0, 0);
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom, 0.0F)
         .setUv(this.uRight, this.vBottom)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * globalAlpha * rightFade)
         .setUv1(Math.round(distanceRange), fontSlot)
         .setUv2(0, 0);
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F)
         .setUv(this.uRight, this.vTop)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * globalAlpha * rightFade)
         .setUv1(Math.round(distanceRange), fontSlot)
         .setUv2(0, 0);
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom + ndcHeight, 0.0F)
         .setUv(this.uLeft, this.vTop)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * globalAlpha * leftFade)
         .setUv1(Math.round(distanceRange), fontSlot)
         .setUv2(0, 0);
      return advancePixels;
   }

   public float appendBatchQuad(VertexConsumer vertexConsumer, float fontSize, float distanceRange, int fontSlot, float x, float baselineY, int color) {
      x += this.leftBearing * fontSize;
      baselineY -= this.topBearing * fontSize;
      float widthPixels = this.width * fontSize;
      float heightPixels = this.height * fontSize;
      float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * widthPixels;
      float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * heightPixels;
      float ndcLeft = Ябц + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
      float ndcBottom = 1.0F - baselineY * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
      Vector4f rgba = ColorMath.toRgbaVector(color);
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom, 0.0F)
         .setUv(this.uLeft, this.vBottom)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * (UiDrawing.getGlobalAlpha() / Ябя))
         .setUv1(Math.round(distanceRange), fontSlot)
         .setUv2(0, 0);
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom, 0.0F)
         .setUv(this.uRight, this.vBottom)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * (UiDrawing.getGlobalAlpha() / ЯЫс))
         .setUv1(Math.round(distanceRange), fontSlot)
         .setUv2(0, 0);
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft + ndcWidth, ndcBottom + ndcHeight, 0.0F)
         .setUv(this.uRight, this.vTop)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * (UiDrawing.getGlobalAlpha() / ЯЫВ))
         .setUv1(Math.round(distanceRange), fontSlot)
         .setUv2(0, 0);
      vertexConsumer.addVertex(UiDrawing.getTransform(), ndcLeft, ndcBottom + ndcHeight, 0.0F)
         .setUv(this.uLeft, this.vTop)
         .setColor(rgba.x, rgba.y, rgba.z, rgba.w * (UiDrawing.getGlobalAlpha() / ЯЫЯ))
         .setUv1(Math.round(distanceRange), fontSlot)
         .setUv2(0, 0);
      return this.advance * fontSize;
   }

   public float getScaledAdvance(float fontSize) {
      return this.advance * fontSize;
   }

   public Vector4f getUvBounds() {
      return new Vector4f(this.uLeft, this.vTop, this.uRight, this.vBottom);
   }

   public float getTopBearing() {
      return this.topBearing;
   }

   private float getHorizontalFade(float x, float fadeStart, float fadeEnd) {
      if (x <= fadeStart) {
         return 1.0F;
      }

      if (x >= fadeEnd) {
         return 0.0F;
      }

      float progress = (fadeEnd - x) / (fadeEnd - fadeStart);
      return progress * progress * (ЯЫА - 2.0F * progress);
   }
}
