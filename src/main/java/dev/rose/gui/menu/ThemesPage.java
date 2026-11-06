package dev.rose.gui.menu;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.rose.config.ThemeLibrary;
import dev.rose.gui.UiSounds;
import dev.rose.module.impl.ThemeModule;
import dev.rose.notify.Notifications;
import dev.rose.render.Render2D;
import dev.rose.render.Surfaces;
import dev.rose.render.style.Corner;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.util.ColorUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;


public final class ThemesPage {
   private static final float CARD_H = 56.0F;
   private static final float CARD_MIN_W = 72.0F;
   private static final float CARD_GAP = 6.0F;
   private static final float ROW = 32.0F;
   private static final float GAP = 10.0F;
   private static final long CONFIRM_MS = 3000L;
   private final TextBuffer name = new TextBuffer(32);
   private final Map<String, ThemesPage.Preview> previews = new HashMap<>();
   private List<String> saved;
   private String pendingDelete;
   private long pendingSince;

   public void refresh() {
      this.saved = ThemeLibrary.saved();
      this.previews.clear();

      for (String theme : this.saved) {
         this.previews.put(theme, preview(ThemeLibrary.read(theme)));
      }

      this.pendingDelete = null;
   }

   public float draw(MenuUi ui, float x, float y, float w) {
      if (this.saved == null) {
         this.refresh();
      }

      float cursor = y;
      cursor += MenuWidgets.sectionHeader(ui, 58783, I18n.get("rose.themes.section.gallery", new Object[0]), x, cursor);
      cursor = this.gallery(ui, x, cursor, w) + 10.0F;
      cursor += MenuWidgets.sectionHeader(ui, 57677, I18n.get("rose.themes.section.save", new Object[0]), x, cursor);
      cursor = this.saveRow(ui, x, cursor, w) + 10.0F;
      cursor += MenuWidgets.sectionHeader(ui, 57502, I18n.get("rose.themes.section.share", new Object[0]), x, cursor);
      cursor = this.share(ui, x, cursor, w) + 10.0F;
      return cursor - y;
   }

   private float gallery(MenuUi ui, float x, float y, float w) {
      int columns = Math.max(2, (int)((w + 6.0F) / 78.0F));
      float cardW = (w - 6.0F * (columns - 1)) / columns;
      int index = 0;

      for (ThemeLibrary.Builtin theme : ThemeLibrary.Builtin.values()) {
         float cx = x + index % columns * (cardW + 6.0F);
         float cy = y + index / columns * 62.0F;
         String label = I18n.get("rose.themes.builtin." + theme.name().toLowerCase(Locale.ROOT), new Object[0]);
         this.card(ui, "themes:builtin:" + theme.name(), cx, cy, cardW, label, preview(theme), () -> {
            ThemeLibrary.applyBuiltin(theme);
            UiSounds.play(UiSounds.Sound.TAB);
            Notifications.show(Notifications.Kind.SUCCESS, I18n.get("rose.themes.applied", new Object[0]), label);
         }, null);
         index++;
      }

      if (this.pendingDelete != null && System.currentTimeMillis() - this.pendingSince > 3000L) {
         this.pendingDelete = null;
      }

      for (String theme : this.saved) {
         float cx = x + index % columns * (cardW + 6.0F);
         float cy = y + index / columns * 62.0F;
         this.card(
            ui,
            "themes:saved:" + theme,
            cx,
            cy,
            cardW,
            theme,
            this.previews.get(theme),
            () -> {
               boolean loaded = ThemeLibrary.load(theme);
               UiSounds.play(UiSounds.Sound.TAB);
               Notifications.show(
                  loaded ? Notifications.Kind.SUCCESS : Notifications.Kind.ERROR,
                  I18n.get(loaded ? "rose.themes.applied" : "rose.themes.load_failed", new Object[0]),
                  theme
               );
            },
            () -> this.delete(theme)
         );
         index++;
      }

      int rows = (index + columns - 1) / columns;
      return y + rows * 56.0F + (rows - 1) * 6.0F;
   }

