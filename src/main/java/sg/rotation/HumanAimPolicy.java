package sg.rotation;

import com.google.gson.annotations.SerializedName;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.StringJoiner;

public final class HumanAimPolicy {
   @SerializedName("4Т")
   private final int transformedFeatureCount;
   @SerializedName("4О")
   private final int encoderWidth;
   @SerializedName("4Ф")
   private final int gruHiddenSize;
   @SerializedName("4Й")
   private final int gruLayerCount;
   @SerializedName("4к")
   private final int mixtureComponentCount;
   @SerializedName("4г")
   private final int predictionHorizonSteps;
   @SerializedName("40")
   private final int executionHorizonSteps;
   private final @com.google.gson.annotations.SerializedName("Я诶")  double minStandardDeviation;
   private final @com.google.gson.annotations.SerializedName("Я1")  double maxStandardDeviation;
   private final @com.google.gson.annotations.SerializedName("а")  String[] rawFeatureNames;
   private final @com.google.gson.annotations.SerializedName("ж")  int[] featureSourceIndices;
   private final @com.google.gson.annotations.SerializedName("弟")  int[] featureTransformTypes;
   private final @com.google.gson.annotations.SerializedName("Х")  double[] featureTransformScales;
   private final @com.google.gson.annotations.SerializedName("е")  double[] featureMeans;
   private final @com.google.gson.annotations.SerializedName("А")  double[] featureStandardDeviations;
   @SerializedName("4")
   private final double[] velocityScales;
   @SerializedName("п")
   private final double[] maxAxisVelocities;
   private final @com.google.gson.annotations.SerializedName("сэ")  Map<String, float[]> tensorsByName = new HashMap<>();
   @SerializedName("Д")
   private final Random random;
   @SerializedName("п")
   private final float[][] recurrentHiddenStates;
   private final @com.google.gson.annotations.SerializedName("ЯЬ")  double samplingTemperature;
   private final @com.google.gson.annotations.SerializedName("Я衣")  double noiseScaleMultiplier;
   private final @com.google.gson.annotations.SerializedName("Я")  ArrayDeque<HumanAimPolicy.VelocityStep> velocityQueue = new ArrayDeque<>();
   private @com.google.gson.annotations.SerializedName("ВУ")  String episodeKey;
   private @com.google.gson.annotations.SerializedName("ХЯ")  long lastActionTimestampNs;
   @SerializedName("Д")
   private double[] previousActionVelocities;
   private @com.google.gson.annotations.SerializedName("Ъ")  double[] reportedOutputDelta;
   @SerializedName("4х")
   private int selectedComponent;
   @SerializedName("4У")
   private int clippedStepCount;
   private static final long INITIAL_ACTION_TIMESTAMP = Long.MIN_VALUE;
   private static final String INVALID_SAMPLING_MESSAGE = "Invalid sampling settings";
   private static final String UNSUPPORTED_MODEL_FORMAT_MESSAGE = "Unsupported model format";
   private static final String INVALID_ARCHITECTURE_MESSAGE = "Invalid architecture";
   private static final String INVALID_SCALE_BOUNDS_MESSAGE = "Invalid scale bounds";
   private static final String INVALID_FEATURE_TRANSFORM_MESSAGE = "Invalid feature transform";
   private static final String INVALID_NORMALIZATION_MESSAGE = "Invalid normalization";
   private static final String INVALID_ACTION_UNITS_MESSAGE = "Invalid action units";
   private static final String INVALID_TENSOR_RANK_MESSAGE = "Invalid tensor rank";
   private static final long MAX_TENSOR_ELEMENTS = 20000000L;
   private static final String TENSOR_TOO_LARGE_MESSAGE = "Tensor too large";
   private static final String NONFINITE_WEIGHT_MESSAGE = "Nonfinite weight";
   private static final String DUPLICATE_TENSOR_MESSAGE = "Duplicate tensor";
   private static final String TRAILING_MODEL_DATA_MESSAGE = "Trailing model data";
   private static final String ENCODER_WEIGHT_KEY = "encoder.weight";
   private static final String ENCODER_BIAS_KEY = "encoder.bias";
   private static final String HEAD_WEIGHT_KEY = "head.weight";
   private static final String HEAD_BIAS_KEY = "head.bias";
   private static final String INVALID_DIMENSION_MESSAGE = "Invalid dimension";
   private static final String NONFINITE_METADATA_MESSAGE = "Nonfinite metadata";
   private static final double SIGMOID_MIN_INPUT = -60.0;
   private static final double SIGMOID_MAX_INPUT = 60.0;
   private static final String WRONG_FEATURE_COUNT_MESSAGE = "Wrong feature count";
   private static final String NONFINITE_INPUT_MESSAGE = "Nonfinite input";
   private static final String ENCODER_LAYER_NAME = "encoder";
   private static final String HEAD_LAYER_NAME = "head";
   private static final String NONFINITE_NETWORK_OUTPUT_MESSAGE = "Nonfinite network output";
   private static final double MAX_TEMPORAL_CORRELATION = 0.95;
   private static final double MAX_AXIS_CORRELATION = 0.95;
   private static final String EPISODE_KEY_PARAMETER_NAME = "episodeKey";
   private static final long OUT_OF_ORDER_TIMESTAMP_SENTINEL = Long.MIN_VALUE;
   private static final long GAP_RESET_TIMESTAMP_SENTINEL = Long.MIN_VALUE;
   private static final long MAX_EPISODE_GAP_NS = 50000000L;
   private static final long RATE_LIMIT_TIMESTAMP_SENTINEL = Long.MIN_VALUE;
   private static final long MIN_ACTION_INTERVAL_NS = 8000000L;
   private static final long FEATURE_INTERVAL_TIMESTAMP_SENTINEL = Long.MIN_VALUE;
   private static final double NANOSECONDS_PER_MILLISECOND = 1000000.0;
   private static final float MIN_SAMPLE_INTERVAL_MS = 8.0F;
   private static final double MAX_SAMPLE_INTERVAL_MS = 33.34;
   private static final double MILLISECONDS_PER_SECOND = 1000.0;
   private static final double MIN_DELTA_MISMATCH_PER_SECOND = 2.0;
   private static final double RELATIVE_DELTA_MISMATCH_TOLERANCE = 0.15;
   private static final String OUTPUT_ACTION_MISMATCH_MESSAGE = "Output must belong to the current action";
   private static final String NONFINITE_OUTPUT_DELTA_MESSAGE = "Nonfinite output delta";
   private static final long RESET_ACTION_TIMESTAMP = Long.MIN_VALUE;
   private static final String CLI_USAGE_MESSAGE = "Usage: HumanAimPolicy policy.bin raw_features.csv";
   private static final long CLI_RANDOM_SEED = 42L;
   private static final String FEATURE_CSV_DELIMITER = ",";
   private static final String OUTPUT_CSV_DELIMITER = ",";

