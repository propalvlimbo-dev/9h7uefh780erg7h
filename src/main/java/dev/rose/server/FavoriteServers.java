package dev.rose.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rose.RoseClient;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.ServerData;


public final class FavoriteServers {
   private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("rose").resolve("favorite_servers.json");
   private static Set<String> favorites;

   private FavoriteServers() {
   }

   public static boolean isFavorite(ServerData server) {
      return server != null && load().contains(key(server));
   }

   public static boolean toggle(ServerData server) {
      Set<String> set = load();
      String key = key(server);
      boolean now = !set.remove(key);
      if (now) {
         set.add(key);
      }

      save();
      return now;
   }

   private static String key(ServerData server) {
      return server.ip.trim().toLowerCase(Locale.ROOT);
   }

   private static Set<String> load() {
      if (favorites != null) {
         return favorites;
      }

      favorites = new LinkedHashSet<>();
      if (!Files.exists(FILE)) {
         return favorites;
      }

      try {
         JsonElement root = JsonParser.parseString(Files.readString(FILE, StandardCharsets.UTF_8));
         if (root.isJsonObject()) {
            JsonElement var2 = root.getAsJsonObject().get("servers");
            if (var2 instanceof JsonArray) {
               for (JsonElement element : (JsonArray)var2) {
                  if (element.isJsonPrimitive()) {
                     favorites.add(element.getAsString().trim().toLowerCase(Locale.ROOT));
                  }
               }
            }
         }
      } catch (IOException | RuntimeException e) {
         RoseClient.LOGGER.warn("[Rose] cannot read favorite servers", e);
      }

      return favorites;
   }

   private static void save() {
      if (RoseClient.uiTest() == null) {
         JsonObject root = new JsonObject();
         JsonArray array = new JsonArray();
         favorites.forEach(array::add);
         root.add("servers", array);

         try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, root.toString(), StandardCharsets.UTF_8);
         } catch (IOException e) {
            RoseClient.LOGGER.warn("[Rose] cannot save favorite servers", e);
         }
      }
   }
}
