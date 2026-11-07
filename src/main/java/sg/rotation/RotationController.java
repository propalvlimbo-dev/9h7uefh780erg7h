package sg.rotation;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.util.Mth;
import org.joml.Vector2f;
import sg.ec.ClientService;
import sg.rotation.SnapAimRotation;
import sg.events.impl.PlayerRotationEvent;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.Frame60Event;
import sg.client.RoseClient;
import sg.events.impl.CameraRotationEvent;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.events.impl.PlayerTurnEvent;
import sg.events.impl.RotationFrameEvent;
import sg.rotation.RotationStrategy;

public class RotationController extends ClientService {
   private final @com.google.gson.annotations.SerializedName("Ю")  Vector2f previousSmoothedHandRotation = new Vector2f();
   private final @com.google.gson.annotations.SerializedName("Ч")  Vector2f smoothedHandRotation = new Vector2f();
   private final @com.google.gson.annotations.SerializedName("Л")  Vector2f handRotation = new Vector2f();
   private final @com.google.gson.annotations.SerializedName("诶")  Vector2f cameraRotation = new Vector2f();
   @SerializedName("1")
   private final Vector2f appliedRotation = new Vector2f();
   private final @com.google.gson.annotations.SerializedName("сз")  EventListener<PlayerMovementSyncEvent> syncListener = this::onSync;
   private @com.google.gson.annotations.SerializedName("е")  RotationStrategy currentStrategy = null;
   private final @com.google.gson.annotations.SerializedName("сц")  EventListener<CameraRotationEvent> cameraRotationListener = this::onCameraRotation;
   private final @com.google.gson.annotations.SerializedName("ся")  EventListener<PlayerTurnEvent> playerTurnListener = this::onPlayerTurn;
   private final @com.google.gson.annotations.SerializedName("Вс")  EventListener<PlayerTickEvent> playerTickListener = new EventListener<PlayerTickEvent>() {
      @Override
      public int getPriority() {
         return -1;
      }

      public void onEvent(PlayerTickEvent event) {
         RotationController.this.onPlayerTick(event);
      }
   };
   private final @com.google.gson.annotations.SerializedName("ВВ")  EventListener<Frame60Event> frame60Listener = new EventListener<Frame60Event>() {
      public void onEvent(Frame60Event event) {
         RotationController.this.onFrame60(event);
      }

      @Override
      public int getPriority() {
         return -1;
      }
   };
   private final @com.google.gson.annotations.SerializedName("ВЯ")  EventListener<RotationFrameEvent> rotationFrameListener = new EventListener<RotationFrameEvent>() {
      public void onEvent(RotationFrameEvent event) {
         RotationController.this.onRotationFrame(event);
      }

      @Override
      public int getPriority() {
         return -1;
      }
   };
   private int Хк = -1;
   private int Хг;
   private static final float HAND_YAW_SMOOTHING_FACTOR = 0.5F;
   private static final float HAND_PITCH_SMOOTHING_FACTOR = 0.5F;
   private static final float ROTATION_RESET_DISTANCE = 5.0F;
   private static final float SLOW_SNAP_RESET_DISTANCE = 5.0F;
   private static final float FRAME_SNAP_RESET_DISTANCE = 5.0F;
   private static final String FUNTIME_SNAP_MODE = "FuntimeSnap";
   private static final float CAMERA_YAW_MOUSE_SCALE = 0.15F;
   private static final float CAMERA_PITCH_MOUSE_SCALE = 0.15F;
   private static final float MIN_CAMERA_PITCH = -90.0F;
   private static final float MAX_CAMERA_PITCH = 90.0F;
   private static final float HAND_YAW_MOUSE_SCALE = 0.15F;
   private static final float HAND_PITCH_MOUSE_SCALE = 0.15F;
   private static final float MIN_HAND_PITCH = -90.0F;
   private static final float MAX_HAND_PITCH = 90.0F;

   @Generated
   public Vector2f getSmoothedHandRotation() {
      return this.smoothedHandRotation;
   }

   @Generated
   public Vector2f getPreviousSmoothedHandRotation() {
      return this.previousSmoothedHandRotation;
   }

