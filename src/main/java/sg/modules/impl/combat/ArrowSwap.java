package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.ArrowSwapScreen;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionCoordinator;
import sg.settings.KeybindSetting;
import sg.modules.impl.init.NotificationManager;

public class ArrowSwap extends ClientModule {
   public static final int ХК = 2;
   public static final int ХI = 6;
   private static final int ХТ = 9;
   private static final int ХО = 36;
   private final @com.google.gson.annotations.SerializedName("Ц")  KeybindSetting swapKeybindSetting;
   private final @com.google.gson.annotations.SerializedName("я")  List<ItemStack> presetArrows;
   private final @com.google.gson.annotations.SerializedName("сс")  List<String> presetNames;
   @SerializedName("Х")
   private final InventoryActionCoordinator actionCoordinator;
   private @com.google.gson.annotations.SerializedName("В")  ArrowSwapScreen activeScreen;
   private @com.google.gson.annotations.SerializedName("ХФ")  int pendingSwapSlot;
   private final @com.google.gson.annotations.SerializedName("сП")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("с西")  EventListener<InputEvent> inputListener;
   private final @com.google.gson.annotations.SerializedName("сд")  EventListener<HudRenderEvent> hudRenderListener;
   private static final String сшЮ = "ArrowSwap";
   private static final String сшЧ = "Колесо для свапа стрел в инвентаре";
   private static final String сшЛ = "Клавиша свапа";
   private static final String сш诶 = "Легитный";
   private static final String сш1 = "Рейджовый";
   private static final String сшЬ = "M";
   private static final String сш衣 = "Стрела в инве";

   private void бь() {
      if (this.pendingSwapSlot != -1) {
         this.б4();
         this.бу();
         this.pendingSwapSlot = -1;
      }
   }

   private void Ю(HudRenderEvent var1) {
      if (this.activeScreen != null && var1.getGraphics() != null) {
         if (this.activeScreen.renderClosing(var1.getGraphics())) {
            this.activeScreen = null;
         }
      }
   }

   public int 诶г() {
      return this.presetArrows.size();
   }

   public ItemStack ч(int var1) {
      return var1 >= 0 && var1 < this.presetArrows.size() ? this.presetArrows.get(var1) : ItemStack.EMPTY;
   }

