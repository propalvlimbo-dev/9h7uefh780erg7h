package sg.modules.impl.movement;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.GlidingVelocityEvent;
import sg.rotation.AimGeometryMath;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;

public class ElytraMotion extends ClientModule {
   private final FloatSetting см;
   private final BooleanSetting сХ;
   private Vec3 М;
   private final EventListener<GlidingVelocityEvent> с_;
   private static final String ХвЦ = "ElytraMotion";
   private static final String Хвч = "Останавливает полёт рядом с целью";
   private static final String ХвЭ = "Дистанция";
   private static final float Хв必 = 3.0F;
   private static final float Хвщ = 5.0F;
   private static final float ХвГ = 0.1F;
   private static final String Хвл = "Возвращать скорость";
   private static final double Хвэ = 2.0;

   private void Э(GlidingVelocityEvent var1) {
      if (this.сХ.getValue() && this.М != null) {
         var1.setVelocity(this.М);
         this.М = null;
      }
   }

   public ElytraMotion() {
      super(ХвЦ, Хвч, ModuleCategory.MOVEMENT);
      this.см = new FloatSetting(ХвЭ, Хв必, 1.0F, Хвщ).withStep(ХвГ);
      this.сХ = new BooleanSetting(Хвл, true);
      this.М = null;
      this.с_ = this::Э;
      this.addSettings(this.см, this.сХ);
   }

   @Override
   public void onDisable() {
      this.М = null;
   }
}