   private void sampleVelocityHorizon(float[] networkOutput) {
      int componentStride = 1 + 11 * this.predictionHorizonSteps;
      double[] componentLogits = new double[this.mixtureComponentCount];

      for (int var4 = 0; var4 < this.mixtureComponentCount; var4++) {
         componentLogits[var4] = networkOutput[var4 * componentStride];
      }

      this.selectedComponent = this.sampleSoftmax(componentLogits);
      this.clippedStepCount = 0;
      double[] previousTransformedVelocity = null;
      double[] previousUnconditionedMean = null;

      for (int var6 = 0; var6 < this.predictionHorizonSteps; var6++) {
         int stepOutputOffset = this.selectedComponent * componentStride + 1 + 11 * var6;
         double[] unconditionedMean = new double[]{networkOutput[stepOutputOffset], networkOutput[stepOutputOffset + 1]};
         double[] conditionedMean = (double[])unconditionedMean.clone();
         if (var6 > 0) {
            for (int var10 = 0; var10 < 2; var10++) {
               conditionedMean[var10] += MAX_TEMPORAL_CORRELATION * Math.tanh(networkOutput[stepOutputOffset + 5 + var10]) * (previousTransformedVelocity[var10] - previousUnconditionedMean[var10]);
            }
         }

         double[] axisMaskLogits = new double[4];

         for (int var11 = 0; var11 < 4; var11++) {
            axisMaskLogits[var11] = networkOutput[stepOutputOffset + 7 + var11];
         }

         int activeAxisMask = this.sampleSoftmax(axisMaskLogits);
         double axisCorrelation = MAX_AXIS_CORRELATION * Math.tanh(networkOutput[stepOutputOffset + 4]);
         double var14 = this.random.nextGaussian();
         double var16 = axisCorrelation * var14 + Math.sqrt(1.0 - axisCorrelation * axisCorrelation) * this.random.nextGaussian();
         double[] gaussianNoise = new double[]{var14, var16};
         double[] transformedVelocity = new double[2];
         double[] axisVelocity = new double[2];
         int clippedAxes = 0;

         for (int var22 = 0; var22 < 2; var22++) {
            double var23 = Math.min(softplus(networkOutput[stepOutputOffset + 2 + var22]) + this.minStandardDeviation, this.maxStandardDeviation) * this.noiseScaleMultiplier;
            transformedVelocity[var22] = (activeAxisMask & 1 << var22) == 0 ? 0.0 : conditionedMean[var22] + var23 * gaussianNoise[var22];
            double var25 = asinh(this.maxAxisVelocities[var22] / this.velocityScales[var22]);
            double var27 = Math.max(-var25, Math.min(var25, transformedVelocity[var22]));
            if (var27 != transformedVelocity[var22]) {
               clippedAxes |= 1 << var22;
            }

            transformedVelocity[var22] = var27;
            axisVelocity[var22] = Math.sinh(transformedVelocity[var22]) * this.velocityScales[var22];
         }

         if (clippedAxes != 0) {
            this.clippedStepCount++;
         }

         if (var6 < this.executionHorizonSteps) {
            this.velocityQueue.add(new HumanAimPolicy.VelocityStep(axisVelocity, clippedAxes));
         }

         previousTransformedVelocity = transformedVelocity;
         previousUnconditionedMean = unconditionedMean;
      }
   }

