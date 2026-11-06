package sg.mixin;

import java.io.InputStream;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settings.render.CodeSourceResources;
import sg.annotation.RequiresMods;

@RequiresMods({"replaymod", "viafabricplus"})
@Pseudo
@Mixin(
   targets = {"com.viaversion.viaversion.api.data.MappingDataLoader", "com.replaymod.replaystudio.lib.viaversion.api.data.MappingDataLoader"},
   remap = false
)
public abstract class ViaVersionMappingDataLoaderResourceMixin {
   @Shadow(remap = false)
   @Final
   private Class<?> dataLoaderClass;
   @Shadow(remap = false)
   @Final
   private String dataPath;

   @Inject(method = "getResource(Ljava/lang/String;)Ljava/io/InputStream;", at = @At("HEAD"), cancellable = true, remap = false)
   private void rose$readOwnedMapping(String var1, CallbackInfoReturnable<InputStream> var2) {
      var2.setReturnValue(CodeSourceResources.openResource(this.dataLoaderClass, this.dataPath + var1));
   }
}
