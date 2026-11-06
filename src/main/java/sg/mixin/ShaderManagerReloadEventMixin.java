package sg.mixin;

import sg.mixinbridge.EntityEspOutlineRenderTypes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.GpuDevice;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sg.ec.EmojiMaskPreview;
import sg.network.StyledComponentCache;
import sg.render.RoseBatchedItemAtlas;

@Mixin(ShaderManager.class)
public abstract class ShaderManagerReloadEventMixin {
   private static final String сРъ = "Vulkan";

   @WrapOperation(
      method = "apply(Lnet/minecraft/client/renderer/ShaderManager$Configs;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;clearPipelineCache()V")
   )
   private void rose$submitBeforeShaderReload(GpuDevice var1, Operation<Void> var2) {
      if (сРъ.equals(var1.getDeviceInfo().backendName())) {
         var1.createCommandEncoder().submit();
      }

      var2.call(var1);
      RoseBatchedItemAtlas._г();
      EmojiMaskPreview.ъ西();
      EntityEspOutlineRenderTypes.必З();
      StyledComponentCache.Ъ1();
   }
}
