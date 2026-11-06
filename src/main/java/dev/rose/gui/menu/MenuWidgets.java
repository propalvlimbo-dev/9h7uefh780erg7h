package dev.rose.gui.menu;

import dev.rose.gui.UiSounds;
import dev.rose.render.Surfaces;
import dev.rose.render.font.SdfFont;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Shadow;
import dev.rose.setting.KeySetting;
import dev.rose.setting.NumberSetting;
import dev.rose.ui.LiquidSlider;
import dev.rose.ui.LiquidToggle;
import dev.rose.ui.SegmentedControl;
import dev.rose.util.ColorUtil;
import java.util.Locale;
import java.util.function.IntConsumer;
import net.minecraft.client.resources.language.I18n;


public final class MenuWidgets {
   public static final float FIELD_H = 18.0F;
   public static final float TOGGLE_W = 30.0F;
   public static final float TOGGLE_H = 17.0F;
   public static final float SLIDER_H = 16.0F;
   public static final float SEGMENTED_H = 18.0F;
   public static final float CHIP_H = 18.0F;
   private static final float CHIP_GAP = 5.0F;
   private static final float CHIP_PAD = 9.0F;
   private static final float CHIP_TEXT = 7.0F;

   private MenuWidgets() {
   }

   public static float sectionHeader(MenuUi ui, int icon, String title, float x, float y) {
      MenuPalette p = ui.palette;
      float titleX = x + 12.0F;
      if (icon != 0) {
         ui.icon(icon, x + 16.0F, y + 10.0F, 8.0F, p.accentText());
         titleX = x + 24.0F;
      }

      ui.textCentered(ui.semibold, title.toUpperCase(Locale.ROOT), titleX, y + 10.0F, 6.0F, 0.8F, ColorUtil.fade(p.text(), 0.46F));
      return 20.0F;
   }

   public static void toggle(MenuUi ui, String id, float x, float y, float w, float h, boolean on, Runnable onToggle) {
      MenuPalette p = ui.palette;
      LiquidToggle toggle = ui.toggle(id, on);
      toggle.squeeze(ui.pressed(id));
      toggle.render(ui.g, x, y, w, h, ui.alpha, p.accent(), p.track());
      ui.target(id, x - 5.0F, y - 6.0F, w + 10.0F, h + 12.0F, MenuUi.click(() -> {
         onToggle.run();
         UiSounds.play(on ? UiSounds.Sound.TOGGLE_OFF : UiSounds.Sound.TOGGLE_ON);
      }));
   }

   public static void toggle(MenuUi ui, String id, float x, float y, boolean on, Runnable onToggle) {
      toggle(ui, id, x, y, 30.0F, 17.0F, on, onToggle);
   }

   public static void toggleVisual(MenuUi ui, String id, float x, float y, float w, float h, boolean on, boolean pressed) {
      MenuPalette p = ui.palette;
      LiquidToggle toggle = ui.toggle(id, on);
      toggle.squeeze(pressed);
      toggle.render(ui.g, x, y, w, h, ui.alpha, p.accent(), p.track());
   }

   public static void slider(MenuUi ui, String id, float x, float y, float w, NumberSetting setting) {
      MenuPalette p = ui.palette;
      LiquidSlider slider = ui.slider(id, (float)setting.normalized());
      boolean dragging = ui.pressed(id);
      boolean hovered = ui.hot(id);
      String value = setting.format();
      float bubbleText = ui.semibold.width(value, 7.0F) + 12.0F;
      float[] bubble = slider.render(ui.g, x, y, w, 16.0F, ui.alpha, p.accent(), p.track(), dragging, hovered, bubbleText, p.glass());
      float visible = slider.bubbleVisibility();
      if (visible > 0.01F) {
         float tw = ui.semibold.width(value, 7.0F);
         ui.textCentered(ui.semibold, value, bubble[0] - tw / 2.0F, bubble[1], 7.0F, ColorUtil.fade(p.textOnAccent(), visible));
      }

      ui.target(id, x - 6.0F, y - 4.0F, w + 12.0F, 24.0F, new MenuUi.Handler() {
         @Override
         public void press(int button, float localX, float localY) {
            if (button == 1) {
               setting.reset();
               UiSounds.play(UiSounds.Sound.TOGGLE_OFF);
            } else {
               this.apply(localX);
            }
         }

         @Override
         public void drag(float localX, float localY) {
            this.apply(localX);
         }

         private void apply(float localX) {
            double before = setting.normalized();
            setting.setNormalized(LiquidSlider.valueAt(localX - 6.0F, w));
            if (setting.normalized() != before) {
               UiSounds.play(UiSounds.Sound.SLIDER);
            }
         }
      });
   }

