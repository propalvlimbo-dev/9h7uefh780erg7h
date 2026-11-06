package sg.ec;

import com.viaversion.viafabricplus.ViaFabricPlus;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import lombok.Generated;
import sg.client.RoseClient;

public final class ViaProtocolVersion {
   private static final String сОэ = "This is a utility class and cannot be instantiated";

   @Generated
   private ViaProtocolVersion() {
      throw new UnsupportedOperationException(сОэ);
   }

   public static ProtocolVersion Г() {
      return RoseClient.getInstance().getServices().getViaIntegration().Як() ? ViaFabricPlus.getImpl().getTargetVersion() : null;
   }
}
