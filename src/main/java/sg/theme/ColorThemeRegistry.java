package sg.theme;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import sg.theme.ThemePresets;
import sg.theme.ThemeColorSlot;
import sg.theme.ThemePresetCatalog;
import sg.settings.ColorSetting;

public class ColorThemeRegistry extends CopyOnWriteArrayList<ColorTheme> {
   private final @com.google.gson.annotations.SerializedName("Х")  EnumMap<ThemeColorSlot, ColorSetting> colorSettings = new EnumMap<>(ThemeColorSlot.class);
   @SerializedName("_")
   private final Map<String, int[]> builtinPresetColors = new LinkedHashMap<>();
   private @com.google.gson.annotations.SerializedName("В")  ColorTheme selectedListedTheme;
   private @com.google.gson.annotations.SerializedName("Я")  ColorTheme activePresetOverride;
   private @com.google.gson.annotations.SerializedName("с6")  boolean presetRainbowEnabled;
   @SerializedName("4")
   private int[] lastCommittedColorsSnapshot;
   private @com.google.gson.annotations.SerializedName("с_")  boolean restoringState;
   private @com.google.gson.annotations.SerializedName("Д")  Runnable changeListener = () -> {};
   private static final String USER_THEME_AUTHOR = "User";
   private static final String PRESET_THEME_AUTHOR = "Готовые";
   private static final String WRITE_LISTED_THEME_KEY = "listed";
   private static final String WRITE_PRESET_OVERRIDE_KEY = "ready";
   private static final String WRITE_RAINBOW_KEY = "rainbow";
   private static final String WRITE_ACTIVE_THEME_NAME_KEY = "name";
   private static final String WRITE_COLORS_KEY = "colors";
   private static final String READ_PRESET_PRESENT_KEY = "ready";
   private static final String READ_PRESET_ENABLED_KEY = "ready";
   private static final String READ_RAINBOW_PRESENT_KEY = "rainbow";
   private static final String READ_RAINBOW_ENABLED_KEY = "rainbow";
   private static final String READ_ACTIVE_NAME_PRESENT_KEY = "name";
   private static final String READ_ACTIVE_NAME_VALUE_KEY = "name";
   private static final String READ_LISTED_NAME_PRESENT_KEY = "listed";
   private static final String READ_LISTED_NAME_VALUE_KEY = "listed";
   private static final String RESTORED_PRESET_NAME_FALLBACK = "Ready";
   private static final String RESTORED_PRESET_AUTHOR = "Готовые";
   private static final String READ_COLORS_PRESENT_KEY = "colors";
   private static final String READ_COLORS_ARRAY_CHECK_KEY = "colors";
   private static final String READ_COLORS_ARRAY_VALUE_KEY = "colors";
   private static final String IMPORT_EXCLUDED_PRESET_AUTHOR = "Готовые";
   private static final String BUILTIN_THEME_AUTHOR = "Rose";
   private static final String PREFERRED_LISTED_THEME_NAME = "Ocean";
   private static final String REPAIRED_INTERFACE_GROUP = "Interface";
   private static final String APPLIED_INTERFACE_GROUP = "Interface";
   private static final String PREFERRED_FALLBACK_PALETTE_NAME = "Ocean";
   private static final String EXTERNAL_FALLBACK_PALETTE_NAME = "Ocean";
   private static final String LEGACY_PRESET_AUTHOR_TO_REMOVE = "Готовые";

   public synchronized String getSelectedListedThemeName() {
      return this.selectedListedTheme == null ? "" : this.selectedListedTheme.getName();
   }

   public synchronized boolean isUserThemeEditable() {
      return this.activePresetOverride == null && this.selectedListedTheme != null && this.selectedListedTheme.isUserCreated();
   }

   public synchronized void restoreUserThemes(List<ColorTheme> themes, String selectedName) {
      this.restoreUserThemes(themes, selectedName, null);
   }

   public synchronized boolean deleteUserTheme(ColorTheme theme) {
      return theme == null ? false : this.deleteUserTheme(theme.getName());
   }

