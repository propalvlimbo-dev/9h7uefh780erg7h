package sg.theme;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import sg.util.time.ColorMath;

public final class ThemePresetCatalog {
   private static final List<ThemePresetCatalog.ThemePreset> presets = new ArrayList<>();
   private static final double SOLID_COLOR_ALPHA = 255.0;
   private static final double RAINBOW_PRIMARY_ALPHA = 255.0;
   private static final double RAINBOW_SECONDARY_ALPHA = 255.0;
   private static final double DUAL_PRIMARY_ALPHA = 255.0;
   private static final double DUAL_SECONDARY_ALPHA = 255.0;
   private static final String RAINBOW_PRESET_NAME_MATCH = "Радужная";
   private static final float RAINBOW_SATURATION = 0.85F;
   private static final float SECONDARY_RAINBOW_HUE_OFFSET = 0.16F;
   private static final float CIRCLE_RAINBOW_HUE_OFFSET = 0.08F;
   private static final float SCROLL_RAINBOW_HUE_OFFSET = 0.1F;
   private static final long RAINBOW_PERIOD_MILLIS = 5000L;
   private static final float RAINBOW_PERIOD_DIVISOR = 5000.0F;
   private static final double BACKGROUND_ALPHA = 200.0;
   private static final double WINDOW_BACKGROUND_AND_SHADOW_ALPHA = 175.0;
   private static final double WINDOW_BACKGROUND_TWO_ALPHA = 150.0;
   private static final double SHADOW_ALPHA = 170.0;
   private static final double DEACTIVE_STATE_ALPHA = 255.0;
   private static final double SELECTED_ALPHA = 200.0;
   private static final double CIRCLE_ALPHA = 255.0;
   private static final double OUTLINE_ALPHA = 13.0;
   private static final double LINE_ALPHA = 13.0;
   private static final double SCROLL_ALPHA = 130.0;
   private static final double ACTIVE_TEXT_ALPHA = 255.0;
   private static final double DEACTIVE_TEXT_ALPHA = 255.0;
   private static final double OPEN_SETTING_TEXT_ALPHA = 255.0;
   private static final double DEOPEN_SETTING_TEXT_ALPHA = 255.0;
   private static final double TEXT_ALPHA = 255.0;
   private static final double SUR_TEXT_ALPHA = 255.0;
   private static final double DESELECT_CATEGORY_TEXT_ALPHA = 255.0;
   private static final double CATEGORY_TEXT_ALPHA = 255.0;
   private static final double CATEGORY_SURTEST_ALPHA = 255.0;
   private static final double WINDOW_TEXT_ALPHA = 255.0;
   private static final double WINDOW_SUR_TEXT_ALPHA = 255.0;
   private static final String SCARLET_PRESET_NAME = "Алый";
   private static final String CORAL_PRESET_NAME = "Коралл";
   private static final String AMBER_PRESET_NAME = "Янтарь";
   private static final String LIME_PRESET_NAME = "Лайм";
   private static final String EMERALD_PRESET_NAME = "Изумруд";
   private static final String MINT_PRESET_NAME = "Мята";
   private static final String TURQUOISE_PRESET_NAME = "Бирюза";
   private static final String SKY_PRESET_NAME = "Небо";
   private static final String AZURE_PRESET_NAME = "Лазурь";
   private static final String INDIGO_PRESET_NAME = "Индиго";
   private static final String VIOLET_PRESET_NAME = "Фиалка";
   private static final String ORCHID_PRESET_NAME = "Орхидея";
   private static final String ROSE_PRESET_NAME = "Роза";
   private static final String STEEL_PRESET_NAME = "Сталь";
   private static final String SNOW_PRESET_NAME = "Снег";
   private static final String RUBY_PRESET_NAME = "Рубин";
   private static final String PEACH_PRESET_NAME = "Персик";
   private static final String GOLD_PRESET_NAME = "Золото";
   private static final String OLIVE_PRESET_NAME = "Олива";
   private static final String AQUAMARINE_PRESET_NAME = "Аквамарин";
   private static final String COBALT_PRESET_NAME = "Кобальт";
   private static final String PLUM_PRESET_NAME = "Слива";
   private static final String FUCHSIA_PRESET_NAME = "Фуксия";
   private static final String CHOCOLATE_PRESET_NAME = "Шоколад";
   private static final String GRAPHITE_PRESET_NAME = "Графит";
   private static final String RAINBOW_PRESET_NAME = "Радужная";
   private static final String BLUE_RED_DUAL_PRESET_NAME = "Синий + Красный";
   private static final String OCEAN_SUNSET_DUAL_PRESET_NAME = "Океан + Закат";
   private static final String ICE_FLAME_DUAL_PRESET_NAME = "Лед + Пламя";
   private static final String MINT_RASPBERRY_DUAL_PRESET_NAME = "Мята + Малина";
   private static final String LAVENDER_PEACH_DUAL_PRESET_NAME = "Лаванда + Персик";
   private static final String EMERALD_GOLD_DUAL_PRESET_NAME = "Изумруд + Золото";
   private static final String NIGHT_NEON_DUAL_PRESET_NAME = "Ночь + Неон";
   private static final String SAKURA_INDIGO_DUAL_PRESET_NAME = "Сакура + Индиго";
   private static final String CYAN_PURPLE_DUAL_PRESET_NAME = "Циан + Пурпур";
   private static final String LIME_ULTRA_DUAL_PRESET_NAME = "Лайм + Ультра";
   private static final String COBALT_AMBER_DUAL_PRESET_NAME = "Кобальт + Янтарь";
   private static final String WINE_ICE_DUAL_PRESET_NAME = "Вино + Лёд";
   private static final String FOREST_DAWN_DUAL_PRESET_NAME = "Лес + Заря";
   private static final String DEPTH_CORAL_DUAL_PRESET_NAME = "Глубина + Коралл";
   private static final String VIOLET_TURQUOISE_DUAL_PRESET_NAME = "Фиолет + Бирюза";
   private static final String RUBY_ICE_DUAL_PRESET_NAME = "Рубин + Лёд";
   private static final String MANGO_INDIGO_DUAL_PRESET_NAME = "Манго + Индиго";
   private static final String OLIVE_CORAL_DUAL_PRESET_NAME = "Олива + Коралл";
   private static final String BLUEBERRY_LEMON_DUAL_PRESET_NAME = "Черника + Лимон";
   private static final String GRAPHITE_NEON_DUAL_PRESET_NAME = "Графит + Неон";
   private static final String CHOCOLATE_MINT_DUAL_PRESET_NAME = "Шоколад + Мята";
   private static final String FUCHSIA_CYAN_DUAL_PRESET_NAME = "Фуксия + Циан";
   private static final String GOLD_VIOLET_DUAL_PRESET_NAME = "Золото + Фиолет";
   private static final String AQUA_ROSE_DUAL_PRESET_NAME = "Аква + Роза";
   private static final String PLUM_AMBER_DUAL_PRESET_NAME = "Слива + Янтарь";

