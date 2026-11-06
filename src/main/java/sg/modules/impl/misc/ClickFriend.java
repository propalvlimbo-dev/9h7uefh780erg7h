package sg.modules.impl.misc;

import net.minecraft.world.entity.player.Player;
import sg.events.EventListener;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;

public class ClickFriend extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("必")  KeybindSetting keybindSetting;
   private final @com.google.gson.annotations.SerializedName("ВI")  EventListener<InputEvent> inputListener;
   private static final String MODULE_NAME = "ClickFriend";
   private static final String BIND_LABEL = "Bind";

   public ClickFriend() {
      super(MODULE_NAME, "", ModuleCategory.MISC);
      this.keybindSetting = new KeybindSetting(BIND_LABEL, -1);
      this.inputListener = this::к;
      this.addSettings(this.keybindSetting);
   }

   public void к(InputEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null && var1.getAction() == 1 && var1.getInputCode() == this.keybindSetting.getValue() && MINECRAFT_CLIENT.crosshairPickEntity instanceof Player var2) {
         String var4 = var2.getGameProfile().name();
         if (RoseClient.getInstance().getServices().getFriends().О(var4)) {
            RoseClient.getInstance().getServices().getFriends().removeIf(var1x -> var1x.lз().equalsIgnoreCase(var4));
         } else {
            RoseClient.getInstance().getServices().getFriends().К(var4, null);
         }

         RoseClient.getInstance().getServices().getConfigManager().getFriendsConfig().save();
      }
   }
}
