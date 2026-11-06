package sg.modules.impl.misc;

import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;

public class TapeMouse extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("М")  EventListener<PlayerTickEvent> playerTickListener = this::method_9;
   private static final String MODULE_NAME = "TapeMouse";
   private static final String MODULE_DESCRIPTION = "Автоматический клик левой кнопкой мыши";
   private static final float ATTACK_STRENGTH_SAMPLE_PARTIAL_TICK = 0.5F;
   private static final float MIN_ATTACK_STRENGTH = 0.9F;

   public TapeMouse() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
   }

   public void method_9(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player.getAttackStrengthScale(ATTACK_STRENGTH_SAMPLE_PARTIAL_TICK) > MIN_ATTACK_STRENGTH && !MINECRAFT_CLIENT.player.isUsingItem()) {
         MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, MINECRAFT_CLIENT.crosshairPickEntity);
      }
   }
}
