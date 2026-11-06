package sg.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.util.ArrayList;
import sg.theme.ColorTheme;
import sg.theme.ColorThemeRegistry;

public class ColorThemeConfig extends JsonConfigFile {
   private final @com.google.gson.annotations.SerializedName("Я")  ColorThemeRegistry themeRegistry;
   private @com.google.gson.annotations.SerializedName("ЯЭ")  boolean readyToSave;
   private static final String CONFIG_DIRECTORY = "rose/configs";
   private static final String CONFIG_NAME = "themes";
   private static final String SAVE_SELECTED_THEME_KEY = "current";
   private static final String SAVE_SELECTION_STATE_KEY = "active";
   private static final String SAVE_THEME_NAME_KEY = "name";
   private static final String SAVE_THEME_CREATOR_KEY = "creator";
   private static final String SAVE_THEME_COLORS_KEY = "colors";
   private static final String SAVE_USER_THEMES_KEY = "themes";
   private static final String CHECK_SELECTED_THEME_KEY = "current";
   private static final String READ_SELECTED_THEME_KEY = "current";
   private static final String CHECK_USER_THEMES_KEY = "themes";
   private static final String CHECK_USER_THEMES_ARRAY_KEY = "themes";
   private static final String READ_USER_THEMES_KEY = "themes";
   private static final String CHECK_THEME_NAME_KEY = "name";
   private static final String CHECK_THEME_COLORS_KEY = "colors";
   private static final String CHECK_THEME_COLORS_ARRAY_KEY = "colors";
   private static final String READ_THEME_NAME_KEY = "name";
   private static final String CHECK_THEME_CREATOR_KEY = "creator";
   private static final String READ_THEME_CREATOR_KEY = "creator";
   private static final String DEFAULT_THEME_CREATOR = "Unknown";
   private static final String READ_THEME_COLORS_KEY = "colors";
   private static final String CHECK_SELECTION_STATE_KEY = "active";
   private static final String CHECK_SELECTION_STATE_OBJECT_KEY = "active";
   private static final String READ_SELECTION_STATE_KEY = "active";

   @Override
   protected void saveContents() {
      if (this.readyToSave) {
         JsonObject configJson = new JsonObject();
         configJson.addProperty(SAVE_SELECTED_THEME_KEY, this.themeRegistry.getSelectedListedThemeName());
         configJson.add(SAVE_SELECTION_STATE_KEY, this.themeRegistry.saveSelectionState());
         JsonArray userThemesJson = new JsonArray();

         for (ColorTheme theme : this.themeRegistry.getUserThemes()) {
            JsonObject themeJson = new JsonObject();
            themeJson.addProperty(SAVE_THEME_NAME_KEY, theme.getName());
            themeJson.addProperty(SAVE_THEME_CREATOR_KEY, theme.getAuthor());
            JsonArray colorsJson = new JsonArray();

            for (int colorArgb : theme.getColors()) {
               colorsJson.add(colorArgb);
            }

            themeJson.add(SAVE_THEME_COLORS_KEY, colorsJson);
            userThemesJson.add(themeJson);
         }

         configJson.add(SAVE_USER_THEMES_KEY, userThemesJson);
         this.writeJson(configJson);
      }
   }

   public ColorThemeConfig(ColorThemeRegistry themeRegistry) {
      super(CONFIG_DIRECTORY, CONFIG_NAME);
      this.themeRegistry = themeRegistry;
   }

   @Override
   protected void loadContents() {
      this.readyToSave = false;
      String fileContents = this.readFileContents();
      if (fileContents != null && !fileContents.isBlank()) {
         JsonElement parsedConfig = JsonParser.parseString(fileContents);
         if (parsedConfig.isJsonObject()) {
            JsonObject configJson = parsedConfig.getAsJsonObject();
            String selectedThemeName = configJson.has(CHECK_SELECTED_THEME_KEY) ? configJson.get(READ_SELECTED_THEME_KEY).getAsString() : "";
            ArrayList userThemes = new ArrayList();
            if (configJson.has(CHECK_USER_THEMES_KEY) && configJson.get(CHECK_USER_THEMES_ARRAY_KEY).isJsonArray()) {
               for (JsonElement themeElement : configJson.getAsJsonArray(READ_USER_THEMES_KEY)) {
                  if (themeElement.isJsonObject()) {
                     JsonObject themeJson = themeElement.getAsJsonObject();
                     if (themeJson.has(CHECK_THEME_NAME_KEY) && themeJson.has(CHECK_THEME_COLORS_KEY) && themeJson.get(CHECK_THEME_COLORS_ARRAY_KEY).isJsonArray()) {
                        String themeName = themeJson.get(READ_THEME_NAME_KEY).getAsString();
                        String themeCreator = themeJson.has(CHECK_THEME_CREATOR_KEY) ? themeJson.get(READ_THEME_CREATOR_KEY).getAsString() : DEFAULT_THEME_CREATOR;
                        JsonArray colorsJson = themeJson.getAsJsonArray(READ_THEME_COLORS_KEY);
                        int[] colorsArgb = new int[colorsJson.size()];

                        for (int colorIndex = 0; colorIndex < colorsJson.size(); colorIndex++) {
                           colorsArgb[colorIndex] = colorsJson.get(colorIndex).getAsInt();
                        }

                        userThemes.add(new ColorTheme(themeName, themeCreator, true, colorsArgb));
                     }
                  }
               }
            }

            JsonObject selectionStateJson = configJson.has(CHECK_SELECTION_STATE_KEY) && configJson.get(CHECK_SELECTION_STATE_OBJECT_KEY).isJsonObject() ? configJson.getAsJsonObject(READ_SELECTION_STATE_KEY) : null;
            this.themeRegistry.restoreUserThemes(userThemes, selectedThemeName, selectionStateJson);
            this.readyToSave = true;
         }
      } else {
         this.readyToSave = !Files.exists(this.getConfigPath());
      }
   }
}
