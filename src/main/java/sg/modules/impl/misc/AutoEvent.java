package sg.modules.impl.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.WaypointManager;

public class AutoEvent extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("Вб")  EventListener<PacketReceiveEvent> packetReceiveListener = this::onPacketReceive;
   private static final String MODULE_NAME = "AutoEvent";
   private static final String MODULE_DESCRIPTION = "АвтоЭвент под ReallyWorld";
   private static final String AIRDROP_COORDINATE_CHECK = "АирДроп » Координаты: ";
   private static final String AIRDROP_COORDINATE_PREFIX = "АирДроп » Координаты: ";
   private static final String OPEN_BRACKET = "[";
   private static final String CLOSE_BRACKET = "]";
   private static final String COORDINATE_SEPARATOR = "; ";
   private static final String WAYPOINT_TO_REMOVE = "AirDrop";
   private static final String WAYPOINT_NAME = "AirDrop";

   public AutoEvent() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
   }

   private void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacket() instanceof ClientboundSystemChatPacket chatPacket) {
         String message = chatPacket.content().getString();
         if (message.contains(AIRDROP_COORDINATE_CHECK)) {
            String[] coordinates = message.substring(message.indexOf(AIRDROP_COORDINATE_PREFIX) + 23).replace(OPEN_BRACKET, "").replace(CLOSE_BRACKET, "").split(COORDINATE_SEPARATOR);
            int x = Integer.parseInt(coordinates[0]);
            int z = Integer.parseInt(coordinates[1]);
            WaypointManager.method__().З_bbeebd87(WAYPOINT_TO_REMOVE);
            WaypointManager.method__().method__(WAYPOINT_NAME, new BlockPos(x, 80, z));
         }
      }
   }
}
