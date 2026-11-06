package sg.events.impl;

import lombok.Generated;
import net.minecraft.network.protocol.Packet;
import sg.events.CancellableEvent;
import sg.events.ClientEvent;

public class PacketSendEvent extends CancellableEvent implements ClientEvent {
   private final @com.google.gson.annotations.SerializedName("В")  Packet<?> packet;

   @Generated
   public Packet<?> getPacket() {
      return this.packet;
   }

   @Generated
   public PacketSendEvent(Packet<?> packet) {
      this.packet = packet;
   }
}
