package dev.rose.module.impl;

import dev.rose.module.Category;
import dev.rose.module.Module;


public final class ProfilesModule extends Module {
   public ProfilesModule() {
      super("profiles", Category.INTERFACE, true);
   }

   @Override
   public boolean isAlwaysOn() {
      return true;
   }

   @Override
   public boolean hasKeybind() {
      return false;
   }

   @Override
   public int icon() {
      return 57677;
   }
}
