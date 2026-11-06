package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.render.pipeline.InventoryActions;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.AutoSwapScreen;
import sg.settings.ChoiceOption;
import sg.modules.impl.init.DeferredMovementActions;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionCoordinator;
import sg.modules.impl.init.InventoryActionQueue;
import sg.settings.KeybindSetting;
import sg.modules.impl.init.NotificationManager;
import sg.settings.SingleChoiceSetting;
import sg.settings.TextSetting;

public class AutoSwap extends ClientModule {
   private static final String П = "minecraft:player_head@count=1#746f5fd026d01d8bf903146741d073afd590a109d4e459a9019a1d5f76571356";
   private final @com.google.gson.annotations.SerializedName("Ч")  SingleChoiceSetting modeSetting;
   private final @com.google.gson.annotations.SerializedName("Л")  SingleChoiceSetting firstItemSetting;
   private final @com.google.gson.annotations.SerializedName("诶")  SingleChoiceSetting secondItemSetting;
   @SerializedName("1")
   private final SingleChoiceSetting thirdItemSetting;
   @SerializedName("В")
   private final TextSetting cerberusItemSignatureSetting;
   @SerializedName("Я")
   private final TextSetting cerberusDataSignatureSetting;
   private final @com.google.gson.annotations.SerializedName("O")  BooleanSetting ignoreNormalTotemsSetting;
   @SerializedName("в")
   private final KeybindSetting swapKeybindSetting;
   private final @com.google.gson.annotations.SerializedName("ъ")  KeybindSetting cerberusSwapKeybindSetting;
   @SerializedName("В")
   private AutoSwap.Selection nextSelection;
   private @com.google.gson.annotations.SerializedName("ц")  boolean swapRequested;
   @SerializedName("В")
   private AutoSwapScreen activeScreen;
   @SerializedName("Я")
   private final InventoryActionQueue actionQueue;
   @SerializedName("Я")
   private final InventoryActionCoordinator actionCoordinator;
   private @com.google.gson.annotations.SerializedName("сО")  int pendingSwapSlot;
   @SerializedName("В")
   private final ItemStack[] presetItemStacks;
   @SerializedName("е")
   private final String[] presetItemNames;
   private final @com.google.gson.annotations.SerializedName("О")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("Ф")  EventListener<InputEvent> inputListener;
   private final @com.google.gson.annotations.SerializedName("Й")  EventListener<HudRenderEvent> hudRenderListener;
   private static final String ЯЩ必 = "AutoSwap";
   private static final String ЯЩщ = "Автоматически перемещает предметы между главной и второй руками";
   private static final String ЯЩГ = "Режим";
   private static final String ЯЩл = "Обычный";
   private static final String ЯЩэ = "Тройной";
   private static final String ЯЩю = "Первый предмет";
   private static final String ЯЩЩ = "Сфера, Шар";
   private static final String ЯЩу = "Талисман";
   private static final String ЯЩ2 = "Щит";
   private static final String ЯЩф = "Золотое яблоко";
   private static final String ЯЩи = "Второй предмет";
   private static final String ЯЩ9 = "Сфера, Шар";
   private static final String ЯЩо = "Талисман";
   private static final String ЯЩO = "Щит";
   private static final String ЯЩ7 = "Золотое яблоко";
   private static final String ЯЩР = "Третий предмет";
   private static final String ЯЩ3 = "Сфера, Шар";
   private static final String ЯЩр = "Талисман";
   private static final String ЯЩ6 = "Щит";
   private static final String ЯЩ_ = "Золотое яблоко";
   private static final String ЯЩШ = "Cerberus Item Signature";
   private static final String ЯЩы = "minecraft:player_head@count=1#746f5fd026d01d8bf903146741d073afd590a109d4e459a9019a1d5f76571356";
   private static final String ЯЩй = "Cerberus Item Data Signature";
   private static final String ЯЩЖ = "Игнорировать обычные тотемы";
   private static final String ЯЩм = "Клавиша свапа";
   private static final String ЯЩК = "Свап Сферы Цербера";
   private static final String ЯЩI = "Тройной";
   private static final String ЯЩТ = "Легитный";
   private static final String ЯЩО = "Рейджовый";
   private static final String ЯЩФ = "swap";
   private static final String ЯЩЙ = "M";
   private static final String ЯЩк = "В левой руке";
   private static final String ЯЩг = "Сфера, Шар";
   private static final String ЯЩ0 = "Талисман";
   private static final String ЯЩх = "Щит";
   private static final String ЯЩУ = "Любая еда";
   private static final String ЯЩЗ = "Золотое яблоко";
   private static final String ЯЩЕ = "Сфера, Шар";
   private static final String ЯЩП = "Талисман";
   private static final String ЯЩ西 = "Щит";
   private static final String ЯЩд = "Любая еда";
   private static final String ЯЩБ = "Золотое яблоко";
   private static final String ЯЩС = "Сфера, Шар";
   private static final String ЯЩз = "Талисман";
   private static final String ЯЩц = "Щит";
   private static final String ЯЩя = "Любая еда";
   private static final String Яус = "Золотое яблоко";
   private static final String ЯуВ = "\\s+";
   private static final String ЯуЯ = " ";
   private static final String ЯуХ = "сфера цербера";
   private static final String Яуе = "cerberus sphere";
   private static final String ЯуА = "SHA-256";
   private static final String Яу4 = "Талисман";
   private static final String Яуп = "Талисман";
   private static final String ЯуД = "Тройной";
   private static final String ЯуЪ = "Талисман";
   private static final String Яут = "Тройной";

