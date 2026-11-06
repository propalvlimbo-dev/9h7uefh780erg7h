package sg.util.entity;

import com.google.gson.annotations.SerializedName;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sg.ec.MinecraftAccess;

public class PlayerMovementSimulator implements MinecraftAccess {
   public static final double ВЯ = 50.0;
   public final @com.google.gson.annotations.SerializedName("Д")  Player player;
   @SerializedName("В")
   public final PlayerMovementSimulator.SimulatedPlayerInput input;
   public @com.google.gson.annotations.SerializedName("ч")  Vec3 position;
   public @com.google.gson.annotations.SerializedName("Э")  Vec3 codecPosition;
   public @com.google.gson.annotations.SerializedName("必")  Vec3 velocity;
   @SerializedName("В")
   public AABB boundingBox;
   public @com.google.gson.annotations.SerializedName("еО")  float yaw;
   public @com.google.gson.annotations.SerializedName("еФ")  float pitch;
   public @com.google.gson.annotations.SerializedName("ВI")  boolean sprinting;
   public @com.google.gson.annotations.SerializedName("ВХ")  double fallDistance;
   public @com.google.gson.annotations.SerializedName("ХШ")  int jumpCooldownTicks;
   public @com.google.gson.annotations.SerializedName("ВТ")  boolean jumping;
   public @com.google.gson.annotations.SerializedName("ВО")  boolean fallFlying;
   public @com.google.gson.annotations.SerializedName("ВФ")  boolean onGround;
   public @com.google.gson.annotations.SerializedName("ВЙ")  boolean horizontalCollision;
   public @com.google.gson.annotations.SerializedName("Вк")  boolean verticalCollision;
   public @com.google.gson.annotations.SerializedName("Вг")  boolean inWater;
   public @com.google.gson.annotations.SerializedName("В0")  boolean swimming;
   public @com.google.gson.annotations.SerializedName("Вх")  boolean wasEyeInWater;
   @SerializedName("В")
   private final Object2DoubleMap<TagKey<Fluid>> fluidHeights;
   private final @com.google.gson.annotations.SerializedName("Я")  HashSet<TagKey<Fluid>> eyeFluidTags;
   private @com.google.gson.annotations.SerializedName("Хы")  int simulatedTicks = 0;
   private @com.google.gson.annotations.SerializedName("ВУ")  boolean edgeClippingDetected = false;
   private @com.google.gson.annotations.SerializedName("щ")  Vec3 previousPosition;
   private @com.google.gson.annotations.SerializedName("Г")  Vec3 previousVelocity;
   private @com.google.gson.annotations.SerializedName("Ве")  double partialTickFraction = 0.0;
   private static final double ВА = 0.5;
   private static final double TICK_DURATION_FOR_ADVANCE_MS = 50.0;
   private static final double PARTIAL_TICK_EPSILON = 1.0E-4;
   private static final double MIN_SIMULATION_Y = -70.0;
   private static final double ZERO_VELOCITY_X_THRESHOLD = 0.003;
   private static final double ZERO_VELOCITY_Y_THRESHOLD = 0.003;
   private static final double ZERO_VELOCITY_Z_THRESHOLD = 0.003;
   private static final float STRAFE_INPUT_DAMPING = 0.98F;
   private static final float FORWARD_INPUT_DAMPING = 0.98F;
   private static final double SWIMMING_DIVE_LOOK_THRESHOLD = -0.2;
   private static final double SWIMMING_DIVE_RESPONSE = 0.085;
   private static final double SWIMMING_VERTICAL_RESPONSE = 0.06;
   private static final double SWIMMING_HEAD_BLOCK_OFFSET = 0.1;
   private static final double DEFAULT_GRAVITY = 0.08;
   private static final double SLOW_FALLING_GRAVITY = 0.01;
   private static final float SPRINTING_WATER_DRAG = 0.9F;
   private static final float WATER_DRAG = 0.8F;
   private static final float WATER_INPUT_ACCELERATION = 0.02F;
   private static final float AIRBORNE_WATER_EFFICIENCY_SCALE = 0.5F;
   private static final float WATER_EFFICIENCY_DRAG_TARGET = 0.54600006F;
   private static final float WATER_EFFICIENCY_DRAG_DIVISOR = 3.0F;
   private static final float WATER_EFFICIENCY_SPEED_DIVISOR = 3.0F;
   private static final float DOLPHINS_GRACE_DRAG = 0.96F;
   private static final double WATER_CLIMB_VELOCITY = 0.2;
   private static final double WATER_VERTICAL_DRAG = 0.8;
   private static final double WATER_COLLISION_FREE_SPACE_HEIGHT = 0.6;
   private static final double WATER_COLLISION_JUMP_VELOCITY = 0.3;
   private static final float LAVA_INPUT_ACCELERATION = 0.02F;
   private static final double SHALLOW_LAVA_X_DRAG = 0.5;
   private static final double SHALLOW_LAVA_Y_DRAG = 0.8;
   private static final double SHALLOW_LAVA_Z_DRAG = 0.5;
   private static final double DEEP_LAVA_DRAG = 0.5;
   private static final double LAVA_GRAVITY_DIVISOR = 4.0;
   private static final double LAVA_COLLISION_FREE_SPACE_HEIGHT = 0.6;
   private static final double LAVA_COLLISION_JUMP_VELOCITY = 0.3;
   private static final double ELYTRA_FALL_DISTANCE_RESET_VELOCITY = -0.5;
   private static final float ELYTRA_PITCH_RADIANS = (float) (Math.PI / 180.0);
   private static final double ELYTRA_LIFT_VECTOR_SCALE = 0.4;
   private static final double ELYTRA_BASE_GRAVITY_SCALE = -1.0;
   private static final double ELYTRA_LIFT_GRAVITY_SCALE = 0.75;
   private static final double ELYTRA_DIVE_CONVERSION = -0.1;
   private static final double ELYTRA_PULL_UP_SCALE = 0.04;
   private static final double ELYTRA_PULL_UP_VERTICAL_GAIN = 3.2;
   private static final double ELYTRA_X_VELOCITY_BLEND = 0.1;
   private static final double ELYTRA_Z_VELOCITY_BLEND = 0.1;
   private static final double ELYTRA_HORIZONTAL_X_DRAG = 0.99;
   private static final double ELYTRA_VERTICAL_DRAG = 0.98;
   private static final double ELYTRA_HORIZONTAL_Z_DRAG = 0.99;
   private static final float GROUND_FRICTION_MULTIPLIER = 0.91F;
   private static final float AIR_FRICTION = 0.91F;
   private static final double LEVITATION_VELOCITY_PER_LEVEL = 0.05;
   private static final double LEVITATION_VELOCITY_BLEND = 0.2;
   private static final double UNLOADED_CHUNK_FALL_VELOCITY = -0.1;
   private static final double VERTICAL_AIR_DRAG = 0.98F;
   private static final double CREATIVE_FLYING_VERTICAL_DRAG = 0.6;
   private static final double CLIMBING_STEP_VELOCITY = 0.2;
   private static final double INPUT_VECTOR_EPSILON = 1.0E-7;
   private static final float INPUT_YAW_SIN_RADIANS = (float) (Math.PI / 180.0);
   private static final float INPUT_YAW_COS_RADIANS = (float) (Math.PI / 180.0);
   private static final double FALLING_FLUID_GRAVITY_DIVISOR = 16.0;
   private static final double RISING_FLUID_GRAVITY_DIVISOR = 32.0;
   private static final float GROUND_FRICTION_CUBE_SPEED_SCALE = 0.21600002F;
   private static final float AIR_CONTROL_SPEED = 0.02F;
   private static final float SPRINT_AIR_CONTROL_BONUS = 0.006F;
   private static final float BASE_MOVEMENT_SPEED = 0.1F;
   private static final double MIN_MOVE_LENGTH_SQUARED = 1.0E-7;
   private static final double SPRINT_JUMP_X_IMPULSE = 0.2;
   private static final double SPRINT_JUMP_Z_IMPULSE = 0.2;
   private static final double MIN_CLIMB_X_VELOCITY = -0.15F;
   private static final double MAX_CLIMB_X_VELOCITY = 0.15F;
   private static final double MIN_CLIMB_Z_VELOCITY = -0.15F;
   private static final double MAX_CLIMB_Z_VELOCITY = 0.15F;
   private static final double MIN_CLIMB_Y_VELOCITY = -0.15F;
   private static final double EDGE_CLIP_STEP = 0.05;
   private static final double EDGE_CLIP_X_VERTICAL_OFFSET = -0.5;
   private static final double EDGE_CLIP_Z_VERTICAL_OFFSET = -0.5;
   private static final double EDGE_CLIP_DIAGONAL_VERTICAL_OFFSET = -0.5;
   private static final double MAX_EDGE_CLIP_FALL_DISTANCE = 0.5;
   private static final double EDGE_CLIP_FALL_TEST_OFFSET = 0.5;
   private static final float BASE_JUMP_VELOCITY = 0.42F;
   private static final float JUMP_BOOST_PER_LEVEL = 0.1F;
   private static final double FLUID_JUMP_IMPULSE = 0.04F;
   private static final double GROUND_SAMPLE_Y_OFFSET = 0.5000001;
   private static final double LOW_EYE_HEIGHT_THRESHOLD = 0.4;
   private static final double DEFAULT_FLUID_JUMP_THRESHOLD = 0.4;
   private static final double WATER_FLOW_PUSH_SCALE = 0.014;
   private static final double EYE_FLUID_SAMPLE_OFFSET = 0.11111111F;
   private static final double FLUID_BOX_DEFLATION = 0.001;
   private static final double SHALLOW_FLOW_DEPTH = 0.4;
   private static final double LOW_HORIZONTAL_X_VELOCITY = 0.003;
   private static final double LOW_HORIZONTAL_Z_VELOCITY = 0.003;
   private static final double MIN_FLOW_PUSH_LENGTH = 0.0045;
   private static final double MIN_FLOW_PUSH_SPEED = 0.0045;
   private static final double LOOK_PITCH_PI = Math.PI;
   private static final double LOOK_PITCH_DEGREES = 180.0;
   private static final double LOOK_YAW_PI = Math.PI;
   private static final double LOOK_YAW_DEGREES = 180.0;

