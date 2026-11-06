package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.ec.RespawnAnchorPlacementPlanner;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.CrystalPlacementPlanner;
import sg.settings.BooleanSetting;
import sg.util.time.Stopwatch;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.BlockPlacementHit;
import sg.modules.impl.init.RoseXrayLines;
import sg.settings.FloatSetting;
import sg.modules.impl.init.FriendRegistry;
import sg.settings.NumericDisplayFormat;
import sg.rotation.SharedRotationCoordinator;

public class AutoAnchor extends ClientModule {
   private static final int АЭ = ColorMath.packRgb(255, 90, 40);
   private static final int А必 = 20;
   private final @com.google.gson.annotations.SerializedName("ЯЛ")  FloatSetting targetRadiusSetting;
   private final @com.google.gson.annotations.SerializedName("Я诶")  FloatSetting placementRangeSetting;
   private final @com.google.gson.annotations.SerializedName("Я1")  FloatSetting breakRangeSetting;
   private final @com.google.gson.annotations.SerializedName("ЯЬ")  FloatSetting minEnemyDamageSetting;
   private final @com.google.gson.annotations.SerializedName("Я衣")  FloatSetting maxSelfDamageSetting;
   private final @com.google.gson.annotations.SerializedName("Яш")  FloatSetting actionDelaySetting;
   private final @com.google.gson.annotations.SerializedName("сЕ")  BooleanSetting placeObsidianSetting;
   private final @com.google.gson.annotations.SerializedName("сП")  BooleanSetting antiSuicideSetting;
   private final @com.google.gson.annotations.SerializedName("с西")  BooleanSetting renderPositionSetting;
   private final @com.google.gson.annotations.SerializedName("сд")  BooleanSetting logsSetting;
   private final @com.google.gson.annotations.SerializedName("Ь")  Stopwatch actionDelayTimer;
   @SerializedName("Х")
   private final SharedRotationCoordinator rotationCoordinator;
   private @com.google.gson.annotations.SerializedName("а")  BlockPos activeAnchorPosition;
   private @com.google.gson.annotations.SerializedName("Ащ")  int anchorPlacedTick;
   private @com.google.gson.annotations.SerializedName("АГ")  int anchorActionPhase;
   private @com.google.gson.annotations.SerializedName("Ал")  int previousHotbarSlot;
   private final @com.google.gson.annotations.SerializedName("Вк")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("Вг")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private static final String Яы_ = "AutoAnchor";
   private static final String ЯыШ = "Взрывает якорь возрождения рядом с целью";
   private static final String Яыы = "Радиус цели";
   private static final float Яый = 8.0F;
   private static final float ЯыЖ = 4.0F;
   private static final float Яым = 12.0F;
   private static final float ЯыК = 0.5F;
   private static final String ЯыI = "Дальность установки";
   private static final float ЯыТ = 4.5F;
   private static final float ЯыО = 3.0F;
   private static final float ЯыФ = 6.0F;
   private static final float ЯыЙ = 0.1F;
   private static final String Яык = "Дальность взрыва";
   private static final float Яыг = 6.0F;
   private static final float Яы0 = 10.0F;
   private static final float Яых = 0.1F;
   private static final String ЯыУ = "Мин. урон врагу";
   private static final float ЯыЗ = 6.0F;
   private static final float ЯыЕ = 20.0F;
   private static final float ЯыП = 0.5F;
   private static final String Яы西 = "Макс. урон себе";
   private static final float Яыд = 8.0F;
   private static final float ЯыБ = 20.0F;
   private static final float ЯыС = 0.5F;
   private static final String Яыз = "Задержка";
   private static final float Яыц = 500.0F;
   private static final float Яыя = 10.0F;
   private static final String Яйс = "Ставить обсидиан";
   private static final String ЯйВ = "Анти-суицид";
   private static final String ЯйЯ = "Рендер позиции";
   private static final String ЯйХ = "Логи";
   private static final float Яйе = Float.MAX_VALUE;
   private static final float ЯйА = 8.0F;
   private static final double Яй4 = 0.5;
   private static final double Яйп = 0.5;
   private static final double ЯйД = 0.5;
   private static final double ЯйЪ = Double.MAX_VALUE;
   private static final double Яйт = 0.001;
   private static final double Яйа = 0.001;
   private static final double ЯйЮ = 0.001;
   private static final double ЯйЧ = 0.001;
   private static final double ЯйЛ = 0.001;
   private static final double Яй诶 = 0.001;
   private static final double Яй1 = 1.0E-7;
   private static final double ЯйЬ = 1.0E-4;

   private void д(String var1) {
      if (this.logsSetting.getValue()) {
         System.out.println("[AutoAnchor] " + var1);
      }
   }

