package sg.mixin;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher.RenderSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.annotation.RequiresMods;
import sg.modules.impl.render.BetterMinecraft;
import sg.client.RoseClient;

@RequiresMods("!sodium")
@Mixin(RenderSection.class)
public class RenderSectionChunkAnimationMixin {
   @Inject(method = "setSectionNode", at = @At("TAIL"))
   private void movedSection(long var1, CallbackInfo var3) {
      RoseClient var4 = RoseClient.getInstance();
      if (var4 != null && var4.getServices() != null && var4.getServices().getModules() != null) {
         BetterMinecraft var5 = var4.getServices().getModules().getBetterMinecraft();
         if (var5.isEnabled() && var5.chunkAnimationSetting.getValue()) {
            var5.chunkRevealAnimation.Б((RenderSection)(Object)this);
         }
      }
   }
}