   public static List<ThemePresetCatalog.ThemePreset> getPresets() {
      return Collections.unmodifiableList(presets);
   }

   public static List<ThemePresetCatalog.ThemePreset> getPresetsByMode(ThemePresetCatalog.ThemeMode mode) {
      ArrayList matchingPresets = new ArrayList();

      for (ThemePresetCatalog.ThemePreset preset : presets) {
         if (preset.getMode() == mode) {
            matchingPresets.add(preset);
         }
      }

      return matchingPresets;
   }

   private static void registerDualPreset(String name, int primaryRed, int primaryGreen, int primaryBlue, int secondaryRed, int secondaryGreen, int secondaryBlue) {
      presets.add(
         new ThemePresetCatalog.ThemePreset(
            name, ThemePresetCatalog.ThemeMode.DUAL, ColorMath.packArgb(primaryRed, primaryGreen, primaryBlue, DUAL_PRIMARY_ALPHA), ColorMath.packArgb(secondaryRed, secondaryGreen, secondaryBlue, DUAL_SECONDARY_ALPHA)
         )
      );
   }

   private static void registerSolidPreset(String name, int red, int green, int blue) {
      int color = ColorMath.packArgb(red, green, blue, SOLID_COLOR_ALPHA);
      presets.add(new ThemePresetCatalog.ThemePreset(name, ThemePresetCatalog.ThemeMode.SOLID, color, color));
   }

   private static int clampInt(int value, int minimum, int maximum) {
      return Math.max(minimum, Math.min(maximum, value));
   }

