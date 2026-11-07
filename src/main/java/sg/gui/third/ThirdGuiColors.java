package sg.gui.third;

/**
 * Original Third palette, independent of the First GUI's selected theme.
 */
public final class ThirdGuiColors {
   public static final int ACCENT = 0xFFE4294B;
   public static final int PANEL = 0xF2101012;
   public static final int CARD = 0xFF18181B;
   public static final int BORDER = 0x12FFFFFF;
   public static final int TEXT = 0xFFEDEDEF;
   public static final int DESC = 0xFF6F6F76;
   public static final int PILL = 0xFF26262A;
   public static final int PILL_TEXT = 0xFF85858C;
   public static final int CHECK_OFF = 0xFF222226;
   public static final int TRACK = 0x1EFFFFFF;
   public static final int SLIDER_FILL = 0xFF8A1B33;
   public static final int DOCK = 0xFF1A1A1D;
   public static final int ICON_IDLE = 0xFF86868C;
   public static final int RED = 0xFFFF6B85;
   public static final int GREEN = 0xFF4ADE80;
   public static final int WHITE = 0xFFFFFFFF;

   private static final float TRANSPARENCY_SCALE = 255.0F;

   private ThirdGuiColors() {
   }

   public static int accent() { return ACCENT; }
   public static int panel() { return PANEL; }
   public static int card() { return CARD; }
   public static int border() { return BORDER; }
   public static int text() { return TEXT; }
   public static int description() { return DESC; }
   public static int checkOff() { return CHECK_OFF; }
   public static int sliderFill() { return SLIDER_FILL; }
   public static int dock() { return DOCK; }

   public static int alpha(int color, float multiplier) {
      float clamped = Math.clamp(multiplier, 0.0F, 1.0F);
      int scaled = Math.round((color >>> 24 & 0xFF) * clamped);
      return scaled << 24 | color & 0xFFFFFF;
   }

   public static int lerp(int from, int to, float progress) {
      float clamped = Math.clamp(progress, 0.0F, 1.0F);
      int fromAlpha = from >>> 24 & 0xFF;
      int fromRed = from >> 16 & 0xFF;
      int fromGreen = from >> 8 & 0xFF;
      int fromBlue = from & 0xFF;
      int toAlpha = to >>> 24 & 0xFF;
      int toRed = to >> 16 & 0xFF;
      int toGreen = to >> 8 & 0xFF;
      int toBlue = to & 0xFF;
      return Math.round(fromAlpha + (toAlpha - fromAlpha) * clamped) << 24
         | Math.round(fromRed + (toRed - fromRed) * clamped) << 16
         | Math.round(fromGreen + (toGreen - fromGreen) * clamped) << 8
         | Math.round(fromBlue + (toBlue - fromBlue) * clamped);
   }

   public static float scale(float value) {
      return value / TRANSPARENCY_SCALE;
   }
}
