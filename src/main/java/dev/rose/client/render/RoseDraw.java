package dev.rose.client.render;

import dev.rose.client.render.font.RoseFont;
import dev.rose.client.render.gfx.RoseGfx;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

/**
 * Rose UI drawing primitives.
 *
 * <p>Everything is recorded into {@link RoseGfx} and drawn at the end of the GUI frame with our own
 * shaders, which is what makes the glass, the SDF text and the exact layer order possible.
 * All coordinates are in GUI units, exactly like {@code GuiGraphics}.
 */
public final class RoseDraw {
	/** Base em size of body text. */
	public static final float TEXT_SIZE = 10.5F;

	private RoseDraw() {
	}

	private static Matrix4f pose(GuiGraphics g) {
		return RoseGfx.pose(g.pose());
	}

	// ------------------------------------------------------------------ quads

	public static void quad(GuiGraphics g, float x1, float y1, float x2, float y2, float x3, float y3, float x4,
			float y4, int c1, int c2, int c3, int c4) {
		boolean invisible = (c1 >>> 24) == 0 && (c2 >>> 24) == 0 && (c3 >>> 24) == 0 && (c4 >>> 24) == 0;

		if (invisible) {
			return;
		}

		RoseGfx.shape(pose(g),
			new float[] {x1, y1, x2, y2, x3, y3, x4, y4},
			new int[] {c1, c2, c3, c4});
	}

	public static void rect(GuiGraphics g, float x, float y, float w, float h, int color) {
		if (w <= 0.0F || h <= 0.0F) {
			return;
		}

		quad(g, x, y, x + w, y, x + w, y + h, x, y + h, color, color, color, color);
	}

	public static void gradientV(GuiGraphics g, float x, float y, float w, float h, int top, int bottom) {
		if (w <= 0.0F || h <= 0.0F) {
			return;
		}

		quad(g, x, y, x + w, y, x + w, y + h, x, y + h, top, top, bottom, bottom);
	}

	public static void gradientH(GuiGraphics g, float x, float y, float w, float h, int left, int right) {
		if (w <= 0.0F || h <= 0.0F) {
			return;
		}

		quad(g, x, y, x + w, y, x + w, y + h, x, y + h, left, right, right, left);
	}

	// ---------------------------------------------------------- rounded shapes

	public static void rrect(GuiGraphics g, float x, float y, float w, float h, float r, int top, int bottom) {
		if (w <= 0.0F || h <= 0.0F) {
			return;
		}

		r = Math.max(0.0F, Math.min(r, Math.min(w, h) * 0.5F));

		if (r < 0.7F) {
			gradientV(g, x, y, w, h, top, bottom);
			return;
		}

		int topColor = RoseTheme.mix(top, bottom, (r * 0.5F) / h);
		int bottomColor = RoseTheme.mix(top, bottom, 1.0F - (r * 0.5F) / h);
		float innerH = h - 2.0F * r;

		gradientV(g, x + r, y, w - 2.0F * r, h, top, bottom);

		if (innerH > 0.1F) {
			gradientV(g, x, y + r, r, innerH, RoseTheme.mix(top, bottom, r / h),
				RoseTheme.mix(top, bottom, (h - r) / h));
			gradientV(g, x + w - r, y + r, r, innerH, RoseTheme.mix(top, bottom, r / h),
				RoseTheme.mix(top, bottom, (h - r) / h));
		}

		cornerFan(g, x + r, y + r, r, (float) Math.PI, (float) (Math.PI * 1.5), topColor);
		cornerFan(g, x + w - r, y + r, r, (float) (Math.PI * 1.5), (float) (Math.PI * 2.0), topColor);
		cornerFan(g, x + r, y + h - r, r, (float) (Math.PI * 0.5), (float) Math.PI, bottomColor);
		cornerFan(g, x + w - r, y + h - r, r, 0.0F, (float) (Math.PI * 0.5), bottomColor);
	}

	public static void rrectSolid(GuiGraphics g, float x, float y, float w, float h, float r, int color) {
		rrect(g, x, y, w, h, r, color, color);
	}

	public static void rrectOutline(GuiGraphics g, float x, float y, float w, float h, float r, float thickness,
			int color) {
		if (w <= 0.0F || h <= 0.0F) {
			return;
		}

		r = Math.max(0.0F, Math.min(r, Math.min(w, h) * 0.5F));

		rect(g, x + r, y, w - 2.0F * r, thickness, color);
		rect(g, x + r, y + h - thickness, w - 2.0F * r, thickness, color);
		rect(g, x, y + r, thickness, h - 2.0F * r, color);
		rect(g, x + w - thickness, y + r, thickness, h - 2.0F * r, color);

		arcStroke(g, x + r, y + r, r, (float) Math.PI, (float) (Math.PI * 1.5), thickness, color);
		arcStroke(g, x + w - r, y + r, r, (float) (Math.PI * 1.5), (float) (Math.PI * 2.0), thickness, color);
		arcStroke(g, x + r, y + h - r, r, (float) (Math.PI * 0.5), (float) Math.PI, thickness, color);
		arcStroke(g, x + w - r, y + h - r, r, 0.0F, (float) (Math.PI * 0.5), thickness, color);
	}

