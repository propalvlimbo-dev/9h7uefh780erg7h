package dev.rose.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.rose.module.Modules;
import dev.rose.util.ColorUtil;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(RenderType.class)
public abstract class RenderTypeMixin {
   @Shadow
   public abstract RenderPipeline pipeline();

   @ModifyArg(
      method = "draw",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"
      ),
      index = 1
   )
   private Vector4fc rose$glintColor(Vector4fc color) {
      if (this.pipeline() != RenderPipelines.GLINT) {
         return color;
      }

      int tint = Modules.entityColors() != null ? Modules.entityColors().glintColor() : 0;
      return (Vector4fc)(tint == 0 ? color : new Vector4f(ColorUtil.red(tint) / 255.0F, ColorUtil.green(tint) / 255.0F, ColorUtil.blue(tint) / 255.0F, 1.0F));
   }
}
