package sg.mixin;

import net.minecraft.client.renderer.SectionOcclusionGraph;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ChunkLoadingRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.events.impl.SectionCullEvent;
import sg.client.RoseClient;

@Mixin(SectionOcclusionGraph.class)
public class SectionOcclusionGraphCullEventMixin {
   @Inject(method = "update", at = @At("HEAD"))
   private void updateSectionOcclusionGraph(CameraRenderState var1, int var2, ChunkLoadingRenderState var3, CallbackInfo var4) {
      SectionCullEvent var5 = new SectionCullEvent();
      RoseClient.getInstance().getEventBus().post(var5);
      var1.smartCull = !var5.isCancelled() && var1.smartCull;
   }
}
