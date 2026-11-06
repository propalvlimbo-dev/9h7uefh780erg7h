package sg.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import sg.client.RoseClient;
import sg.modules.impl.misc.ChestStealer;

public final class ChestStealerConfig extends JsonConfigFile {
   private static final String CONFIG_DIRECTORY_PATH = "rose/client";
   private static final String CONFIG_NAME = "stealer";

   private ChestStealer getChestStealer() {
      return RoseClient.getInstance().getServices().getModules().getChestStealer();
   }

   @Override
   protected void loadContents() {
      String fileContents = this.readFileContents();
      if (!fileContents.isEmpty()) {
         JsonObject configJson = JsonParser.parseString(fileContents).getAsJsonObject();
         LinkedHashSet selectedItemIds = new LinkedHashSet();

         for (Entry itemEntry : configJson.entrySet()) {
            if (!((JsonElement)itemEntry.getValue()).isJsonPrimitive()
               || !((JsonElement)itemEntry.getValue()).getAsJsonPrimitive().isBoolean()
               || ((JsonElement)itemEntry.getValue()).getAsBoolean()) {
               Identifier itemId = Identifier.tryParse((String)itemEntry.getKey());
               if (itemId != null && BuiltInRegistries.ITEM.containsKey(itemId)) {
                  selectedItemIds.add(itemId);
               }
            }
         }

         this.getChestStealer().Н(selectedItemIds);
      }
   }

   @Override
   protected void saveContents() {
      JsonObject configJson = new JsonObject();

      for (Identifier itemId : this.getChestStealer().П_2571477d()) {
         configJson.addProperty(itemId.toString(), true);
      }

      this.writeJson(configJson);
   }

   public ChestStealerConfig() {
      super(CONFIG_DIRECTORY_PATH, CONFIG_NAME);
   }
}
