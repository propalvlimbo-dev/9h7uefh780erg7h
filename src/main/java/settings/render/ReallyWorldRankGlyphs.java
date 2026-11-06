package settings.render;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import sg.util.time.ColorMath;

public class ReallyWorldRankGlyphs {
   private static final Map<Integer, String> сХ = new HashMap<>();
   private static final Map<Integer, int[]> се = new HashMap<>();
   private static final boolean[] Я = new boolean[ReallyWorldRankGlyphs.field_2с];
   private static final LinkedHashMap<Character, String> Х = new LinkedHashMap<>();
   private static final int[] а = new int[]{
      ReallyWorldRankGlyphs.field_2В,
      ReallyWorldRankGlyphs.field_2Я,
      ReallyWorldRankGlyphs.field_2Х,
      ReallyWorldRankGlyphs.field_2е,
      ReallyWorldRankGlyphs.field_2А,
      ReallyWorldRankGlyphs.field_24,
      ReallyWorldRankGlyphs.field_2п,
      ReallyWorldRankGlyphs.field_2Д,
      ReallyWorldRankGlyphs.field_2Ъ,
      ReallyWorldRankGlyphs.field_2т,
      ReallyWorldRankGlyphs.field_2а,
      ReallyWorldRankGlyphs.field_2Ю,
      ReallyWorldRankGlyphs.field_2Ч,
      ReallyWorldRankGlyphs.field_2Л,
      ReallyWorldRankGlyphs.field_2诶,
      ReallyWorldRankGlyphs.field_21,
      ReallyWorldRankGlyphs.field_2Ь,
      ReallyWorldRankGlyphs.field_2衣,
      ReallyWorldRankGlyphs.field_2ш,
      ReallyWorldRankGlyphs.field_2И,
      ReallyWorldRankGlyphs.field_2ь,
      ReallyWorldRankGlyphs.field_2ж,
      ReallyWorldRankGlyphs.field_2弟,
      ReallyWorldRankGlyphs.field_2б,
      ReallyWorldRankGlyphs.field_2Ы,
      ReallyWorldRankGlyphs.field_2l,
      ReallyWorldRankGlyphs.field_28,
      ReallyWorldRankGlyphs.field_25
   };
   private static final int уц = 42240;
   private static final String уя = "rank";
   private static final int field_2с = 65536;
   private static final int field_2В = 42240;
   private static final int field_2Я = 42244;
   private static final int field_2Х = 42248;
   private static final int field_2е = 42258;
   private static final int field_2А = 42262;
   private static final int field_24 = 42272;
   private static final int field_2п = 42276;
   private static final int field_2Д = 42280;
   private static final int field_2Ъ = 42336;
   private static final int field_2т = 42290;
   private static final int field_2а = 42294;
   private static final int field_2Ю = 42308;
   private static final int field_2Ч = 42326;
   private static final int field_2Л = 42312;
   private static final int field_2诶 = 42304;
   private static final int field_21 = 42322;
   private static final int field_2Ь = 42249;
   private static final int field_2衣 = 42259;
   private static final int field_2ш = 42263;
   private static final int field_2И = 42273;
   private static final int field_2ь = 42277;
   private static final int field_2ж = 42281;
   private static final int field_2弟 = 42291;
   private static final int field_2б = 42295;
   private static final int field_2Ы = 42241;
   private static final int field_2l = 42245;
   private static final int field_28 = 42313;
   private static final int field_25 = 42313;
   private static final int field_2в = 42240;
   private static final String field_2ъ = "PLAYER";
   private static final int field_2М = 42244;
   private static final String field_2Н = "HERO";
   private static final int field_2н = 42248;
   private static final String field_2Ц = "TITAN";
   private static final int field_2ч = 42258;
   private static final String field_2Э = "AVENGER";
   private static final int field_2必 = 42262;
   private static final String field_2щ = "OVERLORD";
   private static final int field_2Г = 42272;
   private static final String field_2л = "MAGISTER";
   private static final int field_2э = 42276;
   private static final String field_2ю = "IMPERATOR";
   private static final int field_2Щ = 42280;
   private static final String field_2у = "DRAGON";
   private static final int field_22 = 42336;
   private static final String field_2ф = "D.HELPER";
   private static final int field_2и = 42290;
   private static final String field_29 = "BULL";
   private static final int field_2о = 42294;
   private static final String field_2O = "TIGER";
   private static final int field_27 = 42308;
   private static final String field_2Р = "DRACULA";
   private static final int field_23 = 42326;
   private static final String field_2р = "BUNNY";
   private static final int field_26 = 42312;
   private static final String field_2_ = "COBRA";
   private static final int field_2Ш = 42304;
   private static final String field_2ы = "HYDRA";
   private static final int field_2й = 42322;
   private static final String field_2Ж = "RABBIT";
   private static final int field_2м = 42249;
   private static final String field_2К = "HELPER";
   private static final int field_2I = 42259;
   private static final String field_2Т = "ML.MODER";
   private static final int field_2О = 42263;
   private static final String field_2Ф = "MODER";
   private static final int field_2Й = 42273;
   private static final String field_2к = "MODER+";
   private static final int field_2г = 42277;
   private static final String field_20 = "ST.MODER";
   private static final int field_2х = 42281;
   private static final String field_2У = "GL.MODER";
   private static final int field_2З = 42291;
   private static final String field_2Е = "ML.ADMIN";
   private static final int field_2П = 42295;
   private static final String field_2西 = "ADMIN";
   private static final int field_2д = 42241;
   private static final String field_2Б = "MEDIA";
   private static final int field_2С = 42245;
   private static final String field_2з = "YT";
   private static final int field_2ц = 42305;
   private static final String field_2я = "GOD";
   private static final int фс = 42313;
   private static final String фВ = "PEGAS";
   private static final int фЯ = 42309;
   private static final String фХ = "VAMPIRE";
   private static final String фе = "~";
   private static final String фА = "&";
   private static final String ф4 = "A";
   private static final String фп = "B";
   private static final String фД = "C";
   private static final String фЪ = "D";
   private static final String фт = "E";
   private static final int фа = 42800;
   private static final String фЮ = "F";
   private static final String фЧ = "G";
   private static final String фЛ = "H";
   private static final String ф诶 = "I";
   private static final String ф1 = "J";
   private static final String фЬ = "K";
   private static final String ф衣 = "L";
   private static final String фш = "M";
   private static final String фИ = "N";
   private static final String фь = "O";
   private static final String фж = "P";
   private static final String ф弟 = "Q";
   private static final String фб = "R";
   private static final String фЫ = "T";
   private static final String фl = "U";
   private static final int ф8 = 42801;
   private static final String ф5 = "S";
   private static final String фв = "V";
   private static final String фъ = "W";
   private static final String фМ = "X";
   private static final String фН = "Y";
   private static final String фн = "Z";
   private static final int фЦ = 42240;
   private static final int фч = 42244;
   private static final int фЭ = 42248;
   private static final int ф必 = 42258;
   private static final int фщ = 42262;
   private static final int фГ = 42272;
   private static final int фл = 42276;
   private static final int фэ = 42280;
   private static final int фю = 42336;
   private static final int фЩ = 42290;
   private static final int фу = 42294;
   private static final int ф2 = 42308;
   private static final int фф = 42326;
   private static final int фи = 42312;
   private static final int ф9 = 42304;
   private static final int фо = 42322;
   private static final int фO = 42249;
   private static final int ф7 = 42259;
   private static final int фР = 42263;
   private static final int ф3 = 42273;
   private static final int фр = 42277;
   private static final int ф6 = 42281;
   private static final int ф_ = 42291;
   private static final int фШ = 42295;
   private static final int фы = 42241;
   private static final int фй = 42245;
   private static final int фЖ = 42305;
   private static final int фм = 42309;
   private static final int фК = 42313;
   private static final int фI = 42240;
   private static final String фТ = "PLAYER";
   private static final int фО = 42244;
   private static final String фФ = "HERO";
   private static final int фЙ = 42248;
   private static final String фк = "TITAN";
   private static final int фг = 42258;
   private static final String ф0 = "AVENGER";
   private static final int фх = 42262;
   private static final String фУ = "OVERLORD";
   private static final int фЗ = 42272;
   private static final String фЕ = "MAGISTER";
   private static final int фП = 42276;
   private static final String ф西 = "IMPERATOR";
   private static final int фд = 42280;
   private static final String фБ = "DRAGON";
   private static final int фС = 42336;
   private static final String фз = "D.HELPER";
   private static final int фц = 42290;
   private static final String фя = "BULL";
   private static final int ис = 42294;
   private static final String иВ = "TIGER";
   private static final int иЯ = 42308;
   private static final String иХ = "DRACULA";
   private static final int ие = 42326;
   private static final String иА = "BUNNY";
   private static final int и4 = 42312;
   private static final String ип = "COBRA";
   private static final int иД = 42304;
   private static final String иЪ = "HYDRA";
   private static final int ит = 42322;
   private static final String иа = "RABBIT";
   private static final int иЮ = 42249;
   private static final String иЧ = "HELPER";
   private static final int иЛ = 42259;
   private static final String и诶 = "ML.MODER";
   private static final int и1 = 42263;
   private static final String иЬ = "MODER";
   private static final int и衣 = 42273;
   private static final String иш = "MODER+";
   private static final int иИ = 42277;
   private static final String иь = "ST.MODER";
   private static final int иж = 42281;
   private static final String и弟 = "GL.MODER";
   private static final int иб = 42291;
   private static final String иЫ = "ML.ADMIN";
   private static final int иl = 42295;
   private static final String и8 = "ADMIN";
   private static final int и5 = 42241;
   private static final String ив = "MEDIA";
   private static final int иъ = 42245;
   private static final String иМ = "YT";
   private static final int иН = 42305;
   private static final String ин = "GOD";
   private static final int иЦ = 42313;
   private static final String ич = "PEGAS";
   private static final int иЭ = 42309;
   private static final String и必 = "VAMPIRE";

