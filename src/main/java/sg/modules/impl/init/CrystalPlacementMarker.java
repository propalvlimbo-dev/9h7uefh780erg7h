package sg.modules.impl.init;

import net.minecraft.core.BlockPos;

public record CrystalPlacementMarker(BlockPos pos, boolean crystal, long createdAt) {
}
