package com.deluxe.modules.impl.misc;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import sg.client.RoseClient;

public final class ModuleRegistry {
   private static final List<Module> modules = new ArrayList<>();
   private static final Map<sg.modules.ClientModule, Module> wrappersByModule = new IdentityHashMap<>();
   private static boolean initialized;

   public static Module findByName(String name) {
      ensureInitialized();
      for (Module module : modules) {
         if (module.name.equalsIgnoreCase(name)) {
            return module;
         }
      }

      return null;
   }

   public static void register(Module module) {
      if (module != null && !modules.contains(module)) {
         modules.add(module);
      }
   }

   public static List<Module> modules() {
      ensureInitialized();
      return modules;
   }

   private ModuleRegistry() {
   }

   public static void unregister(Module module) {
      modules.remove(module);
   }

   public static List<Module> byCategory(ModuleCategory category) {
      ensureInitialized();
      ArrayList<Module> matches = new ArrayList();

      for (Module module : modules) {
         if (module.category == category) {
            matches.add(module);
         }
      }

      return matches;
   }

   public static void ensureInitialized() {
      if (!initialized) {
         initialized = true;

         for (sg.modules.ClientModule clientModule : RoseClient.getInstance().getServices().getModules()) {
            Module wrapper = wrappersByModule.computeIfAbsent(clientModule, Module::new);
            if (!modules.contains(wrapper)) {
               modules.add(wrapper);
            }
         }
      }
   }
}
