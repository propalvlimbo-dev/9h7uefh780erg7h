package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import sg.ec.MinecraftAccess;
import sg.ec.CommandPingProbe;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.impl.EntityAttackEvent;
import sg.events.EventListener;
import sg.events.impl.PacketSentEvent;
import sg.ec.PlayerMotionPredictor;
import sg.mixin.FireworkRocketEntityAccessor;
import sg.network.InventoryInteractions;
import sg.render.pipeline.InventoryActions;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.ModuleRegistry;
import sg.events.impl.AttackEntityEvent;
import sg.modules.impl.init.EntityMotionSimulator;
import sg.modules.impl.init.EntityMotionTracker;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.rotation.FixedAngleRotation;
import sg.settings.FloatSetting;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionCoordinator;
import sg.settings.KeybindSetting;
import sg.events.impl.MovementInputEvent;
import sg.modules.impl.init.ProjectileOwnerMotion;
import sg.rotation.RotationController;
import sg.rotation.RotationRegistry;
import sg.settings.SingleChoiceSetting;
import sg.modules.impl.init.WindChargeInterceptSolver;

public class PvPHelper extends ClientModule {
   private static final double UNUSED_LEGACY_REACH = 3.0;
   private static final int UNUSED_LEGACY_TIMEOUT_TICKS = 80;
   private static final float UNUSED_LEGACY_ANGLE_DEGREES = 96.0F;
   @SerializedName("В")
   private final InventoryActionCoordinator inventoryActionCoordinator = new InventoryActionCoordinator();
   @SerializedName("В")
   private final Set<Item> unusedLegacyMeleeWeapons = Set.of(
      Items.WOODEN_SWORD,
      Items.STONE_SWORD,
      Items.IRON_SWORD,
      Items.GOLDEN_SWORD,
      Items.DIAMOND_SWORD,
      Items.NETHERITE_SWORD,
      Items.WOODEN_AXE,
      Items.STONE_AXE,
      Items.IRON_AXE,
      Items.GOLDEN_AXE,
      Items.DIAMOND_AXE,
      Items.NETHERITE_AXE
   );
   @SerializedName("Я")
   private final Set<Item> axeItems = Set.of(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE);
   @SerializedName("Х")
   private final Set<Item> spearItems = Set.of(
      Items.WOODEN_SPEAR, Items.STONE_SPEAR, Items.COPPER_SPEAR, Items.IRON_SPEAR, Items.GOLDEN_SPEAR, Items.DIAMOND_SPEAR, Items.NETHERITE_SPEAR
   );
   @SerializedName("ш")
   private final BooleanSetting autoMaceSetting;
   private final @com.google.gson.annotations.SerializedName("ъ")  FloatSetting autoMaceDelaySetting;
   @SerializedName("И")
   private final BooleanSetting auraOnlySetting;
   @SerializedName("ь")
   private final BooleanSetting forceMaceSetting;
   @SerializedName("ж")
   private final BooleanSetting useChestSetting;
   private final @com.google.gson.annotations.SerializedName("М")  FloatSetting chestPreswapSetting;
   @SerializedName("4")
   private final KeybindSetting spearBoostKeybindSetting;
   @SerializedName("弟")
   private final BooleanSetting spearElytraSetting;
   @SerializedName("б")
   private final BooleanSetting instaCartSetting;
   @SerializedName("п")
   private final KeybindSetting windPearlKeybindSetting;
   private final @com.google.gson.annotations.SerializedName("Д")  KeybindSetting antiMaceKeybindSetting;
   private final @com.google.gson.annotations.SerializedName("Н")  FloatSetting antiMaceRangeSetting;
   @SerializedName("Ы")
   private final BooleanSetting comboAssistSetting;
   @SerializedName("l")
   private final BooleanSetting comboRayTracingSetting;
   @SerializedName("8")
   private final BooleanSetting comboSelfProtectSetting;
   @SerializedName("5")
   private final BooleanSetting comboFarStopSetting;
   @SerializedName("В")
   private final PvPHelper.AutoMaceController autoMaceController;
   @SerializedName("В")
   private final PvPHelper.SpearBoostController spearBoostController;
   @SerializedName("В")
   private final PvPHelper.WindPearlController windPearlController;
   @SerializedName("В")
   private final PvPHelper.AntiMaceController antiMaceController;
   @SerializedName("В")
   private final PvPHelper.SpearElytraController spearElytraController;
   @SerializedName("В")
   private final PvPHelper.InstaCartController instaCartController;
   @SerializedName("В")
   private final PvPHelper.ComboAssistController comboAssistController;
   private final @com.google.gson.annotations.SerializedName("衣")  EventListener<AttackEntityEvent> attackListener;
   @SerializedName("ш")
   private final EventListener<EntityAttackEvent> entityAttackListener;
   @SerializedName("И")
   private final EventListener<InputEvent> inputListener;
   @SerializedName("ь")
   private final EventListener<PlayerTickEvent> playerTickListener;
   @SerializedName("ж")
   private final EventListener<PlayerTickEvent.Post> postPlayerTickListener;
   @SerializedName("弟")
   private final EventListener<MovementInputEvent> movementInputListener;
   @SerializedName("б")
   private final EventListener<PlayerMovementSyncEvent.Post> postMovementSyncListener;
   @SerializedName("Ы")
   private final EventListener<PacketSentEvent> packetSentListener;
   @SerializedName("l")
   private final EventListener<PacketReceiveEvent> packetReceiveListener;
   private static final String MODULE_NAME = "PvPHelper";
   private static final String MODULE_DESCRIPTION = "AutoMace, AntiMace, SpearBoost, InstaCart, WindPearl и ComboAssist";
   private static final String AUTO_MACE_LABEL = "AutoMace";
   private static final String DELAY_LABEL = "Задержка";
   private static final float MAX_AUTO_MACE_DELAY = 20.0F;
   private static final float AUTO_MACE_DELAY_STEP = 0.5F;
   private static final String AURA_ONLY_LABEL = "Только с Aura";
   private static final String FORCE_LABEL = "Force";
   private static final String USE_CHEST_LABEL = "Use Chest";
   private static final String CHEST_PRESWAP_LABEL = "Предсвап нагрудника";
   private static final float DEFAULT_CHEST_PRESWAP = 7.0F;
   private static final float MIN_CHEST_PRESWAP = 5.0F;
   private static final float MAX_CHEST_PRESWAP = 10.0F;
   private static final String SPEAR_BOOST_LABEL = "SpearBoost";
   private static final String SPEAR_ELYTRA_LABEL = "SpearElytra";
   private static final String INSTA_CART_LABEL = "InstaCart";
   private static final String WIND_PEARL_LABEL = "WindPearl";
   private static final String ANTI_MACE_LABEL = "AntiMace";
   private static final String ANTI_MACE_RANGE_LABEL = "AntiMace дальность";
   private static final float DEFAULT_ANTI_MACE_RANGE = 48.0F;
   private static final float MIN_ANTI_MACE_RANGE = 4.0F;
   private static final float MAX_ANTI_MACE_RANGE = 64.0F;
   private static final String COMBO_ASSIST_LABEL = "ComboAssist";
   private static final String RAY_TRACING_LABEL = "RayTracing";
   private static final String SELF_PROTECT_LABEL = "SelfProtect";
   private static final String FAR_STOP_LABEL = "FarStop";
   private static final String LEGITIMATE_LABEL = "Легитный";
   private static final String LEGIT_MODE_NAME = "Legit";

   private void method_6(int var1) {
      if (this.Ъ4() && var1 >= 0 && var1 <= 8) {
         MINECRAFT_CLIENT.player.getInventory().selected = var1;
         MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
      }
   }

   private LivingEntity Ц_95bedd6e() {
      if (!this.auraOnlySetting.getValue()) {
         LivingEntity var1 = this.西_95bedd6e();
         if (var1 != null) {
            return var1;
         }
      }

      Aura var4 = this.Ц_6fa770e6();
      if (var4 == null) {
         return null;
      } else {
         return var4.Ж_95bedd6e() instanceof LivingEntity var2 ? var2 : null;
      }
   }

   private int Е() {
      return this.Ц(this.spearItems);
   }

   private boolean Ц(InputEvent var1, KeybindSetting var2) {
      return var2.getValue() != -1 && var1.getInputCode() == var2.getValue();
   }

   private boolean 西(int var1) {
      if (var1 < 0 || !this.Ъ4()) {
         return false;
      }

      if (this.ЪК()) {
         return this.Ц(var1) == InventoryActionCoordinator.Result.SUCCESS;
      }

      this.й(var1);
      return true;
   }

   @Override
   public void onDisable() {
      this.autoMaceController.З(true);
      this.spearBoostController.м(true);
      this.windPearlController.М(true);
      this.antiMaceController.ц();
      this.spearElytraController.Х4();
      this.instaCartController.хк();
      this.comboAssistController.чO();
      this.ю(false);
      super.onDisable();
   }

   private void ЪЖ() {
      this.antiMaceController.я();
      this.comboAssistController.ч诶();
      this.spearBoostController.Щя();
      this.windPearlController.ЕЮ();
      this.spearElytraController.Х0();
      this.autoMaceController.Oб();
      this.instaCartController.х5();
   }

   private int в_a83dd0cc() {
      return this.I(Items.MACE);
   }

   private int method_9() {
      return MINECRAFT_CLIENT.player.getInventory().selected;
   }

   private void 西(AttackEntityEvent var1) {
      if (!this.autoMaceController.ФЧ() && !this.spearBoostController.ИO()) {
         if (var1.getPhase() == AttackEntityEvent.Phase.POST) {
            this.autoMaceController.OЮ();
         } else {
            if (var1.getPhase() == AttackEntityEvent.Phase.PRE
               && this.autoMaceSetting.getValue()
               && var1.getTarget() instanceof LivingEntity var2) {
               this.autoMaceController.С(var2);
            }
         }
      }
   }

