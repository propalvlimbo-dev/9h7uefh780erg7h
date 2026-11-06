package sg.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.StagedVertexBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.ec.GuiRenderFlush;

@Mixin(GuiRenderer.class)
public class GuiRendererPassCleanupMixin {
   @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/StagedVertexBuffer;endFrame()V"))
   private boolean endBuffersOnFinalPass(StagedVertexBuffer var1) {
      return !GuiRenderFlush.ьд();
   }

   @WrapWithCondition(
      method = "render",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;clearUnusedOversizedItemRenderers()V")
   )
   private boolean releaseUnusedOnFinalPass(GuiRenderer var1) {
      return !GuiRenderFlush.ьд();
   }
}
