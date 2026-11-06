package dev.rose.client.render.font;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.BufferBuilder;
import dev.rose.Rose;
import dev.rose.client.render.gfx.RoseGfx;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Signed-distance-field text.
 *
 * <p>The TTF is rasterised once with stb_truetype (bundled with LWJGL), each glyph is turned into
 * an 8-bit signed distance field and packed into a single atlas. The fragment shader rebuilds the
 * outline with {@code smoothstep}, so a glyph stays razor sharp at any size — no more blocky
 * bitmap scaling like vanilla's {@code ascii.png}.
 */
public final class RoseFont {

	/** Em size the atlas is rasterised at. Bigger = smoother, more atlas memory. */
	private static final int GLYPH_PX = 44;
	/** Distance-field range in texels on each side of the outline. */
	private static final int PAD = 6;
	private static final int ATLAS_WIDTH = 1024;

	private static RoseFont regular;
	private static RoseFont semibold;
	private static int attempts;

	private final Map<Integer, Glyph> glyphs = new HashMap<>();
	private final AbstractFontTexture texture;
	private final float ascent;
	private final float descent;
	private final float capHeight;
	private final float em;

	private record Glyph(float u0, float v0, float u1, float v1, float offX, float offY, float w, float h,
			float advance) {
	}

	private RoseFont(byte[] ttf, String label) {
		ByteBuffer data = BufferUtils.createByteBuffer(ttf.length);
		data.put(ttf).flip();

		STBTTFontinfo info = STBTTFontinfo.create();

		if (!STBTruetype.stbtt_InitFont(info, data)) {
			throw new IllegalStateException("cannot parse " + label);
		}

		float scale = STBTruetype.stbtt_ScaleForPixelHeight(info, GLYPH_PX);
		IntBuffer ascentB = BufferUtils.createIntBuffer(1);
		IntBuffer descentB = BufferUtils.createIntBuffer(1);
		IntBuffer lineGap = BufferUtils.createIntBuffer(1);
		STBTruetype.stbtt_GetFontVMetrics(info, ascentB, descentB, lineGap);

		this.em = GLYPH_PX;
		this.ascent = ascentB.get(0) * scale;
		this.descent = descentB.get(0) * scale;

		IntBuffer capX0 = BufferUtils.createIntBuffer(1);
		IntBuffer capY0 = BufferUtils.createIntBuffer(1);
		IntBuffer capX1 = BufferUtils.createIntBuffer(1);
		IntBuffer capY1 = BufferUtils.createIntBuffer(1);
		STBTruetype.stbtt_GetCodepointBox(info, 'H', capX0, capY0, capX1, capY1);
		this.capHeight = capY1.get(0) * scale;

		Map<Integer, byte[]> masks = new HashMap<>();
		Map<Integer, int[]> boxes = new HashMap<>();

		for (int cp : charset()) {
			IntBuffer w = BufferUtils.createIntBuffer(1);
			IntBuffer h = BufferUtils.createIntBuffer(1);
			IntBuffer xo = BufferUtils.createIntBuffer(1);
			IntBuffer yo = BufferUtils.createIntBuffer(1);
			ByteBuffer bitmap = STBTruetype.stbtt_GetCodepointBitmap(info, scale, scale, cp, w, h, xo, yo);

			if (bitmap != null) {
				int gw = w.get(0);
				int gh = h.get(0);
				byte[] pixels = new byte[gw * gh];

				for (int i = 0; i < pixels.length; i++) {
					pixels[i] = bitmap.get(i);
				}

				masks.put(cp, pixels);
				boxes.put(cp, new int[] {gw, gh, xo.get(0), yo.get(0)});
				STBTruetype.stbtt_FreeBitmap(bitmap);
			} else {
				boxes.put(cp, new int[] {0, 0, 0, 0});
			}
		}

		// ---- pack the fields into one atlas -----------------------------------------------
		int cursorX = 0;
		int cursorY = 0;
		int rowHeight = 0;
		Map<Integer, int[]> placed = new HashMap<>();

		for (Map.Entry<Integer, int[]> entry : boxes.entrySet()) {
			int[] box = entry.getValue();
			int cellW = box[0] + PAD * 2;
			int cellH = box[1] + PAD * 2;

			if (cellW > ATLAS_WIDTH) {
				cellW = ATLAS_WIDTH;
			}

			if (cursorX + cellW > ATLAS_WIDTH) {
				cursorX = 0;
				cursorY += rowHeight + 1;
				rowHeight = 0;
			}

			placed.put(entry.getKey(), new int[] {cursorX, cursorY, box[0], box[1], box[2], box[3]});
			cursorX += cellW + 1;
			rowHeight = Math.max(rowHeight, cellH);
		}

		int atlasHeight = Math.max(64, cursorY + rowHeight + 2);
		int potHeight = Integer.highestOneBit(atlasHeight - 1) << 1;

		NativeImage image = new NativeImage(ATLAS_WIDTH, potHeight, false);

		for (Map.Entry<Integer, int[]> entry : placed.entrySet()) {
			int cp = entry.getKey();
			int[] pos = entry.getValue();
			int gw = pos[2];
			int gh = pos[3];
			byte[] field = field(masks.get(cp), gw, gh, PAD);
			int cellW = gw + PAD * 2;
			int cellH = gh + PAD * 2;

			for (int y = 0; y < cellH; y++) {
				for (int x = 0; x < cellW; x++) {
					int v = field[y * cellW + x] & 0xFF;
					image.setPixelABGR(pos[0] + x, pos[1] + y, 0xFF000000 | (v << 16) | (v << 8) | v);
				}
			}

			float u0 = (float) pos[0] / ATLAS_WIDTH;
			float v0 = (float) pos[1] / potHeight;
			float u1 = (float) (pos[0] + cellW) / ATLAS_WIDTH;
			float v1 = (float) (pos[1] + cellH) / potHeight;

			IntBuffer advance = BufferUtils.createIntBuffer(1);
			IntBuffer bearing = BufferUtils.createIntBuffer(1);
			STBTruetype.stbtt_GetCodepointHMetrics(info, cp, advance, bearing);

			this.glyphs.put(cp, new Glyph(u0, v0, u1, v1,
				pos[4] - PAD, pos[5] - PAD, cellW, cellH, advance.get(0) * scale));
		}

		this.texture = new AbstractFontTexture(label, image);
		Minecraft.getInstance().getTextureManager().register(Rose.id("font/" + label), this.texture);
	}