	private static void cornerFan(GuiGraphics g, float cx, float cy, float r, float a0, float a1, int color) {
		final int steps = 12;
		float step = (a1 - a0) / steps;

		for (int i = 0; i < steps; i++) {
			float s0 = a0 + step * i;
			float s1 = s0 + step;
			float x0 = cx + (float) Math.cos(s0) * r;
			float y0 = cy + (float) Math.sin(s0) * r;
			float x1 = cx + (float) Math.cos(s1) * r;
			float y1 = cy + (float) Math.sin(s1) * r;

			// the fan closes on the centre; the last vertex repeats to keep a clean quad
			quad(g, cx, cy, x0, y0, x1, y1, x1, y1, color, color, color, color);
		}
	}

	private static void arcStroke(GuiGraphics g, float cx, float cy, float r, float a0, float a1, float thickness,
			int color) {
		final int steps = 12;
		float step = (a1 - a0) / steps;
		float outer = r + thickness * 0.5F;
		float inner = Math.max(0.0F, r - thickness * 0.5F);

		for (int i = 0; i < steps; i++) {
			float s0 = a0 + step * i;
			float s1 = s0 + step;

			float ox0 = cx + (float) Math.cos(s0) * outer;
			float oy0 = cy + (float) Math.sin(s0) * outer;
			float ox1 = cx + (float) Math.cos(s1) * outer;
			float oy1 = cy + (float) Math.sin(s1) * outer;
			float ix0 = cx + (float) Math.cos(s0) * inner;
			float iy0 = cy + (float) Math.sin(s0) * inner;
			float ix1 = cx + (float) Math.cos(s1) * inner;
			float iy1 = cy + (float) Math.sin(s1) * inner;

			quad(g, ox0, oy0, ox1, oy1, ix1, iy1, ix0, iy0, color, color, color, color);
		}
	}

	public static void circle(GuiGraphics g, float cx, float cy, float r, int color) {
		if (r <= 0.1F) {
			return;
		}

		final int steps = 32;

		for (int i = 0; i < steps; i++) {
			float a0 = (float) (Math.PI * 2.0 * i / steps);
			float a1 = (float) (Math.PI * 2.0 * (i + 1) / steps);
			float x0 = cx + (float) Math.cos(a0) * r;
			float y0 = cy + (float) Math.sin(a0) * r;
			float x1 = cx + (float) Math.cos(a1) * r;
			float y1 = cy + (float) Math.sin(a1) * r;

			quad(g, cx, cy, x0, y0, x1, y1, x1, y1, color, color, color, color);
		}
	}

	// ----------------------------------------------------------------- glows

	/** Soft radial glow, faked by stacking fading circles (cheap and smooth on dark backgrounds). */
	public static void glow(GuiGraphics g, float cx, float cy, float radius, int color, int steps) {
		if (radius <= 0.5F || RoseTheme.alpha(color) == 0) {
			return;
		}

		int baseAlpha = RoseTheme.alpha(color);

		for (int i = steps; i >= 1; i--) {
			float t = i / (float) steps;
			float falloff = (1.0F - t) * (1.0F - t);
			int a = (int) (baseAlpha * falloff * 0.55F);

			if (a <= 0) {
				continue;
			}

			circle(g, cx, cy, radius * t, RoseTheme.withAlpha(color, a));
		}
	}

	/** Soft drop shadow under a rounded rectangle. */
	public static void shadow(GuiGraphics g, float x, float y, float w, float h, float r, float spread, int color,
			int steps) {
		int baseAlpha = RoseTheme.alpha(color);

		for (int i = steps; i >= 1; i--) {
			float t = i / (float) steps;
			float grow = spread * t;
			int a = (int) (baseAlpha * (1.0F - t) * (1.0F - t) * 1.4F);

			if (a <= 0) {
				continue;
			}

			int c = RoseTheme.withAlpha(color, a);
			rrect(g, x - grow, y - grow + spread * 0.25F, w + grow * 2.0F, h + grow * 2.0F, r + grow, c, c);
		}
	}

	// ---------------------------------------------------------------- panels

	/** The signature Rose glass panel. */
	public static void panel(GuiGraphics g, float x, float y, float w, float h, float r) {
		panel(g, x, y, w, h, r, 232);
	}

