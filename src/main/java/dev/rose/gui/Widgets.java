package dev.rose.gui;

import dev.rose.gui.UiSounds.Sound;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.style.Corners;
import dev.rose.util.ColorUtil;


final class Widgets {
   private Widgets() {
   }

   static float pillWidth(GuiContext ctx, int icon, String label, float h) {
      float size = h * 0.4F;
      float iconW = icon != 0 ? h * 0.5F + (label.isEmpty() ? 0 : 4) : 0.0F;
      return Math.max(h, iconW + ctx.medium.width(label, size) + h * 0.8F);
   }

   static float pill(GuiContext ctx, String id, float x, float y, float h, int icon, String label, Widgets.Style style, Runnable action) {
      float w = pillWidth(ctx, icon, label, h);
      float hover = ctx.animate("pill_hover:" + id, ctx.hovered(x, y, w, h) ? 1.0F : 0.0F, 0.18, 1.0);
      float press = ctx.animate("pill_press:" + id, id.equals(ctx.pressed) ? 1.0F : 0.0F, 0.12, 0.9);
      int accent = Theme.accent();

      int base = switch (style) {
         case NORMAL -> ctx.track();
         case ACCENT -> ColorUtil.withAlpha(accent, 224);
         case DANGER -> ColorUtil.withAlpha(Theme.danger, 224);
      };
      int fill = ColorUtil.lerp(base, ColorUtil.lerp(base, -1, 0.25F), hover * 0.6F);
      float squish = 1.0F - 0.05F * press;
      float pw = w * squish;
      float ph = h * squish;
      float px = x + (w - pw) / 2.0F;
      float py = y + (h - ph) / 2.0F;
      Surfaces.fill(ctx.g, px, py, pw, ph, Corners.round(ph / 2.0F), fill, fill, fill, fill, ctx.alpha);
      int color = style == Widgets.Style.NORMAL ? ctx.label : -1;
      float size = h * 0.4F;
      float contentW = (icon != 0 ? h * 0.5F + (label.isEmpty() ? 0 : 4) : 0.0F) + ctx.medium.width(label, size);
      float cursor = x + (w - contentW) / 2.0F;
      if (icon != 0) {
         ctx.icon(icon, cursor + h * 0.25F, y + h / 2.0F, h * 0.5F, color);
         cursor += h * 0.5F + 4.0F;
      }

      if (!label.isEmpty()) {
         ctx.text(ctx.medium, label, cursor, y + h / 2.0F - size * 0.62F, size, color);
      }

      ctx.target(id, x, y, w, h, new GuiContext.Interaction() {
         @Override
         public void press(float localX, float localY) {
            ctx.pressed = id;
         }

         @Override
         public void release(float localX, float localY, boolean inside) {
            ctx.pressed = null;
            if (inside) {
               UiSounds.play(Sound.CLICK);
               action.run();
            }
         }
      });
      return w;
   }

   enum Style {
      NORMAL,
      ACCENT,
      DANGER;
   }
}
