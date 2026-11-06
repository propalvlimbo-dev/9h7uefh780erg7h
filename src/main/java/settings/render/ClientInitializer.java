package settings.render;

import net.fabricmc.api.ClientModInitializer;
import sg.client.RoseClient;

public class ClientInitializer implements ClientModInitializer {
   @Override
   public void onInitializeClient() {
      HmiIntegration.Тх();
      new RoseClient();
   }
}