   private void card(MenuUi ui, String id, float x, float y, float w, String label, ThemesPage.Preview preview, Runnable apply, Runnable delete) {
      MenuPalette p = ui.palette;
      boolean hovered = ui.inside(x, y, w, 56.0F);
      float hover = ui.anim(id + ":hover", hovered ? 1.0F : 0.0F, 20.0F);
      MenuDraw.rect(ui, x, y, w, 56.0F, 12.0F, p.group());
      if (hover > 0.01F) {
         MenuDraw.border(ui, x, y, w, 56.0F, 8.0F, 1.0F, ColorUtil.withAlpha(p.accent(), Math.round(176.0F * hover)));
      }

      if (preview != null) {
         float px = x + 4.0F;
         float py = y + 4.0F;
         float pw = w - 8.0F;
         float ph = 36.0F;
         int wallA = ColorUtil.lerp(preview.accent, -16777216, 0.35F);
         int wallB = ColorUtil.lerp(preview.accent2, -16777216, 0.45F);
         Surfaces.fill(ui.g, px, py, pw, ph, Corners.themed(5.0F), Paint.linear(35.0F, wallA, wallB), ui.alpha);
         float gx = px + 5.0F;
         float gy = py + 5.0F;
         float gw = pw - 10.0F;
         float gh = ph - 10.0F;
         Corners glassCorners = Corners.all(new Corner(5.0F * preview.scale, preview.shape));
         int tint = ColorUtil.withAlpha(preview.tint, Math.max(64, ColorUtil.alpha(preview.tint)));
         Surfaces.fill(ui.g, gx, gy, gw, gh, glassCorners, tint, tint, tint, tint, ui.alpha);
         Surfaces.border(ui.g, gx, gy, gw, gh, glassCorners, 0.6F, ColorUtil.withAlpha(-1, 64), ui.alpha);
         int line = ColorUtil.withAlpha(preview.label, 192);
         int faint = ColorUtil.withAlpha(preview.label, 96);
         Surfaces.fill(ui.g, gx + 4.0F, gy + 4.0F, gw * 0.45F, 2.2F, Corners.round(1.1F), line, line, line, line, ui.alpha);
         Surfaces.fill(ui.g, gx + 4.0F, gy + 8.5F, gw * 0.3F, 1.8F, Corners.round(0.9F), faint, faint, faint, faint, ui.alpha);
         float pill = Math.max(5.0F, gh * 0.34F);
         Surfaces.fill(
            ui.g,
            gx + gw - 4.0F - pill * 1.9F,
            gy + gh - 3.0F - pill,
            pill * 1.9F,
            pill,
            Corners.round(pill / 2.0F),
            Paint.linear(0.0F, preview.accent, preview.accent2),
            ui.alpha
         );
         Render2D.circle(ui.g, gx + gw - 4.0F - pill / 2.0F, gy + gh - 3.0F - pill / 2.0F, pill * 0.36F, ColorUtil.fade(-1, ui.alpha));
      }

      boolean showDelete = delete != null && (hovered || id.equals("themes:saved:" + this.pendingDelete));
      String text = MenuUi.ellipsize(ui.medium, label, 6.8F, w - 10.0F - (showDelete ? 12 : 0));
      ui.textCentered(ui.medium, text, x + 5.0F, y + 56.0F - 8.0F, 6.8F, p.text());
      ui.target(id, x, y, w, 56.0F, MenuUi.click(apply));
      if (showDelete) {
         boolean confirming = id.equals("themes:saved:" + this.pendingDelete);
         float size = 12.0F;
         float bx = x + w - size - 2.0F;
         float by = y + 56.0F - size - 2.0F;
         if (confirming) {
            MenuDraw.pill(ui, bx, by, size, size, ColorUtil.withAlpha(p.danger(), 64));
         }

         ui.icon(57742, bx + size / 2.0F, by + size / 2.0F, 7.0F, confirming ? p.danger() : p.textDim());
         ui.target(id + ":delete", bx, by, size, size, MenuUi.click(delete));
         ui.tooltip(
            id + ":delete:tip", bx, by, size, size, I18n.get(confirming ? "rose.themes.confirm_delete" : "rose.themes.delete", new Object[0])
         );
      }
   }

   private void delete(String theme) {
      if (!theme.equals(this.pendingDelete)) {
         this.pendingDelete = theme;
         this.pendingSince = System.currentTimeMillis();
      } else {
         ThemeLibrary.delete(theme);
         this.refresh();
         Notifications.show(Notifications.Kind.NEUTRAL, I18n.get("rose.themes.deleted", new Object[0]), theme);
      }
   }

   private float saveRow(MenuUi ui, float x, float y, float w) {
      MenuPalette p = ui.palette;
      float h = 36.0F;
      MenuDraw.rect(ui, x, y, w, h, 14.0F, p.group());
      String save = I18n.get("rose.themes.save", new Object[0]);
      float buttonW = MenuWidgets.buttonWidth(ui, 57677, save);
      float fieldY = y + (h - 18.0F) / 2.0F;
      MenuWidgets.textField(
         ui,
         "themes:name",
         x + 10.0F,
         fieldY,
         w - 28.0F - buttonW,
         this.name,
         I18n.get("rose.themes.name_hint", new Object[0]),
         null,
         this::saveNew
      );
      MenuWidgets.button(
         ui,
         "themes:save",
         x + w - 10.0F - buttonW,
         fieldY,
         buttonW,
         18.0F,
         57677,
         save,
         this.name.isEmpty() ? MenuWidgets.Style.NORMAL : MenuWidgets.Style.ACCENT,
         this::saveNew
      );
      return y + h;
   }

   private void saveNew() {
      String value = this.name.text().trim();
      if (!value.isEmpty()) {
         ThemeLibrary.save(value);
         this.name.clear();
         this.refresh();
         Notifications.show(Notifications.Kind.SUCCESS, I18n.get("rose.themes.saved", new Object[0]), value);
      }
   }

