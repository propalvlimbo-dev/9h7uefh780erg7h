package sg.modules.impl.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public record BlockPlacementHit(BlockPos neighbor, Direction side, Vec3 hitVec, boolean inside) {
   public BlockPlacementHit(BlockPos var1, Direction var2, Vec3 var3) {
      this(var1, var2, var3, false);
   }
}
