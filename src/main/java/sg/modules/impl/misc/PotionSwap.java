package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.render.pipeline.InventoryActions;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionCoordinator;
import sg.settings.KeybindSetting;
import sg.modules.impl.init.PotionSwapScreen;

public final class PotionSwap extends ClientModule {
   private static final int ДЦ = 8;
   @SerializedName("7")
   private final KeybindSetting field_7;
   private final List<ItemStack> ск;
   @SerializedName("В")
   private PotionSwap.ThrowStage В_d5099813;
   private int Дч;
   private int ДЭ;
   private int Д必;
   private boolean еР;
   private boolean е3;
   @SerializedName("В")
   private PotionSwapScreen В_3f8af30e;
   @SerializedName("п")
   private final InventoryActionCoordinator п_at_aef0ee80;
   private final EventListener<InputEvent> Х1;
   private final EventListener<PlayerTickEvent> ХЬ;
   private final EventListener<HudRenderEvent> Х衣;
   private static final String сЧН = "PotionSwap";
   private static final String сЧн = "Колесо для быстрого выбора и броска зелий";
   private static final String сЧЦ = "Колесо";

   public void 西з(int var1) {
      if (this.хю() && this.В_d5099813 == PotionSwap.ThrowStage.IDLE && var1 >= 0 && var1 < this.ск.size()) {
         int var2 = this.з(this.ск.get(var1));
         if (var2 != -1) {
            this.Дч = var2;
            this.ДЭ = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
            this.еР = InventoryActions.б(var2);
            this.В_d5099813 = PotionSwap.ThrowStage.EQUIP;
         }
      }
   }

   public PotionSwap() {
      super(сЧН, сЧн, ModuleCategory.MISC);
      this.field_7 = new KeybindSetting(сЧЦ, -1);
      this.ск = new ArrayList<>();
      this.В_d5099813 = PotionSwap.ThrowStage.IDLE;
      this.Дч = -1;
      this.ДЭ = -1;
      this.п_at_aef0ee80 = new InventoryActionCoordinator();
      this.Х1 = this::Ы;
      this.ХЬ = this::西н;
      this.Х衣 = this::Ш;
      this.addSettings(this.field_7);
   }

   public int мИ() {
      if (this.ск.size() >= 8) {
         return Math.max(0, this.ск.size() - 1);
      }

      this.ск.add(ItemStack.EMPTY);
      return this.ск.size() - 1;
   }

