package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;

public class AutoPotionSwap extends ClientModule {
   @SerializedName("5")
   private int field_5 = 0;
   private final EventListener<PlayerTickEvent> а = this::衣;
   private static final String соП = "AutoPotionSwap";

   public void 衣(PlayerTickEvent var1) {
   }

   private void Ж(int var1) {
      this.field_5 = var1;
   }

   public AutoPotionSwap() {
      super(соП, "", ModuleCategory.MISC);
   }
}
