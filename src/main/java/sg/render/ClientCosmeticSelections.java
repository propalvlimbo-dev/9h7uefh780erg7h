package sg.render;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import sg.handler.PartyCosmeticsPayload;
import sg.handler.PartyClient;
import sg.client.RoseClient;
import sg.animation.Easings;
import sg.animation.FloatAnimation;

public final class ClientCosmeticSelections {
   private static boolean wingsEnabled;
   private static WingSelection selectedWings = WingSelection.ANGEL;
   private static boolean weaponSkinsEnabled;
   private static boolean showOnSelf = true;
   private static boolean showOnFriends = true;
   private static boolean showOnOtherPlayers = true;
   private static WeaponSkinVariant selectedSwordSkin = WeaponSkinVariant.ABOMINABLE_BLADE;
   private static WeaponSkinVariant selectedMaceSkin = WeaponSkinVariant.ABOMINABLE_BLADE;
   private static boolean hatsEnabled;
   private static String selectedHatKey = HatCatalog.getDefaultHatKey();
   private static boolean masksEnabled;
   private static EmojiMaskVariant selectedMask = EmojiMaskVariant.SMILING_SUNGLASSES;
   private static boolean backpacksEnabled;
   private static BackpackColorVariant selectedBackpackColor = BackpackColorVariant.LEATHER;
   private static boolean petsEnabled;
   private static final Map<CosmeticCategory, String> selectedCatalogModelNames = new EnumMap<>(CosmeticCategory.class);
   private static final EnumSet<CosmeticCategory> catalogModelCategories = EnumSet.noneOf(CosmeticCategory.class);
   private static final EnumSet<CosmeticCategory> enabledAdditionalCategories = EnumSet.noneOf(CosmeticCategory.class);
   private static final Map<CosmeticCategory, FloatAnimation> additionalCategoryVisibilityAnimations = new EnumMap<>(CosmeticCategory.class);
   private static final FloatAnimation wingsVisibilityAnimation = new FloatAnimation(0.0F, ClientCosmeticSelections.WINGS_VISIBILITY_SPEED, Easings.CUBIC_OUT);
   private static final FloatAnimation hatsVisibilityAnimation = new FloatAnimation(0.0F, ClientCosmeticSelections.HATS_VISIBILITY_SPEED, Easings.CUBIC_OUT);
   private static final FloatAnimation masksVisibilityAnimation = new FloatAnimation(0.0F, ClientCosmeticSelections.MASKS_VISIBILITY_SPEED, Easings.CUBIC_OUT);
   private static final FloatAnimation backpacksVisibilityAnimation = new FloatAnimation(0.0F, ClientCosmeticSelections.BACKPACKS_VISIBILITY_SPEED, Easings.CUBIC_OUT);
   private static final FloatAnimation petsVisibilityAnimation = new FloatAnimation(0.0F, ClientCosmeticSelections.PETS_VISIBILITY_SPEED, Easings.CUBIC_OUT);
   private static final String REMOTE_BUILTIN_MODEL_EXCLUSION_PREFIX = "@";
   private static final String REMOTE_BUILTIN_MODEL_PREFIX = "@";
   private static final float ADDITIONAL_CATEGORY_VISIBILITY_SPEED = 8.0F;
   private static final float WINGS_VISIBILITY_SPEED = 8.0F;
   private static final float HATS_VISIBILITY_SPEED = 8.0F;
   private static final float MASKS_VISIBILITY_SPEED = 8.0F;
   private static final float BACKPACKS_VISIBILITY_SPEED = 8.0F;
   private static final float PETS_VISIBILITY_SPEED = 8.0F;

   public static boolean areBackpacksEnabled() {
      return backpacksEnabled;
   }

   public static boolean areWingsEnabled() {
      return wingsEnabled;
   }

   public static void toggleWings() {
      wingsEnabled = !wingsEnabled;
   }

   public static void clearCatalogModelSelection(CosmeticCategory category) {
      selectedCatalogModelNames.remove(category);
      catalogModelCategories.remove(category);
   }