   private boolean м(List<ItemStack> var1, ItemStack var2) {
      for (ItemStack var4 : var1) {
         if (ItemStack.isSameItemSameComponents(var4, var2)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onDisable() {
      if (MINECRAFT_CLIENT.gui.screen() instanceof PotionSwapScreen var1) {
         var1.finish(false);
      }

      this.В_3f8af30e = null;
      if (this.В_d5099813 != PotionSwap.ThrowStage.IDLE && this.е3 && this.хю()) {
         if (this.еР) {
            InventoryActions.衣(this.ДЭ);
         } else if (this.Дч >= 0) {
            InventoryActions.method_7(8, this.Дч);
         }
      }

      this.O必();
   }

   private void Oй() {
      if (this.еР) {
         InventoryActions.衣(this.ДЭ);
         this.O必();
      } else {
         if (this.п_at_aef0ee80.л(() -> InventoryActions.method_7(8, this.Дч), true) == InventoryActionCoordinator.Result.SUCCESS) {
            InventoryActions.衣(this.ДЭ);
            this.O必();
         }
      }
   }

   public List<ItemStack> 西Т() {
      return this.ск;
   }

   public boolean щ(ItemStack var1) {
      return this.з(var1) != -1;
   }

   private void O4() {
      if (this.ск.isEmpty()) {
         List var1 = this.西Ч();

         for (int var2 = 0; var2 < 3; var2++) {
            this.ск.add(var2 < var1.size() ? (ItemStack)var1.get(var2) : ItemStack.EMPTY);
         }
      }
   }

   private void 西н(PlayerTickEvent var1) {
      if (this.В_d5099813 != PotionSwap.ThrowStage.IDLE && this.хю()) {
         if (this.Д必 > 0) {
            this.Д必--;
         } else {
            switch (this.В_d5099813) {
               case EQUIP:
                  this.OМ();
                  break;
               case THROW:
                  this.OI();
                  break;
               case RESTORE:
                  this.Oй();
            }
         }
      }
   }

   private void OI() {
      MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
      this.Д必 = 1;
      this.В_d5099813 = PotionSwap.ThrowStage.RESTORE;
   }

   private boolean ъ(ItemStack var1) {
      return var1 != null && (var1.is(Items.SPLASH_POTION) || var1.is(Items.LINGERING_POTION));
   }

   private void O必() {
      this.В_d5099813 = PotionSwap.ThrowStage.IDLE;
      this.Дч = -1;
      this.ДЭ = -1;
      this.Д必 = 0;
      this.еР = false;
      this.е3 = false;
   }

   public PotionSwapScreen м_3f8af30e() {
      return this.В_3f8af30e;
   }

   private boolean хю() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null;
   }

   private int з(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && this.хю()) {
         NonNullList var2 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems();

         for (int var3 = 0; var3 < var2.size(); var3++) {
            if (ItemStack.isSameItemSameComponents((ItemStack)var2.get(var3), var1)) {
               return var3 < 9 ? var3 + 36 : var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public void К(int var1, ItemStack var2) {
      if (var1 >= 0 && var1 < this.ск.size() && this.ъ(var2)) {
         ItemStack var3 = var2.copy();
         var3.setCount(1);
         this.ск.set(var1, var3);
      }
   }

   public void м(PotionSwapScreen var1) {
      this.В_3f8af30e = var1;
   }

   private void Ш(HudRenderEvent var1) {
      if (this.В_3f8af30e != null && var1.getGraphics() != null) {
         if (this.В_3f8af30e.renderClosing(var1.getGraphics())) {
            this.В_3f8af30e = null;
         }
      }
   }

   public void 西O(int var1) {
      if (var1 >= 0 && var1 < this.ск.size() && this.ск.size() > 2) {
         this.ск.remove(var1);
      }
   }

   public List<ItemStack> 西Ч() {
      ArrayList var1 = new ArrayList();
      if (!this.хю()) {
         return var1;
      }

      for (ItemStack var3 : MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems()) {
         if (this.ъ(var3) && !this.м(var1, var3)) {
            ItemStack var4 = var3.copy();
            var4.setCount(1);
            var1.add(var4);
         }
      }

      return var1;
   }

   private void Ы(InputEvent var1) {
      if (var1.getInputCode() == this.field_7.getValue() && this.field_7.getValue() >= 0) {
         if (var1.getAction() == 1 && MINECRAFT_CLIENT.gui.screen() == null && this.хю() && this.В_d5099813 == PotionSwap.ThrowStage.IDLE) {
            this.O4();
            MINECRAFT_CLIENT.gui.setScreen(new PotionSwapScreen(this, this.field_7.getValue()));
         } else if (var1.getAction() == 0 && MINECRAFT_CLIENT.gui.screen() instanceof PotionSwapScreen var2) {
            var2.finish(true);
         }
      }
   }

   private void OМ() {
      if (this.еР) {
         InventoryActions.衣(InventoryActions.ч(this.Дч));
         this.е3 = true;
         this.Д必 = 1;
         this.В_d5099813 = PotionSwap.ThrowStage.THROW;
      } else {
         if (this.п_at_aef0ee80.л(() -> InventoryActions.method_7(8, this.Дч), true) == InventoryActionCoordinator.Result.SUCCESS) {
            InventoryActions.衣(8);
            this.е3 = true;
            this.Д必 = 1;
            this.В_d5099813 = PotionSwap.ThrowStage.THROW;
         }
      }
   }

   private enum ThrowStage {
      IDLE,
      EQUIP,
      THROW,
      RESTORE;
   }
}
