package sg.modules.impl.init;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

public final class InventoryPresetStore {
   public static final int пВ = 32;
   private static final int пЯ = 64;
   private static final String Вя = "Основной";
   private static final Set<String> л = Set.of(InventoryPresetStore.зю, InventoryPresetStore.зЩ, InventoryPresetStore.зу, InventoryPresetStore.з2, InventoryPresetStore.зф);
   private final Map<String, List<InventoryPresetStore.SlotLayout>> сю = new LinkedHashMap<>();
   private final Map<String, InventoryPresetStore.ServerMode> сЩ = new LinkedHashMap<>();
   private final Map<String, Deque<List<InventoryPresetStore.SlotLayout>>> су = new LinkedHashMap<>();
   private final Map<String, Deque<List<InventoryPresetStore.SlotLayout>>> с2 = new LinkedHashMap<>();
   private String Яс;
   private static final String Ся = "Основной";
   private static final String зс = "Оставь хотя бы один пресет";
   private static final String зВ = "Введи название пресета";
   private static final String зЯ = "Название: не больше 32 символов";
   private static final String зХ = "Пресет с таким названием уже есть";
   private static final String зе = "[\\p{Cntrl}§]";
   private static final String зА = "\\s+";
   private static final String з4 = " ";
   private static final String зп = "version";
   private static final String зД = "activePreset";
   private static final String зЪ = "presets";
   private static final String зт = "presetModes";
   private static final String за = "presets";
   private static final String зЮ = "slots";
   private static final String зЧ = "slots";
   private static final String зЛ = "Missing inventory slots";
   private static final String з诶 = "slots";
   private static final String з1 = "presets";
   private static final String зЬ = "version";
   private static final String з衣 = "version";
   private static final String зш = "Unsupported inventory presets format";
   private static final String зИ = "presets";
   private static final String зь = "presetModes";
   private static final String зж = "presetModes";
   private static final String з弟 = "presetModes";
   private static final String зб = "No valid inventory presets";
   private static final String зЫ = "activePreset";
   private static final String зl = "activePreset";
   private static final String з8 = "activePreset";
   private static final String з5 = "item";
   private static final String зв = "(?:[a-z0-9_.-]+:)?[a-z0-9/._-]+";
   private static final String зъ = "name";
   private static final String зМ = "name";
   private static final String зН = "count";
   private static final String зн = "count";
   private static final String зЦ = "kind";
   private static final String зч = "kind";
   private static final String зЭ = "NORMAL";
   private static final String з必 = "NORMAL";
   private static final String зщ = "item";
   private static final String зГ = "name";
   private static final String зл = "count";
   private static final String зэ = "kind";
   private static final String зю = "NORMAL";
   private static final String зЩ = "BALL";
   private static final String зу = "PUMPKIN";
   private static final String з2 = "POTION";
   private static final String зф = "FUNTIME";

   public InventoryPresetStore() {
      this.Яс = Ся;
      this.сю.put(this.Яс, List.of());
   }

   private boolean ж(Map<String, Deque<List<InventoryPresetStore.SlotLayout>>> var1, Map<String, Deque<List<InventoryPresetStore.SlotLayout>>> var2) {
      Deque var3 = (Deque)var1.get(this.Яс);
      if (var3 != null && !var3.isEmpty()) {
         this.ж(var2, this.Ы());
         this.сю.put(this.Яс, (List<InventoryPresetStore.SlotLayout>)var3.removeLast());
         return true;
      } else {
         return false;
      }
   }

   public void П(InventoryPresetStore.ServerMode var1) {
      this.сЩ.put(this.Яс, Objects.requireNonNull(var1));
   }

