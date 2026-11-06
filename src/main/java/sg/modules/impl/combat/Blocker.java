package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.mixin.MultiPlayerGameModeAccessor;
import sg.render.pipeline.InventoryActions;
import sg.util.input.MouseSensitivityMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.BlockTargetRaycaster;
import sg.rotation.RotationStrategy;

public class Blocker extends ClientModule {
   private static final int UNUSED_LEGACY_ROTATION_PRIORITY = 3;
   private static final int UNUSED_LEGACY_ROTATION_DURATION_TICKS = 40;
   private static final float UNUSED_LEGACY_ROTATION_STEP = 180.0F;
   private static final double UNUSED_LEGACY_REACH = 6.0;
   private final @com.google.gson.annotations.SerializedName("Яж")  EventListener<PlayerTickEvent> playerTickListener = this::method_0Ы;
   @SerializedName("В")
   private final Blocker.PlacementRotation placementRotation = new Blocker.PlacementRotation();
   @SerializedName("В")
   private final BlockTargetRaycaster blockRaycaster = new BlockTargetRaycaster();
   private @com.google.gson.annotations.SerializedName("Л")  BlockPos anchorPosition;
   private @com.google.gson.annotations.SerializedName("诶")  BlockPos blockBeingCleared;
   @SerializedName("4Ы")
   private int previousSelectedSlot = -1;
   private static final String MODULE_NAME = "Blocker";
   private static final String MODULE_DESCRIPTION = "Застраивает ноги обсидианом со всех сторон чтоб не бахали ";
   private static final double MAX_BREAK_DISTANCE_SQUARED = 36.0;
   private static final double BREAK_RAYCAST_REACH = 6.0;
   private static final double SUPPORT_CENTER_X_OFFSET = 0.5;
   private static final double FACE_X_OFFSET = 0.5;
   private static final double SUPPORT_CENTER_Y_OFFSET = 0.5;
   private static final double FACE_Y_OFFSET = 0.5;
   private static final double SUPPORT_CENTER_Z_OFFSET = 0.5;
   private static final double FACE_Z_OFFSET = 0.5;
   private static final double MAX_PLACEMENT_DISTANCE_SQUARED = 36.0;
   private static final float YAW_QUARTER_TURN_DEGREES = 90.0F;
   private static final float MIN_PITCH_DEGREES = -90.0F;
   private static final float MAX_PITCH_DEGREES = 90.0F;

   private boolean а(BlockPos var1, int var2) {
      BlockPos var3 = this.Ъ(var1);
      if (var3 == null) {
         return false;
      }

      Direction var4 = this.а(var1, var3);
      Vec3 var5 = new Vec3(var3.getX() + SUPPORT_CENTER_X_OFFSET + var4.getStepX() * FACE_X_OFFSET, var3.getY() + SUPPORT_CENTER_Y_OFFSET + var4.getStepY() * FACE_Y_OFFSET, var3.getZ() + SUPPORT_CENTER_Z_OFFSET + var4.getStepZ() * FACE_Z_OFFSET);
      if (MINECRAFT_CLIENT.player.getEyePosition().distanceToSqr(var5) > MAX_PLACEMENT_DISTANCE_SQUARED) {
         return false;
      }

      this.А(var5);
      this.Рч();
      if (MINECRAFT_CLIENT.player.getInventory().selected != var2) {
         MINECRAFT_CLIENT.player.getInventory().selected = var2;
         MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
      }

      InteractionResult var6 = MINECRAFT_CLIENT.gameMode.useItemOn(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, new BlockHitResult(var5, var4, var3, false));
      if (var6.consumesAction()) {
         MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
      }

      this.РШ();
      return var6.consumesAction();
   }

   private Direction а(BlockPos var1, BlockPos var2) {
      if (var2.equals(var1.below())) {
         return Direction.UP;
      } else if (var2.equals(var1.above())) {
         return Direction.DOWN;
      } else if (var2.equals(var1.north())) {
         return Direction.SOUTH;
      } else if (var2.equals(var1.south())) {
         return Direction.NORTH;
      } else if (var2.equals(var1.west())) {
         return Direction.EAST;
      } else {
         return var2.equals(var1.east()) ? Direction.WEST : Direction.UP;
      }
   }

