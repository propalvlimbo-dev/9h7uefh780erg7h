package sg.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.events.impl.PlayerMovementEvent;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.ItemUseSlowdownEvent;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.client.RoseClient;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMovementEventsMixin extends AbstractClientPlayer {
   @Final
   @Shadow
   public ClientPacketListener connection;
   @Shadow
   public ClientInput input;
   @Unique
   private @com.google.gson.annotations.SerializedName("eventSync")  PlayerMovementSyncEvent movementSyncEvent;
   private static final String BLOCKS_PUSH_OPTION_NAME = "Блоки";

   public LocalPlayerMovementEventsMixin(ClientLevel level, GameProfile profile) {
      super(level, profile);
   }

   @Shadow
   protected abstract void updateAutoJump(float deltaX, float deltaZ);

   @Inject(method = "moveTowardsClosestSpace", at = @At("HEAD"), cancellable = true)
   public void pushOutOfBlocks(double x, double z, CallbackInfo callback) {
      if (RoseClient.getInstance().getServices().getModules().getNoPush().isEnabled() && RoseClient.getInstance().getServices().getModules().getNoPush().Ю.isSelected(BLOCKS_PUSH_OPTION_NAME)) {
         callback.cancel();
      }
   }

   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V"))
   public void tick(CallbackInfo callback) {
      RoseClient.getInstance().getEventBus().post(new PlayerTickEvent());
   }

   @Inject(method = "tick", at = @At("TAIL"))
   public void postTicks(CallbackInfo callback) {
      if (this.connection.hasClientLoaded()) {
         RoseClient.getInstance().getEventBus().post(new PlayerTickEvent.Post());
      }
   }

   @Inject(
      method = "move",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/AbstractClientPlayer;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
      ),
      cancellable = true
   )
   public void onMoveHook(MoverType moverType, Vec3 movement, CallbackInfo callback) {
      PlayerMovementEvent movementEvent = new PlayerMovementEvent(movement);
      RoseClient.getInstance().getEventBus().post(movementEvent);
      double previousX = this.getX();
      double previousZ = this.getZ();
      super.move(moverType, movementEvent.getMovement());
      this.updateAutoJump((float)(this.getX() - previousX), (float)(this.getZ() - previousZ));
      callback.cancel();
   }

   @ModifyExpressionValue(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;itemUseSpeedMultiplier()F"))
   private float modifyItemSlowdown(float slowdownMultiplier) {
      ItemUseSlowdownEvent slowdownEvent = new ItemUseSlowdownEvent();
      RoseClient.getInstance().getEventBus().post(slowdownEvent);
      return slowdownEvent.isCancelled() ? 1.0F : slowdownMultiplier;
   }

   @Inject(method = "sendPosition", at = @At("HEAD"), cancellable = true)
   public void sendMovementPackets(CallbackInfo callback) {
      LocalPlayer player = (LocalPlayer)(Object)this;
      this.movementSyncEvent = new PlayerMovementSyncEvent(player.position(), player.getYRot(), player.getXRot(), player.onGround());
      RoseClient.getInstance().getEventBus().post(this.movementSyncEvent);
      if (this.movementSyncEvent.isCancelled()) {
         callback.cancel();
      }
   }

   @Redirect(
      method = "sendPosition",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;position()Lnet/minecraft/world/phys/Vec3;")
   )
   private Vec3 redirectGetEntityPos(LocalPlayer player) {
      return this.movementSyncEvent != null ? this.movementSyncEvent.getPosition() : player.position();
   }

   @Inject(method = "sendPosition", at = @At("RETURN"))
   public void dispatchPostMovementSyncEvent(CallbackInfo callback) {
      if (this.movementSyncEvent != null && !this.movementSyncEvent.isCancelled()) {
         PlayerMovementSyncEvent.Post postSyncEvent = new PlayerMovementSyncEvent.Post(
            this.movementSyncEvent.getPosition(), this.movementSyncEvent.getYaw(), this.movementSyncEvent.getPitch(), this.movementSyncEvent.isOnGround()
         );
         RoseClient.getInstance().getEventBus().post(postSyncEvent);
      }
   }

   @Redirect(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"))
   public float getYaw(LocalPlayer player) {
      return this.movementSyncEvent != null ? this.movementSyncEvent.getYaw() : player.getYRot();
   }

   @Redirect(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"))
   public float getPitch(LocalPlayer player) {
      return this.movementSyncEvent != null ? this.movementSyncEvent.getPitch() : player.getXRot();
   }

   @Inject(method = "sendPosition", at = @At("HEAD"), cancellable = true)
   private void cancelMovementPacketsInFreeCam(CallbackInfo callback) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled()) {
         callback.cancel();
      }
   }

   @Inject(method = "sendIsSprintingIfNeeded", at = @At("HEAD"), cancellable = true)
   private void cancelSprintPacketsInFreeCam(CallbackInfo callback) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled()) {
         callback.cancel();
      }
   }

   @Inject(method = "applyInput", at = @At("HEAD"), cancellable = true)
   private void cancelMovementInputInFreeCam(CallbackInfo callback) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled()) {
         LocalPlayer player = (LocalPlayer)(Object)this;
         player.input.keyPresses = Input.EMPTY;
         player.xxa = 0.0F;
         player.zza = 0.0F;
         player.yya = 0.0F;
         player.setJumping(false);
         callback.cancel();
      }
   }
}
