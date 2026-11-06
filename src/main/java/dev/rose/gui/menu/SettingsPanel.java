package dev.rose.gui.menu;

import dev.rose.gui.Thumbnails;
import dev.rose.gui.UiSounds;
import dev.rose.module.Module;
import dev.rose.render.Images;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.style.Corner;
import dev.rose.render.style.Corners;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ColorSetting;
import dev.rose.setting.ColorsSetting;
import dev.rose.setting.FileChoiceSetting;
import dev.rose.setting.GallerySetting;
import dev.rose.setting.ImageChoiceSetting;
import dev.rose.setting.KeySetting;
import dev.rose.setting.NumberSetting;
import dev.rose.setting.Setting;
import dev.rose.setting.SoundSetting;
import dev.rose.setting.TextSetting;
import dev.rose.ui.LiquidSlider;
import dev.rose.util.ColorUtil;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.resources.language.I18n;


public final class SettingsPanel {
   private static final float ROW = 30.0F;
   private static final float NUMBER_ROW = 42.0F;
   private static final float SEGMENTED_ROW = 46.0F;
   private static final float HEADER = 20.0F;
   private static final float GROUP_GAP = 12.0F;
   private static final long HIGHLIGHT_MS = 1800L;
   private final Map<Setting<?>, TextBuffer> texts = new HashMap<>();
   private final TextBuffer numberText = new TextBuffer(16);
   private NumberSetting editing;
   private Setting<?> highlight;
   private long highlightSince;
   private float highlightOffset = Float.NaN;

   public void highlight(Setting<?> setting) {
      this.highlight = setting;
      this.highlightSince = System.currentTimeMillis();
      this.highlightOffset = Float.NaN;
   }

   public float highlightOffset() {
      float value = this.highlightOffset;
      this.highlightOffset = Float.NaN;
      return value;
   }

   public float draw(MenuUi ui, Module module, float x, float y, float w) {
      MenuPalette p = ui.palette;
      List<SettingsPanel.Row> rows = this.layout(ui, module, w);
      if (rows.isEmpty()) {
         List<String> lines = MenuUi.wrap(ui.regular, I18n.get("rose.menu.no_settings", new Object[0]), 7.4F, w - 24.0F);

         for (int i = 0; i < lines.size(); i++) {
            ui.text(ui.regular, lines.get(i), x + 12.0F, y + 6.0F + i * 11, 7.4F, p.textDim());
         }

         return lines.size() * 11 + 12;
      } else {
         float fullAlpha = ui.alpha;
         float cursor = y;
         int start = 0;
         int groupIndex = 0;

         while (start < rows.size()) {
            String section = rows.get(start).setting().section();
            int end = start;
            float groupHeight = 0.0F;

            while (end < rows.size() && rows.get(end).setting().section().equals(section)) {
               groupHeight += rows.get(end).height();
               end++;
            }

            String appearKey = "group:" + module.id() + ":" + section;
            float appear = ui.bounce(appearKey, 1.0F, 0.44 + groupIndex * 0.06, 0.74);
            groupIndex++;
            float top = cursor + (1.0F - appear) * 12.0F * ui.motion;
            float groupAlpha = fullAlpha * Math.clamp(appear, 0.0F, 1.0F);
            if (!section.isEmpty()) {
               ui.alpha = groupAlpha * ui.edgeFade(top, 20.0F);
               MenuWidgets.sectionHeader(ui, module.sectionIcon(section), I18n.get("rose.section." + section, new Object[0]), x, top);
               top += 20.0F;
               cursor += 20.0F;
            }

            Corners rounded = Corners.themed(14.0F);
            float rowY = top;

            for (int i = start; i < end; i++) {
               SettingsPanel.Row row = rows.get(i);
               ui.alpha = groupAlpha * ui.edgeFade(rowY, row.height());
               if (ui.alpha > 0.004F) {
                  Corners corners = new Corners(
                     i == start ? rounded.topLeft() : Corner.NONE,
                     i == start ? rounded.topRight() : Corner.NONE,
                     i == end - 1 ? rounded.bottomRight() : Corner.NONE,
                     i == end - 1 ? rounded.bottomLeft() : Corner.NONE
                  );
                  MenuDraw.rect(ui, x, rowY, w, row.height(), corners, p.group());
                  if (i > start) {
                     MenuDraw.hline(ui, x + 14.0F, rowY, w - 28.0F, p.divider());
                  }

                  if (row.setting() == this.highlight) {
                     this.drawHighlight(ui, x, rowY, w, row.height());
                  }

                  this.drawRow(ui, row, x, rowY, w);
               }

               rowY += row.height();
            }

            cursor += groupHeight + 12.0F;
            start = end;
         }

         ui.alpha = fullAlpha;
         return cursor - y;
      }
   }

