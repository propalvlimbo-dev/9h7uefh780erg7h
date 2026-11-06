package sg.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.render.PlayerEmoteController;

@Mixin(HumanoidModel.class)
public class HumanoidModelEmoteMixin {
   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
   private void animateEmoteArmor(HumanoidRenderState var1, CallbackInfo var2) {
      if (!((Object)this instanceof PlayerModel)) {
         if (var1 instanceof AvatarRenderState var3) {
            PlayerEmoteController.衣((HumanoidModel<?>)(Object)this, var3);
         }
      }
   }
}