   public static String getSelectedCatalogModelName(CosmeticCategory category) {
      return selectedCatalogModelNames.get(category);
   }

   public static boolean isCatalogModelSelected(CosmeticCategory category) {
      return catalogModelCategories.contains(category);
   }

   public static boolean isShownOnOtherPlayers() {
      return showOnOtherPlayers;
   }

   public static float getCategoryVisibility(CosmeticCategory category) {
      return switch (category) {
         case HATS -> getHatsVisibility();
         case MASKS -> getMasksVisibility();
         case WINGS -> getWingsVisibility();
         case BACKPACKS -> getBackpacksVisibility();
         case PETS -> getPetsVisibility();
         case EARS, TAILS, PAWS, STOCKINGS, HEADS -> getAdditionalCategoryVisibility(category);
      };
   }

   public static void selectCatalogModel(CosmeticDefinition definition) {
      if (definition != null) {
         selectCatalogModel(definition.category(), definition.name());
      }
   }

   public static CosmeticDefinition getCatalogModelForPlayer(Player player, CosmeticCategory category) {
      Minecraft client = Minecraft.getInstance();
      if (client.player != null && !player.getUUID().equals(client.player.getUUID())) {
         PartyCosmeticsPayload payload = PartyClient.Х_4d6328fc().Х_3b05c6e2(player.getUUID(), player.getGameProfile().name());
         if (payload != null && payload.valid()) {
            String modelName = payload.models().get(category.getConfigKey());
            return modelName != null && !modelName.startsWith(REMOTE_BUILTIN_MODEL_EXCLUSION_PREFIX) ? CosmeticCatalog.getModel(category, modelName) : null;
         } else {
            return null;
         }
      } else {
         return isCatalogModelSelected(category) && isCategoryEnabled(category) ? getSelectedCatalogModel(category) : null;
      }
   }

   public static boolean isShownOnSelf() {
      return showOnSelf;
   }

   public static boolean arePetsEnabled() {
      return petsEnabled;
   }

   public static boolean isCategoryEnabled(CosmeticCategory category) {
      return switch (category) {
         case HATS -> hatsEnabled;
         case MASKS -> masksEnabled;
         case WINGS -> wingsEnabled;
         case BACKPACKS -> backpacksEnabled;
         case PETS -> petsEnabled;
         case EARS, TAILS, PAWS, STOCKINGS, HEADS -> enabledAdditionalCategories.contains(category);
      };
   }

   public static WeaponSkinVariant getLegacyWeaponSkin() {
      return selectedSwordSkin;
   }

   public static void selectMaceSkin(WeaponSkinVariant skin) {
      if (skin != null) {
         selectedMaceSkin = skin;
      }
   }

   public static WeaponSkinVariant getSelectedMaceSkin() {
      return selectedMaceSkin;
   }

   public static WingSelection getSelectedWings() {
      return selectedWings;
   }

   public static void setShownOnSelf(boolean shown) {
      showOnSelf = shown;
   }

   public static void setLegacyWeaponSkin(WeaponSkinVariant skin) {
      selectedSwordSkin = skin;
   }

   public static void setShownOnOtherPlayers(boolean shown) {
      showOnOtherPlayers = shown;
   }

   public static WeaponSkinVariant getSelectedSwordSkin() {
      return selectedSwordSkin;
   }

   public static boolean shouldShowCosmeticsForPlayer(Player player, Minecraft client) {
      if (client.player != null && player.getUUID().equals(client.player.getUUID())) {
         return showOnSelf;
      }

      if (hasRemoteCosmeticsPayload(player)) {
         return true;
      }

      boolean friend = RoseClient.getInstance() != null
         && RoseClient.getInstance().getServices() != null
         && RoseClient.getInstance().getServices().getFriends().О(player.getGameProfile().name());
      return friend ? showOnFriends : showOnOtherPlayers;
   }

