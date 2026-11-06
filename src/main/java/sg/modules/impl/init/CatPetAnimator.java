package sg.modules.impl.init;

import java.util.Random;
import net.minecraft.util.Mth;

public final class CatPetAnimator {
   private static final float тД = (float) (Math.PI * 2);
   private static final int Ду = 2;
   private static final int Д2 = 3;
   private static final int Дф = 6;
   private static final int Ди = 60;
   private static final int Д9 = 8;
   private static final int До = 100;
   private static final float тЪ = 0.26F;
   private static final float тт = 0.2F;
   private static final float та = 3.2F;
   private static final float тЮ = 24.0F;
   private final Random Ю = new Random();
   private float тЧ;
   private float тЛ;
   private float т诶;
   private float т1;
   private float тЬ;
   private float т衣;
   private float тш;
   private float тИ;
   private float ть;
   private float тж;
   private float т弟;
   private float тб;
   private float тЫ;
   private float тl;
   private float т8;
   private float т5;
   private int ДO = 60;
   private int Д7 = -1;
   private int ДР = 100;
   private int Д3 = -1;
   private static final float ЯьЛ = 0.8F;
   private static final float Яь诶 = 0.012F;
   private static final double Яь1 = 0.003;
   private static final float ЯьЬ = 0.01F;
   private static final float Яь衣 = 0.22F;
   private static final float Яьш = 0.3F;
   private static final double ЯьИ = 9.0;
   private static final float Яьь = (float) (Math.PI * 2);
   private static final float Яьж = (float) (Math.PI * 2);
   private static final float Яь弟 = (float) (Math.PI * 2);
   private static final float Яьб = 0.085F;
   private static final float ЯьЫ = 0.13F;
   private static final float Яьl = 0.22F;
   private static final float Яь8 = (float) (Math.PI * 2);
   private static final float Яь5 = (float) (Math.PI * 2);
   private static final float Яьв = (float) (Math.PI * 2);
   private static final float Яьъ = 6.0F;
   private static final float ЯьМ = 4.4F;
   private static final float ЯьН = 0.42F;
   private static final float Яьн = 0.26F;
   private static final float ЯьЦ = 0.2F;
   private static final float Яьч = 3.2F;
   private static final float ЯьЭ = -24.0F;
   private static final float Яь必 = 24.0F;
   private static final float Яьщ = 3.0F;
   private static final float ЯьГ = 8.0F;
   private static final float Яьл = (float) (Math.PI * 2);

   public float Вх(float var1) {
      return Mth.lerp(var1, this.тl, this.тЫ);
   }

   public float Вн(float var1) {
      return Mth.lerp(var1, this.тЛ, this.тЧ);
   }

   public void Ж必() {
      this.тЧ = 0.0F;
      this.тЛ = 0.0F;
      this.т诶 = 0.0F;
      this.т1 = 0.0F;
      this.тЬ = 0.0F;
      this.т衣 = 0.0F;
      this.тш = 0.0F;
      this.тИ = 0.0F;
      this.ть = 0.0F;
      this.тж = 0.0F;
      this.т弟 = 0.0F;
      this.тб = 0.0F;
      this.тЫ = 0.0F;
      this.тl = 0.0F;
      this.т8 = 0.0F;
      this.т5 = 0.0F;
      this.ДO = 60;
      this.Д7 = -1;
      this.ДР = 100;
      this.Д3 = -1;
   }

   private void ЖД() {
      float var1 = this.тЬ * this.тЬ * ЯьМ + Mth.sin(this.тЧ * 2.0F) * this.т诶 * ЯьН;
      float var2 = var1 - this.т5;
      this.т5 = var1;
      this.т8 = this.т8 + (-this.т弟 * Яьн - this.т8 * ЯьЦ - var2 * Яьч);
      this.т弟 = Mth.clamp(this.т弟 + this.т8, ЯьЭ, Яь必);
   }

   private void ЖO() {
      if (this.Д7 < 0) {
         if (--this.ДO <= 0) {
            this.Д7 = 0;
         }
      } else {
         this.Д7++;
         if (this.Д7 <= 2) {
            this.тш = this.Д7 / 2.0F;
         } else if (this.Д7 <= 3) {
            this.тш = 1.0F;
         } else {
            this.тш = Math.max(0.0F, 1.0F - (this.Д7 - 3) / Яьщ);
         }

         if (this.Д7 >= 6) {
            this.Д7 = -1;
            this.тш = 0.0F;
            this.ДO = 60 + this.Ю.nextInt(140);
         }
      }
   }

   private void Жв() {
      if (this.Д3 < 0) {
         if (--this.ДР <= 0) {
            this.Д3 = 0;
         }
      } else {
         this.Д3++;
         this.ть = Mth.sin(this.Д3 / ЯьГ * Яьл);
         if (this.Д3 >= 8) {
            this.Д3 = -1;
            this.ть = 0.0F;
            this.ДР = 100 + this.Ю.nextInt(180);
         }
      }
   }

   public void ЖГ() {
      this.тЛ = this.тЧ;
      this.т1 = this.т诶;
      this.т衣 = this.тЬ;
      this.тИ = this.тш;
      this.тж = this.ть;
      this.тб = this.т弟;
      this.тl = this.тЫ;
   }

   public float Вь(float var1) {
      return Mth.lerp(var1, this.тИ, this.тш);
   }

   public float Вт(float var1) {
      return Mth.lerp(var1, this.тж, this.ть);
   }

   public float ВЕ(float var1) {
      return Mth.lerp(var1, this.т衣, this.тЬ);
   }

   public float ВХ(float var1) {
      return Mth.lerp(var1, this.тб, this.т弟);
   }

   public float В弟(float var1) {
      return Mth.lerp(var1, this.т1, this.т诶);
   }

   public void Ж5() {
      this.тЬ = 1.0F;
      this.т8 = this.т8 - Яьъ;
   }

   public void с(double var1, float var3) {
      this.тЬ = this.тЬ * ЯьЛ;
      if (this.тЬ < Яь诶) {
         this.тЬ = 0.0F;
      }

      float var4 = var1 > Яь1 ? Mth.clamp((float)(var1 / Math.max(ЯьЬ, var3)), Яь衣, 1.0F) : 0.0F;
      this.т诶 = this.т诶 + (var4 - this.т诶) * Яьш;
      this.тЧ = this.тЧ + (float)(var1 * ЯьИ);
      if (this.тЧ > Яьь) {
         this.тЧ = this.тЧ - Яьж;
         this.тЛ = this.тЛ - Яь弟;
      }

      this.тЫ = this.тЫ + (Яьб + this.т诶 * ЯьЫ + this.тЬ * Яьl);
      if (this.тЫ > Яь8) {
         this.тЫ = this.тЫ - Яь5;
         this.тl = this.тl - Яьв;
      }

      this.ЖД();
      this.ЖO();
      this.Жв();
   }
}
