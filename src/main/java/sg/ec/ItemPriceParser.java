package sg.ec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public final class ItemPriceParser {
   private static final Pattern PRICE_PATTERN = Pattern.compile(ItemPriceParser.PRICE_PATTERN_SOURCE, 66);
   private static final String NON_DIGIT_PATTERN = "[^0-9]";
   private static final long MAX_PRICE_LONG = 2147483647L;
   private static final int MAX_PRICE = Integer.MAX_VALUE;
   private static final String PRICE_PATTERN_SOURCE = "(?:\\$|цена)[^0-9]*([0-9][0-9 ,.]*)(?:$|[^0-9])";

   private ItemPriceParser() {
   }

   public static int parsePrice(ItemStack stack) {
      ItemLore lore = stack.get(DataComponents.LORE);
      if (lore == null) {
         return 0;
      }

      for (Component line : lore.lines()) {
         Matcher matcher = PRICE_PATTERN.matcher(line.getString());
         if (matcher.find()) {
            try {
               long price = Long.parseLong(matcher.group(1).replaceAll(NON_DIGIT_PATTERN, ""));
               return price > MAX_PRICE_LONG ? MAX_PRICE : (int)price;
            } catch (NumberFormatException invalidPrice) {
               return 0;
            }
         }
      }

      return 0;
   }
}
