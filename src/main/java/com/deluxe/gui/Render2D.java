package com.deluxe.gui;

import com.deluxe.internal.RoundedClipStack;
import com.deluxe.render.pipeline.RoundedRectangleSpec;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.ec.ClientFonts;
import sg.ec.FontRenderer;
import sg.render.pipeline.UiDrawing;

public final class Render2D {
   private static final float UNIT_TO_PIXELS = 2.0F;
   private static final float DEFAULT_GUI_BLUR_RADIUS = 36.0F;
   private static boolean frameActive;
   private static final ArrayDeque<Float> alphaStack = new ArrayDeque<>();
   private static float alpha = 1.0F;
   private static final HashMap<String, FontRenderer> fontCache = new HashMap<>();

   public static void setSharedBlurLayer(int layer) {
   }

   public static void popAlpha() {
      alpha = alphaStack.isEmpty() ? 1.0F : alphaStack.pop();
   }

   public static void pushAlpha(float multiplier) {
      alphaStack.push(alpha);
      alpha = alpha * Math.max(0.0F, Math.min(1.0F, multiplier));
   }

   public static int applyAlpha(int color) {
      if (alpha >= (0.999F)) {
         return color;
      }

      int effectiveAlpha = Math.round((color >>> 24 & 0xFF) * alpha);
      return effectiveAlpha << 24 | color & (16777215);
   }

   public static float getAlpha() {
      return alpha;
   }

   public static void begin() {
      if (!frameActive) {
         frameActive = true;
         alpha = 1.0F;
         alphaStack.clear();
         RoundedClipStack.clear();
         UiDrawing.resetTransforms();
         UiDrawing.disableScissor();
         UiDrawing.resetGlobalAlpha();
      }
   }

   public static void begin(Object context) {
      begin();
   }

   public static void end() {
      if (frameActive) {
         frameActive = false;
         alpha = 1.0F;
         alphaStack.clear();
         RoundedClipStack.clear();
         UiDrawing.resetTransforms();
         UiDrawing.disableScissor();
         UiDrawing.resetGlobalAlpha();
      }
   }

   public static void nextLayer(Object context) {
   }

   public static void nextLayer() {
   }

   private static float px(float units) {
      return units * UNIT_TO_PIXELS;
   }

   private static FontRenderer resolveFont(String fontName) {
      String key = fontName == null ? "sf" : fontName;
      FontRenderer font = fontCache.get(key);
      if (font == null) {
         switch (key) {
            case "sf_medium":
               font = new FontRenderer("deluxe_medium");
               break;
            case "icons":
               font = ClientFonts.getIconsFont();
               break;
            default:
               font = new FontRenderer("deluxe_regular");
         }

         fontCache.put(key, font);
      }

      return font;
   }

   private static Identifier resolveTexture(String path) {
      return ClientResourceIds.getImageId(path);
   }

   public static void drawText(String fontName, String text, float x, float y, float size, int color) {
      if (text == null || text.isEmpty() || size <= 0.0F) {
         return;
      }

      FontRenderer font = resolveFont(fontName);
      UiDrawing.setGlobalAlpha(255.0F);
      UiDrawing.drawText(font, text, px(x), px(y), px(size), applyAlpha(color), 0.0F, 1.0F, 1.0F);
   }

   public static float getTextWidth(String fontName, String text, float size) {
      if (text == null || text.isEmpty()) {
         return 0.0F;
      }

      FontRenderer font = resolveFont(fontName);
      return UiDrawing.getTextWidth(font, text, px(size), 1.0F) / UNIT_TO_PIXELS;
   }

   public static float[] getMsdfTextBounds(String fontName, String text, float size) {
      float width = getTextWidth(fontName, text, size);
      float capHeight = UiDrawing.getCapHeight(resolveFont(fontName), px(size)) / UNIT_TO_PIXELS;
      return new float[]{0.0F, 0.0F, width, capHeight};
   }

   public static void drawMsdfText(String fontName, String text, float x, float y, float size, int color) {
      drawText(fontName, text, x, y, size, color);
   }

