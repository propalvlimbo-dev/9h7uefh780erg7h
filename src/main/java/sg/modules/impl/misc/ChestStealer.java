package sg.modules.impl.misc;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public final class ChestStealer extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("и")  SingleChoiceSetting modeSetting;
   private final @com.google.gson.annotations.SerializedName("Вс")  FloatSetting delaySetting;
   private final @com.google.gson.annotations.SerializedName("ь")  Set<Identifier> allowedItemIds;
   private final @com.google.gson.annotations.SerializedName("а")  Stopwatch transferTimer;
   private final @com.google.gson.annotations.SerializedName("сЕ")  EventListener<PlayerTickEvent> playerTickListener;
   private @com.google.gson.annotations.SerializedName("ХР")  int activeContainerId;
   private static final String MODULE_NAME = "ChestStealer";
   private static final String MODULE_DESCRIPTION = "Автоматически забирает предметы из контейнера";
   private static final String MODE_LABEL = "Режим";
   private static final String ALL_OPTION_LABEL = "Все";
   private static final String CUSTOM_OPTION_LABEL = "Кастом";
   private static final String DELAY_LABEL = "Задержка";
   private static final float DEFAULT_DELAY_MILLIS = 100.0F;
   private static final float MAX_DELAY_MILLIS = 1000.0F;
   private static final float DELAY_STEP_MILLIS = 10.0F;
   private static final String ALL_MODE_NAME = "Все";

   public boolean Я(Identifier var1) {
      return var1 != null && this.allowedItemIds.remove(var1);
   }

   public void Н(Collection<Identifier> var1) {
      this.allowedItemIds.clear();
      if (var1 != null) {
         this.allowedItemIds.addAll(var1);
      }
   }

   public ChestStealer() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
      this.modeSetting = new SingleChoiceSetting(MODE_LABEL, new ChoiceOption(ALL_OPTION_LABEL), new ChoiceOption(CUSTOM_OPTION_LABEL));
      this.delaySetting = new FloatSetting(DELAY_LABEL, DEFAULT_DELAY_MILLIS, 0.0F, MAX_DELAY_MILLIS).withStep(DELAY_STEP_MILLIS);
      this.allowedItemIds = new LinkedHashSet<>();
      this.transferTimer = new Stopwatch();
      this.playerTickListener = this::у;
      this.activeContainerId = -1;
      this.addSettings(this.modeSetting, this.delaySetting);
   }

   private void у(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.gameMode == null) {
         this.method_4с();
      } else if (!(MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen)) {
         this.method_4с();
      } else if (MINECRAFT_CLIENT.player.containerMenu instanceof ChestMenu var2) {
         if (var2.containerId != this.activeContainerId) {
            this.activeContainerId = var2.containerId;
            this.transferTimer.ВД();
         } else if (this.transferTimer.method_0(this.delaySetting.getValue().longValue())) {
            int var7 = var2.getRowCount() * 9;

            for (int var4 = 0; var4 < var7; var4++) {
               Slot var5 = var2.getSlot(var4);
               if (var5.hasItem() && var5.mayPickup(MINECRAFT_CLIENT.player)) {
                  ItemStack var6 = var5.getItem();
                  if (this.П(var6) && this.Ц(var6)) {
                     MINECRAFT_CLIENT.gameMode.handleContainerInput(var2.containerId, var4, 0, ContainerInput.QUICK_MOVE, MINECRAFT_CLIENT.player);
                     this.transferTimer.ВД();
                     return;
                  }
               }
            }
         }
      } else {
         this.method_4с();
      }
   }

   @Override
   public void onDisable() {
      this.method_4с();
      super.onDisable();
   }

   public boolean method_1(Identifier var1) {
      return var1 != null && this.allowedItemIds.add(var1);
   }

   public List<Identifier> П_2571477d() {
      return List.copyOf(this.allowedItemIds);
   }

   private boolean Ц(ItemStack var1) {
      for (int var2 = 0; var2 < 36; var2++) {
         ItemStack var3 = MINECRAFT_CLIENT.player.getInventory().getItem(var2);
         if (var3.isEmpty()) {
            return true;
         }

         if (ItemStack.isSameItemSameComponents(var1, var3) && var3.getCount() < var3.getMaxStackSize()) {
            return true;
         }
      }

      return false;
   }

   public boolean method_4(Identifier var1) {
      return this.allowedItemIds.contains(var1);
   }

   public void method_4П() {
      this.allowedItemIds.clear();
   }

   private void method_4с() {
      this.activeContainerId = -1;
      this.transferTimer.ВД();
   }

   private boolean П(ItemStack var1) {
      if (this.modeSetting.isSelected(ALL_MODE_NAME)) {
         return true;
      }

      Identifier var2 = BuiltInRegistries.ITEM.getKey(var1.getItem());
      return this.allowedItemIds.contains(var2);
   }

   @Override
   public void onEnable() {
      this.transferTimer.ВД();
      this.activeContainerId = -1;
      super.onEnable();
   }
}
