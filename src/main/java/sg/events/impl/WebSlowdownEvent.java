package sg.events.impl;

import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import sg.events.CancellableEvent;
import sg.events.ClientEvent;

public class WebSlowdownEvent extends CancellableEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("П")  Vec3 slowdownMultiplier;
   private final @com.google.gson.annotations.SerializedName("弟")  BlockPos position;

   @Generated
   public Vec3 getSlowdownMultiplier() {
      return this.slowdownMultiplier;
   }

   @Generated
   public WebSlowdownEvent(Vec3 slowdownMultiplier, BlockPos position) {
      this.slowdownMultiplier = slowdownMultiplier;
      this.position = position;
   }

   @Generated
   public BlockPos getPosition() {
      return this.position;
   }
}
