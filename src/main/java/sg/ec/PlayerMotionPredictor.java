package sg.ec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class PlayerMotionPredictor implements MinecraftAccess {
   private static final Map<UUID, PlayerMotionPredictor.VelocitySample> Е = new HashMap<>();
   private static ClientLevel Л;
   private static final double Ях_ = 1.0E-5;
   private static final double ЯхШ = 1.0E-6;
   private static final double Яхы = 16.0;
   private static final double Яхй = 0.75;
   private static final double ЯхЖ = 0.25;
   private static final double Яхм = 0.75;
   private static final double ЯхК = 0.25;
   private static final double ЯхI = 16.0;
   private static final double ЯхТ = 1.0E-5;
   private static final double ЯхО = 1.0E-5;
   private static final double ЯхФ = 1.0E-5;
   private static final double ЯхЙ = 1.0E-5;
   private static final double Яхк = 1.0E-5;
   private static final double Яхг = 1.0E-5;

   public static boolean method_0(Player var0) {
      return var0 != null && й(var0.getBoundingBox());
   }

   public static List<CollisionMotionPredictor.MotionSample> е_2571477d(Player var0, int var1) {
      return MINECRAFT_CLIENT.level == null
         ? List.of(new CollisionMotionPredictor.MotionSample(0, var0.getBoundingBox(), var0.onGround()))
         : CollisionMotionPredictor.Ч(
            var0.getBoundingBox(),
            в(var0),
            var0.onGround(),
            var0.isNoGravity() ? 0.0 : var0.getGravity(),
            var1,
            (var1x, var2) -> Entity.collideBoundingBox(var0, var2, var1x, MINECRAFT_CLIENT.level, List.of()),
            PlayerMotionPredictor::й
         );
   }

   private PlayerMotionPredictor() {
   }

   private static Vec3 в(Player var0) {
      if (Л != MINECRAFT_CLIENT.level) {
         Е.clear();
         Л = MINECRAFT_CLIENT.level;
      }

      if (Е.size() > 128) {
         Е.clear();
      }

      int var1 = MINECRAFT_CLIENT.player == null ? var0.tickCount : MINECRAFT_CLIENT.player.tickCount;
      Vec3 var2 = var0.getPositionCodec().getBase();
      PlayerMotionPredictor.VelocitySample var3 = Е.get(var0.getUUID());
      if (var3 != null && var3.tick == var1) {
         return var3.velocity;
      }

      Vec3 var4 = new Vec3(var0.getX() - var0.xo, var0.getY() - var0.yo, var0.getZ() - var0.zo);
      Vec3 var5 = var0.getDeltaMovement();
      Vec3 var6 = var5.lengthSqr() > Ях_ ? var5 : var4;
      int var7 = var1;
      if (var3 != null && var1 > var3.tick && var1 - var3.tick <= 5) {
         if (var2.distanceToSqr(var3.server) > ЯхШ) {
            int var8 = Math.max(1, var1 - var3.serverTick);
            Vec3 var9 = var2.subtract(var3.server).scale(1.0 / var8);
            if (var9.lengthSqr() <= Яхы) {
               var6 = new Vec3(var9.x * Яхй + var3.velocity.x * ЯхЖ, var9.y, var9.z * Яхм + var3.velocity.z * ЯхК);
            }
         } else {
            var7 = var3.serverTick;
            if (var1 - var7 <= 2) {
               var6 = var3.velocity;
            }
         }
      }

      if (!Double.isFinite(var6.lengthSqr()) || var6.lengthSqr() > ЯхI) {
         var6 = Vec3.ZERO;
      }

      Е.put(var0.getUUID(), new PlayerMotionPredictor.VelocitySample(var1, var7, var2, var6));
      return var6;
   }

   public static boolean й(AABB var0) {
      if (MINECRAFT_CLIENT.level == null) {
         return false;
      }

      for (BlockPos var2 : BlockPos.betweenClosed(
         Mth.floor(var0.minX + ЯхТ),
         Mth.floor(var0.minY + ЯхО),
         Mth.floor(var0.minZ + ЯхФ),
         Mth.floor(var0.maxX - ЯхЙ),
         Mth.floor(var0.maxY - Яхк),
         Mth.floor(var0.maxZ - Яхг)
      )) {
         if (MINECRAFT_CLIENT.level.getBlockState(var2).is(Blocks.COBWEB)) {
            return true;
         }
      }

      return false;
   }

   public static AABB е_bb456db4(Player var0, int var1) {
      return var0 == null ? null : е_2571477d(var0, var1).getLast().box();
   }

   private record VelocitySample(int tick, int serverTick, Vec3 server, Vec3 velocity) {
   }
}