   public synchronized boolean selectThemeByName(String name) {
      ColorTheme theme = this.findListedTheme(name);
      return theme == null ? false : this.selectTheme(theme);
   }

   private ColorTheme findDefaultListedTheme() {
      ColorTheme preferredTheme = this.findListedTheme(PREFERRED_LISTED_THEME_NAME);
      if (preferredTheme != null && !preferredTheme.isUserCreated()) {
         return preferredTheme;
      }

      for (ColorTheme theme : this) {
         if (!theme.isUserCreated()) {
            return theme;
         }
      }

      return null;
   }

   public synchronized boolean add(ColorTheme theme) {
      if (theme != null && !this.containsThemeName(theme.getName())) {
         boolean added = super.add(theme);
         if (added) {
            this.notifyChanged();
         }

         return added;
      } else {
         return false;
      }
   }

   public synchronized void commitUserThemeColors() {
      if (!this.restoringState && this.activePresetOverride == null && this.selectedListedTheme != null && this.selectedListedTheme.isUserCreated()) {
         int[] colorsSnapshot = this.captureColorsSnapshot();
         this.selectedListedTheme.setColors(colorsSnapshot);
         this.lastCommittedColorsSnapshot = Arrays.copyOf(colorsSnapshot, colorsSnapshot.length);
         this.notifyChanged();
      }
   }

   public synchronized ColorTheme applyPreset(String name, int[] colors, boolean rainbow) {
      String trimmedName = this.trimThemeName(name);
      if (!trimmedName.isBlank() && colors != null) {
         if (this.activePresetOverride == null && this.selectedListedTheme != null && this.selectedListedTheme.isUserCreated()) {
            this.captureSelectedUserThemeColors();
         }

         this.removeIf(listedThemeCandidate -> listedThemeCandidate.isUserCreated() && LEGACY_PRESET_AUTHOR_TO_REMOVE.equals(listedThemeCandidate.getAuthor()));
         this.activePresetOverride = new ColorTheme(trimmedName, PRESET_THEME_AUTHOR, false, colors);
         this.presetRainbowEnabled = rainbow || ThemePresetCatalog.isRainbowPresetName(trimmedName);
         this.applyColorsToSettings(colors);
         this.lastCommittedColorsSnapshot = this.captureColorsSnapshot();
         this.notifyChanged();
         return this.activePresetOverride;
      } else {
         return null;
      }
   }

   public synchronized ColorTheme createUserTheme(String name) {
      return this.createUserTheme(name, USER_THEME_AUTHOR);
   }

   private String trimThemeName(String name) {
      return name == null ? "" : name.trim();
   }

   public synchronized ColorSetting getColorSetting(ThemeColorSlot slot) {
      return this.colorSettings.get(slot);
   }

   public synchronized void loadSelectionState(JsonObject stateJson) {
      if (stateJson != null) {
         this.restoringState = true;

         try {
            boolean hasPresetOverride = stateJson.has(READ_PRESET_PRESENT_KEY) && stateJson.get(READ_PRESET_ENABLED_KEY).getAsBoolean();
            boolean rainbow = stateJson.has(READ_RAINBOW_PRESENT_KEY) && stateJson.get(READ_RAINBOW_ENABLED_KEY).getAsBoolean();
            String activeName = stateJson.has(READ_ACTIVE_NAME_PRESENT_KEY) ? stateJson.get(READ_ACTIVE_NAME_VALUE_KEY).getAsString() : "";
            String listedName = stateJson.has(READ_LISTED_NAME_PRESENT_KEY) ? stateJson.get(READ_LISTED_NAME_VALUE_KEY).getAsString() : "";
            int[] colors = this.readColorsArray(stateJson);
            if (!hasPresetOverride || colors == null) {
               this.activePresetOverride = null;
               this.presetRainbowEnabled = false;
               ColorTheme listedTheme = this.findListedTheme(listedName.isBlank() ? activeName : listedName);
               if (listedTheme == null) {
                  listedTheme = this.findListedTheme(activeName);
               }

               if (listedTheme == null) {
                  listedTheme = this.findDefaultListedTheme();
               }

               this.selectedListedTheme = listedTheme;
               if (colors != null) {
                  this.applyColorsToSettings(colors);
                  if (this.selectedListedTheme != null && this.selectedListedTheme.isUserCreated()) {
                     this.selectedListedTheme.setColors(colors);
                  }
               } else if (this.selectedListedTheme != null) {
                  this.applyColorsToSettings(this.selectedListedTheme.getColors());
               }

               this.lastCommittedColorsSnapshot = this.captureColorsSnapshot();
               return;
            }

            String presetName = this.trimThemeName(activeName.isBlank() ? RESTORED_PRESET_NAME_FALLBACK : activeName);
            this.activePresetOverride = new ColorTheme(presetName, RESTORED_PRESET_AUTHOR, false, colors);
            this.presetRainbowEnabled = rainbow || ThemePresetCatalog.isRainbowPresetName(presetName);
            this.applyColorsToSettings(colors);
            this.lastCommittedColorsSnapshot = this.captureColorsSnapshot();
         } finally {
            this.restoringState = false;
         }
      }
   }

