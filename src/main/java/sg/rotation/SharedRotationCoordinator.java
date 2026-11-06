package sg.rotation;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.ec.MinecraftAccess;
import sg.util.input.MouseSensitivityMath;
import sg.client.RoseClient;
import sg.rotation.RotationStrategy;

public final class SharedRotationCoordinator implements MinecraftAccess {
   private static final SharedRotationCoordinator INSTANCE = new SharedRotationCoordinator();
   private static final int АД = 2;
   private static final int АЪ = 1;
   private static final int Ат = 2;
   private static final int Аа = 30;
   private static final int АЮ = -100;
   @SerializedName("В")
   private final Object[] ownerSlots = new Object[2];
   private final @com.google.gson.annotations.SerializedName("Л")  int[] lastOwnerRequestTicks = new int[]{-100, -100};
   @SerializedName("В")
   private final SharedRotationCoordinator.SharedRotation sharedStrategy = new SharedRotationCoordinator.SharedRotation();
   private @com.google.gson.annotations.SerializedName("АЧ")  int lastAcceptedTick = -100;
   private @com.google.gson.annotations.SerializedName("АЛ")  int lastObservedTick = -100;
   private @com.google.gson.annotations.SerializedName("п")  Object competingOwner;
   private @com.google.gson.annotations.SerializedName("Д")  Object activeOwner;
   private static final double Я5П = 90.0;
   private static final float Я5西 = -90.0F;
   private static final float Я5д = 90.0F;

