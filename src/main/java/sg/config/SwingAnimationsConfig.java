package sg.config;

import com.google.gson.JsonParser;
import java.nio.file.Files;
import sg.client.RoseClient;
import sg.config.JsonConfigFile;

public final class SwingAnimationsConfig extends JsonConfigFile {
   private static final String CONFIG_DIRECTORY = "rose/client";
   private static final String CONFIG_NAME = "swing_animations";

   @Override
   protected void saveContents() {
      this.writeJson(RoseClient.getInstance().getServices().getModules().getSwingAnimations().saveConfig());
   }

   public SwingAnimationsConfig() {
      super(CONFIG_DIRECTORY, CONFIG_NAME);
   }

   @Override
   protected void loadContents() {
      if (!Files.isRegularFile(this.getConfigPath())) {
         this.save();
      } else {
         String fileContents = this.readFileContents();
         if (!fileContents.isBlank()) {
            RoseClient.getInstance().getServices().getModules().getSwingAnimations().loadConfig(JsonParser.parseString(fileContents).getAsJsonObject());
         }
      }
   }
}