   public static float getWingsVisibility() {
      wingsVisibilityAnimation.updateTarget(wingsEnabled ? 1.0F : 0.0F);
      return wingsVisibilityAnimation.getValue();
   }

   public static float getBackpacksVisibility() {
      backpacksVisibilityAnimation.updateTarget(backpacksEnabled ? 1.0F : 0.0F);
      return backpacksVisibilityAnimation.getValue();
   }

   public static void setShownOnFriends(boolean shown) {
      showOnFriends = shown;
   }

   public static float getHatsVisibility() {
      hatsVisibilityAnimation.updateTarget(hatsEnabled ? 1.0F : 0.0F);
      return hatsVisibilityAnimation.getValue();
   }

   public static void selectSwordSkin(WeaponSkinVariant skin) {
      if (skin != null) {
         selectedSwordSkin = skin;
      }
   }

   public static void setMasksEnabled(boolean enabled) {
      masksEnabled = enabled;
   }

   public static boolean areWeaponSkinsEnabled() {
      return weaponSkinsEnabled;
   }

   public static boolean isShownOnFriends() {
      return showOnFriends;
   }

   public static PartyCosmeticsPayload createPartyCosmeticsPayload() {
      HashMap models = new HashMap();

      for (CosmeticCategory category : CosmeticCategory.values()) {
         if (isCategoryEnabled(category) && isCatalogModelSelected(category)) {
            String modelName = getSelectedCatalogModelName(category);
            if (modelName != null && !modelName.isBlank()) {
               models.put(category.getConfigKey(), modelName);
            }
         }
      }

      if (wingsEnabled && !isCatalogModelSelected(CosmeticCategory.WINGS)) {
         models.put(CosmeticCategory.WINGS.getConfigKey(), "@" + selectedWings.name());
      }

      if (hatsEnabled && !isCatalogModelSelected(CosmeticCategory.HATS)) {
         models.put(CosmeticCategory.HATS.getConfigKey(), "@" + selectedHatKey);
      }

      if (masksEnabled && !isCatalogModelSelected(CosmeticCategory.MASKS)) {
         models.put(CosmeticCategory.MASKS.getConfigKey(), "@" + selectedMask.name());
      }

      if (backpacksEnabled && !isCatalogModelSelected(CosmeticCategory.BACKPACKS)) {
         models.put(CosmeticCategory.BACKPACKS.getConfigKey(), "@" + selectedBackpackColor.name());
      }

      return new PartyCosmeticsPayload(1, Map.copyOf(models));
   }

   private ClientCosmeticSelections() {
   }

   public static boolean areHatsEnabled() {
      return hatsEnabled;
   }

   public static boolean hasRemoteCosmeticsPayload(Player player) {
      Minecraft client = Minecraft.getInstance();
      return client.player != null
         && !player.getUUID().equals(client.player.getUUID())
         && PartyClient.Х_4d6328fc().Х_3b05c6e2(player.getUUID(), player.getGameProfile().name()) != null;
   }