   public static float buttonWidth(MenuUi ui, int icon, String label) {
      float w = 16.0F;
      if (icon != 0) {
         w += 10.0F;
      }

      if (!label.isEmpty()) {
         w += ui.medium.width(label, 7.2F) + (icon != 0 ? 4 : 0);
      }

      return Math.max(w, 22.0F);
   }

   public static void button(MenuUi ui, String id, float x, float y, float w, float h, int icon, String label, MenuWidgets.Style style, Runnable action) {
      MenuPalette p = ui.palette;
      boolean hot = ui.hot(id);
      float hover = ui.anim(id + ":hover", hot ? 1.0F : 0.0F, 20.0F);
      float press = ui.anim(id + ":press", ui.pressed(id) ? 1.0F : 0.0F, 30.0F);
      float grow = 0.6F * press;
      float bx = x + grow;
      float by = y + grow;
      float bw = w - grow * 2.0F;
      float bh = h - grow * 2.0F;
      switch (style) {
         case NORMAL:
            MenuDraw.pill(ui, bx, by, bw, bh, ColorUtil.lerp(p.field(), p.groupHover(), hover));
            MenuDraw.pillBorder(ui, bx, by, bw, bh, 0.7F, p.border());
            break;
         case ACCENT:
            Surfaces.shadow(
               ui.g, bx, by + 1.0F, bw, bh, Corners.round(bh / 2.0F), new Shadow(ColorUtil.withAlpha(p.accent(), 80), 5.0F, 0.0F, 0.0F, 1.0F), ui.alpha
            );
            MenuDraw.pill(ui, bx, by, bw, bh, ColorUtil.lerp(p.accent(), -1, 0.12F * hover));
            break;
         case DANGER:
            MenuDraw.pill(ui, bx, by, bw, bh, ColorUtil.withAlpha(p.danger(), Math.round(64.0F + 40.0F * hover)));
            break;
         case GHOST:
            MenuDraw.pill(ui, bx, by, bw, bh, p.hover(hover));
      }
      int textColor = switch (style) {
         case ACCENT -> p.textOnAccent();
         case DANGER -> ColorUtil.lerp(p.danger(), p.text(), 0.3F);
         default -> ColorUtil.lerp(p.textDim(), p.text(), hover);
      };
      float contentW = (icon != 0 ? 10 : 0) + (label.isEmpty() ? 0.0F : ui.medium.width(label, 7.2F) + (icon != 0 ? 4 : 0));
      float cx = x + (w - contentW) / 2.0F;
      if (icon != 0) {
         int iconColor = style != MenuWidgets.Style.NORMAL && style != MenuWidgets.Style.GHOST ? textColor : ColorUtil.lerp(p.textDim(), p.accentText(), hover);
         ui.icon(icon, cx + 5.0F, y + h / 2.0F, 9.0F, iconColor);
         cx += 14.0F;
      }

      if (!label.isEmpty()) {
         ui.textCentered(ui.medium, label, cx, y + h / 2.0F, 7.2F, textColor);
      }

      ui.target(id, x, y, w, h, MenuUi.click(() -> {
         action.run();
         UiSounds.play(UiSounds.Sound.CLICK);
      }));
   }

   public static void iconButton(MenuUi ui, String id, float x, float y, float size, int icon, int color, Runnable action) {
      MenuPalette p = ui.palette;
      boolean hot = ui.hot(id);
      float hover = ui.anim(id + ":hover", hot ? 1.0F : 0.0F, 20.0F);
      if (hover > 0.01F) {
         MenuDraw.pill(ui, x, y, size, size, p.hover(hover * 1.6F));
      }

      ui.icon(icon, x + size / 2.0F, y + size / 2.0F, size * 0.52F, ColorUtil.lerp(color, p.text(), hover));
      ui.target(id, x, y, size, size, MenuUi.click(() -> {
         action.run();
         UiSounds.play(UiSounds.Sound.CLICK);
      }));
   }

