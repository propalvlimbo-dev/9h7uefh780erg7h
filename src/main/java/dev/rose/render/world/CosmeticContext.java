package dev.rose.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


public record CosmeticContext(
   PoseStack pose,
   MultiBufferSource.BufferSource buffers,
   Camera camera,
   Vec3 cameraPos,
   LocalPlayer player,
   float partialTick,
   float time,
   float delta,
   boolean firstPerson,
   Vector3f right,
   Vector3f up
) {
   public Vector3f local(double x, double y, double z) {
      return new Vector3f((float)(x - this.cameraPos.x), (float)(y - this.cameraPos.y), (float)(z - this.cameraPos.z));
   }

   public Vector3f local(Vec3 world) {
      return this.local(world.x, world.y, world.z);
   }

   public static float nearFade(Vector3f cameraRelative, float near, float far) {
      return Math.clamp((cameraRelative.length() - near) / (far - near), 0.0F, 1.0F);
   }
}
