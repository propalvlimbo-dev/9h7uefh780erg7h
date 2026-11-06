package sg.modules.impl.movement;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import sg.events.impl.PlayerTickEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.combat.Aura;
import sg.settings.ChoiceOption;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.FloatSetting;
import sg.modules.impl.init.MovementUtils;
import sg.settings.SingleChoiceSetting;

public class Strafe extends ClientModule {
   private final SingleChoiceSetting с1;
   private final SingleChoiceSetting сЬ;
   private final FloatSetting Х2;
   private final BooleanSetting Вд;
   private final BooleanSetting ВБ;
   private final BooleanSetting ВС;
   private final FloatSetting Хф;
   private final BooleanSetting Вз;
   private final FloatSetting Хи;
   private final BooleanSetting Вц;
   private final FloatSetting Х9;
   private final BooleanSetting Вя;
   private final FloatSetting Хо;
   private boolean ег;
   private long Хй;
   private boolean е0;
   private final EventListener<PlayerMovementSyncEvent> Х8;
   private final EventListener<PlayerTickEvent> Х5;
   private static final String сЖъ = "Strafe";
   private static final String сЖМ = "Быстрое перемещение";
   private static final String сЖН = "Тип";
   private static final String сЖн = "MetaHvH";
   private static final String сЖЦ = "Meta Strafe";
   private static final String сЖч = "Режим";
   private static final String сЖЭ = "Smart";
   private static final String сЖ必 = "Custom";
   private static final String сЖщ = "Скорость";
   private static final float сЖГ = 0.8F;
   private static final float сЖл = 0.3F;
   private static final float сЖэ = 1.5F;
   private static final float сЖю = 0.01F;
   private static final String сЖЩ = "Притягивание";
   private static final String сЖу = "Ускорение таймера";
   private static final String сЖ2 = "Буст с дамагом";
   private static final String сЖф = "Сила буста";
   private static final float сЖи = 0.01F;
   private static final String сЖ9 = "Буст от энтити";
   private static final String сЖо = "Сила энтити";
   private static final float сЖO = 0.5F;
   private static final float сЖ7 = 0.1F;
   private static final float сЖР = 0.01F;
   private static final String сЖ3 = "Предикт цели";
   private static final String сЖр = "Дистанция предикта";
   private static final float сЖ6 = 6.0F;
   private static final float сЖ_ = 0.1F;
   private static final String сЖШ = "Буст прыжка";
   private static final String сЖы = "Сила прыжка";
   private static final float сЖй = 1.5F;
   private static final float сЖЖ = 1.5F;
   private static final float сЖм = 0.05F;
   private static final String сЖК = "MetaHvH";
   private static final float сЖI = 0.19F;
   private static final float сЖТ = 0.25F;
   private static final float сЖО = 0.37F;
   private static final float сЖФ = 0.46F;
   private static final float сЖЙ = 0.7F;
   private static final float сЖк = 0.75F;
   private static final float сЖг = 0.05F;
   private static final float сЖ0 = 0.1F;
   private static final String сЖх = "Meta Strafe";
   private static final String сЖУ = "Smart";
   private static final String сЖЗ = "Custom";
   private static final float сЖЕ = 0.2245F;
   private static final float сЖП = 0.0055F;
   private static final double сЖ西 = 0.004999F;
   private static final float сЖд = 1.03F;
   private static final double сЖБ = 0.1;
   private static final double сЖС = 0.08;
   private static final float сЖз = 0.2245F;
   private static final long сЖц = 50L;
   private static final float сЖя = 0.365F;
   private static final float смс = 1.65F;
   private static final float смВ = 1.41F;
   private static final float смЯ = 1.21F;
   private static final float смХ = 0.66F;
   private static final String сме = "Шар CHAMPION";
   private static final float смА = 1.05F;
   private static final String см4 = "Шар KING";
   private static final float смп = 1.1F;
   private static final String смД = "Шар GOD";
   private static final float смЪ = 1.15F;
   private static final String смт = "Шар YT";
   private static final String сма = "Шар HELPER";
   private static final String смЮ = "Шар Скорости";
   private static final float смЧ = 1.2F;
   private static final String смЛ = "Шлем GLADIATOR";
   private static final String см诶 = "Шлем Мага";
   private static final float см1 = 1.15F;
   private static final float смЬ = -45.0F;
   private static final float см衣 = 45.0F;
   private static final float смш = 45.0F;
   private static final float смИ = -45.0F;
   private static final float смь = -1.0F;
   private static final float смж = 90.0F;
   private static final float см弟 = -45.0F;
   private static final float смб = 45.0F;
   private static final float смЫ = 45.0F;
   private static final float смl = -45.0F;
   private static final float см8 = -1.0F;
   private static final float см5 = 90.0F;
   private static final double смв = 2.0;
   private static final double смъ = 0.199;
   private static final double смМ = 0.199;
   private static final float смН = 90.0F;
   private static final double смн = 0.3;
   private static final double смЦ = 0.3;
   private static final double смч = 0.3;
   private static final double смЭ = 0.3;
   private static final double см必 = 0.3;
   private static final double смщ = 0.1;
   private static final double смГ = 0.3;
   private static final double смл = 0.3;
   private static final double смэ = 1.1;
   private static final double смю = 0.3;
   private static final String смЩ = "Meta Strafe";
   private static final String сму = "Meta Strafe";
   private static final String см2 = "Meta Strafe";
   private static final String смф = "Meta Strafe";
   private static final String сми = "Meta Strafe";
   private static final String см9 = "Meta Strafe";
   private static final String смо = "Meta Strafe";
   private static final String смO = "Meta Strafe";
   private static final String см7 = "Meta Strafe";
   private static final String смР = "Meta Strafe";
   private static final String см3 = "Meta Strafe";
   private static final String смр = "Custom";
   private static final String см6 = "Meta Strafe";