   public void onPlayerTick(PlayerTickEvent ignoredEvent) {
      this.previousSmoothedHandRotation.set(this.smoothedHandRotation);
      this.smoothedHandRotation.x = this.smoothedHandRotation.x + (this.handRotation.x - this.smoothedHandRotation.x) * HAND_YAW_SMOOTHING_FACTOR;
      this.smoothedHandRotation.y = this.smoothedHandRotation.y + (this.handRotation.y - this.smoothedHandRotation.y) * HAND_PITCH_SMOOTHING_FACTOR;
      if (this.isSnapRotationMode()) {
         this.updateSlowSnapRotation();
      } else {
         this.updateRotation(RotationStrategy.Speed.SLOW, 2);
      }
   }

   public void onSync(PlayerMovementSyncEvent ignoredEvent) {
   }

   private void updateRotation(RotationStrategy.Speed speed, int ignoredArgument) {
      RotationRegistry rotationRegistry = RoseClient.getInstance().getServices().getRotations();
      if (speed == RotationStrategy.Speed.SLOW) {
         rotationRegistry.tickScheduledRotation();
      }

      RotationStrategy activeStrategy = rotationRegistry.getActiveStrategy();
      if (this.getEffectiveStrategy() == null || this.getEffectiveStrategy().getSpeed() == speed) {
         if (activeStrategy == null && this.currentStrategy != null) {
            this.currentStrategy.setReturningToCamera(true);
            this.appliedRotation.set(this.currentStrategy.calculateRotation(this.appliedRotation, this.cameraRotation));
            MINECRAFT_CLIENT.player.setYRot(this.appliedRotation.x);
            MINECRAFT_CLIENT.player.setXRot(this.appliedRotation.y);
            if (rotationRegistry.isScheduledStrategyExpired(this.currentStrategy)) {
               this.resetRotation();
            } else {
               Vector2f wrappedCameraRotation = new Vector2f(Mth.wrapDegrees(this.cameraRotation.x), this.cameraRotation.y);
               Vector2f wrappedAppliedRotation = new Vector2f(Mth.wrapDegrees(this.appliedRotation.x), this.appliedRotation.y);
               if (wrappedCameraRotation.distance(wrappedAppliedRotation) < ROTATION_RESET_DISTANCE) {
                  this.resetRotation();
               }
            }
         } else if (activeStrategy != null && activeStrategy.getTargetPosition() != null) {
            if (this.currentStrategy != activeStrategy) {
               if (this.currentStrategy == null && activeStrategy.shouldCaptureCameraRotationOnActivate()) {
                  this.cameraRotation.set(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot());
               }

               this.currentStrategy = activeStrategy;
            }

            this.appliedRotation.set(activeStrategy.calculateRotation(this.appliedRotation, activeStrategy.getTargetRotation()));
            PlayerRotationEvent rotationEvent = new PlayerRotationEvent(this.appliedRotation);
            RoseClient.getInstance().getEventBus().post(rotationEvent);
            MINECRAFT_CLIENT.player.setYRot(rotationEvent.getRotation().x);
            MINECRAFT_CLIENT.player.setXRot(rotationEvent.getRotation().y);
         }
      }
   }

   @Generated
   public Vector2f getAppliedRotation() {
      return this.appliedRotation;
   }

   public void onFrame60(Frame60Event ignoredEvent) {
      if (this.isSnapRotationMode()) {
         this.updateFrameSnapRotation();
      } else {
         this.updateRotation(RotationStrategy.Speed.MEDIUM, 6);
      }
   }

   public RotationStrategy getEffectiveStrategy() {
      RotationStrategy activeStrategy = RoseClient.getInstance().getServices().getRotations().getActiveStrategy();
      return activeStrategy != null ? activeStrategy : this.currentStrategy;
   }

   private void onCameraRotation(CameraRotationEvent event) {
      RotationStrategy cameraStrategy = this.currentStrategy != null ? this.currentStrategy : RoseClient.getInstance().getServices().getRotations().getActiveStrategy();
      if (cameraStrategy != null) {
         if (cameraStrategy.usesVanillaCameraRotation()) {
            return;
         }

         event.setYaw(this.cameraRotation.x);
         event.setPitch(this.cameraRotation.y);
      }
   }

