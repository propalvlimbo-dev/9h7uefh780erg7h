package sg.modules.impl.init;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

public final class StandaloneAimConfig {
   private static final String ЯИ = "standalone-onnx-aim.properties";
   private static final String Яь = "standalone-onnx-aim/aim_gru.onnx";
   public final Path Д;
   final boolean е诶;
   public final boolean е1;
   final double ЯЦ;
   public final double Яч;
   final int ДЯ;
   private static final String ВЬь = "enabledOnStart";
   private static final String ВЬж = "padInitialHistory";
   private static final String ВЬ弟 = "targetHeightRatio";
   private static final double ВЬб = 0.82;
   private static final String ВЬЫ = "temperature";
   private static final double ВЬl = 0.8;
   private static final double ВЬ8 = 0.05;
   private static final double ВЬ5 = 2.0;
   private static final String ВЬв = "minimumEffectiveRateHz";
   private static final String ВЬъ = "modelPath";
   private static final String ВЬМ = "standalone-onnx-aim/aim_gru.onnx";
   private static final String ВЬН = "standalone-onnx-aim/aim_gru.onnx";
   private static final String ВЬн = "standalone-onnx-aim.properties";
   private static final String ВЬЦ = "Standalone ONNX Aim 60 Hz";
   private static final String ВЬч = "Could not read or create {}";
   private static final String ВЬЭ = "Could not create model directory for {}";
   private static final String ВЬ必 = "modelPath";
   private static final String ВЬщ = "standalone-onnx-aim/aim_gru.onnx";
   private static final String ВЬГ = "enabledOnStart";
   private static final String ВЬл = "false";
   private static final String ВЬэ = "padInitialHistory";
   private static final String ВЬю = "true";
   private static final String ВЬЩ = "targetHeightRatio";
   private static final String ВЬу = "0.82";
   private static final String ВЬ2 = "temperature";
   private static final String ВЬф = "0.8";
   private static final String ВЬи = "minimumEffectiveRateHz";
   private static final String ВЬ9 = "55";
   private static final String ВЬо = "true";
   private static final String ВЬO = "false";
   private static final String ВЬ7 = "Invalid {}={}; using {}";
   private static final String ВЬР = "Invalid {}={}; using {}";
   private static final String ВЬ3 = "Invalid {}={}; using {}";

   private StandaloneAimConfig(Properties var1, Logger var2, Path var3) {
      this.е诶 = Ь(var1, var2, ВЬь, false);
      this.е1 = Ь(var1, var2, ВЬж, true);
      this.ЯЦ = Ь(var1, var2, ВЬ弟, ВЬб, 0.0, 1.0);
      this.Яч = Ь(var1, var2, ВЬЫ, ВЬl, ВЬ8, ВЬ5);
      this.ДЯ = Ь(var1, var2, ВЬв, 55, 30, 60);
      String var4 = var1.getProperty(ВЬъ, ВЬМ).trim();
      Path var5 = Path.of(var4.isEmpty() ? ВЬН : var4);
      this.Д = (var5.isAbsolute() ? var5 : var3.resolve(var5)).normalize();
   }

   private static double Ь(Properties var0, Logger var1, String var2, double var3, double var5, double var7) {
      try {
         double var9 = Double.parseDouble(var0.getProperty(var2, Double.toString(var3)).trim());
         if (Double.isFinite(var9) && var9 >= var5 && var9 <= var7) {
            return var9;
         }
      } catch (NumberFormatException var11) {
      }

      var1.warn(ВЬР, var2, var0.getProperty(var2), var3);
      return var3;
   }

   private static boolean Ь(Properties var0, Logger var1, String var2, boolean var3) {
      String var4 = var0.getProperty(var2);
      if (var4 == null) {
         return var3;
      }

      return switch (var4.trim().toLowerCase(Locale.ROOT)) {
         case ВЬо -> true;
         case ВЬO -> false;
         default -> {
            var1.warn(ВЬ7, var2, var4, var3);
            yield var3;
         }
      };
   }

   static StandaloneAimConfig Ь(Logger var0) {
      Path var1 = FabricLoader.getInstance().getConfigDir();
      Path var2 = var1.resolve(ВЬн);
      Properties var3 = Ь();
      Properties var4 = new Properties(var3);

      try {
         Files.createDirectories(var1);
         if (Files.isRegularFile(var2)) {
            try (BufferedReader var5 = Files.newBufferedReader(var2, StandardCharsets.UTF_8)) {
               var4.load(var5);
            }
         } else {
            try (BufferedWriter var14 = Files.newBufferedWriter(var2, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
               var3.store(var14, ВЬЦ);
            }
         }
      } catch (IOException var13) {
         var0.error(ВЬч, var2, var13);
      }

      StandaloneAimConfig var15 = new StandaloneAimConfig(var4, var0, var1);

      try {
         Path var6 = var15.Д.getParent();
         if (var6 != null) {
            Files.createDirectories(var6);
         }
      } catch (IOException var8) {
         var0.warn(ВЬЭ, var15.Д, var8);
      }

      return var15;
   }

   private static Properties Ь() {
      Properties var0 = new Properties();
      var0.setProperty(ВЬ必, ВЬщ);
      var0.setProperty(ВЬГ, ВЬл);
      var0.setProperty(ВЬэ, ВЬю);
      var0.setProperty(ВЬЩ, ВЬу);
      var0.setProperty(ВЬ2, ВЬф);
      var0.setProperty(ВЬи, ВЬ9);
      return var0;
   }

   private static int Ь(Properties var0, Logger var1, String var2, int var3, int var4, int var5) {
      try {
         int var6 = Integer.parseInt(var0.getProperty(var2, Integer.toString(var3)).trim());
         if (var6 >= var4 && var6 <= var5) {
            return var6;
         }
      } catch (NumberFormatException var7) {
      }

      var1.warn(ВЬ3, var2, var0.getProperty(var2), var3);
      return var3;
   }
}
