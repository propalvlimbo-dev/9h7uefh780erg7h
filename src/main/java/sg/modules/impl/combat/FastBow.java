package sg.modules.impl.combat;

import net.minecraft.world.item.BowItem;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;

public class FastBow extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("ЯБ")  FloatSetting releaseDelaySetting;
   private final @com.google.gson.annotations.SerializedName("Яэ")  EventListener<PlayerTickEvent> playerTickListener;
   private static final String MODULE_NAME = "FastBow";
   private static final String MODULE_DESCRIPTION = "Убирает задержку при стрельбе из лука";
   private static final String DELAY_LABEL = "Задержка";
   private static final float DEFAULT_DELAY_TICKS = 3.0F;
   private static final float MAX_DELAY_TICKS = 10.0F;

   public void нж(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null) {
         if (MINECRAFT_CLIENT.player.getInventory().getSelectedItem().getItem() instanceof BowItem
            && MINECRAFT_CLIENT.player.isUsingItem()
            && MINECRAFT_CLIENT.player.getTicksUsingItem() >= this.releaseDelaySetting.getValue()
            && MINECRAFT_CLIENT.player.getTicksUsingItem() >= this.releaseDelaySetting.getValue()) {
            MINECRAFT_CLIENT.gameMode.releaseUsingItem(MINECRAFT_CLIENT.player);
         }
      }
   }

   public FastBow() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
      this.releaseDelaySetting = new FloatSetting(DELAY_LABEL, DEFAULT_DELAY_TICKS, 1.0F, MAX_DELAY_TICKS).withStep(1.0F);
      this.playerTickListener = this::нж;
      this.addSettings(this.releaseDelaySetting);
   }
}
