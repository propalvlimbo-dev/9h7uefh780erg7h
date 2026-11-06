package sg.modules.impl.init;

import lombok.Generated;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import sg.ec.MinecraftAccess;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.client.RoseClient;
import sg.events.impl.MovementInputEvent;

public final class MovementUtils implements MinecraftAccess {
   private static final MovementUtils.SprintPacketListener sprintPacketListener = new MovementUtils.SprintPacketListener();
   private static boolean serverSprinting = false;
   private static final float FORWARD_LEFT_YAW_OFFSET = -45.0F;
   private static final float BACKWARD_LEFT_YAW_OFFSET = 45.0F;
   private static final float FORWARD_RIGHT_YAW_OFFSET = 45.0F;
   private static final float BACKWARD_RIGHT_YAW_OFFSET = -45.0F;
   private static final float BACKWARD_INPUT = -1.0F;
   private static final float VELOCITY_YAW_OFFSET = 90.0F;
   private static final float BACKWARD_DIRECTION_OFFSET = 180.0F;
   private static final float BACKWARD_STRAFE_SCALE = -0.5F;
   private static final float FORWARD_STRAFE_SCALE = 0.5F;
   private static final float LEFT_DIRECTION_OFFSET = 90.0F;
   private static final float RIGHT_DIRECTION_OFFSET = 90.0F;
   private static final float INITIAL_CORRECTION_YAW_ERROR = Float.MAX_VALUE;
   private static final float MIN_CORRECTION_FORWARD = -1.0F;
   private static final float MIN_CORRECTION_LEFT = -1.0F;
   private static final float INITIAL_EXPLICIT_CORRECTION_YAW_ERROR = Float.MAX_VALUE;
   private static final float MIN_EXPLICIT_CORRECTION_FORWARD = -1.0F;
   private static final float MIN_EXPLICIT_CORRECTION_LEFT = -1.0F;
   private static final float CALCULATED_BACKWARD_YAW_OFFSET = 180.0F;
   private static final float CALCULATED_BACKWARD_STRAFE_SCALE = -0.5F;
   private static final float CALCULATED_FORWARD_STRAFE_SCALE = 0.5F;
   private static final float CALCULATED_LEFT_YAW_OFFSET = 90.0F;
   private static final float CALCULATED_RIGHT_YAW_OFFSET = 90.0F;
   private static final double FLIGHT_BACKWARD_INPUT = -1.0;
   private static final float FLIGHT_FORWARD_X_YAW_OFFSET = 90.0F;
   private static final float FLIGHT_LEFT_X_YAW_OFFSET = 90.0F;
   private static final float FLIGHT_FORWARD_Z_YAW_OFFSET = 90.0F;
   private static final float FLIGHT_LEFT_Z_YAW_OFFSET = 90.0F;
   private static final float NO_KEY_MOVEMENT_YAW = -1.0F;
   private static final float KEY_BACKWARD_YAW_OFFSET = 180.0F;
   private static final float KEY_LEFT_YAW_OFFSET = 90.0F;
   private static final float KEY_RIGHT_YAW_OFFSET = 90.0F;
   private static final float KEY_FORWARD_LEFT_YAW_OFFSET = 45.0F;
   private static final float KEY_FORWARD_RIGHT_YAW_OFFSET = 45.0F;
   private static final float KEY_BACKWARD_LEFT_YAW_OFFSET = 45.0F;
   private static final float KEY_BACKWARD_RIGHT_YAW_OFFSET = 45.0F;
   private static final float NO_KEY_MOVEMENT_VECTOR_YAW = -1.0F;
   private static final String UTILITY_CONSTRUCTOR_MESSAGE = "This is a utility class and cannot be instantiated";

