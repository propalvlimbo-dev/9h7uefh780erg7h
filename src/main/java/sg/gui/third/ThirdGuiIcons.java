package sg.gui.third;

import java.util.HashMap;
import java.util.Map;
import sg.ec.FontRenderer;
import sg.render.pipeline.UiDrawing;

/**
 * Icon atlas of the catlavanskid gui, loaded through the rose font pipeline.
 * The atlas itself was copied verbatim to assets/rose/fonts/thirdgui_icons.*.
 */
public final class ThirdGuiIcons {
   private static final Map<String, Integer> CODEPOINTS = new HashMap<>();
   private static final String LOGO = "logo";
   private static final String USERS = "users";
   private static final String USER = "user";
   private static final String SERVER = "server";
   private static final String ROCKET = "rocket";
   private static final String ARROWS = "arrows";
   private static final String TIMER = "timer";
   private static final String KEYBOARD = "keyboard";
   private static final String SPARKLE = "sparkle";
   private static final String SWORD = "sword";
   private static final String SNEAKER = "sneaker";
   private static final String DOTS = "dots";
   private static final String FOLDER = "folder";
   private static final String GLOBE = "globe";
   private static final String CARET = "caret";
   private static final String NOTCH = "notch";
   private static final String CHECK = "check";
   private static final String PIN = "pin";
   private static final String DROP = "drop";
   private static final String SHIRT = "shirt";
   private static final String CROWN = "crown";
   private static final String FLASK = "flask";
   private static final String GEAR = "gear";
   private static final String EYE = "eye";
   private static final String BOLT = "bolt";

   static {
      CODEPOINTS.put(LOGO, 57344);
      CODEPOINTS.put(USERS, 57345);
      CODEPOINTS.put(USER, 57346);
      CODEPOINTS.put(SERVER, 57347);
      CODEPOINTS.put(ROCKET, 57348);
      CODEPOINTS.put(ARROWS, 57349);
      CODEPOINTS.put(TIMER, 57350);
      CODEPOINTS.put(KEYBOARD, 57351);
      CODEPOINTS.put(SPARKLE, 57352);
      CODEPOINTS.put(SWORD, 57353);
      CODEPOINTS.put(SNEAKER, 57354);
      CODEPOINTS.put(DOTS, 57355);
      CODEPOINTS.put(FOLDER, 57356);
      CODEPOINTS.put(GLOBE, 57357);
      CODEPOINTS.put(CARET, 57358);
      CODEPOINTS.put(NOTCH, 57359);
      CODEPOINTS.put(CHECK, 57360);
      CODEPOINTS.put(PIN, 57361);
      CODEPOINTS.put(DROP, 57362);
      CODEPOINTS.put(SHIRT, 57363);
      CODEPOINTS.put(CROWN, 57364);
      CODEPOINTS.put(FLASK, 57365);
      CODEPOINTS.put(GEAR, 57366);
      CODEPOINTS.put(EYE, 57367);
      CODEPOINTS.put(BOLT, 57368);
   }

   private ThirdGuiIcons() {
   }

   public static String logo() {
      return LOGO;
   }

   public static String users() {
      return USERS;
   }

   public static String user() {
      return USER;
   }

   public static String server() {
      return SERVER;
   }

   public static String rocket() {
      return ROCKET;
   }

   public static String arrows() {
      return ARROWS;
   }

   public static String timer() {
      return TIMER;
   }

   public static String keyboard() {
      return KEYBOARD;
   }

   public static String sparkle() {
      return SPARKLE;
   }

   public static String sword() {
      return SWORD;
   }

   public static String sneaker() {
      return SNEAKER;
   }

   public static String dots() {
      return DOTS;
   }

   public static String folder() {
      return FOLDER;
   }

   public static String globe() {
      return GLOBE;
   }

   public static String caret() {
      return CARET;
   }

   public static String notch() {
      return NOTCH;
   }

   public static String check() {
      return CHECK;
   }

   public static String pin() {
      return PIN;
   }

   public static String drop() {
      return DROP;
   }

   public static String shirt() {
      return SHIRT;
   }

   public static String crown() {
      return CROWN;
   }

   public static String flask() {
      return FLASK;
   }

   public static String gear() {
      return GEAR;
   }

   public static String eye() {
      return EYE;
   }

   public static String bolt() {
      return BOLT;
   }

   public static FontRenderer font() {
      return ThirdGuiIcons.IconsHolder.FONT;
   }

   /**
    * Match Gfx.iconRot: pen = cx - size/2, baseline = cy + size/2. The base applies
    * planeBounds itself, including the SDF padding; the quad is 4/3 * size, not size.
    */
    public static void draw(String name, float centerX, float centerY, float size, int color) {
      if (LOGO.equals(name)) {
         sg.render.ClientLogo.drawCentered(centerX, centerY, size, color);
         return;
      }
      Integer codepoint = CODEPOINTS.get(name);
      if (codepoint == null) {
         return;
      }

      if (size <= 0.0F) {
         return;
      }
      UiDrawing.drawText(font(), Character.toString(codepoint.intValue()),
         centerX - size * 0.5F, centerY + size * 0.5F - font().getFontFace().getMetrics().getBaselineOffset() * size,
          size, color, 0.0F, 0.5F, 1.0F);
    }

   /** Center by the actual glyph bounds without changing the original dock's icon placement. */
   public static void drawCentered(String name, float centerX, float centerY, float size, int color) {
      if (LOGO.equals(name)) {
         sg.render.ClientLogo.drawCentered(centerX, centerY, size, color);
         return;
      }
      Integer codepoint = CODEPOINTS.get(name);
      if (codepoint == null || size <= 0.0F) return;
      String glyph = Character.toString(codepoint.intValue());
      UiDrawing.drawText(font(), glyph, centerX - UiDrawing.getTextWidth(font(), glyph, size, 1.0F) * 0.5F,
         UiDrawing.alignTextCenterY(font(), glyph, centerY, size), size, color, 0.0F, 0.5F, 1.0F);
   }

   private static final class IconsHolder {
      private static final FontRenderer FONT = new FontRenderer("thirdgui_icons", true);
   }
}
