package dev.rose.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.rose.compat.Compat;
import dev.rose.module.Modules;
import dev.rose.module.impl.world.SkyModule;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.SkyRenderState;
import net.minecraft.world.level.MoonPhase;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {
   @Unique
   private float rose$vanillaStars;

   @Inject(method = "extractRenderState", at = @At("TAIL"))
   private void rose$skyColor(ClientLevel level, float partialTick, Camera camera, SkyRenderState state, CallbackInfo ci) {
      SkyModule sky = Modules.sky();
      if (sky != null && !Compat.shaderPackActive()) {
         state.skyColor = sky.skyColor(state.skyColor);
      }
   }

   @ModifyVariable(method = "renderSunMoonAndStars", at = @At("HEAD"), argsOnly = true, ordinal = 4)
   private float rose$stars(float brightness) {
      this.rose$vanillaStars = brightness;
      SkyModule sky = Modules.sky();
      return sky != null && !Compat.shaderPackActive() ? sky.starBrightness(brightness) : brightness;
   }

   @Inject(method = "renderSun", at = @At("HEAD"), cancellable = true)
   private void rose$sun(float rainBrightness, PoseStack pose, CallbackInfo ci) {
      SkyModule sky = Modules.sky();
      if (sky != null && sky.hideSun()) {
         ci.cancel();
      }
   }

   @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
   private void rose$moon(MoonPhase phase, float rainBrightness, PoseStack pose, CallbackInfo ci) {
      SkyModule sky = Modules.sky();
      if (sky != null && sky.hideMoon()) {
         ci.cancel();
      }
   }

   @Inject(method = "renderSunMoonAndStars", at = @At("TAIL"))
   private void rose$overlay(
      PoseStack pose, float sunAngle, float moonAngle, float starAngle, MoonPhase phase, float rainBrightness, float starBrightness, CallbackInfo ci
   ) {
      SkyModule sky = Modules.sky();
      if (sky != null && sky.isEnabled() && !Compat.shaderPackActive()) {
         Matrix4f rotation = new Matrix4f(RenderSystem.getModelViewMatrix());
         Modules.guard(sky, () -> sky.renderOverlay(rotation, sunAngle, starAngle, rainBrightness, this.rose$vanillaStars));
      }
   }
}
