package sg.mixin;

import com.mojang.blaze3d.shaders.ShaderType;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.ec.ShaderDepthConvention;
import sg.render.pipeline.RoseChunkOffsets;

@Mixin(targets = "net.minecraft.client.renderer.ShaderManager$CompilationCache")
public class ShaderCompilationCacheSourceTransformMixin {
   @Inject(method = "getShaderSource", at = @At("RETURN"), cancellable = true)
   private void prepareDepthConvention(Identifier var1, ShaderType var2, CallbackInfoReturnable<String> var3) {
      var3.setReturnValue(RoseChunkOffsets.Б(var1, var2, ShaderDepthConvention.injectDepthToClipMacro(var1, (String)var3.getReturnValue())));
   }
}
