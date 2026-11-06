package sg.render.pipeline;

import ai.onnxruntime.OrtException;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import org.slf4j.Logger;
import settings.render.AimFeatureSampler;
import sg.ec.MinecraftAccess;
import sg.ec.Aim60Schema;
import sg.modules.impl.init.StandaloneAimConfig;
import sg.modules.impl.init.OnnxAimModel;
import sg.util.input.MouseSensitivityMath;

public final class OnnxAimPredictor implements MinecraftAccess, AutoCloseable {
   private static final long Хи = 50000000L;
   private static final float Ък = 1.5F;
   private final StandaloneAimConfig Я;
   private final Logger п;
   private final AimFeatureSampler Х;
   @SerializedName("В")
   private final Deque<float[]> В_3b120213 = new ArrayDeque<>(60);
   @SerializedName("В")
   private OnnxAimModel В_587717f5;
   @SerializedName("В")
   private OnnxAimModel.Prediction В_e918849d;
   private UUID 诶;
   private float Ъг;
   private float Ъ0;
   private float Ъх;
   private float ЪУ;
   private float ЪЗ;
   private boolean еO;
   private long Х9;
   private long Хо;
   private String Ял;
   private static final String ЯПГ = "Loaded {} model from {}; input=[1,{},{}], horizon={}";
   private static final String ЯПл = "Could not load ONNX model from {}";
   private static final long ЯПэ = 50000000L;
   private static final String ЯПю = "none";
   private static final float ЯПЩ = -90.0F;
   private static final float ЯПу = 90.0F;
   private static final float ЯП2 = 1.5F;
   private static final long ЯПф = 5000000000L;
   private static final String ЯПи = "ONNX inference failed; rotation is held";
   private static final String ЯП9 = "Could not close ONNX runtime";

   public void Бь() {
      this.诶 = null;
      this.Х9 = 0L;
      this.Х.ЬЫ();
      this.БЦ();
   }

   private void БЗ() {
      OnnxAimModel var1 = this.В_587717f5;
      this.В_587717f5 = null;
      this.Бь();
      if (var1 != null) {
         try {
            var1.close();
         } catch (OrtException var3) {
            this.п.warn(ЯП9, var3);
         }
      }
   }

   private void method_9(LocalPlayer var1) {
   }

   private void Ъ(float[] var1) {
      if (var1.length != Aim60Schema.Хп) {
         throw new IllegalArgumentException("Expected " + Aim60Schema.Хп + " features");
      }

      if (this.В_3b120213.isEmpty() && this.Я.е1) {
         for (int var2 = 0; var2 < 60; var2++) {
            this.В_3b120213.addLast((float[])var1.clone());
         }
      } else {
         if (this.В_3b120213.size() == 60) {
            this.В_3b120213.removeFirst();
         }

         this.В_3b120213.addLast((float[])var1.clone());
      }
   }

   int СП() {
      return this.В_3b120213.size();
   }

   private void БЦ() {
      this.В_3b120213.clear();
      this.БХ();
   }

   int Со() {
      return this.В_e918849d == null ? -1 : this.В_e918849d.mixtureComponent();
   }

   @Override
   public void close() {
      this.БЗ();
   }

   private void С(Exception var1) {
      long var2 = System.nanoTime();
      if (var2 - this.Хо >= ЯПф) {
         this.Хо = var2;
         this.п.error(ЯПи, var1);
      }
   }

   public boolean 诶п() {
      return this.В_587717f5 != null;
   }

   public void С(AbstractClientPlayer var1, float var2, long var3) {
      LocalPlayer var5 = MINECRAFT_CLIENT.player;
      if (this.В_587717f5 != null && var5 != null && MINECRAFT_CLIENT.level != null && С(var5, var1)) {
         UUID var6 = var1.getUUID();
         if (!var6.equals(this.诶)) {
            this.Бь();
            this.诶 = var6;
         }

         if (this.Х9 != 0L && var3 - this.Х9 > ЯПэ) {
            this.Х.ЬЫ();
            this.БЦ();
         }

         this.Х9 = var3;
         boolean var7 = MINECRAFT_CLIENT.crosshairPickEntity == var1;
         this.method_9(var5);
         AimFeatureSampler.SampleResult var8 = this.Х.З(var5, var1, var2, var3);
         if (var8.resetHistory()) {
            this.БЦ();
         }

         if (var8.features() != null) {
            this.Ъ(var8.features());
            if (this.В_3b120213.size() >= 60) {
               try {
                  this.М(var5);
               } catch (OrtException | RuntimeException var10) {
                  this.БХ();
                  this.С(var10);
               }
            }
         }
      } else {
         this.Бь();
      }
   }

   public String Ъг() {
      return this.Ял;
   }

   private void М(LocalPlayer var1) throws OrtException {
      if (this.В_e918849d == null) {
         this.В_e918849d = this.В_587717f5.Ы(this.В_3b120213.toArray(float[][]::new), this.Я.Яч);
         this.Ъг = 0.0F;
         this.Ъ0 = var1.getYRot();
         this.Ъх = var1.getXRot();
      }

      float[][] var2 = this.В_e918849d.trajectory();
      int var3 = var2.length - 1;
      int var4 = Math.min((int)this.Ъг, var3);
      int var5 = Math.min(var4 + 1, var3);
      float var6 = this.Ъг - var4;
      float var7 = Mth.lerp(var6, var2[var4][0], var2[var5][0]);
      float var8 = Mth.lerp(var6, var2[var4][1], var2[var5][1]);
      float var9 = this.Ъ0 + var7;
      float var10 = this.Ъх + var8;
      var1.setYRot(MouseSensitivityMath.snapRotationDelta(var1.getYRot() + Mth.wrapDegrees(var9 - var1.getYRot())));
      var1.setXRot(MouseSensitivityMath.snapRotationDelta(Mth.clamp(var10, ЯПЩ, ЯПу)));
      if (this.Ъг >= var3) {
         this.В_e918849d = null;
         this.Ъг = 0.0F;
      } else {
         this.Ъг = Math.min(this.Ъг + ЯП2, var3);
      }
   }

   private static boolean С(LocalPlayer var0, AbstractClientPlayer var1) {
      return var1 != null && var1 != var0 && var1.isAlive() && !var1.isRemoved() && !var1.isSpectator();
   }

   public boolean 诶к() {
      this.БЗ();

      try {
         this.В_587717f5 = new OnnxAimModel(this.Я.Д);
         this.Ял = null;
         this.п.info(ЯПГ, this.В_587717f5.Ы(), this.Я.Д, 60, Aim60Schema.Хп, 15);
         return true;
      } catch (IOException | OrtException | RuntimeException var2) {
         this.В_587717f5 = null;
         this.Ял = var2.getMessage();
         this.п.error(ЯПл, this.Я.Д, var2);
         return false;
      }
   }

   public OnnxAimPredictor(StandaloneAimConfig var1, Logger var2) {
      this.Я = var1;
      this.п = var2;
      this.Х = new AimFeatureSampler();
   }

   public String Ъ7() {
      return this.В_587717f5 == null ? ЯПю : this.В_587717f5.Ы().name();
   }

   private void БХ() {
      this.В_e918849d = null;
      this.Ъг = 0.0F;
      this.еO = false;
   }
}