   public synchronized JsonObject saveSelectionState() {
      JsonObject stateJson = new JsonObject();
      stateJson.addProperty(WRITE_LISTED_THEME_KEY, this.getSelectedListedThemeName());
      stateJson.addProperty(WRITE_PRESET_OVERRIDE_KEY, this.activePresetOverride != null);
      stateJson.addProperty(WRITE_RAINBOW_KEY, this.isRainbowEnabled());
      stateJson.addProperty(WRITE_ACTIVE_THEME_NAME_KEY, this.getActiveThemeName());
      JsonArray colorsJson = new JsonArray();

      for (int argb : this.captureColorsSnapshot()) {
         colorsJson.add(argb);
      }

      stateJson.add(WRITE_COLORS_KEY, colorsJson);
      return stateJson;
   }

   public synchronized boolean selectTheme(ColorTheme theme) {
      if (theme == null) {
         return false;
      }

      ColorTheme listedTheme = this.findListedTheme(theme.getName());
      if (listedTheme == null) {
         return false;
      }

      if (this.activePresetOverride == null && this.selectedListedTheme != null && this.selectedListedTheme.isUserCreated()) {
         this.captureSelectedUserThemeColors();
      }

      this.activePresetOverride = null;
      this.presetRainbowEnabled = false;
      this.selectedListedTheme = listedTheme;
      this.applyColorsToSettings(this.selectedListedTheme.getColors());
      this.lastCommittedColorsSnapshot = this.captureColorsSnapshot();
      this.notifyChanged();
      return true;
   }

   public synchronized void commitChangedUserThemeColors() {
      if (!this.restoringState && this.activePresetOverride == null && this.selectedListedTheme != null && this.selectedListedTheme.isUserCreated()) {
         int[] colorsSnapshot = this.captureColorsSnapshot();
         if (!Arrays.equals(this.lastCommittedColorsSnapshot, colorsSnapshot)) {
            this.selectedListedTheme.setColors(colorsSnapshot);
            this.lastCommittedColorsSnapshot = Arrays.copyOf(colorsSnapshot, colorsSnapshot.length);
            this.notifyChanged();
         }
      }
   }

   public synchronized List<String> getListedThemeNames() {
      ArrayList names = new ArrayList();

      for (ColorTheme theme : this) {
         names.add(theme.getName());
      }

      return names;
   }

   public synchronized ColorTheme createUserTheme(String name, String author) {
      String trimmedName = this.trimThemeName(name);
      if (trimmedName.isBlank()) {
         return null;
      }

      if (this.containsThemeName(trimmedName)) {
         return null;
      }

      ColorTheme theme = new ColorTheme(trimmedName, author, true, this.captureColorsSnapshot());
      boolean added = super.add(theme);
      if (!added) {
         return null;
      }

      this.selectTheme(theme);
      return theme;
   }

