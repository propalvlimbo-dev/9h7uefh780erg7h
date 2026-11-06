package dev.rose;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Common constants and small helpers for the Rose client. */
public final class Rose {
	public static final String MOD_ID = "rose";
	public static final String VERSION = "0.1.0";
	public static final Logger LOG = LoggerFactory.getLogger("Rose");

	private Rose() {
	}

	public static Identifier id(String path) {
		return Identifier.parse(MOD_ID + ":" + path);
	}
}
