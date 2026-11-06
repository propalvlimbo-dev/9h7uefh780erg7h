package dev.rose.client.render;

/** Design tokens of the Rose UI (ruby glass minimalism). */
public final class RoseTheme {
	// brand
	public static final int RUBY = 0xFFE4294B;
	public static final int RUBY_BRIGHT = 0xFFFF6B85;
	public static final int RUBY_DEEP = 0xFFC1123A;
	public static final int RUBY_SHADOW = 0xFF5A0A1E;

	// surfaces
	public static final int SURFACE_0 = 0xFF0B0B0E;
	public static final int SURFACE_1 = 0xFF121216;
	public static final int SURFACE_2 = 0xFF17171C;

	// ink
	public static final int INK_100 = 0xFFF6F7FA;
	public static final int INK_80 = 0xFFCED2DC;
	public static final int INK_60 = 0xFF9AA0AE;
	public static final int INK_40 = 0xFF6C7280;

	// geometry
	public static final float RADIUS_SM = 6.0F;
	public static final float RADIUS_MD = 10.0F;
	public static final float RADIUS_LG = 16.0F;
	public static final float RADIUS_XL = 24.0F;

	private RoseTheme() {
	}

	public static int argb(int a, int r, int g, int b) {
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
	}

	public static int alpha(int color) {
		return (color >>> 24) & 0xFF;
	}

	public static int withAlpha(int color, int a) {
		return (color & 0x00FFFFFF) | ((a & 0xFF) << 24);
	}

	public static int scaleAlpha(int color, float factor) {
		return withAlpha(color, (int) Math.max(0.0F, Math.min(255.0F, alpha(color) * factor)));
	}

	public static int mix(int c1, int c2, float t) {
		t = Math.max(0.0F, Math.min(1.0F, t));
		int a = (int) (alpha(c1) + (alpha(c2) - alpha(c1)) * t);
		int r = (int) (((c1 >> 16) & 0xFF) + (((c2 >> 16) & 0xFF) - ((c1 >> 16) & 0xFF)) * t);
		int g = (int) (((c1 >> 8) & 0xFF) + (((c2 >> 8) & 0xFF) - ((c1 >> 8) & 0xFF)) * t);
		int b = (int) ((c1 & 0xFF) + ((c2 & 0xFF) - (c1 & 0xFF)) * t);
		return argb(a, r, g, b);
	}
}
