package sg.network;

import com.google.common.collect.ArrayListMultimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.phys.Vec3;

public final class FunTimeItemMetadata {
   private static final Pattern ж = Pattern.compile(FunTimeItemMetadata.ВмЙ);
   private static final Pattern 弟 = Pattern.compile(FunTimeItemMetadata.Вмк);
   private static final Pattern б = Pattern.compile(FunTimeItemMetadata.Вмг);
   private static final Map<String, String> В衣 = new HashMap<>();
   private static final Map<String, ItemStack> Вш = new HashMap<>();
   private static final Map<String, Item> ВИ = new HashMap<>();
   private static final Map<String, FunTimeItemMetadata.EnchantedItemSpec> Вь = new HashMap<>();
   private static final Map<String, FunTimeItemMetadata.AttributeItemSpec> Вж = new HashMap<>();
   private static final String Вмп = "$";
   private static final String ВмД = "Цена";
   private static final String ВмЪ = "[^0-9]";
   private static final String Вмт = "млн";
   private static final String Вма = "m";
   private static final String ВмЮ = "м";
   private static final long ВмЧ = 1000000L;
   private static final String ВмЛ = "тыс";
   private static final String Вм诶 = "k";
   private static final String Вм1 = "к";
   private static final long ВмЬ = 1000L;
   private static final String Вм衣 = "[^0-9]";
   private static final double Вмш = 100.0;
   private static final long ВмИ = -1L;
   private static final String Вмь = "Вы успешно купили ";
   private static final String Вмж = " за ";
   private static final long Вм弟 = -1L;
   private static final String Вмб = "[^\\d]";
   private static final long ВмЫ = -1L;
   private static final long Вмl = -1L;
   private static final long Вм8 = -1L;
   private static final long Вм5 = -1L;
   private static final long Вмв = -1L;
   private static final String Вмъ = "(?i)§.";
   private static final String ВмМ = "(?i)&.";
   private static final String ВмН = "\\[[^\\]]*]";
   private static final String Вмн = " ";
   private static final String ВмЦ = "[★✦✧✪✫✬✭✮✯✰❄☃⚒☠❤❣♕♛♜♞♟]";
   private static final String Вмч = " ";
   private static final String ВмЭ = "xxx";
   private static final String Вм必 = " ";
   private static final String Вмщ = "\\s+";
   private static final String ВмГ = " ";
   private static final String Вмл = "textures";
   private static final String Вмэ = "textures";
   private static final String Вмю = "FunTimeHead";
   private static final double ВмЩ = 1.0E-4;
   private static final String Вму = "minecraft:block.anvil.place";
   private static final float Вм2 = 0.5F;
   private static final float Вмф = 0.7F;
   private static final float Вми = 1.1F;
   private static final float Вм9 = 0.5F;
   private static final long Вмо = 20000L;
   private static final String ВмO = "minecraft:entity.wither.break_block";
   private static final String Вм7 = "minecraft:block.anvil.place";
   private static final float ВмР = 0.7F;
   private static final float Вм3 = 0.2F;
   private static final long Вмр = 15000L;
   private static final String Вм6 = "(?i)§.";
   private static final int Вм_ = 65306;
   private static final String ВмШ = "\\s*:\\s*";
   private static final String Вмы = ":";
   private static final String Вмй = "аукцион";
   private static final String ВмЖ = "auction";
   private static final String Вмм = "поиск:";
   private static final String ВмК = "search:";
   private static final String ВмI = "п:";
   private static final String ВмТ = "漢:";
   private static final String ВмО = "\ud83d\udd0e:";
   private static final String ВмФ = "\ud83d\udd0d:";
   private static final String ВмЙ = "Продавец:\\s*(.+)";
   private static final String Вмк = "(?i)§[0-9A-FK-OR]";
   private static final String Вмг = "(?iu)подождите\\s+(\\d+)\\s*сек";
   private static final String Вм0 = "Сфера Хаоса";
   private static final String Вмх = "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODY0MTkwMCwKICAicHJvZmlsZUlkIiA6ICIxNzRjZmRiNGEzY2I0M2I1YmZjZGU0MjRjM2JiMmM2ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXJhZWwxOCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lN2E3YWU3Y2RjZjYxNmU4YjdhNDIyMWE2MjFiMjQzNTc1M2M2MGVkNmEyNThlYTA2MGRhZTMwMDJmZmU5ZTI4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
   private static final String ВмУ = "Сфера Титана";
   private static final String ВмЗ = "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM1NDQ1NTE5MiwKICAicHJvZmlsZUlkIiA6ICJkOTcwYzEzZTM4YWI0NzlhOTY1OGM1ZDQ1MjZkMTM0YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmltcHlMYWNlODUxMjciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFlOTY5ODQ1OGI3ODQxYzk2YWU0ZjI0ZWM4NGFlMDE3MjQxMDA2NDFjNTY0ZTJhN2IxODVmNDA2ZThlZDIzIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
   private static final String ВмЕ = "Сфера Ареса";
   private static final String ВмП = "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzc3NDI1NSwKICAicHJvZmlsZUlkIiA6ICJhZWNkODIxZTQyYzE0ZDJlOThmNTA1OTg1MWI5OWMzNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJqdXNhbXUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzE2YWRjNmJhZmNiNTdmZDcwN2RlZTdkZDZhNzM2ZmUxMjY3MTFkNTNhMWZkNmNlNzg5ZGE0MWIzYmUxM2YyYSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
   private static final String Вм西 = "Сфера Бестии";
   private static final String Вмд = "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0MzgzNDkzMCwKICAicHJvZmlsZUlkIiA6ICI1MzUzNWIxN2M0ZDY0NWQ0YWUwY2U2ZjM4Zjk0NTFjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVYml2aXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQxMWFjMTczODFiOWZjZTliYWIzYzcyYWZkYjdmMTk4NTcwZGFmNDczMmJkODExZDMxYzIyN2Q4MGZhMzliMSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
   private static final String ВмБ = "Сфера Гидры";
   private static final String ВмС = "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODUzMjE4MywKICAicHJvZmlsZUlkIiA6ICI1OGZmZWI5NTMxNGQ0ODcwYTQwYjVjYjQyZDRlYTU5OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa2luREJuZXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UzYzExOGQ2OTZkOTEwZTU0ZGUwMmNhNGQ4MDc1NDNmOWIxOGMwMDhjOTgzOGQyZmY2OTM3NzYyMmZiMWQzMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
   private static final String Вмз = "Сфера Икара";
   private static final String Вмц = "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODU4MjQ5MSwKICAicHJvZmlsZUlkIiA6ICJhZWNkODIxZTQyYzE0ZDJlOThmNTA1OTg1MWI5OWMzNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJSb2RyaVgyMDc1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M2ODAzZTZkNTY2N2EyZDYxMDYyOGJjM2IzMmY4NjNjZGE0OTVjNDY1NjE2ZGU2NTVjYjMyOTkzM2I2MWFmNzciLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
   private static final String Вмя = "Сфера Эрида";
   private static final String ВКс = "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzg2MTE4NywKICAicHJvZmlsZUlkIiA6ICJlZGUyYzdhMGFjNjM0MTNiYjA5ZDNmMGJlZTllYzhlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0aGVEZXZKYWRlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZlNGUyZjEwNDdmM2VjNmU5ZTQ1OTE4NDczOWUzM2I3YzFmYzYzYWQ4MjAyYmRhYjlmMDI0NTA4YWRkMjNlNWIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
   private static final String ВКВ = "Сфера Сатира";
   private static final String ВКЯ = "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODYwODUyOCwKICAicHJvZmlsZUlkIiA6ICJkMTQ4NjFiM2UwZmM0Njk5OTFlMTcyNTllMzdiZjZhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJyYXhpdG9jbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NzFhOWE0OThiNGZhNWVjNDkzNjJmOWJjODhlZGE0ZjUyYjA0ZGU0OWQ3NWFhM2NhMzMyYTFmZWExYWEwZTU3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
   private static final String ВКХ = "Дезориентация";
   private static final String ВКе = "Явная Пыль";
   private static final String ВКА = "Божья Аура";
   private static final String ВК4 = "Трапка";
   private static final String ВКп = "Пласт";
   private static final String ВКД = "Снежок Заморозка";
   private static final String ВКЪ = "Снотворное";
   private static final String ВКт = "Святая Вода";
   private static final String ВКа = "Хлопушка";
   private static final String ВКЮ = "Зелье Ассасина";
   private static final String ВКЧ = "Зелье Гнева";
   private static final String ВКЛ = "Зелье Палладина";
   private static final String ВК诶 = "Зелье Радиации";
   private static final String ВК1 = "Талисман Демона";
   private static final String ВКЬ = "Талисман Карателя";
   private static final String ВК衣 = "Талисман Крушителя";
   private static final String ВКш = "Талисман Мрака";
   private static final String ВКИ = "Талисман Раздора";
   private static final String ВКь = "Талисман Сара";
   private static final String ВКж = "Талисман Сары";
   private static final String ВК弟 = "Талисман Тирана";
   private static final String ВКб = "Талисман Ярости";
   private static final String ВКЫ = "Божье Касание";
   private static final String ВКl = "Блок дамагер";
   private static final String ВК8 = "Мега Бульдозер";
   private static final String ВК5 = "Огненный Смерч";
   private static final String ВКв = "Проклятая Душа";
   private static final String ВКъ = "Отмычка к Сферам";
   private static final String ВКМ = "Нерушимые Элитры";
   private static final String ВКН = "Маяк";
   private static final String ВКн = "Серебро";
   private static final String ВКЦ = "Драконий Скин";
   private static final String ВКч = "Шлем Крушителя";
   private static final String ВКЭ = "Нагрудник Крушителя";
   private static final String ВК必 = "Поножи Крушителя";
   private static final String ВКщ = "Ботинки Крушителя";
   private static final String ВКГ = "Меч Крушителя";
   private static final String ВКл = "Кирка Крушителя";
   private static final String ВКэ = "Булава Крушителя";
   private static final String ВКю = "Трезубец Крушителя";
   private static final String ВКЩ = "Лук Крушителя";
   private static final String ВКу = "Арбалет Крушителя";
   private static final String ВК2 = "Удочка Крушителя";
   private static final String ВКф = "Элитры Крушителя";
   private static final String ВКи = "Опыт 15";
   private static final String ВК9 = "Опыт 30";
   private static final String ВКо = "Опыт 45";
   private static final String ВКO = "Опыт 50";
   private static final String ВК7 = "Шлем Крушителя";
   private static final String ВКР = "Нагрудник Крушителя";
   private static final String ВК3 = "Поножи Крушителя";
   private static final String ВКр = "Ботинки Крушителя";
   private static final String ВК6 = "Меч Крушителя";
   private static final String ВК_ = "Кирка Крушителя";
   private static final String ВКШ = "Булава Крушителя";
   private static final String ВКы = "Трезубец Крушителя";
   private static final String ВКй = "Арбалет Крушителя";
   private static final String ВКЖ = "Лук Крушителя";
   private static final String ВКм = "Элитры Крушителя";
   private static final String ВКК = "Удочка Крушителя";
   private static final String ВКI = "Сфера Хаоса";
   private static final double ВКТ = 2.5;
   private static final double ВКО = 0.07;
   private static final double ВКФ = 0.13;
   private static final double ВКЙ = 1.5;
   private static final double ВКк = -4.0;
   private static final double ВКг = 0.09;
   private static final String ВК0 = "Сфера Титана";
   private static final double ВКх = 2.5;
   private static final double ВКУ = 2.5;
   private static final double ВКЗ = -0.15;
   private static final String ВКЕ = "Сфера Ареса";
   private static final double ВКП = 6.0;
   private static final double ВК西 = -2.0;
   private static final double ВКд = -2.0;
   private static final String ВКБ = "Сфера Бестии";
   private static final double ВКС = 4.0;
   private static final double ВКз = 0.1;
   private static final double ВКц = 0.1;
   private static final String ВКя = "Сфера Гидры";
   private static final double ВIс = 2.0;
   private static final double ВIВ = 4.0;
   private static final double ВIЯ = 0.5;
   private static final double ВIХ = 0.5;
   private static final String ВIе = "Сфера Икара";
   private static final double ВIА = 2.0;
   private static final double ВI4 = 2.0;
   private static final String ВIп = "Сфера Эрида";
   private static final double ВIД = 2.0;
   private static final String ВIЪ = "Сфера Сатира";
   private static final double ВIт = 2.0;
   private static final double ВIа = -0.1;
   private static final double ВIЮ = 0.15;
   private static final String ВIЧ = "Талисман Демона";
   private static final double ВIЛ = 2.5;
   private static final double ВI诶 = 0.1;
   private static final String ВI1 = "Талисман Карателя";
   private static final double ВIЬ = 7.0;
   private static final double ВI衣 = -4.0;
   private static final double ВIш = 0.1;
   private static final String ВIИ = "Талисман Мрака";
   private static final double ВIь = 1.5;
   private static final double ВIж = 1.5;
   private static final String ВI弟 = "Талисман Ярости";
   private static final double ВIб = 5.0;
   private static final double ВIЫ = -4.0;
   private static final String ВIl = "Талисман Тирана";
   private static final double ВI8 = 2.0;
   private static final double ВI5 = 2.0;
   private static final double ВIв = -4.0;
   private static final String ВIъ = "Талисман Крушителя";
   private static final double ВIМ = 4.0;
   private static final double ВIН = 3.0;
   private static final double ВIн = 2.0;
   private static final double ВIЦ = 2.0;
   private static final String ВIч = "Талисман Раздора";
   private static final double ВIЭ = 4.0;
   private static final double ВI必 = 2.0;
   private static final double ВIщ = 0.1;
   private static final double ВIГ = 0.1;
   private static final double ВIл = -3.0;
   private static final String ВIэ = "Талисман Сара";
   private static final double ВIю = 2.0;
   private static final String ВIЩ = "Талисман Сары";
   private static final double ВIу = 2.0;

