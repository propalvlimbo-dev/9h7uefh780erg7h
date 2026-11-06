package dev.rose.mixin;

import com.mojang.blaze3d.TracyFrameCapture;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.rose.render.gpu.GpuFrame;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(RenderSystem.class)
public abstract class RenderSystemMixin {
   @Inject(method = "flipFrame", at = @At("TAIL"))
   private static void rose$endFrame(Window window, @Nullable TracyFrameCapture capture, CallbackInfo ci) {
      GpuFrame.end();
   }
}