   public static String getRemoteBuiltinSelection(Player player, CosmeticCategory category) {
      Minecraft client = Minecraft.getInstance();
      if (client.player != null && !player.getUUID().equals(client.player.getUUID())) {
         PartyCosmeticsPayload payload = PartyClient.Х_4d6328fc().Х_3b05c6e2(player.getUUID(), player.getGameProfile().name());
         if (payload != null && payload.valid()) {
            String selection = payload.models().get(category.getConfigKey());
            return selection != null && selection.startsWith(REMOTE_BUILTIN_MODEL_PREFIX) ? selection.substring(1) : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void selectBackpackColor(BackpackColorVariant color) {
      if (color != null) {
         selectedBackpackColor = color;
         catalogModelCategories.remove(CosmeticCategory.BACKPACKS);
      }
   }

   public static void setBackpacksEnabled(boolean enabled) {
      backpacksEnabled = enabled;
   }

   public static void selectWings(WingSelection wings) {
      selectedWings = wings;
      catalogModelCategories.remove(CosmeticCategory.WINGS);
   }

   public static void selectMask(EmojiMaskVariant mask) {
      if (mask != null) {
         selectedMask = mask;
         catalogModelCategories.remove(CosmeticCategory.MASKS);
      }
   }

   public static String getEnabledSwordSkinDisplayName() {
      return weaponSkinsEnabled ? selectedSwordSkin.getDisplayName() : "";
   }

   public static void setWeaponSkinsEnabled(boolean enabled) {
      weaponSkinsEnabled = enabled;
   }

   public static boolean areMasksEnabled() {
      return masksEnabled;
   }

   public static float getMasksVisibility() {
      masksVisibilityAnimation.updateTarget(masksEnabled ? 1.0F : 0.0F);
      return masksVisibilityAnimation.getValue();
   }

   public static EmojiMaskVariant getSelectedMask() {
      return selectedMask;
   }

   private static float getAdditionalCategoryVisibility(CosmeticCategory category) {
      FloatAnimation visibilityAnimation = additionalCategoryVisibilityAnimations.computeIfAbsent(category, animationCategory -> new FloatAnimation(0.0F, ADDITIONAL_CATEGORY_VISIBILITY_SPEED, Easings.CUBIC_OUT));
      visibilityAnimation.updateTarget(enabledAdditionalCategories.contains(category) ? 1.0F : 0.0F);
      return visibilityAnimation.getValue();
   }

   public static String getSelectedHatKey() {
      return selectedHatKey;
   }

   public static void setCategoryEnabled(CosmeticCategory category, boolean enabled) {
      switch (category) {
         case HATS:
            setHatsEnabled(enabled);
            break;
         case MASKS:
            setMasksEnabled(enabled);
            break;
         case WINGS:
            setWingsEnabled(enabled);
            break;
         case BACKPACKS:
            setBackpacksEnabled(enabled);
            break;
         case PETS:
            setPetsEnabled(enabled);
            break;
         case EARS:
         case TAILS:
         case PAWS:
         case STOCKINGS:
         case HEADS:
            if (enabled) {
               enabledAdditionalCategories.add(category);
            } else {
               enabledAdditionalCategories.remove(category);
            }
      }
   }

   public static void setWingsEnabled(boolean enabled) {
      wingsEnabled = enabled;
   }

   public static boolean shouldShowWingsForPlayer(Player player, Minecraft client) {
      return !wingsEnabled ? false : shouldShowCosmeticsForPlayer(player, client);
   }

   public static CosmeticDefinition getSelectedCatalogModel(CosmeticCategory category) {
      String modelName = selectedCatalogModelNames.computeIfAbsent(category, CosmeticCatalog::getDefaultModelName);
      CosmeticDefinition definition = CosmeticCatalog.getModel(category, modelName);
      if (definition != null) {
         return definition;
      }

      String fallbackModelName = CosmeticCatalog.getDefaultModelName(category);
      selectedCatalogModelNames.put(category, fallbackModelName);
      return CosmeticCatalog.getModel(category, fallbackModelName);
   }

   public static BackpackColorVariant getSelectedBackpackColor() {
      return selectedBackpackColor;
   }

   public static float getPetsVisibility() {
      petsVisibilityAnimation.updateTarget(petsEnabled ? 1.0F : 0.0F);
      return petsVisibilityAnimation.getValue();
   }

   public static void setPetsEnabled(boolean enabled) {
      petsEnabled = enabled;
   }

   public static void setHatsEnabled(boolean enabled) {
      hatsEnabled = enabled;
   }

   public static void selectHat(String hatKey) {
      if (HatCatalog.getHat(hatKey) != null) {
         selectedHatKey = hatKey;
         catalogModelCategories.remove(CosmeticCategory.HATS);
      }
   }

   public static void selectCatalogModel(CosmeticCategory category, String modelName) {
      if (category != null && modelName != null && !modelName.isEmpty()) {
         selectedCatalogModelNames.put(category, modelName);
         catalogModelCategories.add(category);
      }
   }
}