   public static long Ю(long var0, int var2) {
      double var3 = var2 / Вмш;
      long var5 = (long)(var0 * var3);
      return Math.max(1L, var0 - var5);
   }

   public static boolean Р(String var0, ItemStack var1) {
      if (var1 == null || var1.isEmpty()) {
         return false;
      }

      if (Вь.containsKey(var0)) {
         return Ж(var0, var1);
      }

      FunTimeItemMetadata.AttributeItemSpec var2 = Вж.get(var0);
      return var2 != null ? Ю(var2, var1) : ЗЖ(var1);
   }

   private static boolean ЗЖ(ItemStack var0) {
      return var0.get(DataComponents.LORE) != null || var0.get(DataComponents.CUSTOM_DATA) != null;
   }

   private FunTimeItemMetadata() {
   }

   static {
      В衣.put(Вм0, Вмх);
      В衣.put(ВмУ, ВмЗ);
      В衣.put(ВмЕ, ВмП);
      В衣.put(Вм西, Вмд);
      В衣.put(ВмБ, ВмС);
      В衣.put(Вмз, Вмц);
      В衣.put(Вмя, ВКс);
      В衣.put(ВКВ, ВКЯ);
      ВИ.put(ВКХ, Items.ENDER_EYE);
      ВИ.put(ВКе, Items.SUGAR);
      ВИ.put(ВКА, Items.PHANTOM_MEMBRANE);
      ВИ.put(ВК4, Items.NETHERITE_SCRAP);
      ВИ.put(ВКп, Items.DRIED_KELP);
      ВИ.put(ВКД, Items.SNOWBALL);
      ВИ.put(ВКЪ, Items.SPLASH_POTION);
      ВИ.put(ВКт, Items.SPLASH_POTION);
      ВИ.put(ВКа, Items.SPLASH_POTION);
      ВИ.put(ВКЮ, Items.SPLASH_POTION);
      ВИ.put(ВКЧ, Items.SPLASH_POTION);
      ВИ.put(ВКЛ, Items.SPLASH_POTION);
      ВИ.put(ВК诶, Items.SPLASH_POTION);
      ВИ.put(ВК1, Items.TOTEM_OF_UNDYING);
      ВИ.put(ВКЬ, Items.TOTEM_OF_UNDYING);
      ВИ.put(ВК衣, Items.TOTEM_OF_UNDYING);
      ВИ.put(ВКш, Items.TOTEM_OF_UNDYING);
      ВИ.put(ВКИ, Items.TOTEM_OF_UNDYING);
      ВИ.put(ВКь, Items.TOTEM_OF_UNDYING);
      ВИ.put(ВКж, Items.TOTEM_OF_UNDYING);
      ВИ.put(ВК弟, Items.TOTEM_OF_UNDYING);
      ВИ.put(ВКб, Items.TOTEM_OF_UNDYING);
      ВИ.put(ВКЫ, Items.GOLDEN_PICKAXE);
      ВИ.put(ВКl, Items.JIGSAW);
      ВИ.put(ВК8, Items.NETHERITE_PICKAXE);
      ВИ.put(ВК5, Items.FIRE_CHARGE);
      ВИ.put(ВКв, Items.SOUL_LANTERN);
      ВИ.put(ВКъ, Items.TRIPWIRE_HOOK);
      ВИ.put(ВКМ, Items.ELYTRA);
      ВИ.put(ВКН, Items.BEACON);
      ВИ.put(ВКн, Items.IRON_NUGGET);
      ВИ.put(ВКЦ, Items.PAPER);
      ВИ.put(ВКч, Items.NETHERITE_HELMET);
      ВИ.put(ВКЭ, Items.NETHERITE_CHESTPLATE);
      ВИ.put(ВК必, Items.NETHERITE_LEGGINGS);
      ВИ.put(ВКщ, Items.NETHERITE_BOOTS);
      ВИ.put(ВКГ, Items.NETHERITE_SWORD);
      ВИ.put(ВКл, Items.NETHERITE_PICKAXE);
      ВИ.put(ВКэ, Items.MACE);
      ВИ.put(ВКю, Items.TRIDENT);
      ВИ.put(ВКЩ, Items.BOW);
      ВИ.put(ВКу, Items.CROSSBOW);
      ВИ.put(ВК2, Items.FISHING_ROD);
      ВИ.put(ВКф, Items.ELYTRA);
      ВИ.put(ВКи, Items.EXPERIENCE_BOTTLE);
      ВИ.put(ВК9, Items.EXPERIENCE_BOTTLE);
      ВИ.put(ВКо, Items.EXPERIENCE_BOTTLE);
      ВИ.put(ВКO, Items.EXPERIENCE_BOTTLE);
      Ю(
         ВК7,
         Items.NETHERITE_HELMET,
         Enchantments.UNBREAKING,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.FIRE_PROTECTION,
         5,
         Enchantments.PROJECTILE_PROTECTION,
         5,
         Enchantments.BLAST_PROTECTION,
         5,
         Enchantments.AQUA_AFFINITY,
         1,
         Enchantments.RESPIRATION,
         3,
         Enchantments.PROTECTION,
         5
      );
      Ю(
         ВКР,
         Items.NETHERITE_CHESTPLATE,
         Enchantments.BLAST_PROTECTION,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.FIRE_PROTECTION,
         5,
         Enchantments.PROJECTILE_PROTECTION,
         5,
         Enchantments.PROTECTION,
         5,
         Enchantments.UNBREAKING,
         5
      );
      Ю(
         ВК3,
         Items.NETHERITE_LEGGINGS,
         Enchantments.BLAST_PROTECTION,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.FIRE_PROTECTION,
         5,
         Enchantments.PROJECTILE_PROTECTION,
         5,
         Enchantments.PROTECTION,
         5,
         Enchantments.UNBREAKING,
         5
      );
      Ю(
         ВКр,
         Items.NETHERITE_BOOTS,
         Enchantments.MENDING,
         1,
         Enchantments.FIRE_PROTECTION,
         5,
         Enchantments.DEPTH_STRIDER,
         3,
         Enchantments.PROJECTILE_PROTECTION,
         5,
         Enchantments.FEATHER_FALLING,
         4,
         Enchantments.SOUL_SPEED,
         3,
         Enchantments.BLAST_PROTECTION,
         5,
         Enchantments.PROTECTION,
         5,
         Enchantments.UNBREAKING,
         5
      );
      Ю(
         ВК6,
         Items.NETHERITE_SWORD,
         Enchantments.UNBREAKING,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.SMITE,
         7,
         Enchantments.SWEEPING_EDGE,
         3,
         Enchantments.FIRE_ASPECT,
         2,
         Enchantments.BANE_OF_ARTHROPODS,
         7,
         Enchantments.SHARPNESS,
         7,
         Enchantments.LOOTING,
         5
      );
      Ю(ВК_, Items.NETHERITE_PICKAXE, Enchantments.UNBREAKING, 5, Enchantments.MENDING, 1, Enchantments.EFFICIENCY, 10, Enchantments.FORTUNE, 5);
      Ю(
         ВКШ,
         Items.MACE,
         Enchantments.SHARPNESS,
         7,
         Enchantments.SMITE,
         7,
         Enchantments.BANE_OF_ARTHROPODS,
         7,
         Enchantments.DENSITY,
         5,
         Enchantments.BREACH,
         3,
         Enchantments.SWEEPING_EDGE,
         3,
         Enchantments.FIRE_ASPECT,
         2,
         Enchantments.LOOTING,
         5,
         Enchantments.UNBREAKING,
         5,
         Enchantments.MENDING,
         1
      );
      Ю(
         ВКы,
         Items.TRIDENT,
         Enchantments.UNBREAKING,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.CHANNELING,
         1,
         Enchantments.FIRE_ASPECT,
         2,
         Enchantments.IMPALING,
         5,
         Enchantments.SHARPNESS,
         7,
         Enchantments.LOYALTY,
         3
      );
      Ю(
         ВКй,
         Items.CROSSBOW,
         Enchantments.QUICK_CHARGE,
         3,
         Enchantments.MENDING,
         1,
         Enchantments.PIERCING,
         5,
         Enchantments.UNBREAKING,
         3,
         Enchantments.MULTISHOT,
         1
      );
      Ю(ВКЖ, Items.BOW);
      Ю(ВКм, Items.ELYTRA);
      Ю(ВКК, Items.FISHING_ROD);
      Ж(
         ВКI,
         Items.PLAYER_HEAD,
         Attributes.ATTACK_DAMAGE,
         ВКТ,
         Attributes.MOVEMENT_SPEED,
         ВКО,
         Attributes.ATTACK_SPEED,
         ВКФ,
         Attributes.ARMOR,
         ВКЙ,
         Attributes.MAX_HEALTH,
         ВКк,
         Attributes.GRAVITY,
         ВКг
      );
      Ж(ВК0, Items.PLAYER_HEAD, Attributes.ARMOR, ВКх, Attributes.ARMOR_TOUGHNESS, ВКУ, Attributes.MOVEMENT_SPEED, ВКЗ);
      Ж(ВКЕ, Items.PLAYER_HEAD, Attributes.ATTACK_DAMAGE, ВКП, Attributes.ARMOR, ВК西, Attributes.MAX_HEALTH, ВКд);
      Ж(ВКБ, Items.PLAYER_HEAD, Attributes.ARMOR, 1.0, Attributes.MAX_HEALTH, ВКС, Attributes.MOVEMENT_SPEED, ВКз, Attributes.ATTACK_SPEED, ВКц);
      Ж(ВКя, Items.PLAYER_HEAD, Attributes.ARMOR, ВIс, Attributes.MAX_HEALTH, ВIВ, Attributes.SUBMERGED_MINING_SPEED, ВIЯ, Attributes.OXYGEN_BONUS, ВIХ);
      Ж(ВIе, Items.PLAYER_HEAD, Attributes.ATTACK_DAMAGE, ВIА, Attributes.MAX_HEALTH, ВI4);
      Ж(ВIп, Items.PLAYER_HEAD, Attributes.LUCK, 1.0, Attributes.MAX_HEALTH, ВIД, Attributes.BLOCK_INTERACTION_RANGE, 1.0);
      Ж(ВIЪ, Items.PLAYER_HEAD, Attributes.ATTACK_DAMAGE, ВIт, Attributes.JUMP_STRENGTH, ВIа, Attributes.ATTACK_SPEED, ВIЮ);
      Ж(ВIЧ, Items.TOTEM_OF_UNDYING, Attributes.ATTACK_DAMAGE, ВIЛ, Attributes.ATTACK_SPEED, ВI诶);
      Ж(ВI1, Items.TOTEM_OF_UNDYING, Attributes.ATTACK_DAMAGE, ВIЬ, Attributes.MAX_HEALTH, ВI衣, Attributes.MOVEMENT_SPEED, ВIш);
      Ж(ВIИ, Items.TOTEM_OF_UNDYING, Attributes.ARMOR, ВIь, Attributes.MAX_HEALTH, ВIж);
      Ж(ВI弟, Items.TOTEM_OF_UNDYING, Attributes.ATTACK_DAMAGE, ВIб, Attributes.MAX_HEALTH, ВIЫ);
      Ж(ВIl, Items.TOTEM_OF_UNDYING, Attributes.ATTACK_DAMAGE, ВI8, Attributes.ARMOR, ВI5, Attributes.MAX_HEALTH, ВIв);
      Ж(ВIъ, Items.TOTEM_OF_UNDYING, Attributes.MAX_HEALTH, ВIМ, Attributes.ATTACK_DAMAGE, ВIН, Attributes.ARMOR_TOUGHNESS, ВIн, Attributes.ARMOR, ВIЦ);
      Ж(
         ВIч,
         Items.TOTEM_OF_UNDYING,
         Attributes.ATTACK_DAMAGE,
         ВIЭ,
         Attributes.MAX_HEALTH,
         ВI必,
         Attributes.MOVEMENT_SPEED,
         ВIщ,
         Attributes.ATTACK_SPEED,
         ВIГ,
         Attributes.ARMOR,
         ВIл
      );
      Ж(ВIэ, Items.TOTEM_OF_UNDYING, Attributes.MAX_HEALTH, ВIю);
      Ж(ВIЩ, Items.TOTEM_OF_UNDYING, Attributes.MAX_HEALTH, ВIу);
   }

