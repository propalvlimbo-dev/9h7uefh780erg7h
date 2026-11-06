package dev.rose.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.rose.module.impl.PerformanceModule;
import dev.rose.render.Backdrop.Level;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.ColorTarget;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.gpu.RoseShader;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;


public final class LiquidGlass {
   public static final int MAX_SHAPES = 8;
   private static final LiquidGlass.Batch BATCH = new LiquidGlass.Batch();
   private static final float[] RIPPLES = new float[16];
   private static RippleField ripples;

   private LiquidGlass() {
   }

   public static void draw(GuiGraphics g, float x, float y, float w, float h, Corners corners, Glass glass, float alpha) {
      shapes().add(x, y, w, h, corners).draw(g, glass, 0.0F, alpha);
   }

   public static void draw(GuiGraphics g, float x, float y, float w, float h, float radius, Glass glass, float alpha) {
      draw(g, x, y, w, h, Corners.themed(radius), glass, alpha);
   }

   public static LiquidGlass.Batch shapes() {
      return BATCH.reset();
   }

   public static void useRipples(RippleField field) {
      ripples = field;
   }

   public static final class Batch {
      private final float[] shapes = new float[32];
      private final float[] radii = new float[32];
      private final float[] cornerShapes = new float[32];
      private final float[] uploadShapes = new float[32];
      private final float[] uploadRadii = new float[32];
      private final Corners[] corners = new Corners[8];
      private int count;
      private float minX;
      private float minY;
      private float maxX;
      private float maxY;
      private float minHalf;
      private boolean refresh;

      private LiquidGlass.Batch reset() {
         this.count = 0;
         this.minX = this.minY = this.minHalf = Float.MAX_VALUE;
         this.maxX = this.maxY = -Float.MAX_VALUE;
         this.refresh = false;
         return this;
      }

      public LiquidGlass.Batch add(float x, float y, float w, float h, Corners shapeCorners) {
         if (this.count < 8 && !(w <= 0.0F) && !(h <= 0.0F)) {
            int o = this.count * 4;
            this.shapes[o] = x + w / 2.0F;
            this.shapes[o + 1] = y + h / 2.0F;
            this.shapes[o + 2] = w / 2.0F;
            this.shapes[o + 3] = h / 2.0F;
            shapeCorners.writeRadii(this.radii, o);
            shapeCorners.writeShapes(this.cornerShapes, o);
            this.corners[this.count] = shapeCorners;
            this.minHalf = Math.min(this.minHalf, Math.min(w, h) / 2.0F);
            this.minX = Math.min(this.minX, x);
            this.minY = Math.min(this.minY, y);
            this.maxX = Math.max(this.maxX, x + w);
            this.maxY = Math.max(this.maxY, y + h);
            this.count++;
            return this;
         } else {
            return this;
         }
      }

      public LiquidGlass.Batch add(float x, float y, float w, float h, float radius) {
         return this.add(x, y, w, h, Corners.themed(radius));
      }

      public LiquidGlass.Batch refreshBackdrop() {
         this.refresh = true;
         return this;
      }

