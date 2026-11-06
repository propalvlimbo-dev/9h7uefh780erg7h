package sg.util.time;

import java.awt.Color;
import lombok.Generated;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import org.joml.Vector4f;
import sg.rotation.AimGeometryMath;
import sg.theme.ThemeColorSlot;

public final class ColorMath {
   private static final long еХ = 10000L;
   private static final float В7т = 8.0F;
   private static final float В7а = 360.0F;
   private static final float В7Ю = 360.0F;
   private static final float В7Ч = 0.85F;
   private static final int В7Л = -16777216;
   private static final float В7诶 = 16.0F;
   private static final float В71 = 360.0F;
   private static final int В7Ь = 16777215;
   private static final int В7衣 = 16777215;
   private static final long В7ш = 10000L;
   private static final float В7И = 10000.0F;
   private static final float В7ь = 0.5F;
   private static final float В7ж = 0.5F;
   private static final double В7弟 = Math.PI;
   private static final double В7б = 2.0;
   private static final long В7Ы = 360L;
   private static final float В7l = 180.0F;
   private static final float В78 = 255.0F;
   private static final float В75 = 255.0F;
   private static final float В7в = 255.0F;
   private static final float В7ъ = 1.5F;
   private static final float В7М = 255.0F;
   private static final float В7Н = 255.0F;
   private static final float В7н = 255.0F;
   private static final float В7Ц = 255.0F;
   private static final float В7ч = 255.0F;
   private static final float В7Э = 255.0F;
   private static final float В7必 = 255.0F;
   private static final float В7щ = 255.0F;
   private static final float В7Г = 255.0F;
   private static final float В7л = 255.0F;
   private static final float В7э = 255.0F;
   private static final float В7ю = 255.0F;
   private static final float В7Щ = 255.0F;
   private static final float В7у = 255.0F;
   private static final float В72 = 255.0F;
   private static final float В7ф = 255.0F;
   private static final int В7и = -16777216;
   private static final float В79 = 255.0F;
   private static final float В7о = 255.0F;
   private static final float В7O = 255.0F;
   private static final String В77 = "#";
   private static final String В7Р = "0x";
   private static final String В73 = "0X";
   private static final double В7р = 255.0;
   private static final String В76 = "This is a utility class and cannot be instantiated";

   public static int getAlpha(int argb) {
      return argb >> 24 & 0xFF;
   }

   public static Vector4f toRgbaVector(int argb) {
      float alpha = (argb >> 24 & 0xFF) / В7Щ;
      float red = (argb >> 16 & 0xFF) / В7у;
      float green = (argb >> 8 & 0xFF) / В72;
      float blue = (argb & 0xFF) / В7ф;
      return new Vector4f(red, green, blue, alpha);
   }

   public static int getGreen(int argb) {
      return argb >> 8 & 0xFF;
   }

   public static int saturatedPingPongGradient(int startColor, int endColor, int phaseOffset, int millisPerStep) {
      int pingPongStep = (int)((System.currentTimeMillis() / millisPerStep + phaseOffset) % В7Ы);
      pingPongStep = (pingPongStep > 180 ? 360 - pingPongStep : pingPongStep) + 180;
      int blendedColor = blendArgb(startColor, endColor, Mth.clamp(pingPongStep / В7l - 1.0F, 0.0F, 1.0F));
      float[] rgba = toRgbaArray(blendedColor);
      float[] hsb = Color.RGBtoHSB((int)(rgba[0] * В78), (int)(rgba[1] * В75), (int)(rgba[2] * В7в), null);
      hsb[1] *= В7ъ;
      hsb[1] = Math.min(hsb[1], 1.0F);
      return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
   }

   public static float[] toRgbaArray(int argb) {
      return new float[]{(argb >> 16 & 0xFF) / В7Г, (argb >> 8 & 0xFF) / В7л, (argb & 0xFF) / В7э, (argb >> 24 & 0xFF) / В7ю};
   }

   public static int lerpArgb(int startColor, int endColor, float fraction) {
      Vector4f startChannels = toRgbaVector(startColor).mul(В79);
      Vector4f endChannels = toRgbaVector(endColor).mul(В7о);
      return packArgb(
         (int)AimGeometryMath.lerpFloat(startChannels.x, endChannels.x, fraction),
         (int)AimGeometryMath.lerpFloat(startChannels.y, endChannels.y, fraction),
         (int)AimGeometryMath.lerpFloat(startChannels.z, endChannels.z, fraction),
         AimGeometryMath.lerpFloat(startChannels.w, endChannels.w, fraction)
      );
   }

   @Generated
   private ColorMath() {
      throw new UnsupportedOperationException(В76);
   }

   public static int packArgb(int red, int green, int blue, double alpha) {
      return (int)alpha << 24 | red << 16 | green << 8 | blue;
   }

