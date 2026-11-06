package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerPositionCorrectionEvent;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.render.pipeline.InventoryActions;
import sg.settings.BooleanSetting;
import sg.util.time.ReallyWorldItemCooldowns;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.ModuleRegistry;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.rotation.RotationController;
import sg.settings.SingleChoiceSetting;

public class TargetPearl extends ClientModule {
   private static final int сХ = 320;
   private static final double щ_3f39d5c3 = 0.03;
   private static final double Г = 0.99;
   private static final double л = 0.8;
   private static final double э = 1.5;
   private static final double ю = 0.1;
   private static final float сю = (float) (Math.PI / 180.0);
   private static final double Щ = 0.42;
   private static final double у = 1.75;
   private static final double field_2 = 16.0;
   private static final double ф = 0.3;
   private final @com.google.gson.annotations.SerializedName("М")  BooleanSetting onlyTargetSetting;
   @SerializedName("Э")
   private final FloatSetting targetMemorySetting;
   @SerializedName("必")
   private final FloatSetting minDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("Н")  BooleanSetting compensateWithJumpSetting;
   private final @com.google.gson.annotations.SerializedName("н")  BooleanSetting avoidEntitiesSetting;
   private final @com.google.gson.annotations.SerializedName("Ц")  BooleanSetting accountForWaterSetting;
   private final @com.google.gson.annotations.SerializedName("ч")  BooleanSetting autoTrapSetting;
   @SerializedName("Д")
   private final SingleChoiceSetting trapTypeSetting;
   @SerializedName("п")
   private final Set<Integer> processedPearlIds;
   @SerializedName("Д")
   private final Set<Integer> newPearlIds;
   private @com.google.gson.annotations.SerializedName("В")  UUID rememberedTargetId;
   private @com.google.gson.annotations.SerializedName("Ф")  long targetRememberedAtMillis;
   private @com.google.gson.annotations.SerializedName("о")  boolean jumpCompensationActive;
   private @com.google.gson.annotations.SerializedName("O")  boolean jumpKeyForced;
   private @com.google.gson.annotations.SerializedName("се")  int trackedPearlId;
   private @com.google.gson.annotations.SerializedName("т")  Vec3 trackedPearlLanding;
   private @com.google.gson.annotations.SerializedName("сА")  int jumpWaitTicks;
   @SerializedName("7")
   private boolean trapActive;
   private @com.google.gson.annotations.SerializedName("а")  Vec3 trapPosition;
   @SerializedName("Я")
   private UUID trapTargetId;
   private @com.google.gson.annotations.SerializedName("Й")  long trapExpireAtMillis;
   @SerializedName("Э")
   private final EventListener<PlayerTickEvent> playerTickListener;
   @SerializedName("必")
   private final EventListener<PacketReceiveEvent> packetReceiveListener;
   @SerializedName("щ")
   private final EventListener<PlayerPositionCorrectionEvent> positionCorrectionListener;
   private static final String В诶У = "TargetPearl";
   private static final String В诶З = "Кидает пёрл в траекторию чужого эндер жемчуга";
   private static final String В诶Е = "Только за таргетом";
   private static final String В诶П = "Память таргета";
   private static final float В诶西 = 12.0F;
   private static final float В诶д = 30.0F;
   private static final String В诶Б = "Минимальная дистанция";
   private static final float В诶С = 10.0F;
   private static final float В诶з = 8.0F;
   private static final float В诶ц = 20.0F;
   private static final String В诶я = "Компенсировать прыжком";
   private static final String В1с = "Не кидать в энтити";
   private static final String В1В = "Учитывать воду";
   private static final String В1Я = "Авто ловушка";
   private static final String В1Х = "Тип ловушки";
   private static final String В1е = "Эндер Ловушка";
   private static final String В1А = "Ловушка";
   private static final String В14 = "Любая";
   private static final int В1п = Integer.MAX_VALUE;
   private static final double В1Д = 0.35;
   private static final String В1Ъ = "Легитный";
   private static final long В1т = 4000L;
   private static final double В1а = 25.0;
   private static final long В1Ю = 1500L;
   private static final float В1Ч = 6.0F;
   private static final double В1Л = 0.5;
   private static final float В1诶 = -90.0F;
   private static final float В11 = 90.0F;
   private static final String В1Ь = "ender";
   private static final String В1衣 = "trap";
   private static final String В1ш = "Эндер Ловушка";
   private static final String В1И = "Ловушка";
   private static final String В1ь = "\\s+";
   private static final String В1ж = " ";
   private static final String В1弟 = "эндер ловушка";
   private static final String В1б = "ender";
   private static final String В1Ы = "ловушка";
   private static final String В1l = "trap";
   private static final double В18 = 0.25;
   private static final float В15 = 25.0F;
   private static final float В1в = 25.0F;
   private static final float В1ъ = 0.35F;
   private static final float В1М = 25.0F;
   private static final float В1Н = 25.0F;
   private static final float В1н = 0.35F;
   private static final float В1Ц = -70.0F;
   private static final float В1ч = 70.0F;
   private static final float В1Э = -70.0F;
   private static final float В1必 = 70.0F;
   private static final float В1щ = -90.0F;
   private static final float В1Г = 90.0F;
   private static final float В1л = -90.0F;
   private static final float В1э = 90.0F;
   private static final float В1ю = -0.6F;
   private static final float В1Щ = 0.6F;
   private static final float В1у = 0.1F;
   private static final float В12 = 0.05F;
   private static final double В1ф = 1.75;
   private static final double В1и = 3.5;
   private static final float В19 = -90.0F;
   private static final float В1о = 90.0F;
   private static final double В1O = 1.0E-4;
   private static final double В17 = 3.0;
   private static final double В1Р = 2.0;
   private static final double В13 = 0.8;
   private static final double В1р = 0.99;
   private static final double В16 = -0.03;
   private static final double В1_ = 0.1;
   private static final float В1Ш = (float) (Math.PI / 180.0);
   private static final float В1ы = (float) (Math.PI / 180.0);
   private static final double В1й = 1.5;
   private static final double В1Ж = 0.1;
   private static final double В1м = 0.8;
   private static final double В1К = 0.99;
   private static final double В1I = -0.03;
   private static final double В1Т = 1.3;
   private static final double В1О = Double.MAX_VALUE;
   private static final double В1Ф = 0.3;
   private static final double В1Й = 1.0E-4;
   private static final double В1к = 3.0;
   private static final double В1г = 2.0;
   private static final double В10 = 256.0;
   private static final double В1х = 256.0;
   private static final double В1У = 1.0E-4;
   private static final double В1З = 6.0;
   private static final double В1Е = 4.0;
   private static final double В1П = 64.0;
   private static final float В1西 = 1000.0F;
   private static final double В1д = 0.1;
   private static final double В1Б = 0.42;

