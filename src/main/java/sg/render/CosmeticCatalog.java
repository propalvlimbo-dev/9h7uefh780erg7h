package sg.render;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

public final class CosmeticCatalog {
   private static final Map<CosmeticCategory, List<CosmeticDefinition>> modelsByCategory = new EnumMap<>(CosmeticCategory.class);
   private static final Map<String, CosmeticDefinition> modelsByKey = new LinkedHashMap<>();
   private static boolean loadAttempted;
   private static final String CATALOG_NAMESPACE = "rose";
   private static final String CATALOG_PATH = "cosmetics/catalog.json";
   private static final String NAME_KEY = "name";
   private static final String DISPLAY_NAME_KEY = "displayName";
   private static final String CATEGORY_KEY = "category";
   private static final String MODEL_NAMESPACE = "rose";
   private static final String TEXTURE_NAMESPACE = "rose";
   private static final String HAS_ANIMATION_KEY = "animation";
   private static final String ANIMATION_ENABLED_KEY = "animation";
   private static final String ANIMATION_NAMESPACE = "rose";
   private static final String HAS_ATTACHMENT_KEY = "attach";
   private static final String ATTACHMENT_VALUE_KEY = "attach";
   private static final String SCALE_KEY = "scale";
   private static final String OFFSET_X_KEY = "x";
   private static final String OFFSET_Y_KEY = "y";
   private static final String OFFSET_Z_KEY = "z";

   public static List<CosmeticDefinition> getModels(CosmeticCategory category) {
      ensureLoaded();
      return modelsByCategory.getOrDefault(category, List.of());
   }

   private static void ensureLoaded() {
      if (!loadAttempted) {
         loadAttempted = true;
         Identifier catalogId = Identifier.fromNamespaceAndPath(CATALOG_NAMESPACE, CATALOG_PATH);
         Optional catalogResource = Minecraft.getInstance().getResourceManager().getResource(catalogId);
         if (!catalogResource.isEmpty()) {
            try (InputStream catalogStream = ((Resource)catalogResource.get()).open()) {
               for (JsonElement entryJson : JsonParser.parseReader(new InputStreamReader(catalogStream, StandardCharsets.UTF_8)).getAsJsonArray()) {
                  loadModelDefinition(entryJson.getAsJsonObject());
               }
            } catch (Exception loadFailure) {
            }
         }
      }
   }

   public static void clearCache() {
      loadAttempted = false;
      modelsByCategory.clear();
      modelsByKey.clear();
   }

   public static String getDefaultModelName(CosmeticCategory category) {
      List models = getModels(category);
      return models.isEmpty() ? "" : ((CosmeticDefinition)models.getFirst()).name();
   }

   private static void loadModelDefinition(JsonObject definitionJson) {
      String modelName = definitionJson.get(NAME_KEY).getAsString();
      String displayName = definitionJson.get(DISPLAY_NAME_KEY).getAsString();
      CosmeticCategory category = CosmeticCategory.fromConfigKey(definitionJson.get(CATEGORY_KEY).getAsString());
      String resourceDirectory = "cosmetics/" + category.getConfigKey() + "/" + modelName + "/";
      Identifier modelId = Identifier.fromNamespaceAndPath(MODEL_NAMESPACE, resourceDirectory + "model.geo.json");
      Identifier textureId = Identifier.fromNamespaceAndPath(TEXTURE_NAMESPACE, resourceDirectory + "texture.png");
      Optional modelResource = Minecraft.getInstance().getResourceManager().getResource(modelId);
      if (!modelResource.isEmpty()) {
         try (InputStream modelStream = ((Resource)modelResource.get()).open()) {
            CosmeticAnimationClip defaultClip = null;
            Map clips = Map.of();
            if (definitionJson.has(HAS_ANIMATION_KEY) && definitionJson.get(ANIMATION_ENABLED_KEY).getAsBoolean()) {
               Optional animationResource = Minecraft.getInstance().getResourceManager().getResource(Identifier.fromNamespaceAndPath(ANIMATION_NAMESPACE, resourceDirectory + "animation.json"));
               if (animationResource.isPresent()) {
                  try (InputStream animationStream = ((Resource)animationResource.get()).open()) {
                     clips = Map.copyOf(CosmeticAnimationClip.э_ea411899(animationStream));
                     if (!clips.isEmpty()) {
                        defaultClip = (CosmeticAnimationClip)clips.values().iterator().next();
                     }
                  }
               }
            }

            CosmeticAttachmentPoint attachment = definitionJson.has(HAS_ATTACHMENT_KEY) ? CosmeticAttachmentPoint.fromKey(definitionJson.get(ATTACHMENT_VALUE_KEY).getAsString()) : CosmeticAttachmentPoint.defaultForCategory(category);
            CosmeticDefinition definition = new CosmeticDefinition(
               modelName,
               displayName,
               category,
               definitionJson.get(SCALE_KEY).getAsFloat(),
               definitionJson.get(OFFSET_X_KEY).getAsFloat(),
               definitionJson.get(OFFSET_Y_KEY).getAsFloat(),
               definitionJson.get(OFFSET_Z_KEY).getAsFloat(),
               textureId,
               CosmeticModel.loadAbsolutePivotGeometry(modelStream),
               defaultClip,
               attachment,
               clips
            );
            modelsByCategory.computeIfAbsent(category, categoryKey -> new ArrayList<>()).add(definition);
            modelsByKey.put(category.getConfigKey() + "/" + modelName, definition);
         } catch (Exception loadFailure) {
         }
      }
   }

   public static CosmeticDefinition getModel(CosmeticCategory category, String modelName) {
      ensureLoaded();
      return modelsByKey.get(category.getConfigKey() + "/" + modelName);
   }

   private CosmeticCatalog() {
   }
}
