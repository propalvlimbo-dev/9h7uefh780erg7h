package settings.render;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.CustomData;

public final class WardenLootItemClassifier {
   private static final String LOW_PRIORITY_MODE = "Низкий";
   private static final String HIGH_PRIORITY_MODE = "Высокий";
   private static final String BUKKIT_VALUES_TAG = "PublicBukkitValues";
   private static final String POTION_POPPER_ITEM_ID = "potion-popper";
   private static final String BUKKIT_ITEM_ID_KEY = "minecraft:ftid";

   public static boolean isSpeedPotion(ItemStack stack) {
      if (!stack.is(Items.POTION)) {
         return false;
      }

      PotionContents potionContents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

      for (MobEffectInstance effect : potionContents.getAllEffects()) {
         if (effect.getEffect().equals(MobEffects.SPEED)) {
            return true;
         }
      }

      return false;
   }

   private WardenLootItemClassifier() {
   }

   public static boolean hasBukkitItemId(ItemStack stack, String itemId) {
      CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
      if (customData == null) {
         return false;
      }

      CompoundTag rootTag = customData.copyTag();
      return rootTag.getCompound(BUKKIT_VALUES_TAG).flatMap(var0x -> var0x.getString(BUKKIT_ITEM_ID_KEY)).map(itemId::equals).orElse(false);
   }

   private static boolean isPickaxe(ItemStack stack) {
      return stack.is(Items.WOODEN_PICKAXE)
         || stack.is(Items.STONE_PICKAXE)
         || stack.is(Items.IRON_PICKAXE)
         || stack.is(Items.GOLDEN_PICKAXE)
         || stack.is(Items.DIAMOND_PICKAXE)
         || stack.is(Items.NETHERITE_PICKAXE);
   }

   private static boolean isStandardLootExclusion(ItemStack stack) {
      Item item = stack.getItem();
      return item instanceof ShovelItem
         || item instanceof AxeItem
         || item instanceof BannerItem
         || item instanceof SmithingTemplateItem && !stack.is(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
         || stack.is(Items.BLAZE_ROD)
         || stack.is(Items.ENCHANTED_BOOK)
         || stack.is(Items.TRIDENT)
         || stack.is(Items.NAME_TAG)
         || stack.is(Items.SCULK)
         || stack.is(Items.SCULK_SENSOR)
         || stack.is(Items.ENDER_CHEST)
         || stack.is(Items.REINFORCED_DEEPSLATE)
         || stack.is(Items.PUFFERFISH)
         || stack.is(Items.HONEY_BOTTLE)
         || stack.is(Items.FERMENTED_SPIDER_EYE)
         || stack.is(Items.ANVIL)
         || stack.is(Items.COOKED_PORKCHOP);
   }

   public static boolean shouldSkipLoot(ItemStack stack, String priorityMode) {
      return LOW_PRIORITY_MODE.equals(priorityMode) ? false : isStandardLootExclusion(stack) || HIGH_PRIORITY_MODE.equals(priorityMode) && isExtendedLootExclusion(stack);
   }

   private static boolean isExtendedLootExclusion(ItemStack stack) {
      Item item = stack.getItem();
      return item instanceof ArrowItem
         || isPickaxe(stack)
         || item instanceof AxeItem
         || stack.is(Items.CHORUS_FRUIT)
         || stack.is(Items.DISC_FRAGMENT_5)
         || stack.is(Items.NAUTILUS_SHELL)
         || stack.is(Items.BOOKSHELF)
         || stack.is(Items.COOKED_MUTTON)
         || stack.is(Items.SKELETON_SPAWN_EGG)
         || stack.is(Items.CREEPER_SPAWN_EGG)
         || stack.is(Items.ZOMBIE_SPAWN_EGG)
         || stack.is(Items.VINDICATOR_SPAWN_EGG)
         || stack.is(Items.PIGLIN_SPAWN_EGG)
         || stack.is(Items.FIRE_CHARGE)
         || stack.is(Items.LEATHER)
         || stack.is(Items.SHULKER_SHELL)
         || stack.is(Items.EXPERIENCE_BOTTLE)
         || stack.is(Items.WITHER_ROSE)
         || stack.is(Items.EMERALD)
         || stack.is(Items.SUGAR)
         || hasBukkitItemId(stack, POTION_POPPER_ITEM_ID)
         || stack.has(DataComponents.JUKEBOX_PLAYABLE)
         || stack.is(Items.GHAST_TEAR)
         || stack.is(Items.DRAGON_BREATH)
         || stack.is(Items.VEX_SPAWN_EGG)
         || stack.is(Items.ENDERMITE_SPAWN_EGG)
         || stack.is(Items.CAT_SPAWN_EGG)
         || stack.is(Items.ENCHANTING_TABLE)
         || stack.is(Items.DIAMOND_HELMET)
         || stack.is(Items.DIAMOND_CHESTPLATE)
         || stack.is(Items.DIAMOND_LEGGINGS)
         || stack.is(Items.DIAMOND_BOOTS);
   }

   public static boolean hasInvisibilityPotionType(ItemStack stack) {
      PotionContents potionContents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
      Holder potion = potionContents.potion().orElse(null);
      return potion != null && (potion.equals(Potions.INVISIBILITY) || potion.equals(Potions.LONG_INVISIBILITY));
   }
}
