package dev.rose.client.module;

import dev.rose.client.config.RoseConfig;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
	public final String id;
	public final String name;
	public final String description;
	public final String category;

	private final List<Setting> settings = new ArrayList<>();
	private boolean enabled;

	protected Module(String id, String name, String description, String category) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
	}

	protected final Setting add(Setting setting) {
		settings.add(setting);
		return setting;
	}

	public List<Setting> settings() {
		return settings;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean value) {
		if (enabled == value) {
			return;
		}

		enabled = value;

		try {
			if (value) {
				onEnable();
			} else {
				onDisable();
			}
		} catch (Throwable ignored) {
			// visual modules must never crash the game
		}

		RoseConfig.save();
	}

	public void toggle() {
		setEnabled(!enabled);
	}

	protected void onEnable() {
	}

	protected void onDisable() {
	}

	public void tick(Minecraft mc) {
	}
}
