package sg.ec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import sg.modules.impl.init.BlockPlacementHit;

public final class BlockFaceRaycast {
   private static final double[][] Я = new double[][]{
      {BlockFaceRaycast.сЮб, BlockFaceRaycast.сЮЫ},
      {BlockFaceRaycast.сЮl, BlockFaceRaycast.сЮ8},
      {BlockFaceRaycast.сЮ5, BlockFaceRaycast.сЮв},
      {BlockFaceRaycast.сЮъ, BlockFaceRaycast.сЮМ},
      {BlockFaceRaycast.сЮН, BlockFaceRaycast.сЮн}
   };
   private static final double сЮЮ = 0.1;
   private static final double сЮЧ = 0.1;
   private static final double сЮЛ = 0.1;
   private static final double сЮ诶 = 1.0E-7;
   private static final double сЮ1 = 1.0E-7;
   private static final double сЮЬ = 1.0E-7;
   private static final double сЮ衣 = 1.0E-7;
   private static final double сЮш = 1.0E-7;
   private static final double сЮИ = 1.0E-7;
   private static final double сЮь = 1.0E-10;
   private static final double сЮж = 0.001;
   private static final double сЮ弟 = 1.0E-6;
   private static final double сЮб = 0.5;
   private static final double сЮЫ = 0.5;
   private static final double сЮl = 0.25;
   private static final double сЮ8 = 0.5;
   private static final double сЮ5 = 0.75;
   private static final double сЮв = 0.5;
   private static final double сЮъ = 0.5;
   private static final double сЮМ = 0.25;
   private static final double сЮН = 0.5;
   private static final double сЮн = 0.75;

   private BlockFaceRaycast() {
   }

   public static BlockPlacementHit Ъ(VoxelShape var0, BlockPos var1, Direction var2, Vec3 var3, double var4) {
      if (var0.isEmpty()) {
         return null;
      }

      AABB var6 = var0.bounds().move(var1);
      if (var6.contains(var3)) {
         Vec3 var7 = var3.add(-var2.getStepX() * сЮЮ, -var2.getStepY() * сЮЧ, -var2.getStepZ() * сЮЛ);
         BlockHitResult var8 = var0.clip(var3, var7, var1);
         if (var8 != null && var8.isInside() && var8.getDirection() == var2 && var3.distanceToSqr(var8.getLocation()) <= var4 * var4) {
            return new BlockPlacementHit(var1.immutable(), var2, var8.getLocation(), true);
         }
      }
      boolean var18 = switch (var2) {
         case UP -> var3.y >= var6.maxY - сЮ诶;
         case DOWN -> var3.y <= var6.minY + сЮ1;
         case EAST -> var3.x >= var6.maxX - сЮЬ;
         case WEST -> var3.x <= var6.minX + сЮ衣;
         case SOUTH -> var3.z >= var6.maxZ - сЮш;
         case NORTH -> var3.z <= var6.minZ + сЮИ;
      };
      if (!var6.contains(var3) && !var18) {
         return null;
      }

      for (AABB var9 : var0.toAabbs()) {
         AABB var10 = var9.move(var1);

         for (double[] var14 : Я) {
            Vec3 var15 = switch (var2) {
               case UP -> new Vec3(Mth.lerp(var14[0], var10.minX, var10.maxX), var10.maxY, Mth.lerp(var14[1], var10.minZ, var10.maxZ));
               case DOWN -> new Vec3(Mth.lerp(var14[0], var10.minX, var10.maxX), var10.minY, Mth.lerp(var14[1], var10.minZ, var10.maxZ));
               case EAST -> new Vec3(var10.maxX, Mth.lerp(var14[1], var10.minY, var10.maxY), Mth.lerp(var14[0], var10.minZ, var10.maxZ));
               case WEST -> new Vec3(var10.minX, Mth.lerp(var14[1], var10.minY, var10.maxY), Mth.lerp(var14[0], var10.minZ, var10.maxZ));
               case SOUTH -> new Vec3(Mth.lerp(var14[0], var10.minX, var10.maxX), Mth.lerp(var14[1], var10.minY, var10.maxY), var10.maxZ);
               case NORTH -> new Vec3(Mth.lerp(var14[0], var10.minX, var10.maxX), Mth.lerp(var14[1], var10.minY, var10.maxY), var10.minZ);
            };
            Vec3 var16 = var15.subtract(var3);
            if (!(var16.lengthSqr() < сЮь) && !(var16.lengthSqr() > var4 * var4)) {
               BlockHitResult var17 = var0.clip(var3, var15.add(var16.normalize().scale(сЮж)), var1);
               if (var17 != null
                  && var17.getDirection() == var2
                  && !(var3.distanceToSqr(var17.getLocation()) > var4 * var4)
                  && (var17.isInside() || !(var17.getLocation().distanceToSqr(var15) > сЮ弟))) {
                  return new BlockPlacementHit(var1.immutable(), var2, var17.getLocation(), var17.isInside());
               }
            }
         }
      }

      return null;
   }
}