   public static String ЗД(String var0) {
      return В衣.get(var0);
   }

   public static long Р(String var0) {
      if (var0 == null) {
         return Вм8;
      }

      Matcher var1 = б.matcher(弟.matcher(var0).replaceAll(""));
      if (!var1.find()) {
         return Вм5;
      }

      try {
         return Long.parseLong(var1.group(1));
      } catch (NumberFormatException var3) {
         return Вмв;
      }
   }

   public static boolean Зы(String var0) {
      if (var0 == null) {
         return false;
      }

      String var1 = var0.replaceAll(Вм6, "").replace(' ', ' ').replace((char)Вм_, ':').replaceAll(ВмШ, Вмы).trim().toLowerCase(Locale.ROOT);
      return var1.contains(Вмй)
         || var1.contains(ВмЖ)
         || var1.contains(Вмм)
         || var1.contains(ВмК)
         || var1.contains(ВмI)
         || var1.contains(ВмТ)
         || var1.contains(ВмО)
         || var1.contains(ВмФ);
   }

   private static void Ж(String var0, Item var1, Object... var2) {
      Holder[] var3 = new Holder[var2.length / 2];
      double[] var4 = new double[var2.length / 2];

      for (int var5 = 0; var5 < var2.length; var5 += 2) {
         var3[var5 / 2] = (Holder)var2[var5];
         var4[var5 / 2] = ((Number)var2[var5 + 1]).doubleValue();
      }

      Вж.put(var0, new FunTimeItemMetadata.AttributeItemSpec(var1, var3, var4));
   }

