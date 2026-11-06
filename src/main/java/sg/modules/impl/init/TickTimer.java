package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import sg.client.RoseClient;

public class TickTimer {
   @SerializedName("4Ь")
   private int startTick = 0;

   public void reset() {
      this.startTick = RoseClient.getInstance().getControllers().getClientTicks().getTickCount();
   }

   public boolean hasElapsed(int ticks) {
      return RoseClient.getInstance().getControllers().getClientTicks().getTickCount() - this.startTick >= ticks;
   }
}
