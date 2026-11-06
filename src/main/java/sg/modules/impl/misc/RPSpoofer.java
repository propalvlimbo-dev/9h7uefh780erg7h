package sg.modules.impl.misc;

import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl.PackConfirmScreen;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket.Action;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public class RPSpoofer extends ClientModule {
   private final EventListener<PacketReceiveEvent> Х6 = this::В;
   private final EventListener<PlayerTickEvent> Х_ = this::method_9А;
   private static final String сТу = "RPSpoofer";
   private static final String сТ2 = "Сообщает серверу, что набор ресурсов был загружен";

   private void В(PacketReceiveEvent var1) {
      if (!(var1.getPacket() instanceof ClientboundResourcePackPushPacket)
         || RWFixer.З_d084f225() == null
         || MINECRAFT_CLIENT.getCurrentServer() == null
         || !RWFixer.Ю(MINECRAFT_CLIENT.getCurrentServer().ip)) {
         if (var1.getPacket() instanceof ClientboundResourcePackPushPacket) {
            MINECRAFT_CLIENT.getConnection().send(new ServerboundResourcePackPacket(MINECRAFT_CLIENT.player.getUUID(), Action.ACCEPTED));
            MINECRAFT_CLIENT.getConnection().send(new ServerboundResourcePackPacket(MINECRAFT_CLIENT.player.getUUID(), Action.SUCCESSFULLY_LOADED));
            var1.setCancelled(true);
         }
      }
   }

   public RPSpoofer() {
      super(сТу, сТ2, ModuleCategory.MISC);
   }

   private void method_9А(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() instanceof PackConfirmScreen) {
         MINECRAFT_CLIENT.gui.screen().onClose();
      }
   }
}
