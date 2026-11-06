package sg.theme;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import sg.util.time.ColorMath;

public final class ThemePresets {
   private static final LinkedHashMap<String, int[]> palettes = new LinkedHashMap<>();
   private static final String OCEAN_PRESET_NAME = "Ocean";
   private static final double OCEAN_MAIN_ALPHA = 255.0;
   private static final double OCEAN_MAIN_TWO_ALPHA = 255.0;
   private static final double OCEAN_OUTLINE_ALPHA = 13.0;
   private static final double OCEAN_BACKGROUND_ALPHA = 200.0;
   private static final double OCEAN_SHADOW_ALPHA = 170.0;
   private static final double OCEAN_LINE_ALPHA = 13.0;
   private static final double OCEAN_SCROLL_ALPHA = 130.0;
   private static final double OCEAN_ACTIVE_STATE_ALPHA = 255.0;
   private static final double OCEAN_DEACTIVE_STATE_ALPHA = 255.0;
   private static final double OCEAN_CIRCLE_ALPHA = 255.0;
   private static final double OCEAN_SELECTED_ALPHA = 200.0;
   private static final double OCEAN_ACTIVE_TEXT_ALPHA = 255.0;
   private static final double OCEAN_DEACTIVE_TEXT_ALPHA = 255.0;
   private static final double OCEAN_OPEN_SETTING_TEXT_ALPHA = 255.0;
   private static final double OCEAN_DEOPEN_SETTING_TEXT_ALPHA = 255.0;
   private static final double OCEAN_TEXT_ALPHA = 255.0;
   private static final double OCEAN_SUR_TEXT_ALPHA = 255.0;
   private static final double OCEAN_DESELECT_CATEGORY_TEXT_ALPHA = 255.0;
   private static final double OCEAN_CATEGORY_TEXT_ALPHA = 255.0;
   private static final double OCEAN_CATEGORY_SURTEST_ALPHA = 255.0;
   private static final double OCEAN_WINDOW_TEXT_ALPHA = 255.0;
   private static final double OCEAN_WINDOW_SUR_TEXT_ALPHA = 255.0;
   private static final double OCEAN_WINDOW_BACKGROUND_ALPHA = 175.0;
   private static final double OCEAN_WINDOW_BACKGROUND_TWO_ALPHA = 150.0;
   private static final double OCEAN_WINDOW_SHADOW_ALPHA = 175.0;

   static {
      registerPalette(
         OCEAN_PRESET_NAME,
         new int[]{
            ColorMath.packArgb(128, 179, 255, OCEAN_MAIN_ALPHA),
            ColorMath.packArgb(128, 179, 255, OCEAN_MAIN_TWO_ALPHA),
            ColorMath.packArgb(255, 255, 255, OCEAN_OUTLINE_ALPHA),
            ColorMath.packArgb(15, 19, 28, OCEAN_BACKGROUND_ALPHA),
            ColorMath.packArgb(0, 0, 0, OCEAN_SHADOW_ALPHA),
            ColorMath.packArgb(255, 255, 255, OCEAN_LINE_ALPHA),
            ColorMath.packArgb(128, 179, 255, OCEAN_SCROLL_ALPHA),
            ColorMath.packArgb(128, 179, 255, OCEAN_ACTIVE_STATE_ALPHA),
            ColorMath.packArgb(5, 8, 15, OCEAN_DEACTIVE_STATE_ALPHA),
            ColorMath.packArgb(167, 207, 255, OCEAN_CIRCLE_ALPHA),
            ColorMath.packArgb(31, 32, 40, OCEAN_SELECTED_ALPHA),
            ColorMath.packArgb(206, 207, 210, OCEAN_ACTIVE_TEXT_ALPHA),
            ColorMath.packArgb(133, 136, 142, OCEAN_DEACTIVE_TEXT_ALPHA),
            ColorMath.packArgb(133, 136, 142, OCEAN_OPEN_SETTING_TEXT_ALPHA),
            ColorMath.packArgb(236, 236, 237, OCEAN_DEOPEN_SETTING_TEXT_ALPHA),
            ColorMath.packArgb(206, 207, 210, OCEAN_TEXT_ALPHA),
            ColorMath.packArgb(114, 115, 121, OCEAN_SUR_TEXT_ALPHA),
            ColorMath.packArgb(114, 115, 121, OCEAN_DESELECT_CATEGORY_TEXT_ALPHA),
            ColorMath.packArgb(255, 255, 255, OCEAN_CATEGORY_TEXT_ALPHA),
            ColorMath.packArgb(95, 102, 116, OCEAN_CATEGORY_SURTEST_ALPHA),
            ColorMath.packArgb(255, 255, 255, OCEAN_WINDOW_TEXT_ALPHA),
            ColorMath.packArgb(195, 195, 195, OCEAN_WINDOW_SUR_TEXT_ALPHA),
            ColorMath.packArgb(15, 19, 28, OCEAN_WINDOW_BACKGROUND_ALPHA),
            ColorMath.packArgb(15, 19, 28, OCEAN_WINDOW_BACKGROUND_TWO_ALPHA),
            ColorMath.packArgb(15, 19, 28, OCEAN_WINDOW_SHADOW_ALPHA)
         }
      );
   }

   public static Map<String, int[]> getBuiltinPalettes() {
      return Collections.unmodifiableMap(palettes);
   }

   private ThemePresets() {
   }

   private static void registerPalette(String name, int[] colors) {
      palettes.put(name, colors);
   }
}
