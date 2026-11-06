package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.rose.module.Modules;
import dev.rose.module.impl.world.ItemPhysicsModule;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {
   @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V", at = @At("TAIL"))
   private void rose$remember(ItemEntity item, ItemEntityRenderState state, float partialTick, CallbackInfo ci) {
      ((ItemPhysicsModule.State)state).rose$physics(item.onGround(), item.getId(), item.getItem().getItem() instanceof BlockItem);
   }

   @WrapOperation(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 0)
   )
   private void rose$lift(PoseStack pose, float x, float y, float z, Operation<Void> original, @Local(argsOnly = true) ItemEntityRenderState state) {
      ItemPhysicsModule physics = Modules.itemPhysics();
      if (physics != null && physics.isEnabled()) {
         float rest = -((float)state.item.getModelBoundingBox().minY) + 0.0625F;
         original.call(new Object[]{pose, 0.0F, physics.lift((ItemPhysicsModule.State)state, rest), 0.0F});
      } else {
         original.call(new Object[]{pose, x, y, z});
      }
   }

   @WrapOperation(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 0)
   )
   private void rose$rotate(PoseStack pose, Quaternionfc rotation, Operation<Void> original, @Local(argsOnly = true) ItemEntityRenderState state) {
      ItemPhysicsModule physics = Modules.itemPhysics();
      if (physics != null && physics.isEnabled()) {
         physics.rotate(pose, (ItemPhysicsModule.State)state, state.ageInTicks);
      } else {
         original.call(new Object[]{pose, rotation});
      }
   }
}
