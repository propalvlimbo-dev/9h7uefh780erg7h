package sg.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map.Entry;
import sg.hud.HudLayoutConfig;
import sg.render.WingSelection;
import sg.render.CosmeticCategory;
import sg.render.EmojiMaskVariant;
import sg.render.WeaponSkinVariant;
import sg.render.BackpackColorVariant;
import sg.render.ClientCosmeticSelections;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.impl.render.SwingAnimations;
import sg.theme.ColorThemeRegistry;

public class ModuleConfig extends JsonConfigFile {
   private final @com.google.gson.annotations.SerializedName("Вб")  boolean excludeSwingAnimations;
   private static final String DEFAULT_CONFIG_DIRECTORY = "rose/configs";
   private static final String DEFAULT_CONFIG_NAME = "module";
   private static final String SAVE_DISABLED_AUTO_BUY_STATE_KEY = "State";
   private static final String SAVE_AUTO_BUY_KEY = "AutoBuy";
   private static final String SAVE_WINGS_ENABLED_KEY = "Enabled";
   private static final String SAVE_WING_SELECTION_KEY = "Wing";
   private static final String SAVE_WINGS_ON_SELF_KEY = "WingsOnSelf";
   private static final String SAVE_WINGS_ON_FRIENDS_KEY = "WingsOnFriends";
   private static final String SAVE_WINGS_ON_OTHERS_KEY = "WingsOnOthers";
   private static final String SAVE_WEAPON_SKINS_ENABLED_KEY = "SkinChanger";
   private static final String SAVE_LEGACY_WEAPON_SKIN_KEY = "ItemReplacer";
   private static final String SAVE_SWORD_SKIN_KEY = "SwordItemReplacer";
   private static final String SAVE_MACE_SKIN_KEY = "MaceItemReplacer";
   private static final String SAVE_HATS_ENABLED_KEY = "HatsEnabled";
   private static final String SAVE_HAT_SELECTION_KEY = "Hat";
   private static final String SAVE_MASKS_ENABLED_KEY = "MasksEnabled";
   private static final String SAVE_MASK_SELECTION_KEY = "Mask";
   private static final String SAVE_BACKPACKS_ENABLED_KEY = "BackpacksEnabled";
   private static final String SAVE_BACKPACK_COLOR_KEY = "Backpack";
   private static final String SAVE_PETS_ENABLED_KEY = "PetsEnabled";
   private static final String SAVE_SELECTED_MODELS_KEY = "Models";
   private static final String SAVE_CATEGORY_ENABLED_STATES_KEY = "Furry";
   private static final String SAVE_COSMETICS_KEY = "Cosmetics";
   private static final String SAVE_THEME_KEY = "Theme";
   private static final String SAVE_HUD_POSITIONS_KEY = "HudPositions";
   private static final String CHECK_AUTO_BUY_KEY = "AutoBuy";
   private static final String READ_AUTO_BUY_KEY = "AutoBuy";
   private static final String LOAD_DISABLED_AUTO_BUY_STATE_KEY = "State";
   private static final String CHECK_COSMETICS_KEY = "Cosmetics";
   private static final String READ_COSMETICS_KEY = "Cosmetics";
   private static final String CHECK_WINGS_ENABLED_KEY = "Enabled";
   private static final String READ_WINGS_ENABLED_KEY = "Enabled";
   private static final String CHECK_WING_SELECTION_KEY = "Wing";
   private static final String READ_WING_SELECTION_KEY = "Wing";
   private static final String CHECK_WINGS_ON_SELF_KEY = "WingsOnSelf";
   private static final String READ_WINGS_ON_SELF_KEY = "WingsOnSelf";
   private static final String CHECK_WINGS_ON_FRIENDS_KEY = "WingsOnFriends";
   private static final String READ_WINGS_ON_FRIENDS_KEY = "WingsOnFriends";
   private static final String CHECK_WINGS_ON_OTHERS_KEY = "WingsOnOthers";
   private static final String READ_WINGS_ON_OTHERS_KEY = "WingsOnOthers";
   private static final String CHECK_WEAPON_SKINS_ENABLED_KEY = "SkinChanger";
   private static final String READ_WEAPON_SKINS_ENABLED_KEY = "SkinChanger";
   private static final String CHECK_LEGACY_WEAPON_SKIN_KEY = "ItemReplacer";
   private static final String READ_LEGACY_WEAPON_SKIN_KEY = "ItemReplacer";
   private static final String CHECK_SWORD_SKIN_KEY = "SwordItemReplacer";
   private static final String READ_SWORD_SKIN_KEY = "SwordItemReplacer";
   private static final String CHECK_MACE_SKIN_KEY = "MaceItemReplacer";
   private static final String READ_MACE_SKIN_KEY = "MaceItemReplacer";
   private static final String CHECK_LEGACY_MACE_FALLBACK_SKIN_KEY = "ItemReplacer";
   private static final String READ_LEGACY_MACE_FALLBACK_SKIN_KEY = "ItemReplacer";
   private static final String CHECK_HATS_ENABLED_KEY = "HatsEnabled";
   private static final String READ_HATS_ENABLED_KEY = "HatsEnabled";
   private static final String CHECK_HAT_SELECTION_KEY = "Hat";
   private static final String READ_HAT_SELECTION_KEY = "Hat";
   private static final String CHECK_MASKS_ENABLED_KEY = "MasksEnabled";
   private static final String READ_MASKS_ENABLED_KEY = "MasksEnabled";
   private static final String CHECK_MASK_SELECTION_KEY = "Mask";
   private static final String READ_MASK_SELECTION_KEY = "Mask";
   private static final String CHECK_BACKPACKS_ENABLED_KEY = "BackpacksEnabled";
   private static final String READ_BACKPACKS_ENABLED_KEY = "BackpacksEnabled";
   private static final String CHECK_BACKPACK_COLOR_KEY = "Backpack";
   private static final String READ_BACKPACK_COLOR_KEY = "Backpack";
   private static final String CHECK_PETS_ENABLED_KEY = "PetsEnabled";
   private static final String READ_PETS_ENABLED_KEY = "PetsEnabled";
   private static final String CHECK_SELECTED_MODELS_KEY = "Models";
   private static final String READ_SELECTED_MODELS_TYPE_CHECK_KEY = "Models";
   private static final String READ_SELECTED_MODELS_OBJECT_KEY = "Models";
   private static final String CHECK_CATEGORY_ENABLED_STATES_KEY = "Furry";
   private static final String READ_CATEGORY_ENABLED_STATES_TYPE_CHECK_KEY = "Furry";
   private static final String READ_CATEGORY_ENABLED_STATES_OBJECT_KEY = "Furry";
   private static final String CHECK_THEME_KEY = "Theme";
   private static final String READ_THEME_TYPE_CHECK_KEY = "Theme";
   private static final String READ_THEME_OBJECT_KEY = "Theme";
   private static final String CHECK_HUD_POSITIONS_KEY = "HudPositions";
   private static final String READ_HUD_POSITIONS_TYPE_CHECK_KEY = "HudPositions";
   private static final String READ_HUD_POSITIONS_OBJECT_KEY = "HudPositions";

