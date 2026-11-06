package sg.modules.impl.misc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import sg.ec.ItemPriceParser;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;

public final class AhHelper extends ClientModule {
   private static final long ВБ = 60100L;
   private static AhHelper Я_at_8fa7bc0c;
   private final KeybindSetting щ;
   private final BooleanSetting сБ;
   private final BooleanSetting сС;
   private final BooleanSetting сз;
   private final BooleanSetting сц;
   private final EventListener<InputEvent> В0;
   private final EventListener<PlayerTickEvent> Вх;
   private Set<Integer> ъ;
   private Set<Integer> М;
   private long ВС;
   private long Вз;
   private static final String ЯIв = "AhHelper";
   private static final String ЯIъ = "Помощник аукциона";
   private static final String ЯIМ = "Поиск предмета в руке";
   private static final String ЯIН = "Четыре самых дешёвых";
   private static final String ЯIн = "Четыре выгодных за штуку";
   private static final String ЯIЦ = "Цена за 1 предмет";
   private static final String ЯIч = "Перевыставление предметов";
   private static final long ЯIЭ = -1L;
   private static final long ЯI必 = 300L;
   private static final long ЯIщ = 4L;
   private static final long ЯIГ = 4L;
   private static final String ЯIл = "хранилище";
   private static final String ЯIэ = "storage";
   private static final long ЯIю = -1L;
   private static final long ЯIЩ = 60100L;
   private static final String ЯIу = "причин";
   private static final String ЯI2 = "reason";
   private static final String ЯIф = "продано";
   private static final String ЯIи = "sold";
   private static final double ЯI9 = Double.MAX_VALUE;
   private static final double ЯIо = Double.MAX_VALUE;
   private static final String ЯIO = "Цена за 1 шт:";
   private static final String ЯI7 = "Цена";
   private static final String ЯIР = ".*\\$\\s*[\\d,.]+.*";
   private static final String ЯI3 = "аукцион";
   private static final String ЯIр = "аукционы";
   private static final String ЯI6 = "auction";
   private static final String ЯI_ = "поиск";
   private static final String ЯIШ = "search";
   private static final String ЯIы = "п:";
   private static final String ЯIй = "0a2z";
   private static final String ЯIЖ = "^\\[★\\]\\s*";
   private static final String ЯIм = "[★]";
   private static final String ЯIК = "^\\[.*?]\\s*";
   private static final String ЯII = "\\s+";
   private static final String ЯIТ = " ";
   private static final String ЯIО = "(?i)§[0-9A-FK-OR]";
   private static final String ЯIФ = "(?i)&[0-9A-FK-OR]";
   private static final String ЯIЙ = "\\[[^]]*]";
   private static final String ЯIк = "\\s+";
   private static final String ЯIг = " ";

   private void method_0щ() {
      if (MINECRAFT_CLIENT.gui.screen() instanceof AbstractContainerScreen var1 && ь_bbeebd87(var1)) {
         List<Slot> var6 = this.ь_2571477d(var1);
         if (this.сБ.getValue()) {
            this.ъ = var6.stream()
               .filter(this::л_bbeebd87)
               .map(var1x -> new AhHelper.AuctionListing(var1x.index, ItemPriceParser.parsePrice(var1x.getItem()), this.ь_3f39d5c3(var1x)))
               .sorted(Comparator.comparingInt(AhHelper.AuctionListing::price))
               .limit(ЯIщ)
               .map(AhHelper.AuctionListing::id)
               .collect(Collectors.toUnmodifiableSet());
         } else {
            this.ъ = Set.of();
         }

         if (this.сС.getValue()) {
            this.М = var6.stream()
               .filter(this::л_bbeebd87)
               .filter(var0 -> var0.getItem().getCount() > 1)
               .map(var1x -> new AhHelper.AuctionListing(var1x.index, ItemPriceParser.parsePrice(var1x.getItem()), this.ь_3f39d5c3(var1x)))
               .sorted(Comparator.comparingDouble(AhHelper.AuctionListing::unitPrice))
               .limit(ЯIГ)
               .map(AhHelper.AuctionListing::id)
               .collect(Collectors.toUnmodifiableSet());
         } else {
            this.М = Set.of();
         }

         if (this.сз.getValue()) {
            for (Slot var4 : var6) {
               ItemStack var5 = var4.getItem();
               if (var5 != null && !var5.isEmpty() && var5.getCount() > 1) {
                  this.к(var5);
               }
            }
         }
      } else {
         this.ъ = Set.of();
         this.М = Set.of();
      }
   }

