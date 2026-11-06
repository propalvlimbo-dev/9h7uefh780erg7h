package sg.util.time;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sg.ec.MinecraftAccess;

public final class ReallyWorldItemCooldowns implements MinecraftAccess {
   private static ReallyWorldItemCooldowns В;
   private final Map<ReallyWorldItemCooldowns.TrackedItem, Long> 西 = new EnumMap<>(ReallyWorldItemCooldowns.TrackedItem.class);
   private boolean В7;
   private static final String ЯьД = "reallyworld";
   private static final float ЯьЪ = 1000.0F;
   private static final long Яьт = 500L;
   private static final float Яьа = 1000.0F;
   private static final String ЯьЮ = "\\s+";
   private static final String ЯьЧ = " ";

   public void ЛР() {
      if (this.method_3Щ() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gui.screen() == null) {
         ReallyWorldItemCooldowns.TrackedItem var1 = Ы(MINECRAFT_CLIENT.player.getMainHandItem(), ReallyWorldItemCooldowns.ActivationMethod.USE);
         if (var1 == null) {
            var1 = Ы(MINECRAFT_CLIENT.player.getOffhandItem(), ReallyWorldItemCooldowns.ActivationMethod.USE);
         }

         if (var1 != null) {
            this.Ы_de5a6f78(var1);
         }
      }
   }

   public boolean O(Item var1) {
      if (!this.method_3Щ()) {
         return false;
      }

      long var2 = System.currentTimeMillis();

      for (ReallyWorldItemCooldowns.TrackedItem var7 : ReallyWorldItemCooldowns.TrackedItem.values()) {
         if (var7.В_50cb85c5 == var1) {
            Long var8 = this.西.get(var7);
            if (var8 != null && var8 > var2) {
               return true;
            }
         }
      }

      return false;
   }