   public static void segmented(MenuUi ui, String id, float x, float y, float w, String[] labels, int selected, IntConsumer onSelect) {
      MenuPalette p = ui.palette;
      SegmentedControl control = ui.segmented(id, labels, selected);
      control.render(ui.g, x, y, w, 18.0F, p.glass(), ui.alpha);
      ui.target(id, x, y, w, 18.0F, new MenuUi.Handler() {
         @Override
         public void release(float localX, float localY, boolean inside) {
            if (inside) {
               int index = Math.clamp((int)Math.floor(localX / (w / labels.length)), 0, labels.length - 1);
               if (index != selected) {
                  onSelect.accept(index);
                  UiSounds.play(UiSounds.Sound.CLICK);
               }
            }
         }
      });
   }

   public static boolean fitsSegmented(MenuUi ui, String[] labels, float w) {
      if (labels.length > 4) {
         return false;
      }

      float segment = w / labels.length;

      for (String label : labels) {
         if (ui.semibold.width(label, 7.56F) + 14.0F > segment) {
            return false;
         }
      }

      return true;
   }

   public static float chipsHeight(MenuUi ui, String[] labels, float w) {
      int lines = 1;
      float lineX = 0.0F;

      for (String label : labels) {
         float chip = Math.min(w, ui.medium.width(label, 7.0F) + 18.0F);
         if (lineX > 0.0F && lineX + chip > w) {
            lines++;
            lineX = 0.0F;
         }

         lineX += chip + 5.0F;
      }

      return lines * 18.0F + (lines - 1) * 5.0F;
   }

   public static void chips(MenuUi ui, String id, float x, float y, float w, String[] labels, int selected, IntConsumer onSelect) {
      MenuPalette p = ui.palette;
      float lineX = 0.0F;
      float lineY = y;

      for (int i = 0; i < labels.length; i++) {
         String text = labels[i];
         float chipW = Math.min(w, ui.medium.width(text, 7.0F) + 18.0F);
         if (lineX > 0.0F && lineX + chipW > w) {
            lineX = 0.0F;
            lineY += 23.0F;
         }

         float cx = x + lineX;
         String chipId = id + ":" + i;
         boolean chosen = i == selected;
         float t = ui.anim(chipId + ":on", chosen ? 1.0F : 0.0F, 16.0F);
         float hover = ui.anim(chipId + ":hover", ui.hot(chipId) ? 1.0F : 0.0F, 20.0F);
         int fill = ColorUtil.lerp(ColorUtil.lerp(p.track(), p.groupHover(), hover * 0.6F), p.accent(), t);
         MenuDraw.pill(ui, cx, lineY, chipW, 18.0F, fill);
         SdfFont font = t > 0.5F ? ui.semibold : ui.medium;
         String shown = MenuUi.ellipsize(font, text, 7.0F, chipW - 18.0F + 2.0F);
         ui.textCentered(font, shown, cx + 9.0F, lineY + 9.0F, 7.0F, ColorUtil.lerp(p.text(), p.textOnAccent(), t));
         int index = i;
         ui.target(chipId, cx, lineY, chipW, 18.0F, MenuUi.click(() -> {
            if (index != selected) {
               onSelect.accept(index);
               UiSounds.play(UiSounds.Sound.CLICK);
            }
         }));
         lineX += chipW + 5.0F;
      }
   }

