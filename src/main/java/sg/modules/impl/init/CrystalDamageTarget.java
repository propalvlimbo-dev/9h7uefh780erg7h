package sg.modules.impl.init;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record CrystalDamageTarget(Player player, AABB box, Vec3 center) {
   public CrystalDamageTarget(Player var1, AABB var2) {
      this(var1, var2, new Vec3((var2.minX + var2.maxX) * 0.5, var2.minY, (var2.minZ + var2.maxZ) * 0.5));
   }
}
