package settings.render;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

public final class ClientResourceLoader {
   private static final Identifier ICONS_RESOURCE_ID = Identifier.fromNamespaceAndPath(ClientResourceLoader.RESOURCE_NAMESPACE, ClientResourceLoader.ICONS_RESOURCE_PATH);
   private static final Gson GSON = new Gson();
   private static final String LINE_SEPARATOR = "\n";
   private static final String UTILITY_INSTANTIATION_MESSAGE = "This is a utility class and cannot be instantiated";
   private static final String RESOURCE_NAMESPACE = "rose";
   private static final String ICONS_RESOURCE_PATH = "fonts/icons.json";

   private static InputStream openClasspathResource(Identifier resourceId) throws IOException {
      String resourcePath = "/assets/" + resourceId.getNamespace() + "/" + resourceId.getPath();
      InputStream resourceStream = ClientResourceLoader.class.getResourceAsStream(resourcePath);
      if (resourceStream == null) {
         throw new IOException(resourcePath);
      } else {
         return resourceStream;
      }
   }

   private static InputStream openResource(Identifier resourceId) throws IOException {
      if (resourceId.equals(ICONS_RESOURCE_ID)) {
         return openClasspathResource(resourceId);
      }

      ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
      return resourceManager.getResource(resourceId).isPresent() ? resourceManager.open(resourceId) : openClasspathResource(resourceId);
   }

   public static String readText(Identifier resourceId) {
      return readText(resourceId, LINE_SEPARATOR);
   }

   @Generated
   private ClientResourceLoader() {
      throw new UnsupportedOperationException(UTILITY_INSTANTIATION_MESSAGE);
   }

   public static <T> T readJson(Identifier resourceId, Class<T> targetType) {
      return GSON.fromJson(readText(resourceId), targetType);
   }

   public static String readText(Identifier resourceId, String lineSeparator) {
      try (
         InputStream resourceStream = openResource(resourceId);
         BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
      ) {
         return reader.lines().collect(Collectors.joining(lineSeparator));
      } catch (IOException var10) {
         throw new RuntimeException(var10);
      }
   }

   public static JsonObject readJsonObject(Identifier resourceId) {
      return JsonParser.parseString(readText(resourceId)).getAsJsonObject();
   }
}