   public HumanAimPolicy(Path modelPath, long randomSeed) throws IOException {
      this(modelPath, randomSeed, 1.0, 1.0);
   }

   private static float[] applyDenseLayer(float[] weights, float[] bias, float[] input) {
      float[] output = new float[bias.length];

      for (int var4 = 0; var4 < output.length; var4++) {
         double weightedSum = bias[var4];
         int weightRowOffset = var4 * input.length;

         for (int var8 = 0; var8 < input.length; var8++) {
            weightedSum += (double)weights[weightRowOffset + var8] * input[var8];
         }

         output[var4] = (float)weightedSum;
      }

      return output;
   }

   private static String readUtf8String(DataInputStream input) throws IOException {
      int byteCount = readPositiveDimension(input);
      byte[] bytes = input.readNBytes(byteCount);
      if (bytes.length != byteCount) {
         throw new EOFException();
      } else {
         return new String(bytes, StandardCharsets.UTF_8);
      }
   }

   private int sampleSoftmax(double[] logits) {
      double maxLogit = Arrays.stream(logits).max().orElseThrow();
      double totalWeight = 0.0;
      double[] weights = new double[logits.length];

      for (int var7 = 0; var7 < weights.length; var7++) {
         weights[var7] = Math.exp((logits[var7] - maxLogit) / this.samplingTemperature);
         totalWeight += weights[var7];
      }

      double sampledWeight = this.random.nextDouble() * totalWeight;

      for (int var9 = 0; var9 < weights.length; var9++) {
         sampledWeight -= weights[var9];
         if (sampledWeight < 0.0) {
            return var9;
         }
      }

      return weights.length - 1;
   }

   public static void main(String[] args) throws Exception {
      if (args.length != 2) {
         throw new IllegalArgumentException(CLI_USAGE_MESSAGE);
      }

      HumanAimPolicy policy = new HumanAimPolicy(Path.of(args[0]), CLI_RANDOM_SEED);

      String line;
      try (BufferedReader reader = Files.newBufferedReader(Path.of(args[1]))) {
         while ((line = reader.readLine()) != null) {
            if (!line.isBlank()) {
               String[] featureColumns = line.split(FEATURE_CSV_DELIMITER);
               float[] rawFeatures = new float[featureColumns.length];

               for (int var6 = 0; var6 < featureColumns.length; var6++) {
                  rawFeatures[var6] = Float.parseFloat(featureColumns[var6]);
               }

               float[] networkOutput = policy.evaluateNetwork(rawFeatures);
               StringJoiner outputLine = new StringJoiner(OUTPUT_CSV_DELIMITER);

               for (float var11 : networkOutput) {
                  outputLine.add(Float.toString(var11));
               }

               System.out.println(outputLine);
            }
         }
      }
   }

