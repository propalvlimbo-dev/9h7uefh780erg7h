package dev.rose.render.gpu;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.client.renderer.texture.DynamicTexture;


public class RoseTexture extends DynamicTexture {
   public RoseTexture(String label, NativeImage image) {
      super(() -> "Rose " + label, image);
   }

   public RoseTexture(String label, int width, int height) {
      super(() -> "Rose " + label, width, height, true);
   }

   public RoseTexture filter(boolean linear, boolean repeat) {
      FilterMode mode = linear ? FilterMode.LINEAR : FilterMode.NEAREST;
      AddressMode address = repeat ? AddressMode.REPEAT : AddressMode.CLAMP_TO_EDGE;
      this.sampler = RenderSystem.getSamplerCache().getSampler(address, address, mode, mode, false);
      return this;
   }

   public NativeImage image() {
      NativeImage pixels = this.getPixels();
      if (pixels == null) {
         throw new IllegalStateException("Texture is closed");
      } else {
         return pixels;
      }
   }
}
