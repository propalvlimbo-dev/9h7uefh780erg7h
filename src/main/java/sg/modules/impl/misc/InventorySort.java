package sg.modules.impl.misc;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.equipment.Equippable;
import sg.modules.impl.init.InventoryPresetStore;
import sg.modules.impl.init.FunTimeItemCatalog;
import sg.gui.inventory.InventoryPresetScreen;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.modules.impl.init.NotificationManager;
import sg.settings.ActionSetting;
import sg.settings.FloatSetting;
import sg.network.FunTimeItemMetadata;
import sg.util.time.Stopwatch;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;

public final class InventorySort extends ClientModule {
   public static final int с必 = 41;
   public static final int сщ = 39;
   public static final int сГ = 38;
   public static final int сл = 37;
   public static final int сэ = 36;
   public static final int сю = 40;
   public static final String К = "Шар";
   public static final String I = "Тыква";
   public static final String Т = "Голова Джека";
   public static final String О = "Силка";
   public static final String Ф_388008bb = "Скорка";
   public static final String Й_388008bb = "Огнестойка";
   private static final int[] Х_at_56169efa = Э_28f5b604();
   @SerializedName("В")
   private final InventorySort.SlotRule[] В_d8cfd864 = new InventorySort.SlotRule[41];
   @SerializedName("В")
   private InventoryPresetStore В_3a79a21b = new InventoryPresetStore();
   @SerializedName("е")
   private volatile JsonObject е_at_56169efa = this.В_3a79a21b.Ф();
   @SerializedName("Я")
   private BooleanSupplier Я_at_56169efa = () -> false;
   @SerializedName("Ф")
   private boolean Ф_bbeebd87;
   @SerializedName("Й")
   private boolean Й_bbeebd87 = true;
   @SerializedName("3")
   private final FloatSetting field_3;
   @SerializedName("В")
   private final ActionSetting В_2479d68d;
   @SerializedName("4")
   private final Stopwatch field_4;
   private final EventListener<PlayerTickEvent> о;
   @SerializedName("к")
   private boolean к_at_56169efa;
   private int сЩ;
   private int су;
   private int с2;
   private int сф;
   private static final String ЯЫI = "InventorySort";
   private static final String ЯЫТ = "Сортирует инвентарь по выбранному шаблону";
   private static final String ЯЫО = "Задержка";
   private static final float ЯЫФ = 50.0F;
   private static final float ЯЫЙ = 250.0F;
   private static final float ЯЫк = 10.0F;
   private static final String ЯЫг = "Открыть панель";
   private static final String ЯЫ0 = "H";
   private static final String ЯЫх = "Инвентарь отсортирован";
   private static final String ЯЫУ = "I";
   private static final String ЯЫЗ = "Сначала расставь предметы в шаблоне";
   private static final String ЯЫЕ = "Тыква";
   private static final String ЯЫП = "Голова Джека";
   private static final String ЯЫ西 = "Шар";
   private static final String ЯЫд = "Головы";
   private static final String ЯЫБ = "Хорус";
   private static final String ЯЫС = "Силка";
   private static final String ЯЫз = "Зелье силы";
   private static final String ЯЫц = "Скорка";
   private static final String ЯЫя = "Зелье скорости";
   private static final String Яlс = "Огнестойка";
   private static final String ЯlВ = "Огнестойкость";
   private static final String ЯlЯ = "Чарка";
   private static final String ЯlХ = "Золотое яблоко";
   private static final String Яlе = "Обсидиан";
   private static final String ЯlА = "Кристалл Края";
   private static final String Яl4 = "Эндер-жемчуг";
   private static final String Яlп = "Фейерверк";
   private static final String ЯlД = "Тотем";
   private static final String ЯlЪ = "Щит";
   private static final String Яlт = "Элитры";
   private static final String Яlа = "Шлем";
   private static final String ЯlЮ = "Нагрудник";
   private static final String ЯlЧ = "Поножи";
   private static final String ЯlЛ = "Ботинки";
   private static final String Яl诶 = "Меч";
   private static final String Яl1 = "Кирка";
   private static final String ЯlЬ = "Топор";
   private static final String Яl衣 = "Лопата";
   private static final String Яlш = "Funtime";
   private static final String ЯlИ = "Эндер-жемчуг";
   private static final String Яlь = "Фейерверк";
   private static final String Яlж = "Хорус";
   private static final String Яl弟 = "Обсидиан";
   private static final String Яlб = "Кристалл Края";
   private static final String ЯlЫ = "Тотем";
   private static final String Яll = "Щит";
   private static final String Яl8 = "[★]";
   private static final String Яl5 = "[⚡]";
   private static final String Яlв = "xxx";
   private static final int Яlъ = Integer.MIN_VALUE;
   private static final String ЯlМ = "Шар";
   private static final String ЯlН = "Тыква";
   private static final String Яlн = "Голова Джека";
   private static final String ЯlЦ = "голова джека";
   private static final String Яlч = "джека";
   private static final String ЯlЭ = "\\s+";
   private static final String Яl必 = " ";
   private static final String Яlщ = "Силка";
   private static final String ЯlГ = "Скорка";
   private static final String Яlл = "Огнестойка";
   private static final String Яlэ = "Силка";
   private static final String Яlю = "Скорка";
   private static final String ЯlЩ = "Огнестойка";
   private static final String Яlу = "Силка";
   private static final String Яl2 = "Скорка";
   private static final String Яlф = "Огнестойка";