   private boolean й(ThrownEnderpearl var1, LivingEntity var2) {
      Vec3 var3 = var1.position();
      Vec3 var4 = var1.getDeltaMovement();
      if (var2.distanceToSqr(var3) <= В1х) {
         return true;
      }

      if (var4.lengthSqr() < В1У) {
         return false;
      }

      Vec3 var5 = var3.subtract(var4.normalize().scale(Math.min(В1З, var4.length() * В1Е)));
      return var2.distanceToSqr(var5) <= В1П;
   }

   private void хм() {
      this.х6();
      this.Д(false);
      this.jumpCompensationActive = false;
      this.trackedPearlId = -1;
      this.trackedPearlLanding = null;
      this.jumpWaitTicks = 0;
   }

   private void х弟() {
      this.jumpWaitTicks++;
      if (this.accountForWaterSetting.getValue() && this.хИ()) {
         this.хм();
      } else if (MINECRAFT_CLIENT.player.onGround()) {
         this.хI();
         if (this.jumpWaitTicks > 6) {
            this.хм();
         }
      } else {
         this.х6();
         Vec3 var1 = this.trackedPearlLanding;
         if (MINECRAFT_CLIENT.level.getEntity(this.trackedPearlId) instanceof ThrownEnderpearl var3) {
            Vec3 var4 = this.р(var3);
            if (var4 != null) {
               var1 = var4;
            }
         }

         TargetPearl.ThrowSolution var5 = this.й(this.衣_2f96ac28(), this.й(false), var1);
         if (var5 == null) {
            var5 = this.й(var1);
         }

         if (var5 == null) {
            this.хм();
         } else {
            this.jumpCompensationActive = false;
            this.trackedPearlId = -1;
            this.trackedPearlLanding = null;
            this.й(var1, var5.yaw, var5.pitch);
         }
      }
   }

   private boolean й_bbeebd87(ThrownEnderpearl var1) {
      Entity var2 = this.й_f553a72a(var1);
      if (var2 == MINECRAFT_CLIENT.player) {
         return false;
      } else if (var2 != null && RoseClient.getInstance().getServices().getFriends().О(var2.getName().getString())) {
         return false;
      } else if (!this.onlyTargetSetting.getValue()) {
         return true;
      } else {
         LivingEntity var3 = this.衣_95bedd6e();
         if (var3 == null) {
            return false;
         } else {
            return var2 == null ? this.й(var1, var3) : var2 == var3 || var2.getUUID().equals(var3.getUUID());
         }
      }
   }

