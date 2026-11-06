package sg.events.impl;

import lombok.Generated;
import net.minecraft.world.phys.Vec3;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class CameraPositionEvent extends CancellableEvent implements ClientEvent {
   public @com.google.gson.annotations.SerializedName("Б")  Vec3 position;
   public final @com.google.gson.annotations.SerializedName("тз")  float tickDelta;

   @Generated
   public CameraPositionEvent(Vec3 position, float tickDelta) {
      this.position = position;
      this.tickDelta = tickDelta;
   }

   @Generated
   public float getTickDelta() {
      return this.tickDelta;
   }

   @Generated
   public Vec3 getPosition() {
      return this.position;
   }

   @Generated
   public void setPosition(Vec3 position) {
      this.position = position;
   }
}
