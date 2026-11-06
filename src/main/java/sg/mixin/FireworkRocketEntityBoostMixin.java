package sg.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sg.client.RoseClient;
import sg.events.impl.FireworkBoostRotationEvent;
import sg.events.impl.FireworkBoostEvent;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityBoostMixin extends Projectile {
   @Unique
   private Vec3 rotation;
   @Shadow
   private LivingEntity attachedToEntity;
   private static final float Янч = 1.5F;
   private static final float ЯнЭ = 1.5F;
   private static final float Ян必 = 1.5F;
   private static final double Янщ = 0.1;
   private static final double ЯнГ = 0.5;
   private static final double Янл = 0.1;
   private static final double Янэ = 0.5;
   private static final double Яню = 0.1;
   private static final double ЯнЩ = 0.5;

   public FireworkRocketEntityBoostMixin(EntityType<? extends Projectile> var1, Level var2) {
      super(var1, var2);
   }

   @ModifyExpressionValue(
      method = "tick",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getLookAngle()Lnet/minecraft/world/phys/Vec3;")
   )
   public Vec3 tick(Vec3 var1) {
      if (this.attachedToEntity == Minecraft.getInstance().player) {
         FireworkBoostRotationEvent var2 = new FireworkBoostRotationEvent(this.attachedToEntity.getYRot(), this.attachedToEntity.getXRot());
         RoseClient.getInstance().getEventBus().post(var2);
         this.rotation = this.calculateViewVector(var2.getPitch(), var2.getYaw());
      } else {
         this.rotation = var1;
      }

      return this.rotation;
   }

   @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
   public Vec3 tick(Vec3 var1, double var2, double var4, double var6) {
      FireworkBoostEvent var8 = new FireworkBoostEvent(this.attachedToEntity, Янч, ЯнЭ, Ян必);
      RoseClient.getInstance().getEventBus().post(var8);
      return var1.add(
         this.rotation.x * Янщ + (this.rotation.x * var8.getXBoostMultiplier() - var1.x) * ЯнГ,
         this.rotation.y * Янл + (this.rotation.y * var8.getYBoostMultiplier() - var1.y) * Янэ,
         this.rotation.z * Яню + (this.rotation.z * var8.getZBoostMultiplier() - var1.z) * ЯнЩ
      );
   }
}
