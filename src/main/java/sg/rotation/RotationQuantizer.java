package sg.rotation;
import sg.ec.MinecraftAccess;

public class RotationQuantizer implements MinecraftAccess {
   private static final double FIXED_SENSITIVITY_BASE = 0.502112676056338;
   private static final double STEP_MULTIPLIER = 1.2;

   public static float quantizeRotation(float var0) {
      return snapToRotationStep(var0);
   }

   public static float snapToRotationStep(float var0) {
      double var1 = getQuantizationStep();
      return var1 <= 0.0 ? var0 : (float)(Math.round(var0 / var1) * var1);
   }

   public static double getQuantizationStep() {
      double var0 = MINECRAFT_CLIENT.options.sensitivity().get();
      double var2 = FIXED_SENSITIVITY_BASE;
      return var2 * var2 * var2 * STEP_MULTIPLIER;
   }
}
