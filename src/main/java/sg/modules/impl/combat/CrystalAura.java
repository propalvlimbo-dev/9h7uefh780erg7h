package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult.Success;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.ec.PlayerMotionPredictor;
import sg.events.impl.WorldEffectsEvent;
import sg.mixin.MultiPlayerGameModeAccessor;
import sg.render.pipeline.CrystalPlacementPlanner;
import sg.render.pipeline.InventoryActions;
import sg.settings.BooleanSetting;
import sg.util.time.Stopwatch;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.BlockDestroyDelayEvent;
import sg.modules.impl.init.BlockPlacementHit;
import sg.modules.impl.init.BlockPlacementUtils;
import sg.modules.impl.init.CrystalAttackCandidate;
import sg.modules.impl.init.CrystalDamageTarget;
import sg.modules.impl.init.CrystalPlacementCandidate;
import sg.modules.impl.init.CrystalPlacementMarker;
import sg.modules.impl.init.RoseXrayLines;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.FloatSetting;
import sg.modules.impl.init.FriendRegistry;
import sg.modules.impl.init.HotbarSelectionSession;
import sg.settings.NumericDisplayFormat;
import sg.rotation.SharedRotationCoordinator;

public class CrystalAura extends ClientModule {
   private static final int еР = 6;
   private static final int е3 = 10;
   private static final int ер = 40;
   private static final int е6 = 80;
   private static final long Во = 300L;
   private static final long ВO = 140L;
   private static final long В7 = 160L;
   private static final int е_ = ColorMath.packRgb(45, 125, 255);
   private static final int еШ = ColorMath.packRgb(180, 70, 255);
   private static final int еы = ColorMath.packRgb(255, 55, 55);
   private final @com.google.gson.annotations.SerializedName("Вч")  FloatSetting targetRadiusSetting;
   @SerializedName("ВЭ")
   private final FloatSetting placementRangeSetting;
   @SerializedName("В必")
   private final FloatSetting breakRangeSetting;
   @SerializedName("Вщ")
   private final FloatSetting minEnemyDamageSetting;
   @SerializedName("ВГ")
   private final FloatSetting maxSelfDamageSetting;
   @SerializedName("Вл")
   private final FloatSetting actionDelaySetting;
   private final @com.google.gson.annotations.SerializedName("Вэ")  FloatSetting predictionTicksSetting;
   private final @com.google.gson.annotations.SerializedName("сщ")  BooleanSetting placeObsidianSetting;
   private final @com.google.gson.annotations.SerializedName("сГ")  BooleanSetting matrixBypassSetting;
   private final @com.google.gson.annotations.SerializedName("сл")  BooleanSetting legitSetting;
   private final @com.google.gson.annotations.SerializedName("сэ")  BooleanSetting antiSuicideSetting;
   private final @com.google.gson.annotations.SerializedName("сю")  BooleanSetting autoDigSetting;
   private final @com.google.gson.annotations.SerializedName("сЩ")  BooleanSetting renderPositionSetting;
   private final @com.google.gson.annotations.SerializedName("су")  BooleanSetting logsSetting;
   private final @com.google.gson.annotations.SerializedName("с2")  BooleanSetting saveSelfSetting;
   private final @com.google.gson.annotations.SerializedName("Л")  Stopwatch actionDelayTimer;
   @SerializedName("В")
   private final SharedRotationCoordinator rotationCoordinator;
   @SerializedName("Я")
   private final HotbarSelectionSession hotbarSession;
   private @com.google.gson.annotations.SerializedName("Ь")  ClientLevel observedLevel;
   private @com.google.gson.annotations.SerializedName("ей")  int lastObservedPlayerTick;
   @SerializedName("п")
   private BlockPos activeDigPosition;
   private @com.google.gson.annotations.SerializedName("Аф")  float maxDigProgress;
   private @com.google.gson.annotations.SerializedName("еЖ")  int lastDigProgressTick;
   private final @com.google.gson.annotations.SerializedName("с诶")  Map<BlockPos, Integer> blockedPositionsUntilTick;
   @SerializedName("с1")
   private final Map<BlockPos, Integer> placementCooldownUntilTick;
   private final @com.google.gson.annotations.SerializedName("сЬ")  Map<BlockPos, Integer> pendingCrystalSpawnTicks;
   private final @com.google.gson.annotations.SerializedName("с衣")  Map<BlockPos, Integer> pendingObsidianPlacementTicks;
   private @com.google.gson.annotations.SerializedName("Д")  BlockPos lastConfirmedObsidianPosition;
   private @com.google.gson.annotations.SerializedName("ем")  int lastConfirmedObsidianTick;
   private @com.google.gson.annotations.SerializedName("Ъ")  BlockPos plannedPlacementPosition;
   @SerializedName("В")
   private CrystalAura.PendingAction pendingAction;
   private @com.google.gson.annotations.SerializedName("еК")  int actionReadyTick;
   @SerializedName("с1")
   private final List<CrystalPlacementMarker> placementMarkers;
   private @com.google.gson.annotations.SerializedName("т")  BlockPos digRenderPosition;
   private @com.google.gson.annotations.SerializedName("Аи")  float digRenderProgress;
   private @com.google.gson.annotations.SerializedName("ВР")  long digRenderStartMillis;
   @SerializedName("ВЭ")
   private final EventListener<PlayerTickEvent> playerTickListener;
   @SerializedName("В必")
   private final EventListener<PlayerMovementSyncEvent> movementSyncListener;
   @SerializedName("Вщ")
   private final EventListener<PlayerMovementSyncEvent.Post> postMovementSyncListener;
   @SerializedName("ВГ")
   private final EventListener<WorldEffectsEvent> worldEffectsListener;
   @SerializedName("Вл")
   private final EventListener<BlockDestroyDelayEvent> blockDestroyDelayListener;
   private static final String скТ = "CrystalAura";
   private static final String скО = "Автоматически ставит обсидиан/кристал в выгодных условиях";
   private static final String скФ = "Радиус цели";
   private static final float скЙ = 8.0F;
   private static final float скк = 4.0F;
   private static final float скг = 12.0F;
   private static final float ск0 = 0.5F;
   private static final String скх = "Дальность установки";
   private static final float скУ = 4.5F;
   private static final float скЗ = 3.0F;
   private static final float скЕ = 6.0F;
   private static final float скП = 0.1F;
   private static final String ск西 = "Дальность взрыва";
   private static final float скд = 3.0F;
   private static final float скБ = 6.0F;
   private static final float скС = 0.1F;
   private static final String скз = "Мин. урон врагу";
   private static final float скц = 6.0F;
   private static final float скя = 20.0F;
   private static final float сгс = 0.5F;
   private static final String сгВ = "Макс. урон себе";
   private static final float сгЯ = 8.0F;
   private static final float сгХ = 20.0F;
   private static final float сге = 0.5F;
   private static final String сгА = "Задержка";
   private static final float сг4 = 500.0F;
   private static final float сгп = 10.0F;
   private static final String сгД = "Предикт тиков";
   private static final float сгЪ = 4.0F;
   private static final String сгт = "Ставить обсидиан";
   private static final String сга = "Обход Matrix";
   private static final String сгЮ = "Легитный";
   private static final String сгЧ = "Анти-суицид";
   private static final String сгЛ = "Авто подкоп";
   private static final String сг诶 = "Рендер позиции";
   private static final String сг1 = "Логи";
   private static final String сгЬ = "Сейвить себя";
   private static final String сг衣 = "action timeout";
   private static final float сгш = 8.0F;
   private static final float сгИ = Float.MAX_VALUE;
   private static final float сгь = 300.0F;
   private static final float сгж = 0.4F;
   private static final float сг弟 = 0.6F;
   private static final float сгб = 0.39215687F;
   private static final long сгЫ = 140L;
   private static final long сгl = 140L;
   private static final float сг8 = 160.0F;
   private static final float сг5 = 0.55F;
   private static final float сгв = 0.45F;
   private static final float сгъ = 0.45F;
   private static final float сгМ = 0.4F;
   private static final float сгН = 0.5F;
   private static final double сгн = 0.5;
   private static final double сгЦ = 0.5;
   private static final double сгч = 0.5;
   private static final double сгЭ = 0.5;
   private static final float сг必 = 0.05F;
   private static final double сгщ = 0.1;
   private static final double сгГ = 0.1;
   private static final double сгл = 0.1;
   private static final double сгэ = Double.MAX_VALUE;
   private static final double сгю = 1.5;
   private static final String сгЩ = "crystal is gone or out of range";
   private static final String сгу = "item missing";
   private static final float сг2 = 8.0F;
   private static final String сгф = "unsafe crystal or out of range";
   private static final String сги = "mining no longer allowed";
   private static final String сг9 = "placement invalidated";
   private static final float сго = 1.0E-5F;
   private static final double сгO = 2.0;

