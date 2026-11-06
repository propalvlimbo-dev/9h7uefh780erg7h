package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rose.module.Modules;
import dev.rose.module.impl.visual.NametagsModule;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(NameTagFeatureRenderer.class)
public abstract class NameTagsMixin {
   @WrapOperation(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/network/chat/Component;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)V"
      )
   )
   private void rose$nametagStyle(
      Font font,
      Component text,
      float x,
      float y,
      int color,
      boolean shadow,
      Matrix4f matrix,
      MultiBufferSource buffers,
      Font.DisplayMode mode,
      int background,
      int light,
      Operation<Void> original
   ) {
      NametagsModule module = Modules.nametags();
      if (module != null && module.isEnabled()) {
         int newBackground = background != 0 ? module.background(background) : 0;
         boolean newShadow = background == 0 && module.shadow(shadow);
         original.call(new Object[]{font, text, x, y, color, newShadow, matrix, buffers, mode, newBackground, light});
      } else {
         original.call(new Object[]{font, text, x, y, color, shadow, matrix, buffers, mode, background, light});
      }
   }
}
