package sg.modules.impl.init;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerMovementEvent;
import sg.ec.MinecraftAccess;
import sg.client.RoseClient;

public class StrafeSpeedCalculator implements MinecraftAccess {
   private double Яl;
   private double Я8;
   private boolean ХЙ;
   private boolean Хк;
   private int пщ;
   private int пГ;
   private static final float с诶6 = 0.88F;
   private static final float с诶_ = 0.91F;
   private static final float с诶Ш = 0.16277136F;
   private static final float с诶ы = 0.2F;
   private static final float с诶й = 0.0255F;
   private static final double с诶Ж = 0.25;
   private static final double с诶м = 0.08;
   private static final double с诶К = 0.055;
   private static final double с诶I = 0.043;
   private static final double с诶Т = 0.3;
   private static final double с诶О = 0.019;
   private static final double с诶Ф = 0.25;
   private static final double с诶Й = 0.001;
   private static final double с诶к = 0.002;
   private static final float с诶г = 1.3F;
   private static final float с诶0 = 0.91F;

   public boolean 弟Б() {
      return this.Хк;
   }

   public void п(double var1) {
      this.Яl = var1 * this.Я8;
   }

   public void л(double var1) {
      this.Яl = var1;
   }

   public boolean 弟И() {
      return this.ХЙ;
   }

   public void уз(boolean var1) {
      this.Хк = var1;
   }

   private float Э(LocalPlayer var1) {
      boolean var2 = var1.isSprinting();
      var1.setSprinting(false);
      float var3 = var1.getSpeed() * с诶г;
      var1.setSprinting(var2);
      return var3;
   }

   private float е(LocalPlayer var1) {
      BlockPos var2 = BlockPos.containing(var1.getX(), var1.getBoundingBox().minY - 1.0, var1.getZ());
      return var1.level().getBlockState(var2).getBlock().getFriction() * с诶0;
   }

   public double Э(PlayerMovementEvent var1, boolean var2, boolean var3, boolean var4, float var5) {
      boolean var6 = MINECRAFT_CLIENT.player.onGround();
      Vec3 var7 = var1.getMovement();
      boolean var8 = var6 && var7.y <= 0.0;
      boolean var9 = var7.y > 0.0;
      float var10 = this.Э(MINECRAFT_CLIENT.player);
      float var11 = this.е(MINECRAFT_CLIENT.player);
      float var12 = MINECRAFT_CLIENT.player.hasEffect(MobEffects.JUMP_BOOST) && MINECRAFT_CLIENT.player.isUsingItem() ? с诶6 : с诶_;
      if (var6) {
         var12 = var11;
      }

      float var13 = с诶Ш / (var12 * var12 * var12);
      float var14;
      if (var6) {
         var14 = var10 * var13;
         if (var9) {
            var14 += с诶ы;
         }
      } else {
         var14 = var2 && var3 && (var4 || MINECRAFT_CLIENT.options.keyJump.isDown()) ? var5 : с诶й;
      }

      boolean var15 = false;
      double var16 = this.Яl + var14;
      double var18 = 0.0;
      if (MINECRAFT_CLIENT.player.isUsingItem() && !var9) {
         double var20 = this.Яl + var14 * с诶Ж;
         double var22 = var7.y;
         if (var22 != 0.0 && Math.abs(var22) < с诶м) {
            var20 += с诶К;
         }

         if (var16 > (var18 = Math.max(с诶I, var20))) {
            var15 = true;
            this.пГ++;
         } else {
            this.пГ = Math.max(this.пГ - 1, 0);
         }
      } else {
         this.пГ = 0;
      }

      if (this.пГ > 3) {
         var16 = var18 - (MINECRAFT_CLIENT.player.hasEffect(MobEffects.JUMP_BOOST) && MINECRAFT_CLIENT.player.isUsingItem() ? с诶Т : с诶О);
      } else {
         var16 = Math.max(var15 ? 0.0 : с诶Ф, var16) - (this.пщ++ % 2 == 0 ? с诶Й : с诶к);
      }

      this.Я8 = var12;
      if (!var8 && !var6) {
         this.ХЙ = true;
      }

      if (!var6 && !var8) {
         this.Хк = !RoseClient.getInstance().getControllers().getServerPlayers().method_96();
      }

      if (var8 && var6) {
         this.Хк = false;
      }

      return var16;
   }
}
