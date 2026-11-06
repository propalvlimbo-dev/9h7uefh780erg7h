package sg.modules.impl.combat;

import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import sg.events.impl.PlayerTickEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.mixinbridge.EntityWebStateAccess;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.AttackEntityEvent;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;

public class Criticals extends ClientModule {
   private static final float пЬ = 0.001F;
   private static final double Вк = 1.0E-9;
   private final @com.google.gson.annotations.SerializedName("弟")  MultiChoiceSetting conditionsSetting;
   private final @com.google.gson.annotations.SerializedName("ВЮ")  BooleanSetting attackSpeedupSetting;
   private final @com.google.gson.annotations.SerializedName("Я1")  EventListener<AttackEntityEvent> attackListener;
   private final @com.google.gson.annotations.SerializedName("ЯЬ")  EventListener<PlayerTickEvent> playerTickListener;
   private @com.google.gson.annotations.SerializedName("ХА")  boolean timerSpeedModified;
   private static final String field_93 = "Criticals";
   private static final String field_9р = "Позволяет наносить критические удары";
   private static final String field_96 = "Работать если";
   private static final String field_9_ = "В паутине";
   private static final String field_9Ш = "С плавным падением";
   private static final String field_9ы = "Ускорение ударов";
   private static final String field_9й = "С плавным падением";
   private static final String field_9Ж = "В паутине";
   private static final double field_9м = 1.0E-9;
   private static final String field_9К = "С плавным падением";
   private static final double field_9I = 1.0E-9;
   private static final double field_9Т = 0.001F;
   private static final String field_9О = "С плавным падением";
   private static final float field_9Ф = 2.5F;
   private static final double field_9Й = -0.085F;
   private static final double field_9к = -0.15F;
   private static final float field_9г = 0.6F;
   private static final String field_90 = "С плавным падением";

   private void Шд(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
         this.method_7Х();
      } else if (MINECRAFT_CLIENT.player.isFallFlying() || MINECRAFT_CLIENT.player.isInWater() || MINECRAFT_CLIENT.player.isInLava()) {
         this.method_7Х();
      } else if (RoseClient.getInstance().getServices().getModules().getAura().Ж_95bedd6e() == null) {
         this.method_7Х();
      } else {
         this.method_76();
      }
   }

   public Criticals() {
      super(field_93, field_9р, ModuleCategory.COMBAT);
      this.conditionsSetting = new MultiChoiceSetting(field_96, new ChoiceOption(field_9_, true), new ChoiceOption(field_9Ш, true));
      this.attackSpeedupSetting = new BooleanSetting(field_9ы, false).visibleWhen(() -> this.conditionsSetting.isSelected(field_90));
      this.attackListener = this::Ц;
      this.playerTickListener = this::Шд;
      this.addSettings(this.conditionsSetting, this.attackSpeedupSetting);
   }

   private void method_7Х() {
      if (this.timerSpeedModified) {
         ClientTimerSpeed.Оэ(1.0F);
         this.timerSpeedModified = false;
      }
   }

   @Override
   public void onDisable() {
      this.method_7Х();
   }

   public boolean Э5() {
      return this.isEnabled() && this.conditionsSetting.isSelected(field_9й) && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.hasEffect(MobEffects.SLOW_FALLING);
   }

   private void method_76() {
      if (!this.attackSpeedupSetting.getValue() || !this.conditionsSetting.isSelected(field_9О)) {
         this.method_7Х();
      } else if (MINECRAFT_CLIENT.player.hasEffect(MobEffects.SLOW_FALLING) && !MINECRAFT_CLIENT.player.onGround()) {
         Aura var1 = RoseClient.getInstance().getServices().getModules().getAura();
         if (var1 != null && var1.Ж_95bedd6e() != null) {
            if (MINECRAFT_CLIENT.player.distanceTo(var1.Ж_95bedd6e()) > field_9Ф) {
               this.method_7Х();
            } else {
               double var2 = MINECRAFT_CLIENT.player.getDeltaMovement().y;
               if (var2 <= field_9Й && var2 >= field_9к) {
                  ClientTimerSpeed.Оэ(field_9г);
                  this.timerSpeedModified = true;
               } else {
                  this.method_7Х();
               }
            }
         } else {
            this.method_7Х();
         }
      } else {
         this.method_7Х();
      }
   }

   private void method_72() {
      if (MINECRAFT_CLIENT.player.fallDistance <= 0.0) {
         MINECRAFT_CLIENT.player.fallDistance = field_9Т;
      }
   }

   private boolean Э诶() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (((EntityWebStateAccess)MINECRAFT_CLIENT.player).dimas$isInWeb()) {
            return true;
         }

         AABB var1 = MINECRAFT_CLIENT.player.getBoundingBox();
         int var2 = Mth.floor(var1.minX);
         int var3 = Mth.floor(var1.minY);
         int var4 = Mth.floor(var1.minZ);
         int var5 = Mth.floor(var1.maxX);
         int var6 = Mth.floor(var1.maxY);
         int var7 = Mth.floor(var1.maxZ);
         MutableBlockPos var8 = new MutableBlockPos();

         for (int var9 = var2; var9 <= var5; var9++) {
            for (int var10 = var3; var10 <= var6; var10++) {
               for (int var11 = var4; var11 <= var7; var11++) {
                  if (MINECRAFT_CLIENT.level.getBlockState(var8.set(var9, var10, var11)).is(Blocks.COBWEB)) {
                     return true;
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      this.timerSpeedModified = false;
   }

   private void Ц(AttackEntityEvent var1) {
      if (var1.getPhase() == AttackEntityEvent.Phase.PRE) {
         if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
            if (var1.getTarget() instanceof LivingEntity) {
               if (!MINECRAFT_CLIENT.player.isFallFlying() && !MINECRAFT_CLIENT.player.isInWater() && !MINECRAFT_CLIENT.player.isInLava()) {
                  double var2 = MINECRAFT_CLIENT.player.getX();
                  double var4 = MINECRAFT_CLIENT.player.getY();
                  double var6 = MINECRAFT_CLIENT.player.getZ();
                  if (this.conditionsSetting.isSelected(field_9Ж) && this.Э诶()) {
                     MINECRAFT_CLIENT.player.connection.send(new Pos(var2, var4 - field_9м, var6, false, MINECRAFT_CLIENT.player.horizontalCollision));
                     this.method_72();
                  } else {
                     if (this.conditionsSetting.isSelected(field_9К) && !MINECRAFT_CLIENT.player.onGround() && MINECRAFT_CLIENT.player.hasEffect(MobEffects.SLOW_FALLING)) {
                        MINECRAFT_CLIENT.player
                           .connection
                           .send(new PosRot(var2, var4 - field_9I, var6, MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot(), false, MINECRAFT_CLIENT.player.horizontalCollision));
                        this.method_72();
                     }
                  }
               }
            }
         }
      }
   }
}
