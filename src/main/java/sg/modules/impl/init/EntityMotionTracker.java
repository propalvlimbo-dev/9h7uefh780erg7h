package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class EntityMotionTracker {
   private int АН;
   private int Ан;
   private int АЦ;
   private int Ач;
   @SerializedName("7")
   private Vec3 field_7;
   private Vec3 Р;
   @SerializedName("3")
   private Vec3 field_3;
   @SerializedName("4Ч")
   private float field_4Ч;
   @SerializedName("4Л")
   private float field_4Л;
   @SerializedName("4诶")
   private float field_4诶;
   @SerializedName("41")
   private float field_41;
   private static final int Я西Г = Integer.MIN_VALUE;
   private static final double Я西л = 256.0;
   private static final double Я西э = 8.0;
   private static final double Я西ю = 1.0E-8;
   private static final double Я西Щ = 8.0;
   private static final double Я西у = 0.6;
   private static final double Я西2 = 1.0E-8;
   private static final double Я西ф = 1.0E-4;
   private static final double Я西и = 1.0E-4;
   private static final float Я西9 = -15.0F;
   private static final float Я西о = 15.0F;
   private static final float Я西O = -10.0F;
   private static final float Я西7 = 10.0F;
   private static final double Я西Р = 1.0E-8;
   private static final int Я西3 = Integer.MIN_VALUE;

   private static Vec3 method_2(Vec3 var0, double var1) {
      double var3 = var0.length();
      if (!Double.isFinite(var3)) {
         return Vec3.ZERO;
      } else {
         return var3 > var1 ? var0.scale(var1 / var3) : var0;
      }
   }

   public EntityMotionTracker() {
      this.АН = Я西Г;
      this.Ан = -1;
      this.АЦ = -1;
      this.Ач = -1;
      this.Р = Vec3.ZERO;
      this.field_3 = Vec3.ZERO;
   }

   private EntityMotionTracker.Sample д(int var1) {
      return new EntityMotionTracker.Sample(
         this.field_7, this.Р, this.field_3, this.field_4诶, this.field_41, this.Р.lengthSqr() < Я西Р ? 0 : Math.clamp(var1 - this.АЦ, 0, 2)
      );
   }

   public EntityMotionTracker.Sample д(int var1, int var2, Vec3 var3, Vec3 var4, float var5, float var6) {
      if (var1 != this.АН || this.field_7 == null || var2 < this.Ан || var2 - this.Ан > 10 || var3.distanceToSqr(this.field_7) > Я西л) {
         this.l1();
         this.АН = var1;
         this.field_7 = var3;
         this.Р = method_2(var4, Я西э);
         this.АЦ = this.Ач = this.Ан = var2;
         this.field_4Ч = var5;
         this.field_4Л = var6;
         return this.д(var2);
      }

      if (var2 == this.Ан) {
         return this.д(var2);
      }

      Vec3 var7 = var3.subtract(this.field_7);
      if (var7.lengthSqr() > Я西ю) {
         int var8 = Math.max(1, var2 - this.АЦ);
         Vec3 var9 = method_2(var7.scale(1.0 / var8), Я西Щ);
         Vec3 var10 = var9.subtract(this.Р).scale(1.0 / var8);
         this.field_3 = method_2(var10, Я西у);
         this.Р = var9;
         this.field_7 = var3;
         this.АЦ = var2;
      } else {
         this.field_3 = Vec3.ZERO;
         if (var2 - this.АЦ >= 3) {
            this.Р = Vec3.ZERO;
         }

         if (this.Р.lengthSqr() < Я西2) {
            this.АЦ = var2;
         }
      }

      float var11 = Mth.wrapDegrees(var5 - this.field_4Ч);
      float var12 = var6 - this.field_4Л;
      if (!(Math.abs(var11) > Я西ф) && !(Math.abs(var12) > Я西и)) {
         if (var2 - this.Ач >= 3) {
            this.field_4诶 = this.field_41 = 0.0F;
         }

         if (this.field_4诶 == 0.0F && this.field_41 == 0.0F) {
            this.Ач = var2;
         }
      } else {
         int var13 = Math.max(1, var2 - this.Ач);
         this.field_4诶 = Mth.clamp(var11 / var13, Я西9, Я西о);
         this.field_41 = Mth.clamp(var12 / var13, Я西O, Я西7);
         this.field_4Ч = var5;
         this.field_4Л = var6;
         this.Ач = var2;
      }

      this.Ан = var2;
      return this.д(var2);
   }

   public void l1() {
      this.АН = Я西3;
      this.Ан = this.АЦ = this.Ач = -1;
      this.field_7 = null;
      this.Р = this.field_3 = Vec3.ZERO;
      this.field_4诶 = this.field_41 = 0.0F;
   }

   public record Sample(Vec3 position, Vec3 velocity, Vec3 acceleration, float yawRate, float pitchRate, int age) {
   }
}