   public void onRotationFrame(RotationFrameEvent ignoredEvent) {
      if (!this.isSnapRotationMode()) {
         this.updateRotation(RotationStrategy.Speed.FAST, 24);
      }
   }

   private void updateSlowSnapRotation() {
      RotationRegistry rotationRegistry = RoseClient.getInstance().getServices().getRotations();
      RotationStrategy activeStrategy = rotationRegistry.getActiveStrategy();
      if (this.getEffectiveStrategy() == null || this.getEffectiveStrategy().getSpeed() == RotationStrategy.Speed.SLOW) {
         rotationRegistry.tickScheduledRotation();
         if (activeStrategy == null && this.currentStrategy != null) {
            this.currentStrategy.setReturningToCamera(true);
            this.appliedRotation.set(this.currentStrategy.calculateRotation(this.appliedRotation, this.cameraRotation));
            MINECRAFT_CLIENT.player.setYRot(this.appliedRotation.x);
            MINECRAFT_CLIENT.player.setXRot(this.appliedRotation.y);
            if (rotationRegistry.isScheduledStrategyExpired(this.currentStrategy)) {
               this.resetRotation();
            } else {
               Vector2f wrappedCameraRotation = new Vector2f(Mth.wrapDegrees(this.cameraRotation.x), this.cameraRotation.y);
               Vector2f wrappedAppliedRotation = new Vector2f(Mth.wrapDegrees(this.appliedRotation.x), this.appliedRotation.y);
               if (wrappedCameraRotation.distance(wrappedAppliedRotation) < SLOW_SNAP_RESET_DISTANCE) {
                  this.resetRotation();
               }
            }
         } else if (activeStrategy != null && activeStrategy.getTargetPosition() != null) {
            if (this.currentStrategy != activeStrategy) {
               if (this.currentStrategy == null && activeStrategy.shouldCaptureCameraRotationOnActivate()) {
                  this.cameraRotation.set(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot());
               }

               this.currentStrategy = activeStrategy;
            }

            this.appliedRotation.set(activeStrategy.calculateRotation(this.appliedRotation, activeStrategy.getTargetRotation()));
            PlayerRotationEvent rotationEvent = new PlayerRotationEvent(this.appliedRotation);
            RoseClient.getInstance().getEventBus().post(rotationEvent);
            MINECRAFT_CLIENT.player.setYRot(rotationEvent.getRotation().x);
            MINECRAFT_CLIENT.player.setXRot(rotationEvent.getRotation().y);
         }
      }
   }

   public void resetRotation() {
      RotationRegistry rotationRegistry = RoseClient.getInstance().getServices().getRotations();
      boolean wasSnapStrategy = this.currentStrategy == rotationRegistry.getSnapAimRotation();
      boolean completedScheduledRotation = rotationRegistry.clearExpiredScheduledStrategy(this.currentStrategy);
      this.currentStrategy.resetState();
      this.currentStrategy = null;
      if (completedScheduledRotation) {
         MINECRAFT_CLIENT.player.setYRot(this.cameraRotation.x);
         MINECRAFT_CLIENT.player.setXRot(this.cameraRotation.y);
      } else {
         if (this.cameraRotation.x != 0.0F) {
            if (wasSnapStrategy) {
               MINECRAFT_CLIENT.player.setYRot(this.cameraRotation.x * (MINECRAFT_CLIENT.player.getYRot() / this.cameraRotation.x));
            } else {
               MINECRAFT_CLIENT.player.setYRot(MINECRAFT_CLIENT.player.getYRot() + Mth.wrapDegrees(this.cameraRotation.x - MINECRAFT_CLIENT.player.getYRot()));
            }
         }

         MINECRAFT_CLIENT.player.setXRot(this.cameraRotation.y);
      }
   }

   private boolean isSnapRotationMode() {
      RotationRegistry rotationRegistry = RoseClient.getInstance().getServices().getRotations();
      SnapAimRotation snapStrategy = rotationRegistry.getSnapAimRotation();
      return this.currentStrategy == snapStrategy || rotationRegistry.getActiveStrategy() == snapStrategy;
   }

