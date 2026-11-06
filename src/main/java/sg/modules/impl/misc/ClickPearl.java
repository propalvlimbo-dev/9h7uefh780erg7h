package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.render.pipeline.InventoryActions;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionQueue;
import sg.settings.KeybindSetting;

public class ClickPearl extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("р")  KeybindSetting throwKeybindSetting;
   @SerializedName("е")
   private final InventoryActionQueue inventoryActionQueue;
   private @com.google.gson.annotations.SerializedName("Ъх")  int pearlInventorySlot;
   private @com.google.gson.annotations.SerializedName("АК")  boolean throwPending;
   private final @com.google.gson.annotations.SerializedName("еш")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("еИ")  EventListener<InputEvent> inputListener;
   private static final String MODULE_NAME = "ClickPearl";
   private static final String MODULE_DESCRIPTION = "Кидает пёрл по бинду";
   private static final String THROW_KEY_LABEL = "Клавиша бросания";
   private static final String LEGIT_MODE_NAME = "Легитный";
   private static final String PEARL_HOTBAR_ACTION_ID = "pearl";
   private static final String PEARL_INVENTORY_ACTION_ID = "pearl";

   public void ЩЙ(PlayerTickEvent var1) {
      if (this.ьК()) {
         if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(LEGIT_MODE_NAME)) {
            this.в衣();
         } else {
            this.вЖ();
         }
      }
   }

   @Override
   public void onDisable() {
      this.inventoryActionQueue.з_b1d77d5c().clear();
   }

   private boolean ьК() {
      if (!this.throwPending) {
         return false;
      }

      this.pearlInventorySlot = InventoryActions.ш(Items.ENDER_PEARL);
      this.throwPending = false;
      return this.pearlInventorySlot >= 0;
   }

   public ClickPearl() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
      this.throwKeybindSetting = new KeybindSetting(THROW_KEY_LABEL, -1);
      this.inventoryActionQueue = new InventoryActionQueue();
      this.pearlInventorySlot = -1;
      this.throwPending = false;
      this.playerTickListener = this::ЩЙ;
      this.inputListener = this::Ц;
      this.addSettings(this.throwKeybindSetting);
   }

   private void вЖ() {
      if (InventoryActions.б(this.pearlInventorySlot)) {
         InventoryActions.衣(InventoryActions.ч(this.pearlInventorySlot));
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         InventoryActions.衣(MINECRAFT_CLIENT.player.getInventory().selected);
      } else {
         this.inventoryActionQueue.Э_de5a6f78(this.inventoryActionQueue.ф(), this.pearlInventorySlot);
         InventoryActions.衣(this.inventoryActionQueue.ф());
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         InventoryActions.衣(MINECRAFT_CLIENT.player.getInventory().selected);
         this.inventoryActionQueue.Э_de5a6f78(this.inventoryActionQueue.ф(), this.pearlInventorySlot);
      }
   }

   public void Ц(InputEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null && var1.getAction() == 1 && var1.getInputCode() == this.throwKeybindSetting.getValue()) {
         this.throwPending = true;
      }
   }

   private void в衣() {
      int var1 = this.pearlInventorySlot;
      if (InventoryActions.б(var1)) {
         Runnable var2 = () -> {
            InventoryActions.衣(InventoryActions.ч(var1));
            MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         };
         Runnable var3 = () -> InventoryActions.衣(MINECRAFT_CLIENT.player.getInventory().selected);
         this.inventoryActionQueue.з(PEARL_HOTBAR_ACTION_ID, 1, new Runnable[]{var2, var3});
      } else {
         Runnable var4 = () -> {
            this.inventoryActionQueue.Э_de5a6f78(this.inventoryActionQueue.ф(), var1);
            this.inventoryActionQueue.зl();
            InventoryActions.衣(this.inventoryActionQueue.ф());
            MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         };
         Runnable var5 = () -> {
            InventoryActions.衣(MINECRAFT_CLIENT.player.getInventory().selected);
            this.inventoryActionQueue.Э_de5a6f78(this.inventoryActionQueue.ф(), var1);
            this.inventoryActionQueue.зl();
         };
         this.inventoryActionQueue.Б(PEARL_INVENTORY_ACTION_ID, 1, var4, var5);
      }
   }
}
