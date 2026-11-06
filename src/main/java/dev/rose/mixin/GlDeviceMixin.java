package dev.rose.mixin;

import com.mojang.blaze3d.opengl.GlDevice;
import dev.rose.render.gpu.ColorTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(GlDevice.class)
public abstract class GlDeviceMixin {
   @ModifyArg(
      method = "createTexture(Ljava/lang/String;ILcom/mojang/blaze3d/textures/TextureFormat;IIII)Lcom/mojang/blaze3d/textures/GpuTexture;",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/opengl/GlStateManager;_texImage2D(IIIIIIIILjava/nio/ByteBuffer;)V"),
      index = 2
   )
   private int rose$floatFormat(int internalFormat) {
      return ColorTarget.floatRequested ? 34842 : internalFormat;
   }
}
