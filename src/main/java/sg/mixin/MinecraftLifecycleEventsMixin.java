package sg.mixin;

import sg.mixinbridge.FixedRateTicker;
import sg.mixinbridge.HmiSwingStateAccess;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settings.render.WorldEffectsResources;
import settings.render.HmiIntegration;
import sg.events.impl.ItemUseStartEvent;
import sg.ec.PredicateBlockRaycast;
import sg.events.impl.Frame60Event;
import sg.events.impl.ClientTickEvent;
import sg.events.impl.RotationFrameEvent;
import sg.client.RoseClient;
import sg.modules.impl.misc.OpenWalls;
import sg.modules.impl.misc.PvpSafe;
import sg.events.impl.BlockBreakEvent;

@Mixin(Minecraft.class)
public class MinecraftLifecycleEventsMixin {
   @Unique
   private final FixedRateTicker frame$60 = new FixedRateTicker(false);
   @Unique
   private final FixedRateTicker frame$240 = new FixedRateTicker(false);
   @Shadow
   public @Nullable ClientLevel level;
   @Shadow
   private int rightClickDelay;

   @Inject(method = "updateTitle", at = @At("HEAD"), cancellable = true)
   private void rose$unhookWindowTitle(CallbackInfo var1) {
      if (true) {
         Minecraft.getInstance().getWindow().setTitle("Rose - 26.2");
         var1.cancel();
      }
   }

   @Inject(method = "tick", at = @At("HEAD"))
   private void tick(CallbackInfo var1) {
      RoseClient.getInstance().getEventBus().post(new ClientTickEvent());
   }

   @Inject(method = "runTick", at = @At("HEAD"))
   private void render(boolean var1, CallbackInfo var2) {
      if (Minecraft.getInstance().player != null) {
         this.frame$60.I(60, () -> RoseClient.getInstance().getEventBus().post(new Frame60Event()));
         this.frame$240.I(240, () -> RoseClient.getInstance().getEventBus().post(new RotationFrameEvent()));
      }
   }

   @WrapOperation(
      method = "startUseItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"
      )
   )
   private InteractionResult openWallsInteract(
      MultiPlayerGameMode var1, LocalPlayer var2, InteractionHand var3, BlockHitResult var4, Operation<InteractionResult> var5
   ) {
      OpenWalls var6 = RoseClient.getInstance().getServices().getModules().getOpenWalls();
      if (var6.isEnabled() && !var2.getItemInHand(var3).is(Items.EXPERIENCE_BOTTLE)) {
         BlockHitResult var7 = PredicateBlockRaycast.I(
            var2, var2.blockInteractionRange(), var1x -> var6.С().stream().anyMatch(var1xx -> var1xx.isInstance(var1x))
         );
         if (var7.getType() == Type.BLOCK) {
            InteractionResult var8 = (InteractionResult)var5.call(var1, var2, var3, var7);
            return InteractionResult.SUCCESS;
         } else {
            return (InteractionResult)var5.call(var1, var2, var3, var4);
         }
      } else {
         return (InteractionResult)var5.call(var1, var2, var3, var4);
      }
   }

   @Redirect(
      method = "continueAttack",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
      )
   )
   private boolean hookBlockDamage(MultiPlayerGameMode var1, BlockPos var2, Direction var3) {
      BlockState var4 = this.level.getBlockState(var2);
      BlockBreakEvent var5 = new BlockBreakEvent(var4, var2, BlockBreakEvent.Phase.START, var3);
      RoseClient.getInstance().getEventBus().post(var5);
      if (var5.isCancelled()) {
         return false;
      }

      boolean var6 = var1.continueDestroyBlock(var2, var3);
      RoseClient.getInstance().getEventBus().post(new BlockBreakEvent(var4, var2, BlockBreakEvent.Phase.STOP, var3));
      return var6;
   }

   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
   private boolean freecamambassador(KeyMapping var1, Operation<Boolean> var2) {
      boolean var3 = (Boolean)var2.call(var1);
      return var1 == ((Minecraft)(Object)this).options.keyTogglePerspective
            && RoseClient.getInstance() != null
            && RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled()
         ? false
         : var3;
   }

   @Inject(
      method = "startAttack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V")
   )
   private void hmiAttackSwing(CallbackInfoReturnable<Boolean> var1) {
      Minecraft var2 = (Minecraft)(Object)this;
      if (HmiIntegration.п弟() && var2.player instanceof HmiSwingStateAccess var3) {
         var3.hMI5_0$resetMainHandSwing(false);
      }
   }

   @Redirect(
      method = "startUseItem",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V")
   )
   private void hmiUseSwing(LocalPlayer var1, InteractionHand var2) {
      if (HmiIntegration.п弟() && var1 instanceof HmiSwingStateAccess var3) {
         if (var2 == InteractionHand.MAIN_HAND) {
            var3.hMI5_0$resetMainHandSwing(true);
         } else {
            var3.hMI5_0$resetOffHandSwing(true);
         }
      }

      var1.swing(var2);
   }

   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void onUseItem(CallbackInfo var1) {
      ItemUseStartEvent var2 = new ItemUseStartEvent();
      RoseClient.getInstance().getEventBus().post(var2);
      if (var2.isCancelled()) {
         this.rightClickDelay = 4;
         var1.cancel();
      }
   }

   @Inject(method = {"disconnectWithProgressScreen()V", "disconnectWithSavingScreen()V"}, at = @At("HEAD"), cancellable = true)
   private void blockPvpDisconnect(CallbackInfo var1) {
      if (PvpSafe.method_7()) {
         var1.cancel();
      }
   }

   @Inject(method = "disconnectWithProgressScreen(Z)V", at = @At("HEAD"), cancellable = true)
   private void blockPvpTransferDisconnect(boolean var1, CallbackInfo var2) {
      if (PvpSafe.method_7()) {
         var2.cancel();
      }
   }

   @Inject(method = {"disconnectWithProgressScreen()V", "disconnectWithSavingScreen()V", "disconnectWithProgressScreen(Z)V"}, at = @At("RETURN"))
   private void resetWorldRenderResources(CallbackInfo var1) {
      if (!var1.isCancelled()) {
         WorldEffectsResources.resetWorldEffects();
      }
   }
}