   @Override
   public void onDisable() {
      ClientTimerSpeed.Оэ(1.0F);
      this.ег = false;
      this.Хй = 0L;
      this.е0 = true;
      super.onDisable();
   }

   private double[] У(double var1) {
      Vec2 var3 = MINECRAFT_CLIENT.player.input.getMoveVector();
      float var4 = var3.y;
      float var5 = var3.x;
      float var6 = this.УБ();
      if (var4 != 0.0F) {
         if (var5 > 0.0F) {
            var6 += var4 > 0.0F ? см弟 : смб;
         } else if (var5 < 0.0F) {
            var6 += var4 > 0.0F ? смЫ : смl;
         }

         var5 = 0.0F;
         if (var4 > 0.0F) {
            var4 = 1.0F;
         } else if (var4 < 0.0F) {
            var4 = см8;
         }
      }

      double var7 = Math.toRadians(var6 + см5);
      double var9 = Math.sin(var7);
      double var11 = Math.cos(var7);
      return new double[]{var4 * var1 * var11 + var5 * var1 * var9, var4 * var1 * var9 - var5 * var1 * var11};
   }

   private float УБ() {
      Aura var1 = this.С();
      return this.Вц.getValue() && var1 != null && var1.isEnabled() && var1.Ж_95bedd6e() != null ? this.ь(var1.Ж_95bedd6e()) : MINECRAFT_CLIENT.player.getYRot();
   }

