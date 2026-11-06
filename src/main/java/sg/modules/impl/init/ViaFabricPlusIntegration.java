package sg.modules.impl.init;

import lombok.Generated;
import net.fabricmc.loader.api.FabricLoader;
import sg.ec.MultiplayerMenuIntegration;

public class ViaFabricPlusIntegration {
   private boolean Як = false;
   private static final String ВИы = "viafabricplus";
   private static final String ВИй = "Found ViaFabricPlus!";

   @Generated
   public boolean Як() {
      return this.Як;
   }

   public ViaFabricPlusIntegration() {
      if (FabricLoader.getInstance().getModContainer(ВИы).isPresent()) {
         System.out.println(ВИй);
         this.Як = true;
      }

      MultiplayerMenuIntegration.Б();
   }
}
