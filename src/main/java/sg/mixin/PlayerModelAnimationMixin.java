package sg.mixin;

import sg.mixinbridge.ModelBending;
import sg.mixinbridge.EntityRenderStateIdAccess;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.render.GhostHit;
import sg.modules.impl.movement.NoClip;
import sg.render.PlayerEmoteController;
import sg.util.time.EntityModelPointSampler;

@Mixin(PlayerModel.class)
public class PlayerModelAnimationMixin {
   private static final float 必М = 5.0F;
   private static final float 必Н = 0.22F;
   private static final float 必н = -0.28F;
   private static final float 必Ц = -0.28F;
   private static final float 必ч = 2.4F;
   private static final float 必Э = 0.07F;

   @WrapMethod(
      method = "translateToHand(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V"
   )
   private void transformHeldItem(AvatarRenderState var1, HumanoidArm var2, PoseStack var3, Operation<Void> var4) {
      PlayerModel var5 = (PlayerModel)(Object)this;
      Object var6 = ModelBending.衣_7fd512f1(var5.root());

      try {
         var4.call(var1, var2, var3);
         PlayerEmoteController.衣(var1, var2, var3);
      } finally {
         ModelBending.method_2(var6);
      }
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void animatePlayerModel(AvatarRenderState var1, CallbackInfo var2) {
      PlayerModel var3 = (PlayerModel)(Object)this;
      PlayerEmoteController.衣(var3, var1);
      float var4 = GhostHit.Й_f67ab10a(var1);
      if (var4 >= 0.0F) {
         float var5 = Mth.cos(var4 * 必М) * 必Н;
         var3.rightArm.xRot = var3.rightArm.xRot + (必н + var5);
         var3.leftArm.xRot = var3.leftArm.xRot + (必Ц - var5);
         var3.head.zRot = Mth.sin(var4 * 必ч) * 必Э;
      }

      var3.hat.resetPose();
      var3.jacket.resetPose();
      var3.leftSleeve.resetPose();
      var3.rightSleeve.resetPose();
      var3.leftPants.resetPose();
      var3.rightPants.resetPose();
      if (NoClip.И(((EntityRenderStateIdAccess)var1).rose$getEntityId())) {
         var3.head.xRot = 0.0F;
         var3.hat.xRot = 0.0F;
      }

      EntityModelPointSampler.capturePlayerModel(var1, var3);
   }
}
