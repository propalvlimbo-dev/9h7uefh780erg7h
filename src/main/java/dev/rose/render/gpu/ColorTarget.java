package dev.rose.render.gpu;

import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import org.jetbrains.annotations.Nullable;


public final class ColorTarget implements AutoCloseable {
   public static boolean floatRequested;
   private final String label;
   private final boolean hdr;
   private final boolean withDepth;
   private GpuTexture texture;
   private GpuTextureView view;
   @Nullable
   private GpuTexture depthTexture;
   @Nullable
   private GpuTextureView depthView;
   private int width;
   private int height;

   private ColorTarget(String label, boolean hdr, boolean withDepth) {
      this.label = label;
      this.hdr = hdr;
      this.withDepth = withDepth;
   }

   public static ColorTarget ensure(@Nullable ColorTarget target, String label, int width, int height, boolean hdr) {
      return ensure(target, label, width, height, hdr, false);
   }

   public static ColorTarget ensure(@Nullable ColorTarget target, String label, int width, int height, boolean hdr, boolean withDepth) {
      ColorTarget result = target == null ? new ColorTarget(label, hdr, withDepth) : target;
      int w = Math.max(1, width);
      int h = Math.max(1, height);
      if (result.texture == null || result.width != w || result.height != h) {
         result.create(w, h);
      }

      return result;
   }

   private void create(int newWidth, int newHeight) {
      this.close();
      this.width = newWidth;
      this.height = newHeight;
      GpuDevice device = RenderSystem.getDevice();
      int usage = 15;
      floatRequested = this.hdr;

      try {
         this.texture = device.createTexture(() -> "Rose " + this.label, usage, TextureFormat.RGBA8, this.width, this.height, 1, 1);
      } finally {
         floatRequested = false;
      }

      this.view = device.createTextureView(this.texture);
      if (this.withDepth) {
         this.depthTexture = device.createTexture(() -> "Rose " + this.label + " depth", usage, TextureFormat.DEPTH32, this.width, this.height, 1, 1);
         this.depthView = device.createTextureView(this.depthTexture);
      }
   }

   public GpuTexture texture() {
      return this.texture;
   }

   public GpuTextureView view() {
      return this.view;
   }

   @Nullable
   public GpuTexture depthTexture() {
      return this.depthTexture;
   }

   @Nullable
   public GpuTextureView depthView() {
      return this.depthView;
   }

   public int width() {
      return this.width;
   }

   public int height() {
      return this.height;
   }

   public static GpuSampler linear() {
      return RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
   }

   public void clear() {
      RenderSystem.getDevice().createCommandEncoder().clearColorTexture(this.texture, 0);
   }

   public void clearWithDepth() {
      if (this.depthTexture == null) {
         this.clear();
      } else {
         RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.texture, 0, this.depthTexture, 1.0);
      }
   }

   @Override
   public void close() {
      if (this.view != null) {
         this.view.close();
      }

      if (this.texture != null) {
         this.texture.close();
      }

      if (this.depthView != null) {
         this.depthView.close();
      }

      if (this.depthTexture != null) {
         this.depthTexture.close();
      }

      this.view = null;
      this.texture = null;
      this.depthView = null;
      this.depthTexture = null;
   }
}