   public static void dropdown(MenuUi ui, String id, float x, float y, float w, String[] labels, int selected, IntConsumer onSelect) {
      MenuPalette p = ui.palette;
      boolean open = id.equals(ui.dropdownId);
      float hover = ui.anim(id + ":hover", !ui.hot(id) && !open ? 0.0F : 1.0F, 20.0F);
      MenuDraw.pill(ui, x, y, w, 18.0F, ColorUtil.lerp(p.field(), p.groupHover(), hover));
      MenuDraw.pillBorder(ui, x, y, w, 18.0F, 0.8F, open ? ColorUtil.withAlpha(p.accent(), 192) : p.border());
      String label = selected >= 0 && selected < labels.length ? labels[selected] : "";
      ui.textCentered(ui.medium, MenuUi.ellipsize(ui.medium, label, 7.2F, w - 26.0F), x + 9.0F, y + 9.0F, 7.2F, p.text());
      float arrow = ui.anim(id + ":arrow", open ? 1.0F : 0.0F, 20.0F);
      ui.icon(arrow > 0.5F ? 57456 : 57453, x + w - 11.0F, y + 9.0F, 8.0F, p.textDim());
      if (open) {
         ui.dropdownRect = new float[]{x, y, w, 18.0F};
         ui.dropdownLabels = labels;
         ui.dropdownSelected = selected;
         ui.dropdownPick = onSelect;
      }

      ui.target(id, x, y, w, 18.0F, MenuUi.click(() -> {
         boolean wasOpen = id.equals(ui.dropdownId);
         ui.closePopups();
         if (!wasOpen) {
            ui.dropdownId = id;
            ui.dropdownRect = new float[]{x, y, w, 18.0F};
            ui.dropdownLabels = labels;
            ui.dropdownSelected = selected;
            ui.dropdownPick = onSelect;
            ui.snap(id + ":appear", 0.0F);
         }

         UiSounds.play(UiSounds.Sound.CLICK);
      }));
   }

   public static void drawDropdownList(MenuUi ui) {
      if (ui.dropdownId != null && ui.dropdownRect != null && ui.dropdownLabels != null) {
         MenuPalette p = ui.palette;
         float[] anchor = ui.dropdownRect;
         String[] labels = ui.dropdownLabels;
         float rowH = 18.0F;
         float pad = 4.0F;
         int visible = Math.min(labels.length, 12);
         float listH = visible * rowH + pad * 2.0F;
         float w = anchor[2];

         for (String label : labels) {
            w = Math.max(w, ui.medium.width(label, 7.2F) + 32.0F);
         }

         float x = Math.clamp(anchor[0], 4.0F, Math.max(4.0F, ui.screenWidth - w - 4.0F));
         float y = anchor[1] + anchor[3] + 4.0F;
         if (y + listH > ui.screenHeight - 4.0F) {
            y = anchor[1] - listH - 4.0F;
         }

         String owner = ui.dropdownId;
         float appear = ui.anim(owner + ":appear", 1.0F, 24.0F);
         float previous = ui.alpha;
         ui.alpha = previous * appear;
         ui.setLayer(9);
         ui.target("dropdown_catcher", -100000.0F, -100000.0F, 200000.0F, 200000.0F, MenuUi.click(ui::closePopups, ui::closePopups));
         ui.setLayer(10);
         float top = y - (1.0F - appear) * 5.0F;
         MenuDraw.popup(ui, x, top, w, listH, 11.0F);
         ui.target(owner + ":list", x, top, w, listH, new MenuUi.Handler() {});
         IntConsumer pick = ui.dropdownPick;
         int first = Math.clamp(ui.dropdownSelected - visible / 2, 0, Math.max(0, labels.length - visible));

         for (int i = 0; i < visible; i++) {
            int index = first + i;
            float ry = top + pad + i * rowH;
            String rowId = owner + ":row:" + index;
            boolean hot = ui.hot(rowId);
            boolean selected = index == ui.dropdownSelected;
            if (hot) {
               MenuDraw.rect(ui, x + 4.0F, ry, w - 8.0F, rowH, 7.0F, p.groupHover());
            }

            if (selected) {
               ui.icon(57452, x + w - 13.0F, ry + rowH / 2.0F, 8.0F, p.accentText());
            }

            SdfFont font = selected ? ui.semibold : ui.medium;
            ui.textCentered(
               font, MenuUi.ellipsize(font, labels[index], 7.2F, w - 34.0F), x + 11.0F, ry + rowH / 2.0F, 7.2F, !selected && !hot ? p.textDim() : p.text()
            );
            ui.target(rowId, x + 4.0F, ry, w - 8.0F, rowH, MenuUi.click(() -> {
               ui.closePopups();
               if (pick != null) {
                  pick.accept(index);
               }

               UiSounds.play(UiSounds.Sound.CLICK);
            }));
         }

         ui.setLayer(0);
         ui.alpha = previous;
      }
   }

