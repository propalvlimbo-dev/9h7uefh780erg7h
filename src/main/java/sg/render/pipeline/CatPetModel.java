package sg.render.pipeline;

import net.minecraft.util.Mth;
import sg.modules.impl.init.CatPetAnimator;

public final class CatPetModel {
   private static final int ещ = 24;
   private static final float А5 = 24.0F;
   private static final float Ав = (float) (Math.PI / 2);
   private static final float Ха8 = 3.8F;
   private static final float Ха5 = 2.1F;
   private static final float Хав = 0.12F;
   private static final float Хаъ = 0.55F;
   private static final float ХаМ = 0.35F;
   private static final float ХаН = -1.0F;
   private static final float Хан = -24.0F;
   private static final float ХаЦ = 0.35F;
   private static final float Хач = 0.05F;
   private static final float ХаЭ = -1.0F;
   private static final float Ха必 = 4.2F;
   private static final float Хащ = 6.4F;
   private static final int ХаГ = -1509949440;
   private static final float Хал = 12.0F;
   private static final float Хаэ = -10.0F;
   private static final float Хаю = 90.0F;
   private static final float ХаЩ = 2.5F;
   private static final float Хау = -2.0F;
   private static final float Ха2 = 3.0F;
   private static final float Хаф = -8.0F;
   private static final float Хаи = 4.0F;
   private static final float Ха9 = 16.0F;
   private static final float Хао = 6.0F;
   private static final float ХаO = -1.1F;
   private static final float Ха7 = -1.2F;
   private static final float ХаР = (float) (Math.PI / 2);
   private static final float Ха3 = 1.1F;
   private static final float Хар = (float) Math.PI;
   private static final float Ха6 = 1.2F;
   private static final float Ха_ = (float) (Math.PI * 3.0 / 2.0);
   private static final float ХаШ = 13.8F;
   private static final float Хаы = 0.8F;
   private static final float Хай = -5.0F;
   private static final float ХаЖ = 38.0F;
   private static final float Хам = 12.0F;
   private static final float ХаК = -1.0F;
   private static final float ХаI = 10.0F;
   private static final float ХаТ = 18.0F;
   private static final float ХаО = 0.6F;
   private static final float ХаФ = 5.0F;
   private static final float ХаЙ = 34.0F;
   private static final float Хак = 10.0F;
   private static final float Хаг = -1.0F;
   private static final float Ха0 = 6.0F;
   private static final float Хах = 0.7F;
   private static final float ХаУ = 14.0F;
   private static final float ХаЗ = 4.0F;
   private static final float ХаЕ = 1.15F;
   private static final float ХаП = 3.5F;
   private static final float Ха西 = 14.0F;
   private static final float Хад = 4.0F;
   private static final float ХаБ = 0.48F;
   private static final float ХаС = 4.0F;
   private static final float Хаз = 15.0F;
   private static final float Хац = -9.0F;
   private static final float Хая = -2.5F;
   private static final float ХЮс = -2.0F;
   private static final float ХЮВ = -3.0F;
   private static final float ХЮЯ = 5.0F;
   private static final float ХЮХ = 4.0F;
   private static final float ХЮе = 5.0F;
   private static final float ХЮА = -1.5F;
   private static final float ХЮ4 = -4.0F;
   private static final float ХЮп = 3.0F;
   private static final float ХЮД = -1.0F;
   private static final float ХЮЪ = 0.15F;
   private static final float ХЮт = 0.15F;
   private static final float ХЮа = 0.85F;
   private static final float ХЮЮ = -2.4F;
   private static final float ХЮЧ = -1.2F;
   private static final float ХЮЛ = 1.4F;
   private static final float ХЮ诶 = -3.05F;
   private static final float ХЮ1 = 0.35F;
   private static final float ХЮЬ = 1.6F;
   private static final float ХЮ衣 = 0.2F;
   private static final float ХЮш = 0.4F;
   private static final float ХЮИ = -1.2F;
   private static final float ХЮь = 1.4F;
   private static final float ХЮж = -3.05F;
   private static final float ХЮ弟 = 0.35F;
   private static final float ХЮб = 1.6F;
   private static final float ХЮЫ = 0.2F;
   private static final float ХЮl = 2.4F;
   private static final float ХЮ8 = -2.2F;
   private static final float ХЮ5 = -0.5F;
   private static final float ХЮв = 28.0F;
   private static final float ХЮъ = 0.8F;
   private static final float ХЮМ = 6.0F;
   private static final float ХЮН = 18.0F;
   private static final float ХЮн = 0.25F;
   private static final float ХЮЦ = -1.0F;
   private static final float ХЮч = -0.5F;
   private static final float ХЮЭ = 3.0F;
   private static final float ХЮ必 = 3.0F;
   private static final float ХЮщ = -10.0F;
   private static final float ХЮГ = 0.6F;
   private static final float ХЮл = 8.0F;
   private static final float ХЮэ = -0.9F;
   private static final float ХЮю = -0.5F;
   private static final float ХЮЩ = 1.8F;
   private static final float ХЮу = 3.5F;
   private static final float ХЮ2 = 50.0F;
   private static final float ХЮф = 18.0F;
   private static final float ХЮи = 8.0F;
   private static final float ХЮ9 = 10.0F;
   private static final float ХЮо = 16.0F;
   private static final float ХЮO = 12.0F;
   private static final float ХЮ7 = 6.0F;
   private static final float ХЮР = 8.0F;
   private static final float ХЮ3 = 15.0F;
   private static final float ХЮр = 8.0F;
   private static final float ХЮ6 = 0.35F;
   private static final float ХЮ_ = -0.5F;
   private static final float ХЮШ = 8.0F;
   private static final float ХЮы = 8.0F;
   private static final float ХЮй = 0.85F;
   private static final float ХЮЖ = 8.0F;
   private static final float ХЮм = 0.65F;
   private static final float ХЮК = -0.5F;
   private static final float ХЮI = 8.0F;