   private List<Player> Т(float var1) {
      ArrayList var2 = new ArrayList();
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         FriendRegistry var3 = RoseClient.getInstance().getServices().getFriends();

         for (Player var5 : MINECRAFT_CLIENT.level.players()) {
            if (var5 != MINECRAFT_CLIENT.player
               && var5.isAlive()
               && !var5.isSpectator()
               && !(CrystalPlacementPlanner.method_4(MINECRAFT_CLIENT.player.getEyePosition(), var5.getBoundingBox()) > var1 * var1)
               && var3.О(var5.getGameProfile().name())) {
               var2.add(var5);
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public boolean У3() {
      return this.isEnabled() && this.activeDigPosition != null;
   }

   private void Г(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!this.renderPositionSetting.getValue()) {
            this.placementMarkers.clear();
            this.digRenderPosition = null;
         } else if (!this.placementMarkers.isEmpty() || this.digRenderPosition != null) {
            VertexConsumer var2 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
            VertexConsumer var3 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.QUADS);
            Vec3 var4 = var1.getCameraPosition();
            long var5 = System.currentTimeMillis();
            Iterator var7 = this.placementMarkers.iterator();

            while (var7.hasNext()) {
               CrystalPlacementMarker var8 = (CrystalPlacementMarker)var7.next();
               float var9 = (float)(var5 - var8.createdAt()) / сгь;
               if (var9 >= 1.0F) {
                  var7.remove();
               } else {
                  float var10 = сгж + сг弟 * var9;
                  float var11 = (1.0F - var9) * сгб;
                  int var12 = var8.crystal() ? еШ : е_;
                  int var13 = ColorMath.withAlpha(var12, var11);
                  int var14 = ColorMath.withAlpha(var12, var11);
                  AABB var15 = this.ю(var8.pos(), var10);
                  WorldBoxGeometry.appendBox(var1.getPoses(), var3, var4, var15.minX, var15.minY, var15.minZ, var15.maxX, var15.maxY, var15.maxZ, var14, true);
               }
            }

            this.ю(var1, var2, var4, var5);
         }
      }
   }

   @Override
   public void onDisable() {
      this.Нп();
      super.onDisable();
   }

