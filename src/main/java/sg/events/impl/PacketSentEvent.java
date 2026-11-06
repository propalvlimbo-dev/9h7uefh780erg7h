package sg.events.impl;

import net.minecraft.network.protocol.Packet;
import sg.events.ClientEvent;

public record PacketSentEvent(Packet<?> packet) implements ClientEvent {
}