   public void method_1(String var1) {
      String var2 = this.Ж(var1, this.Яс);
      this.сЩ.put(var2, this.сЩ.getOrDefault(this.Яс, InventoryPresetStore.ServerMode.REALLY_WORLD));
      if (!this.Яс.equals(var2)) {
         this.сЩ.remove(this.Яс);
         if (this.су.containsKey(this.Яс)) {
            this.су.put(var2, this.су.remove(this.Яс));
         }

         if (this.с2.containsKey(this.Яс)) {
            this.с2.put(var2, this.с2.remove(this.Яс));
         }
      }

      LinkedHashMap var3 = new LinkedHashMap();
      this.сю.forEach((var3x, var4) -> var3.put(var3x.equals(this.Яс) ? var2 : var3x, var4));
      this.сю.clear();
      this.сю.putAll(var3);
      this.Яс = var2;
   }

   public InventoryPresetStore.ServerMode П() {
      return this.сЩ.getOrDefault(this.Яс, InventoryPresetStore.ServerMode.REALLY_WORLD);
   }

   public String ПЭ() {
      return this.Яс;
   }

   public void Ж(List<InventoryPresetStore.SlotLayout> var1) {
      List var2 = List.copyOf(var1);
      if (!var2.equals(this.Ы())) {
         this.ж(this.су, this.Ы());
         this.с2.remove(this.Яс);
         this.сю.put(this.Яс, var2);
      }
   }

   public boolean ЖР() {
      return this.су.containsKey(this.Яс) && !this.су.get(this.Яс).isEmpty();
   }

   public List<String> ъ() {
      return List.copyOf(this.сю.keySet());
   }

   public boolean ч(String var1) {
      if (this.сю.containsKey(var1) && !this.Яс.equals(var1)) {
         this.Яс = var1;
         return true;
      } else {
         return false;
      }
   }

   public boolean Жю() {
      return this.ж(this.су, this.с2);
   }

   private static String ъ(String var0) {
      return var0 == null ? "" : var0.replaceAll(зе, "").strip().replaceAll(зА, з4);
   }

   private void ж(Map<String, Deque<List<InventoryPresetStore.SlotLayout>>> var1, List<InventoryPresetStore.SlotLayout> var2) {
      Deque var3 = var1.computeIfAbsent(this.Яс, var0 -> new ArrayDeque());
      if (var3.size() >= 64) {
         var3.removeFirst();
      }

      var3.addLast(var2);
   }

   public void method_8ф() {
      if (this.сю.size() <= 1) {
         throw new IllegalArgumentException(зс);
      }

      this.сю.remove(this.Яс);
      this.сЩ.remove(this.Яс);
      this.су.remove(this.Яс);
      this.с2.remove(this.Яс);
      this.Яс = this.сю.keySet().iterator().next();
   }

   public JsonObject Ф() {
      JsonObject var1 = new JsonObject();
      var1.addProperty(зп, 3);
      var1.addProperty(зД, this.Яс);
      JsonObject var2 = new JsonObject();
      this.сю.forEach((var1x, var2x) -> {
         JsonObject var3x = new JsonObject();

         for (InventoryPresetStore.SlotLayout var5 : var2x) {
            JsonObject var6 = new JsonObject();
            var6.addProperty(зщ, var5.item());
            var6.addProperty(зГ, var5.name());
            var6.addProperty(зл, var5.count());
            var6.addProperty(зэ, var5.kind());
            var3x.add(String.valueOf(var5.index()), var6);
         }

         var2.add(var1x, var3x);
      });
      var1.add(зЪ, var2);
      JsonObject var3 = new JsonObject();
      this.сю.keySet().forEach(var2x -> var3.addProperty(var2x, this.сЩ.getOrDefault(var2x, InventoryPresetStore.ServerMode.REALLY_WORLD).name()));
      var1.add(зт, var3);
      return var1;
   }

   private String Ж(String var1, String var2) {
      String var3 = ъ(var1);
      if (var3.isEmpty()) {
         throw new IllegalArgumentException(зВ);
      }

      if (var3.codePointCount(0, var3.length()) > 32) {
         throw new IllegalArgumentException(зЯ);
      }

      for (String var5 : this.сю.keySet()) {
         if (!var5.equals(var2) && var5.equalsIgnoreCase(var3)) {
            throw new IllegalArgumentException(зХ);
         }
      }

      return var3;
   }

