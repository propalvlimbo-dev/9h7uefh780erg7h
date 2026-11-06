package dev.rose.gui.menu;

import dev.rose.render.LiquidGlass;
import dev.rose.render.Render2D;
import dev.rose.render.Surfaces;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.util.ColorUtil;


public final class MenuDraw {
   private MenuDraw() {
   }

   public static void rect(MenuUi ui, float x, float y, float w, float h, float radius, int color) {
      if (!(w <= 0.0F) && !(h <= 0.0F) && ColorUtil.alpha(color) != 0) {
         Surfaces.fill(ui.g, x, y, w, h, Corners.themed(radius), color, color, color, color, ui.alpha);
      }
   }

   public static void rect(MenuUi ui, float x, float y, float w, float h, Corners corners, int color) {
      if (!(w <= 0.0F) && !(h <= 0.0F) && ColorUtil.alpha(color) != 0) {
         Surfaces.fill(ui.g, x, y, w, h, corners, color, color, color, color, ui.alpha);
      }
   }

   public static void pill(MenuUi ui, float x, float y, float w, float h, int color) {
      if (!(w <= 0.0F) && !(h <= 0.0F) && ColorUtil.alpha(color) != 0) {
         Surfaces.fill(ui.g, x, y, w, h, Corners.round(Math.min(w, h) / 2.0F), color, color, color, color, ui.alpha);
      }
   }

   public static void pillBorder(MenuUi ui, float x, float y, float w, float h, float thickness, int color) {
      if (!(w <= 0.0F) && !(h <= 0.0F) && ColorUtil.alpha(color) != 0) {
         Surfaces.border(ui.g, x, y, w, h, Corners.round(Math.min(w, h) / 2.0F), thickness, color, ui.alpha);
      }
   }

   public static void gradient(MenuUi ui, float x, float y, float w, float h, float radius, int left, int right) {
      if (!(w <= 0.0F) && !(h <= 0.0F)) {
         Surfaces.fill(ui.g, x, y, w, h, Corners.themed(radius), left, right, right, left, ui.alpha);
      }
   }

   public static void border(MenuUi ui, float x, float y, float w, float h, float radius, float thickness, int color) {
      if (!(w <= 0.0F) && !(h <= 0.0F) && ColorUtil.alpha(color) != 0) {
         Surfaces.border(ui.g, x, y, w, h, Corners.themed(radius), thickness, color, ui.alpha);
      }
   }

   public static void shadow(MenuUi ui, float x, float y, float w, float h, float radius, Shadow shadow) {
      Surfaces.shadow(ui.g, x, y, w, h, Corners.themed(radius), shadow, ui.alpha);
   }

   public static void hline(MenuUi ui, float x, float y, float w, int color) {
      rect(ui, x, y, w, 0.6F, 0.0F, color);
   }

   public static void vline(MenuUi ui, float x, float y, float h, int color) {
      rect(ui, x, y, 0.6F, h, 0.0F, color);
   }

   public static void circle(MenuUi ui, float cx, float cy, float r, int color) {
      Render2D.circle(ui.g, cx, cy, r, ColorUtil.fade(color, ui.alpha));
   }

   public static void glass(MenuUi ui, float x, float y, float w, float h, Corners corners, Glass glass, boolean refresh) {
      if (!(w <= 0.0F) && !(h <= 0.0F)) {
         LiquidGlass.Batch batch = LiquidGlass.shapes().add(x, y, w, h, corners);
         if (refresh) {
            batch.refreshBackdrop();
         }

         batch.draw(ui.g, glass, 0.0F, ui.alpha);
      }
   }

   public static void popup(MenuUi ui, float x, float y, float w, float h, float radius) {
      MenuPalette p = ui.palette;
      Surfaces.shadow(ui.g, x, y + 5.0F, w, h, Corners.themed(radius), new Shadow(1509949440, 18.0F, 0.0F, 0.0F, 6.0F), ui.alpha);
      Glass glass = p.glass().copy();
      glass.tint = Paint.solid(p.popupTint());
      glass.blur = Math.max(glass.blur, 0.9F);
      glass.refraction = 5.0F;
      glass.bevel = Math.min(glass.bevel, 7.0F);
      glass.depth = 0.03F;
      glass(ui, x, y, w, h, Corners.themed(radius), glass, true);
   }

   public static void lens(MenuUi ui, float x0, float y0, float x1, float y1, float w, float h, float radius, int tint, float merge, float alpha) {
      Glass glass = Glass.lens();
      glass.tint = Paint.solid(tint);
      glass.refraction = 3.5F;
      glass.specular = 0.55F;
      glass.bevel = Math.min(7.0F, h / 2.0F);
      LiquidGlass.shapes()
         .add(x0, y0, w, h, Corners.round(radius))
         .add(x1, y1, w, h, Corners.round(radius))
         .refreshBackdrop()
         .draw(ui.g, glass, merge, ui.alpha * alpha);
   }
}