   private float[] runDenseLayer(String layerName, float[] input) {
      float[] weights = this.tensorsByName.get(layerName + ".weight");
      float[] bias = this.tensorsByName.get(layerName + ".bias");
      return applyDenseLayer(weights, bias, input);
   }

   private static double sigmoid(double input) {
      return 1.0 / (1.0 + Math.exp(-Math.max(SIGMOID_MIN_INPUT, Math.min(SIGMOID_MAX_INPUT, input))));
   }

   public void resetState() {
      for (float[] var4 : this.recurrentHiddenStates) {
         Arrays.fill(var4, 0.0F);
      }

      this.velocityQueue.clear();
      this.episodeKey = null;
      this.lastActionTimestampNs = RESET_ACTION_TIMESTAMP;
      this.previousActionVelocities = null;
      this.reportedOutputDelta = null;
      this.selectedComponent = -1;
      this.clippedStepCount = 0;
   }

   public void reportAppliedDelta(long timestampNs, double yawDelta, double pitchDelta) {
      if (this.previousActionVelocities == null || timestampNs != this.lastActionTimestampNs) {
         throw new IllegalStateException(OUTPUT_ACTION_MISMATCH_MESSAGE);
      }

      if (Double.isFinite(yawDelta) && Double.isFinite(pitchDelta)) {
         this.reportedOutputDelta = new double[]{yawDelta, pitchDelta};
      } else {
         throw new IllegalArgumentException(NONFINITE_OUTPUT_DELTA_MESSAGE);
      }
   }

   public String[] getRawFeatureNames() {
      return (String[])this.rawFeatureNames.clone();
   }

   private static double[] readFiniteMetadataVector(DataInputStream input, int length) throws IOException {
      double[] values = new double[length];

      for (int var3 = 0; var3 < length; var3++) {
         values[var3] = input.readDouble();
         if (!Double.isFinite(values[var3])) {
            throw new IOException(NONFINITE_METADATA_MESSAGE);
         }
      }

      return values;
   }

   private static int readPositiveDimension(DataInputStream input) throws IOException {
      int dimension = input.readInt();
      if (dimension >= 1 && dimension <= 10000) {
         return dimension;
      } else {
         throw new IOException(INVALID_DIMENSION_MESSAGE);
      }
   }

   private static double softplus(double input) {
      return Math.max(input, 0.0) + Math.log1p(Math.exp(-Math.abs(input)));
   }

