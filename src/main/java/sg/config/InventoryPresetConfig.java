package sg.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.modules.impl.misc.InventorySort;
import sg.client.RoseClient;
import sg.modules.impl.init.InventoryPresetStore;

public final class InventoryPresetConfig extends JsonConfigFile {
   private static final Logger LOGGER = LoggerFactory.getLogger(InventoryPresetConfig.class);
   private @com.google.gson.annotations.SerializedName("АУ")  boolean saveReady;
   private @com.google.gson.annotations.SerializedName("АЗ")  boolean backupPrimaryFileBeforeSave;
   private static final String CONFIG_DIRECTORY_PATH = "rose/client";
   private static final String CONFIG_NAME = "inventorysort";
   private static final String TEMP_FILE_PREFIX = "inventorysort-";
   private static final String TEMP_FILE_SUFFIX = ".tmp";
   private static final String SAVE_PRESETS_ERROR_MESSAGE = "Could not save inventory presets";
   private static final String READ_PRESETS_WARNING_MESSAGE = "Could not read inventory presets from {}";
   private static final String BACKUP_FILE_NAME = "inventorysort.conf.bak";

   private Path getBackupPath() {
      return this.getConfigPath().resolveSibling(BACKUP_FILE_NAME);
   }

   private InventorySort getInventorySort() {
      return RoseClient.getInstance().getServices().getModules().getInventorySort();
   }

   @Override
   protected void saveContents() {
      this.savePresets();
   }

   public InventoryPresetConfig() {
      super(CONFIG_DIRECTORY_PATH, CONFIG_NAME);
   }

   @Override
   protected synchronized void loadContents() {
      Path primaryFile = this.getConfigPath();
      if (!Files.exists(primaryFile) && !Files.exists(this.getBackupPath())) {
         this.saveReady = true;
      } else {
         for (Path candidateFile : new Path[]{primaryFile, this.getBackupPath()}) {
            if (Files.exists(candidateFile)) {
               try {
                  InventoryPresetStore presetStore = InventoryPresetStore.ж_3a79a21b(JsonParser.parseString(Files.readString(candidateFile)).getAsJsonObject());
                  this.getInventorySort().Э(presetStore);
                  this.saveReady = true;
                  this.backupPrimaryFileBeforeSave = candidateFile.equals(primaryFile);
                  return;
               } catch (Exception readException) {
                  LOGGER.warn(READ_PRESETS_WARNING_MESSAGE, candidateFile, readException);
               }
            }
         }

         this.saveReady = false;
      }
   }

   public synchronized boolean savePresets() {
      if (!this.saveReady) {
         return false;
      }

      Path primaryFile = this.getConfigPath();
      Path tempFile = null;

      try {
         String serializedJson = new GsonBuilder().setPrettyPrinting().create().toJson(this.getInventorySort().Щ());
         Files.createDirectories(primaryFile.getParent());
         tempFile = Files.createTempFile(primaryFile.getParent(), TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);
         Files.writeString(tempFile, serializedJson);
         if (this.backupPrimaryFileBeforeSave && Files.exists(primaryFile)) {
            Files.copy(primaryFile, this.getBackupPath(), StandardCopyOption.REPLACE_EXISTING);
         }

         try {
            Files.move(tempFile, primaryFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException atomicMoveException) {
            Files.move(tempFile, primaryFile, StandardCopyOption.REPLACE_EXISTING);
         }

         this.backupPrimaryFileBeforeSave = true;
         return true;
      } catch (Exception saveException) {
         LOGGER.error(SAVE_PRESETS_ERROR_MESSAGE, saveException);
         return false;
      } finally {
         if (tempFile != null) {
            try {
               Files.deleteIfExists(tempFile);
            } catch (Exception cleanupException) {
            }
         }
      }
   }
}