	// ------------------------------------------------------------------- public API

	public static void init() {
		if (regular != null || attempts >= 3) {
			return;
		}

		attempts++;

		try {
			regular = new RoseFont(read("inter-regular.ttf"), "inter_regular");
			semibold = new RoseFont(read("inter-semibold.ttf"), "inter_semibold");
			Rose.LOG.info("[rose] SDF fonts ready");
		} catch (Throwable t) {
			Rose.LOG.error("[rose] cannot build SDF fonts, falling back to vanilla text", t);
		}
	}

	private static byte[] read(String file) throws Exception {
		try (InputStream in = RoseFont.class.getResourceAsStream("/assets/rose/fonts/" + file)) {
			if (in == null) {
				throw new IllegalStateException("missing font " + file);
			}

			return in.readAllBytes();
		}
	}

	/** Font used for body text. */
	public static RoseFont regular() {
		init();
		return regular;
	}

	/** Font used for titles and the wordmark. */
	public static RoseFont semibold() {
		init();
		return semibold;
	}

	public static boolean ready() {
		return regular() != null;
	}

	/** Height of one line of text (ascender to descender) at the given em size. */
	public static float lineHeight(float size) {
		RoseFont font = regular();
		return font == null ? size : (font.ascent - font.descent) / font.em * size;
	}

	public void draw(Matrix4f pose, String text, float x, float y, float size, int color, float tracking) {
		if (text == null || text.isEmpty()) {
			return;
		}

		float k = size / this.em;
		float baseline = y + this.capHeight * k;
		float penX = x;
		int previous = -1;

		BufferBuilder buffer = RoseGfx.begin(RoseGfx.TEXT, this.texture);

		for (int i = 0; i < text.length(); ) {
			int cp = text.codePointAt(i);
			i += Character.charCount(cp);

			Glyph glyph = this.glyphs.get(cp);

			if (glyph == null) {
				continue;
			}

			if (previous != -1) {
				penX += tracking;
			}

			if (glyph.w() > 0.0F) {
				float x0 = penX + glyph.offX() * k;
				float y0 = baseline + glyph.offY() * k;
				float x1 = x0 + glyph.w() * k;
				float y1 = y0 + glyph.h() * k;

				buffer.addVertex(pose, x0, y0, 0.0F).setUv(glyph.u0(), glyph.v0()).setColor(color);
				buffer.addVertex(pose, x0, y1, 0.0F).setUv(glyph.u0(), glyph.v1()).setColor(color);
				buffer.addVertex(pose, x1, y1, 0.0F).setUv(glyph.u1(), glyph.v1()).setColor(color);
				buffer.addVertex(pose, x1, y0, 0.0F).setUv(glyph.u1(), glyph.v0()).setColor(color);
			}

			penX += glyph.advance() * k;
			previous = cp;
		}
	}

