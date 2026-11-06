package dev.rose.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.rose.module.Modules;
import dev.rose.module.impl.visual.NametagsModule;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(NameTagFeatureRenderer.Storage.class)
public abstract class NameTagStorageMixin {
   @Inject(method = "add", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V", shift = Shift.AFTER))
   private void rose$nametagScale(
      PoseStack pose, Vec3 attachment, int offset, Component name, boolean discrete, int light, double distance, CameraRenderState camera, CallbackInfo ci
   ) {
      NametagsModule module = Modules.nametags();
      if (module != null) {
         float scale = module.scale();
         if (scale != 1.0F) {
            pose.scale(scale, scale, scale);
         }
      }
   }
}
