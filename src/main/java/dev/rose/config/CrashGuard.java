package dev.rose.config;

import dev.rose.RoseClient;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.notify.Notifications;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resources.language.I18n;


public final class CrashGuard {
   private static final Path LOCK = FabricLoader.getInstance().getConfigDir().resolve("rose").resolve("session.lock");
   private static final Path CRASH_REPORTS = FabricLoader.getInstance().getGameDir().resolve("crash-reports");

   private CrashGuard() {
   }

   public static void onStart() {
      try {
         if (Files.isRegularFile(LOCK)) {
            inspect(Files.getLastModifiedTime(LOCK).toMillis());
         }

         Files.createDirectories(LOCK.getParent());
         Files.writeString(LOCK, Long.toString(System.currentTimeMillis()), StandardCharsets.UTF_8);
      } catch (IOException | RuntimeException e) {
         RoseClient.LOGGER.warn("[Rose] crash guard", e);
      }
   }

   public static void onStop() {
      try {
         Files.deleteIfExists(LOCK);
      } catch (IOException var1) {
      }
   }

   private static void inspect(long sessionStart) throws IOException {
      if (Files.isDirectory(CRASH_REPORTS)) {
         Optional<Path> report;
         try (Stream<Path> files = Files.list(CRASH_REPORTS)) {
            report = files.filter(file -> file.getFileName().toString().endsWith(".txt"))
               .filter(file -> modified(file) >= sessionStart)
               .max(Comparator.comparingLong(CrashGuard::modified));
         }

         if (!report.isEmpty()) {
            String text = Files.readString(report.get(), StandardCharsets.UTF_8);
            if (text.contains("dev.rose.")) {
               Module culprit = culprit(text);
               if (culprit != null && culprit.isEnabled() && !culprit.isAlwaysOn()) {
                  culprit.setEnabled(false);
                  ConfigManager.save();
                  Notifications.show(
                     Notifications.Kind.ERROR,
                     I18n.get("rose.crash.disabled", new Object[]{culprit.name()}),
                     report.get().getFileName().toString()
                  );
               } else {
                  Notifications.show(
                     Notifications.Kind.ERROR, I18n.get("rose.crash.unknown", new Object[0]), report.get().getFileName().toString()
                  );
               }

               RoseClient.LOGGER
                  .warn("[Rose] previous session crashed inside Rose: {} (module: {})", report.get(), culprit == null ? "?" : culprit.id());
            }
         }
      }
   }

   private static Module culprit(String report) {
      Module best = null;
      int bestIndex = Integer.MAX_VALUE;

      for (Module module : Modules.all()) {
         int index = report.indexOf(module.getClass().getName());
         if (index >= 0 && index < bestIndex) {
            bestIndex = index;
            best = module;
         }
      }

      return best;
   }

   private static long modified(Path file) {
      try {
         return Files.getLastModifiedTime(file).toMillis();
      } catch (IOException e) {
         return 0L;
      }
   }
}
