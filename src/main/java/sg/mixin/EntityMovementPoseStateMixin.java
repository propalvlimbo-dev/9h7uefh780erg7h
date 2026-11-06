package sg.mixin;

import sg.mixinbridge.ResolvedEntityPoseAccess;
import sg.mixinbridge.EntityWebStateAccess;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.events.impl.RelativeMovementEvent;
import sg.events.impl.PlayerMoveCancelEvent;
import sg.modules.impl.combat.Aura;
import sg.modules.impl.combat.HitBox;
import sg.client.RoseClient;
import sg.modules.impl.movement.NoPush;
import sg.events.impl.CollisionBypassQuery;

@Mixin(Entity.class)
public class EntityMovementPoseStateMixin implements ResolvedEntityPoseAccess, EntityWebStateAccess {
   @Unique
   private Vec3 velocityBeforeFluid;
   @Unique
   private boolean restoreFluidVelocity;
   @Unique
   private float resolvedYaw;
   @Unique
   private float resolvedPitch;
   @Unique
   private Vec3 resolvedPos;
   @Unique
   private boolean dimas$inWeb;
   private static final String ЯЦс = "Игроки";
   private static final String ЯЦВ = "Вода";

   @Override
   public boolean dimas$isInWeb() {
      return this.dimas$inWeb;
   }

   @Override
   public void dimas$setInWeb(boolean var1) {
      this.dimas$inWeb = var1;
   }

   @Override
   public void setResolvedYaw(float var1) {
      this.resolvedYaw = var1;
   }

   @Override
   public void setResolvedPitch(float var1) {
      this.resolvedPitch = var1;
   }

   @Override
   public float getResolvedYaw() {
      return this.resolvedYaw;
   }

   @Override
   public float getResolvedPitch() {
      return this.resolvedPitch;
   }

   @Override
   public void setResolvedPos(Vec3 var1) {
      this.resolvedPos = var1;
   }

   @Override
   public Vec3 getResolvedPos() {
      return this.resolvedPos;
   }

   public Vec3 getResolvedRotationVector() {
      return ((Entity)(Object)this).calculateViewVector(this.resolvedPitch, this.resolvedYaw);
   }

   @Override
   public Vec3 getResolvedForward() {
      return ((Entity)(Object)this).calculateViewVector(this.resolvedPitch, this.resolvedYaw);
   }

   @Inject(method = "move", at = @At("HEAD"))
   private void resetWebState(MoverType var1, Vec3 var2, CallbackInfo var3) {
      this.dimas$inWeb = false;
   }

   @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
   private void noPlayerPush(Entity var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null) {
         NoPush var4 = RoseClient.getInstance().getServices().getModules().getNoPush();
         if (var4.isEnabled() && var4.Ю.isSelected(ЯЦс)) {
            if ((Object)this == var3.player || var1 == var3.player) {
               var2.cancel();
            }
         } else {
            Aura var5 = RoseClient.getInstance().getServices().getModules().getAura();
            if (var5 != null && (var5.е(var1) || var5.е((Entity)(Object)this)) && ((Object)this == var3.player || var1 == var3.player)) {
               var2.cancel();
            }
         }
      }
   }

   @Inject(method = "updateFluidInteraction()Z", at = @At("HEAD"))
   private void beforeFluidPush(CallbackInfoReturnable<Boolean> var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.restoreFluidVelocity = false;
      if (var2.player != null && (Object)this == var2.player) {
         NoPush var3 = RoseClient.getInstance().getServices().getModules().getNoPush();
         if (var3.isEnabled() && var3.Ю.isSelected(ЯЦВ)) {
            Entity var4 = (Entity)(Object)this;
            this.velocityBeforeFluid = var4.getDeltaMovement();
            this.restoreFluidVelocity = true;
         }
      }
   }

   @Inject(method = "updateFluidInteraction()Z", at = @At("RETURN"))
   private void afterFluidPush(CallbackInfoReturnable<Boolean> var1) {
      if (this.restoreFluidVelocity && this.velocityBeforeFluid != null) {
         Entity var2 = (Entity)(Object)this;
         var2.setDeltaMovement(this.velocityBeforeFluid);
         this.restoreFluidVelocity = false;
         this.velocityBeforeFluid = null;
      }
   }

   @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private Vec3 move(Vec3 var1) {
      Entity var3 = (Entity)(Object)this;
      if (var3 instanceof LocalPlayer var2) {
         PlayerMoveCancelEvent var4 = new PlayerMoveCancelEvent();
         RoseClient.getInstance().getEventBus().post(var4);
         if (var4.isCancelled()) {
            var1 = Vec3.ZERO;
         }
      }

      return var1;
   }

   @Inject(method = "moveRelative", at = @At("TAIL"))
   private void moves(float var1, Vec3 var2, CallbackInfo var3) {
      if ((Object)this == Minecraft.getInstance().player) {
         RelativeMovementEvent var4 = new RelativeMovementEvent();
         RoseClient.getInstance().getEventBus().post(var4);
      }
   }

   @Inject(method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At("HEAD"), cancellable = true)
   private void adjustMovementForCollisions(Vec3 var1, CallbackInfoReturnable<Vec3> var2) {
      CollisionBypassQuery var3 = new CollisionBypassQuery();
      RoseClient.getInstance().getEventBus().post(var3);
      if (var3.isCancelled()) {
         var2.setReturnValue(var1);
      }
   }

   @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
   private void dimas$visibleHitBox(CallbackInfoReturnable<AABB> var1) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         HitBox var2 = RoseClient.getInstance().getServices().getModules().getHitBox();
         if (var2 != null) {
            Entity var3 = (Entity)(Object)this;
            var1.setReturnValue(var2.Ц(var3, (AABB)var1.getReturnValue()));
         }
      }
   }

   @Inject(method = "getDeltaMovement", at = @At("HEAD"), cancellable = true)
   private void freeCampasta(CallbackInfoReturnable<Vec3> var1) {
      Entity var2 = (Entity)(Object)this;
      Minecraft var3 = Minecraft.getInstance();
      if (RoseClient.getInstance() != null
         && RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled()
         && var2 instanceof LocalPlayer
         && var2 == var3.player) {
         var1.setReturnValue(Vec3.ZERO);
      }
   }
}
