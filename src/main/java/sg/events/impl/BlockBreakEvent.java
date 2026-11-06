package sg.events.impl;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class BlockBreakEvent extends CancellableEvent implements ClientEvent {
   @SerializedName("В")
   private BlockState blockState;
   private @com.google.gson.annotations.SerializedName("Х")  BlockPos position;
   @SerializedName("В")
   private BlockBreakEvent.Phase phase;
   private @com.google.gson.annotations.SerializedName("Я")  Direction face;

   @Generated
   public Direction getFace() {
      return this.face;
   }

   @Generated
   public BlockBreakEvent.Phase getPhase() {
      return this.phase;
   }

   @Generated
   public BlockPos getPosition() {
      return this.position;
   }

   @Generated
   public void setFace(Direction face) {
      this.face = face;
   }

   @Generated
   public void setBlockState(BlockState blockState) {
      this.blockState = blockState;
   }

   @Generated
   public void setPhase(BlockBreakEvent.Phase phase) {
      this.phase = phase;
   }

   @Generated
   public BlockBreakEvent(BlockState blockState, BlockPos position, BlockBreakEvent.Phase phase, Direction face) {
      this.blockState = blockState;
      this.position = position;
      this.phase = phase;
      this.face = face;
   }

   @Generated
   public BlockState getBlockState() {
      return this.blockState;
   }

   @Generated
   public void setPosition(BlockPos position) {
      this.position = position;
   }

   public enum Phase {
      START,
      STOP;
   }
}
