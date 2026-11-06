package dev.rose.gui.menu;

import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.setting.KeySetting;
import dev.rose.setting.Setting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.resources.language.I18n;


public final class KeysPage {
   private static final float ROW_H = 26.0F;
   private static final float GAP = 3.0F;
   private static final float HEADER = 20.0F;
   private final List<KeysPage.Row> rows = new ArrayList<>();
   private String query = "";

   public void refresh() {
      this.rows.clear();

      for (Module module : Modules.all()) {
         if (module.hasKeybind()) {
            this.rows.add(new KeysPage.Row("keys:" + module.id(), module.icon(), module.name(), "", module.key()));
         }

         for (Setting<?> setting : module.settings()) {
            if (setting instanceof KeySetting key && key != module.key()) {
               this.rows.add(new KeysPage.Row("keys:" + module.id() + "." + setting.id(), module.icon(), setting.name(), module.name(), key));
            }
         }
      }
   }

   public void search(String text) {
      this.query = text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
   }

   private boolean matches(KeysPage.Row row) {
      return this.query.isEmpty()
         ? true
         : row.name().toLowerCase(Locale.ROOT).contains(this.query)
            || row.owner().toLowerCase(Locale.ROOT).contains(this.query)
            || row.key().keyName().toLowerCase(Locale.ROOT).contains(this.query);
   }

   public float draw(MenuUi ui, float x, float y, float w) {
      if (this.rows.isEmpty()) {
         this.refresh();
      }

      MenuPalette p = ui.palette;
      float fullAlpha = ui.alpha;
      float cursor = y;

      for (String line : MenuUi.wrap(ui.regular, I18n.get("rose.keys.intro", new Object[0]), 6.8F, w - 8.0F)) {
         ui.alpha = fullAlpha * ui.edgeFade(cursor, 10.0F);
         ui.text(ui.regular, line, x + 4.0F, cursor, 6.8F, p.textDim());
         cursor += 10.0F;
      }

      cursor += 8.0F;
      int shown = 0;

      for (int pass = 0; pass < 2; pass++) {
         boolean bound = pass == 0;
         List<KeysPage.Row> part = new ArrayList<>();

         for (KeysPage.Row row : this.rows) {
            if (row.key().isBound() == bound && this.matches(row)) {
               part.add(row);
            }
         }

         if (!part.isEmpty()) {
            ui.alpha = fullAlpha * ui.edgeFade(cursor, 20.0F);
            ui.text(
               ui.semibold, I18n.get(bound ? "rose.keys.bound" : "rose.keys.free", new Object[0]), x + 4.0F, cursor + 5.0F, 7.2F, p.textDim()
            );
            cursor += 20.0F;

            for (KeysPage.Row row : part) {
               ui.alpha = fullAlpha * ui.edgeFade(cursor, 26.0F);
               if (ui.alpha > 0.004F) {
                  this.row(ui, row, x, cursor, w);
               }

               cursor += 29.0F;
               shown++;
            }

            cursor += 6.0F;
         }
      }

      if (shown == 0) {
         ui.alpha = fullAlpha;
         ui.textCentered(ui.regular, I18n.get("rose.keys.nothing", new Object[0]), x + 10.0F, cursor + 10.0F, 7.0F, p.textFaint());
         cursor += 26.0F;
      }

      ui.alpha = fullAlpha;
      return cursor - y;
   }

   private void row(MenuUi ui, KeysPage.Row row, float x, float y, float w) {
      MenuPalette p = ui.palette;
      boolean hover = ui.inside(x, y, w, 26.0F);
      float lift = ui.anim(row.id() + ":hover", hover ? 1.0F : 0.0F, 16.0F);
      MenuDraw.rect(ui, x, y, w, 26.0F, 9.0F, ColorUtil.lerp(p.group(), p.groupHover(), lift));
      ui.icon(row.icon(), x + 15.0F, y + 13.0F, 9.0F, row.key().isBound() ? p.accent() : p.textDim());
      float keyW = MenuWidgets.keyWidth(ui, row.key(), row.id().equals(ui.listeningId));
      float keyX = x + w - 8.0F - keyW;
      float textX = x + 28.0F;
      float textW = keyX - 8.0F - textX;
      boolean hasOwner = !row.owner().isEmpty();
      float nameY = hasOwner ? y + 5.0F : y + 9.2F;
      ui.text(ui.medium, MenuUi.ellipsize(ui.medium, row.name(), 7.6F, textW), textX, nameY, 7.6F, p.text());
      if (hasOwner) {
         ui.text(ui.regular, MenuUi.ellipsize(ui.regular, row.owner(), 6.4F, textW), textX, y + 15.5F, 6.4F, p.textDim());
      }

      MenuWidgets.keybind(ui, row.id(), keyX, y + 4.0F, row.key());
      ui.tooltip(row.id() + ":tip", keyX, y, keyW, 26.0F, I18n.get("rose.menu.key_hint", new Object[0]));
   }

   public static int icon() {
      return 57988;
   }

   private record Row(String id, int icon, String name, String owner, KeySetting key) {
   }
}
