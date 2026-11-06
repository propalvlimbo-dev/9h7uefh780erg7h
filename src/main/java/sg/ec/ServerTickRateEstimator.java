package sg.ec;

import lombok.Generated;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.util.Mth;
import sg.events.EventListener;
import sg.events.impl.PacketReceiveEvent;

public class ServerTickRateEstimator extends ClientService {
   private static float Аж = 0.0F;
   long Вщ;
   private static float А弟 = ServerTickRateEstimator.ВIи;
   private final EventListener<PacketReceiveEvent> Вв = this::в;
   private static final float ВI2 = 20.0F;
   private static final float ВIф = 1.0E9F;
   private static final float ВIи = 20.0F;

   @Generated
   public static float Л衣() {
      return А弟;
   }

   @Generated
   public static float ЛУ() {
      return Аж;
   }

   private void в(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundSetTimePacket) {
         this.уС();
      }
   }

   private void уС() {
      long var1 = System.nanoTime() - this.Вщ;
      float var3 = ВI2;
      float var4 = var3 * (ВIф / (float)var1);
      float var5 = Mth.clamp(var4, 0.0F, var3);
      А弟 = Math.round(var5 * 2.0F) / 2.0F;
      Аж = var5 - var3;
      this.Вщ = System.nanoTime();
   }
}