   private static String Д(int var0) {
      String var1 = Integer.toString(var0);
      StringBuilder var2 = new StringBuilder();
      int var3 = 0;

      for (int var4 = var1.length() - 1; var4 >= 0; var4--) {
         if (var3 > 0 && var3 % 3 == 0) {
            var2.insert(0, '.');
         }

         var2.insert(0, var1.charAt(var4));
         var3++;
      }

      return var2.toString();
   }

   private static String а(String var0) {
      return var0 == null ? "" : var0.replaceAll(ЯIО, "").replaceAll(ЯIФ, "").replaceAll(ЯIЙ, "").replaceAll(ЯIк, ЯIг).trim();
   }

   private void method_0Е() {
      this.method_0щ();
      this.method_0е();
   }

   public static boolean Ц(int var0) {
      return Я_at_8fa7bc0c != null && Я_at_8fa7bc0c.isEnabled() && Я_at_8fa7bc0c.сБ.getValue() && Я_at_8fa7bc0c.ъ.contains(var0);
   }

   private boolean л_bbeebd87(Slot var1) {
      if (var1 == null || !var1.hasItem()) {
         return false;
      } else {
         return this.й(var1.getItem()) ? false : ItemPriceParser.parsePrice(var1.getItem()) > 0;
      }
   }

