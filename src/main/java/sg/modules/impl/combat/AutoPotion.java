package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket.Handler;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.AABB;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.network.InventoryInteractions;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.rotation.FixedAngleRotation;
import sg.settings.MultiChoiceSetting;

public final class AutoPotion extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("Я衣")  BooleanSetting pvpOnlySetting;
   private final @com.google.gson.annotations.SerializedName("ч")  MultiChoiceSetting potionChoicesSetting;
   @SerializedName("4")
   private final FixedAngleRotation throwRotation;
   private final @com.google.gson.annotations.SerializedName("ХК")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("ХI")  EventListener<PacketReceiveEvent> packetReceiveListener;
   private @com.google.gson.annotations.SerializedName("ЪХ")  int ticksSinceThrow;
   private @com.google.gson.annotations.SerializedName("Ат")  boolean throwRotationScheduled;
   private @com.google.gson.annotations.SerializedName("Аа")  boolean pvpActive;
   @SerializedName("1")
   private UUID pvpBossBarId;
   private static final String сOЩ = "Auto Potion";
   private static final String сOу = "Автоматически использует зелья, бросая их под себя";
   private static final String сO2 = "Только при пвп";
   private static final String сOф = "Что кидать?";
   private static final String сOи = "Силка";
   private static final String сO9 = "Скорка";
   private static final String сOо = "Огнестойка";
   private static final String сOO = "Исцеление";
   private static final String сO7 = "Скорка";
   private static final String сOР = "Силка";
   private static final String сO3 = "Огнестойка";
   private static final String сOр = "Исцеление";
   private static final float сO6 = 90.0F;
   private static final float сO_ = 90.0F;
   private static final double сOШ = -0.5;

   private int method_0(boolean var1, Holder<MobEffect> var2) {
      int var3 = var1 ? 9 : 36;

      for (int var4 = 0; var4 < var3; var4++) {
         ItemStack var5 = MINECRAFT_CLIENT.player.getInventory().getItem(var4);
         if (this.method_1(var5, var2)) {
            return var4;
         }
      }

      return -1;
   }

   private boolean method_85() {
      if (MINECRAFT_CLIENT.player.getY() < 0.0) {
         return false;
      }

      AABB var1 = MINECRAFT_CLIENT.player.getBoundingBox().move(0.0, сOШ, 0.0);
      return !MINECRAFT_CLIENT.level.getBlockCollisions(MINECRAFT_CLIENT.player, var1).iterator().hasNext();
   }

   private void method_0(List<Integer> var1, boolean var2) {
      if (var2 && !(MINECRAFT_CLIENT.player.getHealth() >= MINECRAFT_CLIENT.player.getMaxHealth())) {
         this.method_0(var1, MobEffects.INSTANT_HEALTH);
      }
   }

   @Override
   public void onEnable() {
      this.ticksSinceThrow = 20;
      this.throwRotationScheduled = false;
      super.onEnable();
   }

   @Override
   public void onDisable() {
      this.ъб();
      this.ticksSinceThrow = 20;
      super.onDisable();
   }

   private void method_0(List<Integer> var1, boolean var2, Holder<MobEffect> var3) {
      if (var2 && !MINECRAFT_CLIENT.player.hasEffect(var3)) {
         this.method_0(var1, var3);
      }
   }

   private void вХ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
         this.ticksSinceThrow++;
         ArrayList<Integer> var2 = new ArrayList<>();
         this.method_0(var2, this.potionChoicesSetting.isSelected(сO7), MobEffects.SPEED);
         this.method_0(var2, this.potionChoicesSetting.isSelected(сOР), MobEffects.STRENGTH);
         this.method_0(var2, this.potionChoicesSetting.isSelected(сO3), MobEffects.FIRE_RESISTANCE);
         this.method_0(var2, this.potionChoicesSetting.isSelected(сOр));
         if (this.pvpOnlySetting.getValue() && !this.pvpActive) {
            this.ъб();
         } else if (this.ticksSinceThrow >= 20 && !this.method_85() && !var2.isEmpty()) {
            this.throwRotation.setTargetAngles(MINECRAFT_CLIENT.player.getYRot(), сO6);
            if (RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.throwRotation, 10, 1)) {
               this.throwRotationScheduled = true;
               if (!(Math.abs(сO_ - MINECRAFT_CLIENT.player.getXRot()) > 1.0F)) {
                  int var3 = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();

                  for (int var5 : var2) {
                     if (var5 < 9) {
                        InventoryInteractions.Ъ_de5a6f78(var5);
                        MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
                     } else {
                        MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var5, var3, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
                        InventoryInteractions.Ъ_de5a6f78(var3);
                        MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
                        MINECRAFT_CLIENT.gameMode.handleContainerInput(0, var5, var3, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
                     }
                  }

                  InventoryInteractions.Ъ_de5a6f78(var3);
                  this.ticksSinceThrow = 0;
                  this.ъб();
               }
            }
         } else {
            this.ъб();
         }
      }
   }

   private boolean method_1(ItemStack var1, Holder<MobEffect> var2) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.SPLASH_POTION)) {
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

   private void ъб() {
      if (this.throwRotationScheduled) {
         RoseClient.getInstance().getServices().getRotations().expireScheduledStrategy(this.throwRotation);
         this.throwRotationScheduled = false;
      }
   }

   public AutoPotion() {
      super(сOЩ, сOу, ModuleCategory.COMBAT);
      this.pvpOnlySetting = new BooleanSetting(сO2, true);
      this.potionChoicesSetting = new MultiChoiceSetting(
         сOф, new ChoiceOption(сOи, true), new ChoiceOption(сO9, true), new ChoiceOption(сOо, true), new ChoiceOption(сOO, true)
      );
      this.throwRotation = new FixedAngleRotation();
      this.playerTickListener = this::вХ;
      this.packetReceiveListener = this::ц;
      this.ticksSinceThrow = 20;
      this.addSettings(this.pvpOnlySetting, this.potionChoicesSetting);
   }

   private void ц(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundBossEventPacket var2) {
         var2.dispatch(new Handler() {
            private static final String Пь = "pvp";
            private static final String Пж = "у вас осталось";

            @Override
            public void remove(UUID var1) {
               if (AutoPotion.this.pvpBossBarId != null && AutoPotion.this.pvpBossBarId.equals(var1)) {
                  AutoPotion.this.pvpActive = false;
                  AutoPotion.this.pvpBossBarId = null;
               }
            }

            @Override
            public void add(UUID var1, Component var2x, float var3, BossBarColor var4, BossBarOverlay var5, boolean var6, boolean var7, boolean var8) {
               String var9 = StringUtil.stripColor(var2x.getString()).toLowerCase(Locale.ROOT);
               if (var9.contains(Пь) || var9.contains(Пж)) {
                  AutoPotion.this.pvpActive = true;
                  AutoPotion.this.pvpBossBarId = var1;
               }
            }
         });
      }
   }

   private void method_0(List<Integer> var1, Holder<MobEffect> var2) {
      int var3 = this.method_0(true, var2);
      if (var3 != -1) {
         var1.add(var3);
      } else {
         int var4 = this.method_0(false, var2);
         if (var4 != -1) {
            var1.add(var4);
         }
      }
   }
}
