package sg.util.input;

import sg.ec.MinecraftAccess;

public final class MouseSensitivityMath implements MinecraftAccess {
   private static final String SENSITIVITY_RANGE_ERROR = "Sensitivity must be within 0..1";
   private static final double SENSITIVITY_SCALE = 0.6F;
   private static final double SENSITIVITY_OFFSET = 0.2F;
   private static final double NORMAL_GAIN_MULTIPLIER = 8.0;
   private static final float MOUSE_COUNT_ROTATION_SCALE = 0.15F;
   private static final String INVALID_DELTA_OR_GAIN_ERROR = "Invalid mouse delta or gain";
   private static final double QUANTIZATION_ROTATION_SCALE = 0.15F;
   private static final double COUNT_CONVERSION_ROTATION_SCALE = 0.15F;

   public static float snapRotationDelta(float rotationDelta) {
      return quantizeRotationDelta(rotationDelta);
   }

   public static float mouseCountsToRotationDelta(long mouseCounts, double gain) {
      return (float)(mouseCounts * gain) * MOUSE_COUNT_ROTATION_SCALE;
   }

   public static float getMouseCountsForRotationDelta(float rotationDelta) {
      return rotationDeltaToMouseCounts(rotationDelta);
   }

   public static double calculateSensitivityGain(double sensitivity, boolean useScopedGain) {
      if (Double.isFinite(sensitivity) && !(sensitivity < 0.0) && !(sensitivity > 1.0)) {
         double sensitivityBase = sensitivity * SENSITIVITY_SCALE + SENSITIVITY_OFFSET;
         return sensitivityBase * sensitivityBase * sensitivityBase * (useScopedGain ? 1.0 : NORMAL_GAIN_MULTIPLIER);
      } else {
         throw new IllegalArgumentException(SENSITIVITY_RANGE_ERROR);
      }
   }

   public static float quantizeRotationDelta(float rotationDelta, double gain) {
      if (Float.isFinite(rotationDelta) && Double.isFinite(gain) && !(gain <= 0.0)) {
         double rotationStep = gain * QUANTIZATION_ROTATION_SCALE;
         return mouseCountsToRotationDelta(Math.round(rotationDelta / rotationStep), gain);
      } else {
         throw new IllegalArgumentException(INVALID_DELTA_OR_GAIN_ERROR);
      }
   }

   public static float quantizeRotationDelta(float rotationDelta) {
      return quantizeRotationDelta(rotationDelta, getSensitivityGain());
   }

   public static float rotationDeltaToMouseCounts(float rotationDelta) {
      return (float)Math.round(rotationDelta / (getSensitivityGain() * COUNT_CONVERSION_ROTATION_SCALE));
   }

   public static double getSensitivityGain() {
      boolean useScopedGain = MINECRAFT_CLIENT.player != null && !MINECRAFT_CLIENT.options.smoothCamera && MINECRAFT_CLIENT.options.getCameraType().isFirstPerson() && MINECRAFT_CLIENT.player.isScoping();
      return calculateSensitivityGain(MINECRAFT_CLIENT.options.sensitivity().get(), useScopedGain);
   }

   public static float getMouseSensitivityGain() {
      return getSensitivityGainFloat();
   }

   public static float getSensitivityGainFloat() {
      return (float)getSensitivityGain();
   }

   private MouseSensitivityMath() {
   }

   public static float getMouseRotationStep() {
      return getRotationStep();
   }

   public static float getRotationStep() {
      return mouseCountsToRotationDelta(1L, getSensitivityGain());
   }
}
