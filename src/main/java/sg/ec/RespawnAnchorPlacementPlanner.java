package sg.ec;

import java.util.List;
import lombok.Generated;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sg.util.entity.CombatCalculations;

public final class RespawnAnchorPlacementPlanner implements MinecraftAccess {
   public static final double сг = 10.0;
   private static final int Хъ = 2;
   private static final int ХМ = -2;
   private static final int ХН = 2;
   private static final float Яу7 = -Float.MAX_VALUE;
   private static final double ЯуР = 0.5;
   private static final double Яу3 = 0.5;
   private static final double Яур = 0.5;
   private static final double Яу6 = 10.0;
   private static final float Яу_ = 0.5F;
   private static final double ЯуШ = 10.0;
   private static final double Яуы = 10.0;
   private static final String Яуй = "This is a utility class and cannot be instantiated";

   @Generated
   private RespawnAnchorPlacementPlanner() {
      throw new UnsupportedOperationException(Яуй);
   }

   private static float 西(Vec3 var0, List<Player> var1) {
      float var2 = 0.0F;

      for (Player var4 : var1) {
         if (var4 != null && var4.isAlive()) {
            var2 = Math.max(var2, CombatCalculations.calculateExplosionDamage(var0, var4, var4.getBoundingBox(), null, List.of(), ЯуШ));
         }
      }

      return var2;
   }

   private static float м(Vec3 var0, List<Player> var1) {
      if (var1 != null && !var1.isEmpty()) {
         float var2 = 0.0F;

         for (Player var4 : var1) {
            if (var4 != null && var4 != MINECRAFT_CLIENT.player && var4.isAlive()) {
               var2 = Math.max(var2, CombatCalculations.calculateExplosionDamage(var0, var4, var4.getBoundingBox(), null, List.of(), Яуы));
            }
         }

         return var2;
      } else {
         return 0.0F;
      }
   }

   private static boolean Д(BlockPos var0) {
      if (MINECRAFT_CLIENT.level == null) {
         return true;
      }

      AABB var1 = new AABB(var0);
      return !MINECRAFT_CLIENT.level.getEntities((Entity)null, var1, var0x -> var0x.isAlive() && !var0x.isSpectator()).isEmpty();
   }

   public static RespawnAnchorPlacementPlanner.AnchorPlacement 西(
      List<Player> var0, Vec3 var1, float var2, float var3, float var4, float var5, boolean var6, float var7, List<Player> var8
   ) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && var0 != null && !var0.isEmpty()) {
         if (MINECRAFT_CLIENT.level.dimension().identifier().equals(Level.NETHER.identifier())) {
            return null;
         }

         double var9 = var2 * var2;
         double var11 = var3 * var3;
         AABB var13 = MINECRAFT_CLIENT.player.getBoundingBox();
         RespawnAnchorPlacementPlanner.AnchorPlacement var14 = null;
         float var15 = Яу7;

         for (Player var17 : var0) {
            BlockPos var18 = BlockPos.containing(var17.getX(), var17.getY(), var17.getZ());

            for (int var19 = -2; var19 <= 2; var19++) {
               for (int var20 = -2; var20 <= 2; var20++) {
                  if (var19 != 0 || var20 != 0) {
                     for (int var21 = -2; var21 <= 2; var21++) {
                        BlockPos var22 = var18.offset(var19, var21, var20);
                        BlockState var23 = MINECRAFT_CLIENT.level.getBlockState(var22);
                        boolean var24;
                        if (!var23.isAir() && !var23.canBeReplaced()) {
                           var24 = false;
                        } else {
                           if (!var6 || !var23.canBeReplaced()) {
                              continue;
                           }

                           var24 = true;
                        }

                        BlockPos var25 = var22.above();
                        if (MINECRAFT_CLIENT.level.getBlockState(var25).canBeReplaced()) {
                           Vec3 var26 = new Vec3(var25.getX() + ЯуР, var25.getY() + Яу3, var25.getZ() + Яур);
                           double var27 = var1.distanceToSqr(var26);
                           if (!(var27 > var9) && !(var27 > var11) && !Д(var25)) {
                              float var29 = 西(var26, var0);
                              if (!(var29 < var5)) {
                                 float var30 = CombatCalculations.calculateExplosionDamage(var26, MINECRAFT_CLIENT.player, var13, null, List.of(), Яу6);
                                 if (!(var30 >= var7) && (!(var30 > var4) || !(var30 >= var29))) {
                                    float var31 = м(var26, var8);
                                    if (!(var31 > var4) || !(var31 >= var29)) {
                                       float var32 = var29 - var30 * 2.0F - var31 * 2.0F - (float)Math.sqrt(var27) * Яу_;
                                       if (var32 > var15) {
                                          var15 = var32;
                                          var14 = new RespawnAnchorPlacementPlanner.AnchorPlacement(var22.immutable(), var25.immutable(), var24);
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return var14;
      } else {
         return null;
      }
   }

   public record AnchorPlacement(BlockPos base, BlockPos anchorPos, boolean needsObsidian) {
   }
}
