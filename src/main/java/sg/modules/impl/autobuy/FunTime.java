package sg.modules.impl.autobuy;

import com.google.gson.annotations.SerializedName;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.HashedStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.network.FunTimeItemMetadata;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.modules.impl.init.FunTimeItemCatalog;

public class FunTime extends ClientModule {
   private static final long Яэ = 900L;
   private static final long Яю = 100L;
   private static final long ЯЩ = 100L;
   private static final long Яу = 100L;
   private static final long Я2 = 350L;
   private static final long Яф = 150L;
   private static final long Яи_6da43b94 = 8000L;
   private static final int field_48 = 2;
   private static final long Я9 = 12000L;
   private static final long Яо = 120000L;
   private static final long ЯO = 80000L;
   private static final long Я7 = 30000L;
   private static final long ЯР = 350L;
   private static final long Я3 = 3500L;
   private static final long Яр = 250L;
   private static final int field_45 = 49;
   private final EventListener<PlayerTickEvent> Я弟 = this::method_9Д;
   private final EventListener<PacketReceiveEvent> Яб = this::б;
   private boolean Х诶;
   private final List<FunTime.PurchaseEntry> сГ = new ArrayList<>();
   private final BooleanSetting ВЧ;
   private final BooleanSetting ВЛ;
   private final BooleanSetting В诶;
   @SerializedName("Яи")
   private final FloatSetting Яи_989fed9a;
   private boolean Х1;
   private boolean ХЬ;
   @SerializedName("4в")
   private int field_4в;
   private long Я6;
   private long Я_;
   private long ЯШ;
   private long Яы;
   private volatile long Яй;
   private volatile long ЯЖ;
   @SerializedName("4ъ")
   private volatile int field_4ъ;
   @SerializedName("4М")
   private volatile int field_4М;
   @SerializedName("4Н")
   private volatile int field_4Н;
   @SerializedName("4н")
   private int field_4н;
   private double Вз;
   @SerializedName("4Ц")
   private int field_4Ц;
   @SerializedName("4ч")
   private int field_4ч;
   private long Ям;
   private volatile boolean Х衣;
   private long ЯК;
   @SerializedName("4Э")
   private int field_4Э;
   private long ЯI;
   private String В_;
   private final Map<String, Long> сл;
   private boolean Хш;
   private long ЯТ;
   private double Вц;
   private double Вя;
   private boolean ХИ;
   @SerializedName("4必")
   private int field_4必;
   @SerializedName("4щ")
   private int field_4щ;
   private long ЯО;
   private long ЯФ;
   private static final String ъг = "FunTime";
   private static final String ъ0 = "AutoBuy";
   private static final String ъх = "Авто-бай";
   private static final String ъУ = "Парсить цены";
   private static final String ъЗ = "Авто-ReParse";
   private static final String ъЕ = "Скидка парсинга";
   private static final float ъП = 30.0F;
   private static final float ъ西 = 100.0F;
   private static final long ъд = 120000L;
   private static final String ъБ = "выкл";
   private static final long ъС = 1000000L;
   private static final double ъз = 1000000.0;
   private static final long ъц = 1000L;
   private static final double ъя = 1000.0;
   private static final String Мс = "%,d";
   private static final long МВ = 120000L;
   private static final String МЯ = "block.note_block.basedrum";
   private static final String МХ = "sound";
   private static final String Ме = "inventory";
   private static final long МА = 1000L;
   private static final String М4 = "вы успешно купили";
   private static final String Мп = "вы купили";
   private static final String МД = "недостаточно";
   private static final String МЪ = "не хватает";
   private static final long Мт = 30000L;
   private static final float Ма = 1.0E9F;
   private static final float МЮ = 100.0F;
   private static final long МЧ = 8000L;
   private static final long МЛ = 8000L;
   private static final long М诶 = 100L;
   private static final long М1 = 100L;
   private static final long МЬ = 100L;
   private static final long М衣 = 900L;
   private static final long Мш = 900L;
   private static final String МИ = "ah";
   private static final long Мь = 120000L;
   private static final long Мж = 80000L;
   private static final long М弟 = 120000L;
   private static final long Мб = 120000L;
   private static final long МЫ = 12000L;
   private static final long Мl = 350L;
   private static final double М8 = 150.0;
   private static final double М5 = 0.9;
   private static final double Мв = 0.1;
   private static final String Мъ = "подтверждение покупки";
   private static final String ММ = "подозрительная цена";
   private static final String МН = "[^\\p{L}\\p{N}$¤]+";
   private static final String Мн = " ";
   private static final String МЦ = "[★]";
   private static final String Мч = "[⚡]";
   private static final String МЭ = "xxx";
   private static final double М必 = 0.05;
   private static final String Мщ = "%.0f";
   private static final String МГ = "%.1f";
   private static final String Мл = "\\.0$";
   private static final long Мэ = 75L;
   private static final double Мю = 0.0025;
   private static final double МЩ = 4.0;
   private static final long Му = 350L;
   private static final long М2 = 3500L;
   private static final long Мф = 250L;
   private static final long Ми = -1L;
   private static final long М9 = -1L;

