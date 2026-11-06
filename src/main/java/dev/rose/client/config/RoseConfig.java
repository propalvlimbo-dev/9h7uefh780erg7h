package dev.rose.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.rose.Rose;
import dev.rose.client.RoseClient;
import dev.rose.client.module.Module;
import dev.rose.client.module.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** JSON config: module states and slider values. */
public final class RoseConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private RoseConfig() {
	}

	private static Path path() {
		return FabricLoader.getInstance().getConfigDir().resolve("rose.json");
	}

	public static void save() {
		try {
			JsonObject root = new JsonObject();

			for (Module module : RoseClient.MODULES.modules()) {
				JsonObject entry = new JsonObject();
				entry.addProperty("enabled", module.isEnabled());

				JsonObject settings = new JsonObject();
				for (Setting setting : module.settings()) {
					settings.addProperty(setting.id, setting.getValue());
				}

				entry.add("settings", settings);
				root.add(module.id, entry);
			}

			Path file = path();
			Files.createDirectories(file.getParent());
			Files.writeString(file, GSON.toJson(root));
		} catch (IOException | RuntimeException e) {
			Rose.LOG.warn("Could not save Rose config", e);
		}
	}

	public static void load() {
		try {
			Path file = path();
			if (!Files.exists(file)) {
				return;
			}

			JsonElement parsed = JsonParser.parseString(Files.readString(file));
			if (!parsed.isJsonObject()) {
				return;
			}

			JsonObject root = parsed.getAsJsonObject();

			for (Module module : RoseClient.MODULES.modules()) {
				JsonElement moduleElement = root.get(module.id);
				if (moduleElement == null || !moduleElement.isJsonObject()) {
					continue;
				}

				JsonObject entry = moduleElement.getAsJsonObject();

				if (entry.has("settings") && entry.get("settings").isJsonObject()) {
					JsonObject settings = entry.getAsJsonObject("settings");

					for (Setting setting : module.settings()) {
						if (settings.has(setting.id)) {
							try {
								setting.setValue(settings.get(setting.id).getAsFloat());
							} catch (RuntimeException ignored) {
							}
						}
					}
				}

				if (entry.has("enabled") && entry.get("enabled").getAsBoolean()) {
					module.setEnabled(true);
				}
			}
		} catch (IOException | RuntimeException e) {
			Rose.LOG.warn("Could not load Rose config", e);
		}
	}
}
