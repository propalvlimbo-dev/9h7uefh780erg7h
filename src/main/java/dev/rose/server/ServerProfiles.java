package dev.rose.server;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rose.RoseClient;
import dev.rose.config.ConfigManager;
import dev.rose.module.Modules;
import dev.rose.notify.Notifications;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resources.language.I18n;


public final class ServerProfiles {
   private static final Path DIR = FabricLoader.getInstance().getConfigDir().resolve("rose");
   private static final Path FILE = DIR.resolve("server_profiles.json");
   private static final Path BACKUP = DIR.resolve("before_server.json");
   private static List<ServerProfiles.Binding> bindings;
   private static String activeProfile;
   private static JsonObject previous;

   private ServerProfiles() {
   }

   public static List<ServerProfiles.Binding> bindings() {
      if (bindings == null) {
         load();
      }

      return List.copyOf(bindings);
   }

   public static void bind(String server, String profile) {
      String host = KnownServers.normalizeAddress(server);
      if (!host.isEmpty() && profile != null && !profile.isEmpty()) {
         if (bindings == null) {
            load();
         }

         bindings.removeIf(binding -> binding.server.equals(host));
         bindings.add(new ServerProfiles.Binding(host, profile));
         save();
      }
   }

   public static void unbind(String server) {
      if (bindings == null) {
         load();
      }

      bindings.removeIf(binding -> binding.server.equals(server));
      save();
   }

   public static String activeProfile() {
      return activeProfile;
   }

   public static void restoreAfterCrash() {
      if (Files.isRegularFile(BACKUP)) {
         try {
            JsonElement root = JsonParser.parseString(Files.readString(BACKUP, StandardCharsets.UTF_8));
            if (root.isJsonObject()) {
               ConfigManager.apply(root.getAsJsonObject());
               Modules.onConfigLoaded();
               ConfigManager.save();
               RoseClient.LOGGER.info("[Rose] restored settings saved before a server profile");
            }

            Files.deleteIfExists(BACKUP);
         } catch (IOException | RuntimeException e) {
            RoseClient.LOGGER.warn("[Rose] cannot restore settings from {}", BACKUP, e);
         }
      }
   }

   public static void onJoin() {
      String host = KnownServers.currentHost();
      if (host != null && activeProfile == null) {
         ServerProfiles.Binding binding = bindings().stream().filter(candidate -> candidate.matches(host)).findFirst().orElse(null);
         if (binding != null) {
            if (!ConfigManager.profiles().contains(binding.profile)) {
               Notifications.show(Notifications.Kind.WARNING, I18n.get("rose.server_profiles.missing", new Object[]{binding.profile}), host);
            } else {
               previous = ConfigManager.toJson();
               writeBackup(previous);
               if (ConfigManager.loadProfile(binding.profile)) {
                  activeProfile = binding.profile;
                  Notifications.show(
                     Notifications.Kind.SUCCESS,
                     I18n.get("rose.server_profiles.enabled", new Object[]{binding.profile}),
                     KnownServers.currentName()
                  );
               } else {
                  previous = null;
                  deleteBackup();
               }
            }
         }
      }
   }

   public static void onLeave() {
      if (activeProfile != null && previous != null) {
         String profile = activeProfile;
         activeProfile = null;
         ConfigManager.saveProfile(profile);
         ConfigManager.apply(previous);
         Modules.onConfigLoaded();
         ConfigManager.save();
         previous = null;
         deleteBackup();
         Notifications.show(Notifications.Kind.NEUTRAL, I18n.get("rose.server_profiles.restored", new Object[0]), profile);
      }
   }

   private static void load() {
      bindings = new ArrayList<>();
      if (Files.isRegularFile(FILE)) {
         try {
            JsonElement root = JsonParser.parseString(Files.readString(FILE, StandardCharsets.UTF_8));
            if (root.isJsonObject()) {
               JsonElement var2 = root.getAsJsonObject().get("bindings");
               if (var2 instanceof JsonArray) {
                  for (JsonElement element : (JsonArray)var2) {
                     if (element.isJsonObject()) {
                        JsonObject object = element.getAsJsonObject();
                        if (object.has("server") && object.has("profile")) {
                           bindings.add(
                              new ServerProfiles.Binding(object.get("server").getAsString().toLowerCase(Locale.ROOT), object.get("profile").getAsString())
                           );
                        }
                     }
                  }
               }
            }
         } catch (IOException | RuntimeException e) {
            RoseClient.LOGGER.warn("[Rose] cannot read server profiles", e);
         }
      }
   }

   private static void save() {
      if (RoseClient.uiTest() == null) {
         JsonObject root = new JsonObject();
         JsonArray array = new JsonArray();

         for (ServerProfiles.Binding binding : bindings) {
            JsonObject object = new JsonObject();
            object.addProperty("server", binding.server);
            object.addProperty("profile", binding.profile);
            array.add(object);
         }

         root.add("bindings", array);

         try {
            Files.createDirectories(DIR);
            Files.writeString(FILE, new GsonBuilder().setPrettyPrinting().create().toJson(root), StandardCharsets.UTF_8);
         } catch (IOException e) {
            RoseClient.LOGGER.warn("[Rose] cannot save server profiles", e);
         }
      }
   }

   private static void writeBackup(JsonObject config) {
      if (RoseClient.uiTest() == null) {
         try {
            Files.createDirectories(DIR);
            Files.writeString(BACKUP, config.toString(), StandardCharsets.UTF_8);
         } catch (IOException e) {
            RoseClient.LOGGER.warn("[Rose] cannot back up settings", e);
         }
      }
   }

   private static void deleteBackup() {
      try {
         Files.deleteIfExists(BACKUP);
      } catch (IOException var1) {
      }
   }

   public record Binding(String server, String profile) {
      boolean matches(String host) {
         return host.equals(this.server) || host.endsWith("." + this.server);
      }
   }
}
