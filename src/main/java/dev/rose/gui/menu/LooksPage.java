package dev.rose.gui.menu;

import dev.rose.looks.Look;
import dev.rose.module.impl.visual.LooksModule;
import dev.rose.render.Surfaces;
import dev.rose.render.style.Corners;
import dev.rose.util.ColorUtil;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.resources.language.I18n;


public final class LooksPage {
   private static final float CARD_H = 56.0F;
   private static final float GAP = 6.0F;

   public float draw(MenuUi ui, LooksModule module, float x, float y, float w) {
      MenuPalette p = ui.palette;
      float fullAlpha = ui.alpha;
      float cursor = y;

      for (String line : MenuUi.wrap(ui.regular, I18n.get("rose.look.intro", new Object[0]), 6.8F, w - 8.0F)) {
         ui.alpha = fullAlpha * ui.edgeFade(cursor, 10.0F);
         ui.text(ui.regular, line, x + 4.0F, cursor, 6.8F, p.textDim());
         cursor += 10.0F;
      }

      cursor += 6.0F;
      Look active = module.last();
      int index = 0;

      for (Look look : Look.values()) {
         String key = "rose.look." + look.name().toLowerCase(Locale.ROOT);
         String id = "look:" + look.name();
         float appear = ui.bounce("look:appear:" + look.name(), 1.0F, 0.4 + index * 0.05, 0.76);
         float top = cursor + (1.0F - appear) * 10.0F * ui.motion;
         ui.alpha = fullAlpha * Math.clamp(appear, 0.0F, 1.0F) * ui.edgeFade(top, 56.0F);
         if (ui.alpha > 0.004F) {
            boolean hover = ui.inside(x, top, w, 56.0F);
            float lift = ui.anim(id + ":hover", hover ? 1.0F : 0.0F, 16.0F);
            MenuDraw.rect(ui, x, top, w, 56.0F, 14.0F, ColorUtil.lerp(p.group(), p.groupHover(), lift));
            float swatch = 40.0F;
            float sx = x + 8.0F;
            float sy = top + 8.0F;
            Surfaces.fill(ui.g, sx, sy, swatch, swatch, Corners.themed(11.0F), look.primary(), look.secondary(), look.tertiary(), look.primary(), ui.alpha);
            if (look == active) {
               MenuDraw.circle(ui, sx + swatch - 5.0F, sy + 5.0F, 5.5F, -435154924);
               ui.icon(57452, sx + swatch - 5.0F, sy + 5.0F, 7.0F, -1);
            }

            String label = I18n.get(look == active ? "rose.look.again" : "rose.look.apply", new Object[0]);
            float buttonW = MenuWidgets.buttonWidth(ui, 57452, label);
            float buttonX = x + w - 10.0F - buttonW;
            float textX = sx + swatch + 10.0F;
            float textW = buttonX - 8.0F - textX;
            ui.text(ui.semibold, MenuUi.ellipsize(ui.semibold, I18n.get(key, new Object[0]), 8.6F, textW), textX, top + 11.0F, 8.6F, p.text());
            List<String> lines = MenuUi.wrap(ui.regular, I18n.get(key + ".description", new Object[0]), 6.4F, textW);

            for (int i = 0; i < Math.min(2, lines.size()); i++) {
               ui.text(ui.regular, lines.get(i), textX, top + 24.0F + i * 9.5F, 6.4F, p.textDim());
            }

            MenuWidgets.button(
               ui,
               id,
               buttonX,
               top + 19.0F,
               buttonW,
               18.0F,
               57452,
               label,
               look == active ? MenuWidgets.Style.NORMAL : MenuWidgets.Style.ACCENT,
               () -> module.apply(look, true)
            );
         }

         cursor += 62.0F;
         index++;
      }

      ui.alpha = fullAlpha;
      return cursor - y;
   }

   public void restartAppear(MenuUi ui) {
      for (Look look : Look.values()) {
         ui.spring("look:appear:" + look.name(), 1.0, 0.4, 0.76).snap(0.0);
      }
   }
}
