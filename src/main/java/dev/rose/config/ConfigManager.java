package dev.rose.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import dev.rose.RoseClient;
import dev.rose.looks.Look;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.setting.Setting;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import net.fabricmc.loader.api.FabricLoader;


public final class ConfigManager {
   private static final Gson PRETTY = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   private static final Gson COMPACT = new GsonBuilder().disableHtmlEscaping().create();
   private static final Path DIR = FabricLoader.getInstance().getConfigDir().resolve("rose");
   private static final Path MAIN = DIR.resolve("config.json");
   private static final Path PROFILES = DIR.resolve("profiles");
   private static final long SAVE_DELAY_MS = 1000L;
   private static final int VERSION = 1;
   private static final String CODE_PREFIX = "rose1:";
   private static final int MAX_CODE_BYTES = 1048576;
   private static boolean loading;
   private static long dirtySince = -1L;

   private ConfigManager() {
   }

   public static void load() {
      if (!Files.exists(MAIN)) {
         apply(new JsonObject());
         if (Modules.looks() != null) {
            Modules.looks().apply(Look.SIGNATURE, false);
         }

         save();
         RoseClient.LOGGER.info("[Rose] config created with defaults");
      } else {
         try {
            apply(read(MAIN));
            RoseClient.LOGGER.info("[Rose] config loaded");
         } catch (IOException | RuntimeException e) {
            RoseClient.LOGGER.error("[Rose] config is broken, using defaults", e);

            try {
               Files.copy(MAIN, DIR.resolve("config.broken.json"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException var2) {
            }

            apply(new JsonObject());
         }
      }
   }

   public static void markDirty() {
      if (!loading && dirtySince < 0L) {
         dirtySince = System.currentTimeMillis();
      }
   }

   public static void tick() {
      if (dirtySince >= 0L && System.currentTimeMillis() - dirtySince >= 1000L) {
         save();
      }
   }

   public static void save() {
      dirtySince = -1L;
      if (RoseClient.uiTest() == null) {
         write(MAIN, toJson());
      }
   }

   public static JsonObject toJson() {
      JsonObject root = new JsonObject();
      root.addProperty("version", 1);
      JsonObject modules = new JsonObject();

      for (Module module : Modules.all()) {
         modules.add(module.id(), moduleToJson(module));
      }

      root.add("modules", modules);
      return root;
   }

   public static JsonObject moduleToJson(Module module) {
      JsonObject entry = new JsonObject();
      entry.addProperty("enabled", module.isEnabled());
      if (module.isFavorite()) {
         entry.addProperty("favorite", true);
      }

      entry.add("key", module.key().toJson());
      JsonObject settings = new JsonObject();

      for (Setting<?> setting : module.settings()) {
         settings.add(setting.id(), setting.toJson());
      }

      entry.add("settings", settings);
      return entry;
   }

   public static void applyModule(Module module, JsonObject entry) {
      JsonElement enabled = entry.get("enabled");
      boolean on = enabled != null && enabled.isJsonPrimitive() && enabled.getAsJsonPrimitive().isBoolean()
         ? enabled.getAsBoolean()
         : module.enabledByDefault();
      module.setEnabled(on);
      JsonElement favorite = entry.get("favorite");
      module.setFavorite(favorite != null && favorite.isJsonPrimitive() && favorite.getAsJsonPrimitive().isBoolean() && favorite.getAsBoolean());
      if (entry.has("key")) {
         module.key().fromJson(entry.get("key"));
      } else {
         module.key().reset();
      }

      JsonObject settings = object(entry, "settings");

      for (Setting<?> setting : module.settings()) {
         if (settings.has(setting.id())) {
            setting.fromJson(settings.get(setting.id()));
         } else {
            setting.reset();
         }
      }
   }

   public static void apply(JsonObject root) {
      loading = true;

      try {
         JsonObject modules = object(root, "modules");

         for (Module module : Modules.all()) {
            applyModule(module, object(modules, module.id()));
         }
      } finally {
         loading = false;
      }
   }

   public static List<String> profiles() {
      List<String> names = new ArrayList<>();
      if (!Files.isDirectory(PROFILES)) {
         return names;
      }

      try (Stream<Path> files = Files.list(PROFILES)) {
         files.<String>map(file -> file.getFileName().toString())
            .filter(name -> name.endsWith(".json"))
            .map(name -> name.substring(0, name.length() - 5))
            .sorted(String.CASE_INSENSITIVE_ORDER)
            .forEach(names::add);
      } catch (IOException e) {
         RoseClient.LOGGER.error("[Rose] cannot list profiles", e);
      }

      return names;
   }

   public static void saveProfile(String name) {
      write(PROFILES.resolve(fileName(name)), toJson());
   }

   public static boolean loadProfile(String name) {
      Path file = PROFILES.resolve(fileName(name));
      if (!Files.exists(file)) {
         return false;
      }

      try {
         apply(read(file));
         Modules.onConfigLoaded();
         save();
         return true;
      } catch (IOException | RuntimeException e) {
         RoseClient.LOGGER.error("[Rose] cannot load profile {}", name, e);
         return false;
      }
   }

   public static boolean deleteProfile(String name) {
      try {
         return Files.deleteIfExists(PROFILES.resolve(fileName(name)));
      } catch (IOException e) {
         return false;
      }
   }

   public static String exportCode() {
      byte[] json = COMPACT.toJson(toJson()).getBytes(StandardCharsets.UTF_8);
      ByteArrayOutputStream packed = new ByteArrayOutputStream();

      try (DeflaterOutputStream out = new DeflaterOutputStream(packed)) {
         out.write(json);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }

      return "rose1:" + Base64.getUrlEncoder().withoutPadding().encodeToString(packed.toByteArray());
   }

   public static boolean importCode(String code) {
      String trimmed = code.trim();
      if (!trimmed.startsWith("rose1:")) {
         return false;
      }

      try {
         byte[] packed = Base64.getUrlDecoder().decode(trimmed.substring("rose1:".length()));
         InputStream in = new InflaterInputStream(new ByteArrayInputStream(packed));

         byte[] json;
         try {
            json = in.readNBytes(1048577);
         } catch (Throwable var8) {
            try {
               in.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         in.close();
         if (json.length > 1048576) {
            return false;
         }

         JsonElement root = JsonParser.parseString(new String(json, StandardCharsets.UTF_8));
         if (!root.isJsonObject()) {
            return false;
         }

         apply(root.getAsJsonObject());
         Modules.onConfigLoaded();
         save();
         return true;
      } catch (IOException | RuntimeException e) {
         RoseClient.LOGGER.warn("[Rose] config code rejected", e);
         return false;
      }
   }

   private static JsonObject object(JsonObject parent, String key) {
      JsonElement element = parent.get(key);
      return element != null && element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
   }

   private static JsonObject read(Path file) throws IOException {
      JsonElement root = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8));
      if (!root.isJsonObject()) {
         throw new JsonParseException("Config root is not an object");
      } else {
         return root.getAsJsonObject();
      }
   }

   private static void write(Path file, JsonObject json) {
      try {
         Files.createDirectories(file.getParent());
         Path temp = file.resolveSibling(file.getFileName() + ".tmp");
         Files.writeString(temp, PRETTY.toJson(json), StandardCharsets.UTF_8);

         try {
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (IOException e) {
         RoseClient.LOGGER.error("[Rose] cannot save {}", file, e);
      }
   }

   private static String fileName(String name) {
      String clean = name.replaceAll("[^\\p{L}\\p{N} _-]", "").trim();
      return (clean.isEmpty() ? "profile" : clean) + ".json";
   }
}
