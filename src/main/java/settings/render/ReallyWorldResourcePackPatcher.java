package settings.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import java.nio.charset.StandardCharsets;
import net.minecraft.server.packs.OverlayMetadataSection;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

public final class ReallyWorldResourcePackPatcher {
   private static final int Хи = 88;
   private static final int Х9 = 3;
   private static final String сЗ = "rose_rwfixer_revision";
   private static final JsonObject а = JsonParser.parseString(ReallyWorldResourcePackPatcher.сIф).getAsJsonObject();
   private static final String сКк = "(?:.*/)?assets/[^/]+/items/.+\\.json";
   private static final String сКг = "type";
   private static final String сК0 = "special";
   private static final String сКх = "model";
   private static final String сКУ = "type";
   private static final String сКЗ = "head";
   private static final String сКЕ = "kind";
   private static final String сКП = "player";
   private static final String сК西 = "type";
   private static final String сКд = "minecraft:player_head";
   private static final String сКБ = "type";
   private static final String сКС = "player_head";
   private static final String сКз = "kind";
   private static final String сКц = "transformation";
   private static final String сКя = "transformation";
   private static final String сIс = "type";
   private static final String сIВ = "range_dispatch";
   private static final String сIЯ = "property";
   private static final String сIХ = "custom_model_data";
   private static final String сIе = "index";
   private static final String сIА = "index";
   private static final String сI4 = "pack";
   private static final String сIп = "rose_rwfixer_revision";
   private static final String сIД = "rose_rwfixer_revision";
   private static final String сIЪ = "min_format";
   private static final String сIт = "max_format";
   private static final String сIа = "{\n  \"pack\": {\"description\": \"ReallyWorld 26.2 (Rose fixed)\"},\n  \"overlays\": {\"entries\": [{\"directory\": \"1_21_2\"}]},\n  \"sodium\": {\n    \"ignored_shaders\": [\n      \"rendertype_entity_translucent.fsh\",\n      \"rendertype_entity_translucent.vsh\"\n    ]\n  }\n}\n";
   private static final String сIЮ = "pack";
   private static final String сIЧ = "description";
   private static final String сIЛ = "description";
   private static final String сI诶 = "ReallyWorld 26.2 (RwFixed)";
   private static final String сI1 = "pack_format";
   private static final String сIЬ = "supported_formats";
   private static final String сI衣 = "min_format";
   private static final String сIш = "max_format";
   private static final String сIИ = "overlays";
   private static final String сIь = "entries";
   private static final String сIж = "formats";
   private static final String сI弟 = "min_format";
   private static final String сIб = "min_format";
   private static final String сIЫ = "min_inclusive";
   private static final String сIl = "max_format";
   private static final String сI8 = "max_format";
   private static final String сI5 = "max_inclusive";
   private static final String сIв = "min_format";
   private static final String сIъ = "min_inclusive";
   private static final String сIМ = "min_format";
   private static final String сIН = "max_inclusive";
   private static final String сIн = "max_format";
   private static final String сIЦ = "formats";
   private static final String сIч = "formats";
   private static final String сIЭ = "sodium";
   private static final String сI必 = "ignored_shaders";
   private static final String сIщ = "rendertype_entity_translucent.fsh";
   private static final String сIГ = "rendertype_entity_translucent.vsh";
   private static final String сIл = "ignored_shaders";
   private static final String сIэ = "rose_rwfixer_revision";
   private static final String сIю = "pack";
   private static final String сIЩ = "overlays";
   private static final String сIу = "overlays";
   private static final String сI2 = "min_inclusive";
   private static final String сIф = "{\n  \"translation\": [0.5, 0.0, 0.5],\n  \"left_rotation\": [1.0, 0.0, 0.0, -0.0],\n  \"scale\": [1.0, 1.0, 1.0],\n  \"right_rotation\": [0.0, 0.0, 0.0, 1.0]\n}\n";

   private static boolean А_bbeebd87(JsonElement var0) {
      boolean var1 = false;
      if (var0.isJsonArray()) {
         for (JsonElement var3 : var0.getAsJsonArray()) {
            var1 |= А_bbeebd87(var3);
         }
      } else if (var0.isJsonObject()) {
         JsonObject var5 = var0.getAsJsonObject();
         if (А(var5, сКг, сК0) && var5.get(сКх) instanceof JsonObject var6) {
            if (А(var6, сКУ, сКЗ) && А(var6, сКЕ, сКП)) {
               var6.addProperty(сК西, сКд);
               var1 = true;
            }

            if (А(var6, сКБ, сКС)) {
               var1 |= var6.remove(сКз) != null;
               if (!var5.has(сКц)) {
                  var5.add(сКя, а.deepCopy());
                  var1 = true;
               }
            }
         }

         if (А(var5, сIс, сIВ) && А(var5, сIЯ, сIХ) && !var5.has(сIе)) {
            var5.addProperty(сIА, 0);
            var1 = true;
         }

         for (JsonElement var8 : var5.asMap().values()) {
            var1 |= А_bbeebd87(var8);
         }
      }

      return var1;
   }