   public HumanAimPolicy(Path modelPath, long randomSeed, double samplingTemperature, double noiseScaleMultiplier) throws IOException {
      this.lastActionTimestampNs = INITIAL_ACTION_TIMESTAMP;
      this.selectedComponent = -1;
      if (samplingTemperature > 0.0 && noiseScaleMultiplier >= 0.0 && Double.isFinite(samplingTemperature) && Double.isFinite(noiseScaleMultiplier)) {
         this.samplingTemperature = samplingTemperature;
         this.noiseScaleMultiplier = noiseScaleMultiplier;
         this.random = new Random(randomSeed);

         try (DataInputStream modelInput = new DataInputStream(new BufferedInputStream(Files.newInputStream(modelPath)))) {
            if (!Arrays.equals(modelInput.readNBytes(4), new byte[]{72, 65, 77, 49}) || modelInput.readInt() != 1) {
               throw new IOException(UNSUPPORTED_MODEL_FORMAT_MESSAGE);
            }

            this.transformedFeatureCount = readPositiveDimension(modelInput);
            this.encoderWidth = readPositiveDimension(modelInput);
            this.gruHiddenSize = readPositiveDimension(modelInput);
            this.gruLayerCount = readPositiveDimension(modelInput);
            this.mixtureComponentCount = readPositiveDimension(modelInput);
            this.predictionHorizonSteps = readPositiveDimension(modelInput);
            this.executionHorizonSteps = readPositiveDimension(modelInput);
            if (this.gruLayerCount > 16 || this.mixtureComponentCount > 64 || this.predictionHorizonSteps > 120 || this.executionHorizonSteps > this.predictionHorizonSteps) {
               throw new IOException(INVALID_ARCHITECTURE_MESSAGE);
            }

            this.minStandardDeviation = modelInput.readDouble();
            this.maxStandardDeviation = modelInput.readDouble();
            if (!(this.minStandardDeviation > 0.0) || !(this.maxStandardDeviation >= this.minStandardDeviation)) {
               throw new IOException(INVALID_SCALE_BOUNDS_MESSAGE);
            }

            this.rawFeatureNames = new String[readPositiveDimension(modelInput)];

            for (int var9 = 0; var9 < this.rawFeatureNames.length; var9++) {
               this.rawFeatureNames[var9] = readUtf8String(modelInput);
            }

            this.featureSourceIndices = new int[this.transformedFeatureCount];
            this.featureTransformTypes = new int[this.transformedFeatureCount];
            this.featureTransformScales = new double[this.transformedFeatureCount];

            for (int var20 = 0; var20 < this.transformedFeatureCount; var20++) {
               this.featureSourceIndices[var20] = modelInput.readInt();
               this.featureTransformTypes[var20] = modelInput.readInt();
               this.featureTransformScales[var20] = modelInput.readDouble();
               if (this.featureSourceIndices[var20] < 0 || this.featureSourceIndices[var20] >= this.rawFeatureNames.length || this.featureTransformTypes[var20] < 0 || this.featureTransformTypes[var20] > 4 || !(this.featureTransformScales[var20] > 0.0)) {
                  throw new IOException(INVALID_FEATURE_TRANSFORM_MESSAGE);
               }
            }

            this.featureMeans = readFiniteMetadataVector(modelInput, this.transformedFeatureCount);
            this.featureStandardDeviations = readFiniteMetadataVector(modelInput, this.transformedFeatureCount);
            this.velocityScales = readFiniteMetadataVector(modelInput, 2);
            this.maxAxisVelocities = readFiniteMetadataVector(modelInput, 2);

            for (double var12 : this.featureStandardDeviations) {
               if (!(var12 > 0.0)) {
                  throw new IOException(INVALID_NORMALIZATION_MESSAGE);
               }
            }

            for (int var22 = 0; var22 < 2; var22++) {
               if (!(this.velocityScales[var22] > 0.0) || !(this.maxAxisVelocities[var22] > 0.0)) {
                  throw new IOException(INVALID_ACTION_UNITS_MESSAGE);
               }
            }

            int tensorCount = readPositiveDimension(modelInput);

            for (int var24 = 0; var24 < tensorCount; var24++) {
               String tensorName = readUtf8String(modelInput);
               int tensorRank = readPositiveDimension(modelInput);
               long tensorElementCount = 1L;
               if (tensorRank > 4) {
                  throw new IOException(INVALID_TENSOR_RANK_MESSAGE);
               }

               for (int var15 = 0; var15 < tensorRank; var15++) {
                  tensorElementCount *= readPositiveDimension(modelInput);
                  if (tensorElementCount > MAX_TENSOR_ELEMENTS) {
                     throw new IOException(TENSOR_TOO_LARGE_MESSAGE);
                  }
               }

               float[] tensorValues = new float[(int)tensorElementCount];

               for (int var16 = 0; var16 < tensorValues.length; var16++) {
                  tensorValues[var16] = modelInput.readFloat();
                  if (!Float.isFinite(tensorValues[var16])) {
                     throw new IOException(NONFINITE_WEIGHT_MESSAGE);
                  }
               }

               if (this.tensorsByName.put(tensorName, tensorValues) != null) {
                  throw new IOException(DUPLICATE_TENSOR_MESSAGE);
               }
            }

            if (modelInput.read() != -1) {
               throw new IOException(TRAILING_MODEL_DATA_MESSAGE);
            }
         }

         this.requireTensorLength(ENCODER_WEIGHT_KEY, this.encoderWidth * this.transformedFeatureCount);
         this.requireTensorLength(ENCODER_BIAS_KEY, this.encoderWidth);

         for (int var19 = 0; var19 < this.gruLayerCount; var19++) {
            this.requireTensorLength("gru.weight_ih_l" + var19, 3 * this.gruHiddenSize * (var19 == 0 ? this.encoderWidth : this.gruHiddenSize));
            this.requireTensorLength("gru.weight_hh_l" + var19, 3 * this.gruHiddenSize * this.gruHiddenSize);
            this.requireTensorLength("gru.bias_ih_l" + var19, 3 * this.gruHiddenSize);
            this.requireTensorLength("gru.bias_hh_l" + var19, 3 * this.gruHiddenSize);
         }

         this.requireTensorLength(HEAD_WEIGHT_KEY, this.mixtureComponentCount * (1 + 11 * this.predictionHorizonSteps) * this.gruHiddenSize);
         this.requireTensorLength(HEAD_BIAS_KEY, this.mixtureComponentCount * (1 + 11 * this.predictionHorizonSteps));
         this.recurrentHiddenStates = new float[this.gruLayerCount][this.gruHiddenSize];
      } else {
         throw new IllegalArgumentException(INVALID_SAMPLING_MESSAGE);
      }
   }

