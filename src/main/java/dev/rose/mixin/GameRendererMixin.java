package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.rose.gui.PromoRecorder;
import dev.rose.module.Modules;
import dev.rose.render.GlowEngine;
import dev.rose.render.PostEffects;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
   @Inject(
      method = "render",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(Lnet/minecraft/client/DeltaTracker;)V", shift = Shift.AFTER)
   )
   private void rose$postEffects(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
      PostEffects.apply();
   }

   @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
   private float rose$zoom(float fov, @Local(argsOnly = true) boolean useFovSetting) {
      return useFovSetting && Modules.zoom() != null ? fov / Modules.zoom().factor() : fov;
   }

   @ModifyArg(method = "getProjectionMatrix", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFF)Lorg/joml/Matrix4f;"), index = 1)
   private float rose$aspect(float aspect) {
      return Modules.aspectRatio() != null ? Modules.aspectRatio().aspect(aspect) : aspect;
   }

   @Inject(method = "render", at = @At("RETURN"), require = 0)
   private void rose$promoFrame(DeltaTracker tracker, boolean renderLevel, CallbackInfo ci) {
      PromoRecorder.frame();
   }

   @Inject(method = "renderItemInHand", at = @At("HEAD"))
   private void rose$handGlowStart(float partialTick, boolean sleeping, Matrix4f view, CallbackInfo ci) {
      GlowEngine.beginWorld();
   }

   @Inject(
      method = "renderLevel",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFog(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V")
   )
   private void rose$handGlowEnd(DeltaTracker deltaTracker, CallbackInfo ci) {
      GlowEngine.endWorld();
   }

   @ModifyExpressionValue(method = "bobHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"))
   private Object rose$hurtTilt(Object strength) {
      return strength instanceof Double value && Modules.camera() != null ? Modules.camera().hurtTilt(value) : strength;
   }

   @ModifyExpressionValue(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/ClientAvatarState;getInterpolatedBob(F)F"))
   private float rose$bobbing(float bob) {
      return Modules.camera() != null ? Modules.camera().bobbing(bob) : bob;
   }

   @ModifyExpressionValue(
      method = "getFov",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0)
   )
   private Object rose$fov(Object fov) {
      return fov instanceof Integer value && Modules.camera() != null ? Modules.camera().fov(value) : fov;
   }

   @ModifyExpressionValue(method = "getFov", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(FFF)F"))
   private float rose$fovModifier(float modifier) {
      return Modules.camera() != null ? Modules.camera().fovModifier(modifier) : modifier;
   }
}
