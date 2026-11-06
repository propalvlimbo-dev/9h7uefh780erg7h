package sg.modules.impl.movement;

import sg.events.impl.PlayerPositionCorrectionEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;

public class PearlBoost extends ClientModule {
   private final FloatSetting Яф;
   private final EventListener<PlayerPositionCorrectionEvent> Я衣;
   private static final String ВЦм = "PearlBoost";
   private static final String ВЦК = "Увеличивает буст эндер-жемчуга по Y";
   private static final String ВЦI = "Значение буста";
   private static final float ВЦТ = 1.2F;
   private static final float ВЦО = 3.0F;
   private static final float ВЦФ = 0.1F;

   public void э(PlayerPositionCorrectionEvent var1) {
      MINECRAFT_CLIENT.player.setPosRaw(MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getY() + this.Яф.getValue().floatValue(), MINECRAFT_CLIENT.player.getZ());
   }

   public PearlBoost() {
      super(ВЦм, ВЦК, ModuleCategory.MOVEMENT);
      this.Яф = new FloatSetting(ВЦI, ВЦТ, 0.0F, ВЦО).withStep(ВЦФ);
      this.Я衣 = this::э;
      this.addSettings(this.Яф);
   }
}
