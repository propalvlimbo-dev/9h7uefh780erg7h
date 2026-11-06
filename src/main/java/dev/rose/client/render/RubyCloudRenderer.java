package dev.rose.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.client.RoseClient;
import dev.rose.client.module.modules.CustomSky;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

/**
 * Builds the custom sky: a grid of world-anchored quads.
 * The actual cloud shape, lighting and drift are computed in rose:core/ruby_clouds (fragment shader),
 * the CPU only decides where the quads are and which tint/coverage they carry.
 */
public final class RubyCloudRenderer {
	private static final float CELL = 20.0F;
	private static final float MAX_DIST = 260.0F;
	private static final float LAYER_GAP = 13.0F;

	private RubyCloudRenderer() {
	}

	public static void render(PoseStack pose, MultiBufferSource.BufferSource buffers, Vec3 camera) {
		CustomSky sky = RoseClient.MODULES.customSky;
		if (!sky.isEnabled()) {
			return;
		}

		VertexConsumer vc = buffers.getBuffer(RoseRenderTypes.RUBY_CLOUDS);
		PoseStack.Pose entry = pose.last();

		double camX = camera.x;
		double camZ = camera.z;

		long originCellX = (long) Math.floor(camX / CELL);
		long originCellZ = (long) Math.floor(camZ / CELL);

		int radius = (int) Math.ceil(MAX_DIST / CELL);
		float altitude = sky.altitude.getValue();
		float coverage = sky.density.getValue();
		float glow = sky.glow.getValue();
		int layers = Math.max(1, Math.round(sky.layers.getValue()));

		float maxDistSq = MAX_DIST * MAX_DIST;

		for (int layer = 0; layer < layers; layer++) {
			float y = altitude + layer * LAYER_GAP;
			float layerCoverage = Math.max(0.12F, coverage - layer * 0.06F);
			int color = tint(layer, glow, layerCoverage);

			for (int i = -radius; i <= radius; i++) {
				for (int j = -radius; j <= radius; j++) {
					double wx = (originCellX + i) * (double) CELL;
					double wz = (originCellZ + j) * (double) CELL;

					double dx = wx + CELL * 0.5 - camX;
					double dz = wz + CELL * 0.5 - camZ;

					if (dx * dx + dz * dz > maxDistSq) {
						continue;
					}

					vc.addVertex(entry, (float) wx, y, (float) wz).setColor(color);
					vc.addVertex(entry, (float) wx, y, (float) (wz + CELL)).setColor(color);
					vc.addVertex(entry, (float) (wx + CELL), y, (float) (wz + CELL)).setColor(color);
					vc.addVertex(entry, (float) (wx + CELL), y, (float) wz).setColor(color);
				}
			}
		}

		buffers.endBatch(RoseRenderTypes.RUBY_CLOUDS);
	}

	/**
	 * Packs the material parameters into the vertex colour:
	 * RGB = tint (brightness from the "glow" setting), A = coverage threshold used by the shader.
	 */
	private static int tint(int layer, float glow, float coverage) {
		float dim = 1.0F - layer * 0.10F;

		int r = clamp(0xE4 * glow * dim);
		int g = clamp(0x3A * glow * dim);
		int b = clamp(0x5C * glow * dim);
		int a = clamp(coverage * 255.0F);

		return RoseTheme.argb(a, r, g, b);
	}

	private static int clamp(float v) {
		return (int) Math.max(0.0F, Math.min(255.0F, v));
	}
}
