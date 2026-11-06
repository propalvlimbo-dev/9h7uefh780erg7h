package sg.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.Identifier;
import sg.client.RoseClient;
import sg.modules.impl.misc.HeadSwap;

public final class HeadSwapConfig extends JsonConfigFile {
   private static final String CONFIG_DIRECTORY_PATH = "rose/client";
   private static final String CONFIG_NAME = "headswap";
   private static final String WRITE_ITEM_KEY = "item";
   private static final String WRITE_NAME_KEY = "name";
   private static final String READ_ITEM_PRESENT_KEY = "item";
   private static final String READ_NAME_PRESENT_KEY = "name";
   private static final String READ_ITEM_VALUE_KEY = "item";
   private static final String READ_NAME_VALUE_KEY = "name";

   @Override
   protected void saveContents() {
      JsonObject configJson = new JsonObject();
      HeadSwap headSwapModule = this.getHeadSwap();
      if (headSwapModule.ЧЙ()) {
         Identifier selectedItemId = headSwapModule.л();
         if (selectedItemId != null) {
            configJson.addProperty(WRITE_ITEM_KEY, selectedItemId.toString());
         }

         configJson.addProperty(WRITE_NAME_KEY, headSwapModule.Лш());
      }

      this.writeJson(configJson);
   }

   public HeadSwapConfig() {
      super(CONFIG_DIRECTORY_PATH, CONFIG_NAME);
   }

   @Override
   protected void loadContents() {
      String fileContents = this.readFileContents();
      if (!fileContents.isEmpty()) {
         JsonObject configJson = JsonParser.parseString(fileContents).getAsJsonObject();
         if (configJson.has(READ_ITEM_PRESENT_KEY) && configJson.has(READ_NAME_PRESENT_KEY)) {
            Identifier selectedItemId = Identifier.tryParse(configJson.get(READ_ITEM_VALUE_KEY).getAsString());
            String displayName = configJson.get(READ_NAME_VALUE_KEY).getAsString();
            this.getHeadSwap().к(selectedItemId, displayName);
         } else {
            this.getHeadSwap().лк();
         }
      }
   }

   private HeadSwap getHeadSwap() {
      return RoseClient.getInstance().getServices().getModules().getHeadSwap();
   }
}