   private void Э(int var1, Identifier var2, String var3, int var4, InventorySort.ItemMatchKind var5) {
      if (var1 >= 0 && var1 < 41 && var2 != null && BuiltInRegistries.ITEM.containsKey(var2)) {
         Item var6 = BuiltInRegistries.ITEM.getValue(var2);
         ItemStack var7 = this.Э_a4db5d8e(var5, var3, new ItemStack(var6));
         String var8 = this.Э_388008bb(var5, var3, var7);
         if (!var7.isEmpty()) {
            this.В_d8cfd864[var1] = new InventorySort.SlotRule(var7.copyWithCount(1), var8, Math.max(1, var4), var5);
         }
      }
   }

   @Override
   public void onEnable() {
      this.ЭЩ();
   }

   public void method_3(int var1, int var2) {
      if (this.Э_bbeebd87(var1, var2)) {
         InventorySort.SlotRule var3 = this.В_d8cfd864[var2];
         this.В_d8cfd864[var2] = this.В_d8cfd864[var1];
         this.В_d8cfd864[var1] = var3;
         this.ЭН();
      }
   }

   public void Э(int var1, ItemStack var2, String var3, InventorySort.ItemMatchKind var4) {
      if (this.Э(var1, var2) && var4 != null) {
         this.ЭИ();
         String var5 = this.Э_388008bb(var4, var3, var2);
         this.В_d8cfd864[var1] = new InventorySort.SlotRule(this.Э_a4db5d8e(var4, var5, var2), var5, Math.max(1, var2.getCount()), var4);
         this.ЭН();
      }
   }

   public void Н(String var1) {
      this.В_3a79a21b.method_1(var1);
      this.Э3();
   }

   private void ЭО() {
      this.сЩ = -1;
      this.су = -1;
      this.сф = 0;
      this.с2 = 8;
   }

   public void ЭГ() {
      if (MINECRAFT_CLIENT.player != null) {
         this.ЭИ();
         if (!this.иэ()) {
            NotificationManager.р(ЯЫУ, ЯЫЗ, -1);
         } else {
            this.Эг();
            this.ЭО();
            this.к_at_56169efa = true;
            this.field_4.ВД();
         }
      }
   }

   private static FunTimeItemCatalog.CatalogEntry Э_eab85ef7(String var0) {
      return FunTimeItemCatalog.с0.stream().filter(var1 -> var1.name().equals(var0)).findFirst().orElse(null);
   }

   private void ЭЪ() {
      this.ЭО();
      this.к_at_56169efa = false;
      this.Ф_bbeebd87 = true;
      this.ЭИ();
      this.Э3();
   }