   private void requireTensorLength(String tensorName, int expectedLength) throws IOException {
      if (!this.tensorsByName.containsKey(tensorName) || this.tensorsByName.get(tensorName).length != expectedLength) {
         throw new IOException("Invalid tensor " + tensorName);
      }
   }

   public float[] orderRawFeatures(Map<String, ? extends Number> featuresByName) {
      float[] rawFeatures = new float[this.rawFeatureNames.length];

      for (int var3 = 0; var3 < rawFeatures.length; var3++) {
         Number featureValue = (Number)featuresByName.get(this.rawFeatureNames[var3]);
         if (featureValue == null) {
            throw new IllegalArgumentException("Missing feature: " + this.rawFeatureNames[var3]);
         }

         rawFeatures[var3] = featureValue.floatValue();
         if (!Float.isFinite(rawFeatures[var3])) {
            throw new IllegalArgumentException("Nonfinite feature: " + this.rawFeatureNames[var3]);
         }
      }

      return rawFeatures;
   }

   public HumanAimPolicy.Action nextAction(Map<String, ? extends Number> featuresByName, String episodeKey, long timestampNs) {
      Objects.requireNonNull(episodeKey, EPISODE_KEY_PARAMETER_NAME);
      if (!episodeKey.equals(this.episodeKey)) {
         this.resetState();
         this.episodeKey = episodeKey;
      }

      if (this.lastActionTimestampNs != OUT_OF_ORDER_TIMESTAMP_SENTINEL && timestampNs <= this.lastActionTimestampNs) {
         return null;
      }

      if (this.lastActionTimestampNs != GAP_RESET_TIMESTAMP_SENTINEL && timestampNs - this.lastActionTimestampNs > MAX_EPISODE_GAP_NS) {
         this.resetState();
         this.episodeKey = episodeKey;
      }

      if (this.lastActionTimestampNs != RATE_LIMIT_TIMESTAMP_SENTINEL && timestampNs - this.lastActionTimestampNs < MIN_ACTION_INTERVAL_NS) {
         return null;
      }

      float[] rawFeatures = this.orderRawFeatures(featuresByName);
      if (this.lastActionTimestampNs != FEATURE_INTERVAL_TIMESTAMP_SENTINEL) {
         rawFeatures[0] = (float)((timestampNs - this.lastActionTimestampNs) / NANOSECONDS_PER_MILLISECOND);
      }

      if (!(rawFeatures[0] < MIN_SAMPLE_INTERVAL_MS) && !(rawFeatures[0] > MAX_SAMPLE_INTERVAL_MS)) {
         if (this.previousActionVelocities != null) {
            double sampleIntervalSeconds = rawFeatures[0] / MILLISECONDS_PER_SECOND;

            for (int var8 = 0; var8 < 2; var8++) {
               double previousAppliedDelta = this.reportedOutputDelta == null ? this.previousActionVelocities[var8] * sampleIntervalSeconds : this.reportedOutputDelta[var8];
               double observedDelta = rawFeatures[12 + var8] * sampleIntervalSeconds;
               if (Math.abs(observedDelta - previousAppliedDelta) > Math.max(MIN_DELTA_MISMATCH_PER_SECOND * sampleIntervalSeconds, RELATIVE_DELTA_MISMATCH_TOLERANCE * Math.abs(previousAppliedDelta))) {
                  this.velocityQueue.clear();
               }
            }
         }

         this.reportedOutputDelta = null;
         float[] networkOutput = this.evaluateNetwork(rawFeatures);
         if (this.velocityQueue.isEmpty()) {
            this.sampleVelocityHorizon(networkOutput);
         }

         HumanAimPolicy.VelocityStep velocityStep = this.velocityQueue.removeFirst();
         this.previousActionVelocities = velocityStep.velocity();
         this.lastActionTimestampNs = timestampNs;
         return new HumanAimPolicy.Action(this.previousActionVelocities[0], this.previousActionVelocities[1], this.selectedComponent, this.clippedStepCount, timestampNs, velocityStep.clippedAxes());
      } else {
         this.resetState();
         this.episodeKey = episodeKey;
         this.lastActionTimestampNs = timestampNs;
         return null;
      }
   }