   private static int Ю(ItemStack var0, ResourceKey<Enchantment> var1) {
      ItemEnchantments var2 = var0.get(DataComponents.ENCHANTMENTS);
      if (var2 == null) {
         return 0;
      }

      for (Entry var4 : var2.entrySet()) {
         if (((Holder)var4.getKey()).is(var1)) {
            return var4.getIntValue();
         }
      }

      return 0;
   }

   private static void Ю(String var0, Item var1, Object... var2) {
      ResourceKey[] var3 = new ResourceKey[var2.length / 2];
      int[] var4 = new int[var2.length / 2];

      for (int var5 = 0; var5 < var2.length; var5 += 2) {
         var3[var5 / 2] = (ResourceKey)var2[var5];
         var4[var5 / 2] = (Integer)var2[var5 + 1];
      }

      Вь.put(var0, new FunTimeItemMetadata.EnchantedItemSpec(var1, var3, var4, false));
   }

   public static long Ж(String var0) {
      if (var0 == null) {
         return ВмИ;
      }

      String var1 = Вмь;
      String var2 = Вмж;
      int var3 = var0.indexOf(var1);
      int var4 = var0.indexOf(var2);
      if (var3 != -1 && var4 != -1) {
         String var5 = var0.substring(var4 + var2.length()).replaceAll(Вмб, "").trim();
         if (var5.isEmpty()) {
            return ВмЫ;
         }

         try {
            return Long.parseLong(var5);
         } catch (NumberFormatException var7) {
            return Вмl;
         }
      } else {
         return Вм弟;
      }
   }

