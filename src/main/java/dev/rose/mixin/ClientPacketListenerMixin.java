package dev.rose.mixin;

import dev.rose.combat.CombatEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
   @Shadow
   private ClientLevel level;

   @Inject(method = "handleDamageEvent", at = @At("TAIL"))
   private void rose$damage(ClientboundDamageEventPacket packet, CallbackInfo ci) {
      if (this.level != null) {
         CombatEvents.onDamage(this.level.getEntity(packet.entityId()), packet.sourceCauseId());
      }
   }

   @Inject(method = "handleAnimate", at = @At("TAIL"))
   private void rose$animate(ClientboundAnimatePacket packet, CallbackInfo ci) {
      int action = packet.getAction();
      if (this.level != null && (action == 4 || action == 5)) {
         CombatEvents.onCrit(this.level.getEntity(packet.getId()));
      }
   }

   @Inject(method = "handleEntityEvent", at = @At("TAIL"))
   private void rose$entityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci) {
      if (this.level != null) {
         byte event = packet.getEventId();
         if (event == 3) {
            CombatEvents.onDeath(packet.getEntity(this.level));
         } else if (event == 35) {
            CombatEvents.onTotem(packet.getEntity(this.level));
         }
      }
   }
}
