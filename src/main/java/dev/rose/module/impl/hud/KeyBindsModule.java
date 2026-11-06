package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.render.Theme;
import dev.rose.setting.BoolSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.language.I18n;


public final class KeyBindsModule extends HudElement {
   private static final float HEADER = 17.0F;
   private static final float ROW = 13.0F;
   private static final float PAD = 7.0F;
   private static final float TEXT = 6.8F;
   private static final float KEY_TEXT = 6.0F;
   private final BoolSetting header = this.add(new BoolSetting("header", true));
   private final BoolSetting onlyEnabled = this.add(new BoolSetting("only_enabled", false));

   public KeyBindsModule() {
      super("keybinds", false, Anchor.LEFT, 6.0F, -24.0F);
   }

   @Override
   public int icon() {
      return 57988;
   }

   private List<KeyBindsModule.Row> rows(HudContext hud) {
      List<KeyBindsModule.Row> rows = new ArrayList<>();

      for (Module module : Modules.all()) {
         if (module.hasKeybind() && module.key().isBound() && (!this.onlyEnabled.isOn() || module.isEnabled())) {
            rows.add(new KeyBindsModule.Row(module.name(), module.key().keyName(), module.isEnabled(), module.id()));
         }
      }

      if (rows.isEmpty() && hud.preview) {
         String[] samples = I18n.get("rose.hud.keybinds.samples", new Object[0]).split("\\|");

         for (int i = 0; i + 1 < samples.length; i += 2) {
            rows.add(new KeyBindsModule.Row(samples[i], samples[i + 1], i == 0, "sample" + i));
         }
      }

      return rows;
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      return !this.rows(hud).isEmpty();
   }

   @Override
   protected Size measure(HudContext hud) {
      List<KeyBindsModule.Row> rows = this.rows(hud);
      float width = this.header.isOn() ? 26.0F + hud.semibold.width(I18n.get("rose.hud.keybinds.title", new Object[0]), 7.0F) + 16.0F : 0.0F;

      for (KeyBindsModule.Row row : rows) {
         width = Math.max(width, 22.0F + hud.medium.width(row.name(), 6.8F) + 10.0F + keyWidth(hud, row.key()));
      }

      float height = (this.header.isOn() ? 17.0F : 5.0F) + rows.size() * 13.0F + 4.0F;
      return new Size(Math.max(width, 96.0F), height);
   }

   private static float keyWidth(HudContext hud, String key) {
      return Math.max(12.0F, hud.semibold.width(key, 6.0F) + 8.0F);
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      List<KeyBindsModule.Row> rows = this.rows(hud);
      float y = 5.0F;
      if (this.header.isOn()) {
         hud.icon(57988, 11.5F, 9.5F, 9.0F, hud.accent());
         hud.text(
            hud.semibold, I18n.get("rose.hud.keybinds.title", new Object[0]), 19.0F, HudContext.centered(1.0F, 17.0F, 7.0F), 7.0F, hud.label
         );
         String count = Integer.toString(rows.size());
         hud.textRight(hud.medium, count, w - 7.0F, HudContext.centered(1.0F, 17.0F, 6.2F), 6.2F, hud.secondary);
         hud.fill(7.0F, 16.5F, w - 14.0F, 0.6F, 0.0F, ColorUtil.fade(hud.label, 0.1F));
         y = 18.0F;
      }

      int accent = Theme.accent();

      for (KeyBindsModule.Row row : rows) {
         float on = Math.clamp(hud.animate("on:" + row.id(), row.on() ? 1.0F : 0.0F, 0.25, 0.9), 0.0F, 1.0F);
         float dot = 4.0F;
         hud.fill(7.0F, y + (13.0F - dot) / 2.0F, dot, dot, dot / 2.0F, ColorUtil.lerp(ColorUtil.fade(hud.label, 0.25F), accent, on));
         hud.text(hud.medium, row.name(), 15.0F, HudContext.centered(y, 13.0F, 6.8F), 6.8F, ColorUtil.lerp(hud.secondary, hud.label, on));
         float keyW = keyWidth(hud, row.key());
         float keyH = 10.0F;
         float keyX = w - 7.0F - keyW;
         float keyY = y + (13.0F - keyH) / 2.0F;
         hud.fill(keyX, keyY, keyW, keyH, 3.5F, ColorUtil.lerp(ColorUtil.fade(hud.label, 0.1F), ColorUtil.withAlpha(accent, 112), on));
         float keyTextW = hud.semibold.width(row.key(), 6.0F);
         hud.text(hud.semibold, row.key(), keyX + (keyW - keyTextW) / 2.0F, HudContext.centered(keyY, keyH, 6.0F), 6.0F, ColorUtil.lerp(hud.secondary, -1, on));
         y += 13.0F;
      }
   }

   private record Row(String name, String key, boolean on, String id) {
   }
}
