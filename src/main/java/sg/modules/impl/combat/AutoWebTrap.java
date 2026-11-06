package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult.Success;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.ec.BlockPathGeometry;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.mixin.FireworkRocketEntityAccessor;
import sg.render.pipeline.CrystalPlacementPlanner;
import sg.util.input.MouseSensitivityMath;
import sg.settings.BooleanSetting;
import sg.client.ClientServices;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.BlockPlacementHit;
import sg.modules.impl.init.BlockPlacementUtils;
import sg.modules.impl.init.EntityMotionSimulator;
import sg.modules.impl.init.EntityMotionTracker;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.modules.impl.init.HotbarSelectionSession;
import sg.rotation.RotationStrategy;
import sg.modules.impl.init.TargetMotionPredictor;
import sg.modules.impl.init.WebTrapConfirmation;

public class AutoWebTrap extends ClientModule {
   private static final double сю = 4.5;
   private static final int ЯЙ = 20;
   private static final double сЩ = 0.25;
   private final @com.google.gson.annotations.SerializedName("сс")  BooleanSetting matrixBypassSetting;
   @SerializedName("В")
   private final AutoWebTrap.PlacementRotation placementRotation;
   @SerializedName("В")
   private final HotbarSelectionSession hotbarSession;
   @SerializedName("В")
   private final WebTrapConfirmation confirmationTracker;
   @SerializedName("В")
   private final TargetMotionPredictor targetMotionPredictor;
   private final @com.google.gson.annotations.SerializedName("К")  Map<BlockPos, Integer> blockedPositionsUntilTick;
   @SerializedName("I")
   private final Map<BlockPos, Integer> placementFailureCounts;
   @SerializedName("I")
   private final List<BlockPos> placementPath;
   private @com.google.gson.annotations.SerializedName("а")  ClientLevel observedLevel;
   @SerializedName("А")
   private Player currentTarget;
   @SerializedName("В")
   private BlockPlacementHit pendingPlacementHit;
   @SerializedName("А")
   private BlockPos selectedPlacementPosition;
   private @com.google.gson.annotations.SerializedName("Як")  int selectedPlacementTick;
   private @com.google.gson.annotations.SerializedName("Яг")  int placementReadyTick;
   private @com.google.gson.annotations.SerializedName("Я0")  int lastObservedPlayerTick;
   private @com.google.gson.annotations.SerializedName("су")  double smoothedPlacementLatencyTicks;
   @SerializedName("5")
   private Vec3 lastObservedTargetPosition;
   private final @com.google.gson.annotations.SerializedName("Т")  Map<BlockPos, Integer> pendingPlacementsSinceTick;
   private final @com.google.gson.annotations.SerializedName("с必")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("сщ")  EventListener<PlayerMovementSyncEvent> movementSyncListener;
   private final @com.google.gson.annotations.SerializedName("сГ")  EventListener<PlayerMovementSyncEvent.Post> postMovementSyncListener;
   private static final String иЩ = "AutoWebTrap";
   private static final String иу = "Ловит цель в паутину и передаёт её CrystalAura";
   private static final String и2 = "Обход Matrix";
   private static final double иф = 2.0;
   private static final double ии = 4.5;
   private static final double и9 = 4.0;
   private static final double ио = 0.91F;
   private static final double иO = 0.98F;
   private static final double и7 = 3.0;
   private static final double иР = 0.25;
   private static final double и3 = 3.0;
   private static final double ир = Double.MAX_VALUE;
   private static final double и6 = 3.0;
   private static final double и_ = 0.25;
   private static final double иШ = 0.75;
   private static final double иы = 2.0;
   private static final double ий = 0.5;
   private static final double иЖ = 0.02;
   private static final double им = 1.0E-10;
   private static final double иК = 4.5;
   private static final double иI = 4.5;
   private static final double иТ = 0.5;
   private static final double иО = 0.15;
   private static final double иФ = 0.05;
   private static final double иЙ = 4.5;
   private static final double ик = 4.5;
   private static final double иг = 256.0;
   private static final double и0 = Double.MAX_VALUE;
   private static final double их = 64.0;
   private static final double иУ = 2.0;
   private static final double иЗ = 4.5;