   private static void registerRainbowPreset(String name) {
      presets.add(
         new ThemePresetCatalog.ThemePreset(
            name, ThemePresetCatalog.ThemeMode.SOLID, ColorMath.packArgb(255, 90, 160, RAINBOW_PRIMARY_ALPHA), ColorMath.packArgb(80, 210, 255, RAINBOW_SECONDARY_ALPHA), true
         )
      );
   }

   private static int[] buildPalette(int primaryColor, int secondaryColor) {
      int primaryRed = ColorMath.getRed(primaryColor);
      int primaryGreen = ColorMath.getGreen(primaryColor);
      int primaryBlue = ColorMath.getBlue(primaryColor);
      int backgroundRed = clampInt(8 + primaryRed / 14, 8, 40);
      int backgroundGreen = clampInt(10 + primaryGreen / 14, 10, 42);
      int backgroundBlue = clampInt(14 + primaryBlue / 12, 14, 48);
      int backgroundColor = ColorMath.packArgb(backgroundRed, backgroundGreen, backgroundBlue, BACKGROUND_ALPHA);
      int windowBackgroundAndShadowColor = ColorMath.packArgb(backgroundRed, backgroundGreen, backgroundBlue, WINDOW_BACKGROUND_AND_SHADOW_ALPHA);
      int secondaryWindowBackgroundColor = ColorMath.packArgb(backgroundRed, backgroundGreen, backgroundBlue, WINDOW_BACKGROUND_TWO_ALPHA);
      int shadowColor = ColorMath.packArgb(0, 0, 0, SHADOW_ALPHA);
      int inactiveStateColor = ColorMath.packArgb(clampInt(backgroundRed - 4, 4, 30), clampInt(backgroundGreen - 4, 4, 30), clampInt(backgroundBlue - 4, 4, 32), DEACTIVE_STATE_ALPHA);
      int selectedColor = ColorMath.packArgb(clampInt(backgroundRed + 16, 20, 55), clampInt(backgroundGreen + 14, 20, 55), clampInt(backgroundBlue + 18, 24, 60), SELECTED_ALPHA);
      int circleColor = ColorMath.packArgb(clampInt(primaryRed + 35, 0, 255), clampInt(primaryGreen + 30, 0, 255), clampInt(primaryBlue + 30, 0, 255), CIRCLE_ALPHA);
      return new int[]{
         primaryColor,
         secondaryColor,
         ColorMath.packArgb(255, 255, 255, OUTLINE_ALPHA),
         backgroundColor,
         shadowColor,
         ColorMath.packArgb(255, 255, 255, LINE_ALPHA),
         ColorMath.packArgb(primaryRed, primaryGreen, primaryBlue, SCROLL_ALPHA),
         primaryColor,
         inactiveStateColor,
         circleColor,
         selectedColor,
         ColorMath.packArgb(206, 207, 210, ACTIVE_TEXT_ALPHA),
         ColorMath.packArgb(133, 136, 142, DEACTIVE_TEXT_ALPHA),
         ColorMath.packArgb(133, 136, 142, OPEN_SETTING_TEXT_ALPHA),
         ColorMath.packArgb(236, 236, 237, DEOPEN_SETTING_TEXT_ALPHA),
         ColorMath.packArgb(206, 207, 210, TEXT_ALPHA),
         ColorMath.packArgb(114, 115, 121, SUR_TEXT_ALPHA),
         ColorMath.packArgb(114, 115, 121, DESELECT_CATEGORY_TEXT_ALPHA),
         ColorMath.packArgb(255, 255, 255, CATEGORY_TEXT_ALPHA),
         ColorMath.packArgb(95, 102, 116, CATEGORY_SURTEST_ALPHA),
         ColorMath.packArgb(255, 255, 255, WINDOW_TEXT_ALPHA),
         ColorMath.packArgb(195, 195, 195, WINDOW_SUR_TEXT_ALPHA),
         windowBackgroundAndShadowColor,
         secondaryWindowBackgroundColor,
         windowBackgroundAndShadowColor
      };
   }

   public static int getRainbowColor(ThemeColorSlot slot, int alpha) {
      float hueOffset = switch (slot) {
         case MAIN_TWO -> SECONDARY_RAINBOW_HUE_OFFSET;
         case CIRCLE -> CIRCLE_RAINBOW_HUE_OFFSET;
         case SCROLL -> SCROLL_RAINBOW_HUE_OFFSET;
         default -> 0.0F;
      };
      return getRainbowColor(hueOffset, alpha);
   }

