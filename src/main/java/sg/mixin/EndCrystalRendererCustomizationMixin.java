package sg.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.render.CustomCrystal;
import sg.client.RoseClient;

@Mixin(EndCrystalRenderer.class)
public class EndCrystalRendererCustomizationMixin {
   @Unique
   private boolean rose$cubeVisible;
   @Unique
   private boolean rose$baseVisible;
   @Unique
   private float rose$outerGlassPivotY;
   @Unique
   private boolean rose$prepared;

   @Inject(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At("HEAD")
   )
   private void prepareCustomCrystal(EndCrystalRenderState var1, PoseStack var2, SubmitNodeCollector var3, CameraRenderState var4, CallbackInfo var5) {
      CustomCrystal var6 = getModule();
      if (var6 != null) {
         EndCrystalModel var7 = ((EndCrystalRendererAccessor)this).rose$getModel();
         this.rose$cubeVisible = var7.cube.visible;
         this.rose$baseVisible = var7.base.visible;
         this.rose$outerGlassPivotY = var7.outerGlass.y;
         var6.л(var7);
         var6.к(var2);
         this.rose$prepared = true;
      }
   }

   @Inject(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At("RETURN")
   )
   private void restoreCustomCrystal(EndCrystalRenderState var1, PoseStack var2, SubmitNodeCollector var3, CameraRenderState var4, CallbackInfo var5) {
      if (this.rose$prepared) {
         CustomCrystal var6 = getModule();
         if (var6 == null) {
            this.rose$prepared = false;
         } else {
            EndCrystalModel var7 = ((EndCrystalRendererAccessor)this).rose$getModel();
            var6.л(var7, this.rose$cubeVisible, this.rose$baseVisible, this.rose$outerGlassPivotY);
            this.rose$prepared = false;
         }
      }
   }

   private static CustomCrystal getModule() {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         CustomCrystal var0 = RoseClient.getInstance().getServices().getModules().getCustomCrystal();
         return var0 != null && var0.isEnabled() ? var0 : null;
      } else {
         return null;
      }
   }
}