   private float share(MenuUi ui, float x, float y, float w) {
      MenuPalette p = ui.palette;
      float h = 64.0F;
      MenuDraw.rect(ui, x, y, w, h, 14.0F, p.group());
      Minecraft mc = Minecraft.getInstance();
      this.shareRow(
         ui,
         x,
         y,
         w,
         "copy",
         57502,
         () -> {
            mc.keyboardHandler.setClipboard(ThemeLibrary.exportCode());
            Notifications.show(
               Notifications.Kind.SUCCESS,
               I18n.get("rose.themes.copied", new Object[0]),
               I18n.get("rose.themes.copied_hint", new Object[0])
            );
         }
      );
      MenuDraw.hline(ui, x + 10.0F, y + 32.0F, w - 20.0F, p.divider());
      this.shareRow(
         ui,
         x,
         y + 32.0F,
         w,
         "paste",
         58344,
         () -> {
            boolean imported = ThemeLibrary.importCode(mc.keyboardHandler.getClipboard());
            Notifications.show(
               imported ? Notifications.Kind.SUCCESS : Notifications.Kind.ERROR,
               I18n.get(imported ? "rose.themes.imported" : "rose.themes.import_failed", new Object[0]),
               ""
            );
         }
      );
      return y + h;
   }

   private void shareRow(MenuUi ui, float x, float y, float w, String id, int icon, Runnable action) {
      MenuPalette p = ui.palette;
      String button = I18n.get("rose.themes." + id + ".button", new Object[0]);
      float buttonW = MenuWidgets.buttonWidth(ui, icon, button);
      ui.textCentered(
         ui.medium,
         MenuUi.ellipsize(ui.medium, I18n.get("rose.themes." + id, new Object[0]), 8.0F, w - buttonW - 28.0F),
         x + 10.0F,
         y + 16.0F,
         8.0F,
         p.text()
      );
      MenuWidgets.button(ui, "themes:" + id, x + w - 10.0F - buttonW, y + 7.0F, buttonW, 18.0F, icon, button, MenuWidgets.Style.NORMAL, action);
   }

   private static ThemesPage.Preview preview(ThemeLibrary.Builtin theme) {
      Glass glass = switch (theme.glass()) {
         case CLEAR -> Glass.clear();
         case LIGHT -> Glass.light();
         case DARK -> Glass.dark();
      };
      int tint = theme.tint() != 0 ? theme.tint() : glass.tint.color(0);
      return new ThemesPage.Preview(
         theme.palette().primary(),
         theme.palette().secondary(),
         tint,
         label(theme.label(), tint),
         shape(theme.corners()),
         theme.corners() == ThemeModule.CornerStyle.SHARP ? 0.0F : theme.cornerScale() / 100.0F
      );
   }

   private static ThemesPage.Preview preview(JsonObject json) {
      if (json == null) {
         return null;
      }

      int accent = color(json, "accent", ThemeModule.Palette.ROSE.primary());
      int accent2 = color(json, "accent2", ThemeModule.Palette.ROSE.secondary());
      int tint = color(json, "tint", -2146562542);
      ThemeModule.LabelColor label = ThemeModule.LabelColor.AUTO;
      ThemeModule.CornerStyle corners = ThemeModule.CornerStyle.SQUIRCLE;

      try {
         if (json.has("label")) {
            label = ThemeModule.LabelColor.valueOf(json.get("label").getAsString());
         }

         if (json.has("corner_style")) {
            corners = ThemeModule.CornerStyle.valueOf(json.get("corner_style").getAsString());
         }
      } catch (RuntimeException var7) {
      }

      float scale = json.has("corner_scale") ? json.get("corner_scale").getAsFloat() / 100.0F : 1.0F;
      return new ThemesPage.Preview(accent, accent2, tint, label(label, tint), shape(corners), corners == ThemeModule.CornerStyle.SHARP ? 0.0F : scale);
   }

   private static int color(JsonObject json, String key, int fallback) {
      JsonElement element = json.get(key);
      if (element == null) {
         return fallback;
      }

      if (element.isJsonObject()) {
         element = element.getAsJsonObject().get("color");
      }

      if (element == null) {
         return fallback;
      }

      try {
         if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsInt();
         }

         if (element.isJsonPrimitive()) {
            return (int)Long.parseLong(element.getAsString().replace("#", ""), 16);
         }
      } catch (RuntimeException var5) {
      }

      return fallback;
   }

   private static int label(ThemeModule.LabelColor label, int tint) {
      return switch (label) {
         case LIGHT -> -1;
         case DARK -> -14935010;
         case AUTO -> {
            float alpha = ColorUtil.alpha(tint) / 255.0F;
            float luminance = (ColorUtil.red(tint) * 0.2126F + ColorUtil.green(tint) * 0.7152F + ColorUtil.blue(tint) * 0.0722F) / 255.0F;
            yield luminance * alpha + 0.22F * (1.0F - alpha) > 0.5F ? -14935010 : -1;
         }
      };
   }

   private static float shape(ThemeModule.CornerStyle style) {
      return switch (style) {
         case ROUND, SHARP -> 2.0F;
         case SQUIRCLE -> 2.6F;
         case BEVEL -> 1.0F;
         case SCOOP -> -1.0F;
      };
   }

   private record Preview(int accent, int accent2, int tint, int label, float shape, float scale) {
   }
}
