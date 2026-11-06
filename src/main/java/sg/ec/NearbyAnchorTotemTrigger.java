package sg.ec;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import sg.modules.impl.init.TickTimer;

public class NearbyAnchorTotemTrigger extends TotemTrigger {
   @SerializedName("А")
   private final TickTimer А_at_6ce7f835 = new TickTimer();
   private boolean Вя = false;
   private static final String сOю = "Якорь";

   @Override
   public boolean ац() {
      if (!this.В.Ы().isSelected(сOю)) {
         return false;
      } else {
         BlockState var1 = NearbyBlockSearch.А(Blocks.RESPAWN_ANCHOR, Math.round(this.В.getAnchorDistanceSetting().getValue()));
         if (var1 != null) {
            this.А_at_6ce7f835.reset();
            this.Вя = true;
            return true;
         } else {
            return this.Вя && !this.А_at_6ce7f835.hasElapsed(this.В.弟().getValue().intValue());
         }
      }
   }
}
