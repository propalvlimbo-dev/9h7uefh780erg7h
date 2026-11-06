package sg.ec;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map.Entry;
import settings.render.ClientLocalization;

public final class GuiEnglishTranslations {
   private static final String сЛЬ = "assets/rose/lang/gui_en.json";
   private static final String сЛ衣 = "[Lang] gui_en.json not found";

   public static void еВ() {
      HashMap var0 = new HashMap();

      try {
         label67: {
            try (InputStream var1 = GuiEnglishTranslations.class.getClassLoader().getResourceAsStream(сЛЬ)) {
               if (var1 != null) {
                  JsonObject var2 = JsonParser.parseReader(new InputStreamReader(var1, StandardCharsets.UTF_8)).getAsJsonObject();

                  for (Entry var4 : var2.entrySet()) {
                     if (((JsonElement)var4.getValue()).isJsonPrimitive()) {
                        var0.put((String)var4.getKey(), ((JsonElement)var4.getValue()).getAsString());
                     }
                  }
                  break label67;
               }

               System.err.println(сЛ衣);
               ClientLocalization.replaceEnglishTranslations(var0);
            }

            return;
         }
      } catch (Throwable var7) {
         System.err.println("[Lang] Failed to load gui_en.json: " + var7.getMessage());
      }

      ClientLocalization.replaceEnglishTranslations(var0);
      System.out.println("[Lang] Loaded " + var0.size() + " EN strings");
   }

   private GuiEnglishTranslations() {
   }
}
