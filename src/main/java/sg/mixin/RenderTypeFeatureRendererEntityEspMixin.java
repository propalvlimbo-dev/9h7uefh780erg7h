package sg.mixin;

import sg.mixinbridge.EntityEspOutlineRenderTypes;

import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RenderTypeFeatureRenderer.class)
public class RenderTypeFeatureRendererEntityEspMixin {
   @ModifyVariable(method = "getVertexBuilder", at = @At("HEAD"), argsOnly = true)
   private RenderType renderCompleteEntityEspMask(RenderType var1) {
      return EntityEspOutlineRenderTypes.Ь_15b47058(var1);
   }
}
