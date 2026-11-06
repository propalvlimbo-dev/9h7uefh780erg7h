package dev.rose.ui;

import dev.rose.anim.Spring;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Render2D;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.style.Glass;
import dev.rose.util.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;


public final class SegmentedControl {
   private static final Glass LENS = Glass.lens();
   private final String[] options;
   private final Spring head = new Spring(0.0, 0.32, 0.75);
   private final Spring tail = new Spring(0.0, 0.55, 0.95);
   private int selected;

   public SegmentedControl(String... options) {
      this.options = options;
   }

   public void select(int index) {
      this.selected = Math.clamp(index, 0, this.options.length - 1);
      this.head.setTarget(this.selected);
      this.tail.setTarget(this.selected);
   }

   public int selected() {
      return this.selected;
   }

   public void render(GuiGraphics g, float x, float y, float w, float h, Glass glass, float alpha) {
      SdfFont regular = Fonts.get(FontWeight.MEDIUM);
      SdfFont bold = Fonts.get(FontWeight.SEMIBOLD);
      if (regular != null && bold != null) {
         Render2D.rect(g, x, y, w, h, h / 2.0F, ColorUtil.fade(Controls.track(glass), alpha));
         float segment = w / this.options.length;
         float inset = 2.0F;
         float pillW = segment - inset * 2.0F;
         float pillH = h - inset * 2.0F;
         float a = this.head.getFloat();
         float b = this.tail.getFloat();
         float ax = x + inset + a * segment;
         float bx = x + inset + b * segment;
         float motion = Math.min((float)Math.abs(this.head.velocity()) / 4.0F, 1.0F);
         float left = Math.min(ax, bx);
         float right = Math.max(ax, bx) + pillW;
         Render2D.shadow(g, left, y + inset + 0.8F, right - left, pillH, pillH / 2.0F, 3.0F, ColorUtil.fade(805306368, alpha));
         Render2D.rect(g, ax, y + inset, pillW, pillH, pillH / 2.0F, ColorUtil.fade(Controls.selection(glass), alpha * (1.0F - motion)));
         LiquidGlass.shapes()
            .add(ax, y + inset, pillW, pillH, pillH / 2.0F)
            .add(bx, y + inset, pillW, pillH, pillH / 2.0F)
            .refreshBackdrop()
            .draw(g, LENS, pillH * 0.9F, alpha * (0.35F + 0.65F * motion));
         float size = h * 0.42F;

         for (int i = 0; i < this.options.length; i++) {
            float closeness = 1.0F - Math.min(Math.abs(a - i), 1.0F);
            SdfFont font = closeness > 0.5F ? bold : regular;
            float textWidth = font.width(this.options[i], size);
            font.draw(
               g,
               this.options[i],
               x + segment * i + (segment - textWidth) / 2.0F,
               y + (h - size) / 2.0F,
               size,
               ColorUtil.fade(glass.label, alpha * (0.6F + 0.4F * closeness))
            );
         }
      }
   }
}