   private void РШ() {
      if (this.previousSelectedSlot != -1 && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
         if (MINECRAFT_CLIENT.player.getInventory().selected != this.previousSelectedSlot) {
            MINECRAFT_CLIENT.player.getInventory().selected = this.previousSelectedSlot;
            MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
         }

         this.previousSelectedSlot = -1;
      }
   }

   private Vector2f ч(Vec3 var1) {
      Vec3 var2 = var1.subtract(MINECRAFT_CLIENT.player.getEyePosition());
      double var3 = Math.hypot(var2.x, var2.z);
      float var5 = (float)Math.toDegrees(Math.atan2(var2.z, var2.x)) - YAW_QUARTER_TURN_DEGREES;
      float var6 = (float)(-Math.toDegrees(Math.atan2(var2.y, var3)));
      return new Vector2f(Mth.wrapDegrees(var5), Mth.clamp(var6, MIN_PITCH_DEGREES, MAX_PITCH_DEGREES));
   }

   public Blocker() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
   }

   public boolean Бб() {
      return this.blockBeingCleared != null;
   }

   @Override
   public void onDisable() {
      if (MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.gameMode.isDestroying()) {
         MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
      }

      this.РШ();
      RoseClient.getInstance().getServices().getRotations().removeStrategy(this.placementRotation);
      this.blockRaycaster.дН();
      this.anchorPosition = null;
      this.blockBeingCleared = null;
      super.onDisable();
   }

   private void method_0Ы(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && this.anchorPosition != null) {
         BlockPos[] var2 = this.а_3ba7e3d7();
         if (!this.а(var2)) {
            int var3 = this.method_0_();
            if (var3 != -1) {
               if (this.blockBeingCleared != null) {
                  BlockState var4 = MINECRAFT_CLIENT.level.getBlockState(this.blockBeingCleared);
                  if (!var4.is(Blocks.OBSIDIAN) && !var4.canBeReplaced()) {
                     if (this.Ъ(var4, this.blockBeingCleared)) {
                        this.а(this.blockBeingCleared, var4);
                        return;
                     }

                     this.blockBeingCleared = null;
                  } else {
                     BlockPos var5 = this.blockBeingCleared;
                     if (MINECRAFT_CLIENT.gameMode.isDestroying()) {
                        MINECRAFT_CLIENT.gameMode.stopDestroyBlock();
                     }

                     this.blockBeingCleared = null;
                     this.РШ();
                     if (var4.canBeReplaced() && this.а(var5, var3)) {
                        return;
                     }
                  }
               }

               for (BlockPos var7 : var2) {
                  BlockState var8 = MINECRAFT_CLIENT.level.getBlockState(var7);
                  if (!var8.is(Blocks.OBSIDIAN)) {
                     if (!var8.canBeReplaced()) {
                        if (this.Ъ(var8, var7)) {
                           this.blockBeingCleared = var7.immutable();
                           this.а(this.blockBeingCleared, var8);
                           return;
                        }
                     } else if (this.а(var7, var3)) {
                        return;
                     }
                  }
               }

               if (this.а(var2)) {
               }
            }
         }
      }
   }

   private void А(Vec3 var1) {
      this.placementRotation.setTargetPosition(var1);
      this.placementRotation.ъ(this.ч(var1));
      RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.placementRotation, 3, 40);
   }

   private BlockPos Ъ(BlockPos var1) {
      BlockPos[] var2 = new BlockPos[]{var1.below(), var1.north(), var1.south(), var1.west(), var1.east(), var1.above()};

      for (BlockPos var6 : var2) {
         BlockState var7 = MINECRAFT_CLIENT.level.getBlockState(var6);
         if (!var7.isAir() && var7.isRedstoneConductor(MINECRAFT_CLIENT.level, var6)) {
            return var6;
         }
      }

      return null;
   }

   private void а(BlockPos var1, BlockState var2) {
      Vec3 var3 = Vec3.atCenterOf(var1);
      if (!(MINECRAFT_CLIENT.player.getEyePosition().distanceToSqr(var3) > MAX_BREAK_DISTANCE_SQUARED)) {
         this.А(var3);
         int var4 = InventoryActions.б(var2);
         if (var4 >= 0 && var4 <= 8) {
            this.Рч();
            if (MINECRAFT_CLIENT.player.getInventory().selected != var4) {
               MINECRAFT_CLIENT.player.getInventory().selected = var4;
               MINECRAFT_CLIENT.gameMode.ensureHasSentCarriedItem();
            }
         }

         this.blockRaycaster.Б(var1);

         BlockHitResult var5;
         try {
            var5 = this.blockRaycaster.l(var1, BREAK_RAYCAST_REACH);
         } finally {
            this.blockRaycaster.дН();
         }

         if (var5 != null) {
            ((MultiPlayerGameModeAccessor)MINECRAFT_CLIENT.gameMode).setBlockBreakingCooldown(0);
            boolean var6 = MINECRAFT_CLIENT.gameMode.continueDestroyBlock(var1, var5.getDirection());
            if (var6) {
               MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
            }

            if (MINECRAFT_CLIENT.level.getBlockState(var1).canBeReplaced()) {
               this.blockBeingCleared = null;
               this.РШ();
               int var7 = this.method_0_();
               if (var7 != -1) {
                  this.а(var1, var7);
               }
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.anchorPosition = MINECRAFT_CLIENT.player == null ? null : MINECRAFT_CLIENT.player.blockPosition().immutable();
      this.blockBeingCleared = null;
      this.previousSelectedSlot = -1;
      super.onEnable();
   }

   private void Рч() {
      if (this.previousSelectedSlot == -1 && MINECRAFT_CLIENT.player != null) {
         this.previousSelectedSlot = MINECRAFT_CLIENT.player.getInventory().selected;
      }
   }

   private boolean Ъ(BlockState var1, BlockPos var2) {
      return !var1.isAir() && var1.getFluidState().isEmpty() && var1.getDestroySpeed(MINECRAFT_CLIENT.level, var2) >= 0.0F && !var1.is(Blocks.BEDROCK);
   }

   private int method_0_() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var1).is(Items.OBSIDIAN)) {
            return var1;
         }
      }

      return -1;
   }

   private boolean а(BlockPos[] var1) {
      for (BlockPos var5 : var1) {
         BlockState var6 = MINECRAFT_CLIENT.level.getBlockState(var5);
         if (!var6.is(Blocks.OBSIDIAN) && !var6.is(Blocks.BEDROCK)) {
            return false;
         }
      }

      return true;
   }

   private BlockPos[] а_3ba7e3d7() {
      return new BlockPos[]{this.anchorPosition.below(), this.anchorPosition.north(), this.anchorPosition.south(), this.anchorPosition.west(), this.anchorPosition.east()};
   }

   private static final class PlacementRotation extends RotationStrategy {
      private final @com.google.gson.annotations.SerializedName("ь")  Vector2f targetRotation = new Vector2f();
      private static final float MAX_YAW_STEP_DEGREES = 180.0F;
      private static final float MAX_PITCH_STEP_DEGREES = 180.0F;
      private static final float MIN_PITCH_DEGREES = -90.0F;
      private static final float MAX_PITCH_DEGREES = 90.0F;

      @Override
      public Vector2f getTargetRotation() {
         return new Vector2f(this.targetRotation);
      }

      private PlacementRotation() {
         super(null);
         this.setUsesVanillaCameraRotation(false);
      }

      @Override
      public Vector2f calculateRotation(Vector2f var1, Vector2f var2) {
         float var3 = Mth.wrapDegrees(var2.x - var1.x);
         float var4 = var2.y - var1.y;
         float var5 = Math.abs(var3) + Math.abs(var4);
         float var6 = var5 == 0.0F ? 0.0F : Math.abs(var3 / var5) * MAX_YAW_STEP_DEGREES;
         float var7 = var5 == 0.0F ? 0.0F : Math.abs(var4 / var5) * MAX_PITCH_STEP_DEGREES;
         float var8 = var1.x + this.method_8Ч(Mth.clamp(var3, -var6, var6));
         float var9 = Mth.clamp(var1.y + this.method_8Ч(Mth.clamp(var4, -var7, var7)), MIN_PITCH_DEGREES, MAX_PITCH_DEGREES);
         return new Vector2f(var8, var9);
      }

      private void ъ(Vector2f var1) {
         this.targetRotation.set(var1);
      }

      private float method_8Ч(float var1) {
         float var2 = MouseSensitivityMath.getRotationStep();
         return var2 <= 0.0F ? var1 : MouseSensitivityMath.rotationDeltaToMouseCounts(var1) * var2;
      }
   }
}
