package sg.modules.impl.init;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import sg.modules.impl.misc.NoInteract;
import sg.ec.MinecraftAccess;
import sg.ec.BlockFaceRaycast;
import sg.mixin.BlockStatePredictionHandlerAccessor;
import sg.render.pipeline.CrystalPlacementPlanner;
import sg.client.RoseClient;
import sg.events.impl.PlayerMovementSyncEvent;

public final class BlockPlacementUtils implements MinecraftAccess {
   private static final double ХВЙ = Double.MAX_VALUE;
   private static final double ХВк = Double.MAX_VALUE;

   public static BlockPlacementHit method_5(BlockPos var0, Vec3 var1, double var2) {
      if (!CrystalPlacementPlanner.е(var0)) {
         return null;
      }

      BlockPlacementHit var4 = method_5(var0, Direction.UP, var1, O(var2));
      return var4 != null ? var4 : Г(var0, var1, var2);
   }

   private static boolean method_5(BlockPlacementHit var0, BlockHitResult var1) {
      return var1 != null && var1.getType() == Type.BLOCK && var1.getBlockPos().equals(var0.neighbor()) && var1.getDirection() == var0.side();
   }

   public static boolean method_5(BlockPos var0, BlockHitResult var1, ItemStack var2) {
      if (var1 != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !var2.isEmpty()) {
         BlockPlaceContext var3 = new BlockPlaceContext(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND, var2, var1);
         return var3.getClickedPos().equals(var0) && var3.canPlace();
      } else {
         return false;
      }
   }

   public static BlockHitResult method_5(BlockPlacementHit var0, double var1, PlayerMovementSyncEvent.Post var3) {
      if (var0 != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !method_3(var0.neighbor())) {
         double var4 = O(var1);
         if (!(var4 <= 0.0) && !var3.isCancelled()) {
            Vec3 var6 = var3.getPosition().add(0.0, MINECRAFT_CLIENT.player.getEyeHeight(), 0.0);
            Vec3 var7 = Vec3.directionFromRotation(var3.getPitch(), var3.getYaw());
            BlockPos var8 = var0.neighbor();
            BlockHitResult var9 = MINECRAFT_CLIENT.level.getBlockState(var8).getShape(MINECRAFT_CLIENT.level, var8).clip(var6, var6.add(var7.scale(var4)), var8);
            return method_5(var0, var9) && var6.distanceToSqr(var9.getLocation()) <= var4 * var4 ? var9 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static double O(double var0) {
      return MINECRAFT_CLIENT.player == null ? 0.0 : Math.max(0.0, Math.min(var0, MINECRAFT_CLIENT.player.blockInteractionRange()));
   }

   public static int О西() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getConnection() != null) {
         PlayerInfo var0 = MINECRAFT_CLIENT.getConnection().getPlayerInfo(MINECRAFT_CLIENT.player.getUUID());
         int var1 = var0 == null ? 0 : Math.max(0, var0.getLatency());
         return Mth.clamp(12 + (var1 + 49) / 50 * 2, 12, 60);
      } else {
         return 20;
      }
   }

   public static BlockPlacementHit method_5(BlockPos var0, Vec3 var1, double var2, boolean var4) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level.getBlockState(var0).canBeReplaced()) {
         BlockPlacementHit var5 = null;
         double var6 = ХВЙ;
         ItemStack var8 = (var4 ? Items.COBWEB : Items.OBSIDIAN).getDefaultInstance();

         for (Direction var12 : Direction.values()) {
            BlockPos var13 = var0.relative(var12.getOpposite());
            BlockState var14 = MINECRAFT_CLIENT.level.getBlockState(var13);
            if (!var14.canBeReplaced() && !var14.isAir() && (var4 || !var14.is(Blocks.COBWEB)) && !Ы(var13)) {
               BlockPlacementHit var15 = method_5(var13, var12, var1, O(var2));
               if (var15 != null && method_5(var0, var15, var8) && var1.distanceToSqr(var15.hitVec()) < var6) {
                  var5 = var15;
                  var6 = var1.distanceToSqr(var15.hitVec());
               }
            }
         }

         return var5;
      } else {
         return null;
      }
   }

   public static BlockPlacementHit Г(BlockPos var0, Vec3 var1, double var2) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         BlockPlacementHit var4 = null;
         double var5 = ХВк;

         for (Direction var10 : Direction.values()) {
            BlockPlacementHit var11 = method_5(var0, var10, var1, O(var2));
            if (var11 != null && var1.distanceToSqr(var11.hitVec()) < var5) {
               var4 = var11;
               var5 = var1.distanceToSqr(var11.hitVec());
            }
         }

         return var4;
      } else {
         return null;
      }
   }

   private static BlockPlacementHit method_5(BlockPos var0, Direction var1, Vec3 var2, double var3) {
      return method_3(var0) ? null : BlockFaceRaycast.Ъ(MINECRAFT_CLIENT.level.getBlockState(var0).getShape(MINECRAFT_CLIENT.level, var0), var0, var1, var2, var3);
   }

   public static boolean method_5(BlockPos var0, BlockPlacementHit var1, ItemStack var2) {
      return var1 != null && method_5(var0, new BlockHitResult(var1.hitVec(), var1.side(), var1.neighbor(), var1.inside()), var2);
   }

   public static boolean method_3(BlockPos var0) {
      return MINECRAFT_CLIENT.level != null && ((BlockStatePredictionHandlerAccessor)MINECRAFT_CLIENT.level.getBlockStatePredictionHandler()).rose$getPendingBlockStates().containsKey(var0.asLong());
   }

   private BlockPlacementUtils() {
   }

   private static boolean Ы(BlockPos var0) {
      if (MINECRAFT_CLIENT.player.isSecondaryUseActive()) {
         return false;
      }

      BlockState var1 = MINECRAFT_CLIENT.level.getBlockState(var0);
      Block var2 = var1.getBlock();
      NoInteract var3 = RoseClient.getInstance().getServices().getModules().getNoInteract();
      return var1.getMenuProvider(MINECRAFT_CLIENT.level, var0) != null
         || var2 instanceof DoorBlock
         || var2 instanceof TrapDoorBlock
         || var2 instanceof FenceGateBlock
         || var2 instanceof ButtonBlock
         || var2 instanceof BedBlock
         || var2 instanceof LeverBlock
         || var2 instanceof CakeBlock
         || var2 instanceof NoteBlock
         || var2 instanceof RespawnAnchorBlock
         || var1.is(Blocks.ENDER_CHEST)
         || var1.is(Blocks.JUKEBOX)
         || var3 != null && var3.isEnabled() && var2 instanceof FenceBlock;
   }
}