   public boolean иэ() {
      this.ЭИ();

      for (InventorySort.SlotRule var4 : this.В_d8cfd864) {
         if (var4 != null && !var4.stack().isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private static String З_388008bb(String var0) {
      return var0.replace(Яl8, "").replace(Яl5, "").replace(Яlв, "").trim();
   }

   public void ЭI() {
      if (this.В_3a79a21b.ЖЙ()) {
         this.ЭЪ();
      }
   }

   private int Э_a83dd0cc(int var1, int var2) {
      for (int var3 = 8; var3 >= 0; var3--) {
         if (var3 != var1 && var3 != var2) {
            InventorySort.SlotRule var4 = this.В_d8cfd864[var3];
            if (var4 == null && this.Э_a4db5d8e(var3).isEmpty()) {
               return var3;
            }
         }
      }

      for (int var5 = 8; var5 >= 0; var5--) {
         if (var5 != var1 && var5 != var2) {
            return var5;
         }
      }

      return 8;
   }

   private boolean Э(InventorySort.SlotRule var1, ItemStack var2) {
      if (var1 != null && var2 != null && !var2.isEmpty()) {
         return switch (var1.kind()) {
            case NORMAL -> var2.getItem() == var1.stack().getItem();
            case BALL -> var2.is(Items.PLAYER_HEAD) && !this.method_5(var2);
            case PUMPKIN -> this.method_5(var2);
            case POTION -> this.Э(var2, this.Э_7265b46b(var1.name()));
            case FUNTIME -> {
               FunTimeItemCatalog.CatalogEntry var3 = Э_eab85ef7(var1.name());
               yield var3 != null && var3.matches(var2);
            }
         };
      } else {
         return false;
      }
   }

   public void Э(int var1, ItemStack var2, String var3) {
      this.Э(var1, var2, var3, this.Э_ddb39f8f(var2));
   }

   public boolean Э_bbeebd87(int var1, int var2) {
      this.ЭИ();
      if (var1 < 0 || var1 >= 41 || var1 == var2 || this.В_d8cfd864[var1] == null) {
         return false;
      } else {
         return !this.Э(var2, this.В_d8cfd864[var1].stack()) ? false : this.В_d8cfd864[var2] == null || this.Э(var1, this.В_d8cfd864[var2].stack());
      }
   }

   public void Н(int var1, int var2) {
      if (var1 >= 0 && var1 < 41) {
         this.ЭИ();
         InventorySort.SlotRule var3 = this.В_d8cfd864[var1];
         if (var3 != null) {
            int var4 = Math.clamp(var3.count() + var2, 1, var3.stack().getMaxStackSize());
            if (var4 != var3.count()) {
               this.В_d8cfd864[var1] = new InventorySort.SlotRule(var3.stack(), var3.name(), var4, var3.kind());
               this.ЭН();
            }
         }
      }
   }

   private boolean к(int var1, ItemStack var2) {
      EquipmentSlot var3 = this.Э_65a2d573(var1);
      if (var3 == null) {
         return true;
      }

      if (var3 == EquipmentSlot.HEAD && var2.is(Items.PLAYER_HEAD)) {
         return true;
      }

      Equippable var4 = var2.get(DataComponents.EQUIPPABLE);
      return var4 != null && var4.slot() == var3;
   }

   private Holder<MobEffect> Э_7265b46b(String var1) {
      if (Яlу.equals(var1)) {
         return MobEffects.STRENGTH;
      } else if (Яl2.equals(var1)) {
         return MobEffects.SPEED;
      } else {
         return Яlф.equals(var1) ? MobEffects.FIRE_RESISTANCE : null;
      }
   }

   private void Эг() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
         ItemStack var1 = MINECRAFT_CLIENT.player.inventoryMenu.getCarried();
         if (!var1.isEmpty()) {
            int var2 = -1;

            for (int var3 = 9; var3 < 36; var3++) {
               if (this.Э_a4db5d8e(var3).isEmpty()) {
                  var2 = var3;
                  break;
               }
            }

            if (var2 < 0) {
               for (int var4 = 0; var4 < 9; var4++) {
                  if (this.Э_a4db5d8e(var4).isEmpty()) {
                     var2 = var4;
                     break;
                  }
               }
            }

            if (var2 >= 0) {
               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, Г(var2), 0, ContainerInput.PICKUP, MINECRAFT_CLIENT.player);
            }
         }
      }
   }

   public void З(BooleanSupplier var1) {
      this.Я_at_56169efa = var1;
   }

   public void Э5() {
      this.В_3a79a21b.method_8ф();
      this.ЭО();
      this.к_at_56169efa = false;
      this.Ф_bbeebd87 = true;
      this.ЭИ();
      this.Э3();
   }

