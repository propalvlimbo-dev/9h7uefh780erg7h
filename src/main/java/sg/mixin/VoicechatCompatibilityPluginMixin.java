package sg.mixin;

import de.maxhenkel.voicechat.api.VoicechatPlugin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settings.render.RoseVoicechatPlugin;

@Pseudo
@Mixin(targets = "de.maxhenkel.voicechat.intercompatibility.FabricCommonCompatibilityManager", remap = false)
public abstract class VoicechatCompatibilityPluginMixin {
   @Inject(method = "loadPlugins", at = @At("RETURN"), cancellable = true, remap = false)
   private void addRoseVoicechatPlugin(CallbackInfoReturnable<List<VoicechatPlugin>> var1) {
      ArrayList var2 = new ArrayList((Collection)var1.getReturnValue());
      var2.add(new RoseVoicechatPlugin());
      var1.setReturnValue(var2);
   }
}
