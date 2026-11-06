package sg.events.impl;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Generated;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import sg.events.ClientEvent;

public class WorldEffectsEvent implements ClientEvent {
   @SerializedName("В")
   private final DeltaTracker deltaTracker;
   private final @com.google.gson.annotations.SerializedName("е")  PoseStack poses;
   @SerializedName("В")
   private final WorldGeometryBuffer geometryBuffer;
   private final @com.google.gson.annotations.SerializedName("з")  Vec3 cameraPosition;
   private final @com.google.gson.annotations.SerializedName("аС")  float tickDelta;
   private final @com.google.gson.annotations.SerializedName("р")  Matrix4f projectionMatrix;
   @SerializedName("6")
   private final Matrix4f viewMatrix;

   @Generated
   public float getTickDelta() {
      return this.tickDelta;
   }

   @Generated
   public DeltaTracker getDeltaTracker() {
      return this.deltaTracker;
   }

   @Generated
   public PoseStack getPoses() {
      return this.poses;
   }

   @Generated
   public Matrix4f getViewMatrix() {
      return this.viewMatrix;
   }

   @Generated
   public WorldEffectsEvent(DeltaTracker deltaTracker, PoseStack poses, WorldGeometryBuffer geometryBuffer, Vec3 cameraPosition, float tickDelta, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
      this.deltaTracker = deltaTracker;
      this.poses = poses;
      this.geometryBuffer = geometryBuffer;
      this.cameraPosition = cameraPosition;
      this.tickDelta = tickDelta;
      this.projectionMatrix = projectionMatrix;
      this.viewMatrix = viewMatrix;
   }

   @Generated
   public WorldGeometryBuffer getGeometryBuffer() {
      return this.geometryBuffer;
   }

   @Generated
   public Matrix4f getProjectionMatrix() {
      return this.projectionMatrix;
   }

   @Generated
   public Vec3 getCameraPosition() {
      return this.cameraPosition;
   }
}
