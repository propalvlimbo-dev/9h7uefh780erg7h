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
import sg.modules.impl.combat.ElytraTarget;
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
      LivingEntity var2 = RoseClient.getInstance().getServices().getModules().getAura().Ж_95bedd6e();
      ElytraTarget var3 = RoseClient.getInstance().getServices().getModules().getElytraTarget();
      if (var2 != null
         && MINECRAFT_CLIENT.player.isFallFlying()
         && MINECRAFT_CLIENT.player.getEyePosition().distanceTo(var2.getBoundingBox().getCenter().add(0.0, (var2.getY() - var2.yo) * Хвэ, 0.0)) < this.см.getValue().floatValue()
         && (AimGeometryMath.getEntityMovementPerSecond(var2) < var3.РР().getValue().floatValue() || !var3.е_3276742a().getValue())) {
         if (this.сХ.getValue() && this.М == null) {
            this.М = var1.getVelocity();
         }

         var1.setVelocity(Vec3.ZERO);
      } else if (this.сХ.getValue() && this.М != null) {
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