   public static void drawRotatedText(String fontName, String text, float x, float y, float size, int color, float rotationDegrees, float originX, float originY) {
      if (text == null || text.isEmpty() || size <= 0.0F) {
         return;
      }

      Matrix4f rotation = createRotationMatrix(rotationDegrees, originX, originY);
      UiDrawing.setTransform(rotation);

      try {
         drawText(fontName, text, x, y, size, color);
      } finally {
         UiDrawing.resetTransforms();
      }
   }

   public static void drawRotatedMsdfText(String fontName, String text, float x, float y, float size, int color, float rotationDegrees, float originX, float originY) {
      drawRotatedText(fontName, text, x, y, size, color, rotationDegrees, originX, originY);
   }

   public static void drawObfuscatedText(String fontName, String text, float x, float y, float size, int color) {
      drawText(fontName, text, x, y, size, color);
   }

   public static void drawGradientText(String fontName, String text, float x, float y, float size, int colorTopLeft, int colorTopRight, int colorBottomRight, int colorBottomLeft) {
      drawText(fontName, text, x, y, size, colorTopLeft);
   }

   public static void drawFadedText(
      String fontName, String text, float x, float y, float size, int color, float fadeLeftX, float fadeRightX, float fadeWidth, float fadeLeftStrength, float fadeRightStrength
   ) {
      if (text == null || text.isEmpty() || size <= 0.0F) {
         return;
      }

      float textWidth = getTextWidth(fontName, text, size);
      float clipLeft = fadeLeftStrength > 0.0F ? Math.max(fadeLeftX, x) : x;
      float clipRight = fadeRightStrength > 0.0F ? Math.min(fadeRightX, x + textWidth) : x + textWidth;
      if (clipRight - clipLeft <= 0.0F) {
         return;
      }

      pushScissor(null, clipLeft, y - size, clipRight - clipLeft, size * 2.0F);

      try {
         drawText(fontName, text, x, y, size, color);
      } finally {
         popScissor(null);
      }
   }

   public static void drawFadedText(
      String fontName, String text, float x, float y, float size, int color, float fadeLeftX, float fadeRightX, float fadeWidth, boolean fadeLeft, boolean fadeRight
   ) {
      drawFadedText(fontName, text, x, y, size, color, fadeLeftX, fadeRightX, fadeWidth, fadeLeft ? 1.0F : 0.0F, fadeRight ? 1.0F : 0.0F);
   }

   public static void fillRoundedRectangle(float x, float y, float width, float height, float radius, int color) {
      if (width <= 0.0F || height <= 0.0F) {
         return;
      }

      UiDrawing.setGlobalAlpha(255.0F);
      UiDrawing.drawRoundedRect(px(x), px(y), px(width), px(height), px(radius), applyAlpha(color));
   }

   public static void fillRoundedRectangle(
      float x, float y, float width, float height, float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft, int color
   ) {
      if (width <= 0.0F || height <= 0.0F) {
         return;
      }

      UiDrawing.setGlobalAlpha(255.0F);
      UiDrawing.drawRoundedRect(
         px(x),
         px(y),
         px(width),
         px(height),
         new Vector4f(px(radiusBottomLeft), px(radiusBottomRight), px(radiusTopRight), px(radiusTopLeft)),
         applyAlpha(color)
      );
   }

   public static void fillRoundedRectangle(RoundedRectangleSpec spec) {
      if (spec == null || !spec.visible()) {
         return;
      }

      fillRoundedGradient(
         spec.x(),
         spec.y(),
         spec.width(),
         spec.height(),
         spec.radiusTopLeft(),
         spec.radiusTopRight(),
         spec.radiusBottomRight(),
         spec.radiusBottomLeft(),
         spec.colorTopLeft(),
         spec.colorTopRight(),
         spec.colorBottomRight(),
         spec.colorBottomLeft()
      );
   }

   public static void fillRectangle(float x, float y, float width, float height, int color) {
      fillRoundedRectangle(x, y, width, height, 0.0F, color);
   }

