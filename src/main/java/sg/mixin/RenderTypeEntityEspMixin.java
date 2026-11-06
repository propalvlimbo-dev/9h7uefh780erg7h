package sg.mixin;

import sg.mixinbridge.EntityEspOutlineRenderTypes;

import java.util.Optional;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderType.class)
public class RenderTypeEntityEspMixin {
   @Inject(method = "outline", at = @At("RETURN"), cancellable = true)
   private void renderCompleteEntityEspMask(CallbackInfoReturnable<Optional<RenderType>> var1) {
      Optional var2 = (Optional)var1.getReturnValue();
      if (var2 != null && !var2.isEmpty()) {
         RenderType var3 = EntityEspOutlineRenderTypes.Ь_15b47058((RenderType)var2.get());
         if (var3 != var2.get()) {
            var1.setReturnValue(Optional.of(var3));
         }
      }
   }
}