   public synchronized boolean deleteUserTheme(String name) {
      ColorTheme theme = this.findListedTheme(name);
      if (theme != null && theme.isUserCreated()) {
         boolean wasSelected = this.selectedListedTheme == theme;
         boolean removed = super.remove(theme);
         if (!removed) {
            return false;
         }

         if (wasSelected) {
            this.selectDefaultListedTheme();
         }

         this.notifyChanged();
         return true;
      } else {
         return false;
      }
   }

   public ColorThemeRegistry() {
      for (ThemeColorSlot slot : ThemeColorSlot.values()) {
         this.colorSettings.put(slot, new ColorSetting(slot.getDisplayName(), true, 0));
      }

      this.builtinPresetColors.putAll(ThemePresets.getBuiltinPalettes());
      this.addBuiltinThemes();
      this.selectDefaultListedTheme();
      this.lastCommittedColorsSnapshot = this.captureColorsSnapshot();
   }

   private int[] getFallbackColors() {
      int[] colors = this.builtinPresetColors.get(PREFERRED_FALLBACK_PALETTE_NAME);
      return colors != null ? colors : ThemePresets.getBuiltinPalettes().get(EXTERNAL_FALLBACK_PALETTE_NAME);
   }

   public synchronized ColorTheme findListedTheme(String name) {
      if (name == null) {
         return null;
      }

      for (ColorTheme theme : this) {
         if (theme.getName().equalsIgnoreCase(name)) {
            return theme;
         }
      }

      return null;
   }

   public synchronized ColorTheme getActiveTheme() {
      return this.activePresetOverride != null ? this.activePresetOverride : this.selectedListedTheme;
   }

   private void captureSelectedUserThemeColors() {
      if (this.selectedListedTheme != null && this.selectedListedTheme.isUserCreated()) {
         this.selectedListedTheme.setColors(this.captureColorsSnapshot());
      }
   }

