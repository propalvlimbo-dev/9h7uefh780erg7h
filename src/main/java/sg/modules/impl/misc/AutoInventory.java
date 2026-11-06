package sg.modules.impl.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.HashedStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.util.StringUtil;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.network.FunTimeItemMetadata;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.autobuy.FunTime;
import sg.settings.ActionSetting;
import sg.modules.impl.init.AutoInventoryScreen;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;

public final class AutoInventory extends ClientModule {
   private static final Pattern Ь = Pattern.compile(AutoInventory.Ягз);
   private static final Pattern 衣 = Pattern.compile(AutoInventory.Ягц);
   private static final long Яв = 100L;
   private static final long Яъ = 100L;
   private static final long ЯМ = 100L;
   private static final int АС = 49;
   @SerializedName("В")
   private final AutoInventory.InventoryItem[][] В_ef9cdb33 = new AutoInventory.InventoryItem[2][36];
   private final SingleChoiceSetting д;
   @SerializedName("А")
   private final ActionSetting А_at_eca9855d;
   private final EventListener<PlayerTickEvent> Ят;
   @SerializedName("В")
   private AutoInventory.PurchaseStage В_2613c564;
   @SerializedName("В")
   private AutoInventory.InventoryItem В_822a36f7;
   private long ЯН;
   private int Аз;
   private int Ац;
   private final List<AutoInventory.CollectedItem> сМ;
   private final List<AutoInventory.CollectionEntry> сН;
   private boolean Яя;
   @SerializedName("Х")
   private AutoInventory.CollectionEntry Х_at_eca9855d;
   private int Ая;
   private final List<AutoInventory.AuctionOffer> сн;
   @SerializedName("В")
   private AutoInventory.AuctionOffer В_5593eff0;
   private final Map<String, Integer> сщ;
   private final Map<String, Integer> сГ;
   @SerializedName("4с")
   private int field_4с;
   @SerializedName("4В")
   private int field_4В;
   @SerializedName("4Я")
   private int field_4Я;
   private long Ян;
   private static final String Яг1 = "AutoInventory";
   private static final String ЯгЬ = "Автоматически собирает инвентарь";
   private static final String Яг衣 = "Сервер";
   private static final String Ягш = "Funtime 1.21";
   private static final String ЯгИ = "Открыть панель";
   private static final long Ягь = 1000L;
   private static final long Ягж = 3500L;
   private static final long Яг弟 = 100L;
   private static final long Ягб = 300L;
   private static final long ЯгЫ = 100L;
   private static final long Ягl = 300L;
   private static final long Яг8 = 100L;
   private static final long Яг5 = 1500L;
   private static final long Ягв = 5000L;
   private static final String Ягъ = "������������� �������";
   private static final long ЯгМ = 100L;
   private static final String ЯгН = "������� ��������";
   private static final long Ягн = 100L;
   private static final long ЯгЦ = 300L;
   private static final long Ягч = 3000L;
   private static final long ЯгЭ = 700L;
   private static final float Яг必 = 0.5F;
   private static final long Ягщ = Long.MAX_VALUE;
   private static final long ЯгГ = Long.MAX_VALUE;
   private static final String Ягл = "следующая страница";
   private static final String Ягэ = "следующая страница";
   private static final String Ягю = "предыдущая страница";
   private static final long ЯгЩ = Long.MAX_VALUE;
   private static final String Ягу = "подтверждение покупки";
   private static final String Яг2 = "подозрительная цена";
   private static final String Ягф = "купить";
   private static final String Яги = "подтвердить";
   private static final String Яг9 = "[������]";
   private static final String Яго = "�������";
   private static final String ЯгO = "[★]";
   private static final String Яг7 = "[⚡]";
   private static final String ЯгР = "xxx";
   private static final String Яг3 = "[^\\p{L}\\p{N}$¤]+";
   private static final String Ягр = " ";
   private static final long Яг6 = Long.MAX_VALUE;
   private static final String Яг_ = " ";
   private static final String ЯгШ = ",";
   private static final String Ягы = " ";
   private static final String Ягй = "\\s+";
   private static final String ЯгЖ = " ";
   private static final String Ягм = "HolyWorld";
   private static final float ЯгК = 0.5F;
   private static final String ЯгI = "Name";
   private static final String ЯгТ = "Enabled";
   private static final String ЯгО = "Scan";
   private static final String ЯгФ = "Count";
   private static final String ЯгЙ = "Loadout";
   private static final String Ягк = "Loadout";
   private static final String Ягг = "Loadout";
   private static final String Яг0 = "Loadout";
   private static final String Ягх = "Name";
   private static final String ЯгУ = "Name";
   private static final String ЯгЗ = "Enabled";
   private static final String ЯгЕ = "Enabled";
   private static final String ЯгП = "Scan";
   private static final String Яг西 = "Scan";
   private static final String Ягд = "Count";
   private static final String ЯгБ = "Count";
   private static final String ЯгС = "Funtime 1.21";
   private static final String Ягз = "(?:\\$|¤)\\s*(?:Цена:?\\s*)?([0-9][\\d, ]*)";
   private static final String Ягц = "(\\d+)\\s*/\\s*(\\d+)";

