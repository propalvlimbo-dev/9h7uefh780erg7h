package sg.render;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

public final class HatCatalog {
   private static final String[][] BUILTIN_HATS = new String[][]{
      {HatCatalog.CHEF_KEY, HatCatalog.CHEF_DISPLAY_NAME},
      {HatCatalog.CAPTAIN_KEY, HatCatalog.CAPTAIN_DISPLAY_NAME},
      {HatCatalog.PLUMBER_KEY, HatCatalog.PLUMBER_DISPLAY_NAME},
      {HatCatalog.KINGS_GUARD_KEY, HatCatalog.KINGS_GUARD_DISPLAY_NAME},
      {HatCatalog.MAGE_KEY, HatCatalog.MAGE_DISPLAY_NAME},
      {HatCatalog.GIBUS_KEY, HatCatalog.GIBUS_DISPLAY_NAME},
      {HatCatalog.CLOWN_NOSE_KEY, HatCatalog.CLOWN_NOSE_DISPLAY_NAME},
      {HatCatalog.AI_GLASSES_KEY, HatCatalog.AI_GLASSES_DISPLAY_NAME},
      {HatCatalog.MOVIE_SHADES_KEY, HatCatalog.MOVIE_SHADES_DISPLAY_NAME},
      {HatCatalog.EYE_PATCH_KEY, HatCatalog.EYE_PATCH_DISPLAY_NAME},
      {HatCatalog.GARLAND_KEY, HatCatalog.GARLAND_DISPLAY_NAME},
      {HatCatalog.CREWMATE_KEY, HatCatalog.CREWMATE_DISPLAY_NAME},
      {HatCatalog.ADVENTURER_KEY, HatCatalog.ADVENTURER_DISPLAY_NAME},
      {HatCatalog.CRYING_BABY_KEY, HatCatalog.CRYING_BABY_DISPLAY_NAME},
      {HatCatalog.EGG_FRIEND_KEY, HatCatalog.EGG_FRIEND_DISPLAY_NAME},
      {HatCatalog.EGGED_KEY, HatCatalog.EGGED_DISPLAY_NAME},
      {HatCatalog.FLAG_KEY, HatCatalog.FLAG_DISPLAY_NAME},
      {HatCatalog.FUNGUY_KEY, HatCatalog.FUNGUY_DISPLAY_NAME},
      {HatCatalog.HELLBREAKER_KEY, HatCatalog.HELLBREAKER_DISPLAY_NAME},
      {HatCatalog.MAGISTRATE_KEY, HatCatalog.MAGISTRATE_DISPLAY_NAME},
      {HatCatalog.SPONGE_KEY, HatCatalog.SPONGE_DISPLAY_NAME},
      {HatCatalog.THEATER_KEY, HatCatalog.THEATER_DISPLAY_NAME},
      {HatCatalog.MASTER_ADVENTURER_KEY, HatCatalog.MASTER_ADVENTURER_DISPLAY_NAME},
      {HatCatalog.AI_GLASSES_ON_KEY, HatCatalog.AI_GLASSES_ON_DISPLAY_NAME}
   };
   private static final Map<String, HatDefinition> loadedHats = new LinkedHashMap<>();
   private static boolean loadAttempted;
   private static final String MODEL_NAMESPACE = "rose";
   private static final String TEXTURE_NAMESPACE = "rose";
   private static final String CHEF_KEY = "chef";
   private static final String CHEF_DISPLAY_NAME = "Шеф-повар";
   private static final String CAPTAIN_KEY = "captain";
   private static final String CAPTAIN_DISPLAY_NAME = "Капитан";
   private static final String PLUMBER_KEY = "plumber";
   private static final String PLUMBER_DISPLAY_NAME = "Сантехник";
   private static final String KINGS_GUARD_KEY = "kings_guard";
   private static final String KINGS_GUARD_DISPLAY_NAME = "Гвардеец";
   private static final String MAGE_KEY = "mage";
   private static final String MAGE_DISPLAY_NAME = "Маг";
   private static final String GIBUS_KEY = "gibus";
   private static final String GIBUS_DISPLAY_NAME = "Гибус";
   private static final String CLOWN_NOSE_KEY = "clown_nose";
   private static final String CLOWN_NOSE_DISPLAY_NAME = "Нос клоуна";
   private static final String AI_GLASSES_KEY = "ai_glasses";
   private static final String AI_GLASSES_DISPLAY_NAME = "AI-очки";
   private static final String MOVIE_SHADES_KEY = "movie_shades";
   private static final String MOVIE_SHADES_DISPLAY_NAME = "Очки";
   private static final String EYE_PATCH_KEY = "eye_patch";
   private static final String EYE_PATCH_DISPLAY_NAME = "Повязка";
   private static final String GARLAND_KEY = "garland";
   private static final String GARLAND_DISPLAY_NAME = "Гирлянда";
   private static final String CREWMATE_KEY = "crewmate";
   private static final String CREWMATE_DISPLAY_NAME = "Амонгус";
   private static final String ADVENTURER_KEY = "adventurer";
   private static final String ADVENTURER_DISPLAY_NAME = "Авантюрист";
   private static final String CRYING_BABY_KEY = "crying_baby";
   private static final String CRYING_BABY_DISPLAY_NAME = "Малыш";
   private static final String EGG_FRIEND_KEY = "egg_friend";
   private static final String EGG_FRIEND_DISPLAY_NAME = "Яйцо-друг";
   private static final String EGGED_KEY = "egged";
   private static final String EGGED_DISPLAY_NAME = "Яйцо";
   private static final String FLAG_KEY = "flag";
   private static final String FLAG_DISPLAY_NAME = "Флаг";
   private static final String FUNGUY_KEY = "funguy";
   private static final String FUNGUY_DISPLAY_NAME = "Грибок";
   private static final String HELLBREAKER_KEY = "hellbreaker";
   private static final String HELLBREAKER_DISPLAY_NAME = "Визор";
   private static final String MAGISTRATE_KEY = "magistrate";
   private static final String MAGISTRATE_DISPLAY_NAME = "Магистрат";
   private static final String SPONGE_KEY = "sponge";
   private static final String SPONGE_DISPLAY_NAME = "Губка";
   private static final String THEATER_KEY = "theater";
   private static final String THEATER_DISPLAY_NAME = "Театр";
   private static final String MASTER_ADVENTURER_KEY = "master_adventurer";
   private static final String MASTER_ADVENTURER_DISPLAY_NAME = "Мастер-авантюрист";
   private static final String AI_GLASSES_ON_KEY = "ai_glasses_on";
   private static final String AI_GLASSES_ON_DISPLAY_NAME = "AI-очки (вкл)";

