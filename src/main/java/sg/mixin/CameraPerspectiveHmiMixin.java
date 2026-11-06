package sg.mixin;

import sg.mixinbridge.HmiCameraAccess;

import com.mojang.math.Axis;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import sg.events.impl.UiAnimationEvents;
import settings.render.HmiIntegration;
import sg.ec.PlayerCameraPositionSmoothing;
import sg.modules.impl.render.BetterMinecraft;
import sg.modules.impl.misc.FreeCam;
import sg.client.RoseClient;
import sg.events.impl.CameraPositionEvent;
import sg.events.impl.RenderSuppressionQuery;
import sg.events.impl.AspectRatioEvent;
import sg.events.impl.CameraRotationEvent;

@Mixin(Camera.class)
public abstract class CameraPerspectiveHmiMixin implements HmiCameraAccess {
   @Shadow
   private float eyeHeight;
   @Shadow
   private float eyeHeightOld;
   @Shadow
   private Quaternionf rotation;
   @Shadow
   private float xRot;
   @Shadow
   private float yRot;
   @Shadow
   @Final
   private static Vector3fc FORWARDS;
   @Shadow
   @Final
   private static Vector3fc UP;
   @Shadow
   @Final
   private static Vector3fc LEFT;
   @Shadow
   private Vector3f forwards;
   @Shadow
   private Vector3f up;
   @Shadow
   private Vector3f left;
   @Shadow
   private int matrixPropertiesDirty;
   @Unique
   private int rose$rotationCalls;
   @Unique
   private float hmiPitch;
   @Unique
   private float hmiYaw;
   @Unique
   private float hmiRoll;
   @Unique
   private float hmiX;
   @Unique
   private float hmiY;
   @Unique
   private float hmiZ;
   @Unique
   private CameraType perspective;
   @Unique
   private CameraType transitionFrom;
   @Unique
   private CameraType transitionTo;
   @Unique
   private Vec3 frameStartPosition = Vec3.ZERO;
   @Unique
   private Vec3 frameAnchor = Vec3.ZERO;
   @Unique
   private Vec3 transitionStartOffset = Vec3.ZERO;
   @Unique
   private long transitionStartedAt;
   @Unique
   private long transitionDurationNanos;
   @Unique
   private float transitionStartYaw;
   @Unique
   private float transitionStartPitch;
   @Unique
   private boolean pendingOffsetCapture;
   private static final String ЯП弟 = "do_a_barrel_roll";
   private static final float ЯПб = (float) Math.PI;
   private static final float ЯПЫ = (float) (Math.PI / 180.0);
   private static final float ЯПl = (float) (Math.PI / 180.0);
   private static final float ЯП8 = (float) (Math.PI / 180.0);
   private static final String ЯП5 = "do_a_barrel_roll";
   private static final double ЯПв = 1.0E-4;
   private static final double ЯПъ = 1.0E-4;
   private static final double ЯПМ = 0.15;
   private static final double ЯПН = 0.85;
   private static final double ЯПн = 1.25;
   private static final double ЯПЦ = 0.5;
   private static final double ЯПч = 0.12;
   private static final double ЯПЭ = 3.0;
   private static final float ЯП必 = 180.0F;
   private static final float ЯПщ = 360.0F;

   @Shadow
   protected abstract void setPosition(Vec3 var1);

   @Override
   public void hMI5_0$applyRotation() {
      this.rotation.add(Axis.XP.rotationDegrees(this.hmiPitch));
      this.rotation.add(Axis.YP.rotationDegrees(this.hmiYaw));
      this.rotation.add(Axis.ZP.rotationDegrees(this.hmiRoll));
   }

   @Override
   public void hMI5_0$setRotationValues(float var1, float var2, float var3) {
      this.hmiPitch = var1;
      this.hmiYaw = var2;
      this.hmiRoll = var3;
   }

   @Override
   public void hMI5_0$setPosValues(float var1, float var2, float var3) {
      this.hmiX = var1;
      this.hmiY = var2;
      this.hmiZ = var3;
   }