   private boolean requestRotation(Object owner, Vector2f rotation, Vec3 targetPosition) {
      int tick = MINECRAFT_CLIENT.player.tickCount;
      if (tick < this.lastObservedTick) {
         this.resetOwnership();
      }

      this.lastObservedTick = tick;
      int ownerSlot = this.registerOwnerSlot(owner);
      if (ownerSlot != -1) {
         this.lastOwnerRequestTicks[ownerSlot] = tick;
      }

      if (this.lastAcceptedTick == tick) {
         return false;
      } else {
         Object otherOwner = ownerSlot == -1 ? null : this.ownerSlots[1 - ownerSlot];
         if (otherOwner != null && this.wasOwnerRecentlyActive(1 - ownerSlot, tick) && this.competingOwner != null && this.competingOwner != owner) {
            return false;
         } else {
            this.lastAcceptedTick = tick;
            this.competingOwner = otherOwner;
            this.sharedStrategy.setTargetPosition(targetPosition);
            this.sharedStrategy.setTargetAngles(rotation.x, rotation.y);
            RotationRegistry rotationRegistry = RoseClient.getInstance().getServices().getRotations();
            boolean scheduled = rotationRegistry.scheduleStrategy(this.sharedStrategy, 2, 30);
            if (scheduled && rotationRegistry.getActiveStrategy() == this.sharedStrategy) {
               this.activeOwner = owner;
               Vector2f appliedRotation = this.sharedStrategy.calculateRotation(new Vector2f(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot()), this.sharedStrategy.getTargetRotation());
               RoseClient.getInstance().getControllers().getRotations().getAppliedRotation().set(appliedRotation);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   public static SharedRotationCoordinator getInstance() {
      return INSTANCE;
   }

   public boolean isActiveOwner(Object owner) {
      return this.activeOwner == owner && RoseClient.getInstance().getServices().getRotations().getActiveStrategy() == this.sharedStrategy;
   }

   public boolean requestRotation(Object owner, Vector2f rotation) {
      return owner != null && rotation != null && MINECRAFT_CLIENT.player != null ? this.requestRotation(owner, rotation, MINECRAFT_CLIENT.player.getEyePosition()) : false;
   }

   private SharedRotationCoordinator() {
   }

   private boolean wasOwnerRecentlyActive(int ownerSlot, int tick) {
      int ticksSinceRequest = tick - this.lastOwnerRequestTicks[ownerSlot];
      return ticksSinceRequest >= 0 && ticksSinceRequest <= 1;
   }

   public void resetOwnership() {
      this.lastOwnerRequestTicks[0] = -100;
      this.lastOwnerRequestTicks[1] = -100;
      this.lastAcceptedTick = -100;
      this.competingOwner = null;
      this.activeOwner = null;
   }

   private int registerOwnerSlot(Object owner) {
      int existingSlot = this.findOwnerSlot(owner);
      if (existingSlot != -1) {
         return existingSlot;
      }

      for (int slot = 0; slot < 2; slot++) {
         if (this.ownerSlots[slot] == null) {
            this.ownerSlots[slot] = owner;
            return slot;
         }
      }

      return -1;
   }

   private Vector2f rotationToPosition(Vec3 targetPosition) {
      Vec3 targetDirection = targetPosition.subtract(MINECRAFT_CLIENT.player.getEyePosition());
      float yaw = (float)(Math.toDegrees(Math.atan2(targetDirection.z, targetDirection.x)) - Я5П);
      float pitch = (float)(-Math.toDegrees(Math.atan2(targetDirection.y, Math.sqrt(targetDirection.x * targetDirection.x + targetDirection.z * targetDirection.z))));
      return new Vector2f(Mth.wrapDegrees(yaw), Mth.clamp(pitch, Я5西, Я5д));
   }

   public boolean requestRotation(Object owner, Vec3 targetPosition) {
      return owner != null && targetPosition != null && MINECRAFT_CLIENT.player != null ? this.requestRotation(owner, this.rotationToPosition(targetPosition), targetPosition) : false;
   }

   public void releaseOwner(Object owner) {
      int ownerSlot = this.findOwnerSlot(owner);
      if (ownerSlot != -1) {
         this.ownerSlots[ownerSlot] = null;
         this.lastOwnerRequestTicks[ownerSlot] = -100;
      }

      if (this.competingOwner == owner) {
         this.competingOwner = null;
      }

      if (this.activeOwner == owner) {
         RoseClient.getInstance().getServices().getRotations().removeStrategy(this.sharedStrategy);
         RoseClient.getInstance().getControllers().getRotations().releaseStrategy(this.sharedStrategy);
         this.activeOwner = null;
      }
   }

   private int findOwnerSlot(Object owner) {
      for (int slot = 0; slot < 2; slot++) {
         if (this.ownerSlots[slot] == owner) {
            return slot;
         }
      }

      return -1;
   }

   private static final class SharedRotation extends RotationStrategy {
      private final @com.google.gson.annotations.SerializedName("Я")  Vector2f targetAngles = new Vector2f();
      private static final float Щ3 = -90.0F;
      private static final float Щр = 90.0F;
      private static final float Щ6 = 180.0F;
      private static final float Щ_ = 180.0F;
      private static final float ЩШ = 1.0E-6F;
      private static final float Щы = -90.0F;
      private static final float Щй = 90.0F;

      @Override
      public Vector2f calculateRotation(Vector2f currentRotation, Vector2f targetRotation) {
         float yawDelta = Mth.wrapDegrees(targetRotation.x - currentRotation.x);
         float pitchDelta = targetRotation.y - currentRotation.y;
         float totalDelta = Math.abs(yawDelta) + Math.abs(pitchDelta);
         float maxYawStep = totalDelta == 0.0F ? 0.0F : Math.abs(yawDelta / totalDelta) * Щ6;
         float maxPitchStep = totalDelta == 0.0F ? 0.0F : Math.abs(pitchDelta / totalDelta) * Щ_;
         float rotationStep = MouseSensitivityMath.getRotationStep();
         float yawStep = Mth.clamp(yawDelta, -maxYawStep, maxYawStep);
         float pitchStep = Mth.clamp(pitchDelta, -maxPitchStep, maxPitchStep);
         if (rotationStep > ЩШ) {
            yawStep = Math.round(yawStep / rotationStep) * rotationStep;
            pitchStep = Math.round(pitchStep / rotationStep) * rotationStep;
         }

         float nextYaw = currentRotation.x + yawStep;
         float nextPitch = Mth.clamp(currentRotation.y + pitchStep, Щы, Щй);
         return new Vector2f(nextYaw, nextPitch);
      }

      private SharedRotation() {
         super(null);
         this.setUsesVanillaCameraRotation(false);
         this.setCaptureCameraRotationOnActivate(true);
      }

      private void setTargetAngles(float yaw, float pitch) {
         this.targetAngles.set(Mth.wrapDegrees(yaw), Mth.clamp(pitch, Щ3, Щр));
      }

      @Override
      public void resetState() {
         this.targetAngles.zero();
         this.targetPosition = null;
         this.returningToCamera = false;
      }

      @Override
      public Vector2f getTargetRotation() {
         return new Vector2f(this.targetAngles);
      }
   }
}