   private void И(int var1, int var2) {
      int var3 = Г(var1);
      int var4 = Г(var2);
      if (var3 >= 0 && var4 >= 0 && var3 != var4) {
         int var5 = MINECRAFT_CLIENT.player.inventoryMenu.containerId;
         if (var1 >= 0 && var1 < 9) {
            this.Э(var5, var4, var1);
         } else if (var2 >= 0 && var2 < 9) {
            this.Э(var5, var3, var2);
         } else if (var1 == 40) {
            this.Э(var5, var4, 40);
         } else if (var2 == 40) {
            this.Э(var5, var3, 40);
         } else {
            this.сЩ = var1;
            this.су = var2;
            this.с2 = this.Э_a83dd0cc(var1, var2);
            this.сф = 0;
            this.Э诶();
         }
      }
   }

   public List<InventorySort.CatalogItem> Н() {
      if (this.Э_4f9d66f8() == InventoryPresetStore.ServerMode.FUNTIME) {
         return this.И_2571477d();
      }

      ArrayList var1 = new ArrayList();
      this.Э(var1, ЯЫЕ, ЯЫП, this.Э_a4db5d8e(), InventorySort.ItemMatchKind.PUMPKIN);
      this.Э(var1, ЯЫ西, ЯЫд, this.к(), InventorySort.ItemMatchKind.BALL);
      this.Э(var1, ЯЫБ, "", new ItemStack(Items.CHORUS_FRUIT), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯЫС, ЯЫз, this.Э(Potions.STRENGTH), InventorySort.ItemMatchKind.POTION);
      this.Э(var1, ЯЫц, ЯЫя, this.Э(Potions.SWIFTNESS), InventorySort.ItemMatchKind.POTION);
      this.Э(var1, Яlс, ЯlВ, this.Э(Potions.FIRE_RESISTANCE), InventorySort.ItemMatchKind.POTION);
      this.Э(var1, ЯlЯ, "", new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯlХ, "", new ItemStack(Items.GOLDEN_APPLE), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яlе, "", new ItemStack(Items.OBSIDIAN), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯlА, "", new ItemStack(Items.END_CRYSTAL), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яl4, "", new ItemStack(Items.ENDER_PEARL), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яlп, "", new ItemStack(Items.FIREWORK_ROCKET), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯlД, "", new ItemStack(Items.TOTEM_OF_UNDYING), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯlЪ, "", new ItemStack(Items.SHIELD), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яlт, "", new ItemStack(Items.ELYTRA), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яlа, "", new ItemStack(Items.NETHERITE_HELMET), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯlЮ, "", new ItemStack(Items.NETHERITE_CHESTPLATE), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯlЧ, "", new ItemStack(Items.NETHERITE_LEGGINGS), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯlЛ, "", new ItemStack(Items.NETHERITE_BOOTS), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яl诶, "", new ItemStack(Items.NETHERITE_SWORD), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яl1, "", new ItemStack(Items.NETHERITE_PICKAXE), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯlЬ, "", new ItemStack(Items.NETHERITE_AXE), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яl衣, "", new ItemStack(Items.NETHERITE_SHOVEL), InventorySort.ItemMatchKind.NORMAL);
      return var1;
   }

   private void Э诶() {
      int var1 = MINECRAFT_CLIENT.player.inventoryMenu.containerId;
      int var2 = Г(this.сЩ);
      int var3 = Г(this.су);
      if (this.сф == 0) {
         this.Э(var1, var3, this.с2);
      } else if (this.сф == 1) {
         this.Э(var1, var2, this.с2);
      } else {
         this.Э(var1, var3, this.с2);
      }

      this.сф++;
      if (this.сф > 2) {
         this.ЭО();
      }
   }

