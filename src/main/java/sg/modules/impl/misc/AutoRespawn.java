package sg.modules.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;

public class AutoRespawn extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("ЯТ")  EventListener<PlayerTickEvent> playerTickListener = this::method_7ъ;
   private static final String MODULE_NAME = "AutoRespawn";
   private static final String MODULE_DESCRIPTION = "Автоматически возраждается";

   public void method_7ъ(PlayerTickEvent var1) {
      if (Minecraft.getInstance().gui.screen() instanceof DeathScreen) {
         Minecraft.getInstance().player.respawn();
         Minecraft.getInstance().gui.setScreen(null);
      }
   }

   public AutoRespawn() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
   }
}