   private int method_3н() {
      return this.В_822a36f7 == null ? 0 : Math.max(0, this.а(this.В_822a36f7) - this.field_4В);
   }

   public void 弟(int var1) {
      if (var1 >= 0 && var1 < 36) {
         this.а_05eb9788()[var1] = null;
      }
   }

   private boolean Ч(ItemStack var1) {
      Item var2 = var1.getItem();
      return var2 == Items.BARRIER
         || var2 == Items.CHEST
         || var2 == Items.ENDER_CHEST
         || var2 == Items.HOPPER
         || var2 == Items.COMPASS
         || var2 == Items.ARROW
         || var2 == Items.SPECTRAL_ARROW
         || var2 == Items.PAPER
         || var2 == Items.STAINED_GLASS_PANE.green()
         || var2 == Items.STAINED_GLASS_PANE.black()
         || var2 == Items.STAINED_GLASS_PANE.lime()
         || var2 == Items.STAINED_GLASS_PANE.red()
         || var2 == Items.STAINED_GLASS_PANE.gray()
         || var2 == Items.STAINED_GLASS_PANE.white()
         || var2 == Items.STAINED_GLASS_PANE.orange()
         || var2 == Items.STAINED_GLASS_PANE.yellow()
         || var2 == Items.DYE.gray();
   }

   private int а_a83dd0cc(AbstractContainerMenu var1, String var2) {
      int var3 = Math.max(0, var1.slots.size() - 36);

      for (int var4 = 0; var4 < var3; var4++) {
         Slot var5 = var1.getSlot(var4);
         if (var5.hasItem() && this.Ч(var5.getItem().getHoverName().getString()).equals(var2)) {
            return var4;
         }
      }

      return -1;
   }

   public void л(int var1, int var2) {
      if (var1 >= 0 && var1 < 36) {
         AutoInventory.InventoryItem var3 = this.а_05eb9788()[var1];
         if (var3 != null) {
            int var4 = Math.clamp(var3.count() + var2, 1, var3.stack().getMaxStackSize());
            this.а_05eb9788()[var1] = new AutoInventory.InventoryItem(var3.stack(), var3.name(), var4, var3.durability());
         }
      }
   }

   private boolean а_bbeebd87(AbstractContainerMenu var1) {
      String var2 = this.Ч(MINECRAFT_CLIENT.gui.screen().getTitle().getString());
      return var2.contains(Ягу) || var2.contains(Яг2);
   }

   public List<AutoInventory.CollectedItem> Ч() {
      return List.copyOf(this.сМ);
   }

   public boolean УI() {
      return this.Яя;
   }

   private void а(AbstractContainerMenu var1, int var2, int var3) {
      MINECRAFT_CLIENT.gameMode.handleContainerInput(var1.containerId, var2, var3, ContainerInput.PICKUP, MINECRAFT_CLIENT.player);
   }

   private void М4() {
      this.field_4с = 0;
      this.field_4В = 0;
      this.field_4Я = -1;
      this.Ян = 0L;
   }

   private String С(String var1) {
      return var1 == null ? "" : var1.replace(ЯгO, "").replace(Яг7, "").replace(ЯгР, "").trim();
   }

   private int[] й_28f5b604() {
      if (MINECRAFT_CLIENT.gui.screen() != null && MINECRAFT_CLIENT.gui.screen().getTitle() != null) {
         Matcher var1 = 衣.matcher(this.Ч(MINECRAFT_CLIENT.gui.screen().getTitle().getString()));
         if (!var1.find()) {
            return null;
         }

         try {
            return new int[]{Integer.parseInt(var1.group(1)), Integer.parseInt(var1.group(2))};
         } catch (NumberFormatException var3) {
            return null;
         }
      } else {
         return null;
      }
   }