   private void updateSwimming() {
      if (this.swimming) {
         this.swimming = this.isSprinting() && this.isInWater() && !this.player.isPassenger();
      } else {
         this.swimming = this.isSprinting() && this.isUnderWater() && !this.player.isPassenger() && this.player.level().getFluidState(this.blockPositionAt(this.position)).is(FluidTags.WATER);
      }
   }

   private void updateEyeFluidState() {
      this.wasEyeInWater = this.eyeFluidTags.contains(FluidTags.WATER);
      this.eyeFluidTags.clear();
      double eyeFluidSampleY = this.getEyeY() - EYE_FLUID_SAMPLE_OFFSET;
      if (!(this.player.getVehicle() instanceof Boat var4 && !var4.isUnderWater() && var4.getBoundingBox().maxY >= eyeFluidSampleY && var4.getBoundingBox().minY <= eyeFluidSampleY)) {
         BlockPos eyeBlockPosition = BlockPos.containing(this.position.x, eyeFluidSampleY, this.position.z);
         FluidState fluidState = this.player.level().getFluidState(eyeBlockPosition);
         double fluidSurfaceY = eyeBlockPosition.getY() + fluidState.getHeight(this.player.level(), eyeBlockPosition);
         if (fluidSurfaceY > eyeFluidSampleY) {
            this.eyeFluidTags.addAll(fluidState.tags().toList());
         }
      }
   }

   private void move(Vec3 requestedMovement) {
      Vec3 edgeClippedMovement = requestedMovement;
      edgeClippedMovement = this.applyEdgeClipping(edgeClippedMovement);
      Vec3 collisionMovement = this.collide(edgeClippedMovement);
      if (collisionMovement.lengthSqr() > MIN_MOVE_LENGTH_SQUARED) {
         this.position = this.position.add(collisionMovement);
         this.boundingBox = this.player.getDimensions(this.player.getPose()).makeBoundingBox(this.position);
      }

      boolean xCollision = !Mth.equal(requestedMovement.x, collisionMovement.x);
      boolean zCollision = !Mth.equal(requestedMovement.z, collisionMovement.z);
      this.horizontalCollision = xCollision || zCollision;
      this.verticalCollision = requestedMovement.y != collisionMovement.y;
      this.onGround = this.verticalCollision && requestedMovement.y < 0.0;
      if (!this.isInWater()) {
         this.updateWaterState();
      }

      if (this.onGround) {
         this.resetFallDistance();
      } else if (requestedMovement.y < 0.0) {
         this.fallDistance = this.fallDistance - (float)requestedMovement.y;
      }

      Vec3 previousVelocity = this.velocity;
      if (this.horizontalCollision || this.verticalCollision) {
         this.velocity = new Vec3(xCollision ? 0.0 : previousVelocity.x, this.onGround ? 0.0 : previousVelocity.y, zCollision ? 0.0 : previousVelocity.z);
      }
   }

