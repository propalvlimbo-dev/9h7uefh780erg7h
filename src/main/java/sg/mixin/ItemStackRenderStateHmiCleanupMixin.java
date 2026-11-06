package sg.mixin;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settings.render.HmiHeldItemQueue;
import sg.ec.HmiHandSwitchQueue;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateHmiCleanupMixin {
   @Inject(method = "clear", at = @At("HEAD"))
   private void hmiClear(CallbackInfo var1) {
      HmiHeldItemQueue.н3();
      HmiHandSwitchQueue.фс();
   }
}