   private static double asinh(double input) {
      return Math.copySign(Math.log(Math.abs(input) + Math.hypot(input, 1.0)), input);
   }

   public float[] evaluateNetwork(float[] rawFeatures) {
      if (rawFeatures.length != this.rawFeatureNames.length) {
         throw new IllegalArgumentException(WRONG_FEATURE_COUNT_MESSAGE);
      }

      for (float var5 : rawFeatures) {
         if (!Float.isFinite(var5)) {
            throw new IllegalArgumentException(NONFINITE_INPUT_MESSAGE);
         }
      }

      float[] layerOutput = new float[this.transformedFeatureCount];

      for (int var16 = 0; var16 < this.transformedFeatureCount; var16++) {
         double transformedFeature = rawFeatures[this.featureSourceIndices[var16]];
         double transformScale = this.featureTransformScales[var16];

         transformedFeature = switch (this.featureTransformTypes[var16]) {
            case 0 -> transformedFeature / transformScale;
            case 1 -> asinh(transformedFeature / transformScale);
            case 2 -> Math.sin(transformedFeature * transformScale);
            case 3 -> Math.cos(transformedFeature * transformScale);
            case 4 -> Math.log1p(Math.max(transformedFeature, 0.0) / transformScale);
            default -> throw new IllegalStateException();
         };
         layerOutput[var16] = (float)(((float)transformedFeature - this.featureMeans[var16]) / this.featureStandardDeviations[var16]);
      }

      layerOutput = this.runDenseLayer(ENCODER_LAYER_NAME, layerOutput);

      for (int var17 = 0; var17 < layerOutput.length; var17++) {
         layerOutput[var17] = (float)(layerOutput[var17] * sigmoid(layerOutput[var17]));
      }

      for (int var18 = 0; var18 < this.gruLayerCount; var18++) {
         float[] inputGateValues = applyDenseLayer(this.tensorsByName.get("gru.weight_ih_l" + var18), this.tensorsByName.get("gru.bias_ih_l" + var18), layerOutput);
         float[] recurrentGateValues = applyDenseLayer(this.tensorsByName.get("gru.weight_hh_l" + var18), this.tensorsByName.get("gru.bias_hh_l" + var18), this.recurrentHiddenStates[var18]);
         float[] nextHiddenState = new float[this.gruHiddenSize];

         for (int var7 = 0; var7 < this.gruHiddenSize; var7++) {
            double resetGate = sigmoid(inputGateValues[var7] + recurrentGateValues[var7]);
            double updateGate = sigmoid(inputGateValues[this.gruHiddenSize + var7] + recurrentGateValues[this.gruHiddenSize + var7]);
            double candidateState = Math.tanh(inputGateValues[2 * this.gruHiddenSize + var7] + resetGate * recurrentGateValues[2 * this.gruHiddenSize + var7]);
            nextHiddenState[var7] = (float)((1.0 - updateGate) * candidateState + updateGate * this.recurrentHiddenStates[var18][var7]);
         }

         this.recurrentHiddenStates[var18] = nextHiddenState;
         layerOutput = nextHiddenState;
      }

      float[] networkOutput = this.runDenseLayer(HEAD_LAYER_NAME, layerOutput);

      for (float var28 : networkOutput) {
         if (!Float.isFinite(var28)) {
            this.resetState();
            throw new IllegalStateException(NONFINITE_NETWORK_OUTPUT_MESSAGE);
         }
      }

      return networkOutput;
   }

   private record VelocityStep(double[] velocity, int clippedAxes) {
   }

   public record Action(double yawVelocity, double pitchVelocity, int component, int clippedSteps, long timestampNs, int clippedAxes) {
   }
}
