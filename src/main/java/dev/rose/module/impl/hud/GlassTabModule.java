package dev.rose.module.impl.hud;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.render.Backdrop;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.style.Corner;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import net.minecraft.client.gui.GuiGraphics;


public final class GlassTabModule extends Module {
   public GlassTabModule() {
      super("glass_tab", Category.HUD, false);
   }

   @Override
   public int icon() {
      return 57764;
   }

   public void drawPanel(GuiGraphics g, float x0, float y0, float x1, float y1, boolean top, boolean bottom) {
      Backdrop.capture();
      float pad = 3.0F;
      float x = x0 - pad;
      float y = y0 - (top ? pad : 0.0F);
      float w = x1 - x0 + pad * 2.0F;
      float h = y1 - y0 + (top ? pad : 0.0F) + (bottom ? pad : 0.0F);
      Corners round = Corners.themed(10.0F);
      Corners corners = new Corners(
         top ? round.topLeft() : Corner.NONE,
         top ? round.topRight() : Corner.NONE,
         bottom ? round.bottomRight() : Corner.NONE,
         bottom ? round.bottomLeft() : Corner.NONE
      );
      Glass glass = Theme.glass().copy();
      glass.tint = Paint.solid(-1609560044);
      if (top) {
         Surfaces.shadow(g, x, y + 2.0F, w, h, corners, new Shadow(1073741824, 8.0F, 0.0F, 0.0F, 2.0F), 1.0F);
      }

      LiquidGlass.draw(g, x, y, w, h, corners, glass, 1.0F);
   }
}
