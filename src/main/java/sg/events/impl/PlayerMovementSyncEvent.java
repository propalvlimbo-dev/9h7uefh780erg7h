package sg.events.impl;

import lombok.Generated;
import net.minecraft.world.phys.Vec3;
import sg.events.ClientEvent;
import sg.events.CancellableEvent;

public class PlayerMovementSyncEvent extends CancellableEvent implements ClientEvent {
   private @com.google.gson.annotations.SerializedName("х")  Vec3 position;
   private @com.google.gson.annotations.SerializedName("Д5")  float yaw;
   private @com.google.gson.annotations.SerializedName("Дв")  float pitch;
   private @com.google.gson.annotations.SerializedName("ХФ")  boolean onGround;

   @Generated
   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }

   @Generated
   public boolean isOnGround() {
      return this.onGround;
   }

   @Generated
   public PlayerMovementSyncEvent(Vec3 position, float yaw, float pitch, boolean onGround) {
      this.position = position;
      this.yaw = yaw;
      this.pitch = pitch;
      this.onGround = onGround;
   }

   @Generated
   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   @Generated
   public float getYaw() {
      return this.yaw;
   }

   @Generated
   public float getPitch() {
      return this.pitch;
   }

   @Generated
   public void setPosition(Vec3 position) {
      this.position = position;
   }

   @Generated
   public Vec3 getPosition() {
      return this.position;
   }

   @Generated
   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public static class Post extends PlayerMovementSyncEvent {
      public Post(Vec3 position, float yaw, float pitch, boolean onGround) {
         super(position, yaw, pitch, onGround);
      }
   }
}