   private boolean method_6(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && var1.is(Items.TOTEM_OF_UNDYING) && !this.method_3(var1);
   }

   private int Э_a83dd0cc(ItemStack var1, String var2) {
      if (var1 != null && !var1.isEmpty() && MINECRAFT_CLIENT.player != null && !this.I(var1)) {
         NonNullList var3 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems();

         for (int var4 = 0; var4 < var3.size(); var4++) {
            ItemStack var5 = (ItemStack)var3.get(var4);
            if (!this.I(var5) && (ItemStack.isSameItemSameComponents(var5, var1) || !var2.isBlank() && var5.getHoverName().getString().equalsIgnoreCase(var2))) {
               return this.о(var4);
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private boolean method_4И() {
      return this.cerberusSwapKeybindSetting.getValue() >= 0;
   }

   private int Х() {
      if (MINECRAFT_CLIENT.player == null) {
         return -1;
      }

      for (int var1 = 0; var1 < 36; var1++) {
         ItemStack var2 = MINECRAFT_CLIENT.player.getInventory().getItem(var1);
         if (this.С(var2)) {
            this.ц(var2);
            return this.о(var1);
         }
      }

      for (int var3 = 0; var3 < 36; var3++) {
         ItemStack var4 = MINECRAFT_CLIENT.player.getInventory().getItem(var3);
         if (this.у(var4)) {
            return this.о(var3);
         }
      }

      return -1;
   }

   private Item ц() {
      return switch (this.firstItemSetting.getSelectedOption().getName()) {
         case ЯЩг -> Items.PLAYER_HEAD;
         case ЯЩ0 -> Items.TOTEM_OF_UNDYING;
         case ЯЩх -> Items.SHIELD;
         case ЯЩУ -> null;
         case ЯЩЗ -> Items.GOLDEN_APPLE;
         default -> Items.AIR;
      };
   }

   private String Г(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && MINECRAFT_CLIENT.level != null) {
         ItemStack var2 = var1.copy();
         var2.setCount(1);
         Tag var3 = ItemStack.CODEC.encodeStart(MINECRAFT_CLIENT.level.registryAccess().createSerializationContext(NbtOps.INSTANCE), var2).result().orElse(null);
         if (var3 == null) {
            return "";
         }

         String var4 = new SnbtPrinterTagVisitor().visit(var3);
         String var5 = BuiltInRegistries.ITEM.getKey(var2.getItem()).toString();

         try {
            byte[] var6 = MessageDigest.getInstance(ЯуА).digest(var4.getBytes(StandardCharsets.UTF_8));
            return var5 + "@count=1#" + HexFormat.of().formatHex(var6);
         } catch (NoSuchAlgorithmException var7) {
            return "";
         }
      } else {
         return "";
      }
   }

   private boolean Э_bbeebd87(ItemStack var1, String var2) {
      if (MINECRAFT_CLIENT.player != null && var1 != null && !var1.isEmpty() && !this.I(var1)) {
         ItemStack var3 = MINECRAFT_CLIENT.player.getOffhandItem();
         return !var3.isEmpty() && !this.I(var3)
            ? ItemStack.isSameItemSameComponents(var3, var1) || !var2.isBlank() && var3.getHoverName().getString().equalsIgnoreCase(var2)
            : false;
      } else {
         return false;
      }
   }

   private boolean С(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.PLAYER_HEAD)) {
         String var2 = StringUtil.stripColor(var1.getHoverName().getString())
            .toLowerCase(Locale.ROOT)
            .replace('ё', 'е')
            .replace(' ', ' ')
            .trim()
            .replaceAll(ЯуВ, ЯуЯ);
         return var2.contains(ЯуХ) || var2.contains(Яуе);
      } else {
         return false;
      }
   }

   public boolean й(int var1, ItemStack var2) {
      if (var1 >= 0 && var1 < this.presetItemStacks.length && var2 != null && !var2.isEmpty() && !this.I(var2)) {
         ItemStack var3 = var2.copy();
         var3.setCount(1);
         this.presetItemStacks[var1] = var3;
         this.presetItemNames[var1] = var2.getHoverName().getString();
         return true;
      } else {
         return false;
      }
   }

   private void Эщ() {
      int var1 = this.pendingSwapSlot;
      this.actionQueue.з(ЯЩФ, () -> this.actionQueue.Э_de5a6f78(40, var1), this.actionQueue::зl);
      this.pendingSwapSlot = -1;
   }

   public boolean У(int var1) {
      return var1 >= 0
         && var1 < this.presetItemStacks.length
         && !this.presetItemStacks[var1].isEmpty()
         && !this.I(this.presetItemStacks[var1])
         && (this.Э_bbeebd87(this.presetItemStacks[var1], this.presetItemNames[var1]) || this.Э_a83dd0cc(this.presetItemStacks[var1], this.presetItemNames[var1]) != -1);
   }

   private Item й_50cb85c5() {
      return switch (this.secondItemSetting.getSelectedOption().getName()) {
         case ЯЩЕ -> Items.PLAYER_HEAD;
         case ЯЩП -> Items.TOTEM_OF_UNDYING;
         case ЯЩ西 -> Items.SHIELD;
         case ЯЩд -> null;
         case ЯЩБ -> Items.GOLDEN_APPLE;
         default -> Items.AIR;
      };
   }

   public void с_de5a6f78(int var1) {
      if (var1 >= 0 && var1 < this.presetItemStacks.length && !this.presetItemStacks[var1].isEmpty() && !this.I(this.presetItemStacks[var1])) {
         if (!this.Э_bbeebd87(this.presetItemStacks[var1], this.presetItemNames[var1])) {
            ItemStack var2 = this.presetItemStacks[var1].copy();
            String var3 = this.presetItemNames[var1];
            if (this.Э_a83dd0cc(var2, var3) != -1) {
               boolean[] var4 = new boolean[]{false};
               int var5 = RoseClient.getInstance().getServices().getModules().getGuiMove().И_989fed9a().getValue().intValue();
               DeferredMovementActions.Ф(() -> {
                  if (!this.Э_bbeebd87(var2, var3)) {
                     int var4x = this.Э_a83dd0cc(var2, var3);
                     if (var4x != -1) {
                        InventoryActions.method_7(40, var4x);
                        var4[0] = true;
                     }
                  }
               }, () -> {
                  if (var4[0] && MINECRAFT_CLIENT.player != null) {
                     this.Эы();
                  }
               }, var5);
            }
         }
      }
   }

   private int Ъ_a83dd0cc() {
      int var1 = -1;
      int var2 = -1;

      for (int var3 = 0; var3 < 36; var3++) {
         ItemStack var4 = MINECRAFT_CLIENT.player.getInventory().getItem(var3);
         if (!this.I(var4)) {
            FoodProperties var5 = var4.get(DataComponents.FOOD);
            if (var5 != null && var5.nutrition() > var2) {
               var2 = var5.nutrition();
               var1 = this.о(var3);
            }
         }
      }

      return var1;
   }

   public void л(InputEvent var1) {
      if (var1.getInputCode() == this.cerberusSwapKeybindSetting.getValue() && this.cerberusSwapKeybindSetting.getValue() >= 0) {
         if (var1.getAction() == 1 && MINECRAFT_CLIENT.gui.screen() == null) {
            int var4 = this.Х();
            if (var4 != -1) {
               this.pendingSwapSlot = var4;
            }
         }
      } else if (var1.getInputCode() == this.swapKeybindSetting.getValue() && this.swapKeybindSetting.getValue() >= 0) {
         if (!this.modeSetting.isSelected(ЯЩI)) {
            if (MINECRAFT_CLIENT.gui.screen() == null && var1.getAction() == 1) {
               this.swapRequested = true;
            }
         } else {
            if (var1.getAction() == 1 && MINECRAFT_CLIENT.gui.screen() == null) {
               MINECRAFT_CLIENT.gui.setScreen(new AutoSwapScreen(this, this.swapKeybindSetting.getValue()));
            } else if (var1.getAction() == 0 && MINECRAFT_CLIENT.gui.screen() instanceof AutoSwapScreen var2) {
               var2.finish(true);
            }
         }
      }
   }

   public void method_1(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && this.cerberusDataSignatureSetting.getValue().isBlank()) {
         for (ItemStack var3 : MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems()) {
            if (this.С(var3)) {
               this.ц(var3);
               if (!this.cerberusDataSignatureSetting.getValue().isBlank()) {
                  break;
               }
            }
         }

         if (this.С(MINECRAFT_CLIENT.player.getOffhandItem())) {
            this.ц(MINECRAFT_CLIENT.player.getOffhandItem());
         }
      }

      if (MINECRAFT_CLIENT.gui.screen() == null) {
         if (this.swapRequested) {
            this.Эс();
         }

         if (this.pendingSwapSlot != -1) {
            if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(ЯЩТ)) {
               this.Эщ();
            } else if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(ЯЩО)) {
               this.ЭТ();
            }
         }
      }
   }

   private int о(int var1) {
      return var1 < 9 ? var1 + 36 : var1;
   }

   private Item У_50cb85c5() {
      return this.Э(this.thirdItemSetting);
   }

   private void ЭТ() {
      this.actionQueue.Э_de5a6f78(40, this.pendingSwapSlot);
      this.actionQueue.зl();
      this.pendingSwapSlot = -1;
   }

   private String У(ItemStack var1) {
      ItemStack var2 = var1.copy();
      var2.setCount(1);
      var2.remove(DataComponents.CUSTOM_NAME);
      var2.remove(DataComponents.ITEM_NAME);
      var2.remove(DataComponents.REPAIR_COST);
      ItemStack var3 = new ItemStack(Items.PLAYER_HEAD);
      var3.remove(DataComponents.ITEM_NAME);
      return ItemStack.isSameItemSameComponents(var2, var3) ? "" : this.Г(var2);
   }

   private int ч_a83dd0cc() {
      for (int var1 = 0; var1 < 36; var1++) {
         ItemStack var2 = MINECRAFT_CLIENT.player.getInventory().getItem(var1);
         if (!this.I(var2) && this.method_6(var2)) {
            return this.о(var1);
         }
      }

      return -1;
   }

   private void С(HudRenderEvent var1) {
      if (this.activeScreen != null && var1.getGraphics() != null) {
         if (this.activeScreen.renderClosing(var1.getGraphics())) {
            this.activeScreen = null;
         }
      }
   }

   public AutoSwapScreen Э_e281f645() {
      return this.activeScreen;
   }

   public ItemStack ц(int var1) {
      if (var1 >= 0 && var1 < this.presetItemStacks.length) {
         ItemStack var2 = this.presetItemStacks[var1];
         return this.I(var2) ? ItemStack.EMPTY : var2;
      } else {
         return ItemStack.EMPTY;
      }
   }

   private boolean method_3(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.TOTEM_OF_UNDYING)) {
         String var2 = StringUtil.stripColor(var1.getHoverName().getString()).trim();
         String var3 = StringUtil.stripColor(Component.translatable(Items.TOTEM_OF_UNDYING.getDescriptionId()).getString()).trim();
         return var2.equalsIgnoreCase(var3);
      } else {
         return false;
      }
   }

   @Override
   public void onDisable() {
      if (MINECRAFT_CLIENT.gui.screen() instanceof AutoSwapScreen var1) {
         var1.finish(false);
      }

      this.activeScreen = null;
      this.swapRequested = false;
      this.pendingSwapSlot = -1;
   }

   private int а(Item var1) {
      if (var1 == Items.TOTEM_OF_UNDYING && this.ignoreNormalTotemsSetting.getValue()) {
         return this.ч_a83dd0cc();
      } else {
         return var1 == null ? this.Ъ_a83dd0cc() : this.С(var1);
      }
   }

   private void Эы() {
      ItemStack var1 = MINECRAFT_CLIENT.player.getOffhandItem();
      NotificationManager.р(ЯЩЙ, var1.getHoverName(), ЯЩк, -1, var1);
   }

   private Item Э(SingleChoiceSetting var1) {
      return switch (var1.getSelectedOption().getName()) {
         case ЯЩС -> Items.PLAYER_HEAD;
         case ЯЩз -> Items.TOTEM_OF_UNDYING;
         case ЯЩц -> Items.SHIELD;
         case ЯЩя -> null;
         case Яус -> Items.GOLDEN_APPLE;
         default -> Items.AIR;
      };
   }

   private void ц(ItemStack var1) {
      String var2 = this.У(var1);
      if (!var2.isEmpty()) {
         this.cerberusDataSignatureSetting.setValue(var2);
      }
   }

   private int С(Item var1) {
      for (int var2 = 0; var2 < 36; var2++) {
         ItemStack var3 = MINECRAFT_CLIENT.player.getInventory().getItem(var2);
         if (!this.I(var3) && var3.getItem() == var1) {
            return this.о(var2);
         }
      }

      return -1;
   }

   public AutoSwap() {
      super(ЯЩ必, ЯЩщ, ModuleCategory.COMBAT);
      this.modeSetting = new SingleChoiceSetting(ЯЩГ, new ChoiceOption(ЯЩл), new ChoiceOption(ЯЩэ));
      this.firstItemSetting = new SingleChoiceSetting(ЯЩю, new ChoiceOption(ЯЩЩ), new ChoiceOption(ЯЩу), new ChoiceOption(ЯЩ2), new ChoiceOption(ЯЩф));
      this.secondItemSetting = new SingleChoiceSetting(ЯЩи, new ChoiceOption(ЯЩ9), new ChoiceOption(ЯЩо), new ChoiceOption(ЯЩO), new ChoiceOption(ЯЩ7));
      this.thirdItemSetting = new SingleChoiceSetting(ЯЩР, new ChoiceOption(ЯЩ3), new ChoiceOption(ЯЩр), new ChoiceOption(ЯЩ6), new ChoiceOption(ЯЩ_))
         .visibleWhen(() -> this.modeSetting.isSelected(Яут));
      this.cerberusItemSignatureSetting = new TextSetting(ЯЩШ, ЯЩы).visibleWhen(() -> false);
      this.cerberusDataSignatureSetting = new TextSetting(ЯЩй, "").visibleWhen(() -> false);
      this.ignoreNormalTotemsSetting = new BooleanSetting(ЯЩЖ, true).visibleWhen(() -> this.firstItemSetting.isSelected(Яу4) || this.secondItemSetting.isSelected(Яуп) || this.modeSetting.isSelected(ЯуД) && this.thirdItemSetting.isSelected(ЯуЪ));
      this.swapKeybindSetting = new KeybindSetting(ЯЩм, -1);
      this.cerberusSwapKeybindSetting = new KeybindSetting(ЯЩК, 71);
      this.nextSelection = AutoSwap.Selection.FIRST;
      this.swapRequested = false;
      this.actionQueue = new InventoryActionQueue();
      this.actionCoordinator = new InventoryActionCoordinator();
      this.pendingSwapSlot = -1;
      this.presetItemStacks = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
      this.presetItemNames = new String[]{"", "", ""};
      this.playerTickListener = this::method_1;
      this.inputListener = this::л;
      this.hudRenderListener = this::С;
      this.addSettings(this.modeSetting, this.firstItemSetting, this.secondItemSetting, this.thirdItemSetting, this.ignoreNormalTotemsSetting, this.swapKeybindSetting, this.cerberusSwapKeybindSetting, this.cerberusItemSignatureSetting, this.cerberusDataSignatureSetting);
   }

   public void Э(AutoSwapScreen var1) {
      this.activeScreen = var1;
   }

   private void Эс() {
      if (this.swapRequested) {
         if (this.nextSelection == AutoSwap.Selection.FIRST) {
            int var1 = this.а(this.ц());
            if (var1 != -1) {
               this.pendingSwapSlot = var1;
            }

            this.nextSelection = AutoSwap.Selection.SECOND;
         } else if (this.nextSelection == AutoSwap.Selection.SECOND) {
            int var2 = this.а(this.й_50cb85c5());
            if (var2 != -1) {
               this.pendingSwapSlot = var2;
            }

            this.nextSelection = AutoSwap.Selection.FIRST;
         }

         this.swapRequested = false;
      }
   }

   private boolean у(ItemStack var1) {
      if (var1 == null || var1.isEmpty() || !var1.is(Items.PLAYER_HEAD)) {
         return false;
      } else if (this.С(var1)) {
         this.ц(var1);
         return true;
      } else {
         String var2 = this.cerberusDataSignatureSetting.getValue();
         if (var2 != null && !var2.isBlank() && var2.equals(this.У(var1))) {
            return true;
         } else {
            String var3 = this.cerberusItemSignatureSetting.getValue();
            if (var3 != null && !var3.isBlank() && var3.equals(this.Г(var1))) {
               this.ц(var1);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   private boolean I(ItemStack var1) {
      return this.method_4И() && this.у(var1);
   }

   public enum Selection {
      FIRST,
      SECOND;
   }
}