   private Vec3 moveWithGroundFriction(Vec3 movementInput, float blockFriction) {
      this.accelerateRelative(this.getFrictionAdjustedSpeed(blockFriction), movementInput);
      this.velocity = this.limitClimbingVelocity(this.velocity);
      this.move(this.velocity);
      Vec3 movement = this.velocity;
      BlockPos blockPosition = this.blockPositionAt(this.position);
      BlockState blockState = this.getBlockState(blockPosition);
      if ((this.horizontalCollision || this.jumping) && (this.isClimbable() || blockState != null && blockState.is(Blocks.POWDER_SNOW) && PowderSnowBlock.canEntityWalkOnPowderSnow(this.player))) {
         movement = new Vec3(movement.x, CLIMBING_STEP_VELOCITY, movement.z);
      }

      return movement;
   }

   private float getAirControlSpeed() {
      float baseSpeed = AIR_CONTROL_SPEED;
      return this.input.simulatedKeys.sprint() ? baseSpeed + SPRINT_AIR_CONTROL_BONUS : baseSpeed;
   }

   public boolean isClimbable() {
      BlockPos blockPosition = this.blockPositionAt(this.position);
      BlockState blockState = this.getBlockState(blockPosition);
      return blockState.is(BlockTags.CLIMBABLE) ? true : blockState.getBlock() instanceof TrapDoorBlock && this.isOpenTrapdoorAboveLadder(blockPosition, blockState);
   }

   private Vec3 limitClimbingVelocity(Vec3 velocity) {
      if (!this.isClimbable()) {
         return velocity;
      }

      this.resetFallDistance();
      double xVelocity = Mth.clamp(velocity.x, MIN_CLIMB_X_VELOCITY, MAX_CLIMB_X_VELOCITY);
      double zVelocity = Mth.clamp(velocity.z, MIN_CLIMB_Z_VELOCITY, MAX_CLIMB_Z_VELOCITY);
      double yVelocity = Math.max(velocity.y, MIN_CLIMB_Y_VELOCITY);
      if (yVelocity < 0.0 && !this.getBlockState(this.blockPositionAt(this.position)).is(Blocks.SCAFFOLDING) && this.player.isSuppressingSlidingDownLadder()) {
         yVelocity = 0.0;
      }

      return new Vec3(xVelocity, yVelocity, zVelocity);
   }

   public int getSimulatedTicks() {
      return this.simulatedTicks;
   }

   public double getPartialTickFraction() {
      return this.partialTickFraction;
   }

   private double getFluidJumpThreshold() {
      return this.player.getEyeHeight() < LOW_EYE_HEIGHT_THRESHOLD ? 0.0 : DEFAULT_FLUID_JUMP_THRESHOLD;
   }

   public Vec3 getPosition() {
      return this.position;
   }

   public static PlayerMovementSimulator fromPlayer(Player player, PlayerMovementSimulator.SimulatedPlayerInput input, Vec3 velocity) {
      return new PlayerMovementSimulator(
         player,
         input,
         player.position(),
         player.getPositionCodec().getBase(),
         velocity,
         player.getBoundingBox(),
         player.getYRot(),
         player.getXRot(),
         player.isSprinting(),
         player.fallDistance,
         0,
         input.simulatedKeys.jump(),
         player.isFallFlying(),
         player.onGround(),
         player.horizontalCollision,
         player.verticalCollision,
         player.isInWater(),
         player.isSwimming(),
         player.isUnderWater(),
         copyFluidHeights(player),
         copyEyeFluidTags(player)
      );
   }

   private Vec3 applyEdgeClipping(Vec3 movement) {
      if (movement.y <= 0.0 && this.hasGroundForEdgeClipping()) {
         double xMovement = movement.x;
         double zMovement = movement.z;

         double clipStep;
         for (clipStep = EDGE_CLIP_STEP; xMovement != 0.0 && this.player.level().noCollision(this.player, this.boundingBox.move(xMovement, EDGE_CLIP_X_VERTICAL_OFFSET, 0.0)); xMovement += xMovement > 0.0 ? -clipStep : clipStep) {
            if (xMovement < clipStep && xMovement >= -clipStep) {
               xMovement = 0.0;
               break;
            }
         }

         while (zMovement != 0.0 && this.player.level().noCollision(this.player, this.boundingBox.move(0.0, EDGE_CLIP_Z_VERTICAL_OFFSET, zMovement))) {
            if (zMovement < clipStep && zMovement >= -clipStep) {
               zMovement = 0.0;
               break;
            }

            zMovement += zMovement > 0.0 ? -clipStep : clipStep;
         }

         while (xMovement != 0.0 && zMovement != 0.0 && this.player.level().noCollision(this.player, this.boundingBox.move(xMovement, EDGE_CLIP_DIAGONAL_VERTICAL_OFFSET, zMovement))) {
            xMovement = xMovement < clipStep && xMovement >= -clipStep ? 0.0 : (xMovement > 0.0 ? xMovement - clipStep : xMovement + clipStep);
            if (zMovement < clipStep && zMovement >= -clipStep) {
               zMovement = 0.0;
               break;
            }

            zMovement += zMovement > 0.0 ? -clipStep : clipStep;
         }

         if (movement.x != xMovement || movement.z != zMovement) {
            this.edgeClippingDetected = true;
         }

         if (this.shouldClipEdges()) {
            movement = new Vec3(xMovement, movement.y, zMovement);
         }
      }

      return movement;
   }

   private void accelerateRelative(float speed, Vec3 movementInput) {
      Vec3 acceleration = movementInputToWorld(movementInput, speed, this.yaw);
      this.velocity = this.velocity.add(acceleration);
   }

   protected boolean shouldClipEdges() {
      return this.input.simulatedKeys.shift() || this.input.forceEdgeClipping;
   }

   private boolean isSprinting() {
      return this.sprinting;
   }

   public boolean isUnderWater() {
      return this.wasEyeInWater && this.isInWater();
   }

   private void updateWaterState() {
      if (this.player.getVehicle() instanceof Boat) {
         Boat boat = (Boat)this.player.getVehicle();
         if (!boat.isUnderWater()) {
            this.inWater = false;
            return;
         }
      }

      if (this.updateFluidHeightAndPush(FluidTags.WATER, WATER_FLOW_PUSH_SCALE)) {
         this.resetFallDistance();
         this.inWater = true;
      } else {
         this.inWater = false;
      }
   }