   private Entity й_f553a72a(ThrownEnderpearl var1) {
      Entity var2 = var1.getOwner();
      if (var2 != null) {
         return var2;
      }

      Vec3 var3 = var1.position();
      Vec3 var4 = var1.getDeltaMovement();
      Vec3 var5 = var4.lengthSqr() < В1Й ? var3 : var3.subtract(var4.normalize().scale(Math.min(В1к, var4.length() * В1г)));
      Player var6 = null;
      double var7 = В10;

      for (Player var10 : MINECRAFT_CLIENT.level.players()) {
         if (var10 != MINECRAFT_CLIENT.player && var10.isAlive()) {
            double var11 = var10.distanceToSqr(var5);
            if (var11 < var7) {
               var7 = var11;
               var6 = var10;
            }
         }
      }

      return var6;
   }

   private void хП() {
      this.trapActive = false;
      this.trapPosition = null;
      this.trapTargetId = null;
      this.trapExpireAtMillis = 0L;
   }

   private TargetPearl.ThrowSolution й(Vec3 var1) {
      Vec3 var2 = this.衣_2f96ac28();
      Vec3 var3 = var1.subtract(var2);
      float var4 = (float)Math.toDegrees(Math.atan2(-var3.x, var3.z));
      double var5 = Math.sqrt(var3.x * var3.x + var3.z * var3.z);
      float var7 = (float)(-Math.toDegrees(Math.atan2(var3.y, var5)));
      TargetPearl.ThrowSolution var8 = null;

      for (float var9 = var7 - В15; var9 <= var7 + В1в; var9 += В1ъ) {
         TargetPearl.ThrowSolution var10 = this.й(var2, this.й(false), var1, var4, var9);
         if (var10 != null && (!this.avoidEntitiesSetting.getValue() || !var10.collidedWithEntity) && (var8 == null || var10.landingError < var8.landingError)) {
            var8 = var10;
         }
      }

      if (var8 != null) {
         return var8.Е(false);
      }

      for (float var11 = var7 - В1М; var11 <= var7 + В1Н; var11 += В1н) {
         TargetPearl.ThrowSolution var12 = this.й(var2, this.й(false), var1, var4, var11);
         if (var12 != null && (var8 == null || var12.landingError < var8.landingError)) {
            var8 = var12;
         }
      }

      return var8 == null ? null : var8.Е(false);
   }

   private boolean Ч(int var1) {
      if (InventoryActions.б(var1)) {
         int var3 = MINECRAFT_CLIENT.player.getInventory().selected;
         InventoryActions.衣(InventoryActions.ч(var1));
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         InventoryActions.衣(var3);
         return true;
      } else {
         int var2 = MINECRAFT_CLIENT.player.getInventory().selected;
         InventoryActions.method_7(8, var1);
         InventoryActions.衣(8);
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         InventoryActions.衣(var2);
         InventoryActions.method_7(8, var1);
         return true;
      }
   }

   private void хЫ() {
      if (this.trapActive) {
         if (this.autoTrapSetting.getValue() && ReallyWorldItemCooldowns.Ы().method_3Щ()) {
            if (System.currentTimeMillis() > this.trapExpireAtMillis) {
               this.хП();
            } else if (this.trapPosition == null || !(MINECRAFT_CLIENT.player.distanceToSqr(this.trapPosition) > В1а)) {
               LivingEntity var1 = this.Ч();
               if (var1 != null && var1.isAlive()) {
                  float var2 = this.ы_f67ab10a();
                  if (!(MINECRAFT_CLIENT.player.distanceTo(var1) > var2)) {
                     if (this.Ч(var1)) {
                        this.хП();
                     }
                  }
               } else {
                  if (System.currentTimeMillis() + В1Ю > this.trapExpireAtMillis) {
                     this.хП();
                  }
               }
            }
         } else {
            this.хП();
         }
      }
   }