   public void releaseStrategy(RotationStrategy strategy) {
      if (this.currentStrategy == strategy) {
         this.currentStrategy = null;
         this.appliedRotation.set(this.cameraRotation);
         if (MINECRAFT_CLIENT.player != null) {
            MINECRAFT_CLIENT.player.setYRot(this.cameraRotation.x);
            MINECRAFT_CLIENT.player.setXRot(this.cameraRotation.y);
         }
      }
   }

   @Generated
   public Vector2f getCameraRotation() {
      return this.cameraRotation;
   }

   private void updateFrameSnapRotation() {
      RotationRegistry rotationRegistry = RoseClient.getInstance().getServices().getRotations();
      rotationRegistry.tickScheduledRotation();
      RotationStrategy activeStrategy = rotationRegistry.getActiveStrategy();
      RotationStrategy updateStrategy = this.currentStrategy != null ? this.currentStrategy : rotationRegistry.getActiveStrategy();
      if (updateStrategy == null || updateStrategy.getSpeed() == RotationStrategy.Speed.FAST || updateStrategy == rotationRegistry.getDirectAimRotation()) {
         if (activeStrategy != null || this.currentStrategy != null) {
            if (activeStrategy == null) {
               this.currentStrategy.setReturningToCamera(true);
               this.appliedRotation.set(this.currentStrategy.calculateRotation(this.appliedRotation, this.cameraRotation));
               MINECRAFT_CLIENT.player.setYRot(this.appliedRotation.x);
               MINECRAFT_CLIENT.player.setXRot(this.appliedRotation.y);
               if (rotationRegistry.isScheduledStrategyExpired(this.currentStrategy)) {
                  this.resetRotation();
               } else {
                  Vector2f wrappedCameraRotation = new Vector2f(Mth.wrapDegrees(this.cameraRotation.x), this.cameraRotation.y);
                  Vector2f wrappedAppliedRotation = new Vector2f(Mth.wrapDegrees(this.appliedRotation.x), this.appliedRotation.y);
                  if (wrappedCameraRotation.distance(wrappedAppliedRotation) < FRAME_SNAP_RESET_DISTANCE) {
                     this.resetRotation();
                  }
               }
            } else if (activeStrategy.getTargetPosition() != null) {
               this.currentStrategy = activeStrategy;
               this.appliedRotation.set(activeStrategy.calculateRotation(this.appliedRotation, activeStrategy.getTargetRotation()));
               PlayerRotationEvent rotationEvent = new PlayerRotationEvent(this.appliedRotation);
               RoseClient.getInstance().getEventBus().post(rotationEvent);
               MINECRAFT_CLIENT.player.setYRot(this.appliedRotation.x);
               MINECRAFT_CLIENT.player.setXRot(this.appliedRotation.y);
            }
         }
      }
   }

   private void onPlayerTurn(PlayerTurnEvent event) {
      RotationStrategy cameraStrategy = this.currentStrategy != null ? this.currentStrategy : RoseClient.getInstance().getServices().getRotations().getActiveStrategy();
      if (cameraStrategy != null) {
         if (cameraStrategy.usesVanillaCameraRotation()) {
            return;
         }

         this.cameraRotation.x = this.cameraRotation.x + event.getYawDelta() * CAMERA_YAW_MOUSE_SCALE;
         this.cameraRotation.y = this.cameraRotation.y + event.getPitchDelta() * CAMERA_PITCH_MOUSE_SCALE;
         this.cameraRotation.y = Math.clamp(this.cameraRotation.y, MIN_CAMERA_PITCH, MAX_CAMERA_PITCH);
         event.setCancelled(true);
      } else {
         this.cameraRotation.set(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot());
         this.appliedRotation.set(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot());
      }

      this.handRotation.x = this.handRotation.x + event.getYawDelta() * HAND_YAW_MOUSE_SCALE;
      this.handRotation.y = this.handRotation.y + event.getPitchDelta() * HAND_PITCH_MOUSE_SCALE;
      this.handRotation.y = Math.clamp(this.cameraRotation.y, MIN_HAND_PITCH, MAX_HAND_PITCH);
   }

   @Generated
   public Vector2f getHandRotation() {
      return this.handRotation;
   }
}