   private void гв(PlayerTickEvent var1) {
      if (this.с1.isSelected(сЖх) && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.сЬ.isSelected(сЖУ)) {
            this.lж();
         }

         if (this.сЬ.isSelected(сЖЗ)) {
            if (!MovementUtils.hasMovementInput() || MINECRAFT_CLIENT.player.isInWater() || MINECRAFT_CLIENT.player.isFallFlying()) {
               this.ег = MINECRAFT_CLIENT.player.onGround();
               this.Хй = 0L;
               this.е0 = true;
               if (this.ВБ.getValue()) {
                  ClientTimerSpeed.Оэ(1.0F);
               }

               return;
            }

            float var2 = this.Х2.getValue() - сЖЕ;
            float var3 = this.Х2.getValue() - сЖП;
            this.д(var2, var3);
         }

         LivingEntity var8 = this.А_95bedd6e();
         if (!MINECRAFT_CLIENT.player.horizontalCollision && !this.це() && MINECRAFT_CLIENT.player.getEffect(MobEffects.SLOWNESS) == null && this.Вд.getValue() && var8 == null && !this.цП()) {
            Vec3 var9 = MINECRAFT_CLIENT.player.getDeltaMovement();
            MINECRAFT_CLIENT.player.setDeltaMovement(var9.x, var9.y - сЖ西, var9.z);
         }

         if (MINECRAFT_CLIENT.player.getDeltaMovement().y < 0.0 && this.ВБ.getValue() && var8 == null && !this.цП()) {
            ClientTimerSpeed.Оэ(сЖд);
         } else if (this.ВБ.getValue()) {
            ClientTimerSpeed.Оэ(1.0F);
         }

         if (this.Вз.getValue() && !this.цП()) {
            AABB var10 = MINECRAFT_CLIENT.player.getBoundingBox().inflate(сЖБ);
            boolean var4 = false;

            for (Entity var6 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
               if (var6 != MINECRAFT_CLIENT.player
                  && !(var6 instanceof ArmorStand)
                  && (var6 instanceof LivingEntity || var6 instanceof Boat)
                  && var10.intersects(var6.getBoundingBox())) {
                  var4 = true;
                  break;
               }
            }

            double[] var11 = this.У(сЖС * (var4 ? 1 : 0));
            double var12 = this.Хи.getValue().floatValue();
            MINECRAFT_CLIENT.player.push(var11[0] * var12, 0.0, var11[1] * var12);
         }
      }
   }

   public Strafe() {
      super(сЖъ, сЖМ, ModuleCategory.MOVEMENT);
      this.с1 = new SingleChoiceSetting(сЖН, new ChoiceOption(сЖн), new ChoiceOption(сЖЦ));
      this.сЬ = new SingleChoiceSetting(сЖч, new ChoiceOption(сЖЭ), new ChoiceOption(сЖ必)).visibleWhen(() -> this.с1.isSelected(см6));
      this.Х2 = new FloatSetting(сЖщ, сЖГ, сЖл, сЖэ).withStep(сЖю).visibleWhen(() -> this.с1.isSelected(см3) && this.сЬ.isSelected(смр));
      this.Вд = new BooleanSetting(сЖЩ, false).visibleWhen(() -> this.с1.isSelected(смР));
      this.ВБ = new BooleanSetting(сЖу, false).visibleWhen(() -> this.с1.isSelected(см7));
      this.ВС = new BooleanSetting(сЖ2, false).visibleWhen(() -> this.с1.isSelected(смO));
      this.Хф = new FloatSetting(сЖф, 1.0F, 1.0F, 2.0F).withStep(сЖи).visibleWhen(() -> this.с1.isSelected(смо) && this.ВС.getValue());
      this.Вз = new BooleanSetting(сЖ9, false).visibleWhen(() -> this.с1.isSelected(см9));
      this.Хи = new FloatSetting(сЖо, сЖO, сЖ7, 1.0F).withStep(сЖР).visibleWhen(() -> this.с1.isSelected(сми) && this.Вз.getValue());
      this.Вц = new BooleanSetting(сЖ3, false).visibleWhen(() -> this.с1.isSelected(смф));
      this.Х9 = new FloatSetting(сЖр, 1.0F, 1.0F, сЖ6).withStep(сЖ_).visibleWhen(() -> this.с1.isSelected(см2) && this.Вц.getValue());
      this.Вя = new BooleanSetting(сЖШ, true).visibleWhen(() -> this.с1.isSelected(сму));
      this.Хо = new FloatSetting(сЖы, сЖй, 1.0F, сЖЖ).withStep(сЖм).visibleWhen(() -> this.с1.isSelected(смЩ) && this.Вя.getValue());
      this.е0 = true;
      this.Х8 = this::Р;
      this.Х5 = this::гв;
      this.addSettings(this.с1, this.сЬ, this.Х2, this.Вд, this.ВБ, this.ВС, this.Хф, this.Вз, this.Хи, this.Вц, this.Х9, this.Вя, this.Хо);
   }

   private void г必(float var1) {
      Vec2 var2 = MINECRAFT_CLIENT.player.input.getMoveVector();
      float var3 = var2.y;
      float var4 = var2.x;
      float var5 = this.УБ();
      if (var3 == 0.0F && var4 == 0.0F) {
         MINECRAFT_CLIENT.player.setDeltaMovement(0.0, MINECRAFT_CLIENT.player.getDeltaMovement().y, 0.0);
      } else {
         if (var3 != 0.0F) {
            if (var4 > 0.0F) {
               var5 += var3 > 0.0F ? смЬ : см衣;
            } else if (var4 < 0.0F) {
               var5 += var3 > 0.0F ? смш : смИ;
            }

            var4 = 0.0F;
            var3 = var3 > 0.0F ? 1.0F : смь;
         }

         double var6 = Math.toRadians(var5 + смж);
         double var8 = var3 * var1 * Math.cos(var6) + var4 * var1 * Math.sin(var6);
         double var10 = var3 * var1 * Math.sin(var6) - var4 * var1 * Math.cos(var6);
         MINECRAFT_CLIENT.player.setDeltaMovement(var8, MINECRAFT_CLIENT.player.getDeltaMovement().y, var10);
      }
   }

   private float ь(LivingEntity var1) {
      Vec3 var2 = var1.position();
      double var3 = var2.x - MINECRAFT_CLIENT.player.getX();
      double var5 = var2.y + var1.getEyeHeight(var1.getPose()) / смв - (MINECRAFT_CLIENT.player.getY() + MINECRAFT_CLIENT.player.getEyeHeight(MINECRAFT_CLIENT.player.getPose()));
      double var7 = var2.z - MINECRAFT_CLIENT.player.getZ();
      Vec3 var9 = new Vec3(var3, var5, var7);
      if (!MINECRAFT_CLIENT.player.isFallFlying()) {
         boolean var10 = MovementUtils.hasMovementInput();
         boolean var11 = Math.abs(var2.x - var1.xo) > смъ || Math.abs(var2.z - var1.zo) > смМ;
         if (var10 && var11) {
            float var12 = var1.getYRot();
            float var13 = var1.getXRot();
            double var14 = -Math.sin(Math.toRadians(var12)) * Math.cos(Math.toRadians(var13));
            double var16 = -Math.sin(Math.toRadians(var13));
            double var18 = Math.cos(Math.toRadians(var12)) * Math.cos(Math.toRadians(var13));
            var9 = var9.add(new Vec3(var14, var16, var18).scale(this.Х9.getValue().floatValue()));
         }
      }

      return (float)Math.toDegrees(Math.atan2(var9.z, var9.x)) - смН;
   }

   private void д(float var1, float var2) {
      if (this.ВС.getValue() && MINECRAFT_CLIENT.player.hurtTime > 0) {
         float var3 = this.Хф.getValue();
         var1 *= var3;
         var2 *= var3;
      }

      boolean var8 = MINECRAFT_CLIENT.player.onGround();
      boolean var4 = MINECRAFT_CLIENT.options.keyJump.isDown();
      long var5 = System.currentTimeMillis();
      if (var8 && var4 && !this.ег) {
         if (this.е0) {
            this.е0 = false;
         } else {
            this.Хй = var5 + сЖц;
         }
      }

      if (var8 && !var4) {
         this.е0 = true;
      }

      if (this.Вя.getValue() && var5 < this.Хй) {
         float var7 = this.Хо.getValue();
         var2 *= var7;
         var1 *= var7;
      }

      if (var4 && !this.цП()) {
         this.г必(var2);
      }

      if (var8 && !this.цП()) {
         this.г必(var1);
      }

      this.ег = var8 && var4;
   }

   private LivingEntity А_95bedd6e() {
      Aura var1 = this.С();
      return var1 != null && var1.isEnabled() ? var1.Ж_95bedd6e() : null;
   }

   public void Р(PlayerMovementSyncEvent var1) {
      if (MINECRAFT_CLIENT.player != null && this.с1.isSelected(сЖК)) {
         if (!MINECRAFT_CLIENT.player.isFallFlying() && (!MINECRAFT_CLIENT.player.isInWater() || !MINECRAFT_CLIENT.player.isSwimming()) && MovementUtils.hasMovementInput()) {
            float var2 = сЖI;
            MobEffectInstance var3 = MINECRAFT_CLIENT.player.getEffect(MobEffects.SPEED);
            if (var3 != null) {
               int var4 = var3.getAmplifier();
               switch (var4) {
                  case 0:
                     var2 = сЖТ;
                     break;
                  case 1:
                     var2 = сЖО;
                     break;
                  case 2:
                     var2 = сЖФ;
                     break;
                  case 3:
                     var2 = сЖЙ;
                     break;
                  default:
                     var2 = сЖк + (var4 - 3) * сЖг;
               }
            }

            if (MINECRAFT_CLIENT.options.keyJump.isDown()) {
               var2 += сЖ0;
            }

            MovementUtils.setHorizontalSpeed(var2);
         }
      }
   }

   private void lж() {
      if (MovementUtils.hasMovementInput() && !MINECRAFT_CLIENT.player.isInWater() && !MINECRAFT_CLIENT.player.isFallFlying()) {
         MobEffectInstance var1 = MINECRAFT_CLIENT.player.getEffect(MobEffects.SPEED);
         MobEffectInstance var2 = MINECRAFT_CLIENT.player.getEffect(MobEffects.SLOWNESS);
         String var3 = MINECRAFT_CLIENT.player.getOffhandItem().getHoverName().getString();
         ItemStack var4 = MINECRAFT_CLIENT.player.getItemBySlot(EquipmentSlot.HEAD);
         String var5 = var4.getHoverName().getString();
         float var6 = this.а(var1, var2, var3, var5);
         this.д(var6 - сЖз, var6);
      } else {
         this.ег = MINECRAFT_CLIENT.player.onGround();
         this.Хй = 0L;
         this.е0 = true;
      }
   }

   private boolean цП() {
      return MINECRAFT_CLIENT.player.isInWater() || MINECRAFT_CLIENT.player.isInLava() || MINECRAFT_CLIENT.player.isFallFlying();
   }

   private boolean це() {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         double var1 = MINECRAFT_CLIENT.player.getX();
         double var3 = MINECRAFT_CLIENT.player.getY();
         double var5 = MINECRAFT_CLIENT.player.getZ();
         double var7 = MINECRAFT_CLIENT.player.getBbHeight();
         AABB var9 = new AABB(var1 - смн, var3, var5 - смЦ, var1 + смч, var3 + var7, var5 + смЭ);
         AABB var10 = new AABB(var1 - см必, var3 + var7 + смщ, var5 - смГ, var1 + смл, var3 + var7 + смэ, var5 + смю);
         boolean var11 = true;

         for (VoxelShape var13 : MINECRAFT_CLIENT.level.getBlockCollisions(MINECRAFT_CLIENT.player, var9)) {
            if (!var13.isEmpty()) {
               var11 = false;
               break;
            }
         }

         boolean var15 = false;

         for (VoxelShape var14 : MINECRAFT_CLIENT.level.getBlockCollisions(MINECRAFT_CLIENT.player, var10)) {
            if (!var14.isEmpty()) {
               var15 = true;
               break;
            }
         }

         return var11 && var15;
      } else {
         return false;
      }
   }

   private float а(MobEffectInstance var1, MobEffectInstance var2, String var3, String var4) {
      float var5 = сЖя;
      if (var1 != null) {
         int var6 = var1.getAmplifier();
         if (var6 == 2) {
            var5 *= смс;
         } else if (var6 == 1) {
            var5 *= смВ;
         } else if (var6 == 0) {
            var5 *= смЯ;
         }
      }

      if (var2 != null) {
         var5 *= смХ;
      }

      if (var3 != null) {
         if (var3.contains(сме)) {
            var5 *= смА;
         }

         if (var3.contains(см4)) {
            var5 *= смп;
         }

         if (var3.contains(смД)) {
            var5 *= смЪ;
         }

         if (var3.contains(смт) || var3.contains(сма) || var3.contains(смЮ)) {
            var5 *= смЧ;
         }
      }

      if (var4 != null && (var4.contains(смЛ) || var4.contains(см诶))) {
         var5 *= см1;
      }

      return var5;
   }

   private Aura С() {
      return RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null
         ? RoseClient.getInstance().getServices().getModules().getAura()
         : null;
   }
}