   private int к(Item var1) {
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

   private int ю(CrystalAura.PendingAction var1) {
      return switch (var1.kind()) {
         case OBSIDIAN -> this.к(Items.OBSIDIAN);
         case CRYSTAL -> this.к(Items.END_CRYSTAL);
         case ATTACK -> this.legitSetting.getValue() ? this.к(Items.END_CRYSTAL) : MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
         case DIG -> {
            int var2 = InventoryActions.б(MINECRAFT_CLIENT.level.getBlockState(var1.pos()));
            yield var2 >= 0 ? var2 : MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
         }
      };
   }

   private boolean х(BlockPos var1) {
      if (!this.pendingCrystalSpawnTicks.containsKey(var1) && CrystalPlacementPlanner.西(var1)) {
         BlockPlacementHit var2 = BlockPlacementUtils.method_5(var1, MINECRAFT_CLIENT.player.getEyePosition(), this.placementRangeSetting.getValue().floatValue());
         return this.ю(CrystalAura.ActionKind.CRYSTAL, var1, var2, null);
      } else {
         return false;
      }
   }

   private void ю(WorldEffectsEvent var1, VertexConsumer var2, Vec3 var3, long var4) {
      if (this.digRenderPosition != null) {
         long var6 = var4 - this.digRenderStartMillis;
         float var8;
         if (var6 <= сгЫ) {
            var8 = 1.0F;
         } else {
            float var9 = (float)(var6 - сгl) / сг8;
            if (var9 >= 1.0F) {
               this.digRenderPosition = null;
               return;
            }

            var8 = 1.0F - var9;
         }

         float var15 = сг5 + сгв * this.digRenderProgress;
         float var10 = (сгъ + сгМ * this.digRenderProgress) * var8 * сгН;
         int var11 = ColorMath.withAlpha(еы, var10);
         int var12 = ColorMath.withAlpha(еы, var10);
         AABB var13 = this.ю(this.digRenderPosition, var15);
         VertexConsumer var14 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.QUADS);
         WorldBoxGeometry.appendBox(var1.getPoses(), var14, var3, var13.minX, var13.minY, var13.minZ, var13.maxX, var13.maxY, var13.maxZ, var12, true);
      }
   }

   private void Нф() {
      this.hotbarSession.еЬ();
   }

   private boolean УИ() {
      return this.matrixBypassSetting.getValue();
   }