   private Vec3 й(Vec3 var1, Vec3 var2) {
      AABB var3 = new AABB(var1, var2).inflate(В1Т);
      List<Entity> var4 = MINECRAFT_CLIENT.level.getEntities(MINECRAFT_CLIENT.player, var3, this::р);
      if (var4.isEmpty()) {
         return null;
      }

      Vec3 var5 = null;
      double var6 = В1О;

      for (Entity var9 : var4) {
         Optional var10 = var9.getBoundingBox().inflate(В1Ф).clip(var1, var2);
         if (!var10.isEmpty()) {
            double var11 = var1.distanceToSqr((Vec3)var10.get());
            if (!(var11 >= var6)) {
               var5 = (Vec3)var10.get();
               var6 = var11;
            }
         }
      }

      return var5;
   }

   private TargetPearl.ThrowSolution й(Vec3 var1, Vec3 var2, Vec3 var3, float var4, float var5) {
      TargetPearl.TrajectoryResult var6 = this.й(var1, var2, var4, var5, true);
      return var6 == null ? null : new TargetPearl.ThrowSolution(var4, Mth.clamp(var5, В19, В1о), var6.landingPosition.distanceTo(var3), false, var6.collidedWithEntity);
   }

   private void хI() {
      MINECRAFT_CLIENT.options.keyJump.setDown(true);
      this.jumpKeyForced = true;
   }

   private boolean хИ() {
      return MINECRAFT_CLIENT.player.isUnderWater() || MINECRAFT_CLIENT.player.isEyeInFluid(FluidTags.WATER);
   }

   @Override
   public void onEnable() {
      this.хГ();
   }

   private void Д(boolean var1) {
      Aura var2 = RoseClient.getInstance().getServices().getModules().getAura();
      if (var2 != null) {
         var2.衣(var1);
      }
   }