   @Override
   protected void loadContents() {
      String fileContents = this.readFileContents();
      if (!fileContents.isEmpty()) {
         JsonParser jsonParser = new JsonParser();
         JsonObject configJson = jsonParser.parse(fileContents).getAsJsonObject();

         for (ClientModule module : RoseClient.getInstance().getServices().getModules()) {
            if ((!this.excludeSwingAnimations || !(module instanceof SwingAnimations)) && configJson.has(module.getName())) {
               module.loadConfig(configJson.get(module.getName()).getAsJsonObject());
            }
         }

         RoseClient.getInstance().getServices().getModules().getClickGui().Юр();
         if (configJson.has(CHECK_AUTO_BUY_KEY)) {
            JsonObject autoBuyJson = configJson.getAsJsonObject(READ_AUTO_BUY_KEY);
            autoBuyJson.addProperty(LOAD_DISABLED_AUTO_BUY_STATE_KEY, false);
            RoseClient.getInstance().getServices().getModules().getFunTime().loadConfig(autoBuyJson);
         }

         if (configJson.has(CHECK_COSMETICS_KEY)) {
            JsonObject cosmeticsJson = configJson.getAsJsonObject(READ_COSMETICS_KEY);
            if (cosmeticsJson.has(CHECK_WINGS_ENABLED_KEY)) {
               ClientCosmeticSelections.setWingsEnabled(cosmeticsJson.get(READ_WINGS_ENABLED_KEY).getAsBoolean());
            }

            if (cosmeticsJson.has(CHECK_WING_SELECTION_KEY)) {
               try {
                  ClientCosmeticSelections.selectWings(WingSelection.valueOf(cosmeticsJson.get(READ_WING_SELECTION_KEY).getAsString()));
               } catch (IllegalArgumentException invalidWingSelection) {
                  ClientCosmeticSelections.selectWings(WingSelection.ANGEL);
               }
            }

            if (cosmeticsJson.has(CHECK_WINGS_ON_SELF_KEY)) {
               ClientCosmeticSelections.setShownOnSelf(cosmeticsJson.get(READ_WINGS_ON_SELF_KEY).getAsBoolean());
            }

            if (cosmeticsJson.has(CHECK_WINGS_ON_FRIENDS_KEY)) {
               ClientCosmeticSelections.setShownOnFriends(cosmeticsJson.get(READ_WINGS_ON_FRIENDS_KEY).getAsBoolean());
            }

            if (cosmeticsJson.has(CHECK_WINGS_ON_OTHERS_KEY)) {
               ClientCosmeticSelections.setShownOnOtherPlayers(cosmeticsJson.get(READ_WINGS_ON_OTHERS_KEY).getAsBoolean());
            }

            if (cosmeticsJson.has(CHECK_WEAPON_SKINS_ENABLED_KEY)) {
               ClientCosmeticSelections.setWeaponSkinsEnabled(cosmeticsJson.get(READ_WEAPON_SKINS_ENABLED_KEY).getAsBoolean());
            }

            if (cosmeticsJson.has(CHECK_LEGACY_WEAPON_SKIN_KEY)) {
               try {
                  ClientCosmeticSelections.setLegacyWeaponSkin(WeaponSkinVariant.valueOf(cosmeticsJson.get(READ_LEGACY_WEAPON_SKIN_KEY).getAsString()));
               } catch (IllegalArgumentException invalidLegacyWeaponSkin) {
                  ClientCosmeticSelections.setLegacyWeaponSkin(WeaponSkinVariant.ABOMINABLE_BLADE);
               }
            }

            if (cosmeticsJson.has(CHECK_SWORD_SKIN_KEY)) {
               try {
                  ClientCosmeticSelections.selectSwordSkin(WeaponSkinVariant.valueOf(cosmeticsJson.get(READ_SWORD_SKIN_KEY).getAsString()));
               } catch (IllegalArgumentException invalidSwordSkin) {
                  ClientCosmeticSelections.selectSwordSkin(WeaponSkinVariant.ABOMINABLE_BLADE);
               }
            }

            if (cosmeticsJson.has(CHECK_MACE_SKIN_KEY)) {
               try {
                  ClientCosmeticSelections.selectMaceSkin(WeaponSkinVariant.valueOf(cosmeticsJson.get(READ_MACE_SKIN_KEY).getAsString()));
               } catch (IllegalArgumentException invalidMaceSkin) {
                  ClientCosmeticSelections.selectMaceSkin(WeaponSkinVariant.ABOMINABLE_BLADE);
               }
            } else if (cosmeticsJson.has(CHECK_LEGACY_MACE_FALLBACK_SKIN_KEY)) {
               try {
                  ClientCosmeticSelections.selectMaceSkin(WeaponSkinVariant.valueOf(cosmeticsJson.get(READ_LEGACY_MACE_FALLBACK_SKIN_KEY).getAsString()));
               } catch (IllegalArgumentException invalidLegacyMaceSkin) {
                  ClientCosmeticSelections.selectMaceSkin(WeaponSkinVariant.ABOMINABLE_BLADE);
               }
            }

            if (cosmeticsJson.has(CHECK_HATS_ENABLED_KEY)) {
               ClientCosmeticSelections.setHatsEnabled(cosmeticsJson.get(READ_HATS_ENABLED_KEY).getAsBoolean());
            }

            if (cosmeticsJson.has(CHECK_HAT_SELECTION_KEY)) {
               ClientCosmeticSelections.selectHat(cosmeticsJson.get(READ_HAT_SELECTION_KEY).getAsString());
            }

            if (cosmeticsJson.has(CHECK_MASKS_ENABLED_KEY)) {
               ClientCosmeticSelections.setMasksEnabled(cosmeticsJson.get(READ_MASKS_ENABLED_KEY).getAsBoolean());
            }

            if (cosmeticsJson.has(CHECK_MASK_SELECTION_KEY)) {
               try {
                  ClientCosmeticSelections.selectMask(EmojiMaskVariant.valueOf(cosmeticsJson.get(READ_MASK_SELECTION_KEY).getAsString()));
               } catch (IllegalArgumentException invalidMaskSelection) {
                  ClientCosmeticSelections.selectMask(EmojiMaskVariant.SMILING_SUNGLASSES);
               }
            }

            if (cosmeticsJson.has(CHECK_BACKPACKS_ENABLED_KEY)) {
               ClientCosmeticSelections.setBackpacksEnabled(cosmeticsJson.get(READ_BACKPACKS_ENABLED_KEY).getAsBoolean());
            }

            if (cosmeticsJson.has(CHECK_BACKPACK_COLOR_KEY)) {
               try {
                  ClientCosmeticSelections.selectBackpackColor(BackpackColorVariant.valueOf(cosmeticsJson.get(READ_BACKPACK_COLOR_KEY).getAsString()));
               } catch (IllegalArgumentException invalidBackpackColor) {
                  ClientCosmeticSelections.selectBackpackColor(BackpackColorVariant.LEATHER);
               }
            }

            if (cosmeticsJson.has(CHECK_PETS_ENABLED_KEY)) {
               ClientCosmeticSelections.setPetsEnabled(cosmeticsJson.get(READ_PETS_ENABLED_KEY).getAsBoolean());
            }

            if (cosmeticsJson.has(CHECK_SELECTED_MODELS_KEY) && cosmeticsJson.get(READ_SELECTED_MODELS_TYPE_CHECK_KEY).isJsonObject()) {
               JsonObject selectedModelsJson = cosmeticsJson.getAsJsonObject(READ_SELECTED_MODELS_OBJECT_KEY);

               for (Entry modelEntry : selectedModelsJson.entrySet()) {
                  try {
                     ClientCosmeticSelections.selectCatalogModel(CosmeticCategory.fromConfigKey((String)modelEntry.getKey()), ((JsonElement)modelEntry.getValue()).getAsString());
                  } catch (IllegalArgumentException unknownModelCategory) {
                  }
               }
            }

            if (cosmeticsJson.has(CHECK_CATEGORY_ENABLED_STATES_KEY) && cosmeticsJson.get(READ_CATEGORY_ENABLED_STATES_TYPE_CHECK_KEY).isJsonObject()) {
               JsonObject categoryEnabledStatesJson = cosmeticsJson.getAsJsonObject(READ_CATEGORY_ENABLED_STATES_OBJECT_KEY);

               for (Entry categoryEntry : categoryEnabledStatesJson.entrySet()) {
                  try {
                     ClientCosmeticSelections.setCategoryEnabled(CosmeticCategory.fromConfigKey((String)categoryEntry.getKey()), ((JsonElement)categoryEntry.getValue()).getAsBoolean());
                  } catch (IllegalArgumentException unknownEnabledCategory) {
                  }
               }
            }
         }

         if (configJson.has(CHECK_THEME_KEY) && configJson.get(READ_THEME_TYPE_CHECK_KEY).isJsonObject()) {
            ColorThemeRegistry themeRegistry = RoseClient.getInstance().getServices().getThemes();
            themeRegistry.loadSelectionState(configJson.getAsJsonObject(READ_THEME_OBJECT_KEY));
            themeRegistry.notifyChanged();
         }

         if (configJson.has(CHECK_HUD_POSITIONS_KEY) && configJson.get(READ_HUD_POSITIONS_TYPE_CHECK_KEY).isJsonObject()) {
            HudLayoutConfig.Ы(configJson.getAsJsonObject(READ_HUD_POSITIONS_OBJECT_KEY));
         }
      }
   }