   public List<ReallyWorldItemCooldowns.CooldownEntry> 必_2571477d() {
      ArrayList var1 = new ArrayList();
      if (this.method_3Щ() && MINECRAFT_CLIENT.player != null) {
         long var2 = System.currentTimeMillis();

         for (ReallyWorldItemCooldowns.TrackedItem var7 : ReallyWorldItemCooldowns.TrackedItem.values()) {
            Long var8 = this.西.get(var7);
            if (var8 != null) {
               float var9 = (float)(var8 - var2) / ЯьЪ;
               if (!(var9 <= 0.0F)) {
                  var1.add(new ReallyWorldItemCooldowns.CooldownEntry(var7, net.minecraft.network.chat.Component.literal(var7.щ), var9, var7.В_50cb85c5));
               }
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static String Б(String var0) {
      return var0 == null ? "" : var0.toLowerCase(Locale.ROOT).replace('ё', 'е').replaceAll(ЯьЮ, ЯьЧ).trim();
   }

   public static ReallyWorldItemCooldowns Ы() {
      if (В == null) {
         В = new ReallyWorldItemCooldowns();
      }

      return В;
   }

   private ReallyWorldItemCooldowns() {
   }

   public static ReallyWorldItemCooldowns.TrackedItem Ы(ItemStack var0) {
      if (var0 != null && !var0.isEmpty()) {
         String var1 = Б(StringUtil.stripColor(var0.getHoverName().getString()));
         if (var1.isEmpty()) {
            return null;
         }

         for (ReallyWorldItemCooldowns.TrackedItem var5 : ReallyWorldItemCooldowns.TrackedItem.values()) {
            if (var0.getItem() == var5.В_50cb85c5 && var5.я(var1)) {
               return var5;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public boolean method_3Щ() {
      if (MINECRAFT_CLIENT.getConnection() == null) {
         return false;
      }

      ServerData var1 = MINECRAFT_CLIENT.getConnection().getServerData();
      return var1 != null && var1.ip != null ? var1.ip.toLowerCase(Locale.ROOT).contains(ЯьД) : false;
   }

   private static ReallyWorldItemCooldowns.TrackedItem Ы(ItemStack var0, ReallyWorldItemCooldowns.ActivationMethod var1) {
      ReallyWorldItemCooldowns.TrackedItem var2 = Ы(var0);
      return var2 != null && var2.В_ecbefc53 == var1 ? var2 : null;
   }

   private void Ы_de5a6f78(ReallyWorldItemCooldowns.TrackedItem var1) {
      Long var2 = this.西.get(var1);
      long var3 = System.currentTimeMillis();
      if (var2 == null || var2 <= var3 + Яьт) {
         this.西.put(var1, var3 + (long)(var1.сл * Яьа));
      }
   }

   public boolean Ы_bbeebd87(ReallyWorldItemCooldowns.TrackedItem var1) {
      if (var1 != null && this.method_3Щ()) {
         Long var2 = this.西.get(var1);
         return var2 != null && var2 > System.currentTimeMillis();
      } else {
         return false;
      }
   }

   public void Лб() {
      if (this.method_3Щ() && MINECRAFT_CLIENT.player != null) {
         boolean var1 = MINECRAFT_CLIENT.player.isShiftKeyDown() || MINECRAFT_CLIENT.options.keyShift.isDown();
         if (var1 && !this.В7 && MINECRAFT_CLIENT.gui.screen() == null) {
            ReallyWorldItemCooldowns.TrackedItem var2 = Ы(MINECRAFT_CLIENT.player.getOffhandItem(), ReallyWorldItemCooldowns.ActivationMethod.SNEAK_OFFHAND);
            if (var2 != null) {
               this.Ы_de5a6f78(var2);
            }
         }

         this.В7 = var1;
         long var4 = System.currentTimeMillis();
         this.西.entrySet().removeIf(var2x -> var2x.getValue() <= var4);
      } else {
         this.В7 = false;
         this.西.clear();
      }
   }

   public enum ActivationMethod {
      USE,
      SNEAK_OFFHAND;
   }

   public enum TrackedItem {
      TRAP("Ловушка", Items.HEART_OF_THE_SEA, ReallyWorldItemCooldowns.ActivationMethod.USE, 60.0F),
      ENDER_TRAP("Эндер Ловушка", Items.HEART_OF_THE_SEA, ReallyWorldItemCooldowns.ActivationMethod.USE, 60.0F),
      ANTI_FLIGHT("Анти Полет", Items.FIREWORK_STAR, ReallyWorldItemCooldowns.ActivationMethod.SNEAK_OFFHAND, 1800.0F),
      DARK_PULSE("Тёмный Пульс", Items.FIREWORK_STAR, ReallyWorldItemCooldowns.ActivationMethod.SNEAK_OFFHAND, 180.0F),
      STONE_TRAP("Каменная ловушка", Items.FIREWORK_STAR, ReallyWorldItemCooldowns.ActivationMethod.USE, 90.0F, true),
      HELL_TRAP("Адская ловушка", Items.NETHERITE_SCRAP, ReallyWorldItemCooldowns.ActivationMethod.USE, 120.0F, true);

      private final String щ;
      private final String Г;
      @SerializedName("В")
      private final Item В_50cb85c5;
      @SerializedName("В")
      private final ReallyWorldItemCooldowns.ActivationMethod В_ecbefc53;
      private final float сл;
      private final boolean у;

      TrackedItem(String var3, Item var4, ReallyWorldItemCooldowns.ActivationMethod var5, float var6) {
         this(var3, var4, var5, var6, false);
      }

      TrackedItem(String var3, Item var4, ReallyWorldItemCooldowns.ActivationMethod var5, float var6, boolean var7) {
         this.щ = var3;
         this.Г = ReallyWorldItemCooldowns.Б(var3);
         this.В_50cb85c5 = var4;
         this.В_ecbefc53 = var5;
         this.сл = var6;
         this.у = var7;
      }

      private boolean я(String var1) {
         return this.у ? var1.contains(this.Г) : var1.equals(this.Г);
      }

      public String бЩ() {
         return this.щ;
      }

      public Item С_50cb85c5() {
         return this.В_50cb85c5;
      }

      public ReallyWorldItemCooldowns.ActivationMethod С_ecbefc53() {
         return this.В_ecbefc53;
      }
   }

   public record CooldownEntry(ReallyWorldItemCooldowns.TrackedItem item, net.minecraft.network.chat.Component name, float remaining, Item icon) {
   }
}
