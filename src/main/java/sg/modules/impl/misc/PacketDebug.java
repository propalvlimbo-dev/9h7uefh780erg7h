package sg.modules.impl.misc;

import net.minecraft.network.protocol.common.ServerboundPongPacket;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.events.impl.PacketSendEvent;
import sg.events.EventListener;

public class PacketDebug extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("ХВ")  EventListener<PacketSendEvent> packetSendListener = this::е;
   private static final String MODULE_NAME = "PacketDebug";
   private static final String MODULE_DESCRIPTION = "Выводит получаемые пакеты в консоль";

   public void е(PacketSendEvent var1) {
      if (!(var1.getPacket() instanceof ServerboundPongPacket)) {
         System.out.println(var1.getPacket());
      }
   }

   public PacketDebug() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
   }
}
