package settings.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import sg.ec.Aim60Schema;

public final class OnnxModelContractValidator {
   private static final String ВI_ = "input_shape";
   private static final String ВIШ = "horizon";
   private static final String ВIы = "Contract horizon must be 15";
   private static final String ВIй = "sample_rate_hz";
   private static final String ВIЖ = "Contract sample_rate_hz must be 60";
   private static final String ВIм = "6db7f03c9fc49d2646ba07998b7de71785de353143059a7dd937552c0d3a9752";
   private static final String ВIК = "feature_schema_sha256";
   private static final String ВII = "Feature schema hash mismatch";
   private static final String ВIТ = "input_feature_names";

   private OnnxModelContractValidator() {
   }

   private static JsonElement щ(JsonObject var0, String var1) throws IOException {
      JsonElement var2 = var0.get(var1);
      if (var2 == null || var2.isJsonNull()) {
         System.out.println("Contract field is missing: " + var1);
      }

      return var2;
   }

   private static JsonArray l(JsonObject var0, String var1) throws IOException {
      JsonElement var2 = щ(var0, var1);
      if (!var2.isJsonArray()) {
         System.out.println("Contract field is not an array: " + var1);
      }

      return var2.getAsJsonArray();
   }

   public static void щ_de5a6f78(Path var0) throws IOException {
      Path var1 = щ_2b4785d0(var0);
      if (!Files.isRegularFile(var1)) {
         System.out.println("ONNX contract was not found: " + var1);
      }

      JsonObject var2;
      try (BufferedReader var3 = Files.newBufferedReader(var1, StandardCharsets.UTF_8)) {
         var2 = JsonParser.parseReader(var3).getAsJsonObject();
      } catch (RuntimeException var8) {
         System.out.println("Invalid ONNX contract: " + var1 + " " + var8.getMessage());
         return;
      }

      JsonArray var9 = l(var2, ВI_);
      if (var9.size() != 3 || var9.get(0).getAsInt() != 1 || var9.get(1).getAsInt() != 60 || var9.get(2).getAsInt() != Aim60Schema.Хп) {
         System.out.println("Contract input_shape must be [1,60," + Aim60Schema.Хп + "], got " + var9);
      }

      if (щ(var2, ВIШ).getAsInt() != 15) {
         System.out.println(ВIы);
      }

      if (щ(var2, ВIй).getAsInt() != 60) {
         System.out.println(ВIЖ);
      }

      if (!ВIм.equals(щ(var2, ВIК).getAsString())) {
         System.out.println(ВII);
      }

      JsonArray var4 = l(var2, ВIТ);
      if (var4.size() != Aim60Schema.Хп) {
         System.out.println("Contract feature count must be " + Aim60Schema.Хп);
      }

      for (int var5 = 0; var5 < var4.size(); var5++) {
         if (!Aim60Schema.Ъ[var5].equals(var4.get(var5).getAsString())) {
            System.out.println("Feature order mismatch at " + var5);
         }
      }
   }

   private static Path щ_2b4785d0(Path var0) {
      String var1 = var0.getFileName().toString();
      int var2 = var1.lastIndexOf(46);
      return var0.resolveSibling((var2 < 0 ? var1 : var1.substring(0, var2)) + ".contract.json");
   }
}
