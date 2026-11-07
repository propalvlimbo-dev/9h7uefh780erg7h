package sg.rotation;

import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongSupplier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import settings.render.AimFeatureSampler;
import sg.ec.Aim60Schema;
import sg.ec.Aim60SchemaValidator;
import sg.client.RoseClient;
import sg.rotation.RotationStrategy;
import sg.util.input.MouseSensitivityMath;

public class HumanAimRotation extends RotationStrategy {
   private static final long Д_6da43b94 = 16666667L;
   private static final long Ъ_6da43b94 = 750000L;
   private static final long т = 8000000L;
   private static final long а = 50000000L;
   private static final long Ю = 2000000000L;
   private static final double В_3f39d5c3_at_85525b22 = 0.016666666666666666;
   private static final float В_f67ab10a_at_85525b22 = 0.82F;
   private static final long Ч = Long.MIN_VALUE;
   @SerializedName("В")
   private final AimFeatureSampler featureSampler = new AimFeatureSampler();
   @SerializedName("В")
   private final LongSupplier nanoClock;
   @SerializedName("В")
   private HumanAimPolicy policy;
   @SerializedName("Д")
   private boolean modelLoaded;
   @SerializedName("В")
   private LivingEntity target;
   @SerializedName("В")
   private Object worldIdentity;
   private @com.google.gson.annotations.SerializedName("Л")  long episodeCounter;
   @SerializedName("п")
   private String episodeKey;
   private @com.google.gson.annotations.SerializedName("诶")  long lastUpdateNs;
   @SerializedName("1")
   private long nextUpdateNs;
   private @com.google.gson.annotations.SerializedName("Ь")  long lastObservationNs;
   private @com.google.gson.annotations.SerializedName("衣")  long lastErrorLogNs;
   private @com.google.gson.annotations.SerializedName("Я")  float lastObservationYaw;
   private @com.google.gson.annotations.SerializedName("Х")  float lastObservationPitch;
   private @com.google.gson.annotations.SerializedName("е")  float yawQuantizationRemainder;
   @SerializedName("А")
   private float pitchQuantizationRemainder;
   @SerializedName("4")
   private float lastOutputYaw;
   @SerializedName("п")
   private float lastOutputPitch;
   @SerializedName("Ъ")
   private boolean hasLastOutput;
   private static final String INITIAL_EPISODE_KEY = "0";
   private static final long INITIAL_UPDATE_TIMESTAMP = Long.MIN_VALUE;
   private static final long INITIAL_NEXT_UPDATE_TIMESTAMP = Long.MIN_VALUE;
   private static final long INITIAL_OBSERVATION_TIMESTAMP = Long.MIN_VALUE;
   private static final long INITIAL_ERROR_LOG_TIMESTAMP = Long.MIN_VALUE;
   private static final long GAP_UPDATE_SENTINEL = Long.MIN_VALUE;
   private static final long MAX_UPDATE_GAP_NS = 50000000L;
   private static final String NONFINITE_SELF_ROTATION_MESSAGE = "Nonfinite self rotation";
   private static final double SAMPLER_ROTATION_SMOOTHING = 0.82F;
   private static final String UPDATE_ERROR_MESSAGE = "Human aim update failed";
   private static final long OBSERVATION_TIMESTAMP_SENTINEL = Long.MIN_VALUE;
   private static final double NANOSECONDS_PER_SECOND = 1.0E9;
   private static final double MILLISECONDS_PER_SECOND = 1000.0;
   private static final double MIN_OBSERVATION_INTERVAL_SECONDS = 0.008;
   private static final double MAX_OBSERVATION_INTERVAL_SECONDS = 0.03334;
   private static final float MIN_SAMPLE_INTERVAL_MS = 8.0F;
   private static final float MAX_SAMPLE_INTERVAL_MS = 33.34F;
   private static final float YAW_EXTERNAL_CHANGE_EPSILON = 1.0E-4F;
   private static final float PITCH_EXTERNAL_CHANGE_EPSILON = 1.0E-4F;
   private static final double YAW_ACTION_DURATION_SECONDS = 0.016666666666666666;
   private static final double PITCH_ACTION_DURATION_SECONDS = 0.016666666666666666;
   private static final float MIN_PITCH = -90.0F;
   private static final float MAX_PITCH = 90.0F;
   private static final String NONFINITE_QUANTIZED_ROTATION_MESSAGE = "GCDUtils returned a nonfinite rotation";
   private static final long UPDATE_DUE_SENTINEL = Long.MIN_VALUE;
   private static final long MIN_UPDATE_INTERVAL_NS = 8000000L;
   private static final long EARLY_UPDATE_TOLERANCE_NS = -750000L;
   private static final long INITIAL_SCHEDULE_SENTINEL = Long.MIN_VALUE;
   private static final long FIRST_UPDATE_DELAY_NS = 16666667L;
   private static final long UPDATE_PERIOD_NS = 16666667L;
   private static final long MAX_SCHEDULE_LAG_NS = 750000L;
   private static final long RESCHEDULE_DELAY_NS = 16666667L;
   private static final long RESET_OBSERVATION_TIMESTAMP = Long.MIN_VALUE;
   private static final long RESET_SCHEDULE_TIMESTAMP = Long.MIN_VALUE;
   private static final String POLICY_RELATIVE_PATH = "standalone-onnx-aim/policy.bin";
   private static final long ERROR_LOG_TIMESTAMP_SENTINEL = Long.MIN_VALUE;
   private static final long ERROR_LOG_INTERVAL_NS = 2000000000L;

