package dev.rose.render;

import dev.rose.util.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/**
 * The Rose mark.
 *
 * <p>The logo is a static texture ({@code assets/rose/textures/gui/logo_192.png}) so the brand stays
 * pixel-perfect at any size. {@code level} only gently drives the brightness, which keeps the
 * splash / splash-in animations of the menu alive without repainting the mark every frame.
 */
public final class RoseLogo {
	/** Height / width of the mark. */
	public static final float ASPECT = 1.0F;
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("rose", "textures/gui/logo_192.png");
	private static final int SOURCE_SIZE = 192;

	private final String name;

	public RoseLogo(String name) {
		this.name = name;
	}

	public void draw(GuiGraphics g, float x, float y, float width, float level, float alpha) {
		if (alpha <= 0.003F || width <= 1.0F) {
			return;
		}

		int size = Math.max(4, Math.round(width));
		float brightness = 0.82F + 0.18F * Math.clamp(level, 0.0F, 1.0F);
		int tint = ColorUtil.fade(-1, Math.clamp(alpha * brightness, 0.0F, 1.0F));

		g.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, Math.round(x), Math.round(y), 0.0F, 0.0F,
			size, size, SOURCE_SIZE, SOURCE_SIZE, tint);
	}

	public void close() {
	}

	public String name() {
		return this.name;
	}
}
