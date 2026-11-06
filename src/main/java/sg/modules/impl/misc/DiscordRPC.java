package sg.modules.impl.misc;

import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.integration.discord.presence.DiscordPresenceDefaults;
import sg.integration.discord.presence.DiscordPresenceManager;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public class DiscordRPC extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("В")  DiscordPresenceManager presenceManager = new DiscordPresenceManager(DiscordPresenceDefaults.Х, MINECRAFT_CLIENT);
   private final @com.google.gson.annotations.SerializedName("е衣")  EventListener<PlayerTickEvent> playerTickListener = this::Дг;
   private static final String MODULE_NAME = "DiscordRPC";
   private static final String MODULE_DESCRIPTION = "Отображает клиент в активности дискорда";

   private void Дг(PlayerTickEvent var1) {
      this.presenceManager.tick();
   }

   @Override
   public void onDisable() {
      this.presenceManager.stop();
      super.onDisable();
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.presenceManager.start();
   }

   public DiscordRPC() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
   }
}
