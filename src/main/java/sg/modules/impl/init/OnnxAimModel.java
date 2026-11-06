package sg.modules.impl.init;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.TensorInfo;
import ai.onnxruntime.OrtSession.Result;
import ai.onnxruntime.OrtSession.SessionOptions;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import settings.render.OnnxModelContractValidator;
import sg.ec.Aim60Schema;
import sg.modules.impl.render.Prediction;

public class OnnxAimModel implements AutoCloseable {
   private static final String Ят = "sequence";
   private static final String Яа = "trajectory";
   private static final String ЯЮ = "mixture_logits";
   private static final String ЯЧ = "means";
   private static final String ЯЛ = "log_scales";
   private static final OrtEnvironment В_118c222a = OrtEnvironment.getEnvironment();
   @SerializedName("В")
   private final OrtSession В_8a5b57cc;
   @SerializedName("В")
   private final OnnxAimModel.OutputMode В_61a83f3e;
   @SerializedName("В")
   private final FloatBuffer В_18894945;
   private final Random Ъ = new Random();
   private static final long ИЮ = 60L;
   private static final String ИЧ = "sequence";
   private static final String ИЛ = "trajectory";
   private static final String И诶 = "mixture_logits";
   private static final String И1 = "means";
   private static final String ИЬ = "sequence";
   private static final String И衣 = "Missing ONNX input: sequence";
   private static final String Иш = "sequence";
   private static final long ИИ = 60L;
   private static final String Иь = "trajectory";
   private static final String Иж = "mixture_logits";
   private static final String И弟 = "means";
   private static final String Иб = "log_scales";
   private static final String ИЫ = "Expected 60 frames";
   private static final double Иl = 0.05;
   private static final double И8 = -Double.MAX_VALUE;

   private static float[][] п(float[][] var0) {
      if (var0.length != 15) {
         throw new IllegalStateException("Expected 15 points, got " + var0.length);
      }

      for (int var1 = 0; var1 < var0.length; var1++) {
         if (var0[var1].length < 2 || !Float.isFinite(var0[var1][0]) || !Float.isFinite(var0[var1][1])) {
            throw new IllegalStateException("Invalid trajectory point " + var1);
         }
      }

      return var0;
   }

   private OnnxAimModel.OutputMode п() throws OrtException {
      Set var1 = this.В_8a5b57cc.getOutputNames();
      if (var1.contains(Иь)) {
         return OnnxAimModel.OutputMode.BASELINE;
      } else if (var1.containsAll(Set.of(Иж, И弟, Иб))) {
         return OnnxAimModel.OutputMode.MDN;
      } else {
         throw new IllegalStateException("Unsupported ONNX outputs: " + var1);
      }
   }

   private static float[][] 诶(float[][] var0) {
      float[][] var1 = new float[var0.length][2];

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1[var2][0] = var0[var2][0];
         var1[var2][1] = var0[var2][1];
      }

      return var1;
   }

   private void Ы(float[][] var1) {
      if (var1.length != 60) {
         throw new IllegalArgumentException(ИЫ);
      }

      for (int var2 = 0; var2 < var1.length; var2++) {
         if (var1[var2].length != Aim60Schema.Хп) {
            throw new IllegalArgumentException("Expected " + Aim60Schema.Хп + " features at " + var2);
         }
      }
   }

   private int Ы(float[] var1, double var2) {
      double var4 = Math.max(Иl, var2);
      double var6 = И8;
      double var8 = 0.0;

      for (float var13 : var1) {
         var6 = Math.max(var6, var13 / var4);
      }

      double[] var14 = new double[var1.length];

      for (int var15 = 0; var15 < var1.length; var15++) {
         var8 += var14[var15] = Math.exp(var1[var15] / var4 - var6);
      }

      double var16 = this.Ъ.nextDouble() * var8;

      for (int var17 = 0; var17 < var14.length; var17++) {
         if ((var16 -= var14[var17]) <= 0.0) {
            return var17;
         }
      }

      return var14.length - 1;
   }

   @Override
   public synchronized void close() throws OrtException {
      this.В_8a5b57cc.close();
   }

   private static Object Ы(Result var0, String var1) throws OrtException {
      return var0.get(var1).orElseThrow(() -> new IllegalStateException("Missing ONNX output: " + var1)).getValue();
   }

   public OnnxAimModel.OutputMode Ы() {
      return this.В_61a83f3e;
   }

   public synchronized OnnxAimModel.Prediction Ы(float[][] var1, double var2) throws OrtException {
      this.Ы(var1);
      this.В_18894945.clear();

      for (float[] var7 : var1) {
         this.В_18894945.put(var7);
      }

      this.В_18894945.flip();

      try (
         OnnxTensor var14 = OnnxTensor.createTensor(В_118c222a, this.В_18894945, new long[]{1L, ИЮ, Aim60Schema.Хп});
         Result var15 = this.В_8a5b57cc.run(Map.of(ИЧ, var14));
      ) {
         if (this.В_61a83f3e != OnnxAimModel.OutputMode.BASELINE) {
            float[][] var17 = (float[][])Ы(var15, И诶);
            float[][][][] var19 = (float[][][][])Ы(var15, И1);
            int var8 = this.Ы(var17[0], var2);
            return new OnnxAimModel.Prediction(var8, п(诶(var19[0][var8])));
         } else {
            float[][][] var16 = (float[][][])Ы(var15, ИЛ);
            return new OnnxAimModel.Prediction(-1, п(诶(var16[0])));
         }
      }
   }

   private void М2() throws OrtException {
      if (!this.В_8a5b57cc.getInputNames().contains(ИЬ)) {
         throw new IllegalStateException(И衣);
      } else {
         long[] var1 = ((TensorInfo)this.В_8a5b57cc.getInputInfo().get(Иш).getInfo()).getShape();
         if (var1.length != 3 || var1[0] != 1L || var1[1] != ИИ || var1[2] != Aim60Schema.Хп) {
            throw new IllegalStateException("Expected ONNX input [1,60," + Aim60Schema.Хп + "], got " + Arrays.toString(var1));
         }
      }
   }

   public OnnxAimModel(Path var1) throws IOException, OrtException {
      if (!Files.isRegularFile(var1)) {
         throw new IOException("ONNX model was not found: " + var1);
      }

      OnnxModelContractValidator.щ_de5a6f78(var1);

      try (SessionOptions var2 = new SessionOptions()) {
         var2.setIntraOpNumThreads(1);
         var2.setInterOpNumThreads(1);
         this.В_8a5b57cc = В_118c222a.createSession(Files.readAllBytes(var1), var2);
      }

      try {
         this.М2();
         this.В_61a83f3e = this.п();
      } catch (RuntimeException | OrtException var6) {
         this.В_8a5b57cc.close();
         throw var6;
      }

      this.В_18894945 = ByteBuffer.allocateDirect(240 * Aim60Schema.Хп).order(ByteOrder.nativeOrder()).asFloatBuffer();
   }

   public enum OutputMode {
      BASELINE,
      MDN;
   }

   public record Prediction(int mixtureComponent, float[][] trajectory) {
   }
}