   public static void setHorizontalSpeedOrStop(double horizontalSpeed) {
      float forward = MINECRAFT_CLIENT.player.input.getMoveVector().y;
      float left = MINECRAFT_CLIENT.player.input.getMoveVector().x;
      float yawDegrees = MINECRAFT_CLIENT.player.getYRot();
      if (forward == 0.0F && left == 0.0F) {
         MINECRAFT_CLIENT.player.setDeltaMovement(0.0, MINECRAFT_CLIENT.player.getDeltaMovement().y, 0.0);
      } else {
         if (forward != 0.0F) {
            if (left > 0.0F) {
               yawDegrees += forward > 0.0F ? FORWARD_LEFT_YAW_OFFSET : BACKWARD_LEFT_YAW_OFFSET;
            } else if (left < 0.0F) {
               yawDegrees += forward > 0.0F ? FORWARD_RIGHT_YAW_OFFSET : BACKWARD_RIGHT_YAW_OFFSET;
            }

            left = 0.0F;
            forward = forward > 0.0F ? 1.0F : BACKWARD_INPUT;
         }

         double yawRadians = Math.toRadians(yawDegrees + VELOCITY_YAW_OFFSET);
         double velocityX = forward * horizontalSpeed * Math.cos(yawRadians) + left * horizontalSpeed * Math.sin(yawRadians);
         double velocityZ = forward * horizontalSpeed * Math.sin(yawRadians) - left * horizontalSpeed * Math.cos(yawRadians);
         MINECRAFT_CLIENT.player.setDeltaMovement(velocityX, MINECRAFT_CLIENT.player.getDeltaMovement().y, velocityZ);
      }
   }

   @Generated
   private MovementUtils() {
      throw new UnsupportedOperationException(UTILITY_CONSTRUCTOR_MESSAGE);
   }

   public static void correctMovementInput(MovementInputEvent event) {
      float forward = event.getForwardAmount();
      float left = event.getLeftAmount();
      double targetMovementYaw = Mth.wrapDegrees(
         Math.toDegrees(calculateMovementDirectionRadians(MINECRAFT_CLIENT.player.isFallFlying() ? MINECRAFT_CLIENT.player.getYRot() : RoseClient.getInstance().getControllers().getRotations().getCameraRotation().x, forward, left))
      );
      if (forward != 0.0F || left != 0.0F) {
         float correctedForward = 0.0F;
         float correctedLeft = 0.0F;
         float smallestYawError = INITIAL_CORRECTION_YAW_ERROR;

         for (float candidateForward = MIN_CORRECTION_FORWARD; candidateForward <= 1.0F; candidateForward++) {
            for (float candidateLeft = MIN_CORRECTION_LEFT; candidateLeft <= 1.0F; candidateLeft++) {
               if (candidateLeft != 0.0F || candidateForward != 0.0F) {
                  double candidateYaw = Mth.wrapDegrees(Math.toDegrees(calculateMovementDirectionRadians(MINECRAFT_CLIENT.player.getYRot(), candidateForward, candidateLeft)));
                  double yawError = Math.abs(Mth.wrapDegrees(targetMovementYaw - candidateYaw));
                  if (yawError < smallestYawError) {
                     smallestYawError = (float)yawError;
                     correctedForward = candidateForward;
                     correctedLeft = candidateLeft;
                  }
               }
            }
         }

         event.setForwardPressed(correctedForward > 0.0F);
         event.setBackwardPressed(correctedForward < 0.0F);
         event.setLeftPressed(correctedLeft > 0.0F);
         event.setRightPressed(correctedLeft < 0.0F);
         event.setForwardAmount(correctedForward);
         event.setLeftAmount(correctedLeft);
      }
   }

   public static boolean hasMovementInput() {
      Vec2 input = MINECRAFT_CLIENT.player.input.getMoveVector();
      return input.y != 0.0F || input.x != 0.0F;
   }

   public static double[] getKeyMovementDirectionVector() {
      float movementYaw = getKeyMovementYaw();
      if (movementYaw == NO_KEY_MOVEMENT_VECTOR_YAW) {
         return new double[]{0.0, 0.0};
      }

      double yawRadians = Math.toRadians(movementYaw);
      return new double[]{-Math.sin(yawRadians), Math.cos(yawRadians)};
   }

