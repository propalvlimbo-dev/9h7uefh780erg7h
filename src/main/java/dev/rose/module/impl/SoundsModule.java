package dev.rose.module.impl;

import dev.rose.gui.UiSounds;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;


public final class SoundsModule extends Module {
   private final ChoiceSetting<SoundsModule.Pack> pack = this.add(new ChoiceSetting<>("pack", SoundsModule.Pack.LIQUID));
   private final NumberSetting master = this.add(new NumberSetting("master", 60.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
   private final NumberSetting menu;
   private final NumberSetting hover;
   private final NumberSetting toggles;
   private final NumberSetting notifications;
   private final NumberSetting character;
   private final ActionSetting preview;

   public SoundsModule() {
      super("sounds", Category.SOUNDS, true);
      this.section("categories", 58010);
      this.menu = this.add(new NumberSetting("menu", 100.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.hover = this.add(new NumberSetting("hover", 70.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.toggles = this.add(new NumberSetting("toggles", 100.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.notifications = this.add(new NumberSetting("notifications", 45.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.character = this.add(new NumberSetting("character", 100.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
      this.preview = this.add(new ActionSetting("preview", 57660, SoundsModule::preview));
   }

   @Override
   public int icon() {
      return 57771;
   }

   public SoundsModule.Pack pack() {
      return this.pack.get();
   }

   public float volume(UiSounds.Category category) {
      if (!this.isEnabled()) {
         return 0.0F;
      }

      NumberSetting setting = switch (category) {
         case MENU -> this.menu;
         case HOVER -> this.hover;
         case TOGGLES -> this.toggles;
         case NOTIFICATIONS -> this.notifications;
         case CHARACTER -> this.character;
      };
      return this.master.floatValue() / 100.0F * setting.floatValue() / 100.0F;
   }

   private static void preview() {
      UiSounds.play(UiSounds.Sound.TAB);
      UiSounds.play(UiSounds.Sound.TOGGLE_ON);
      UiSounds.play(UiSounds.Sound.NOTIFY);
   }

   public enum Pack {
      STUDIO,
      LIQUID,
      GLASS,
      SOFT,
      CLASSIC;
   }
}
