package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public final class MotionBlurFrameInterpolator {
   public static final long ХЩ = 16666666L;
   private static final long Ху = 250000000L;
   private static final int Дь = 128;
   @SerializedName("В")
   private final MotionBlurFrameInterpolator.FrameSample[] В_0fab1828 = new MotionBlurFrameInterpolator.FrameSample[128];
   @SerializedName("В")
   private final MotionBlurFrameInterpolator.FrameSample В_d7aea33e = new MotionBlurFrameInterpolator.FrameSample();
   private final Matrix4f л = new Matrix4f();
   private int Дж;
   private int Д弟;
   private static final long Х诶Б = 250000000L;
   private static final double Х诶С = 64.0;
   private static final long Х诶з = 16666666L;
   private static final float Х诶ц = 180.0F;
   private static final float Х诶я = 360.0F;
   private static final float Х1с = 360.0F;

   public void ВШ() {
      this.Дж = 0;
      this.Д弟 = 0;
   }

   public Matrix4fc Х() {
      return this.л;
   }

   public Matrix4fc Т_72a92728() {
      return this.В_d7aea33e.ш;
   }

   public float ХЕ() {
      return this.В_d7aea33e.е7;
   }

   public float Хл() {
      return this.В_d7aea33e.еР;
   }

   private MotionBlurFrameInterpolator.FrameSample Т(int var1) {
      return this.В_0fab1828[(this.Дж + var1) % 128];
   }

   public Vector3dc Т_afb6a793() {
      return this.В_d7aea33e.В_30836749;
   }

   public boolean Т(long var1, Matrix4fc var3, Matrix4fc var4, double var5, double var7, double var9, float var11, float var12) {
      if (var3.isFinite()
         && var4.isFinite()
         && Double.isFinite(var5)
         && Double.isFinite(var7)
         && Double.isFinite(var9)
         && Float.isFinite(var11)
         && Float.isFinite(var12)) {
         if (this.Д弟 > 0) {
            MotionBlurFrameInterpolator.FrameSample var13 = this.Т(this.Д弟 - 1);
            if (var1 <= var13.Вш || var1 - var13.Вш > Х诶Б || var13.В_30836749.distanceSquared(var5, var7, var9) > Х诶С) {
               this.ВШ();
            }
         }

         int var22 = (this.Дж + this.Д弟) % 128;
         MotionBlurFrameInterpolator.FrameSample var14 = this.В_0fab1828[var22];
         if (var14 == null) {
            this.В_0fab1828[var22] = var14 = new MotionBlurFrameInterpolator.FrameSample();
         }

         var14.Вш = var1;
         var14.ш.set(var3);
         var4.getNormalizedRotation(var14.В_7aa83f03);
         var14.В_30836749.set(var5, var7, var9);
         var14.е7 = var11;
         var14.еР = var12;
         if (this.Д弟 == 128) {
            this.Дж = (this.Дж + 1) % 128;
         } else {
            this.Д弟++;
         }

         if (this.Д弟 < 2) {
            return false;
         }

         long var15;
         for (var15 = var1 - Х诶з; this.Д弟 > 2 && this.Т(1).Вш <= var15; this.Д弟--) {
            this.Дж = (this.Дж + 1) % 128;
         }

         MotionBlurFrameInterpolator.FrameSample var17 = this.Т(0);
         MotionBlurFrameInterpolator.FrameSample var18 = this.Т(1);
         double var19 = Math.clamp((double)(var15 - var17.Вш) / (var18.Вш - var17.Вш), 0.0, 1.0);
         var17.ш.lerp(var18.ш, (float)var19, this.В_d7aea33e.ш);
         var17.В_7aa83f03.slerp(var18.В_7aa83f03, var19, this.В_d7aea33e.В_7aa83f03).normalize();
         this.В_d7aea33e.В_7aa83f03.get(this.л);
         var17.В_30836749.lerp(var18.В_30836749, var19, this.В_d7aea33e.В_30836749);
         float var21 = var18.е7 - var17.е7;
         var21 -= (float)Math.floor((var21 + Х诶ц) / Х诶я) * Х1с;
         this.В_d7aea33e.е7 = var17.е7 + var21 * (float)var19;
         this.В_d7aea33e.еР = var17.еР + (var18.еР - var17.еР) * (float)var19;
         return true;
      } else {
         this.ВШ();
         return false;
      }
   }

   private static final class FrameSample {
      private long Вш;
      private final Matrix4f ш = new Matrix4f();
      @SerializedName("В")
      private final Quaterniond В_7aa83f03 = new Quaterniond();
      @SerializedName("В")
      private final Vector3d В_30836749 = new Vector3d();
      private float е7;
      private float еР;
   }
}
