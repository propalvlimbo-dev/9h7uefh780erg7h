package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerMovementEvent;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.modules.impl.init.StrafeSpeedCalculator;

public class TargetStrafe extends ClientModule {
   private final FloatSetting ХВ;
   private final FloatSetting ХЯ;
   private final BooleanSetting ВГ;
   private final FloatSetting ХХ;
   private final FloatSetting Хе;
   private final BooleanSetting Вл;
   private float Д1;
   private LivingEntity 衣;
   @SerializedName("В")
   private final TargetStrafe.DamageWindow В_ac2e5cce;
   private String ЯХ;
   @SerializedName("В")
   private final StrafeSpeedCalculator В_566fcb6d;
   private final List<Packet<?>> си;
   private final EventListener<PlayerMovementEvent> Яр;
   private final EventListener<PlayerTickEvent> Я6;
   private final EventListener<PacketReceiveEvent> Я_;
   private final EventListener<PacketSendEvent> ЯШ;
   private static final String ХМв = "TargetStrafe";
   private static final String ХМъ = "Стрейф вокруг цели ауры";
   private static final String ХММ = "Дистанция";
   private static final float ХМН = 0.1F;
   private static final float ХМн = 6.0F;
   private static final float ХМЦ = 0.05F;
   private static final String ХМч = "Скорость";
   private static final float ХМЭ = -0.1F;
   private static final float ХМ必 = 0.5F;
   private static final float ХМщ = 0.01F;
   private static final String ХМГ = "Буст с дамагом";
   private static final String ХМл = "Значение буста";
   private static final float ХМэ = 1.5F;
   private static final float ХМю = 0.1F;
   private static final float ХМЩ = 5.0F;
   private static final float ХМу = 0.05F;
   private static final String ХМ2 = "Время буста";
   private static final float ХМф = 10.0F;
   private static final float ХМи = 20.0F;
   private static final String ХМ9 = "Сохранять цель";
   private static final float ХМо = -1.0F;
   private static final float ХМO = -1.0F;
   private static final long ХМ7 = 100L;
   private static final float ХМР = 10.0F;
   private static final double ХМ3 = 90.0;

   private boolean method_5(BlockPos var1) {
      return MINECRAFT_CLIENT.level.getBlockState(var1).is(Blocks.COBWEB) || MINECRAFT_CLIENT.level.getBlockState(var1.below()).getBlock() instanceof SoulSandBlock;
   }