	public float width(String text, float size, float tracking) {
		if (text == null || text.isEmpty()) {
			return 0.0F;
		}

		float k = size / this.em;
		float width = 0.0F;
		int previous = -1;

		for (int i = 0; i < text.length(); ) {
			int cp = text.codePointAt(i);
			i += Character.charCount(cp);

			Glyph glyph = this.glyphs.get(cp);

			if (glyph == null) {
				continue;
			}

			if (previous != -1) {
				width += tracking;
			}

			width += glyph.advance() * k;
			previous = cp;
		}

		return width;
	}

	// --------------------------------------------------------------- SDF generation

	private static int[] charset() {
		int[] extra = {0x2013, 0x2014, 0x2018, 0x2019, 0x201C, 0x201D, 0x2022, 0x2026, 0x2039, 0x203A, 0x2116,
			0x20BD, 0x2190, 0x2191, 0x2192, 0x2193, 0x2212, 0x221E, 0x2713, 0x2715, 0x00D7, 0x00B7};
		int count = (127 - 32) + (256 - 160) + (0x460 - 0x400) + extra.length;
		int[] all = new int[count];
		int n = 0;

		for (int c = 32; c < 127; c++) {
			all[n++] = c;
		}

		for (int c = 160; c < 256; c++) {
			all[n++] = c;
		}

		for (int c = 0x400; c < 0x460; c++) {
			all[n++] = c;
		}

		for (int c : extra) {
			all[n++] = c;
		}

		return all;
	}

	/**
	 * Builds a padded signed distance field for one glyph.
	 * Inside of the glyph = bright, outside = dark, 0.5 sits exactly on the outline.
	 */
	private static byte[] field(byte[] mask, int gw, int gh, int pad) {
		int w = gw + pad * 2;
		int h = gh + pad * 2;
		boolean[] inside = new boolean[w * h];

		if (mask != null) {
			for (int y = 0; y < gh; y++) {
				for (int x = 0; x < gw; x++) {
					inside[(y + pad) * w + (x + pad)] = (mask[y * gw + x] & 0xFF) > 127;
				}
			}
		}

		// 8SSEDT: vector to the nearest outline texel, measured from the inside and from the outside.
		int[] fromOutside = nearest(inside, w, h, false);
		int[] fromInside = nearest(inside, w, h, true);
		byte[] out = new byte[w * h];

		for (int i = 0; i < out.length; i++) {
			int[] vectors = inside[i] ? fromOutside : fromInside;
			float dx = vectors[i * 2];
			float dy = vectors[i * 2 + 1];
			float distance = (float) Math.sqrt(dx * dx + dy * dy);

			if (!inside[i]) {
				distance = -distance;
			}

			float v = 0.5F + 0.5F * distance / pad;
			out[i] = (byte) Math.max(0, Math.min(255, Math.round(v * 255.0F)));
		}

		return out;
	}

	/** Two-pass (8SSEDT) nearest-seed search; returns the offset to the nearest seed per texel. */
	private static int[] nearest(boolean[] inside, int w, int h, boolean seedsAreInside) {
		final int far = 4096;
		int[] grid = new int[w * h * 2];

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = (y * w + x) * 2;
				boolean seed = seedsAreInside == inside[y * w + x];

				grid[i] = seed ? 0 : far;
				grid[i + 1] = seed ? 0 : far;
			}
		}

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				relax(grid, w, h, x, y, -1, 0);
				relax(grid, w, h, x, y, 0, -1);
				relax(grid, w, h, x, y, -1, -1);
				relax(grid, w, h, x, y, 1, -1);
			}
		}

		for (int y = h - 1; y >= 0; y--) {
			for (int x = w - 1; x >= 0; x--) {
				relax(grid, w, h, x, y, 1, 0);
				relax(grid, w, h, x, y, 0, 1);
				relax(grid, w, h, x, y, 1, 1);
				relax(grid, w, h, x, y, -1, 1);
			}
		}

		return grid;
	}

	private static void relax(int[] grid, int w, int h, int x, int y, int offsetX, int offsetY) {
		int nx = x + offsetX;
		int ny = y + offsetY;

		if (nx < 0 || ny < 0 || nx >= w || ny >= h) {
			return;
		}

		int i = (y * w + x) * 2;
		int j = (ny * w + nx) * 2;
		int cx = grid[j] + offsetX;
		int cy = grid[j + 1] + offsetY;

		if (cx * cx + cy * cy < grid[i] * grid[i] + grid[i + 1] * grid[i + 1]) {
			grid[i] = cx;
			grid[i + 1] = cy;
		}
	}

	/** Thin texture wrapper so the atlas can live in the texture manager. */
	private static final class AbstractFontTexture extends DynamicTexture {
		AbstractFontTexture(String label, NativeImage image) {
			super(() -> "rose " + label, image);
		}
	}
}
