package sg.ec;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.network.protocol.Packet;

public final class PacketEventBypass implements MinecraftAccess {
   private static final List<Packet<?>> сх = new ArrayList<>();
   private static final String Яуд = "This is a utility class and cannot be instantiated";

   @Generated
   private PacketEventBypass() {
      throw new UnsupportedOperationException(Яуд);
   }

   public static void А(Packet<?> var0) {
      сх.add(var0);
      MINECRAFT_CLIENT.getConnection().send(var0);
   }

   @Generated
   public static List<Packet<?>> хI() {
      return сх;
   }

   public static void method_4(Packet<?> var0) {
      MINECRAFT_CLIENT.getConnection().send(var0);
   }
}
