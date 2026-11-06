package sg.modules.impl.combat;

import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.network.InventoryInteractions;
import sg.settings.BooleanSetting;
import sg.util.time.Stopwatch;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.MovementInputEvent;

public final class TriggerBot extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("се")  BooleanSetting onlyCriticalsSetting;
   private final @com.google.gson.annotations.SerializedName("сА")  BooleanSetting autoShieldBreakSetting;
   private final @com.google.gson.annotations.SerializedName("с4")  BooleanSetting onlyWhileJumpHeldSetting;
   private final @com.google.gson.annotations.SerializedName("сп")  BooleanSetting tpsSyncSetting;
   private final @com.google.gson.annotations.SerializedName("сД")  BooleanSetting skipWhileUsingItemSetting;
   private final @com.google.gson.annotations.SerializedName("сЪ")  BooleanSetting onlyWithWeaponSetting;
   private final @com.google.gson.annotations.SerializedName("ст")  BooleanSetting resetSprintSetting;
   private final @com.google.gson.annotations.SerializedName("Ъ")  Stopwatch attackTimer;
   private @com.google.gson.annotations.SerializedName("Хн")  int sprintResetTicks;
   private @com.google.gson.annotations.SerializedName("Вю")  boolean criticalWindowOpen;
   private final @com.google.gson.annotations.SerializedName("сШ")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("сы")  EventListener<PlayerTickEvent.Post> postTickListener;
   private final @com.google.gson.annotations.SerializedName("сй")  EventListener<MovementInputEvent> movementInputListener;
   private static final String су6 = "TriggerBot";
   private static final String су_ = "Автоматически атакует существ в радиусе";
   private static final String суШ = "Бить только критами";
   private static final String суы = "Автоматически ломать щит";
   private static final String суй = "Только при зажатом пробеле";
   private static final String суЖ = "Синхронизация с ТПС";
   private static final String сум = "Не бить при использовании";
   private static final String суК = "Только с оружием";
   private static final String суI = "Сбрасывать спринт";
   private static final float суТ = 0.5F;
   private static final long суО = 450L;
   private static final float суФ = 0.9F;
   private static final double суЙ = 0.1F;
   private static final double сук = 0.08;
   private static final double суг = 2.5;
   private static final double су0 = 1.5;
   private static final double сух = 0.05;
   private static final double суУ = 0.2;
   private static final double суЗ = 0.3;
   private static final double суЕ = 0.3;
   private static final double суП = 0.3;
   private static final double су西 = 0.3;

   public TriggerBot() {
      super(су6, су_, ModuleCategory.COMBAT);
      this.onlyCriticalsSetting = new BooleanSetting(суШ, true);
      this.autoShieldBreakSetting = new BooleanSetting(суы, true);
      this.onlyWhileJumpHeldSetting = new BooleanSetting(суй, false).visibleWhen(this.onlyCriticalsSetting::getValue);
      this.tpsSyncSetting = new BooleanSetting(суЖ, false);
      this.skipWhileUsingItemSetting = new BooleanSetting(сум, false);
      this.onlyWithWeaponSetting = new BooleanSetting(суК, false);
      this.resetSprintSetting = new BooleanSetting(суI, true);
      this.attackTimer = new Stopwatch();
      this.criticalWindowOpen = true;
      this.playerTickListener = this::Б;
      this.postTickListener = this::Ф;
      this.movementInputListener = this::з;
      this.addSettings(this.onlyWithWeaponSetting, this.resetSprintSetting, this.onlyCriticalsSetting, this.onlyWhileJumpHeldSetting, this.tpsSyncSetting, this.skipWhileUsingItemSetting, this.autoShieldBreakSetting);
   }

   private void бе() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (MINECRAFT_CLIENT.player.fallDistance > суЙ && this.Фр()) {
            this.criticalWindowOpen = false;
         } else {
            this.criticalWindowOpen = true;
         }
      } else {
         this.criticalWindowOpen = true;
      }
   }

   private boolean Фю() {
      return MINECRAFT_CLIENT.player.fallDistance > 0.0 && !MINECRAFT_CLIENT.player.onGround();
   }

   private boolean Ф0() {
      boolean var1 = MINECRAFT_CLIENT.player.getPose() == Pose.CROUCHING && MINECRAFT_CLIENT.options.keyJump.isDown();
      double var2 = MINECRAFT_CLIENT.player.getBbHeight() + (var1 ? сух : суУ);
      return !this.Н(var2);
   }

   private void Б(PlayerTickEvent var1) {
      if (RoseClient.getInstance().getServices().getModules().getAirStuck().isEnabled()) {
         this.criticalWindowOpen = true;
      } else {
         this.бе();
      }

      if (!RoseClient.getInstance().getServices().getModules().getCriticals().isEnabled()) {
         this.бЗ();
      }
   }

   private boolean ФУ() {
      if (MINECRAFT_CLIENT.player.isInWater() || MINECRAFT_CLIENT.player.onClimbable() || MINECRAFT_CLIENT.player.getAbilities().flying) {
         return false;
      }

      if (!MINECRAFT_CLIENT.player.onGround()) {
         return false;
      }

      boolean var1 = this.Н(MINECRAFT_CLIENT.player.onGround() ? суг : су0);
      boolean var2 = !this.Ф0();
      return var1 && var2;
   }

   public boolean Фl() {
      if (MINECRAFT_CLIENT.player == null) {
         return false;
      }

      if (MINECRAFT_CLIENT.player.isUsingItem() && this.skipWhileUsingItemSetting.getValue()) {
         return false;
      }

      ItemStack var1 = MINECRAFT_CLIENT.player.getMainHandItem();
      boolean var2 = var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES);
      if (!var2 && this.onlyWithWeaponSetting.getValue()) {
         return false;
      }

      float var3 = this.tpsSyncSetting.getValue() ? 0.0F : суТ;
      boolean var4 = this.attackTimer.method_0(суО) && MINECRAFT_CLIENT.player.getAttackStrengthScale(var3) > суФ;
      boolean var5 = MINECRAFT_CLIENT.options.keyJump.isDown() || !MINECRAFT_CLIENT.player.onGround();
      if (this.ФУ()) {
         return var4;
      }

      boolean var6 = RoseClient.getInstance().getServices().getModules().getCriticals().isEnabled();
      boolean var7 = this.ФЕ();
      boolean var8 = this.Фю();
      return var6
         ? var4 && (!this.onlyCriticalsSetting.getValue() || !var7 || this.onlyWhileJumpHeldSetting.getValue() && !var5 || var8)
         : var4 && (!this.onlyCriticalsSetting.getValue() || !var7 || this.onlyWhileJumpHeldSetting.getValue() && !var5 || var8 && this.criticalWindowOpen);
   }

   private void Ф(PlayerTickEvent.Post var1) {
      if (RoseClient.getInstance().getServices().getModules().getCriticals().isEnabled()) {
         this.бЗ();
      }
   }

   private boolean ФЕ() {
      return !MINECRAFT_CLIENT.player.isInWater()
         && !MINECRAFT_CLIENT.player.isInLava()
         && !MINECRAFT_CLIENT.player.onClimbable()
         && !MINECRAFT_CLIENT.player.getAbilities().flying
         && !MINECRAFT_CLIENT.player.isPassenger()
         && !MINECRAFT_CLIENT.player.hasEffect(MobEffects.BLINDNESS)
         && !MINECRAFT_CLIENT.player.hasEffect(MobEffects.SLOW_FALLING)
         && !MINECRAFT_CLIENT.player.hasEffect(MobEffects.LEVITATION)
         && !MINECRAFT_CLIENT.player.isFallFlying();
   }

   private void з(MovementInputEvent var1) {
      if (this.sprintResetTicks > 0 && this.resetSprintSetting.getValue()) {
         var1.setForwardAmount(0.0F);
         var1.setLeftAmount(0.0F);
         var1.setForwardPressed(false);
         var1.setBackwardPressed(false);
         var1.setLeftPressed(false);
         var1.setRightPressed(false);
         this.sprintResetTicks--;
      }
   }

   @Override
   public void onDisable() {
      this.criticalWindowOpen = false;
      this.sprintResetTicks = 0;
      super.onDisable();
   }

   private boolean К(Entity var1) {
      if (!this.autoShieldBreakSetting.getValue()) {
         return false;
      }

      if (var1 instanceof LivingEntity var2) {
         int var3 = InventoryInteractions.Ч();
         if (var2.getUseItem().getItem() == Items.SHIELD && var3 != -1) {
            if (var3 < 9) {
               MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(var3));
               MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, var2);
               MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
               MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(MINECRAFT_CLIENT.player.getInventory().selected));
            } else {
               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, var3, MINECRAFT_CLIENT.player.getInventory().selected, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
               MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, var2);
               MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, var3, MINECRAFT_CLIENT.player.getInventory().selected, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void бЗ() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
         if (MINECRAFT_CLIENT.hitResult instanceof EntityHitResult var1 && var1.getType() == Type.ENTITY) {
            if (this.Фl()) {
               boolean var4 = RoseClient.getInstance().getControllers().getServerPlayers().method_96()
                  && !MINECRAFT_CLIENT.player.isInWater()
                  && !MINECRAFT_CLIENT.player.isInLava()
                  && !MINECRAFT_CLIENT.player.isSwimming();
               if (var4 && this.resetSprintSetting.getValue()) {
                  this.sprintResetTicks = 1;
                  if (RoseClient.getInstance().getControllers().getServerPlayers().method_96()) {
                     MINECRAFT_CLIENT.player.setSprinting(false);
                     MINECRAFT_CLIENT.getConnection().send(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.STOP_SPRINTING));
                  }
               }

               Entity var3 = var1.getEntity();
               MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, var3);
               MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
               this.criticalWindowOpen = false;
               this.attackTimer.ВД();
               this.К(var3);
            }
         }
      }
   }

   private boolean Фр() {
      AABB var1 = MINECRAFT_CLIENT.player.getBoundingBox().move(0.0, Math.min(MINECRAFT_CLIENT.player.getDeltaMovement().y, 0.0) - сук, 0.0);
      return MINECRAFT_CLIENT.level.getBlockCollisions(MINECRAFT_CLIENT.player, var1).iterator().hasNext();
   }

   private boolean Н(double var1) {
      double var3 = MINECRAFT_CLIENT.player.getX();
      double var5 = MINECRAFT_CLIENT.player.getY();
      double var7 = MINECRAFT_CLIENT.player.getZ();
      AABB var9 = new AABB(var3 - суЗ, var5 + MINECRAFT_CLIENT.player.getBbHeight(), var7 - суЕ, var3 + суП, var5 + var1, var7 + су西);
      return MINECRAFT_CLIENT.level.getBlockCollisions(MINECRAFT_CLIENT.player, var9).iterator().hasNext();
   }
}