   private boolean updateFluidHeightAndPush(TagKey<Fluid> fluidTag, double pushScale) {
      if (this.hasMissingCollisionChunks()) {
         return false;
      }

      AABB fluidTestBox = this.boundingBox.deflate(FLUID_BOX_DEFLATION);
      int var5 = Mth.floor(fluidTestBox.minX);
      int var6 = Mth.ceil(fluidTestBox.maxX);
      int var7 = Mth.floor(fluidTestBox.minY);
      int var8 = Mth.ceil(fluidTestBox.maxY);
      int var9 = Mth.floor(fluidTestBox.minZ);
      int var10 = Mth.ceil(fluidTestBox.maxZ);
      double fluidHeight = 0.0;
      boolean applyFluidPush = true;
      boolean touchingFluid = false;
      Vec3 flow = Vec3.ZERO;
      int flowSampleCount = 0;
      MutableBlockPos blockPosition = new MutableBlockPos();

      for (int var18 = var5; var18 < var6; var18++) {
         for (int var19 = var7; var19 < var8; var19++) {
            for (int var20 = var9; var20 < var10; var20++) {
               blockPosition.set(var18, var19, var20);
               FluidState fluidState = this.player.level().getFluidState(blockPosition);
               if (fluidState.is(fluidTag)) {
                  double fluidSurfaceY = var19 + fluidState.getHeight(this.player.level(), blockPosition);
                  if (fluidSurfaceY >= fluidTestBox.minY) {
                     touchingFluid = true;
                     fluidHeight = Math.max(fluidSurfaceY - fluidTestBox.minY, fluidHeight);
                     if (applyFluidPush) {
                        Vec3 blockFlow = fluidState.getFlow(this.player.level(), blockPosition);
                        if (fluidHeight < SHALLOW_FLOW_DEPTH) {
                           blockFlow = blockFlow.scale(fluidHeight);
                        }

                        flow = flow.add(blockFlow);
                        flowSampleCount++;
                     }
                  }
               }
            }
         }
      }

      if (flow.length() > 0.0) {
         if (flowSampleCount > 0) {
            flow = flow.scale(1.0 / flowSampleCount);
         }

         flow = flow.scale(pushScale);
         if (Math.abs(this.velocity.x) < LOW_HORIZONTAL_X_VELOCITY && Math.abs(this.velocity.z) < LOW_HORIZONTAL_Z_VELOCITY && flow.length() < MIN_FLOW_PUSH_LENGTH) {
            flow = flow.normalize().scale(MIN_FLOW_PUSH_SPEED);
         }

         this.velocity = this.velocity.add(flow);
      }

      this.fluidHeights.put(fluidTag, fluidHeight);
      return touchingFluid;
   }

   private static Vec3 movementInputToWorld(Vec3 movementInput, float speed, float yaw) {
      double inputLengthSquared = movementInput.lengthSqr();
      if (inputLengthSquared < INPUT_VECTOR_EPSILON) {
         return Vec3.ZERO;
      }

      Vec3 scaledInput = (inputLengthSquared > 1.0 ? movementInput.normalize() : movementInput).scale(speed);
      float yawSin = Mth.sin(yaw * INPUT_YAW_SIN_RADIANS);
      float yawCos = Mth.cos(yaw * INPUT_YAW_COS_RADIANS);
      return new Vec3(scaledInput.x * yawCos - scaledInput.z * yawSin, scaledInput.y, scaledInput.z * yawCos + scaledInput.x * yawSin);
   }

   private boolean isFreeAtOffset(double xOffset, double yOffset, double zOffset) {
      return this.isFree(this.boundingBox.move(xOffset, yOffset, zOffset));
   }

   private static Vec3 lerpVector(Vec3 start, Vec3 end, double fraction) {
      return new Vec3(start.x + (end.x - start.x) * fraction, start.y + (end.y - start.y) * fraction, start.z + (end.z - start.z) * fraction);
   }

   private Vec3 collide(Vec3 movement) {
      AABB box = this.boundingBox;
      List entityCollisions = Collections.emptyList();
      Vec3 collisionMovement;
      if (movement.lengthSqr() == 0.0) {
         collisionMovement = movement;
      } else {
         collisionMovement = Entity.collideBoundingBox(this.player, movement, box, this.player.level(), entityCollisions);
      }

      boolean var5 = movement.x != collisionMovement.x;
      boolean var6 = movement.y != collisionMovement.y;
      boolean var7 = movement.z != collisionMovement.z;
      boolean canStep = this.onGround || var6 && movement.y < 0.0;
      if (this.player.maxUpStep() > 0.0F && canStep && (var5 || var7)) {
         Vec3 stepMovement = Entity.collideBoundingBox(this.player, new Vec3(movement.x, this.player.maxUpStep(), movement.z), box, this.player.level(), entityCollisions);
         Vec3 verticalStep = Entity.collideBoundingBox(this.player, new Vec3(0.0, this.player.maxUpStep(), 0.0), box.expandTowards(movement.x, 0.0, movement.z), this.player.level(), entityCollisions);
         Vec3 horizontalStep = Entity.collideBoundingBox(this.player, new Vec3(movement.x, 0.0, movement.z), box.move(verticalStep), this.player.level(), entityCollisions).add(verticalStep);
         if (verticalStep.y < this.player.maxUpStep() && horizontalStep.horizontalDistanceSqr() > stepMovement.horizontalDistanceSqr()) {
            stepMovement = horizontalStep;
         }

         if (stepMovement.horizontalDistanceSqr() > collisionMovement.horizontalDistanceSqr()) {
            return stepMovement.add(Entity.collideBoundingBox(this.player, new Vec3(0.0, -stepMovement.y + movement.y, 0.0), box.move(stepMovement), this.player.level(), entityCollisions));
         }
      }

      return collisionMovement;
   }

   public static PlayerMovementSimulator predictTicks(int ticks) {
      PlayerMovementSimulator simulation = fromClientPlayer(PlayerMovementSimulator.SimulatedPlayerInput.fromClientPlayer(MINECRAFT_CLIENT.player.getLastSentInput()));

      for (int var2 = 0; var2 < ticks; var2++) {
         simulation.tick();
      }

      return simulation;
   }

   private double getEyeY() {
      return this.position.y + this.player.getEyeHeight();
   }

   private void jumpInFluid(TagKey<Fluid> fluidTag) {
      this.velocity = this.velocity.add(0.0, FLUID_JUMP_IMPULSE, 0.0);
   }

   private static HashSet<TagKey<Fluid>> copyEyeFluidTags(Player player) {
      HashSet fluidTags = new HashSet();
      if (player.isEyeInFluid(FluidTags.WATER)) {
         fluidTags.add(FluidTags.WATER);
      }

      if (player.isEyeInFluid(FluidTags.LAVA)) {
         fluidTags.add(FluidTags.LAVA);
      }

      return fluidTags;
   }

   public BlockPos blockPositionAt(Vec3 position) {
      return new BlockPos(Mth.floor(position.x), Mth.floor(position.y), Mth.floor(position.z));
   }

   public static PlayerMovementSimulator predictForDuration(Player player, long durationMs) {
      PlayerMovementSimulator simulation = fromPlayer(player, PlayerMovementSimulator.SimulatedPlayerInput.guessInput(player));
      simulation.advanceForDuration(durationMs);
      return simulation;
   }

   private double getFluidHeight(TagKey<Fluid> fluidTag) {
      return this.fluidHeights.getDouble(fluidTag);
   }

