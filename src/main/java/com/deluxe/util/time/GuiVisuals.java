package com.deluxe.util.time;

import com.deluxe.gui.Render2D;
import java.util.ArrayList;
import java.util.List;

public final class GuiVisuals {
   public static int white = new java.awt.Color(255, 255, 255, 255).getRGB();
   public static int brightTextColor = new java.awt.Color(255, 255, 255, 184).getRGB();
   public static int mutedTextColor = new java.awt.Color(255, 255, 255, 122).getRGB();
   public static int accentColor = new java.awt.Color(71, 123, 255, 255).getRGB();
   public static int accentLightColor = new java.awt.Color(123, 163, 255, 255).getRGB();
   public static int accentGradientColor = new java.awt.Color(97, 142, 255, 255).getRGB();
   public static int accentOutlineColor = new java.awt.Color(102, 144, 255, 255).getRGB();
   public static int accentAlpha20 = new java.awt.Color(71, 123, 255, 20).getRGB();
   public static final String ICON_FONT = "icons";

   public static List<String> wrapWords(GuiFont font, String text, float fontSize, float maxWidth) {
      ArrayList<String> lines = new ArrayList();
      StringBuilder textBuilder = new StringBuilder();

      for (String word : text.split((" "))) {
         String candidateLine = textBuilder.length() == 0 ? word : textBuilder + " " + word;
         if (textBuilder.length() != 0 && !(GuiTextRenderer.getWidth(font, candidateLine, fontSize) <= maxWidth)) {
            lines.add(textBuilder.toString());
            textBuilder = new StringBuilder(word);
         } else {
            textBuilder = new StringBuilder(candidateLine);
         }
      }

      if (textBuilder.length() > 0) {
         lines.add(textBuilder.toString());
      }

      return lines;
   }

   public static float easeOutBack(float progress) {
      progress = Math.max(0.0F, Math.min(1.0F, progress));
      return 1.0F + (2.70158F) * (progress - 1.0F) * (progress - 1.0F) * (progress - 1.0F) + (1.70158F) * (progress - 1.0F) * (progress - 1.0F);
   }

   public static int multiplyAlpha(int color, float alpha) {
      return Math.round((color >>> 24 & 0xFF) * Math.max(0.0F, Math.min(1.0F, alpha))) << 24 | color & (16777215);
   }

   private GuiVisuals() {
   }

   public static boolean containsPoint(float pointX, float pointY, float x, float y, float width, float height) {
      return pointX >= x && pointX <= x + width && pointY >= y && pointY <= y + height;
   }

   public static void drawCenteredMsdfText(String font, String text, float centerX, float centerY, float fontSize, int color) {
      float[] bounds = Render2D.getMsdfTextBounds(font, text, fontSize);
      Render2D.drawMsdfText(font, text, centerX - (bounds[0] + bounds[2]) / 2.0F, centerY - (bounds[1] + bounds[3]) / 2.0F + 1.0F, fontSize, color);
   }

   public static int lerpColor(int startColor, int endColor, float progress) {
      float blendProgress = Math.max(0.0F, Math.min(1.0F, progress));
      return lerpChannel(startColor >>> 24 & 0xFF, endColor >>> 24 & 0xFF, blendProgress) << 24
         | lerpChannel(startColor >>> 16 & 0xFF, endColor >>> 16 & 0xFF, blendProgress) << 16
         | lerpChannel(startColor >>> 8 & 0xFF, endColor >>> 8 & 0xFF, blendProgress) << 8
         | lerpChannel(startColor & 0xFF, endColor & 0xFF, blendProgress);
   }

   private static int lerpChannel(int startChannel, int endChannel, float progress) {
      return Math.round(startChannel + (endChannel - startChannel) * progress);
   }

   public static void drawCenteredIcon(String glyph, float centerX, float centerY, float size, int color) {
      drawCenteredMsdfText(("icons"), glyph, centerX, centerY, size, color);
   }
}