   public static double getMovementDirection(boolean radians) {
      Vec2 input = MINECRAFT_CLIENT.player.input.getMoveVector();
      float forward = input.y;
      float left = input.x;
      float yawDegrees = MINECRAFT_CLIENT.player.getYRot();
      if (forward < 0.0F) {
         yawDegrees += BACKWARD_DIRECTION_OFFSET;
      }

      float strafeScale = 1.0F;
      if (forward < 0.0F) {
         strafeScale = BACKWARD_STRAFE_SCALE;
      } else if (forward > 0.0F) {
         strafeScale = FORWARD_STRAFE_SCALE;
      }

      if (left > 0.0F) {
         yawDegrees -= LEFT_DIRECTION_OFFSET * strafeScale;
      }

      if (left < 0.0F) {
         yawDegrees += RIGHT_DIRECTION_OFFSET * strafeScale;
      }

      return radians ? Math.toRadians(yawDegrees) : yawDegrees;
   }

   public static void setFlightVelocity(double horizontalSpeed, double verticalSpeed) {
      double forward = MINECRAFT_CLIENT.player.input.getMoveVector().y;
      double left = MINECRAFT_CLIENT.player.input.getMoveVector().x;
      float yawDegrees = MINECRAFT_CLIENT.player.getYRot();
      double verticalVelocity = 0.0;
      if (MINECRAFT_CLIENT.options.keyJump.isDown()) {
         verticalVelocity = verticalSpeed;
      } else if (MINECRAFT_CLIENT.options.keyShift.isDown()) {
         verticalVelocity = -verticalSpeed;
      }

      if (forward == 0.0 && left == 0.0) {
         MINECRAFT_CLIENT.player.setDeltaMovement(0.0, verticalVelocity, 0.0);
      } else {
         if (forward != 0.0) {
            if (left > 0.0) {
               yawDegrees += forward > 0.0 ? -45 : 45;
            } else if (left < 0.0) {
               yawDegrees += forward > 0.0 ? 45 : -45;
            }

            left = 0.0;
            if (forward > 0.0) {
               forward = 1.0;
            } else if (forward < 0.0) {
               forward = FLIGHT_BACKWARD_INPUT;
            }
         }

         MINECRAFT_CLIENT.player
            .setDeltaMovement(
               forward * horizontalSpeed * Mth.cos((float)Math.toRadians(yawDegrees + FLIGHT_FORWARD_X_YAW_OFFSET)) + left * horizontalSpeed * Mth.sin((float)Math.toRadians(yawDegrees + FLIGHT_LEFT_X_YAW_OFFSET)),
               verticalVelocity,
               forward * horizontalSpeed * Mth.sin((float)Math.toRadians(yawDegrees + FLIGHT_FORWARD_Z_YAW_OFFSET)) - left * horizontalSpeed * Mth.cos((float)Math.toRadians(yawDegrees + FLIGHT_LEFT_Z_YAW_OFFSET))
            );
      }
   }

   public static float getKeyMovementYaw() {
      boolean rightPressed = MINECRAFT_CLIENT.options.keyRight.isDown();
      boolean leftPressed = MINECRAFT_CLIENT.options.keyLeft.isDown();
      boolean forwardPressed = MINECRAFT_CLIENT.options.keyUp.isDown();
      boolean backwardPressed = MINECRAFT_CLIENT.options.keyDown.isDown();
      float yawDegrees = MINECRAFT_CLIENT.player.getYRot();
      if (!rightPressed && !backwardPressed && !leftPressed && !forwardPressed) {
         return NO_KEY_MOVEMENT_YAW;
      }

      if (backwardPressed) {
         yawDegrees -= KEY_BACKWARD_YAW_OFFSET;
      }

      boolean hasExclusiveForwardBackwardInput = (backwardPressed || forwardPressed) && (!backwardPressed || !forwardPressed);
      if (!hasExclusiveForwardBackwardInput) {
         if (leftPressed) {
            yawDegrees -= KEY_LEFT_YAW_OFFSET;
         }

         if (rightPressed) {
            yawDegrees += KEY_RIGHT_YAW_OFFSET;
         }
      } else if (yawDegrees == MINECRAFT_CLIENT.player.getYRot()) {
         if (leftPressed) {
            yawDegrees -= KEY_FORWARD_LEFT_YAW_OFFSET;
         }

         if (rightPressed) {
            yawDegrees += KEY_FORWARD_RIGHT_YAW_OFFSET;
         }
      } else {
         if (leftPressed) {
            yawDegrees += KEY_BACKWARD_LEFT_YAW_OFFSET;
         }

         if (rightPressed) {
            yawDegrees -= KEY_BACKWARD_RIGHT_YAW_OFFSET;
         }
      }

      return Mth.wrapDegrees(yawDegrees);
   }

