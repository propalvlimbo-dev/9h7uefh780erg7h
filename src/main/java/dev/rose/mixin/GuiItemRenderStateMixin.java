package dev.rose.mixin;

import dev.rose.render.gpu.ItemAlpha;
import net.minecraft.client.gui.render.state.GuiItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GuiItemRenderState.class)
public abstract class GuiItemRenderStateMixin implements ItemAlpha.Holder {
   @Unique
   private float rose$alpha = 1.0F;

   @Inject(method = "<init>", at = @At("TAIL"))
   private void rose$rememberAlpha(CallbackInfo ci) {
      this.rose$alpha = ItemAlpha.current;
   }

   @Override
   public float rose$alpha() {
      return this.rose$alpha;
   }
}
