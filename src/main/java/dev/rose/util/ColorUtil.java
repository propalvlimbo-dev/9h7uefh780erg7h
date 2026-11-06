package dev.rose.util;

public final class ColorUtil {
   private ColorUtil() {
   }

   public static int argb(int alpha, int red, int green, int blue) {
      return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
   }

   public static int alpha(int color) {
      return color >>> 24;
   }

   public static int red(int color) {
      return color >> 16 & 0xFF;
   }

   public static int green(int color) {
      return color >> 8 & 0xFF;
   }

   public static int blue(int color) {
      return color & 0xFF;
   }

   public static int withAlpha(int color, int alpha) {
      return Math.clamp(alpha, 0, 255) << 24 | color & 16777215;
   }

   public static int fade(int color, float factor) {
      return withAlpha(color, Math.round(alpha(color) * factor));
   }

   public static int lerp(int from, int to, float t) {
      return argb(
         Math.round(alpha(from) + (alpha(to) - alpha(from)) * t),
         Math.round(red(from) + (red(to) - red(from)) * t),
         Math.round(green(from) + (green(to) - green(from)) * t),
         Math.round(blue(from) + (blue(to) - blue(from)) * t)
      );
   }

   public static float[] toOklab(int color) {
      double r = srgbToLinear(red(color) / 255.0);
      double g = srgbToLinear(green(color) / 255.0);
      double b = srgbToLinear(blue(color) / 255.0);
      double l = Math.cbrt(0.4122214708 * r + 0.5363325363 * g + 0.0514459929 * b);
      double m = Math.cbrt(0.2119034982 * r + 0.6806995451 * g + 0.1073969566 * b);
      double s = Math.cbrt(0.0883024619 * r + 0.2817188376 * g + 0.6299787005 * b);
      return new float[]{
         (float)(0.2104542553 * l + 0.793617785 * m - 0.0040720468 * s),
         (float)(1.9779984951 * l - 2.428592205 * m + 0.4505937099 * s),
         (float)(0.0259040371 * l + 0.7827717662 * m - 0.808675766 * s)
      };
   }

   private static double srgbToLinear(double c) {
      return c <= 0.04045 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
   }

   public static float[] toHsb(int color) {
      float r = red(color) / 255.0F;
      float g = green(color) / 255.0F;
      float b = blue(color) / 255.0F;
      float max = Math.max(r, Math.max(g, b));
      float min = Math.min(r, Math.min(g, b));
      float delta = max - min;
      float hue = 0.0F;
      if (delta > 0.0F) {
         if (max == r) {
            hue = (g - b) / delta % 6.0F;
         } else if (max == g) {
            hue = (b - r) / delta + 2.0F;
         } else {
            hue = (r - g) / delta + 4.0F;
         }

         hue /= 6.0F;
         if (hue < 0.0F) {
            hue++;
         }
      }

      return new float[]{hue, max == 0.0F ? 0.0F : delta / max, max};
   }

   public static int hsb(float hue, float saturation, float brightness, int alpha) {
      float h = (hue - (float)Math.floor(hue)) * 6.0F;
      int sector = (int)h;
      float f = h - sector;
      float p = brightness * (1.0F - saturation);
      float q = brightness * (1.0F - saturation * f);
      float t = brightness * (1.0F - saturation * (1.0F - f));
      float r;
      float g;
      float b;
      switch (sector) {
         case 0:
            r = brightness;
            g = t;
            b = p;
            break;
         case 1:
            r = q;
            g = brightness;
            b = p;
            break;
         case 2:
            r = p;
            g = brightness;
            b = t;
            break;
         case 3:
            r = p;
            g = q;
            b = brightness;
            break;
         case 4:
            r = t;
            g = p;
            b = brightness;
            break;
         default:
            r = brightness;
            g = p;
            b = q;
      }

      return argb(alpha, Math.round(r * 255.0F), Math.round(g * 255.0F), Math.round(b * 255.0F));
   }
}
