package sg.render;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import sg.ec.WingAnimationMath;

public abstract class WingMotionState {
   static final int DEFAULT_TRANSITION_AGE_THRESHOLD = 2;
   private final @com.google.gson.annotations.SerializedName("Н")  int transitionAgeThreshold;
   private final @com.google.gson.annotations.SerializedName("е")  Consumer<WingAnimator> activationAction;
   private @com.google.gson.annotations.SerializedName("н")  int ageTicks;

   protected WingMotionState toGlidingState() {
      return new WingMotionState.GlidingState();
   }

   public final void activateAnimation(WingAnimator animator) {
      this.activationAction.accept(animator);
   }

   protected WingMotionState toGroundedState() {
      return new WingMotionState.GroundedState();
   }

   private WingMotionState evaluateTransition(boolean airborne, double deltaX, double deltaY, double deltaZ, Player player) {
      if (airborne) {
         return deltaY < 0.0 && player.getXRot() >= this.getMotionPitchDegrees(deltaX, deltaY, deltaZ) ? this.toGlidingState() : this.toFlightState();
      } else {
         return deltaY < 0.0 ? this.onGroundedDescent(player) : this.onGroundedVerticalMotion(deltaY);
      }
   }

   protected WingMotionState(int transitionAgeThreshold, Consumer<WingAnimator> activationAction) {
      this.transitionAgeThreshold = transitionAgeThreshold;
      this.activationAction = activationAction;
   }

   protected WingMotionState(Consumer<WingAnimator> activationAction) {
      this(2, activationAction);
   }

   protected WingMotionState toExtendedFlapState() {
      return new WingMotionState.ExtendedFlapState();
   }

   protected WingMotionState onGroundedVerticalMotion(double deltaY) {
      return this.toGroundedState();
   }

   public final WingMotionState update(boolean airborne, double deltaX, double deltaY, double deltaZ, Player player) {
      return this.ageTicks++ > this.transitionAgeThreshold ? this.evaluateTransition(airborne, deltaX, deltaY, deltaZ, player) : this;
   }

   protected WingMotionState toGroundDescentState() {
      return new WingMotionState.GroundDescentState();
   }

   protected WingMotionState toFlightState() {
      return new WingMotionState.FlightState();
   }

   private float getMotionPitchDegrees(double deltaX, double deltaY, double deltaZ) {
      return WingAnimationMath.toDegrees((float)(-Math.atan2(deltaY, Math.sqrt(deltaX * deltaX + deltaZ * deltaZ))));
   }

   protected WingMotionState onGroundedDescent(Player player) {
      return this.toGroundDescentState();
   }

   public static final class GlidingState extends WingMotionState {
      public GlidingState() {
         super(WingAnimator::setGliding);
      }

      @Override
      protected WingMotionState toFlightState() {
         return new WingMotionState.FlightState(6);
      }

      @Override
      protected WingMotionState toGlidingState() {
         return this;
      }
   }

   public static final class GroundDescentState extends WingMotionState {
      public GroundDescentState() {
         super(WingAnimator::setFalling);
      }

      @Override
      protected WingMotionState toGroundDescentState() {
         return this;
      }
   }

   public static final class FlightState extends WingMotionState {
      public FlightState() {
         this(2);
      }

      public FlightState(int transitionAgeThreshold) {
         super(transitionAgeThreshold, WingAnimator::setFlapping);
      }

      @Override
      protected WingMotionState onGroundedVerticalMotion(double deltaY) {
         return deltaY >= 0.0 ? this.toGlidingState() : super.onGroundedVerticalMotion(deltaY);
      }

      @Override
      protected WingMotionState toFlightState() {
         return this;
      }
   }

   public static final class GroundedState extends WingMotionState {
      private static final double SUPPORT_PROBE_Y_OFFSET = 0.25;

      public GroundedState() {
         super(WingAnimator::setFoldedIdle);
      }

      @Override
      protected WingMotionState onGroundedDescent(Player player) {
         BlockPos supportProbePos = BlockPos.containing(player.getX(), player.getY() - SUPPORT_PROBE_Y_OFFSET, player.getZ());
         return player.level().getBlockState(supportProbePos).isAir() && player.level().getBlockState(supportProbePos.below()).isAir() ? super.onGroundedDescent(player) : this.toGroundedState();
      }

      @Override
      protected WingMotionState toGroundedState() {
         return this;
      }
   }

   public static final class ExtendedFlapState extends WingMotionState {
      public ExtendedFlapState() {
         super(WingAnimator::setExtendedFlapping);
      }

      @Override
      protected WingMotionState toExtendedFlapState() {
         return this;
      }
   }
}
