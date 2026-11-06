package com.deluxe.modules.impl.misc;

import com.deluxe.modules.impl.player.InputBinding;
import com.deluxe.util.time.Setting;
import java.util.ArrayList;

public class Module {
   public String name;
   public String description;
   public ModuleCategory category;
   public boolean expanded;
   private final sg.modules.ClientModule backing;
   private final ArrayList<Setting<?>> settings = new ArrayList<>();

   public Module(sg.modules.ClientModule backing) {
      this.backing = backing;
      this.name = backing.getName();
      this.description = backing.getDescription() == null ? "" : backing.getDescription();
      this.category = ModuleCategory.fromClientCategory(backing.getCategory());

      for (sg.settings.ValueSetting<?> clientSetting : backing.getSettings()) {
         Setting<?> wrappedSetting = wrapSetting(clientSetting);
         if (wrappedSetting != null) {
            this.settings.add(wrappedSetting);
         }
      }
   }

   private static Setting<?> wrapSetting(sg.settings.ValueSetting<?> clientSetting) {
      if (clientSetting instanceof sg.settings.BooleanSetting booleanSetting) {
         return new com.deluxe.modules.impl.render.BooleanSetting(booleanSetting);
      } else if (clientSetting instanceof sg.settings.FloatSetting floatSetting) {
         return new com.deluxe.render.pipeline.SliderSetting(floatSetting);
      } else if (clientSetting instanceof sg.settings.ColorSetting colorSetting) {
         return new com.deluxe.modules.impl.render.ColorSetting(colorSetting);
      } else if (clientSetting instanceof sg.settings.MultiChoiceSetting multiChoiceSetting) {
         return new MultiChoiceSetting(multiChoiceSetting);
      } else if (clientSetting instanceof sg.settings.SingleChoiceSetting singleChoiceSetting) {
         return new com.deluxe.util.time.ChoiceSetting(singleChoiceSetting);
      } else if (clientSetting instanceof sg.settings.KeybindSetting keybindSetting) {
         return new com.deluxe.modules.impl.player.BindSetting(keybindSetting);
      } else if (clientSetting instanceof sg.settings.TextSetting textSetting) {
         return new com.deluxe.modules.impl.render.TextSetting(textSetting);
      } else if (clientSetting instanceof sg.settings.SectionHeaderSetting headerSetting) {
         return new com.deluxe.modules.impl.render.SettingSeparator(headerSetting.getName());
      } else {
         return null;
      }
   }

   public Module withKeyCode(int keyCode) {
      this.backing.getKeybind().setKeyCode(keyCode);
      return this;
   }

   public boolean isBound() {
      return this.getKeyCode() != -1;
   }

   public int getKeyCode() {
      return this.backing.getKeybind().getKeyCode();
   }

   public String getBindingName() {
      return !this.isBound() ? "" : InputBinding.keyboard(this.getKeyCode()).getDisplayName();
   }

   public void toggle() {
      this.setEnabled(!this.isEnabled());
   }

   public boolean hasSettings() {
      return !this.settings.isEmpty();
   }

   public ArrayList<Setting<?>> getSettings() {
      return this.settings;
   }

   public void setEnabledFlag(boolean enabledFlag) {
      if (enabledFlag != this.isEnabled()) {
         this.backing.toggle();
      }
   }

   public boolean isEnabled() {
      return this.backing.isEnabled();
   }

   public void setEnabled(boolean enabled) {
      if (enabled != this.isEnabled()) {
         this.backing.toggle();
      }
   }

   public sg.modules.ClientModule getBackingModule() {
      return this.backing;
   }
}