   private boolean isOpenTrapdoorAboveLadder(BlockPos blockPosition, BlockState blockState) {
      if (!blockState.getValue(TrapDoorBlock.OPEN)) {
         return false;
      }

      BlockState belowState = this.player.level().getBlockState(blockPosition.below());
      return belowState.is(Blocks.LADDER) && belowState.getValue(LadderBlock.FACING).equals(blockState.getValue(TrapDoorBlock.FACING));
   }

   private Vec3 lookVector(float pitch, float yaw) {
      float pitchRadians = (float)(pitch * LOOK_PITCH_PI / LOOK_PITCH_DEGREES);
      float negativeYawRadians = (float)(-yaw * LOOK_YAW_PI / LOOK_YAW_DEGREES);
      float yawCos = Mth.cos(negativeYawRadians);
      float yawSin = Mth.sin(negativeYawRadians);
      float pitchCos = Mth.cos(pitchRadians);
      float pitchSin = Mth.sin(pitchRadians);
      return new Vec3(yawSin * pitchCos, -pitchSin, yawCos * pitchCos);
   }

   private boolean hasMissingCollisionChunks() {
      AABB testBox = this.boundingBox.inflate(1.0);
      int var2 = Mth.floor(testBox.minX);
      int var3 = Mth.ceil(testBox.maxX);
      int var4 = Mth.floor(testBox.minZ);
      int var5 = Mth.ceil(testBox.maxZ);
      return !this.player.level().hasChunksAt(var2, var4, var3, var5);
   }

   public static PlayerMovementSimulator predictForDuration(Player player, Vec3 velocity, long durationMs) {
      PlayerMovementSimulator simulation = fromPlayer(player, PlayerMovementSimulator.SimulatedPlayerInput.guessInput(player), velocity);
      simulation.advanceForDuration(durationMs);
      return simulation;
   }

   public static PlayerMovementSimulator predictTicks(Player player, int ticks) {
      PlayerMovementSimulator simulation = fromPlayer(player, PlayerMovementSimulator.SimulatedPlayerInput.guessInput(player));

      for (int var3 = 0; var3 < ticks; var3++) {
         simulation.tick();
      }

      return simulation;
   }

   private void travel(Vec3 movementInput) {
      if (this.swimming && !this.player.isPassenger()) {
         double var2 = this.getLookDirection().y;
         double var4 = var2 < SWIMMING_DIVE_LOOK_THRESHOLD ? SWIMMING_DIVE_RESPONSE : SWIMMING_VERTICAL_RESPONSE;
         BlockPos var6 = new BlockPos(Mth.floor(this.position.x), Mth.floor(this.position.y + 1.0 - SWIMMING_HEAD_BLOCK_OFFSET), Mth.floor(this.position.z));
         if (var2 <= 0.0 || this.input.simulatedKeys.jump() || !this.player.level().getBlockState(var6).getFluidState().isEmpty()) {
            this.velocity = this.velocity.add(0.0, (var2 - this.velocity.y) * var4, 0.0);
         }
      }

      double initialVerticalVelocity = this.velocity.y;
      double gravity = DEFAULT_GRAVITY;
      boolean falling = this.velocity.y <= 0.0;
      if (this.velocity.y <= 0.0 && this.hasActiveEffect(MobEffects.SLOW_FALLING)) {
         gravity = SLOW_FALLING_GRAVITY;
         this.resetFallDistance();
      }

      if (this.isInWater() && this.player.isAffectedByFluids()) {
         double var25 = this.position.y;
         float var28 = this.isSprinting() ? SPRINTING_WATER_DRAG : WATER_DRAG;
         float var30 = WATER_INPUT_ACCELERATION;
         float var32 = (float)this.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
         if (!this.onGround) {
            var32 *= AIRBORNE_WATER_EFFICIENCY_SCALE;
         }

         if (var32 > 0.0F) {
            var28 += (WATER_EFFICIENCY_DRAG_TARGET - var28) * var32 / WATER_EFFICIENCY_DRAG_DIVISOR;
            var30 += (this.getBaseMovementSpeed() - var30) * var32 / WATER_EFFICIENCY_SPEED_DIVISOR;
         }

         if (this.hasActiveEffect(MobEffects.DOLPHINS_GRACE)) {
            var28 = DOLPHINS_GRACE_DRAG;
         }

         this.accelerateRelative(var30, movementInput);
         this.move(this.velocity);
         Vec3 var33 = this.velocity;
         if (this.horizontalCollision && this.isClimbable()) {
            var33 = new Vec3(var33.x, WATER_CLIMB_VELOCITY, var33.z);
         }

         this.velocity = var33.multiply(var28, WATER_VERTICAL_DRAG, var28);
         Vec3 var34 = this.applyFluidGravity(gravity, falling, this.velocity);
         this.velocity = var34;
         if (this.horizontalCollision && this.isFreeAtOffset(var34.x, var34.y + WATER_COLLISION_FREE_SPACE_HEIGHT - this.position.y + var25, var34.z)) {
            this.velocity = new Vec3(var34.x, WATER_COLLISION_JUMP_VELOCITY, var34.z);
         }
      } else if (this.isInLava() && this.player.isAffectedByFluids()) {
         double var24 = this.position.y;
         this.accelerateRelative(LAVA_INPUT_ACCELERATION, movementInput);
         this.move(this.velocity);
         if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
            this.velocity = this.velocity.multiply(SHALLOW_LAVA_X_DRAG, SHALLOW_LAVA_Y_DRAG, SHALLOW_LAVA_Z_DRAG);
            this.velocity = this.applyFluidGravity(gravity, falling, this.velocity);
         } else {
            this.velocity = this.velocity.scale(DEEP_LAVA_DRAG);
         }

         if (!this.player.isNoGravity()) {
            this.velocity = this.velocity.add(0.0, -gravity / LAVA_GRAVITY_DIVISOR, 0.0);
         }

         if (this.horizontalCollision && this.isFreeAtOffset(this.velocity.x, this.velocity.y + LAVA_COLLISION_FREE_SPACE_HEIGHT - this.position.y + var24, this.velocity.z)) {
            this.velocity = new Vec3(this.velocity.x, LAVA_COLLISION_JUMP_VELOCITY, this.velocity.z);
         }
      } else if (this.fallFlying) {
         Vec3 elytraVelocity = this.velocity;
         if (elytraVelocity.y > ELYTRA_FALL_DISTANCE_RESET_VELOCITY) {
            this.fallDistance = 1.0;
         }

         Vec3 lookDirection = this.getLookDirection();
         float pitchRadians = this.pitch * ELYTRA_PITCH_RADIANS;
         double var12 = Math.sqrt(lookDirection.x * lookDirection.x + lookDirection.z * lookDirection.z);
         double var14 = this.velocity.horizontalDistance();
         double var16 = lookDirection.length();
         float var18 = Mth.cos(pitchRadians);
         var18 = (float)(var18 * (var18 * Math.min(1.0, var16 / ELYTRA_LIFT_VECTOR_SCALE)));
         elytraVelocity = this.velocity.add(0.0, gravity * (ELYTRA_BASE_GRAVITY_SCALE + var18 * ELYTRA_LIFT_GRAVITY_SCALE), 0.0);
         if (elytraVelocity.y < 0.0 && var12 > 0.0) {
            double var7 = elytraVelocity.y * ELYTRA_DIVE_CONVERSION * var18;
            elytraVelocity = elytraVelocity.add(lookDirection.x * var7 / var12, var7, lookDirection.z * var7 / var12);
         }

         if (pitchRadians < 0.0F && var12 > 0.0) {
            double var22 = var14 * -Mth.sin(pitchRadians) * ELYTRA_PULL_UP_SCALE;
            elytraVelocity = elytraVelocity.add(-lookDirection.x * var22 / var12, var22 * ELYTRA_PULL_UP_VERTICAL_GAIN, -lookDirection.z * var22 / var12);
         }

         if (var12 > 0.0) {
            elytraVelocity = elytraVelocity.add((lookDirection.x / var12 * var14 - elytraVelocity.x) * ELYTRA_X_VELOCITY_BLEND, 0.0, (lookDirection.z / var12 * var14 - elytraVelocity.z) * ELYTRA_Z_VELOCITY_BLEND);
         }

         this.velocity = elytraVelocity.multiply(ELYTRA_HORIZONTAL_X_DRAG, ELYTRA_VERTICAL_DRAG, ELYTRA_HORIZONTAL_Z_DRAG);
         this.move(this.velocity);
      } else {
         BlockPos groundBlockPosition = this.getGroundBlockPosition();
         float blockFriction = this.player.level().getBlockState(groundBlockPosition).getBlock().getFriction();
         float horizontalDrag = this.onGround ? blockFriction * GROUND_FRICTION_MULTIPLIER : AIR_FRICTION;
         Vec3 groundMovement = this.moveWithGroundFriction(movementInput, blockFriction);
         double verticalVelocity = groundMovement.y;
         if (this.hasActiveEffect(MobEffects.LEVITATION)) {
            MobEffectInstance levitation = this.getActiveEffect(MobEffects.LEVITATION);
            if (levitation != null) {
               verticalVelocity += (LEVITATION_VELOCITY_PER_LEVEL * (levitation.getAmplifier() + 1) - groundMovement.y) * LEVITATION_VELOCITY_BLEND;
            }
         } else if (this.player.level().isClientSide() && !this.player.level().hasChunkAt(groundBlockPosition)) {
            verticalVelocity = this.position.y > this.player.level().getMinY() ? UNLOADED_CHUNK_FALL_VELOCITY : 0.0;
         } else if (!this.player.isNoGravity()) {
            verticalVelocity -= gravity;
         }

         this.velocity = new Vec3(groundMovement.x * horizontalDrag, verticalVelocity * VERTICAL_AIR_DRAG, groundMovement.z * horizontalDrag);
      }

