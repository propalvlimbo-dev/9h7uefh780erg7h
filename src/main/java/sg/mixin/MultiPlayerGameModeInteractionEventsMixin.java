package sg.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.modules.impl.misc.NoInteract;
import sg.events.impl.EntityAttackEvent;
import sg.modules.ModuleRegistry;
import sg.client.RoseClient;
import sg.events.impl.BlockBreakEvent;
import sg.events.impl.BlockUseEvent;
import sg.events.impl.BlockDestroyDelayEvent;
import sg.events.impl.AttackEntityEvent;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeInteractionEventsMixin {
   @Shadow
   private int destroyDelay;

   @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
   private void onAttackBlock(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.level != null) {
         BlockBreakEvent var5 = new BlockBreakEvent(var4.level.getBlockState(var1), var1, BlockBreakEvent.Phase.START, var2);
         RoseClient.getInstance().getEventBus().post(var5);
         if (var5.isCancelled()) {
            var3.setReturnValue(false);
         }
      }
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   public void attackEntity(Player var1, Entity var2, CallbackInfo var3) {
      try {
         if (var1 != null && var2 != null) {
            EntityAttackEvent var4 = new EntityAttackEvent(var1, var2);
            RoseClient.getInstance().getEventBus().post(var4);
            if (var4.isCancelled()) {
               var3.cancel();
               return;
            }

            RoseClient.getInstance().getEventBus().post(new AttackEntityEvent(var1, var2, AttackEntityEvent.Phase.PRE));
         }
      } catch (Exception var5) {
      }
   }

   @Inject(method = "attack", at = @At("RETURN"))
   public void attackEntityPost(Player var1, Entity var2, CallbackInfo var3) {
      try {
         if (var1 != null && var2 != null) {
            RoseClient.getInstance().getEventBus().post(new AttackEntityEvent(var1, var2, AttackEntityEvent.Phase.POST));
         }
      } catch (Exception var5) {
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   public void interactBlock(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      BlockUseEvent var5 = new BlockUseEvent(var1, var2, var3);
      RoseClient.getInstance().getEventBus().post(var5);
      if (var5.isCancelled()) {
         var4.setReturnValue(InteractionResult.SUCCESS);
      }

      Block var6 = null;
      if (Minecraft.getInstance().level != null) {
         var6 = Minecraft.getInstance().level.getBlockState(var3.getBlockPos()).getBlock();
      }

      ModuleRegistry var7 = RoseClient.getInstance().getServices().getModules();
      if (var7.getAutoBrewerBuilder() == null || !var7.getAutoBrewerBuilder().isEnabled()) {
         NoInteract var8 = var7.getNoInteract();
         if (var8 != null
            && var8.isEnabled()
            && !var1.isShiftKeyDown()
            && (
               var6 == Blocks.CHEST
                  || var6 == Blocks.TRAPPED_CHEST
                  || var6 == Blocks.FURNACE
                  || var6 == Blocks.ANVIL
                  || var6 == Blocks.CRAFTING_TABLE
                  || var6 == Blocks.HOPPER
                  || var6 == Blocks.JUKEBOX
                  || var6 == Blocks.NOTE_BLOCK
                  || var6 == Blocks.ENDER_CHEST
                  || var6 == Blocks.DISPENSER
                  || var6 == Blocks.DROPPER
                  || var6 instanceof ShulkerBoxBlock
                  || var6 instanceof FenceBlock
                  || var6 instanceof FenceGateBlock
            )) {
            var4.setReturnValue(InteractionResult.PASS);
         }
      }
   }

   @Inject(method = "continueDestroyBlock", at = @At("HEAD"))
   private void updateBlockBreakingProgress(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      BlockDestroyDelayEvent var4 = new BlockDestroyDelayEvent(this.destroyDelay);
      RoseClient.getInstance().getEventBus().post(var4);
      if (var4.isClearDelayRequested()) {
         this.destroyDelay = 0;
      }
   }
}
