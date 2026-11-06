package sg.ec;

import lombok.Generated;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import sg.mixinbridge.EntityWebStateAccess;
import sg.modules.impl.combat.ElytraTarget;
import sg.client.RoseClient;
import sg.render.pipeline.AimRaycast;
import sg.rotation.AimGeometryMath;

public final class AttackReadiness implements MinecraftAccess {
   private static int Х = 0;
   private static final float Х诶К = 6.0F;
   private static final String Х诶I = "Перегнал";
   private static final float Х诶Т = 1.5F;
   private static final float Х诶О = 0.92F;
   private static final float Х诶Ф = 6.0F;
   private static final String Х诶Й = "Перегнал";
   private static final double Х诶к = 2.0;
   private static final float Х诶г = 1.5F;
   private static final float Х诶0 = 0.92F;
   private static final double Х诶х = -1.0E-5;
   private static final double Х诶У = 0.08;
   private static final double Х诶З = 0.98;
   private static final double Х诶Е = 0.99;
   private static final double Х诶П = 0.98;
   private static final double Х诶西 = 0.99;
   private static final String Х诶д = "This is a utility class and cannot be instantiated";

   public static boolean Х(LivingEntity var0, boolean var1, boolean var2, boolean var3, boolean var4, float var5, boolean var6, boolean var7) {
      ElytraTarget var8 = RoseClient.getInstance().getServices().getModules().getElytraTarget();
      if (RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled() || RoseClient.getInstance().getServices().getModules().getAirStuck().isEnabled()) {
         var5 = Х诶К;
      }

      boolean var9 = var4 && var0.isFallFlying() && var8.И_f1816549().isSelected(Х诶I) && AimGeometryMath.getEntityMovementPerSecond(var0) > var8.РР().getValue().floatValue()
         ? Math.sqrt(MINECRAFT_CLIENT.player.distanceToSqr(var8.К(var0, var0.position().add(0.0, var0.getBbHeight() / 2.0F, 0.0)))) <= var5
         : MINECRAFT_CLIENT.player.distanceTo(var0) <= var5;
      boolean var10 = !var7 || RoseClient.getInstance().getControllers().getClientTicks().getTickCount() >= Х;
      boolean var11 = var9;
      boolean var12 = MINECRAFT_CLIENT.player.getAttackStrengthScale(Х诶Т) > Х诶О;
      boolean var13 = MINECRAFT_CLIENT.player.isInWater() && !MINECRAFT_CLIENT.options.keyJump.isDown();
      boolean var14 = RoseClient.getInstance().getServices().getModules().getAirStuck().isEnabled()
         || RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled();
      boolean var15 = MINECRAFT_CLIENT.player.fallDistance > 0.0
         || ((EntityWebStateAccess)MINECRAFT_CLIENT.player).dimas$isInWeb()
         || !var1
         || var2 && MINECRAFT_CLIENT.player.onGround() && !MINECRAFT_CLIENT.options.keyJump.isDown();
      boolean var16 = MINECRAFT_CLIENT.player.hasEffect(MobEffects.LEVITATION) || MINECRAFT_CLIENT.player.hasEffect(MobEffects.BLINDNESS);
      boolean var17 = MINECRAFT_CLIENT.player.isInLava() || MINECRAFT_CLIENT.player.onClimbable() || MINECRAFT_CLIENT.player.getAbilities().flying;
      boolean var18 = !var6
         || AimRaycast.method_2(
            MINECRAFT_CLIENT.player.getViewYRot(MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true)),
            MINECRAFT_CLIENT.player.getViewXRot(MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true)),
            var5,
            var0,
            false
         );
      boolean var19 = var10
         && var11
         && var12
         && var18
         && (MINECRAFT_CLIENT.player.isEyeInFluid(FluidTags.WATER) || var15 || var17 || var16 || var13 || var14 || MINECRAFT_CLIENT.player.isFallFlying());
      if (var19 && var3) {
         Х = RoseClient.getInstance().getControllers().getClientTicks().getTickCount() + 10;
      }

      return var19;
   }

   @Generated
   private AttackReadiness() {
      throw new UnsupportedOperationException(Х诶д);
   }

   public static int Х(LivingEntity var0, boolean var1, boolean var2, boolean var3, float var4, boolean var5, int var6) {
      if (var0 != null && MINECRAFT_CLIENT.player != null && var6 >= 0) {
         ElytraTarget var7 = RoseClient.getInstance().getServices().getModules().getElytraTarget();
         boolean var8 = RoseClient.getInstance().getServices().getModules().getAirStuck().isEnabled()
            || RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled();
         if (var8) {
            var4 = Х诶Ф;
         }

         boolean var9 = MINECRAFT_CLIENT.player.hasEffect(MobEffects.LEVITATION) || MINECRAFT_CLIENT.player.hasEffect(MobEffects.BLINDNESS);
         boolean var10 = MINECRAFT_CLIENT.player.isInLava() || MINECRAFT_CLIENT.player.onClimbable() || MINECRAFT_CLIENT.player.getAbilities().flying;
         boolean var11 = MINECRAFT_CLIENT.player.isInWater() && !MINECRAFT_CLIENT.options.keyJump.isDown();
         Vec3 var12 = MINECRAFT_CLIENT.player.position();
         Vec3 var13 = var0.position();
         Vec3 var14 = MINECRAFT_CLIENT.player.getDeltaMovement();
         Vec3 var15 = var0.getDeltaMovement();
         double var16 = var14.y;
         boolean var18 = MINECRAFT_CLIENT.player.onGround();
         float var19 = (float)MINECRAFT_CLIENT.player.fallDistance;

         for (int var20 = 0; var20 <= var6; var20++) {
            boolean var21 = var3 && var7.И_f1816549().isSelected(Х诶Й) && var0.isFallFlying() && AimGeometryMath.getEntityMovementPerSecond(var0) > var7.РР().getValue().floatValue();
            Vec3 var22 = var21 ? var7.К(var0, var13.add(0.0, var0.getBbHeight() / Х诶к, 0.0)) : var13;
            boolean var23 = var12.distanceTo(var22) <= var4;
            boolean var24 = !var5 || RoseClient.getInstance().getControllers().getClientTicks().getTickCount() + var20 >= Х;
            boolean var25 = MINECRAFT_CLIENT.player.getAttackStrengthScale(Х诶г + var20) > Х诶0;
            boolean var26 = var19 > 0.0F || !var18 && var16 < Х诶х;
            boolean var27 = var26 || !var1 || var2 && var18 && !MINECRAFT_CLIENT.options.keyJump.isDown();
            boolean var28 = ((EntityWebStateAccess)MINECRAFT_CLIENT.player).dimas$isInWeb() && var26;
            boolean var29 = MINECRAFT_CLIENT.player.isEyeInFluid(FluidTags.WATER) || var27 || var10 || var9 || var11 || var8 || var28 || MINECRAFT_CLIENT.player.isFallFlying();
            if (var23 && var24 && var25 && var29) {
               return var20;
            }

            if (var20 == var6) {
               break;
            }

            var12 = var12.add(var14);
            var13 = var13.add(var15);
            if (!var18) {
               var16 = (var16 - Х诶У) * Х诶З;
               var14 = new Vec3(var14.x, var16, var14.z);
               if (var16 < 0.0) {
                  var19 += (float)(-var16);
               }
            }

            var15 = var15.multiply(Х诶Е, Х诶П, Х诶西);
         }

         return -1;
      } else {
         return -1;
      }
   }
}
