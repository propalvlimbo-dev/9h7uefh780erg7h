package settings.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public final class ChinaHatGeometry {
   private static final int п诶 = 256;
   private static final int п1 = 18;
   private static final float ДЬ = -0.503125F;
   private static final float Д衣 = (float)Math.sqrt(ChinaHatGeometry.Янж) + ChinaHatGeometry.Ян弟;
   private static final float Дш = 0.286F;
   private static final float[] Н = new float[257];
   private static final float[] н = new float[257];
   private static final float[] Ц = new float[19];
   private static final float[] ч = new float[19];
   private static final float ЯМЖ = 0.286F;
   private static final int ЯМм = 16777215;
   private static final int ЯМК = 16777215;
   private static final float ЯМI = 0.07F;
   private static final float ЯМТ = 256.0F;
   private static final float ЯМО = 256.0F;
   private static final float ЯМФ = -0.769105F;
   private static final float ЯМЙ = 0.286F;
   private static final float ЯМк = 0.286F;
   private static final float ЯМг = 0.035F;
   private static final float ЯМ0 = 0.58F;
   private static final float ЯМх = -0.769105F;
   private static final float ЯМУ = 0.286F;
   private static final float ЯМЗ = 0.286F;
   private static final float ЯМЕ = 0.035F;
   private static final float ЯМП = 0.58F;
   private static final float ЯМ西 = -0.503125F;
   private static final float ЯМд = 0.286F;
   private static final float ЯМБ = 0.286F;
   private static final float ЯМС = 0.985F;
   private static final float ЯМз = -0.503125F;
   private static final float ЯМц = 0.286F;
   private static final float ЯМя = 0.286F;
   private static final float ЯНс = 0.985F;
   private static final float ЯНВ = 0.055F;
   private static final float ЯНЯ = 0.085F;
   private static final float ЯНХ = 0.235F;
   private static final float ЯНе = 0.155F;
   private static final float ЯНА = -0.789125F;
   private static final float ЯН4 = 0.286F;
   private static final float ЯНп = -0.789125F;
   private static final float ЯНД = 0.286F;
   private static final float ЯНЪ = 0.42F;
   private static final float ЯНт = 0.68F;
   private static final float ЯНа = 256.0F;
   private static final float ЯНЮ = 256.0F;
   private static final float ЯНЧ = 0.286F;
   private static final float ЯНЛ = 0.286F;
   private static final float ЯН诶 = 0.055F;
   private static final float ЯН1 = 0.48F;
   private static final float ЯНЬ = 0.286F;
   private static final float ЯН衣 = 0.286F;
   private static final float ЯНш = 0.055F;
   private static final float ЯНИ = 0.48F;
   private static final float ЯНь = 0.286F;
   private static final float ЯНж = 0.286F;
   private static final float ЯН弟 = 0.82F;
   private static final float ЯНб = 0.286F;
   private static final float ЯНЫ = 0.286F;
   private static final float ЯНl = 0.82F;
   private static final float ЯН8 = 0.972F;
   private static final float ЯН5 = 1.052F;
   private static final float ЯНв = 256.0F;
   private static final float ЯНъ = 256.0F;
   private static final float ЯНМ = -0.500625F;
   private static final float ЯНН = -1.0F;
   private static final float ЯНн = 1.1800001F;
   private static final float ЯНЦ = 0.2F;
   private static final float ЯНч = -0.500625F;
   private static final float ЯНЭ = -1.0F;
   private static final float ЯН必 = 1.1800001F;
   private static final float ЯНщ = 0.2F;
   private static final float ЯНГ = -0.500625F;
   private static final float ЯНл = -0.045F;
   private static final float ЯНэ = 1.9200001F;
   private static final float ЯНю = -0.500625F;
   private static final float ЯНЩ = -0.045F;
   private static final float ЯНу = 1.9200001F;
   private static final float ЯН2 = 1.072F;
   private static final float ЯНф = 0.07F;
   private static final float ЯНи = 1.36F;
   private static final float ЯН9 = 0.38F;
   private static final float ЯНо = 256.0F;
   private static final float ЯНO = 256.0F;
   private static final float ЯН7 = -0.77210504F;
   private static final float ЯНР = 0.286F;
   private static final float ЯН3 = 0.286F;
   private static final float ЯНр = 2.035F;
   private static final float ЯН6 = 0.1F;
   private static final float ЯН_ = -0.77210504F;
   private static final float ЯНШ = 0.286F;
   private static final float ЯНы = 0.286F;
   private static final float ЯНй = 2.035F;
   private static final float ЯНЖ = 0.1F;
   private static final float ЯНм = -0.504625F;
   private static final float ЯНК = 0.286F;
   private static final float ЯНI = 0.286F;
   private static final float ЯНТ = 2.325F;
   private static final float ЯНО = -0.504625F;
   private static final float ЯНФ = 0.286F;
   private static final float ЯНЙ = 0.286F;
   private static final float ЯНк = 2.325F;
   private static final float ЯНг = 0.97F;
   private static final float ЯН0 = 1.075F;
   private static final float ЯНх = 1.02F;
   private static final float ЯНУ = 0.44F;
   private static final float ЯНЗ = 0.94F;
   private static final float ЯНЕ = 0.34F;
   private static final float ЯНП = 256.0F;
   private static final float ЯН西 = 256.0F;
   private static final float ЯНд = -0.501625F;
   private static final float ЯНБ = -1.0F;
   private static final float ЯНС = 2.02F;
   private static final float ЯНз = 0.42F;
   private static final float ЯНц = -0.501625F;
   private static final float ЯНя = -1.0F;
   private static final float Янс = 2.02F;
   private static final float ЯнВ = 0.42F;
   private static final float ЯнЯ = -0.501625F;
   private static final float ЯнХ = -0.045F;
   private static final float Яне = 2.72F;
   private static final float ЯнА = -0.501625F;
   private static final float Ян4 = -0.045F;
   private static final float Янп = 2.72F;
   private static final float ЯнД = -0.500125F;
   private static final float ЯнЪ = 0.122F;
   private static final float Янт = -0.500125F;
   private static final float Яна = 2.035F;
   private static final float ЯнЮ = -0.500125F;
   private static final float ЯнЧ = 2.035F;
   private static final float ЯнЛ = 2.995F;
   private static final float Ян诶 = 0.18F;
   private static final float Ян1 = 2.995F;
   private static final float ЯнЬ = 0.18F;
   private static final float Ян衣 = 1.0E-6F;
   private static final float Янш = 255.0F;
   private static final float ЯнИ = 255.0F;
   private static final float Янь = 255.0F;
   private static final double Янж = 0.17626953F;
   private static final float Ян弟 = 0.14F;
   private static final float Янб = (float) (Math.PI * 2);
   private static final float ЯнЫ = 256.0F;
   private static final float Янl = (float) (Math.PI * 2);
   private static final float Ян8 = 18.0F;

   static {
      for (int var0 = 0; var0 <= 256; var0++) {
         float var1 = Янб * var0 / ЯнЫ;
         н[var0] = (float)Math.sin(var1);
         Н[var0] = (float)Math.cos(var1);
      }

      for (int var2 = 0; var2 <= 18; var2++) {
         float var3 = Янl * var2 / Ян8;
         ч[var2] = (float)Math.sin(var3);
         Ц[var2] = (float)Math.cos(var3);
      }
   }

   private static int б(BufferBuilder var0, Matrix4f var1, Matrix3f var2, int var3, int var4, float var5, float var6, float var7) {
      int var8 = var3 >> 16 & 0xFF;
      int var9 = var3 >> 8 & 0xFF;
      int var10 = var3 & 0xFF;
      int var11 = var4 >> 16 & 0xFF;
      int var12 = var4 >> 8 & 0xFF;
      int var13 = var4 & 0xFF;
      float var14 = Д衣 * ЯНг;
      float var15 = Д衣 * ЯН0;
      float var16 = Д衣 * ЯНх;
      float var17 = Д衣 * ЯНУ;
      float var18 = var5 * ЯНЗ;
      float var19 = var5 * ЯНЕ;
      int var20 = 0;

      for (int var21 = 0; var21 < 256; var21++) {
         int var22 = var21 + 1;
         float var23 = var21 / ЯНП;
         float var24 = var22 / ЯН西;
         var20 += method_5(
            var0,
            var1,
            var2,
            var6,
            var7,
            Н[var21] * var14,
            ЯНд,
            н[var21] * var14,
            0.0F,
            ЯНБ,
            0.0F,
            var23,
            ЯНС,
            var8,
            var9,
            var10,
            var18 * ЯНз,
            Н[var22] * var14,
            ЯНц,
            н[var22] * var14,
            0.0F,
            ЯНя,
            0.0F,
            var24,
            Янс,
            var8,
            var9,
            var10,
            var18 * ЯнВ,
            Н[var22] * var15,
            ЯнЯ,
            н[var22] * var15,
            Н[var22],
            ЯнХ,
            н[var22],
            var24,
            Яне,
            var11,
            var12,
            var13,
            var18,
            Н[var21] * var15,
            ЯнА,
            н[var21] * var15,
            Н[var21],
            Ян4,
            н[var21],
            var23,
            Янп,
            var11,
            var12,
            var13,
            var18
         );

         for (int var25 = 0; var25 < 18; var25++) {
            float var26 = ч[var25];
            float var27 = Ц[var25];
            float var28 = var16 + var27 * var17;
            float var29 = ЯнД + var26 * ЯнЪ;
            var20 += method_5(
               var0,
               var1,
               var2,
               var6,
               var7,
               Н[var21] * var16,
               Янт,
               н[var21] * var16,
               Н[var21],
               0.0F,
               н[var21],
               var23,
               Яна,
               var8,
               var9,
               var10,
               var19,
               Н[var22] * var16,
               ЯнЮ,
               н[var22] * var16,
               Н[var22],
               0.0F,
               н[var22],
               var24,
               ЯнЧ,
               var8,
               var9,
               var10,
               var19,
               Н[var22] * var28,
               var29,
               н[var22] * var28,
               Н[var22] * var27,
               var26,
               н[var22] * var27,
               var24,
               ЯнЛ,
               var11,
               var12,
               var13,
               var19 * Ян诶,
               Н[var21] * var28,
               var29,
               н[var21] * var28,
               Н[var21] * var27,
               var26,
               н[var21] * var27,
               var23,
               Ян1,
               var11,
               var12,
               var13,
               var19 * ЯнЬ
            );
         }
      }

      return var20;
   }

   private static void method_5(
      BufferBuilder var0,
      Matrix4f var1,
      Matrix3f var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      int var13,
      int var14,
      int var15,
      float var16
   ) {
      float var17 = var5 * var3;
      float var18 = -var6 * var4;
      float var19 = var7 * var3;
      float var20 = var8;
      float var21 = -var9;
      float var22 = var10;
      float var23 = var2.m00() * var20 + var2.m10() * var21 + var2.m20() * var22;
      float var24 = var2.m01() * var20 + var2.m11() * var21 + var2.m21() * var22;
      float var25 = var2.m02() * var20 + var2.m12() * var21 + var2.m22() * var22;
      float var26 = var23 * var23 + var24 * var24 + var25 * var25;
      float var27 = var26 <= Ян衣 ? 1.0F : (float)(1.0 / Math.sqrt(var26));
      float var28 = Mth.clamp(var16, 0.0F, 1.0F);
      var0.addVertex(var1, var17, var18, var19)
         .setUv(var11, var12)
         .setColor(var13 / Янш, var14 / ЯнИ, var15 / Янь, var28)
         .setNormal(var23 * var27, var24 * var27, var25 * var27);
   }

   static int method_5(int[] var0, float var1) {
      float var2 = var1 - (float)Math.floor(var1);
      float var3 = var2 * (var0.length - 1);
      int var4 = Math.min(var0.length - 2, Math.max(0, (int)Math.floor(var3)));
      return ь(var0[var4] & ЯМм, var0[var4 + 1] & ЯМК, var3 - var4);
   }

   static int Е(int var0, float var1) {
      float var2 = Mth.clamp(var1, 0.0F, 1.0F);
      int var3 = var0 >> 16 & 0xFF;
      int var4 = var0 >> 8 & 0xFF;
      int var5 = var0 & 0xFF;
      var3 = Math.min(255, Math.round(var3 + (255 - var3) * var2));
      var4 = Math.min(255, Math.round(var4 + (255 - var4) * var2));
      var5 = Math.min(255, Math.round(var5 + (255 - var5) * var2));
      return var3 << 16 | var4 << 8 | var5;
   }

   static float method_5к() {
      return Д衣;
   }

   private static int method_5(
      BufferBuilder var0,
      Matrix4f var1,
      Matrix3f var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      int var13,
      int var14,
      int var15,
      float var16,
      float var17,
      float var18,
      float var19,
      float var20,
      float var21,
      float var22,
      float var23,
      float var24,
      int var25,
      int var26,
      int var27,
      float var28,
      float var29,
      float var30,
      float var31,
      float var32,
      float var33,
      float var34,
      float var35,
      float var36,
      int var37,
      int var38,
      int var39,
      float var40,
      float var41,
      float var42,
      float var43,
      float var44,
      float var45,
      float var46,
      float var47,
      float var48,
      int var49,
      int var50,
      int var51,
      float var52
   ) {
      method_5(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16);
      method_5(var0, var1, var2, var3, var4, var17, var18, var19, var20, var21, var22, var23, var24, var25, var26, var27, var28);
      method_5(var0, var1, var2, var3, var4, var29, var30, var31, var32, var33, var34, var35, var36, var37, var38, var39, var40);
      method_5(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16);
      method_5(var0, var1, var2, var3, var4, var29, var30, var31, var32, var33, var34, var35, var36, var37, var38, var39, var40);
      method_5(var0, var1, var2, var3, var4, var41, var42, var43, var44, var45, var46, var47, var48, var49, var50, var51, var52);
      return 6;
   }

   static int ь(int var0, int var1, float var2) {
      var2 = Mth.clamp(var2, 0.0F, 1.0F);
      int var3 = var0 >> 16 & 0xFF;
      int var4 = var0 >> 8 & 0xFF;
      int var5 = var0 & 0xFF;
      int var6 = var1 >> 16 & 0xFF;
      int var7 = var1 >> 8 & 0xFF;
      int var8 = var1 & 0xFF;
      int var9 = Math.round(var3 + (var6 - var3) * var2);
      int var10 = Math.round(var4 + (var7 - var4) * var2);
      int var11 = Math.round(var5 + (var8 - var5) * var2);
      return var9 << 16 | var10 << 8 | var11;
   }

   public static int method_5(BufferBuilder var0, Matrix4f var1, Matrix3f var2, int var3, int var4, float var5, float var6, float var7) {
      int var8 = 0;
      var8 += С(var0, var1, var2, var3, var4, var5, var6, var7);
      var8 += method_5(var0, var1, var2, var3, var4, var5, false, var6, var7);
      return var8 + Л(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   private ChinaHatGeometry() {
   }

   private static int ь(BufferBuilder var0, Matrix4f var1, Matrix3f var2, int var3, int var4, float var5, float var6, float var7) {
      int var8 = var3 >> 16 & 0xFF;
      int var9 = var3 >> 8 & 0xFF;
      int var10 = var3 & 0xFF;
      int var11 = var4 >> 16 & 0xFF;
      int var12 = var4 >> 8 & 0xFF;
      int var13 = var4 & 0xFF;
      float var14 = Д衣 * ЯН2;
      float var15 = Д衣 * ЯНф * ЯНи;
      float var16 = var5 * ЯН9;
      int var17 = 0;

      for (int var18 = 0; var18 < 256; var18++) {
         int var19 = var18 + 1;
         float var20 = var18 / ЯНо;
         float var21 = var19 / ЯНO;
         var17 += method_5(
            var0,
            var1,
            var2,
            var6,
            var7,
            Н[var18] * var15,
            ЯН7,
            н[var18] * var15,
            Н[var18] * ЯНР,
            -var14,
            н[var18] * ЯН3,
            var20,
            ЯНр,
            var8,
            var9,
            var10,
            var16 * ЯН6,
            Н[var19] * var15,
            ЯН_,
            н[var19] * var15,
            Н[var19] * ЯНШ,
            -var14,
            н[var19] * ЯНы,
            var21,
            ЯНй,
            var8,
            var9,
            var10,
            var16 * ЯНЖ,
            Н[var19] * var14,
            ЯНм,
            н[var19] * var14,
            Н[var19] * ЯНК,
            -var14,
            н[var19] * ЯНI,
            var21,
            ЯНТ,
            var11,
            var12,
            var13,
            var16,
            Н[var18] * var14,
            ЯНО,
            н[var18] * var14,
            Н[var18] * ЯНФ,
            -var14,
            н[var18] * ЯНЙ,
            var20,
            ЯНк,
            var11,
            var12,
            var13,
            var16
         );
      }

      return var17;
   }

   static float method_5В() {
      return ЯМЖ;
   }

   private static int method_5(BufferBuilder var0, Matrix4f var1, Matrix3f var2, int var3, int var4, float var5, boolean var6, float var7, float var8) {
      int var9 = var3 >> 16 & 0xFF;
      int var10 = var3 >> 8 & 0xFF;
      int var11 = var3 & 0xFF;
      int var12 = var4 >> 16 & 0xFF;
      int var13 = var4 >> 8 & 0xFF;
      int var14 = var4 & 0xFF;
      float var15 = var6 ? ЯНВ : ЯНЯ;
      float var16 = var6 ? ЯНХ : ЯНе;
      float var17 = Д衣 * var15;
      float var18 = Д衣 * var16;
      float var19 = ЯНА + ЯН4 * var15;
      float var20 = ЯНп + ЯНД * var16;
      float var21 = var6 ? 2.0F : 1.0F;
      float var22 = var5 * (var6 ? ЯНЪ : ЯНт);
      int var23 = 0;

      for (int var24 = 0; var24 < 256; var24++) {
         int var25 = var24 + 1;
         float var26 = var24 / ЯНа;
         float var27 = var25 / ЯНЮ;
         var23 += method_5(
            var0,
            var1,
            var2,
            var7,
            var8,
            Н[var24] * var17,
            var19,
            н[var24] * var17,
            Н[var24] * ЯНЧ,
            -Д衣,
            н[var24] * ЯНЛ,
            var26,
            var21 + ЯН诶,
            var9,
            var10,
            var11,
            var22 * ЯН1,
            Н[var25] * var17,
            var19,
            н[var25] * var17,
            Н[var25] * ЯНЬ,
            -Д衣,
            н[var25] * ЯН衣,
            var27,
            var21 + ЯНш,
            var9,
            var10,
            var11,
            var22 * ЯНИ,
            Н[var25] * var18,
            var20,
            н[var25] * var18,
            Н[var25] * ЯНь,
            -Д衣,
            н[var25] * ЯНж,
            var27,
            var21 + ЯН弟,
            var12,
            var13,
            var14,
            var22,
            Н[var24] * var18,
            var20,
            н[var24] * var18,
            Н[var24] * ЯНб,
            -Д衣,
            н[var24] * ЯНЫ,
            var26,
            var21 + ЯНl,
            var12,
            var13,
            var14,
            var22
         );
      }

      return var23;
   }

   private static int Л(BufferBuilder var0, Matrix4f var1, Matrix3f var2, int var3, int var4, float var5, float var6, float var7) {
      int var8 = var3 >> 16 & 0xFF;
      int var9 = var3 >> 8 & 0xFF;
      int var10 = var3 & 0xFF;
      int var11 = var4 >> 16 & 0xFF;
      int var12 = var4 >> 8 & 0xFF;
      int var13 = var4 & 0xFF;
      float var14 = Д衣 * ЯН8;
      float var15 = Д衣 * ЯН5;
      int var16 = 0;

      for (int var17 = 0; var17 < 256; var17++) {
         int var18 = var17 + 1;
         float var19 = var17 / ЯНв;
         float var20 = var18 / ЯНъ;
         var16 += method_5(
            var0,
            var1,
            var2,
            var6,
            var7,
            Н[var17] * var14,
            ЯНМ,
            н[var17] * var14,
            0.0F,
            ЯНН,
            0.0F,
            var19,
            ЯНн,
            var8,
            var9,
            var10,
            var5 * ЯНЦ,
            Н[var18] * var14,
            ЯНч,
            н[var18] * var14,
            0.0F,
            ЯНЭ,
            0.0F,
            var20,
            ЯН必,
            var8,
            var9,
            var10,
            var5 * ЯНщ,
            Н[var18] * var15,
            ЯНГ,
            н[var18] * var15,
            Н[var18],
            ЯНл,
            н[var18],
            var20,
            ЯНэ,
            var11,
            var12,
            var13,
            var5,
            Н[var17] * var15,
            ЯНю,
            н[var17] * var15,
            Н[var17],
            ЯНЩ,
            н[var17],
            var19,
            ЯНу,
            var11,
            var12,
            var13,
            var5
         );
      }

      return var16;
   }

   public static int т(BufferBuilder var0, Matrix4f var1, Matrix3f var2, int var3, int var4, float var5, float var6, float var7) {
      int var8 = 0;
      var8 += ь(var0, var1, var2, var3, var4, var5, var6, var7);
      var8 += method_5(var0, var1, var2, var3, var4, var5, true, var6, var7);
      return var8 + б(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   private static int С(BufferBuilder var0, Matrix4f var1, Matrix3f var2, int var3, int var4, float var5, float var6, float var7) {
      int var8 = var3 >> 16 & 0xFF;
      int var9 = var3 >> 8 & 0xFF;
      int var10 = var3 & 0xFF;
      int var11 = var4 >> 16 & 0xFF;
      int var12 = var4 >> 8 & 0xFF;
      int var13 = var4 & 0xFF;
      float var14 = Д衣 * ЯМI;
      int var15 = 0;

      for (int var16 = 0; var16 < 256; var16++) {
         int var17 = var16 + 1;
         float var18 = var16 / ЯМТ;
         float var19 = var17 / ЯМО;
         var15 += method_5(
            var0,
            var1,
            var2,
            var6,
            var7,
            Н[var16] * var14,
            ЯМФ,
            н[var16] * var14,
            Н[var16] * ЯМЙ,
            -Д衣,
            н[var16] * ЯМк,
            var18,
            ЯМг,
            var8,
            var9,
            var10,
            var5 * ЯМ0,
            Н[var17] * var14,
            ЯМх,
            н[var17] * var14,
            Н[var17] * ЯМУ,
            -Д衣,
            н[var17] * ЯМЗ,
            var19,
            ЯМЕ,
            var8,
            var9,
            var10,
            var5 * ЯМП,
            Н[var17] * Д衣,
            ЯМ西,
            н[var17] * Д衣,
            Н[var17] * ЯМд,
            -Д衣,
            н[var17] * ЯМБ,
            var19,
            ЯМС,
            var11,
            var12,
            var13,
            var5,
            Н[var16] * Д衣,
            ЯМз,
            н[var16] * Д衣,
            Н[var16] * ЯМц,
            -Д衣,
            н[var16] * ЯМя,
            var18,
            ЯНс,
            var11,
            var12,
            var13,
            var5
         );
      }

      return var15;
   }
}
