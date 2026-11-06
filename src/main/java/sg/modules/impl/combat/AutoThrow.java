package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.ec.EntityHealth;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionCoordinator;
import sg.settings.KeybindSetting;
import sg.settings.SingleChoiceSetting;

public final class AutoThrow extends ClientModule {
   private static final String Вп = "Автоматический";
   private static final String ВД = "По бинду";
   private static final long ВЭ = 5000000000L;
   @SerializedName("6")
   private final SingleChoiceSetting modeSetting;
   private final @com.google.gson.annotations.SerializedName("Э")  KeybindSetting throwKeybindSetting;
   private final @com.google.gson.annotations.SerializedName("Вж")  FloatSetting targetHealthSetting;
   private final @com.google.gson.annotations.SerializedName("В弟")  FloatSetting targetDistanceSetting;
   @SerializedName("е")
   private final InventoryActionCoordinator actionCoordinator;
   private final @com.google.gson.annotations.SerializedName("ВЫ")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("Вl")  EventListener<InputEvent> inputListener;
   private @com.google.gson.annotations.SerializedName("В")  AutoThrow.ThrowStage throwStage;
   private @com.google.gson.annotations.SerializedName("еь")  int potionInventorySlot;
   private @com.google.gson.annotations.SerializedName("еж")  int previousHotbarSlot;
   private @com.google.gson.annotations.SerializedName("е弟")  int stageDelayTicks;
   private @com.google.gson.annotations.SerializedName("еб")  int stageElapsedTicks;
   private @com.google.gson.annotations.SerializedName("Ят")  boolean potionInHotbar;
   private @com.google.gson.annotations.SerializedName("Яа")  boolean swappedFromInventory;
   private @com.google.gson.annotations.SerializedName("В必")  long lastThrowNanos;
   private static final String ВфЯ = "Auto Throw";
   private static final String ВфХ = "Автоматически бросает взрывное зелье исцеления";
   private static final String Вфе = "Режим";
   private static final String ВфА = "Автоматический";
   private static final String Вф4 = "По бинду";
   private static final String Вфп = "Кнопка";
   private static final String ВфД = "Здоровье цели";
   private static final float ВфЪ = 15.0F;
   private static final float Вфт = 20.0F;
   private static final float Вфа = 0.5F;
   private static final String ВфЮ = "Дистанция";
   private static final float ВфЧ = 3.0F;
   private static final float ВфЛ = 6.0F;
   private static final float Вф诶 = 0.1F;
   private static final String Вф1 = "По бинду";
   private static final String ВфЬ = "Автоматический";
   private static final long Вф衣 = 5000000000L;
   private static final String Вфш = "В инвентаре нет взрывного зелья исцеления.";
   private static final double ВфИ = 0.15;
   private static final String Вфь = "Автоматический";
   private static final String Вфж = "Автоматический";
   private static final String Вф弟 = "По бинду";

   public AutoThrow() {
      super(ВфЯ, ВфХ, ModuleCategory.COMBAT);
      this.modeSetting = new SingleChoiceSetting(Вфе, new ChoiceOption(ВфА), new ChoiceOption(Вф4));
      this.throwKeybindSetting = new KeybindSetting(Вфп, () -> this.modeSetting.isSelected(Вф弟));
      this.targetHealthSetting = new FloatSetting(ВфД, ВфЪ, 1.0F, Вфт).withStep(Вфа).visibleWhen(() -> this.modeSetting.isSelected(Вфж));
      this.targetDistanceSetting = new FloatSetting(ВфЮ, ВфЧ, 1.0F, ВфЛ).withStep(Вф诶).visibleWhen(() -> this.modeSetting.isSelected(Вфь));
      this.actionCoordinator = new InventoryActionCoordinator();
      this.playerTickListener = this::ПБ;
      this.inputListener = this::弟;
      this.throwStage = AutoThrow.ThrowStage.IDLE;
      this.potionInventorySlot = -1;
      this.previousHotbarSlot = -1;
      this.addSettings(this.modeSetting, this.throwKeybindSetting, this.targetHealthSetting, this.targetDistanceSetting);
   }

   private void ПБ(PlayerTickEvent var1) {
      if (!this.нД()) {
         this.ПП(false);
      } else if (this.throwStage != AutoThrow.ThrowStage.IDLE) {
         this.БД();
      } else if (this.modeSetting.isSelected(ВфЬ) && MINECRAFT_CLIENT.gui.screen() == null && System.nanoTime() - this.lastThrowNanos >= Вф衣) {
         LivingEntity var2 = RoseClient.getInstance().getServices().getModules().getAura().Ж_95bedd6e();
         if (var2 instanceof Player
            && var2.isAlive()
            && this.Ф(var2)
            && !(EntityHealth.getHealth(var2) + var2.getAbsorptionAmount() >= this.targetHealthSetting.getValue())
            && !(MINECRAFT_CLIENT.player.distanceTo(var2) > this.targetDistanceSetting.getValue())
            && this.Ъ(var2)) {
            this.Ю(false);
         }
      }
   }

