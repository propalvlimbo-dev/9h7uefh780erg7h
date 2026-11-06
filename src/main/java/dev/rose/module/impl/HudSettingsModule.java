package dev.rose.module.impl;

import dev.rose.gui.HudEditorScreen;
import dev.rose.hud.HudLayouts;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.notify.Notifications;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;


public final class HudSettingsModule extends Module {
   private final BoolSetting snap;
   private final BoolSetting grid;
   private final NumberSetting gridSize;
   private final BoolSetting hideWithDebug;
   private final BoolSetting animations;

   public HudSettingsModule() {
      super("hud", Category.HUD, true);
      this.section("editor", 57633);
      this.add(new ActionSetting("open_editor", 57633, HudEditorScreen::open));
      this.snap = this.add(new BoolSetting("snap", true));
      this.grid = this.add(new BoolSetting("grid", false));
      this.gridSize = this.add(new NumberSetting("grid_size", 8.0, 4.0, 32.0, 1.0, "rose.unit.pixels"));
      this.section("layouts", 57793);
      this.add(new ActionSetting("layout_classic", 57793, () -> this.applyLayout(HudLayouts.Layout.CLASSIC)));
      this.add(new ActionSetting("layout_minimal", 57793, () -> this.applyLayout(HudLayouts.Layout.MINIMAL)));
      this.add(new ActionSetting("layout_focus", 57793, () -> this.applyLayout(HudLayouts.Layout.FOCUS)));
      this.add(new ActionSetting("reset_layout", 57672, () -> {
         HudLayouts.resetPlacements();
         Notifications.show(Notifications.Kind.NEUTRAL, this.name(), I18n.get("rose.hud.editor.reset_done", new Object[0]));
      }));
      this.section("behavior", 57530);
      this.hideWithDebug = this.add(new BoolSetting("hide_debug", true));
      this.animations = this.add(new BoolSetting("animations", true));
   }

   private void applyLayout(HudLayouts.Layout layout) {
      HudLayouts.apply(layout);
      Notifications.show(
         Notifications.Kind.SUCCESS,
         I18n.get("rose.hud.layout." + layout.name().toLowerCase(Locale.ROOT), new Object[0]),
         I18n.get("rose.hud.editor.layout_applied", new Object[0])
      );
   }

   @Override
   public int icon() {
      return 57793;
   }

   @Override
   public boolean isAlwaysOn() {
      return true;
   }

   @Override
   public boolean hasKeybind() {
      return true;
   }

   @Override
   public void onKeyBind() {
      if (Minecraft.getInstance().screen == null) {
         HudEditorScreen.open();
      }
   }

   public boolean snap() {
      return this.snap.isOn();
   }

   public void setSnap(boolean value) {
      this.snap.set(value);
   }

   public boolean grid() {
      return this.grid.isOn();
   }

   public void setGrid(boolean value) {
      this.grid.set(value);
   }

   public int gridSize() {
      return this.gridSize.intValue();
   }

   public boolean hideWithDebug() {
      return this.hideWithDebug.isOn();
   }

   public boolean animations() {
      return this.animations.isOn();
   }
}