   public boolean Жэ() {
      return this.с2.containsKey(this.Яс) && !this.с2.get(this.Яс).isEmpty();
   }

   public List<InventoryPresetStore.SlotLayout> Ы() {
      return this.сю.get(this.Яс);
   }

   public static InventoryPresetStore ж_3a79a21b(JsonObject var0) {
      InventoryPresetStore var1 = new InventoryPresetStore();
      if (!var0.has(за)) {
         if (var0.has(зЮ) && var0.get(зЧ).isJsonObject()) {
            var1.сю.put(var1.Яс, ж_2571477d(var0.getAsJsonObject(з诶)));
            return var1;
         } else {
            throw new IllegalArgumentException(зЛ);
         }
      } else if (var0.get(з1).isJsonObject() && (!var0.has(зЬ) || var0.get(з衣).getAsInt() <= 3)) {
         var1.сю.clear();

         for (Entry var3 : var0.getAsJsonObject(зИ).entrySet()) {
            if (((JsonElement)var3.getValue()).isJsonObject()) {
               try {
                  String var4 = var1.Ж((String)var3.getKey(), null);
                  var1.сю.put(var4, ж_2571477d(((JsonElement)var3.getValue()).getAsJsonObject()));
                  if (var0.has(зь) && var0.get(зж).isJsonObject()) {
                     JsonElement var5 = var0.getAsJsonObject(з弟).get((String)var3.getKey());
                     if (var5 != null && var5.isJsonPrimitive()) {
                        try {
                           var1.сЩ.put(var4, InventoryPresetStore.ServerMode.valueOf(var5.getAsString()));
                        } catch (IllegalArgumentException var7) {
                        }
                     }
                  }
               } catch (IllegalArgumentException var8) {
               }
            }
         }

         if (var1.сю.isEmpty()) {
            throw new IllegalArgumentException(зб);
         }

         String var9 = var0.has(зЫ) && var0.get(зl).isJsonPrimitive() ? var0.get(з8).getAsString() : "";
         var1.Яс = var1.сю.containsKey(var9) ? var9 : var1.сю.keySet().iterator().next();
         return var1;
      } else {
         throw new IllegalArgumentException(зш);
      }
   }

   private static List<InventoryPresetStore.SlotLayout> ж_2571477d(JsonObject var0) {
      ArrayList var1 = new ArrayList();

      for (Entry var3 : var0.entrySet()) {
         try {
            int var4 = Integer.parseInt((String)var3.getKey());
            if (var4 >= 0 && var4 < 41 && ((JsonElement)var3.getValue()).isJsonObject()) {
               JsonObject var5 = ((JsonElement)var3.getValue()).getAsJsonObject();
               String var6 = var5.get(з5).getAsString();
               if (var6.matches(зв)) {
                  String var7 = var5.has(зъ) ? var5.get(зМ).getAsString() : "";
                  int var8 = var5.has(зН) ? Math.max(1, var5.get(зн).getAsInt()) : 1;
                  String var9 = var5.has(зЦ) ? var5.get(зч).getAsString() : зЭ;
                  var1.add(new InventoryPresetStore.SlotLayout(var4, var6, var7, var8, л.contains(var9) ? var9 : з必));
               }
            }
         } catch (RuntimeException var10) {
         }
      }

      return List.copyOf(var1);
   }

   public boolean ЖЙ() {
      return this.ж(this.с2, this.су);
   }

   public void ш(String var1) {
      String var2 = this.Ж(var1, null);
      this.сю.put(var2, List.copyOf(this.Ы()));
      this.сЩ.put(var2, this.П());
      this.Яс = var2;
   }

   public record SlotLayout(int index, String item, String name, int count, String kind) {
   }

   public enum ServerMode {
      REALLY_WORLD("ReallyWorld"),
      FUNTIME("Funtime");

      private final String В6;

      ServerMode(String var3) {
         this.В6 = var3;
      }

      public String щ弟() {
         return this.В6;
      }
   }
}