   public static void drawRotatedRectangle(
      float x, float y, float width, float height, float radius, int color, float rotationDegrees, float originX, float originY
   ) {
      if (width <= 0.0F || height <= 0.0F) {
         return;
      }

      Matrix4f rotation = createRotationMatrix(rotationDegrees, originX, originY);
      UiDrawing.setTransform(rotation);

      try {
         fillRoundedRectangle(x, y, width, height, radius, color);
      } finally {
         UiDrawing.resetTransforms();
      }
   }

   public static void fillRoundedGradient(float x, float y, float width, float height, float radius, int colorTopLeft, int colorTopRight, int colorBottomRight, int colorBottomLeft) {
      fillRoundedGradient(x, y, width, height, radius, radius, radius, radius, colorTopLeft, colorTopRight, colorBottomRight, colorBottomLeft);
   }

   public static void fillRoundedGradient(
      float x, float y, float width, float height, float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft, int colorTopLeft, int colorTopRight, int colorBottomRight, int colorBottomLeft
   ) {
      if (width <= 0.0F || height <= 0.0F) {
         return;
      }

      UiDrawing.setGlobalAlpha(255.0F);
      Vector4i cornerColors = new Vector4i(applyAlpha(colorBottomLeft), applyAlpha(colorTopLeft), applyAlpha(colorBottomRight), applyAlpha(colorTopRight));
      Vector4f cornerRadii = new Vector4f(px(radiusBottomLeft), px(radiusBottomRight), px(radiusTopRight), px(radiusTopLeft));
      UiDrawing.drawRoundedRect(px(x), px(y), px(width), px(height), cornerRadii, cornerColors);
   }

   public static void fillCircle(float centerX, float centerY, float radius, int color) {
      fillRoundedRectangle(centerX - radius, centerY - radius, radius * 2.0F, radius * 2.0F, radius, color);
   }

   public static void fillCircle(float centerX, float centerY, float radius, float smoothness, int color) {
      fillCircle(centerX, centerY, radius, color);
   }

   public static void strokeCircle(float centerX, float centerY, float radius, float thickness, int color) {
      if (radius <= 0.0F || thickness <= 0.0F) {
         return;
      }

      UiDrawing.setGlobalAlpha(255.0F);
      UiDrawing.drawOutline(px(centerX - radius), px(centerY - radius), px(radius * 2.0F), px(radius * 2.0F), px(thickness), px(radius), applyAlpha(color));
   }

   public static void drawOutline(float x, float y, float width, float height, float radius, float thickness, int color) {
      drawOutline(x, y, width, height, radius, radius, radius, radius, thickness, color);
   }

   public static void drawOutline(float x, float y, float width, float height, float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft, float thickness, int color) {
      if (width <= 0.0F || height <= 0.0F || thickness <= 0.0F) {
         return;
      }

      UiDrawing.setGlobalAlpha(255.0F);
      UiDrawing.drawOutline(
         px(x),
         px(y),
         px(width),
         px(height),
         px(thickness),
         new Vector4f(px(radiusBottomLeft), px(radiusBottomRight), px(radiusTopRight), px(radiusTopLeft)),
         applyAlpha(color)
      );
   }

   public static void drawRoundedImage(String path, float x, float y, float width, float height, float radius, int color) {
      if (path == null || width <= 0.0F || height <= 0.0F) {
         return;
      }

      UiDrawing.setGlobalAlpha(255.0F);
      UiDrawing.drawTexture(px(x), px(y), px(width), px(height), px(radius), applyAlpha(color), resolveTexture(path));
   }

    public static void drawImage(String path, float x, float y, float width, float height) {
       drawRoundedImage(path, x, y, width, height, 0.0F, -1);
    }

   public static void drawClientLogo(float x, float y, float width, float height, int color) {
      UiDrawing.setGlobalAlpha(255.0F);
      sg.render.ClientLogo.drawInBox(px(x + width * 0.035F), px(y + height * 0.035F),
         px(width * 0.93F), px(height * 0.93F), applyAlpha(color));
   }

