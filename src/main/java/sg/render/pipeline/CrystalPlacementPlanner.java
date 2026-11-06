package sg.render.pipeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;
import sg.ec.MinecraftAccess;
import sg.ec.PlayerMotionPredictor;
import sg.ec.CrystalPlacementRanking;
import sg.modules.impl.init.BlockPlacementHit;
import sg.modules.impl.init.CrystalPlacementCandidate;
import sg.modules.impl.init.CrystalDamageTarget;
import sg.modules.impl.init.CrystalAttackCandidate;
import sg.modules.impl.init.BlockPlacementUtils;
import sg.util.entity.CombatCalculations;

public final class CrystalPlacementPlanner implements MinecraftAccess {
   private static final int АХ = 150;
   private static final int Ае = 3;
   private static final float АЙ = 240.0F;
   private static final float Ак = 480.0F;
   public static boolean ЯЫ;
   public static String ВЛ = "";
   private static final float ХН7 = 240.0F;
   private static final double ХНР = 0.02;
   private static final double ХН3 = 0.02;
   private static final double ХНр = 0.02;
   private static final double ХН6 = 0.02;
   private static final double ХН_ = 0.02;
   private static final double ХНШ = 0.02;
   private static final float ХНы = -Float.MAX_VALUE;
   private static final double ХНй = 1.0E-4;
   private static final String ХНЖ = "none";
   private static final double ХНм = 2.0;
   private static final float ХНК = 480.0F;
   private static final float ХНI = -1.0F;
   private static final double ХНТ = 0.5;
   private static final double ХНО = 0.5;

   public static boolean method_4_bbeebd87(BlockPos var0, Vec3 var1, float var2) {
      return MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.level.getBlockState(var0).canBeReplaced() && !method_7(var0) && method_4_85745e18(var0, var1, var2) != null;
   }

