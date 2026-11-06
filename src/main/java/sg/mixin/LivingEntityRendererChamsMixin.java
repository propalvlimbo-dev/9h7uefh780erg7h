package sg.mixin;

import sg.mixinbridge.RenderSuppressionQueries;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.render.AntiAim;
import sg.modules.impl.render.Chams;
import sg.modules.impl.init.RoseXrayLines;
import sg.modules.impl.render.GhostHit;
import sg.client.RoseClient;
import sg.events.impl.RenderSuppressionQuery;
import sg.render.pipeline.PrismaticChams;
import sg.util.time.EntityModelPointSampler;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererChamsMixin {
   @Unique
   private float prev = 0.0F;

   @ModifyExpressionValue(
      method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getXRot(F)F")
   )
   private float getLerpedPitchHook(float vanillaPitch, @Local(ordinal = 0, argsOnly = true) LivingEntity entity, @Local(ordinal = 0, argsOnly = true) float partialTick) {
      return entity instanceof LocalPlayer ignoredLocalPlayer
         ? Mth.lerp(partialTick, RoseClient.getInstance().getControllers().getClientTicks().getPitchHistory().y, RoseClient.getInstance().getControllers().getClientTicks().getPitchHistory().x)
         : vanillaPitch;
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
      at = @At("RETURN")
   )
   private void applyVisualAntiAim(LivingEntity entity, LivingEntityRenderState renderState, float ignoredPartialTick, CallbackInfo ignoredCallback) {
      AntiAim.А(entity, renderState);
      EntityModelPointSampler.trackEntityRenderState(entity, renderState);
   }

   @ModifyConstant(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      constant = @Constant(intValue = 654311423)
   )
   private int changeGhostHitGhostColor(int vanillaColor) {
      return GhostHit.М(vanillaColor);
   }

   @ModifyExpressionValue(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/renderer/rendertype/RenderType;"
      )
   )
   private RenderType useCleanGhostLayer(RenderType vanillaRenderType, @Local(ordinal = 0, argsOnly = true) LivingEntityRenderState renderState) {
      if (renderState instanceof AvatarRenderState avatarState && GhostHit.Щ(avatarState)) {
         return GhostHit.Й(avatarState.skin.body().texturePath());
      } else {
         Chams chams = Chams.getActiveInstance();
         if (chams != null && chams.shouldRenderChams(renderState)) {
            return vanillaRenderType != null ? vanillaRenderType : RoseXrayLines.QUADS;
         } else {
            return vanillaRenderType;
         }
      }
   }

   @WrapOperation(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
      )
   )
   private void submitPrismaticChams(
      SubmitNodeCollector collector,
      Model model,
      Object renderState,
      PoseStack poseStack,
      RenderType renderType,
      int packedLight,
      int packedOverlay,
      int color,
      TextureAtlasSprite sprite,
      int outlineColor,
      CrumblingOverlay crumblingOverlay,
      Operation<Void> original
   ) {
      if (renderState instanceof LivingEntityRenderState sampledRenderState) {
         EntityModelPointSampler.beginModelCapture(sampledRenderState, model);
      }

      if (this.submitChams(model, renderState, poseStack, packedLight, packedOverlay, color)) {
         if (renderType != null && outlineColor != 0) {
            RenderType outlineRenderType = renderType.isOutline() ? renderType : renderType.outline().orElse(null);
            if (outlineRenderType != null) {
               original.call(collector, model, renderState, poseStack, outlineRenderType, packedLight, packedOverlay, color, sprite, outlineColor, null);
            }
         }

         if (renderState instanceof LivingEntityRenderState chamsRenderState) {
            EntityModelPointSampler.finishModelCapture(chamsRenderState, model);
         }
      } else {
         original.call(collector, model, renderState, poseStack, renderType, packedLight, packedOverlay, color, sprite, outlineColor, crumblingOverlay);
         if (renderState instanceof LivingEntityRenderState vanillaRenderState) {
            EntityModelPointSampler.finishModelCapture(vanillaRenderState, model);
         }
      }
   }

   @Unique
   private boolean submitChams(Model model, Object renderState, PoseStack poseStack, int packedLight, int packedOverlay, int color) {
      if (renderState instanceof LivingEntityRenderState livingRenderState) {
         if (livingRenderState instanceof AvatarRenderState avatarState && GhostHit.Щ(avatarState)) {
            return false;
         } else {
            Chams chams = Chams.getActiveInstance();
            if (chams != null && chams.shouldRenderChams(livingRenderState)) {
               Pose modelPose = poseStack.last().copy();
               if (chams.isHybridDepthMode()) {
                  PrismaticChams.submitModel(model, livingRenderState, modelPose, packedLight, packedOverlay, color, 2.0F);
                  return true;
               } else {
                  float depthPass = chams.isSceneDepthMode() ? 1.0F : 0.0F;
                  PrismaticChams.submitModel(model, livingRenderState, modelPose, packedLight, packedOverlay, color, depthPass);
                  return true;
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   @ModifyReturnValue(method = "shouldRenderLayers(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Z", at = @At("RETURN"))
   private boolean hideChamsFeatures(boolean vanillaShouldRenderLayers, LivingEntityRenderState renderState) {
      if (!vanillaShouldRenderLayers) {
         return false;
      }

      Chams chams = Chams.getActiveInstance();
      return chams == null || !chams.shouldHideLayers(renderState);
   }

   @ModifyReturnValue(method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)F", at = @At("RETURN"))
   private float removeShadow(float vanillaShadowRadius, EntityRenderState renderState) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.SHADOWS)) {
         return 0.0F;
      }

      if (renderState instanceof LivingEntityRenderState livingRenderState) {
         Chams chams = Chams.getActiveInstance();
         if (chams != null && chams.shouldHideShadow(livingRenderState)) {
            return 0.0F;
         }
      }

      return vanillaShadowRadius;
   }
}