   public static int getRainbowColor(float hueOffset, int alpha) {
      float hue = (getRainbowHue() + hueOffset) % 1.0F;
      if (hue < 0.0F) {
         hue++;
      }

      int rgb = Color.HSBtoRGB(hue, RAINBOW_SATURATION, 1.0F);
      return ColorMath.packArgb(ColorMath.getRed(rgb), ColorMath.getGreen(rgb), ColorMath.getBlue(rgb), alpha);
   }

   private ThemePresetCatalog() {
   }

   public static boolean isRainbowPresetName(String name) {
      return name != null && RAINBOW_PRESET_NAME_MATCH.equalsIgnoreCase(name.trim());
   }

   static {
      registerSolidPreset(SCARLET_PRESET_NAME, 220, 60, 60);
      registerSolidPreset(CORAL_PRESET_NAME, 255, 112, 96);
      registerSolidPreset(AMBER_PRESET_NAME, 245, 166, 35);
      registerSolidPreset(LIME_PRESET_NAME, 140, 220, 70);
      registerSolidPreset(EMERALD_PRESET_NAME, 46, 204, 113);
      registerSolidPreset(MINT_PRESET_NAME, 80, 220, 180);
      registerSolidPreset(TURQUOISE_PRESET_NAME, 45, 190, 200);
      registerSolidPreset(SKY_PRESET_NAME, 100, 180, 255);
      registerSolidPreset(AZURE_PRESET_NAME, 64, 120, 255);
      registerSolidPreset(INDIGO_PRESET_NAME, 110, 90, 255);
      registerSolidPreset(VIOLET_PRESET_NAME, 170, 100, 255);
      registerSolidPreset(ORCHID_PRESET_NAME, 220, 100, 200);
      registerSolidPreset(ROSE_PRESET_NAME, 255, 105, 150);
      registerSolidPreset(STEEL_PRESET_NAME, 150, 165, 185);
      registerSolidPreset(SNOW_PRESET_NAME, 230, 235, 245);
      registerSolidPreset(RUBY_PRESET_NAME, 190, 30, 70);
      registerSolidPreset(PEACH_PRESET_NAME, 255, 160, 110);
      registerSolidPreset(GOLD_PRESET_NAME, 230, 190, 50);
      registerSolidPreset(OLIVE_PRESET_NAME, 140, 170, 50);
      registerSolidPreset(AQUAMARINE_PRESET_NAME, 50, 210, 190);
      registerSolidPreset(COBALT_PRESET_NAME, 40, 80, 210);
      registerSolidPreset(PLUM_PRESET_NAME, 130, 60, 150);
      registerSolidPreset(FUCHSIA_PRESET_NAME, 255, 50, 180);
      registerSolidPreset(CHOCOLATE_PRESET_NAME, 140, 85, 50);
      registerSolidPreset(GRAPHITE_PRESET_NAME, 90, 100, 115);
      registerRainbowPreset(RAINBOW_PRESET_NAME);
      registerDualPreset(BLUE_RED_DUAL_PRESET_NAME, 80, 150, 255, 230, 70, 70);
      registerDualPreset(OCEAN_SUNSET_DUAL_PRESET_NAME, 64, 170, 220, 255, 120, 70);
      registerDualPreset(ICE_FLAME_DUAL_PRESET_NAME, 140, 210, 255, 255, 90, 50);
      registerDualPreset(MINT_RASPBERRY_DUAL_PRESET_NAME, 70, 220, 180, 235, 70, 120);
      registerDualPreset(LAVENDER_PEACH_DUAL_PRESET_NAME, 170, 130, 255, 255, 170, 120);
      registerDualPreset(EMERALD_GOLD_DUAL_PRESET_NAME, 40, 190, 120, 245, 190, 50);
      registerDualPreset(NIGHT_NEON_DUAL_PRESET_NAME, 90, 110, 255, 0, 245, 180);
      registerDualPreset(SAKURA_INDIGO_DUAL_PRESET_NAME, 255, 140, 180, 100, 90, 230);
      registerDualPreset(CYAN_PURPLE_DUAL_PRESET_NAME, 40, 210, 230, 200, 70, 220);
      registerDualPreset(LIME_ULTRA_DUAL_PRESET_NAME, 160, 240, 60, 90, 80, 255);
      registerDualPreset(COBALT_AMBER_DUAL_PRESET_NAME, 50, 100, 230, 255, 170, 40);
      registerDualPreset(WINE_ICE_DUAL_PRESET_NAME, 180, 50, 90, 150, 210, 255);
      registerDualPreset(FOREST_DAWN_DUAL_PRESET_NAME, 50, 160, 90, 255, 130, 80);
      registerDualPreset(DEPTH_CORAL_DUAL_PRESET_NAME, 40, 80, 180, 255, 110, 100);
      registerDualPreset(VIOLET_TURQUOISE_DUAL_PRESET_NAME, 150, 80, 255, 50, 210, 200);
      registerDualPreset(RUBY_ICE_DUAL_PRESET_NAME, 190, 30, 70, 160, 210, 255);
      registerDualPreset(MANGO_INDIGO_DUAL_PRESET_NAME, 255, 160, 40, 90, 80, 230);
      registerDualPreset(OLIVE_CORAL_DUAL_PRESET_NAME, 130, 165, 50, 255, 110, 100);
      registerDualPreset(BLUEBERRY_LEMON_DUAL_PRESET_NAME, 80, 70, 180, 235, 220, 60);
      registerDualPreset(GRAPHITE_NEON_DUAL_PRESET_NAME, 90, 100, 120, 0, 255, 170);
      registerDualPreset(CHOCOLATE_MINT_DUAL_PRESET_NAME, 140, 85, 50, 70, 220, 180);
      registerDualPreset(FUCHSIA_CYAN_DUAL_PRESET_NAME, 255, 50, 180, 40, 220, 230);
      registerDualPreset(GOLD_VIOLET_DUAL_PRESET_NAME, 230, 190, 50, 150, 80, 255);
      registerDualPreset(AQUA_ROSE_DUAL_PRESET_NAME, 40, 200, 200, 255, 105, 150);
      registerDualPreset(PLUM_AMBER_DUAL_PRESET_NAME, 130, 60, 150, 245, 166, 35);
   }

