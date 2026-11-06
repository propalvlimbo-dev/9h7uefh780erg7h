package dev.rose.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;

/**
 * Rose UI drawing primitives on top of {@link GuiGraphics}.
 *
 * Everything is built from per-vertex coloured quads submitted to the GUI render state,
 * which gives us real gradients, per-vertex alpha and soft glows without any textures.
 * (Textured glass/SDF shaders will replace this in the next milestone.)
 */
public final class RoseDraw {
	private RoseDraw() {
	}

	// ------------------------------------------------------------------ quads

	public static void quad(GuiGraphics g, float x1, float y1, float x2, float y2, float x3, float y3, float x4,
			float y4, int c1, int c2, int c3, int c4) {
		float minX = Math.min(Math.min(x1, x2), Math.min(x3, x4));
		float minY = Math.min(Math.min(y1, y2), Math.min(y3, y4));
		float maxX = Math.max(Math.max(x1, x2), Math.max(x3, x4));
		float maxY = Math.max(Math.max(y1, y2), Math.max(y3, y4));

		if (maxX - minX < 0.02F && maxY - minY < 0.02F) {
			return;
		}

		if ((c1 >>> 24) == 0 && (c2 >>> 24) == 0 && (c3 >>> 24) == 0 && (c4 >>> 24) == 0) {
			return;
		}

		Matrix3x2f pose = new Matrix3x2f(g.pose());
		ScreenRectangle scissor = g.scissorStack.peek();

		ScreenRectangle bounds = new ScreenRectangle(
			(int) minX,
			(int) minY,
			Math.max(1, (int) Math.ceil(maxX - minX)),
			Math.max(1, (int) Math.ceil(maxY - minY))
		).transformMaxBounds(pose);

		if (scissor != null) {
			bounds = scissor.intersection(bounds);
			if (bounds == null) {
				return;
			}
		}

		g.guiRenderState.submitGuiElement(new RoseQuad(
			RenderPipelines.GUI,
			TextureSetup.noTexture(),
			pose,
			new float[] {x1, y1, x2, y2, x3, y3, x4, y4},
			new int[] {c1, c2, c3, c4},
			scissor,
			bounds
		));
	}

	public static void rect(GuiGraphics g, float x, float y, float w, float h, int color) {
		quad(g, x, y, x + w, y, x + w, y + h, x, y + h, color, color, color, color);
	}

	public static void gradientV(GuiGraphics g, float x, float y, float w, float h, int top, int bottom) {
		quad(g, x, y, x + w, y, x + w, y + h, x, y + h, top, top, bottom, bottom);
	}

	public static void gradientH(GuiGraphics g, float x, float y, float w, float h, int left, int right) {
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
		final int steps = 10;
		float step = (a1 - a0) / steps;

		for (int i = 0; i < steps; i++) {
			float s0 = a0 + step * i;
			float s1 = s0 + step;
			float x0 = cx + (float) Math.cos(s0) * r;
			float y0 = cy + (float) Math.sin(s0) * r;
			float x1 = cx + (float) Math.cos(s1) * r;
			float y1 = cy + (float) Math.sin(s1) * r;

			quad(g, cx, cy, x0, y0, x1, y1, x1, y1, color, color, color, color);
		}
	}

	private static void arcStroke(GuiGraphics g, float cx, float cy, float r, float a0, float a1, float thickness,
			int color) {
		final int steps = 10;
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

		final int steps = 28;
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
		panel(g, x, y, w, h, r, 214);
	}

	public static void panel(GuiGraphics g, float x, float y, float w, float h, float r, int baseAlpha) {
		shadow(g, x, y, w, h, r, 14.0F, 0x7A000000, 9);

		int top = RoseTheme.withAlpha(0xFF170C12, baseAlpha);
		int bottom = RoseTheme.withAlpha(0xFF070408, baseAlpha);
		rrect(g, x, y, w, h, r, top, bottom);

		// glass sheen at the top
		gradientV(g, x + 1.5F, y + 1.5F, w - 3.0F, h * 0.45F,
			RoseTheme.withAlpha(0xFFFFFFFF, 26), RoseTheme.withAlpha(0xFFFFFFFF, 0));

		// ruby light bleeding from the bottom edge
		gradientV(g, x + r * 0.6F, y + h - 14.0F, w - r * 1.2F, 12.0F,
			RoseTheme.withAlpha(RoseTheme.RUBY, 0), RoseTheme.withAlpha(RoseTheme.RUBY, 30));

		// hairline border
		rrectOutline(g, x + 0.5F, y + 0.5F, w - 1.0F, h - 1.0F, Math.max(1.0F, r - 0.5F), 1.0F,
			RoseTheme.withAlpha(0xFFFFFFFF, 34));
	}

	// ------------------------------------------------------------------ text

	public static void text(GuiGraphics g, String s, float x, float y, int color, boolean shadow) {
		g.drawString(Minecraft.getInstance().font, s, (int) x, (int) y, color, shadow);
	}

	public static void textScaled(GuiGraphics g, String s, float x, float y, float scale, int color,
			boolean shadow) {
		g.pose().pushMatrix();
		g.pose().translate(x, y);
		g.pose().scale(scale);
		g.drawString(Minecraft.getInstance().font, s, 0, 0, color, shadow);
		g.pose().popMatrix();
	}

	/** Scaled text with manual letter spacing (used for the ROSE wordmark). */
	public static void textSpaced(GuiGraphics g, String s, float x, float y, float scale, float spacing, int color,
			boolean shadow) {
		Font font = Minecraft.getInstance().font;
		float cursor = 0.0F;

		for (int i = 0; i < s.length(); i++) {
			String ch = String.valueOf(s.charAt(i));

			g.pose().pushMatrix();
			g.pose().translate(x + cursor * scale, y);
			g.pose().scale(scale);
			g.drawString(font, ch, 0, 0, color, shadow);
			g.pose().popMatrix();

			cursor += font.width(ch) + spacing;
		}
	}

	public static float textSpacedWidth(String s, float scale, float spacing) {
		Font font = Minecraft.getInstance().font;
		float cursor = 0.0F;

		for (int i = 0; i < s.length(); i++) {
			cursor += font.width(String.valueOf(s.charAt(i))) + spacing;
		}

		return cursor * scale;
	}

	public static int textWidth(String s) {
		return Minecraft.getInstance().font.width(s);
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
		g.blit(RenderPipelines.GUI_TEXTURED, texture, (int) x, (int) y, 0, 0, size, size, size, size, tint);
	}
}
