package dev.rose.client.module;

import dev.rose.client.module.modules.CustomSky;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
	public final CustomSky customSky = new CustomSky();

	private final List<Module> modules = new ArrayList<>();

	public ModuleManager() {
		modules.add(customSky);
	}

	public List<Module> modules() {
		return modules;
	}

	public List<Module> byCategory(String category) {
		List<Module> out = new ArrayList<>();

		for (Module m : modules) {
			if (m.category.equals(category)) {
				out.add(m);
			}
		}

		return out;
	}

	public List<String> categories() {
		List<String> out = new ArrayList<>();

		for (Module m : modules) {
			if (!out.contains(m.category)) {
				out.add(m.category);
			}
		}

		if (out.isEmpty()) {
			out.add("Visuals");
		}

		return out;
	}

	public void tick(Minecraft mc) {
		for (Module m : modules) {
			m.tick(mc);
		}
	}
}
