package sg.events.impl;

import lombok.Generated;
import net.minecraft.core.BlockPos;
import sg.events.CancellableEvent;
import sg.events.ClientEvent;

public class BlockPlacementEvent extends CancellableEvent implements ClientEvent {
    @com.google.gson.annotations.SerializedName("Ч") BlockPos position;

   @Generated
   public BlockPlacementEvent(BlockPos position) {
      this.position = position;
   }

   @Generated
   public BlockPos getPosition() {
      return this.position;
   }
}
