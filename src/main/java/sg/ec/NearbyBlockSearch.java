package sg.ec;

import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class NearbyBlockSearch implements MinecraftAccess {
   private static final String чO = "This is a utility class and cannot be instantiated";

   public static BlockState А(Block var0, int var1) {
      BlockPos var2 = MINECRAFT_CLIENT.player.getBlockPosBelowThatAffectsMyMovement().offset(-var1, -var1, -var1);
      BlockPos var3 = MINECRAFT_CLIENT.player.getBlockPosBelowThatAffectsMyMovement().offset(var1, var1, var1);

      for (int var4 = var2.getX(); var4 < var3.getX(); var4++) {
         for (int var5 = var2.getY(); var5 < var3.getY(); var5++) {
            for (int var6 = var2.getZ(); var6 < var3.getZ(); var6++) {
               BlockPos var7 = new BlockPos(var4, var5, var6);
               BlockState var8 = MINECRAFT_CLIENT.level.getBlockState(var7);
               if (var8.getBlock().equals(var0)) {
                  return var8;
               }
            }
         }
      }

      return null;
   }

   @Generated
   private NearbyBlockSearch() {
      throw new UnsupportedOperationException(чO);
   }
}
