package sg.mixin;

import sg.mixinbridge.RenderSuppressionQueries;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.modules.impl.movement.NoWebController;
import sg.ec.HiddenBlockPositions;
import sg.client.RoseClient;
import sg.modules.impl.misc.Optimization;
import sg.events.impl.RenderSuppressionQuery;

@Mixin(BlockStateBase.class)
public abstract class BlockStateBaseRenderCollisionMixin {
   @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
   private void onGetRenderType(CallbackInfoReturnable<RenderShape> var1) {
      BlockState var2 = (BlockState)(Object)this;
      if (var2.getBlock() instanceof VegetationBlock && (Optimization.method_7М() || RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.GRASS))) {
         var1.setReturnValue(RenderShape.INVISIBLE);
      }
   }

   @Inject(
      method = "getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
      at = @At("HEAD"),
      cancellable = true
   )
   private void onGetOutlineShape(BlockGetter var1, BlockPos var2, CollisionContext var3, CallbackInfoReturnable<VoxelShape> var4) {
      if (this.isCobwebBypass()) {
         var4.setReturnValue(Shapes.empty());
      }
   }

   @Inject(
      method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
      at = @At("HEAD"),
      cancellable = true
   )
   private void fullRwCollision(BlockGetter var1, BlockPos var2, CollisionContext var3, CallbackInfoReturnable<VoxelShape> var4) {
      if (HiddenBlockPositions.О(var2)) {
         var4.setReturnValue(Shapes.empty());
      }
   }

   @Inject(
      method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
      at = @At("HEAD"),
      cancellable = true
   )
   private void fullRwCollisionNoContext(BlockGetter var1, BlockPos var2, CallbackInfoReturnable<VoxelShape> var3) {
      if (HiddenBlockPositions.О(var2)) {
         var3.setReturnValue(Shapes.empty());
      }
   }

   @Inject(method = "getInteractionShape", at = @At("HEAD"), cancellable = true)
   private void onGetRaycastShape(BlockGetter var1, BlockPos var2, CallbackInfoReturnable<VoxelShape> var3) {
      if (this.isCobwebBypass()) {
         var3.setReturnValue(Shapes.empty());
      }
   }

   private boolean isCobwebBypass() {
      BlockState var1 = (BlockState)(Object)this;
      if (!(var1.getBlock() instanceof WebBlock)) {
         return false;
      }

      if (RoseClient.getInstance() == null) {
         return false;
      }

      NoWebController var2 = RoseClient.getInstance().getServices().getModules().getNoWebController();
      return var2 != null && var2.isEnabled();
   }
}