   public static String method__(int var0) {
      return сХ.get(var0);
   }

   public static boolean щ(char var0) {
      return Х.containsKey(var0);
   }

   public static String[] щ() {
      return Х.values().toArray(new String[0]);
   }

   public static boolean Ь(int var0) {
      int var1 = var0 - уц;
      if (var1 >= 0 && var1 <= 99) {
         int var2 = var1 >>> 4;
         int var3 = var1 & 15;
         return var2 < 6 ? var3 <= 9 : var2 == 6 && var3 <= 3;
      } else {
         return false;
      }
   }

   public static String Ч(int var0) {
      return Х.getOrDefault((char)var0, уя);
   }

   public static int 必(int var0, int var1, int var2, float var3, int var4) {
      if (var0 >= 0 && var0 < Я.length && Я[var0]) {
         int[] var5 = се.get(var0);
         if (var5 == null) {
            return ColorMath.withAlpha(var4, var3);
         }

         int var6 = var5[0];
         int var7 = var5[1];
         float var8 = var2 <= 1 ? 0.0F : (float)var1 / (var2 - 1);
         int var9 = ColorMath.lerpArgb(var6, var7, var8);
         return ColorMath.withAlpha(var9, var3);
      } else {
         return ColorMath.withAlpha(var4, var3);
      }
   }

