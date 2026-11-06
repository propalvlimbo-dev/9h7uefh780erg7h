package sg.modules.impl.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record CrystalPlacementCandidate(BlockPos pos, Vec3 crystalPos, boolean needsObsidian, @Nullable BlockPos digPos, float enemyDamage, float selfDamage) {
   public CrystalPlacementCandidate(BlockPos var1, boolean var2, @Nullable BlockPos var3) {
      this(var1, new Vec3(var1.getX() + 0.5, var1.getY() + 1.0, var1.getZ() + 0.5), var2, var3, 0.0F, 0.0F);
   }
}
