package sg.config;

import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import sg.config.SwingAnimationsConfig;
import sg.config.FriendsConfig;
import sg.config.MacrosConfig;
import sg.hud.HudLayoutConfig;
import sg.client.RoseClient;
import sg.theme.ColorThemeRegistry;
import sg.modules.impl.init.SoundPadManager;

public class ConfigurationManager extends ArrayList<JsonConfigFile> {
   private @com.google.gson.annotations.SerializedName("Ве")  String lastSavedModuleConfigName;
   @SerializedName("В")
   private final ModuleConfig moduleConfig;
   @SerializedName("В")
   private final SwingAnimationsConfig swingAnimationsConfig;
   @SerializedName("В")
   private final MacrosConfig macrosConfig;
   @SerializedName("В")
   private final FriendsConfig friendsConfig;
   @SerializedName("В")
   private final ChestStealerConfig chestStealerConfig;
   @SerializedName("В")
   private final HeadSwapConfig headSwapConfig;
   @SerializedName("В")
   private final InventoryPresetConfig inventoryPresetConfig;
   @SerializedName("В")
   private final BlockEspConfig blockEspConfig;
   @SerializedName("В")
   private final HudLayoutConfig hudLayoutConfig;
   @SerializedName("В")
   private final ColorThemeConfig colorThemeConfig;
   @SerializedName("В")
   private final SoundPadConfig soundPadConfig;
   @SerializedName("В")
   private final MainMenuConfig mainMenuConfig;
   private final @com.google.gson.annotations.SerializedName("Я")  ServerSelectionConfig serverSelectionConfig;
   @SerializedName("В")
   private final ColorThemeRegistry themeRegistry;
   private static final String INITIAL_LAST_SAVED_CONFIG_NAME = "auto";
   private static final String SHUTDOWN_SAVE_THREAD_NAME = "Auto Save Thread";
   private static final String CUSTOM_CONFIG_ROOT_SYSTEM_DRIVE_TEST_ENV = "SystemDrive";
   private static final String CUSTOM_CONFIG_ROOT_SYSTEM_DRIVE_ENV = "SystemDrive";
   private static final String CUSTOM_CONFIG_DIRECTORY = "rose/configs/custom";
   private static final String CUSTOM_CONFIG_DIRECTORY_CREATED_MESSAGE = "Created configs folder.";
   private static final String CUSTOM_CONFIG_DIRECTORY_CREATE_FAILED_MESSAGE = "Failed to create configs folder.";
   private static final String CUSTOM_CONFIG_FILE_EXTENSION = ".conf";

   @Generated
   public SoundPadConfig getSoundPadConfig() {
      return this.soundPadConfig;
   }

   @Generated
   public FriendsConfig getFriendsConfig() {
      return this.friendsConfig;
   }

   @Generated
   public ModuleConfig getModuleConfig() {
      return this.moduleConfig;
   }

   @Generated
   public HeadSwapConfig getHeadSwapConfig() {
      return this.headSwapConfig;
   }

   @Generated
   public ChestStealerConfig getChestStealerConfig() {
      return this.chestStealerConfig;
   }

   public void scheduleLoadAll() {
      Minecraft client = Minecraft.getInstance();
      if (client.getResourceManager() != null) {
         client.execute(this::loadAll);
      } else {
         ClientLifecycleEvents.CLIENT_STARTED.register(startedClient -> startedClient.execute(this::loadAll));
      }
   }

   @Generated
   public HudLayoutConfig getHudLayoutConfig() {
      return this.hudLayoutConfig;
   }

   @Generated
   public void setLastSavedModuleConfigName(String configName) {
      this.lastSavedModuleConfigName = configName;
   }

   public void saveAll() {
      if (RoseClient.getInstance() == null
         || RoseClient.getInstance().getServices() == null
         || RoseClient.getInstance().getServices().getUnhookFiles() == null
         || !RoseClient.getInstance().getServices().getUnhookFiles().аА()) {
         for (JsonConfigFile config : this) {
            config.save();
         }
      }
   }

   @Generated
   public BlockEspConfig getBlockEspConfig() {
      return this.blockEspConfig;
   }

   private void registerShutdownSaveHook() {
      Thread shutdownSaveThread = new Thread(SHUTDOWN_SAVE_THREAD_NAME) {
         private static final String SHUTDOWN_SAVE_FINISHED_MESSAGE = "Thread finished.";

         @Override
         public void run() {
            ConfigurationManager.this.saveAll();
            System.out.println(SHUTDOWN_SAVE_FINISHED_MESSAGE);
         }
      };
      Runtime.getRuntime().addShutdownHook(shutdownSaveThread);
   }

