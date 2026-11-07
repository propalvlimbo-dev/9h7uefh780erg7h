package sg.modules.impl.init;

import com.google.gson.JsonObject;
import settings.render.ClientLocalization;

public final class ClickGuiPreferences {
   private static boolean Вь;
   private static boolean Вж = true;
   private static float Х必 = 1.0F;
   private static int Яд = 2;
   private static ClickGuiPreferences.Layout В = ClickGuiPreferences.Layout.DEFAULT;
   private static final float с衣0 = 100.0F;
   private static final float с衣х = 100.0F;
   private static final float с衣У = 0.7F;
   private static final float с衣З = 1.2F;
   private static final String с衣Е = "Language";
   private static final String с衣П = "English";
   private static final String с衣西 = "Русский";
   private static final String с衣д = "Hints";
   private static final String с衣Б = "Scale";
   private static final String с衣С = "Quality";
   private static final String с衣з = "Layout";
   private static final String с衣ц = "Language";
   private static final String с衣я = "English";
   private static final String сшс = "Language";
   private static final String сшВ = "Hints";
   private static final String сшЯ = "Hints";
   private static final String сшХ = "Scale";
   private static final String сше = "Scale";
   private static final String сшА = "Quality";
   private static final String сш4 = "Quality";
   private static final String сшп = "Layout";
   private static final String сшД = "Layout";
   private static final String сшЪ = "Layout";

   public static ClickGuiPreferences.Layout 衣() {
      return В;
   }

   public static int х弟() {
      return Яд;
   }

   public static JsonObject Е() {
      JsonObject var0 = new JsonObject();
      var0.addProperty(с衣Е, Вь ? с衣П : с衣西);
      var0.addProperty(с衣д, Вж);
      var0.addProperty(с衣Б, Х必);
      var0.addProperty(с衣С, Яд);
      var0.addProperty(с衣з, В.Я弟);
      return var0;
   }

   public static float х8() {
      return Х必;
   }

   public static void 衣(ClickGuiPreferences.Layout var0) {
      В = var0 == null ? ClickGuiPreferences.Layout.DEFAULT : var0;
   }

   public static void 必(int var0) {
      Яд = Math.clamp(var0, 0, 2);
   }

   public static void Н(boolean var0) {
      Вь = var0;
      ClientLocalization.setEnglishEnabled(var0);
   }

   public static boolean йЩ() {
      // Подсказки держим включёнными всегда: в старых конфигах флаг мог сохраниться выключенным,
      // а пункта для его включения в интерфейсе нет.
      return true;
   }

   public static void т(boolean var0) {
      Вж = var0;
   }

   public static void о(float var0) {
      if (Float.isFinite(var0)) {
         Х必 = Math.clamp(Math.round(var0 * с衣0) / с衣х, с衣У, с衣З);
      }
   }

   public static void э(JsonObject var0) {
      if (var0 != null) {
         if (var0.has(с衣ц)) {
            Н(с衣я.equals(var0.get(сшс).getAsString()));
         }

         if (var0.has(сшВ)) {
            т(var0.get(сшЯ).getAsBoolean());
         }

         if (var0.has(сшХ)) {
            о(var0.get(сше).getAsFloat());
         }

         if (var0.has(сшА)) {
            必(var0.get(сш4).getAsInt());
         }

         В = ClickGuiPreferences.Layout.DEFAULT;
         if (var0.has(сшп) && var0.get(сшД).isJsonPrimitive()) {
            String var1 = var0.get(сшЪ).getAsString();

            for (ClickGuiPreferences.Layout var5 : ClickGuiPreferences.Layout.values()) {
               if (var5.Я弟.equals(var1)) {
                  В = var5;
                  break;
               }
            }
         }
      }
   }

   private ClickGuiPreferences() {
   }

   public static boolean й_() {
      return Вь;
   }

   public enum Layout {
      COMPACT("compact", "Компактный", "Compact", "\ue121", true, false),
      COMPACT_SETTINGS("compact_settings", "Компактный с настройками", "Compact with settings", "\ue122", true, true),
      FULL_SETTINGS("full_settings", "Полный с настройками", "Full with settings", "\ue123", false, true),
      DEFAULT("default", "Обычный", "Default", "\ue124", false, false);

      private final String Я弟;
      private final String Яб;
      private final String ЯЫ;
      private final String Яl;
      private final boolean еж;
      private final boolean е弟;

      Layout(String var3, String var4, String var5, String var6, boolean var7, boolean var8) {
         this.Я弟 = var3;
         this.Яб = var4;
         this.ЯЫ = var5;
         this.Яl = var6;
         this.еж = var7;
         this.е弟 = var8;
      }

      public String method_67() {
         return ClientLocalization.selectLocalizedText(this.Яб, this.ЯЫ);
      }

      public String method_60() {
         return this.Яl;
      }

      public boolean Пж() {
         return this.еж;
      }

      public boolean ПЪ() {
         return this.е弟;
      }
   }
}
