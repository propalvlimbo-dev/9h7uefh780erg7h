package sg.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import org.slf4j.LoggerFactory;
import sg.client.RoseClient;

public abstract class JsonConfigFile {
   private final @com.google.gson.annotations.SerializedName("ЯБ")  String configName;
   private final @com.google.gson.annotations.SerializedName("ЯС")  String fileExtension;
   private final @com.google.gson.annotations.SerializedName("п")  File configDirectory;
   private final @com.google.gson.annotations.SerializedName("Д")  File configFile;
   private static final String DEFAULT_FILE_EXTENSION = ".conf";
   private static final String CONFIG_ROOT_SYSTEM_DRIVE_TEST_ENV = "SystemDrive";
   private static final String CONFIG_ROOT_SYSTEM_DRIVE_ENV = "SystemDrive";
   private static final String LOAD_CONFIG_ERROR_MESSAGE = "Could not load config {}";

   protected final Path getConfigPath() {
      return this.configFile.toPath();
   }

   private void ensureDirectoryExists() {
      if (!this.configDirectory.exists()) {
         boolean directoryCreated = this.configDirectory.mkdirs();
         if (!directoryCreated) {
            System.out.println("Can't create directory: " + this.configDirectory.getAbsolutePath());
         }
      }
   }

   public JsonConfigFile(String relativeDirectory, String configName) {
      this.fileExtension = DEFAULT_FILE_EXTENSION;
      String storageRoot = sg.config.ClientDirectories.forDevelopment(System.getenv(CONFIG_ROOT_SYSTEM_DRIVE_TEST_ENV) == null ? Minecraft.getInstance().gameDirectory.toString() : System.getenv(CONFIG_ROOT_SYSTEM_DRIVE_ENV));
      this.configDirectory = new File(storageRoot + "/Dimas/", relativeDirectory);
      this.configFile = new File(this.configDirectory, configName + ".conf");
      this.configName = configName;
   }

   protected String readFileContents() {
      String fileContents = "";

      try {
         fileContents = Files.readString(this.configFile.toPath());
      } catch (Exception readFailure) {
         System.out.println(readFailure.getMessage());
      }

      return fileContents;
   }

   protected abstract void saveContents();

   protected abstract void loadContents();

   protected void writeJson(JsonObject configJson) {
      String jsonText = new GsonBuilder().setPrettyPrinting().create().toJson(configJson);

      try {
         Files.writeString(this.configFile.toPath(), jsonText);
      } catch (Exception writeFailure) {
         System.out.println(writeFailure.getMessage());
      }
   }

   public void save() {
      this.ensureDirectoryExists();

      try {
         this.saveContents();
         if (this instanceof ModuleConfig) {
            RoseClient.getInstance().getServices().getConfigManager().setLastSavedModuleConfigName(this.configName);
         }
      } catch (Exception saveFailure) {
         System.out.println(saveFailure.getMessage());
      }
   }

   public void load() {
      this.ensureDirectoryExists();

      try {
         this.loadContents();
      } catch (Exception loadFailure) {
         LoggerFactory.getLogger(JsonConfigFile.class).error(LOAD_CONFIG_ERROR_MESSAGE, this.configFile, loadFailure);
      }
   }
}
