package sg.events.impl;

import lombok.Generated;
import net.minecraft.world.phys.Vec3;
import sg.events.CancellableEvent;
import sg.events.ClientEvent;

public class PlayerMovementEvent extends CancellableEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("н")  Vec3 movement;

   @Generated
   public Vec3 getMovement() {
      return this.movement;
   }

   @Generated
   public void setMovement(Vec3 movement) {
      this.movement = movement;
   }

   @Generated
   public PlayerMovementEvent(Vec3 movement) {
      this.movement = movement;
   }
}