   public static float keyWidth(MenuUi ui, KeySetting setting, boolean listening) {
      String label = listening ? I18n.get("rose.menu.press_key", new Object[0]) : setting.keyName();
      return Math.max(44.0F, ui.medium.width(label, 7.2F) + 28.0F);
   }

   public static void keybind(MenuUi ui, String id, float x, float y, KeySetting setting) {
      MenuPalette p = ui.palette;
      boolean listening = id.equals(ui.listeningId);
      String label = listening ? I18n.get("rose.menu.press_key", new Object[0]) : setting.keyName();
      float w = keyWidth(ui, setting, listening);
      float hover = ui.anim(id + ":hover", ui.hot(id) ? 1.0F : 0.0F, 20.0F);
      float listen = ui.anim(id + ":listen", listening ? 1.0F : 0.0F, 18.0F);
      MenuDraw.pill(ui, x, y, w, 18.0F, ColorUtil.lerp(p.track(), p.groupHover(), hover * 0.5F));
      if (listen > 0.01F) {
         float pulse = 0.6F + 0.4F * (float)Math.sin(System.currentTimeMillis() / 150.0);
         MenuDraw.pill(ui, x, y, w, 18.0F, ColorUtil.withAlpha(p.accent(), Math.round(64.0F * listen)));
         MenuDraw.pillBorder(ui, x, y, w, 18.0F, 1.0F, ColorUtil.withAlpha(p.accent(), Math.round(255.0F * listen * pulse)));
      }

      ui.icon(57988, x + 11.0F, y + 9.0F, 9.0F, ColorUtil.lerp(p.textDim(), p.accentText(), listen));
      ui.textCentered(ui.medium, label, x + 20.0F, y + 9.0F, 7.2F, !setting.isBound() && !listening ? p.textDim() : p.text());
      ui.target(id, x, y, w, 18.0F, MenuUi.click(() -> {
         if (listening) {
            ui.listening = null;
            ui.listeningId = null;
         } else {
            ui.closePopups();
            ui.listen(id, setting::set);
         }

         UiSounds.play(UiSounds.Sound.CLICK);
      }, () -> setting.set(-1)));
   }

   public static void textField(MenuUi ui, String id, float x, float y, float w, TextBuffer buffer, String placeholder, Runnable onChange, Runnable onSubmit) {
      MenuPalette p = ui.palette;
      boolean focused = buffer == ui.focused;
      float hover = ui.anim(id + ":hover", ui.hot(id) && !focused ? 1.0F : 0.0F, 20.0F);
      float focus = ui.anim(id + ":focus", focused ? 1.0F : 0.0F, 20.0F);
      MenuDraw.pill(ui, x, y, w, 18.0F, ColorUtil.lerp(p.track(), p.groupHover(), hover * 0.5F));
      if (focus > 0.01F) {
         MenuDraw.pillBorder(ui, x, y, w, 18.0F, 1.0F, ColorUtil.withAlpha(p.accent(), Math.round(224.0F * focus)));
      }

      SdfFont font = ui.regular;
      float size = 7.4F;
      boolean empty = buffer.isEmpty();
      ui.clip(x + 6.0F, y, w - 12.0F, 18.0F);
      String shown = empty && !focused ? placeholder : buffer.text();
      float caretOffset = font.width(buffer.text().substring(0, buffer.cursor()), size);
      float shift = Math.max(0.0F, caretOffset - (w - 22.0F));
      ui.textCentered(font, shown, x + 10.0F - (focused ? shift : 0.0F), y + 9.0F, size, empty && !focused ? p.textFaint() : p.text());
      if (focused && System.currentTimeMillis() / 530L % 2L == 0L) {
         MenuDraw.rect(ui, x + 10.0F + caretOffset - shift, y + 4.5F, 0.8F, 9.0F, 0.0F, p.accent());
      }

      ui.unclip();
      ui.target(id, x, y, w, 18.0F, MenuUi.click(() -> ui.focus(id, buffer, onChange, onSubmit)));
   }

   public enum Style {
      NORMAL,
      ACCENT,
      DANGER,
      GHOST;
   }
}
