package dev.rose.module.impl;

import dev.rose.module.Category;
import dev.rose.module.Module;


public final class KeysModule extends Module {
   public KeysModule() {
      super("keys", Category.INTERFACE, true);
   }

   @Override
   public int icon() {
      return 57988;
   }

   @Override
   public boolean isAlwaysOn() {
      return true;
   }
}
