package sg.modules.impl.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import sg.ec.MinecraftAccess;

public final class CatPetMovement implements MinecraftAccess {
   private static final double с诶 = 28.0;
   private static final double с1 = 0.025;
   private static final int Вц = 8;
   private double сЬ;
   private double с衣;
   private double сш;
   private double сИ;
   private double сь;
   private double сж;
   private float Як;
   private float Яг;
   private boolean сШ;
   private static final double ЧУ = 28.0;
   private static final double ЧЗ = 0.025;
   private static final double ЧЕ = 1.12;
   private static final double ЧП = 0.012;
   private static final double Ч西 = 0.34;
   private static final float Чд = 0.28F;
   private static final float ЧБ = 0.18F;
   private static final float ЧС = 180.0F;
   private static final float Чз = 0.08F;
   private static final double Чц = 0.35;
   private static final double Чя = -0.42;
   private static final double Лс = 0.42;
   private static final double ЛВ = 0.45;
   private static final double ЛЯ = 0.001;
   private static final long ЛХ = 13L;
   private static final double Ле = 0.045;
   private static final double ЛА = 0.42;
   private static final double Л4 = 0.9;
   private static final double Лп = 0.78;
   private static final double ЛД = 0.55;
   private static final float ЛЪ = 180.0F;
   private static final float Лт = 360.0F;
   private static final float Ла = 540.0F;
   private static final float ЛЮ = 360.0F;
   private static final float ЛЧ = 180.0F;

   private Vec3 method_3(LivingEntity var1, float var2) {
      if (var1 != null) {
         return this.ъ(var1, var2);
      }

      float var3 = (float)Math.toRadians(MINECRAFT_CLIENT.player.getYRot());
      double var4 = -var2;
      double var6 = var2 * ЛВ;
      double var8 = MINECRAFT_CLIENT.player.getX() - Math.sin(var3) * var4 + Math.cos(var3) * var6;
      double var10 = MINECRAFT_CLIENT.player.getZ() + Math.cos(var3) * var4 + Math.sin(var3) * var6;
      return new Vec3(var8, this.l(var8, MINECRAFT_CLIENT.player.getY(), var10), var10);
   }

   private float З(float var1, float var2, float var3) {
      float var4 = ((var2 - var1) % Лт + Ла) % ЛЮ - ЛЧ;
      return var1 + var4 * var3;
   }

   private void l(Vec3 var1, LivingEntity var2) {
      this.сЬ = var1.x;
      this.с衣 = var1.y;
      this.сш = var1.z;
      this.Як = var2 != null ? this.ъ(var2.getX() - this.сЬ, var2.getZ() - this.сш) : ЛЪ - MINECRAFT_CLIENT.player.getYRot();
      this.сИ = this.сЬ;
      this.сь = this.с衣;
      this.сж = this.сш;
      this.Яг = this.Як;
   }

   private Vec3 ъ(LivingEntity var1, float var2) {
      double var3 = var1.getX() - MINECRAFT_CLIENT.player.getX();
      double var5 = var1.getZ() - MINECRAFT_CLIENT.player.getZ();
      double var7 = Math.sqrt(var3 * var3 + var5 * var5);
      if (var7 < ЛЯ) {
         float var9 = (float)Math.toRadians(MINECRAFT_CLIENT.player.getYRot());
         var3 = -Math.sin(var9);
         var5 = Math.cos(var9);
         var7 = 1.0;
      }

      double var23 = var3 / var7;
      double var11 = var5 / var7;
      double var13 = (MINECRAFT_CLIENT.level.getGameTime() + var1.getId() * ЛХ) * Ле;
      double var15 = Math.sin(var13) * ЛА;
      double var17 = Math.max(Л4, var2 * Лп + var1.getBbWidth() * ЛД);
      double var19 = var1.getX() - var23 * var17 - var11 * var15;
      double var21 = var1.getZ() - var11 * var17 + var23 * var15;
      return new Vec3(var19, this.l(var19, var1.getY(), var21), var21);
   }

   public double т(float var1) {
      return Mth.lerp(var1, this.сж, this.сш);
   }

   public double в(float var1) {
      return Mth.lerp(var1, this.сь, this.с衣);
   }

   private float ъ(double var1, double var3) {
      return (float)Math.toDegrees(Math.atan2(-var1, -var3));
   }

   public double ф(float var1) {
      return Mth.lerp(var1, this.сИ, this.сЬ);
   }

   private double l(double var1, double var3, double var5) {
      int var7 = Mth.floor(var3);

      for (int var8 = 0; var8 >= -8; var8--) {
         BlockPos var9 = BlockPos.containing(var1, var7 + var8, var5);
         BlockState var10 = MINECRAFT_CLIENT.level.getBlockState(var9);
         VoxelShape var11 = var10.getCollisionShape(MINECRAFT_CLIENT.level, var9);
         if (!var11.isEmpty()) {
            return var9.getY() + var11.max(Axis.Y);
         }
      }

      return var3;
   }

   public boolean lЯ() {
      return this.сШ;
   }

   public float Т(float var1) {
      return this.З(this.Яг, this.Як, var1);
   }

   public double l(LivingEntity var1, float var2, float var3) {
      Vec3 var4 = this.method_3(var1, var3);
      if (!this.сШ) {
         this.l(var4, var1);
         this.сШ = true;
         return 0.0;
      }

      this.сИ = this.сЬ;
      this.сь = this.с衣;
      this.сж = this.сш;
      this.Яг = this.Як;
      double var5 = var4.x - this.сЬ;
      double var7 = var4.y - this.с衣;
      double var9 = var4.z - this.сш;
      double var11 = Math.sqrt(var5 * var5 + var9 * var9);
      if (Math.sqrt(var5 * var5 + var7 * var7 + var9 * var9) > ЧУ) {
         this.l(var4, var1);
         return 0.0;
      }

      if (var11 > ЧЗ) {
         double var13 = var2 * (var1 != null ? ЧЕ : 1.0);
         double var15 = Math.min(var11, Math.min(var13, Math.max(ЧП, var11 * Ч西)));
         this.сЬ += var5 / var11 * var15;
         this.сш += var9 / var11 * var15;
         this.Як = this.З(this.Як, this.ъ(var5, var9), Чд);
      } else if (var1 != null) {
         this.Як = this.З(this.Як, this.ъ(var1.getX() - this.сЬ, var1.getZ() - this.сш), ЧБ);
      } else {
         this.Як = this.З(this.Як, ЧС - MINECRAFT_CLIENT.player.getYRot(), Чз);
      }

      this.с衣 = this.с衣 + Mth.clamp(var7 * Чц, Чя, Лс);
      return Math.sqrt((this.сЬ - this.сИ) * (this.сЬ - this.сИ) + (this.сш - this.сж) * (this.сш - this.сж));
   }

   public void method_3衣() {
      this.сШ = false;
   }
}
