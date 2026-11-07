package sg.modules.impl.movement;

import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.gui.click.ClickGuiScreen;

public class AutoSprint extends ClientModule {
   private final EventListener<PlayerTickEvent> ЯЯ = this::method_9ф;
   private static final String сЧе = "AutoSprint";
   private static final String сЧА = "Автоматически включает режим бега";
   private static final String сЧ4 = "Grim";

   public AutoSprint() {
      super(сЧе, сЧА, ModuleCategory.MOVEMENT);
   }

   public void method_9ф(PlayerTickEvent var1) {
      if (this.вг()) {
         MINECRAFT_CLIENT.options.keySprint.setDown(true);
      } else {
         MINECRAFT_CLIENT.options.keySprint.setDown(false);
         MINECRAFT_CLIENT.player.setSprinting(false);
      }
   }

   private boolean вг() {
      boolean var1 = false;
      boolean var2 = MINECRAFT_CLIENT.player.isInWater() || MINECRAFT_CLIENT.player.isSuppressingSlidingDownLadder() || MINECRAFT_CLIENT.player.isInLava();
      boolean var3 = RoseClient.getInstance().getServices().getModules().getGuiMove().isEnabled()
         && RoseClient.getInstance().getServices().getModules().getGuiMove().Ь().isSelected(сЧ4)
         && MINECRAFT_CLIENT.gui.screen() != null
         && !(MINECRAFT_CLIENT.gui.screen() instanceof ClickGuiScreen);
      boolean var4 = MINECRAFT_CLIENT.player.onGround() || !var1;
      boolean var5 = MINECRAFT_CLIENT.player.getAbilities().flying;
      return !var3 && (var4 || var5 || var2);
   }
}