   private void БЪ() {
      if (!this.swappedFromInventory) {
         this.г(this.previousHotbarSlot);
         this.ПП(false);
      } else {
         if (this.actionCoordinator.л(() -> MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, this.potionInventorySlot, 8, ContainerInput.SWAP, MINECRAFT_CLIENT.player), true)
            == InventoryActionCoordinator.Result.SUCCESS) {
            this.swappedFromInventory = false;
            this.г(this.previousHotbarSlot);
            this.ПП(false);
         }
      }
   }

   private void 弟(InputEvent var1) {
      if (this.modeSetting.isSelected(Вф1) && var1.getAction() == 1 && this.throwKeybindSetting.getValue() >= 0 && var1.getInputCode() == this.throwKeybindSetting.getValue() && MINECRAFT_CLIENT.gui.screen() == null) {
         this.Ю(true);
      }
   }

   private void ПП(boolean var1) {
      if (var1 && this.нД()) {
         if (this.swappedFromInventory && this.potionInventorySlot >= 9) {
            MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, this.potionInventorySlot, 8, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         }

         this.г(this.previousHotbarSlot);
      }

      this.actionCoordinator.яш();
      this.throwStage = AutoThrow.ThrowStage.IDLE;
      this.potionInventorySlot = -1;
      this.previousHotbarSlot = -1;
      this.stageDelayTicks = 0;
      this.stageElapsedTicks = 0;
      this.potionInHotbar = false;
      this.swappedFromInventory = false;
   }

   private boolean Ъ(LivingEntity var1) {
      Vec3 var2 = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 var3 = var2.add(MINECRAFT_CLIENT.player.getViewVector(1.0F).scale(this.targetDistanceSetting.getValue() + 1.0F));
      AABB var4 = var1.getBoundingBox().inflate(ВфИ);
      return var4.contains(var2) || var4.clip(var2, var3).isPresent();
   }

   private void г(int var1) {
      if (var1 >= 0 && var1 <= 8 && MINECRAFT_CLIENT.player.getInventory().getSelectedSlot() != var1) {
         MINECRAFT_CLIENT.player.getInventory().selected = var1;
         MINECRAFT_CLIENT.player.connection.send(new ServerboundSetCarriedItemPacket(var1));
      }
   }

   private int Пъ() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (this.ч(MINECRAFT_CLIENT.player.getInventory().getItem(var1))) {
            return var1;
         }
      }

      for (int var2 = 9; var2 < 36; var2++) {
         if (this.ч(MINECRAFT_CLIENT.player.getInventory().getItem(var2))) {
            return var2;
         }
      }

      return -1;
   }

   @Override
   public void onDisable() {
      this.ПП(true);
   }

   private void БД() {
      if (++this.stageElapsedTicks > 60 || MINECRAFT_CLIENT.player.isDeadOrDying()) {
         this.ПП(true);
      } else if (this.stageDelayTicks > 0) {
         this.stageDelayTicks--;
      } else {
         switch (this.throwStage) {
            case EQUIP:
               this.Б弟();
               break;
            case THROW:
               this.БФ();
               break;
            case RESTORE:
               this.БЪ();
         }
      }
   }

   private boolean Ф(LivingEntity var1) {
      return var1.isUsingItem() && var1.getTicksUsingItem() >= 10 && !var1.getUseItem().is(Items.SPLASH_POTION) && this.Й(var1.getUseItem());
   }

   private boolean нД() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.getConnection() != null;
   }

   private boolean ч(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && var1.is(Items.SPLASH_POTION) && this.Й(var1);
   }

   private void Б弟() {
      if (this.potionInHotbar) {
         this.г(this.potionInventorySlot);
         this.stageDelayTicks = 1;
         this.throwStage = AutoThrow.ThrowStage.THROW;
      } else {
         if (this.actionCoordinator.л(() -> {
            MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, this.potionInventorySlot, 8, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
            this.swappedFromInventory = true;
         }, true) == InventoryActionCoordinator.Result.SUCCESS) {
            this.г(8);
            this.stageDelayTicks = 1;
            this.throwStage = AutoThrow.ThrowStage.THROW;
         }
      }
   }

   private void Ю(boolean var1) {
      if (this.нД() && MINECRAFT_CLIENT.gui.screen() == null && this.throwStage == AutoThrow.ThrowStage.IDLE) {
         int var2 = this.Пъ();
         if (var2 < 0) {
            if (var1) {
               ClientChat.З(Вфш);
            }
         } else {
            this.potionInventorySlot = var2;
            this.previousHotbarSlot = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
            this.potionInHotbar = var2 < 9;
            this.swappedFromInventory = false;
            this.stageDelayTicks = 0;
            this.stageElapsedTicks = 0;
            this.throwStage = AutoThrow.ThrowStage.EQUIP;
         }
      }
   }

   private boolean Й(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         PotionContents var2 = var1.get(DataComponents.POTION_CONTENTS);
         if (var2 == null) {
            return false;
         }

         for (MobEffectInstance var4 : var2.getAllEffects()) {
            if (var4.getEffect().equals(MobEffects.INSTANT_HEALTH)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      this.lastThrowNanos = 0L;
      this.ПП(false);
   }

   private void БФ() {
      if (!this.ч(MINECRAFT_CLIENT.player.getInventory().getSelectedItem())) {
         this.ПП(true);
      } else {
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         this.lastThrowNanos = System.nanoTime();
         this.stageDelayTicks = 1;
         this.throwStage = AutoThrow.ThrowStage.RESTORE;
      }
   }

   private enum ThrowStage {
      IDLE,
      EQUIP,
      THROW,
      RESTORE;
   }
}