	public static void panel(GuiGraphics g, float x, float y, float w, float h, float r, int baseAlpha) {
		shadow(g, x, y, w, h, r, 16.0F, 0x99000000, 10);

		// glass body: a cold dark ruby that stays readable over any world behind it
		int top = RoseTheme.withAlpha(0xFF241019, baseAlpha);
		int bottom = RoseTheme.withAlpha(0xFF0A0509, Math.min(255, baseAlpha + 12));
		rrect(g, x, y, w, h, r, top, bottom);

		// wide sheen across the upper half, like light hitting the pane
		gradientV(g, x + 1.5F, y + 1.5F, w - 3.0F, h * 0.5F,
			RoseTheme.withAlpha(0xFFFFFFFF, 30), RoseTheme.withAlpha(0xFFFFFFFF, 0));

		// ruby light bleeding from the bottom edge
		gradientV(g, x + r * 0.6F, y + h - 16.0F, w - r * 1.2F, 14.0F,
			RoseTheme.withAlpha(RoseTheme.RUBY, 0), RoseTheme.withAlpha(RoseTheme.RUBY, 44));

		// hairline border + a brighter top edge
		rrectOutline(g, x + 0.5F, y + 0.5F, w - 1.0F, h - 1.0F, Math.max(1.0F, r - 0.5F), 1.0F,
			RoseTheme.withAlpha(0xFFFFFFFF, 46));
		gradientH(g, x + r, y + 0.5F, w - r * 2.0F, 1.0F,
			RoseTheme.withAlpha(0xFFFFFFFF, 0), RoseTheme.withAlpha(0xFFFFFFFF, 90));
		gradientH(g, x + r, y + 0.5F, (w - r * 2.0F) * 0.5F, 1.0F,
			RoseTheme.withAlpha(0xFFFFFFFF, 90), RoseTheme.withAlpha(0xFFFFFFFF, 0));
	}

	// ------------------------------------------------------------------ text

	public static void text(GuiGraphics g, String s, float x, float y, int color, boolean shadow) {
		RoseFont font = RoseFont.regular();

		if (font == null) {
			g.drawString(Minecraft.getInstance().font, s, (int) x, (int) y, color, shadow);
			return;
		}

		font.draw(pose(g), s, x, y, TEXT_SIZE, color, 0.0F);
	}

	public static void textScaled(GuiGraphics g, String s, float x, float y, float scale, int color,
			boolean shadow) {
		RoseFont font = RoseFont.regular();

		if (font == null) {
			g.drawString(Minecraft.getInstance().font, s, (int) x, (int) y, color, shadow);
			return;
		}

		font.draw(pose(g), s, x, y, TEXT_SIZE * scale, color, 0.0F);
	}

	/** Scaled text with manual letter spacing (used for the ROSE wordmark and section labels). */
	public static void textSpaced(GuiGraphics g, String s, float x, float y, float scale, float spacing, int color,
			boolean shadow) {
		RoseFont font = RoseFont.semibold();

		if (font == null) {
			g.drawString(Minecraft.getInstance().font, s, (int) x, (int) y, color, shadow);
			return;
		}

		font.draw(pose(g), s, x, y, TEXT_SIZE * scale, color, spacing * scale);
	}

	public static float textSpacedWidth(String s, float scale, float spacing) {
		RoseFont font = RoseFont.semibold();

		if (font == null) {
			return Minecraft.getInstance().font.width(s) * scale;
		}

		return font.width(s, TEXT_SIZE, spacing) * scale;
	}

	public static int textWidth(String s) {
		RoseFont font = RoseFont.regular();

		if (font == null) {
			return Minecraft.getInstance().font.width(s);
		}

		return (int) Math.ceil(font.width(s, TEXT_SIZE, 0.0F));
	}

	public static float textHeight() {
		return RoseFont.lineHeight(TEXT_SIZE);
	}

	public static void textCentered(GuiGraphics g, String s, float cx, float y, int color, boolean shadow) {
		text(g, s, cx - textWidth(s) / 2.0F, y, color, shadow);
	}

	// ------------------------------------------------------------------ lines

	public static void line(GuiGraphics g, float x1, float y1, float x2, float y2, float thickness, int color) {
		float dx = x2 - x1;
		float dy = y2 - y1;
		float len = (float) Math.sqrt(dx * dx + dy * dy);

		if (len < 0.001F) {
			return;
		}

		float nx = -dy / len * thickness * 0.5F;
		float ny = dx / len * thickness * 0.5F;

		quad(g, x1 + nx, y1 + ny, x2 + nx, y2 + ny, x2 - nx, y2 - ny, x1 - nx, y1 - ny, color, color, color, color);
	}

	// ---------------------------------------------------------------- images

	public static void image(GuiGraphics g, Identifier texture, float x, float y, int size) {
		image(g, texture, x, y, size, 0xFFFFFFFF);
	}

	public static void image(GuiGraphics g, Identifier texture, float x, float y, int size, int tint) {
		AbstractTexture abstractTexture = Minecraft.getInstance().getTextureManager().getTexture(texture);

		if (abstractTexture == null) {
			return;
		}

		RoseGfx.textured(RoseGfx.IMAGE, pose(g), abstractTexture,
			new float[] {x, y, x, y + size, x + size, y + size, x + size, y},
			new float[] {0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F},
			new int[] {tint, tint, tint, tint});
	}
}
