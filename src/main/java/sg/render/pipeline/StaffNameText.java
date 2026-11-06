package sg.render.pipeline;

import java.util.Locale;
import settings.render.PlayerNamePatterns;
import sg.ec.FontRenderer;

public class StaffNameText {
   private static final String рЩ = "owner";
   private static final String ру = "admin";
   private static final String р2 = "админ";
   private static final String рф = "curator";
   private static final String ри = "куратор";
   private static final String р9 = "moder";
   private static final String ро = "модер";
   private static final String рO = "helper";
   private static final String р7 = "хелпер";
   private static final String рР = "support";
   private static final String р3 = "поддержка";
   private static final String рр = "staff";
   private static final String р6 = "сотрудник";
   private static final String р_ = "стажер";
   private static final String рШ = "стажёр";

   public static int Ч(String var0) {
      if (var0 == null) {
         return 0;
      } else {
         String var1 = var0.toLowerCase(Locale.ROOT);
         if (var1.contains(рЩ)) {
            return 10;
         } else if (var1.contains(ру) || var1.contains(р2)) {
            return 9;
         } else if (var1.contains(рф) || var1.contains(ри)) {
            return 8;
         } else if (var1.contains(р9) || var1.contains(ро)) {
            return 7;
         } else if (var1.contains(рO) || var1.contains(р7)) {
            return 6;
         } else if (var1.contains(рР) || var1.contains(р3)) {
            return 5;
         } else if (var1.contains(рр) || var1.contains(р6)) {
            return 4;
         } else {
            return !var1.contains(р_) && !var1.contains(рШ) ? 0 : 3;
         }
      }
   }

   public static String I(FontRenderer var0, String var1, float var2, int var3) {
      int var4 = 0;

      while (var4 < var1.length()) {
         int var5 = var1.offsetByCodePoints(var4, 1);
         if (var0.getFontFace().measureTextWidth(var1.substring(0, var5), var3, 0.0F) > var2) {
            break;
         }

         var4 = var5;
      }

      return var1.substring(0, var4);
   }

   public static boolean ЫO(String var0) {
      return var0 != null && PlayerNamePatterns.ж().matcher(var0.toLowerCase(Locale.ROOT)).matches();
   }
}
