package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.rose.module.Modules;
import dev.rose.module.impl.cosmetic.ItemGlowModule;
import dev.rose.module.impl.cosmetic.SwingTrailModule;
import dev.rose.module.impl.visual.HandModule;
import dev.rose.render.world.ItemProbe;
import dev.rose.render.world.RecordingBuffers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
   @Unique
   private final ItemProbe rose$probe = new ItemProbe();
   @Unique
   private final RecordingBuffers rose$buffers = new RecordingBuffers();

   @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = Shift.AFTER))
   private void rose$handTransform(
      AbstractClientPlayer player,
      float partialTick,
      float pitch,
      InteractionHand hand,
      float swingProgress,
      ItemStack stack,
      float equipProgress,
      PoseStack pose,
      SubmitNodeCollector collector,
      int light,
      CallbackInfo ci
   ) {
      HandModule module = Modules.hand();
      if (module != null) {
         module.applyHandTransform(pose, player, hand, stack);
      }
   }

   @ModifyVariable(method = "renderArmWithItem", at = @At("HEAD"), argsOnly = true, ordinal = 2)
   private float rose$swingProgress(float swingProgress, @Local(argsOnly = true) InteractionHand hand, @Local(argsOnly = true) ItemStack stack) {
      HandModule module = Modules.hand();
      return module != null ? module.swingProgress(hand, stack, swingProgress) : swingProgress;
   }

   @ModifyVariable(method = "renderArmWithItem", at = @At("HEAD"), argsOnly = true, ordinal = 3)
   private float rose$equipProgress(float equipProgress) {
      HandModule module = Modules.hand();
      return module != null ? module.equipProgress(equipProgress) : equipProgress;
   }

   @WrapOperation(
      method = "swingArm",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmAttackTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V")
   )
   private void rose$attackRotation(ItemInHandRenderer renderer, PoseStack pose, HumanoidArm arm, float progress, Operation<Void> original) {
      HandModule module = Modules.hand();
      original.call(new Object[]{renderer, pose, arm, module != null && module.skipAttackRotation() ? 0.0F : progress});
   }

   @WrapOperation(
      method = "renderArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
      )
   )
   private void rose$itemTransform(
      ItemInHandRenderer renderer, LivingEntity entity, ItemStack stack, ItemDisplayContext context, PoseStack pose, SubmitNodeCollector collector, int light, Operation<Void> original
   ) {
      boolean leftHand = context.leftHand();
      HandModule module = Modules.hand();
      boolean transform = module != null && module.isEnabled();
      if (transform) {
         pose.pushPose();
         module.applyItemTransform(pose, entity, leftHand);
      }

      original.call(new Object[]{renderer, entity, stack, context, pose, collector, light});
      if (context.firstPerson() && entity instanceof LocalPlayer player) {
         boolean mainHand = leftHand == (player.getMainArm() == HumanoidArm.LEFT);
         ItemGlowModule glow = Modules.itemGlow();
         if (glow != null && glow.wants(stack, mainHand)) {
            Modules.guard(glow, () -> original.call(new Object[]{renderer, entity, stack, context, pose, glow.silhouette(collector, mainHand), light}));
         }

         SwingTrailModule trail = Modules.swingTrail();
         if (trail != null && trail.isEnabled()) {
            Modules.guard(trail, () -> {
               original.call(new Object[]{renderer, entity, stack, context, pose, this.rose$probe.capture(), light});
               trail.afterItem(this.rose$probe, entity, stack, context, leftHand, this.rose$buffers);
               this.rose$buffers.submit(collector);
            });
         }
      }

      if (transform) {
         pose.popPose();
      }
   }
}