   public static float getRainbowHue() {
      return (float)(System.currentTimeMillis() % RAINBOW_PERIOD_MILLIS) / RAINBOW_PERIOD_DIVISOR;
   }

   public static final class ThemePreset {
      private final @com.google.gson.annotations.SerializedName("Яз")  String name;
      private final @com.google.gson.annotations.SerializedName("В")  ThemePresetCatalog.ThemeMode mode;
      private final @com.google.gson.annotations.SerializedName("必")  int[] colors;
      private final @com.google.gson.annotations.SerializedName("ЪЩ")  int primaryColor;
      private final @com.google.gson.annotations.SerializedName("Ъу")  int secondaryColor;
      private final @com.google.gson.annotations.SerializedName("Аъ")  boolean rainbow;

      @Generated
      public int[] getColors() {
         return this.colors;
      }

      private ThemePreset(String name, ThemePresetCatalog.ThemeMode mode, int primaryColor, int secondaryColor, boolean rainbow) {
         this.name = name;
         this.mode = mode;
         this.primaryColor = primaryColor;
         this.secondaryColor = secondaryColor;
         this.rainbow = rainbow;
         this.colors = ThemePresetCatalog.buildPalette(primaryColor, secondaryColor);
      }

      private ThemePreset(String name, ThemePresetCatalog.ThemeMode mode, int primaryColor, int secondaryColor) {
         this(name, mode, primaryColor, secondaryColor, false);
      }

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public ThemePresetCatalog.ThemeMode getMode() {
         return this.mode;
      }

      @Generated
      public int getSecondaryColor() {
         return this.secondaryColor;
      }

      @Generated
      public int getPrimaryColor() {
         return this.primaryColor;
      }

      @Generated
      public boolean isRainbow() {
         return this.rainbow;
      }
   }

   public enum ThemeMode {
      SOLID("Однотонные"),
      DUAL("Двухцветные");

      private final @com.google.gson.annotations.SerializedName("Яд")  String displayName;

      ThemeMode(String displayName) {
         this.displayName = displayName;
      }

      @Generated
      public String getDisplayName() {
         return this.displayName;
      }
   }
}
