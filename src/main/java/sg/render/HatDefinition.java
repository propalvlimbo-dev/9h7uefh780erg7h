package sg.render;

import net.minecraft.resources.Identifier;

public record HatDefinition(String key, String displayName, Identifier texture, CosmeticModel model) {
}