   private void й(CatPetRenderer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = 1.0F - var4;
      float var10 = Mth.sin(var2 * Хах) * ХаУ * var9 + Mth.sin(var3) * var4 * ХаЗ;
      float var11 = Mth.sin(var2 * ХаЕ) * ХаП * var9 - var5 * Ха西 + var4 * Хад;
      float var12 = Mth.sin(var2 * ХаБ) * ХаС * var9;
      var1.йщ();
      var1.Х_de5a6f78(0.0F, Хаз, Хац);
      var1.ыь(var10);
      var1.ы9(var11);
      var1.ыъ(var12);
      var1.д(Хая, ХЮс, ХЮВ, ХЮЯ, ХЮХ, ХЮе, 0, 0);
      var1.д(ХЮА, 0.0F, ХЮ4, ХЮп, 2.0F, 2.0F, 0, 24);
      this.method_3(var1, ХЮД, var7, var8);
      this.method_3(var1, 1.0F, var7, var8);
      if (var6 > ХЮЪ) {
         float var13 = Mth.clamp((var6 - ХЮт) / ХЮа, 0.0F, 1.0F);
         var1.д(ХЮЮ, ХЮЧ + var13 * ХЮЛ, ХЮ诶, 2.0F, ХЮ1 + var13 * ХЮЬ, ХЮ衣, 0, 0);
         var1.д(ХЮш, ХЮИ + var13 * ХЮь, ХЮж, 2.0F, ХЮ弟 + var13 * ХЮб, ХЮЫ, 0, 0);
      }

      var1.йК();
   }

   private void method_3(CatPetRenderer var1, float var2, float var3, float var4) {
      var1.йщ();
      var1.Х_de5a6f78(var2 * ХЮl, ХЮ8, ХЮ5);
      var1.ыъ(var2 * (ХЮв + var4 * ХЮъ) + var3 * var2 * ХЮМ);
      var1.ы9(ХЮН + var4 * ХЮн);
      var1.д(ХЮЦ, 0.0F, ХЮч, 2.0F, ХЮЭ, 1.0F, var2 < 0.0F ? 0 : 6, 10);
      var1.Х_de5a6f78(0.0F, ХЮ必, 0.0F);
      var1.ыъ(var2 * (ХЮщ + var4 * ХЮГ));
      var1.ы9(ХЮл);
      var1.д(ХЮэ, 0.0F, ХЮю, ХЮЩ, ХЮу, 1.0F, var2 < 0.0F ? 0 : 6, 10);
      var1.йК();
   }

