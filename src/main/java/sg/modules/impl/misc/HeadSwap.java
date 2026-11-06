package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.render.pipeline.InventoryActions;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionCoordinator;
import sg.settings.KeybindSetting;

public class HeadSwap extends ClientModule {
   private static final int field_4衣 = 5;
   private final KeybindSetting л;
   @SerializedName("1")
   private ItemStack field_1;
   private String В7;
   @SerializedName("А")
   private final InventoryActionCoordinator А_at_66902f7a;
   private boolean ХЪ;
   @SerializedName("4ш")
   private int field_4ш;
   private ItemStack Ь;
   private final EventListener<PlayerTickEvent> Яш;
   private final EventListener<InputEvent> ЯИ;
   private static final String шУ = "HeadSwap";
   private static final String шЗ = "Свапает добавленный предмет на голову и обратно";
   private static final String шЕ = "Бинд";
   private static final String шП = "Легитный";

   public HeadSwap() {
      super(шУ, шЗ, ModuleCategory.MISC);
      this.л = new KeybindSetting(шЕ, -1);
      this.field_1 = ItemStack.EMPTY;
      this.В7 = "";
      this.А_at_66902f7a = new InventoryActionCoordinator();
      this.field_4ш = -1;
      this.Ь = ItemStack.EMPTY;
      this.Яш = this::НЦ;
      this.ЯИ = this::Ш;
      this.addSettings(this.л);
   }

   private int НЙ() {
      for (int var1 = 0; var1 < MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().size(); var1++) {
         if (this.п(MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var1))) {
            return Ц(var1);
         }
      }

      return -1;
   }

   private int НI() {
      if (!this.Ь.isEmpty()) {
         int var1 = this.ж(this.Ь);
         if (var1 >= 0) {
            this.Ь = ItemStack.EMPTY;
            return var1;
         }
      }

      int var2 = this.НК();
      this.Ь = ItemStack.EMPTY;
      return var2;
   }

   private boolean ЧК() {
      return this.п(MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.HEAD));
   }

   public void лк() {
      this.field_1 = ItemStack.EMPTY;
      this.В7 = "";
      this.Ь = ItemStack.EMPTY;
   }

   private int НК() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var1).isEmpty()) {
            return var1 + 36;
         }
      }

      for (int var2 = 9; var2 < 36; var2++) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var2).isEmpty()) {
            return var2;
         }
      }

      return -1;
   }

   public void к(Identifier var1, String var2) {
      if (var1 != null && BuiltInRegistries.ITEM.containsKey(var1) && var2 != null && !var2.isBlank()) {
         Item var3 = BuiltInRegistries.ITEM.getValue(var1);
         this.field_1 = new ItemStack(var3);
         this.В7 = var2;
      } else {
         this.лк();
      }
   }

   public boolean ЧЙ() {
      return this.field_1 != null && !this.field_1.isEmpty() && !this.В7.isBlank();
   }

   @Generated
   public String Лш() {
      return this.В7;
   }

   private int ж(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         for (int var2 = 0; var2 < MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().size(); var2++) {
            ItemStack var3 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var2);
            if (ItemStack.isSameItemSameComponents(var3, var1)) {
               return Ц(var2);
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public boolean method_9(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         ItemStack var2 = var1.copy();
         var2.setCount(1);
         this.field_1 = var2;
         this.В7 = var1.getHoverName().getString();
         this.Ь = ItemStack.EMPTY;
         return true;
      } else {
         return false;
      }
   }

   private static int Ц(int var0) {
      return var0 < 9 ? var0 + 36 : var0;
   }

   @Generated
   public ItemStack ж() {
      return this.field_1;
   }

   private void _О() {
      if (InventoryActions.б(this.field_4ш)) {
         if (this.А_at_66902f7a.л(() -> InventoryActions.method_7(InventoryActions.ч(this.field_4ш), 5), true)
            == InventoryActionCoordinator.Result.SUCCESS) {
            this.field_4ш = -1;
         }
      } else if (this.А_at_66902f7a.л(this::лН, true) == InventoryActionCoordinator.Result.SUCCESS) {
         this.field_4ш = -1;
      }
   }

   private void Ш(InputEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null) {
         if (this.л.getValue() >= 0) {
            if (var1.getAction() == 1 && var1.getInputCode() == this.л.getValue()) {
               this.ХЪ = true;
            }
         }
      }
   }

   @Override
   public void onDisable() {
      this.ХЪ = false;
      this.field_4ш = -1;
      this.Ь = ItemStack.EMPTY;
   }

   private boolean п(ItemStack var1) {
      return this.ЧЙ() && var1 != null && !var1.isEmpty()
         ? ItemStack.isSameItemSameComponents(var1, this.field_1) || var1.getHoverName().getString().equalsIgnoreCase(this.В7)
         : false;
   }

   private void _а() {
      if (this.ХЪ) {
         this.ХЪ = false;
         if (this.ЧЙ()) {
            if (this.ЧК()) {
               this.field_4ш = this.НI();
            } else {
               this.Ь = MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.HEAD).copy();
               this.field_4ш = this.НЙ();
            }
         }
      }
   }

   private void НЦ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (MINECRAFT_CLIENT.gui.screen() == null) {
            this._а();
            if (this.field_4ш >= 0) {
               if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(шП)) {
                  this._О();
               } else {
                  this._Ы();
               }
            }
         }
      }
   }

   private void _Ы() {
      if (InventoryActions.б(this.field_4ш)) {
         InventoryActions.method_7(InventoryActions.ч(this.field_4ш), 5);
      } else {
         this.лН();
      }

      this.field_4ш = -1;
   }

   public Identifier л() {
      return !this.ЧЙ() ? null : BuiltInRegistries.ITEM.getKey(this.field_1.getItem());
   }

   private void лН() {
      InventoryActions.method_7(8, this.field_4ш);
      InventoryActions.method_7(8, 5);
      InventoryActions.method_7(8, this.field_4ш);
   }
}