   private static void onPacketSend(PacketSendEvent event) {
      if (event.getPacket() instanceof ServerboundPlayerCommandPacket commandPacket) {
         if (commandPacket.getAction().equals(Action.STOP_SPRINTING)) {
            serverSprinting = false;
         } else if (commandPacket.getAction().equals(Action.START_SPRINTING)) {
            serverSprinting = true;
         }
      }
   }

   public static double calculateMovementDirectionRadians(float yawDegrees, double forward, double left) {
      if (forward < 0.0) {
         yawDegrees += CALCULATED_BACKWARD_YAW_OFFSET;
      }

      float strafeScale = 1.0F;
      if (forward < 0.0) {
         strafeScale = CALCULATED_BACKWARD_STRAFE_SCALE;
      } else if (forward > 0.0) {
         strafeScale = CALCULATED_FORWARD_STRAFE_SCALE;
      }

      if (left > 0.0) {
         yawDegrees -= CALCULATED_LEFT_YAW_OFFSET * strafeScale;
      }

      if (left < 0.0) {
         yawDegrees += CALCULATED_RIGHT_YAW_OFFSET * strafeScale;
      }

      return Math.toRadians(yawDegrees);
   }

   public static boolean isServerSprinting() {
      return serverSprinting;
   }

   public static void setHorizontalSpeed(double horizontalSpeed) {
      if (hasMovementInput()) {
         double yawRadians = getMovementDirection(true);
         MINECRAFT_CLIENT.player.setDeltaMovement(-Math.sin(yawRadians) * horizontalSpeed, MINECRAFT_CLIENT.player.getDeltaMovement().y, Math.cos(yawRadians) * horizontalSpeed);
      }
   }

   public static void correctMovementInput(MovementInputEvent event, float referenceYaw) {
      float forward = event.getForwardAmount();
      float left = event.getLeftAmount();
      double targetMovementYaw = Mth.wrapDegrees(Math.toDegrees(calculateMovementDirectionRadians(MINECRAFT_CLIENT.player.isFallFlying() ? MINECRAFT_CLIENT.player.getYRot() : referenceYaw, forward, left)));
      if (forward != 0.0F || left != 0.0F) {
         float correctedForward = 0.0F;
         float correctedLeft = 0.0F;
         float smallestYawError = INITIAL_EXPLICIT_CORRECTION_YAW_ERROR;

         for (float candidateForward = MIN_EXPLICIT_CORRECTION_FORWARD; candidateForward <= 1.0F; candidateForward++) {
            for (float candidateLeft = MIN_EXPLICIT_CORRECTION_LEFT; candidateLeft <= 1.0F; candidateLeft++) {
               if (candidateLeft != 0.0F || candidateForward != 0.0F) {
                  double candidateYaw = Mth.wrapDegrees(Math.toDegrees(calculateMovementDirectionRadians(MINECRAFT_CLIENT.player.getYRot(), candidateForward, candidateLeft)));
                  double yawError = Math.abs(Mth.wrapDegrees(targetMovementYaw - candidateYaw));
                  if (yawError < smallestYawError) {
                     smallestYawError = (float)yawError;
                     correctedForward = candidateForward;
                     correctedLeft = candidateLeft;
                  }
               }
            }
         }

         event.setForwardPressed(correctedForward > 0.0F);
         event.setBackwardPressed(correctedForward < 0.0F);
         event.setLeftPressed(correctedLeft > 0.0F);
         event.setRightPressed(correctedLeft < 0.0F);
         event.setForwardAmount(correctedForward);
         event.setLeftAmount(correctedLeft);
      }
   }

   static {
      RoseClient.getInstance().getEventBus().register(sprintPacketListener);
   }

   public static class SprintPacketListener {
      private final @com.google.gson.annotations.SerializedName("еэ")  EventListener<PacketSendEvent> packetSendListener = MovementUtils::onPacketSend;
   }
}