   public static boolean А_bbeebd87(byte[] var0) {
      try {
         JsonObject var1 = JsonParser.parseString(new String(var0, StandardCharsets.UTF_8)).getAsJsonObject();
         JsonObject var2 = var1.getAsJsonObject(сI4);
         boolean var3 = var1.has(сIп) && var1.get(сIД).getAsInt() == 3 && var2.get(сIЪ).getAsInt() == 88 && var2.get(сIт).getAsInt() == 88;
         if (var3) {
            м(var1);
         }

         return var3;
      } catch (RuntimeException var4) {
         return false;
      }
   }

   private static void м(JsonObject var0) {
      PackMetadataSection.CLIENT_TYPE.codec().parse(JsonOps.INSTANCE, var0.get(сIю)).getOrThrow(IllegalArgumentException::new);
      if (var0.has(сIЩ)) {
         OverlayMetadataSection.CLIENT_TYPE.codec().parse(JsonOps.INSTANCE, var0.get(сIу)).getOrThrow(IllegalArgumentException::new);
      }
   }

   private static JsonElement А(JsonElement var0, String var1) {
      if (var0 instanceof JsonObject var2 && var2.has(var1)) {
         return var2.get(var1).deepCopy();
      } else if (var0 != null && var0.isJsonPrimitive()) {
         return var0.deepCopy();
      } else {
         return var0 instanceof JsonArray var3 && !var3.isEmpty() ? var3.get(сI2.equals(var1) ? 0 : var3.size() - 1).deepCopy() : new JsonPrimitive(88);
      }
   }

   private static boolean А(JsonObject var0, String var1, String var2) {
      JsonElement var3 = var0.get(var1);
      return var3 != null && var3.isJsonPrimitive() && (var2.equals(var3.getAsString()) || ("minecraft:" + var2).equals(var3.getAsString()));
   }

   private static JsonObject Щ(JsonObject var0, String var1) {
      if (var0.get(var1) instanceof JsonObject var4) {
         return var4;
      } else {
         JsonObject var2 = new JsonObject();
         var0.add(var1, var2);
         return var2;
      }
   }

   public static byte[] Ъ() {
      JsonObject var0 = JsonParser.parseString(сIа).getAsJsonObject();
      return А(var0);
   }

   public static byte[] Щ(byte[] var0) {
      JsonObject var1;
      try {
         var1 = JsonParser.parseString(new String(var0, StandardCharsets.UTF_8)).getAsJsonObject();
      } catch (RuntimeException var3) {
         return Ъ();
      }

      return А(var1);
   }

   private static byte[] А(JsonObject var0) {
      JsonObject var1 = Щ(var0, сIЮ);
      if (!var1.has(сIЧ)) {
         var1.addProperty(сIЛ, сI诶);
      }

      var1.remove(сI1);
      var1.remove(сIЬ);
      var1.addProperty(сI衣, 88);
      var1.addProperty(сIш, 88);
      if (var0.get(сIИ) instanceof JsonObject var2 && var2.get(сIь) instanceof JsonArray var3) {
         boolean var12 = false;

         for (JsonElement var6 : var3) {
            if (var6 instanceof JsonObject var7) {
               JsonElement var8 = var7.get(сIж);
               if (!var7.has(сI弟)) {
                  var7.add(сIб, А(var8, сIЫ));
               }

               if (!var7.has(сIl)) {
                  var7.add(сI8, А(var8, сI5));
               }

               var12 |= А_a83dd0cc(var7.get(сIв)) <= PackFormat.lastPreMinorVersion(PackType.CLIENT_RESOURCES);
            }
         }

         for (JsonElement var18 : var3) {
            if (var18 instanceof JsonObject var20) {
               if (var12) {
                  JsonObject var22 = new JsonObject();
                  var22.addProperty(сIъ, А_a83dd0cc(var20.get(сIМ)));
                  var22.addProperty(сIН, А_a83dd0cc(var20.get(сIн)));
                  var20.add(сIЦ, var22);
               } else {
                  var20.remove(сIч);
               }
            }
         }
      }

      JsonObject var9 = Щ(var0, сIЭ);
      JsonArray var10 = var9.get(сI必) instanceof JsonArray var13 ? var13 : new JsonArray();

      for (String var21 : new String[]{сIщ, сIГ}) {
         JsonPrimitive var23 = new JsonPrimitive(var21);
         if (!var10.contains(var23)) {
            var10.add(var23);
         }
      }

      var9.add(сIл, var10);
      var0.addProperty(сIэ, 3);
      м(var0);
      return var0.toString().getBytes(StandardCharsets.UTF_8);
   }

   public static String О(String var0) {
      return "RwFixed-r3-" + var0 + ".zip";
   }

   public static boolean 西(String var0) {
      return var0.matches(сКк);
   }

   private static int А_a83dd0cc(JsonElement var0) {
      return var0 instanceof JsonArray var1 ? var1.get(0).getAsInt() : var0.getAsInt();
   }

   private ReallyWorldResourcePackPatcher() {
   }

   public static byte[] А_1be31765(byte[] var0) {
      JsonElement var1 = JsonParser.parseString(new String(var0, StandardCharsets.UTF_8));
      return А_bbeebd87(var1) ? var1.toString().getBytes(StandardCharsets.UTF_8) : var0;
   }
}