   public void restartAppear(MenuUi ui, Module module) {
      for (Setting<?> setting : module.settings()) {
         ui.spring("group:" + module.id() + ":" + setting.section(), 1.0, 0.44, 0.74).snap(0.0);
      }

      ui.spring("group:" + module.id() + ":", 1.0, 0.44, 0.74).snap(0.0);
   }

   private List<SettingsPanel.Row> layout(MenuUi ui, Module module, float w) {
      List<SettingsPanel.Row> rows = new ArrayList<>();
      float inner = w - 28.0F;

      for (Setting<?> setting : module.settings()) {
         if (setting.isVisible()) {
            switch (setting) {
               case BoolSetting ignored:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.BOOL, setting, 30.0F));
                  break;
               case NumberSetting ignored:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.NUMBER, setting, 42.0F));
                  break;
               case GallerySetting<?> ignored:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.GALLERY, setting, 58.0F));
                  break;
               case ChoiceSetting<?> choice:
                  String[] labels = labels(choice);
                  if (MenuWidgets.fitsSegmented(ui, labels, inner)) {
                     rows.add(new SettingsPanel.Row(SettingsPanel.Kind.SEGMENTED, setting, 46.0F));
                  } else if (labels.length <= 8) {
                     rows.add(new SettingsPanel.Row(SettingsPanel.Kind.CHIPS, setting, 26.0F + MenuWidgets.chipsHeight(ui, labels, inner) + 9.0F));
                  } else {
                     rows.add(new SettingsPanel.Row(SettingsPanel.Kind.DROPDOWN, setting, 30.0F));
                  }
                  break;
               case ColorsSetting colors:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.COLORS, setting, colors.get().mode() == ColorsSetting.Mode.SINGLE ? 54.0F : 76.0F));
                  break;
               case ColorSetting ignored:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.COLOR, setting, 30.0F));
                  break;
               case KeySetting ignored:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.KEY, setting, 30.0F));
                  break;
               case TextSetting ignored:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.TEXT, setting, 30.0F));
                  break;
               case ActionSetting ignored:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.ACTION, setting, 30.0F));
                  break;
               case ImageChoiceSetting images:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.IMAGE, setting, images.files().isEmpty() ? 44.0F : 90.0F));
                  break;
               case FileChoiceSetting ignored:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.FILES, setting, 30.0F));
                  break;
               case SoundSetting ignored:
                  rows.add(new SettingsPanel.Row(SettingsPanel.Kind.SOUND, setting, 30.0F));
                  break;
               default:
            }
         }
      }

      return rows;
   }

   private static String[] labels(ChoiceSetting<?> choice) {
      Enum<?>[] options = choice.options();
      String[] labels = new String[options.length];

      for (int i = 0; i < options.length; i++) {
         labels[i] = ((ChoiceSetting)choice).label(options[i]);
      }

      return labels;
   }

   private static String id(Setting<?> setting) {
      return "setting:" + setting.owner().id() + "." + setting.id();
   }

   private void drawHighlight(MenuUi ui, float x, float y, float w, float h) {
      if (Float.isNaN(this.highlightOffset)) {
         this.highlightOffset = y;
      }

      float t = (float)(System.currentTimeMillis() - this.highlightSince) / 1800.0F;
      if (t >= 1.0F) {
         this.highlight = null;
      } else {
         float pulse = (float)Math.pow(Math.sin(t * Math.PI * 2.0), 2.0) * (1.0F - t);
         MenuDraw.rect(ui, x + 3.0F, y + 2.0F, w - 6.0F, h - 4.0F, 11.0F, ColorUtil.withAlpha(Theme.accent(), Math.round(85.0F * pulse)));
      }
   }

   private void drawRow(MenuUi ui, SettingsPanel.Row row, float x, float y, float w) {
      Setting<?> setting = row.setting();
      String id = id(setting);
      float h = row.height();
      if (row.kind() != SettingsPanel.Kind.ACTION) {
         ui.target(id + ":row", x, y, w, h, MenuUi.click(() -> {
            if (setting instanceof BoolSetting bool) {
               bool.toggle();
               UiSounds.play(bool.isOn() ? UiSounds.Sound.TOGGLE_ON : UiSounds.Sound.TOGGLE_OFF);
            }
         }, () -> {
            if (!setting.isDefault()) {
               setting.reset();
               setting.owner().onConfigLoaded();
               UiSounds.play(UiSounds.Sound.TOGGLE_OFF);
            }
         }));
      }

      ui.tooltip(id + ":tip", x, y, w * 0.6F, h, setting.description());
      float right = x + w - 12.0F;
      switch (row.kind()) {
         case BOOL:
            BoolSetting bool = (BoolSetting)setting;
            this.name(ui, setting, x + 14.0F, y + h / 2.0F, w - 70.0F);
            MenuWidgets.toggle(ui, id, right - 30.0F, y + (h - 17.0F) / 2.0F, bool.isOn(), bool::toggle);
            break;
         case NUMBER:
            this.number(ui, id, (NumberSetting)setting, x, y, w);
            break;
         case SEGMENTED: {
            ChoiceSetting<?> choice = (ChoiceSetting<?>)setting;
            this.name(ui, setting, x + 14.0F, y + 12.0F, w - 28.0F);
            MenuWidgets.segmented(ui, id, x + 14.0F, y + 22.0F, w - 28.0F, labels(choice), choice.get().ordinal(), indexxx -> select(choice, indexxx));
            break;
         }
         case CHIPS: {
            ChoiceSetting<?> choice = (ChoiceSetting<?>)setting;
            this.name(ui, setting, x + 14.0F, y + 12.0F, w - 28.0F);
            MenuWidgets.chips(ui, id, x + 14.0F, y + 24.0F, w - 28.0F, labels(choice), choice.get().ordinal(), indexxx -> select(choice, indexxx));
            break;
         }
         case DROPDOWN: {
            ChoiceSetting<?> choice = (ChoiceSetting<?>)setting;
            float boxW = Math.min(150.0F, (w - 28.0F) * 0.5F);
            this.name(ui, setting, x + 14.0F, y + h / 2.0F, w - 42.0F - boxW);
            MenuWidgets.dropdown(ui, id, right - boxW, y + (h - 18.0F) / 2.0F, boxW, labels(choice), choice.get().ordinal(), indexxx -> select(choice, indexxx));
            break;
         }
         case GALLERY:
            this.gallery(ui, id, (GallerySetting<?>)setting, x, y, w);
            break;
         case COLOR:
            this.name(ui, setting, x + 14.0F, y + h / 2.0F, w - 70.0F);
            this.colorSwatch(ui, id, (ColorSetting)setting, right - 30.0F, y + (h - 16.0F) / 2.0F);
            break;
         case COLORS:
            this.colors(ui, id, (ColorsSetting)setting, x, y, w);
            break;
         case KEY:
            KeySetting key = (KeySetting)setting;
            float keyW = MenuWidgets.keyWidth(ui, key, id.equals(ui.listeningId));
            this.name(ui, setting, x + 14.0F, y + h / 2.0F, w - 36.0F - keyW);
            MenuWidgets.keybind(ui, id, right - keyW, y + (h - 18.0F) / 2.0F, key);
            break;
         case TEXT:
            TextSetting text = (TextSetting)setting;
            float fieldW = Math.min(160.0F, (w - 28.0F) * 0.55F);
            this.name(ui, setting, x + 14.0F, y + h / 2.0F, w - 42.0F - fieldW);
            TextBuffer buffer = this.texts.computeIfAbsent(text, t -> new TextBuffer(text.maxLength()));
            if (buffer != ui.focused && !buffer.text().equals(text.get())) {
               buffer.set(text.get());
            }

            MenuWidgets.textField(ui, id, right - fieldW, y + (h - 18.0F) / 2.0F, fieldW, buffer, "", () -> text.set(buffer.text()), ui::unfocus);
            break;
         case ACTION:
            ActionSetting action = (ActionSetting)setting;
            String label = action.buttonLabel();
            float buttonW = MenuWidgets.buttonWidth(ui, action.icon(), label);
            this.name(ui, setting, x + 14.0F, y + h / 2.0F, w - 42.0F - buttonW);
            MenuWidgets.button(ui, id, right - buttonW, y + (h - 18.0F) / 2.0F, buttonW, 18.0F, action.icon(), label, MenuWidgets.Style.NORMAL, action::run);
            break;
         case IMAGE:
            this.images(ui, id, (ImageChoiceSetting)setting, x, y, w);
            break;
         case FILES: {
            FileChoiceSetting files = (FileChoiceSetting)setting;
            float boxW = Math.min(150.0F, (w - 28.0F) * 0.55F);
            this.name(ui, setting, x + 14.0F, y + h / 2.0F, w - 42.0F - boxW);
            List<Path> list = files.files();
            if (list.isEmpty()) {
               String empty = I18n.get("rose.menu.folder_empty", new Object[0]);
               ui.textCentered(ui.regular, empty, right - ui.regular.width(empty, 7.2F), y + h / 2.0F, 7.2F, ui.palette.textFaint());
            } else {
               String[] labelsx = list.stream().map(path -> path.getFileName().toString()).toArray(String[]::new);
               int indexx = Math.max(0, list.indexOf(files.selected()));
               MenuWidgets.dropdown(ui, id, right - boxW, y + (h - 18.0F) / 2.0F, boxW, labelsx, indexx, i -> files.set(labelsx[i]));
            }
            break;
         }
         case SOUND: {
            SoundSetting sound = (SoundSetting)setting;
            float playSize = 20.0F;
            float boxW = Math.min(140.0F, (w - 28.0F) * 0.48F);
            this.name(ui, setting, x + 14.0F, y + h / 2.0F, w - 48.0F - boxW - playSize);
            List<String> options = sound.options();
            String[] labels = options.stream().map(SoundSetting::label).toArray(String[]::new);
            int index = Math.max(0, options.indexOf(sound.get()));
            MenuWidgets.dropdown(ui, id, right - playSize - 6.0F - boxW, y + (h - 18.0F) / 2.0F, boxW, labels, index, i -> {
               sound.set(options.get(i));
               sound.preview(0.8F);
            });
            MenuWidgets.iconButton(
               ui, id + ":play", right - playSize, y + (h - playSize) / 2.0F, playSize, 57660, ui.palette.accentText(), () -> sound.preview(0.8F)
            );
            ui.tooltip(
               id + ":play:tip",
               right - playSize,
               y + (h - playSize) / 2.0F,
               playSize,
               playSize,
               I18n.get("rose.menu.preview_sound", new Object[0])
            );
         }
      }
   }

   private static void select(ChoiceSetting<?> choice, int index) {
      Enum<?>[] options = choice.options();
      ((ChoiceSetting)choice).set(options[Math.clamp(index, 0, options.length - 1)]);
   }

   private void name(MenuUi ui, Setting<?> setting, float x, float centerY, float maxWidth) {
      MenuPalette p = ui.palette;
      String text = MenuUi.ellipsize(ui.medium, setting.name(), 8.0F, Math.max(20.0F, maxWidth - 8.0F));
      float width = ui.textCentered(ui.medium, text, x, centerY, 8.0F, p.text());
      if (!setting.isDefault() && !(setting instanceof ActionSetting)) {
         MenuDraw.circle(ui, x + width + 5.0F, centerY, 1.7F, p.accentText());
      }
   }

   private void number(MenuUi ui, String id, NumberSetting setting, float x, float y, float w) {
      MenuPalette p = ui.palette;
      float right = x + w - 14.0F;
      boolean editingThis = this.editing == setting && ui.focused == this.numberText;
      float valueW;
      if (editingThis) {
         valueW = 58.0F;
         MenuWidgets.textField(ui, id + ":edit", right - valueW, y + 3.0F, valueW, this.numberText, "", null, () -> {
            try {
               setting.set(Double.parseDouble(this.numberText.text().replace(',', '.').replaceAll("[^0-9.+-]", "")));
            } catch (NumberFormatException var4) {
            }

            this.editing = null;
            ui.unfocus();
         });
      } else {
         if (this.editing == setting) {
            this.editing = null;
         }

         String value = setting.format();
         valueW = ui.medium.width(value, 7.2F);
         boolean hot = ui.hot(id + ":value");
         boolean dragging = ui.pressed(id);
         if (hot) {
            MenuDraw.pill(ui, right - valueW - 5.0F, y + 5.0F, valueW + 10.0F, 13.0F, p.groupHover());
         }

         ui.textCentered(ui.medium, value, right - valueW, y + 11.5F, 7.2F, hot ? p.accentText() : (dragging ? p.text() : p.textDim()));
         ui.target(id + ":value", right - valueW - 5.0F, y + 4.0F, valueW + 10.0F, 15.0F, MenuUi.click(() -> {
            this.editing = setting;
            this.numberText.set(setting.format().replaceAll("[^0-9.,+-]", ""));
            ui.focus(id + ":edit", this.numberText, null, null);
         }));
         ui.tooltip(id + ":value:tip", right - valueW - 5.0F, y + 4.0F, valueW + 10.0F, 15.0F, I18n.get("rose.menu.type_value", new Object[0]));
      }

      this.name(ui, setting, x + 14.0F, y + 11.5F, w - 40.0F - valueW);
      MenuWidgets.slider(ui, id, x + 16.0F, y + 22.0F, w - 32.0F, setting);
   }

   private void gallery(MenuUi ui, String id, GallerySetting<?> gallery, float x, float y, float w) {
      MenuPalette p = ui.palette;
      this.name(ui, gallery, x + 14.0F, y + 12.0F, w - 28.0F);
      Enum<?>[] options = gallery.options();
      float gap = 6.0F;
      float left = x + 14.0F;
      float width = w - 28.0F;
      float tileW = Math.min(48.0F, (width - gap * (options.length - 1)) / options.length);
      float tileH = 22.0F;
      float top = y + 25.0F;

      for (int i = 0; i < options.length; i++) {
         GallerySetting.Preview preview = (GallerySetting.Preview)options[i];
         float tx = left + i * (tileW + gap);
         String tileId = id + ":" + i;
         boolean selected = gallery.get() == options[i];
         float hover = ui.anim(tileId + ":hover", ui.hot(tileId) ? 1.0F : 0.0F, 20.0F);
         float lift = hover * 1.2F;
         Surfaces.fill(
            ui.g, tx, top - lift, tileW, tileH, Corners.themed(7.0F), preview.primary(), preview.secondary(), preview.tertiary(), preview.primary(), ui.alpha
         );
         if (selected) {
            MenuDraw.border(ui, tx - 2.0F, top - lift - 2.0F, tileW + 4.0F, tileH + 4.0F, 9.0F, 1.4F, p.text());
         }

         ui.tooltip(tileId + ":tip", tx, top, tileW, tileH, ((GallerySetting)gallery).label(options[i]));
         Enum<?> option = options[i];
         ui.target(tileId, tx, top, tileW, tileH, MenuUi.click(() -> {
            ((GallerySetting)gallery).set(option);
            UiSounds.play(UiSounds.Sound.CLICK);
         }));
      }
   }

   private void colorSwatch(MenuUi ui, String id, ColorSetting color, float x, float y) {
      MenuPalette p = ui.palette;
      float w = 30.0F;
      float h = 16.0F;
      boolean open = ui.colorPopup != null && ui.colorPopup.id.equals(id + ":popup");
      float hover = ui.anim(id + ":hover", !ui.hot(id) && !open ? 0.0F : 1.0F, 20.0F);
      if (color.hasAlpha()) {
         MenuDraw.pill(ui, x, y, w, h, -1);

         for (int i = 1; i < 6; i += 2) {
            MenuDraw.rect(ui, x + i * w / 6.0F, y + 2.0F, w / 6.0F, h - 4.0F, 0.0F, -3881788);
         }
      }

      MenuDraw.pill(ui, x, y, w, h, color.color());
      MenuDraw.pillBorder(ui, x - hover, y - hover, w + hover * 2.0F, h + hover * 2.0F, 1.0F, ColorUtil.lerp(ColorUtil.fade(p.text(), 0.25F), p.text(), hover));
      if (color.isRainbow()) {
         String label = I18n.get("rose.menu.rainbow", new Object[0]);
         ui.textCentered(ui.medium, label, x - 6.0F - ui.medium.width(label, 6.6F), y + h / 2.0F, 6.6F, p.accentText());
      }

      ui.target(id, x, y, w, h, MenuUi.click(() -> {
         boolean wasOpen = ui.colorPopup != null && ui.colorPopup.id.equals(id + ":popup");
         ui.closePopups();
         if (!wasOpen) {
            ui.colorPopup = new ColorPopup(id + ":popup", x + w, y + h, color.get(), color.hasAlpha(), color::set).rainbow(color::isRainbow, color::setRainbow);
         }

         UiSounds.play(UiSounds.Sound.CLICK);
      }));
   }

   private void colors(MenuUi ui, String id, ColorsSetting colors, float x, float y, float w) {
      MenuPalette p = ui.palette;
      ColorsSetting.Value value = colors.get();
      ColorsSetting.Mode[] modes = ColorsSetting.Mode.values();
      String[] labels = new String[modes.length];

      for (int i = 0; i < modes.length; i++) {
         labels[i] = I18n.get("rose.menu.colors." + modes[i].name().toLowerCase(Locale.ROOT), new Object[0]);
      }

      float right = x + w - 12.0F;
      float boxW = Math.min(118.0F, (w - 28.0F) * 0.46F);
      this.name(ui, colors, x + 14.0F, y + 15.0F, w - 42.0F - boxW);
      MenuWidgets.dropdown(ui, id + ":mode", right - boxW, y + 6.0F, boxW, labels, value.mode().ordinal(), i -> colors.setMode(modes[i]));
      float lineY = y + 30.0F;
      float cx = x + 14.0F;

      int chips = switch (value.mode()) {
         case SINGLE, RAINBOW -> 1;
         case GRADIENT -> value.count();
         case THEME -> 0;
      };

      for (int i = 0; i < chips; i++) {
         int index = i;
         String chipId = id + ":chip:" + i;
         float hover = ui.anim(chipId + ":hover", ui.hot(chipId) ? 1.0F : 0.0F, 20.0F);
         MenuDraw.circle(ui, cx + 8.0F, lineY + 8.0F, 7.4F + hover, ColorUtil.fade(p.text(), 0.3F + 0.5F * hover));
         MenuDraw.circle(ui, cx + 8.0F, lineY + 8.0F, 6.4F, value.color(i));
         float chipX = cx;
         ui.target(chipId, cx, lineY, 16.0F, 16.0F, MenuUi.click(() -> {
            ui.closePopups();
            ui.colorPopup = new ColorPopup(chipId + ":popup", chipX + 16.0F, lineY + 16.0F, colors.get().color(index), false, c -> colors.setColor(index, c));
            UiSounds.play(UiSounds.Sound.CLICK);
         }));
         cx += 20.0F;
      }

      if (value.mode() == ColorsSetting.Mode.GRADIENT) {
         boolean three = value.count() == 3;
         MenuWidgets.iconButton(ui, id + ":count", cx, lineY, 16.0F, three ? 57628 : 57661, p.textDim(), () -> colors.setCount(three ? 2 : 3));
         ui.tooltip(
            id + ":count:tip", cx, lineY, 16.0F, 16.0F, I18n.get(three ? "rose.menu.colors.less" : "rose.menu.colors.more", new Object[0])
         );
         cx += 20.0F;
      }

      float barX = cx + (chips > 0 ? 4 : 0);
      float barW = right - barX;
      float barY = lineY + 4.0F;
      float barH = 8.0F;
      float time = Theme.time();
      int steps = 28;

      for (int i = 0; i < steps; i++) {
         float t0 = (float)i / steps;
         float t1 = (float)(i + 1) / steps;
         Corners corners = i == 0
            ? new Corners(Corner.round(4.0F), Corner.NONE, Corner.NONE, Corner.round(4.0F))
            : (i == steps - 1 ? new Corners(Corner.NONE, Corner.round(4.0F), Corner.round(4.0F), Corner.NONE) : Corners.NONE);
         int c0 = colors.color(t0, time);
         int c1 = colors.color(t1, time);
         float sx = barX + t0 * barW;
         Surfaces.fill(ui.g, sx - (i > 0 ? 0.3F : 0.0F), barY, barW / steps + (i > 0 ? 0.3F : 0.0F), barH, corners, c0, c1, c1, c0, ui.alpha);
      }

      if (value.mode() != ColorsSetting.Mode.SINGLE) {
         float speedY = lineY + 26.0F;
         String label = I18n.get("rose.menu.colors.speed", new Object[0]);
         float labelW = ui.medium.width(label, 7.2F);
         ui.textCentered(ui.medium, label, x + 14.0F, speedY + 8.0F, 7.2F, p.textDim());
         String speed = String.format(Locale.ROOT, "%.1f×", value.speed());
         float speedW = ui.medium.width(speed, 7.2F);
         ui.textCentered(ui.medium, speed, right - speedW, speedY + 8.0F, 7.2F, p.textDim());
         float sliderX = x + 24.0F + labelW;
         final float sliderW = right - speedW - 10.0F - sliderX;
         String sliderId = id + ":speed";
         LiquidSlider slider = ui.slider(sliderId, value.speed() / 5.0F);
         slider.render(ui.g, sliderX, speedY, sliderW, 16.0F, ui.alpha, p.accent(), p.track(), false, ui.hot(sliderId), 0.0F, p.glass());
         ui.target(sliderId, sliderX - 6.0F, speedY - 4.0F, sliderW + 12.0F, 24.0F, new MenuUi.Handler() {
            @Override
            public void press(int button, float localX, float localY) {
               this.apply(localX);
            }

            @Override
            public void drag(float localX, float localY) {
               this.apply(localX);
            }

            private void apply(float localX) {
               colors.setSpeed(Math.round(LiquidSlider.valueAt(localX - 6.0F, sliderW) * 50.0F) / 10.0F);
            }
         });
      }
   }

   private void images(MenuUi ui, String id, ImageChoiceSetting setting, float x, float y, float w) {
      MenuPalette p = ui.palette;
      this.name(ui, setting, x + 14.0F, y + 12.0F, w - 28.0F);
      List<Path> files = setting.files();
      if (files.isEmpty()) {
         ui.text(ui.regular, I18n.get("rose.menu.folder_empty", new Object[0]), x + 14.0F, y + 24.0F, 7.0F, p.textFaint());
      } else {
         Path selected = setting.selected();
         float tileW = 40.0F;
         float tileH = 54.0F;
         float gap = 6.0F;
         float top = y + 26.0F;
         int fit = Math.max(1, (int)((w - 28.0F + gap) / (tileW + gap)));

         for (int i = 0; i < Math.min(fit, files.size()); i++) {
            Path file = files.get(i);
            float tx = x + 14.0F + i * (tileW + gap);
            String tileId = id + ":" + i;
            boolean chosen = file.equals(selected);
            MenuDraw.rect(ui, tx, top, tileW, tileH, 8.0F, p.track());
            Thumbnails.Thumbnail thumbnail = Thumbnails.get(file);
            if (thumbnail != null) {
               float scale = Math.min((tileW - 4.0F) / thumbnail.width(), (tileH - 4.0F) / thumbnail.height());
               float iw = thumbnail.width() * scale;
               float ih = thumbnail.height() * scale;
               Images.draw(
                  ui.g,
                  thumbnail.texture(),
                  0.0F,
                  0.0F,
                  1.0F,
                  1.0F,
                  tx + (tileW - iw) / 2.0F,
                  top + (tileH - ih) / 2.0F,
                  iw,
                  ih,
                  Corners.themed(5.0F),
                  ui.alpha
               );
            }

            if (chosen || ui.hot(tileId)) {
               MenuDraw.border(ui, tx - 2.0F, top - 2.0F, tileW + 4.0F, tileH + 4.0F, 10.0F, 1.4F, chosen ? p.accent() : p.textFaint());
            }

            String name = file.getFileName().toString();
            ui.tooltip(tileId + ":tip", tx, top, tileW, tileH, name);
            ui.target(tileId, tx, top, tileW, tileH, MenuUi.click(() -> {
               setting.set(name);
               UiSounds.play(UiSounds.Sound.CLICK);
            }));
         }
      }
   }

   private enum Kind {
      BOOL,
      NUMBER,
      SEGMENTED,
      CHIPS,
      DROPDOWN,
      GALLERY,
      COLOR,
      COLORS,
      KEY,
      TEXT,
      ACTION,
      IMAGE,
      FILES,
      SOUND;
   }

   private record Row(SettingsPanel.Kind kind, Setting<?> setting, float height) {
   }
}