   private void А(InputEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null && var1.getAction() == 1) {
         if (this.Ц(var1, this.spearBoostKeybindSetting)) {
            this.spearBoostController.Щ6();
         } else if (this.Ц(var1, this.windPearlKeybindSetting)) {
            this.windPearlController.Еб();
         } else {
            if (this.Ц(var1, this.antiMaceKeybindSetting)) {
               this.antiMaceController.Й_de5a6f78();
            }
         }
      }
   }

   public PvPHelper() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
      this.autoMaceSetting = new BooleanSetting(AUTO_MACE_LABEL, true);
      this.autoMaceDelaySetting = new FloatSetting(DELAY_LABEL, 0.0F, 0.0F, MAX_AUTO_MACE_DELAY).withStep(AUTO_MACE_DELAY_STEP).visibleWhen(this.autoMaceSetting::getValue);
      this.auraOnlySetting = new BooleanSetting(AURA_ONLY_LABEL, true).visibleWhen(this.autoMaceSetting::getValue);
      this.forceMaceSetting = new BooleanSetting(FORCE_LABEL, false).visibleWhen(this.autoMaceSetting::getValue);
      this.useChestSetting = new BooleanSetting(USE_CHEST_LABEL, false).visibleWhen(this.autoMaceSetting::getValue);
      this.chestPreswapSetting = new FloatSetting(CHEST_PRESWAP_LABEL, DEFAULT_CHEST_PRESWAP, MIN_CHEST_PRESWAP, MAX_CHEST_PRESWAP).withStep(1.0F).visibleWhen(() -> this.autoMaceSetting.getValue() && this.useChestSetting.getValue());
      this.spearBoostKeybindSetting = new KeybindSetting(SPEAR_BOOST_LABEL);
      this.spearElytraSetting = new BooleanSetting(SPEAR_ELYTRA_LABEL, false).visibleWhen(() -> this.spearBoostKeybindSetting.getValue() != -1);
      this.instaCartSetting = new BooleanSetting(INSTA_CART_LABEL, false);
      this.windPearlKeybindSetting = new KeybindSetting(WIND_PEARL_LABEL);
      this.antiMaceKeybindSetting = new KeybindSetting(ANTI_MACE_LABEL);
      this.antiMaceRangeSetting = new FloatSetting(ANTI_MACE_RANGE_LABEL, DEFAULT_ANTI_MACE_RANGE, MIN_ANTI_MACE_RANGE, MAX_ANTI_MACE_RANGE).withStep(1.0F).visibleWhen(() -> this.antiMaceKeybindSetting.getValue() != -1);
      this.comboAssistSetting = new BooleanSetting(COMBO_ASSIST_LABEL, false);
      this.comboRayTracingSetting = new BooleanSetting(RAY_TRACING_LABEL, true).visibleWhen(this.comboAssistSetting::getValue);
      this.comboSelfProtectSetting = new BooleanSetting(SELF_PROTECT_LABEL, false).visibleWhen(this.comboAssistSetting::getValue);
      this.comboFarStopSetting = new BooleanSetting(FAR_STOP_LABEL, false).visibleWhen(this.comboAssistSetting::getValue);
      this.autoMaceController = new PvPHelper.AutoMaceController();
      this.spearBoostController = new PvPHelper.SpearBoostController();
      this.windPearlController = new PvPHelper.WindPearlController();
      this.antiMaceController = new PvPHelper.AntiMaceController();
      this.spearElytraController = new PvPHelper.SpearElytraController();
      this.instaCartController = new PvPHelper.InstaCartController();
      this.comboAssistController = new PvPHelper.ComboAssistController();
      this.attackListener = this::西;
      this.entityAttackListener = this::Ц;
      this.inputListener = this::А;
      this.playerTickListener = var1 -> this.ЪЖ();
      this.postPlayerTickListener = var1 -> this.comboAssistController.чД();
      this.movementInputListener = var1 -> this.comboAssistController.И(var1);
      this.postMovementSyncListener = var1 -> this.antiMaceController.Й(var1);
      this.packetSentListener = var1 -> this.antiMaceController.Й(var1);
      this.packetReceiveListener = var1 -> {
         if (!var1.isCancelled() && var1.getPacket() instanceof ClientboundPlayerPositionPacket) {
            this.antiMaceController.positionCorrectionPending = true;
         }
      };
      this.addSettings(
         this.autoMaceSetting,
         this.autoMaceDelaySetting,
         this.auraOnlySetting,
         this.forceMaceSetting,
         this.useChestSetting,
         this.chestPreswapSetting,
         this.spearBoostKeybindSetting,
         this.spearElytraSetting,
         this.instaCartSetting,
         this.windPearlKeybindSetting,
         this.antiMaceKeybindSetting,
         this.antiMaceRangeSetting,
         this.comboAssistSetting,
         this.comboRayTracingSetting,
         this.comboSelfProtectSetting,
         this.comboFarStopSetting
      );
   }

   private void Ъс() {
      if (this.Ъ4()) {
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
      }
   }

   private boolean Ъ2() {
      return this.spearBoostController.Иа() || this.windPearlController.эь() || this.antiMaceController.Й_bbeebd87() || this.spearElytraController.Гк();
   }

   private boolean Ъь() {
      if (!this.auraOnlySetting.getValue()) {
         return true;
      }

      Aura var1 = this.Ц_6fa770e6();
      return var1 != null && var1.Ж_95bedd6e() != null;
   }

   private LivingEntity 西_95bedd6e() {
      return MINECRAFT_CLIENT.hitResult instanceof EntityHitResult var1 && var1.getEntity() instanceof LivingEntity var2 ? var2 : null;
   }

   private int I(Item var1) {
      if (MINECRAFT_CLIENT.player == null) {
         return -1;
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var2).is(var1)) {
            return var2;
         }
      }

      return -1;
   }

   private void Ъ0() {
      if (this.Ъ4() && !MINECRAFT_CLIENT.player.onGround() && MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
         MINECRAFT_CLIENT.player.startFallFlying();
         ClientPacketListener var1 = MINECRAFT_CLIENT.getConnection();
         if (var1 != null) {
            var1.send(new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, Action.START_FALL_FLYING));
         }
      }
   }

   private boolean ЪК() {
      SingleChoiceSetting var1 = RoseClient.getInstance().getServices().getModules().getGuiMove().Щ();
      return var1.isSelected(LEGITIMATE_LABEL) || var1.isSelected(LEGIT_MODE_NAME);
   }

   private Aura Ц_6fa770e6() {
      return RoseClient.getInstance().getServices().getModules().getAura();
   }

   private boolean Ъ5() {
      return this.autoMaceController.Фь() || this.spearBoostController.Иа() || this.windPearlController.эь() || this.antiMaceController.Й_bbeebd87() || this.spearElytraController.Гя();
   }

   private int Ц(Set<Item> var1) {
      if (MINECRAFT_CLIENT.player == null) {
         return -1;
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (var1.contains(MINECRAFT_CLIENT.player.getInventory().getItem(var2).getItem())) {
            return var2;
         }
      }

      return -1;
   }

   private void ю(boolean var1) {
      Aura var2 = this.Ц_6fa770e6();
      if (var2 != null) {
         var2.衣(var1);
      }
   }

   private void Ц(EntityAttackEvent var1) {
      this.spearElytraController.щ(var1);
   }

   private InventoryActionCoordinator.Result Ц(int var1) {
      return InventoryActions.б(var1)
         ? this.inventoryActionCoordinator.л(() -> InventoryActions.method_7(InventoryActions.ч(var1), 6), true)
         : this.inventoryActionCoordinator.л(() -> this.я(var1), true);
   }

   private boolean 西(LivingEntity var1) {
      return var1 != null && var1.isAlive() && !var1.isRemoved();
   }

   private boolean Ъ4() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null;
   }

   private void й(int var1) {
      if (InventoryActions.б(var1)) {
         InventoryActions.method_7(InventoryActions.ч(var1), 6);
      } else {
         this.я(var1);
      }
   }

   private int П_a83dd0cc() {
      return this.Ц(this.axeItems);
   }

   private void я(int var1) {
      InventoryActions.method_7(8, var1);
      InventoryActions.method_7(8, 6);
      InventoryActions.method_7(8, var1);
   }

   private final class ComboAssistController {
      private static final double ВМ = 6.0;
      private static final long Яс = 200L;
      private static final long ЯВ = 500L;
      private static final long ЯЯ = 400L;
      private static final double ВН = 1.0;
      private static final float field_45 = 10.0F;
      private static final double Вн = 0.05;
      private static final double ВЦ = 0.12;
      private static final double Вч = 2.5;
      private static final double ВЭ = 0.4;
      private static final double В必 = 0.08;
      private @com.google.gson.annotations.SerializedName("п")  UUID lastAttackedTargetId;
      @SerializedName("Д")
      private UUID pursuitTargetId;
      private @com.google.gson.annotations.SerializedName("ЯХ")  long attackPauseUntilMillis;
      private @com.google.gson.annotations.SerializedName("Яе")  long movementFreezeUntilMillis;
      private @com.google.gson.annotations.SerializedName("ЯА")  long selfProtectionUntilMillis;
      private @com.google.gson.annotations.SerializedName("Ау")  int pursuitReadyTick;
      @SerializedName("4в")
      private float pursuitStartYaw;
      private @com.google.gson.annotations.SerializedName("Я6")  boolean restoreSprintNextTick;
      private @com.google.gson.annotations.SerializedName("Я_")  boolean forcedSprintActive;
      private @com.google.gson.annotations.SerializedName("Я4")  long retreatUntilMillis;
      @SerializedName("_")
      private Vec3 retreatStartPosition;
      private @com.google.gson.annotations.SerializedName("Ш")  Vec3 retreatDirection;
      private static final long INPUT_FREEZE_EXTENSION_MILLIS = 50L;
      private static final long ATTACK_PAUSE_MILLIS = 500L;
      private static final long SELF_PROTECTION_MILLIS = 200L;
      private static final float RADIANS_PER_DEGREE = (float) (Math.PI / 180.0);
      private static final long RETREAT_WINDOW_MILLIS = 400L;
      private static final float MAX_PURSUIT_YAW_CHANGE_DEGREES = 10.0F;
      private static final double BASE_REACH_MARGIN = 0.05;
      private static final double MAX_REACH_MARGIN = 0.4;
      private static final double RELATIVE_SPEED_MARGIN_SCALE = 2.5;
      private static final double PLAYER_EYE_EXTRAPOLATION_TICKS = 2.5;
      private static final double PLAYER_BOX_EXTRAPOLATION_TICKS = 2.5;
      private static final double TARGET_EYE_EXTRAPOLATION_TICKS = 2.5;
      private static final double TARGET_BOX_EXTRAPOLATION_TICKS = 2.5;
      private static final double CLOSE_TARGET_MARGIN = 0.05;
      private static final double PREDICTED_REACH_EDGE_MARGIN = 0.12;
      private static final double MIN_ATTACK_EDGE_MARGIN = 0.12;
      private static final double BASE_ATTACK_EDGE_MARGIN = 0.08;
      private static final double RELATIVE_MARGIN_WEIGHT = 0.5;
      private static final double TARGET_ACQUISITION_REACH = 6.0;
      private static final double DISTANCE_COMPARISON_EPSILON = 1.0E-4;
      private static final double HORIZONTAL_SPEED_EPSILON_SQUARED = 1.0E-6;
      private static final float BACKWARD_MOVEMENT_AMOUNT = -1.0F;
      private static final int NO_PURSUIT_READY_TICK = Integer.MIN_VALUE;

      private double Ь(Vec3 var1, AABB var2) {
         double var3 = Mth.clamp(var1.x, var2.minX, var2.maxX);
         double var5 = Mth.clamp(var1.y, var2.minY, var2.maxY);
         double var7 = Mth.clamp(var1.z, var2.minZ, var2.maxZ);
         return var1.distanceTo(new Vec3(var3, var5, var7));
      }

      private Player н() {
         if (MinecraftAccess.MINECRAFT_CLIENT.hitResult instanceof EntityHitResult var1) {
            return var1.getEntity() instanceof Player var4 ? var4 : null;
         } else {
            return null;
         }
      }

      private void в(MovementInputEvent var1) {
         var1.setForwardAmount(0.0F);
         var1.setLeftAmount(0.0F);
         var1.setForwardPressed(false);
         var1.setBackwardPressed(false);
         var1.setLeftPressed(false);
         var1.setRightPressed(false);
         var1.setJumpPressed(false);
         var1.setSneakPressed(false);
         var1.setSprintPressed(false);
      }

      private void ВС() {
         this.pursuitTargetId = null;
         this.pursuitReadyTick = NO_PURSUIT_READY_TICK;
         this.pursuitStartYaw = 0.0F;
      }

      private boolean Ь(LocalPlayer var1, long var2) {
         if (this.retreatUntilMillis <= var2 || var1 == null || this.retreatStartPosition == null || this.retreatDirection == null) {
            this.Ву();
            return false;
         }

         if (!var1.horizontalCollision && !var1.isInWater() && !var1.onClimbable() && !var1.isSwimming() && !var1.isPassenger()) {
            Vec3 var4 = var1.position().subtract(this.retreatStartPosition);
            double var5 = var4.x * this.retreatDirection.x + var4.z * this.retreatDirection.z;
            if (var5 >= 1.0) {
               this.Ву();
               return false;
            } else {
               return true;
            }
         } else {
            this.Ву();
            return false;
         }
      }

      private boolean у(Player var1, Player var2, double var3) {
         return this.Ь(var1.getEyePosition(), var2.getBoundingBox()) <= var3 + DISTANCE_COMPARISON_EPSILON;
      }

      private boolean Т(LocalPlayer var1, Player var2) {
         if (var2 != null && var2 != var1 && var2.isAlive() && !var2.isRemoved()) {
            AntiBot var3 = RoseClient.getInstance().getServices().getModules().getAntiBot();
            return var3.isEnabled() && var3.detectedBots.contains(var2)
               ? false
               : !RoseClient.getInstance().getServices().getModules().getNoFriendDamage().isEnabled()
                  || !RoseClient.getInstance().getServices().getFriends().О(var2.getGameProfile().name());
         } else {
            return false;
         }
      }

      private void Ву() {
         this.retreatUntilMillis = 0L;
         this.retreatStartPosition = null;
         this.retreatDirection = null;
      }

      private void у_de5a6f78(LocalPlayer var1, Player var2) {
         if (this.pursuitTargetId == null || !this.pursuitTargetId.equals(var2.getUUID())) {
            this.pursuitTargetId = var2.getUUID();
            this.pursuitReadyTick = var1.tickCount + 1;
            this.pursuitStartYaw = var1.getYRot();
         }

         this.у_de5a6f78(var1);
      }

      private void у(LocalPlayer var1, Player var2, long var3, boolean var5) {
         this.ВС();
         if (var5) {
            this.у_de5a6f78(var1);
         } else {
            this.чы();
         }

         MinecraftAccess.MINECRAFT_CLIENT.startAttack();
         this.lastAttackedTargetId = var2.getUUID();
         this.attackPauseUntilMillis = var3 + ATTACK_PAUSE_MILLIS;
         if (PvPHelper.this.comboSelfProtectSetting.getValue()) {
            this.у_de5a6f78(var1, var3);
            this.selfProtectionUntilMillis = 0L;
            this.movementFreezeUntilMillis = 0L;
         } else {
            this.selfProtectionUntilMillis = var3 + SELF_PROTECTION_MILLIS;
            this.movementFreezeUntilMillis = this.selfProtectionUntilMillis;
         }

         this.restoreSprintNextTick = var5;
      }

      private void у_de5a6f78(LocalPlayer var1, long var2) {
         float var4 = var1.getYRot() * RADIANS_PER_DEGREE;
         this.retreatDirection = new Vec3(Mth.sin(var4), 0.0, -Mth.cos(var4)).normalize();
         this.retreatStartPosition = var1.position();
         this.retreatUntilMillis = var2 + RETREAT_WINDOW_MILLIS;
      }

      private double у(Vec3 var1, Vec3 var2, Vec3 var3) {
         Vec3 var4 = new Vec3(var3.x, 0.0, var3.z);
         double var5 = var4.lengthSqr();
         if (var5 < HORIZONTAL_SPEED_EPSILON_SQUARED) {
            return 0.0;
         }

         Vec3 var7 = var4.normalize();
         Vec3 var8 = var2.subtract(var1);
         return Math.max(0.0, var8.dot(var7));
      }

      private void чы() {
         if (this.forcedSprintActive) {
            if (MinecraftAccess.MINECRAFT_CLIENT.options != null) {
               MinecraftAccess.MINECRAFT_CLIENT.options.keySprint.setDown(this.у(MinecraftAccess.MINECRAFT_CLIENT.options.keySprint));
            }

            this.forcedSprintActive = false;
         }
      }

      private void чД() {
         if (PvPHelper.this.comboAssistSetting.getValue()) {
            if (this.restoreSprintNextTick) {
               this.чы();
               this.restoreSprintNextTick = false;
            }
         }
      }

      private Vec3 Й(Vec3 var1) {
         return new Vec3(var1.x, 0.0, var1.z);
      }

      private Player у_d012b85c(LocalPlayer var1) {
         Player var2 = this.н();
         return this.Т(var1, var2) && this.у(var1, var2, TARGET_ACQUISITION_REACH) ? var2 : null;
      }

      private void у_de5a6f78(LocalPlayer var1) {
         if (this.Ь_bbeebd87(var1)) {
            MinecraftAccess.MINECRAFT_CLIENT.options.keySprint.setDown(true);
            this.forcedSprintActive = true;
         }
      }

      private void чУ() {
         this.ВС();
         this.чы();
      }

      private boolean у(KeyMapping var1) {
         return InputConstants.isKeyDown(MinecraftAccess.MINECRAFT_CLIENT.getWindow(), var1.key.getValue());
      }

      private void д(MovementInputEvent var1) {
         var1.setForwardAmount(BACKWARD_MOVEMENT_AMOUNT);
         var1.setLeftAmount(0.0F);
         var1.setForwardPressed(false);
         var1.setBackwardPressed(true);
         var1.setLeftPressed(false);
         var1.setRightPressed(false);
         var1.setJumpPressed(false);
         var1.setSneakPressed(false);
         var1.setSprintPressed(false);
      }

      private Player у(double var1) {
         if (MinecraftAccess.MINECRAFT_CLIENT.level != null && !(var1 <= 0.0)) {
            Vec3 var3 = MinecraftAccess.MINECRAFT_CLIENT.player.getEyePosition(1.0F);
            Vec3 var4 = MinecraftAccess.MINECRAFT_CLIENT.player.getViewVector(1.0F);
            Vec3 var5 = var3.add(var4.scale(var1));
            BlockHitResult var6 = MinecraftAccess.MINECRAFT_CLIENT.level.clip(new ClipContext(var3, var5, Block.COLLIDER, Fluid.NONE, MinecraftAccess.MINECRAFT_CLIENT.player));
            AABB var7 = MinecraftAccess.MINECRAFT_CLIENT.player.getBoundingBox().expandTowards(var4.scale(var1)).inflate(1.0, 1.0, 1.0);
            EntityHitResult var8 = ProjectileUtil.getEntityHitResult(
               MinecraftAccess.MINECRAFT_CLIENT.player, var3, var5, var7, var1x -> var1x instanceof Player var2 && this.Т(MinecraftAccess.MINECRAFT_CLIENT.player, var2), var1 * var1
            );
            if (var8 == null) {
               return null;
            }

            if (var6.getType() != Type.MISS) {
               double var9 = var3.distanceToSqr(var8.getLocation());
               double var11 = var3.distanceToSqr(var6.getLocation());
               if (var11 < var9) {
                  return null;
               }
            }

            return var8.getEntity() instanceof Player var13 ? var13 : null;
         } else {
            return null;
         }
      }

      private void чO() {
         this.чы();
         this.lastAttackedTargetId = null;
         this.ВС();
         this.attackPauseUntilMillis = 0L;
         this.movementFreezeUntilMillis = 0L;
         this.restoreSprintNextTick = false;
         this.selfProtectionUntilMillis = 0L;
         this.Ву();
      }

      private boolean Б(long var1) {
         if (this.attackPauseUntilMillis <= var1) {
            this.attackPauseUntilMillis = 0L;
            this.lastAttackedTargetId = null;
            if (this.selfProtectionUntilMillis <= var1) {
               this.selfProtectionUntilMillis = 0L;
            }
         }

         return this.attackPauseUntilMillis > var1 || this.selfProtectionUntilMillis > var1;
      }

      private void И(MovementInputEvent var1) {
         if (PvPHelper.this.comboAssistSetting.getValue() && MinecraftAccess.MINECRAFT_CLIENT.player != null) {
            long var2 = System.currentTimeMillis();
            if (this.Ь(MinecraftAccess.MINECRAFT_CLIENT.player, var2)) {
               this.д(var1);
            } else if (this.Т(MinecraftAccess.MINECRAFT_CLIENT.player, var2)) {
               this.а(var1);
            } else {
               if (this.а(var2) || this.у_bbeebd87(MinecraftAccess.MINECRAFT_CLIENT.player, var2)) {
                  this.в(var1);
               }
            }
         }
      }

      private double Ж(Player var1) {
         return var1.entityInteractionRange();
      }

      private boolean Ь(LocalPlayer var1, Player var2) {
         double var3 = this.Ж(var1);
         double var5 = this.Ж(var2);
         Vec3 var7 = this.Й(var1.getDeltaMovement());
         Vec3 var8 = this.Й(var2.getDeltaMovement());
         Vec3 var9 = var1.position().subtract(var2.position());
         double var10 = this.у(var7, var8, var9);
         double var12 = BASE_REACH_MARGIN + Math.min(MAX_REACH_MARGIN, var10 * RELATIVE_SPEED_MARGIN_SCALE);
         double var14 = this.Ь(var1.getEyePosition(), var2.getBoundingBox());
         double var16 = this.Ь(var2.getEyePosition(), var1.getBoundingBox());
         Vec3 var18 = var1.getEyePosition().add(var7.scale(PLAYER_EYE_EXTRAPOLATION_TICKS));
         AABB var19 = var1.getBoundingBox().move(var7.scale(PLAYER_BOX_EXTRAPOLATION_TICKS));
         Vec3 var20 = var2.getEyePosition().add(var8.scale(TARGET_EYE_EXTRAPOLATION_TICKS));
         AABB var21 = var2.getBoundingBox().move(var8.scale(TARGET_BOX_EXTRAPOLATION_TICKS));
         double var22 = this.Ь(var18, var21);
         double var24 = this.Ь(var20, var19);
         if (var14 > var3) {
            return false;
         } else if (var24 <= var5 + var12) {
            return true;
         } else if (var16 <= var5 + CLOSE_TARGET_MARGIN) {
            return true;
         } else {
            return var22 >= var3 - PREDICTED_REACH_EDGE_MARGIN ? true : var14 >= var3 - Math.max(MIN_ATTACK_EDGE_MARGIN, BASE_ATTACK_EDGE_MARGIN + var12 * RELATIVE_MARGIN_WEIGHT);
         }
      }

      private void а(MovementInputEvent var1) {
         var1.setForwardAmount(1.0F);
         var1.setLeftAmount(0.0F);
         var1.setForwardPressed(true);
         var1.setBackwardPressed(false);
         var1.setLeftPressed(false);
         var1.setRightPressed(false);
         var1.setJumpPressed(false);
         var1.setSneakPressed(false);
         var1.setSprintPressed(MinecraftAccess.MINECRAFT_CLIENT.options.keySprint.isDown());
      }

      private boolean Т(LocalPlayer var1, long var2) {
         if (this.attackPauseUntilMillis <= var2 && this.selfProtectionUntilMillis <= var2 && this.pursuitTargetId != null) {
            return Math.abs(Mth.wrapDegrees(var1.getYRot() - this.pursuitStartYaw)) > MAX_PURSUIT_YAW_CHANGE_DEGREES ? false : this.Ь_bbeebd87(var1);
         } else {
            return false;
         }
      }

      private boolean у_bbeebd87(LocalPlayer var1, Player var2) {
         return this.pursuitTargetId != null && this.pursuitTargetId.equals(var2.getUUID()) ? var1.tickCount >= this.pursuitReadyTick : false;
      }

      private boolean у_bbeebd87(LocalPlayer var1, long var2) {
         if (PvPHelper.this.comboFarStopSetting.getValue() && this.attackPauseUntilMillis <= var2 && this.selfProtectionUntilMillis <= var2) {
            Player var4 = this.у_d012b85c(var1);
            return this.Т(var1, var4) && this.Ь(var1, var4);
         } else {
            return false;
         }
      }

      private boolean а(long var1) {
         if (this.movementFreezeUntilMillis <= var1) {
            this.movementFreezeUntilMillis = 0L;
            return false;
         } else {
            return true;
         }
      }

      private boolean Ь_bbeebd87(LocalPlayer var1) {
         if (this.а(System.currentTimeMillis())) {
            return false;
         } else if (this.Ь(var1, System.currentTimeMillis())) {
            return false;
         } else {
            Player var2 = this.у_d012b85c(var1);
            if (PvPHelper.this.comboFarStopSetting.getValue() && this.Т(var1, var2) && this.Ь(var1, var2)) {
               return false;
            } else if (!this.у(MinecraftAccess.MINECRAFT_CLIENT.options.keyUp) || this.у(MinecraftAccess.MINECRAFT_CLIENT.options.keyDown)) {
               return false;
            } else if (var1.isShiftKeyDown() || var1.isUsingItem()) {
               return false;
            } else if (var1.isInWater() || var1.onClimbable() || var1.isSwimming()) {
               return false;
            } else {
               return !var1.isFallFlying() && !var1.isPassenger() && !var1.horizontalCollision
                  ? var1.isCreative() || var1.getFoodData().getFoodLevel() > 6
                  : false;
            }
         }
      }

      private boolean у_bbeebd87(LocalPlayer var1) {
         return var1.getAttackStrengthScale(1.0F) >= 1.0F;
      }

      private Player Ь_d012b85c(LocalPlayer var1) {
         Player var2 = this.н();
         if (!this.Т(var1, var2)) {
            return null;
         }

         double var3 = this.Ж(var1);
         if (!this.у(var1, var2, var3)) {
            return null;
         }

         if (!PvPHelper.this.comboRayTracingSetting.getValue()) {
            return var2;
         }

         Player var5 = this.у(var3);
         return var5 != null && var5.getUUID().equals(var2.getUUID()) ? var2 : null;
      }

      private void ч诶() {
         if (!PvPHelper.this.comboAssistSetting.getValue()) {
            this.чO();
         } else if (!PvPHelper.this.Ъ4()) {
            this.чO();
         } else if (MinecraftAccess.MINECRAFT_CLIENT.gui.screen() != null || MinecraftAccess.MINECRAFT_CLIENT.player.isSpectator() || !MinecraftAccess.MINECRAFT_CLIENT.player.isAlive()) {
            this.чO();
         } else if (!this.ч8() && !PvPHelper.this.Ъ5()) {
            long var1 = System.currentTimeMillis();
            boolean var3 = this.Ь(MinecraftAccess.MINECRAFT_CLIENT.player, var1);
            if (this.Б(var1)) {
               if (!var3 && this.selfProtectionUntilMillis > var1) {
                  this.movementFreezeUntilMillis = Math.max(this.selfProtectionUntilMillis, var1 + INPUT_FREEZE_EXTENSION_MILLIS);
               }
            } else if (!this.а(var1)) {
               if (!MinecraftAccess.MINECRAFT_CLIENT.player.isUsingItem() && !this.у(MinecraftAccess.MINECRAFT_CLIENT.options.keyDown) && !MinecraftAccess.MINECRAFT_CLIENT.player.isShiftKeyDown()) {
                  Player var4 = this.Ь_d012b85c(MinecraftAccess.MINECRAFT_CLIENT.player);
                  if (!this.Т(MinecraftAccess.MINECRAFT_CLIENT.player, var4)) {
                     this.чУ();
                  } else if (!this.у_bbeebd87(MinecraftAccess.MINECRAFT_CLIENT.player)) {
                     this.чУ();
                  } else {
                     if (!this.Ь_bbeebd87(MinecraftAccess.MINECRAFT_CLIENT.player)) {
                        this.чУ();
                        this.у(MinecraftAccess.MINECRAFT_CLIENT.player, var4, var1, false);
                     } else if (this.у_bbeebd87(MinecraftAccess.MINECRAFT_CLIENT.player, var4)) {
                        this.у(MinecraftAccess.MINECRAFT_CLIENT.player, var4, var1, true);
                     } else {
                        this.у_de5a6f78(MinecraftAccess.MINECRAFT_CLIENT.player, var4);
                     }
                  }
               } else {
                  this.чУ();
               }
            }
         } else {
            this.чУ();
         }
      }

      private boolean ч8() {
         ModuleRegistry var1 = RoseClient.getInstance().getServices().getModules();
         return var1.getAura().isEnabled() || var1.getTriggerBot().isEnabled();
      }
   }

   private enum WindPearlStage {
      IDLE,
      WAITING_WIND;
   }

   private final class AutoMaceController {
      private @com.google.gson.annotations.SerializedName("В")  PvPHelper.MaceAttackStage stage = PvPHelper.MaceAttackStage.IDLE;
      private @com.google.gson.annotations.SerializedName("п")  LivingEntity target;
      private @com.google.gson.annotations.SerializedName("Яр")  int previousSelectedSlot = -1;
      private @com.google.gson.annotations.SerializedName("Я6")  int remainingPreparationTicks;
      private @com.google.gson.annotations.SerializedName("сг")  long lastAttackMillis;
      private @com.google.gson.annotations.SerializedName("В诶")  boolean attackCallInProgress;
      private static final float MILLIS_PER_SECOND = 1000.0F;

      private void З(boolean var1) {
         if (var1 && this.Фь() && PvPHelper.this.Ъ4() && this.previousSelectedSlot != -1) {
            PvPHelper.this.method_6(this.previousSelectedSlot);
         }

         this.stage = PvPHelper.MaceAttackStage.IDLE;
         this.target = null;
         this.previousSelectedSlot = -1;
         this.remainingPreparationTicks = 0;
         this.attackCallInProgress = false;
      }

      private void Х(LivingEntity var1) {
         if (PvPHelper.this.Ъ4()
            && PvPHelper.this.autoMaceSetting.getValue()
            && this.stage == PvPHelper.MaceAttackStage.IDLE
            && PvPHelper.this.西(var1)
            && PvPHelper.this.в_a83dd0cc() != -1) {
            this.previousSelectedSlot = PvPHelper.this.method_9();
            this.target = var1;
            this.remainingPreparationTicks = Math.max(20, PvPHelper.this.chestPreswapSetting.getValue().intValue() + 12);
            this.stage = this.о(var1) && PvPHelper.this.П_a83dd0cc() != -1 ? PvPHelper.MaceAttackStage.PREPARED_AXE : PvPHelper.MaceAttackStage.PREPARED_MACE;
         }
      }

      private void Oб() {
         if (this.Фь()) {
            if (!PvPHelper.this.Ъ4()) {
               this.З(false);
            } else if (!this.Фт()) {
               this.OЬ();
            } else {
               if (--this.remainingPreparationTicks <= 0 || !PvPHelper.this.西(this.target) || MinecraftAccess.MINECRAFT_CLIENT.player.onGround()) {
                  this.OЬ();
               }
            }
         }
      }

      private void OЬ() {
         if (this.previousSelectedSlot != -1 && PvPHelper.this.Ъ4() && PvPHelper.this.method_9() != this.previousSelectedSlot) {
            PvPHelper.this.method_6(this.previousSelectedSlot);
         }

         this.З(false);
      }

      private boolean Р(LivingEntity var1, int var2, int var3) {
         try {
            if (PvPHelper.this.method_9() != var2) {
               PvPHelper.this.method_6(var2);
            }

            this.attackCallInProgress = true;

            try {
               MinecraftAccess.MINECRAFT_CLIENT.gameMode.attack(MinecraftAccess.MINECRAFT_CLIENT.player, var1);
               MinecraftAccess.MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
            } finally {
               this.attackCallInProgress = false;
            }

            if (PvPHelper.this.Ъ4() && PvPHelper.this.西(var1)) {
               if (PvPHelper.this.method_9() != var3) {
                  PvPHelper.this.method_6(var3);
               }

               this.stage = PvPHelper.MaceAttackStage.ATTACK_ARMED;
               return true;
            } else {
               this.OЬ();
               return false;
            }
         } catch (RuntimeException var8) {
            this.attackCallInProgress = false;
            this.OЬ();
            throw var8;
         }
      }

      private void OЮ() {
         if (this.stage == PvPHelper.MaceAttackStage.ATTACK_ARMED) {
            this.OЬ();
         }
      }

      private void OЦ() {
         if (this.Фт()) {
            this.OЬ();
         }
      }

      private boolean Й(LivingEntity var1) {
         return PvPHelper.this.西(var1) && (var1.getMainHandItem().is(Items.SHIELD) || var1.getOffhandItem().is(Items.SHIELD));
      }

      private boolean Фт() {
         return this.stage == PvPHelper.MaceAttackStage.PREPARED_AXE || this.stage == PvPHelper.MaceAttackStage.PREPARED_MACE;
      }

      private void С(LivingEntity var1) {
         if (PvPHelper.this.Ъ4() && !PvPHelper.this.Ъ2() && PvPHelper.this.西(var1)) {
            if (PvPHelper.this.auraOnlySetting.getValue()) {
               Aura var2 = PvPHelper.this.Ц_6fa770e6();
               if (var2 == null || var2.Ж_95bedd6e() != var1) {
                  return;
               }
            }

            if (this.Фт() && this.target != var1) {
               this.OЬ();
            } else {
               long var8 = System.currentTimeMillis();
               long var4 = (long)(PvPHelper.this.autoMaceDelaySetting.getValue() * MILLIS_PER_SECOND);
               if (this.Фт() || var8 - this.lastAttackMillis >= var4) {
                  int var6 = PvPHelper.this.в_a83dd0cc();
                  if (var6 != -1) {
                     if (this.о(var1)) {
                        int var7 = PvPHelper.this.П_a83dd0cc();
                        if (var7 != -1) {
                           if (this.previousSelectedSlot == -1) {
                              this.previousSelectedSlot = PvPHelper.this.method_9();
                           }

                           if (this.Р(var1, var7, var6)) {
                              this.target = null;
                              this.remainingPreparationTicks = 0;
                              this.lastAttackMillis = var8;
                           }
                        }
                     } else {
                        if (this.previousSelectedSlot == -1) {
                           this.previousSelectedSlot = PvPHelper.this.method_9();
                        }

                        if (PvPHelper.this.method_9() != var6) {
                           PvPHelper.this.method_6(var6);
                        }

                        this.target = null;
                        this.remainingPreparationTicks = 0;
                        this.stage = PvPHelper.MaceAttackStage.ATTACK_ARMED;
                        this.lastAttackMillis = var8;
                     }
                  }
               }
            }
         }
      }

      private boolean о(LivingEntity var1) {
         return this.Й(var1) && (PvPHelper.this.forceMaceSetting.getValue() || var1.isBlocking() && var1.getUseItem().is(Items.SHIELD));
      }

      private boolean Фь() {
         return this.stage != PvPHelper.MaceAttackStage.IDLE;
      }

      private boolean ФЧ() {
         return this.attackCallInProgress;
      }
   }

   private enum MaceFlightStage {
      IDLE,
      EQUIP_CHESTPLATE,
      WAIT_CHESTPLATE,
      WAIT_GLIDING_STOP;
   }

   private enum MaceAttackStage {
      IDLE,
      PREPARED_AXE,
      PREPARED_MACE,
      ATTACK_ARMED;
   }

   private final class SpearBoostController {
      private @com.google.gson.annotations.SerializedName("В")  PvPHelper.SpearBoostStage stage = PvPHelper.SpearBoostStage.IDLE;
      private @com.google.gson.annotations.SerializedName("ЯЬ")  int previousSelectedSlot = -1;
      private @com.google.gson.annotations.SerializedName("Я衣")  int spearSlot = -1;
      private @com.google.gson.annotations.SerializedName("Яш")  int chestplateInventorySlot = -1;
      private @com.google.gson.annotations.SerializedName("ЯИ")  int transitionDelayTicks;
      private @com.google.gson.annotations.SerializedName("Яь")  int flightResumeTimeoutTicks;
      private @com.google.gson.annotations.SerializedName("Яж")  int consecutiveGlidingTicks;
      private @com.google.gson.annotations.SerializedName("сО")  boolean attackCallInProgress;
      private @com.google.gson.annotations.SerializedName("сФ")  boolean restoreElytraOnExit;

      private void м(boolean var1) {
         if (var1 && PvPHelper.this.Ъ4()) {
            if (this.restoreElytraOnExit) {
               if (MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
                  PvPHelper.this.Ъ0();
               } else {
                  int var2 = InventoryActions.ш(Items.ELYTRA);
                  if (var2 != -1 && PvPHelper.this.西(var2) && MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
                     PvPHelper.this.Ъ0();
                  }
               }
            }

            if (this.previousSelectedSlot != -1 && PvPHelper.this.method_9() != this.previousSelectedSlot) {
               PvPHelper.this.method_6(this.previousSelectedSlot);
            }
         }

         this.stage = PvPHelper.SpearBoostStage.IDLE;
         this.previousSelectedSlot = -1;
         this.spearSlot = -1;
         this.chestplateInventorySlot = -1;
         this.transitionDelayTicks = 0;
         this.flightResumeTimeoutTicks = 0;
         this.consecutiveGlidingTicks = 0;
         this.attackCallInProgress = false;
         this.restoreElytraOnExit = false;
      }

      private void ЩА() {
         this.chestplateInventorySlot = -1;
         this.stage = PvPHelper.SpearBoostStage.RESUME_FLIGHT;
         this.transitionDelayTicks = 1;
         this.flightResumeTimeoutTicks = 8;
         this.consecutiveGlidingTicks = 0;
      }

      private void Щн() {
         if (MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            this.ЩА();
         } else {
            int var1 = InventoryActions.ш(Items.ELYTRA);
            if (var1 != -1) {
               if (PvPHelper.this.西(var1)) {
                  if (MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
                     this.ЩА();
                  } else {
                     this.stage = PvPHelper.SpearBoostStage.CONFIRM_ELYTRA;
                  }
               }
            }
         }
      }

      private boolean Иа() {
         return this.stage != PvPHelper.SpearBoostStage.IDLE;
      }

      private void ЩТ() {
         if (!MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            this.Щв();
         } else {
            if (this.chestplateInventorySlot < 0) {
               this.chestplateInventorySlot = InventoryInteractions.Ц_a83dd0cc();
            }

            if (this.chestplateInventorySlot == -1) {
               this.restoreElytraOnExit = false;
               this.ЩO();
            } else {
               if (PvPHelper.this.西(this.chestplateInventorySlot)) {
                  this.chestplateInventorySlot = -1;
                  this.Щв();
               }
            }
         }
      }

      private void Щ6() {
         if (PvPHelper.this.Ъ4() && !PvPHelper.this.Ъ5()) {
            int var1 = PvPHelper.this.Е();
            if (var1 != -1) {
               this.previousSelectedSlot = PvPHelper.this.method_9();
               this.spearSlot = var1;
               if (this.Ив()) {
                  int var2 = InventoryInteractions.Ц_a83dd0cc();
                  if (var2 != -1) {
                     this.chestplateInventorySlot = var2;
                     this.restoreElytraOnExit = true;
                     this.stage = PvPHelper.SpearBoostStage.EQUIP_CHESTPLATE;
                     return;
                  }
               }

               this.ЩO();
            }
         }
      }

      private void ЩO() {
         if (!PvPHelper.this.Ъ4()) {
            this.м(false);
         } else {
            if (this.spearSlot < 0 || this.spearSlot > 8 || !PvPHelper.this.spearItems.contains(MinecraftAccess.MINECRAFT_CLIENT.player.getInventory().getItem(this.spearSlot).getItem())) {
               this.spearSlot = PvPHelper.this.Е();
            }

            if (this.spearSlot == -1) {
               this.Щ4();
            } else {
               if (PvPHelper.this.method_9() != this.spearSlot) {
                  PvPHelper.this.method_6(this.spearSlot);
               }

               this.stage = this.restoreElytraOnExit ? PvPHelper.SpearBoostStage.EQUIP_ELYTRA : PvPHelper.SpearBoostStage.WAIT_SLOT_RESTORE;
               this.transitionDelayTicks = 1;
               this.attackCallInProgress = true;

               try {
                  MinecraftAccess.MINECRAFT_CLIENT.startAttack();
               } finally {
                  this.attackCallInProgress = false;
               }

               if (this.restoreElytraOnExit) {
                  this.Щн();
               }
            }
         }
      }

      private void Щ4() {
         if (this.restoreElytraOnExit) {
            this.stage = PvPHelper.SpearBoostStage.EQUIP_ELYTRA;
         } else {
            this.stage = PvPHelper.SpearBoostStage.WAIT_SLOT_RESTORE;
            this.transitionDelayTicks = 0;
         }
      }

      private void Ща() {
         if (this.transitionDelayTicks <= 0 || --this.transitionDelayTicks <= 0) {
            if (MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
               this.chestplateInventorySlot = InventoryInteractions.Ц_a83dd0cc();
               this.stage = PvPHelper.SpearBoostStage.EQUIP_CHESTPLATE;
            } else {
               this.ЩO();
            }
         }
      }

      private void ЩЪ() {
         if (MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            this.ЩА();
         } else {
            this.stage = PvPHelper.SpearBoostStage.EQUIP_ELYTRA;
         }
      }

      private void ЩО() {
         if (this.previousSelectedSlot != -1 && PvPHelper.this.Ъ4() && PvPHelper.this.method_9() != this.previousSelectedSlot) {
            PvPHelper.this.method_6(this.previousSelectedSlot);
         }

         this.м(false);
      }

      private void Щв() {
         this.stage = PvPHelper.SpearBoostStage.WAIT_CHESTPLATE;
         this.transitionDelayTicks = 1;
      }

      private boolean Ив() {
         return PvPHelper.this.spearElytraSetting.getValue()
            && MinecraftAccess.MINECRAFT_CLIENT.player.isFallFlying()
            && MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
      }

      private void Щя() {
         if (this.Иа()) {
            if (!PvPHelper.this.Ъ4()) {
               this.м(false);
            } else {
               switch (this.stage) {
                  case IDLE:
                  default:
                     break;
                  case EQUIP_CHESTPLATE:
                     this.ЩТ();
                     break;
                  case WAIT_CHESTPLATE:
                     this.Ща();
                     break;
                  case EQUIP_ELYTRA:
                     this.Щн();
                     break;
                  case CONFIRM_ELYTRA:
                     this.ЩЪ();
                     break;
                  case RESUME_FLIGHT:
                     this.Щз();
                     break;
                  case WAIT_SLOT_RESTORE:
                     this.Щ3();
               }
            }
         }
      }

      private void Щ9() {
         this.restoreElytraOnExit = false;
         this.flightResumeTimeoutTicks = 0;
         this.consecutiveGlidingTicks = 0;
         this.stage = PvPHelper.SpearBoostStage.WAIT_SLOT_RESTORE;
         this.transitionDelayTicks = 1;
      }

      private boolean ИO() {
         return this.attackCallInProgress;
      }

      private void Щ3() {
         if (this.transitionDelayTicks <= 0 || --this.transitionDelayTicks <= 0) {
            this.ЩО();
         }
      }

      private void Щз() {
         if (!MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            this.consecutiveGlidingTicks = 0;
            this.stage = PvPHelper.SpearBoostStage.EQUIP_ELYTRA;
         } else if (MinecraftAccess.MINECRAFT_CLIENT.player.onGround()) {
            this.Щ9();
         } else if (this.transitionDelayTicks > 0) {
            this.transitionDelayTicks--;
         } else {
            if (MinecraftAccess.MINECRAFT_CLIENT.player.isFallFlying()) {
               this.consecutiveGlidingTicks++;
            } else {
               this.consecutiveGlidingTicks = 0;
               PvPHelper.this.Ъ0();
            }

            if (this.flightResumeTimeoutTicks > 0) {
               this.flightResumeTimeoutTicks--;
            }

            if (this.consecutiveGlidingTicks >= 2 || this.flightResumeTimeoutTicks <= 0) {
               this.Щ9();
            }
         }
      }
   }

   private final class InstaCartController {
      @SerializedName("4")
      private final Set<Integer> processedArrowIds = new HashSet<>();
      private static final double ARROW_SEARCH_RADIUS = 128.0;
      private static final double ARROW_DRAG = 0.99;
      private static final double ARROW_VERTICAL_ACCELERATION = -0.05;
      private static final double TARGET_CENTER_Y_OFFSET = 0.5;
      private static final double CART_INTERACTION_Y_OFFSET = 0.25;

      private BlockPos 诶_10b53497(Arrow var1) {
         Vec3 var2 = var1.position();
         Vec3 var3 = var1.getDeltaMovement();

         for (int var4 = 0; var4 < 160; var4++) {
            Vec3 var5 = var2.add(var3);
            BlockHitResult var6 = MinecraftAccess.MINECRAFT_CLIENT.level.clip(new ClipContext(var2, var5, Block.COLLIDER, Fluid.NONE, var1));
            if (var6.getType() == Type.BLOCK) {
               return this.诶(var6);
            }

            var2 = var5;
            var3 = var3.scale(ARROW_DRAG).add(0.0, ARROW_VERTICAL_ACCELERATION, 0.0);
         }

         return null;
      }

      private void М_de5a6f78(BlockPos var1) {
         Vec3 var2 = Vec3.atCenterOf(var1).add(0.0, CART_INTERACTION_Y_OFFSET, 0.0);
         BlockHitResult var3 = new BlockHitResult(var2, Direction.UP, var1, false);
         MinecraftAccess.MINECRAFT_CLIENT.gameMode.useItemOn(MinecraftAccess.MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var3);
      }

      private void х5() {
         if (!PvPHelper.this.instaCartSetting.getValue() || !PvPHelper.this.Ъ4()) {
            this.хк();
         } else if (!PvPHelper.this.Ъ5()) {
            this.processedArrowIds.removeIf(var0 -> MinecraftAccess.MINECRAFT_CLIENT.level.getEntity(var0) == null);

            for (Arrow var2 : MinecraftAccess.MINECRAFT_CLIENT
               .level
               .getEntitiesOfClass(Arrow.class, MinecraftAccess.MINECRAFT_CLIENT.player.getBoundingBox().inflate(ARROW_SEARCH_RADIUS), var0 -> var0.getOwner() == MinecraftAccess.MINECRAFT_CLIENT.player)) {
               if (!this.processedArrowIds.contains(var2.getId())) {
                  if (var2.tickCount > 10) {
                     this.processedArrowIds.add(var2.getId());
                  } else {
                     BlockPos var3 = this.诶_10b53497(var2);
                     if (var3 != null && this.诶_bbeebd87(var3)) {
                        int var4 = PvPHelper.this.I(Items.RAIL);
                        int var5 = PvPHelper.this.I(Items.TNT_MINECART);
                        if (var4 != -1 && var5 != -1) {
                           this.processedArrowIds.add(var2.getId());
                           this.诶(var3, var4, var5);
                           break;
                        }
                     }
                  }
               }
            }
         }
      }

      private void 诶(BlockPos var1, int var2, int var3) {
         int var4 = PvPHelper.this.method_9();

         try {
            PvPHelper.this.method_6(var2);
            this.诶_de5a6f78(var1);
            PvPHelper.this.method_6(var3);
            this.М_de5a6f78(var1);
         } finally {
            if (PvPHelper.this.Ъ4() && PvPHelper.this.method_9() != var4) {
               PvPHelper.this.method_6(var4);
            }
         }
      }

      private boolean М_bbeebd87(BlockPos var1) {
         BlockState var2 = MinecraftAccess.MINECRAFT_CLIENT.level.getBlockState(var1);
         BlockState var3 = MinecraftAccess.MINECRAFT_CLIENT.level.getBlockState(var1.below());
         return var2.canBeReplaced() && var3.isFaceSturdy(MinecraftAccess.MINECRAFT_CLIENT.level, var1.below(), Direction.UP);
      }

      private void хк() {
         this.processedArrowIds.clear();
      }

      private BlockPos 诶(BlockHitResult var1) {
         BlockPos var2 = var1.getBlockPos();
         BlockPos var3 = var1.getDirection() == Direction.UP ? var2.above() : var2;

         for (int var4 = 2; var4 >= -4; var4--) {
            BlockPos var5 = var3.offset(0, var4, 0);
            if (this.М_bbeebd87(var5)) {
               return var5;
            }
         }

         return null;
      }

      private boolean 诶_bbeebd87(BlockPos var1) {
         return MinecraftAccess.MINECRAFT_CLIENT.player.isWithinBlockInteractionRange(var1.below(), 0.0)
            && MinecraftAccess.MINECRAFT_CLIENT.player.isWithinBlockInteractionRange(var1, 0.0);
      }

      private void 诶_de5a6f78(BlockPos var1) {
         BlockPos var2 = var1.below();
         Vec3 var3 = Vec3.atCenterOf(var2).add(0.0, TARGET_CENTER_Y_OFFSET, 0.0);
         BlockHitResult var4 = new BlockHitResult(var3, Direction.UP, var2, false);
         MinecraftAccess.MINECRAFT_CLIENT.gameMode.useItemOn(MinecraftAccess.MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var4);
      }
   }

   private final class AntiMaceController {
      @SerializedName("В")
      private final EntityMotionTracker targetMotionTracker = new EntityMotionTracker();
      @SerializedName("В")
      private final ProjectileOwnerMotion projectileOwnerMotion = new ProjectileOwnerMotion();
      @SerializedName("В")
      private final WindChargeInterceptSolver windChargeInterceptSolver = new WindChargeInterceptSolver();
      @SerializedName("В")
      private final FixedAngleRotation aimRotation = new FixedAngleRotation();
      @SerializedName("В")
      private Player targetPlayer;
      @SerializedName("В")
      private LocalPlayer observedPlayer;
      @SerializedName("В")
      private ClientLevel observedLevel;
      @SerializedName("В")
      private boolean rotationScheduled;
      @SerializedName("В")
      private int rotationScheduledTick;
      @SerializedName("Я")
      private int rotationExpiryTick = -1;
      @SerializedName("Я")
      private volatile boolean positionCorrectionPending;
      private static final double MAX_POSITION_CODEC_DRIFT_SQUARED = 256.0;
      private static final double PING_MILLIS_PER_TICK = 50.0;
      private static final double MAX_EXTRA_PREDICTION_TICKS = 6.0;
      private static final double ROCKET_SEARCH_RADIUS = 4.0;
      private static final double HORIZONTAL_DRAG = 0.91F;
      private static final double VERTICAL_DRAG = 0.98F;
      private static final double COLLISION_BOX_INFLATION = 0.3;

      private InteractionHand Й_8332a9ec() {
         if (MinecraftAccess.MINECRAFT_CLIENT.player.getOffhandItem().is(Items.WIND_CHARGE)
            && !MinecraftAccess.MINECRAFT_CLIENT.player.getCooldowns().isOnCooldown(MinecraftAccess.MINECRAFT_CLIENT.player.getOffhandItem())) {
            return InteractionHand.OFF_HAND;
         }

         int var1 = PvPHelper.this.I(Items.WIND_CHARGE);
         return var1 != -1 && !MinecraftAccess.MINECRAFT_CLIENT.player.getCooldowns().isOnCooldown(MinecraftAccess.MINECRAFT_CLIENT.player.getInventory().getItem(var1))
            ? InteractionHand.MAIN_HAND
            : null;
      }

      private void Р() {
         this.I();
         Player var1 = null;
         double var2 = PvPHelper.this.antiMaceRangeSetting.getValue() * PvPHelper.this.antiMaceRangeSetting.getValue();

         for (Player var5 : MinecraftAccess.MINECRAFT_CLIENT.level.players()) {
            if (this.Й(var5)) {
               double var6 = MinecraftAccess.MINECRAFT_CLIENT.player.distanceToSqr(var5);
               if (var6 < var2 || var6 == var2 && (var1 == null || var5.getId() < var1.getId())) {
                  var1 = var5;
                  var2 = var6;
               }
            }
         }

         if (var1 != this.targetPlayer) {
            this.targetMotionTracker.l1();
         }

         this.targetPlayer = var1;
         if (this.targetPlayer != null) {
            this.Й_65128868();
         }
      }

      private EntityMotionTracker.Sample Й_65128868() {
         Vec3 var1 = this.targetPlayer.getPositionCodec().getBase();
         if (var1.distanceToSqr(this.targetPlayer.position()) > MAX_POSITION_CODEC_DRIFT_SQUARED) {
            var1 = this.targetPlayer.position();
         }

         return this.targetMotionTracker
            .д(
               this.targetPlayer.getId(),
               MinecraftAccess.MINECRAFT_CLIENT.player.tickCount,
               var1,
               this.targetPlayer.getKnownMovement(),
               this.targetPlayer.getInterpolation().yRot(),
               this.targetPlayer.getInterpolation().xRot()
            );
      }

      private boolean Й_bbeebd87() {
         return this.rotationScheduled || this.rotationExpiryTick >= 0;
      }

      private void Й_de5a6f78() {
         if (PvPHelper.this.Ъ4()
            && MinecraftAccess.MINECRAFT_CLIENT.getConnection() != null
            && !PvPHelper.this.Ъ5()
            && !MinecraftAccess.MINECRAFT_CLIENT.player.isSpectator()
            && !MinecraftAccess.MINECRAFT_CLIENT.player.isUsingItem()) {
            this.Р();
            if (this.targetPlayer != null && this.Й_8332a9ec() != null) {
               RotationRegistry var1 = RoseClient.getInstance().getServices().getRotations();
               RotationController var2 = RoseClient.getInstance().getControllers().getRotations();
               if (var2.getEffectiveStrategy() == null) {
                  var2.getCameraRotation().set(MinecraftAccess.MINECRAFT_CLIENT.player.getYRot(), MinecraftAccess.MINECRAFT_CLIENT.player.getXRot());
                  var2.getAppliedRotation().set(MinecraftAccess.MINECRAFT_CLIENT.player.getYRot(), MinecraftAccess.MINECRAFT_CLIENT.player.getXRot());
               }

               this.aimRotation.setTargetAngles(MinecraftAccess.MINECRAFT_CLIENT.player.getYRot(), MinecraftAccess.MINECRAFT_CLIENT.player.getXRot());
               if (var1.scheduleStrategy(this.aimRotation, 6, 20)) {
                  this.rotationScheduled = true;
                  this.rotationScheduledTick = MinecraftAccess.MINECRAFT_CLIENT.player.tickCount;
               }
            }
         }
      }

      private void Й(PacketSentEvent var1) {
         if (PvPHelper.this.Ъ4() && PvPHelper.this.antiMaceKeybindSetting.getValue() != -1) {
            this.I();
            if (var1.packet() instanceof ServerboundMovePlayerPacket var2) {
               Vec3 var4 = this.projectileOwnerMotion.l().position();
               this.projectileOwnerMotion.Г(new Vec3(var2.getX(var4.x), var2.getY(var4.y), var2.getZ(var4.z)), var2.isOnGround());
            } else if (var1.packet() instanceof ServerboundClientTickEndPacket) {
               this.projectileOwnerMotion.П衣();
            }
         }
      }

      private boolean Й(Player var1) {
         return var1 != MinecraftAccess.MINECRAFT_CLIENT.player
            && var1.isAlive()
            && !var1.isRemoved()
            && !var1.isSpectator()
            && var1.level() == MinecraftAccess.MINECRAFT_CLIENT.level
            && PvPHelper.this.comboAssistController.Т(MinecraftAccess.MINECRAFT_CLIENT.player, var1);
      }

      private void Й(PlayerMovementSyncEvent.Post var1) {
         if (this.rotationScheduled) {
            if (PvPHelper.this.Ъ4()
               && this.observedLevel == MinecraftAccess.MINECRAFT_CLIENT.level
               && this.observedPlayer == MinecraftAccess.MINECRAFT_CLIENT.player
               && MinecraftAccess.MINECRAFT_CLIENT.getConnection() != null
               && !this.positionCorrectionPending
               && !MinecraftAccess.MINECRAFT_CLIENT.player.isPassenger()
               && MinecraftAccess.MINECRAFT_CLIENT.gui.screen() == null
               && !MinecraftAccess.MINECRAFT_CLIENT.player.isSpectator()
               && !MinecraftAccess.MINECRAFT_CLIENT.player.isUsingItem()) {
               if (RoseClient.getInstance().getServices().getRotations().getActiveStrategy() != this.aimRotation) {
                  this.ц();
               } else {
                  this.Р();
                  InteractionHand var2 = this.Й_8332a9ec();
                  if (this.targetPlayer != null && var2 != null) {
                     EntityMotionTracker.Sample var3 = this.Й_65128868();
                     double var4 = var3.age() + Math.clamp(CommandPingProbe.ЪУ / PING_MILLIS_PER_TICK, 0.0, MAX_EXTRA_PREDICTION_TICKS);
                     MobEffectInstance var6 = this.targetPlayer.getEffect(MobEffects.LEVITATION);
                     boolean var7 = this.targetPlayer.isFallFlying()
                        && MinecraftAccess.MINECRAFT_CLIENT
                           .level
                           .getEntitiesOfClass(FireworkRocketEntity.class, this.targetPlayer.getBoundingBox().inflate(ROCKET_SEARCH_RADIUS), Entity::isAlive)
                           .stream()
                           .anyMatch(var1x -> ((FireworkRocketEntityAccessor)var1x).rose$getAttachedEntity() == this.targetPlayer);
                     EntityMotionSimulator.MotionProfile var8 = new EntityMotionSimulator.MotionProfile(
                        this.targetPlayer.onGround(),
                        this.targetPlayer.isFallFlying(),
                        this.targetPlayer.getAbilities().flying,
                        this.targetPlayer.isInWater() || this.targetPlayer.isInLava(),
                        this.targetPlayer.onClimbable(),
                        this.targetPlayer.hasEffect(MobEffects.SLOW_FALLING),
                        var6 == null ? -1 : var6.getAmplifier(),
                        var7,
                        this.targetPlayer.isNoGravity() ? 0.0 : this.targetPlayer.getGravity(),
                        HORIZONTAL_DRAG,
                        VERTICAL_DRAG,
                        this.targetPlayer.maxUpStep(),
                        this.targetPlayer.getInterpolation().yRot(),
                        this.targetPlayer.getInterpolation().xRot()
                     );
                     AABB var9 = this.targetPlayer.getBoundingBox().move(var3.position().subtract(this.targetPlayer.position()));
                     List var10 = EntityMotionSimulator.К(
                        var9,
                        var3,
                        var8,
                        40 + (int)Math.ceil(var4) + 1,
                        (var1x, var2x) -> Entity.collideBoundingBox(this.targetPlayer, var2x, var1x, MinecraftAccess.MINECRAFT_CLIENT.level, List.of()),
                        PlayerMotionPredictor::й
                     );
                     ProjectileOwnerMotion.Sample var11 = this.projectileOwnerMotion.l();
                     Vec3 var12 = var11.inherited();
                     Vec3 var13 = var11.position().add(0.0, MinecraftAccess.MINECRAFT_CLIENT.player.getEyeHeight(), 0.0);
                     WindChargeInterceptSolver.Solution var14 = this.windChargeInterceptSolver.М(var13, var12, var10, var4, this::Й);
                     if (var14 == null) {
                        this.ц();
                     } else {
                        int var15 = PvPHelper.this.method_9();
                        float var16 = MinecraftAccess.MINECRAFT_CLIENT.player.getYRot();
                        float var17 = MinecraftAccess.MINECRAFT_CLIENT.player.getXRot();
                        this.aimRotation.setTargetAngles(var14.yaw(), var14.pitch());

                        try {
                           if (var2 == InteractionHand.MAIN_HAND) {
                              PvPHelper.this.method_6(PvPHelper.this.I(Items.WIND_CHARGE));
                           }

                           MinecraftAccess.MINECRAFT_CLIENT.player.setYRot(var14.yaw());
                           MinecraftAccess.MINECRAFT_CLIENT.player.setXRot(var14.pitch());
                           if (MinecraftAccess.MINECRAFT_CLIENT.gameMode.useItem(MinecraftAccess.MINECRAFT_CLIENT.player, var2).consumesAction()) {
                              MinecraftAccess.MINECRAFT_CLIENT.player.swing(var2);
                           }
                        } finally {
                           MinecraftAccess.MINECRAFT_CLIENT.player.setYRot(var16);
                           MinecraftAccess.MINECRAFT_CLIENT.player.setXRot(var17);
                           PvPHelper.this.method_6(var15);
                           this.rotationScheduled = false;
                           this.rotationExpiryTick = MinecraftAccess.MINECRAFT_CLIENT.player.tickCount + 2;
                        }
                     }
                  } else {
                     this.ц();
                  }
               }
            } else {
               this.ц();
            }
         }
      }

      private void ц() {
         RoseClient.getInstance().getServices().getRotations().removeStrategy(this.aimRotation);
         RoseClient.getInstance().getControllers().getRotations().releaseStrategy(this.aimRotation);
         this.rotationScheduled = false;
         this.rotationExpiryTick = -1;
         this.targetPlayer = null;
         this.observedPlayer = null;
         this.observedLevel = null;
         this.targetMotionTracker.l1();
      }

      private void I() {
         if (this.positionCorrectionPending) {
            this.ц();
            this.positionCorrectionPending = false;
         }

         if (this.observedLevel != MinecraftAccess.MINECRAFT_CLIENT.level || this.observedPlayer != MinecraftAccess.MINECRAFT_CLIENT.player) {
            this.ц();
            this.observedLevel = MinecraftAccess.MINECRAFT_CLIENT.level;
            this.observedPlayer = MinecraftAccess.MINECRAFT_CLIENT.player;
            this.projectileOwnerMotion.l(MinecraftAccess.MINECRAFT_CLIENT.player.position(), MinecraftAccess.MINECRAFT_CLIENT.player.onGround());
         }
      }

      private double Й(Vec3 var1, Vec3 var2) {
         BlockHitResult var3 = MinecraftAccess.MINECRAFT_CLIENT.level.clipIncludingBorder(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, MinecraftAccess.MINECRAFT_CLIENT.player));
         double var4 = var3.getType() == Type.MISS ? Double.POSITIVE_INFINITY : var1.distanceTo(var3.getLocation());

         for (Entity var7 : MinecraftAccess.MINECRAFT_CLIENT
            .level
            .getEntities(
               MinecraftAccess.MINECRAFT_CLIENT.player,
               new AABB(var1, var2).inflate(1.0),
               var1x -> var1x != this.targetPlayer
                  && var1x.isPickable()
                  && !var1x.isSpectator()
                  && !(var1x instanceof AbstractWindCharge)
                  && !var1x.is(EntityTypes.END_CRYSTAL)
            )) {
            AABB var8 = var7.getBoundingBox().inflate(COLLISION_BOX_INFLATION);
            if (var8.contains(var1)) {
               return 0.0;
            }

            Optional var9 = var8.clip(var1, var2);
            if (var9.isPresent()) {
               var4 = Math.min(var4, var1.distanceTo((Vec3)var9.get()));
            }
         }

         return var4;
      }

      private void я() {
         if (PvPHelper.this.Ъ4() && PvPHelper.this.antiMaceKeybindSetting.getValue() != -1) {
            if (!this.rotationScheduled || MinecraftAccess.MINECRAFT_CLIENT.gui.screen() == null && MinecraftAccess.MINECRAFT_CLIENT.player.tickCount - this.rotationScheduledTick <= 4) {
               if (this.rotationExpiryTick >= 0 && MinecraftAccess.MINECRAFT_CLIENT.player.tickCount >= this.rotationExpiryTick) {
                  RoseClient.getInstance().getServices().getRotations().expireScheduledStrategy(this.aimRotation);
                  this.rotationExpiryTick = -1;
               }

               this.Р();
            } else {
               this.ц();
            }
         } else {
            this.ц();
         }
      }
   }

   private final class SpearElytraController {
      private @com.google.gson.annotations.SerializedName("В")  PvPHelper.MaceFlightStage stage = PvPHelper.MaceFlightStage.IDLE;
      private @com.google.gson.annotations.SerializedName("т")  LivingEntity target;
      private @com.google.gson.annotations.SerializedName("Х0")  int chestplateInventorySlot = -1;
      private @com.google.gson.annotations.SerializedName("Хх")  int waitTicks;
      private @com.google.gson.annotations.SerializedName("ХУ")  int equipAttempts;
      private @com.google.gson.annotations.SerializedName("ХЗ")  int stageTimeoutTicks;
      private @com.google.gson.annotations.SerializedName("ХЕ")  int cooldownTicks;
      private @com.google.gson.annotations.SerializedName("ВЕ")  boolean chestplateSwapInProgress;
      private @com.google.gson.annotations.SerializedName("ВП")  boolean interceptedAttackPending;
      private @com.google.gson.annotations.SerializedName("В西")  boolean attackCallInProgress;
      private @com.google.gson.annotations.SerializedName("ХП")  int trackedTargetEntityId = -1;
      private @com.google.gson.annotations.SerializedName("э")  Vec3 previousPlayerVelocity = Vec3.ZERO;
      private @com.google.gson.annotations.SerializedName("ю")  Vec3 previousTargetVelocity = Vec3.ZERO;
      private @com.google.gson.annotations.SerializedName("Щ")  Vec3 smoothedPlayerAcceleration = Vec3.ZERO;
      private @com.google.gson.annotations.SerializedName("у")  Vec3 smoothedTargetAcceleration = Vec3.ZERO;
      private @com.google.gson.annotations.SerializedName("еУ")  float previousPlayerYaw;
      private @com.google.gson.annotations.SerializedName("еЗ")  float previousPlayerPitch;
      private @com.google.gson.annotations.SerializedName("еЕ")  float previousTargetYaw;
      private @com.google.gson.annotations.SerializedName("еП")  float previousTargetPitch;
      private @com.google.gson.annotations.SerializedName("е西")  float smoothedPlayerYawDelta;
      private @com.google.gson.annotations.SerializedName("ед")  float smoothedPlayerPitchDelta;
      private @com.google.gson.annotations.SerializedName("еБ")  float smoothedTargetYawDelta;
      private @com.google.gson.annotations.SerializedName("еС")  float smoothedTargetPitchDelta;
      private @com.google.gson.annotations.SerializedName("Вд")  boolean motionHistoryReady;
      private static final float AURA_TARGET_SEARCH_RANGE = 96.0F;
      private static final double MAX_PLAYER_ACCELERATION_DELTA = 1.25;
      private static final double MAX_TARGET_ACCELERATION_DELTA = 0.75;
      private static final double PLAYER_ACCELERATION_HISTORY_WEIGHT = 0.55;
      private static final double PLAYER_ACCELERATION_SAMPLE_WEIGHT = 0.45;
      private static final double TARGET_ACCELERATION_HISTORY_WEIGHT = 0.65;
      private static final double TARGET_ACCELERATION_SAMPLE_WEIGHT = 0.35;
      private static final float PLAYER_YAW_HISTORY_WEIGHT = 0.55F;
      private static final float PLAYER_YAW_SAMPLE_WEIGHT = 0.45F;
      private static final float PLAYER_PITCH_HISTORY_WEIGHT = 0.55F;
      private static final float PLAYER_PITCH_SAMPLE_WEIGHT = 0.45F;
      private static final float TARGET_YAW_HISTORY_WEIGHT = 0.65F;
      private static final float TARGET_YAW_SAMPLE_WEIGHT = 0.35F;
      private static final float TARGET_PITCH_HISTORY_WEIGHT = 0.65F;
      private static final float TARGET_PITCH_SAMPLE_WEIGHT = 0.35F;
      private static final double PREDICTED_ATTACK_DISTANCE = 3.0;
      private static final double TARGET_ACCELERATION_DECAY = 0.82;
      private static final float TARGET_YAW_RATE_DECAY = 0.8F;
      private static final float TARGET_PITCH_RATE_DECAY = 0.8F;
      private static final double PLAYER_FALL_GRAVITY = -0.08;
      private static final double PLAYER_FALL_HORIZONTAL_X_DRAG = 0.91;
      private static final double PLAYER_FALL_VERTICAL_DRAG = 0.98;
      private static final double PLAYER_FALL_HORIZONTAL_Z_DRAG = 0.91;
      private static final double GROUND_HORIZONTAL_X_DRAG = 0.91;
      private static final double GROUND_HORIZONTAL_Z_DRAG = 0.91;
      private static final double AIR_GRAVITY = -0.08;
      private static final double AIR_HORIZONTAL_X_DRAG = 0.91;
      private static final double AIR_VERTICAL_DRAG = 0.98;
      private static final double AIR_HORIZONTAL_Z_DRAG = 0.91;
      private static final double RADIANS_PER_HALF_TURN = Math.PI;
      private static final double DEGREES_PER_HALF_TURN = 180.0;
      private static final double VIEW_DIRECTION_SCALE_REFERENCE = 0.4;
      private static final double GLIDE_BASE_VERTICAL_ACCELERATION = -0.08;
      private static final double GLIDE_LOOK_LIFT_SCALE = 0.06;
      private static final double GLIDE_DESCENT_COMPENSATION = -0.1;
      private static final double GLIDE_UPWARD_LIFT_SCALE = 0.04;
      private static final double GLIDE_UPWARD_LIFT_MULTIPLIER = 3.2;
      private static final double GLIDE_HORIZONTAL_X_CORRECTION = 0.1;
      private static final double GLIDE_HORIZONTAL_Z_CORRECTION = 0.1;
      private static final double GLIDE_HORIZONTAL_X_DRAG = 0.99;
      private static final double GLIDE_VERTICAL_DRAG = 0.98;
      private static final double GLIDE_HORIZONTAL_Z_DRAG = 0.99;
      private static final float FULL_TURN_DEGREES = 360.0F;
      private static final float HALF_TURN_DEGREES = 180.0F;
      private static final float POSITIVE_YAW_WRAP_DEGREES = 360.0F;
      private static final float MIN_WRAPPED_YAW_DEGREES = -180.0F;
      private static final float NEGATIVE_YAW_WRAP_DEGREES = 360.0F;
      private static final float MIN_PITCH_DEGREES = -90.0F;
      private static final float MAX_PITCH_DEGREES = 90.0F;

      private boolean Гк() {
         return this.Гя() && !this.attackCallInProgress;
      }

      private float method_0(float var1) {
         float var2 = var1 % FULL_TURN_DEGREES;
         if (var2 >= HALF_TURN_DEGREES) {
            var2 -= POSITIVE_YAW_WRAP_DEGREES;
         }

         if (var2 < MIN_WRAPPED_YAW_DEGREES) {
            var2 += NEGATIVE_YAW_WRAP_DEGREES;
         }

         return var2;
      }

      private boolean П(LivingEntity var1) {
         if (!PvPHelper.this.auraOnlySetting.getValue()) {
            return true;
         }

         Aura var2 = PvPHelper.this.Ц_6fa770e6();
         return var2 != null && var2.Ж_95bedd6e() == var1;
      }

      private void Х7() {
         if (this.interceptedAttackPending) {
            this.Хн();
         } else {
            this.ХИ();
         }
      }

      private void Х4() {
         PvPHelper.this.autoMaceController.OЦ();
         this.stage = PvPHelper.MaceFlightStage.IDLE;
         this.target = null;
         this.chestplateInventorySlot = -1;
         this.waitTicks = 0;
         this.equipAttempts = 0;
         this.stageTimeoutTicks = 0;
         this.cooldownTicks = 0;
         this.chestplateSwapInProgress = false;
         this.interceptedAttackPending = false;
         this.attackCallInProgress = false;
         this.Ху();
      }

      private void Х0() {
         if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
         }

         if (!this.Гя()) {
            this.ХБ();
         } else if (PvPHelper.this.Ъ4() && PvPHelper.this.西(this.target)) {
            if (--this.stageTimeoutTicks <= 0 && !this.chestplateSwapInProgress) {
               if (this.Г4()) {
                  this.Х7();
               } else {
                  this.Х4();
               }
            } else {
               switch (this.stage) {
                  case IDLE:
                  default:
                     break;
                  case EQUIP_CHESTPLATE:
                     this.ХЪ();
                     break;
                  case WAIT_CHESTPLATE:
                     this.Хз();
                     break;
                  case WAIT_GLIDING_STOP:
                     this.Хт();
               }
            }
         } else {
            this.Х4();
         }
      }

      private void Г(LivingEntity var1, boolean var2) {
         int var3 = InventoryInteractions.Ц_a83dd0cc();
         if (var3 != -1) {
            if (!var2) {
               PvPHelper.this.autoMaceController.Х(var1);
            }

            this.target = var1;
            this.chestplateInventorySlot = var3;
            this.waitTicks = 0;
            this.equipAttempts = 0;
            this.stageTimeoutTicks = 100;
            this.chestplateSwapInProgress = false;
            this.interceptedAttackPending = var2;
            this.stage = PvPHelper.MaceFlightStage.EQUIP_CHESTPLATE;
         }
      }

      private Vec3 method_8(Vec3 var1) {
         return var1.add(0.0, PLAYER_FALL_GRAVITY, 0.0).multiply(PLAYER_FALL_HORIZONTAL_X_DRAG, PLAYER_FALL_VERTICAL_DRAG, PLAYER_FALL_HORIZONTAL_Z_DRAG);
      }

      private void Хз() {
         if (this.waitTicks > 0) {
            this.waitTicks--;
         } else if (!MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            this.Хэ();
         } else {
            this.chestplateInventorySlot = InventoryInteractions.Ц_a83dd0cc();
            this.stageTimeoutTicks = 20;
            this.stage = PvPHelper.MaceFlightStage.EQUIP_CHESTPLATE;
         }
      }

      private float Ъ(float var1) {
         return Math.max(MIN_PITCH_DEGREES, Math.min(MAX_PITCH_DEGREES, var1));
      }

      private boolean Гя() {
         return this.stage != PvPHelper.MaceFlightStage.IDLE;
      }

      private void ХИ() {
         this.stage = PvPHelper.MaceFlightStage.IDLE;
         this.target = null;
         this.chestplateInventorySlot = -1;
         this.waitTicks = 0;
         this.equipAttempts = 0;
         this.stageTimeoutTicks = 0;
         this.cooldownTicks = 2;
         this.chestplateSwapInProgress = false;
         this.interceptedAttackPending = false;
         this.Ху();
      }

      private boolean Г4() {
         return PvPHelper.this.Ъ4()
            && !MinecraftAccess.MINECRAFT_CLIENT.player.isFallFlying()
            && !MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
      }

      private void Ху() {
         this.trackedTargetEntityId = -1;
         this.previousPlayerVelocity = Vec3.ZERO;
         this.previousTargetVelocity = Vec3.ZERO;
         this.smoothedPlayerAcceleration = Vec3.ZERO;
         this.smoothedTargetAcceleration = Vec3.ZERO;
         this.previousPlayerYaw = 0.0F;
         this.previousPlayerPitch = 0.0F;
         this.previousTargetYaw = 0.0F;
         this.previousTargetPitch = 0.0F;
         this.smoothedPlayerYawDelta = 0.0F;
         this.smoothedPlayerPitchDelta = 0.0F;
         this.smoothedTargetYawDelta = 0.0F;
         this.smoothedTargetPitchDelta = 0.0F;
         this.motionHistoryReady = false;
      }

      private void щ(EntityAttackEvent var1) {
         if (!this.attackCallInProgress && var1.getTarget() instanceof LivingEntity var2) {
            if (this.Гя()) {
               if (this.target == var2) {
                  if (this.Г4()) {
                     this.ХИ();
                  } else {
                     var1.setCancelled(true);
                  }
               }
            } else if (this.В(var1, var2)) {
               var1.setCancelled(true);
               this.Г(var2, true);
            }
         }
      }

      private Vec3 щ(LivingEntity var1, Vec3 var2, float var3, float var4) {
         Vec3 var5 = var1.calculateViewVector(var4, var3);
         double var6 = Math.hypot(var5.x, var5.z);
         double var8 = Math.hypot(var2.x, var2.z);
         double var10 = var5.length();
         double var12 = var4 * RADIANS_PER_HALF_TURN / DEGREES_PER_HALF_TURN;
         double var14 = Math.cos(var12);
         var14 = var14 * var14 * Math.min(1.0, var10 / VIEW_DIRECTION_SCALE_REFERENCE);
         Vec3 var16 = var2.add(0.0, GLIDE_BASE_VERTICAL_ACCELERATION + var14 * GLIDE_LOOK_LIFT_SCALE, 0.0);
         if (var16.y < 0.0 && var6 > 0.0) {
            double var17 = var16.y * GLIDE_DESCENT_COMPENSATION * var14;
            var16 = var16.add(var5.x / var6 * var17, var17, var5.z / var6 * var17);
         }

         if (var12 < 0.0 && var6 > 0.0) {
            double var20 = var8 * -Math.sin(var12) * GLIDE_UPWARD_LIFT_SCALE;
            var16 = var16.add(-var5.x / var6 * var20, var20 * GLIDE_UPWARD_LIFT_MULTIPLIER, -var5.z / var6 * var20);
         }

         if (var6 > 0.0) {
            var16 = var16.add((var5.x / var6 * var8 - var16.x) * GLIDE_HORIZONTAL_X_CORRECTION, 0.0, (var5.z / var6 * var8 - var16.z) * GLIDE_HORIZONTAL_Z_CORRECTION);
         }

         return var16.multiply(GLIDE_HORIZONTAL_X_DRAG, GLIDE_VERTICAL_DRAG, GLIDE_HORIZONTAL_Z_DRAG);
      }

      private void ХБ() {
         if (PvPHelper.this.Ъ4() && this.cooldownTicks <= 0) {
            Aura var1 = PvPHelper.this.Ц_6fa770e6();
            LivingEntity var2 = var1 == null ? null : var1.Б(AURA_TARGET_SEARCH_RANGE);
            if (PvPHelper.this.西(var2) && this.ж(var2)) {
               this.诶(var2);
               if (this.motionHistoryReady) {
                  int var3 = this.В(var2, 80);
                  int var4 = PvPHelper.this.chestPreswapSetting.getValue().intValue();
                  if (var3 >= 1 && var3 <= var4) {
                     this.Г(var2, false);
                  }
               }
            } else {
               if (!MinecraftAccess.MINECRAFT_CLIENT.player.isFallFlying() || !MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
                  this.Ху();
               }
            }
         } else {
            this.Ху();
         }
      }

      private void Хн() {
         LivingEntity var1 = this.target;
         if (PvPHelper.this.Ъ4() && PvPHelper.this.西(var1) && this.Г4()) {
            this.attackCallInProgress = true;

            try {
               MinecraftAccess.MINECRAFT_CLIENT.gameMode.attack(MinecraftAccess.MINECRAFT_CLIENT.player, var1);
            } finally {
               this.attackCallInProgress = false;
               this.ХИ();
            }
         } else {
            this.Х4();
         }
      }

      private boolean ж(LivingEntity var1) {
         return PvPHelper.this.autoMaceSetting.getValue()
            && PvPHelper.this.useChestSetting.getValue()
            && MinecraftAccess.MINECRAFT_CLIENT.gui.screen() == null
            && this.cooldownTicks == 0
            && !PvPHelper.this.autoMaceController.Фь()
            && !PvPHelper.this.spearBoostController.Иа()
            && !PvPHelper.this.windPearlController.эь()
            && PvPHelper.this.西(var1)
            && PvPHelper.this.в_a83dd0cc() != -1
            && InventoryInteractions.Ц_a83dd0cc() != -1
            && !MinecraftAccess.MINECRAFT_CLIENT.player.onGround()
            && MinecraftAccess.MINECRAFT_CLIENT.player.isFallFlying()
            && MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
      }

      private void 诶(LivingEntity var1) {
         Vec3 var2 = MinecraftAccess.MINECRAFT_CLIENT.player.getDeltaMovement();
         Vec3 var3 = var1.getDeltaMovement();
         if (this.trackedTargetEntityId != var1.getId()) {
            this.trackedTargetEntityId = var1.getId();
            this.previousPlayerVelocity = var2;
            this.previousTargetVelocity = var3;
            this.previousPlayerYaw = MinecraftAccess.MINECRAFT_CLIENT.player.getYRot();
            this.previousPlayerPitch = MinecraftAccess.MINECRAFT_CLIENT.player.getXRot();
            this.previousTargetYaw = var1.getYRot();
            this.previousTargetPitch = var1.getXRot();
            this.smoothedPlayerAcceleration = Vec3.ZERO;
            this.smoothedTargetAcceleration = Vec3.ZERO;
            this.smoothedPlayerYawDelta = 0.0F;
            this.smoothedPlayerPitchDelta = 0.0F;
            this.smoothedTargetYawDelta = 0.0F;
            this.smoothedTargetPitchDelta = 0.0F;
            this.motionHistoryReady = false;
         } else {
            float var4 = MinecraftAccess.MINECRAFT_CLIENT.player.getYRot();
            float var5 = MinecraftAccess.MINECRAFT_CLIENT.player.getXRot();
            float var6 = var1.getYRot();
            float var7 = var1.getXRot();
            Vec3 var8 = this.В(MinecraftAccess.MINECRAFT_CLIENT.player, this.previousPlayerVelocity, this.previousPlayerYaw, this.previousPlayerPitch);
            Vec3 var9 = this.В(var1, this.previousTargetVelocity, this.previousTargetYaw, this.previousTargetPitch);
            Vec3 var10 = this.щ(var2.subtract(var8), MAX_PLAYER_ACCELERATION_DELTA);
            Vec3 var11 = this.щ(var3.subtract(var9), MAX_TARGET_ACCELERATION_DELTA);
            this.smoothedPlayerAcceleration = this.smoothedPlayerAcceleration.scale(PLAYER_ACCELERATION_HISTORY_WEIGHT).add(var10.scale(PLAYER_ACCELERATION_SAMPLE_WEIGHT));
            this.smoothedTargetAcceleration = this.smoothedTargetAcceleration.scale(TARGET_ACCELERATION_HISTORY_WEIGHT).add(var11.scale(TARGET_ACCELERATION_SAMPLE_WEIGHT));
            this.smoothedPlayerYawDelta = this.smoothedPlayerYawDelta * PLAYER_YAW_HISTORY_WEIGHT + this.method_0(var4 - this.previousPlayerYaw) * PLAYER_YAW_SAMPLE_WEIGHT;
            this.smoothedPlayerPitchDelta = this.smoothedPlayerPitchDelta * PLAYER_PITCH_HISTORY_WEIGHT + (var5 - this.previousPlayerPitch) * PLAYER_PITCH_SAMPLE_WEIGHT;
            this.smoothedTargetYawDelta = this.smoothedTargetYawDelta * TARGET_YAW_HISTORY_WEIGHT + this.method_0(var6 - this.previousTargetYaw) * TARGET_YAW_SAMPLE_WEIGHT;
            this.smoothedTargetPitchDelta = this.smoothedTargetPitchDelta * TARGET_PITCH_HISTORY_WEIGHT + (var7 - this.previousTargetPitch) * TARGET_PITCH_SAMPLE_WEIGHT;
            this.previousPlayerVelocity = var2;
            this.previousTargetVelocity = var3;
            this.previousPlayerYaw = var4;
            this.previousPlayerPitch = var5;
            this.previousTargetYaw = var6;
            this.previousTargetPitch = var7;
            this.motionHistoryReady = true;
         }
      }

      private void Хэ() {
         MinecraftAccess.MINECRAFT_CLIENT.player.stopFallFlying();
         this.waitTicks = 0;
         this.stageTimeoutTicks = 12;
         this.stage = PvPHelper.MaceFlightStage.WAIT_GLIDING_STOP;
      }

      private void ХЪ() {
         if (!this.chestplateSwapInProgress && !MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            this.Хэ();
         } else {
            if (this.chestplateInventorySlot < 0) {
               this.chestplateInventorySlot = InventoryInteractions.Ц_a83dd0cc();
            }

            if (this.chestplateInventorySlot != -1 && this.equipAttempts < 4) {
               this.chestplateSwapInProgress = true;
               if (PvPHelper.this.西(this.chestplateInventorySlot)) {
                  this.chestplateSwapInProgress = false;
                  this.equipAttempts++;
                  if (!MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
                     this.Хэ();
                  } else {
                     this.stageTimeoutTicks = 8;
                     this.waitTicks = 0;
                     this.stage = PvPHelper.MaceFlightStage.WAIT_CHESTPLATE;
                  }
               }
            } else {
               this.Х4();
            }
         }
      }

      private Vec3 щ(Vec3 var1, double var2) {
         double var4 = var1.lengthSqr();
         double var6 = var2 * var2;
         return !(var4 <= var6) && var4 != 0.0 ? var1.scale(var2 / Math.sqrt(var4)) : var1;
      }

      private int В(LivingEntity var1, int var2) {
         Vec3 var3 = MinecraftAccess.MINECRAFT_CLIENT.player.position();
         Vec3 var4 = var1.position();
         Vec3 var5 = MinecraftAccess.MINECRAFT_CLIENT.player.getDeltaMovement();
         Vec3 var6 = var1.getDeltaMovement();
         Vec3 var7 = this.smoothedPlayerAcceleration;
         Vec3 var8 = this.smoothedTargetAcceleration;
         float var9 = var1.getYRot();
         float var10 = var1.getXRot();
         float var11 = this.smoothedTargetYawDelta;
         float var12 = this.smoothedTargetPitchDelta;
         double var13 = var3.distanceTo(var4);

         for (int var15 = 1; var15 <= var2; var15++) {
            var9 = this.method_0(var9 + var11);
            var10 = this.Ъ(var10 + var12);
            if (var15 == 1) {
               float var16 = this.method_0(MinecraftAccess.MINECRAFT_CLIENT.player.getYRot() + this.smoothedPlayerYawDelta);
               float var17 = this.Ъ(MinecraftAccess.MINECRAFT_CLIENT.player.getXRot() + this.smoothedPlayerPitchDelta);
               var5 = this.щ(MinecraftAccess.MINECRAFT_CLIENT.player, var5, var16, var17).add(var7);
            } else {
               var5 = this.method_8(var5);
            }

            var6 = this.В(var1, var6, var9, var10).add(var8);
            var3 = var3.add(var5);
            var4 = var4.add(var6);
            double var18 = var3.distanceTo(var4);
            if (var18 <= PREDICTED_ATTACK_DISTANCE && var18 < var13) {
               return var15;
            }

            var13 = var18;
            var7 = Vec3.ZERO;
            var8 = var8.scale(TARGET_ACCELERATION_DECAY);
            var11 *= TARGET_YAW_RATE_DECAY;
            var12 *= TARGET_PITCH_RATE_DECAY;
         }

         return -1;
      }

      private Vec3 В(LivingEntity var1, Vec3 var2, float var3, float var4) {
         if (var1.isFallFlying()) {
            return this.щ(var1, var2, var3, var4);
         } else {
            return var1.onGround() ? new Vec3(var2.x * GROUND_HORIZONTAL_X_DRAG, 0.0, var2.z * GROUND_HORIZONTAL_Z_DRAG) : var2.add(0.0, AIR_GRAVITY, 0.0).multiply(AIR_HORIZONTAL_X_DRAG, AIR_VERTICAL_DRAG, AIR_HORIZONTAL_Z_DRAG);
         }
      }

      private boolean В(EntityAttackEvent var1, LivingEntity var2) {
         return PvPHelper.this.Ъ4() && !var1.isCancelled() && var1.getAttacker() == MinecraftAccess.MINECRAFT_CLIENT.player && this.ж(var2) && this.П(var2);
      }

      private void Хт() {
         if (MinecraftAccess.MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            this.chestplateInventorySlot = InventoryInteractions.Ц_a83dd0cc();
            this.stageTimeoutTicks = 20;
            this.stage = PvPHelper.MaceFlightStage.EQUIP_CHESTPLATE;
         } else if (MinecraftAccess.MINECRAFT_CLIENT.player.isFallFlying()) {
            MinecraftAccess.MINECRAFT_CLIENT.player.stopFallFlying();
         } else {
            this.Х7();
         }
      }
   }

   private final class WindPearlController {
      private @com.google.gson.annotations.SerializedName("В")  PvPHelper.WindPearlStage stage = PvPHelper.WindPearlStage.IDLE;
      private @com.google.gson.annotations.SerializedName("В6")  int previousSelectedSlot = -1;
      private @com.google.gson.annotations.SerializedName("В_")  int windChargeDelayTicks;
      private @com.google.gson.annotations.SerializedName("Яй")  float savedYaw;
      private @com.google.gson.annotations.SerializedName("ЯЖ")  float savedPitch;

      private void М(boolean var1) {
         if (var1 && this.эь() && PvPHelper.this.Ъ4() && this.previousSelectedSlot != -1 && PvPHelper.this.method_9() != this.previousSelectedSlot) {
            PvPHelper.this.method_6(this.previousSelectedSlot);
         }

         PvPHelper.this.ю(false);
         this.stage = PvPHelper.WindPearlStage.IDLE;
         this.previousSelectedSlot = -1;
         this.windChargeDelayTicks = 0;
         this.savedYaw = 0.0F;
         this.savedPitch = 0.0F;
      }

      private void ЕЮ() {
         if (this.эь()) {
            PvPHelper.this.ю(true);
            if (!PvPHelper.this.Ъ4()) {
               this.М(false);
            } else if (this.windChargeDelayTicks > 0) {
               this.windChargeDelayTicks--;
            } else {
               int var1 = PvPHelper.this.I(Items.WIND_CHARGE);
               if (var1 == -1) {
                  this.Ел();
               } else {
                  float var2 = MinecraftAccess.MINECRAFT_CLIENT.player.getYRot();
                  float var3 = MinecraftAccess.MINECRAFT_CLIENT.player.getXRot();

                  try {
                     MinecraftAccess.MINECRAFT_CLIENT.player.setYRot(this.savedYaw);
                     MinecraftAccess.MINECRAFT_CLIENT.player.setXRot(this.savedPitch);
                     PvPHelper.this.method_6(var1);
                     PvPHelper.this.Ъс();
                  } finally {
                     MinecraftAccess.MINECRAFT_CLIENT.player.setYRot(var2);
                     MinecraftAccess.MINECRAFT_CLIENT.player.setXRot(var3);
                     this.Ел();
                  }
               }
            }
         }
      }

      private void Ел() {
         if (this.previousSelectedSlot != -1 && PvPHelper.this.Ъ4() && PvPHelper.this.method_9() != this.previousSelectedSlot) {
            PvPHelper.this.method_6(this.previousSelectedSlot);
         }

         this.М(false);
      }

      private void Еб() {
         if (PvPHelper.this.Ъ4() && !PvPHelper.this.Ъ5()) {
            int var1 = PvPHelper.this.I(Items.ENDER_PEARL);
            int var2 = PvPHelper.this.I(Items.WIND_CHARGE);
            if (var1 != -1 && var2 != -1) {
               this.previousSelectedSlot = PvPHelper.this.method_9();
               this.savedYaw = MinecraftAccess.MINECRAFT_CLIENT.player.getYRot();
               this.savedPitch = MinecraftAccess.MINECRAFT_CLIENT.player.getXRot();
               this.stage = PvPHelper.WindPearlStage.WAITING_WIND;
               this.windChargeDelayTicks = 3;
               PvPHelper.this.ю(true);
               PvPHelper.this.method_6(var1);
               MinecraftAccess.MINECRAFT_CLIENT.options.keyJump.setDown(true);

               try {
                  PvPHelper.this.Ъс();
               } finally {
                  MinecraftAccess.MINECRAFT_CLIENT.options.keyJump.setDown(false);
               }
            }
         }
      }

      private boolean эь() {
         return this.stage != PvPHelper.WindPearlStage.IDLE;
      }
   }

   private enum SpearBoostStage {
      IDLE,
      EQUIP_CHESTPLATE,
      WAIT_CHESTPLATE,
      EQUIP_ELYTRA,
      CONFIRM_ELYTRA,
      RESUME_FLIGHT,
      WAIT_SLOT_RESTORE;
   }
}