      public void draw(GuiGraphics g, Glass glass, float merge, float alpha) {
         if (this.count != 0 && !(alpha <= 0.001F)) {
            if (PerformanceModule.lite()) {
               this.drawLite(g, glass, alpha);
            } else {
               RoseShader shader = Shaders.liquidGlass();
               if (shader.ready(Blend.NORMAL) && Backdrop.available()) {
                  float level = Math.clamp(glass.blur, 0.0F, 1.0F) * 3.0F;
                  boolean fresh = this.refresh;
                  float blend = this.count > 1 ? merge : 0.0F;
                  float shrink = blend * 0.25F;

                  for (int i = 0; i < this.count * 4; i += 4) {
                     this.uploadShapes[i] = this.shapes[i];
                     this.uploadShapes[i + 1] = this.shapes[i + 1];
                     this.uploadShapes[i + 2] = Math.max(0.5F, this.shapes[i + 2] - shrink);
                     this.uploadShapes[i + 3] = Math.max(0.5F, this.shapes[i + 3] - shrink);

                     for (int c = 0; c < 4; c++) {
                        this.uploadRadii[i + c] = Math.max(0.0F, this.radii[i + c] - shrink);
                     }
                  }

                  Minecraft mc = Minecraft.getInstance();
                  RenderTarget main = mc.getMainRenderTarget();
                  Matrix4f pose = GuiDraw.pose(g);
                  float poseScale = (float)Math.hypot(pose.m00(), pose.m01());
                  float boundsW = this.maxX - this.minX;
                  float boundsH = this.maxY - this.minY;
                  shader.safeGetUniform("OutSize").set((float)main.width, (float)main.height);
                  shader.safeGetUniform("PixelScale").set(mc.getWindow().getGuiScale() * poseScale);
                  shader.safeGetUniform("Time").set(Theme.time());
                  shader.safeGetUniform("Shapes").set(this.uploadShapes);
                  shader.safeGetUniform("Radii").set(this.uploadRadii);
                  shader.safeGetUniform("Corners").set(this.cornerShapes);
                  shader.safeGetUniform("ShapeCount").set(this.count);
                  shader.safeGetUniform("Merge").set(blend);
                  shader.safeGetUniform("Bounds").set(this.minX, this.minY, boundsW, boundsH);
                  shader.safeGetUniform("Blur").set(glass.blur);
                  shader.safeGetUniform("Refraction").set(glass.refraction);
                  shader.safeGetUniform("Bevel").set(Math.max(0.5F, Math.min(glass.bevel, this.minHalf - shrink)));
                  shader.safeGetUniform("Curve").set(glass.curve);
                  shader.safeGetUniform("Magnify").set(glass.magnify);
                  shader.safeGetUniform("Dispersion").set(glass.dispersion);
                  shader.safeGetUniform("Specular").set(glass.specular);
                  shader.safeGetUniform("Sharpness").set(glass.sharpness);
                  shader.safeGetUniform("SpecularColor")
                     .set(
                        ColorUtil.red(glass.specularColor) / 255.0F,
                        ColorUtil.green(glass.specularColor) / 255.0F,
                        ColorUtil.blue(glass.specularColor) / 255.0F
                     );
                  setLight(shader, glass, mc, this.minX + boundsW / 2.0F, this.minY + boundsH / 2.0F);
                  shader.safeGetUniform("Rim").set(glass.rim);
                  shader.safeGetUniform("RimWidth").set(glass.rimWidth);
                  shader.safeGetUniform("Fresnel").set(glass.fresnel);
                  shader.safeGetUniform("Saturation").set(glass.saturation);
                  shader.safeGetUniform("Brightness").set(glass.brightness);
                  shader.safeGetUniform("Contrast").set(glass.contrast);
                  shader.safeGetUniform("Grain").set(glass.grain);
                  shader.safeGetUniform("Wobble").set(glass.wobble);
                  shader.safeGetUniform("WobbleScale").set(glass.wobbleScale);
                  shader.safeGetUniform("WobbleSpeed").set(glass.wobbleSpeed);
                  shader.safeGetUniform("Depth").set(glass.depth);
                  PaintUniforms.upload(shader, "Tint", glass.tint);
                  shader.safeGetUniform("RippleCount").set(LiquidGlass.ripples == null ? 0 : LiquidGlass.ripples.write(LiquidGlass.RIPPLES));
                  if (LiquidGlass.ripples != null) {
                     shader.safeGetUniform("Ripples").set(LiquidGlass.RIPPLES);
                  }

                  float pad = blend + 2.0F;
                  float x0 = this.minX - pad;
                  float y0 = this.minY - pad;
                  float x1 = this.maxX + pad;
                  float y1 = this.maxY + pad;
                  int color = ColorUtil.fade(-1, alpha);
                  BufferBuilder buffer = GuiDraw.begin(shader);
                  buffer.addVertex(pose, x0, y0, 0.0F).setUv(x0, y0).setColor(color);
                  buffer.addVertex(pose, x0, y1, 0.0F).setUv(x0, y1).setColor(color);
                  buffer.addVertex(pose, x1, y1, 0.0F).setUv(x1, y1).setColor(color);
                  buffer.addVertex(pose, x1, y0, 0.0F).setUv(x1, y0).setColor(color);
                  GuiDraw.submit(g, shader, Blend.NORMAL, buffer, element -> {
                     if (fresh ? Backdrop.refreshSharp() : Backdrop.capture()) {
                        GpuTextureView sharp = Backdrop.sharpTexture();
                        GpuTextureView light = level > 0.0F && level < 2.0F ? orSharp(Backdrop.blurTexture(Level.LIGHT), sharp) : sharp;
                        GpuTextureView medium = level >= 1.0F ? orSharp(Backdrop.blurTexture(Level.MEDIUM), sharp) : sharp;
                        GpuTextureView strong = level >= 2.0F ? orSharp(Backdrop.blurTexture(Level.STRONG), sharp) : sharp;
                        element.texture("Sampler0", sharp, ColorTarget.linear());
                        element.texture("Sampler1", light, ColorTarget.linear());
                        element.texture("Sampler2", medium, ColorTarget.linear());
                        element.texture("Sampler3", strong, ColorTarget.linear());
                     } else {
                        element.skip();
                     }
                  });
               } else {
                  this.drawFallback(g, glass, alpha);
               }
            }
         }
      }