   private Entity й(UUID var1) {
      for (Entity var3 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
         if (var3.getUUID().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private void Ч(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundAddEntityPacket var2) {
         if (var2.getType() == EntityTypes.ENDER_PEARL) {
            this.newPearlIds.add(var2.getId());
         }
      }
   }

   private TargetPearl.ThrowSolution й(TargetPearl.ThrowSolution var1, TargetPearl.ThrowSolution var2) {
      if (var1 != null && var2 != null) {
         return var2.landingError + В18 < var1.landingError ? var2.Е(true) : var1.Е(false);
      } else if (var2 != null) {
         return var2.Е(true);
      } else {
         return var1 == null ? null : var1.Е(false);
      }
   }

   private static String й(ItemStack var0) {
      if (var0 != null && !var0.isEmpty() && var0.is(Items.HEART_OF_THE_SEA)) {
         String var1 = StringUtil.stripColor(var0.getHoverName().getString()).toLowerCase(Locale.ROOT).replace('ё', 'е').replaceAll(В1ь, В1ж).trim();
         if (var1.equals(В1弟)) {
            return В1б;
         } else {
            return var1.equals(В1Ы) ? В1l : null;
         }
      } else {
         return null;
      }
   }

   private Vec3 衣_2f96ac28() {
      return new Vec3(MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getEyeY() - В1д, MINECRAFT_CLIENT.player.getZ());
   }

   private void хл() {
      LivingEntity var1 = this.у();
      if (var1 != null && var1.isAlive()) {
         this.rememberedTargetId = var1.getUUID();
         this.targetRememberedAtMillis = System.currentTimeMillis();
      }
   }

   private int г_a83dd0cc() {
      int var1 = -1;
      int var2 = -1;

      for (int var3 = 0; var3 < MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().size(); var3++) {
         ItemStack var4 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems().get(var3);
         String var5 = й(var4);
         if (var5 != null) {
            int var6 = var3 < 9 ? var3 + 36 : var3;
            if (var5.equals(В1Ь)) {
               var2 = var6;
            } else if (var5.equals(В1衣)) {
               var1 = var6;
            }
         }
      }

      if (this.trapTypeSetting.isSelected(В1ш)) {
         return var2;
      } else if (this.trapTypeSetting.isSelected(В1И)) {
         return var1;
      } else {
         return var2 != -1 ? var2 : var1;
      }
   }

   private Vec3 р(ThrownEnderpearl var1) {
      Vec3 var2 = var1.position();
      Vec3 var3 = var1.getDeltaMovement();

      for (int var4 = 0; var4 < 320; var4++) {
         Vec3 var5 = var2;
         var2 = var2.add(var3);
         var3 = var3.scale(var1.isInWater() ? В13 : В1р).add(0.0, В16, 0.0);
         BlockHitResult var6 = MINECRAFT_CLIENT.level.clip(new ClipContext(var5.add(0.0, В1_, 0.0), var2, Block.COLLIDER, Fluid.NONE, MINECRAFT_CLIENT.player));
         if (var6.getType() == Type.BLOCK) {
            return var6.getLocation();
         }
      }

      return var2;
   }

   private void й(PlayerPositionCorrectionEvent var1) {
      if (this.trapActive) {
         this.хЫ();
      }
   }

   private boolean Ч(Vec3 var1) {
      BlockPos var2 = BlockPos.containing(var1);
      FluidState var3 = MINECRAFT_CLIENT.level.getFluidState(var2);
      return var3.is(FluidTags.WATER) && var3.getHeight(MINECRAFT_CLIENT.level, var2) > 0.0;
   }

   private void хд() {
      if (InventoryActions.ш(Items.ENDER_PEARL) != -1) {
         if (MINECRAFT_CLIENT.gui.screen() == null) {
            if (!this.accountForWaterSetting.getValue() || !this.хИ()) {
               ThrownEnderpearl var1 = null;
               Vec3 var2 = null;
               int var3 = В1п;

               for (Integer var5 : Set.copyOf(this.newPearlIds)) {
                  if (MINECRAFT_CLIENT.level.getEntity(var5) instanceof ThrownEnderpearl var7) {
                     this.newPearlIds.remove(var5);
                     if (!this.processedPearlIds.contains(var7.getId()) && this.й_bbeebd87(var7)) {
                        Vec3 var8 = this.р(var7);
                        if (var8 != null) {
                           if (this.й_bbeebd87(var7, var8)) {
                              var1 = var7;
                              var2 = var8;
                              var3 = -1;
                              break;
                           }

                           this.processedPearlIds.add(var7.getId());
                        }
                     }
                  }
               }

               if (var1 == null) {
                  for (Entity var10 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
                     if (var10 instanceof ThrownEnderpearl var11 && !this.processedPearlIds.contains(var11.getId()) && this.й_bbeebd87(var11)) {
                        Vec3 var12 = this.р(var11);
                        if (var12 != null) {
                           if (!this.й_bbeebd87(var11, var12)) {
                              this.processedPearlIds.add(var11.getId());
                           } else if (var11.tickCount < var3) {
                              var3 = var11.tickCount;
                              var1 = var11;
                              var2 = var12;
                           }
                        }
                     }
                  }
               }

               if (var1 != null && var2 != null) {
                  this.й_de5a6f78(var1, var2);
               }
            }
         }
      }
   }

   private Vec3 й_2f96ac28(ThrownEnderpearl var1) {
      Entity var2 = this.й_f553a72a(var1);
      if (var2 != null) {
         return var2.position();
      }

      Vec3 var3 = var1.position();
      Vec3 var4 = var1.getDeltaMovement();
      return var4.lengthSqr() < В1O ? var3 : var3.subtract(var4.normalize().scale(Math.min(В17, var4.length() * В1Р)));
   }

   private void хГ() {
      this.хм();
      this.хП();
      this.processedPearlIds.clear();
      this.newPearlIds.clear();
      this.rememberedTargetId = null;
      this.targetRememberedAtMillis = 0L;
   }

   public TargetPearl() {
      super(В诶У, В诶З, ModuleCategory.COMBAT);
      this.onlyTargetSetting = new BooleanSetting(В诶Е, true);
      this.targetMemorySetting = new FloatSetting(В诶П, В诶西, 1.0F, В诶д).withStep(1.0F).visibleWhen(this.onlyTargetSetting::getValue);
      this.minDistanceSetting = new FloatSetting(В诶Б, В诶С, В诶з, В诶ц).withStep(1.0F);
      this.compensateWithJumpSetting = new BooleanSetting(В诶я, true);
      this.avoidEntitiesSetting = new BooleanSetting(В1с, true);
      this.accountForWaterSetting = new BooleanSetting(В1В, true);
      this.autoTrapSetting = new BooleanSetting(В1Я, false);
      this.trapTypeSetting = new SingleChoiceSetting(В1Х, new ChoiceOption(В1е), new ChoiceOption(В1А), new ChoiceOption(В14)).visibleWhen(this.autoTrapSetting::getValue);
      this.processedPearlIds = new HashSet<>();
      this.newPearlIds = new HashSet<>();
      this.trackedPearlId = -1;
      this.playerTickListener = this::ч;
      this.packetReceiveListener = this::Ч;
      this.positionCorrectionListener = this::й;
      this.addSettings(this.onlyTargetSetting, this.targetMemorySetting, this.minDistanceSetting, this.compensateWithJumpSetting, this.avoidEntitiesSetting, this.accountForWaterSetting, this.autoTrapSetting, this.trapTypeSetting);
   }

   private LivingEntity Ч() {
      if (this.trapTargetId != null) {
         for (Player var2 : MINECRAFT_CLIENT.level.players()) {
            if (var2.getUUID().equals(this.trapTargetId) && var2.isAlive() && !var2.isSpectator()) {
               return var2;
            }
         }

         if (this.й(this.trapTargetId) instanceof LivingEntity var4 && var4.isAlive() && !var4.isSpectator()) {
            return var4;
         }
      }

      return this.衣_95bedd6e();
   }

   private Vec3 й(boolean var1) {
      Vec3 var2 = MINECRAFT_CLIENT.player.getDeltaMovement();
      if (var1) {
         return new Vec3(var2.x, В1Б, var2.z);
      } else {
         return MINECRAFT_CLIENT.player.onGround() ? new Vec3(var2.x, 0.0, var2.z) : var2;
      }
   }

   private boolean р(Entity var1) {
      if (var1 == null || var1 == MINECRAFT_CLIENT.player || !var1.isAlive() || var1.isSpectator()) {
         return false;
      } else {
         return var1 instanceof ThrownEnderpearl ? false : var1.canBeHitByProjectile();
      }
   }

   @Override
   public void onDisable() {
      this.хГ();
   }

   private boolean й_bbeebd87(ThrownEnderpearl var1, Vec3 var2) {
      Vec3 var3 = this.й_2f96ac28(var1);
      double var4 = var2.x - var3.x;
      double var6 = var2.z - var3.z;
      return Math.sqrt(var4 * var4 + var6 * var6) >= this.minDistanceSetting.getValue().floatValue();
   }

   private float ы_f67ab10a() {
      ModuleRegistry var1 = RoseClient.getInstance().getServices().getModules();
      return var1.getRwHelper() != null ? var1.getRwHelper().уу() : В1Ч;
   }

   private void й_de5a6f78(ThrownEnderpearl var1, Vec3 var2) {
      TargetPearl.ThrowSolution var3 = this.й(this.衣_2f96ac28(), this.й(false), var2);
      TargetPearl.ThrowSolution var4 = this.compensateWithJumpSetting.getValue() && MINECRAFT_CLIENT.player.onGround() ? this.й(this.衣_2f96ac28(), this.й(true), var2) : null;
      TargetPearl.ThrowSolution var5 = this.й(var3, var4);
      if (var5 == null) {
         var5 = this.й(var2);
      }

      if (var5 != null) {
         if (this.avoidEntitiesSetting.getValue() && var5.collidedWithEntity && var5.landingError > В1Д) {
            TargetPearl.ThrowSolution var6 = this.й(this.衣_2f96ac28(), this.й(false), var2);
            if (var6 != null && !var6.collidedWithEntity) {
               var5 = var6;
            } else if (var5.collidedWithEntity) {
               return;
            }
         }

         if (var5.usesJump && this.compensateWithJumpSetting.getValue() && MINECRAFT_CLIENT.player.onGround()) {
            this.processedPearlIds.add(var1.getId());
            this.trackedPearlId = var1.getId();
            this.trackedPearlLanding = var2;
            this.jumpCompensationActive = true;
            this.jumpWaitTicks = 0;
            this.хI();
            this.Д(true);
         } else {
            this.processedPearlIds.add(var1.getId());
            this.й(var2, var5.yaw, var5.pitch);
         }
      }
   }

   private TargetPearl.TrajectoryResult й(Vec3 var1, Vec3 var2, float var3, float var4, boolean var5) {
      float var6 = var3 * В1Ш;
      float var7 = var4 * В1ы;
      Vec3 var8 = new Vec3(-Mth.sin(var6) * Mth.cos(var7), -Mth.sin(var7), Mth.cos(var6) * Mth.cos(var7)).normalize();
      Vec3 var9 = var1;
      Vec3 var10 = var8.scale(В1й).add(var2);

      for (int var11 = 0; var11 < 320; var11++) {
         Vec3 var12 = var9;
         var9 = var9.add(var10);
         if (var5) {
            Vec3 var13 = this.й(var12, var9);
            if (var13 != null) {
               return new TargetPearl.TrajectoryResult(var13, true);
            }
         }

         BlockHitResult var16 = MINECRAFT_CLIENT.level.clip(new ClipContext(var12.add(0.0, В1Ж, 0.0), var9, Block.COLLIDER, Fluid.NONE, MINECRAFT_CLIENT.player));
         if (var16.getType() != Type.MISS) {
            return new TargetPearl.TrajectoryResult(var16.getLocation(), false);
         }

         double var14 = this.Ч(var9) ? В1м : В1К;
         var10 = var10.scale(var14).add(0.0, В1I, 0.0);
      }

      return new TargetPearl.TrajectoryResult(var9, false);
   }

   private boolean Ч(LivingEntity var1) {
      int var2 = this.г_a83dd0cc();
      if (var2 == -1) {
         return false;
      }

      Vec3 var3 = var1.position().add(0.0, var1.getBbHeight() * В1Л, 0.0);
      Vec3 var4 = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 var5 = var3.subtract(var4);
      float var6 = (float)Math.toDegrees(Math.atan2(-var5.x, var5.z));
      float var7 = (float)(-Math.toDegrees(Math.atan2(var5.y, Math.sqrt(var5.x * var5.x + var5.z * var5.z))));
      var7 = Mth.clamp(var7, В1诶, В11);
      float var8 = MINECRAFT_CLIENT.player.getYRot();
      float var9 = MINECRAFT_CLIENT.player.getXRot();
      RotationController var10 = RoseClient.getInstance().getControllers().getRotations();
      this.Д(true);
      MINECRAFT_CLIENT.player.setYRot(var6);
      MINECRAFT_CLIENT.player.setXRot(var7);
      PacketEventBypass.А(new PosRot(MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getY(), MINECRAFT_CLIENT.player.getZ(), var6, var7, MINECRAFT_CLIENT.player.onGround(), MINECRAFT_CLIENT.player.horizontalCollision));
      boolean var11 = this.Ч(var2);
      MINECRAFT_CLIENT.player.setYRot(var8);
      MINECRAFT_CLIENT.player.setXRot(var9);
      var10.getAppliedRotation().set(var8, var9);
      this.Д(false);
      return var11;
   }

   private LivingEntity 衣_95bedd6e() {
      LivingEntity var1 = this.у();
      if (var1 != null && var1.isAlive()) {
         return var1;
      }

      if (this.rememberedTargetId == null) {
         return null;
      }

      long var2 = Math.round(this.targetMemorySetting.getValue() * В1西);
      if (System.currentTimeMillis() - this.targetRememberedAtMillis > var2) {
         this.rememberedTargetId = null;
         return null;
      }

      for (Player var5 : MINECRAFT_CLIENT.level.players()) {
         if (var5.getUUID().equals(this.rememberedTargetId) && var5.isAlive() && !var5.isSpectator()) {
            return var5;
         }
      }

      return this.й(this.rememberedTargetId) instanceof LivingEntity var7 && var7.isAlive() && !var7.isSpectator() ? var7 : null;
   }

   private void 衣(Vec3 var1) {
      if (ReallyWorldItemCooldowns.Ы().method_3Щ()) {
         if (this.г_a83dd0cc() != -1) {
            LivingEntity var2 = this.衣_95bedd6e();
            this.trapActive = true;
            this.trapPosition = var1;
            this.trapTargetId = var2 != null ? var2.getUUID() : this.rememberedTargetId;
            this.trapExpireAtMillis = System.currentTimeMillis() + В1т;
         }
      }
   }

   private LivingEntity у() {
      Aura var1 = RoseClient.getInstance().getServices().getModules().getAura();
      return var1 != null && var1.isEnabled() ? var1.Ж_95bedd6e() : null;
   }

   private TargetPearl.ThrowSolution й(Vec3 var1, Vec3 var2, Vec3 var3) {
      double var4 = var3.x - var1.x;
      double var6 = var3.z - var1.z;
      float var8 = (float)Math.toDegrees(Math.atan2(-var4, var6));
      TargetPearl.ThrowSolution var9 = null;

      for (float var10 = В1Ц; var10 <= В1ч; var10++) {
         TargetPearl.ThrowSolution var11 = this.й(var1, var2, var3, var8, var10);
         if (var11 != null && (!this.avoidEntitiesSetting.getValue() || !var11.collidedWithEntity) && (var9 == null || var11.landingError < var9.landingError)) {
            var9 = var11;
         }
      }

      if (var9 == null) {
         for (float var15 = В1Э; var15 <= В1必; var15++) {
            TargetPearl.ThrowSolution var17 = this.й(var1, var2, var3, var8, var15);
            if (var17 != null && (var9 == null || var17.landingError < var9.landingError)) {
               var9 = var17;
            }
         }
      }

      if (var9 == null) {
         return null;
      }

      float var16 = Mth.clamp(var9.pitch - 1.0F, В1щ, В1Г);
      float var18 = Mth.clamp(var9.pitch + 1.0F, В1л, В1э);

      for (float var12 = var16; var12 <= var18; var12 += В12) {
         for (float var13 = В1ю; var13 <= В1Щ; var13 += В1у) {
            TargetPearl.ThrowSolution var14 = this.й(var1, var2, var3, var8 + var13, var12);
            if (var14 != null && (!this.avoidEntitiesSetting.getValue() || !var14.collidedWithEntity) && (var9 == null || var14.landingError < var9.landingError)) {
               var9 = var14;
            }
         }
      }

      if (var9 == null) {
         return null;
      } else if (var9.landingError > В1ф) {
         return var9.landingError < В1и ? var9 : null;
      } else {
         return var9;
      }
   }

   private void х6() {
      if (this.jumpKeyForced) {
         MINECRAFT_CLIENT.options.keyJump.setDown(false);
         this.jumpKeyForced = false;
      }
   }

   private void ч(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.хл();
         this.processedPearlIds.removeIf(var0 -> !(MINECRAFT_CLIENT.level.getEntity(var0) instanceof ThrownEnderpearl));
         this.newPearlIds.removeIf(var1x -> this.processedPearlIds.contains(var1x));
         if (this.jumpCompensationActive) {
            this.х弟();
         } else {
            this.хд();
            this.хЫ();
         }
      } else {
         this.хГ();
      }
   }

   private void й(Vec3 var1, float var2, float var3) {
      this.Д(true);
      float var4 = MINECRAFT_CLIENT.player.getYRot();
      float var5 = MINECRAFT_CLIENT.player.getXRot();
      RotationController var6 = RoseClient.getInstance().getControllers().getRotations();
      MINECRAFT_CLIENT.player.setYRot(var2);
      MINECRAFT_CLIENT.player.setXRot(var3);
      PacketEventBypass.А(new PosRot(MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getY(), MINECRAFT_CLIENT.player.getZ(), var2, var3, MINECRAFT_CLIENT.player.onGround(), MINECRAFT_CLIENT.player.horizontalCollision));
      boolean var7 = this.х8();
      MINECRAFT_CLIENT.player.setYRot(var4);
      MINECRAFT_CLIENT.player.setXRot(var5);
      var6.getAppliedRotation().set(var4, var5);
      this.Д(false);
      this.х6();
      if (var7 && this.autoTrapSetting.getValue()) {
         this.衣(var1);
      }
   }

   private boolean х8() {
      int var1 = InventoryActions.ш(Items.ENDER_PEARL);
      if (var1 == -1) {
         return false;
      }

      if (RoseClient.getInstance().getServices().getModules().getGuiMove().Щ().isSelected(В1Ъ) && !InventoryActions.б(var1)) {
      }

      if (InventoryActions.б(var1)) {
         int var3 = MINECRAFT_CLIENT.player.getInventory().selected;
         InventoryActions.衣(InventoryActions.ч(var1));
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         InventoryActions.衣(var3);
         return true;
      } else {
         int var2 = MINECRAFT_CLIENT.player.getInventory().selected;
         InventoryActions.method_7(8, var1);
         InventoryActions.衣(8);
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         InventoryActions.衣(var2);
         InventoryActions.method_7(8, var1);
         return true;
      }
   }

   private static final class ThrowSolution {
      private final @com.google.gson.annotations.SerializedName("пщ")  float yaw;
      private final @com.google.gson.annotations.SerializedName("пГ")  float pitch;
      private final @com.google.gson.annotations.SerializedName("ВБ")  double landingError;
      private final @com.google.gson.annotations.SerializedName("ХЮ")  boolean usesJump;
      private final @com.google.gson.annotations.SerializedName("ХЧ")  boolean collidedWithEntity;

      private ThrowSolution(float var1, float var2, double var3, boolean var5, boolean var6) {
         this.yaw = var1;
         this.pitch = var2;
         this.landingError = var3;
         this.usesJump = var5;
         this.collidedWithEntity = var6;
      }

      private TargetPearl.ThrowSolution Е(boolean var1) {
         return new TargetPearl.ThrowSolution(this.yaw, this.pitch, this.landingError, var1, this.collidedWithEntity);
      }
   }

   private static final class TrajectoryResult {
      private final @com.google.gson.annotations.SerializedName("С")  Vec3 landingPosition;
      private final @com.google.gson.annotations.SerializedName("АН")  boolean collidedWithEntity;

      private TrajectoryResult(Vec3 var1, boolean var2) {
         this.landingPosition = var1;
         this.collidedWithEntity = var2;
      }
   }
}