   private void Ж(CatPetRenderer var1, float var2, float var3) {
      this.Ж(var1, ХаO, var2, var3);
      this.й(var1, Ха7, var2 + ХаР, var3);
      this.Ж(var1, Ха3, var2 + Хар, var3);
      this.й(var1, Ха6, var2 + Ха_, var3);
   }

   private void Н(CatPetRenderer var1, float var2, float var3, float var4) {
      float var5 = ХЮ2 + var4 * ХЮф - var3 * ХЮи;
      float var6 = Mth.sin(var2) * (ХЮ9 + var3 * ХЮо + var4 * ХЮO);
      float var7 = Mth.cos(var2) * (ХЮ7 + var3 * ХЮР);
      var1.йщ();
      var1.Х_de5a6f78(0.0F, ХЮ3, ХЮр);
      var1.ы9(var5 + var7);
      var1.ыъ(var6 * ХЮ6);
      var1.д(ХЮ_, 0.0F, 0.0F, 1.0F, ХЮШ, 1.0F, 0, 15);
      var1.Х_de5a6f78(0.0F, ХЮы, 0.0F);
      var1.ы9(var7 * ХЮй + ХЮЖ);
      var1.ыъ(var6 * ХЮм);
      var1.д(ХЮК, 0.0F, 0.0F, 1.0F, ХЮI, 1.0F, 4, 15);
      var1.йК();
   }

   public void й(CatPetRenderer var1, CatPetAnimator var2, float var3, float var4) {
      float var5 = var2.Вн(var3);
      float var6 = var2.В弟(var3);
      float var7 = var2.ВЕ(var3);
      float var8 = var2.Вь(var3);
      float var9 = var2.Вт(var3);
      float var10 = var2.ВХ(var3);
      float var11 = var2.Вх(var3);
      float var12 = var7 * var7 * Ха8;
      float var13 = Mth.sin(var4 * Ха5) * Хав * (1.0F - var6 * Хаъ);
      float var14 = Mth.sin(var5 * 2.0F) * var6 * ХаМ;
      this.й(var1, var7);
      var1.йщ();
      var1.Х_de5a6f78(0.0F, var12 + var13 + var14, 0.0F);
      var1.method_7(1.0F, ХаН, 1.0F);
      var1.Х_de5a6f78(0.0F, Хан, 0.0F);
      this.й(var1, var5, var6);
      this.Ж(var1, var5, var6);
      this.й(var1, var4, var5, var6, var7, var8, var9, var10);
      this.Н(var1, var11, var6, var7);
      var1.йК();
   }

   private void й(CatPetRenderer var1, float var2) {
      float var3 = 1.0F - var2 * ХаЦ;
      var1.йщ();
      var1.Х_de5a6f78(0.0F, Хач, ХаЭ);
      var1.В(Ха必 * var3, Хащ * var3, 0.0F, ХаГ, 24);
      var1.йК();
   }

   private void й(CatPetRenderer var1, float var2, float var3) {
      var1.йщ();
      var1.Х_de5a6f78(0.0F, Хал, Хаэ);
      var1.ы9(Хаю + Mth.sin(var2 * 2.0F) * var3 * ХаЩ);
      var1.ыъ(Mth.sin(var2) * var3 * 2.0F);
      var1.д(Хау, Ха2, Хаф, Хаи, Ха9, Хао, 20, 0);
      var1.йК();
   }

   private void Ж(CatPetRenderer var1, float var2, float var3, float var4) {
      float var5 = Mth.sin(var3) * var4;
      float var6 = Math.max(0.0F, Mth.cos(var3)) * var4;
      var1.йщ();
      var1.Х_de5a6f78(var2, ХаТ - var6 * ХаО, ХаФ);
      var1.ы9(var5 * ХаЙ - var6 * Хак);
      var1.д(Хаг, 0.0F, 1.0F, 2.0F, Ха0, 2.0F, 8, 13);
      var1.йК();
   }

   private void й(CatPetRenderer var1, float var2, float var3, float var4) {
      float var5 = Mth.sin(var3) * var4;
      float var6 = Math.max(0.0F, Mth.cos(var3)) * var4;
      var1.йщ();
      var1.Х_de5a6f78(var2, ХаШ - var6 * Хаы, Хай);
      var1.ы9(var5 * ХаЖ - var6 * Хам);
      var1.д(ХаК, 0.0F, 0.0F, 2.0F, ХаI, 2.0F, 40, 0);
      var1.йК();
   }
}
