package sg.hud;

import com.google.gson.annotations.SerializedName;

public final class AnimatedIntegerText {
   @SerializedName("4И")
   private int field_4И;
   @SerializedName("4ь")
   private int field_4ь;
   private float пЫ = 1.0F;
   private String ВР;
   private String В3;
   private static final String ЯуЩ = "0";
   private static final String Яуу = "0";
   private static final float Яу2 = 0.14F;
   private static final float Яуф = 0.22F;
   private static final float Яуи = 3.0F;

   public AnimatedIntegerText() {
      this.ВР = ЯуЩ;
      this.В3 = Яуу;
   }

   public boolean Тв() {
      return this.пЫ < 1.0F;
   }

   public void Р(int var1, float var2) {
      int var3 = Math.max(0, var1);
      if (this.пЫ >= 1.0F && var3 != this.field_4ь) {
         this.field_4И = this.field_4ь;
         this.field_4ь = var3;
         this.ВР = this.В3;
         this.В3 = Integer.toString(this.field_4ь);
         this.пЫ = 0.0F;
      }

      float var4 = var3 != this.field_4ь ? Яу2 : Яуф;
      this.пЫ = Math.min(1.0F, this.пЫ + Math.max(0.0F, var2) / var4);
   }

   public String цг() {
      return this.ВР;
   }

   public float РВ() {
      return this.пЫ * this.пЫ * (Яуи - 2.0F * this.пЫ);
   }

   public void method_2Р(int var1) {
      this.field_4И = this.field_4ь = Math.max(0, var1);
      this.ВР = this.В3 = Integer.toString(this.field_4ь);
      this.пЫ = 1.0F;
   }

   public int method_2в() {
      return Integer.compare(this.field_4И, this.field_4ь);
   }

   public String цС() {
      return this.В3;
   }
}