   @Inject(method = "setRotation", at = @At("HEAD"), cancellable = true)
   private void hmiSetRotation(float var1, float var2, CallbackInfo var3) {
      if (HmiIntegration.п弟() && !FabricLoader.getInstance().isModLoaded(ЯП弟)) {
         this.xRot = var2;
         this.yRot = var1;
         this.rotation.rotationYXZ(ЯПб - (var1 + this.hmiYaw) * ЯПЫ, (-var2 + this.hmiPitch) * ЯПl, this.hmiRoll * ЯП8);
         FORWARDS.rotate(this.rotation, this.forwards);
         UP.rotate(this.rotation, this.up);
         LEFT.rotate(this.rotation, this.left);
         this.matrixPropertiesDirty |= 3;
         var3.cancel();
      }
   }

   @Inject(method = "setPosition(DDD)V", at = @At("HEAD"), cancellable = true)
   private void hmiSetPos(double var1, double var3, double var5, CallbackInfo var7) {
      if (HmiIntegration.п弟() && !FabricLoader.getInstance().isModLoaded(ЯП5)) {
         this.setPosition(new Vec3(var1 + this.hmiX, var3 + this.hmiY, var5 + this.hmiZ));
         var7.cancel();
      }
   }

   @Unique
   private static Vec3 shoulderSlideOffset(Vec3 var0, Vec3 var1, float var2) {
      double var3 = Math.sqrt(var0.x * var0.x + var0.z * var0.z);
      double var5 = Math.sqrt(var1.x * var1.x + var1.z * var1.z);
      if (!(var3 < ЯПв) && !(var5 < ЯПъ)) {
         double var7 = Mth.clamp((var3 + var5) * ЯПМ, ЯПН, ЯПн);
         Vec3 var9 = new Vec3(var0.z / var3 * var7, Mth.lerp(ЯПЦ, var0.y, var1.y) + ЯПч, -var0.x / var3 * var7);
         double var10 = 1.0 - var2;
         double var12 = var10 * var10 * var10;
         double var14 = ЯПЭ * var10 * var2;
         double var16 = var2 * var2 * var2;
         return new Vec3(
            var0.x * var12 + var9.x * var14 + var1.x * var16,
            var0.y * var12 + var9.y * var14 + var1.y * var16,
            var0.z * var12 + var9.z * var14 + var1.z * var16
         );
      } else {
         return var0.lerp(var1, var2);
      }
   }

   @Unique
   private static UiAnimationEvents.PerspectiveAnimationEvent perspectiveAnimation(float var0) {
      UiAnimationEvents.PerspectiveAnimationEvent var1 = new UiAnimationEvents.PerspectiveAnimationEvent(var0);
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getEventBus() != null) {
         RoseClient.getInstance().getEventBus().post(var1);
      }

