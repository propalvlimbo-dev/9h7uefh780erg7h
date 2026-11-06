package sg.ec;

import sg.modules.impl.misc.AutoJoiner;
import sg.modules.impl.init.SupportedServer;
import sg.client.RoseClient;
import sg.config.ServerSelectionConfig;
import sg.events.EventListener;
import sg.events.impl.ClientTickEvent;

public final class ServerSelectionTracker extends ClientService {
   private static final int ЯР = 40;
   private int Я3;
   private final EventListener<ClientTickEvent> сН = this::ь;

   private void ь(ClientTickEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         if (++this.Я3 >= 40) {
            this.Я3 = 0;
            SupportedServer var2 = SupportedServer.method_8();
            if (var2 != null) {
               ServerSelectionConfig var3 = RoseClient.getInstance().getServices().getConfigManager().getServerSelectionConfig();
               if (var2 == SupportedServer.HOLY_WORLD) {
                  AutoJoiner.HolyWorldSelection var5 = AutoJoiner.method_9();
                  if (var5 != null) {
                     var3.setHolySelection(var2, var5);
                  }
               } else {
                  int var4 = AutoJoiner.method_9С();
                  if (var4 > 0) {
                     var3.setGriefSelection(var2, var4);
                  }
               }
            }
         }
      } else {
         this.Я3 = 0;
      }
   }
}
