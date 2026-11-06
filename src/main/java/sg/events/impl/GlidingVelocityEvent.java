package sg.events.impl;

import lombok.Generated;
import net.minecraft.world.phys.Vec3;
import sg.events.ClientEvent;

public class GlidingVelocityEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("Л")  Vec3 velocity;

   @Generated
   public void setVelocity(Vec3 velocity) {
      this.velocity = velocity;
   }

   @Generated
   public GlidingVelocityEvent(Vec3 velocity) {
      this.velocity = velocity;
   }

   @Generated
   public Vec3 getVelocity() {
      return this.velocity;
   }
}
