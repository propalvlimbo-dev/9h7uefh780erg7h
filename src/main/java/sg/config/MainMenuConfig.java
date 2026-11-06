package sg.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Generated;
import sg.theme.MainMenuTheme;

public class MainMenuConfig extends JsonConfigFile {
   private @com.google.gson.annotations.SerializedName("В诶")  String themeId = MainMenuTheme.CLASSIC.getId();
   private static final String CONFIG_DIRECTORY = "rose/client";
   private static final String CONFIG_NAME = "mainmenu";
   private static final String SAVE_THEME_ID_KEY = "theme";
   private static final String CHECK_THEME_ID_KEY = "theme";
   private static final String CHECK_THEME_ID_NOT_NULL_KEY = "theme";
   private static final String READ_THEME_ID_KEY = "theme";

   public MainMenuConfig() {
      super(CONFIG_DIRECTORY, CONFIG_NAME);
   }

   public void setThemeId(String themeId) {
      this.themeId = MainMenuTheme.findById(themeId).getId();
      this.save();
   }

   @Override
   protected void saveContents() {
      JsonObject configJson = new JsonObject();
      configJson.addProperty(SAVE_THEME_ID_KEY, this.themeId);
      this.writeJson(configJson);
   }

   public MainMenuTheme getTheme() {
      return MainMenuTheme.findById(this.themeId);
   }

   @Override
   protected void loadContents() {
      String fileContents = this.readFileContents();
      if (fileContents != null && !fileContents.isBlank()) {
         JsonObject configJson = JsonParser.parseString(fileContents).getAsJsonObject();
         if (configJson.has(CHECK_THEME_ID_KEY) && !configJson.get(CHECK_THEME_ID_NOT_NULL_KEY).isJsonNull()) {
            this.themeId = MainMenuTheme.findById(configJson.get(READ_THEME_ID_KEY).getAsString()).getId();
         }
      }
   }

   @Generated
   public String getThemeId() {
      return this.themeId;
   }
}