   public static int getAnimatedThemeColor(int phaseOffset) {
      if (ThemeEditor.isRainbowEnabled()) {
         float hue = ((float)System.currentTimeMillis() / В7т + phaseOffset * 16) % В7а / В7Ю;
         if (hue < 0.0F) {
            hue++;
         }

         return Color.HSBtoRGB(hue, В7Ч, 1.0F) | В7Л;
      } else {
         return oscillatingGradient(ThemeEditor.getBaseColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getBaseColorArgb(ThemeColorSlot.MAIN_TWO), phaseOffset * В7诶 / В71, 255);
      }
   }

   public static int parseHexColor(String input) {
      String hex = input.trim();
      if (hex.startsWith(В77)) {
         hex = hex.substring(1);
      }

      if (hex.startsWith(В7Р) || hex.startsWith(В73)) {
         hex = hex.substring(2);
      }

      if (hex.length() == 3 || hex.length() == 4) {
         char redNibble = hex.charAt(0);
         char greenNibble = hex.charAt(1);
         char blueNibble = hex.charAt(2);
         char alphaNibble = hex.length() == 4 ? hex.charAt(3) : 'F';
         hex = "" + redNibble + redNibble + greenNibble + greenNibble + blueNibble + blueNibble + alphaNibble + alphaNibble;
      }

      if (hex.length() == 6) {
         int rgbRed = Integer.parseInt(hex.substring(0, 2), 16);
         int rgbGreen = Integer.parseInt(hex.substring(2, 4), 16);
         int rgbBlue = Integer.parseInt(hex.substring(4, 6), 16);
         return packArgb(rgbRed, rgbGreen, rgbBlue, В7р);
      } else if (hex.length() == 8) {
         int rgbaRed = Integer.parseInt(hex.substring(0, 2), 16);
         int rgbaGreen = Integer.parseInt(hex.substring(2, 4), 16);
         int rgbaBlue = Integer.parseInt(hex.substring(4, 6), 16);
         int rgbaAlpha = Integer.parseInt(hex.substring(6, 8), 16);
         return packArgb(rgbaRed, rgbaGreen, rgbaBlue, rgbaAlpha);
      } else {
         throw new IllegalArgumentException("Unsupported hex format: " + input);
      }
   }

   public static int withAlpha(int argb, float opacity) {
      int alphaChannel = Math.min(255, Math.max(0, (int)(opacity * В7O)));
      return packArgb(getRed(argb), getGreen(argb), getBlue(argb), alphaChannel);
   }

   public static int oscillatingGradient(int startColor, int endColor, float phaseOffset, int alpha) {
      if ((startColor & В7Ь) == (endColor & В7衣)) {
         return packArgb(getRed(startColor), getGreen(startColor), getBlue(startColor), alpha);
      }

      float phase = ((float)(System.currentTimeMillis() % В7ш) / В7И + phaseOffset) % 1.0F;
      if (phase < 0.0F) {
         phase++;
      }

      float blendFactor = В7ь - В7ж * (float)Math.cos(phase * В7弟 * В7б);
      int blendedColor = blendArgb(startColor, endColor, blendFactor);
      return packArgb(getRed(blendedColor), getGreen(blendedColor), getBlue(blendedColor), alpha);
   }

   public static int getBlue(int argb) {
      return argb & 0xFF;
   }

   public static int getRed(int argb) {
      return argb >> 16 & 0xFF;
   }

   public static int packRgb(int red, int green, int blue) {
      return В7и | red << 16 | green << 8 | blue;
   }

   public static net.minecraft.network.chat.Component colorizeWithTheme(String text) {
      MutableComponent component = net.minecraft.network.chat.Component.empty();

      for (int charIndex = 0; charIndex < text.length(); charIndex++) {
         char character = text.charAt(charIndex);
         MutableComponent styledCharacter = net.minecraft.network.chat.Component.literal(String.valueOf(character))
            .setStyle(Style.EMPTY.withColor(new Color(getAnimatedThemeColor(charIndex)).getRGB()));
         component.append(styledCharacter);
      }

      return component;
   }

   public static int blendArgb(int startColor, int endColor, float fraction) {
      float[] startRgba = toRgbaArray(startColor);
      float[] endRgba = toRgbaArray(endColor);
      return packArgb(
         (int)AimGeometryMath.lerpFloat(startRgba[0] * В7М, endRgba[0] * В7Н, fraction),
         (int)AimGeometryMath.lerpFloat(startRgba[1] * В7н, endRgba[1] * В7Ц, fraction),
         (int)AimGeometryMath.lerpFloat(startRgba[2] * В7ч, endRgba[2] * В7Э, fraction),
         (int)AimGeometryMath.lerpFloat(startRgba[3] * В7必, endRgba[3] * В7щ, fraction)
      );
   }

   public static int scaleArgb(int argb, float multiplier) {
      int red = (int)(getRed(argb) * multiplier);
      int green = (int)(getGreen(argb) * multiplier);
      int blue = (int)(getBlue(argb) * multiplier);
      int alpha = (int)(getAlpha(argb) * multiplier);
      return packArgb(red, green, blue, alpha);
   }
}