   @Override
   public void onEnable() {
      this.СИ();
   }

   private TargetMotionPredictor.PredictionWindow у() {
      Vec3 var1 = method_1_2f96ac28(this.currentTarget);
      EntityMotionTracker.Sample var2 = this.targetMotionPredictor
         .O(this.currentTarget.getId(), MINECRAFT_CLIENT.player.tickCount, var1, this.currentTarget.getKnownMovement());
      this.lastObservedTargetPosition = var1;
      MobEffectInstance var3 = this.currentTarget.getEffect(MobEffects.LEVITATION);
      boolean var4 = this.currentTarget.isFallFlying()
         && MINECRAFT_CLIENT.level
            .getEntitiesOfClass(FireworkRocketEntity.class, this.currentTarget.getBoundingBox().inflate(и9), Entity::isAlive)
            .stream()
            .anyMatch(var1x -> ((FireworkRocketEntityAccessor)var1x).rose$getAttachedEntity() == this.currentTarget);
      EntityMotionSimulator.MotionProfile var5 = new EntityMotionSimulator.MotionProfile(
         this.currentTarget.onGround(),
         this.currentTarget.isFallFlying(),
         this.currentTarget.getAbilities().flying,
         this.currentTarget.isInWater() || this.currentTarget.isInLava(),
         this.currentTarget.onClimbable(),
         this.currentTarget.hasEffect(MobEffects.SLOW_FALLING),
         var3 == null ? -1 : var3.getAmplifier(),
         var4,
         this.currentTarget.isNoGravity() ? 0.0 : this.currentTarget.getGravity(),
         ио,
         иO,
         this.currentTarget.maxUpStep(),
         this.currentTarget.getInterpolation().yRot(),
         this.currentTarget.getInterpolation().xRot()
      );
      return TargetMotionPredictor.O(
         у(this.currentTarget),
         var2,
         var5,
         this.smoothedPlacementLatencyTicks,
         this.matrixBypassSetting.getValue(),
         (var1x, var2x) -> Entity.collideBoundingBox(this.currentTarget, var2x, var1x, MINECRAFT_CLIENT.level, List.of()),
         var1x -> BlockPathGeometry.У(var1x, this::Щ)
      );
   }

   private void СО() {
      this.placementReadyTick = -1;
      this.hotbarSession.еЬ();
      this.pendingPlacementHit = null;
      this.selectedPlacementPosition = null;
      RoseClient.getInstance().getServices().getRotations().removeStrategy(this.placementRotation);
      RoseClient.getInstance().getControllers().getRotations().releaseStrategy(this.placementRotation);
   }