   private static void Ю(String var0, Item var1) {
      Вь.put(var0, new FunTimeItemMetadata.EnchantedItemSpec(var1, new ResourceKey[0], new int[0], true));
   }

   public static long Ю(String var0) {
      if (var0 == null) {
         return 0L;
      }

      String var1 = var0.replace(' ', ' ').toLowerCase(Locale.ROOT).trim();
      long var2 = 1L;
      if (var1.contains(Вмт) || var1.endsWith(Вма) || var1.endsWith(ВмЮ)) {
         var2 = ВмЧ;
      } else if (var1.contains(ВмЛ) || var1.endsWith(Вм诶) || var1.endsWith(Вм1)) {
         var2 = ВмЬ;
      }

      String var4 = var1.replaceAll(Вм衣, "");
      if (var4.isEmpty()) {
         return 0L;
      }

      try {
         return Math.multiplyExact(Long.parseLong(var4), var2);
      } catch (ArithmeticException | NumberFormatException var6) {
         return 0L;
      }
   }

   public static long Ж(ItemStack var0) {
      if (var0 != null && !var0.isEmpty()) {
         ItemLore var1 = var0.getComponents().get(DataComponents.LORE);
         if (var1 == null) {
            return 0L;
         }

         for (Component var3 : var1.lines()) {
            String var4 = var3.getString();
            if (var4.contains(Вмп) || var4.contains(ВмД)) {
               String var5 = var4.replaceAll(ВмЪ, "");
               if (!var5.isEmpty()) {
                  try {
                     return Long.parseLong(var5);
                  } catch (NumberFormatException var7) {
                     return 0L;
                  }
               }
            }
         }

         return 0L;
      } else {
         return 0L;
      }
   }

