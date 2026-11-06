package sg.rotation;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import lombok.Generated;
import sg.rotation.AcceleratingAimRotation;
import sg.rotation.SnapAimRotation;
import sg.rotation.DirectAimRotation;
import sg.rotation.SlowAimRotation;
import sg.rotation.OscillatingAimRotation;
import sg.rotation.HumanAimRotation;
import sg.rotation.HumanizedRotation;
import sg.rotation.RotationStrategy;

public class RotationRegistry extends ArrayList<RotationStrategy> {
   private final FunTimeSmoothMode funTimeSmoothMode = new FunTimeSmoothMode(null);
   private final SpookyTimeSmoothMode spookyTimeSmoothMode = new SpookyTimeSmoothMode(null);

   public FunTimeSmoothMode getFunTimeSmoothMode() { return this.funTimeSmoothMode; }
   public SpookyTimeSmoothMode getSpookyTimeSmoothMode() { return this.spookyTimeSmoothMode; }
   @SerializedName("В")
   private final SlowAimRotation slowAimRotation = new SlowAimRotation(null);
   @SerializedName("В")
   private final OscillatingAimRotation oscillatingAimRotation = new OscillatingAimRotation(null);
   @SerializedName("В")
   private final RandomizedSmoothRotation randomizedSmoothRotation = new RandomizedSmoothRotation(null);
   @SerializedName("В")
   private final SnapAimRotation snapAimRotation = new SnapAimRotation(null);
   @SerializedName("В")
   private final HumanizedRotation humanizedRotation = new HumanizedRotation(null);
   @SerializedName("В")
   private final DirectAimRotation directAimRotation = new DirectAimRotation(null);
   @SerializedName("В")
   private final AcceleratingAimRotation acceleratingAimRotation = new AcceleratingAimRotation(null);
   @SerializedName("В")
   private final DefensiveJitterRotation defensiveJitterRotation = new DefensiveJitterRotation(null);
   @SerializedName("В")
   private final HumanAimRotation humanAimRotation = new HumanAimRotation(null);
   @SerializedName("В")
   private RotationStrategy activeStrategy = null;
   private @com.google.gson.annotations.SerializedName("Я")  RotationStrategy scheduledStrategy = null;
   private @com.google.gson.annotations.SerializedName("ВЗ")  int scheduledPriority = 0;
   private @com.google.gson.annotations.SerializedName("ВЕ")  int scheduledDurationTicks = 0;
   private @com.google.gson.annotations.SerializedName("ВП")  int elapsedScheduledTicks = 0;
   private @com.google.gson.annotations.SerializedName("сР")  boolean scheduledRotationExpired = false;

   @Generated
   public DefensiveJitterRotation getDefensiveJitterRotation() {
      return this.defensiveJitterRotation;
   }

   @Generated
   public int getElapsedScheduledTicks() {
      return this.elapsedScheduledTicks;
   }

   public boolean activateStrategy(RotationStrategy strategy) {
      if (this.scheduledPriority > 0) {
         return false;
      }

      this.activeStrategy = strategy;
      return true;
   }

   @Generated
   public RandomizedSmoothRotation getRandomizedSmoothRotation() {
      return this.randomizedSmoothRotation;
   }

   public boolean removeStrategy(RotationStrategy strategy) {
      if (this.activeStrategy != strategy && this.scheduledStrategy != strategy) {
         return false;
      }

      this.activeStrategy = null;
      this.scheduledStrategy = null;
      this.scheduledPriority = 0;
      this.scheduledDurationTicks = 0;
      this.elapsedScheduledTicks = 0;
      this.scheduledRotationExpired = false;
      return true;
   }

   @Generated
   public HumanAimRotation getHumanAimRotation() {
      return this.humanAimRotation;
   }

   @Generated
   public boolean isScheduledRotationExpired() {
      return this.scheduledRotationExpired;
   }

   public void tickScheduledRotation() {
      if (this.scheduledStrategy != null && !this.scheduledRotationExpired && this.activeStrategy == this.scheduledStrategy) {
         if (++this.elapsedScheduledTicks > this.scheduledDurationTicks) {
            this.activeStrategy = null;
            this.scheduledRotationExpired = true;
         }
      }
   }

   @Generated
   public AcceleratingAimRotation getAcceleratingAimRotation() {
      return this.acceleratingAimRotation;
   }

   @Generated
   public DirectAimRotation getDirectAimRotation() {
      return this.directAimRotation;
   }

   @Generated
   public OscillatingAimRotation getOscillatingAimRotation() {
      return this.oscillatingAimRotation;
   }

   @Generated
   public HumanizedRotation getHumanizedRotation() {
      return this.humanizedRotation;
   }

   public boolean clearExpiredScheduledStrategy(RotationStrategy strategy) {
      if (!this.isScheduledStrategyExpired(strategy)) {
         return false;
      }

      this.scheduledStrategy = null;
      this.scheduledPriority = 0;
      this.scheduledDurationTicks = 0;
      this.elapsedScheduledTicks = 0;
      this.scheduledRotationExpired = false;
      return true;
   }

   @Generated
   public int getScheduledDurationTicks() {
      return this.scheduledDurationTicks;
   }

   public boolean isScheduledStrategyExpired(RotationStrategy strategy) {
      return this.scheduledRotationExpired && this.scheduledStrategy == strategy;
   }

   public RotationRegistry() {
      this.add(this.funTimeSmoothMode);
      this.add(this.spookyTimeSmoothMode);
      this.add(this.slowAimRotation);
      this.add(this.oscillatingAimRotation);
      this.add(this.acceleratingAimRotation);
      this.add(this.randomizedSmoothRotation);
      this.add(this.snapAimRotation);
      this.add(this.humanizedRotation);
      this.add(this.defensiveJitterRotation);
      this.add(this.directAimRotation);
      this.add(this.humanAimRotation);
   }

   public boolean expireScheduledStrategy(RotationStrategy strategy) {
      if (this.activeStrategy == strategy && this.scheduledStrategy == strategy) {
         this.activeStrategy = null;
         this.scheduledRotationExpired = true;
         return true;
      } else {
         return false;
      }
   }

   @Generated
   public SnapAimRotation getSnapAimRotation() {
      return this.snapAimRotation;
   }

   public boolean scheduleStrategy(RotationStrategy strategy, int durationTicks, int priority) {
      if (this.scheduledPriority > priority) {
         return false;
      }

      this.activeStrategy = strategy;
      this.scheduledStrategy = strategy;
      this.scheduledPriority = priority;
      this.scheduledDurationTicks = durationTicks;
      this.elapsedScheduledTicks = -1;
      this.scheduledRotationExpired = false;
      return true;
   }

   @Generated
   public SlowAimRotation getSlowAimRotation() {
      return this.slowAimRotation;
   }

   @Generated
   public int getScheduledPriority() {
      return this.scheduledPriority;
   }

   @Generated
   public RotationStrategy getActiveStrategy() {
      return this.activeStrategy;
   }

   @Generated
   public RotationStrategy getScheduledStrategy() {
      return this.scheduledStrategy;
   }
}