   private void У(PlayerMovementSyncEvent.Post var1) {
      if (!RoseClient.getInstance().getServices().getModules().getAimbot().щж()) {
         if (this.pendingAction != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && !MINECRAFT_CLIENT.player.isUsingItem()) {
            AutoWebTrap var2 = RoseClient.getInstance().getServices().getModules().getAutoWebTrap();
            if (var2 != null && var2.method_1Ю()) {
               this.НЩ();
            } else if (this.actionReadyTick == MINECRAFT_CLIENT.player.tickCount && !var1.isCancelled() && this.rotationCoordinator.isActiveOwner(this)) {
               this.actionReadyTick = -1;
               CrystalAura.PendingAction var3 = this.pendingAction;
               int var4 = this.ю(var3);
               if (var4 < 0) {
                  this.Д(сгу);
               } else if (this.hotbarSession.Я(var4, this.УИ())) {
                  if (var3.kind() == CrystalAura.ActionKind.ATTACK) {
                     Player var17 = var2 == null ? null : var2.д_d012b85c();
                     List var18 = var17 != null ? List.of(var17) : this.и(this.targetRadiusSetting.getValue());
                     EndCrystal var19 = var3.crystal();
                     if (CrystalPlacementPlanner.method_4(var19, MINECRAFT_CLIENT.player.getEyePosition(), this.breakRangeSetting.getValue().floatValue()) != null
                        && CrystalPlacementPlanner.method_4(var19, var18, this.maxSelfDamageSetting.getValue(), this.minEnemyDamageSetting.getValue(), this.юЕ(), this.Т(this.targetRadiusSetting.getValue() + сг2))) {
                        this.pendingAction = null;

                        try {
                           MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, var19);
                           MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
                           this.actionDelayTimer.ВД();
                        } finally {
                           if (!this.legitSetting.getValue()) {
                              this.hotbarSession.еЬ();
                           }
                        }
                     } else {
                        this.Д(сгф);
                     }
                  } else {
                     BlockHitResult var5 = BlockPlacementUtils.method_5(var3.hit(), this.placementRangeSetting.getValue().floatValue(), var1);
                     if (var5 != null) {
                        if (var3.kind() == CrystalAura.ActionKind.DIG) {
                           if (this.autoDigSetting.getValue() && CrystalPlacementPlanner.п(MINECRAFT_CLIENT.level.getBlockState(var3.pos()))) {
                              this.pendingAction = null;
                              this.ю(var3.pos(), var5);
                           } else {
                              this.Д(сги);
                           }
                        } else {
                           boolean var6 = var3.kind() == CrystalAura.ActionKind.CRYSTAL
                              ? !this.pendingCrystalSpawnTicks.containsKey(var3.pos()) && CrystalPlacementPlanner.西(var3.pos())
                              : BlockPlacementUtils.method_5(var3.pos(), var5, MINECRAFT_CLIENT.player.getMainHandItem()) && !CrystalPlacementPlanner.method_7(var3.pos());
                           if (!var6) {
                              this.Д(сг9);
                           } else {
                              this.pendingAction = null;

                              try {
                                 InteractionResult var7 = MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var5);
                                 if (this.logsSetting.getValue()) {
                                    this.н(
                                       "place "
                                          + var3.kind()
                                          + " target="
                                          + var3.pos().toShortString()
                                          + " support="
                                          + var5.getBlockPos().toShortString()
                                          + " face="
                                          + var5.getDirection()
                                          + " cursor="
                                          + var5.getLocation().subtract(Vec3.atLowerCornerOf(var5.getBlockPos()))
                                          + " yaw="
                                          + var1.getYaw()
                                          + " pitch="
                                          + var1.getPitch()
                                          + " result="
                                          + var7
                                    );
                                 }

                                 boolean var8 = var7 instanceof Success var9
                                    && var9.wasItemInteraction()
                                    && (var3.kind() == CrystalAura.ActionKind.CRYSTAL || MINECRAFT_CLIENT.level.getBlockState(var3.pos()).is(Blocks.OBSIDIAN));
                                 if (var8) {
                                    MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
                                    this.actionDelayTimer.ВД();
                                    if (var3.kind() == CrystalAura.ActionKind.CRYSTAL) {
                                       this.pendingCrystalSpawnTicks.putIfAbsent(var3.pos(), MINECRAFT_CLIENT.player.tickCount);
                                    } else {
                                       this.pendingObsidianPlacementTicks.putIfAbsent(var3.pos(), MINECRAFT_CLIENT.player.tickCount);
                                    }
                                 } else {
                                    this.placementCooldownUntilTick.put(var3.pos(), MINECRAFT_CLIENT.player.tickCount + 12);
                                    if (this.logsSetting.getValue()) {
                                       this.н("placement rejected locally: " + var3.pos().toShortString());
                                    }
                                 }
                              } finally {
                                 if (var3.kind() != CrystalAura.ActionKind.CRYSTAL || !this.legitSetting.getValue()) {
                                    this.hotbarSession.еЬ();
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void ю(BlockDestroyDelayEvent var1) {
      if (!this.УИ() && this.activeDigPosition != null && var1.getDestroyDelay() > 0) {
         var1.setClearDelayRequested(true);
      }
   }

   private List<Player> и(float var1) {
      ArrayList<Player> var2 = new ArrayList<>();
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         AntiBot var3 = RoseClient.getInstance().getServices().getModules().getAntiBot();
         FriendRegistry var4 = RoseClient.getInstance().getServices().getFriends();

         for (Player var6 : MINECRAFT_CLIENT.level.players()) {
            if (var6 != MINECRAFT_CLIENT.player
               && var6.isAlive()
               && !var6.isSpectator()
               && !(CrystalPlacementPlanner.method_4(MINECRAFT_CLIENT.player.getEyePosition(), var6.getBoundingBox()) > var1 * var1)
               && !var4.О(var6.getGameProfile().name())
               && !var3.detectedBots.contains(var6)) {
               var2.add(var6);
            }
         }

         var2.sort(Comparator.comparingDouble(var0 -> CrystalPlacementPlanner.method_4(MINECRAFT_CLIENT.player.getEyePosition(), var0.getBoundingBox())));
         return var2;
      } else {
         return var2;
      }
   }

   private boolean ю(EndCrystal var1) {
      if (var1 != null && var1.isAlive()) {
         this.Нъ();
         return this.ю(CrystalAura.ActionKind.ATTACK, var1.blockPosition(), null, var1);
      } else {
         return false;
      }
   }

   private void Нг() {
      this.rotationCoordinator.releaseOwner(this);
   }

   private void ю(BlockPos var1, boolean var2) {
      if (this.renderPositionSetting.getValue()) {
         this.placementMarkers.add(new CrystalPlacementMarker(var1, var2, System.currentTimeMillis()));
      }
   }

   private void р(PlayerMovementSyncEvent var1) {
      this.actionReadyTick = -1;
      if (!RoseClient.getInstance().getServices().getModules().getAimbot().щж()) {
         if (this.pendingAction != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !MINECRAFT_CLIENT.player.isUsingItem()) {
            AutoWebTrap var2 = RoseClient.getInstance().getServices().getModules().getAutoWebTrap();
            if (var2 != null && var2.method_1Ю()) {
               this.НЩ();
            } else if (!var1.isCancelled() && this.rotationCoordinator.isActiveOwner(this)) {
               Vec3 var3 = var1.getPosition().add(0.0, MINECRAFT_CLIENT.player.getEyeHeight(), 0.0);
               CrystalAura.PendingAction var4 = this.pendingAction;
               Vec3 var5;
               if (var4.kind() == CrystalAura.ActionKind.ATTACK) {
                  var5 = CrystalPlacementPlanner.method_4(var4.crystal(), var3, this.breakRangeSetting.getValue().floatValue());
               } else {
                  BlockPlacementHit var6 = switch (var4.kind()) {
                     case OBSIDIAN -> BlockPlacementUtils.method_5(var4.pos(), var3, this.placementRangeSetting.getValue().floatValue(), false);
                     case CRYSTAL -> BlockPlacementUtils.method_5(var4.pos(), var3, this.placementRangeSetting.getValue().floatValue());
                     default -> BlockPlacementUtils.Г(var4.pos(), var3, this.placementRangeSetting.getValue().floatValue());
                  };
                  var5 = var6 == null ? null : var6.hitVec();
                  this.pendingAction = new CrystalAura.PendingAction(var4.kind(), var4.pos(), var6, var4.crystal(), var4.tick());
               }

               if (var5 == null) {
                  this.pendingAction = null;
                  this.Нъ();
                  this.hotbarSession.еЬ();
                  this.Нг();
               } else {
                  this.actionReadyTick = MINECRAFT_CLIENT.player.tickCount;
               }
            }
         }
      }
   }

   private void Н7() {
      int var1 = MINECRAFT_CLIENT.player.tickCount;
      this.blockedPositionsUntilTick.values().removeIf(var1x -> var1 >= var1x);
      this.placementCooldownUntilTick.values().removeIf(var1x -> var1 >= var1x);
   }

   private void Нъ() {
      if (this.activeDigPosition != null) {
         if (MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.gameMode.isDestroying()) {
            MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
         }

         if (this.pendingAction != null && this.pendingAction.kind() == CrystalAura.ActionKind.DIG) {
            this.pendingAction = null;
         }

         this.activeDigPosition = null;
         this.hotbarSession.еЬ();
      }
   }

   private void НЩ() {
      this.actionReadyTick = -1;
      this.pendingAction = null;
      this.Нъ();
      this.hotbarSession.еЬ();
      this.Нг();
   }

   private void Нн() {
      int var1 = MINECRAFT_CLIENT.player.tickCount;
      Iterator var2 = this.pendingObsidianPlacementTicks.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         BlockPos var4 = (BlockPos)var3.getKey();
         int var5 = var1 - (Integer)var3.getValue();
         if (!BlockPlacementUtils.method_3(var4) && MINECRAFT_CLIENT.level.getBlockState(var4).is(Blocks.OBSIDIAN)) {
            this.lastConfirmedObsidianPosition = var4;
            this.lastConfirmedObsidianTick = var1;
            this.ю(var4, false);
            var2.remove();
         } else if (var5 > BlockPlacementUtils.О西() || var5 >= 2 && !BlockPlacementUtils.method_3(var4)) {
            this.placementCooldownUntilTick.put(var4, var1 + 4);
            if (this.logsSetting.getValue()) {
               this.н("obsidian not confirmed: " + var4.toShortString());
            }

            var2.remove();
         }
      }

      Iterator var6 = this.pendingCrystalSpawnTicks.entrySet().iterator();

      while (var6.hasNext()) {
         Entry var7 = (Entry)var6.next();
         BlockPos var8 = (BlockPos)var7.getKey();
         if (this.ю(var8.above()) != null) {
            this.ю(var8.above(), true);
            var6.remove();
         } else if (var1 - (Integer)var7.getValue() > BlockPlacementUtils.О西()) {
            this.placementCooldownUntilTick.put(var8, var1 + 4);
            if (this.logsSetting.getValue()) {
               this.н("crystal spawn not confirmed: " + var8.above().toShortString());
            }

            var6.remove();
         }
      }
   }

   private boolean Л(Vec3 var1) {
      if (this.activeDigPosition == null) {
         return false;
      }

      BlockPos var2 = this.activeDigPosition;
      BlockState var3 = MINECRAFT_CLIENT.level.getBlockState(var2);
      if (!var3.isAir() && !var3.canBeReplaced()) {
         if (this.autoDigSetting.getValue()
            && CrystalPlacementPlanner.п(var3)
            && !(CrystalPlacementPlanner.method_4(var1, var2) > Math.pow(BlockPlacementUtils.O(this.placementRangeSetting.getValue().floatValue()), сгO))
            && MINECRAFT_CLIENT.player.tickCount - this.lastDigProgressTick <= Math.max(10, this.matrixBypassSetting.getValue() ? 16 : 10)) {
            this.й(var2);
            return this.activeDigPosition != null;
         } else {
            this.blockedPositionsUntilTick.put(var2, MINECRAFT_CLIENT.player.tickCount + 40);
            this.Нъ();
            return false;
         }
      } else {
         this.Нъ();
         return false;
      }
   }

   private EndCrystal ю(BlockPos var1) {
      AABB var2 = new AABB(var1.getX(), var1.getY(), var1.getZ(), var1.getX() + 1, var1.getY() + 2, var1.getZ() + 1);
      List var3 = MINECRAFT_CLIENT.level.getEntitiesOfClass(EndCrystal.class, var2, var1x -> var1x.isAlive() && var1x.blockPosition().equals(var1));
      return var3.isEmpty() ? null : (EndCrystal)var3.getFirst();
   }

   private void эЭ(PlayerTickEvent var1) {
      if (this.observedLevel != MINECRAFT_CLIENT.level || MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.tickCount < this.lastObservedPlayerTick) {
         this.Нп();
         this.observedLevel = MINECRAFT_CLIENT.level;
      }

      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && !MINECRAFT_CLIENT.player.isSpectator()) {
         this.lastObservedPlayerTick = MINECRAFT_CLIENT.player.tickCount;
         if (!RoseClient.getInstance().getServices().getModules().getAimbot().щж()) {
            AutoWebTrap var2 = RoseClient.getInstance().getServices().getModules().getAutoWebTrap();
            Player var3 = var2 != null && var2.isEnabled() ? var2.д_d012b85c() : null;
            if (var2 != null && var2.method_1Ю()) {
               this.НЩ();
            } else {
               this.Н7();
               CrystalPlacementPlanner.ЯЫ = this.logsSetting.getValue();
               List var4 = var3 != null ? List.of(var3) : this.и(this.targetRadiusSetting.getValue());
               if (!var4.isEmpty() && !MINECRAFT_CLIENT.player.isUsingItem()) {
                  this.Нн();
                  if (this.pendingAction != null) {
                     if (MINECRAFT_CLIENT.player.tickCount - this.pendingAction.tick() <= BlockPlacementUtils.О西()) {
                        this.НД();
                        return;
                     }

                     this.Д(сг衣);
                  }

                  Vec3 var5 = MINECRAFT_CLIENT.player.getEyePosition();
                  if (this.actionDelaySetting.getValue() > 0.0F && !this.actionDelayTimer.М(this.actionDelaySetting.getValue().longValue())) {
                     this.Л(var5);
                  } else {
                     float var6 = this.юЕ();
                     List var7 = this.Т(this.targetRadiusSetting.getValue() + сгш);
                     CrystalAttackCandidate var8 = CrystalPlacementPlanner.method_4(var4, var5, this.breakRangeSetting.getValue(), this.maxSelfDamageSetting.getValue(), this.minEnemyDamageSetting.getValue(), var6, var7);
                     if (var8 == null || !this.ю(var8.crystal())) {
                        if (!this.Т(var4)) {
                           boolean var9 = this.к(Items.END_CRYSTAL) >= 0;
                           boolean var10 = this.placeObsidianSetting.getValue() && this.к(Items.OBSIDIAN) >= 0;
                           List var11 = this.ю(var4);
                           float var12 = (float)BlockPlacementUtils.O(this.placementRangeSetting.getValue().floatValue());
                           float var13 = this.breakRangeSetting.getValue();
                           BlockPos var14 = this.plannedPlacementPosition != null ? this.plannedPlacementPosition : (this.lastConfirmedObsidianPosition != null && MINECRAFT_CLIENT.player.tickCount - this.lastConfirmedObsidianTick <= 80 ? this.lastConfirmedObsidianPosition : null);
                           ArrayList var15 = new ArrayList<>(this.blockedPositionsUntilTick.keySet());
                           var15.addAll(this.placementCooldownUntilTick.keySet());
                           var15.addAll(this.pendingCrystalSpawnTicks.keySet());
                           CrystalPlacementCandidate var16 = var9
                              ? CrystalPlacementPlanner.method_4(
                                 var11, var5, var12, var13, this.maxSelfDamageSetting.getValue(), this.minEnemyDamageSetting.getValue(), var10, false, var6, var7, null, 0.0F, var14, var15
                              )
                              : null;
                           if (var16 != null) {
                              this.Нъ();
                              this.plannedPlacementPosition = var16.pos();
                              if (var16.needsObsidian()) {
                                 this.ю(var16.pos(), Items.OBSIDIAN);
                              } else {
                                 this.х(var16.pos());
                              }
                           } else if (!this.pendingCrystalSpawnTicks.isEmpty()) {
                              this.Нъ();
                              this.hotbarSession.еЬ();
                              this.Нг();
                           } else {
                              if (!this.autoDigSetting.getValue() || !var9) {
                                 this.Нъ();
                              }

                              if (!this.Л(var5)) {
                                 var16 = var9 && this.autoDigSetting.getValue()
                                    ? CrystalPlacementPlanner.method_4(
                                       var11, var5, var12, var13, this.maxSelfDamageSetting.getValue(), this.minEnemyDamageSetting.getValue(), var10, true, var6, var7, null, 0.0F, var14, var15
                                    )
                                    : null;
                                 if (this.logsSetting.getValue()) {
                                    this.н(CrystalPlacementPlanner.ВЛ);
                                 }

                                 if (var16 == null) {
                                    this.hotbarSession.еЬ();
                                    this.Нг();
                                 } else {
                                    this.plannedPlacementPosition = var16.pos();
                                    if (var16.digPos() != null) {
                                       this.й(var16.digPos());
                                    } else if (var16.needsObsidian()) {
                                       this.ю(var16.pos(), Items.OBSIDIAN);
                                    } else {
                                       this.х(var16.pos());
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               } else {
                  this.НЩ();
                  this.plannedPlacementPosition = null;
               }
            }
         }
      } else {
         this.Нп();
      }
   }

   private void Д(String var1) {
      if (this.pendingAction != null) {
         if (this.logsSetting.getValue()) {
            this.н(var1 + " at " + this.pendingAction.pos().toShortString());
         }

         if (this.pendingAction.kind() == CrystalAura.ActionKind.DIG) {
            this.blockedPositionsUntilTick.put(this.pendingAction.pos(), MINECRAFT_CLIENT.player.tickCount + 40);
            this.Нъ();
         } else if (this.pendingAction.kind() != CrystalAura.ActionKind.ATTACK) {
            this.placementCooldownUntilTick.put(this.pendingAction.pos(), MINECRAFT_CLIENT.player.tickCount + 12);
         }

         this.pendingAction = null;
         this.hotbarSession.еЬ();
         this.Нг();
      }
   }

   private void НД() {
      if (this.pendingAction != null) {
         Vec3 var1 = this.pendingAction.kind() == CrystalAura.ActionKind.ATTACK
            ? CrystalPlacementPlanner.method_4(this.pendingAction.crystal(), MINECRAFT_CLIENT.player.getEyePosition(), this.breakRangeSetting.getValue().floatValue())
            : this.pendingAction.hit().hitVec();
         if (var1 == null) {
            this.Д(сгЩ);
         } else if (!this.rotationCoordinator.requestRotation(this, var1) && !this.rotationCoordinator.isActiveOwner(this)) {
            this.hotbarSession.еЬ();
         } else {
            if (this.УИ()) {
               int var2 = this.ю(this.pendingAction);
               if (var2 >= 0) {
                  this.hotbarSession.Я(var2, true);
               }
            }
         }
      }
   }

   private void ю(BlockPos var1, BlockHitResult var2) {
      boolean var3 = MINECRAFT_CLIENT.gameMode.continueDestroyBlock(var1, var2.getDirection());
      if (var3) {
         MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
      }

      float var4 = Mth.clamp(((MultiPlayerGameModeAccessor)MINECRAFT_CLIENT.gameMode).getCurrentBreakingProgress(), 0.0F, 1.0F);
      if (var4 > this.maxDigProgress + сго) {
         this.maxDigProgress = var4;
         this.lastDigProgressTick = MINECRAFT_CLIENT.player.tickCount;
      }

      if (this.renderPositionSetting.getValue()) {
         this.digRenderPosition = var1;
         this.digRenderProgress = var4;
         this.digRenderStartMillis = System.currentTimeMillis();
      }

      if (this.logsSetting.getValue()) {
         this.н("dig " + var1.toShortString() + " progress=" + var4);
      }
   }

   private boolean ю(BlockPos var1, Item var2) {
      BlockPlacementHit var3 = BlockPlacementUtils.method_5(var1, MINECRAFT_CLIENT.player.getEyePosition(), this.placementRangeSetting.getValue().floatValue(), false);
      return this.ю(CrystalAura.ActionKind.OBSIDIAN, var1, var3, null);
   }

   public CrystalAura() {
      super(скТ, скО, ModuleCategory.COMBAT);
      this.targetRadiusSetting = new FloatSetting(скФ, скЙ, скк, скг).withStep(ск0);
      this.placementRangeSetting = new FloatSetting(скх, скУ, скЗ, скЕ).withStep(скП);
      this.breakRangeSetting = new FloatSetting(ск西, скд, 1.0F, скБ).withStep(скС);
      this.minEnemyDamageSetting = new FloatSetting(скз, скц, 1.0F, скя).withStep(сгс);
      this.maxSelfDamageSetting = new FloatSetting(сгВ, сгЯ, 0.0F, сгХ).withStep(сге);
      this.actionDelaySetting = new FloatSetting(сгА, 0.0F, 0.0F, сг4).withStep(сгп).withDisplayFormat(NumericDisplayFormat.INTEGER);
      this.predictionTicksSetting = new FloatSetting(сгД, 2.0F, 0.0F, сгЪ).withStep(1.0F).withDisplayFormat(NumericDisplayFormat.INTEGER);
      this.placeObsidianSetting = new BooleanSetting(сгт, true);
      this.matrixBypassSetting = new BooleanSetting(сга, false);
      this.legitSetting = new BooleanSetting(сгЮ, false);
      this.antiSuicideSetting = new BooleanSetting(сгЧ, true);
      this.autoDigSetting = new BooleanSetting(сгЛ, true);
      this.renderPositionSetting = new BooleanSetting(сг诶, true);
      this.logsSetting = new BooleanSetting(сг1, false);
      this.saveSelfSetting = new BooleanSetting(сгЬ, false);
      this.actionDelayTimer = new Stopwatch();
      this.rotationCoordinator = SharedRotationCoordinator.getInstance();
      this.hotbarSession = new HotbarSelectionSession();
      this.lastObservedPlayerTick = -1;
      this.blockedPositionsUntilTick = new HashMap<>();
      this.placementCooldownUntilTick = new HashMap<>();
      this.pendingCrystalSpawnTicks = new HashMap<>();
      this.pendingObsidianPlacementTicks = new HashMap<>();
      this.actionReadyTick = -1;
      this.placementMarkers = new ArrayList<>();
      this.playerTickListener = this::эЭ;
      this.movementSyncListener = new EventListener<PlayerMovementSyncEvent>() {
         public void onEvent(PlayerMovementSyncEvent var1) {
            CrystalAura.this.р(var1);
         }

         @Override
         public int getPriority() {
            return -90;
         }
      };
      this.postMovementSyncListener = this::У;
      this.worldEffectsListener = this::Г;
      this.blockDestroyDelayListener = this::ю;
      this.addSettings(
         this.targetRadiusSetting,
         this.placementRangeSetting,
         this.breakRangeSetting,
         this.minEnemyDamageSetting,
         this.maxSelfDamageSetting,
         this.actionDelaySetting,
         this.predictionTicksSetting,
         this.placeObsidianSetting,
         this.legitSetting,
         this.matrixBypassSetting,
         this.antiSuicideSetting,
         this.autoDigSetting,
         this.renderPositionSetting,
         this.logsSetting,
         this.saveSelfSetting
      );
   }

   private boolean ю(CrystalAura.ActionKind var1, BlockPos var2, BlockPlacementHit var3, EndCrystal var4) {
      if (var1 != CrystalAura.ActionKind.ATTACK && var3 == null) {
         this.placementCooldownUntilTick.put(var2.immutable(), MINECRAFT_CLIENT.player.tickCount + 6);
         if (this.logsSetting.getValue()) {
            this.н("no reachable face at " + var2.toShortString());
         }

         return false;
      } else {
         this.pendingAction = new CrystalAura.PendingAction(var1, var2.immutable(), var3, var4, MINECRAFT_CLIENT.player.tickCount);
         this.НД();
         return true;
      }
   }

   private void й(BlockPos var1) {
      if (!this.blockedPositionsUntilTick.containsKey(var1) && CrystalPlacementPlanner.п(MINECRAFT_CLIENT.level.getBlockState(var1))) {
         BlockPlacementHit var2 = BlockPlacementUtils.Г(var1, MINECRAFT_CLIENT.player.getEyePosition(), this.placementRangeSetting.getValue().floatValue());
         if (var2 == null) {
            this.blockedPositionsUntilTick.put(var1.immutable(), MINECRAFT_CLIENT.player.tickCount + 10);
            this.Нъ();
         } else {
            if (!var1.equals(this.activeDigPosition)) {
               this.Нъ();
               this.activeDigPosition = var1.immutable();
               this.lastDigProgressTick = MINECRAFT_CLIENT.player.tickCount;
               this.maxDigProgress = 0.0F;
            }

            this.ю(CrystalAura.ActionKind.DIG, var1, var2, null);
         }
      } else {
         this.Нъ();
      }
   }

   private void н(String var1) {
      System.out.println("[CrystalAura] " + var1);
   }

   private float юЕ() {
      return this.antiSuicideSetting.getValue() ? Math.max(0.0F, MINECRAFT_CLIENT.player.getHealth() + MINECRAFT_CLIENT.player.getAbsorptionAmount() - 1.0F) : сгИ;
   }

   private boolean У(AABB var1) {
      int var2 = Mth.floor(var1.minX);
      int var3 = Mth.floor(var1.minY);
      int var4 = Mth.floor(var1.minZ);
      int var5 = Mth.floor(var1.maxX);
      int var6 = Mth.floor(var1.maxY);
      int var7 = Mth.floor(var1.maxZ);

      for (BlockPos var9 : BlockPos.betweenClosed(var2, var3, var4, var5, var6, var7)) {
         if (MINECRAFT_CLIENT.level.getBlockState(var9).is(Blocks.COBWEB)) {
            return true;
         }
      }

      return false;
   }

   private AABB ю(BlockPos var1, float var2) {
      double var3 = var1.getX() + сгн;
      double var5 = var1.getY() + сгЦ;
      double var7 = var1.getZ() + сгч;
      double var9 = сгЭ * Mth.clamp(var2, сг必, 1.0F);
      return new AABB(var3 - var9, var5 - var9, var7 - var9, var3 + var9, var5 + var9, var7 + var9);
   }

   private boolean Т(List<Player> var1) {
      if (!this.saveSelfSetting.getValue()) {
         return false;
      }

      if (this.к(Items.OBSIDIAN) == -1) {
         return false;
      }

      Player var2 = null;
      double var3 = сгэ;

      for (Player var6 : var1) {
         double var7 = MINECRAFT_CLIENT.player.distanceToSqr(var6);
         if (var7 < var3) {
            var3 = var7;
            var2 = var6;
         }
      }

      if (var2 == null) {
         return false;
      }

      double var11 = var2.getX() - MINECRAFT_CLIENT.player.getX();
      double var12 = var2.getZ() - MINECRAFT_CLIENT.player.getZ();
      if (Math.sqrt(var11 * var11 + var12 * var12) > this.placementRangeSetting.getValue().floatValue() + сгю) {
         return false;
      }

      if (Math.abs(var2.getY() - MINECRAFT_CLIENT.player.getY()) > 1.0) {
         return false;
      }

      Direction var9 = Math.abs(var11) > Math.abs(var12) ? (var11 > 0.0 ? Direction.EAST : Direction.WEST) : (var12 > 0.0 ? Direction.SOUTH : Direction.NORTH);
      BlockPos var10 = MINECRAFT_CLIENT.player.blockPosition().relative(var9);
      if (!MINECRAFT_CLIENT.level.getBlockState(var10).canBeReplaced()) {
         return false;
      }

      if (CrystalPlacementPlanner.method_7(var10)) {
         return false;
      }

      this.Нф();
      return this.ю(var10, Items.OBSIDIAN);
   }

   private List<CrystalDamageTarget> ю(List<Player> var1) {
      int var2 = this.predictionTicksSetting.getValue().intValue();
      ArrayList<Player> var3 = new ArrayList<>(var1);
      var3.sort(Comparator.comparingDouble(var0 -> CrystalPlacementPlanner.method_4(MINECRAFT_CLIENT.player.getEyePosition(), var0.getBoundingBox())));
      ArrayList var4 = new ArrayList();
      int var5 = Math.min(var3.size(), 6);

      for (int var6 = 0; var6 < var5; var6++) {
         Player var7 = (Player)var3.get(var6);
         AABB var8 = var7.getBoundingBox();
         if (var2 > 0 && !this.У(var8)) {
            AABB var9 = PlayerMotionPredictor.е_bb456db4(var7, var2);
            if (var9 != null && var9.getXsize() >= сгщ && var9.getYsize() >= сгГ && var9.getZsize() >= сгл) {
               var8 = var9;
            }
         }

         var4.add(new CrystalDamageTarget(var7, var8));
      }

      return var4;
   }

   private void Нп() {
      this.actionReadyTick = -1;
      this.pendingAction = null;
      this.Нъ();
      this.hotbarSession.еЬ();
      this.Нг();
      this.pendingObsidianPlacementTicks.clear();
      this.pendingCrystalSpawnTicks.clear();
      this.lastConfirmedObsidianPosition = null;
      this.plannedPlacementPosition = null;
      this.blockedPositionsUntilTick.clear();
      this.placementCooldownUntilTick.clear();
      this.placementMarkers.clear();
      this.digRenderPosition = null;
   }

   private enum ActionKind {
      OBSIDIAN,
      CRYSTAL,
      ATTACK,
      DIG;
   }

   private record PendingAction(CrystalAura.ActionKind kind, BlockPos pos, BlockPlacementHit hit, EndCrystal crystal, int tick) {
   }
}
