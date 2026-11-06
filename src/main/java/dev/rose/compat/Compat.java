package dev.rose.compat;

import dev.rose.RoseClient;
import java.lang.reflect.Method;
import net.fabricmc.loader.api.FabricLoader;


public final class Compat {
   private static final boolean SODIUM = loaded("sodium");
   private static final boolean IRIS = loaded("iris");
   private static final boolean IMMEDIATELY_FAST = loaded("immediatelyfast");
   private static final boolean MOD_MENU = loaded("modmenu");
   private static Method irisInstance;
   private static Method irisShaderPackInUse;
   private static boolean irisBroken;

   private Compat() {
   }

   private static boolean loaded(String id) {
      return FabricLoader.getInstance().isModLoaded(id);
   }

   public static boolean sodium() {
      return SODIUM;
   }

   public static boolean iris() {
      return IRIS;
   }

   public static boolean immediatelyFast() {
      return IMMEDIATELY_FAST;
   }

   public static boolean modMenu() {
      return MOD_MENU;
   }

   public static boolean shaderPackActive() {
      if (IRIS && !irisBroken) {
         try {
            if (irisInstance == null) {
               Class<?> api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
               irisInstance = api.getMethod("getInstance");
               irisShaderPackInUse = api.getMethod("isShaderPackInUse");
            }

            return (Boolean)irisShaderPackInUse.invoke(irisInstance.invoke(null));
         } catch (ReflectiveOperationException | RuntimeException e) {
            irisBroken = true;
            RoseClient.LOGGER.warn("[Rose] cannot ask Iris about shader packs", e);
            return false;
         }
      } else {
         return false;
      }
   }
}