   private HatCatalog() {
   }

   public static void clearCache() {
      loadAttempted = false;
      loadedHats.clear();
   }

   public static String getDefaultHatKey() {
      return BUILTIN_HATS[0][0];
   }

   private static void ensureAvailableHatsLoaded() {
      if (!loadAttempted || loadedHats.size() != BUILTIN_HATS.length) {
         loadAttempted = true;

         for (String[] builtinHat : BUILTIN_HATS) {
            if (!loadedHats.containsKey(builtinHat[0])) {
               HatDefinition hat = loadHat(builtinHat[0], builtinHat[1]);
               if (hat != null) {
                  loadedHats.put(hat.key(), hat);
               }
            }
         }
      }
   }

   public static HatDefinition getHat(String hatKey) {
      ensureAvailableHatsLoaded();
      return loadedHats.get(hatKey);
   }

   private static HatDefinition loadHat(String hatKey, String displayName) {
      Identifier modelId = Identifier.fromNamespaceAndPath(MODEL_NAMESPACE, "hats/" + hatKey + "/model.geo.json");
      Identifier textureId = Identifier.fromNamespaceAndPath(TEXTURE_NAMESPACE, "hats/" + hatKey + "/texture.png");
      Optional modelResource = Minecraft.getInstance().getResourceManager().getResource(modelId);
      if (modelResource.isEmpty()) {
         return null;
      }

      try (InputStream modelStream = ((Resource)modelResource.get()).open()) {
         return new HatDefinition(hatKey, displayName, textureId, CosmeticModel.loadRelativePivotGeometry(modelStream));
      } catch (Exception loadFailure) {
         return null;
      }
   }

   public static List<HatDefinition> getHats() {
      ensureAvailableHatsLoaded();
      return new ArrayList<>(loadedHats.values());
   }
}
