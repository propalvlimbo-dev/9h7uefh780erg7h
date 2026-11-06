package dev.rose.client.ui;

/** Tiny animation toolkit: easings, damped approach and a spring-ish follower. */
public final class Anim {
	private Anim() {
	}

	public static float now() {
		return (float) (System.nanoTime() / 1_000_000_000.0);
	}

	public static float clamp01(float v) {
		return v < 0.0F ? 0.0F : (v > 1.0F ? 1.0F : v);
	}

	public static float lerp(float a, float b, float t) {
		return a + (b - a) * clamp01(t);
	}

	public static float easeOutCubic(float t) {
		t = clamp01(t);
		float f = 1.0F - t;
		return 1.0F - f * f * f;
	}

	public static float easeOutQuint(float t) {
		t = clamp01(t);
		float f = 1.0F - t;
		return 1.0F - f * f * f * f * f;
	}

	public static float easeInOutCubic(float t) {
		t = clamp01(t);
		return t < 0.5F ? 4.0F * t * t * t : 1.0F - (float) Math.pow(-2.0F * t + 2.0F, 3.0F) / 2.0F;
	}

	public static float easeOutBack(float t) {
		t = clamp01(t);
		float c1 = 1.70158F;
		float c3 = c1 + 1.0F;
		float f = t - 1.0F;
		return 1.0F + c3 * f * f * f + c1 * f * f;
	}

	/** Frame-rate independent exponential approach. */
	public static float approach(float current, float target, float speed, float dt) {
		float k = 1.0F - (float) Math.exp(-speed * Math.max(0.0F, dt));
		return current + (target - current) * k;
	}

	/** 0..1 progress of a timer that started at {@code start} seconds and lasts {@code duration}. */
	public static float progress(float start, float duration, float now) {
		if (duration <= 0.0F) {
			return 1.0F;
		}
		return clamp01((now - start) / duration);
	}
}