   public void шш() {
      this.ХЬ = !this.ХЬ;
      this.В诶.setValue(this.ХЬ);
      long var1 = System.currentTimeMillis();
      this.ЯК = this.ХЬ ? var1 + ъд : 0L;
      this.ЯI = var1;
   }

   private boolean Ш(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && !this.Ь(var1);
   }

   private boolean И(FunTime.PurchaseEntry var1, ItemStack var2) {
      return this.И(var1, this.с(var2)) && var1.catalogEntry().matches(var2);
   }

   private void О(long var1) {
      MINECRAFT_CLIENT.player.connection.sendCommand(МИ);
      this.Я6 = var1;
      this.Ям = 0L;
   }

   private boolean Г(String var1) {
      Long var2 = this.сл.get(var1);
      if (var2 == null) {
         return false;
      } else if (System.currentTimeMillis() >= var2) {
         this.сл.remove(var1);
         return false;
      } else {
         return true;
      }
   }

   private void К(long var1) {
      int var3;
      do {
         var3 = 901 + ThreadLocalRandom.current().nextInt(3);
      } while (var3 == this.field_4Э);

      this.Р(var3, var1);
      this.ЯК = var1 + М弟;
      this.ЯI = var1;
   }

   public String Р(long var1) {
      if (var1 <= 0L) {
         return ъБ;
      } else if (var1 >= ъС) {
         double var5 = var1 / ъз;
         return this.И(var5) + "м";
      } else if (var1 >= ъц) {
         double var3 = var1 / ъя;
         return this.И(var3) + "к";
      } else {
         return String.format(Locale.ROOT, Мс, var1).replace(',', ' ');
      }
   }

   private void Р(boolean var1, boolean var2) {
      if (MINECRAFT_CLIENT.options != null && MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.options.keyUp.setDown(false);
         MINECRAFT_CLIENT.options.keyDown.setDown(false);
         MINECRAFT_CLIENT.options.keyLeft.setDown(var1);
         MINECRAFT_CLIENT.options.keyRight.setDown(var2);
         this.И(false, false, var1, var2);
      }
   }

   private String method__(String var1) {
      return StringUtil.stripColor(var1 == null ? "" : var1).toLowerCase(Locale.ROOT).replace('ё', 'е').replaceAll(МН, Мн).trim();
   }

   private boolean И(FunTime.PurchaseEntry var1, String var2) {
      if (var2.contains(var1.normalizedName())) {
         return true;
      }

      for (String var6 : var1.aliases()) {
         if (!var6.isEmpty() && var2.contains(var6)) {
            return true;
         }
      }

      return false;
   }

   private void Р(int var1, long var2) {
      this.field_4Э = var1;
      this.шЕ();
      this.field_4Н = -1;
      this.field_4н = -1;
      this.ЯШ = 0L;
      this.шч();
      this.Х衣 = false;
      if (MINECRAFT_CLIENT.gui.screen() != null) {
         MINECRAFT_CLIENT.player.clientSideCloseContainer();
      }

      MINECRAFT_CLIENT.player.connection.sendCommand("an" + var1);
      this.Я6 = var2;
      this.Ям = var2 + МЫ;
   }

