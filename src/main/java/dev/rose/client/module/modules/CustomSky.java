package dev.rose.client.module.modules;

import dev.rose.client.module.Module;
import dev.rose.client.module.Setting;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;

/** Ruby clouds instead of the vanilla ones. The rendering itself lives in RubyCloudRenderer. */
public class CustomSky extends Module {
	public final Setting density = add(new Setting("density", "Density", "", 0.15F, 0.9F, 0.62F));
	public final Setting altitude = add(new Setting("altitude", "Altitude", "m", 110.0F, 300.0F, 188.0F));
	public final Setting glow = add(new Setting("glow", "Brightness", "", 0.4F, 1.8F, 1.05F));
	public final Setting layers = add(new Setting("layers", "Layers", "", 1.0F, 3.0F, 2.0F));

	private CloudStatus previousStatus;

	public CustomSky() {
		super("custom_sky", "Custom Sky", "Ruby clouds instead of vanilla ones", "Visuals");
	}

	@Override
	protected void onEnable() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.options == null) {
			return;
		}

		previousStatus = mc.options.cloudStatus().get();
		mc.options.cloudStatus().set(CloudStatus.OFF);
	}

	@Override
	protected void onDisable() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.options == null) {
			return;
		}

		mc.options.cloudStatus().set(previousStatus == null ? CloudStatus.FANCY : previousStatus);
	}

	@Override
	public void tick(Minecraft mc) {
		// keep vanilla clouds hidden while the module is on (unless the player is in another world)
		if (isEnabled() && mc.options != null && mc.level != null && mc.options.cloudStatus().get() != CloudStatus.OFF) {
			mc.options.cloudStatus().set(CloudStatus.OFF);
		}
	}
}
