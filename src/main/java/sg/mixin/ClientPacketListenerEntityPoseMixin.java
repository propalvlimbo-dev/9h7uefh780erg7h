package sg.mixin;

import sg.mixinbridge.ResolvedEntityPoseAccess;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.events.impl.PlayerPositionCorrectionEvent;
import sg.client.RoseClient;
import sg.modules.impl.movement.NoPush;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerEntityPoseMixin {
   @Shadow
   private ClientLevel level;
   private static final String Выо = "Удочка";

   @Inject(method = "handleEntityEvent(Lnet/minecraft/network/protocol/game/ClientboundEntityEventPacket;)V", at = @At("HEAD"), cancellable = true)
   private void cancelFishingPull(ClientboundEntityEventPacket var1, CallbackInfo var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.level != null && var3.player != null) {
         NoPush var4 = RoseClient.getInstance().getServices().getModules().getNoPush();
         if (var4.isEnabled() && var4.Ю.isSelected(Выо)) {
            if (var1.getEventId() == 31) {
               Entity var5 = var1.getEntity(var3.level);
               if (var5 instanceof FishingHook) {
                  var2.cancel();
               }
            }
         }
      }
   }

   @Inject(method = "handleMoveEntity", at = @At("TAIL"))
   private void onEntity(ClientboundMoveEntityPacket var1, CallbackInfo var2) {
      Entity var3 = var1.getEntity(this.level);
      if (var3 != null) {
         InterpolationHandler var4 = var3.getInterpolation();
         float var5 = var1.hasRotation() ? var1.getYRot() : (var4 != null ? var4.yRot() : var3.getYRot());
         float var6 = var1.hasRotation() ? var1.getXRot() : (var4 != null ? var4.xRot() : var3.getXRot());
         ((ResolvedEntityPoseAccess)var3).setResolvedYaw(var5);
         ((ResolvedEntityPoseAccess)var3).setResolvedPitch(var6);
         if (var3 instanceof Player) {
            Vec3 var7 = var4 != null ? var4.position() : var3.position();
            ((ResolvedEntityPoseAccess)var3).setResolvedPos(var7);
         }
      }
   }

   @Inject(method = "handleEntityPositionSync", at = @At("TAIL"))
   private void onEntityPositionSync(ClientboundEntityPositionSyncPacket var1, CallbackInfo var2) {
      Entity var3 = this.level.getEntity(var1.id());
      if (var3 != null) {
         float var4 = var1.values().yRot();
         float var5 = var1.values().xRot();
         if (var3 instanceof Player) {
            Vec3 var6 = var1.values().position();
            ((ResolvedEntityPoseAccess)var3).setResolvedPos(var6);
            ((ResolvedEntityPoseAccess)var3).setResolvedYaw(var4);
            ((ResolvedEntityPoseAccess)var3).setResolvedPitch(var5);
         }
      }
   }

   @Inject(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getY()D"))
   private void onPlayerPositionLook(ClientboundPlayerPositionPacket var1, CallbackInfo var2) {
      PlayerPositionCorrectionEvent var3 = new PlayerPositionCorrectionEvent();
      RoseClient.getInstance().getEventBus().post(var3);
   }
}
