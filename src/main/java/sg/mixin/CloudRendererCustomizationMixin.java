package sg.mixin;

import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.misc.HpHelper;
import sg.client.RoseClient;
import sg.modules.impl.render.ShaderMist;
import sg.modules.impl.render.Ambience;

@Mixin(CloudRenderer.class)
public abstract class CloudRendererCustomizationMixin {
   @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int ghoulCloudColor(int var1) {
      return HpHelper.method_2西() ? HpHelper.х_a83dd0cc() : var1;
   }

   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void removeClouds(CallbackInfo var1) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         Ambience var2 = RoseClient.getInstance().getServices().getModules().getAmbience();
         if (var2 != null && var2.shouldRemoveClouds()) {
            var1.cancel();
         } else {
            ShaderMist var3 = RoseClient.getInstance().getServices().getModules().getShaderMist();
            if (var3 != null && var3.isSkyEffectEnabled()) {
               var1.cancel();
            }
         }
      }
   }
}
