package sg.modules.impl.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

public record ExpiringLootLocation(Item item, Vec3 position, String world, int anarchy, long expiresAt) {
}
