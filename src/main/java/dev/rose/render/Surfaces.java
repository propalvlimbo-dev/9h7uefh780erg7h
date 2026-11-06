package dev.rose.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.rose.render.gpu.Blend;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.gpu.RoseShader;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.render.style.Surface;
import dev.rose.util.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;


public final class Surfaces {
   private static final int FILL = 0;
   private static final int BORDER = 1;
   private static final int SHADOW = 2;
   private static final int INNER_SHADOW = 3;
   private static final int VERTEX_PAINT = 4;
   private static final int OUTSIDE = 4;
   private static final float[] RADII = new float[4];
   private static final float[] SHAPES = new float[4];

   private Surfaces() {
   }

   public static void draw(GuiGraphics g, float x, float y, float w, float h, Surface surface, float alpha) {
      if (!(alpha <= 0.001F) && !(w <= 0.0F) && !(h <= 0.0F)) {
         if (surface.shadow.visible()) {
            shadow(g, x, y, w, h, surface.corners, surface.shadow, alpha);
         }

         if (surface.glow.visible()) {
            glow(g, x, y, w, h, surface.corners, surface.glow, alpha);
         }

         if (surface.glass != null) {
            LiquidGlass.draw(g, x, y, w, h, surface.corners, surface.glass, alpha);
         }

         if (surface.fill != null) {
            fill(g, x, y, w, h, surface.corners, surface.fill, alpha);
         }

         if (surface.innerShadow.visible()) {
            innerShadow(g, x, y, w, h, surface.corners, surface.innerShadow, alpha);
         }

         if (surface.border != null && surface.borderWidth > 0.0F) {
            border(g, x, y, w, h, surface.corners, surface.borderWidth, surface.border, alpha);
         }
      }
   }

   public static void fill(GuiGraphics g, float x, float y, float w, float h, Corners corners, Paint paint, float alpha) {
      render(g, x, y, w, h, corners, 0, 0.0F, Shadow.NONE, paint, 0, 0, 0, 0, alpha, false);
   }

   public static void fill(
      GuiGraphics g, float x, float y, float w, float h, Corners corners, int topLeft, int topRight, int bottomRight, int bottomLeft, float alpha
   ) {
      render(g, x, y, w, h, corners, 0, 0.0F, Shadow.NONE, null, topLeft, topRight, bottomRight, bottomLeft, alpha, false);
   }

   public static void border(GuiGraphics g, float x, float y, float w, float h, Corners corners, float thickness, Paint paint, float alpha) {
      render(g, x, y, w, h, corners, 1, thickness, Shadow.NONE, paint, 0, 0, 0, 0, alpha, false);
   }

   public static void border(GuiGraphics g, float x, float y, float w, float h, Corners corners, float thickness, int color, float alpha) {
      render(g, x, y, w, h, corners, 1, thickness, Shadow.NONE, null, color, color, color, color, alpha, false);
   }

   public static void shadow(GuiGraphics g, float x, float y, float w, float h, Corners corners, Shadow shadow, float alpha) {
      int c = shadow.color();
      render(g, x, y, w, h, corners, 2, 0.0F, shadow, null, c, c, c, c, alpha, false);
   }

   public static void glow(GuiGraphics g, float x, float y, float w, float h, Corners corners, Shadow glow, float alpha) {
      int c = glow.color();
      render(g, x, y, w, h, corners, 2, 0.0F, glow, null, c, c, c, c, alpha, true);
   }