   private int Э(AbstractContainerMenu var1, AutoInventory.InventoryItem var2) {
      String var3 = MINECRAFT_CLIENT.player.getName().getString();
      int var4 = Math.min(45, this.а_a83dd0cc(var1));
      int var5 = -1;
      long var6 = ЯгГ;

      for (int var8 = 0; var8 < var4; var8++) {
         ItemStack var9 = var1.getSlot(var8).getItem();
         if (this.р(var9) && this.а(var2, var9) && var9.getCount() <= var2.count()) {
            String var10 = FunTimeItemMetadata.П(var9);
            if (var10 == null || !var10.equalsIgnoreCase(var3)) {
               long var11 = FunTimeItemMetadata.Ж(var9);
               if (var11 > 0L && var11 < var6) {
                  var6 = var11;
                  var5 = var8;
               }
            }
         }
      }

      return var5;
   }

   private void а(ItemStack var1, int var2, long var3) {
      this.field_4с = var1 != null && !var1.isEmpty() ? Math.max(1, var1.getCount()) : 1;
      this.field_4В = this.В_822a36f7 == null ? 0 : this.а(this.В_822a36f7);
      this.field_4Я = var2;
      this.Ян = var3;
   }

   @Override
   public void onDisable() {
      this.Яя = false;
      this.Ме();
   }

   private boolean Х(AbstractContainerMenu var1) {
      return var1.slots.size() >= 45 && !this.Ч(MINECRAFT_CLIENT.gui.screen().getTitle().getString()).contains(Яго);
   }

   public void Л(int var1) {
      if (MINECRAFT_CLIENT.player != null && var1 >= 0 && var1 < 36) {
         ItemStack var2 = MINECRAFT_CLIENT.player.getMainHandItem();
         if (!var2.isEmpty()) {
            float var3 = var2.isDamageableItem() ? (float)(var2.getMaxDamage() - var2.getDamageValue()) / var2.getMaxDamage() : 0.0F;
            this.а_05eb9788()[var1] = new AutoInventory.InventoryItem(
               var2.copyWithCount(1), var2.getHoverName().getString(), Math.max(1, var2.getCount()), var3
            );
         }
      }
   }

   private AutoInventory.InventoryItem а_822a36f7() {
      List<AutoInventory.CollectionEntry> var1 = this.р();
      this.Ая = 0;

      for (AutoInventory.CollectionEntry var3 : var1) {
         if (var3.иЬ()) {
            AutoInventory.InventoryItem var4 = new AutoInventory.InventoryItem(var3.В(), var3.ЮЖ(), var3.Л6(), var3.В().isDamageableItem() ? Яг必 : 0.0F);
            int var5 = this.а(var4);
            int var6 = this.сщ.getOrDefault(var3.ЮЖ(), var5) + this.сГ.getOrDefault(var3.ЮЖ(), 0);
            int var7 = Math.max(var5, var6);
            if (var7 < var4.count()) {
               this.Х_at_eca9855d = var3;
               return new AutoInventory.InventoryItem(var4.stack(), var4.name(), var4.count() - var7, var4.durability());
            }

            this.Ая++;
         }
      }

      this.Х_at_eca9855d = null;
      return null;
   }

   @Override
   public void onEnable() {
      this.МН();
   }

   public AutoInventory.InventoryItem[] а_05eb9788() {
      return this.В_ef9cdb33[this.д.isSelected(Ягм) ? 1 : 0];
   }

   public void М7() {
      AutoInventory.InventoryItem[] var1 = this.а_05eb9788();

      for (int var2 = 0; var2 < 36; var2++) {
         ItemStack var3 = MINECRAFT_CLIENT.player.getInventory().getItem(var2);
         float var4 = var3.isDamageableItem() ? (float)(var3.getMaxDamage() - var3.getDamageValue()) / var3.getMaxDamage() : 0.0F;
         var1[var2] = var3.isEmpty() ? null : new AutoInventory.InventoryItem(var3.copyWithCount(1), var3.getHoverName().getString(), var3.getCount(), var4);
      }
   }

   private boolean а_bbeebd87(AbstractContainerMenu var1, String var2) {
      int var3 = this.а_a83dd0cc(var1);

      for (int var4 = 0; var4 < var3; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty() && this.Ч(var5.getHoverName().getString()).contains(var2)) {
            this.а(var1, var4, ContainerInput.QUICK_MOVE);
            return true;
         }
      }