   @Generated
   public InventoryPresetConfig getInventoryPresetConfig() {
      return this.inventoryPresetConfig;
   }

   public void loadAll() {
      for (JsonConfigFile config : this) {
         config.load();
      }

      this.themeRegistry.restoreOpaqueInterfaceColors();
   }

   @Generated
   public MainMenuConfig getMainMenuConfig() {
      return this.mainMenuConfig;
   }

   @Generated
   public ColorThemeRegistry getThemeRegistry() {
      return this.themeRegistry;
   }

   public ConfigurationManager(ColorThemeRegistry themeRegistry, SoundPadManager soundPadManager) {
      this.lastSavedModuleConfigName = INITIAL_LAST_SAVED_CONFIG_NAME;
      this.themeRegistry = themeRegistry;
      this.moduleConfig = new ModuleConfig();
      this.swingAnimationsConfig = new SwingAnimationsConfig();
      this.macrosConfig = new MacrosConfig();
      this.friendsConfig = new FriendsConfig();
      this.chestStealerConfig = new ChestStealerConfig();
      this.headSwapConfig = new HeadSwapConfig();
      this.inventoryPresetConfig = new InventoryPresetConfig();
      this.blockEspConfig = new BlockEspConfig();
      this.hudLayoutConfig = new HudLayoutConfig();
      this.colorThemeConfig = new ColorThemeConfig(themeRegistry);
      this.soundPadConfig = new SoundPadConfig(soundPadManager);
      this.mainMenuConfig = new MainMenuConfig();
      this.serverSelectionConfig = new ServerSelectionConfig();
      this.add(this.colorThemeConfig);
      this.add(this.moduleConfig);
      this.add(this.swingAnimationsConfig);
      this.add(this.macrosConfig);
      this.add(this.friendsConfig);
      this.add(this.chestStealerConfig);
      this.add(this.headSwapConfig);
      this.add(this.inventoryPresetConfig);
      this.add(this.blockEspConfig);
      this.add(this.hudLayoutConfig);
      this.add(this.soundPadConfig);
      this.add(this.mainMenuConfig);
      this.add(this.serverSelectionConfig);
      themeRegistry.setChangeListener(this.colorThemeConfig::save);
      soundPadManager.б(this.soundPadConfig::save);
      this.registerShutdownSaveHook();
   }

   @Generated
   public ColorThemeConfig getColorThemeConfig() {
      return this.colorThemeConfig;
   }

   @Generated
   public SwingAnimationsConfig getSwingAnimationsConfig() {
      return this.swingAnimationsConfig;
   }

   @Generated
   public ServerSelectionConfig getServerSelectionConfig() {
      return this.serverSelectionConfig;
   }

   @Generated
   public MacrosConfig getMacrosConfig() {
      return this.macrosConfig;
   }

   public List<String> listCustomConfigNames() {
      ArrayList configNames = new ArrayList();
      String storageRoot = sg.config.ClientDirectories.forDevelopment(System.getenv(CUSTOM_CONFIG_ROOT_SYSTEM_DRIVE_TEST_ENV) == null ? Minecraft.getInstance().gameDirectory.toString() : System.getenv(CUSTOM_CONFIG_ROOT_SYSTEM_DRIVE_ENV));
      File customConfigDirectory = new File(storageRoot + "/Dimas/", CUSTOM_CONFIG_DIRECTORY);
      if (!customConfigDirectory.exists()) {
         if (customConfigDirectory.mkdirs()) {
            System.out.println(CUSTOM_CONFIG_DIRECTORY_CREATED_MESSAGE);
         } else {
            System.out.println(CUSTOM_CONFIG_DIRECTORY_CREATE_FAILED_MESSAGE);
         }
      }

      File[] configFiles = customConfigDirectory.listFiles();
      if (configFiles == null) {
         return configNames;
      }

      for (File configFile : configFiles) {
         if (configFile.isFile() && configFile.getName().endsWith(CUSTOM_CONFIG_FILE_EXTENSION)) {
            configNames.add(configFile.getName().substring(0, configFile.getName().length() - 5));
         }
      }

      return configNames;
   }

   @Generated
   public String getLastSavedModuleConfigName() {
      return this.lastSavedModuleConfigName;
   }
}