   public static void drawRotatedImage(String path, float x, float y, float width, float height, float rotationDegrees, float originX, float originY, int color) {
      if (path == null || width <= 0.0F || height <= 0.0F) {
         return;
      }

      Matrix4f rotation = createRotationMatrix(rotationDegrees, originX, originY);
      UiDrawing.setTransform(rotation);

      try {
         UiDrawing.setGlobalAlpha(255.0F);
         UiDrawing.drawTexture(px(x), px(y), px(width), px(height), 0.0F, applyAlpha(color), resolveTexture(path));
      } finally {
         UiDrawing.resetTransforms();
      }
   }

   public static void drawSharedBlur(float x, float y, float width, float height, float radius, float blurRadius, float smoothness, int color) {
      if (width <= 0.0F || height <= 0.0F) {
         return;
      }

      UiDrawing.setGlobalAlpha(255.0F);
      UiDrawing.drawBlur(px(x), px(y), px(width), px(height), Math.max(1.0F, blurRadius), px(radius), applyAlpha(color));
   }

   public static void drawGuiBlur(float x, float y, float width, float height, float radius) {
      drawSharedBlur(x, y, width, height, radius, DEFAULT_GUI_BLUR_RADIUS, 1.0F, -1);
   }

   public static void drawGuiBlur(float x, float y, float width, float height, float radius, int color) {
      drawSharedBlur(x, y, width, height, radius, DEFAULT_GUI_BLUR_RADIUS, 1.0F, color);
   }

   public static void drawHuePicker(float x, float y, float width, float height, float opacity) {
      if (width <= 0.0F || height <= 0.0F) {
         return;
      }

      UiDrawing.setGlobalAlpha(255.0F);
      int strips = 16;

      for (int stripIndex = 0; stripIndex < strips; stripIndex++) {
         float hueStart = (float)stripIndex / strips;
         float hueEnd = (float)(stripIndex + 1) / strips;
         int colorStart = Color.HSBtoRGB(hueStart, 1.0F, 1.0F);
         int colorEnd = Color.HSBtoRGB(hueEnd, 1.0F, 1.0F);
         float stripX = x + width * hueStart;
         float stripWidth = width / strips;
         Vector4i stripColors = new Vector4i(
            applyArgbOpacity(colorStart, opacity),
            applyArgbOpacity(colorStart, opacity),
            applyArgbOpacity(colorEnd, opacity),
            applyArgbOpacity(colorEnd, opacity)
         );
         UiDrawing.drawRoundedRect(px(stripX), px(y), px(stripWidth), px(height), new Vector4f(0.0F), stripColors);
      }
   }

   private static int applyArgbOpacity(int rgbColor, float opacity) {
      float combinedOpacity = Math.max(0.0F, Math.min(1.0F, opacity)) * alpha;
      int effectiveAlpha = Math.round(combinedOpacity * 255.0F);
      return effectiveAlpha << 24 | rgbColor & (16777215);
   }

   public static void pushScissor(Object context, float x, float y, float width, float height) {
      UiDrawing.pushScissor(px(x), px(y), px(width), px(height));
   }

   public static void pushScissor(float x, float y, float width, float height) {
      UiDrawing.pushScissor(px(x), px(y), px(width), px(height));
   }

   public static void popScissor(Object context) {
      UiDrawing.popScissor();
   }

   public static void popScissor() {
      UiDrawing.popScissor();
   }

   private static Matrix4f createRotationMatrix(float rotationDegrees, float originX, float originY) {
      Minecraft client = Minecraft.getInstance();
      float screenWidth = Math.max(1.0F, client.getWindow().getScreenWidth());
      float screenHeight = Math.max(1.0F, client.getWindow().getScreenHeight());
      float originClipX = -1.0F + px(originX) * 2.0F / screenWidth;
      float originClipY = 1.0F - px(originY) * 2.0F / screenHeight;
      Matrix4f rotation = new Matrix4f();
      rotation.translate(originClipX, originClipY, 0.0F);
      rotation.rotate((float)Math.toRadians(-rotationDegrees), 0.0F, 0.0F, 1.0F);
      rotation.translate(-originClipX, -originClipY, 0.0F);
      return rotation;
   }

   private Render2D() {
   }
}
