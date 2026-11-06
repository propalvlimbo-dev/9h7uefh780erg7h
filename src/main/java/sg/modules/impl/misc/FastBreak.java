package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import sg.events.EventListener;
import sg.mixin.MultiPlayerGameModeAccessor;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.BlockBreakEvent;
import sg.settings.FloatSetting;

public class FastBreak extends ClientModule {
   @SerializedName("А")
   private final FloatSetting progressThresholdSetting;
   private final @com.google.gson.annotations.SerializedName("诶")  EventListener<BlockBreakEvent> blockBreakListener;
   private static final String MODULE_NAME = "FastBreak";
   private static final String MODULE_DESCRIPTION = "Позволяет быстро ломать блоки";
   private static final String SPEED_LABEL = "Скорость";
   private static final float DEFAULT_PROGRESS_THRESHOLD = 0.8F;
   private static final float MIN_PROGRESS_THRESHOLD = 0.1F;
   private static final float PROGRESS_THRESHOLD_STEP = 0.1F;

   public void И(BlockBreakEvent var1) {
      if (MINECRAFT_CLIENT.gameMode != null) {
         MultiPlayerGameModeAccessor var2 = (MultiPlayerGameModeAccessor)MINECRAFT_CLIENT.gameMode;
         var2.setBlockBreakingCooldown(0);
         if (var2.getCurrentBreakingProgress() > this.progressThresholdSetting.getValue()) {
            var2.setCurrentBreakingProgress(1.0F);
         }
      }
   }

   public FastBreak() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
      this.progressThresholdSetting = new FloatSetting(SPEED_LABEL, DEFAULT_PROGRESS_THRESHOLD, MIN_PROGRESS_THRESHOLD, 1.0F).withStep(PROGRESS_THRESHOLD_STEP);
      this.blockBreakListener = this::И;
      this.addSettings(this.progressThresholdSetting);
   }
}