   private void I(InputEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null || MINECRAFT_CLIENT.gui.screen() instanceof AbstractContainerScreen) {
         if (var1.getAction() == 1) {
            if (var1.getInputCode() == this.щ.getValue()) {
               if (System.currentTimeMillis() - this.ВС >= ЯI必) {
                  if (MINECRAFT_CLIENT.player != null) {
                     ItemStack var2 = MINECRAFT_CLIENT.player.getMainHandItem();
                     if (var2 != null && !var2.isEmpty()) {
                        String var3 = Х(var2);
                        if (!var3.isBlank()) {
                           MINECRAFT_CLIENT.player.connection.sendCommand("ah search " + var3);
                           this.ВС = System.currentTimeMillis();
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void method_0е() {
      if (this.сц.getValue()) {
         if (MINECRAFT_CLIENT.gui.screen() instanceof AbstractContainerScreen var1) {
            if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
               String var7 = г(var1.getTitle().getString());
               if (var7.contains(ЯIл) || var7.contains(ЯIэ)) {
                  long var3 = System.currentTimeMillis();
                  if (this.Вз == ЯIю || var3 - this.Вз >= ЯIЩ) {
                     NonNullList var5 = var1.getMenu().slots;
                     if (var5.size() > 52) {
                        Slot var6 = (Slot)var5.get(52);
                        if (var6 != null && var6.hasItem()) {
                           MINECRAFT_CLIENT.gameMode.handleContainerInput(var1.getMenu().containerId, 52, 0, ContainerInput.QUICK_MOVE, MINECRAFT_CLIENT.player);
                           this.Вз = var3;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void к(ItemStack var1) {
      int var2 = ItemPriceParser.parsePrice(var1);
      if (var2 > 0) {
         int var3 = var1.getCount();
         if (var3 > 1) {
            int var4 = var2 / var3;
            String var5 = "§a$ §fЦена за 1 шт: §a$" + Д(var4);
            ItemLore var6 = var1.get(DataComponents.LORE);
            ArrayList<Component> var7 = new ArrayList<>();
            if (var6 != null) {
               for (Component var9 : var6.lines()) {
                  if (var9.getString().contains(ЯIO)) {
                     return;
                  }

                  var7.add(var9);
               }
            }

            boolean var13 = false;
            ArrayList var14 = new ArrayList();

            for (Component var11 : var7) {
               var14.add(var11);
               if (!var13) {
                  String var12 = var11.getString();
                  if (var12.contains(ЯI7) || var12.matches(ЯIР)) {
                     var14.add(Component.literal(var5));
                     var13 = true;
                  }
               }
            }

            if (!var13) {
               var14.add(Component.literal(var5));
            }

            var1.set(DataComponents.LORE, new ItemLore(var14));
         }
      }
   }

   private boolean й(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         ItemLore var2 = var1.get(DataComponents.LORE);
         if (var2 == null) {
            return false;
         }

         for (Component var4 : var2.lines()) {
            String var5 = г(var4.getString());
            boolean var6 = var5.contains(ЯIу) || var5.contains(ЯI2);
            boolean var7 = var5.contains(ЯIф) || var5.contains(ЯIи);
            if (var6 && var7) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static String г(String var0) {
      return var0 == null ? "" : var0.toLowerCase(Locale.ROOT).replace(' ', ' ').trim();
   }

   public static boolean l(int var0) {
      return Я_at_8fa7bc0c != null && Я_at_8fa7bc0c.isEnabled() && Я_at_8fa7bc0c.сС.getValue() && Я_at_8fa7bc0c.М.contains(var0);
   }

   public AhHelper() {
      super(ЯIв, ЯIъ, ModuleCategory.MISC);
      this.щ = new KeybindSetting(ЯIМ);
      this.сБ = new BooleanSetting(ЯIН, true);
      this.сС = new BooleanSetting(ЯIн, true);
      this.сз = new BooleanSetting(ЯIЦ, true);
      this.сц = new BooleanSetting(ЯIч, false);
      this.В0 = this::I;
      this.Вх = var1 -> this.method_0Е();
      this.ъ = Set.of();
      this.М = Set.of();
      this.Вз = ЯIЭ;
      Я_at_8fa7bc0c = this;
      this.addSettings(this.щ, this.сБ, this.сС, this.сз, this.сц);
   }

   private static boolean ь_bbeebd87(AbstractContainerScreen<?> var0) {
      String var1 = г(var0.getTitle().getString());
      return var1.contains(ЯI3)
         || var1.contains(ЯIр)
         || var1.contains(ЯI6)
         || var1.contains(ЯI_)
         || var1.contains(ЯIШ)
         || var1.contains(ЯIы)
         || var1.contains(ЯIй);
   }

   private List<Slot> ь_2571477d(AbstractContainerScreen<?> var1) {
      NonNullList var2 = var1.getMenu().slots;
      return var2 != null && !var2.isEmpty() ? var2.subList(0, Math.min(54, var2.size())) : List.of();
   }

   private double ь_3f39d5c3(Slot var1) {
      if (var1 != null && var1.hasItem()) {
         int var2 = ItemPriceParser.parsePrice(var1.getItem());
         if (var2 <= 0) {
            return ЯIо;
         }

         int var3 = Math.max(1, var1.getItem().getCount());
         return (double)var2 / var3;
      } else {
         return ЯI9;
      }
   }

   private static String Х(ItemStack var0) {
      String var1 = а(var0.getHoverName().getString());
      if (var1.isBlank()) {
         var1 = а(var0.getItemName().getString());
      }

      return var1.replaceAll(ЯIЖ, "").replace(ЯIм, "").replaceAll(ЯIК, "").replaceAll(ЯII, ЯIТ).trim();
   }

   private record AuctionListing(int id, int price, double unitPrice) {
   }
}