   private static void 必(char var0, int var1, int var2) {
      се.put(Integer.valueOf(var0), new int[]{var1, var2});
   }

   static {
      for (int var3 : а) {
         if (var3 >= 0 && var3 < Я.length) {
            Я[var3] = true;
         }
      }

      сХ.put(field_2в, field_2ъ);
      сХ.put(field_2М, field_2Н);
      сХ.put(field_2н, field_2Ц);
      сХ.put(field_2ч, field_2Э);
      сХ.put(field_2必, field_2щ);
      сХ.put(field_2Г, field_2л);
      сХ.put(field_2э, field_2ю);
      сХ.put(field_2Щ, field_2у);
      сХ.put(field_22, field_2ф);
      сХ.put(field_2и, field_29);
      сХ.put(field_2о, field_2O);
      сХ.put(field_27, field_2Р);
      сХ.put(field_23, field_2р);
      сХ.put(field_26, field_2_);
      сХ.put(field_2Ш, field_2ы);
      сХ.put(field_2й, field_2Ж);
      сХ.put(field_2м, field_2К);
      сХ.put(field_2I, field_2Т);
      сХ.put(field_2О, field_2Ф);
      сХ.put(field_2Й, field_2к);
      сХ.put(field_2г, field_20);
      сХ.put(field_2х, field_2У);
      сХ.put(field_2З, field_2Е);
      сХ.put(field_2П, field_2西);
      сХ.put(field_2д, field_2Б);
      сХ.put(field_2С, field_2з);
      сХ.put(field_2ц, field_2я);
      сХ.put(фс, фВ);
      сХ.put(фЯ, фХ);
      сХ.put(9679, фе);
      сХ.put(9733, фА);
      сХ.put(7424, ф4);
      сХ.put(665, фп);
      сХ.put(7428, фД);
      сХ.put(7429, фЪ);
      сХ.put(7431, фт);
      сХ.put(фа, фЮ);
      сХ.put(610, фЧ);
      сХ.put(668, фЛ);
      сХ.put(618, ф诶);
      сХ.put(7434, ф1);
      сХ.put(7435, фЬ);
      сХ.put(671, ф衣);
      сХ.put(7437, фш);
      сХ.put(628, фИ);
      сХ.put(7439, фь);
      сХ.put(7448, фж);
      сХ.put(491, ф弟);
      сХ.put(640, фб);
      сХ.put(7451, фЫ);
      сХ.put(7452, фl);
      сХ.put(ф8, ф5);
      сХ.put(7456, фв);
      сХ.put(7457, фъ);
      сХ.put(7521, фМ);
      сХ.put(655, фН);
      сХ.put(7458, фн);
      必((char)фЦ, ColorMath.packRgb(160, 160, 160), ColorMath.packRgb(90, 90, 90));
      必((char)фч, ColorMath.packRgb(130, 145, 255), ColorMath.packRgb(70, 85, 200));
      必((char)фЭ, ColorMath.packRgb(250, 235, 90), ColorMath.packRgb(170, 145, 20));
      必((char)ф必, ColorMath.packRgb(140, 220, 90), ColorMath.packRgb(60, 140, 40));
      必((char)фщ, ColorMath.packRgb(120, 255, 255), ColorMath.packRgb(30, 170, 200));
      必((char)фГ, ColorMath.packRgb(230, 160, 90), ColorMath.packRgb(150, 90, 30));
      必((char)фл, ColorMath.packRgb(230, 90, 90), ColorMath.packRgb(140, 30, 30));
      必((char)фэ, ColorMath.packRgb(255, 90, 245), ColorMath.packRgb(170, 30, 160));
      必((char)фю, ColorMath.packRgb(250, 235, 90), ColorMath.packRgb(170, 145, 20));
      必((char)фЩ, ColorMath.packRgb(160, 110, 230), ColorMath.packRgb(80, 40, 150));
      必((char)фу, ColorMath.packRgb(230, 160, 90), ColorMath.packRgb(150, 90, 30));
      必((char)ф2, ColorMath.packRgb(230, 90, 90), ColorMath.packRgb(140, 30, 30));
      必((char)фф, ColorMath.packRgb(110, 110, 110), ColorMath.packRgb(40, 40, 40));
      必((char)фи, ColorMath.packRgb(160, 240, 110), ColorMath.packRgb(80, 160, 50));
      必((char)ф9, ColorMath.packRgb(150, 190, 40), ColorMath.packRgb(60, 90, 5));
      必((char)фо, ColorMath.packRgb(160, 160, 160), ColorMath.packRgb(90, 90, 90));
      必((char)фO, ColorMath.packRgb(250, 235, 90), ColorMath.packRgb(170, 145, 20));
      必((char)ф7, ColorMath.packRgb(130, 145, 255), ColorMath.packRgb(70, 85, 200));
      必((char)фР, ColorMath.packRgb(130, 145, 255), ColorMath.packRgb(70, 85, 200));
      必((char)ф3, ColorMath.packRgb(160, 110, 230), ColorMath.packRgb(80, 40, 150));
      必((char)фр, ColorMath.packRgb(130, 145, 255), ColorMath.packRgb(70, 85, 200));
      必((char)ф6, ColorMath.packRgb(160, 110, 230), ColorMath.packRgb(80, 40, 150));
      必((char)ф_, ColorMath.packRgb(100, 190, 240), ColorMath.packRgb(30, 100, 160));
      必((char)фШ, ColorMath.packRgb(230, 90, 90), ColorMath.packRgb(140, 30, 30));
      必((char)фы, ColorMath.packRgb(160, 110, 230), ColorMath.packRgb(80, 40, 150));
      必((char)фй, ColorMath.packRgb(255, 255, 255), ColorMath.packRgb(180, 180, 180));
      必((char)фЖ, ColorMath.packRgb(250, 235, 90), ColorMath.packRgb(170, 145, 20));
      必((char)фм, ColorMath.packRgb(200, 30, 30), ColorMath.packRgb(90, 0, 0));
      必((char)фК, ColorMath.packRgb(255, 230, 90), ColorMath.packRgb(180, 130, 0));
      Х.put(Character.valueOf((char)фI), фТ);
      Х.put(Character.valueOf((char)фО), фФ);
      Х.put(Character.valueOf((char)фЙ), фк);
      Х.put(Character.valueOf((char)фг), ф0);
      Х.put(Character.valueOf((char)фх), фУ);
      Х.put(Character.valueOf((char)фЗ), фЕ);
      Х.put(Character.valueOf((char)фП), ф西);
      Х.put(Character.valueOf((char)фд), фБ);
      Х.put(Character.valueOf((char)фС), фз);
      Х.put(Character.valueOf((char)фц), фя);
      Х.put(Character.valueOf((char)ис), иВ);
      Х.put(Character.valueOf((char)иЯ), иХ);
      Х.put(Character.valueOf((char)ие), иА);
      Х.put(Character.valueOf((char)и4), ип);
      Х.put(Character.valueOf((char)иД), иЪ);
      Х.put(Character.valueOf((char)ит), иа);
      Х.put(Character.valueOf((char)иЮ), иЧ);
      Х.put(Character.valueOf((char)иЛ), и诶);
      Х.put(Character.valueOf((char)и1), иЬ);
      Х.put(Character.valueOf((char)и衣), иш);
      Х.put(Character.valueOf((char)иИ), иь);
      Х.put(Character.valueOf((char)иж), и弟);
      Х.put(Character.valueOf((char)иб), иЫ);
      Х.put(Character.valueOf((char)иl), и8);
      Х.put(Character.valueOf((char)и5), ив);
      Х.put(Character.valueOf((char)иъ), иМ);
      Х.put(Character.valueOf((char)иН), ин);
      Х.put(Character.valueOf((char)иЦ), ич);
      Х.put(Character.valueOf((char)иЭ), и必);
   }

   public static char 必(String var0) {
      for (Entry var2 : Х.entrySet()) {
         if (((String)var2.getValue()).equalsIgnoreCase(var0)) {
            return (Character)var2.getKey();
         }
      }

      return Х.keySet().iterator().next();
   }
}
