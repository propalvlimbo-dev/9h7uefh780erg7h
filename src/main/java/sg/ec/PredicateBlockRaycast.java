package sg.ec;

import java.util.function.Predicate;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class PredicateBlockRaycast {
   public static BlockHitResult I(LocalPlayer var0, double var1, Predicate<Block> var3) {
      Vec3 var4 = var0.getEyePosition(1.0F);
      Vec3 var5 = var0.getViewVector(1.0F).normalize();
      Vec3 var6 = var4.add(var5.scale(var1));
      Level var7 = var0.level();
      return BlockGetter.traverseBlocks(
         var4,
         var6,
         var7,
         (var4x, var5x) -> {
            BlockState var6x = var4x.getBlockState(var5x);
            Block var7x = var6x.getBlock();
            if (!var3.test(var7x)) {
               return null;
            }

            VoxelShape var8 = var6x.getShape(var4x, var5x, CollisionContext.of(var0));
            if (var8.isEmpty()) {
               return null;
            }

            BlockHitResult var9 = var4x.clipWithInteractionOverride(var4, var6, var5x, var8, var6x);
            if (var9 == null) {
               return null;
            }

            System.out
               .println(
                  "[OpenWalls Raycast] block="
                     + var9.getBlockPos()
                     + ", side="
                     + var9.getDirection()
                     + ", hitPos="
                     + var9.getLocation()
                     + ", inside="
                     + var9.isInside()
               );
            return var9;
         },
         var2 -> BlockHitResult.miss(var6, Direction.getApproximateNearest(-var5.x, -var5.y, -var5.z), BlockPos.containing(var6))
      );
   }

   private PredicateBlockRaycast() {
   }
}