   public static boolean Ж(String var0, ItemStack var1) {
      FunTimeItemMetadata.EnchantedItemSpec var2 = Вь.get(var0);
      if (var2 == null) {
         return true;
      }

      if (var1 != null && !var1.isEmpty() && var1.is(var2.item())) {
         if (var2.needGlint() && !var1.isEnchanted()) {
            return false;
         }

         for (int var3 = 0; var3 < var2.enchants().length; var3++) {
            if (Ю(var1, var2.enchants()[var3]) < var2.levels()[var3]) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean Ю(FunTimeItemMetadata.AttributeItemSpec var0, ItemStack var1) {
      if (!var1.is(var0.item())) {
         return false;
      }

      ItemAttributeModifiers var2 = var1.get(DataComponents.ATTRIBUTE_MODIFIERS);
      if (var2 == null) {
         return var0.attributes().length == 0;
      }

      HashMap var3 = new HashMap();
      int var4 = 0;

      for (net.minecraft.world.item.component.ItemAttributeModifiers.Entry var6 : var2.modifiers()) {
         var4++;
         var3.put(var6.attribute(), var6.modifier().amount());
      }

      if (var4 == var0.attributes().length && var3.size() == var0.attributes().length) {
         for (int var7 = 0; var7 < var0.attributes().length; var7++) {
            Double var8 = (Double)var3.get(var0.attributes()[var7]);
            if (var8 == null || Math.abs(var8 - var0.values()[var7]) > ВмЩ) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static ItemStack method_4(String var0) {
      if (var0 == null) {
         return null;
      } else if (Вш.containsKey(var0)) {
         return Вш.get(var0);
      } else {
         String var1 = В衣.get(var0);
         if (var1 == null) {
            Вш.put(var0, null);
            return null;
         } else {
            ItemStack var2 = new ItemStack(Items.PLAYER_HEAD);
            ArrayListMultimap var3 = ArrayListMultimap.create();
            var3.put(Вмл, new Property(Вмэ, var1));
            GameProfile var4 = new GameProfile(UUID.randomUUID(), Вмю, new PropertyMap(var3));
            var2.set(DataComponents.PROFILE, ResolvableProfile.createResolved(var4));
            Вш.put(var0, var2);
            return var2;
         }
      }
   }

   public static String Зж(String var0) {
      return var0 == null
         ? ""
         : var0.replaceAll(Вмъ, "").replaceAll(ВмМ, "").replaceAll(ВмН, Вмн).replaceAll(ВмЦ, Вмч).replace(ВмЭ, Вм必).replaceAll(Вмщ, ВмГ).trim();
   }

   public static List<FunTimeItemMetadata.TimedItemLocation> Ю(ClientboundSoundPacket var0) {
      ArrayList var1 = new ArrayList(2);
      if (var0 == null) {
         return var1;
      }

      String var2 = Ж(var0);
      float var3 = var0.getVolume();
      float var4 = var0.getPitch();
      Vec3 var5 = Vec3.atCenterOf(BlockPos.containing(var0.getX(), var0.getY(), var0.getZ()).above());
      if (var2.contains(Вму) && (var3 == Вм2 || var3 == Вмф) && (var4 == Вми || var4 == Вм9)) {
         var1.add(new FunTimeItemMetadata.TimedItemLocation(Items.DRIED_KELP, var5, Вмо));
      }

      if ((var2.contains(ВмO) || var2.contains(Вм7)) && var3 == ВмР || var3 == Вм3 && var4 == 1.0F) {
         var1.add(new FunTimeItemMetadata.TimedItemLocation(Items.NETHERITE_SCRAP, var5, Вмр));
      }

      return var1;
   }

   public static ItemStack 必(String var0) {
      ItemStack var1 = method_4(var0);
      if (var1 != null) {
         return var1.copy();
      }

      Item var2 = ВИ.get(var0);
      return var2 == null ? null : new ItemStack(var2);
   }

   private static String Ж(ClientboundSoundPacket var0) {
      Identifier var1 = var0.getSound().value().location();
      return var1 == null ? "" : var1.toString();
   }

   public static String П(ItemStack var0) {
      if (var0 != null && !var0.isEmpty()) {
         ItemLore var1 = var0.getComponents().get(DataComponents.LORE);
         if (var1 == null) {
            return null;
         }

         for (Component var3 : var1.lines()) {
            String var4 = 弟.matcher(var3.getString()).replaceAll("");
            Matcher var5 = ж.matcher(var4);
            if (var5.find()) {
               return var5.group(1).trim();
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public record TimedItemLocation(Item item, Vec3 position, long durationMs) {
   }

   private record AttributeItemSpec(Item item, Holder<Attribute>[] attributes, double[] values) {
   }

   private record EnchantedItemSpec(Item item, ResourceKey<Enchantment>[] enchants, int[] levels, boolean needGlint) {
   }
}