   private BlockHitResult method__(BlockPos var1, double var2) {
      if (MINECRAFT_CLIENT.level == null) {
         return null;
      }

      BlockHitResult var4 = null;
      double var5 = ЯйЪ;
      Vec3 var7 = MINECRAFT_CLIENT.player.getEyePosition();

      for (Direction var11 : Direction.values()) {
         BlockPos var12 = var1.relative(var11);
         BlockState var13 = MINECRAFT_CLIENT.level.getBlockState(var12);
         if (var13.isAir() || var13.canBeReplaced()) {
            BlockHitResult var14 = this.method__(var1, var11, var2);
            if (var14 != null) {
               double var15 = Math.min(Math.max(var14.getLocation().x, var1.getX() + Яйт), var1.getX() + 1.0 - Яйа);
               double var17 = Math.min(Math.max(var14.getLocation().y, var1.getY() + ЯйЮ), var1.getY() + 1.0 - ЯйЧ);
               double var19 = Math.min(Math.max(var14.getLocation().z, var1.getZ() + ЯйЛ), var1.getZ() + 1.0 - Яй诶);
               BlockHitResult var21 = new BlockHitResult(new Vec3(var15, var17, var19), var14.getDirection(), var14.getBlockPos(), false);
               double var22 = var7.distanceToSqr(var21.getLocation());
               if (var22 < var5) {
                  var5 = var22;
                  var4 = var21;
               }
            }
         }
      }

      return var4;
   }

   private List<Player> method_1(float var1) {
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

   private void Ъ(WorldEffectsEvent var1) {
      if (this.renderPositionSetting.getValue() && this.activeAnchorPosition != null && MINECRAFT_CLIENT.level != null) {
         VertexConsumer var2 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
         AABB var3 = new AABB(this.activeAnchorPosition.getX(), this.activeAnchorPosition.getY(), this.activeAnchorPosition.getZ(), this.activeAnchorPosition.getX() + 1.0, this.activeAnchorPosition.getY() + 1.0, this.activeAnchorPosition.getZ() + 1.0);
         WorldBoxGeometry.appendBox(var1.getPoses(), var2, var1.getCameraPosition(), var3.minX, var3.minY, var3.minZ, var3.maxX, var3.maxY, var3.maxZ, АЭ, true);
      }
   }

   public AutoAnchor() {
      super(Яы_, ЯыШ, ModuleCategory.COMBAT);
      this.targetRadiusSetting = new FloatSetting(Яыы, Яый, ЯыЖ, Яым).withStep(ЯыК);
      this.placementRangeSetting = new FloatSetting(ЯыI, ЯыТ, ЯыО, ЯыФ).withStep(ЯыЙ);
      this.breakRangeSetting = new FloatSetting(Яык, Яыг, 1.0F, Яы0).withStep(Яых);
      this.minEnemyDamageSetting = new FloatSetting(ЯыУ, ЯыЗ, 1.0F, ЯыЕ).withStep(ЯыП);
      this.maxSelfDamageSetting = new FloatSetting(Яы西, Яыд, 0.0F, ЯыБ).withStep(ЯыС);
      this.actionDelaySetting = new FloatSetting(Яыз, 0.0F, 0.0F, Яыц).withStep(Яыя).withDisplayFormat(NumericDisplayFormat.INTEGER);
      this.placeObsidianSetting = new BooleanSetting(Яйс, true);
      this.antiSuicideSetting = new BooleanSetting(ЯйВ, true);
      this.renderPositionSetting = new BooleanSetting(ЯйЯ, true);
      this.logsSetting = new BooleanSetting(ЯйХ, false);
      this.actionDelayTimer = new Stopwatch();
      this.rotationCoordinator = SharedRotationCoordinator.getInstance();
      this.anchorPlacedTick = -100;
      this.previousHotbarSlot = -1;
      this.playerTickListener = this::Ма;
      this.worldEffectsListener = this::Ъ;
      this.addSettings(this.targetRadiusSetting, this.placementRangeSetting, this.breakRangeSetting, this.minEnemyDamageSetting, this.maxSelfDamageSetting, this.actionDelaySetting, this.placeObsidianSetting, this.antiSuicideSetting, this.renderPositionSetting, this.logsSetting);
   }

   private void Ма(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && !MINECRAFT_CLIENT.player.isSpectator()) {
         if (MINECRAFT_CLIENT.level.dimension().identifier().equals(Level.NETHER.identifier())) {
            this.method_1Ю();
            this.method_1э();
         } else {
            List var2 = this.ь(this.targetRadiusSetting.getValue());
            if (var2.isEmpty()) {
               this.method_1Ю();
               this.method_1э();
            } else if (this.actionDelayTimer.М(this.actionDelaySetting.getValue().longValue())) {
               float var3 = this.antiSuicideSetting.getValue() ? Math.max(0.0F, MINECRAFT_CLIENT.player.getHealth() + MINECRAFT_CLIENT.player.getAbsorptionAmount() - 1.0F) : Яйе;
               if (this.activeAnchorPosition != null) {
                  BlockState var6 = MINECRAFT_CLIENT.level.getBlockState(this.activeAnchorPosition);
                  if (!(var6.getBlock() instanceof RespawnAnchorBlock)) {
                     if (MINECRAFT_CLIENT.player.tickCount - this.anchorPlacedTick > 20) {
                        this.method_1Ю();
                     }
                  } else if (this.anchorActionPhase == 1) {
                     if (this.и(Items.GLOWSTONE) != -1 && !this.б(this.activeAnchorPosition)) {
                        this.actionDelayTimer.ВД();
                     } else {
                        this.anchorActionPhase = 2;
                        this.actionDelayTimer.ВД();
                     }
                  } else if (this.anchorActionPhase == 2) {
                     if (this.Ь(this.activeAnchorPosition)) {
                        this.method_1Ю();
                     }

                     this.actionDelayTimer.ВД();
                  } else {
                     if (this.Ь(this.activeAnchorPosition)) {
                        this.method_1Ю();
                     }

                     this.actionDelayTimer.ВД();
                  }
               } else if (this.и(Items.RESPAWN_ANCHOR) == -1) {
                  this.method_1э();
               } else {
                  List var4 = this.method_1(this.targetRadiusSetting.getValue() + ЯйА);
                  RespawnAnchorPlacementPlanner.AnchorPlacement var5 = RespawnAnchorPlacementPlanner.西(
                     var2, MINECRAFT_CLIENT.player.getEyePosition(), this.placementRangeSetting.getValue(), this.breakRangeSetting.getValue(), this.maxSelfDamageSetting.getValue(), this.minEnemyDamageSetting.getValue(), this.placeObsidianSetting.getValue(), var3, var4
                  );
                  if (var5 == null) {
                     this.method_1э();
                  } else if (var5.needsObsidian()) {
                     if (this.и(Items.OBSIDIAN) == -1) {
                        this.method_1э();
                     } else {
                        if (this.ж(var5.base(), Items.OBSIDIAN)) {
                           this.actionDelayTimer.ВД();
                        }
                     }
                  } else {
                     if (this.й(var5.anchorPos())) {
                        this.activeAnchorPosition = var5.anchorPos().immutable();
                        this.anchorPlacedTick = MINECRAFT_CLIENT.player.tickCount;
                        this.anchorActionPhase = 1;
                        this.actionDelayTimer.ВД();
                     }
                  }
               }
            }
         }
      } else {
         this.method_1Ю();
         this.method_1э();
      }
   }

