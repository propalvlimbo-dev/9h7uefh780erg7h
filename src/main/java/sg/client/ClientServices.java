package sg.client;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import sg.command.CommandRegistry;
import sg.modules.ModuleRegistry;
import sg.modules.impl.init.BlockEspTargets;
import sg.theme.ColorThemeRegistry;
import sg.modules.impl.init.CommandShortcutRegistry;
import sg.config.ConfigurationManager;
import sg.modules.impl.init.RoseUnhookFiles;
import sg.modules.impl.init.FriendRegistry;
import sg.hud.HudElementRegistry;
import sg.modules.impl.init.ProxyManager;
import sg.rotation.RotationRegistry;
import sg.modules.impl.init.SoundPadManager;
import sg.modules.impl.init.StaffNameRegistry;
import sg.modules.impl.init.ViaFabricPlusIntegration;

public class ClientServices {
   @SerializedName("В")
   private final BlockEspTargets blockEspTargets = new BlockEspTargets();
   @SerializedName("В")
   private final ModuleRegistry modules = new ModuleRegistry();
   @SerializedName("В")
   private final CommandRegistry commands = new CommandRegistry();
   @SerializedName("Я")
   private final ConfigurationManager configManager;
   @SerializedName("В")
   private final RotationRegistry rotations = new RotationRegistry();
   @SerializedName("В")
   private final FriendRegistry friends = new FriendRegistry();
   private final @com.google.gson.annotations.SerializedName("Х")  ColorThemeRegistry themes;
   @SerializedName("В")
   private final ViaFabricPlusIntegration viaIntegration;
   @SerializedName("Я")
   private final HudElementRegistry hudElements;
   @SerializedName("В")
   private final CommandShortcutRegistry commandShortcuts;
   @SerializedName("В")
   private final StaffNameRegistry staffNames = new StaffNameRegistry();
   @SerializedName("В")
   private final ProxyManager proxyManager;
   @SerializedName("Я")
   private final SoundPadManager soundPadManager;
   @SerializedName("В")
   private final RoseUnhookFiles unhookFiles;

   @Generated
   public ModuleRegistry getModules() {
      return this.modules;
   }

   @Generated
   public ConfigurationManager getConfigManager() {
      return this.configManager;
   }

   @Generated
   public SoundPadManager getSoundPadManager() {
      return this.soundPadManager;
   }

   @Generated
   public FriendRegistry getFriends() {
      return this.friends;
   }

   public ClientServices() {
      this.themes = new ColorThemeRegistry();
      this.viaIntegration = new ViaFabricPlusIntegration();
      this.hudElements = new HudElementRegistry(
         this.modules.getHud().Р_f1816549(),
         this.modules.getHud().Й(),
         this.modules.getHud().getFromHeadSetting(),
         this.modules.getHud().getFromBarSetting(),
         this.modules.getHud().Юз(),
         this.modules.getHud().method_2_f1816549(),
         this.modules.getHud().щ(),
         this.modules.getHud().Юя(),
         this.modules.getHud().Юъ(),
         this.modules.getHud().ЮБ(),
         this.modules.getHud().Т_3276742a(),
         this.modules.getHud().Щ(),
         this.modules.getHud().ъ_3bdb855e()
      );
      this.commandShortcuts = new CommandShortcutRegistry();
      this.soundPadManager = new SoundPadManager();
      this.unhookFiles = new RoseUnhookFiles();
      this.configManager = new ConfigurationManager(this.themes, this.soundPadManager);
      this.modules.getInventorySort().З(this.configManager.getInventoryPresetConfig()::savePresets);
      this.proxyManager = new ProxyManager();
   }

   @Generated
   public RoseUnhookFiles getUnhookFiles() {
      return this.unhookFiles;
   }

   @Generated
   public ViaFabricPlusIntegration getViaIntegration() {
      return this.viaIntegration;
   }

   @Generated
   public CommandShortcutRegistry getCommandShortcuts() {
      return this.commandShortcuts;
   }

   @Generated
   public HudElementRegistry getHudElements() {
      return this.hudElements;
   }

   @Generated
   public CommandRegistry getCommands() {
      return this.commands;
   }

   @Generated
   public BlockEspTargets getBlockEspTargets() {
      return this.blockEspTargets;
   }

   @Generated
   public ProxyManager getProxyManager() {
      return this.proxyManager;
   }

   @Generated
   public ColorThemeRegistry getThemes() {
      return this.themes;
   }

   @Generated
   public RotationRegistry getRotations() {
      return this.rotations;
   }

   @Generated
   public StaffNameRegistry getStaffNames() {
      return this.staffNames;
   }
}
