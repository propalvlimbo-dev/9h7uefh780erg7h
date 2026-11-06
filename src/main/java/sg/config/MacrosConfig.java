package sg.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map.Entry;
import sg.command.KeyMacro;
import sg.client.RoseClient;
import sg.config.JsonConfigFile;

public class MacrosConfig extends JsonConfigFile {
   private static final String CONFIG_DIRECTORY = "rose/client";
   private static final String CONFIG_NAME = "macro";

   public MacrosConfig() {
      super(CONFIG_DIRECTORY, CONFIG_NAME);
   }

   @Override
   protected void loadContents() {
      if (!this.readFileContents().isEmpty()) {
         JsonParser jsonParser = new JsonParser();
         JsonObject macrosJson = jsonParser.parse(this.readFileContents()).getAsJsonObject();
         RoseClient.getInstance().getServices().getCommandShortcuts().clear();

         for (Entry macroEntry : macrosJson.entrySet()) {
            RoseClient.getInstance().getServices().getCommandShortcuts().add(new KeyMacro((String)macroEntry.getKey(), ((JsonElement)macroEntry.getValue()).getAsInt()));
         }
      }
   }

   @Override
   protected void saveContents() {
      JsonObject macrosJson = new JsonObject();

      for (KeyMacro macro : RoseClient.getInstance().getServices().getCommandShortcuts()) {
         macrosJson.addProperty(macro.ещ(), macro.О());
      }

      this.writeJson(macrosJson);
   }
}
