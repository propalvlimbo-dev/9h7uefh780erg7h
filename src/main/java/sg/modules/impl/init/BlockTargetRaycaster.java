package sg.modules.impl.init;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import sg.ec.MinecraftAccess;

public final class BlockTargetRaycaster implements MinecraftAccess {
   private boolean Ям;
   private BlockPos Ю;
   private static final double Хlъ = 1.0E-8;
   private static final double ХlМ = 1.0E-8;
   private static final double ХlН = 1.0E-4;
   private static final double Хlн = 1.0E-4;
   private static final float ХlЦ = (float) (Math.PI / 180.0);
   private static final float Хlч = (float) Math.PI;
   private static final float ХlЭ = (float) (Math.PI / 180.0);

   private Vec3 А(Vec3 var1, Vec3 var2, BlockPos var3) {
      AABB var4 = new AABB(var3);
      double var5 = Double.POSITIVE_INFINITY;
      double var7 = ХlМ;
      if (var2.x > var7) {
         var5 = Math.min(var5, (var4.maxX - var1.x) / var2.x);
      } else if (var2.x < -var7) {
         var5 = Math.min(var5, (var4.minX - var1.x) / var2.x);
      }

      if (var2.y > var7) {
         var5 = Math.min(var5, (var4.maxY - var1.y) / var2.y);
      } else if (var2.y < -var7) {
         var5 = Math.min(var5, (var4.minY - var1.y) / var2.y);
      }

      if (var2.z > var7) {
         var5 = Math.min(var5, (var4.maxZ - var1.z) / var2.z);
      } else if (var2.z < -var7) {
         var5 = Math.min(var5, (var4.minZ - var1.z) / var2.z);
      }

      return Double.isFinite(var5) && !(var5 < 0.0) ? var1.add(var2.scale(var5 + Хlн)) : var1.add(var2.scale(ХlН));
   }

   public void Б(BlockPos var1) {
      this.Ям = var1 != null;
      this.Ю = var1 == null ? null : var1.immutable();
   }

   public BlockHitResult Е(double var1) {
      return this.Ям ? this.А(this.Ю, var1) : null;
   }

   public void дН() {
      this.Ям = false;
      this.Ю = null;
   }

   public BlockHitResult А(BlockPos var1, double var2) {
      return var1 != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !(var2 <= 0.0) ? this.Е(var1, var2, this.l(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot())) : null;
   }

   private Vec3 l(float var1, float var2) {
      float var3 = -var1 * ХlЦ - Хlч;
      float var4 = -var2 * ХlЭ;
      float var5 = Mth.cos(var3);
      float var6 = Mth.sin(var3);
      float var7 = -Mth.cos(var4);
      float var8 = Mth.sin(var4);
      return new Vec3(var6 * var7, var8, var5 * var7);
   }

   public BlockHitResult l(BlockPos var1, double var2) {
      return this.Е(var1, var1 == null ? null : Vec3.atCenterOf(var1), var2);
   }

   private BlockHitResult Е(BlockPos var1, double var2, Vec3 var4) {
      Vec3 var5 = MINECRAFT_CLIENT.player.getEyePosition();
      Vec3 var6 = var5.add(var4.scale(var2));
      Vec3 var7 = var5;
      double var8 = var2 * var2;

      for (int var10 = 0; var10 < 64; var10++) {
         BlockHitResult var11 = MINECRAFT_CLIENT.level.clip(new ClipContext(var7, var6, Block.OUTLINE, Fluid.NONE, MINECRAFT_CLIENT.player));
         if (var11.getType() == Type.MISS) {
            return null;
         }

         if (var1.equals(var11.getBlockPos())) {
            return var11;
         }

         Vec3 var12 = this.А(var11.getLocation(), var4, var11.getBlockPos());
         if (var12 == null || var5.distanceToSqr(var12) >= var8) {
            return null;
         }

         var7 = var12;
      }

      return null;
   }

   public BlockHitResult Е(BlockPos var1, Vec3 var2, double var3) {
      if (var1 != null && var2 != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !(var3 <= 0.0)) {
         Vec3 var5 = MINECRAFT_CLIENT.player.getEyePosition();
         Vec3 var6 = var2.subtract(var5);
         return var6.lengthSqr() <= Хlъ ? null : this.Е(var1, var3, var6.normalize());
      } else {
         return null;
      }
   }
}
