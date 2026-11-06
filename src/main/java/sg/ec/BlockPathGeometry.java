package sg.ec;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public final class BlockPathGeometry {
   private static final double Вт = 0.03;
   private static final Direction[] Я = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP};
   private static final double ХЧф = 1.0E-7;
   private static final double ХЧи = 0.03;
   private static final double ХЧ9 = 0.03;
   private static final double ХЧо = 1.0E-9;
   private static final double ХЧO = 1.0E-7;
   private static final double ХЧ7 = 0.2;
   private static final double ХЧР = 0.2;
   private static final double ХЧ3 = 0.8;
   private static final double ХЧр = 1.0E-5;
   private static final double ХЧ6 = 1.0E-5;
   private static final double ХЧ_ = 1.0E-5;
   private static final double ХЧШ = 1.0E-5;
   private static final double ХЧы = 1.0E-5;
   private static final double ХЧй = 1.0E-5;
   private static final double ХЧЖ = 1.0E-5;
   private static final double ХЧм = 1.0E-5;
   private static final double ХЧК = 1.0E-5;
   private static final double ХЧI = 1.0E-5;

   public static boolean ш(AABB var0, Predicate<BlockPos> var1) {
      double var2 = var0.minY + Math.min(ХЧ7, var0.getYsize() * ХЧР);
      double var4 = var0.minY + var0.getYsize() * ХЧ3;
      return ш(var0, var2, var1) && ш(var0, var4, var1);
   }

   public static BlockPathGeometry.OverlapInterval ш(List<AABB> var0, BlockPos var1, double var2, double var4) {
      double var6 = Math.max(0.0, var2);
      double var8 = Math.min(var4, var0.size() - 1);
      BlockPathGeometry.OverlapInterval var10 = null;

      for (int var11 = (int)Math.floor(var6); var11 < var8; var11++) {
         BlockPathGeometry.OverlapInterval var12 = ш(
            (AABB)var0.get(var11), (AABB)var0.get(var11 + 1), var1, Math.max(0.0, var6 - var11), Math.min(1.0, var8 - var11)
         );
         if (var12 == null) {
            if (var10 != null) {
               break;
            }
         } else {
            double var13 = var11 + var12.enter();
            double var15 = var11 + var12.exit();
            if (var10 != null && var13 > var10.exit() + ХЧф) {
               break;
            }

            var10 = new BlockPathGeometry.OverlapInterval(var10 == null ? var13 : var10.enter(), var15);
         }
      }

      return var10;
   }

   private static BlockPathGeometry.OverlapInterval ш(AABB var0, AABB var1, BlockPos var2, double var3, double var5) {
      double[] var7 = new double[]{var0.minX, var0.minY, var0.minZ};
      double[] var8 = new double[]{var0.maxX, var0.maxY, var0.maxZ};
      double[] var9 = new double[]{var1.minX - var0.minX, var1.minY - var0.minY, var1.minZ - var0.minZ};
      double[] var10 = new double[]{var2.getX(), var2.getY(), var2.getZ()};

      for (int var11 = 0; var11 < 3; var11++) {
         double var12 = var10[var11] + ХЧи - var8[var11];
         double var14 = var10[var11] + 1.0 - ХЧ9 - var7[var11];
         if (Math.abs(var9[var11]) < ХЧо) {
            if (var12 >= 0.0 || var14 <= 0.0) {
               return null;
            }
         } else {
            double var16 = var12 / var9[var11];
            double var18 = var14 / var9[var11];
            var3 = Math.max(var3, Math.min(var16, var18));
            var5 = Math.min(var5, Math.max(var16, var18));
         }

         if (var5 <= var3 + ХЧO) {
            return null;
         }
      }

      return new BlockPathGeometry.OverlapInterval(var3, var5);
   }

   private BlockPathGeometry() {
   }

   public static List<BlockPos> ш(List<AABB> var0, double var1, double var3) {
      LinkedHashSet var5 = new LinkedHashSet();
      double var6 = Math.max(0.0, var1);
      double var8 = Math.min(var3, var0.size() - 1);

      for (int var10 = (int)Math.floor(var6); var10 < var8; var10++) {
         AABB var11 = (AABB)var0.get(var10);
         AABB var12 = (AABB)var0.get(var10 + 1);
         BlockPos var13 = BlockPos.containing(Math.min(var11.minX, var12.minX), Math.min(var11.minY, var12.minY), Math.min(var11.minZ, var12.minZ));
         BlockPos var14 = BlockPos.containing(Math.max(var11.maxX, var12.maxX), Math.max(var11.maxY, var12.maxY), Math.max(var11.maxZ, var12.maxZ));

         for (BlockPos var16 : BlockPos.betweenClosed(var13, var14)) {
            if (!var5.contains(var16) && ш(var11, var12, var16, Math.max(0.0, var6 - var10), Math.min(1.0, var8 - var10)) != null) {
               var5.add(var16.immutable());
            }
         }
      }

      return new ArrayList<>(var5);
   }

   private static boolean ш(AABB var0, double var1, Predicate<BlockPos> var3) {
      for (BlockPos var5 : BlockPos.betweenClosed(
         BlockPos.containing(var0.minX + ХЧЖ, var1, var0.minZ + ХЧм), BlockPos.containing(var0.maxX - ХЧК, var1, var0.maxZ - ХЧI)
      )) {
         if (var3.test(var5)) {
            return true;
         }
      }

      return false;
   }

   public static List<BlockPos> ш(BlockPos var0, int var1, Predicate<BlockPos> var2, Predicate<BlockPos> var3) {
      if (!var2.test(var0)) {
         return List.of();
      }

      ArrayDeque var4 = new ArrayDeque();
      HashSet var5 = new HashSet();
      var4.add(List.of(var0.immutable()));
      var5.add(var0);
      int var6 = 0;

      while (!var4.isEmpty() && var6++ < 64) {
         List var7 = (List)var4.removeFirst();
         BlockPos var8 = (BlockPos)var7.getLast();
         if (var3.test(var8)) {
            return new ArrayList<>(var7.reversed());
         }

         if (var7.size() < var1) {
            for (Direction var12 : Я) {
               BlockPos var13 = var8.relative(var12);
               if (var5.add(var13) && var2.test(var13)) {
                  ArrayList var14 = new ArrayList(var7);
                  var14.add(var13);
                  var4.addLast(var14);
               }
            }
         }
      }

      return List.of();
   }

   public static boolean У(AABB var0, Predicate<BlockPos> var1) {
      for (BlockPos var3 : BlockPos.betweenClosed(
         BlockPos.containing(var0.minX + ХЧр, var0.minY + ХЧ6, var0.minZ + ХЧ_), BlockPos.containing(var0.maxX - ХЧШ, var0.maxY - ХЧы, var0.maxZ - ХЧй)
      )) {
         if (var1.test(var3)) {
            return true;
         }
      }

      return false;
   }

   public record OverlapInterval(double enter, double exit) {
   }
}
