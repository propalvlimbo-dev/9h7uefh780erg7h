package sg.modules.impl.init;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;

final class TeleportSurfacePlanner {
   static final int ВШ = 65;
   static final double сп = 1.0E-7;
   private static final int Вы = 1;
   private static final double сД = 0.25;
   private static final double сЪ = 0.29;
   private static final double ст = 1.0;
   private static final double са = 1.8;
   private static final double сВЦ = 0.5;
   private static final double сВч = 1.0E-7;
   private static final double сВЭ = 1.0E-7;
   private static final double сВ必 = 65.0;
   private static final double сВщ = 1.0E-7;
   private static final double сВГ = Double.MAX_VALUE;
   private static final double сВл = 0.25;
   private static final double сВэ = 0.29;
   private static final double сВю = 0.29;
   private static final double сВЩ = 0.29;
   private static final double сВу = 1.8;
   private static final double сВ2 = 0.29;
   private static final double сВф = 0.29;
   private static final double сВи = 0.29;
   private static final double сВ9 = 0.29;
   private static final double сВо = 1.8;
   private static final double сВO = 0.29;
   private static final double сВ7 = 65.0000001;

   private boolean Ю(ClientLevel var1, LocalPlayer var2, double var3, double var5, double var7) {
      AABB var9 = new AABB(var3 - сВэ, var5 + 1.0, var7 - сВю, var3 + сВЩ, var5 + сВу, var7 + сВ2);
      return var1.noCollision(var2, var9);
   }

   TeleportSurfacePlanner.Surface Ю_6a0a43d3(ClientLevel var1, int var2, int var3) {
      int var4 = Math.min(var1.getHeight(Types.WORLD_SURFACE, var2, var3) - 1, var1.getMaxY());
      boolean var5 = false;

      for (int var6 = var4; var6 >= var1.getMinY(); var6--) {
         BlockPos var7 = new BlockPos(var2, var6, var3);
         BlockState var8 = var1.getBlockState(var7);
         if (var8.getFluidState().is(FluidTags.WATER)) {
            var5 = true;
         }

         if (!var8.getCollisionShape(var1, var7).isEmpty()) {
            return new TeleportSurfacePlanner.Surface(var6, var5);
         }
      }

      return null;
   }

   private boolean Ю(double var1, double var3, double var5, double var7) {
      return Math.hypot(var5 - var1, var7 - var3) <= сВ7;
   }

   private boolean Ч(ClientLevel var1, LocalPlayer var2, double var3, double var5, double var7) {
      AABB var9 = new AABB(var3 - сВф, var5, var7 - сВи, var3 + сВ9, var5 + сВо, var7 + сВO);
      return var1.noCollision(var2, var9);
   }

   boolean Ю_bbeebd87(ClientLevel var1, int var2, int var3) {
      return var1.getChunkSource().getChunk(var2 >> 4, var3 >> 4, ChunkStatus.FULL, false) != null;
   }

   private TeleportSurfacePlanner.Step Ю(
      ClientLevel var1, LocalPlayer var2, double var3, double var5, double var7, double var9, double var11, double var13
   ) {
      int var15 = (int)Math.floor(var7);
      int var16 = (int)Math.floor(var9);
      double var17 = Math.hypot(var11 - var3, var13 - var5);
      TeleportSurfacePlanner.Step var19 = null;
      double var20 = сВГ;

      for (int var22 = -1; var22 <= 1; var22++) {
         for (int var23 = -1; var23 <= 1; var23++) {
            int var24 = var15 + var22;
            int var25 = var16 + var23;
            double var26 = this.Ю(var24);
            double var28 = this.Ю(var25);
            if (this.Ю(var3, var5, var26, var28)) {
               double var30 = Math.hypot(var11 - var26, var13 - var28);
               if (!(var30 > var17 - сВл) && this.Ю(var1, var26, var28)) {
                  TeleportSurfacePlanner.Surface var32 = this.Ю_6a0a43d3(var1, var24, var25);
                  if (var32 != null && this.Ю(var1, var2, var26, var32.y(), var28) && this.Ч(var1, var2, var26, var32.y() + 1.0, var28)) {
                     double var33 = var26 - var7;
                     double var35 = var28 - var9;
                     double var37 = var33 * var33 + var35 * var35;
                     if (var37 < var20) {
                        var20 = var37;
                        var19 = new TeleportSurfacePlanner.Step(var26, var28, var32);
                     }
                  }
               }
            }
         }
      }

      return var19;
   }

   TeleportSurfacePlanner.Step Ю(ClientLevel var1, LocalPlayer var2, double var3, double var5, int var7, int var8) {
      double var9 = this.Ю(var7);
      double var11 = this.Ю(var8);
      double var13 = var9 - var3;
      double var15 = var11 - var5;
      double var17 = Math.hypot(var13, var15);
      if (var17 <= сВЭ) {
         return null;
      }

      for (double var19 = Math.min(var17, сВ必); var19 > сВщ; var19 -= Math.min(1.0, var19)) {
         double var21 = var19 / var17;
         double var23 = var3 + var13 * var21;
         double var25 = var5 + var15 * var21;
         TeleportSurfacePlanner.Step var27 = this.Ю(var1, var2, var3, var5, var23, var25, var9, var11);
         if (var27 != null) {
            return var27;
         }
      }

      return null;
   }

   private boolean Ю(ClientLevel var1, double var2, double var4) {
      int var6 = (int)Math.floor(var2) >> 4;
      int var7 = (int)Math.floor(var4) >> 4;
      return var1.getChunkSource().getChunk(var6, var7, ChunkStatus.FULL, false) != null;
   }

   double Ю(int var1) {
      return var1 + сВЦ;
   }

   boolean Ю(double var1, double var3, int var5, int var6) {
      return Math.hypot(this.Ю(var5) - var1, this.Ю(var6) - var3) <= сВч;
   }

   record Step(double x, double z, TeleportSurfacePlanner.Surface surface) {
   }

   record Surface(int y, boolean underwater) {
   }
}