      return var1;
   }

   @Inject(method = "alignWithEntity", at = @At("HEAD"))
   private void captureCameraStart(float var1, CallbackInfo var2) {
      this.rose$rotationCalls = 0;
      this.frameStartPosition = ((Camera)(Object)this).position();
      BetterMinecraft var3 = betterMinecraft();
      if (var3 != null) {
         var3.Ъ(((Camera)(Object)this).entity());
      }
   }

   @Redirect(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"))
   private void hookSetRotation(Camera var1, float var2, float var3, float var4) {
      CameraType var5 = Minecraft.getInstance().options.getCameraType();
      boolean var6 = !var5.isFirstPerson();
      boolean var7 = var5.isMirrored();
      if (this.rose$rotationCalls++ <= 0 || !var7) {
         CameraRotationEvent var8 = new CameraRotationEvent(var2, var3);
         RoseClient.getInstance().getEventBus().post(var8);
         float var9 = var8.getYaw();
         float var10 = var8.getPitch();
         Vec2 var11 = RoseClient.getInstance().getServices().getModules().getFreeCam().э(var9, var10);
         var9 = var11.x;
         var10 = var11.y;
         BetterMinecraft var12 = betterMinecraft();
         if (var12 != null) {
            Vec2 var13 = var12.м(var9, var10);
            var9 = var13.x;
            var10 = var13.y;
         }

         if (var6 && var7) {
            var9 += ЯП必;
            var10 = -var10;
         }

         CameraType var20 = !var6 ? CameraType.FIRST_PERSON : (var7 ? CameraType.THIRD_PERSON_FRONT : CameraType.THIRD_PERSON_BACK);
         this.beginPerspectiveTransition(var20);
         float var14 = this.transitionProgress();
         if (var14 < 1.0F) {
            UiAnimationEvents.PerspectiveAnimationEvent var15 = perspectiveAnimation(var14);
            float var16 = var15.isAnimationEnabled() ? var15.getEasedProgress() : 1.0F;
            float var17 = Mth.wrapDegrees(var9 - this.transitionStartYaw);
            if (this.isThirdPersonFlip() && var17 > 0.0F) {
               var17 -= ЯПщ;
            }

            var9 = this.transitionStartYaw + var17 * var16;
            var10 = Mth.lerp(var16, this.transitionStartPitch, var10);
         }

         ((Camera)(Object)this).setRotation(var9, var10);
      }
   }

   @Redirect(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V", ordinal = 0), require = 1)
   private void redirectSetPos(Camera var1, double var2, double var4, double var6, float var8) {
      Minecraft var9 = Minecraft.getInstance();
      Entity var10 = var1.entity();
      if (var10 == null) {
         PlayerCameraPositionSmoothing.Ъ7();
         Vec3 var18 = new Vec3(var2, var4, var6);
         this.frameAnchor = var18;
         var1.setPosition(var18);
      } else {
         Vec3 var11 = new Vec3(
            Mth.lerp(var8, var10.xo, var10.getX()),
            Mth.lerp(var8, var10.yo, var10.getY()) + Mth.lerp((double)var8, (double)this.eyeHeightOld, (double)this.eyeHeight),
            Mth.lerp(var8, var10.zo, var10.getZ())
         );
         RoseClient var12 = RoseClient.getInstance();
         if (var12 != null && var12.getEventBus() != null && var12.getServices() != null && var12.getServices().getModules() != null) {
            FreeCam var13 = var12.getServices().getModules().getFreeCam();
            boolean var14 = var9.options.getCameraType().isFirstPerson() && var10 == var9.player && !var13.isEnabled() && this.transitionProgress() >= 1.0F;
            if (var14 && var10 instanceof LocalPlayer var15) {
               var11 = PlayerCameraPositionSmoothing.к(var15, var11);
            } else {
               PlayerCameraPositionSmoothing.Ъ7();
            }

            CameraPositionEvent var19 = new CameraPositionEvent(var11, var8);
            var12.getEventBus().post(var19);
            Vec3 var16 = var13.э(var19.getPosition());
            BetterMinecraft var17 = betterMinecraft();
            if (var17 != null) {
               var16 = var17.А(var16);
            }

            this.frameAnchor = var16;
            var1.setPosition(var16);
         } else {
            PlayerCameraPositionSmoothing.Ъ7();
            this.frameAnchor = var11;
            var1.setPosition(var11);
         }
      }
   }

   @Inject(method = "alignWithEntity", at = @At("TAIL"))
   private void animatePerspective(float var1, CallbackInfo var2) {
      Camera var3 = (Camera)(Object)this;
      float var4 = this.transitionProgress();
      if (!(var4 >= 1.0F)) {
         UiAnimationEvents.PerspectiveAnimationEvent var5 = perspectiveAnimation(var4);
         if (!var5.isAnimationEnabled()) {
            this.transitionStartedAt = 0L;
            this.pendingOffsetCapture = false;
         } else {
            if (this.pendingOffsetCapture) {
               this.transitionStartOffset = this.frameStartPosition.subtract(this.frameAnchor);
               this.pendingOffsetCapture = false;
            }

            Vec3 var6 = var3.position().subtract(this.frameAnchor);
            float var7 = var5.getEasedProgress();
            Vec3 var8;
            if (this.isThirdPersonFlip()) {
               var8 = shoulderSlideOffset(this.transitionStartOffset, var6, var7);
            } else {
               var8 = this.transitionStartOffset.lerp(var6, var7);
            }

            var3.setPosition(this.frameAnchor.add(var8));
         }
       }
       Vec3 shake = sg.modules.impl.render.TNTEffect.cameraShake();
       if (shake != Vec3.ZERO) var3.setPosition(var3.position().add(shake));
    }

    @Unique
    private boolean isThirdPersonFlip() {
      return this.transitionFrom != null
         && !this.transitionFrom.isFirstPerson()
         && this.transitionTo != null
         && !this.transitionTo.isFirstPerson()
         && this.transitionFrom != this.transitionTo;
   }

   @Unique
   private void beginPerspectiveTransition(CameraType var1) {
      if (this.perspective == null) {
         this.perspective = var1;
         this.transitionFrom = var1;
         this.transitionTo = var1;
      } else if (this.perspective != var1) {
         Camera var2 = (Camera)(Object)this;
         this.transitionFrom = this.perspective;
         this.transitionTo = var1;
         this.perspective = var1;
         UiAnimationEvents.PerspectiveAnimationEvent var3 = perspectiveAnimation(0.0F);
         if (!var3.isAnimationEnabled()) {
            this.transitionStartedAt = 0L;
            this.transitionDurationNanos = 0L;
            this.pendingOffsetCapture = false;
         } else {
            this.transitionDurationNanos = Math.max(1L, var3.getDurationNanos());
            this.transitionStartedAt = System.nanoTime();
            this.transitionStartYaw = var2.yRot();
            this.transitionStartPitch = var2.xRot();
            this.pendingOffsetCapture = true;
         }
      }
   }

   @Unique
   private float transitionProgress() {
      return this.transitionStartedAt != 0L && this.transitionDurationNanos > 0L
         ? Mth.clamp((float)(System.nanoTime() - this.transitionStartedAt) / (float)this.transitionDurationNanos, 0.0F, 1.0F)
         : 1.0F;
   }

   @Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
   private void clipToSpace(float var1, CallbackInfoReturnable<Float> var2) {
      BetterMinecraft var3 = betterMinecraft();
      if (var3 != null && var3.ПЗ() && var3.cameraThroughWallsSetting.getValue()) {
         var2.setReturnValue(var1);
      } else {
         RenderSuppressionQuery var4 = new RenderSuppressionQuery(RenderSuppressionQuery.Effect.CAMERA_CLIP);
         RoseClient.getInstance().getEventBus().post(var4);
         if (var4.isCancelled()) {
            var2.setReturnValue(var1);
         }
      }
   }

   @Unique
   private static BetterMinecraft betterMinecraft() {
      RoseClient var0 = RoseClient.getInstance();
      return var0 != null && var0.getServices() != null && var0.getServices().getModules() != null ? var0.getServices().getModules().getBetterMinecraft() : null;
   }

   @ModifyArg(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(F)F"), index = 0)
   private float customCameraZoom(float var1) {
      BetterMinecraft var2 = betterMinecraft();
      return var2 == null ? var1 : var2.нэ(var1);
   }

   @ModifyArg(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;move(FFF)V", ordinal = 0), index = 2)
   private float customCameraShoulder(float var1) {
      BetterMinecraft var2 = betterMinecraft();
      return var2 != null && var2.ПЗ() ? var2.cameraOffsetXSetting.getValue() : var1;
   }

   @ModifyArgs(method = "setupPerspective", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Projection;setupPerspective(FFFFF)V"))
   private void rose$aspectRatio(Args var1) {
      float var2 = var1.<Float>get(3);
      float var3 = var1.<Float>get(4);
      AspectRatioEvent var4 = new AspectRatioEvent(var2 / var3);
      if (RoseClient.getInstance() != null) {
         RoseClient.getInstance().getEventBus().post(var4);
      }

      var1.set(3, var3 * var4.getAspectRatio());
   }
}
