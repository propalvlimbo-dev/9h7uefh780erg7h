package sg.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class CosmeticAnimationClip {
   @SerializedName("4Ъ")
   private final float field_4Ъ;
   private final boolean Ящ;
   private final Map<String, CosmeticAnimationClip.BoneTracks> св;
   private static final float В5я = 0.001F;
   private static final String Ввс = "animations";
   private static final String ВвВ = "animations";
   private static final String ВвЯ = "animation_length";
   private static final String ВвХ = "animation_length";
   private static final String Вве = "loop";
   private static final String ВвА = "loop";
   private static final String Вв4 = "loop";
   private static final String Ввп = "loop";
   private static final String ВвД = "loop";
   private static final String ВвЪ = "loop";
   private static final String Ввт = "bones";
   private static final String Вва = "bones";
   private static final String ВвЮ = "rotation";
   private static final String ВвЧ = "position";
   private static final String ВвЛ = "scale";
   private static final String Вв诶 = "vector";
   private static final String Вв1 = "post";
   private static final String ВвЬ = "pre";
   private static final String Вв衣 = "post";
   private static final String Ввш = "post";
   private static final String ВвИ = "vector";
   private static final String Ввь = "vector";
   private static final String Ввж = "pre";
   private static final String Вв弟 = "pre";
   private static final float Ввб = 1.0E-4F;

   public CosmeticAnimationClip.TransformSample э(String var1, float var2) {
      CosmeticAnimationClip.BoneTracks var3 = this.св.get(var1);
      if (var3 == null) {
         return CosmeticAnimationClip.TransformSample.EMPTY;
      }

      float var4 = this.Ящ ? var2 % this.field_4Ъ : Math.min(var2, this.field_4Ъ);
      return new CosmeticAnimationClip.TransformSample(э(var3.rotation, var4), э(var3.position, var4), э(var3.scale, var4));
   }

   private CosmeticAnimationClip(float var1, boolean var2, Map<String, CosmeticAnimationClip.BoneTracks> var3) {
      this.field_4Ъ = Math.max(В5я, var1);
      this.Ящ = var2;
      this.св = var3;
   }

   private static CosmeticAnimationClip э(JsonObject var0) {
      float var1 = var0.has(ВвЯ) ? var0.get(ВвХ).getAsFloat() : 1.0F;
      boolean var2 = var0.has(Вве)
         && var0.get(ВвА).isJsonPrimitive()
         && (var0.get(Вв4).getAsJsonPrimitive().isBoolean() ? var0.get(Ввп).getAsBoolean() : ВвД.equalsIgnoreCase(var0.get(ВвЪ).getAsString()));
      HashMap var3 = new HashMap();
      if (var0.has(Ввт)) {
         for (Entry var5 : var0.getAsJsonObject(Вва).entrySet()) {
            JsonObject var6 = ((JsonElement)var5.getValue()).getAsJsonObject();
            var3.put((String)var5.getKey(), new CosmeticAnimationClip.BoneTracks(э_2571477d(var6.get(ВвЮ)), э_2571477d(var6.get(ВвЧ)), э_2571477d(var6.get(ВвЛ))));
         }
      }

      return new CosmeticAnimationClip(var1, var2, var3);
   }

   public static CosmeticAnimationClip э_553f84e9(InputStream var0) {
      Map var1 = э_ea411899(var0);
      return var1.isEmpty() ? null : (CosmeticAnimationClip)var1.values().iterator().next();
   }

   private static List<CosmeticAnimationClip.VectorKeyframe> э_2571477d(JsonElement var0) {
      if (var0 == null || var0.isJsonNull()) {
         return List.of();
      }

      if (var0.isJsonArray()) {
         return List.of(new CosmeticAnimationClip.VectorKeyframe(0.0F, э_06b4556e(var0)));
      }

      JsonObject var1 = var0.getAsJsonObject();
      if (!var1.has(Вв诶) && !var1.has(Вв1) && !var1.has(ВвЬ)) {
         ArrayList var2 = new ArrayList();

         for (Entry var4 : var1.entrySet()) {
            try {
               var2.add(new CosmeticAnimationClip.VectorKeyframe(Float.parseFloat((String)var4.getKey()), э_06b4556e((JsonElement)var4.getValue())));
            } catch (NumberFormatException var6) {
            }
         }

         var2.sort(Comparator.comparingDouble(CosmeticAnimationClip.VectorKeyframe::time));
         return var2;
      } else {
         return List.of(new CosmeticAnimationClip.VectorKeyframe(0.0F, э_06b4556e(var1)));
      }
   }

   private static float[] э_06b4556e(JsonElement var0) {
      if (var0 == null || var0.isJsonNull()) {
         return new float[]{0.0F, 0.0F, 0.0F};
      } else if (var0.isJsonArray()) {
         JsonArray var2 = var0.getAsJsonArray();
         return new float[]{
            var2.size() > 0 ? э_f67ab10a(var2.get(0)) : 0.0F,
            var2.size() > 1 ? э_f67ab10a(var2.get(1)) : 0.0F,
            var2.size() > 2 ? э_f67ab10a(var2.get(2)) : 0.0F
         };
      } else {
         JsonObject var1 = var0.getAsJsonObject();
         if (var1.has(Вв衣)) {
            return э_06b4556e(var1.get(Ввш));
         } else if (var1.has(ВвИ)) {
            return э_06b4556e(var1.get(Ввь));
         } else {
            return var1.has(Ввж) ? э_06b4556e(var1.get(Вв弟)) : new float[]{0.0F, 0.0F, 0.0F};
         }
      }
   }

   private static float[] э(List<CosmeticAnimationClip.VectorKeyframe> var0, float var1) {
      if (var0.isEmpty()) {
         return null;
      }

      if (var0.size() != 1 && !(var1 <= ((CosmeticAnimationClip.VectorKeyframe)var0.getFirst()).time)) {
         if (var1 >= ((CosmeticAnimationClip.VectorKeyframe)var0.getLast()).time) {
            return (float[])((CosmeticAnimationClip.VectorKeyframe)var0.getLast()).value.clone();
         }

         for (int var2 = 0; var2 < var0.size() - 1; var2++) {
            CosmeticAnimationClip.VectorKeyframe var3 = (CosmeticAnimationClip.VectorKeyframe)var0.get(var2);
            CosmeticAnimationClip.VectorKeyframe var4 = (CosmeticAnimationClip.VectorKeyframe)var0.get(var2 + 1);
            if (!(var1 < var3.time) && !(var1 > var4.time)) {
               float var5 = (var1 - var3.time) / Math.max(Ввб, var4.time - var3.time);
               return new float[]{
                  var3.value[0] + (var4.value[0] - var3.value[0]) * var5,
                  var3.value[1] + (var4.value[1] - var3.value[1]) * var5,
                  var3.value[2] + (var4.value[2] - var3.value[2]) * var5
               };
            }
         }

         return (float[])((CosmeticAnimationClip.VectorKeyframe)var0.getLast()).value.clone();
      } else {
         return (float[])((CosmeticAnimationClip.VectorKeyframe)var0.getFirst()).value.clone();
      }
   }

   private static float э_f67ab10a(JsonElement var0) {
      if (var0 != null && var0.isJsonPrimitive()) {
         try {
            return var0.getAsFloat();
         } catch (Exception var2) {
            return 0.0F;
         }
      } else {
         return 0.0F;
      }
   }

   public static Map<String, CosmeticAnimationClip> э_ea411899(InputStream var0) {
      JsonObject var1 = JsonParser.parseReader(new InputStreamReader(var0, StandardCharsets.UTF_8)).getAsJsonObject();
      LinkedHashMap var2 = new LinkedHashMap();
      if (!var1.has(Ввс)) {
         return var2;
      }

      JsonObject var3 = var1.getAsJsonObject(ВвВ);

      for (Entry var5 : var3.entrySet()) {
         if (((JsonElement)var5.getValue()).isJsonObject()) {
            CosmeticAnimationClip var6 = э(((JsonElement)var5.getValue()).getAsJsonObject());
            if (var6 != null) {
               var2.put((String)var5.getKey(), var6);
            }
         }
      }

      return var2;
   }

   private record BoneTracks(List<CosmeticAnimationClip.VectorKeyframe> rotation, List<CosmeticAnimationClip.VectorKeyframe> position, List<CosmeticAnimationClip.VectorKeyframe> scale) {
   }

   private record VectorKeyframe(float time, float[] value) {
   }

   public record TransformSample(float[] rotation, float[] position, float[] scale) {
      private static final CosmeticAnimationClip.TransformSample EMPTY = new CosmeticAnimationClip.TransformSample(null, null, null);
   }
}