   @Nullable
   public static CrystalAttackCandidate method_4(List<Player> var0, Vec3 var1, float var2, float var3, float var4, float var5, List<Player> var6) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && !var0.isEmpty()) {
         CrystalAttackCandidate var7 = null;
         float var8 = ХНы;

         for (Entity var10 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            if (var10 instanceof EndCrystal var11 && method_4(var11, var1, var2) != null) {
               float var12 = Й(var11.position(), var0);
               if (!(var12 < var4)) {
                  float var13 = CombatCalculations.calculateExplosionDamage(var11.position(), MINECRAFT_CLIENT.player);
                  if (!(var13 >= var5) && !(var13 > var3)) {
                     float var14 = method_4(var11.position(), var6, null, List.of(), var3);
                     if (!(var14 < 0.0F)) {
                        float var15 = var12 - var13 * 2.0F - var14 * 2.0F;
                        if (var15 > var8) {
                           var8 = var15;
                           var7 = new CrystalAttackCandidate(var11, var12, var13);
                        }
                     }
                  }
               }
            }
         }

         return var7;
      } else {
         return null;
      }
   }

   @Nullable
   public static CrystalPlacementCandidate method_4(
      List<CrystalDamageTarget> var0,
      Vec3 var1,
      float var2,
      float var3,
      float var4,
      float var5,
      boolean var6,
      boolean var7,
      float var8,
      List<Player> var9,
      @Nullable BlockPos var10,
      float var11,
      @Nullable BlockPos var12,
      Collection<BlockPos> var13
   ) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && !var0.isEmpty()) {
         Collection var14 = var13 == null ? List.of() : var13;
         HashMap var15 = new HashMap();
         CombatCalculations.beginCachedCalculation();

         try {
            for (CrystalDamageTarget var17 : var0) {
               BlockPos var18 = BlockPos.containing(var17.center());

               for (int var19 = -3; var19 <= 3; var19++) {
                  for (int var20 = -3; var20 <= 3; var20++) {
                     for (int var21 = -4; var21 <= 1; var21++) {
                        BlockPos var22 = var18.offset(var19, var21, var20);
                        if (!var15.containsKey(var22) && !var14.contains(var22)) {
                           CrystalPlacementPlanner.PlacementCandidate var23 = method_4(var22, var18, var1, var2, var3, var6, var7, var10, var11, var12, var14);
                           if (var23 != null) {
                              var15.put(var22, var23);
                           }
                        }
                     }
                  }
               }
            }

            ArrayList<CrystalPlacementPlanner.PlacementCandidate> var32 = new ArrayList<>(var15.values());
            var32.sort(
               Comparator.<CrystalPlacementPlanner.PlacementCandidate, CrystalPlacementRanking.PlacementCost>comparing(var0x -> var0x.cost())
                  .thenComparingLong(var0x -> var0x.base().asLong())
            );
            CrystalPlacementCandidate var33 = null;
            CrystalPlacementRanking.PlacementCost var34 = null;
            int var35 = 0;

            for (CrystalPlacementPlanner.PlacementCandidate var37 : var32) {
               if (var35++ >= 150 || var34 != null && var34.blocks() == 0 && !var37.digs().isEmpty()) {
                  break;
               }

               Vec3 var38 = method_4(var37.base());
               AABB var39 = var37.obsidian() ? new AABB(var37.base()).deflate(ХНй) : null;
               float var24 = 0.0F;

               for (CrystalDamageTarget var26 : var0) {
                  float var27 = CombatCalculations.calculateExplosionDamage(var38, var26.player(), var26.box(), var39, var37.digs());
                  if (var26.box() != var26.player().getBoundingBox()) {
                     var27 = Math.min(var27, CombatCalculations.calculateExplosionDamage(var38, var26.player(), var26.player().getBoundingBox(), var39, var37.digs()));
                  }

                  var24 = Math.max(var24, var27);
               }

               if (!(var24 < var5)) {
                  float var40 = CombatCalculations.calculateExplosionDamage(var38, MINECRAFT_CLIENT.player, MINECRAFT_CLIENT.player.getBoundingBox(), var39, var37.digs());
                  if (!(var40 >= var8) && !(var40 > var4)) {
                     float var41 = method_4(var38, var9, var39, var37.digs(), var4);
                     if (!(var41 < 0.0F)) {
                        float var42 = var24 - var40 * 2.0F - var41 * 2.0F - (var37.obsidian() ? 3 : 0);
                        CrystalPlacementRanking.PlacementCost var28 = new CrystalPlacementRanking.PlacementCost(
                           var37.digs().size(), var37.cost().ticks(), var37.cost().current(), var37.cost().preferred(), var42
                        );
                        if (var34 == null || var28.compareTo(var34) < 0) {
                           var34 = var28;
                           var33 = new CrystalPlacementCandidate(
                              var37.base(), var38, var37.obsidian(), var37.digs().isEmpty() ? null : var37.digs().getFirst(), var24, var40
                           );
                        }
                     }
                  }
               }
            }

            ВЛ = "candidates="
               + var32.size()
               + " checked="
               + Math.min(var35, 150)
               + " best="
               + (var33 == null ? ХНЖ : var33.pos().toShortString() + " dig=" + var33.digPos() + " ticks=" + var34.ticks());
            return var33;
         } finally {
            CombatCalculations.endCachedCalculation();
         }
      } else {
         return null;
      }
   }

   private static float method_4(Vec3 var0, List<Player> var1, AABB var2, List<BlockPos> var3, float var4) {
      float var5 = 0.0F;
      if (var1 == null) {
         return var5;
      }

      for (Player var7 : var1) {
         if (var7 != MINECRAFT_CLIENT.player && var7.isAlive()) {
            float var8 = CombatCalculations.calculateExplosionDamage(var0, var7, var7.getBoundingBox(), var2, var3);
            if (var8 > var4 || var8 > 0.0F && var8 >= var7.getHealth() + var7.getAbsorptionAmount() - 1.0F) {
               return ХНI;
            }

            var5 = Math.max(var5, var8);
         }
      }

      return var5;
   }

   private CrystalPlacementPlanner() {
   }

   private static boolean method_4(BlockPos var0, List<BlockPos> var1) {
      for (Direction var5 : Direction.values()) {
         BlockPos var6 = var0.relative(var5);
         BlockState var7 = MINECRAFT_CLIENT.level.getBlockState(var6);
         if (!var1.contains(var6) && !var7.canBeReplaced() && !var7.is(Blocks.COBWEB) && !var7.getShape(MINECRAFT_CLIENT.level, var6).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   public static boolean е(BlockPos var0) {
      return MINECRAFT_CLIENT.level != null && (MINECRAFT_CLIENT.level.getBlockState(var0).is(Blocks.OBSIDIAN) || MINECRAFT_CLIENT.level.getBlockState(var0).is(Blocks.BEDROCK));
   }

   private static CrystalPlacementPlanner.PlacementCandidate method_4(
      BlockPos var0,
      BlockPos var1,
      Vec3 var2,
      float var3,
      float var4,
      boolean var5,
      boolean var6,
      BlockPos var7,
      float var8,
      BlockPos var9,
      Collection<BlockPos> var10
   ) {
      BlockPos var11 = var0.above();
      if (method_4(var2, var0) > var3 * var3) {
         return null;
      }

      Vec3 var12 = method_4(var0);
      AABB var13 = new AABB(var12.x - 1.0, var12.y, var12.z - 1.0, var12.x + 1.0, var12.y + ХНм, var12.z + 1.0);
      if (method_4(var2, var13) > var4 * var4) {
         return null;
      }

      BlockState var14 = MINECRAFT_CLIENT.level.getBlockState(var0);
      BlockState var15 = MINECRAFT_CLIENT.level.getBlockState(var11);
      ArrayList<BlockPos> var16 = new ArrayList<>(2);
      if (!var15.isAir()) {
         if (!var6 || !method_4(var15, var11)) {
            return null;
         }

         var16.add(var11);
      }

      boolean var17 = !е(var0);
      if (var17) {
         if (!var5) {
            return null;
         }

         if (!var14.canBeReplaced()) {
            if (!var6 || !method_4(var14, var0)) {
               return null;
            }

            var16.add(var0);
         }
      }

      if (!O(var11) && (!var17 || method_4(var0, var16))) {
         float var18 = 0.0F;

         for (BlockPos var20 : var16) {
            if (var10.contains(var20) || var20.getY() > var1.getY() + 1 || var20.getY() < var1.getY() - 4 || method_4(var2, var20) > var3 * var3) {
               return null;
            }

            float var21 = CombatCalculations.estimateBlockBreakTicks(MINECRAFT_CLIENT.level.getBlockState(var20), var20);
            if (var20.equals(var7)) {
               var21 *= 1.0F - Mth.clamp(var8, 0.0F, 1.0F);
            }

            var18 += var21;
         }

         if (var18 > ХНК) {
            return null;
         }

         if (var16.isEmpty()) {
            if (var17 ? !method_4_bbeebd87(var0, var2, var3) : BlockPlacementUtils.method_5(var0, var2, var3) == null) {
               return null;
            }
         } else if (BlockPlacementUtils.Г((BlockPos)var16.getFirst(), var2, var3) == null) {
            if (var16.size() != 2 || BlockPlacementUtils.Г((BlockPos)var16.getLast(), var2, var3) == null) {
               return null;
            }

            BlockPos var22 = (BlockPos)var16.removeLast();
            var16.addFirst(var22);
         }

         return new CrystalPlacementPlanner.PlacementCandidate(
            var0.immutable(),
            var17,
            List.copyOf(var16),
            new CrystalPlacementRanking.PlacementCost(var16.size(), var18, var16.contains(var7), var0.equals(var9), -((float)var2.distanceToSqr(var12)))
         );
      } else {
         return null;
      }
   }

   public static boolean Е(BlockState var0) {
      return var0 != null
         && MINECRAFT_CLIENT.level != null
         && !var0.isAir()
         && !(var0.getBlock() instanceof LiquidBlock)
         && var0.getDestroySpeed(MINECRAFT_CLIENT.level, BlockPos.ZERO) >= 0.0F;
   }

   private static Vec3 method_4(BlockPos var0) {
      return new Vec3(var0.getX() + ХНТ, var0.getY() + 1, var0.getZ() + ХНО);
   }

   public static boolean п(BlockState var0) {
      return method_4(var0, BlockPos.ZERO);
   }

   private static boolean method_4(BlockState var0, BlockPos var1) {
      if (Е(var0) && MINECRAFT_CLIENT.player != null && !var0.is(Blocks.COBWEB) && !var0.is(Blocks.SWEET_BERRY_BUSH)) {
         float var2 = CombatCalculations.estimateBlockBreakTicks(var0, var1);
         boolean var3 = var0.is(Blocks.OBSIDIAN) || var0.is(Blocks.CRYING_OBSIDIAN);
         boolean var4 = !MINECRAFT_CLIENT.player.onGround();
         return var2 <= ХН7 && CrystalPlacementRanking.М(var3, var4, var3 && var4 && PlayerMotionPredictor.method_0(MINECRAFT_CLIENT.player), var2);
      } else {
         return false;
      }
   }

   public static double method_4(Vec3 var0, AABB var1) {
      return var0.distanceToSqr(Mth.clamp(var0.x, var1.minX, var1.maxX), Mth.clamp(var0.y, var1.minY, var1.maxY), Mth.clamp(var0.z, var1.minZ, var1.maxZ));
   }

   public static double method_4(Vec3 var0, BlockPos var1) {
      return method_4(var0, new AABB(var1));
   }

   private static boolean O(BlockPos var0) {
      return !MINECRAFT_CLIENT.level
         .getEntities(
            (Entity)null,
            new AABB(var0.getX(), var0.getY(), var0.getZ(), var0.getX() + 1, var0.getY() + 2, var0.getZ() + 1),
            var0x -> var0x.isAlive() && !var0x.isSpectator()
         )
         .isEmpty();
   }

   public static boolean method_7(BlockPos var0) {
      return MINECRAFT_CLIENT.player == null
         || MINECRAFT_CLIENT.level == null
         || MINECRAFT_CLIENT.player.getBoundingBox().intersects(new AABB(var0))
         || !MINECRAFT_CLIENT.level.isUnobstructed(Blocks.OBSIDIAN.defaultBlockState(), var0, CollisionContext.empty());
   }

   @Nullable
   public static Vec3 method_4(EndCrystal var0, Vec3 var1, double var2) {
      if (var0 != null && var0.isAlive()) {
         AABB var4 = var0.getBoundingBox();
         Vec3 var5 = new Vec3(
            Mth.clamp(var1.x, var4.minX + ХНР, var4.maxX - ХН3),
            Mth.clamp(var1.y, var4.minY + ХНр, var4.maxY - ХН6),
            Mth.clamp(var1.z, var4.minZ + ХН_, var4.maxZ - ХНШ)
         );
         return method_4(var1, var4) <= var2 * var2 ? var5 : null;
      } else {
         return null;
      }
   }

   public static boolean method_4(EndCrystal var0, List<Player> var1, float var2, float var3, float var4, List<Player> var5) {
      if (var0 != null && var0.isAlive() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (Й(var0.position(), var1) < var3) {
            return false;
         }

         float var6 = CombatCalculations.calculateExplosionDamage(var0.position(), MINECRAFT_CLIENT.player);
         return var6 < var4 && var6 <= var2 && method_4(var0.position(), var5, null, List.of(), var2) >= 0.0F;
      } else {
         return false;
      }
   }

   private static float Й(Vec3 var0, List<Player> var1) {
      float var2 = 0.0F;

      for (Player var4 : var1) {
         if (var4.isAlive()) {
            var2 = Math.max(var2, CombatCalculations.calculateExplosionDamage(var0, var4));
         }
      }

      return var2;
   }

   public static boolean 西(BlockPos var0) {
      return е(var0) && MINECRAFT_CLIENT.level.getBlockState(var0.above()).isAir() && !O(var0.above());
   }

   @Nullable
   public static BlockPlacementHit method_4_85745e18(BlockPos var0, Vec3 var1, float var2) {
      return BlockPlacementUtils.method_5(var0, var1, var2, false);
   }

   private record PlacementCandidate(BlockPos base, boolean obsidian, List<BlockPos> digs, CrystalPlacementRanking.PlacementCost cost) {
   }
}