   public void ши() {
      this.method_9Р(!this.Х1);
   }

   private int method_3(AbstractContainerMenu var1) {
      return Math.max(0, Math.min(54, var1.slots.size() - 36));
   }

   public List<FunTime.PurchaseEntry> Ж_2571477d() {
      return Collections.unmodifiableList(this.сГ);
   }

   @Override
   public void onEnable() {
      this.шЭ();
   }

   private void шЭ() {
      long var1 = System.currentTimeMillis();
      this.Я6 = 0L;
      this.Я_ = 0L;
      this.ЯШ = 0L;
      this.Яы = 0L;
      this.ЯI = var1;
      this.В_ = null;
      this.шЕ();
      this.field_4Н = -1;
      this.field_4н = -1;
      this.шч();
      this.Х衣 = false;
      this.Ям = 0L;
      this.сл.clear();
   }

   private boolean 诶Я() {
      if (!this.ХЬ) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      if (this.ЯК == 0L) {
         this.ЯК = var1 + Мь;
         this.ЯI = var1;
         return false;
      }

      boolean var3 = this.诶3() && var1 - this.ЯI >= Мж;
      boolean var4 = var1 >= this.ЯК;
      if (!var3 && !var4) {
         return false;
      }

      this.К(var1);
      return true;
   }

   private void шУ() {
      if (this.ВЧ.getValue() != this.Х1) {
         this.Х1 = this.ВЧ.getValue();
         this.шЭ();
      }

      if (this.ВЛ.getValue() != this.ХИ) {
         if (this.ВЛ.getValue()) {
            this.field_4必 = 0;
            this.field_4щ = 0;
            this.ЯО = 0L;
            this.ЯФ = 0L;
            this.ХИ = true;
         } else {
            this.ХИ = false;
            this.ш6();
         }
      }

      if (this.В诶.getValue() != this.ХЬ) {
         this.ХЬ = this.В诶.getValue();
         long var1 = System.currentTimeMillis();
         this.ЯК = this.ХЬ ? var1 + МВ : 0L;
         this.ЯI = var1;
      }

      this.field_4в = Mth.clamp(Math.round(this.Яи_989fed9a.getValue()), 0, 100);
   }

   private void шс() {
      if (MINECRAFT_CLIENT.options != null) {
         MINECRAFT_CLIENT.options.keyUp.setDown(false);
         MINECRAFT_CLIENT.options.keyDown.setDown(false);
         MINECRAFT_CLIENT.options.keyLeft.setDown(false);
         MINECRAFT_CLIENT.options.keyRight.setDown(false);
      }

      this.И(false, false, false, false);
   }

   private void Р(String var1, long var2) {
      if (this.Яй != 0L) {
         this.Яй = 0L;
         this.field_4ъ = -1;
         this.field_4М = -1;
      }
   }