   private void Ш(PlayerMovementEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player.tickCount >= 10) {
         boolean var2 = MINECRAFT_CLIENT.options.keyLeft.isDown();
         boolean var3 = MINECRAFT_CLIENT.options.keyRight.isDown();
         LivingEntity var4 = this.а_95bedd6e();
         if (var4 != null) {
            this.ЯХ = var4.getName().getString();
         }

         if (this.Ц(var4)) {
            this.衣 = this.Л(this.衣);
         } else {
            this.衣 = var4;
         }

         if (this.衣 != null && this.衣.isAlive() && this.衣.getHealth() > 0.0F) {
            if (MINECRAFT_CLIENT.player.horizontalCollision) {
               this.Д1 = this.Д1 * ХМо;
            }

            if (var2) {
               this.Д1 = 1.0F;
            }

            if (var3) {
               this.Д1 = ХМO;
            }

            double var5 = Math.atan2(MINECRAFT_CLIENT.player.getZ() - this.衣.getZ(), MINECRAFT_CLIENT.player.getX() - this.衣.getX());
            double var7 = Math.hypot(MINECRAFT_CLIENT.player.getDeltaMovement().x, MINECRAFT_CLIENT.player.getDeltaMovement().z);
            var5 += var7 / Math.max(MINECRAFT_CLIENT.player.distanceTo(this.衣), this.ХВ.getMinimum()) * this.Д1;
            double var9 = this.衣.getX() + this.ХВ.getValue().floatValue() * Math.cos(var5);
            double var11 = this.衣.getZ() + this.ХВ.getValue().floatValue() * Math.sin(var5);
            double var13 = this.Л(MINECRAFT_CLIENT.player, var9, var11);
            this.В_ac2e5cce.O(this.Хе.getValue().longValue() * ХМ7);
            float var15 = this.ХХ.getValue() / ХМР;
            double var16 = this.В_566fcb6d.Э(var1, this.ВГ.getValue(), this.В_ac2e5cce.Н弟(), true, var15) + this.ХЯ.getValue().floatValue();
            Vec3 var18 = var1.getMovement();
            var1.setMovement(new Vec3(var16 * -Math.sin(Math.toRadians(var13)), var18.y, var16 * Math.cos(Math.toRadians(var13))));
            this.В_566fcb6d.п(Math.hypot(var1.getMovement().x, var1.getMovement().z));
         }
      }
   }

   public TargetStrafe() {
      super(ХМв, ХМъ, ModuleCategory.MOVEMENT);
      this.ХВ = new FloatSetting(ХММ, 1.0F, ХМН, ХМн).withStep(ХМЦ);
      this.ХЯ = new FloatSetting(ХМч, 0.0F, ХМЭ, ХМ必).withStep(ХМщ);
      this.ВГ = new BooleanSetting(ХМГ, true);
      this.ХХ = new FloatSetting(ХМл, ХМэ, ХМю, ХМЩ).withStep(ХМу).visibleWhen(this.ВГ::getValue);
      this.Хе = new FloatSetting(ХМ2, ХМф, 1.0F, ХМи).withStep(1.0F).visibleWhen(this.ВГ::getValue);
      this.Вл = new BooleanSetting(ХМ9, true);
      this.Д1 = 1.0F;
      this.В_ac2e5cce = new TargetStrafe.DamageWindow();
      this.ЯХ = "";
      this.В_566fcb6d = new StrafeSpeedCalculator();
      this.си = new ArrayList<>();
      this.Яр = this::Ш;
      this.Я6 = this::ьд;
      this.Я_ = this::е;
      this.ЯШ = this::г;
      this.addSettings(this.ХВ, this.ХЯ, this.Вл, this.ВГ, this.ХХ, this.Хе);
   }

   private LivingEntity а_95bedd6e() {
      return null;
   }

   private double Л(LivingEntity var1, double var2, double var4) {
      return Math.toDegrees(Math.atan2(var4 - var1.getZ(), var2 - var1.getX())) - ХМ3;
   }

   private LivingEntity Л(LivingEntity var1) {
      for (Entity var3 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
         if (var3 instanceof Player && var3.getName().getString().equalsIgnoreCase(this.ЯХ)) {
            return (LivingEntity)var3;
         }
      }

      return var1;
   }

   private void е(PacketReceiveEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.В_ac2e5cce.Ъ(var1);
         if (var1.getPacket() instanceof ClientboundPlayerPositionPacket) {
            this.В_566fcb6d.л(0.0);
         }
      }
   }

   private void ьг(boolean var1) {
      ServerboundPlayerCommandPacket var2 = new ServerboundPlayerCommandPacket(MINECRAFT_CLIENT.player, var1 ? Action.START_SPRINTING : Action.STOP_SPRINTING);
      this.си.add(var2);
      MINECRAFT_CLIENT.player.connection.send(var2);
   }

   @Override
   public void onEnable() {
      this.В_566fcb6d.л(0.0);
      this.衣 = null;
      super.onEnable();
   }

   private void ьд(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player.onGround() && !MINECRAFT_CLIENT.options.keyJump.isDown() && this.衣 != null && this.衣.isAlive()) {
         MINECRAFT_CLIENT.player.jumpFromGround();
      }
   }

   public boolean I2() {
      if (this.IЬ()) {
         return false;
      }

      BlockPos var1 = MINECRAFT_CLIENT.player.blockPosition();
      BlockPos var2 = var1.above();
      BlockPos var3 = var1.below();
      return this.Л(var2, var3) ? false : !this.method_5(var1) && this.I9();
   }

   private boolean I9() {
      return !MINECRAFT_CLIENT.player.getAbilities().flying && !MINECRAFT_CLIENT.player.hasEffect(MobEffects.LEVITATION);
   }

   private boolean Ц(LivingEntity var1) {
      return this.Вл.getValue() && var1 != null && this.ЯХ != null;
   }

   private void г(PacketSendEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && var1.getPacket() instanceof ServerboundPlayerCommandPacket var2) {
         if (var2.getAction() == Action.START_SPRINTING || var2.getAction() == Action.STOP_SPRINTING) {
            if (!this.си.remove(var1.getPacket())) {
               boolean var4 = RoseClient.getInstance().getControllers().getServerPlayers().method_96();
               if (this.I2() && var4 != this.В_566fcb6d.弟Б()) {
                  var1.setCancelled(true);
                  this.ьг(this.В_566fcb6d.弟Б());
               } else {
                  if (this.В_566fcb6d.弟И()) {
                     var1.setCancelled(true);
                     this.ьг(!var4);
                     this.В_566fcb6d.уз(false);
                  }
               }
            }
         }
      }
   }

   private boolean IЬ() {
      return MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null || MINECRAFT_CLIENT.player.isShiftKeyDown() || MINECRAFT_CLIENT.player.isFallFlying() || MINECRAFT_CLIENT.player.isInWater() || MINECRAFT_CLIENT.player.isInLava();
   }

   private boolean Л(BlockPos var1, BlockPos var2) {
      return MINECRAFT_CLIENT.level.getBlockState(var1).isAir() && MINECRAFT_CLIENT.level.getBlockState(var2).is(Blocks.WATER);
   }

   private static class DamageWindow implements MinecraftAccess {
      private long Хн = System.currentTimeMillis();
      private boolean еВ;
      private boolean еЯ;
      private boolean еХ;
      private boolean ее;
      private boolean еА;

      private boolean Н弟() {
         return this.еВ;
      }

      private void Ъ(PacketReceiveEvent var1) {
         boolean var2 = this.еЯ || this.ее || this.еХ || this.еА;
         if (!this.ц0()) {
            if (var1.getPacket() instanceof ClientboundExplodePacket) {
               this.еХ = true;
            }

            if (!var2) {
               if (var1.getPacket() instanceof ClientboundEntityEventPacket var3 && var3.getEventId() == 2 && var3.getEntity(MINECRAFT_CLIENT.level) == MINECRAFT_CLIENT.player) {
                  this.еВ = true;
               }
            } else if (MINECRAFT_CLIENT.player.hurtTime > 0) {
               this.еВ = false;
               this.хР();
            }
         }
      }

      private boolean ц0() {
         return MINECRAFT_CLIENT.player != null
            && (MINECRAFT_CLIENT.player.hasEffect(MobEffects.POISON) || MINECRAFT_CLIENT.player.hasEffect(MobEffects.WITHER) || MINECRAFT_CLIENT.player.hasEffect(MobEffects.INSTANT_DAMAGE));
      }

      private void хР() {
         this.еЯ = false;
         this.еХ = false;
         this.ее = false;
         this.еА = false;
      }

      private boolean O(long var1) {
         if (this.еВ) {
            if (System.currentTimeMillis() - this.Хн > var1) {
               this.еВ = false;
               this.Хн = System.currentTimeMillis();
               return true;
            }
         } else {
            this.Хн = System.currentTimeMillis();
         }

         return false;
      }
   }
}
