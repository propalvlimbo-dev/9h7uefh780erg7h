package sg.mixin;

import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.render.FullBright;
import sg.client.RoseClient;

@Mixin(LightmapRenderStateExtractor.class)
public class LightmapRenderStateExtractorFullBrightMixin {
   @Inject(method = "extract", at = @At("TAIL"))
   private void applyFullBright(LightmapRenderState var1, float var2, CallbackInfo var3) {
      if (var1.needsUpdate
         && RoseClient.getInstance() != null
         && RoseClient.getInstance().getServices() != null
         && RoseClient.getInstance().getServices().getModules() != null) {
         FullBright var4 = RoseClient.getInstance().getServices().getModules().getFullBright();
         if (var4 != null) {
            var1.ambientColor = new Vector3f(var4.б(var1.ambientColor.x()), var4.б(var1.ambientColor.y()), var4.б(var1.ambientColor.z()));
         }
      }
   }
}