   public static void fillOutside(
      GuiGraphics g, float areaX, float areaY, float areaW, float areaH, float x, float y, float w, float h, Corners corners, int color, float alpha
   ) {
      RoseShader shader = Shaders.surface();
      if (!(w <= 0.0F) && !(h <= 0.0F) && !(alpha <= 0.001F)) {
         corners.writeRadii(RADII, 0);
         corners.writeShapes(SHAPES, 0);
         shader.safeGetUniform("Size").set(w, h);
         shader.safeGetUniform("Radius").set(RADII[0], RADII[1], RADII[2], RADII[3]);
         shader.safeGetUniform("Corner").set(SHAPES[0], SHAPES[1], SHAPES[2], SHAPES[3]);
         shader.safeGetUniform("Mode").set(4);
         shader.safeGetUniform("PaintType").set(4);
         int c = ColorUtil.fade(color, alpha);
         Matrix4f pose = GuiDraw.pose(g);
         float x1 = areaX + areaW;
         float y1 = areaY + areaH;
         BufferBuilder buffer = GuiDraw.begin(shader);
         buffer.addVertex(pose, areaX, areaY, 0.0F).setUv(areaX - x, areaY - y).setColor(c);
         buffer.addVertex(pose, areaX, y1, 0.0F).setUv(areaX - x, y1 - y).setColor(c);
         buffer.addVertex(pose, x1, y1, 0.0F).setUv(x1 - x, y1 - y).setColor(c);
         buffer.addVertex(pose, x1, areaY, 0.0F).setUv(x1 - x, areaY - y).setColor(c);
         GuiDraw.submit(g, shader, Blend.NORMAL, buffer);
      }
   }

   public static void innerShadow(GuiGraphics g, float x, float y, float w, float h, Corners corners, Shadow shadow, float alpha) {
      int c = shadow.color();
      render(g, x, y, w, h, corners, 3, 0.0F, shadow, null, c, c, c, c, alpha, false);
   }

   private static void render(
      GuiGraphics g,
      float x,
      float y,
      float w,
      float h,
      Corners corners,
      int mode,
      float thickness,
      Shadow shadow,
      Paint paint,
      int topLeft,
      int topRight,
      int bottomRight,
      int bottomLeft,
      float alpha,
      boolean additive
   ) {
      RoseShader shader = Shaders.surface();
      if (!(w <= 0.0F) && !(h <= 0.0F) && !(alpha <= 0.001F)) {
         float pad = 1.0F;
         if (mode == 2) {
            pad += shadow.blur() * 3.0F + Math.max(0.0F, shadow.spread()) + Math.max(Math.abs(shadow.offsetX()), Math.abs(shadow.offsetY()));
         }

         corners.writeRadii(RADII, 0);
         corners.writeShapes(SHAPES, 0);
         shader.safeGetUniform("Size").set(w, h);
         shader.safeGetUniform("Radius").set(RADII[0], RADII[1], RADII[2], RADII[3]);
         shader.safeGetUniform("Corner").set(SHAPES[0], SHAPES[1], SHAPES[2], SHAPES[3]);
         shader.safeGetUniform("Mode").set(mode);
         shader.safeGetUniform("Thickness").set(thickness);
         shader.safeGetUniform("Blur").set(shadow.blur());
         shader.safeGetUniform("Spread").set(shadow.spread());
         shader.safeGetUniform("Offset").set(shadow.offsetX(), shadow.offsetY());
         int cTL;
         int cTR;
         int cBR;
         int cBL;
         if (paint != null) {
            PaintUniforms.upload(shader, "Paint", paint);
            cTL = cTR = cBR = cBL = ColorUtil.fade(-1, alpha);
         } else {
            shader.safeGetUniform("PaintType").set(4);
            cTL = ColorUtil.fade(topLeft, alpha);
            cTR = ColorUtil.fade(topRight, alpha);
            cBR = ColorUtil.fade(bottomRight, alpha);
            cBL = ColorUtil.fade(bottomLeft, alpha);
         }

         float x0 = x - pad;
         float y0 = y - pad;
         float x1 = x + w + pad;
         float y1 = y + h + pad;
         Matrix4f pose = GuiDraw.pose(g);
         BufferBuilder buffer = GuiDraw.begin(shader);
         buffer.addVertex(pose, x0, y0, 0.0F).setUv(-pad, -pad).setColor(cTL);
         buffer.addVertex(pose, x0, y1, 0.0F).setUv(-pad, h + pad).setColor(cBL);
         buffer.addVertex(pose, x1, y1, 0.0F).setUv(w + pad, h + pad).setColor(cBR);
         buffer.addVertex(pose, x1, y0, 0.0F).setUv(w + pad, -pad).setColor(cTR);
         GuiDraw.submit(g, shader, additive ? Blend.ADDITIVE : Blend.NORMAL, buffer);
      }
   }
}
