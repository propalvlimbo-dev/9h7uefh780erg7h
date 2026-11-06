package dev.rose.render;

import dev.rose.render.gpu.QuadState;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Shadow;
import net.minecraft.client.gui.GuiGraphics;


public final class Render2D {
   private Render2D() {
   }

   public static void rect(GuiGraphics g, float x, float y, float w, float h, float radius, int color) {
      Surfaces.fill(g, x, y, w, h, Corners.themed(radius), color, color, color, color, 1.0F);
   }

   public static void gradientH(GuiGraphics g, float x, float y, float w, float h, float radius, int left, int right) {
      Surfaces.fill(g, x, y, w, h, Corners.themed(radius), left, right, right, left, 1.0F);
   }

   public static void gradientV(GuiGraphics g, float x, float y, float w, float h, float radius, int top, int bottom) {
      Surfaces.fill(g, x, y, w, h, Corners.themed(radius), top, top, bottom, bottom, 1.0F);
   }

   public static void outline(GuiGraphics g, float x, float y, float w, float h, float radius, float thickness, int color) {
      Surfaces.border(g, x, y, w, h, Corners.themed(radius), thickness, color, 1.0F);
   }

   public static void shadow(GuiGraphics g, float x, float y, float w, float h, float radius, float softness, int color) {
      Surfaces.shadow(g, x, y, w, h, Corners.themed(radius), new Shadow(color, softness * 0.5F, 0.0F, 0.0F, 0.0F), 1.0F);
   }

   public static void quad(GuiGraphics g, float x0, float y0, float x1, float y1, int color) {
      if (!(x1 <= x0) && !(y1 <= y0) && color >>> 24 != 0) {
         QuadState.submit(g, x0, y0, x1, y1, color);
      }
   }

   public static void circle(GuiGraphics g, float centerX, float centerY, float radius, int color) {
      Surfaces.fill(g, centerX - radius, centerY - radius, radius * 2.0F, radius * 2.0F, Corners.round(radius), color, color, color, color, 1.0F);
   }
}
