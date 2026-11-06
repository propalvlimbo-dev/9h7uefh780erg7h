package sg.modules.impl.movement;

import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerMovementEvent;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.modules.impl.init.MovementUtils;
import sg.settings.SingleChoiceSetting;

public class WaterSpeed extends ClientModule {
   private final SingleChoiceSetting М;
   private final FloatSetting с7;
   private final EventListener<PlayerTickEvent> сч;
   private final EventListener<PlayerMovementEvent> сЭ;
   private static final String сЧч = "WaterSpeed";
   private static final String сЧЭ = "Ускорение в воде";
   private static final String сЧ必 = "Режим";
   private static final String сЧщ = "HVH";
   private static final String сЧГ = "Скорость";
   private static final float сЧл = 0.5F;
   private static final float сЧэ = 0.1F;
   private static final float сЧю = 0.05F;
   private static final String сЧЩ = "HVH";
   private static final String сЧу = "HVH";
   private static final String сЧ2 = "HVH";

   private void ц(PlayerMovementEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && this.М.isSelected(сЧу) && MINECRAFT_CLIENT.player.isInWater()) {
         Vec3 var2 = MINECRAFT_CLIENT.player.getDeltaMovement();
         Vec3 var3 = var1.getMovement();
         var1.setMovement(new Vec3(var2.x, var3.y, var2.z));
      }
   }

   private void К(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && this.М.isSelected(сЧЩ)) {
         if (MINECRAFT_CLIENT.player.isInWater() && (MINECRAFT_CLIENT.player.isSprinting() || MINECRAFT_CLIENT.player.isSwimming())) {
            if (MovementUtils.hasMovementInput()) {
               MovementUtils.setHorizontalSpeed(this.с7.getValue().floatValue());
            }
         }
      }
   }

   public WaterSpeed() {
      super(сЧч, сЧЭ, ModuleCategory.MOVEMENT);
      this.М = new SingleChoiceSetting(сЧ必, new ChoiceOption(сЧщ));
      this.с7 = new FloatSetting(сЧГ, сЧл, сЧэ, 2.0F).withStep(сЧю).visibleWhen(() -> this.М.isSelected(сЧ2));
      this.сч = this::К;
      this.сЭ = this::ц;
      this.addSettings(this.М, this.с7);
   }
}