   private boolean method_5(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.PLAYER_HEAD)) {
         String var2 = this.Щ(var1.getHoverName().getString());
         return var2.contains(ЯlЦ) || var2.contains(Яlч);
      } else {
         return false;
      }
   }

   private static ItemStack Э(FunTimeItemCatalog.CatalogEntry var0) {
      ItemStack var1 = FunTimeItemMetadata.method_4(З_388008bb(var0.name()));
      return var1 == null ? var0.stack() : var1.copyWithCount(1);
   }

   private boolean Э(ItemStack var1, Holder<MobEffect> var2) {
      if (var2 != null && var1 != null && !var1.isEmpty()) {
         if (!var1.is(Items.POTION) && !var1.is(Items.SPLASH_POTION) && !var1.is(Items.LINGERING_POTION)) {
            return false;
         }

         PotionContents var3 = var1.get(DataComponents.POTION_CONTENTS);
         if (var3 == null) {
            return false;
         }

         for (MobEffectInstance var5 : var3.getAllEffects()) {
            if (var5.getEffect().equals(var2)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private ItemStack Э(Holder<Potion> var1) {
      return PotionContents.createItemStack(Items.SPLASH_POTION, var1);
   }

   private List<InventorySort.CatalogItem> И_2571477d() {
      ArrayList var1 = new ArrayList();

      for (FunTimeItemCatalog.CatalogEntry var3 : FunTimeItemCatalog.с0) {
         this.Э(var1, var3.name(), Яlш, Э(var3), InventorySort.ItemMatchKind.FUNTIME);
      }

      this.Э(var1, ЯlИ, "", new ItemStack(Items.ENDER_PEARL), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яlь, "", new ItemStack(Items.FIREWORK_ROCKET), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яlж, "", new ItemStack(Items.CHORUS_FRUIT), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яl弟, "", new ItemStack(Items.OBSIDIAN), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яlб, "", new ItemStack(Items.END_CRYSTAL), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, ЯlЫ, "", new ItemStack(Items.TOTEM_OF_UNDYING), InventorySort.ItemMatchKind.NORMAL);
      this.Э(var1, Яll, "", new ItemStack(Items.SHIELD), InventorySort.ItemMatchKind.NORMAL);
      return var1;
   }

   private InventorySort.SlotRule Э_461faf9f(ItemStack var1) {
      if (this.Э_4f9d66f8() == InventoryPresetStore.ServerMode.FUNTIME) {
         FunTimeItemCatalog.CatalogEntry var4 = FunTimeItemCatalog.Т(var1);
         return var4 != null
            ? new InventorySort.SlotRule(Э(var4), var4.name(), var1.getCount(), InventorySort.ItemMatchKind.FUNTIME)
            : new InventorySort.SlotRule(
               var1.copyWithCount(1), var1.getHoverName().getString(), var1.getCount(), InventorySort.ItemMatchKind.NORMAL
            );
      } else {
         InventorySort.ItemMatchKind var2 = this.Э_ddb39f8f(var1);
         String var3 = this.Э_388008bb(var2, var1.getHoverName().getString(), var1);
         return new InventorySort.SlotRule(this.Э_a4db5d8e(var2, var3, var1), var3, var1.getCount(), var2);
      }
   }

   public boolean Э(int var1, ItemStack var2) {
      return var1 >= 0 && var1 < 41 && var2 != null && !var2.isEmpty() && this.к(var1, var2);
   }

   private ItemStack Э_a4db5d8e(int var1) {
      if (MINECRAFT_CLIENT.player == null) {
         return ItemStack.EMPTY;
      }

      if (var1 >= 0 && var1 < 36) {
         return MINECRAFT_CLIENT.player.getInventory().getItem(var1);
      }

      return switch (var1) {
         case 36 -> MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.FEET);
         case 37 -> MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.LEGS);
         case 38 -> MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST);
         case 39 -> MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.HEAD);
         case 40 -> MINECRAFT_CLIENT.player.getOffhandItem();
         default -> ItemStack.EMPTY;
      };
   }

   public boolean ил() {
      return this.В_3a79a21b.Жэ();
   }

   private ItemStack к() {
      ItemStack var1 = this.Э(false);
      return var1.isEmpty() ? new ItemStack(Items.PLAYER_HEAD) : var1;
   }

   private EquipmentSlot Э_65a2d573(int var1) {
      return switch (var1) {
         case 36 -> EquipmentSlot.FEET;
         case 37 -> EquipmentSlot.LEGS;
         case 38 -> EquipmentSlot.CHEST;
         case 39 -> EquipmentSlot.HEAD;
         default -> null;
      };
   }

   private void Э(List<InventorySort.CatalogItem> var1, String var2, String var3, ItemStack var4, InventorySort.ItemMatchKind var5) {
      if (var4 != null && !var4.isEmpty()) {
         var1.add(new InventorySort.CatalogItem(var2, var3, var4.copyWithCount(1), var5));
      }
   }

   public void И(String var1) {
      if (this.В_3a79a21b.ч(var1)) {
         this.ЭО();
         this.к_at_56169efa = false;
         this.Ф_bbeebd87 = true;
         this.ЭИ();
         this.Э3();
      }
   }

   public boolean и7() {
      return this.В_3a79a21b.ЖР();
   }

   private void О(PlayerTickEvent var1) {
      if (this.к_at_56169efa) {
         if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null || MINECRAFT_CLIENT.gameMode == null) {
            this.к_at_56169efa = false;
         } else if (!(MINECRAFT_CLIENT.gui.screen() instanceof InventoryPresetScreen)) {
            if (!(MINECRAFT_CLIENT.player.containerMenu instanceof InventoryMenu)) {
               this.к_at_56169efa = false;
            } else if (this.field_4.method_0(this.field_3.getValue().longValue())) {
               if (!MINECRAFT_CLIENT.player.inventoryMenu.getCarried().isEmpty()) {
                  this.Эг();
                  this.field_4.ВД();
               } else {
                  if (!this.и_()) {
                     this.Эг();
                     this.ЭО();
                     this.к_at_56169efa = false;
                     NotificationManager.р(ЯЫ0, ЯЫх, -1);
                  }
               }
            }
         }
      }
   }

   public boolean иЖ() {
      return this.Й_bbeebd87;
   }

   private InventorySort.ItemMatchKind Э_ddb39f8f(ItemStack var1) {
      if (this.method_5(var1)) {
         return InventorySort.ItemMatchKind.PUMPKIN;
      } else if (var1.is(Items.PLAYER_HEAD)) {
         return InventorySort.ItemMatchKind.BALL;
      } else {
         return this.З(var1) != null ? InventorySort.ItemMatchKind.POTION : InventorySort.ItemMatchKind.NORMAL;
      }
   }

   private ItemStack Э_a4db5d8e(InventorySort.ItemMatchKind var1, String var2, ItemStack var3) {
      return switch (var1) {
         case NORMAL -> var3.copyWithCount(1);
         case BALL -> this.к();
         case PUMPKIN -> this.Э_a4db5d8e();
         case POTION -> this.З_a4db5d8e(var2);
         case FUNTIME -> {
            FunTimeItemCatalog.CatalogEntry var4 = Э_eab85ef7(var2);
            yield var4 == null ? var3.copyWithCount(1) : Э(var4);
         }
      };
   }

   public void Э(InventoryPresetStore.ServerMode var1) {
      if (var1 != this.В_3a79a21b.П()) {
         this.В_3a79a21b.П(var1);
         this.Э3();
      }
   }

   public void Э(InventoryPresetStore var1) {
      this.В_3a79a21b = var1;
      this.Ф_bbeebd87 = true;
      this.е_at_56169efa = this.В_3a79a21b.Ф();
      this.Й_bbeebd87 = true;
   }

   public void Эо() {
      if (MINECRAFT_CLIENT.player != null) {
         this.ЭИ();

         for (int var1 = 0; var1 < 41; var1++) {
            ItemStack var2 = this.Э_a4db5d8e(var1);
            this.В_d8cfd864[var1] = var2.isEmpty() ? null : this.Э_461faf9f(var2);
         }

         this.ЭН();
      }
   }

   private void ЭЩ() {
      if (MINECRAFT_CLIENT.player != null) {
         if (!this.isEnabled()) {
            this.enable();
         } else {
            this.ЭО();
            this.к_at_56169efa = false;
            MINECRAFT_CLIENT.gui.setScreen(new InventoryPresetScreen(this));
         }
      }
   }

   public InventoryPresetStore.ServerMode Э_4f9d66f8() {
      return this.В_3a79a21b.П();
   }

   @Override
   public void onDisable() {
      this.ЭО();
      this.к_at_56169efa = false;
      this.Эг();
   }

   private ItemStack Э(boolean var1) {
      if (MINECRAFT_CLIENT.player == null) {
         return ItemStack.EMPTY;
      }

      ItemStack var2 = MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.HEAD);
      if (this.к(var2, var1)) {
         return var2.copyWithCount(1);
      }

      ItemStack var3 = MINECRAFT_CLIENT.player.getOffhandItem();
      if (this.к(var3, var1)) {
         return var3.copyWithCount(1);
      }

      for (int var4 = 0; var4 < 36; var4++) {
         ItemStack var5 = MINECRAFT_CLIENT.player.getInventory().getItem(var4);
         if (this.к(var5, var1)) {
            return var5.copyWithCount(1);
         }
      }

      return ItemStack.EMPTY;
   }

   public void Д(int var1) {
      this.ЭИ();
      if (var1 >= 0 && var1 < 41 && this.В_d8cfd864[var1] != null) {
         this.В_d8cfd864[var1] = null;
         this.ЭН();
      }
   }

   public void Эф() {
      this.ЭИ();

      for (int var1 = 0; var1 < this.В_d8cfd864.length; var1++) {
         this.В_d8cfd864[var1] = null;
      }

      this.ЭН();
   }

   private ItemStack З_a4db5d8e(String var1) {
      if (Яlщ.equals(var1)) {
         return this.Э(Potions.STRENGTH);
      } else if (ЯlГ.equals(var1)) {
         return this.Э(Potions.SWIFTNESS);
      } else {
         return Яlл.equals(var1) ? this.Э(Potions.FIRE_RESISTANCE) : this.Э(Potions.STRENGTH);
      }
   }

   private int Э(int var1, InventorySort.SlotRule var2) {
      int var3 = -1;
      int var4 = Яlъ;

      for (int var5 = 0; var5 < 41; var5++) {
         if (var5 != var1) {
            ItemStack var6 = this.Э_a4db5d8e(var5);
            if (this.Э(var2, var6) && this.к(var1, var6)) {
               InventorySort.SlotRule var7 = this.В_d8cfd864[var5];
               if (var7 == null || !this.Э(var7, var6) || var7.count() > 1 && var6.getCount() != var7.count()) {
                  int var8 = var7 == null ? 200 : 80;
                  if (var5 >= 0 && var5 < 9) {
                     var8 += 25;
                  }

                  var8 -= Math.abs(var6.getCount() - var2.count());
                  if (var8 > var4) {
                     var4 = var8;
                     var3 = var5;
                  }
               }
            }
         }
      }

      return var3;
   }

   private ItemStack Э_a4db5d8e() {
      ItemStack var1 = this.Э(true);
      if (!var1.isEmpty()) {
         return var1;
      }

      ItemStack var2 = new ItemStack(Items.PLAYER_HEAD);
      var2.set(DataComponents.CUSTOM_NAME, Component.literal(Яlн));
      return var2;
   }

   private void Э3() {
      this.е_at_56169efa = this.В_3a79a21b.Ф();
      this.Й_bbeebd87 = this.Я_at_56169efa.getAsBoolean();
   }

   public void Эl() {
      this.Э3();
   }

   public void method_3(String var1) {
      this.В_3a79a21b.ш(var1);
      this.Э3();
   }

   private boolean и_() {
      if (this.сЩ >= 0) {
         this.Э诶();
         this.field_4.ВД();
         return true;
      }

      for (int var4 : Х_at_56169efa) {
         InventorySort.SlotRule var5 = this.В_d8cfd864[var4];
         if (var5 != null && !var5.stack().isEmpty()) {
            ItemStack var6 = this.Э_a4db5d8e(var4);
            if (!this.Э(var5, var6) || var5.count() > 1 && var6.getCount() != var5.count()) {
               int var7 = this.Э(var4, var5);
               if (var7 >= 0) {
                  this.И(var4, var7);
                  this.field_4.ВД();
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void ЭН() {
      ArrayList var1 = new ArrayList();

      for (int var2 = 0; var2 < this.В_d8cfd864.length; var2++) {
         InventorySort.SlotRule var3 = this.В_d8cfd864[var2];
         if (var3 != null && !var3.stack().isEmpty()) {
            var1.add(
               new InventoryPresetStore.SlotLayout(
                  var2, BuiltInRegistries.ITEM.getKey(var3.stack().getItem()).toString(), var3.name(), var3.count(), var3.kind().name()
               )
            );
         }
      }

      this.В_3a79a21b.Ж(var1);
      this.Э3();
   }

   public void ЭЯ() {
      if (this.В_3a79a21b.Жю()) {
         this.ЭЪ();
      }
   }

   private void Э(int var1, int var2, int var3) {
      MINECRAFT_CLIENT.gameMode.handleContainerInput(var1, var2, var3, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
   }

   private void ЭИ() {
      if (this.Ф_bbeebd87 && MINECRAFT_CLIENT.player != null) {
         Arrays.fill(this.В_d8cfd864, null);

         for (InventoryPresetStore.SlotLayout var2 : this.В_3a79a21b.Ы()) {
            this.Э(var2.index(), Identifier.tryParse(var2.item()), var2.name(), var2.count(), InventorySort.ItemMatchKind.valueOf(var2.kind()));
         }

         this.Ф_bbeebd87 = false;
      }
   }

   public List<String> method_3() {
      return this.В_3a79a21b.ъ();
   }

   private static int Г(int var0) {
      if (var0 >= 0 && var0 < 9) {
         return 36 + var0;
      }

      if (var0 >= 9 && var0 < 36) {
         return var0;
      }

      return switch (var0) {
         case 36 -> 8;
         case 37 -> 7;
         case 38 -> 6;
         case 39 -> 5;
         case 40 -> 45;
         default -> -1;
      };
   }

   public InventorySort() {
      super(ЯЫI, ЯЫТ, ModuleCategory.MISC);
      this.field_3 = new FloatSetting(ЯЫО, ЯЫФ, 0.0F, ЯЫЙ).withStep(ЯЫк);
      this.В_2479d68d = new ActionSetting(ЯЫг).onClick(this::ЭЩ).showOtherSettings();
      this.field_4 = new Stopwatch();
      this.о = this::О;
      this.сЩ = -1;
      this.су = -1;
      this.с2 = 8;
      this.addSettings(this.field_3, this.В_2479d68d);
   }

   private String Э_388008bb(InventorySort.ItemMatchKind var1, String var2, ItemStack var3) {
      return switch (var1) {
         case NORMAL, FUNTIME -> var2 != null && !var2.isBlank() ? var2 : var3.getHoverName().getString();
         case BALL -> ЯlМ;
         case PUMPKIN -> ЯlН;
         case POTION -> {
            String var4 = this.З(var3);
            yield var4 != null ? var4 : var2;
         }
      };
   }

   private String Щ(String var1) {
      return StringUtil.stripColor(var1 == null ? "" : var1).toLowerCase(Locale.ROOT).replaceAll(ЯlЭ, Яl必).trim();
   }

   private boolean к(ItemStack var1, boolean var2) {
      return var1.is(Items.PLAYER_HEAD) && this.method_5(var1) == var2;
   }

   public JsonObject Щ() {
      return this.е_at_56169efa.deepCopy();
   }

   public String иБ() {
      return this.В_3a79a21b.ПЭ();
   }

   private static int[] Э_28f5b604() {
      int[] var0 = new int[41];
      int var1 = 0;
      var0[var1++] = 39;
      var0[var1++] = 38;
      var0[var1++] = 37;
      var0[var1++] = 36;
      var0[var1++] = 40;
      int var2 = 0;

      while (var2 < 9) {
         var0[var1++] = var2++;
      }

      var2 = 9;

      while (var2 < 36) {
         var0[var1++] = var2++;
      }

      return var0;
   }

   public InventorySort.SlotRule[] Э_d8cfd864() {
      this.ЭИ();
      return this.В_d8cfd864;
   }

   private String З(ItemStack var1) {
      if (this.Э(var1, MobEffects.STRENGTH)) {
         return Яlэ;
      } else if (this.Э(var1, MobEffects.SPEED)) {
         return Яlю;
      } else {
         return this.Э(var1, MobEffects.FIRE_RESISTANCE) ? ЯlЩ : null;
      }
   }

   public enum ItemMatchKind {
      NORMAL,
      BALL,
      PUMPKIN,
      POTION,
      FUNTIME;
   }

   public record CatalogItem(String name, String subtitle, ItemStack stack, InventorySort.ItemMatchKind kind) {
      public String label() {
         return this.kind == InventorySort.ItemMatchKind.FUNTIME ? InventorySort.З_388008bb(this.name) : this.name;
      }
   }

   public record SlotRule(ItemStack stack, String name, int count, InventorySort.ItemMatchKind kind) {
      public String label() {
         return this.kind == InventorySort.ItemMatchKind.FUNTIME ? InventorySort.З_388008bb(this.name) : this.name;
      }
   }
}