      if (this.player.getAbilities().flying && !this.player.isPassenger()) {
         this.velocity = new Vec3(this.velocity.x, initialVerticalVelocity * CREATIVE_FLYING_VERTICAL_DRAG, this.velocity.z);
         this.resetFallDistance();
      }
   }

   private void resetFallDistance() {
      this.fallDistance = 0.0;
   }

   public void jump() {
      this.velocity = this.velocity.add(0.0, this.getJumpVelocity() - this.velocity.y, 0.0);
      if (this.isSprinting()) {
         float yawRadians = (float)Math.toRadians(this.yaw);
         this.velocity = this.velocity.add(-Mth.sin(yawRadians) * SPRINT_JUMP_X_IMPULSE, 0.0, Mth.cos(yawRadians) * SPRINT_JUMP_Z_IMPULSE);
      }
   }

   public void tick() {
      this.simulatedTicks++;
      this.edgeClippingDetected = false;
      if (!(this.position.y <= MIN_SIMULATION_Y)) {
         this.input.update();
         this.updateWaterState();
         this.updateEyeFluidState();
         this.updateSwimming();
         if (this.jumpCooldownTicks > 0) {
            this.jumpCooldownTicks--;
         }

         this.jumping = this.input.simulatedKeys.jump();
         double xVelocity = this.velocity.x;
         double yVelocity = this.velocity.y;
         double zVelocity = this.velocity.z;
         if (Math.abs(this.velocity.x) < ZERO_VELOCITY_X_THRESHOLD) {
            xVelocity = 0.0;
         }

         if (Math.abs(this.velocity.y) < ZERO_VELOCITY_Y_THRESHOLD) {
            yVelocity = 0.0;
         }

         if (Math.abs(this.velocity.z) < ZERO_VELOCITY_Z_THRESHOLD) {
            zVelocity = 0.0;
         }

         if (this.onGround) {
            this.fallFlying = false;
         }

         this.velocity = new Vec3(xVelocity, yVelocity, zVelocity);
         if (this.jumping) {
            double fluidHeight = this.isInLava() ? this.getFluidHeight(FluidTags.LAVA) : this.getFluidHeight(FluidTags.WATER);
            boolean touchingWater = this.isInWater() && fluidHeight > 0.0;
            double fluidJumpThreshold = this.getFluidJumpThreshold();
            if (!touchingWater || this.onGround && !(fluidHeight > fluidJumpThreshold)) {
               if (!this.isInLava() || this.onGround && !(fluidHeight > fluidJumpThreshold)) {
                  if ((this.onGround || touchingWater && fluidHeight <= fluidJumpThreshold) && this.jumpCooldownTicks == 0) {
                     this.jump();
                     this.jumpCooldownTicks = 10;
                  }
               } else {
                  this.jumpInFluid(FluidTags.LAVA);
               }
            } else {
               this.jumpInFluid(FluidTags.WATER);
            }
         }

         float strafeInput = this.input.leftAmount * STRAFE_INPUT_DAMPING;
         float forwardInput = this.input.forwardAmount * FORWARD_INPUT_DAMPING;
         float verticalInput = 0.0F;
         if (this.hasActiveEffect(MobEffects.SLOW_FALLING) || this.hasActiveEffect(MobEffects.LEVITATION)) {
            this.resetFallDistance();
         }

         this.previousPosition = this.position;
         this.previousVelocity = this.velocity;
         this.travel(new Vec3(strafeInput, verticalInput, forwardInput));
         this.codecPosition = this.codecPosition != null ? this.codecPosition : this.position;
      }
   }

   public boolean isInLava() {
      return this.fluidHeights.getDouble(FluidTags.LAVA) > 0.0;
   }

   public boolean hasActiveEffect(Holder<MobEffect> effect) {
      MobEffectInstance effectInstance = this.player.getEffect(effect);
      return effectInstance != null && effectInstance.getDuration() >= this.simulatedTicks;
   }

   public PlayerMovementSimulator clone() {
      return new PlayerMovementSimulator(
         this.player,
         this.input,
         this.position,
         this.codecPosition,
         this.velocity,
         this.boundingBox,
         this.yaw,
         this.pitch,
         this.sprinting,
         this.fallDistance,
         this.jumpCooldownTicks,
         this.jumping,
         this.fallFlying,
         this.onGround,
         this.horizontalCollision,
         this.verticalCollision,
         this.inWater,
         this.swimming,
         this.wasEyeInWater,
         new Object2DoubleArrayMap<>(this.fluidHeights),
         new HashSet<>(this.eyeFluidTags)
      );
   }

   private float getBaseMovementSpeed() {
      return BASE_MOVEMENT_SPEED;
   }

   public static PlayerMovementSimulator fromClientPlayer(PlayerMovementSimulator.SimulatedPlayerInput input) {
      LocalPlayer player = MINECRAFT_CLIENT.player;
      return new PlayerMovementSimulator(
         player,
         input,
         player.position(),
         player.getPositionCodec().getBase(),
         player.getDeltaMovement(),
         player.getBoundingBox(),
         player.getYRot(),
         player.getXRot(),
         player.isSprinting(),
         player.fallDistance,
         0,
         input.simulatedKeys.jump(),
         player.isFallFlying(),
         player.onGround(),
         player.horizontalCollision,
         player.verticalCollision,
         player.isInWater(),
         player.isSwimming(),
         player.isUnderWater(),
         copyFluidHeights(player),
         copyEyeFluidTags(player)
      );
   }

   public void advanceForDuration(long durationMs) {
      double tickDuration = durationMs / TICK_DURATION_FOR_ADVANCE_MS;
      int fullTicks = (int)Math.floor(tickDuration);
      double fraction = tickDuration - fullTicks;

      for (int var8 = 0; var8 < fullTicks; var8++) {
         this.tick();
      }

      if (fraction > PARTIAL_TICK_EPSILON) {
         Vec3 startPosition = this.position;
         Vec3 startCodecPosition = this.codecPosition;
         Vec3 startVelocity = this.velocity;
         boolean var11 = this.onGround;
         boolean var12 = this.horizontalCollision;
         boolean var13 = this.verticalCollision;
         double var14 = this.fallDistance;
         int var16 = this.jumpCooldownTicks;
         boolean var17 = this.inWater;
         boolean var18 = this.swimming;
         boolean var19 = this.wasEyeInWater;
         boolean var20 = this.fallFlying;
         Object2DoubleArrayMap savedFluidHeights = new Object2DoubleArrayMap<>(this.fluidHeights);
         HashSet savedEyeFluidTags = new HashSet<>(this.eyeFluidTags);
         this.tick();
         Vec3 endPosition = this.position;
         Vec3 endCodecPosition = this.codecPosition;
         Vec3 endVelocity = this.velocity;
         this.position = lerpVector(startPosition, endPosition, fraction);
         this.codecPosition = lerpVector(startCodecPosition, endCodecPosition, fraction);
         this.velocity = lerpVector(startVelocity, endVelocity, fraction);
         this.boundingBox = this.player.getDimensions(this.player.getPose()).makeBoundingBox(this.position);
         this.onGround = var11;
         this.horizontalCollision = var12;
         this.verticalCollision = var13;
         this.fallDistance = var14;
         this.jumpCooldownTicks = var16;
         this.inWater = var17;
         this.swimming = var18;
         this.wasEyeInWater = var19;
         this.fallFlying = var20;
         this.fluidHeights.clear();
         this.fluidHeights.putAll(savedFluidHeights);
         this.eyeFluidTags.clear();
         this.eyeFluidTags.addAll(savedEyeFluidTags);
         this.partialTickFraction = fraction;
      } else {
         this.partialTickFraction = 0.0;
      }
   }

   private boolean isFree(AABB box) {
      return this.player.level().noCollision(this.player, box) && !this.player.level().containsAnyLiquid(box);
   }

   private Vec3 applyFluidGravity(double gravity, boolean falling, Vec3 velocity) {
      return this.player.isNoGravity() ? velocity : velocity.add(0.0, -gravity / (falling ? FALLING_FLUID_GRAVITY_DIVISOR : RISING_FLUID_GRAVITY_DIVISOR), 0.0);
   }

   public double getAttributeValue(Holder<Attribute> attribute) {
      return this.player.getAttributeValue(attribute);
   }

   private MobEffectInstance getActiveEffect(Holder<MobEffect> effect) {
      MobEffectInstance effectInstance = this.player.getEffect(effect);
      return effectInstance != null && effectInstance.getDuration() >= this.simulatedTicks ? effectInstance : null;
   }

   private Vec3 getLookDirection() {
      return this.lookVector(this.pitch, this.yaw);
   }

   private float getJumpBoost() {
      if (this.hasActiveEffect(MobEffects.JUMP_BOOST)) {
         MobEffectInstance jumpBoost = this.getActiveEffect(MobEffects.JUMP_BOOST);
         return JUMP_BOOST_PER_LEVEL * (jumpBoost.getAmplifier() + 1);
      } else {
         return 0.0F;
      }
   }

   private float getFrictionAdjustedSpeed(float blockFriction) {
      return this.onGround ? this.getBaseMovementSpeed() * (GROUND_FRICTION_CUBE_SPEED_SCALE / (blockFriction * blockFriction * blockFriction)) : this.getAirControlSpeed();
   }

   public PlayerMovementSimulator(
      Player player,
      PlayerMovementSimulator.SimulatedPlayerInput input,
      Vec3 position,
      Vec3 codecPosition,
      Vec3 velocity,
      AABB boundingBox,
      float yaw,
      float pitch,
      boolean sprinting,
      double fallDistance,
      int jumpCooldownTicks,
      boolean jumping,
      boolean fallFlying,
      boolean onGround,
      boolean horizontalCollision,
      boolean verticalCollision,
      boolean inWater,
      boolean swimming,
      boolean wasEyeInWater,
      Object2DoubleMap<TagKey<Fluid>> fluidHeights,
      HashSet<TagKey<Fluid>> eyeFluidTags
   ) {
      this.player = player;
      this.input = input;
      this.position = position;
      this.codecPosition = codecPosition;
      this.velocity = velocity;
      this.boundingBox = boundingBox;
      this.yaw = yaw;
      this.pitch = pitch;
      this.sprinting = sprinting;
      this.fallDistance = fallDistance;
      this.jumpCooldownTicks = jumpCooldownTicks;
      this.jumping = jumping;
      this.fallFlying = fallFlying;
      this.onGround = onGround;
      this.horizontalCollision = horizontalCollision;
      this.verticalCollision = verticalCollision;
      this.inWater = inWater;
      this.swimming = swimming;
      this.wasEyeInWater = wasEyeInWater;
      this.fluidHeights = fluidHeights;
      this.eyeFluidTags = eyeFluidTags;
      this.previousPosition = position;
      this.previousVelocity = velocity;
   }

   public BlockState getBlockState(BlockPos blockPosition) {
      return this.player.level().getBlockState(blockPosition);
   }

   private boolean hasGroundForEdgeClipping() {
      return this.onGround || this.fallDistance < MAX_EDGE_CLIP_FALL_DISTANCE && !this.player.level().noCollision(this.player, this.boundingBox.move(0.0, this.fallDistance - EDGE_CLIP_FALL_TEST_OFFSET, 0.0));
   }

   private BlockPos getGroundBlockPosition() {
      return BlockPos.containing(this.position.x, this.boundingBox.minY - GROUND_SAMPLE_Y_OFFSET, this.position.z);
   }

   private boolean isInWater() {
      return this.inWater;
   }

   public static PlayerMovementSimulator predictForDuration(long durationMs) {
      PlayerMovementSimulator simulation = fromClientPlayer(PlayerMovementSimulator.SimulatedPlayerInput.fromClientPlayer(MINECRAFT_CLIENT.player.getLastSentInput()));
      simulation.advanceForDuration(durationMs);
      return simulation;
   }

   private float getJumpVelocity() {
      return BASE_JUMP_VELOCITY * this.getBlockJumpFactor() + this.getJumpBoost();
   }

   private float getBlockJumpFactor() {
      float currentJumpFactor = 0.0F;
      Block currentBlock = this.getBlockState(this.blockPositionAt(this.position)).getBlock();
      if (currentBlock != null) {
         currentJumpFactor = currentBlock.getJumpFactor();
      }

      float groundJumpFactor = 0.0F;
      Block groundBlock = this.getBlockState(this.getGroundBlockPosition()).getBlock();
      if (groundBlock != null) {
         groundJumpFactor = groundBlock.getJumpFactor();
      }

      return currentJumpFactor == 1.0F ? groundJumpFactor : currentJumpFactor;
   }

   public static PlayerMovementSimulator fromPlayer(Player player, PlayerMovementSimulator.SimulatedPlayerInput input) {
      return new PlayerMovementSimulator(
         player,
         input,
         player.position(),
         player.getPositionCodec().getBase(),
         player.position().subtract(new Vec3(player.xo, player.yo, player.zo)),
         player.getBoundingBox(),
         player.getYRot(),
         player.getXRot(),
         player.isSprinting(),
         player.fallDistance,
         0,
         input.simulatedKeys.jump(),
         player.isFallFlying(),
         player.onGround(),
         player.horizontalCollision,
         player.verticalCollision,
         player.isInWater(),
         player.isSwimming(),
         player.isUnderWater(),
         copyFluidHeights(player),
         copyEyeFluidTags(player)
      );
   }

   private static Object2DoubleMap<TagKey<Fluid>> copyFluidHeights(Player player) {
      Object2DoubleArrayMap fluidHeights = new Object2DoubleArrayMap();
      fluidHeights.put(FluidTags.WATER, player.getFluidHeight(FluidTags.WATER));
      fluidHeights.put(FluidTags.LAVA, player.getFluidHeight(FluidTags.LAVA));
      return fluidHeights;
   }

   public static class SimulatedPlayerInput extends ClientInput {
      public @com.google.gson.annotations.SerializedName("ек")  boolean forceEdgeClipping = false;
      public @com.google.gson.annotations.SerializedName("тв")  float forwardAmount;
      public @com.google.gson.annotations.SerializedName("тъ")  float leftAmount;
      public @com.google.gson.annotations.SerializedName("В")  Input simulatedKeys;
      public static final double Яф = 0.121;
      private static final float BACKWARD_IMPULSE = -1.0F;
      private static final float RIGHT_IMPULSE = -1.0F;
      private static final float SNEAK_STRAFE_SCALE = 0.3F;
      private static final float SNEAK_FORWARD_SCALE = 0.3F;
      private static final float INPUT_GUESS_RADIANS = (float) (Math.PI / 180.0);
      private static final double INPUT_GUESS_VELOCITY_THRESHOLD = 0.001;

      public SimulatedPlayerInput(Input keyPresses) {
         this.simulatedKeys = keyPresses;
      }

      public static PlayerMovementSimulator.SimulatedPlayerInput fromClientPlayer(Input keyPresses) {
         return new PlayerMovementSimulator.SimulatedPlayerInput(keyPresses);
      }

      public void update() {
         if (this.simulatedKeys.forward() != this.simulatedKeys.backward()) {
            this.forwardAmount = this.simulatedKeys.forward() ? 1.0F : BACKWARD_IMPULSE;
         } else {
            this.forwardAmount = 0.0F;
         }

         if (this.simulatedKeys.left() == this.simulatedKeys.right()) {
            this.leftAmount = 0.0F;
         } else {
            this.leftAmount = this.simulatedKeys.left() ? 1.0F : RIGHT_IMPULSE;
         }

         if (this.simulatedKeys.shift()) {
            this.leftAmount = this.leftAmount * SNEAK_STRAFE_SCALE;
            this.forwardAmount = this.forwardAmount * SNEAK_FORWARD_SCALE;
         }
      }

      public static PlayerMovementSimulator.SimulatedPlayerInput guessInput(Player player) {
         Vec3 velocity = player.getDeltaMovement();
         float yawRadians = player.getYRot() * INPUT_GUESS_RADIANS;
         double forwardVelocity = -Mth.sin(yawRadians) * velocity.x + Mth.cos(yawRadians) * velocity.z;
         double leftVelocity = Mth.cos(yawRadians) * velocity.x + Mth.sin(yawRadians) * velocity.z;
         double threshold = INPUT_GUESS_VELOCITY_THRESHOLD;
         return new PlayerMovementSimulator.SimulatedPlayerInput(
            new Input(forwardVelocity > threshold, forwardVelocity < -threshold, leftVelocity > threshold, leftVelocity < -threshold, false, player.isShiftKeyDown(), player.isSprinting())
         );
      }

      @Override
      public String toString() {
         return "SimulatedPlayerInput(forwards={"
            + this.simulatedKeys.forward()
            + "}, backwards={"
            + this.simulatedKeys.backward()
            + "}, left={"
            + this.simulatedKeys.left()
            + "}, right={"
            + this.simulatedKeys.right()
            + "}, jumping={"
            + this.simulatedKeys.jump()
            + "}, sprinting="
            + this.simulatedKeys.sprint()
            + ", slowDown="
            + this.simulatedKeys.shift()
            + ")";
      }
   }
}