   public void method_9Д(PlayerTickEvent var1) {
      this.шУ();
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player.connection != null) {
         if (this.ХИ) {
            this.ше();
         } else {
            this.ш1();
            if (this.Х1) {
               if (this.诶Я()) {
                  return;
               }

               this.шо();
            }
         }
      } else {
         if (this.Хш) {
            this.ш6();
         }
      }
   }

   private boolean Ь(ItemStack var1) {
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

   public int method_9必() {
      int var1 = 0;

      for (FunTime.PurchaseEntry var3 : this.сГ) {
         if (var3.enabled()) {
            var1++;
         }
      }

      return var1;
   }

   private static String л(String var0) {
      return var0.replace(МЦ, "").replace(Мч, "").replace(МЭ, "").trim();
   }

   public void method_9Р(int var1) {
      this.field_4в = Mth.clamp(var1, 0, 100);
      this.Яи_989fed9a.setValue((float)this.field_4в);
   }

   private long И(FunTime.PurchaseEntry var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.containerMenu != null) {
         AbstractContainerMenu var2 = MINECRAFT_CLIENT.player.containerMenu;
         long var3 = М9;
         int var5 = Math.min(45, this.method_3(var2));

         for (int var6 = 0; var6 < var5; var6++) {
            ItemStack var7 = var2.getSlot(var6).getItem();
            if (this.Ш(var7) && this.И(var1, var7)) {
               long var8 = FunTimeItemMetadata.Ж(var7);
               if (var8 > 0L && (var3 < 0L || var8 < var3)) {
                  var3 = var8;
               }
            }
         }

         return var3;
      } else {
         return Ми;
      }
   }

   private boolean 诶и() {
      return MINECRAFT_CLIENT.gui.screen() != null && MINECRAFT_CLIENT.gui.screen().getTitle() != null ? FunTimeItemMetadata.Зы(MINECRAFT_CLIENT.gui.screen().getTitle().getString()) : false;
   }

   private void ш6() {
      this.Хш = false;
      this.шс();
   }

   private void ь(long var1) {
      int var3;
      do {
         var3 = ThreadLocalRandom.current().nextBoolean() ? ThreadLocalRandom.current().nextInt(205, 231) : ThreadLocalRandom.current().nextInt(305, 325);
      } while (var3 == this.field_4Э);

      this.Р(var3, var1);
      this.ЯI = var1;
      if (this.ХЬ) {
         this.ЯК = var1 + Мб;
      }
   }

   private synchronized void Я(String var1, long var2) {
      boolean var4 = var2 >= Мl;
      boolean var5 = this.field_4Ц >= 2 && var2 >= this.Вз + М8;
      if (!var4 && !var5) {
         this.field_4ч = 0;
         this.Вз = this.field_4Ц == 0 ? var2 : this.Вз * М5 + var2 * Мв;
         this.field_4Ц++;
      } else {
         this.field_4ч++;
      }

      this.Х衣 = this.field_4ч >= 2;
   }

   public boolean 诶Р() {
      return this.ХЬ;
   }

   private void б(long var1) {
      this.field_4必++;
      this.field_4щ = 0;
      this.ЯО = var1;
      this.ЯФ = var1 + Мф;
   }

   private Boolean И_6c785b53(boolean var1) {
      if (MINECRAFT_CLIENT.player == null) {
         return var1;
      } else {
         double var2 = MINECRAFT_CLIENT.player.getX() - this.Вц;
         double var4 = MINECRAFT_CLIENT.player.getZ() - this.Вя;
         double var6 = var2 * var2 + var4 * var4;
         if (var6 <= 1.0) {
            return var1;
         } else {
            double var8 = Math.toRadians(MINECRAFT_CLIENT.player.getYRot());
            double var10 = Math.cos(var8) * -var2 + Math.sin(var8) * -var4;
            if (Math.abs(var10) > Мю) {
               return var10 > 0.0;
            } else {
               return var6 >= МЩ ? null : var1;
            }
         }
      }
   }

   private boolean И(ContainerScreen var1) {
      String var2 = this.method__(var1.getTitle().getString());
      return var2.contains(Мъ) || var2.contains(ММ);
   }

   private FunTime.PurchaseEntry И(ItemStack var1) {
      String var2 = this.с(var1);

      for (FunTime.PurchaseEntry var4 : this.сГ) {
         if (var4.enabled() && this.И(var4, var2) && var4.catalogEntry().matches(var1)) {
            return var4;
         }
      }

      return null;
   }

   private String с(ItemStack var1) {
      StringBuilder var2 = new StringBuilder(this.method__(var1.getHoverName().getString()));
      ItemLore var3 = var1.getComponents().get(DataComponents.LORE);
      if (var3 != null) {
         for (Component var5 : var3.lines()) {
            var2.append(' ').append(this.method__(var5.getString()));
         }
      }

      return var2.toString();
   }

   private void И(boolean var1, boolean var2, boolean var3, boolean var4) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.input != null) {
         Input var5 = MINECRAFT_CLIENT.player.input.keyPresses;
         if (var5 != null) {
            MINECRAFT_CLIENT.player.input.keyPresses = new Input(var1, var2, var3, var4, var5.jump(), var5.shift(), var5.sprint());
         }
      }
   }

   public void б(PacketReceiveEvent var1) {
      long var2 = System.currentTimeMillis();
      if (var1.getPacket() instanceof ClientboundSoundPacket var11) {
         if (this.Х1 && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen && var11.getSound().value().location().getPath().equals(МЯ) && this.诶и()) {
            long var16 = this.ЯЖ;
            if (var16 != 0L) {
               this.ЯЖ = 0L;
               this.Я(МХ, var2 - var16);
            }

            this.field_4Н = MINECRAFT_CLIENT.player.containerMenu.containerId;
         }
      } else if (var1.getPacket() instanceof ClientboundContainerSetContentPacket var10) {
         if (this.Х1 && this.Яй != 0L && var10.containerId() == this.field_4ъ) {
            this.Р(Ме, var2);
         }
      } else if (var1.getPacket() instanceof ClientboundContainerSetSlotPacket var9) {
         if (this.Х1 && this.Яй != 0L && var9.getContainerId() == this.field_4ъ && var9.getSlot() >= 0 && var9.getSlot() < 45) {
            this.Р("slot:" + var9.getSlot(), var2);
         }
      } else if (var1.getPacket() instanceof ClientboundSystemChatPacket var4) {
         String var15 = StringUtil.stripColor(var4.content().getString());
         String var6 = var15.toLowerCase(Locale.ROOT).replace('ё', 'е');
         long var7 = FunTimeItemMetadata.Р(var15);
         if (var7 > 0L) {
            this.ЯФ = var2 + var7 * МА;
         }

         if (var6.contains(М4) || var6.contains(Мп)) {
            this.ЯI = var2;
         }

         if (this.В_ != null && (var6.contains(МД) || var6.contains(МЪ))) {
            this.сл.put(this.В_, var2 + Мт);
            this.В_ = null;
         }
      }
   }

   @Override
   public void onDisable() {
      this.Х1 = false;
      this.ХИ = false;
      this.шЕ();
      this.field_4Н = -1;
      this.field_4н = -1;
      this.шч();
      this.Ям = 0L;
      this.Х衣 = false;
      this.ш6();
   }

   private void И(FunTimeItemCatalog.CatalogEntry var1) {
      String var2 = var1.name();
      FloatSetting var3 = new FloatSetting(var2, 0.0F, 0.0F, Ма).withStep(МЮ);
      this.сГ.add(new FunTime.PurchaseEntry(var2, var3, this.method__(var2), new String[0], var1));
   }

   private boolean 诶3() {
      for (FunTime.PurchaseEntry var2 : this.сГ) {
         if (var2.enabled()) {
            return true;
         }
      }

      return false;
   }

   public int method_9я() {
      return this.field_4в;
   }

   private void ше() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.connection != null) {
         long var1 = System.currentTimeMillis();
         if (var1 >= this.ЯФ) {
            if (this.field_4必 >= this.сГ.size()) {
               this.ХИ = false;
               this.ВЛ.setValue(false);
            } else {
               FunTime.PurchaseEntry var3 = this.сГ.get(this.field_4必);
               if (this.field_4щ == 0) {
                  MINECRAFT_CLIENT.player.connection.sendCommand("ah search " + л(var3.name()));
                  this.field_4щ = 1;
                  this.ЯО = var1;
               } else {
                  boolean var4 = MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen && this.诶и();
                  if (this.field_4щ == 1) {
                     if (var4 && var1 - this.ЯО >= Му) {
                        this.field_4щ = 2;
                     } else {
                        if (var1 - this.ЯО >= М2) {
                           this.б(var1);
                        }
                     }
                  } else {
                     if (this.field_4щ == 2) {
                        if (var4) {
                           long var5 = this.И(var3);
                           if (var5 > 0L) {
                              byte var7 = 1;
                              long var8 = Math.max(1L, (var5 + var7 - 1L) / var7);
                              var3.setting().setValue((float)FunTimeItemMetadata.Ю(var8, this.field_4в));
                           }

                           MINECRAFT_CLIENT.player.clientSideCloseContainer();
                        }

                        this.б(var1);
                     }
                  }
               }
            }
         }
      } else {
         this.ХИ = false;
      }
   }

   private int Я(AbstractContainerMenu var1) {
      String var2 = MINECRAFT_CLIENT.player.getName().getString();
      int var3 = Math.min(45, this.method_3(var1));

      for (int var4 = 0; var4 < var3; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (this.Ш(var5)) {
            long var6 = FunTimeItemMetadata.Ж(var5);
            if (var6 > 0L) {
               String var8 = FunTimeItemMetadata.П(var5);
               if (var8 == null || !var8.equalsIgnoreCase(var2)) {
                  int var9 = Math.max(1, var5.getCount());
                  long var10 = Math.max(1L, (var6 + var9 - 1L) / var9);
                  FunTime.PurchaseEntry var12 = this.И(var5);
                  if (var12 != null && var10 <= var12.price() && !this.Г(var12.name())) {
                     this.В_ = var12.name();
                     return var4;
                  }
               }
            }
         }
      }

      return -1;
   }

   private String И(double var1) {
      return Math.abs(var1 - Math.rint(var1)) < М必 ? String.format(Locale.ROOT, Мщ, var1) : String.format(Locale.ROOT, МГ, var1).replaceAll(Мл, "");
   }

   public void ш诶() {
      if (this.ХИ) {
         this.ХИ = false;
         this.ВЛ.setValue(false);
         this.ш6();
      } else {
         this.ВЛ.setValue(true);
         this.ХИ = true;
         this.field_4必 = 0;
         this.field_4щ = 0;
         this.ЯО = 0L;
         this.ЯФ = 0L;
      }
   }

   public FunTime() {
      super(ъг, ъ0, ModuleCategory.AUTOBUY);
      this.ВЧ = new BooleanSetting(ъх, false);
      this.ВЛ = new BooleanSetting(ъУ, false);
      this.В诶 = new BooleanSetting(ъЗ, false);
      this.Яи_989fed9a = new FloatSetting(ъЕ, ъП, 0.0F, ъ西).withStep(1.0F);
      this.field_4в = 25;
      this.field_4ъ = -1;
      this.field_4М = -1;
      this.field_4Н = -1;
      this.field_4н = -1;
      this.field_4Э = -1;
      this.сл = new HashMap<>();

      for (FunTimeItemCatalog.CatalogEntry var2 : FunTimeItemCatalog.с0) {
         this.И(var2);
      }

      this.addSettings(this.ВЧ);
      this.addSettings(this.сГ.stream().map(FunTime.PurchaseEntry::setting).toArray(FloatSetting[]::new));
   }

   private void шЕ() {
      this.Яй = 0L;
      this.ЯЖ = 0L;
      this.field_4ъ = -1;
      this.field_4М = -1;
   }

   public void method_9Р(boolean var1) {
      this.Х1 = var1;
      this.ВЧ.setValue(var1);
      this.шЭ();
   }

   public boolean 诶9() {
      return this.Х1;
   }

   public boolean 诶И() {
      return this.ХИ;
   }

   private void Р(AbstractContainerMenu var1, int var2, ContainerInput var3) {
      HashedStack var4 = HashedStack.create(var1.getCarried(), MINECRAFT_CLIENT.player.connection.decoratedHashOpsGenenerator());
      MINECRAFT_CLIENT.player
         .connection
         .send(new ServerboundContainerClickPacket(var1.containerId, var1.getStateId(), (short)var2, (byte)0, var3, Int2ObjectMaps.emptyMap(), var4));
   }

   private synchronized void шч() {
      this.Вз = 0.0;
      this.field_4Ц = 0;
      this.field_4ч = 0;
   }

   private void ш1() {
      boolean var1 = false;
      if (!var1) {
         if (this.Хш) {
            this.ш6();
         }
      } else {
         if (!this.Хш) {
            this.Хш = true;
            this.ЯТ = System.currentTimeMillis();
            this.Вц = MINECRAFT_CLIENT.player.getX();
            this.Вя = MINECRAFT_CLIENT.player.getZ();
         }

         long var2 = Math.max(0L, System.currentTimeMillis() - this.ЯТ);
         boolean var4 = (var2 / Мэ & 1L) == 0L;
         Boolean var5 = this.И_6c785b53(var4);
         if (var5 == null) {
            this.шс();
         } else {
            this.Р(var5, !var5);
         }
      }
   }

   private void шо() {
      if (MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.player.containerMenu != null) {
         if (this.诶3()) {
            long var1 = System.currentTimeMillis();
            if (this.Яй != 0L && var1 - this.Яй >= МЧ || this.ЯЖ != 0L && var1 - this.ЯЖ >= МЛ) {
               this.Х衣 = true;
            }

            if (this.Х衣) {
               this.ь(var1);
            } else if (var1 >= this.Ям) {
               if (MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen var3) {
                  AbstractContainerMenu var7 = var3.getMenu();
                  if (this.И(var3)) {
                     this.шЕ();
                     this.field_4Н = -1;
                     this.field_4н = -1;
                     this.ЯШ = 0L;
                     this.шч();
                     if (var1 - this.Яы >= М诶) {
                        if (var7.slots.size() > 1) {
                           this.Р(var7, 1, ContainerInput.QUICK_MOVE);
                        }

                        this.Яы = var1;
                     }
                  } else if (this.诶и()) {
                     if (this.field_4н != var7.containerId) {
                        long var5 = this.ЯЖ;
                        if (var5 != 0L) {
                           this.ЯЖ = 0L;
                           this.Я("screen:" + this.field_4н + "->" + var7.containerId, var1 - var5);
                           if (this.Х衣) {
                              this.ь(var1);
                              return;
                           }
                        }

                        this.шЕ();
                        this.field_4н = var7.containerId;
                        this.field_4Н = var7.containerId;
                     }

                     if (this.Яй != 0L && this.field_4ъ == var7.containerId && this.field_4М != var7.getStateId()) {
                        this.Р("revision:" + this.field_4М + "->" + var7.getStateId(), var1);
                     }

                     int var8 = this.Я(var7);
                     if (var8 != -1) {
                        this.шЕ();
                        this.ЯШ = 0L;
                        this.шч();
                        if (var1 - this.Я_ >= М1) {
                           this.Р(var7, var8, ContainerInput.QUICK_MOVE);
                           this.Я_ = var1;
                        }
                     } else {
                        if (var7.slots.size() > 49 && this.field_4Н == var7.containerId && var1 - this.ЯШ >= МЬ) {
                           ContainerInput var6 = ThreadLocalRandom.current().nextInt(100) < 25 ? ContainerInput.QUICK_MOVE : ContainerInput.PICKUP;
                           this.Р(var7, 49, var6);
                           this.ЯШ = var1;
                           this.field_4Н = -1;
                           this.Яй = var1;
                           this.ЯЖ = var1;
                           this.field_4ъ = var7.containerId;
                           this.field_4М = var7.getStateId();
                        }
                     }
                  } else {
                     this.шЕ();
                     this.field_4Н = -1;
                     this.field_4н = -1;
                     this.ЯШ = 0L;
                     this.шч();
                     if (var1 - this.Я6 >= М衣) {
                        MINECRAFT_CLIENT.player.clientSideCloseContainer();
                        this.О(var1);
                     }
                  }
               } else {
                  if (MINECRAFT_CLIENT.gui.screen() == null && var1 - this.Я6 >= Мш) {
                     this.О(var1);
                  }
               }
            }
         }
      }
   }

   public record PurchaseEntry(String name, FloatSetting setting, String normalizedName, String[] aliases, FunTimeItemCatalog.CatalogEntry catalogEntry) {
      public ItemStack stack() {
         try {
            ItemStack var1 = FunTimeItemMetadata.method_4(FunTime.л(this.name));
            return var1 != null ? var1.copy() : this.catalogEntry.stack();
         } catch (RuntimeException var2) {
            return ItemStack.EMPTY;
         }
      }

      public long price() {
         return this.setting.getValue().longValue();
      }

      public boolean enabled() {
         return this.price() > 0L;
      }
   }
}