   public ModuleConfig(String relativeDirectory, String configName) {
      super(relativeDirectory, configName);
      this.excludeSwingAnimations = true;
   }

   public void loadModuleConfig(ClientModule module) {
      if (!this.excludeSwingAnimations || !(module instanceof SwingAnimations)) {
         String fileContents = this.readFileContents();
         if (!fileContents.isEmpty()) {
            JsonParser jsonParser = new JsonParser();
            JsonObject configJson = jsonParser.parse(fileContents).getAsJsonObject();
            if (configJson.has(module.getName())) {
               module.loadConfig(configJson.get(module.getName()).getAsJsonObject());
            }
         }
      }
   }

   @Override
   protected void saveContents() {
      JsonObject configJson = new JsonObject();

      for (ClientModule module : RoseClient.getInstance().getServices().getModules()) {
         if (!this.excludeSwingAnimations || !(module instanceof SwingAnimations)) {
            configJson.add(module.getName(), module.saveConfig());
         }
      }

      JsonObject autoBuyJson = RoseClient.getInstance().getServices().getModules().getFunTime().saveConfig();
      autoBuyJson.addProperty(SAVE_DISABLED_AUTO_BUY_STATE_KEY, false);
      configJson.add(SAVE_AUTO_BUY_KEY, autoBuyJson);
      JsonObject cosmeticsJson = new JsonObject();
      cosmeticsJson.addProperty(SAVE_WINGS_ENABLED_KEY, ClientCosmeticSelections.areWingsEnabled());
      cosmeticsJson.addProperty(SAVE_WING_SELECTION_KEY, ClientCosmeticSelections.getSelectedWings().name());
      cosmeticsJson.addProperty(SAVE_WINGS_ON_SELF_KEY, ClientCosmeticSelections.isShownOnSelf());
      cosmeticsJson.addProperty(SAVE_WINGS_ON_FRIENDS_KEY, ClientCosmeticSelections.isShownOnFriends());
      cosmeticsJson.addProperty(SAVE_WINGS_ON_OTHERS_KEY, ClientCosmeticSelections.isShownOnOtherPlayers());
      cosmeticsJson.addProperty(SAVE_WEAPON_SKINS_ENABLED_KEY, ClientCosmeticSelections.areWeaponSkinsEnabled());
      cosmeticsJson.addProperty(SAVE_LEGACY_WEAPON_SKIN_KEY, ClientCosmeticSelections.getLegacyWeaponSkin().name());
      cosmeticsJson.addProperty(SAVE_SWORD_SKIN_KEY, ClientCosmeticSelections.getSelectedSwordSkin().name());
      cosmeticsJson.addProperty(SAVE_MACE_SKIN_KEY, ClientCosmeticSelections.getSelectedMaceSkin().name());
      cosmeticsJson.addProperty(SAVE_HATS_ENABLED_KEY, ClientCosmeticSelections.areHatsEnabled());
      cosmeticsJson.addProperty(SAVE_HAT_SELECTION_KEY, ClientCosmeticSelections.getSelectedHatKey());
      cosmeticsJson.addProperty(SAVE_MASKS_ENABLED_KEY, ClientCosmeticSelections.areMasksEnabled());
      cosmeticsJson.addProperty(SAVE_MASK_SELECTION_KEY, ClientCosmeticSelections.getSelectedMask().name());
      cosmeticsJson.addProperty(SAVE_BACKPACKS_ENABLED_KEY, ClientCosmeticSelections.areBackpacksEnabled());
      cosmeticsJson.addProperty(SAVE_BACKPACK_COLOR_KEY, ClientCosmeticSelections.getSelectedBackpackColor().name());
      cosmeticsJson.addProperty(SAVE_PETS_ENABLED_KEY, ClientCosmeticSelections.arePetsEnabled());
      JsonObject selectedModelsJson = new JsonObject();
      JsonObject categoryEnabledStatesJson = new JsonObject();

      for (CosmeticCategory category : CosmeticCategory.values()) {
         if (ClientCosmeticSelections.isCatalogModelSelected(category)) {
            String modelName = ClientCosmeticSelections.getSelectedCatalogModelName(category);
            if (modelName != null && !modelName.isEmpty()) {
               selectedModelsJson.addProperty(category.getConfigKey(), modelName);
            }
         }

         if (category.isFurryCategory()) {
            categoryEnabledStatesJson.addProperty(category.getConfigKey(), ClientCosmeticSelections.isCategoryEnabled(category));
         }
      }

      cosmeticsJson.add(SAVE_SELECTED_MODELS_KEY, selectedModelsJson);
      cosmeticsJson.add(SAVE_CATEGORY_ENABLED_STATES_KEY, categoryEnabledStatesJson);
      configJson.add(SAVE_COSMETICS_KEY, cosmeticsJson);
      configJson.add(SAVE_THEME_KEY, RoseClient.getInstance().getServices().getThemes().saveSelectionState());
      configJson.add(SAVE_HUD_POSITIONS_KEY, HudLayoutConfig.Ы());
      this.writeJson(configJson);
   }

   public ModuleConfig() {
      super(DEFAULT_CONFIG_DIRECTORY, DEFAULT_CONFIG_NAME);
      this.excludeSwingAnimations = false;
   }
}