      return false;
   }

   private void а(int var1, long var2) {
      AutoInventory.CollectionEntry var4 = this.Х_at_eca9855d;
      this.method_33(var1);
      AutoInventory.InventoryItem var5 = this.а_822a36f7();
      if (var5 == null) {
         this.Яя = false;
         if (MINECRAFT_CLIENT.gui.screen() != null) {
            MINECRAFT_CLIENT.player.closeContainer();
         }

         this.Ме();
      } else {
         this.В_822a36f7 = var5;
         if (var4 == this.Х_at_eca9855d && this.Уб()) {
            this.В_2613c564 = AutoInventory.PurchaseStage.WAIT_AUCTION;
            this.ЯН = var2;
         } else {
            if (MINECRAFT_CLIENT.gui.screen() != null) {
               MINECRAFT_CLIENT.player.closeContainer();
            }

            MINECRAFT_CLIENT.player.connection.sendCommand("ah search " + this.С(this.В_822a36f7.name()));
            this.В_2613c564 = AutoInventory.PurchaseStage.WAIT_AUCTION;
            this.ЯН = var2;
            this.Аз = -1;
         }
      }
   }

   private boolean а(AbstractContainerMenu var1, AutoInventory.InventoryItem var2, long var3) {
      int[] var5 = this.й_28f5b604();
      if (var5 == null) {
         return false;
      }

      int var6 = var5[0];
      int var7 = Math.min(4, var5[1]);
      if (this.В_5593eff0 == null) {
         boolean var8 = this.сн.stream().anyMatch(var1x -> var1x.page() == var6);
         if (!var8) {
            int var9 = Math.min(45, this.а_a83dd0cc(var1));

            for (int var10 = 0; var10 < var9; var10++) {
               ItemStack var11 = var1.getSlot(var10).getItem();
               if (this.р(var11) && this.а(var2, var11) && var11.getCount() <= var2.count()) {
                  long var12 = FunTimeItemMetadata.Ж(var11);
                  if (var12 > 0L) {
                     this.сн.add(new AutoInventory.AuctionOffer(var6, var12));
                  }
               }
            }
         }

         if (var6 < var7) {
            if (this.а_bbeebd87(var1, Ягл)) {
               this.ЯН = var3;
            }

            return true;
         }

         this.В_5593eff0 = this.сн.stream().min((var0, var1x) -> Long.compare(var0.price(), var1x.price())).orElse(null);
         if (this.В_5593eff0 == null) {
            this.сн.clear();
            return false;
         }
      }

      if (var6 != this.В_5593eff0.page()) {
         if (this.а_bbeebd87(var1, var6 < this.В_5593eff0.page() ? Ягэ : Ягю)) {
            this.ЯН = var3;
         }

         return true;
      } else {
         int var16 = Math.min(45, this.а_a83dd0cc(var1));
         int var17 = -1;
         long var18 = ЯгЩ;

         for (int var19 = 0; var19 < var16; var19++) {
            ItemStack var13 = var1.getSlot(var19).getItem();
            if (this.р(var13) && this.а(var2, var13) && var13.getCount() <= var2.count()) {
               long var14 = FunTimeItemMetadata.Ж(var13);
               if (var14 == this.В_5593eff0.price()) {
                  this.а(var13, var19, var3);
                  this.а(var1, var19, ContainerInput.QUICK_MOVE);
                  this.В_2613c564 = AutoInventory.PurchaseStage.WAIT_PURCHASE;
                  this.ЯН = var3;
                  return true;
               }

               if (var14 > 0L && var14 < var18) {
                  var18 = var14;
                  var17 = var19;
               }
            }
         }

         if (var17 >= 0) {
            this.а(var1.getSlot(var17).getItem(), var17, var3);
            this.а(var1, var17, ContainerInput.QUICK_MOVE);
            this.В_2613c564 = AutoInventory.PurchaseStage.WAIT_PURCHASE;
            this.ЯН = var3;
            return true;
         } else {
            this.В_5593eff0 = null;
            this.сн.clear();
            return false;
         }
      }
   }

   public String ЭУ() {
      return this.д.getSelectedOption().getName();
   }

   private void а(AbstractContainerMenu var1, int var2, ContainerInput var3) {
      HashedStack var4 = HashedStack.create(var1.getCarried(), MINECRAFT_CLIENT.player.connection.decoratedHashOpsGenenerator());
      MINECRAFT_CLIENT.player
         .connection
         .send(new ServerboundContainerClickPacket(var1.containerId, var1.getStateId(), (short)var2, (byte)0, var3, Int2ObjectMaps.emptyMap(), var4));
   }

   public int method_35() {
      return this.method_3Д();
   }

   private void З(long var1) {
      this.В_2613c564 = AutoInventory.PurchaseStage.IDLE;
      this.В_822a36f7 = null;
      this.ЯН = var1;
      this.Аз = -1;
      this.М4();
      this.сн.clear();
      this.В_5593eff0 = null;
   }

   public void МТ() {
      AutoInventory.InventoryItem[] var1 = this.а_05eb9788();

      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = null;
      }
   }

   private boolean Уб() {
      return MINECRAFT_CLIENT.gui.screen() != null && MINECRAFT_CLIENT.gui.screen().getTitle() != null && FunTimeItemMetadata.Зы(MINECRAFT_CLIENT.gui.screen().getTitle().getString());
   }

   public AutoInventory() {
      super(Яг1, ЯгЬ, ModuleCategory.MISC);
      this.д = new SingleChoiceSetting(Яг衣, new ChoiceOption(Ягш));
      this.А_at_eca9855d = new ActionSetting(ЯгИ).onClick(this::МН).showOtherSettings();
      this.Ят = this::method_3Б;
      this.В_2613c564 = AutoInventory.PurchaseStage.IDLE;
      this.Аз = -1;
      this.сМ = new ArrayList<>();
      this.сН = new ArrayList<>();
      this.сн = new ArrayList<>();
      this.сщ = new HashMap<>();
      this.сГ = new HashMap<>();
      this.field_4Я = -1;
      this.addSettings(this.д, this.А_at_eca9855d);
   }

   private long а(ItemStack var1) {
      Matcher var2 = Ь.matcher(this.Ь(var1));
      long var3 = Яг6;

      while (var2.find()) {
         try {
            var3 = Math.min(var3, Long.parseLong(var2.group(1).replace(Яг_, "").replace(ЯгШ, "")));
         } catch (NumberFormatException var6) {
         }
      }

      return var3;
   }

   private void Ме() {
      this.В_2613c564 = AutoInventory.PurchaseStage.IDLE;
      this.В_822a36f7 = null;
      this.ЯН = System.currentTimeMillis();
      this.Аз = -1;
      this.Ац = 0;
      this.М4();
      this.Х_at_eca9855d = null;
      this.сн.clear();
      this.В_5593eff0 = null;
   }

   private int а(AbstractContainerMenu var1, AutoInventory.InventoryItem var2) {
      int var3 = Math.min(45, Math.max(0, var1.slots.size() - 36));
      int var4 = -1;
      long var5 = Ягщ;

      for (int var7 = 0; var7 < var3; var7++) {
         Slot var8 = var1.getSlot(var7);
         if (var8.hasItem() && this.а(var2, var8.getItem())) {
            long var9 = this.а(var8.getItem());
            if (var9 < var5) {
               var5 = var9;
               var4 = var7;
            }
         }
      }

      return var4;
   }

   private int а_a83dd0cc(AbstractContainerMenu var1) {
      return Math.max(0, Math.min(54, var1.slots.size() - 36));
   }

   private void МН() {
      if (MINECRAFT_CLIENT.player != null) {
         if (!this.isEnabled()) {
            this.enable();
         } else {
            this.Ме();
            this.Яя = false;
            this.сМ.clear();
            if (MINECRAFT_CLIENT.player != null) {
               MINECRAFT_CLIENT.gui.setScreen(new AutoInventoryScreen(this));
            }
         }
      }
   }

   @Override
   public void loadConfig(JsonObject var1) {
      super.loadConfig(var1);
      if (var1 != null && var1.has(Ягк) && var1.get(Ягг).isJsonArray()) {
         List<AutoInventory.CollectionEntry> var2 = this.р();

         for (JsonElement var4 : var1.getAsJsonArray(Яг0)) {
            if (var4.isJsonObject()) {
               JsonObject var5 = var4.getAsJsonObject();
               if (var5.has(Ягх)) {
                  String var6 = var5.get(ЯгУ).getAsString();
                  AutoInventory.CollectionEntry var7 = var2.stream().filter(var1x -> var1x.ЮЖ().equals(var6)).findFirst().orElse(null);
                  if (var7 != null) {
                     if (var5.has(ЯгЗ)) {
                        var7.Я_(var5.get(ЯгЕ).getAsBoolean());
                     }

                     if (var5.has(ЯгП)) {
                        var7.Я0(var5.get(Яг西).getAsBoolean());
                     }

                     if (var5.has(Ягд)) {
                        var7.Яр(var5.get(ЯгБ).getAsInt());
                     }
                  }
               }
            }
         }
      }
   }

   private boolean Уп() {
      return this.д.isSelected(ЯгС);
   }

   private void method_33(int var1) {
      if (this.В_822a36f7 != null) {
         var1 = Math.max(1, var1);
         if (this.Х_at_eca9855d != null) {
            this.сГ.merge(this.Х_at_eca9855d.ЮЖ(), var1, Integer::sum);
         }

         this.сМ.add(new AutoInventory.CollectedItem(this.В_822a36f7.stack().copy(), this.В_822a36f7.name(), var1, System.currentTimeMillis()));
         this.М4();
      }
   }

   private int Э_a83dd0cc(AbstractContainerMenu var1) {
      int var2 = Math.max(0, var1.slots.size() - 36);

      for (int var3 = 0; var3 < var2; var3++) {
         Slot var4 = var1.getSlot(var3);
         if (var4.hasItem()) {
            String var5 = this.Ч(var4.getItem().getHoverName().getString());
            if (var5.contains(Ягф) || var5.contains(Яги)) {
               return var3;
            }
         }
      }

      return var2 == 27 ? 0 : this.а_a83dd0cc(var1, Яг9);
   }

   private String р(String var1) {
      return StringUtil.stripColor(var1 == null ? "" : var1).toLowerCase(Locale.ROOT).replace('ё', 'е').replaceAll(Яг3, Ягр).trim();
   }

   @Override
   public JsonObject saveConfig() {
      JsonObject var1 = super.saveConfig();
      JsonArray var2 = new JsonArray();

      for (AutoInventory.CollectionEntry var4 : this.р()) {
         JsonObject var5 = new JsonObject();
         var5.addProperty(ЯгI, var4.ЮЖ());
         var5.addProperty(ЯгТ, var4.иЬ());
         var5.addProperty(ЯгО, var4.им());
         var5.addProperty(ЯгФ, var4.Л6());
         var2.add(var5);
      }

      var1.add(ЯгЙ, var2);
      return var1;
   }

   public List<AutoInventory.CollectionEntry> р() {
      for (FunTime.PurchaseEntry var3 : RoseClient.getInstance().getServices().getModules().getFunTime().Ж_2571477d()) {
         boolean var4 = this.сН.stream().anyMatch(var1 -> var1.ЮЖ().equals(var3.name()));
         if (!var4) {
            this.сН.add(new AutoInventory.CollectionEntry(var3));
         }
      }

      return this.сН;
   }

   private String Ч(String var1) {
      return StringUtil.stripColor(var1 == null ? "" : var1).toLowerCase(Locale.ROOT).replaceAll(Ягй, ЯгЖ).trim();
   }

   private int а(AutoInventory.InventoryItem var1) {
      if (MINECRAFT_CLIENT.player == null) {
         return 0;
      }

      int var2 = 0;

      for (int var3 = 0; var3 < MINECRAFT_CLIENT.player.getInventory().getContainerSize(); var3++) {
         ItemStack var4 = MINECRAFT_CLIENT.player.getInventory().getItem(var3);
         if (this.а(var1, var4)) {
            var2 += var4.getCount();
         }
      }

      return var2;
   }

   private boolean а(AutoInventory.InventoryItem var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.getItem() == var1.stack().getItem()) {
         if (var1.durability() > 0.0F && var2.isDamageableItem()) {
            float var3 = (float)(var2.getMaxDamage() - var2.getDamageValue()) / var2.getMaxDamage();
            if (var3 < var1.durability()) {
               return false;
            }
         }

         AutoInventory.CollectionEntry var5 = this.р().stream().filter(var1x -> var1x.ЮЖ().equals(var1.name())).findFirst().orElse(null);
         if (var5 != null) {
            return var5.ы(var2, this.р(this.Ь(var2)));
         }

         String var4 = this.Ч(this.С(var1.name()));
         return (var4.isEmpty() || this.Ь(var2).contains(var4)) && FunTimeItemMetadata.Р(this.С(var1.name()), var2);
      } else {
         return false;
      }
   }

   public void МЙ() {
      if (this.method_3Д() != 0) {
         this.Ме();
         this.сщ.clear();
         this.сГ.clear();

         for (AutoInventory.CollectionEntry var2 : this.р()) {
            if (var2.иЬ()) {
               AutoInventory.InventoryItem var3 = new AutoInventory.InventoryItem(
                  var2.В(), var2.ЮЖ(), var2.Л6(), var2.В().isDamageableItem() ? ЯгК : 0.0F
               );
               this.сщ.put(var2.ЮЖ(), this.а(var3));
            }
         }

         this.Яя = true;
         this.ЯН = 0L;
      }
   }

   public int method_3Ф() {
      return this.Ая;
   }

   private void method_3Б(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.player.connection != null) {
         long var2 = System.currentTimeMillis();
         if (!(MINECRAFT_CLIENT.gui.screen() instanceof AutoInventoryScreen)) {
            if (this.Яя) {
               if (MINECRAFT_CLIENT.player.getInventory().getFreeSlot() == -1 && this.а_822a36f7() != null) {
                  this.Яя = false;
                  if (MINECRAFT_CLIENT.gui.screen() != null) {
                     MINECRAFT_CLIENT.player.closeContainer();
                  }

                  this.Ме();
               } else if (this.В_2613c564 == AutoInventory.PurchaseStage.IDLE) {
                  if (var2 - this.ЯН >= Ягь) {
                     this.В_822a36f7 = this.а_822a36f7();
                     if (this.В_822a36f7 == null) {
                        this.Яя = false;
                        this.Ме();
                     } else {
                        MINECRAFT_CLIENT.player.connection.sendCommand("ah search " + this.С(this.В_822a36f7.name()));
                        this.В_2613c564 = AutoInventory.PurchaseStage.WAIT_AUCTION;
                        this.ЯН = var2;
                     }
                  }
               } else {
                  if (this.В_2613c564 == AutoInventory.PurchaseStage.WAIT_PURCHASE) {
                     int var4 = this.method_3н();
                     if (var4 > 0) {
                        this.а(var4, var2);
                        return;
                     }
                  }

                  if (!(MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen)) {
                     if (var2 - this.ЯН > Ягж) {
                        this.З(var2);
                     }
                  } else {
                     AbstractContainerMenu var10 = MINECRAFT_CLIENT.player.containerMenu;
                     if (this.В_2613c564 == AutoInventory.PurchaseStage.WAIT_AUCTION) {
                        if (this.Аз != var10.containerId) {
                           this.Аз = var10.containerId;
                           this.ЯН = var2;
                        }

                        long var11 = this.Уп() ? Яг弟 : Ягб;
                        if (var2 - this.ЯН >= var11) {
                           if (!this.Уп() || this.Х_at_eca9855d == null || !this.Х_at_eca9855d.им() || !this.а(var10, this.В_822a36f7, var2)) {
                              int var7 = this.Уп() ? this.Э(var10, this.В_822a36f7) : this.а(var10, this.В_822a36f7);
                              if (var7 >= 0) {
                                 ItemStack var8 = var10.getSlot(var7).getItem();
                                 this.а(var8, var7, var2);
                                 boolean var9 = !this.Уп() && var8.getCount() > this.В_822a36f7.count();
                                 this.Ац = var9 ? Math.max(0, this.В_822a36f7.count() - 1) : 0;
                                 if (this.Уп()) {
                                    this.а(var10, var7, ContainerInput.QUICK_MOVE);
                                 } else {
                                    this.а(var10, var7, var9 ? 1 : 0);
                                 }

                                 this.В_2613c564 = AutoInventory.PurchaseStage.WAIT_PURCHASE;
                                 this.ЯН = var2;
                              } else if (this.Уп() && var10.slots.size() > 49 && var2 - this.ЯН >= ЯгЫ) {
                                 ContainerInput var12 = ThreadLocalRandom.current().nextInt(100) < 25 ? ContainerInput.QUICK_MOVE : ContainerInput.PICKUP;
                                 this.а(var10, 49, var12);
                                 this.ЯН = var2;
                              } else if (this.Уп()) {
                                 this.ЯН = var2;
                              } else if (this.Э_bbeebd87(var10) && var2 - this.ЯН >= Ягl) {
                                 this.а(var10, 50, 0);
                                 this.ЯН = var2;
                                 this.Аз = -1;
                              } else if (!this.Э_bbeebd87(var10)) {
                                 MINECRAFT_CLIENT.player.closeContainer();
                                 this.З(var2);
                              }
                           }
                        }
                     } else if (this.В_2613c564 == AutoInventory.PurchaseStage.WAIT_PURCHASE) {
                        if (this.Уп()) {
                           if (this.а_bbeebd87(var10)) {
                              if (var2 - this.Ян >= Яг8 && var10.slots.size() > 1) {
                                 this.а(var10, 1, ContainerInput.QUICK_MOVE);
                                 this.Ян = var2;
                              }
                           } else if (this.Уб()) {
                              if (var2 - this.ЯН > Яг5) {
                                 this.М4();
                                 this.В_2613c564 = AutoInventory.PurchaseStage.WAIT_AUCTION;
                                 this.ЯН = var2;
                              }
                           } else if (var2 - this.ЯН > Ягв) {
                              if (MINECRAFT_CLIENT.gui.screen() != null) {
                                 MINECRAFT_CLIENT.player.closeContainer();
                              }

                              this.М4();
                              this.З(var2);
                           }
                        } else {
                           String var5 = this.Ч(MINECRAFT_CLIENT.gui.screen().getTitle().getString());
                           if (this.Уп() && var5.contains(Ягъ)) {
                              if (var2 - this.ЯН >= ЯгМ) {
                                 int var6 = this.Э_a83dd0cc(var10);
                                 if (var6 >= 0) {
                                    this.а(var10, var6, 0);
                                    this.Мв();
                                    this.В_2613c564 = AutoInventory.PurchaseStage.VERIFY;
                                    this.ЯН = var2;
                                 }
                              }
                           } else if (!this.Уп() && var5.contains(ЯгН) && var2 - this.ЯН >= Ягн) {
                              if (this.Ац >= 10) {
                                 this.а(var10, 4, 0);
                                 this.Ац -= 10;
                              } else if (this.Ац > 0) {
                                 this.а(var10, 3, 0);
                                 this.Ац--;
                              } else if (var10.slots.size() > 2) {
                                 this.а(var10, 2, 0);
                                 this.Мв();
                                 this.В_2613c564 = AutoInventory.PurchaseStage.VERIFY;
                              }

                              this.ЯН = var2;
                           } else if (this.Х(var10) && var2 - this.ЯН >= ЯгЦ) {
                              this.В_2613c564 = AutoInventory.PurchaseStage.WAIT_AUCTION;
                              this.ЯН = var2;
                           } else if (var2 - this.ЯН > Ягч) {
                              MINECRAFT_CLIENT.player.closeContainer();
                              this.З(var2);
                           }
                        }
                     } else {
                        if (this.В_2613c564 == AutoInventory.PurchaseStage.VERIFY && var2 - this.ЯН >= ЯгЭ) {
                           if (MINECRAFT_CLIENT.gui.screen() != null) {
                              MINECRAFT_CLIENT.player.closeContainer();
                           }

                           this.З(var2);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private String Ь(ItemStack var1) {
      ArrayList var2 = new ArrayList();
      var2.add(var1.getHoverName().getString());
      ItemLore var3 = var1.getComponents().get(DataComponents.LORE);
      if (var3 != null) {
         var3.lines().forEach(var1x -> var2.add(var1x.getString()));
      }

      return this.Ч(String.join(Ягы, var2));
   }

   private boolean Э_bbeebd87(AbstractContainerMenu var1) {
      return var1.slots.size() > 50 && var1.getSlot(50).hasItem() && var1.getSlot(50).getItem().is(Items.ARROW);
   }

   private boolean р(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && !this.Ч(var1);
   }

   private void Мв() {
      this.method_33(Math.max(1, this.field_4с));
   }

   public void Х(int var1, ItemStack var2, String var3) {
      if (var1 >= 0 && var1 < 36 && var2 != null && !var2.isEmpty()) {
         float var4 = var2.isDamageableItem() ? 1.0F : 0.0F;
         this.а_05eb9788()[var1] = new AutoInventory.InventoryItem(var2.copyWithCount(1), var3, Math.max(1, var2.getCount()), var4);
      }
   }

   public int method_3Д() {
      int var1 = 0;

      for (AutoInventory.CollectionEntry var3 : this.р()) {
         if (var3.иЬ()) {
            var1++;
         }
      }

      return var1;
   }

   public record InventoryItem(ItemStack stack, String name, int count, float durability) {
   }

   public static final class CollectionEntry {
      private final FunTime.PurchaseEntry Х;
      private boolean А8;
      private boolean А5;
      private int Ъэ = 1;
      private boolean Ав;

      private CollectionEntry(FunTime.PurchaseEntry var1) {
         this.Х = var1;
      }

      public void Я_(boolean var1) {
         this.А8 = var1;
      }

      public int Л6() {
         if (this.Ав && MinecraftAccess.MINECRAFT_CLIENT.level != null) {
            this.Яр(this.Ъэ);
         }

         return this.Ъэ;
      }

      public boolean им() {
         return this.А5;
      }

      private boolean ы(ItemStack var1, String var2) {
         if (!this.Х.catalogEntry().matches(var1)) {
            return false;
         }

         if (var2.contains(this.Х.normalizedName())) {
            return true;
         }

         for (String var6 : this.Х.aliases()) {
            if (!var6.isEmpty() && var2.contains(var6)) {
               return true;
            }
         }

         return false;
      }

      public ItemStack В() {
         return this.Х.stack();
      }

      public String ЮЖ() {
         return this.Х.name();
      }

      public boolean иЬ() {
         return this.А8;
      }

      public void Яр(int var1) {
         this.Ав = MinecraftAccess.MINECRAFT_CLIENT.level == null;
         this.Ъэ = this.Ав ? Math.max(1, var1) : Math.clamp(var1, 1, Math.max(1, this.В().getMaxStackSize() * 4));
      }

      public void Я0(boolean var1) {
         this.А5 = var1;
      }
   }

   private enum PurchaseStage {
      IDLE,
      WAIT_AUCTION,
      WAIT_PURCHASE,
      VERIFY;
   }

   private record AuctionOffer(int page, long price) {
   }

   public record CollectedItem(ItemStack stack, String name, int count, long time) {
   }
}