   private void logRateLimitedError(String message, RuntimeException error, long timestampNs) {
      if (this.lastErrorLogNs == ERROR_LOG_TIMESTAMP_SENTINEL || timestampNs - this.lastErrorLogNs >= ERROR_LOG_INTERVAL_NS) {
         this.lastErrorLogNs = timestampNs;
         System.out.println(message + ": " + error.getMessage());
      }
   }

   public void resetTargetAndEpisode() {
      this.target = null;
      this.worldIdentity = null;
      this.resetSamplerState();
   }

   @Override
   public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
      if (!this.returningToCamera && this.modelLoaded) {
         return this.updatePolicyRotation(currentRotation);
      }

      this.resetTargetAndEpisode();
      RoseClient.getInstance().getControllers().getRotations().resetRotation();
      return new Vector2f(currentRotation.x, currentRotation.y);
   }

   public void disableModel() {
      this.clearModel();
   }

   public HumanAimRotation(Vec3 targetPosition) {
      this(targetPosition, System::nanoTime);
   }

   private boolean isValidTarget(LivingEntity target) {
      return target != null && target != MINECRAFT_CLIENT.player && target.isAlive() && !target.isRemoved() && !target.isSpectator();
   }

   public boolean isModelLoaded() {
      return this.modelLoaded;
   }

   private void advanceUpdateSchedule(long timestampNs) {
      if (this.lastUpdateNs == INITIAL_SCHEDULE_SENTINEL) {
         this.nextUpdateNs = timestampNs + FIRST_UPDATE_DELAY_NS;
      } else {
         this.nextUpdateNs = this.nextUpdateNs + UPDATE_PERIOD_NS;
         if (timestampNs - this.nextUpdateNs > MAX_SCHEDULE_LAG_NS) {
            this.nextUpdateNs = timestampNs + RESCHEDULE_DELAY_NS;
         }
      }

      this.lastUpdateNs = timestampNs;
   }

   private void resetEpisode() {
      if (this.policy != null) {
         this.policy.resetState();
      }

      this.episodeKey = Long.toString(++this.episodeCounter);
      this.lastObservationNs = RESET_OBSERVATION_TIMESTAMP;
      this.yawQuantizationRemainder = this.pitchQuantizationRemainder = 0.0F;
      this.hasLastOutput = false;
   }

   private void resetSamplerState() {
      this.featureSampler.ЬЫ();
      this.resetEpisode();
      this.lastUpdateNs = this.nextUpdateNs = RESET_SCHEDULE_TIMESTAMP;
   }

   private boolean isUpdateDue(long timestampNs) {
      if (this.lastUpdateNs == UPDATE_DUE_SENTINEL) {
         return true;
      }

      long elapsedNs = timestampNs - this.lastUpdateNs;
      return elapsedNs >= MIN_UPDATE_INTERVAL_NS && timestampNs - this.nextUpdateNs >= EARLY_UPDATE_TOLERANCE_NS;
   }

   @Override
   public boolean usesVanillaCameraRotation() {
      return false;
   }

   private void clearModel() {
      this.resetTargetAndEpisode();
      this.policy = null;
      this.modelLoaded = false;
   }

   public boolean loadModel() {
      this.clearModel();
      Path modelPath = FabricLoader.getInstance().getConfigDir().resolve(POLICY_RELATIVE_PATH);

      try {
         HumanAimPolicy loadedPolicy = new HumanAimPolicy(modelPath, ThreadLocalRandom.current().nextLong());
         Aim60SchemaValidator.л(loadedPolicy, Aim60Schema.Хп);
         this.policy = loadedPolicy;
         this.modelLoaded = true;
         System.out.println("Human aim model successfully loaded: " + modelPath);
         return true;
      } catch (IOException | RuntimeException loadError) {
         this.policy = null;
         this.modelLoaded = false;
         System.out.println("Human aim model could not be loaded: " + modelPath + ": " + loadError.getMessage());
         return false;
      }
   }

   HumanAimRotation(Vec3 targetPosition, LongSupplier nanoClock) {
      super(targetPosition, RotationStrategy.Speed.MEDIUM);
      this.episodeKey = INITIAL_EPISODE_KEY;
      this.lastUpdateNs = INITIAL_UPDATE_TIMESTAMP;
      this.nextUpdateNs = INITIAL_NEXT_UPDATE_TIMESTAMP;
      this.lastObservationNs = INITIAL_OBSERVATION_TIMESTAMP;
      this.lastErrorLogNs = INITIAL_ERROR_LOG_TIMESTAMP;
      this.nanoClock = nanoClock;
      this.loadModel();
   }

   private Vector2f updatePolicyRotation(Vector2f currentRotation) {
      if (this.policy != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         LivingEntity target = null;
         if (!this.isValidTarget(target)) {
            this.resetTargetAndEpisode();
            return new Vector2f(currentRotation.x, currentRotation.y);
         }

         long timestampNs = this.nanoClock.getAsLong();
         if (target != this.target || MINECRAFT_CLIENT.level != this.worldIdentity) {
            this.resetTargetAndEpisode();
            this.target = target;
            this.worldIdentity = MINECRAFT_CLIENT.level;
         }

         if (this.lastUpdateNs != GAP_UPDATE_SENTINEL && timestampNs - this.lastUpdateNs > MAX_UPDATE_GAP_NS) {
            this.resetSamplerState();
         }

         if (!this.isUpdateDue(timestampNs)) {
            return new Vector2f(currentRotation.x, currentRotation.y);
         }

         try {
            if (Float.isFinite(currentRotation.x) && Float.isFinite(currentRotation.y)) {
               this.featureSampler.К(SAMPLER_ROTATION_SMOOTHING);
               AimFeatureSampler.SampleResult sampleResult = this.featureSampler.З(MINECRAFT_CLIENT.player, target, MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true), timestampNs);
               if (sampleResult.resetHistory()) {
                  this.resetEpisode();
               }

               if (sampleResult.features() == null) {
                  return new Vector2f(currentRotation.x, currentRotation.y);
               }

               this.advanceUpdateSchedule(timestampNs);
               return this.applyPolicyAction(currentRotation, sampleResult.features(), timestampNs);
            } else {
               throw new IllegalArgumentException(NONFINITE_SELF_ROTATION_MESSAGE);
            }
         } catch (RuntimeException updateError) {
            this.resetSamplerState();
            this.logRateLimitedError(UPDATE_ERROR_MESSAGE, updateError, timestampNs);
            return new Vector2f(currentRotation.x, currentRotation.y);
         }
      } else {
         this.resetTargetAndEpisode();
         return new Vector2f(currentRotation.x, currentRotation.y);
      }
   }

   private Vector2f applyPolicyAction(Vector2f currentRotation, float[] rawFeatures, long timestampNs) {
      if (rawFeatures.length != 30) {
         throw new IllegalArgumentException("Extractor returned " + rawFeatures.length + " features; expected 30 raw features");
      }

      float[] features = (float[])rawFeatures.clone();
      if (this.lastObservationNs != OBSERVATION_TIMESTAMP_SENTINEL) {
         double observationIntervalSeconds = (timestampNs - this.lastObservationNs) / NANOSECONDS_PER_SECOND;
         features[0] = (float)(observationIntervalSeconds * MILLISECONDS_PER_SECOND);
         if (!(observationIntervalSeconds >= MIN_OBSERVATION_INTERVAL_SECONDS) || !(observationIntervalSeconds <= MAX_OBSERVATION_INTERVAL_SECONDS)) {
            throw new IllegalStateException("Accepted observations are " + features[0] + " ms apart; invoke rotation at about 60 Hz with fresh features");
         }

         features[12] = (float)(Mth.wrapDegrees(currentRotation.x - this.lastObservationYaw) / observationIntervalSeconds);
         features[13] = (float)((currentRotation.y - this.lastObservationPitch) / observationIntervalSeconds);
      }

      if (features[0] >= MIN_SAMPLE_INTERVAL_MS && features[0] <= MAX_SAMPLE_INTERVAL_MS) {
         HumanAimPolicy.Action action = this.policy.nextAction(Aim60SchemaValidator.л(features), this.episodeKey, timestampNs);
         this.lastObservationNs = timestampNs;
         this.lastObservationYaw = currentRotation.x;
         this.lastObservationPitch = currentRotation.y;
         if (action == null) {
            return new Vector2f(currentRotation.x, currentRotation.y);
         }

         if (this.hasLastOutput && (Math.abs(Mth.wrapDegrees(currentRotation.x - this.lastOutputYaw)) > YAW_EXTERNAL_CHANGE_EPSILON || Math.abs(currentRotation.y - this.lastOutputPitch) > PITCH_EXTERNAL_CHANGE_EPSILON)) {
            this.yawQuantizationRemainder = 0.0F;
            this.pitchQuantizationRemainder = 0.0F;
         }

         float yawStep = 0.0F;
         float pitchStep = 0.0F;
         if (action.yawVelocity() != 0.0) {
            float unquantizedYawDelta = (float)(action.yawVelocity() * YAW_ACTION_DURATION_SECONDS) + this.yawQuantizationRemainder;
            yawStep = MouseSensitivityMath.snapRotationDelta(unquantizedYawDelta);
            this.yawQuantizationRemainder = unquantizedYawDelta - yawStep;
         }

         if (action.pitchVelocity() != 0.0) {
            float unquantizedPitchDelta = (float)(action.pitchVelocity() * PITCH_ACTION_DURATION_SECONDS) + this.pitchQuantizationRemainder;
            pitchStep = MouseSensitivityMath.snapRotationDelta(unquantizedPitchDelta);
            this.pitchQuantizationRemainder = unquantizedPitchDelta - pitchStep;
         }

         float outputYaw = currentRotation.x + yawStep;
         float unclampedPitch = currentRotation.y + pitchStep;
         float outputPitch = Mth.clamp(unclampedPitch, MIN_PITCH, MAX_PITCH);
         if (outputPitch != unclampedPitch) {
            this.pitchQuantizationRemainder = 0.0F;
         }

         if (Float.isFinite(outputYaw) && Float.isFinite(outputPitch)) {
            this.lastOutputYaw = outputYaw;
            this.lastOutputPitch = outputPitch;
            this.hasLastOutput = true;
            return new Vector2f(outputYaw, outputPitch);
         } else {
            throw new IllegalStateException(NONFINITE_QUANTIZED_ROTATION_MESSAGE);
         }
      } else {
         throw new IllegalStateException("Extractor sample_dt_ms must be within 8..33.34 ms; got " + features[0]);
      }
   }
}
