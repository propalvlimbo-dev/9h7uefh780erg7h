package sg.mixin;

import sg.mixinbridge.EntityEspOutlineRenderTypes;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderTypes.class)
public class RenderTypesEntityEspMixin {
   @Inject(method = "outline", at = @At("HEAD"), cancellable = true)
   private static void renderCompleteEntityEspMask(Identifier var0, CallbackInfoReturnable<RenderType> var1) {
      if (EntityEspOutlineRenderTypes.method_9Ц()) {
         var1.setReturnValue(EntityEspOutlineRenderTypes.б(var0));
      }
   }
}