   private int УХ() {
      if (MINECRAFT_CLIENT.player == null) {
         return -1;
      }

      for (int var1 = 0; var1 < 9; var1++) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var1).is(Items.COBWEB)) {
            return var1;
         }
      }

      return -1;
   }

   private List<BlockPos> у(TargetMotionPredictor.PredictionWindow var1, int var2) {
      double var3 = var1.readyAt(1);
      double var5 = var1.readyAt(var2) + и3;
      ArrayList<AutoWebTrap.TrapCandidate> var7 = new ArrayList<>();

      for (BlockPos var9 : BlockPathGeometry.ш(var1.boxes(), var3, var5)) {
         if (this.д(var9)) {
            BlockPathGeometry.OverlapInterval var10 = BlockPathGeometry.ш(var1.boxes(), var9, var3, var5);
            if (var10 != null) {
               var7.add(new AutoWebTrap.TrapCandidate(var9, var10.enter()));
            }
         }
      }

      var7.sort(Comparator.comparingDouble(AutoWebTrap.TrapCandidate::arrival));
      HashMap var21 = new HashMap();
      List var22 = List.of();
      double var23 = ир;
      int var12 = 0;

      for (AutoWebTrap.TrapCandidate var14 : var7) {
         if (var12++ >= 24) {
            break;
         }

         List var15 = BlockPathGeometry.ш(var14.position(), var2, this::д, var1x -> {
            if (!var21.containsKey(var1x)) {
               var21.put(var1x, BlockPlacementUtils.method_5(var1x, MINECRAFT_CLIENT.player.getEyePosition(), иЗ, true));
            }

            return var21.get(var1x) != null;
         });
         if (!var15.isEmpty()) {
            double var16 = var1.readyAt(var15.size());
            BlockPathGeometry.OverlapInterval var18 = BlockPathGeometry.ш(var1.boxes(), var14.position(), var16, var16 + и6);
            if (var18 != null && !(var18.exit() - var18.enter() < и_)) {
               double var19 = var18.enter()
                  - var3
                  + (var15.size() - 1) * иШ
                  - Math.min(иы, var18.exit() - var18.enter()) * ий
                  + MINECRAFT_CLIENT.player.getEyePosition().distanceToSqr(Vec3.atCenterOf((Vec3i)var15.getFirst())) * иЖ;
               if (var19 < var23) {
                  var23 = var19;
                  var22 = var15;
               }
            }
         }
      }

      return var22;
   }

   private boolean в(Player var1) {
      return BlockPathGeometry.ш(у(var1), this::Щ);
   }

   private boolean у(TargetMotionPredictor.PredictionWindow var1) {
      int var2 = this.placementPath.size();
      double var3 = 0.0;
      Integer var5 = this.pendingPlacementsSinceTick.get(this.placementPath.getFirst());
      if (var5 != null) {
         var2--;
         var3 = Math.max(0.0, this.smoothedPlacementLatencyTicks - (MINECRAFT_CLIENT.player.tickCount - var5)) + 1.0;
      }

      double var6 = var1.readyAt(var2) + var3;
      BlockPathGeometry.OverlapInterval var8 = BlockPathGeometry.ш(var1.boxes(), this.placementPath.getLast(), var6, var6 + и7);
      return var8 != null && var8.exit() - var8.enter() >= иР;
   }

   private static Vec3 method_1_2f96ac28(Player var0) {
      Vec3 var1 = var0.getPositionCodec().getBase();
      return Double.isFinite(var1.lengthSqr()) && var1.distanceToSqr(var0.position()) <= иг ? var1 : var0.position();
   }

   private boolean Ф(Player var1) {
      return BlockPathGeometry.У(у(var1), this::Щ);
   }

   private static AABB у(Player var0) {
      return TargetMotionPredictor.O(var0.getBoundingBox(), var0.position(), method_1_2f96ac28(var0));
   }

   private void Сж() {
      if (this.observedLevel != MINECRAFT_CLIENT.level || MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.tickCount < this.lastObservedPlayerTick) {
         this.СИ();
         this.observedLevel = MINECRAFT_CLIENT.level;
      }

      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && !MINECRAFT_CLIENT.player.isSpectator()) {
         this.lastObservedPlayerTick = MINECRAFT_CLIENT.player.tickCount;
         if (!RoseClient.getInstance().getServices().getModules().getAimbot().щж()) {
            int var1 = this.УХ();
            if (!this.А(this.currentTarget)) {
               this.СИ();
               this.observedLevel = MINECRAFT_CLIENT.level;
               if (var1 >= 0) {
                  this.currentTarget = this.Щ();
               }
            }

            if (this.currentTarget != null) {
               this.blockedPositionsUntilTick.values().removeIf(var0 -> var0 <= MINECRAFT_CLIENT.player.tickCount);
               this.СП();
               boolean var2 = this.в(this.currentTarget);
               this.confirmationTracker.Ш(this.currentTarget.getUUID(), MINECRAFT_CLIENT.player.tickCount, var2, this.Ф(this.currentTarget));
               if (var2 || this.confirmationTracker.э(this.currentTarget.getUUID()) || var1 < 0) {
                  this.placementPath.clear();
                  this.СО();
               } else if (this.pendingPlacementsSinceTick.size() >= 6) {
                  this.СО();
               } else if (MINECRAFT_CLIENT.player.isUsingItem()) {
                  this.placementPath.clear();
                  this.СО();
               } else {
                  TargetMotionPredictor.PredictionWindow var3 = this.у();

                  while (!this.placementPath.isEmpty() && this.Щ(this.placementPath.getFirst())) {
                     this.placementPath.removeFirst();
                  }

                  if (this.placementPath.size() == 1 && this.pendingPlacementsSinceTick.containsKey(this.placementPath.getFirst())) {
                     this.placementPath.clear();
                  }

                  if (!this.placementPath.isEmpty() && !this.у(var3)) {
                     this.placementPath.clear();
                  }

                  if (!this.placementPath.isEmpty() && BlockPlacementUtils.method_3(this.placementPath.getFirst())) {
                     List var4 = this.у(var3, 1);
                     if (var4.isEmpty()) {
                        this.СО();
                        return;
                     }

                     this.placementPath.clear();
                     this.placementPath.addAll(var4);
                  }

                  if (this.placementPath.isEmpty()) {
                     this.placementPath.addAll(this.у(var3, 3));
                  }

                  if (this.placementPath.isEmpty()) {
                     this.СО();
                  } else {
                     BlockPos var5 = this.placementPath.getFirst();
                     if (!var5.equals(this.selectedPlacementPosition)) {
                        this.selectedPlacementPosition = var5;
                        this.selectedPlacementTick = MINECRAFT_CLIENT.player.tickCount;
                     } else if (MINECRAFT_CLIENT.player.tickCount - this.selectedPlacementTick > 20) {
                        this.Ф(var5);
                        this.СО();
                        return;
                     }

                     if (!this.д(var5)) {
                        this.placementPath.clear();
                        this.СО();
                     } else {
                        this.pendingPlacementHit = BlockPlacementUtils.method_5(var5, MINECRAFT_CLIENT.player.getEyePosition(), ии, true);
                        if (this.pendingPlacementHit == null) {
                           this.placementPath.clear();
                           this.СО();
                        } else {
                           this.placementRotation.setTargetPosition(this.pendingPlacementHit.hitVec());
                           if (!RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.placementRotation, 2, 35)) {
                              this.СО();
                           } else {
                              this.hotbarSession.Я(var1, this.matrixBypassSetting.getValue());
                           }
                        }
                     }
                  }
               }
            }
         }
      } else {
         this.СИ();
      }
   }

   private void СП() {
      Iterator var1 = this.pendingPlacementsSinceTick.entrySet().iterator();

      while (var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         BlockPos var3 = (BlockPos)var2.getKey();
         if (this.Щ(var3)) {
            double var6 = Math.clamp(MINECRAFT_CLIENT.player.tickCount - (Integer)var2.getValue(), 1, 8);
            this.smoothedPlacementLatencyTicks = this.smoothedPlacementLatencyTicks + (var6 - this.smoothedPlacementLatencyTicks) * (var6 > this.smoothedPlacementLatencyTicks ? иТ : иО);
            this.placementFailureCounts.remove(var3);
            var1.remove();
         } else {
            int var4 = MINECRAFT_CLIENT.player.tickCount - (Integer)var2.getValue();
            if (var4 > 20 || var4 >= 2 && !BlockPlacementUtils.method_3(var3)) {
               this.Ф(var3);
               var1.remove();
            }
         }
      }
   }

   private boolean д(BlockPos var1) {
      return MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.player != null
         && !this.blockedPositionsUntilTick.containsKey(var1)
         && !this.pendingPlacementsSinceTick.containsKey(var1)
         && MINECRAFT_CLIENT.level.getBlockState(var1).canBeReplaced()
         && !MINECRAFT_CLIENT.player.getBoundingBox().inflate(иФ).intersects(new AABB(var1))
         && CrystalPlacementPlanner.method_4(MINECRAFT_CLIENT.player.getEyePosition(), var1) <= BlockPlacementUtils.O(иЙ) * BlockPlacementUtils.O(ик);
   }

   private void СИ() {
      this.СО();
      this.currentTarget = null;
      this.pendingPlacementsSinceTick.clear();
      this.targetMotionPredictor.method_1ж();
      this.lastObservedTargetPosition = null;
      this.smoothedPlacementLatencyTicks = иУ;
      this.confirmationTracker.method_4ч();
      this.placementPath.clear();
      this.blockedPositionsUntilTick.clear();
      this.placementFailureCounts.clear();
   }

   public boolean method_1Ю() {
      return this.isEnabled()
         && this.А(this.currentTarget)
         && this.д_d012b85c() == null
         && (this.pendingPlacementHit != null || !this.pendingPlacementsSinceTick.isEmpty() || this.confirmationTracker.I(this.currentTarget.getUUID()));
   }

   public Player д_d012b85c() {
      return this.isEnabled()
            && this.А(this.currentTarget)
            && this.confirmationTracker.э(this.currentTarget.getUUID())
            && this.Ф(this.currentTarget)
         ? this.currentTarget
         : null;
   }

   @Override
   public void onDisable() {
      this.СИ();
   }

   private void а(PlayerMovementSyncEvent var1) {
      this.placementReadyTick = -1;
      if (!RoseClient.getInstance().getServices().getModules().getAimbot().щж()) {
         if (this.pendingPlacementHit != null && !this.placementPath.isEmpty() && this.А(this.currentTarget) && !MINECRAFT_CLIENT.player.isUsingItem()) {
            if (!var1.isCancelled() && RoseClient.getInstance().getServices().getRotations().getActiveStrategy() == this.placementRotation) {
               if (this.УХ() < 0) {
                  this.СИ();
               } else if ((this.lastObservedTargetPosition == null || method_1_2f96ac28(this.currentTarget).distanceToSqr(this.lastObservedTargetPosition) > им) && !this.у(this.у())) {
                  this.placementPath.clear();
                  this.СО();
               } else {
                  Vec3 var2 = var1.getPosition().add(0.0, MINECRAFT_CLIENT.player.getEyeHeight(), 0.0);
                  BlockPlacementHit var3 = BlockPlacementUtils.method_5(this.placementPath.getFirst(), var2, иК, true);
                  if (var3 == null) {
                     this.placementPath.clear();
                     this.СО();
                  } else {
                     this.pendingPlacementHit = var3;
                     this.placementRotation.setTargetPosition(var3.hitVec());
                     this.placementReadyTick = MINECRAFT_CLIENT.player.tickCount;
                  }
               }
            }
         }
      }
   }

   private boolean А(Player var1) {
      if (MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.level != null
         && var1 != null
         && var1 != MINECRAFT_CLIENT.player
         && var1.level() == MINECRAFT_CLIENT.level
         && var1.isAlive()
         && !var1.isRemoved()
         && !var1.isSpectator()
         && !var1.isCreative()
         && var1.tickCount >= 3
         && !(CrystalPlacementPlanner.method_4(MINECRAFT_CLIENT.player.getEyePosition(), var1.getBoundingBox()) > их)) {
         ClientServices var2 = RoseClient.getInstance().getServices();
         return !var2.getFriends().О(var1.getGameProfile().name()) && !var2.getModules().getAntiBot().detectedBots.contains(var1);
      } else {
         return false;
      }
   }

   private void Ф(BlockPos var1) {
      int var2 = this.placementFailureCounts.merge(var1.immutable(), 1, Integer::sum);
      this.blockedPositionsUntilTick.put(var1.immutable(), MINECRAFT_CLIENT.player.tickCount + (var2 >= 3 ? 40 : 6));
      this.placementPath.clear();
   }

   private boolean Щ(BlockPos var1) {
      return MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.level.getBlockState(var1).is(Blocks.COBWEB) && !BlockPlacementUtils.method_3(var1);
   }

   public AutoWebTrap() {
      super(иЩ, иу, ModuleCategory.COMBAT);
      this.matrixBypassSetting = new BooleanSetting(и2, false);
      this.placementRotation = new AutoWebTrap.PlacementRotation();
      this.hotbarSession = new HotbarSelectionSession();
      this.confirmationTracker = new WebTrapConfirmation();
      this.targetMotionPredictor = new TargetMotionPredictor();
      this.blockedPositionsUntilTick = new HashMap<>();
      this.placementFailureCounts = new HashMap<>();
      this.placementPath = new ArrayList<>();
      this.placementReadyTick = -1;
      this.lastObservedPlayerTick = -1;
      this.smoothedPlacementLatencyTicks = иф;
      this.pendingPlacementsSinceTick = new HashMap<>();
      this.playerTickListener = new EventListener<PlayerTickEvent>() {
         public void onEvent(PlayerTickEvent var1) {
            AutoWebTrap.this.Сж();
         }

         @Override
         public int getPriority() {
            return 50;
         }
      };
      this.movementSyncListener = new EventListener<PlayerMovementSyncEvent>() {
         public void onEvent(PlayerMovementSyncEvent var1) {
            AutoWebTrap.this.а(var1);
         }

         @Override
         public int getPriority() {
            return -100;
         }
      };
      this.postMovementSyncListener = this::С;
      this.addSettings(this.matrixBypassSetting);
   }

   private Player Щ() {
      Player var1 = null;
      double var2 = и0;

      for (Player var5 : MINECRAFT_CLIENT.level.players()) {
         if (this.А(var5)) {
            double var6 = MINECRAFT_CLIENT.player.distanceToSqr(var5);
            if (var6 < var2) {
               var2 = var6;
               var1 = var5;
            }
         }
      }

      return var1;
   }

   private void С(PlayerMovementSyncEvent.Post var1) {
      if (!RoseClient.getInstance().getServices().getModules().getAimbot().щж()) {
         if (this.pendingPlacementHit != null && !this.placementPath.isEmpty() && this.А(this.currentTarget) && MINECRAFT_CLIENT.gameMode != null && !MINECRAFT_CLIENT.player.isUsingItem()) {
            if (this.placementReadyTick == MINECRAFT_CLIENT.player.tickCount && !var1.isCancelled() && RoseClient.getInstance().getServices().getRotations().getActiveStrategy() == this.placementRotation) {
               this.placementReadyTick = -1;
               BlockPos var2 = this.placementPath.getFirst();
               if (this.д(var2)) {
                  int var3 = this.УХ();
                  if (var3 < 0) {
                     this.СИ();
                  } else if (this.hotbarSession.Я(var3, this.matrixBypassSetting.getValue())) {
                     BlockHitResult var4 = BlockPlacementUtils.method_5(this.pendingPlacementHit, иI, var1);
                     if (var4 != null) {
                        if (!BlockPlacementUtils.method_5(var2, var4, MINECRAFT_CLIENT.player.getMainHandItem())) {
                           this.placementPath.clear();
                           this.СО();
                        } else {
                           try {
                              if (MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var4) instanceof Success var6
                                 && var6.wasItemInteraction()
                                 && MINECRAFT_CLIENT.level.getBlockState(var2).is(Blocks.COBWEB)) {
                                 this.pendingPlacementsSinceTick.putIfAbsent(var2.immutable(), MINECRAFT_CLIENT.player.tickCount);
                                 MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
                              } else {
                                 this.Ф(var2);
                              }
                           } finally {
                              this.СО();
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static final class PlacementRotation extends RotationStrategy {
      private static final float В6衣 = 1.0E-6F;
      private static final float В6ш = -180.0F;
      private static final float В6И = 180.0F;
      private static final float В6ь = -90.0F;
      private static final float В6ж = 90.0F;

      private PlacementRotation() {
         super(null);
         this.setCaptureCameraRotationOnActivate(true);
      }

      @Override
      public Vector2f calculateRotation(Vector2f var1, Vector2f var2) {
         float var3 = Mth.wrapDegrees(var2.x - var1.x);
         float var4 = var2.y - var1.y;
         float var5 = MouseSensitivityMath.getRotationStep();
         if (var5 > В6衣) {
            var3 = Math.round(var3 / var5) * var5;
            var4 = Math.round(var4 / var5) * var5;
         }

         return new Vector2f(var1.x + Mth.clamp(var3, В6ш, В6И), Mth.clamp(var1.y + var4, В6ь, В6ж));
      }
   }

   private record TrapCandidate(BlockPos position, double arrival) {
   }
}