   private boolean б(BlockPos var1) {
      return this.method_8(var1, Items.GLOWSTONE);
   }

   private boolean й(BlockPos var1) {
      return this.ж(var1, Items.RESPAWN_ANCHOR);
   }

   private BlockHitResult method__(BlockPos var1, Direction var2, double var3) {
      if (MINECRAFT_CLIENT.player != null && var1 != null && var2 != null && !(var3 <= 0.0)) {
         Vec3 var5 = MINECRAFT_CLIENT.player.getEyePosition();
         Vec3 var6 = Vec3.directionFromRotation(MINECRAFT_CLIENT.player.getXRot(), MINECRAFT_CLIENT.player.getYRot());
         double var7;
         double var9;
         double var11;
         switch (var2.getAxis()) {
            case X:
               var7 = var1.getX() + (var2 == Direction.EAST ? 1.0 : 0.0);
               var9 = var5.x;
               var11 = var6.x;
               break;
            case Y:
               var7 = var1.getY() + (var2 == Direction.UP ? 1.0 : 0.0);
               var9 = var5.y;
               var11 = var6.y;
               break;
            case Z:
               var7 = var1.getZ() + (var2 == Direction.SOUTH ? 1.0 : 0.0);
               var9 = var5.z;
               var11 = var6.z;
               break;
            default:
               return null;
         }

         if (Math.abs(var11) < Яй1) {
            return null;
         }

         double var13 = (var7 - var9) / var11;
         if (!(var13 < 0.0) && !(var13 > var3)) {
            Vec3 var15 = var5.add(var6.scale(var13));
            double var16 = ЯйЬ;

            boolean var18 = switch (var2.getAxis()) {
               case X -> var15.y >= var1.getY() - var16
                  && var15.y <= var1.getY() + 1.0 + var16
                  && var15.z >= var1.getZ() - var16
                  && var15.z <= var1.getZ() + 1.0 + var16;
               case Y -> var15.x >= var1.getX() - var16
                  && var15.x <= var1.getX() + 1.0 + var16
                  && var15.z >= var1.getZ() - var16
                  && var15.z <= var1.getZ() + 1.0 + var16;
               case Z -> var15.x >= var1.getX() - var16
                  && var15.x <= var1.getX() + 1.0 + var16
                  && var15.y >= var1.getY() - var16
                  && var15.y <= var1.getY() + 1.0 + var16;
            };
            return !var18 ? null : new BlockHitResult(var15, var2, var1, false);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void method_1э() {
      if (this.previousHotbarSlot != -1 && MINECRAFT_CLIENT.player != null) {
         this.Ю(this.previousHotbarSlot);
      }

      this.previousHotbarSlot = -1;
   }

   private boolean method_8(BlockPos var1, Item var2) {
      int var3 = this.и(var2);
      if (var3 == -1) {
         return false;
      }

      Vec3 var4 = new Vec3(var1.getX() + Яй4, var1.getY() + Яйп, var1.getZ() + ЯйД);
      if (!this.Ж(var4)) {
         return false;
      }

      double var5 = MINECRAFT_CLIENT.player.getEyePosition().distanceTo(var4) + 1.0;
      BlockHitResult var7 = this.method__(var1, var5);
      if (var7 == null) {
         return false;
      }

      this.Ю(var3);
      InteractionResult var8 = MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var7);
      boolean var9 = var8 != null && var8.consumesAction();
      if (var9) {
         MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
      }

      return var9;
   }

   private boolean Ь(BlockPos var1) {
      int var2 = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
      Item var3 = MINECRAFT_CLIENT.player.getInventory().getItem(var2).getItem();
      int var4;
      if (var3 != Items.GLOWSTONE && var3 != Items.RESPAWN_ANCHOR) {
         var4 = var2;
      } else {
         var4 = -1;

         for (int var5 = 0; var5 < 9; var5++) {
            Item var6 = MINECRAFT_CLIENT.player.getInventory().getItem(var5).getItem();
            if (var6 != Items.GLOWSTONE && var6 != Items.RESPAWN_ANCHOR) {
               var4 = var5;
               break;
            }
         }

         if (var4 == -1) {
            for (int var7 = 0; var7 < 9; var7++) {
               if (!MINECRAFT_CLIENT.player.getInventory().getItem(var7).is(Items.GLOWSTONE)) {
                  var4 = var7;
                  break;
               }
            }
         }
      }

      return var4 == -1 ? false : this.method_8(var1, MINECRAFT_CLIENT.player.getInventory().getItem(var4).getItem());
   }

   @Override
   public void onDisable() {
      this.method_1Д();
      this.method_1э();
      this.method_1Ю();
      super.onDisable();
   }

   private boolean ж(BlockPos var1, Item var2) {
      BlockPlacementHit var3 = CrystalPlacementPlanner.method_4_85745e18(var1, MINECRAFT_CLIENT.player.getEyePosition(), this.placementRangeSetting.getValue());
      if (var3 == null) {
         return false;
      }

      int var4 = this.и(var2);
      if (var4 == -1) {
         return false;
      }

      if (!this.Ж(var3.hitVec())) {
         return false;
      }

      double var5 = MINECRAFT_CLIENT.player.getEyePosition().distanceTo(var3.hitVec()) + 1.0;
      BlockHitResult var7 = this.method__(var3.neighbor(), var3.side(), var5);
      if (var7 == null) {
         return false;
      }

      this.Ю(var4);
      InteractionResult var8 = MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var7);
      boolean var9 = var8 != null && var8.consumesAction();
      if (var9) {
         MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
      }

      return var9;
   }

   private void Ю(int var1) {
      if (MINECRAFT_CLIENT.player != null && var1 >= 0 && var1 <= 8) {
         if (MINECRAFT_CLIENT.player.getInventory().getSelectedSlot() != var1) {
            if (this.previousHotbarSlot == -1) {
               this.previousHotbarSlot = MINECRAFT_CLIENT.player.getInventory().getSelectedSlot();
            }

            MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(var1);
            MINECRAFT_CLIENT.player.connection.send(new ServerboundSetCarriedItemPacket(var1));
         }
      }
   }

   private void method_1Ю() {
      this.activeAnchorPosition = null;
      this.anchorActionPhase = 0;
      this.method_1э();
   }

   private int и(Item var1) {
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

   private boolean Ж(Vec3 var1) {
      return this.rotationCoordinator.requestRotation(this, var1);
   }

   private List<Player> ь(float var1) {
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

   private void method_1Д() {
      this.rotationCoordinator.releaseOwner(this);
   }
}
