package dev.rose.client.module;

/** A simple float setting rendered as a slider in the ClickGUI. */
public final class Setting {
	public final String id;
	public final String name;
	public final String suffix;
	public final float min;
	public final float max;
	public final float defaultValue;

	private float value;

	public Setting(String id, String name, String suffix, float min, float max, float defaultValue) {
		this.id = id;
		this.name = name;
		this.suffix = suffix;
		this.min = min;
		this.max = max;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float v) {
		this.value = Math.max(min, Math.min(max, v));
	}

	public void reset() {
		this.value = defaultValue;
	}

	/** 0..1 position of the value inside [min, max]. */
	public float getProgress() {
		return max - min <= 0.0F ? 0.0F : (value - min) / (max - min);
	}

	public void setProgress(float p) {
		setValue(min + (max - min) * Math.max(0.0F, Math.min(1.0F, p)));
	}

	public String displayValue() {
		if (value == Math.round(value)) {
			return String.valueOf(Math.round(value)) + suffix;
		}
		return String.format(java.util.Locale.ROOT, "%.1f%s", value, suffix);
	}
}
