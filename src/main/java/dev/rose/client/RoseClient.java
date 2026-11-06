package dev.rose.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.rose.client.config.RoseConfig;
import dev.rose.client.module.ModuleManager;
import dev.rose.client.render.font.RoseFont;
import dev.rose.client.render.gfx.RoseGfx;
import dev.rose.client.render.RoseRenderTypes;
import dev.rose.client.screens.RoseClickGui;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class RoseClient implements ClientModInitializer {
	public static final ModuleManager MODULES = new ModuleManager();
	public static final KeyMapping.Category CATEGORY =
		KeyMapping.Category.register(Identifier.fromNamespaceAndPath("rose", "main"));

	public static KeyMapping openGuiKey;

	@Override
	public void onInitializeClient() {
		RoseConfig.load();
		RoseRenderTypes.init();
		RoseGfx.init();
		RoseFont.init();

		openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.rose.open_gui",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_RIGHT_SHIFT,
			CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(this::onEndTick);
	}

	private void onEndTick(Minecraft mc) {
		while (openGuiKey.consumeClick()) {
			if (mc.screen instanceof RoseClickGui gui) {
				gui.onClose();
			} else if (mc.screen == null) {
				mc.setScreen(new RoseClickGui());
			}
		}
		MODULES.tick(mc);
	}
}
