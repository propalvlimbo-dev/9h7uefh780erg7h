package sg.modules.impl.init;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import sg.ec.MinecraftAccess;

public final class FunTimeItemCatalog implements MinecraftAccess {
   public static final List<FunTimeItemCatalog.CatalogEntry> с0 = List.of(
      Т(
         FunTimeItemCatalog.сюЦ,
         Items.SPLASH_POTION,
         Т(MobEffects.SLOWNESS, 10, 200),
         Т(MobEffects.SPEED, 5, 300),
         Т(MobEffects.BLINDNESS, 10, 100),
         Т(MobEffects.GLOWING, 1, 3600)
      ),
      Т(FunTimeItemCatalog.сюч, Items.SPLASH_POTION, Т(MobEffects.REGENERATION, 2, 900), Т(MobEffects.INVISIBILITY, 2, 12000), Т(MobEffects.INSTANT_HEALTH, 2, 0)),
      Т(FunTimeItemCatalog.сюЭ, Items.SPLASH_POTION, Т(MobEffects.STRENGTH, 5, 600), Т(MobEffects.SLOWNESS, 4, 600)),
      Т(
         FunTimeItemCatalog.сю必,
         Items.SPLASH_POTION,
         Т(MobEffects.RESISTANCE, 1, 12000),
         Т(MobEffects.FIRE_RESISTANCE, 1, 12000),
         Т(MobEffects.HEALTH_BOOST, 3, 1200),
         Т(MobEffects.INVISIBILITY, 1, 18000)
      ),
      Т(
         FunTimeItemCatalog.сющ,
         Items.SPLASH_POTION,
         Т(MobEffects.STRENGTH, 4, 1200),
         Т(MobEffects.SPEED, 3, 6000),
         Т(MobEffects.HASTE, 1, 1200),
         Т(MobEffects.INSTANT_DAMAGE, 2, 0)
      ),
      Т(
         FunTimeItemCatalog.сюГ,
         Items.SPLASH_POTION,
         Т(MobEffects.POISON, 2, 300),
         Т(MobEffects.WITHER, 2, 300),
         Т(MobEffects.SLOWNESS, 3, 300),
         Т(MobEffects.HUNGER, 5, 300),
         Т(MobEffects.GLOWING, 1, 300)
      ),
      Т(
         FunTimeItemCatalog.сюл,
         Items.SPLASH_POTION,
         Т(MobEffects.WEAKNESS, 2, 1800),
         Т(MobEffects.MINING_FATIGUE, 2, 200),
         Т(MobEffects.WITHER, 3, 1800),
         Т(MobEffects.BLINDNESS, 1, 200)
      ),
      Т(
         FunTimeItemCatalog.сюэ,
         Items.TOTEM_OF_UNDYING,
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сюю, Operation.ADD_VALUE),
         Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сюЩ, Operation.ADD_VALUE),
         Т(Attributes.ARMOR_TOUGHNESS, FunTimeItemCatalog.сюу, Operation.ADD_VALUE),
         Т(Attributes.ARMOR, FunTimeItemCatalog.сю2, Operation.ADD_VALUE)
      ),
      Т(
         FunTimeItemCatalog.сюф,
         Items.TOTEM_OF_UNDYING,
         Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сюи, Operation.ADD_VALUE),
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сю9, Operation.ADD_VALUE),
         Т(Attributes.MOVEMENT_SPEED, FunTimeItemCatalog.сюо, Operation.ADD_MULTIPLIED_BASE),
         Т(Attributes.ATTACK_SPEED, FunTimeItemCatalog.сюO, Operation.ADD_MULTIPLIED_BASE),
         Т(Attributes.ARMOR, FunTimeItemCatalog.сю7, Operation.ADD_VALUE)
      ),
      Т(
         FunTimeItemCatalog.сюР,
         Items.TOTEM_OF_UNDYING,
         Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сю3, Operation.ADD_VALUE),
         Т(Attributes.ARMOR, FunTimeItemCatalog.сюр, Operation.ADD_VALUE),
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сю6, Operation.ADD_VALUE)
      ),
      Т(FunTimeItemCatalog.сю_, Items.TOTEM_OF_UNDYING, Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сюШ, Operation.ADD_VALUE), Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сюы, Operation.ADD_VALUE)),
      Т(
         FunTimeItemCatalog.сюй,
         Items.TOTEM_OF_UNDYING,
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сюЖ, Operation.ADD_VALUE),
         Т(Attributes.MOVEMENT_SPEED, FunTimeItemCatalog.сюм, Operation.ADD_MULTIPLIED_BASE),
         Т(Attributes.ATTACK_SPEED, FunTimeItemCatalog.сюК, Operation.ADD_MULTIPLIED_BASE)
      ),
      Т(FunTimeItemCatalog.сюI, Items.TOTEM_OF_UNDYING, Т(Attributes.ARMOR, FunTimeItemCatalog.сюТ, Operation.ADD_VALUE), Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сюО, Operation.ADD_VALUE)),
      Т(
         FunTimeItemCatalog.сюФ,
         Items.TOTEM_OF_UNDYING,
         Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сюЙ, Operation.ADD_VALUE),
         Т(Attributes.ATTACK_SPEED, FunTimeItemCatalog.сюк, Operation.ADD_MULTIPLIED_BASE)
      ),
      Т(
         FunTimeItemCatalog.сюг,
         Items.TOTEM_OF_UNDYING,
         Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сю0, Operation.ADD_VALUE),
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сюх, Operation.ADD_VALUE),
         Т(Attributes.MOVEMENT_SPEED, FunTimeItemCatalog.сюУ, Operation.ADD_MULTIPLIED_BASE)
      ),
      Т(
         FunTimeItemCatalog.сюЗ,
         Items.PLAYER_HEAD,
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сюЕ, Operation.ADD_VALUE),
         Т(Attributes.ARMOR, FunTimeItemCatalog.сюП, Operation.ADD_VALUE),
         Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сю西, Operation.ADD_VALUE),
         Т(Attributes.MOVEMENT_SPEED, FunTimeItemCatalog.сюд, Operation.ADD_MULTIPLIED_BASE),
         Т(Attributes.ATTACK_SPEED, FunTimeItemCatalog.сюБ, Operation.ADD_MULTIPLIED_BASE),
         Т(Attributes.GRAVITY, FunTimeItemCatalog.сюС, Operation.ADD_MULTIPLIED_BASE)
      ),
      Т(
         FunTimeItemCatalog.сюз,
         Items.PLAYER_HEAD,
         Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сюц, Operation.ADD_VALUE),
         Т(Attributes.JUMP_STRENGTH, FunTimeItemCatalog.сюя, Operation.ADD_MULTIPLIED_BASE),
         Т(Attributes.ATTACK_SPEED, FunTimeItemCatalog.сЩс, Operation.ADD_MULTIPLIED_BASE)
      ),
      Т(
         FunTimeItemCatalog.сЩВ,
         Items.PLAYER_HEAD,
         Т(Attributes.ARMOR, 1.0, Operation.ADD_VALUE),
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сЩЯ, Operation.ADD_VALUE),
         Т(Attributes.MOVEMENT_SPEED, FunTimeItemCatalog.сЩХ, Operation.ADD_MULTIPLIED_BASE),
         Т(Attributes.ATTACK_SPEED, FunTimeItemCatalog.сЩе, Operation.ADD_MULTIPLIED_BASE)
      ),
      Т(
         FunTimeItemCatalog.сЩА,
         Items.PLAYER_HEAD,
         Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сЩ4, Operation.ADD_VALUE),
         Т(Attributes.ARMOR, FunTimeItemCatalog.сЩп, Operation.ADD_VALUE),
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сЩД, Operation.ADD_VALUE)
      ),
      Т(
         FunTimeItemCatalog.сЩЪ,
         Items.PLAYER_HEAD,
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сЩт, Operation.ADD_VALUE),
         Т(Attributes.ARMOR, FunTimeItemCatalog.сЩа, Operation.ADD_VALUE),
         Т(Attributes.SUBMERGED_MINING_SPEED, FunTimeItemCatalog.сЩЮ, Operation.ADD_MULTIPLIED_BASE),
         Т(Attributes.OXYGEN_BONUS, FunTimeItemCatalog.сЩЧ, Operation.ADD_MULTIPLIED_BASE)
      ),
      Т(FunTimeItemCatalog.сЩЛ, Items.PLAYER_HEAD, Т(Attributes.ATTACK_DAMAGE, FunTimeItemCatalog.сЩ诶, Operation.ADD_VALUE), Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сЩ1, Operation.ADD_VALUE)),
      Т(
         FunTimeItemCatalog.сЩЬ,
         Items.PLAYER_HEAD,
         Т(Attributes.LUCK, 1.0, Operation.ADD_VALUE),
         Т(Attributes.MAX_HEALTH, FunTimeItemCatalog.сЩ衣, Operation.ADD_VALUE),
         Т(Attributes.BLOCK_INTERACTION_RANGE, 1.0, Operation.ADD_VALUE)
      ),
      Т(
         FunTimeItemCatalog.сЩш,
         Items.PLAYER_HEAD,
         Т(Attributes.ARMOR, FunTimeItemCatalog.сЩИ, Operation.ADD_VALUE),
         Т(Attributes.ARMOR_TOUGHNESS, FunTimeItemCatalog.сЩь, Operation.ADD_VALUE),
         Т(Attributes.MOVEMENT_SPEED, FunTimeItemCatalog.сЩж, Operation.ADD_MULTIPLIED_BASE)
      ),
      Т(FunTimeItemCatalog.сЩ弟, Items.NETHERITE_SCRAP, Т(FunTimeItemCatalog.сЩб)),
      Т(FunTimeItemCatalog.сЩЫ, Items.SUGAR, Т(FunTimeItemCatalog.сЩl)),
      Т(FunTimeItemCatalog.сЩ8, Items.SNOWBALL, Т(FunTimeItemCatalog.сЩ5)),
      Т(FunTimeItemCatalog.сЩв, Items.PHANTOM_MEMBRANE, Т(FunTimeItemCatalog.сЩъ)),
      Т(FunTimeItemCatalog.сЩМ, Items.ENDER_EYE, Т(FunTimeItemCatalog.сЩН)),
      Т(FunTimeItemCatalog.сЩн, Items.DRIED_KELP, Т(FunTimeItemCatalog.сЩЦ)),
      Т(
         FunTimeItemCatalog.сЩч,
         Items.NETHERITE_SWORD,
         List.of(),
         List.of(Б(Enchantments.KNOCKBACK, 0)),
         List.of(Т(FunTimeItemCatalog.сЩЭ), Т(FunTimeItemCatalog.сЩ必, 3), Т(FunTimeItemCatalog.сЩщ, 2), Т(FunTimeItemCatalog.сЩГ, 2), Т(FunTimeItemCatalog.сЩл, 3), Т(FunTimeItemCatalog.сЩэ, 3)),
         List.of(),
         List.of()
      ),
      Т(FunTimeItemCatalog.сЩю, Items.MACE, Т(FunTimeItemCatalog.сЩЩ), Т(FunTimeItemCatalog.сЩу, 3), Т(FunTimeItemCatalog.сЩ2, 2), Т(FunTimeItemCatalog.сЩф, 2), Т(FunTimeItemCatalog.сЩи, 3), Т(FunTimeItemCatalog.сЩ9, 3)),
      Т(FunTimeItemCatalog.сЩо, Items.TRIDENT, Т(FunTimeItemCatalog.сЩO), Т(FunTimeItemCatalog.сЩ7, 3), Т(FunTimeItemCatalog.сЩР, 3), Т(FunTimeItemCatalog.сЩ3, 2), Т(FunTimeItemCatalog.сЩр), Т(FunTimeItemCatalog.сЩ6)),
      Т(FunTimeItemCatalog.сЩ_, Items.NETHERITE_PICKAXE, Т(FunTimeItemCatalog.сЩШ), Т(FunTimeItemCatalog.сЩы, 2), Т(FunTimeItemCatalog.сЩй, 3), Т(FunTimeItemCatalog.сЩЖ), Т(FunTimeItemCatalog.сЩм), Т(FunTimeItemCatalog.сЩК), Т(FunTimeItemCatalog.сЩI)),
      Т(FunTimeItemCatalog.сЩТ, Items.CROSSBOW, Т(FunTimeItemCatalog.сЩО)),
      Т(
         FunTimeItemCatalog.сЩФ,
         Items.NETHERITE_HELMET,
         Т(Enchantments.PROTECTION, 5),
         Т(Enchantments.FIRE_PROTECTION, 5),
         Т(Enchantments.BLAST_PROTECTION, 5),
         Т(Enchantments.PROJECTILE_PROTECTION, 5),
         Т(Enchantments.UNBREAKING, 5),
         Т(Enchantments.MENDING, 1),
         Т(Enchantments.AQUA_AFFINITY, 1),
         Т(Enchantments.RESPIRATION, 3),
         Б(Enchantments.THORNS, 0)
      ),
      Т(
         FunTimeItemCatalog.сЩЙ,
         Items.NETHERITE_CHESTPLATE,
         Т(Enchantments.PROTECTION, 5),
         Т(Enchantments.FIRE_PROTECTION, 5),
         Т(Enchantments.BLAST_PROTECTION, 5),
         Т(Enchantments.PROJECTILE_PROTECTION, 5),
         Т(Enchantments.UNBREAKING, 5),
         Т(Enchantments.MENDING, 1),
         Б(Enchantments.THORNS, 0)
      ),
      Т(
         FunTimeItemCatalog.сЩк,
         Items.NETHERITE_LEGGINGS,
         Т(Enchantments.PROTECTION, 5),
         Т(Enchantments.FIRE_PROTECTION, 5),
         Т(Enchantments.BLAST_PROTECTION, 5),
         Т(Enchantments.PROJECTILE_PROTECTION, 5),
         Т(Enchantments.UNBREAKING, 5),
         Т(Enchantments.MENDING, 1),
         Б(Enchantments.THORNS, 0)
      ),
      Т(
         FunTimeItemCatalog.сЩг,
         Items.NETHERITE_BOOTS,
         Т(Enchantments.PROTECTION, 5),
         Т(Enchantments.FIRE_PROTECTION, 5),
         Т(Enchantments.BLAST_PROTECTION, 5),
         Т(Enchantments.PROJECTILE_PROTECTION, 5),
         Т(Enchantments.UNBREAKING, 5),
         Т(Enchantments.MENDING, 1),
         Т(Enchantments.FEATHER_FALLING, 4),
         Т(Enchantments.SOUL_SPEED, 3),
         Т(Enchantments.DEPTH_STRIDER, 3),
         Б(Enchantments.THORNS, 0)
      ),
      Т(FunTimeItemCatalog.сЩ0, Items.TRIPWIRE_HOOK, Т(FunTimeItemCatalog.сЩх)),
      Т(FunTimeItemCatalog.сЩУ, Items.GOLDEN_PICKAXE, Т(FunTimeItemCatalog.сЩЗ)),
      Т(FunTimeItemCatalog.сЩЕ, Items.GOLDEN_PICKAXE, Т(FunTimeItemCatalog.сЩП)),
      Т(FunTimeItemCatalog.сЩ西, Items.CAMPFIRE, Т(FunTimeItemCatalog.сЩд)),
      Т(FunTimeItemCatalog.сЩБ, Items.CAMPFIRE, Т(FunTimeItemCatalog.сЩС)),
      Т(FunTimeItemCatalog.сЩз, Items.SOUL_CAMPFIRE, Т(FunTimeItemCatalog.сЩц)),
      Т(FunTimeItemCatalog.сЩя, Items.PAPER, Т(FunTimeItemCatalog.сус)),
      Т(FunTimeItemCatalog.суВ, Items.PAPER, Т(FunTimeItemCatalog.суЯ)),
      Т(FunTimeItemCatalog.суХ, Items.STRUCTURE_BLOCK, Т(FunTimeItemCatalog.суе)),
      Т(FunTimeItemCatalog.суА, Items.STRUCTURE_BLOCK, Т(FunTimeItemCatalog.су4)),
      Т(FunTimeItemCatalog.суп, Items.STRUCTURE_BLOCK, Т(FunTimeItemCatalog.суД)),
      Т(FunTimeItemCatalog.суЪ, Items.CHAIN_COMMAND_BLOCK, Т(FunTimeItemCatalog.сут)),
      Т(FunTimeItemCatalog.суа, Items.REDSTONE_TORCH, Т(FunTimeItemCatalog.суЮ)),
      Т(FunTimeItemCatalog.суЧ, Items.JIGSAW, Т(FunTimeItemCatalog.суЛ)),
      Т(FunTimeItemCatalog.су诶, Items.FEATHER, Т(FunTimeItemCatalog.су1)),
      Т(FunTimeItemCatalog.суЬ, Items.AMETHYST_SHARD, Т(FunTimeItemCatalog.су衣)),
      Т(FunTimeItemCatalog.суш, Items.TRIAL_KEY, Т(FunTimeItemCatalog.суИ)),
      Т(FunTimeItemCatalog.суь, Items.TRIAL_KEY, Т(FunTimeItemCatalog.суж)),
      Т(FunTimeItemCatalog.су弟, Items.TNT, Т(FunTimeItemCatalog.суб), Б(FunTimeItemCatalog.суЫ)),
      Т(FunTimeItemCatalog.суl, Items.TNT, Т(FunTimeItemCatalog.су8)),
      Т(
         FunTimeItemCatalog.су5,
         Items.TIPPED_ARROW,
         Т(MobEffects.WEAKNESS, 3, 60),
         Т(MobEffects.BLINDNESS, 1, 40),
         Т(MobEffects.MINING_FATIGUE, 1, 40),
         Т(MobEffects.NAUSEA, 1, 100)
      ),
      Т(FunTimeItemCatalog.сув, Items.TIPPED_ARROW, Т(MobEffects.SLOWNESS, 10, 100), Т(MobEffects.MINING_FATIGUE, 3, 40)),
      Т(FunTimeItemCatalog.суъ, Items.TIPPED_ARROW, Т(MobEffects.SLOWNESS, 3, 100), Т(MobEffects.WITHER, 3, 100), Т(MobEffects.POISON, 3, 100)),
      Т(FunTimeItemCatalog.суМ, Items.GOLDEN_APPLE),
      Т(FunTimeItemCatalog.суН, Items.ENCHANTED_GOLDEN_APPLE),
      Т(FunTimeItemCatalog.сун, Items.SPAWNER),
      Т(FunTimeItemCatalog.суЦ, Items.EMERALD_ORE),
      Т(FunTimeItemCatalog.суч, Items.DRAGON_HEAD),
      Т(FunTimeItemCatalog.суЭ, Items.DRAGON_EGG),
      Т(FunTimeItemCatalog.су必, Items.ELYTRA),
      Т(FunTimeItemCatalog.сущ, Items.BEACON),
      Т(FunTimeItemCatalog.суГ, Items.VILLAGER_SPAWN_EGG),
      Т(FunTimeItemCatalog.сул, Items.ENDERMAN_SPAWN_EGG),
      Т(FunTimeItemCatalog.суэ, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
      Т(FunTimeItemCatalog.сую, Items.DIAMOND),
      Т(FunTimeItemCatalog.суЩ, Items.NETHER_STAR),
      Т(FunTimeItemCatalog.суу, Items.ANCIENT_DEBRIS),
      Т(FunTimeItemCatalog.су2, Items.NETHERITE_INGOT)
   );
   private static final String сюЧ = "§.";
   private static final String сюЛ = "\\s+";
   private static final String сю诶 = "\\[\\d+x\\d+]";
   private static final String сю1 = "⚡";
   private static final String сюЬ = "xxx";
   private static final String сю衣 = "[";
   private static final String сюш = "]";
   private static final String сюИ = "★";
   private static final String сюь = "§.";
   private static final String сюж = "\\s+";
   private static final String сю弟 = "i";
   private static final String сюб = "ii";
   private static final String сюЫ = "iii";
   private static final String сюl = "iv";
   private static final String сю8 = "v";
   private static final String сю5 = "vi";
   private static final String сюв = "vii";
   private static final String сюъ = "viii";
   private static final String сюМ = "ix";
   private static final String сюН = "x";
   private static final String сюн = "[^0-9]";
   private static final String сюЦ = "[★] Хлопушка";
   private static final String сюч = "[★] Святая вода";
   private static final String сюЭ = "[★] Зелье Гнева";
   private static final String сю必 = "[★] Зелье Палладина";
   private static final String сющ = "[★] Зелье Ассасина";
   private static final String сюГ = "[★] Зелье Радиации";
   private static final String сюл = "[★] Снотворное";
   private static final String сюэ = "[★] Талисман Крушителя";
   private static final double сюю = 4.0;
   private static final double сюЩ = 3.0;
   private static final double сюу = 2.0;
   private static final double сю2 = 2.0;
   private static final String сюф = "[★] Талисман Раздора";
   private static final double сюи = 4.0;
   private static final double сю9 = 2.0;
   private static final double сюо = 0.10000001817743467;
   private static final double сюO = 0.10000001817743467;
   private static final double сю7 = -3.0;
   private static final String сюР = "[★] Талисман Тирана";
   private static final double сю3 = 2.0;
   private static final double сюр = 2.0;
   private static final double сю6 = -4.0;
   private static final String сю_ = "[★] Талисман Ярости";
   private static final double сюШ = 5.0;
   private static final double сюы = -4.0;
   private static final String сюй = "[★] Талисман Вихря";
   private static final double сюЖ = 2.0;
   private static final double сюм = 0.1500000347540806;
   private static final double сюК = 0.1500000347540806;
   private static final String сюI = "[★] Талисман Мрака";
   private static final double сюТ = 1.5;
   private static final double сюО = 1.5;
   private static final String сюФ = "[★] Талисман Демона";
   private static final double сюЙ = 2.5;
   private static final double сюк = 0.10000001817743467;
   private static final String сюг = "[★] Талисман Карателя xxx";
   private static final double сю0 = 7.0;
   private static final double сюх = -4.0;
   private static final double сюУ = 0.10000001817743467;
   private static final String сюЗ = "[★] Сфера Хаоса";
   private static final double сюЕ = -4.0;
   private static final double сюП = 1.5;
   private static final double сю西 = 2.5;
   private static final double сюд = 0.07000004566537438;
   private static final double сюБ = 0.13000000024761527;
   private static final double сюС = 0.09000001839414021;
   private static final String сюз = "[★] Сфера Сатира";
   private static final double сюц = 2.0;
   private static final double сюя = -0.10000000768432185;
   private static final double сЩс = 0.1500000347540806;
   private static final String сЩВ = "[★] Сфера Бестии";
   private static final double сЩЯ = 4.0;
   private static final double сЩХ = 0.10000001817743467;
   private static final double сЩе = 0.10000001817743467;
   private static final String сЩА = "[★] Сфера Ареса";
   private static final double сЩ4 = 6.0;
   private static final double сЩп = -2.0;
   private static final double сЩД = -2.0;
   private static final String сЩЪ = "[★] Сфера Гидры";
   private static final double сЩт = 4.0;
   private static final double сЩа = 2.0;
   private static final double сЩЮ = 0.5;
   private static final double сЩЧ = 0.5;
   private static final String сЩЛ = "[★] Сфера Икара";
   private static final double сЩ诶 = 2.0;
   private static final double сЩ1 = 2.0;
   private static final String сЩЬ = "[★] Сфера Эрида";
   private static final double сЩ衣 = 2.0;
   private static final String сЩш = "[★] Сфера Титана";
   private static final double сЩИ = 2.5;
   private static final double сЩь = 2.5;
   private static final double сЩж = -0.15000007945819008;
   private static final String сЩ弟 = "[★] Трапка";
   private static final String сЩб = "Каст: Нерушимая клетка";
   private static final String сЩЫ = "[★] Явная пыль";
   private static final String сЩl = "Каст: Световая вспышка";
   private static final String сЩ8 = "[★] Снежок заморозка";
   private static final String сЩ5 = "Каст: Ледяная сфера";
   private static final String сЩв = "[★] Божья аура";
   private static final String сЩъ = "Каст: Божественная аура";
   private static final String сЩМ = "[★] Дезориентация";
   private static final String сЩН = "Каст: Звуковая волна";
   private static final String сЩн = "[★] Пласт";
   private static final String сЩЦ = "Каст: Нерушимая стена";
   private static final String сЩч = "xxx Меч Крушителя xxx";
   private static final String сЩЭ = "[★] Оригинальный предмет";
   private static final String сЩ必 = "Опытный";
   private static final String сЩщ = "Вампиризм";
   private static final String сЩГ = "Окисление";
   private static final String сЩл = "Яд";
   private static final String сЩэ = "Детекция";
   private static final String сЩю = "xxx Булава Крушителя xxx";
   private static final String сЩЩ = "[★] Оригинальный предмет";
   private static final String сЩу = "Опытный";
   private static final String сЩ2 = "Вампиризм";
   private static final String сЩф = "Окисление";
   private static final String сЩи = "Яд";
   private static final String сЩ9 = "Детекция";
   private static final String сЩо = "xxx Трезубец Крушителя xxx";
   private static final String сЩO = "[★] Оригинальный предмет";
   private static final String сЩ7 = "Скаут";
   private static final String сЩР = "Ступор";
   private static final String сЩ3 = "Притяжение";
   private static final String сЩр = "Возвращение";
   private static final String сЩ6 = "Подрывник";
   private static final String сЩ_ = "xxx Кирка Крушителя xxx";
   private static final String сЩШ = "[★] Оригинальный предмет";
   private static final String сЩы = "Бульдозер";
   private static final String сЩй = "Опытный";
   private static final String сЩЖ = "Магнит";
   private static final String сЩм = "Авто-Плавка";
   private static final String сЩК = "Паутина";
   private static final String сЩI = "Пингер";
   private static final String сЩТ = "xxx Арбалет Крушителя xxx";
   private static final String сЩО = "[★] Оригинальный предмет";
   private static final String сЩФ = "xxx Шлем Крушителя xxx";
   private static final String сЩЙ = "xxx Нагрудник Крушителя xxx";
   private static final String сЩк = "xxx Поножи Крушителя xxx";
   private static final String сЩг = "xxx Ботинки Крушителя xxx";
   private static final String сЩ0 = "[★] Отмычка к Сферам";
   private static final String сЩх = "С Сферами";
   private static final String сЩУ = "[★] Божье касание";
   private static final String сЩЗ = "Может добыть спавнер";
   private static final String сЩЕ = "[★] Мощный удар";
   private static final String сЩП = "Может разрушить бедрок";
   private static final String сЩ西 = "Обычный мист";
   private static final String сЩд = "Уровень лута: Обычный";
   private static final String сЩБ = "Богатый мист";
   private static final String сЩС = "Уровень лута: Богатый";
   private static final String сЩз = "Легендарный мист";
   private static final String сЩц = "Уровень лута: Легендарный";
   private static final String сЩя = "[★] Неизбежный скин";
   private static final String сус = "получаете Неизбежный скин";
   private static final String суВ = "[★] Драконий скин";
   private static final String суЯ = "получаете Драконий скин";
   private static final String суХ = "[★] Прогрузчик чанков [1x1]";
   private static final String суе = "прогружаемой области (1x1)";
   private static final String суА = "[★] Прогрузчик чанков [3x3]";
   private static final String су4 = "прогружаемой области (3x3)";
   private static final String суп = "[★] Прогрузчик чанков [5x5]";
   private static final String суД = "прогружаемой области (5x5)";
   private static final String суЪ = "[★] Регион 25x25";
   private static final String сут = "Размер: 25x25 блоков";
   private static final String суа = "[★] Аирдроп";
   private static final String суЮ = "призыва Аирдропа";
   private static final String суЧ = "[★] Блок дамагер";
   private static final String суЛ = "Каст: Нанесение урона";
   private static final String су诶 = "[⚡] Модификатор полёта";
   private static final String су1 = "Доступ к /fly";
   private static final String суЬ = "[⚡] Модификатор починки";
   private static final String су衣 = "Доступ к /fix";
   private static final String суш = "[★] Ключ от кейса с Привилегиями";
   private static final String суИ = "Открывает: Кейс с Привилегиями";
   private static final String суь = "[★] Ключ от кейса с Токенами";
   private static final String суж = "Открывает: Кейс с Токенами";
   private static final String су弟 = "[★] Вайт";
   private static final String суб = "в 10 раз сильнее обычного";
   private static final String суЫ = "способен взорвать обсидиан";
   private static final String суl = "[★] Блэк";
   private static final String су8 = "способен взорвать обсидиан";
   private static final String су5 = "Кровавая стрела";
   private static final String сув = "Стрела обледенения";
   private static final String суъ = "Мучительная стрела";
   private static final String суМ = "Золотое яблоко";
   private static final String суН = "Чарка";
   private static final String сун = "Спавнер";
   private static final String суЦ = "Изумрудная руда";
   private static final String суч = "Голова дракона";
   private static final String суЭ = "Яйцо дракона";
   private static final String су必 = "Элитры";
   private static final String сущ = "Маяк";
   private static final String суГ = "Яйцо призыва крестьянина";
   private static final String сул = "Яйцо призыва эндермена";
   private static final String суэ = "Отделка незеритовая";
   private static final String сую = "Алмаз";
   private static final String суЩ = "Звезда Незера";
   private static final String суу = "Древние обломки";
   private static final String су2 = "Незеритовый слиток";

   private static FunTimeItemCatalog.EnchantmentRequirement Б(ResourceKey<Enchantment> var0, int var1) {
      return new FunTimeItemCatalog.EnchantmentRequirement(var0, var1, true);
   }

   private static FunTimeItemCatalog.EnchantmentRequirement Т(ResourceKey<Enchantment> var0, int var1) {
      return new FunTimeItemCatalog.EnchantmentRequirement(var0, var1, false);
   }

   private static FunTimeItemCatalog.TooltipRequirement Б(String var0) {
      return new FunTimeItemCatalog.TooltipRequirement(var0, 0, true);
   }

   private static FunTimeItemCatalog.CatalogEntry Т(String var0, Item var1, FunTimeItemCatalog.AttributeRequirement... var2) {
      return Т(var0, var1, List.of(var2), List.of(), List.of(), List.of(), List.of());
   }

   private static String з(String var0) {
      return var0.replaceAll(сю诶, "")
         .replace(сю1, "")
         .replace(сюЬ, "")
         .replace(сю衣, "")
         .replace(сюш, "")
         .replace(сюИ, "")
         .replaceAll(сюь, "")
         .toLowerCase()
         .replaceAll(сюж, "")
         .trim();
   }

   private static FunTimeItemCatalog.CatalogEntry Т(String var0, Item var1) {
      return Т(var0, var1, List.of(), List.of(), List.of(), List.of(), List.of());
   }

   private static FunTimeItemCatalog.CatalogEntry Т(String var0, Item var1, FunTimeItemCatalog.PotionRequirement... var2) {
      return Т(var0, var1, List.of(), List.of(), List.of(), List.of(), List.of(var2));
   }

   private FunTimeItemCatalog() {
   }

   private static FunTimeItemCatalog.AttributeRequirement Т(Holder<Attribute> var0, double var1, Operation var3) {
      return new FunTimeItemCatalog.AttributeRequirement(var0, var1, var3);
   }

   private static String к(String var0) {
      return var0.replaceAll(сюЧ, "").toLowerCase().replaceAll(сюЛ, "").trim();
   }

   private static FunTimeItemCatalog.PotionRequirement Т(Holder<MobEffect> var0, int var1, int var2) {
      return new FunTimeItemCatalog.PotionRequirement(var0, var1, var2);
   }

   public static FunTimeItemCatalog.CatalogEntry Т(ItemStack var0) {
      for (FunTimeItemCatalog.CatalogEntry var2 : с0) {
         if (var2.matches(var0)) {
            return var2;
         }
      }

      if (var0.getCustomName() == null) {
         return null;
      }

      String var4 = з(var0.getCustomName().getString());

      for (FunTimeItemCatalog.CatalogEntry var3 : с0) {
         if (var0.is(var3.item()) && з(var3.name()).equals(var4)) {
            return var3;
         }
      }

      return null;
   }

   private static FunTimeItemCatalog.TooltipRequirement Т(String var0, int var1) {
      return new FunTimeItemCatalog.TooltipRequirement(var0, var1, false);
   }

   private static FunTimeItemCatalog.CatalogEntry Т(String var0, Item var1, FunTimeItemCatalog.TooltipRequirement... var2) {
      return Т(var0, var1, List.of(), List.of(), List.of(var2), List.of(), List.of());
   }

   private static int Н(String var0) {
      if (var0 != null && !var0.isBlank()) {
         String var1 = к(var0);
         String[] var2 = new String[]{сю弟, сюб, сюЫ, сюl, сю8, сю5, сюв, сюъ, сюМ, сюН};

         for (int var3 = var2.length - 1; var3 >= 0; var3--) {
            if (var1.contains(var2[var3])) {
               return var3 + 1;
            }
         }

         String var5 = var1.replaceAll(сюн, "");
         if (!var5.isEmpty()) {
            int var4 = Integer.parseInt(var5);
            if (var4 >= 1 && var4 <= 10) {
               return var4;
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   private static FunTimeItemCatalog.TooltipRequirement Т(String var0) {
      return new FunTimeItemCatalog.TooltipRequirement(var0, 0, false);
   }

   private static FunTimeItemCatalog.CatalogEntry Т(String var0, Item var1, FunTimeItemCatalog.EnchantmentRequirement... var2) {
      return Т(var0, var1, List.of(), List.of(var2), List.of(), List.of(), List.of());
   }

   private static FunTimeItemCatalog.CatalogEntry Т(
      String var0,
      Item var1,
      List<FunTimeItemCatalog.AttributeRequirement> var2,
      List<FunTimeItemCatalog.EnchantmentRequirement> var3,
      List<FunTimeItemCatalog.TooltipRequirement> var4,
      List<FunTimeItemCatalog.NbtRequirement> var5,
      List<FunTimeItemCatalog.PotionRequirement> var6
   ) {
      return new FunTimeItemCatalog.CatalogEntry(var0, var1, var2, var3, var4, var5, var6);
   }

   private record NbtRequirement(String value) {
      boolean matches(ItemStack var1) {
         if (MinecraftAccess.MINECRAFT_CLIENT.level == null) {
            return false;
         }

         Tag var2 = ItemStack.CODEC
            .encodeStart(MinecraftAccess.MINECRAFT_CLIENT.level.registryAccess().createSerializationContext(NbtOps.INSTANCE), var1)
            .result()
            .orElse(null);
         return var2 != null && FunTimeItemCatalog.к(var2.toString()).contains(FunTimeItemCatalog.к(this.value));
      }
   }

   public record CatalogEntry(
      String name,
      Item item,
      List<FunTimeItemCatalog.AttributeRequirement> attributes,
      List<FunTimeItemCatalog.EnchantmentRequirement> enchantments,
      List<FunTimeItemCatalog.TooltipRequirement> descriptions,
      List<FunTimeItemCatalog.NbtRequirement> nbt,
      List<FunTimeItemCatalog.PotionRequirement> potions
   ) {
      public ItemStack stack() {
         ItemStack var1;
         try {
            var1 = new ItemStack(this.item);
         } catch (RuntimeException var3) {
            // Item holders are not bound outside a loaded level, e.g. when the panel is previewed from the main menu.
            return ItemStack.EMPTY;
         }

         if (this.potions.isEmpty()) {
            return var1;
         }

         Optional var2 = switch (this.name) {
            case "[★] Хлопушка" -> Optional.of(16711680);
            case "[★] Святая вода" -> Optional.of(16777215);
            case "[★] Зелье Гнева" -> Optional.of(10040115);
            case "[★] Зелье Палладина" -> Optional.of(65535);
            case "[★] Зелье Ассасина" -> Optional.of(3355443);
            case "[★] Зелье Радиации" -> Optional.of(3329330);
            case "[★] Снотворное" -> Optional.of(4737096);
            default -> Optional.empty();
         };
         List var5 = this.potions.stream().map(var0 -> new MobEffectInstance(var0.effect(), var0.duration(), var0.level() - 1)).toList();
         var1.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), var2, var5, Optional.empty()));
         return var1;
      }

      public boolean matches(ItemStack var1) {
         if (!var1.is(this.item)) {
            return false;
         }

         for (FunTimeItemCatalog.AttributeRequirement var3 : this.attributes) {
            if (!var3.matches(var1)) {
               return false;
            }
         }

         for (FunTimeItemCatalog.EnchantmentRequirement var8 : this.enchantments) {
            if (!var8.matches(var1)) {
               return false;
            }
         }

         for (FunTimeItemCatalog.TooltipRequirement var9 : this.descriptions) {
            if (!var9.matches(var1)) {
               return false;
            }
         }

         for (FunTimeItemCatalog.NbtRequirement var10 : this.nbt) {
            if (!var10.matches(var1)) {
               return false;
            }
         }

         for (FunTimeItemCatalog.PotionRequirement var11 : this.potions) {
            if (!var11.matches(var1)) {
               return false;
            }
         }

         return true;
      }
   }

   private record EnchantmentRequirement(ResourceKey<Enchantment> enchantment, int level, boolean denied) {
      boolean matches(ItemStack var1) {
         Reference var2 = MinecraftAccess.MINECRAFT_CLIENT.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(this.enchantment);
         ItemEnchantments var3 = var1.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
         int var4 = var3.getLevel(var2);
         int var5 = this.level == 0 ? 1 : this.level;
         return this.denied ? var4 < var5 : !var3.isEmpty() && var4 >= var5;
      }
   }

   private record PotionRequirement(Holder<MobEffect> effect, int level, int duration) {
      boolean matches(ItemStack var1) {
         PotionContents var2 = var1.get(DataComponents.POTION_CONTENTS);
         return var2 == null
            ? false
            : StreamSupport.stream(var2.getAllEffects().spliterator(), false)
               .anyMatch(var1x -> var1x.getEffect().equals(this.effect) && var1x.getAmplifier() + 1 == this.level && var1x.getDuration() >= this.duration);
      }
   }

   private record TooltipRequirement(String description, int level, boolean denied) {
      boolean matches(ItemStack var1) {
         String var2 = var1.getTooltipLines(TooltipContext.EMPTY, MinecraftAccess.MINECRAFT_CLIENT.player, TooltipFlag.NORMAL)
            .stream()
            .skip(1L)
            .map(var0 -> FunTimeItemCatalog.к(var0.getString()))
            .collect(Collectors.joining(""));
         String var3 = FunTimeItemCatalog.к(this.description);
         if (this.denied) {
            return !var2.contains(var3);
         }

         if (!var2.contains(var3)) {
            return false;
         }

         if (this.level == 0) {
            return true;
         }

         String var4 = var2.substring(var2.indexOf(var3) + var3.length()).trim();
         int var5 = var4.indexOf(32);
         int var6 = var4.isEmpty() ? 0 : FunTimeItemCatalog.Н(var5 > 0 ? var4.substring(0, var5) : var4);
         return Math.max(1, var6) >= this.level;
      }
   }

   private record AttributeRequirement(Holder<Attribute> attribute, double amount, Operation operation) {
      boolean matches(ItemStack var1) {
         ItemAttributeModifiers var2 = var1.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

         for (Entry var4 : var2.modifiers()) {
            if (var4.attribute().equals(this.attribute)
               && Math.abs(var4.modifier().amount() - this.amount) < 9.9999942618434E-4
               && var4.modifier().operation() == this.operation) {
               return true;
            }
         }

         return false;
      }
   }
}
