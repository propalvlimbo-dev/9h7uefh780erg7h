package dev.rose.client.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.joml.Matrix3x2f;

/**
 * A single coloured quad submitted to the GUI render state.
 * Every Rose UI primitive (rect, gradient, rounded corner, glow ring) is built from these,
 * which lets us do real per-vertex gradients and per-vertex alpha on top of vanilla's GUI pass.
 */
public record RoseQuad(
		RenderPipeline pipeline,
		TextureSetup textureSetup,
		Matrix3x2f pose,
		float[] xy,
		int[] colors,
		ScreenRectangle scissorArea,
		ScreenRectangle bounds
) implements GuiElementRenderState {

	@Override
	public void buildVertices(VertexConsumer vertices) {
		for (int i = 0; i < 4; i++) {
			vertices.addVertexWith2DPose(pose, xy[i * 2], xy[i * 2 + 1]).setColor(colors[i]);
		}
	}
}
