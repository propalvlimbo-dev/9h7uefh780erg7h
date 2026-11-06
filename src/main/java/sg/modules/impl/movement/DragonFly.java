package sg.modules.impl.movement;

import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.modules.impl.init.MovementUtils;

public class DragonFly extends ClientModule {
   private final FloatSetting Хн;
   private final FloatSetting ХЦ;
   private final EventListener<PlayerTickEvent> ХЪ;
   private static final String сO5 = "DragonFly";
   private static final String сOв = "Ускоряет полет на /fly";
   private static final String сOъ = "Speed XZ";
   private static final float сOМ = 1.012F;
   private static final float сOН = 0.1F;
   private static final float сOн = 5.0F;
   private static final float сOЦ = 0.1F;
   private static final String сOч = "Speed Y";
   private static final float сOЭ = 0.1F;
   private static final float сO必 = 5.0F;
   private static final float сOщ = 0.1F;

   public DragonFly() {
      super(сO5, сOв, ModuleCategory.MOVEMENT);
      this.Хн = new FloatSetting(сOъ, сOМ, сOН, сOн).withStep(сOЦ);
      this.ХЦ = new FloatSetting(сOч, 1.0F, сOЭ, сO必).withStep(сOщ);
      this.ХЪ = this::ЮЖ;
      this.addSettings(this.Хн, this.ХЦ);
   }

   public void ЮЖ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player.getAbilities().flying) {
         MovementUtils.setFlightVelocity(this.Хн.getValue().floatValue(), this.ХЦ.getValue().floatValue());
      }
   }
}
