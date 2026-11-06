package sg.config;

import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

/** Data root override used only by explicit local Fabric development runs. */
public final class ClientDirectories {
   private ClientDirectories() {
   }

   public static String forDevelopment(String productionRoot) {
      if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
         String developmentRoot = System.getProperty("rose.configRoot");
         if (developmentRoot != null && !developmentRoot.isBlank()) {
            Path path = Path.of(developmentRoot);
            if (!path.isAbsolute()) {
               throw new IllegalArgumentException("rose.configRoot must be an absolute directory");
            }
            return path.normalize().toString();
         }
      }
      return productionRoot;
   }
}
