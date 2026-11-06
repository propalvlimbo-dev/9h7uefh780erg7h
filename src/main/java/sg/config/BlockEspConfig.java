package sg.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import sg.client.RoseClient;
import sg.modules.impl.init.BlockEspTargets;

public final class BlockEspConfig extends JsonConfigFile {
   private static final String CONFIG_DIRECTORY_PATH = "rose/client";
   private static final String CONFIG_NAME = "blockesp";
   private static final String WRITE_TARGET_ID_KEY = "target";
   private static final String WRITE_COLOR_KEY = "color";
   private static final String WRITE_TARGETS_ARRAY_KEY = "blockesp";
   private static final String READ_TARGETS_ARRAY_KEY = "blockesp";
   private static final String READ_COLOR_PRESENT_KEY = "color";
   private static final String READ_TARGET_PRESENT_KEY = "target";
   private static final String READ_TARGET_VALUE_KEY = "target";
   private static final String READ_LEGACY_BLOCK_PRESENT_KEY = "block";
   private static final String READ_LEGACY_BLOCK_VALUE_KEY = "block";
   private static final String READ_COLOR_VALUE_KEY = "color";
   private static final int RGB_COLOR_MASK = 16777215;

   private BlockEspTargets getBlockEspTargets() {
      return RoseClient.getInstance().getServices().getBlockEspTargets();
   }

   public BlockEspConfig() {
      super(CONFIG_DIRECTORY_PATH, CONFIG_NAME);
   }

   @Override
   protected void saveContents() {
      JsonArray targetsJson = new JsonArray();

      for (BlockEspTargets.Target targetEntry : this.getBlockEspTargets().Т2()) {
         JsonObject targetJson = new JsonObject();
         targetJson.addProperty(WRITE_TARGET_ID_KEY, targetEntry.target());
         targetJson.addProperty(WRITE_COLOR_KEY, targetEntry.color());
         targetsJson.add(targetJson);
      }

      JsonObject configJson = new JsonObject();
      configJson.add(WRITE_TARGETS_ARRAY_KEY, targetsJson);
      this.writeJson(configJson);
   }

   @Override
   protected void loadContents() {
      String fileContents = this.readFileContents();
      if (!fileContents.isEmpty()) {
         JsonObject configJson = JsonParser.parseString(fileContents).getAsJsonObject();
         JsonArray targetsJson = configJson.getAsJsonArray(READ_TARGETS_ARRAY_KEY);
         if (targetsJson != null) {
            ArrayList restoredTargets = new ArrayList();

            for (JsonElement targetElement : targetsJson) {
               if (targetElement.isJsonObject()) {
                  JsonObject targetJson = targetElement.getAsJsonObject();
                  if (targetJson.has(READ_COLOR_PRESENT_KEY)) {
                     String targetKey = targetJson.has(READ_TARGET_PRESENT_KEY) ? READ_TARGET_VALUE_KEY : (targetJson.has(READ_LEGACY_BLOCK_PRESENT_KEY) ? READ_LEGACY_BLOCK_VALUE_KEY : null);
                     if (targetKey != null) {
                        String normalizedTargetId = BlockEspTargets.То_388008bb(targetJson.get(targetKey).getAsString());
                        if (normalizedTargetId != null) {
                           Identifier targetId = Identifier.tryParse(normalizedTargetId);
                           if (targetId != null && (BuiltInRegistries.BLOCK.containsKey(targetId) || BuiltInRegistries.ENTITY_TYPE.containsKey(targetId))) {
                              restoredTargets.add(new BlockEspTargets.Target(normalizedTargetId, targetJson.get(READ_COLOR_VALUE_KEY).getAsInt() & RGB_COLOR_MASK));
                           }
                        }
                     }
                  }
               }
            }

            this.getBlockEspTargets().с(restoredTargets);
         }
      }
   }
}