   private int method_1_a83dd0cc(ItemStack var1, String var2) {
      if (var1 != null && !var1.isEmpty() && MINECRAFT_CLIENT.player != null) {
         int var3 = this.诶Ж();
         NonNullList var4 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems();

         for (int var5 = 0; var5 < var4.size(); var5++) {
            int var6 = this.н(var5);
            if (var6 != var3) {
               ItemStack var7 = (ItemStack)var4.get(var5);
               if (this.Ш(var7, var1, var2)) {
                  return var6;
               }
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public int 诶Ф() {
      if (this.presetArrows.size() >= 6) {
         return Math.max(0, this.presetArrows.size() - 1);
      }

      this.presetArrows.add(ItemStack.EMPTY);
      this.presetNames.add("");
      return this.presetArrows.size() - 1;
   }

   private void бу() {
      ItemStack var1 = MINECRAFT_CLIENT.player.getInventory().getItem(this.诶Ж());
      if (!var1.isEmpty()) {
         NotificationManager.р(сшЬ, var1.getHoverName(), сш衣, -1, var1);
      }
   }

   private int 诶х() {
      return this.诶Ж();
   }

   public boolean с_bbeebd87(int var1) {
      return var1 >= 0
         && var1 < this.presetArrows.size()
         && !this.presetArrows.get(var1).isEmpty()
         && (this.method_1_bbeebd87(this.presetArrows.get(var1), this.presetNames.get(var1)) || this.method_1_a83dd0cc(this.presetArrows.get(var1), this.presetNames.get(var1)) != -1);
   }

   public void O(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null) {
         if (this.pendingSwapSlot != -1) {
            if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(сш诶)) {
               this.б5();
            } else if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(сш1)) {
               this.бь();
            }
         }
      }
   }

   private int 诶Ж() {
      if (MINECRAFT_CLIENT.player == null) {
         return 9;
      }

      for (int var1 = 9; var1 < 36; var1++) {
         if (Ф(MINECRAFT_CLIENT.player.getInventory().getItem(var1))) {
            return var1;
         }
      }

      return 9;
   }

   public void т(int var1) {
      if (this.presetArrows.size() > 2) {
         int var2 = var1 >= 0 && var1 < this.presetArrows.size() ? var1 : this.presetArrows.size() - 1;
         this.presetArrows.remove(var2);
         this.presetNames.remove(var2);
      }
   }

   private void бГ() {
      if (this.presetArrows.isEmpty()) {
         for (int var1 = 0; var1 < 3; var1++) {
            this.presetArrows.add(ItemStack.EMPTY);
            this.presetNames.add("");
         }
      }
   }

   private void и(int var1, int var2) {
      MultiPlayerGameMode var3 = MINECRAFT_CLIENT.gameMode;
      LocalPlayer var4 = MINECRAFT_CLIENT.player;
      int var5 = var4.inventoryMenu.containerId;
      var3.handleContainerInput(var5, var1, 0, ContainerInput.PICKUP, var4);
      var3.handleContainerInput(var5, var2, 0, ContainerInput.PICKUP, var4);
      var3.handleContainerInput(var5, var1, 0, ContainerInput.PICKUP, var4);
   }

   public void Ш(int var1, ItemStack var2) {
      if (var1 >= 0 && var1 < this.presetArrows.size() && var2 != null && !var2.isEmpty() && Ф(var2)) {
         ItemStack var3 = var2.copy();
         var3.setCount(1);
         this.presetArrows.set(var1, var3);
         this.presetNames.set(var1, var2.getHoverName().getString());
      }
   }

   private boolean Ш(ItemStack var1, ItemStack var2, String var3) {
      return ItemStack.isSameItemSameComponents(var1, var2) || !var3.isBlank() && var1.getHoverName().getString().equalsIgnoreCase(var3);
   }

   private int н(int var1) {
      return var1 < 9 ? var1 + 36 : var1;
   }

   public ArrowSwap() {
      super(сшЮ, сшЧ, ModuleCategory.COMBAT);
      this.swapKeybindSetting = new KeybindSetting(сшЛ, -1);
      this.presetArrows = new ArrayList<>();
      this.presetNames = new ArrayList<>();
      this.actionCoordinator = new InventoryActionCoordinator();
      this.pendingSwapSlot = -1;
      this.playerTickListener = this::O;
      this.inputListener = this::с;
      this.hudRenderListener = this::Ю;
      this.addSettings(this.swapKeybindSetting);
      this.бГ();
   }

   private void б5() {
      if (this.pendingSwapSlot != -1 && this.actionCoordinator.л(this::б4, true) == InventoryActionCoordinator.Result.SUCCESS) {
         this.бу();
         this.pendingSwapSlot = -1;
      }
   }

   public void с(InputEvent var1) {
      if (var1.getInputCode() == this.swapKeybindSetting.getValue() && this.swapKeybindSetting.getValue() >= 0) {
         if (var1.getAction() == 1 && MINECRAFT_CLIENT.gui.screen() == null) {
            this.бГ();
            MINECRAFT_CLIENT.gui.setScreen(new ArrowSwapScreen(this, this.swapKeybindSetting.getValue()));
         } else if (var1.getAction() == 0 && MINECRAFT_CLIENT.gui.screen() instanceof ArrowSwapScreen var2) {
            var2.finish(true);
         }
      }
   }

   private boolean method_1_bbeebd87(ItemStack var1, String var2) {
      if (MINECRAFT_CLIENT.player != null && var1 != null && !var1.isEmpty()) {
         ItemStack var3 = MINECRAFT_CLIENT.player.getInventory().getItem(this.诶Ж());
         return var3.isEmpty() ? false : this.Ш(var3, var1, var2);
      } else {
         return false;
      }
   }

   public void Ш(ArrowSwapScreen var1) {
      this.activeScreen = var1;
   }

   public static boolean Ф(ItemStack var0) {
      return var0 != null && (var0.is(Items.ARROW) || var0.is(Items.SPECTRAL_ARROW) || var0.is(Items.TIPPED_ARROW));
   }

   public ArrowSwapScreen Ш_a217576d() {
      return this.activeScreen;
   }

   private void б4() {
      int var1 = this.pendingSwapSlot;
      int var2 = this.诶х();
      if (var1 >= 0 && var2 >= 0 && var1 != var2) {
         this.и(var1, var2);
      }
   }

   @Override
   public void onDisable() {
      if (MINECRAFT_CLIENT.gui.screen() instanceof ArrowSwapScreen var1) {
         var1.finish(false);
      }

      this.activeScreen = null;
      this.pendingSwapSlot = -1;
   }

   public void С(int var1) {
      if (var1 >= 0 && var1 < this.presetArrows.size() && !this.presetArrows.get(var1).isEmpty()) {
         if (!this.method_1_bbeebd87(this.presetArrows.get(var1), this.presetNames.get(var1))) {
            int var2 = this.method_1_a83dd0cc(this.presetArrows.get(var1), this.presetNames.get(var1));
            if (var2 != -1) {
               this.pendingSwapSlot = var2;
            }
         }
      }
   }
}