   private void applyColorsToSettings(int[] colors) {
      if (colors != null) {
         ThemeColorSlot[] slots = ThemeColorSlot.values();
         int[] fallbackColors = this.getFallbackColors();

         for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
            ColorSetting setting = this.colorSettings.get(slots[slotIndex]);
            if (setting != null) {
               int argb;
               if (slotIndex < colors.length) {
                  argb = colors[slotIndex];
               } else {
                  if (fallbackColors == null || slotIndex >= fallbackColors.length) {
                     continue;
                  }

                  argb = fallbackColors[slotIndex];
               }

               if (argb >>> 24 == 0 && APPLIED_INTERFACE_GROUP.equals(slots[slotIndex].getGroupName()) && fallbackColors != null && slotIndex < fallbackColors.length && fallbackColors[slotIndex] >>> 24 != 0) {
                  argb = fallbackColors[slotIndex];
               }

               setting.setValue(argb);
            }
         }
      }
   }

   public synchronized List<ColorTheme> getUserThemes() {
      ArrayList userThemes = new ArrayList();

      for (ColorTheme theme : this) {
         if (theme.isUserCreated()) {
            userThemes.add(theme);
         }
      }

      return userThemes;
   }

   public synchronized boolean isRainbowEnabled() {
      return this.presetRainbowEnabled && this.activePresetOverride != null;
   }

   public synchronized int[] captureColorsSnapshot() {
      ThemeColorSlot[] slots = ThemeColorSlot.values();
      int[] colors = new int[slots.length];

      for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
         ColorSetting setting = this.colorSettings.get(slots[slotIndex]);
         colors[slotIndex] = setting == null ? 0 : setting.getValue();
      }

      return colors;
   }

   public synchronized void restoreOpaqueInterfaceColors() {
      int[] fallbackColors = this.getFallbackColors();
      if (fallbackColors != null) {
         ThemeColorSlot[] slots = ThemeColorSlot.values();
         boolean changed = false;

         for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
            if (REPAIRED_INTERFACE_GROUP.equals(slots[slotIndex].getGroupName())) {
               ColorSetting setting = this.colorSettings.get(slots[slotIndex]);
               if (setting != null && setting.getValue() >>> 24 == 0) {
                  int fallbackArgb = slotIndex < fallbackColors.length ? fallbackColors[slotIndex] : 0;
                  if (fallbackArgb >>> 24 != 0) {
                     setting.setValue(fallbackArgb);
                     changed = true;
                  }
               }
            }
         }

         if (changed) {
            this.lastCommittedColorsSnapshot = this.captureColorsSnapshot();
            if (this.selectedListedTheme != null && this.selectedListedTheme.isUserCreated()) {
               this.selectedListedTheme.setColors(this.lastCommittedColorsSnapshot);
            }

            this.notifyChanged();
         }
      }
   }

   public synchronized void notifyChanged() {
      if (!this.restoringState) {
         this.changeListener.run();
      }
   }

   private void addBuiltinThemes() {
      for (Entry entry : this.builtinPresetColors.entrySet()) {
         super.add(new ColorTheme((String)entry.getKey(), BUILTIN_THEME_AUTHOR, false, (int[])entry.getValue()));
      }
   }

   public synchronized boolean hasPresetOverride() {
      return this.activePresetOverride != null;
   }

   public synchronized boolean containsThemeName(String name) {
      return this.findListedTheme(name) != null;
   }

   private int[] readColorsArray(JsonObject stateJson) {
      if (stateJson != null && stateJson.has(READ_COLORS_PRESENT_KEY) && stateJson.get(READ_COLORS_ARRAY_CHECK_KEY).isJsonArray()) {
         JsonArray colorsJson = stateJson.getAsJsonArray(READ_COLORS_ARRAY_VALUE_KEY);
         int[] colors = new int[colorsJson.size()];

         for (int colorIndex = 0; colorIndex < colorsJson.size(); colorIndex++) {
            colors[colorIndex] = colorsJson.get(colorIndex).getAsInt();
         }

         return colors;
      } else {
         return null;
      }
   }

   private void selectDefaultListedTheme() {
      this.activePresetOverride = null;
      this.presetRainbowEnabled = false;
      ColorTheme defaultTheme = this.findDefaultListedTheme();
      this.selectedListedTheme = defaultTheme;
      if (defaultTheme != null) {
         this.applyColorsToSettings(defaultTheme.getColors());
      }

      this.lastCommittedColorsSnapshot = this.captureColorsSnapshot();
   }

   public synchronized ColorTheme applyPreset(String name, int[] colors) {
      return this.applyPreset(name, colors, ThemePresetCatalog.isRainbowPresetName(name));
   }

   public synchronized void setChangeListener(Runnable listener) {
      this.changeListener = listener == null ? () -> {} : listener;
   }

   public synchronized void restoreUserThemes(List<ColorTheme> themes, String selectedName, JsonObject selectionState) {
      this.restoringState = true;
      this.removeIf(ColorTheme::isUserCreated);
      if (themes != null) {
         for (ColorTheme theme : themes) {
            if (theme != null && theme.isUserCreated() && !IMPORT_EXCLUDED_PRESET_AUTHOR.equals(theme.getAuthor()) && !this.containsThemeName(theme.getName())) {
               super.add(theme);
            }
         }
      }

      this.activePresetOverride = null;
      this.presetRainbowEnabled = false;
      ColorTheme selectedTheme = this.findListedTheme(selectedName);
      if (selectedTheme == null) {
         selectedTheme = this.findDefaultListedTheme();
      }

      this.selectedListedTheme = selectedTheme;
      if (this.selectedListedTheme != null) {
         this.applyColorsToSettings(this.selectedListedTheme.getColors());
      }

      this.lastCommittedColorsSnapshot = this.captureColorsSnapshot();
      this.restoringState = false;
      if (selectionState != null) {
         this.loadSelectionState(selectionState);
      }

      this.restoreOpaqueInterfaceColors();
   }

   public synchronized String getActiveThemeName() {
      ColorTheme theme = this.getActiveTheme();
      return theme == null ? "" : theme.getName();
   }
}