      private static void setLight(RoseShader shader, Glass glass, Minecraft mc, float centerX, float centerY) {
         float lx = Theme.lightX();
         float ly = Theme.lightY();
         if (glass.lightFollowsMouse) {
            Window window = mc.getWindow();
            float mouseX = (float)(mc.mouseHandler.xpos() * window.getGuiScaledWidth() / Math.max(1, window.getScreenWidth()));
            float mouseY = (float)(mc.mouseHandler.ypos() * window.getGuiScaledHeight() / Math.max(1, window.getScreenHeight()));
            float dx = mouseX - centerX;
            float dy = mouseY - centerY;
            float length = (float)Math.hypot(dx, dy);
            if (length > 1.0F) {
               lx = dx / length;
               ly = dy / length;
            }
         }

         shader.safeGetUniform("LightDir").set(lx, ly);
      }

      private static GpuTextureView orSharp(GpuTextureView texture, GpuTextureView sharp) {
         return texture == null ? sharp : texture;
      }

      private void drawLite(GuiGraphics g, Glass glass, float alpha) {
         int tint = glass.tint.color(0);
         int base = ColorUtil.withAlpha(tint, Math.max(ColorUtil.alpha(tint), 200));
         int top = ColorUtil.lerp(base, ColorUtil.withAlpha(-1, ColorUtil.alpha(base)), 0.07F);
         int rim = ColorUtil.withAlpha(-1, Math.round(56.0F * Math.min(1.0F, glass.rim * 2.5F + 0.2F)));

         for (int i = 0; i < this.count; i++) {
            int o = i * 4;
            float x = this.shapes[o] - this.shapes[o + 2];
            float y = this.shapes[o + 1] - this.shapes[o + 3];
            float w = this.shapes[o + 2] * 2.0F;
            float h = this.shapes[o + 3] * 2.0F;
            Surfaces.fill(g, x, y, w, h, this.corners[i], top, top, base, base, alpha);
            Surfaces.border(g, x, y, w, h, this.corners[i], 0.8F, rim, alpha);
         }
      }

      private void drawFallback(GuiGraphics g, Glass glass, float alpha) {
         int tint = glass.tint.color(0);
         int color = ColorUtil.withAlpha(tint, Math.max(ColorUtil.alpha(tint), 176));

         for (int i = 0; i < this.count; i++) {
            int o = i * 4;
            Surfaces.fill(
               g,
               this.shapes[o] - this.shapes[o + 2],
               this.shapes[o + 1] - this.shapes[o + 3],
               this.shapes[o + 2] * 2.0F,
               this.shapes[o + 3] * 2.0F,
               this.corners[i],
               color,
               color,
               color,
               color,
               alpha
            );
         }
      }
   }
}
