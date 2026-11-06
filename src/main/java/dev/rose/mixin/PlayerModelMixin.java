package dev.rose.mixin;

import dev.rose.module.Modules;
import dev.rose.module.impl.visual.SelfViewModule;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin {
   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void rose$selfView(AvatarRenderState state, CallbackInfo ci) {
      PlayerModel model = (PlayerModel)(Object)this;
      SelfViewModule self = Modules.selfView();
      boolean hide = self != null && self.isSelf(state);
      model.head.visible = !hide;
      if (hide) {
         self.hideParts(model);
      }
   }
}
