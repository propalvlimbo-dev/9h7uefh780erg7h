package dev.rose.client.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.rose.client.render.RubyCloudRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Hook for our custom sky / cloud layer. */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Inject(
		method = "renderLevel(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V",
		at = @At("RETURN")
	)
	private void rose$renderCustomSky(GraphicsResourceAllocator allocator, DeltaTracker deltaTracker,
			boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f projectionMatrix,
			Matrix4f frustumMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean flag, CallbackInfo ci) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			return;
		}

		try {
			PoseStack pose = new PoseStack();
			pose.mulPose(positionMatrix);

			Vec3 cam = camera.getPosition();
			pose.translate(-cam.x, -cam.y, -cam.z);

			RubyCloudRenderer.render(pose, mc.renderBuffers().bufferSource(), cam);
		} catch (Throwable ignored) {
			// never break the game because of a visual effect
		}
	}
}
